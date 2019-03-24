package com.zero.caslib;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class ZeroLock05 implements Lock {//非公平的，可能AV老师又偷偷请抢啦

    //定义一个线程13号
//    Thread thread = null;
    AtomicReference<Thread> thech13 = new AtomicReference<>();//拿到啦一个针对线程操作的原子操作类（13号技师）

    //定义一个等待列表,没抢到13的Lance老师 我们给他一个小板凳
    //用于存放没有抢到13号的业务线程
    public LinkedBlockingQueue<Thread> waiter = new LinkedBlockingQueue<>();


    //加锁的方法
    @Override
    public void lock() {
        /*
        if(thread == null){//如果13号没有被人抢到
            thread = Thread.currentThread();//当前线程赋给13号
        }else{
            //加入等待列表

        }
        */
        //我们要去拿锁啦，CAS机制，比较和交换(拿到13号技师的这把锁)
//        thech13.compareAndSet(null,Thread.currentThread());//可以确保原子性 ---1

        while (!thech13.compareAndSet(null, Thread.currentThread())) {//如果拿不到13号技师 该怎么办？
            waiter.add(Thread.currentThread());//拿不到没关系，给你一条小板凳 加入等待列表
            LockSupport.park();//等待，让当前线程等待(等待的意思就是没有被唤醒，就会一直卡在这里，就跟你打断点一样，就会一直卡在这)

            //被唤醒了，怎么办(先预留在这里，这段代码肯定是还没写完的)(代码执行到这行，证明被唤醒了)
            //一定要记得从等待列表里面删除
            waiter.remove(Thread.currentThread());//这行是预留的

        }


    }

    //解锁的方式
    @Override
    public void unlock() {
        /*
        //解锁的线程是不是拿到了13号技师的线程
        if(Thread.currentThread().equals(thread)){
            thread = null;//这里问同学们有没有什么问题
            // 没有确保线程的CAS
        }
        */

        //是否锁的线程是不是拿到了13号技师的线程(Allen老师老在靠破坏，AV 老师怎么还不出来，allen他在一直掉释放锁的方法)
        //必须判断多线程情况下，必须使用CAS机制，才行
        if (thech13.compareAndSet(Thread.currentThread(), null)) {//如果是当前线程拿到了13号，就把13号设置为空
            //把这些等待13号技师的狼放出来
            Object[] objects = waiter.toArray();//把列表转成数组

            for (Object object : objects) {//遍历，拿出所有等待的线程
                Thread next = (Thread)object;
                LockSupport.unpark(next);//使用工具，唤醒线程 拿跟棒子 把他们都敲醒，
                //同学们，到这里是不是就写完了，认为写完了的Q1，没写完的Q2
            }

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


    @Override
    public Condition newCondition() {
        return null;
    }
}
