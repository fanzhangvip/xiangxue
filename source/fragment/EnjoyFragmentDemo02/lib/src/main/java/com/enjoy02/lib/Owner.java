package com.enjoy02.lib;

import com.enjoy02.lib.arch.Observable;
import com.enjoy02.lib.arch.Observer;

public class Owner implements Observer {

    private String name;

    public Owner(String name) {
        this.name = name;
        System.out.println("我是小主人:"+ name);
    }

    @Override
    public void update(Observable observable) {
        playWithCat();
    }

    public void playWithCat() {
        System.out.println(name + "准备去逗猫玩。。。");
    }
}
