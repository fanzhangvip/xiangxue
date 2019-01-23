package com.enjoy.zero.viewpagerdemo.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ChangeKeyAdapter extends PagerAdapter {

    private ArrayList<View> viewList;

    public ChangeKeyAdapter(ArrayList<View> views) {
        this.viewList = views;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        /**
         * ViewPager里面对每个页面的管理是key-value形式的，也就是说每个page都有个对应的id（id是object类型），
         * 需要对page操作的时候都是通过id来完成的
         * 看下addNewItem方法，会找到instantiateItem的使用方法，注意这里的mItems变量。然后你再搜索下isViewFromObject，会发现其被infoForChild方法调用，返回值是ItemInfo。
         * 再去看下ItemInfo的结构，其中有一个object对象，该值就是instantiateItem返回的。
         * 也就是说，ViewPager里面用了一个mItems(ArrayList)来存储每个page的信息(ItemInfo)，
         * 当界面要展示或者发生变化时，需要依据page的当前信息来调整，但此时只能通过view来查找，所以只能遍历mItems通过比较view和object来找到对应的ItemInfo。
         */
        // 根据传来的key，找到view,判断与传来的参数View o
        return view == viewList.get(Integer.parseInt(o.toString()));
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.i("Zero","destroyItem, position: " + position + ", object: " + object + ", container: " + container);
        container.removeView(viewList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i("Zero","instantiateItem, position: " + position +  ", container: " + container);
        View view = viewList.get(position);
        container.addView(view);
        // 把当前新增视图的位置（position）作为Key传过去
        return position;
    }
}
