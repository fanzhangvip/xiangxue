package com.enjoy.zero.libproxy;
/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明: “资深”挤奶工牛耿，他需要诉讼老板，但是他不知道具体该怎么做
 */
public class NiuGeng implements ILawsuit {
    @Override
    public void submit() {
        System.out.println("老板拖欠工资，特此申请仲裁");
    }

    @Override
    public void burden() {
        System.out.println("2017年共计上班了365天，才发了5个月工资");
    }

    @Override
    public void defend() {
        System.out.println("证据确凿，没什么好说的");
    }

    @Override
    public void finish() {
        System.out.println("诉讼成功，判决老板立马结算工资");
    }
}
