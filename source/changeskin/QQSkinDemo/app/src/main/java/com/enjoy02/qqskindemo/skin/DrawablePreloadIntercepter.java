package com.enjoy02.qqskindemo.skin;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build;
import android.util.LongSparseArray;
import android.util.TypedValue;

/**
 * http://blog.csdn.net/cc_want/article/details/49179109
 * @author CSDN id:cc_want
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class DrawablePreloadIntercepter extends LongSparseArray<ConstantState> {
	
	public LongSparseArray<ConstantState>[] mOldPreloadCache;
	private LongSparseArray<Integer> mKeyToResourcesId = new LongSparseArray<Integer>();
	private SkinEngine mSkinEngine;

	public DrawablePreloadIntercepter(SkinEngine skinEngine,LongSparseArray<ConstantState>[] preloadCache) {
		mSkinEngine = skinEngine;
		mOldPreloadCache=preloadCache.clone();
	}
	public void add(Resources resources, int resId) {
		TypedValue value = new TypedValue();
		resources.getValue(resId, value, true);
		if ((value.string != null)&& !(value.string.toString().endsWith(".xml"))) {
			final long key = (((long) value.assetCookie) << 32) | value.data;
			mKeyToResourcesId.put(key, Integer.valueOf(resId));
		}
	}
	@Override
	public ConstantState get(long key) {
		if ((Integer) mKeyToResourcesId.get(key) == null) {
			return (ConstantState) mOldPreloadCache[0].get(key);
		}
		return mSkinEngine.loadConstantState(((Integer) this.mKeyToResourcesId.get(key)).intValue());
	}

}
