package com.enjoy02.skindemo;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * [享学课堂]
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 咨询伊娜老师QQ 2133576719
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    private AppCompatDelegate mDelegate;
    private List<SkinView> cacheSkinView = new ArrayList<>();


    public void setDelegate(AppCompatDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    final Object[] mConstructorArgs = new Object[2];

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();
    static final String[] prefixs = new String[]{
            "android.widget.",
            "android.view.",
            "android.webkit."
    };


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = mDelegate.createView(parent, name, context, attrs);
        if (view == null) {
            mConstructorArgs[0] = context;

            if (-1 == name.indexOf('.')) {// TODO: 系统的view
                view = createView(context, name, prefixs, attrs);
            } else {// TODO: 自定义的view
                view = createView(context, name, null, attrs);
            }
        }
        Log.i("Zero", "name: " + name + " view: " + view);
        if (view != null) {
            collectSkinView(context, attrs, view);
        }
        return view;
    }


    public final View createView(Context context, String name, String[] prefixs, AttributeSet attrs) {

        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz = null;

        if (constructor == null) {
            try {
                if (prefixs != null && prefixs.length > 0) {
                    for (String prefix : prefixs) {
                        clazz = context.getClassLoader().loadClass(
                                prefix != null ? (prefix + name) : name).asSubclass(View.class);
                        if (clazz != null) break;
                    }
                } else {
                    if (clazz == null) {
                        clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                    }
                }

                Log.i("Zero", "clazz: " + clazz);
                if (clazz == null) {
                    return null;
                }
                constructor = clazz.getConstructor(mConstructorSignature);
                Log.i("Zero", "constructor: " + constructor);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            constructor.setAccessible(true);
            sConstructorMap.put(name, constructor);
        }
        Object[] args = mConstructorArgs;
        args[1] = attrs;
        try {
            final View view = constructor.newInstance(args);
            return view;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
    * TODO: 收集需要换肤的控件
    */
    private void collectSkinView(Context context, AttributeSet attrs, View view) {

        final int Len = attrs.getAttributeCount();
        List<SkinItem> skinItemList = new ArrayList<>();
        for (int i = 0; i < Len; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
//            Log.i("Zero","attrName: " + attrName + " attrValue: " + attrValue);
            if (TextUtils.equals(attrName, "textColor") || TextUtils.equals(attrName, "background")) {
                SkinItem skinItem = new SkinItem();
                skinItem.attrName = attrName;
                skinItem.id = Integer.parseInt(attrValue.substring(1));
                skinItem.attrValue = context.getResources().getResourceEntryName(skinItem.id);
                skinItem.attrType = context.getResources().getResourceTypeName(skinItem.id);
                Log.i("Zero", "skinItem: " + skinItem);
                skinItemList.add(skinItem);
            }
        }
        if (!skinItemList.isEmpty()) {
            SkinView skinView = new SkinView();
            skinView.view = view;
            skinView.skinItemList = skinItemList;
            cacheSkinView.add(skinView);
            skinView.changeSkin();
        }
    }

    public void changeSkin() {
        for (SkinView skinView : cacheSkinView) {
            skinView.changeSkin();
        }
    }

    static class SkinView {
        View view;
        List<SkinItem> skinItemList;

        /**
         * TODO: 换肤
         */
        public void changeSkin() {
            for (SkinItem skinItem : skinItemList) {
                if (TextUtils.equals(skinItem.attrName, "textColor")) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(SkinEngine.getInstance().getColor(skinItem.id));
                    }
                } else if (TextUtils.equals(skinItem.attrName, "background")) {
                    if (TextUtils.equals(skinItem.attrType, "drawable")) {
                        view.setBackgroundDrawable(SkinEngine.getInstance().getDrawable(skinItem.id));
                    } else if (TextUtils.equals(skinItem.attrType, "color")) {
                        view.setBackgroundColor(SkinEngine.getInstance().getColor(skinItem.id));
                    }
                }
            }
        }
    }

    static class SkinItem {
        String attrName;
        int id;
        String attrValue;
        String attrType;

        @Override
        public String toString() {
            return "SkinItem{" +
                    "attrName='" + attrName + '\'' +
                    ", id=" + id +
                    ", attrValue='" + attrValue + '\'' +
                    ", attrType='" + attrType + '\'' +
                    '}';
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
