package com.enjoy02.lib;

import com.enjoy02.lib.arch.Observable;

public class Cat extends Observable {

    private String name;

    public Cat(String name){
        this.name = name;
        System.out.println("我是"+ name+",我正在睡觉。。。");
    }

    /**
     * 猫叫
     */
    public void Meow(){
        System.out.println("猫爷我醒了，发出一声‘喵~~~’");
        notifyObservers();
    }
}
