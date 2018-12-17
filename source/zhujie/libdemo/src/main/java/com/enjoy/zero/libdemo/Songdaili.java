package com.enjoy.zero.libdemo;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class Songdaili implements Iplayer {
    //真实对象
    Qiangge mQiange;

    public Songdaili(Qiangge qiangge){
        mQiange = qiangge;
    }

    @Override
    public void film() {

        System.out.println("宋代理先跟公司谈好价格 约定时间地点");
        //真正的拍电影
        mQiange.film();
    }
}
