package com.enjoy.zero.viewpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.enjoy.zero.viewpagerdemo.adapter.PreloadingPagerAdapter;

public class ViewPager07Activity extends AppCompatActivity {

    private ViewPager viewPager;  //对应的viewPager

    private Button btnPre;
    private Button btnLast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vp7);

        viewPager = findViewById(R.id.viewpager01);
        btnPre = findViewById(R.id.btnPre);
        btnLast = findViewById(R.id.btnLast);

        PreloadingPagerAdapter pagerAdapter = new PreloadingPagerAdapter(ViewPager07Activity.this);

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mCurrItem = viewPager.getCurrentItem();
                int bound = Math.min(mCurrItem, 5);
                viewPager.setCurrentItem(mCurrItem - Utils.getRandom(1, bound), true);
            }
        });

        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mCurrItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem(mCurrItem + Utils.getRandom(1, 5), true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 加载ViewPager 时调用一次，同时不断滑动 ViewPage r的时候会不停地调用
             * @param position
             * @param positionOffset 当前页面偏移的百分比（向右滑动：范围0~1；向左滑动：范围1~0；）
             * @param positionOffsetPixels 当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i("Zero", "onPageScrolled position: " + position + " positionOffset: " + positionOffset + " positionOffsetPixels: " + positionOffsetPixels);
            }

            /**
             * 当 ViewPager 滑动后定格在 新的 ViewPager后会调用这个方法
             * @param position 新 ViewPager 的位置
             */
            @Override
            public void onPageSelected(int position) {
                Log.i("Zero", "onPageSelected position: " + position);
            }

            /**
             * ViewPager 的状态改变的时候调用
             * @param state SCROLL_STATE_DRAGGING（值为 1）：表示用户手指 按在屏幕上并且开始拖动 的状态（手指按下但是还没有拖动的话，就不会调用这个方法，也不会有这个状态）
             *              SCROLL_STATE_IDLE（值为 0）：滑动动画做完的状态，也就是不存在余速的状态，这个是滑动 ViewPager 后 ViewPager禁止后的最终状态
             *              SCROLL_STATE_SETTLING（值为 2）：手指离开屏幕的状态，就是滑动过程中离开了屏幕的状态，这个时候一般会存在余速，所以会切换到 SCROLL_STATE_IDLE 状态

             */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("Zero", "onPageSelected state: " + Utils.pagerScrollStateToString(state));
            }
        });
        //TODO: 添加切换动画效果
        viewPager.setPageTransformer(true, new DepthPageTransformer());

//        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pagerAdapter.getCount() / 2, true);


    }
}
