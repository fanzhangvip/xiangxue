package com.enjoy.zero.viewpagerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.zero.viewpagerdemo.adapter.ChangeKeyAdapter;

import java.util.ArrayList;

public class ViewPager01Activity extends AppCompatActivity {

    // TODO: 1.声明的变量的意义
    private ViewPager viewPager;  //对应的viewPager
    private ArrayList<View> viewList;//view数组


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vp1);

        // TODO:2.初始化过程
        viewPager = findViewById(R.id.viewpager01);

        LayoutInflater inflater = getLayoutInflater();
        viewList = new ArrayList<>();

        viewList.add(inflater.inflate(R.layout.layout_1, null));
        viewList.add(inflater.inflate(R.layout.layout_2, null));
        viewList.add(inflater.inflate(R.layout.layout_3, null));
        viewList.add(inflater.inflate(R.layout.layout_4, null));
        viewList.add(inflater.inflate(R.layout.layout_5, null));
        viewList.add(inflater.inflate(R.layout.layout_6, null));
        viewList.add(inflater.inflate(R.layout.layout_7, null));

        /**
         * 实例化一个PagerAdapter
         * 必须重写的四个方法
         * getCount
         * isViewFromObject
         * destroyItem
         * instantiateItem
         */

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                // TODO: 返回要滑动的VIew的个数 
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                // TODO:  从当前container中删除指定位置（position）的View
                container.removeView(viewList.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                // TODO:  做了两件事，第一：将当前视图添加到container中，第二：返回当前View
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }
        };

        // TODO:3. PageView的适配器
        viewPager.setAdapter(pagerAdapter);
        ChangeKeyAdapter changeKeyAdapter = new ChangeKeyAdapter(viewList);
        viewPager.setAdapter(changeKeyAdapter);

    }
}
