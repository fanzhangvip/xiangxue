package com.enjoy.zero.libzhujie01;

import java.lang.annotation.Documented;
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
 * 类说明:水果生产工厂
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitProducerFactory {

    /**
     * 水果商品编号
     */
    public int id() default  -1;

    /**
     * 水果工厂名称
     */
    public String name() default "";

    /**
     * 水果工厂地址
     */
    public String address() default "";

//    Apple value();



}
