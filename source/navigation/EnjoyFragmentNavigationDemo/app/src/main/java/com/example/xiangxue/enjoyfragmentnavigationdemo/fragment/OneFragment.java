package com.example.xiangxue.enjoyfragmentnavigationdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiangxue.enjoyfragmentnavigationdemo.R;

public class OneFragment extends Fragment {


    public static Fragment newIntance() {
        OneFragment fragment = new OneFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchFragment(OneFragment1.class.getName(), OneFragment1.newIntance(OneFragment.this));
    }

    public void switchFragment(final String oldTag, final Fragment newFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment oldTagFragment = getChildFragmentManager().findFragmentByTag(oldTag);
        Log.i("Zero", "oldTag: " + oldTag);
        Log.i("Zero", "oldTagFragment: " + oldTagFragment);
        Log.i("Zero", "newFragment: " + newFragment);
        if (oldTagFragment != null) {
            transaction.hide(oldTagFragment);
        }
        Fragment newTagFragment = getChildFragmentManager().findFragmentByTag(newFragment.getClass().getName());
        Log.i("Zero", "newTagFragment: " + newTagFragment);
        if (newTagFragment == null) {
            transaction.add(R.id.childframeLayout, newFragment, newFragment.getClass().getName());
            transaction.show(newFragment);
        } else {
            transaction.show(newTagFragment);
        }
//        transaction.addToBackStack(newFragment.getClass().getName());
        Log.i("Zero", "count: " + getChildFragmentManager().getBackStackEntryCount());
        transaction.commitAllowingStateLoss();
    }


}
