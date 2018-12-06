package com.enjoy.zero.visitor;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明: 客户端
 */
public class MyClass {

    public static void main(String[] args) {

        //构建报表
        Report report = new Report();
        System.out.println("==============给CEO看的报表=========================");
        report.showReport(new CEOVisitor());
        System.out.println("==============给CTO看的报表=========================");
        report.showReport(new CTOVisitor());
    }
}
