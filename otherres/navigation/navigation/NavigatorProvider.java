
package com.me94me.example_navigatioin_resource.navigation;

import android.annotation.SuppressLint;

/**
 * 保存了一个{@link Navigator}的集合
 */
@SuppressLint("TypeParameterUnusedInFormals")
public interface NavigatorProvider {

    /**
     * 使用navigator类获取{@link Navigator}
     *
     * @param navigatorClass class of the navigator to return
     * @return the registered navigator with the given {@link Navigator.Name}
     *
     * @throws IllegalArgumentException 如果navigator没有@link Navigator.Name Navigator.Name annotation}
     * @throws IllegalStateException 如果navigator还没有添加
     *
     * @see #addNavigator(Navigator)
     */
    <D extends NavDestination, T extends Navigator<? extends D>> T getNavigator(Class<T> navigatorClass);

    /**
     * 使用由{@link Navigator.Name Navigator.Name annotation}提供的name获取{@link Navigator}
     *
     * @param name name of the navigator to return
     * @return the registered navigator with the given name
     *
     * @throws IllegalStateException 如果navigator还没有添加
     *
     * @see #addNavigator(String, Navigator)
     */
    <D extends NavDestination, T extends Navigator<? extends D>> T getNavigator(String name);

    /**
     * 使用{@link Navigator.Name Navigator.Name annotation}提供的name注册navigator
     * {@link NavDestination destinations}可以通过名称引用任何注册的导航器来进行inflate
     * 如果已经注册了此名称的导航器，则此新导航器将替换它。
     * @param navigator 将要添加的导航器
     * @return 返回之前通过{@link Navigator.Name Navigator.Name annotation}注解添加的导航器
     */
    Navigator<? extends NavDestination> addNavigator(Navigator<? extends NavDestination> navigator);

    /**
     * 通过name注册一个导航器
     * {@link NavDestination destinations}可以通过name引用这个导航器
     * 如果已经注册了此name的导航器，则此新导航器将替换它。
     *
     * @param name navigator的name
     * @param navigator 将要添加的导航器
     * @return 返回此名称之前的导航器
     */
    Navigator<? extends NavDestination> addNavigator(String name, Navigator<? extends NavDestination> navigator);
}
