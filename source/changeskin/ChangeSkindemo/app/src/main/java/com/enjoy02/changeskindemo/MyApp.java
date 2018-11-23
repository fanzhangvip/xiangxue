package com.enjoy02.changeskindemo;

import android.app.Application;

/**
* [享学课堂]
* 学无止境，让学习成为一种享受
* TODO: 主讲Zero老师QQ 2124346685
* TODO: 咨询伊娜老师QQ 2133576719
*/
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinEngine.getInstance().init(this);
    }
}
