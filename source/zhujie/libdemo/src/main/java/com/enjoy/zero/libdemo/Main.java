package com.enjoy.zero.libdemo;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class Main {

    public static void main(String[] args) {
        //拍电影的公司 客户端
        //创建一个真实对象
        Qiangge qiangge = new Qiangge();
        // 创建一个代理对象  宋代理
        //Songdaili songdaili = new Songdaili(qiangge);
        //songdaili.film();
        //生成动态代理对象 Proxy
        /**
         * classLoader 加载class文件
         * Class<?>[]  需要实现的业务接口
         * InvocationHandler  调用真实对象业务方法的地方
         */
        Iplayer proxy = (Iplayer) Proxy.newProxyInstance(qiangge.getClass().getClassLoader(),
                new Class[]{Iplayer.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("经纪人公司派小张 帮强哥 处理签合同的业务");
                        qiangge.film();
                        return null;
                    }
                });
        generyProxyFile();
        proxy.film();

    }

    /**
     * 生成代理类文件
     */
    public static void generyProxyFile() {
        byte[] classFile = ProxyGenerator.generateProxyClass("iplay", Qiangge.class.getInterfaces());
        String path = "./iplay.class";
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
