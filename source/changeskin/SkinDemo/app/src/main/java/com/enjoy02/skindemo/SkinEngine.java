package com.enjoy02.skindemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.lang.reflect.Method;

/**
 * [享学课堂]
 * 学无止境，让学习成为一种享受
 * 主讲Zero老师QQ:2124346685
 * 咨询伊娜老师QQ:2133576719
 */
public class SkinEngine {

    private final static SkinEngine instance = new SkinEngine();

    public static SkinEngine getInstance() {
        return instance;
    }

    private SkinEngine() {
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    private Resources mOutResource;
    private Context mContext;
    private String mOutPkgName;

    public void load(final String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        PackageManager mPm = mContext.getPackageManager();
        PackageInfo mInfo = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        mOutPkgName = mInfo.packageName;
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);

            mOutResource = new Resources(assetManager, mContext.getResources().getDisplayMetrics(), mContext.getResources().getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getColor(int resId) {
        if (mOutResource == null) {
            return  resId;
        }
        String resName = mOutResource.getResourceEntryName(resId);
        int outResId = mOutResource.getIdentifier(resName, "color", mOutPkgName);
        if (outResId == 0) {
            return  resId;
        }
        return mOutResource.getColor(outResId);
    }

    public Drawable getDrawable(int resId) {
        if (mOutResource == null) {
            return ContextCompat.getDrawable(mContext,resId);
        }
        String resName = mOutResource.getResourceEntryName(resId);
        int outResId = mOutResource.getIdentifier(resName, "drawable", mOutPkgName);
        if (outResId == 0) {
            return  ContextCompat.getDrawable(mContext,resId);
        }
        return mOutResource.getDrawable(outResId);
    }

}
