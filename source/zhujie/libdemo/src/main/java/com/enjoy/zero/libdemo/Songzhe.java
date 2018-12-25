package com.enjoy.zero.libdemo;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 *  代理类，委托类
 */
public class Songzhe implements IPlayer {

    Qiangge mQiangge;

    public Songzhe(Qiangge qiangge){
        mQiangge = qiangge;
    }

    @Override
    public void film() {

        System.out.println("谈合同，定什么时间 什么地点拍， 通知强哥");
        mQiangge.film();
    }
}
