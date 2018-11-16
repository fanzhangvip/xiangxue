package com.enjoy02.qqskindemo.skin;

import android.content.res.Resources;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.LongSparseArray;
import android.util.TypedValue;


public class DrawablePreloadIntercepter extends LongSparseArray<ConstantState> {

    public LongSparseArray<ConstantState>[] mOldPreloadCache;
    private LongSparseArray<Integer> mKeyToResourcesId = new LongSparseArray<Integer>();
    private SkinEngine mSkinEngine;

    public DrawablePreloadIntercepter(SkinEngine skinEngine, LongSparseArray<ConstantState>[] preloadCache) {
        mSkinEngine = skinEngine;
        mOldPreloadCache = preloadCache.clone();
    }

    public void add(Resources resources, int resId) {
        TypedValue value = new TypedValue();
        resources.getValue(resId, value, true);
        if ((value.string != null) && !(value.string.toString().endsWith(".xml"))) {
            final long key = (((long) value.assetCookie) << 32) | value.data;
            mKeyToResourcesId.put(key, Integer.valueOf(resId));
        }
    }



}
