package com.zero.xiangxue.handlerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.UUID;

/**
 * Created by wanghuajun on 2018/9/19.
 */

public class HandlerDemoActivity extends Activity {

    private static final String TAG = "HandlerDemo";

    private static final int FLASH_UI = 1 << 0;

    private Handler mHandler = null;

    private Handler myThreadHandler = null;
    private MyThread myThread = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        customHandlerTest();
        Log.i(TAG, "onCreate 当前线程： " + Thread.currentThread().getName());
        setContentView(R.layout.activity_handler_demo);

        mHandler = new Handler();

    }

    public void handlerUsageOne(View view) {
        //handler用法一
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                try {
//                    //模拟耗时操作
//                    Thread.sleep(60 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                Log.i(TAG, "handler用法1 Runnable.run 当前线程： " + Thread.currentThread().getName());
            }
        });
    }

    public void handlerUsageTwo(View view) {

        Log.i(TAG, "handler用法2 handlerUsageTwo ： " + Thread.currentThread().getName());
        final MyHandler1 myHandler1 = new MyHandler1();
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "handler用法2 Runnable.run 当前线程： " + Thread.currentThread().getName());
//                try {
//                    //模拟耗时操作
//                    Thread.sleep(60 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

//                MyHandler1 myHandler1 = new MyHandler1();
                Message msg = myHandler1.obtainMessage(FLASH_UI);
                myHandler1.sendMessage(msg);
            }
        };


    }

    public void handlerUsageThree(View view) {

        myThread = new MyThread();
        myThread.start();

        myThreadHandler.sendEmptyMessage(0);

    }

    class MyThread extends Thread {
        public void run() {
            Log.d(TAG, "Thread[{0}]-- run:" + Thread.currentThread().getName()); // 其它线程中新建一个handler
            Looper.prepare();
            myThreadHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    Log.d(TAG, "Thread[{0}]--myThreadHandler handleMessage run..." + Thread.currentThread().getName());
                }
            };
            Looper.myLooper().loop();//建立一个消息循环，该线程不会退出
        }
    }

    class MyHandler1 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLASH_UI:
                    Log.i(TAG, "handler用法2 handleMessage： " + Thread.currentThread().getName());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void customHandlerTest() {
        MyLooper.prepare();
        final MyHandler handler = new MyHandler() {
            @Override
            public void handleMessage(MyMessage message) {
                Log.i(TAG, "main thread recv message------" + message.obj.toString());
            }
        };

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyMessage msg = new MyMessage();
                    synchronized (UUID.class) {
                        msg.obj = UUID.randomUUID().toString();
                    }
                    Log.i(TAG, "sup thread " + Thread.currentThread().getName() + ": send message------" + msg.obj);
                    handler.sendMessage(msg);
                }
            }).start();
        }
        MyLooper.loop();
    }
}
