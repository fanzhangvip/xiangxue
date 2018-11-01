//package com.example.xiangxue.enjoyfragmentnavigationdemo;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.xiangxue.enjoyfragmentnavigationdemo.fragment.FourFragment;
//import com.example.xiangxue.enjoyfragmentnavigationdemo.fragment.OneFragment;
//import com.example.xiangxue.enjoyfragmentnavigationdemo.fragment.ThreeFragment;
//import com.example.xiangxue.enjoyfragmentnavigationdemo.fragment.TwoFragment;
//
//
//public class MainFragment extends Fragment {
//
//    //TODO: 记录最后添加的是哪个Fragment
//    private int lastShowFragment = 0;
//
//    private BottomNavigationView bottomNavigationView;
//
//    private Fragment[] fragments;
//
//
//    public static Fragment newIntance() {
//        MainFragment fragment = new MainFragment();
//        return fragment;
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_main, container, false);
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
//        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
//        initFragments();
//    }
//
//    private void initFragments() {
//
//        fragments = new Fragment[]{OneFragment.newIntance(), TwoFragment.newIntance(), ThreeFragment.newIntance(), FourFragment.newIntance()};
//        lastShowFragment = 0;
//        getChildFragmentManager()
//                .beginTransaction()
//                .add(R.id.frameLayout, fragments[lastShowFragment])
//                .show(fragments[lastShowFragment])
//                .commit();
//    }
//
//    //TODO: 第三步 添加NavigationItemSelected监听
//    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////            switch (item.getItemId()) {
////                case R.id.oneFragment1:
////                    if (lastShowFragment != 0) {
////                        switchFragment(0);
////                        lastShowFragment = 0;
////                    }
////                    return true;
////                case R.id.twoFragment:
////                    if (lastShowFragment != 1) {
////                        switchFragment(1);
////                        lastShowFragment = 1;
////                    }
////                    return true;
////                case R.id.threeFragment:
////                    if (lastShowFragment != 2) {
////                        switchFragment(2);
////                        lastShowFragment = 2;
////                    }
////                    return true;
////                case R.id.fourFragment:
////                    if (lastShowFragment != 3) {
////                        switchFragment(3);
////                        lastShowFragment = 3;
////                    }
////                    return true;
//            }
//            return false;
//        }
//
//    };
//
//    private void switchFragment(final int index) {
//        if (lastShowFragment != index) {
//            switchFragmentInner(lastShowFragment, index);
//            lastShowFragment = index;
//        }
//    }
//
//    void switchFragmentInner(final int lastIndex, final int index) {
//
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.hide(fragments[lastIndex]);
//
//        if (!fragments[index].isAdded()) {
//            transaction.add(R.id.frameLayout, fragments[index]);
//        }
//
//        transaction.show(fragments[index]).commitAllowingStateLoss();
//    }
//
//}
