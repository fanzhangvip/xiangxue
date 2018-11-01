package com.enjoy.enjoyfragmentdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy.enjoyfragmentdemo.R;

public class OneFragment3 extends Fragment {



    public static Fragment newIntance(OneFragment oneFragment) {
        OneFragment3 fragment = new OneFragment3();
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
        return inflater.inflate(R.layout.fragment_13, container, false);
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
        oneFragment.switchFragment(OneFragment3.class.getName(), OneFragment4.newIntance(oneFragment));
    }

}
