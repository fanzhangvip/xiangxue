package com.enjoy.zero.libdemo;

import java.io.FileOutputStream;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

public class Main {


    /**
     * 客户端  拍电影的公司
     * @param args
     */
    public static void main(String[] args) {



//        IPlayer songmou = new Songmou();
//        songmou.film();

        IPlayer mouqiangge = new MouQiangge();

        //第一步 构造动态代理
        DynamicProxy proxy = new DynamicProxy(mouqiangge);

        //经纪人小张
        IPlayer xiaozhang = (IPlayer) Proxy
                 .newProxyInstance
                         (IPlayer.class.getClassLoader(),
                                 new Class[]{IPlayer.class},proxy);
        generyProxyFile();
        xiaozhang.film();





    }




    /**
     * 生成代理类文件
     */
    public static void generyProxyFile() {
        byte[] classFile = ProxyGenerator.generateProxyClass("Proxy0", MouQiangge.class.getInterfaces());
        String path = "./Proxy0.class";
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
}
