package com.enjoy.zero.archdemo;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

public class MyApp extends Application {

    public Stack<Activity> mActivitys;

    private MyActLifeCycle myActLifeCycle;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivitys = new Stack<>();
        myActLifeCycle = new MyActLifeCycle();
//        registerActivityLifecycleCallbacks(myActLifeCycle);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(myActLifeCycle);
    }

    class MyActLifeCycle implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivitys.push(activity);
//            dump("onCreated",activity);
            Log.i("Zero","======================");
            dumpLifeCycle("onActivityCreated", activity);
//            Log.v("Zero", "activity堆栈：\n" + mActivitys.toString());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            dumpLifeCycle("onActivityStarted", activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            dumpLifeCycle("onActivityResumed", activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            dumpLifeCycle("onActivityPaused", activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            dumpLifeCycle("onActivityStopped", activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            dumpLifeCycle("onActivitySaveInstanceState", activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

//            mActivitys.pop();
//            dump("onDestoryed",activity);
            dumpLifeCycle("onActivityDestroyed", activity);
//            Log.e("Zero", "activity堆栈：\n" + mActivitys.toString());
        }

        private void dumpLifeCycle(final String method, final Activity act) {
            Log.i("Zero", "activityName: " + act.getClass().getSimpleName() + " -> " + method);
        }


        private void dump(final String method, final Activity act) {
            final StringBuffer sb = new StringBuffer();
            sb.append(method).append(":\n");
            String flag = method.equals("onCreated") ? "add: " : "removed: ";
            Log.i("Zero", method + "-> " + flag + act);
            if (mActivitys.size() <= 0) {
                Log.i("Zero", "mActivitys.size= " + mActivitys.size());
            }

            for (Activity activity : mActivitys) {
                sb.append(dumpActivity(activity, getActivityInfo(activity))).append("\n");
                Log.i("Zero", dumpActivity(activity, getActivityInfo(activity)));
            }
            Log.i("Zero", "---------------------------------------------------");
//            Log.i("Zero",sb.toString());
        }

        public String dumpActivity(final Activity activity, final ActivityInfo activityInfo) {
            return getLauchModeString(activityInfo.launchMode) + " taskId: " + activity.getTaskId() + " " + activity + " taskAffinity: " + activityInfo.taskAffinity;
        }

        public ActivityInfo getActivityInfo(final Activity activity) {
            try {
                ActivityInfo info = getPackageManager()
                        .getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
                return info;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return null;
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
    }
}
