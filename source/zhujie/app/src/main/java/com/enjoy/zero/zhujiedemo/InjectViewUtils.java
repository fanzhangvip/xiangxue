package com.enjoy.zero.zhujiedemo;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class InjectViewUtils {

    public static void injectView(Activity activity) {
        if (null == activity) return;

        Class<? extends Activity> activityClass = activity.getClass();
        Field[] declaredFields = activityClass.getDeclaredFields();
        for (Field field : declaredFields) {//获取Activity类里面声明的所有成员变量
            if (field.isAnnotationPresent(InjectView.class)) {//找出标注了@InjectView的成员变量
                //解析InjectView 获取button id
                InjectView injectView = field.getAnnotation(InjectView.class);
                int value = injectView.value();//获取变量的值，也就是控件的id

                try {
                    //找到findViewById方法
                    Method findViewById = activityClass.getMethod("findViewById", int.class);
                    findViewById.setAccessible(true);
                    Object view = findViewById.invoke(activity,value);
                    field.set(activity,view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void injectEvent(Activity activity) {
        if (null == activity) {
            return;
        }
        Class<? extends Activity> activityClass = activity.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Onclick.class)) {
                Log.i("Zero", method.getName());
                Onclick onclick = method.getAnnotation(Onclick.class);
                //get button id
                int[] value = onclick.value();
                //get Event
                Event event = onclick.annotationType().getAnnotation(Event.class);
                Class listener = event.listener();
                String  listenerName= event.setOnClickListener();
                String methodName = event.methodName();

                //创建InvocationHandler和动态代理(在这里就是处理onClick点击事件)
                ProxyHandler proxyHandler = new ProxyHandler(activity);
                proxyHandler.setMethod(methodName,method);
                try {
                    Object proxyInstance = Proxy.newProxyInstance(listener.getClassLoader(), new Class[]{listener}, proxyHandler);
                    for (int id : value) {
                        //找到Button
                        Method findViewById = activityClass.getMethod("findViewById", int.class);
                        findViewById.setAccessible(true);
                        View btn = (View) findViewById.invoke(activity, id);
                        //根据listener方法名和event方法参数找到method
                        Method setOnClickListener = btn.getClass().getMethod(listenerName, listener);
                        setOnClickListener.setAccessible(true);
                        setOnClickListener.invoke(btn, proxyInstance);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
