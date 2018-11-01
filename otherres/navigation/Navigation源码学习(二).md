# Jetpack之Navigation源码学习（二）

> 如果有人不相信代码是简单的，那是因为他们没有意识到人生有多复杂。 

本篇梳理NavGraph的构建和加载流程

## NavHostFragement#onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState)

```Java
@Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavHostFragment);
        /**
         *  <fragment
         *             android:layout_width="match_parent"
         *             android:layout_height="match_parent"
         *             android:id="@+id/my_nav_host_fragment"
         *             android:name="androidx.navigation.fragment.NavHostFragment"
         *             app:navGraph="@navigation/mobile_navigation"
         *             app:defaultNavHost="true" />
         */
        //获取布局中Graph的id
        final int graphId = a.getResourceId(R.styleable.NavHostFragment_navGraph, 0);
        //是否是默认Host
        final boolean defaultHost = a.getBoolean(R.styleable.NavHostFragment_defaultNavHost, false);


        //不为0设置graph
        if (graphId != 0) {
            setGraph(graphId);
        }
        //如果是主导航fragment
        if (defaultHost) {
            mDefaultNavHost = true;
            if (isAdded()) {
                //如果是主导航在此FragmentManager中将当前fragment设置为主导航fragment
                getFragmentManager().beginTransaction().setPrimaryNavigationFragment(this).commit();
            }
        }
        a.recycle();
    }
```

该方法在Fragment进行layout布局的时候调用，并获取布局中的navGraph和defaultNavHost，而navGraph就是我们在资源文件中添加的导航文件，获取到了navGraph，调用setGraph()进行加载

接着，看setGraph(graphId)

## NavHostFragement#setGraph(int graphResId)

```java
  public void setGraph(int graphResId) {
        //mNavController为null保存在arguments里
        if (mNavController == null) {
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            //为null就保存到arguments中
            args.putInt(KEY_GRAPH_ID, graphResId);
            setArguments(args);
        } else {
            //不为null就立即设置
            mNavController.setGraph(graphResId);
        }
    }
```

## NavController#setGraph(int graphResId)

注释里很详细了，接着调用NavController的setGraph()

```java
   public void setGraph(int graphResId) {
        mGraph = getNavInflater().inflate(graphResId);
        mGraphId = graphResId;
        onGraphCreated();
    }
```

## NavController#getNavInflater()

获取NavInflater并加载，接着看下getNavInflater()

```java
    public NavInflater getNavInflater() {
        if (mInflater == null) { mInflater = new NavInflater(mContext, mNavigatorProvider); }
        return mInflater;
    }
```

这个mNavigatorProvider是navController中的全局变量

```Java
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
```

## NavInflater#inflate(int graphResId)

接着看NavInflater中inflate()

```Java
/**
 * 从给定的XML资源id加载Graph
 *
 * @param graphResId GraphId
 * @return NavGraph
 */
public NavGraph inflate(int graphResId) {
    //mContext从构造方法传入
    Resources res = mContext.getResources();
    XmlResourceParser parser = res.getXml(graphResId);
    final AttributeSet attrs = Xml.asAttributeSet(parser);
    try {
        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
            //空循环
        }
        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }

        String rootElement = parser.getName();
        //获取NavDestination
        NavDestination destination = inflate(res, parser, attrs);
        if (!(destination instanceof NavGraph)) {
            throw new IllegalArgumentException("Root element <" + rootElement + ">"
                    + " did not inflate into a NavGraph");
        }
        return (NavGraph) destination;
    } catch (Exception e) {
        throw new RuntimeException("Exception inflating "
                + res.getResourceName(graphResId) + " line "
                + parser.getLineNumber(), e);
    } finally {
        parser.close();
    }
}
```

## NavInflater#inflate(Resources res, XmlResourceParser parser, AttributeSet attrs)

接着调用了inflate(Resources res, XmlResourceParser parser, AttributeSet attrs)

```Java
private NavDestination inflate(Resources res, XmlResourceParser parser, AttributeSet attrs)
        throws XmlPullParserException, IOException {
    //获取navigator
    Navigator navigator = mNavigatorProvider.getNavigator(parser.getName());
    //创建节点
    final NavDestination dest = navigator.createDestination();
    //加载每个节点，通过attrs获取每个destination的id和label
    dest.onInflate(mContext, attrs);
    //parser的深度
    final int innerDepth = parser.getDepth() + 1;
    int type;
    int depth;
    while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
            && ((depth = parser.getDepth()) >= innerDepth
            || type != XmlPullParser.END_TAG)) {
        if (type != XmlPullParser.START_TAG) {
            continue;
        }

        if (depth > innerDepth) {
            continue;
        }

        final String name = parser.getName();
        if (TAG_ARGUMENT.equals(name)) {
            //加载参数
            inflateArgument(res, dest, attrs);
        } else if (TAG_DEEP_LINK.equals(name)) {
            //加载深度链接
            inflateDeepLink(res, dest, attrs);
        } else if (TAG_ACTION.equals(name)) {
            //加载Action
            inflateAction(res, dest, attrs);
        } else if (TAG_INCLUDE.equals(name) && dest instanceof NavGraph) {//如果子节点为graph加载子节点的destination
            final TypedArray a = res.obtainAttributes(attrs, R.styleable.NavInclude);
            final int id = a.getResourceId(R.styleable.NavInclude_graph, 0);
            ((NavGraph) dest).addDestination(inflate(id));
            a.recycle();
        } else if (dest instanceof NavGraph) {
            //如果子节点为graph加载子节点的destination
            //向每个NavGraph中加入Destination
            ((NavGraph) dest).addDestination(inflate(res, parser, attrs));
        }
    }
    return dest;
}
```

这里就将整个资源文件navigation加载完毕了

我学习Navigation的项目源码在这里(有更多注释):[学习源码](https://github.com/me94me/Mandroid/tree/master/PreferredLibrary/Navigation/example_navigatioin_resource)
