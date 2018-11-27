package com.enjoy.zero.zhujiedemo;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class ProxyHandler implements InvocationHandler {

    private Object mTarget;

    private HashMap<String,Method> methodHashMap = new HashMap<>();

    public ProxyHandler(Object target) {
        this.mTarget = target;
    }

    public void setMethod(String methodName,Method method){
        methodHashMap.put(methodName,method);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i("Zero", "method name = " + method.getName() + " and args = " + Arrays.toString(args));
        Method realMethod = methodHashMap.get(method.getName());
        if(realMethod!=null){
            return  realMethod.invoke(mTarget,args);
        }
        return null;
    }
}
