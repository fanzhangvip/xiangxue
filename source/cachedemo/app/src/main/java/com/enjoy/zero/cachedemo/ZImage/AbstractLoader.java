package com.enjoy.zero.cachedemo.ZImage;

import java.util.concurrent.atomic.AtomicInteger;


public abstract class AbstractLoader implements Loader {

    private static final String TAG = "AbstractLoader";

    private AtomicInteger integer = new AtomicInteger(0);
    /**
     * 加载器加载图片的逻辑是先缓存后网络，所以需要持有缓存对象的引用
     */
    private Cache bitmapCache = ZImage.getInstance().getConfig().getBitmapCache();

    /**
     * 同样因为要处理显示时的逻辑，所以需要持有显示配置对象的引用
     */
    private DisplayConfig displayConfig = ZImage.getInstance().getConfig().getDisplayConfig();

    @Override
    public void load(Request request) {
        //从缓存中获取Bitmap
        Source source = null;
        if (bitmapCache != null) {
            source = bitmapCache.get(request);
        }
        if (source == null) {
            //显示加载中图片
            showLoadingImg(request);
            //开始加载网络图，加载的逻辑不同加载器有所不同，所以交给各自
            //加载器实现，抽象
            source = onLoad(request);
            if (source == null) {
                while (integer.incrementAndGet() <= 3) {
                    source = onLoad(request);
                    if (source != null) {
                        break;
                    }
                }
                integer.set(0);
            }
            if (source == null) {
            }
            //加入缓存
            if (bitmapCache != null && source != null)
                cacheBitmap(request, source);
        } else {
            //有缓存
        }
        deliveryToUIThread(request, source);
    }

    public abstract Source onLoad(Request request);

    protected void deliveryToUIThread(final Request request, final Source source) {
        Target target = request.getTarget();
        if (target != null) {
            target.setSource(source);
        }

    }

    /**
     * 缓存图片
     *
     * @param request
     * @param source
     */
    private void cacheBitmap(Request request, Source source) {
        if (request != null && source != null) {
            synchronized (AbstractLoader.class) {
                bitmapCache.put(request, source);
            }
        }
    }

    /**
     * 显示加载中占位图,需要判断用户有没有配置
     *
     * @param request
     */
    private void showLoadingImg(Request request) {
        if (hasLoadingPlaceHolder()) {
            final Target target = request.getTarget();
            if (target != null) {
                target.setSource(new BitmapSource(null));
            }
        }
    }

    /**
     * 是否设置了加载中图片
     *
     * @return
     */
    private boolean hasLoadingPlaceHolder() {
        return displayConfig != null && displayConfig.loadingImage > 0;
    }
}
