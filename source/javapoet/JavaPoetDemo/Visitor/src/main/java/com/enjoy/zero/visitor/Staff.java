package com.enjoy.zero.visitor;

import java.util.Random;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明: 员工基类  -> Element 定义了一个accept方法，指明每一个元素都要可以被访问者访问
 */
public abstract class Staff {

    public String name;

    public float kpi;//员工kpi


    public Staff(String name){
        this.name = name;
        kpi = new Random().nextFloat();
    }

    /**
     * 接受Visitor的访问
     * @param visitor
     */
    public abstract void accept(Visitor visitor);
}
