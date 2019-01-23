package com.enjoy.zero.viewpagerdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.zero.viewpagerdemo.R;
import com.enjoy.zero.viewpagerdemo.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;


public class Fragment2WithViewPager extends BaseFragment {

    private ViewPager viewPager;  //对应的viewPager
    private ArrayList<Fragment> fragmentsList;//view数组


    public static Fragment newIntance() {
        Fragment2WithViewPager fragment = new Fragment2WithViewPager();
        fragment.setFragmentDelegater(new FragmentDelegater(fragment));
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_2_vp, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewpager01);

        fragmentsList = new ArrayList<>();

        fragmentsList.add(Fragment2_vp_1.newIntance());
        fragmentsList.add(Fragment2_vp_2.newIntance());
        fragmentsList.add(Fragment2_vp_3.newIntance());
        fragmentsList.add(Fragment2_vp_4.newIntance());
        //https://juejin.im/post/5adcb0e36fb9a07aa7673fbc

        /**
         * 实例化一个PagerAdapter
         * 必须重写的两个方法
         * getCount
         * getItem
         */

        PagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentsList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentsList.size();
            }
        };

        viewPager.setAdapter(pagerAdapter);

    }


}
