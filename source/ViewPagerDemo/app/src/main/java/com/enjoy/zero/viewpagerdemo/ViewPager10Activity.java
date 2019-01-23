package com.enjoy.zero.viewpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.enjoy.zero.viewpagerdemo.adapter.MyFragmentPagerAdapter;
import com.enjoy.zero.viewpagerdemo.fragment.Fragment1;
import com.enjoy.zero.viewpagerdemo.fragment.Fragment2;
import com.enjoy.zero.viewpagerdemo.fragment.Fragment2WithViewPager;
import com.enjoy.zero.viewpagerdemo.fragment.Fragment3;
import com.enjoy.zero.viewpagerdemo.fragment.Fragment4;
import com.enjoy.zero.viewpagerdemo.fragment.Fragment5;

import java.util.ArrayList;

public class ViewPager10Activity extends AppCompatActivity {

    private ViewPager viewPager;  //对应的viewPager
    private ArrayList<Fragment> fragmentsList;//view数组


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vp10);

        viewPager = findViewById(R.id.viewpager01);

        fragmentsList = new ArrayList<>();

        fragmentsList.add(Fragment1.newIntance());
        fragmentsList.add(Fragment2.newIntance());
        fragmentsList.add(Fragment2WithViewPager.newIntance());
        fragmentsList.add(Fragment4.newIntance());
        fragmentsList.add(Fragment5.newIntance());

        /**
         * 实例化一个PagerAdapter
         * 必须重写的两个方法
         * getCount
         * getItem
         */

        PagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager()) {
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
