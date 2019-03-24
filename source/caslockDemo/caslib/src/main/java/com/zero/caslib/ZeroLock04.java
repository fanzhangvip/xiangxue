package com.zero.caslib;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZeroLock04 implements Lock {

    //定义一个线程13号
    Thread thread = null;

    //加锁的方法
    @Override
    public void lock() {
        if(thread == null){//如果13号没有被人抢到
            thread = Thread.currentThread();//当前线程赋给13号
        }else{
            //加入等待列表

        }
    }

    @Override
    public void unlock() {
        //解锁的线程是不是拿到了13号技师的线程
        if(Thread.currentThread().equals(thread)){
            thread = null;//这里问同学们有没有什么问题
            // 没有确保线程的CAS
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
