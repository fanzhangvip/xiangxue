package com.enjoy.zero.viewpagerdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.zero.viewpagerdemo.R;

import java.util.ArrayList;

public class CustomPageTabStripAdapter extends PagerAdapter {

    private ArrayList<View> viewList;
    private ArrayList<String> titleList;
    private Context context;


    public CustomPageTabStripAdapter(Context context, ArrayList<View> views, ArrayList<String> titles) {
        this.context = context;
        this.viewList = views;
        this.titleList = titles;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.i("Zero", "destroyItem, position: " + position + ", object: " + object + ", container: " + container);
        container.removeView(viewList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i("Zero", "instantiateItem, position: " + position + ", container: " + container);
        View view = viewList.get(position);
        container.addView(view);
        return view;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(titleList.get(position));
        Drawable myDrawable = context.getResources().getDrawable(
                R.mipmap.ic_launcher);
        myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(),
                myDrawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(myDrawable,
                ImageSpan.ALIGN_BASELINE);

        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.DKGRAY);// 字体颜色设置为绿色
        ssb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置图标
        ssb.setSpan(fcs, 1, ssb.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置字体颜色
        ssb.setSpan(new RelativeSizeSpan(1f), 1, ssb.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.8f;
    }
}
