package com.zero.xiangxue.handlerdemo;


public class MyHandler {
    private MyLooper mLooper;
    private MyMessageQueue mQueue;

    public MyHandler() {
        mLooper = MyLooper.myLooper();
        mQueue = mLooper.mQueue;
    }

    public void sendMessage(MyMessage message) {
        message.target = this;
        mQueue.enqueueMessage(message);
    }

    /**
     * 子类处理消息
     *
     * @param message
     */
    public void handleMessage(MyMessage message) {

    }

    /**
     * 分发消息
     *
     * @param message
     */
    public void dispatchMessage(MyMessage message) {
        handleMessage(message);
    }
}
