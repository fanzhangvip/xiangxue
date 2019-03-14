package com.enjoy.zero.cachedemo.ZImage;

import android.widget.ImageView;

import java.lang.ref.SoftReference;

public class BitmapRequest implements Request, Comparable<Request> {


    /**
     * 展示配置
     */
    private DisplayConfig disPlayConfig;
    /**
     * 加载策略
     */
    private RequestPolicy requestPolicy = ZImage.getInstance().getConfig().getRequestPolicy();

    /**
     * 序列号，用于顺序比较
     */
    private int lev;


    /**
     * 持有ImageView的软引用
     */
    private SoftReference<Target> targetSoftReference;

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 图片路径的md5值
     */
    private String imageUrlKey;

    /**
     * 下载完成的监听
     */
    private ZImage.ImageListener imageListener;


    public BitmapRequest(ImageView imageView, String imageUrl) {
        this(imageView, imageUrl, null, null);
    }

    public BitmapRequest(ImageView imageView, String imageUrl, DisplayConfig displayConfig) {
        this(imageView, imageUrl, displayConfig, null);
    }

    public BitmapRequest(ImageView imageView, String imageUrl, ZImage.ImageListener imageListener) {
        this(imageView, imageUrl, null, imageListener);
    }

    public BitmapRequest(ImageView imageView, String imageUrl, DisplayConfig displayConfig,
                         ZImage.ImageListener imageListener) {
        this.targetSoftReference = new SoftReference<Target>(new ImageViewTarget(imageView));

        if (imageUrl != null) {
            imageView.setTag(imageUrl);
            imageUrlKey = Md5Util.toMD5(imageUrl);
        }
        this.imageUrl = imageUrl;

        if (displayConfig != null) {
            this.disPlayConfig = displayConfig;
        }
        if (imageListener != null) {
            this.imageListener = imageListener;
        }
    }

    /**
     * 请求的先后顺序是根据加载的策略进行的，不同的策略比较的条件也不同，所以
     * 这里要把比较的逻辑交割具体的策略去做，策略有多种，所以通过接口调用，增强扩展性
     *
     * @return
     */
    @Override
    public int compareTo(Request o) {
        return requestPolicy.compareTo(o, this);
    }


    @Override
    public DisplayConfig getDisplayConfig() {
        return disPlayConfig;
    }

    @Override
    public RequestPolicy getRequestPolicy() {
        return requestPolicy;
    }

    @Override
    public int getLevel() {
        return lev;
    }

    @Override
    public void setLevel(int lev) {
        this.lev = lev;
    }

    @Override
    public Target getTarget() {
        if (targetSoftReference == null) {
            return null;
        }
        return targetSoftReference.get();
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getImageUrlKey() {
        return imageUrlKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitmapRequest request = (BitmapRequest) o;
        return lev == request.getLevel() &&
                requestPolicy.equals(request.requestPolicy);
    }

    @Override
    public int hashCode() {
        int result = requestPolicy != null ? requestPolicy.hashCode() : 0;
        result = 128 * result + lev;
        return result;
    }

}
