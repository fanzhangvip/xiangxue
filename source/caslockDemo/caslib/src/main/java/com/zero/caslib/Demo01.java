package com.zero.caslib;

/**
 * 演示并发问题
 */
public class Demo01 {

    volatile int i = 0;

    public void incr(){
        i++;//加1操作
    }

    public static void main(String ... args) throws Exception{
        Demo01 demo01 = new Demo01();

        for (int j = 0; j < 2; j++) {//new两个线程
            new Thread(() ->{
                for (int k = 0; k < 10000; k++) {//每个线程执行10000次
                    demo01.incr();
                }
            }).start();
        }
        Thread.sleep(1000L);
        System.out.println(demo01.i);//打印结果是20000呢？
    }
}
