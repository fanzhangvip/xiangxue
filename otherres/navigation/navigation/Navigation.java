package com.me94me.example_navigatioin_resource.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;

import com.me94me.example_navigatioin_resource.R;

import java.lang.ref.WeakReference;

import androidx.core.app.ActivityCompat;

/**
 *  提供NavController的入口类
 */
public class Navigation {

    private Navigation() {
    }


    /**
     * 通过activity和viewId找到与view关联的{@link NavController}
     *
     * 最终调用了{@link #findNavController(View)}
     *
     * @param activity view所在的Activity
     * @param viewId View的id
     * @return 与view关联的{@link NavController}
     * @throws IllegalStateException 如果给定的viewId与{@link NavHost}不对应或不在NavHost中。
     */
    public static NavController findNavController(Activity activity,int viewId) {
        View view = ActivityCompat.requireViewById(activity, viewId);
        NavController navController = findViewNavController(view);
        if (navController == null) { throw new IllegalStateException("Activity " + activity
                + " does not have a NavController set on " + viewId); }
        return navController;
    }
    /**
     * 通过View获取controller
     */
    public static NavController findNavController(View view) {
        NavController navController = findViewNavController(view);
        if (navController == null) {
            throw new IllegalStateException("View " + view + " does not have a NavController set");
        }
        return navController;
    }

    /**
     * 当点击导航到目的地时创建点击监听事件{@link android.view.View.OnClickListener}
     *
     * 并支持通过{@link NavDestination#getAction(int) action}进行的导航
     * @param resId 当View点击时的{@link NavDestination#getAction(int) action}的ActionId 或者 destinationId
     * @return view的点击监听器
     */
    public static View.OnClickListener createNavigateOnClickListener(final int resId) {
        return createNavigateOnClickListener(resId, null);
    }

    /**
     * @param args arguments参数
     */
    public static View.OnClickListener createNavigateOnClickListener(final int resId,final Bundle args) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(resId, args);
            }
        };
    }

    /**
     * 将NavController与给定的View相关联
     * 允许开发人员使用{@link #findNavController（View）}和{@link #findNavController（Activity，int）}
     * 与该View或其任何子项一起检索NavController。
     *
     * 在{@link NavHostFragment#onViewCreated}中调用
     *
     * @param view 与NavController相关联的View
     * @param controller 通过{@link #findNavController(View)}获取的NavController
     */
    public static void setViewNavController(View view, NavController controller) {
        view.setTag(R.id.nav_controller_view_tag, controller);
    }


    /******************************* 私有方法 ********************************/


    /**
     * 递归View层次结构，查找NavController
     */
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
    /**
     * 通过view的Tag获取NavController
     */
    @SuppressWarnings("unchecked")
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
}
