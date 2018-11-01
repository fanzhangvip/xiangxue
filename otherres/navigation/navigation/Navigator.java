package com.me94me.example_navigatioin_resource.navigation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.IntDef;

/**
 * Navigator定义了一种在应用程序中导航的机制。
 *
 * 每一个navigator为导航指定了规则
 * 比如{@link ActivityNavigator}知道使用{@link Context#startActivity(Intent) startActivity}导航到一个activity
 *
 * 当在两个destination导航时，navigators应该能够管理它们自己的回退栈
 * {@link NavController}管理当前导航堆栈的所有导航器的后退堆栈。
 *
 * 每一个Navigator应该在类上添加{@link Name Navigator.Name}注解
 *
 * {@link NavDestination destination}子类使用的任何自定义属性都应具有与导航器名称对应的名称
 */
public abstract class Navigator<D extends NavDestination> {

    /**
     * 该注解应该被添加到每一个navigator类上以表示用于在{@link NavigatorProvider}上注册的navigator的name
     *
     * @see NavigatorProvider#addNavigator(Navigator)
     * @see NavigatorProvider#getNavigator(Class)
     */
    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface Name {
        String value();
    }

    /**
     * 影响回退事件的值
     */
    @Retention(SOURCE)
    @IntDef({BACK_STACK_UNCHANGED, BACK_STACK_DESTINATION_ADDED, BACK_STACK_DESTINATION_POPPED})
    @interface BackStackEffect {}

    /**
     * 表示导航事件不会改变{@link NavController}的回退栈
     * 例如，如果现有目标位于堆栈顶部，则{@link NavOptions＃shouldLaunchSingleTop()}导航事件可能不会导致返回堆栈更改。
     * @see #dispatchOnNavigatorNavigated
     */
    public static final int BACK_STACK_UNCHANGED = 0;

    /**
     * 表示导航事件会添加一个新值到回退事件
     * 仅仅当destination拥有该flag才会被{@link NavController#navigateUp()}处理
     * @see #dispatchOnNavigatorNavigated
     */
    public static final int BACK_STACK_DESTINATION_ADDED = 1;

    /**
     * 表示导航事件导航事件从回退栈中弹出了一个条目
     * @see #dispatchOnNavigatorNavigated
     */
    public static final int BACK_STACK_DESTINATION_POPPED = 2;


    /**
     * 创建一个与navigator关联的destination
     *
     * 任何destination的初始化都在该destination的构造方法中完成，因为不能保证每个目标都将通过此方法创建
     * @return a new NavDestination
     */
    public abstract D createDestination();

    /**
     * 导航到一个目的地
     * 在导航图上，请求导航到一个给定的与该navigator关联的目的地
     * 这个方法不应该直接被调用，应该使用{@link NavController}
     *
     * 实现类需要调用{@link #dispatchOnNavigatorNavigated}来通知监听器
     *
     * @param destination destination node to navigate to
     * @param args arguments to use for navigation
     * @param navOptions additional options for navigation
     */
    public abstract void navigate( D destination, Bundle args,NavOptions navOptions);


    /**
     * 尝试弹出navigator的回退栈
     *
     * 实现类应该调用{@link #dispatchOnNavigatorNavigated}通知监听器并且成功了返回true
     * 如果导航未能正确执行返回false，例如navigator的回退栈为空
     *
     * @return {@code true} if pop was successful
     */
    public abstract boolean popBackStack();


    //所有导航监听器
    private final CopyOnWriteArrayList<OnNavigatorNavigatedListener> mOnNavigatedListeners = new CopyOnWriteArrayList<>();

    /**
     * 添加监听器
     * 大多数application应该使用{@link NavController#addOnNavigatedListener(NavController.OnNavigatedListener)}
     * @param listener listener to add
     */
    public final void addOnNavigatorNavigatedListener( OnNavigatorNavigatedListener listener) {
        mOnNavigatedListeners.add(listener);
    }

    /**
     * 移除一个监听器
     * @param listener 将被移除的监听器
     */
    public final void removeOnNavigatorNavigatedListener(OnNavigatorNavigatedListener listener) {
        mOnNavigatedListeners.remove(listener);
    }

    /**
     * 将导航事件发送到所有已注册的{@link OnNavigatorNavigatedListener listeners}。导航器实现的功能。
     * @param destId 新的目的地的Id
     * @param backStackEffect 导航事件对回退栈产生了如何的影响
     */
    public final void dispatchOnNavigatorNavigated( int destId, @BackStackEffect int backStackEffect) {
        for (OnNavigatorNavigatedListener listener : mOnNavigatedListeners) {
            listener.onNavigatorNavigated(this, destId, backStackEffect);
        }
    }

    /**
     * 观察导航器导航事件的监听器
     * app代码应该使用{@link NavController.OnNavigatedListener}来监听
     */
    public interface OnNavigatorNavigatedListener {
        /**
         * 当Navigator导航到了一个新目的地时调用
         * @param navigator navigator
         * @param destId destId
         * @param backStackEffect
         */
        void onNavigatorNavigated(Navigator navigator, int destId,
                @BackStackEffect int backStackEffect);
    }
}
