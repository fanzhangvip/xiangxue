package com.enjoy02.enjoyfragmentdemo02.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enjoy02.enjoyfragmentdemo02.BaseFragment;
import com.enjoy02.enjoyfragmentdemo02.R;


public class Bug51Fragment extends BaseFragment {

    private String name;

    public static int count = 0;

    public static Fragment newIntance(final String name) {
        Bug51Fragment fragment = new Bug51Fragment();
        count++;
        Bundle args = new Bundle();
        args.putString("name", name);
//        FragmentUtils.getFragmentInfo(fragment);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("Zero", "onAttach Bug51Fragment");
        name = getArguments().getString("name");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Zero", "onDetach Bug51Fragment");
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
//        Log.i("Zero", "onInflate");
//        FragmentUtils.getFragmentInfo(this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.i("Zero", "onCreateView");
//        FragmentUtils.getFragmentInfo(this);
        return inflater.inflate(R.layout.fragment_bug51, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView info = view.findViewById(R.id.fragmentstack);
//        Log.i("Zero", "name: " + name + " count: " + count);
//        FragmentUtils.getFragmentInfo(this);
        info.setText(name + " " + count);
    }

}
