package com.enjoy02.changeskindemo;

import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

/**
* [享学课堂]
* 学无止境，让学习成为一种享受
* TODO: 主讲Zero老师QQ 2124346685
* TODO: 咨询伊娜老师QQ 2133576719
*/
public class SkinActivity extends AppCompatActivity {

    protected SkinFactory mSkinFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: hook view的加载，设置factory进去
        mSkinFactory = new SkinFactory();
        mSkinFactory.setDelegate(getDelegate());
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),mSkinFactory);
        super.onCreate(savedInstanceState);

    }
}
