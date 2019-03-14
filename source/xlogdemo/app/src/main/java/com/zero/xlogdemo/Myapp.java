package com.zero.xlogdemo;

import android.app.Application;

public class Myapp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XlogUtils.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        XlogUtils.close();
    }
}
