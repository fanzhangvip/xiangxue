package com.enjoy.zero.viewpagerdemo;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPager06Activity extends AppCompatActivity {

    private ViewPager viewPager;  //对应的viewPager
    private ArrayList<View> viewList;//view数组

    // LocalActivityManager是Android封装的把activity转换成view对象的一个api.
    //LocalActivityManager类是管理activity的，然后通过startActivity（String id,Intent intent）这个方法获取Window获取当前Window对象
    private LocalActivityManager manager;

    private Intent intentOne, intenTwo, intentThree;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vp6);

        viewPager = findViewById(R.id.viewpager01);

        viewList = new ArrayList<>();

        manager = new LocalActivityManager(this, true);
        //这句是必须的
        manager.dispatchCreate(savedInstanceState);

        intentOne = new Intent(ViewPager06Activity.this, OneActivity.class);
        View view01 = manager.startActivity("viewID", intentOne).getDecorView();
        intenTwo = new Intent(ViewPager06Activity.this, TwoActivity.class);
        View view02 = manager.startActivity("viewID", intenTwo).getDecorView();
        intentThree = new Intent(ViewPager06Activity.this, ThreeActivity.class);
        View view03 = manager.startActivity("viewID", intentThree).getDecorView();

        viewList.add(view01);
        viewList.add(view02);
        viewList.add(view03);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewList.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }
        };
        viewPager.setAdapter(pagerAdapter);

    }
}
