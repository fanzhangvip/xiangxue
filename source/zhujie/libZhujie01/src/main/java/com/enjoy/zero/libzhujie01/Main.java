package com.enjoy.zero.libzhujie01;

import java.io.FileFilter;
import java.lang.reflect.AnnotatedElement;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
@Test
public class Main {

    Runnable r1 = () -> { System.out.println(this); };
    Runnable r2 = () -> { System.out.println(toString()); };
    @Test
    public String toString() {  return "Hello, world"; }

    @Test
    public static void main(@Test String[] args) {
        System.out.println(" Hello world");

        @Test
        Apple apple = new Apple();
        FuitTools.getFruitInfo(Apple.class);

    }

}
