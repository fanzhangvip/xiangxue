package com.zero.caslib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import sun.misc.Unsafe;

public class UnsafeTest {

    interface Counter {
        void increment();
        long getCounter();
    }

    static class CounterClient implements Runnable {
        private Counter c;
        private int num;

        public CounterClient(Counter c, int num) {
            this.c = c;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                c.increment();
            }
        }
    }

    static class SyncCounter implements Counter {
        private long counter = 0;

        @Override
        public synchronized void increment() {
            counter++;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class AtomicCounter implements Counter {
        AtomicLong counter = new AtomicLong(0);

        @Override
        public void increment() {
            counter.incrementAndGet();
        }

        @Override
        public long getCounter() {
            return counter.get();
        }
    }

    static class LockCounter implements Counter {
        private long counter = 0;
        private ReentrantReadWriteLock.WriteLock lock = new ReentrantReadWriteLock().writeLock();

        @Override
        public void increment() {
            lock.lock();
            counter++;
            lock.unlock();
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class CASCounter implements Counter {
        private volatile long counter = 0;
        private Unsafe unsafe;
        private long offset;

        public CASCounter() throws Exception {
            unsafe = Unsafes.getUnsafe();
            offset = unsafe.objectFieldOffset(CASCounter.class.getDeclaredField("counter"));
        }

        @Override
        public void increment() {
            long before = counter;
            while (!unsafe.compareAndSwapLong(this, offset, before, before + 1)) {
                before = counter;
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }


    static int NUM_OF_THREADS = 1000;
    static int NUM_OF_INCREMENTS = 10000;

    public static void main(String ...args){

        ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
        Counter counter = null; // creating instance of specific counter
        try {
            counter = new CASCounter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long before = System.currentTimeMillis();
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
        }
        service.shutdown();
        try {
            service.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long after = System.currentTimeMillis();
        System.out.println("Counter result: " + counter.getCounter());
        System.out.println("Time passed in ms:" + (after - before));
    }
}
