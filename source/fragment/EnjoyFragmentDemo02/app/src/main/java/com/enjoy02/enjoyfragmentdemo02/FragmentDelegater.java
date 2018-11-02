package com.enjoy02.enjoyfragmentdemo02;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDelegater {

    Fragment mFragment;

    public FragmentDelegater(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * 第一次进来不会触发
     * 跳转到下一个页面的时候会触发：true
     * 在回来的时候会触发：false
     * 返回到上一级的时候 不会促发
     * @param hidden
     */
    public void onHiddenChanged(boolean hidden) {
//        Log.i("Zero", "onHiddenChanged hidden: " + hidden);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i("Zero", "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
//        dumpLifeCycle("onInflate");
    }

    public void onAttach(Activity activity) {
        dumpLifeCycle("onAttach: " + mFragment.hashCode());
    }

    public void onCreate(Bundle savedInstanceState) {
        dumpLifeCycle("onCreate");
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        dumpLifeCycle("onCreateView");
        return null;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        dumpLifeCycle("onViewCreated");
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        dumpLifeCycle("onActivityCreated");
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        dumpLifeCycle("onViewStateRestored");
    }

    public void onStart() {
        dumpLifeCycle("onStart");
    }

    public void onResume() {
        dumpLifeCycle("onResume");
    }

    public void onSaveInstanceState(Bundle outState) {
//        dumpLifeCycle("onSaveInstanceState");
    }

    public void onConfigurationChanged(Configuration newConfig) {
//        dumpLifeCycle("onConfigurationChanged");
    }

    public void onPause() {
        dumpLifeCycle("onPause");
    }

    public void onStop() {
        dumpLifeCycle("onStop");
    }

    public void onDestroyView() {
        dumpLifeCycle("onDestroyView");
    }

    public void onDestroy() {
        dumpLifeCycle("onDestroy");
    }

    public void onDetach() {
        dumpLifeCycle("onDetach");
    }

    private void dumpLifeCycle(final String method) {
        Log.i("Zero", "name: " + mFragment.getClass().getSimpleName() + " -> " + method);
    }
}
