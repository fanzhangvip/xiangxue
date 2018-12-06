package com.enjoy.zero.visitor;

import java.util.Random;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明: 经理类型 -> ElementB
 */
public class Manager extends Staff {


    public Manager(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * @return 返回产品数量
     */
    public int getProducts(){
        return  new Random().nextInt();
    }
}
