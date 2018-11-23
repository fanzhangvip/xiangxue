package com.enjoy.zero.libproxy;
/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class Main {

    public static void main(String[] args) {
        //构造一个 牛耿
        ILawsuit niugeng = new NiuGeng();
        // 构造一个代理律师，并将牛耿作为构造参数传递进去
        ILawsuit layer = new XXLayer(niugeng);

        //律师提交诉讼材料
        layer.submit();
        //律师进行举证
        layer.burden();
        //律师代替牛耿辩护
        layer.defend();
        //完成诉讼
        layer.finish();
    }
}
