package com.enjoy02.skindemo;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class TextViewSkinable extends AppCompatTextView implements Skinable {
    public TextViewSkinable(Context context) {
        super(context);
    }

    public TextViewSkinable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewSkinable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void applySkin() {
        setTextColor(SkinColorHelper.getColor(0));
    }
}
