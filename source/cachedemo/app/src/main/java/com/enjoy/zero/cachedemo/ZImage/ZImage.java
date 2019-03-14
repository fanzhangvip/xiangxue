package com.enjoy.zero.cachedemo.ZImage;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ZImage {

    /**
     * 持有配置信息对象的引用
     */
    private ImageLoaderConfig config;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;


    private static volatile ZImage instance;


    private ZImage(ImageLoaderConfig config) {
        this.config = config;
        //初始化请求队列
        requestQueue = new RequestQueue(config.getThreadCount());
        //开启请求队列
        requestQueue.start();
    }

    public static ZImage init(ImageLoaderConfig config) {
        if (instance == null) {
            synchronized (ZImage.class) {
                if (instance == null) {
                    instance = new ZImage(config);
                }
            }
        }
        return instance;
    }

    public static ZImage getInstance() {
        if (instance == null) {
            throw new UnsupportedOperationException("instance = null,not init");
        }
        return instance;
    }


    /**
     * 显示图片
     *
     * @param imageView
     * @param url
     */
    public void display(ImageView imageView, String url) {
        display(imageView, url, null, null);
    }

    /**
     * 显示图片
     *
     * @param imageView
     * @param url
     */
    public void display(ImageView imageView, String url, DisplayConfig displayConfig) {
        display(imageView, url, displayConfig, null);
    }

    /**
     * 显示图片
     *
     * @param imageView
     * @param url
     */
    public void display(ImageView imageView, String url, ImageListener listener) {
        display(imageView, url, null, listener);
    }

    /**
     * 显示图片，用于针对特殊图片配置特殊的配置信息
     *
     * @param imageView
     * @param url
     * @param displayConfig
     * @param listener
     */
    public void display(ImageView imageView, String url, DisplayConfig displayConfig, ImageListener listener) {
        if (imageView == null) {
            throw new NullPointerException("ImageView cannot be null");
        }
        //封装成一个请求对象
        Request request = new BitmapRequest(imageView, url, displayConfig, listener);
        //加入请求队列
        requestQueue.addRequest(request);
    }

    /**
     * 监听图片，设置后期处理，仿Glide
     */
    public static interface ImageListener {
        void onComplete(ImageView imageView, Bitmap bitmap, String url);
    }

    /**
     * 获取全局配置
     *
     * @return
     */
    public ImageLoaderConfig getConfig() {
        return config;
    }

}
