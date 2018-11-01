package com.me94me.example_navigatioin_resource.navigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;

import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 通过{@link FragmentTransaction fragment transactions}进行导航
 * 使用该navigator必须设置一个可用的fragment使用<code>android:name</code> 或者 {@link Destination#setFragmentClass}
 */
@Navigator.Name("fragment")
public class FragmentNavigator extends Navigator<FragmentNavigator.Destination> {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private int mBackStackCount;

    /**
     * 回退栈监听器
     */
    private final FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    int newCount = mFragmentManager.getBackStackEntryCount();
                    //设置回退栈影响因素
                    int backStackEffect;
                    if (newCount < mBackStackCount) {
                        backStackEffect = BACK_STACK_DESTINATION_POPPED;
                    } else if (newCount > mBackStackCount) {
                        backStackEffect = BACK_STACK_DESTINATION_ADDED;
                    } else {
                        backStackEffect = BACK_STACK_UNCHANGED;
                    }
                    mBackStackCount = newCount;

                    int destId = 0;
                    //获取当前destination
                    StateFragment state = getState();
                    if (state != null) {
                        destId = state.mCurrentDestId;
                    }
                    //分发事件
                    dispatchOnNavigatorNavigated(destId, backStackEffect);
                }
            };

    public FragmentNavigator(Context context, FragmentManager manager, int containerId) {
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;

        mBackStackCount = mFragmentManager.getBackStackEntryCount();
        mFragmentManager.addOnBackStackChangedListener(mOnBackStackChangedListener);
    }

    @Override
    public boolean popBackStack() {
        return mFragmentManager.popBackStackImmediate();
    }


    @Override
    public Destination createDestination() {
        return new Destination(this);
    }

    /**
     * 获取destinationId
     */
    private String getBackStackName(int destinationId) {
        // This gives us the resource name if it exists,
        // or just the destinationId if it doesn't exist
        try {
            return mContext.getResources().getResourceName(destinationId);
        } catch (Resources.NotFoundException e) {
            return Integer.toString(destinationId);
        }
    }


    @Override
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
        //获取状态
        final StateFragment oldState = getState();
        //移除之前的保存的状态
        if (oldState != null) {
            ft.remove(oldState);
        }
        final int destId = destination.getId();
        //设置当前的destinationId
        final StateFragment newState = new StateFragment();
        newState.mCurrentDestId = destId;
        //添加了StateFragment,用该保存当前destinationId
        ft.add(newState, StateFragment.FRAGMENT_TAG);

        final boolean initialNavigation = mFragmentManager.getFragments().isEmpty();
        final boolean isClearTask = navOptions != null && navOptions.shouldClearTask();
        // TODO Build first class singleTop behavior for fragments
        final boolean isSingleTopReplacement = navOptions != null && oldState != null
                && navOptions.shouldLaunchSingleTop()
                && oldState.mCurrentDestId == destId;
        if (!initialNavigation && !isClearTask && !isSingleTopReplacement) {
            //回退栈不为null并且不清空回退栈，不是singleTop加入回退栈
            ft.addToBackStack(getBackStackName(destId));
        } else {
            ft.runOnCommit(new Runnable() {
                @Override
                public void run() {
                    dispatchOnNavigatorNavigated(destId, isSingleTopReplacement ? BACK_STACK_UNCHANGED : BACK_STACK_DESTINATION_ADDED);
                }
            });
        }
        ft.commit();
        //立即执行
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 获取保存状态的fragment
     */
    private StateFragment getState() {
        return (StateFragment) mFragmentManager.findFragmentByTag(StateFragment.FRAGMENT_TAG);
    }


    /**
     * {@link FragmentNavigator}的NavDestination
     */
    public static class Destination extends NavDestination {

        private static final HashMap<String, Class<? extends Fragment>> sFragmentClasses = new HashMap<>();

        private Class<? extends Fragment> mFragmentClass;

        /**
         * 构造一个新的fragment destination
         * 只有在{@link #setFragmentClass(Class)}才合法
         *
         * @param navigatorProvider The {@link NavController} which this destination will be associated with.
         */
        public Destination(NavigatorProvider navigatorProvider) {
            this(navigatorProvider.getNavigator(FragmentNavigator.class));
        }
        /**
         * @param fragmentNavigator {@link FragmentNavigator}
         */
        public Destination( Navigator<? extends Destination> fragmentNavigator) {
            super(fragmentNavigator);
        }

        @Override
        public void onInflate(Context context, AttributeSet attrs) {
            super.onInflate(context, attrs);
            TypedArray a = context.getResources().obtainAttributes(attrs,
                    R.styleable.FragmentNavigator);
            //获取layout中fragment的name属性
            //设置该Fragment的class
            setFragmentClass(getFragmentClassByName(context, a.getString(R.styleable.FragmentNavigator_android_name)));
            a.recycle();
        }

        /**
         * 通过layout中的name获取fragment
         * @param context context
         * @param name name
         * @return fragment
         */
        @SuppressWarnings("unchecked")
        private Class<? extends Fragment> getFragmentClassByName(Context context, String name) {
            if (name != null && name.charAt(0) == '.') {
                name = context.getPackageName() + name;
            }
            //从缓存中查找
            Class<? extends Fragment> clazz = sFragmentClasses.get(name);
            if (clazz == null) {
                try {
                    //反射构建fragment实例并缓存起来
                    clazz = (Class<? extends Fragment>) Class.forName(name, true,context.getClassLoader());
                    sFragmentClasses.put(name, clazz);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return clazz;
        }

        /**
         * 设置与该destination关联的fragment
         * @param clazz 当导航到该目的地需要显示的fragment、
         * @return this {@link Destination}
         */
        public Destination setFragmentClass(Class<? extends Fragment> clazz) {
            mFragmentClass = clazz;
            return this;
        }

        /**
         * 获取该fragment
         */
        public Class<? extends Fragment> getFragmentClass() {
            return mFragmentClass;
        }

        /**
         * 创建一个与该destination关联的{@link Fragment}
         * @param args 新fragment附加的args
         * @return an instance of the {@link #getFragmentClass() Fragment class} associated with this destination
         */
        @SuppressWarnings("ClassNewInstance")
        public Fragment createFragment( Bundle args) {
            Class<? extends Fragment> clazz = getFragmentClass();
            if (clazz == null) {
                throw new IllegalStateException("fragment class not set");
            }
            Fragment f;
            try {
                //当需要导航的时候新建实例
                f = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (args != null) {
                f.setArguments(args);
            }
            return f;
        }
    }

    /**
     * FragmentNavigator用于跟踪其他导航状态的内部fragment
     * An internal fragment used by FragmentNavigator to track additional navigation state.
     *
     */
    public static class StateFragment extends Fragment {
        //通过该tag找到该StateFragment
        static final String FRAGMENT_TAG = "android-support-nav:FragmentNavigator.StateFragment";

        private static final String KEY_CURRENT_DEST_ID = "currentDestId";

        int mCurrentDestId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (savedInstanceState != null) {
                mCurrentDestId = savedInstanceState.getInt(KEY_CURRENT_DEST_ID);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CURRENT_DEST_ID, mCurrentDestId);
        }
    }
}
