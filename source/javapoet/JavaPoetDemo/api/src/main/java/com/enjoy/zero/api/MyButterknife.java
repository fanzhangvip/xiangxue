package com.enjoy.zero.api;

import android.app.Activity;

import java.util.LinkedHashMap;
import java.util.Map;


//public class Sample         // TypeElement
//        <T extends List> {  // TypeParameterElement
//
//    private int num;        // VariableElement
//    String name;            // VariableElement
//
//    public Sample() {}      // ExecuteableElement
//
//    public void setName(    // ExecuteableElement
//                            String name     // VariableElement
//    ) {}
//}
public class MyButterknife {
    private static final ActivityViewFinder activityFinder = new ActivityViewFinder();//默认声明一个Activity View查找器
    private static final Map<String, ViewBinder> binderMap = new LinkedHashMap<>();//管理保持管理者Map集合

    /**
     * Activity注解绑定 ActivityViewFinder
     *
     * @param activity
     */
    public static void bind(Activity activity) {
        bind(activity, activity, activityFinder);
    }

    /**
     * '注解绑定
     *
     * @param host   表示注解 View 变量所在的类，也就是注解类 进行绑定的目标对象
     * @param object 表示查找 View 的地方，Activity & View 自身就可以查找，Fragment 需要在自己的 itemView 中查找
     * @param finder ui绑定提供者接口 这个用来统一处理Activity、View、Dialog等查找 View 和 Context 的方法
     */
    private static void bind(Object host, Object object, ViewFinder finder) {
        //获取注解使用类的 类名
        String className = host.getClass().getName();
        try {
            //看下对应的ViewBinder是否存在
            ViewBinder binder = binderMap.get(className);
            if (binder == null) {
                //不存在则通过反射创建一个 然后存入缓存 这个类是通过javapoet生成的
                Class aClass = Class.forName(className + "$ViewBinder");
                binder = (ViewBinder) aClass.newInstance();
                binderMap.put(className, binder);
            }
            if (binder != null) {
                //把finder类跟使用注解类的 类 绑定
                binder.bindView(host, object, finder);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除注解绑定 ActivityViewFinder
     *
     * @param host
     */
    public static void unBind(Object host) {
        String className = host.getClass().getName();
        ViewBinder binder = binderMap.get(className);
        if (binder != null) {
            binder.unBindView(host);
        }
        binderMap.remove(className);
    }
}
