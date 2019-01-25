package com.enjoy.zero.viewpagerdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.enjoy.zero.viewpagerdemo.R;


public class Fragment5 extends MiddleFragment {

    private Button mReplace;
    private Button mHideShow;

    private Fragment mCurrentFragment;

    private Fragment fragment51;
    private Fragment fragment52;


    public static Fragment newIntance() {
        Fragment5 fragment = new Fragment5();
        fragment.setFragmentDelegater(new FragmentDelegater(fragment));
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_5, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReplace = view.findViewById(R.id.btnReplace);
        mHideShow = view.findViewById(R.id.btnHideShow);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        mCurrentFragment = fragment51 = Fragment5_1.newIntance();
        fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment, Fragment5_1.class.getName());
        fragmentTransaction.show(mCurrentFragment);
        fragmentTransaction.commit();


        mReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                if (mCurrentFragment instanceof Fragment5_1) {
                    mCurrentFragment = fragment52 = Fragment5_2.newIntance();
                    fragmentTransaction.replace(R.id.frameLayout_5, mCurrentFragment, Fragment5_2.class.getName());
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                } else {
                    mCurrentFragment = fragment51 = Fragment5_1.newIntance();
                    fragmentTransaction.replace(R.id.frameLayout_5, mCurrentFragment, Fragment5_1.class.getName());
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                }
            }
        });
        mHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                if (mCurrentFragment instanceof Fragment5_1) {
                    Fragment tmp = getChildFragmentManager().findFragmentByTag(Fragment5_2.class.getName());
                    fragmentTransaction.hide(mCurrentFragment);
                    if (tmp == null) {
                        mCurrentFragment = fragment52 = Fragment5_2.newIntance();
                        fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment, Fragment5_2.class.getName());
                    } else {
                        mCurrentFragment = tmp;
                    }
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                } else {
                    Fragment tmp = getChildFragmentManager().findFragmentByTag(Fragment5_1.class.getName());
                    fragmentTransaction.hide(mCurrentFragment);
                    if (tmp == null) {
                        mCurrentFragment = fragment51 = Fragment5_1.newIntance();
                        fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment, Fragment5_1.class.getName());
                    } else {
                        mCurrentFragment = tmp;
                    }
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                }
            }
        });
    }


}
