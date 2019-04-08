package com.enjoy.zero.libdemo;

/**
 * 1. 真实主题类
 * 2. 实现我们的具体的业务
 */
public class Actor implements IPlayer {

    private String name;

    public Actor(String name){
        this.name = name;
    }

    @Override
    public void film() {
        System.out.println(name + "正在拍某某电影。。。");
    }
}
