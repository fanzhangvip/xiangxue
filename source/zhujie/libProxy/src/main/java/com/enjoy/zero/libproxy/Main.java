package com.enjoy.zero.libproxy;

import net.sf.cglib.proxy.Enhancer;

import java.io.FileOutputStream;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class Main {

    public static void main(String[] args) {
        //构造一个 牛耿
        ILawsuit niugeng = new NiuGeng();
        // 构造一个代理律师，并将牛耿作为构造参数传递进去
        ILawsuit lawyer = new XXLayer(niugeng);

        //律师提交诉讼材料
        lawyer.submit();
        //律师进行举证
        lawyer.burden();
        //律师代替牛耿辩护
        lawyer.defend();
        //完成诉讼
        lawyer.finish();

        System.out.println("====动态代理============");
        dynamicProxyTest();
        System.out.println("====cglib动态代理=========");
//        cglibProxy();
    }

    public static void dynamicProxyTest() {
        ILawsuit niugeng = new NiuGeng();


        //构造一个动态代理
        DynamicProxy proxy = new DynamicProxy(niugeng);

        //动态构建一个代理者律师
        /**
         * loader 当前目标对象使用的类加载器
         * interfaces 目标对象实现的接口的类型
         * invocationHandler 动态处理器
         */
        ILawsuit lawyer = (ILawsuit) Proxy.newProxyInstance(ILawsuit.class.getClassLoader(), new Class[]{ILawsuit.class}, proxy);

//        generyProxyFile();

        //律师提交诉讼材料
        lawyer.submit();
        //律师进行举证
        lawyer.burden();
        //律师代替牛耿辩护
        lawyer.defend();
        //完成诉讼
        lawyer.finish();

    }


    public static void cglibProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ILawsuit.class);
        enhancer.setCallback(new CglibProxy());
        ILawsuit lawyer = (ILawsuit) enhancer.create();
        //律师提交诉讼材料
        lawyer.submit();
        //律师进行举证
        lawyer.burden();
        //律师代替牛耿辩护
        lawyer.defend();
        //完成诉讼
        lawyer.finish();
    }


}
