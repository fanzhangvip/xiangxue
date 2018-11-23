package com.enjoy.enjoyfragmentdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.enjoyfragmentdemo.fragment.FourFragment;
import com.enjoy.enjoyfragmentdemo.fragment.OneFragment;
import com.enjoy.enjoyfragmentdemo.fragment.ThreeFragment;
import com.enjoy.enjoyfragmentdemo.fragment.TwoFragment;
/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class MainFragment extends Fragment {

    //TODO: 记录最后添加的是哪个Fragment
    private int lastShowFragment = 0;

    private BottomNavigationView bottomNavigationView;

    private Fragment[] fragments;


    public static Fragment newIntance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        initFragments();
    }

    private void initFragments() {

        fragments = new Fragment[]{OneFragment.newIntance(), TwoFragment.newIntance(), ThreeFragment.newIntance(), FourFragment.newIntance()};
        lastShowFragment = 0;
        getChildFragmentManager()
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

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);

        if (!fragments[index].isAdded()) {
            transaction.add(R.id.frameLayout, fragments[index], fragments[index].getClass().getName());
        }

        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

}
