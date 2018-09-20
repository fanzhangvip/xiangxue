package com.zero.xiangxue.handlerdemo;

public final class MyLooper {


    static final ThreadLocal<MyLooper> sThreadLocal = new ThreadLocal<>();
    public MyMessageQueue mQueue;

    public MyLooper() {
        mQueue = new MyMessageQueue();
    }

    /**
     * 实例化一个属于当前线程的looper对象
     */
    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one MyLooper may be created per thread");
        }
        sThreadLocal.set(new MyLooper());
    }

    public static MyLooper myLooper() {
        return sThreadLocal.get();
    }

    /**
     * 轮询消息队列
     */
    public static void loop() {
        MyLooper me = myLooper();
        MyMessageQueue queue = me.mQueue;
        //轮询
        MyMessage msg;
        for (; ; ) {
            msg = queue.next();
            //获取到发送消息的 msg.target （handler）本身，然后分发消息
            if (msg == null || msg.target == null) {
                continue;
            }

            msg.target.dispatchMessage(msg);

        }
    }
}
