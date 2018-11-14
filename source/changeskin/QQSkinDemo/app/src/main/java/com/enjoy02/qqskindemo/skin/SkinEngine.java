package com.enjoy02.qqskindemo.skin;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.TypedValue;

/**
 * http://blog.csdn.net/cc_want/article/details/49179109
 * @author CSDN id:cc_want
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SkinEngine {

	private static SkinEngine instances;
	private Resources mResources;
	private DrawablePreloadIntercepter drawablePreloadIntercepter;

	public static SkinEngine getInstances() {
		if (instances == null) {
			instances = new SkinEngine();
		}
		return instances;
	}
	public void init(Resources res) {
		mResources = res;
		initIntercepter(res);
	}
	private void initIntercepter(Resources res) {
		try {
			Class<?> resourcesClz = res.getClass();
			Field field = resourcesClz.getDeclaredField("sPreloadedDrawables");
			field.setAccessible(true);
			Object object = field.get(res);
			if (object instanceof LongSparseArray[]) {
				LongSparseArray[] preloadedDrawables = new LongSparseArray[2];
				preloadedDrawables = ((LongSparseArray[]) object);
				drawablePreloadIntercepter = new DrawablePreloadIntercepter(SkinEngine.this,preloadedDrawables);
				preloadedDrawables[0] = drawablePreloadIntercepter;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public Drawable.ConstantState loadConstantState(int resId) {
		try {
			TypedValue value = new TypedValue();
			mResources.getValue(resId, value, true);
			String str = value.string.toString();
			String filePath = str.substring(1 + str.lastIndexOf("/"));
			Log.e("", "from Assets load skin ：" + filePath+"(ResourceID="+resId+").");
			InputStream is = mResources.getAssets().open(filePath);
			Bitmap bitmap = BitmapFactory.decodeResourceStream(mResources,value, is, new Rect(), null);
			Drawable.ConstantState cs = new SkinnableBitmapDrawable.BitmapState(bitmap);
			return cs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * add drawable preload Intercepter resource
	 * @param res
	 * @param resId
	 */
	public void addResource(Resources res,int resId) {
		drawablePreloadIntercepter.add(res,resId);
	}
	/**
	 * start load skin
	 */
	public void run() {
		clearDrawableCache();
	}

	/**
	 * cancel load skin
	 */
	public void unInit() {
		clearDrawableCache();
		try {
			Resources res = mResources;
			Class<?> clazz = res.getClass();
			Field field = clazz.getDeclaredField("sPreloadedDrawables");
			field.setAccessible(true);
			field.set(null, drawablePreloadIntercepter.mOldPreloadCache);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Clear Resource Drawable Cache
	 */
	private void clearDrawableCache(){
		try {
			Resources res = mResources;
			Class<?> clazz = res.getClass();
			Field field = clazz.getDeclaredField("mDrawableCache");
			field.setAccessible(true);
			Object obj = field.get(res);
			if (obj instanceof LongSparseArray) {
				LongSparseArray<WeakReference<Drawable.ConstantState>> drawableCache = (LongSparseArray<WeakReference<Drawable.ConstantState>>) obj;
				drawableCache.clear();
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
