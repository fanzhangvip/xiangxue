package com.enjoy.zero.viewpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.enjoy.zero.viewpagerdemo.adapter.PreloadingPagerAdapter;
import com.enjoy.zero.viewpagerdemo.view.NoPreloadViewPager;

public class ViewPager08Activity extends AppCompatActivity {

    private NoPreloadViewPager viewPager;  //对应的viewPager

    private Button btnPre;
    private Button btnLast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vp8);

        viewPager = findViewById(R.id.viewpager01);
        btnPre = findViewById(R.id.btnPre);
        btnLast = findViewById(R.id.btnLast);

        PreloadingPagerAdapter pagerAdapter = new PreloadingPagerAdapter(ViewPager08Activity.this);

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


        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pagerAdapter.getCount() / 2, true);


    }
}
