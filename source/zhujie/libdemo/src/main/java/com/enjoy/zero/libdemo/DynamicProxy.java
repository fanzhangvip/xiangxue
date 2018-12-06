package com.enjoy.zero.libdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler {


    private Object targe; //被代理类 某强哥

    public DynamicProxy(IPlayer player){
        targe = player;
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        System.out.println("现在经纪人指派的小张 帮强哥 谈拍电影业务");
        Object result = method.invoke(targe,objects);
        return result;
    }
}
