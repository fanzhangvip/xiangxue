package com.enjoy.zero.zhujiedemo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明: InjectView用于注入view，用来代替findViewById方法
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {
    int value();//value就是用来指定id，即findViewById的参数
}
