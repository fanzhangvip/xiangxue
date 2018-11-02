package com.enjoy02.enjoyfragmentdemo02.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.enjoy02.enjoyfragmentdemo02.BottomNavigationViewHelper;
import com.enjoy02.enjoyfragmentdemo02.R;
import com.enjoy02.enjoyfragmentdemo02.fragment.Bug51Fragment;
import com.enjoy02.enjoyfragmentdemo02.fragment.Bug52Fragment;
import com.enjoy02.enjoyfragmentdemo02.fragment.Bug53Fragment;
import com.enjoy02.enjoyfragmentdemo02.fragment.Bug54Fragment;
import com.enjoy02.enjoyfragmentdemo02.fragment.Bug5Fragment;


public class ViewpagerActivity extends FragmentActivity {

    //TODO: 记录最后添加的是哪个Fragment
    private int lastShowFragment = 0;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewpager);
        initFragments();

    }

    private void initFragments() {

        fragments = new Fragment[]{Bug51Fragment.newIntance("第一页"), Bug52Fragment.newIntance("第二页"), Bug53Fragment.newIntance("第三页"), Bug54Fragment.newIntance("第四页")};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, fragments[lastShowFragment], fragments[lastShowFragment].getClass().getName())
                .show(fragments[lastShowFragment])
                .commit();
    }

    //TODO: 第三步 添加NavigationItemSelected监听
    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.fragment_1:
                    if (lastShowFragment != 0) {
                        switchFragment(0);
                        lastShowFragment = 0;
                    }
                    return true;
                case R.id.fragment_2:
                    if (lastShowFragment != 1) {
                        switchFragment(1);
                        lastShowFragment = 1;
                    }
                    return true;
                case R.id.fragment_3:
                    if (lastShowFragment != 2) {
                        switchFragment(2);
                        lastShowFragment = 2;
                    }
                    return true;
                case R.id.fragment_4:
                    if (lastShowFragment != 3) {
                        switchFragment(3);
                        lastShowFragment = 3;
                    }
                    return true;
            }
            return false;
        }

    };

    private void switchFragment(final int index) {
        if (lastShowFragment != index) {
            switchFragmentInner(lastShowFragment, index);
            lastShowFragment = index;
        }
    }

    void switchFragmentInner(final int lastIndex, final int index) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);

        if (!fragments[index].isAdded()) {
            transaction.add(R.id.frameLayout, fragments[index], fragments[index].getClass().getName());
        }

        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

}
