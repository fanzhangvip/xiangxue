package com.enjoy02.lib;

import com.enjoy02.lib.arch.Observable;
import com.enjoy02.lib.arch.Observer;

public class Mouse implements Observer {

    private String name;

    @Override
    public void update(Observable observable) {

    }

    /**
     * 老鼠逃跑了
     */
    public void runAway(){
        System.out.println("我是"+ name+",猫醒了，我要赶紧逃。。。");
    }
}
