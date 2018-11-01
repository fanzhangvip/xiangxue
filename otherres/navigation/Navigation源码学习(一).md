# Jetpack之Navigation源码学习（一）

> 如果有人不相信代码是简单的，那是因为他们没有意识到人生有多复杂。 

本篇按照使用流程进行梳理代码结构

## Navigation

```kotlin
Navigation.findNavController(it).navigate(R.id.action_page)
```

一句代码实现导航

往往凝聚了多少智慧

## Navigation.findNavController()

```Java
public static NavController findNavController(View view) {
    NavController navController = findViewNavController(view);
    if (navController == null) {
        throw new IllegalStateException("View " + view + " does not have a NavController set");
    }
    return navController;
}
```

通过view就去查找就NavController

```Java
 private static NavController findViewNavController(View view) {
    while (view != null) {
        NavController controller = getViewNavController(view);
        if (controller != null) {
            return controller;
        }
        ViewParent parent = view.getParent();
        view = parent instanceof View ? (View) parent : null;
    }
    return null;
}
```

通过view递归循环查找NavController

```Java
private static NavController getViewNavController(View view) {
    //获取view的Tag,通过tag获取controller
    Object tag = view.getTag(R.id.nav_controller_view_tag);
    NavController controller = null;
    //tag为WeakReference
    if (tag instanceof WeakReference) {
        controller = ((WeakReference<NavController>) tag).get();
    } else if (tag instanceof NavController) {//tag为NavController
        controller = (NavController) tag;
    }
    return controller;
}
```

原来view的Tag就包含了controller

问题来了

* 哪个View的Tag
* 什么时候设置Tag

回头看看配置

```Java
  <fragment
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:id="@+id/nav_host_fragment"        		android:name="androidx.navigation.fragment.NavHostFragment"
      app:navGraph="@navigation/navigation"
      app:defaultNavHost="true" />
```

Q，为什么有NavHostFragment
A，作为一个容器，所有的导航操作都是NavHostFragment中进行

Q，navGraph是什么
A，导航图

Q，defalutNavHost代表什么
A，该HostFragment是否作为主导航fragment，通俗一点，为true点击back事件会自动处理回退事件

## NavHostFragment

看看主导航fragment里面作了哪些操作

```Java
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Context context = getContext();
    //该hostFragment获取的所有NavController均为该navController
    mNavController = new NavController(context);
    //在SimpleNavigatorProvider中以键值对保存
    mNavController.getNavigatorProvider().addNavigator(createFragmentNavigator());

    Bundle navState = null;
    if (savedInstanceState != null) {
        //navController状态
        navState = savedInstanceState.getBundle(KEY_NAV_CONTROLLER_STATE);
        //恢复controller的状态
        if (savedInstanceState.getBoolean(KEY_DEFAULT_NAV_HOST, false)) {
            mDefaultNavHost = true;
            //若为主Host设置为主导航fragment
            getFragmentManager().beginTransaction().setPrimaryNavigationFragment(this).commit();
        }
    }

    if (navState != null) {
        // 恢复controller状态
        mNavController.restoreState(navState);
    } else {
        final Bundle args = getArguments();
        //将graph设置给navController
        final int graphId = args != null ? args.getInt(KEY_GRAPH_ID) : 0;
        if (graphId != 0) {
            mNavController.setGraph(graphId);
        } else {
            mNavController.setMetadataGraph();
        }
    }
}
```

重点

* NavController在这里创建，并且该HostFragment下仅有一个controller
* 解释了defaltNavHost为true，将会被设置为主导航fragment

```Java
public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    FrameLayout frameLayout = new FrameLayout(inflater.getContext());
    // 当通过XML添加时，这没有任何效果（因为此FrameLayout会自动获得ID），
    // 但是这确保了View在作为子fragment事务所需的编程方式添加NavHostFragment的情况下作为此Fragment的View层次结构的一部分存在
    frameLayout.setId(getId());
    //NavHostFragment就是一个frameLayout,一个容器
    return frameLayout;
}
```

重点

* 该NavHostFragment的视图就只有一个FrameLayout

```Java
public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (!(view instanceof ViewGroup)) {
        throw new IllegalStateException("created host view " + view + " is not a ViewGroup");
    }
    // 当通过XML添加时，父级是null，我们的view就是是NavHostFragment的根
    // 但是当以代码方式添加时，我们需要在父级上设置NavController，具有与此NavHostFragment匹配的ID的视图.
    View rootView = view.getParent() != null ? (View) view.getParent() : view;
    //在Navigation中findNavController就是在这里设置的
    Navigation.setViewNavController(rootView, mNavController);
}
```

```Java
public static void setViewNavController(View view, NavController controller) {
    view.setTag(R.id.nav_controller_view_tag, controller);
}
```

重点

* 这里解释了为什么我们从Navigation.findNavController()找到了Controller
* 也解释了这个view为FrameLayout或者FrameLayout的父级，即NavHostFragment的根视图

小结

* NavHostFragment作为容器进行导航操作，NavHostFragment可以作为主导航也可以嵌套使用
* 每个NavHostFragment只有一个Controller，NavHostFragment将导航操作交给了Controller

## NavController.navigate(R.id.action)

```Java
    /**
     * @param resId {@link NavDestination#getAction(int) action}的ActionId或者destinationId
     * @param args arguments参数
     * @param navOptions 导航操作的选项
     */
public void navigate(int resId, Bundle args, NavOptions navOptions) {
    //回退栈为null返回NavGraph
    //不为null返回回退栈中的最后一项
    NavDestination currentNode = mBackStack.isEmpty() ? mGraph : mBackStack.peekLast();
    if (currentNode == null) {
        throw new IllegalStateException("no current navigation node");
    }
    int destId = resId;
    //获取resId对应的NavAction
    final NavAction navAction = currentNode.getAction(resId);
    if (navAction != null) {
        if (navOptions == null) {
            navOptions = navAction.getNavOptions();
        }
        //通过NavAction获取目的地id
        destId = navAction.getDestinationId();
    }
    //若destId为0而navOptions又不为null则弹出到该navOptions的指定的页面
    if (destId == 0 && navOptions != null && navOptions.getPopUpTo() != 0) {
        popBackStack(navOptions.getPopUpTo(), navOptions.isPopUpToInclusive());
        return;
    }
    //为0报错
    if (destId == 0) {
        throw new IllegalArgumentException("Destination id == 0 can only be used" + " in conjunction with navOptions.popUpTo != 0");
    }

    //找到准备前往的目的地
    NavDestination node = findDestination(destId);
    if (node == null) {
        final String dest = NavDestination.getDisplayName(mContext, destId);
        throw new IllegalArgumentException("navigation destination " + dest
                + (navAction != null
                ? " referenced from action " + NavDestination.getDisplayName(mContext, resId)
                : "")
                + " is unknown to this NavController");
    }
    if (navOptions != null) {
        //是否清除回退栈
        if (navOptions.shouldClearTask()) {
            popBackStack(0, true);
            mBackStack.clear();
        } else if (navOptions.getPopUpTo() != 0) {
            //导航之前弹出栈到指定栈
            // 是否将该页面也弹出
            popBackStack(navOptions.getPopUpTo(), navOptions.isPopUpToInclusive());
        }
    }
    //进行导航
    node.navigate(args, navOptions);
}
```

这里只是做了导航前的准备工作

* 检查参数并获取目的地的destId
* 导航携带的参数Bundle
* 导航操作特殊的配置NavOptions

顺带解释一下NavDestination是graph上的每个节点

## NavDestination.navigate()

```Java
public void navigate(@Nullable Bundle args, @Nullable NavOptions navOptions) {
    Bundle defaultArgs = getDefaultArguments();
    Bundle finalArgs = new Bundle();
    //合并参数
    finalArgs.putAll(defaultArgs);
    if (args != null) {
        finalArgs.putAll(args);
    }
    mNavigator.navigate(this, finalArgs, navOptions);
}
```

这里只是合并了Bundle参数，最终将导航操作交给了mNavigator导航器，真正的导航操作交给了mNavigator

Navigator是一个抽象类，实现类ActivityNavigator，FragmentNavigator

```Java
public abstract void navigate(@NonNull D destination, @Nullable Bundle args,@Nullable NavOptions navOptions);
```
FragmentNavigator的实现

```Java
public void navigate(Destination destination,Bundle args,NavOptions navOptions) {
    //通过反射构建Fragment实例
    final Fragment frag = destination.createFragment(args);
    final FragmentTransaction ft = mFragmentManager.beginTransaction();
    //处理动画
    int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
    int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
    int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
    int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
    if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
        enterAnim = enterAnim != -1 ? enterAnim : 0;
        exitAnim = exitAnim != -1 ? exitAnim : 0;
        popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
        popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
        ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }
    //放入容器中
    ft.replace(mContainerId, frag);
    final StateFragment oldState = getState();
    if (oldState != null) {
        ft.remove(oldState);
    }
    final @IdRes int destId = destination.getId();
    final StateFragment newState = new StateFragment();
    newState.mCurrentDestId = destId;
    ft.add(newState, StateFragment.FRAGMENT_TAG);
    final boolean initialNavigation = mFragmentManager.getFragments().isEmpty();
    final boolean isClearTask = navOptions != null && navOptions.shouldClearTask();
    // TODO Build first class singleTop behavior for fragments
    final boolean isSingleTopReplacement = navOptions != null && oldState != null
            && navOptions.shouldLaunchSingleTop()
            && oldState.mCurrentDestId == destId;
    if (!initialNavigation && !isClearTask && !isSingleTopReplacement) {
        ft.addToBackStack(getBackStackName(destId));
    } else {
        ft.runOnCommit(new Runnable() {
            @Override
            public void run() {
                dispatchOnNavigatorNavigated(destId, isSingleTopReplacement
                        ? BACK_STACK_UNCHANGED
                        : BACK_STACK_DESTINATION_ADDED);
            }
        });
    }
    ft.commit();
    mFragmentManager.executePendingTransactions();
}
```
最终还是交由FragmentManager处理

ActivityNavigator的实现

```Java
public void navigate(Destination destination, Bundle args,NavOptions navOptions) {
    if (destination.getIntent() == null) {
        throw new IllegalStateException("Destination " + destination.getId()
                + " does not have an Intent set.");
    }
    //构造新的Intent
    Intent intent = new Intent(destination.getIntent());
    if (args != null) {
        intent.putExtras(args);
        //动态数据模式
        String dataPattern = destination.getDataPattern();
        if (!TextUtils.isEmpty(dataPattern)) {
            // 使用args填充数据模式以构建有效的URI
            StringBuffer data = new StringBuffer();
            Pattern fillInPattern = Pattern.compile("\\{(.+?)\\}");
            Matcher matcher = fillInPattern.matcher(dataPattern);
            while (matcher.find()) {
                String argName = matcher.group(1);
                if (args.containsKey(argName)) {
                    matcher.appendReplacement(data, "");
                    data.append(Uri.encode(args.getString(argName)));
                } else {
                    throw new IllegalArgumentException("Could not find " + argName + " in "
                            + args + " to fill data pattern " + dataPattern);
                }
            }
            matcher.appendTail(data);
            intent.setData(Uri.parse(data.toString()));
        }
    }
    if (navOptions != null && navOptions.shouldClearTask()) {
        //最终转换成了FLAG_ACTIVITY_CLEAR_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
    if (navOptions != null && navOptions.shouldLaunchDocument()
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
    } else if (!(mContext instanceof Activity)) {
        // 如果我们没有从Activity上下文启动，我们必须在新任务中启动。
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    if (navOptions != null && navOptions.shouldLaunchSingleTop()) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
    if (mHostActivity != null) {
        final Intent hostIntent = mHostActivity.getIntent();
        if (hostIntent != null) {
            final int hostCurrentId = hostIntent.getIntExtra(EXTRA_NAV_CURRENT, 0);
            if (hostCurrentId != 0) {
                intent.putExtra(EXTRA_NAV_SOURCE, hostCurrentId);
            }
        }
    }
    final int destId = destination.getId();
    //将该目的地设置当前
    intent.putExtra(EXTRA_NAV_CURRENT, destId);
    NavOptions.addPopAnimationsToIntent(intent, navOptions);
    //依然调用了startActivity
    mContext.startActivity(intent);
    if (navOptions != null && mHostActivity != null) {
        int enterAnim = navOptions.getEnterAnim();
        int exitAnim = navOptions.getExitAnim();
        if (enterAnim != -1 || exitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            //执行了activity的转场动画
            mHostActivity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    // 无法从新Activity的调用者弹出后台堆栈，因此不会将此导航器添加到控制器的后台堆栈中
    dispatchOnNavigatorNavigated(destId, BACK_STACK_UNCHANGED);
}
```

最终还是交由startActivity执行跳转

fragment的操作还是交由FragmentManager在操作，activity交由startActivity执行，Navigation为我们作了一层封装便于我们操作

Navigation源码学习远不止于此，这仅是一个流程，其中的设计，模式，为何要如此设计更是我们需要学习的

我学习Navigation的项目源码在这里(有更多注释):[学习源码](https://github.com/me94me/Mandroid/tree/master/PreferredLibrary/Navigation/example_navigatioin_resource)

