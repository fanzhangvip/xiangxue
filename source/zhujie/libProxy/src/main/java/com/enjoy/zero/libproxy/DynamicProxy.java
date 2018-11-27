package com.enjoy.zero.libproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:动态代理
 */
public class DynamicProxy implements InvocationHandler {

    private Object target; //被代理类的引用

    public DynamicProxy(Object o){
        this.target = o;
    }

    /**
     * @param proxy  动态代理对象｛XXLayer｝
     * @param method 代表正在执行的方法
     * @param args 代表当前执行方法传入的实参
     * @return 表示当前执行方法的返回值
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //调用被代理类对象的方法
        System.out.print("执行流程:  ");
        Object result = method.invoke(target,args);
        return result;
    }
}
