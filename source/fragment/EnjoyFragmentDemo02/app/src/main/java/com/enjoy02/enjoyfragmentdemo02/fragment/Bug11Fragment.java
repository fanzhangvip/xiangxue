package com.enjoy02.enjoyfragmentdemo02.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.enjoy02.enjoyfragmentdemo02.BaseFragment;
import com.enjoy02.enjoyfragmentdemo02.R;
import com.enjoy02.enjoyfragmentdemo02.arch.FragmentObservable;
import com.enjoy02.enjoyfragmentdemo02.arch.FragmentObserver;

/**
 * TODO: getActivity==null
 */
public class Bug11Fragment extends BaseFragment {

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getActivity(), "任务执行了", Toast.LENGTH_LONG).show();
        }
    };

    FragmentObservable fragmentObservable = new FragmentObservable();

    class NetWorkTask implements Runnable {

        boolean isCancel = false;

        public NetWorkTask() {
            fragmentObservable.addObserver(fragmentObserver);
        }

        FragmentObserver fragmentObserver = new FragmentObserver() {
            @Override
            public void onAttach(Context context) {

            }

            @Override
            public void onDetach() {
                Log.i("Zero", "观察到fragment生命周期走到onDetach了，任务取消。。。");
                isCancel = true;
                mhandler.removeCallbacksAndMessages(null);
            }
        };

        @Override
        public void run() {
            try {
                Thread.sleep(8500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancel) {
                return;
            }
            mhandler.sendEmptyMessage(1);
        }
    }


    //TODO: [解决方案]在onAttach(Activity activity)里赋值，使用mActivity代替getActivity()，保证Fragment即使在onDetach后，仍持有Activity的引用（有引起内存泄露的风险，但是异步任务没停止的情况下，本身就可能已内存泄漏，相比Crash，这种做法“安全”些）
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentObservable.onAttach(context);
        Log.i("Zero", "onAttach : " + getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Zero", "onCreate : " + getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Zero", "onCreateView : " + getActivity());
        return inflater.inflate(R.layout.fragment_bug1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("Zero", "onViewCreated : " + getActivity());
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new NetWorkTask());
                thread.start();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Zero", "onActivityCreated : " + getActivity());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("Zero", "onViewStateRestored : " + getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Zero", "onStart : " + getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Zero", "onResume : " + getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Zero", "onPause : " + getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Zero", "onStop : " + getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Zero", "onDestroyView : " + getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Zero", "onDestroy : " + getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentObservable.onDetach();
        Log.i("Zero", "onDetach : " + getActivity());
    }

    public static Fragment newIntance() {
        Bug11Fragment fragment = new Bug11Fragment();
        return fragment;
    }


}
