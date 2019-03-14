package com.enjoy.zero.libproxy;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 【享学课堂】 King老师
 * 手写操作原子性
 */
public class DemoLock {
    volatile int i=0;
    Lock lock = new ReentrantLock();
    //多线程调用
    public void incr(){
        lock.lock();//获取锁(一直等待，知道获取数据)
        try{
            i++;
        }finally {
            lock.unlock();//释放锁
        }
    }
    public static void main(String[] args) throws Exception{
        DemoLock lockDemo = new DemoLock();
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