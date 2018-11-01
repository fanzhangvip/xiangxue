
package com.me94me.example_navigatioin_resource.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

/**
 * 管理应用导航
 *
 * 通常通过host直接获取controller,或者通过使用{@link Navigation}获取,而不是直接创建一个controller
 * 导航流和目的地通常有controller持有的{@link NavGraph}决定
 *
 * 这些NavGraph通常通过{@link #getNavInflater()}从资源文件中
 * 但是像Views一样,也可以通过构造方法或者组合方式构成或者用于动态导航结构
 * 例如,导航结构也可以通过远程服务器决定获取
 */
public class NavController {

    //两个Controller的状态,当NavHostFragment执行onSaveInstanceState时保存
    //GraphId
    private static final String KEY_GRAPH_ID = "android-support-nav:controller:graphId";
    //回退栈目的地Id
    private static final String KEY_BACK_STACK_IDS = "android-support-nav:controller:backStackIds";


    static final String KEY_DEEP_LINK_IDS = "android-support-nav:controller:deepLinkIds";
    static final String KEY_DEEP_LINK_EXTRAS = "android-support-nav:controller:deepLinkExtras";
    /**
     * 触发深度链接到目的地的Intent
     */
    public static final String KEY_DEEP_LINK_INTENT = "android-support-nav:controller:deepLinkIntent";

    private final Context mContext;
    private Activity mActivity;
    private NavInflater mInflater;
    private NavGraph mGraph;
    private int mGraphId;
    //保存的回退栈
    private int[] mBackStackToRestore;

    //回退栈,使用了双端队列
    private final Deque<NavDestination> mBackStack = new ArrayDeque<>();

    /**
     * 保存了所有的Navigator
     * 重写addNavigator移除之前navigator的监听器，添加现在navigator的监听器
     * 提供Navigator,在NavHostFragment的onCreate()中创建赋值
     */
    private final SimpleNavigatorProvider mNavigatorProvider = new SimpleNavigatorProvider() {
        @Override
        public Navigator<? extends NavDestination> addNavigator(String name,
                Navigator<? extends NavDestination> navigator) {
            //mNavigators.put(name, navigator)返回的hashMap返回的上一个与name关联的Navigator
            //之前的Navigator
            Navigator<? extends NavDestination> previousNavigator = super.addNavigator(name, navigator);
            //如果与当前navigator不一致
            if (previousNavigator != navigator) {
                //移除之前的监听器
                if (previousNavigator != null) {
                    previousNavigator.removeOnNavigatorNavigatedListener(mOnNavigatedListener);
                }
                //为现在的navigator添加的监听器
                navigator.addOnNavigatorNavigatedListener(mOnNavigatedListener);
            }
            //依然返回之前的navigator
            return previousNavigator;
        }
    };

    /**
     * OnNavigatorNavigatedListener导航监听器
     */
    private final Navigator.OnNavigatorNavigatedListener mOnNavigatedListener = new Navigator.OnNavigatorNavigatedListener() {
                @Override
                public void onNavigatorNavigated(Navigator navigator, @IdRes int destId, @Navigator.BackStackEffect int backStackEffect) {
                    if (destId != 0) {
                        NavDestination newDest = findDestination(destId);
                        if (newDest == null) {
                            throw new IllegalArgumentException("Navigator " + navigator
                                    + " reported navigation to unknown destination id "
                                    + NavDestination.getDisplayName(mContext, destId));
                        }
                        switch (backStackEffect) {
                            //如果是移除就移除最后一个
                            case Navigator.BACK_STACK_DESTINATION_POPPED:
                                while (!mBackStack.isEmpty() && mBackStack.peekLast().getId() != destId) {
                                    mBackStack.removeLast();
                                }
                                break;
                                //如果是添加就添加一个NavDestination进入后退栈
                            case Navigator.BACK_STACK_DESTINATION_ADDED:
                                mBackStack.add(newDest);
                                break;
                            case Navigator.BACK_STACK_UNCHANGED:
                                // Don't update the back stack and don't dispatchOnNavigated
                                return;
                        }
                        //分发导航事件
                        dispatchOnNavigated(newDest);
                    }
                }
            };
    /**
     * 导航事件监听器集合
     */
    private final CopyOnWriteArrayList<OnNavigatedListener> mOnNavigatedListeners = new CopyOnWriteArrayList<>();


    /**
     * 导航接口
     */
    public interface OnNavigatedListener {
        /**
         * 当导航完成了才调用，关联的transitions还是会执行
         * @param controller 用于导航的controller
         * @param destination 新的目的地
         */
        void onNavigated(NavController controller,NavDestination destination);
    }


    /**
     * 使用context构造一个Controller
     *
     * 控制器不应在context之外使用，并保留对context的强引用。
     * 如果你想一个全局controller，可以通过{@link Context#getApplicationContext()}
     *
     * app通常不新建建控制器，而是通过{@link NavHost＃getNavController（）}
     * 或使用{@link Navigation}类中的一种实用程序方法直接从navHost获取相关控制器。
     *
     *
     * @param context context for this controller
     */
    public NavController(@NonNull Context context) {
        mContext = context;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                mActivity = (Activity) context;
                break;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        mNavigatorProvider.addNavigator(new NavGraphNavigator(mContext));
        mNavigatorProvider.addNavigator(new ActivityNavigator(mContext));
    }

    @NonNull
    Context getContext() {
        return mContext;
    }

    /**
     * 获取NavController的{@link NavigatorProvider}
     * 用于构建此导航控制器的{@link NavGraph 导航图}的所有{@link Navigation Navigators}应在构建Graph之前添加到此NavigatorProvider
     *
     * 通常，导航器是由此NavController的{@link NavHost}设置的，无需手动与导航器提供程序进行交互
     * @return The {@link NavigatorProvider} used by this NavController.
     */
    @NonNull
    public NavigatorProvider getNavigatorProvider() {
        return mNavigatorProvider;
    }

    /**
     * 添加{@link OnNavigatedListener}
     * @param listener the listener to receive events
     */
    public void addOnNavigatedListener(@NonNull OnNavigatedListener listener) {
        //监听器会立即接收到当前的destination
        if (!mBackStack.isEmpty()) {
            listener.onNavigated(this, mBackStack.peekLast());
        }
        mOnNavigatedListeners.add(listener);
    }

    /**
     * 移除监听器
     * @param listener the listener to remove
     */
    public void removeOnNavigatedListener(@NonNull OnNavigatedListener listener) {
        mOnNavigatedListeners.remove(listener);
    }

    /**
     * 弹出回退栈
     * @return true if the stack was popped, false otherwise
     */
    public boolean popBackStack() {
        if (mBackStack.isEmpty()) {
            throw new IllegalArgumentException("NavController back stack is empty");
        }
        boolean popped = false;
        while (!mBackStack.isEmpty()) {
            popped = mBackStack.removeLast().getNavigator().popBackStack();
            if (popped) {
                break;
            }
        }
        return popped;
    }


    /**
     * 将控制器的后退堆栈弹回特定目标。
     *
     * @param destinationId 保留的最高目的地
     * @param inclusive 是否也应弹出给定目的地。
     *
     * @return 如果堆栈至少弹出一次，则为true，否则为false
     */
    public boolean popBackStack(@IdRes int destinationId, boolean inclusive) {
        if (mBackStack.isEmpty()) {
            throw new IllegalArgumentException("NavController back stack is empty");
        }
        ArrayList<NavDestination> destinationsToRemove = new ArrayList<>();
        Iterator<NavDestination> iterator = mBackStack.descendingIterator();
        while (iterator.hasNext()) {
            NavDestination destination = iterator.next();
            //循环回退栈若不等于destinationId就加入需要移除的集合
            //inclusive表示是否移除给定的destinationId
            if (inclusive || destination.getId() != destinationId) {
                destinationsToRemove.add(destination);
            }
            //等于的时候就停止加入
            if (destination.getId() == destinationId) {
                break;
            }
        }
        boolean popped = false;
        //循环需要移除的回退集合
        iterator = destinationsToRemove.iterator();
        while (iterator.hasNext()) {
            NavDestination destination = iterator.next();
            //跳过已经移除的
            // Skip destinations already removed by a previous popBackStack operation
            while (!mBackStack.isEmpty() && mBackStack.peekLast().getId() != destination.getId()) {
                if (iterator.hasNext()) {
                    destination = iterator.next();
                } else {
                    destination = null;
                    break;
                }
            }
            //移除destinationsToRemove所包含的destination
            if (destination != null) {
                popped = destination.getNavigator().popBackStack() || popped;
            }
        }
        return popped;
    }

    /**
     * Attempts to navigate up in the navigation hierarchy. Suitable for when the
     * user presses the "Up" button marked with a left (or start)-facing arrow in the upper left
     * (or starting) corner of the app UI.
     *
     * <p>The intended behavior of Up differs from {@link #popBackStack() Back} when the user
     * did not reach the current destination from the application's own task. e.g. if the user
     * is viewing a document or link in the current app in an activity hosted on another app's
     * task where the user clicked the link. In this case the current activity (determined by the
     * context used to create this NavController) will be {@link Activity#finish() finished} and
     * the user will be taken to an appropriate destination in this app on its own task.</p>
     *
     * @return true if navigation was successful, false otherwise
     */
    public boolean navigateUp() {
        if (mBackStack.size() == 1) {
            // If there's only one entry, then we've deep linked into a specific destination
            // on another task so we need to find the parent and start our task from there
            NavDestination currentDestination = getCurrentDestination();
            int destId = currentDestination.getId();
            NavGraph parent = currentDestination.getParent();
            while (parent != null) {
                if (parent.getStartDestination() != destId) {
                    TaskStackBuilder parentIntents = new NavDeepLinkBuilder(NavController.this)
                            .setDestination(parent.getId())
                            .createTaskStackBuilder();
                    parentIntents.startActivities();
                    if (mActivity != null) {
                        mActivity.finish();
                    }
                    return true;
                }
                destId = parent.getId();
                parent = parent.getParent();
            }
            // We're already at the startDestination of the graph so there's no 'Up' to go to
            return false;
        } else {
            return popBackStack();
        }
    }

    /**
     * 分发导航事件
     * @param destination NavDestination
     */
    void dispatchOnNavigated(NavDestination destination) {
        for (OnNavigatedListener listener : mOnNavigatedListeners) {
            listener.onNavigated(this, destination);
        }
    }

    /**
     * 加载manifest中graph资源文件
     * <pre class="prettyprint">
     *     <meta-data android:name="android.nav.graph" android:resource="@xml/my_nav_graph" />
     * </pre>
     * @see NavInflater#METADATA_KEY_GRAPH
     * @see NavInflater#inflateMetadataGraph()
     * @see #getGraph
     */
    public void setMetadataGraph() {
        setGraph(getNavInflater().inflateMetadataGraph());
    }


    /**
     * 返回{@link NavInflater inflater}
     *
     * @return navigation资源加载器
     */
    public NavInflater getNavInflater() {
        if (mInflater == null) { mInflater = new NavInflater(mContext, mNavigatorProvider); }
        return mInflater;
    }
    /**
     * 设置GraphId并替换当前的Graph
     * @see #getNavInflater()
     * @see #setGraph(NavGraph)
     * @see #getGraph
     */
    public void setGraph(int graphResId) {
        mGraph = getNavInflater().inflate(graphResId);
        mGraphId = graphResId;
        onGraphCreated();
    }

    /**
     * 设置Graph并替换当前的Graph
     * @see #setGraph(int)
     * @see #getGraph
     */
    public void setGraph(NavGraph graph) {
        mGraph = graph;
        mGraphId = 0;
        onGraphCreated();
    }

    /**
     * 当设置了Graph
     */
    private void onGraphCreated() {
        if (mBackStackToRestore != null) {
            for (int destinationId : mBackStackToRestore) {
                //将需要恢复回退栈的navDestination重新加入回退栈
                NavDestination node = findDestination(destinationId);
                if (node == null) {throw new IllegalStateException("unknown destination during restore: "
                            + mContext.getResources().getResourceName(destinationId));
                }
                mBackStack.add(node);
            }
            //添加至回退栈后清空
            mBackStackToRestore = null;
        }
        if (mGraph != null && mBackStack.isEmpty()) {
            boolean deepLinked = mActivity != null && onHandleDeepLink(mActivity.getIntent());
            if (!deepLinked) {
                // Navigate to the first destination in the graph
                // if we haven't deep linked to a destination
                mGraph.navigate(null, null);
            }
        }
    }

    /**
     * Checks the given Intent for a Navigation deep link and navigates to the deep link if present.
     * This is called automatically for you the first time you set the graph if you've passed in an
     * {@link Activity} as the context when constructing this NavController, but should be manually
     * called if your Activity receives new Intents in {@link Activity#onNewIntent(Intent)}.
     * <p>
     * The types of Intents that are supported include:
     * <ul>
     *     <ol>Intents created by {@link NavDeepLinkBuilder} or
     *     {@link #createDeepLink()}. This assumes that the current graph shares
     *     the same hierarchy to get to the deep linked destination as when the deep link was
     *     constructed.</ol>
     *     <ol>Intents that include a {@link Intent#getData() data Uri}. This Uri will be checked
     *     against the Uri patterns added via {@link NavDestination#addDeepLink(String)}.</ol>
     * </ul>
     * <p>The {@link #getGraph() navigation graph} should be set before calling this method.</p>
     * @param intent The Intent that may contain a valid deep link
     * @return True if the navigation controller found a valid deep link and navigated to it.
     * @see NavDestination#addDeepLink(String)
     */
    public boolean onHandleDeepLink(@Nullable Intent intent) {
        if (intent == null) {
            return false;
        }
        Bundle extras = intent.getExtras();
        int[] deepLink = extras != null ? extras.getIntArray(KEY_DEEP_LINK_IDS) : null;
        Bundle bundle = extras != null ? extras.getBundle(KEY_DEEP_LINK_EXTRAS) : null;
        if ((deepLink == null || deepLink.length == 0) && intent.getData() != null) {
            Pair<NavDestination, Bundle> matchingDeepLink = mGraph.matchDeepLink(intent.getData());
            if (matchingDeepLink != null) {
                deepLink = matchingDeepLink.first.buildDeepLinkIds();
                bundle = matchingDeepLink.second;
            }
        }
        if (deepLink == null || deepLink.length == 0) {
            return false;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putParcelable(KEY_DEEP_LINK_INTENT, intent);
        int flags = intent.getFlags();
        if ((flags & Intent.FLAG_ACTIVITY_NEW_TASK) != 0
                && (flags & Intent.FLAG_ACTIVITY_CLEAR_TASK) == 0) {
            // Someone called us with NEW_TASK, but we don't know what state our whole
            // task stack is in, so we need to manually restart the whole stack to
            // ensure we're in a predictably good state.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder
                    .create(mContext)
                    .addNextIntentWithParentStack(intent);
            taskStackBuilder.startActivities();
            if (mActivity != null) {
                mActivity.finish();
            }
            return true;
        }
        if ((flags & Intent.FLAG_ACTIVITY_NEW_TASK) != 0) {
            // Start with a cleared task starting at our root when we're on our own task
            if (!mBackStack.isEmpty()) {
                navigate(mGraph.getStartDestination(), bundle, new NavOptions.Builder()
                        .setClearTask(true).setEnterAnim(0).setExitAnim(0).build());
            }
            while (mBackStack.size() < deepLink.length) {
                int destinationId = deepLink[mBackStack.size()];
                NavDestination node = findDestination(destinationId);
                if (node == null) {
                    throw new IllegalStateException("unknown destination during deep link: "
                            + NavDestination.getDisplayName(mContext, destinationId));
                }
                node.navigate(bundle,
                        new NavOptions.Builder().setEnterAnim(0).setExitAnim(0).build());
            }
            return true;
        }
        // Assume we're on another apps' task and only start the final destination
        NavGraph graph = mGraph;
        for (int i = 0; i < deepLink.length; i++) {
            int destinationId = deepLink[i];
            NavDestination node = i == 0 ? mGraph : graph.findNode(destinationId);
            if (node == null) {
                throw new IllegalStateException("unknown destination during deep link: "
                        + NavDestination.getDisplayName(mContext, destinationId));
            }
            if (i != deepLink.length - 1) {
                // We're not at the final NavDestination yet, so keep going through the chain
                graph = (NavGraph) node;
            } else {
                // Navigate to the last NavDestination, clearing any existing destinations
                node.navigate(bundle, new NavOptions.Builder()
                        .setClearTask(true).setEnterAnim(0).setExitAnim(0).build());
            }
        }
        return true;
    }

    /**
     * Gets the topmost navigation graph associated with this NavController.
     *
     * @see #setGraph(int)
     * @see #setGraph(NavGraph)
     * @see #setMetadataGraph()
     */
    public NavGraph getGraph() {
        return mGraph;
    }

    /**
     * Gets the current destination.
     */
    public NavDestination getCurrentDestination() {
        return mBackStack.peekLast();
    }


    /**
     * 根据destinationId找到NavDestination
     */
    private NavDestination findDestination(@IdRes int destinationId) {
        if (mGraph == null) {
            return null;
        }
        //如果是Graph返回
        if (mGraph.getId() == destinationId) {
            return mGraph;
        }
        //当前的NavDestination
        NavDestination currentNode = mBackStack.isEmpty() ? mGraph : mBackStack.peekLast();
        //如果currentNode是Graph就返回不是就getParent()获取Graph
        NavGraph currentGraph = currentNode instanceof NavGraph ? (NavGraph) currentNode : currentNode.getParent();
        return currentGraph.findNode(destinationId);
    }



    /**
     * 从导航图中导航中目的地
     * 支持通过{@link NavDestination#getAction(int) action}并导航到该目的地
     *
     * @param resId {@link NavDestination#getAction(int) action}的ActionId或者destinationId
     */
    public final void navigate(int resId) {
        navigate(resId, null);
    }
    /**
     * @param resId {@link NavDestination#getAction(int) action}的ActionId或者destinationId
     * @param args arguments参数
     */
    public final void navigate(int resId,Bundle args) {
        navigate(resId, args, null);
    }
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

    /**
     * Navigate via the given {@link NavDirections}
     *
     * @param directions directions that describe this navigation operation
     */
    public void navigate(@NonNull NavDirections directions) {
        navigate(directions.getActionId(), directions.getArguments());
    }

    /**
     * Navigate via the given {@link NavDirections}
     *
     * @param directions directions that describe this navigation operation
     */
    public void navigate(NavDirections directions, NavOptions navOptions) {
        navigate(directions.getActionId(), directions.getArguments(), navOptions);
    }
    /**
     * Create a deep link to a destination within this NavController.
     *
     * @return a {@link NavDeepLinkBuilder} suitable for constructing a deep link
     */
    @NonNull
    public NavDeepLinkBuilder createDeepLink() {
        return new NavDeepLinkBuilder(this);
    }



    /**
     *
     * 在{@link NavHostFragment#onSaveInstanceState(Bundle)}调用
     * 将所有导航控制器状态保存到Bundle
     */
    public Bundle saveState() {
        Bundle b = null;
        //保存Graph
        if (mGraphId != 0) {
            b = new Bundle();
            b.putInt(KEY_GRAPH_ID, mGraphId);
        }
        //保存回退栈中destination的id
        if (!mBackStack.isEmpty()) {
            if (b == null) {
                b = new Bundle();
            }
            int[] backStack = new int[mBackStack.size()];
            int index = 0;
            for (NavDestination destination : mBackStack) {
                backStack[index++] = destination.getId();
            }
            b.putIntArray(KEY_BACK_STACK_IDS, backStack);
        }
        return b;
    }

    /**
     * 由{@link NavHostFragment#onCreate(Bundle)}
     *
     * 保存了Controller的状态
     */
    public void restoreState(Bundle navState) {
        if (navState == null) {
            return;
        }
        //恢复了graphId
        mGraphId = navState.getInt(KEY_GRAPH_ID);
        //保存的回退栈
        mBackStackToRestore = navState.getIntArray(KEY_BACK_STACK_IDS);
        if (mGraphId != 0) {
            //不为0立即设置graph
            setGraph(mGraphId);
        }
    }
}
