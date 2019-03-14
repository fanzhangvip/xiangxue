package com.enjoy.zero.cachedemo.ZImage;

import android.util.LruCache;

public class MemoryCache implements Cache {

    private LruCache<String, Source> mLruCache;

    public MemoryCache() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        mLruCache = new LruCache<String, Source>(maxSize) {
            @Override
            protected int sizeOf(String key, Source value) {
                return value.getSize();
            }
        };
    }

    @Override
    public Source get(Request request) {
        if (mLruCache == null) return null;
        return mLruCache.get(request.getImageUrlKey());
    }

    @Override
    public void put(Request request, Source source) {
        if (mLruCache == null) return;
        mLruCache.put(request.getImageUrlKey(), source);
    }

    @Override
    public void remove(Request request) {
        if (mLruCache == null) return;
        mLruCache.remove(request.getImageUrlKey());
    }
}
