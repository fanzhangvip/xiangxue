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
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i("Zero", "onPageScrolled position: " + position + " positionOffset: " + positionOffset + " positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Zero", "onPageSelected position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("Zero", "onPageSelected state: " + Utils.pagerScrollStateToString(state));
            }
        });

//        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pagerAdapter.getCount() / 2, true);


    }
}
