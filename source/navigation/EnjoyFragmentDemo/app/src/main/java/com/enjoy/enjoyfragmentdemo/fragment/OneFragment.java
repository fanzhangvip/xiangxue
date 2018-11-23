package com.enjoy.enjoyfragmentdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.enjoyfragmentdemo.R;
/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class OneFragment extends Fragment {


    public static Fragment newIntance() {
        OneFragment fragment = new OneFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchFragment(OneFragment1.class.getName(), OneFragment1.newIntance(OneFragment.this));
    }

    public void switchFragment(final String oldTag, final Fragment newFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment oldTagFragment = getChildFragmentManager().findFragmentByTag(oldTag);
        if (oldTagFragment != null) {
            transaction.hide(oldTagFragment);
        }
        Fragment newTagFragment = getChildFragmentManager().findFragmentByTag(newFragment.getClass().getName());
        if (newTagFragment == null) {
            transaction.add(R.id.childframeLayout, newFragment, newFragment.getClass().getName());
            transaction.show(newFragment);
            transaction.addToBackStack(newFragment.getClass().getName());
        } else {
//            transaction.show(newTagFragment);
            Log.i("Zero", "tag: " + newFragment.getTag() + " id: " + newFragment.getId());
            getChildFragmentManager().popBackStackImmediate(newFragment.getId(), 0);
        }
        transaction.commitAllowingStateLoss();
    }


}
