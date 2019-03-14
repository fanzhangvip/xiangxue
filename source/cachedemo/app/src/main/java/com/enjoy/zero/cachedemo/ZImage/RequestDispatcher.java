package com.enjoy.zero.cachedemo.ZImage;

import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

public class RequestDispatcher extends Thread {

    /**
     * 从队列中转发请求需要持有队列的引用
     */
    private BlockingQueue<Request> blockingQueue;

    public RequestDispatcher(BlockingQueue<Request> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    /**
     * 阻塞式队列，转发器开启，从队列中取请求队列，如果没有则会阻塞当前线程，所以这里
     * 是在子线程开启的
     */
    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                Request request = blockingQueue.take();
                //处理请求对象，交给loader
                String schema = parseSchema(request.getImageUrl());
                //获取加载器
                Loader loader = LoaderManager.getInstance().getLoader(schema);
                if (loader == null){
                    Log.d("TAG",request.getImageUrl() + "没有找到对应的加载器");
                    return;
                }
                loader.load(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据图片url判断加载类型
     * @param imageUrl
     * @return
     */
    private String parseSchema(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)){
            return null;
        }
        if (imageUrl.contains("://")){
            return imageUrl.split("://")[0];
        }else{
            Log.d("TAG","不持支的图片类型");
        }
        return null;
    }
}
