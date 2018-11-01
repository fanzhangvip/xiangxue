package com.zero.enjoy02.launchmodedemo02;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dumpActivityInfo("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        dumpActivityInfo("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dumpActivityInfo("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTextView().setText("当前activity: " + getClass().getSimpleName());
        dumpActivityInfo("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        dumpActivityInfo("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        dumpActivityInfo("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dumpActivityInfo("onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("Zero","onNewIntent: " + this);
        dumpActivityInfo("onNewIntent");
    }

    public void dumpActivityInfo(final String methodName) {
        try {
            ActivityInfo info = this.getPackageManager()
                    .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
//            Log.i("Zero", getClass().getSimpleName()+" 启动次数： "+getInstacnceCount() + " "+methodName + " "+getLauchModeString(info.launchMode) +" taskId: " +getTaskId()+ " taskAffinity: " + info.taskAffinity);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getLauchModeString(int launchMode) {
        String ret = "null";
        switch (launchMode) {
            case ActivityInfo.LAUNCH_MULTIPLE:
                ret = "Standard";
                break;
            case ActivityInfo.LAUNCH_SINGLE_TOP:
                ret = "SingleTop";
                break;
            case ActivityInfo.LAUNCH_SINGLE_TASK:
                ret = "SingleTask";
                break;
            case ActivityInfo.LAUNCH_SINGLE_INSTANCE:
                ret = "SingleInstance";
                break;
            default:
                ret = "null";

        }
        return ret;
    }

    public abstract int getInstacnceCount();

    public abstract TextView getTextView();
}
