package com.enjoy.zero.visitor;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明: 如果不用访问者模式 怎么写
 */
public class ReportUtils {

    /**
     * 只关心kpi
     * @param staff
     */
    public void ceovisitor(Staff staff){
        if(staff instanceof  Manager){
            Manager manager = (Manager) staff;

        }else{
            Engineer engineer = (Engineer) staff;
        }
    }

    /**
     * 关心经理的产品数量，工程师的代码行数
     * @param staff
     */
    public void ctovisitor(Staff staff){
        if(staff instanceof  Manager){
            Manager manager = (Manager) staff;

        }else{
            Engineer engineer = (Engineer) staff;
        }
    }
}
