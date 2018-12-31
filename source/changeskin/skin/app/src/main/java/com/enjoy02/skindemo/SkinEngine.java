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
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
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

    private Resources mOutResource;// TODO: 外部资源
    private Context mContext;
    private String mOutPkgName;// TODO: 外部资源包的packageName

    /**
     * TODO: 加载外部资源包
     */
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

            //TODO: 关键技术点3 通过反射获取AssetManager 用来加载外面的资源包
            assetManager  = AssetManager.class.newInstance();
            //addAssetPath方法可以加载外部的资源包
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,path);

            mOutResource = new Resources(assetManager,mContext.getResources().getDisplayMetrics(),
                    mContext.getResources().getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getColor(int resId) {
        if (mOutResource == null) {
            return resId;
        }
        String resName = mOutResource.getResourceEntryName(resId);
        int outResId = mOutResource.getIdentifier(resName, "color", mOutPkgName);
        if (outResId == 0) {
            return resId;
        }
        return mOutResource.getColor(outResId);
    }

    public Drawable getDrawable(int resId) {//获取图片
        if (mOutResource == null) {
            return ContextCompat.getDrawable(mContext, resId);
        }
        String resName = mOutResource.getResourceEntryName(resId);
        int outResId = mOutResource.getIdentifier(resName, "drawable", mOutPkgName);
        if (outResId == 0) {
            return ContextCompat.getDrawable(mContext, resId);
        }
        return mOutResource.getDrawable(outResId);
    }

}
