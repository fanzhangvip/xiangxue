package com.enjoy.zero.viewpagerdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.zero.viewpagerdemo.adapter.CustomPageTabStripAdapter;

import java.util.ArrayList;

public class ViewPager03Activity extends AppCompatActivity {

    // TODO: 1.声明的变量的意义
    private ViewPager viewPager;  //对应的viewPager
    private ArrayList<View> viewList;//view数组
    private ArrayList<String> titleList;//标题数组

    private PagerTabStrip pagerTabStrip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vp3);

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

        // 初始化标题数组 和view数组的个数一致
        titleList = new ArrayList<>();
        titleList.add("第一页");
        titleList.add("第二页");
        titleList.add("第三页");
        titleList.add("第四页");
        titleList.add("第五页");
        titleList.add("第六页");
        titleList.add("第七页");

        //更改下划线颜色
        pagerTabStrip =  findViewById(R.id.pagertabtitle);
        pagerTabStrip.setTabIndicatorColorResource(R.color.colorAccent);

        pagerTabStrip.setTextColor(Color.RED);
        pagerTabStrip.setClickable(false);
        pagerTabStrip.setTextSpacing(40);
        pagerTabStrip.setBackgroundColor(Color.GRAY);
        pagerTabStrip.setDrawFullUnderline(true);



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

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        };

        // TODO:3. PageView的适配器
//        viewPager.setAdapter(pagerAdapter);

        CustomPageTabStripAdapter adapter = new CustomPageTabStripAdapter(this,viewList, titleList);
        viewPager.setAdapter(adapter);

    }
}
