package com.enjoy02.qqskindemo.skin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.TypedValue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


public class SkinEngine {

    private static SkinEngine instances;
    private Resources mResources;
    private DrawablePreloadIntercepter drawablePreloadIntercepter;

    private Object mResourcesImplObject;
    private Class<?> mResourcesImplClz;
    private Field mResourcesImplField;
    private Field mSPreloadedDrawables;

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
            mResourcesImplField = resourcesClz.getDeclaredField("mResourcesImpl");
            mResourcesImplField.setAccessible(true);
            mResourcesImplObject = mResourcesImplField.get(res);
            mResourcesImplClz = mResourcesImplObject.getClass();
            mSPreloadedDrawables = mResourcesImplClz.getDeclaredField("sPreloadedDrawables");
            mSPreloadedDrawables.setAccessible(true);
            Object object = mSPreloadedDrawables.get(mResourcesImplObject);
            if (object instanceof LongSparseArray[]) {
                LongSparseArray[] preloadedDrawables = new LongSparseArray[2];
                preloadedDrawables = ((LongSparseArray[]) object);
                drawablePreloadIntercepter = new DrawablePreloadIntercepter(SkinEngine.this, preloadedDrawables);
                preloadedDrawables[0] = drawablePreloadIntercepter;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Zero", e.getMessage());
        }
    }

    public Drawable.ConstantState loadConstantState(int resId) {
        try {
            TypedValue value = new TypedValue();
            mResources.getValue(resId, value, true);
            String str = value.string.toString();
            String filePath = str.substring(1 + str.lastIndexOf("/"));
            Log.e("Zero", "from Assets load skin ：" + filePath + "(ResourceID=" + resId + ").");
            InputStream is = mResources.getAssets().open(filePath);
            Bitmap bitmap = BitmapFactory.decodeResourceStream(mResources, value, is, new Rect(), null);
            Drawable.ConstantState cs = new SkinnableBitmapDrawable.BitmapState(bitmap);
            return cs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * add drawable preload Intercepter resource
     *
     * @param res
     * @param resId
     */
    public void addResource(Resources res, int resId) {
        drawablePreloadIntercepter.add(res, resId);
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
            mSPreloadedDrawables.set(null, drawablePreloadIntercepter.mOldPreloadCache);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Zero", e.getMessage());
        }

    }

    /**
     * Clear Resource Drawable Cache
     */
    private void clearDrawableCache() {
        try {
            Field field = mResourcesImplClz.getDeclaredField("mDrawableCache");
            field.setAccessible(true);
            Object obj = field.get(mResourcesImplObject);
            Field field1 = obj.getClass().getSuperclass().getDeclaredField("mUnthemedEntries");
            field1.setAccessible(true);
            Object obj1 = field1.get(obj);
            if (obj1 instanceof LongSparseArray) {
                LongSparseArray<WeakReference<Drawable.ConstantState>> drawableCache = (LongSparseArray<WeakReference<Drawable.ConstantState>>) obj1;
                if (drawableCache != null) {
                    drawableCache.clear();
                }

            }

            Field field2 = obj.getClass().getSuperclass().getDeclaredField("mNullThemedEntries");
            field2.setAccessible(true);
            Object obj2 = field2.get(obj);
            if (obj1 instanceof LongSparseArray) {
                LongSparseArray<WeakReference<Drawable.ConstantState>> drawableCache = (LongSparseArray<WeakReference<Drawable.ConstantState>>) obj2;
                if (drawableCache != null) {
                    drawableCache.clear();
                }

            }
            Field field3 = obj.getClass().getSuperclass().getDeclaredField("mThemedEntries");
            field3.setAccessible(true);
            Object obj3 = field3.get(obj);
            if (obj3 instanceof ArrayMap) {
                ArrayMap arrayMap = (ArrayMap<?, ?>) obj3;
                if (arrayMap != null) {
                    arrayMap.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Zero", e.getMessage());
        }
    }
}
