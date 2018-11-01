package com.me94me.example_navigatioin_resource.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * NavHostFragment在布局中提供了一个区域，用于进行自包含导航.
 * <p>
 * NavHostFragment旨在用作布局资源中的内容区域，用于定义应用程序围绕它的镶边
 *
 * <pre class="prettyprint">
 *     <android.support.v4.widget.DrawerLayout
 *             xmlns:android="http://schemas.android.com/apk/res/android"
 *             xmlns:app="http://schemas.android.com/apk/res-auto"
 *             android:layout_width="match_parent"
 *             android:layout_height="match_parent">
 *         <fragment
 *                 android:layout_width="match_parent"
 *                 android:layout_height="match_parent"
 *                 android:id="@+id/my_nav_host_fragment"
 *                 android:name="androidx.navigation.fragment.NavHostFragment"
 *                 app:navGraph="@xml/nav_sample"
 *                 app:defaultNavHost="true" />
 *         <android.support.design.widget.NavigationView
 *                 android:layout_width="wrap_content"
 *                 android:layout_height="match_parent"
 *                 android:layout_gravity="start"/>
 *     </android.support.v4.widget.DrawerLayout>
 * </pre>
 * <p>
 * 每个NavHostFragment都有一个{@link NavController}，用于定义导航主机中的有效导航。
 * 这包括{@link NavGraph 导航图}以及导航状态，例如当前位置和后台堆栈，它们将与NavHostFragment本身一起保存和恢复
 * <p>
 * NavHostFragments在其视图子view的host中注册其导航控制器，
 * 以便任何后代可以通过{@link Navigation}帮助程序类的方法（例如{@link Navigation＃findNavController（View）}）获取控制器实例。
 * 在导航目标fragment中查看事件监听实现（例如{@link android.view.View.OnClickListener}）可以使用这些帮助程序基于用户交互进行导航，而无需与导航host建立紧密耦合
 */
public class NavHostFragment extends Fragment implements NavHost {

    //******************** onSaveInstanceState()里保存的状态 ************************
    //保存了controller状态的bundle的key
    //保存了一个bundle，保存了两个值：controller的graphId
//                                    controller回退栈destinationId
    private static final String KEY_NAV_CONTROLLER_STATE = "android-support-nav:fragment:navControllerState";

    //保存该Fragment是不是HostFragment
    private static final String KEY_DEFAULT_NAV_HOST = "android-support-nav:fragment:defaultHost";



    //******************** 临时保存在Fragment的arguments里的graph的bundle的键 *****************
    //将GraphId保存在arguments的key
    private static final String KEY_GRAPH_ID = "android-support-nav:fragment:graphId";

    //全局变量
    //NavController
    private NavController mNavController;
    //判断是否是HostFragment
    private boolean mDefaultNavHost;

    /**
     * 找一个{@link NavController}给一个本地{@link Fragment}.
     * <p>
     * 此方法将找到与此Fragment关联的{@link NavController}，首先查找给定Fragment的父链上的{@link NavHostFragment}。
     * 如果找不到{@link NavController}，此方法将查找此方法 片段的{@link Fragment＃getView（）视图层次结构}由{@link Navigation＃findNavController（View）}指定。
     *
     * @param fragment the locally scoped Fragment for navigation
     * @return the locally scoped {@link NavController} for navigating from this {@link Fragment}
     * @throws IllegalStateException 如果给定的片段与{@link NavHost}不对应或不在NavHost中.
     */
    public static NavController findNavController(Fragment fragment) {
        Fragment findFragment = fragment;
        while (findFragment != null) {
            //直接通过NavHostFragment获取mNavHostFragment
            if (findFragment instanceof NavHostFragment) {
                return ((NavHostFragment) findFragment).getNavController();
            }
            Fragment primaryNavFragment = findFragment.getFragmentManager().getPrimaryNavigationFragment();
            if (primaryNavFragment instanceof NavHostFragment) {
                return ((NavHostFragment) primaryNavFragment).getNavController();
            }
            //获取父Parent
            findFragment = findFragment.getParentFragment();
        }
        // 如果适用，请尝试寻找与view相关联的视图
        View view = fragment.getView();
        if (view != null) {
            return Navigation.findNavController(view);
        }
        throw new IllegalStateException("Fragment " + fragment + " does not have a NavController set");
    }


    /**
     * 创建一个新的NavHostFragment实例
     *
     * @param graphResId graph的资源Id
     * @return NavHostFragment实例
     */
    public static NavHostFragment create(int graphResId) {
        Bundle b = null;
        if (graphResId != 0) {
            b = new Bundle();
            b.putInt(KEY_GRAPH_ID, graphResId);
        }
        //新建NavHostFragment并将graph以bundle形式保存起来
        final NavHostFragment result = new NavHostFragment();
        if (b != null) {
            result.setArguments(b);
        }
        return result;
    }

    /**
     * 在调用{@link #onCreate(Bundle)}之前返回null
     *
     * @return controller
     * @throws IllegalStateException 在{@link #onCreate(Bundle)}之前调用
     */
    @Override
    public NavController getNavController() {
        if (mNavController == null) {
            throw new IllegalStateException("NavController is not available before onCreate()");
        }
        return mNavController;
    }

    /**
     * Graph_Id:若mNavController为null保存在NavHostFragment的arguments
     * 若mNavController不为null则立即设置
     *
     * @param graphResId graph的资源Id
     */
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // TODO This feature should probably be a first-class feature of the Fragment system,
        // 但它可以留在这里，直到我们可以将必要的attr资源添加到fragment。
        if (mDefaultNavHost) {
            //将当前NavHostFragment设置为PrimaryNavigationFragment
            getFragmentManager().beginTransaction().setPrimaryNavigationFragment(this).commit();
        }
    }

    /**
     * 1，构建NavController实例
     * 2，添加FragmentNavigator到SimpleNavigatorProvider
     * 3，恢复状态
     * @param savedInstanceState bundle
     */
    @Override
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
                //设置manifest里的graph
                mNavController.setMetadataGraph();
            }
        }
    }

    /**
     * 创建此NavHostFragment将使用的FragmentNavigator。
     * 默认情况下，它使用{@link FragmentNavigator}，它取代了NavHostFragment的全部内容。
     * 这只在{@link #onCreate（Bundle）}中调用一次，不应该由子类直接调用。
     *
     * @return a new instance of a FragmentNavigator
     */
    protected Navigator<? extends FragmentNavigator.Destination> createFragmentNavigator() {
        return new FragmentNavigator(getContext(), getChildFragmentManager(), getId());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        // 当通过XML添加时，这没有任何效果（因为此FrameLayout会自动获得ID），
        // 但是这确保了View在作为子fragment事务所需的编程方式添加NavHostFragment的情况下作为此Fragment的View层次结构的一部分存在
        //Id设置为fragment的Id
        frameLayout.setId(getId());
        //NavHostFragment就是一个frameLayout,一个容器
        return frameLayout;
    }

    @Override
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


    /**
     * 加载布局中的navGraph和defaultNavHost
     * 当fragment被创建进行layout进行加载的时候调用
     */
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


    /**
     * 保存状态:1,是否是Nav_host
     *          2,保存navController的状态:
     *              GraphId和回退栈中所有destinationId作为IntArray保存在outState
     * @param outState outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存了graph
        //保存了回退栈中所有目的地的id
        Bundle navState = mNavController.saveState();
        if (navState != null) {
            //将controller的状态保存到outState
            outState.putBundle(KEY_NAV_CONTROLLER_STATE, navState);
        }
        //在加载布局中获取的
        //如果是默认host,保存为true
        if (mDefaultNavHost) {
            outState.putBoolean(KEY_DEFAULT_NAV_HOST, true);
        }
    }
}
