package com.enjoy.zero.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明: ObjectStructure
 */
public class Report {

    List<Staff> mStaffs = new ArrayList<>();

    public Report(){
        mStaffs.add(new Manager("经理-Zero"));
        mStaffs.add(new Manager("经理-AV"));

        mStaffs.add(new Engineer("工程师- Lison"));
        mStaffs.add(new Engineer("工程师- James"));
        mStaffs.add(new Engineer("工程师- Deer"));
        mStaffs.add(new Engineer("工程师- Peter"));

    }

    /**
     * 为访问者展示报表
     * @param visitor 公司高层 如 CEO,CTO
     */
    public void showReport(Visitor visitor){
        for(Staff staff:mStaffs){
            staff.accept(visitor);
        }
    }
}
