package com.me94me.example_navigatioin_resource.navigation;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayDeque;

/**
 * {@link NavGraph}元素的导航器
 * 当NavGraph作为actions进行导航时的操作
 */
@Navigator.Name("navigation")
public class NavGraphNavigator extends Navigator<NavGraph> {
    private static final String KEY_BACK_STACK_IDS = "androidx-nav-graph:navigator:backStackIds";
    private Context mContext;
    //回退栈(保存Graph的destinationId)
    private ArrayDeque<Integer> mBackStack = new ArrayDeque<>();

    /**
     * 构造导航器
     * @param context context
     */
    public NavGraphNavigator(Context context) {
        mContext = context;
    }

    /**
     * 创建一个与navigator相关的{@link NavGraph}
     */
    @Override
    public NavGraph createDestination() {
        return new NavGraph(this);
    }

    @Override
    public void navigate(NavGraph destination,Bundle args,NavOptions navOptions) {
        //找到第一个目的地
        int startId = destination.getStartDestination();
        if (startId == 0) {
            throw new IllegalStateException("no start destination defined via"
                    + " app:startDestination for "
                    + (destination.getId() != 0
                            ? NavDestination.getDisplayName(mContext, destination.getId())
                            : "the root navigation"));
        }
        NavDestination startDestination = destination.findNode(startId, false);
        if (startDestination == null) {
            final String dest = NavDestination.getDisplayName(mContext, startId);
            throw new IllegalArgumentException("navigation destination " + dest
                    + " is not a direct child of this NavGraph");
        }
        if (navOptions != null && navOptions.shouldLaunchSingleTop()
                && isAlreadyTop(destination)) {
            dispatchOnNavigatorNavigated(destination.getId(), BACK_STACK_UNCHANGED);
        } else {
            mBackStack.add(destination.getId());
            dispatchOnNavigatorNavigated(destination.getId(), BACK_STACK_DESTINATION_ADDED);
        }
        //通过该目的地自己的navigator进行导航
        startDestination.navigate(args, navOptions);
    }

    /**
     * This method to checks to see if navigating to the given destId would result in you
     * being right back where you started (we want to avoid creating a duplicate stack of the
     * same destinations).
     *
     * Because you can have a NavGraph as the start destination of another graph, we need to both
     * check the current NavGraph (i.e., no direct singleTop copies) and all of the parents that
     * start the current NavGraph via their start destinations.
     */
    private boolean isAlreadyTop(NavGraph destination) {
        if (mBackStack.isEmpty()) {
            return false;
        }
        int topDestId = mBackStack.peekLast();
        NavGraph current = destination;
        while (current.getId() != topDestId) {
            NavDestination startDestination = current.findNode(current.getStartDestination());
            if (startDestination instanceof NavGraph) {
                current = (NavGraph) startDestination;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean popBackStack() {
        if (mBackStack.isEmpty()) {
            return false;
        }
        mBackStack.removeLast();
        int destId = mBackStack.isEmpty() ? 0 : mBackStack.peekLast();
        dispatchOnNavigatorNavigated(destId, BACK_STACK_DESTINATION_POPPED);
        return true;
    }

    @Override
    public Bundle onSaveState() {
        Bundle b = new Bundle();
        int[] backStack = new int[mBackStack.size()];
        int index = 0;
        for (Integer id : mBackStack) {
            backStack[index++] = id;
        }
        b.putIntArray(KEY_BACK_STACK_IDS, backStack);
        return b;
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        if (savedState != null) {
            int[] backStack = savedState.getIntArray(KEY_BACK_STACK_IDS);
            if (backStack != null) {
                mBackStack.clear();
                for (int destId : backStack) {
                    mBackStack.add(destId);
                }
            }
        }
    }
}
