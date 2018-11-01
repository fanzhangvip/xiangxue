import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoOfLatch {

    // 利用闭锁 CountDownLatch 控制主线程和子线程的同步
    public static void main(String[] args) {

        int numberOfThread = 5;
        final CountDownLatch startLatch = new CountDownLatch(1);                // 用于控制子线程开始
        final CountDownLatch stopLatch = new CountDownLatch(numberOfThread);    // 用于子线程计数
        final AtomicInteger count = new AtomicInteger(0);                       // 用于分配子线程唯一标识

        System.out.println("Main thread start...");

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    int tid = count.getAndIncrement();
                    try {
                        // 等代主线程打开启动信号
                        startLatch.await();
                        System.out.printf("Thread %d started...%n", tid);
                        int duration = (int) (Math.random() * 5000);
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    } finally {
                        System.out.printf("Thread %d stoped...%n", tid);
                        // 线程终止前减少线程计数
                        stopLatch.countDown();
                    }
                }

            });
            thread.start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread do preparation work for child threads...");
        try {

            // 打开闭锁放行所有子线程
            System.out.println("Main thread let child threads go...");
            startLatch.countDown();

            try {
                // 等待子线程计数降为 0 即所有子线程执行完毕
                System.out.println("Main thread wait for all child threads...");
                stopLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Main thread exit...");
        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }


}