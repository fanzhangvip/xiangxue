package com.enjoy02.lib;

import com.enjoy02.lib.arch.Observable;
import com.enjoy02.lib.arch.Observer;

public class Mouse implements Observer {

    private String name;

    public Mouse(String name){
        this.name = name;
        System.out.println("我是"+ name+",我现在在偷粮食。。。");
    }

    @Override
    public void update(Observable observable) {
        runAway();
    }

    /**
     * 老鼠逃跑了
     */
    public void runAway(){
        System.out.println("我是"+ name+",猫醒了，我要赶紧逃。。。");
    }
}
