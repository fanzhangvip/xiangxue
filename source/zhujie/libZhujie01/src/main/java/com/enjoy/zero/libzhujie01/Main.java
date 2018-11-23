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
public class Main {

    Runnable r1 = () -> { System.out.println(this); };
    Runnable r2 = () -> { System.out.println(toString()); };
    public String toString() {  return "Hello, world"; }

    public static void main(String[] args) {
        System.out.println(" Hello world");
        AnnotatedElement element;
        Apple apple = new Apple();
        FuitTools.getFruitInfo(Apple.class);

        FileFilter java = f -> f.getName().endsWith(".java");
        new Main().r1.run();
        new Main().r2.run();
    }

}
