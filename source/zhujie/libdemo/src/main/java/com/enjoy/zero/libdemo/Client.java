package com.enjoy.zero.libdemo;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

/**
 * 代理模式的客户端
 */
public class Client {

    /**
     * 生成代理类文件
     */
    public static void generyProxyFile(String fileName,Class<?> clazz) {
        byte[] classFile = ProxyGenerator.generateProxyClass(fileName, clazz.getInterfaces());
        String path = "./"+fileName+".class";
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(classFile);
            fos.flush();
            System.out.println("代理类class文件写入成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("写入出错类");
        }
    }

    public static void main(String ... args){

        //静态代理
        Actor qiangge = new Actor("强哥");

//        //构造一个代理类
//        Manager mrSong = new Manager("宋先生",qiangge);
//
//        //访问真实业务
//        mrSong.film();

        //动态代理
        //动态生成代理对象
        /**
         * ClassLoader :加载类class文件，与加载真实主题类的classloader是同一个
         * Class<?>[] :需要实现的共同的业务接口
         * InvocationHandler:调用真实主题类的具体业务
         */
        IPlayer xiaozhang = (IPlayer) Proxy.newProxyInstance(qiangge.getClass().getClassLoader(),
                new Class[]{IPlayer.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        System.out.println("小张在洽谈业务。。。");
                        qiangge.film();
                        return null;
                    }
                });
        generyProxyFile("xiaozhang",qiangge.getClass());
        xiaozhang.film();

    }
}
