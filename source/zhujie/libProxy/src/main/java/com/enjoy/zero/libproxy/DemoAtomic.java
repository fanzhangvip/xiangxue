package com.enjoy.zero.libproxy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 【享学课堂】 King老师
 * 多线程操作(Atomic原子封装类)
 */
public class DemoAtomic {
    //volatile int i=0;
    AtomicInteger i = new AtomicInteger(0);
    public void incr(){
        i.incrementAndGet();//对变量进行加1操作，并返回(这个是一个原子操作)
    }
    public static void main(String[] args) throws Exception{
        DemoAtomic lockDemo = new DemoAtomic();
        for(int j =0;j<2;j++){
            new Thread(() ->{
                for(int k =0;k<10000;k++){
                    lockDemo.incr();
                }
            }
            ).start();
        }
        Thread.sleep(1000L);
        System.out.println(lockDemo.i);
    }

}