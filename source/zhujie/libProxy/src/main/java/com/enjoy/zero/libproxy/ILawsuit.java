package com.enjoy.zero.libproxy;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明: 诉讼流程接口
 */
public interface ILawsuit {

    /**
     * 提交材料
     */
    void submit();

    /**
     * 举证
     */
    void burden();

    /**
     * 辩护
     */
    void defend();

    /**
     * 完成
     */
    void finish();
}
