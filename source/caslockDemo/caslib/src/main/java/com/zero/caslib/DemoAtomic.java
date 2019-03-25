package com.zero.caslib;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoAtomic {

    AtomicInteger i = new AtomicInteger(0);//默认赋值

    public void incr(){
        //i++;
        i.incrementAndGet();//对我们的变量加1操作，并返回(原子操作)
    }

    public static void main(String ... args) throws Exception{
        DemoAtomic demoAtomic = new DemoAtomic();

        for (int j = 0; j < 2; j++) {//new两个线程
            new Thread(() ->{
                for (int k = 0; k < 10000; k++) {//每个线程执行10000次
                    demoAtomic.incr();
                }
            }).start();
        }
        Thread.sleep(1000L);
        System.out.println(demoAtomic.i);//打印结果是20000呢？
    }

}
