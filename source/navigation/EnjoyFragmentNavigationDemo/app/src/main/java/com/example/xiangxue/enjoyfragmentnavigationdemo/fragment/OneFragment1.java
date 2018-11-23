package com.example.xiangxue.enjoyfragmentnavigationdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiangxue.enjoyfragmentnavigationdemo.R;

import androidx.navigation.Navigation;
/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class OneFragment1 extends Fragment {


    public static Fragment newIntance(OneFragment oneFragment) {
        OneFragment1 fragment = new OneFragment1();
        fragment.setOneFragment(oneFragment);
        return fragment;
    }

    OneFragment oneFragment;

    public void setOneFragment(OneFragment oneFragment) {
        this.oneFragment = oneFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_11, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick(v);
            }
        });

    }

    public void myClick(View view) {
        Navigation.createNavigateOnClickListener(R.id.action_oneFragment1_to_oneFragment2).onClick(view);
    }


}
