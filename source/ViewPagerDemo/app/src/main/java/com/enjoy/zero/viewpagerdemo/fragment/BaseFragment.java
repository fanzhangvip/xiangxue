package com.enjoy.zero.viewpagerdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {

    FragmentDelegater fragmentDelegater;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (fragmentDelegater != null) {
            fragmentDelegater.onAttach(context);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fragmentDelegater != null) {
            fragmentDelegater.onCreate(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragmentDelegater != null) {
            fragmentDelegater.onCreateView(inflater, container, savedInstanceState);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fragmentDelegater != null) {
            fragmentDelegater.onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (fragmentDelegater != null) {
            fragmentDelegater.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (fragmentDelegater != null) {
            fragmentDelegater.onViewStateRestored(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (fragmentDelegater != null) {
            fragmentDelegater.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentDelegater != null) {
            fragmentDelegater.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fragmentDelegater != null) {
            fragmentDelegater.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fragmentDelegater != null) {
            fragmentDelegater.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fragmentDelegater != null) {
            fragmentDelegater.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fragmentDelegater != null) {
            fragmentDelegater.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fragmentDelegater != null) {
            fragmentDelegater.onDetach();
        }
    }

    public void setFragmentDelegater(FragmentDelegater fragmentDelegater) {
        this.fragmentDelegater = fragmentDelegater;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (fragmentDelegater != null) {
            fragmentDelegater.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (fragmentDelegater != null) {
            fragmentDelegater.onHiddenChanged(hidden);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentDelegater != null) {
            fragmentDelegater.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        if (fragmentDelegater != null) {
            fragmentDelegater.onInflate(activity, attrs, savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragmentDelegater != null) {
            fragmentDelegater.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (fragmentDelegater != null) {
            fragmentDelegater.onConfigurationChanged(newConfig);
        }
    }

}
