package com.enjoy.zero.cachedemo.ZImage;

import android.content.Context;

public class MemoryDiskCache implements Cache {
    //内存缓存
    private MemoryCache mMemoryCache;
    //硬盘缓存
    private DiskCache mDiskCache;

    public MemoryDiskCache(Context context) {
        mMemoryCache = new MemoryCache();
        mDiskCache = DiskCache.getInstance(context);
    }

    @Override
    public void put(Request request, Source source) {
        mMemoryCache.put(request, source);
        mDiskCache.put(request, source);
    }

    @Override
    public Source get(Request request) {
        Source source = mMemoryCache.get(request);
        if (source == null) {
            source = mDiskCache.get(request);
            if (source != null) {
                //放入内存，方便再获取
                mMemoryCache.put(request, source);
            }
        }
        return source;
    }

    @Override
    public void remove(Request request) {
        mMemoryCache.remove(request);
        mDiskCache.remove(request);
    }
}
