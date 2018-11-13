package com.enjoy02.skindemo;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
* [享学课堂]
* 学无止境，让学习成为一种享受
* TODO:主讲Zero老师QQ 2124346685
* TODO:咨询伊娜老师QQ 2133576719
*/
public class SkinFactory implements LayoutInflater.Factory2 {

    private AppCompatDelegate mDelegate;

    public void setDelegate(AppCompatDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        mDelegate.createView(parent, name, context, attrs);

        View view;
        if (-1 == name.indexOf('.')) {
            view = onCreateView(parent, name, attrs);
        } else {
            view = createView(name, null, attrs);
        }

        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
