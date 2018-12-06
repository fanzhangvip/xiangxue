package com.enjoy.zero.visitor;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明: CEO 只关心kpi
 */
public class CEOVisitor implements Visitor {
    @Override
    public void visit(Engineer staff) {
        System.out.println("工程师："+ staff.name + ", kpi: " + staff.kpi);
    }

    @Override
    public void visit(Manager staff) {
        System.out.println("经理："+ staff.name + ", kpi: " + staff.kpi);
    }
}
