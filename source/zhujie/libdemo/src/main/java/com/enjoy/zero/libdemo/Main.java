package com.enjoy.zero.libdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

        //客户端
        //静态代理模式
        //构建我们的被代理类
        Qiangge qiangge = new Qiangge();

//        //构建我们的代理类
//        Songzhe songzhe = new Songzhe(qiangge);
//
//        //执行拍电影业务
//        songzhe.film();

        // Proxy 经纪人公司 临时指派 小张
        /**
         * ClassLoader : 加载class文件，和加载被代理类的classloader是同一个
         * Class<?>[]:  需要实现的业务接口
         * InvocationHandler：调用被代理类的真实业务的
         */
        IPlayer proxy = (IPlayer) Proxy.newProxyInstance(qiangge.getClass().getClassLoader(),
                new Class[]{IPlayer.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("经纪人公司帮忙处理拍电影的业务洽谈工作");
                        qiangge.film();
                        return null;
                    }
                });

        proxy.film();

    }
}
