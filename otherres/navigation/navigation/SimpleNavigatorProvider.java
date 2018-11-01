
package com.me94me.example_navigatioin_resource.navigation;

import android.annotation.SuppressLint;

import java.util.HashMap;

import androidx.annotation.RestrictTo;

/**
 * {@link NavigatorProvider}的简单实现
 * 在给定类名时使用{@link Navigator.Name}的注解内容存储{@link Navigator navigators}的实例
 *
 */
@SuppressLint("TypeParameterUnusedInFormals")
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class SimpleNavigatorProvider implements NavigatorProvider {

    /**
     * {@link NavGraphNavigator}中的navigation就是{@link Navigator.Name}注解的内容
     *
     * {@link NavGraphNavigator}的注解内容为"navigation"  {NavGraphNavigator,"navigation"}
     * {@link ActivityNavigator}的注解内容为"activity"    {ActivityNavigator,"activity"}
     * {@link FragmentNavigator}的注解内容为"fragment"    {FragmentNavigator,"fragment"}
     */
    //以类名为键，注解内容为值保存
    //注解名
    private static final HashMap<Class, String> sAnnotationNames = new HashMap<>();


    //以注解内容为键，类实例为值保存
    //navigators
    private final HashMap<String, Navigator<? extends NavDestination>> mNavigators = new HashMap<>();

    /**
     * 获取navigatorClass的{@link Navigator.Name}注解的标识内容
     */
    private String getNameForNavigator(Class<? extends Navigator> navigatorClass) {
        String name = sAnnotationNames.get(navigatorClass);
        if (name == null) {
            //获取navigatorClass的注解内容
            Navigator.Name annotation = navigatorClass.getAnnotation(Navigator.Name.class);
            //不为null获取注解内容
            name = annotation != null ? annotation.value() : null;
            if (!validateName(name)) {
                throw new IllegalArgumentException("No @Navigator.Name annotation found for "
                        + navigatorClass.getSimpleName());
            }
            sAnnotationNames.put(navigatorClass, name);
        }
        return name;
    }

    /**
     * 通过类名获取Navigator
     */
    @Override
    public <D extends NavDestination, T extends Navigator<? extends D>> T getNavigator(Class<T> navigatorClass) {
        //获取navigatorClass类注解的标识内容
        String name = getNameForNavigator(navigatorClass);
        return getNavigator(name);
    }

    /**
     * 通过类名获取Navigator
     */
    @SuppressWarnings("unchecked")
    @Override
    public <D extends NavDestination, T extends Navigator<? extends D>> T getNavigator(String name) {
        if (!validateName(name)) {//验证合法性
            throw new IllegalArgumentException("navigator name cannot be an empty string");
        }
        Navigator<? extends NavDestination> navigator = mNavigators.get(name);
        //如果未找到报错
        if (navigator == null) {
            throw new IllegalStateException("Could not find Navigator with name \"" + name+ "\". You must call NavController.addNavigator() for each navigation type.");
        }
        return (T) navigator;
    }


    /**
     * 添加Navigator,默认为FragmentNavigator
     * 并以类注解标识值name和navigator存入hashMap中
     */
    @Override
    public Navigator<? extends NavDestination> addNavigator( Navigator<? extends NavDestination> navigator) {
        String name = getNameForNavigator(navigator.getClass());
        return addNavigator(name, navigator);
    }


    @Override
    public Navigator<? extends NavDestination> addNavigator(String name, Navigator<? extends NavDestination> navigator) {
        if (!validateName(name)) {
            throw new IllegalArgumentException("navigator name cannot be an empty string");
        }
        return mNavigators.put(name, navigator);
    }

    /**
     * 检验合法性
     */
    private boolean validateName(String name) {
        return name != null && !name.isEmpty();
    }
}
