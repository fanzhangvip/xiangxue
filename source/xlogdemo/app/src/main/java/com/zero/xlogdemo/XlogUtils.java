package com.zero.xlogdemo;

import android.content.Context;
import android.os.Environment;

import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

public class XlogUtils {

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
        android.util.Log.i("Zero","static: ");
    }

    public static void init(final Context context) {
        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String logPath = SDCARD + "/xlogdemo/log";
        android.util.Log.i("Zero","logPath: " + logPath);

// this is necessary, or may cash for SIGBUS
        final String cachePath = context.getFilesDir() + "/xlog";
        android.util.Log.i("Zero","cachePath: " + cachePath);

//init xlog
        if (BuildConfig.DEBUG) {
            Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeSync, cachePath, logPath, "Zero", 5, "fanzhang");
            Xlog.setConsoleLogOpen(true);

        } else {
            Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, "Zero", 5, "fanzhang");
            Xlog.setConsoleLogOpen(false);
        }

        Log.setLogImp(new Xlog());
    }

    public static void close() {
        Log.appenderClose();
    }
}
