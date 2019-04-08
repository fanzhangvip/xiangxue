package com.enjoy.zero.libdemo;

/*
 1. 抽象主题
 2. 定义了真实主题类(被委托类，被代理类)与代理类(委托类)的共同的业务接口
 */
public interface IPlayer {
    void film();//拍电影的业务
}

//public abstract class IPlayer{}//1. 为什么？
