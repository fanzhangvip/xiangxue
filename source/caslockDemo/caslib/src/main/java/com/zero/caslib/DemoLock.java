package com.zero.caslib;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DemoLock {


    int i = 0;
    int j = 0;

    //弄一把锁出来
    final Lock lock = new ZLock();

    public void incr() {//多线程调用
        try {
            System.out.println("lock调用前 i=: " + i +" => "+ Thread.currentThread());
            lock.lock();//获取锁
            System.out.println("lock调用后 i=: " + i+" => "+ Thread.currentThread());
            i++;
//            Thread.sleep(1000);
            j++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("unlock调用前 i= : " + i+" => "+ Thread.currentThread());
            lock.unlock();//释放锁
            System.out.println("unlock调用后 i= : " + i+" => "+ Thread.currentThread());
        }

    }

    public static void main(String... args) throws Exception {
        DemoLock demoLock = new DemoLock();

        for (int j = 0; j < 2; j++) {//new两个线程

            Thread t = new Thread("thread:" + j) {
                public void run() {
                    for (int k = 0; k < 5; k++) {//每个线程执行10000次
                        demoLock.incr();
                    }

                }
            };
            t.start();
        }
        Thread.sleep(1000L);
        System.out.println(demoLock.i);//打印结果是20000呢？
        System.out.println(demoLock.j);
    }

}
