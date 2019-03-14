package com.enjoy.zero.libproxy;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author 【享学课堂】 King老师
 * 带你一起写Lock实现
 */
public class KingLock implements Lock {
    //定义一个线程标志
    //Thread thread = null;  ---1---
    AtomicReference<Thread> owner = new AtomicReference<>(); //* ---2---
    //获取到一把锁
    //集合---存储正在等待的线程
    public LinkedBlockingQueue<Thread> waiter = new LinkedBlockingQueue<>();//* ---3---
    @Override
    public void lock() {
        //如果当前线程拿到了,就完了，没有拿到，则需要加入等待
        while(!owner.compareAndSet(null,Thread.currentThread())){  //* ---2---
            //没拿到锁，需要等待，等待其他线程释放锁
            waiter.add(Thread.currentThread());  //---3---
            LockSupport.park();//等待，让你线程等待(等待的意思就是没有没唤醒一直会卡在这里) //---3---
            waiter.remove(Thread.currentThread());//如果能执行到这里，证明被唤醒了，所以就需要从等待列表中删除//---3---
        }
        /* ---1---
        if(thread ==null){//锁没有人拿
            thread = Thread.currentThread();//当前线程拿到这把锁
        }else{
            //等待其他线程释放
        }
        */
    }
    @Override
    public void unlock() {
        if(owner.compareAndSet(Thread.currentThread(),null)){ //* ---2---
            //释放锁，告知其他线程，你们可以拿锁了(唤醒所有等待其他线程)
            Object[] objects = waiter.toArray();//---3---
            for(Object object:objects){//遍历， 拿出所有等待的线程  ---3---
                Thread next = (Thread)object;//---3---
                LockSupport.unpark(next);//唤醒线程   ---3---
            }
        }
        /*---1---
        if(Thread.currentThread().equals(thread)){
            thread = null;
        }
        */
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }
}