package com.enjoy.zero.viewpagerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainFragment extends ListFragment {


    public static Fragment newIntance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    ArrayAdapter<String> arrayAdapter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] array = new String[]{
                "ViewPager基本使用",//0
                "PagerTitleStrip基本使用",//1
                "PagerTabStrip基本使用",//2
                "FragmentPagerAdapter基本使用",//3
                "FragmentStatePagerAdapter基本使用",//4
                "ViewPager加载Activity",//5
                "ViewPager预加载",//6
                "ViewPager禁止预加载",//7
                "ViewPager禁止预加载优化",//8
                "ViewPager懒加载"
        };
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        String item = arrayAdapter.getItem(position);
        Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
        Intent gotoAct;
        switch (position) {
            case 0://ViewPager基本使用
                gotoAct = new Intent(getActivity(), ViewPager01Activity.class);
                startActivity(gotoAct);
                break;
            case 1://PagerTitleStrip基本使用
                gotoAct = new Intent(getActivity(), ViewPager02Activity.class);
                startActivity(gotoAct);
                break;
            case 2://PagerTabStrip基本使用
                gotoAct = new Intent(getActivity(), ViewPager03Activity.class);
                startActivity(gotoAct);
                break;
            case 3://FragmentPagerAdapter基本使用
                gotoAct = new Intent(getActivity(), ViewPager04Activity.class);
                startActivity(gotoAct);
                break;
            case 4://FragmentStatePagerAdapter基本使用
                gotoAct = new Intent(getActivity(), ViewPager05Activity.class);
                startActivity(gotoAct);
                break;
            case 5://ViewPager加载Activity
                gotoAct = new Intent(getActivity(), ViewPager06Activity.class);
                startActivity(gotoAct);
                break;
            case 6://ViewPager预加载
                gotoAct = new Intent(getActivity(), ViewPager07Activity.class);
                startActivity(gotoAct);
                break;
            case 7://ViewPager禁止预加载
                gotoAct = new Intent(getActivity(), ViewPager08Activity.class);
                startActivity(gotoAct);
                break;
            case 8://ViewPager禁止预加载优化
                gotoAct = new Intent(getActivity(), ViewPager09Activity.class);
                startActivity(gotoAct);
                break;
            case 9://ViewPager懒加载
                gotoAct = new Intent(getActivity(), ViewPager10Activity.class);
                startActivity(gotoAct);
                break;
            default:
                break;
        }
    }


}
