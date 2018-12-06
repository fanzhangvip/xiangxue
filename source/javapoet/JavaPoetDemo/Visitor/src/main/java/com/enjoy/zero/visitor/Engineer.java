package com.enjoy.zero.visitor;

import java.util.Random;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明: 工程师类型 -> ElementA
 */
public class Engineer extends Staff {


    public Engineer(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * @return 返回工程师写的代码行数
     */
    public int getCodeLines(){
        return  new Random().nextInt();
    }
}
