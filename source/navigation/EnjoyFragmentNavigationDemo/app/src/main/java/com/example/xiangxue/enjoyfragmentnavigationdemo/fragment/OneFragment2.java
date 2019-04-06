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

public class OneFragment2 extends Fragment {



    public static Fragment newIntance(OneFragment oneFragment) {
        OneFragment2 fragment = new OneFragment2();
        fragment.setOneFragment(oneFragment);
        return fragment;
    }

    OneFragment oneFragment;

    public void setOneFragment(OneFragment oneFragment){
        this.oneFragment = oneFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_12, container, false);
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
//        oneFragment.switchFragment(OneFragment2.class.getName(), OneFragment3.newIntance(oneFragment));
        Navigation.findNavController(view).navigate(R.id.action_oneFragment2_to_oneFragment3);
        //1. findNavController view  导航管理者 从哪儿来的？
        //2.navigate 导航action id ？
    }


}
