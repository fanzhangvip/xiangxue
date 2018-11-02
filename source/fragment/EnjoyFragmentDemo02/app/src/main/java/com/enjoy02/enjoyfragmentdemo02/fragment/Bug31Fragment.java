package com.enjoy02.enjoyfragmentdemo02.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoy02.enjoyfragmentdemo02.BaseFragment;
import com.enjoy02.enjoyfragmentdemo02.FragmentDelegater;
import com.enjoy02.enjoyfragmentdemo02.R;

/**
 * TODO: Fragment重叠异常
 */
@SuppressLint("ValidFragment")
public class Bug31Fragment extends BaseFragment {

    private Activity mActivity;
    private Handler mHandler;

    public static Fragment newIntance(Handler handler) {
        Bug31Fragment fragment = new Bug31Fragment(handler);
        fragment.setHandler(handler);
        fragment.setFragmentDelegater(new FragmentDelegater(fragment));
        return fragment;
    }

    @SuppressLint("ValidFragment")
    public Bug31Fragment(Handler handler){
        super();
        mHandler = handler;
    }

    public void setHandler(Handler handler){
        mHandler = handler;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bug3, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        while (true){
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }.start();
            }
        });

    }

}
