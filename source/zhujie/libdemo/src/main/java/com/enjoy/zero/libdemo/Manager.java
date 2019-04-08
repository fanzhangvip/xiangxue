package com.enjoy.zero.libdemo;

/**
 * 1. 代理类(委托类)
 * 2. 实现共同的业务接口，让我们的客户端能够通过这个代理类的
 * 业务接口间接地访问真实主题的业务
 * 3. 持有一个真实主题类的引用
 */
public class Manager  implements IPlayer{

    private String name;

    private Actor actor;

    public Manager(String name,Actor actor){
        this.name = name;
        this.actor = actor;
    }

    @Override
    public void film() {
        System.out.println(name +"洽谈业务，约定拍电影的时间地点 价格。。。");
        //客户端间接访问真实业务
        actor.film();
    }
}
