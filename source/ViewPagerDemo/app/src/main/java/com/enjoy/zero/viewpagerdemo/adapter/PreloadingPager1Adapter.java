package com.enjoy.zero.viewpagerdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.zero.viewpagerdemo.Utils;
import com.enjoy.zero.viewpagerdemo.view.PagerAdapter;
import com.enjoy.zero.viewpagerdemo.view.PreloadView;

public class PreloadingPager1Adapter extends PagerAdapter {

    private SparseArray<View> mViews;//用sparseArray保存加载过的页面
    private int mCount = 6000;
    private Context mContext;


    public PreloadingPager1Adapter(Context context) {
        this.mContext = context;
        mViews = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        Log.i("Zero", "destroyItem, position: " + position + ", object: " + object + ", container: " + container);
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i("Zero", "instantiateItem, position: " + position + ", container: " + container);

        if (mViews.get(position) == null) {//首先判断mViews中是否保存了之前加载过的页面，如果没有加载过就加载，如果加载过了，那么就不用加载了
            PreloadView preloadView = new PreloadView(mContext);
            preloadView.setText(Integer.toString(position));
            preloadView.setTextColor(Utils.getRandomColor());
            mViews.put(position, preloadView);//将加载的页面view保存在sparseArray里面
        }
        container.addView(mViews.get(position));//从sparseArray里面获取页面view用于显示
        return mViews.get(position);//从sparseArray里面获取保存的页面view
    }

}
