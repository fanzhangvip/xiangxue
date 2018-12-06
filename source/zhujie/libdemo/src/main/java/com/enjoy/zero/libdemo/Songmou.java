package com.enjoy.zero.libdemo;

/**
 * 经纪人
 *
 * 代理类 委托类
 */
public class Songmou implements IPlayer {

    /**
     * 包含真实的代理类
     */
    private IPlayer mMouQiange = new MouQiangge();

    @Override
    public void film() {

        System.out.println("谈价格，场地 。。。。");
        mMouQiange.film();

    }
}
