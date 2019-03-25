package com.zero.caslib;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class ZLock implements Lock {


//    Thread  thread = null;//定义一个线程 13号

    AtomicReference<Thread> thech13 = new AtomicReference<>();//拿到了一个针对线程操作的原子操作类（13号）

    //定义一个小板凳 等待列表
    LinkedBlockingQueue<Thread> waiter = new LinkedBlockingQueue<>();

    //加锁的方法
    @Override
    public void lock() {
        /*
        if(thread == null){//假设13号没有被人抢到
            thread =Thread.currentThread();//把当线程给了13号
        }else{

        }
        */
        AtomicInteger count = new AtomicInteger(0);
        Thread target = Thread.currentThread();
        while (!thech13.compareAndSet(null, target)) {
            int k =count.incrementAndGet();
            System.out.println("k = " + k);
            //证明抢输了
            waiter.add(target);
            System.out.println("LockSupport.park();前面：thread: " + Thread.currentThread() + " size: " + waiter.size());
            LockSupport.park();
            waiter.remove(target);
            System.out.println("LockSupport.park();后面：thread: " + Thread.currentThread() + " size: " + waiter.size());



        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }


    //
    @Override
    public void unlock() {
        /*
        if(Thread.currentThread().equals(thread)){
            thread = null;//释放13 ，把13让给我们的Lance Allen
        }
      */

        Thread target = Thread.currentThread();
        if (thech13.compareAndSet(target, null)) {//AV 服务完了 13 设置为空
            //把这些等待13号的狼 放出来
            Thread[] threads = new Thread[waiter.size()];
            waiter.toArray(threads);
            for (Thread next : threads) {
                LockSupport.unpark(next);
                System.out.println("LockSupport.unpark();：next: " + next);
//                waiter.remove(next);
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
