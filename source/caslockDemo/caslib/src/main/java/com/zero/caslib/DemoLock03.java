package com.zero.caslib;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DemoLock03 {
     int i=0;
    //演示完后再演示两个变量
    int j = 0;
//    final Lock lock = new ReentrantLock();//引入lock锁
    final Lock lock = new ZeroLock05();//验证一下
    //多线程调用
    public void incr(){//incr方法需要进行原子性操作
        lock.lock();//获取锁(一直等待，知道获取数据)
        try{
            i++;
            j++;
        }finally {
            lock.unlock();//释放锁
        }
    }
    public static void main(String[] args) throws Exception{
        DemoLock03 lockDemo = new DemoLock03();
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
        System.out.println(lockDemo.j);
    }

}