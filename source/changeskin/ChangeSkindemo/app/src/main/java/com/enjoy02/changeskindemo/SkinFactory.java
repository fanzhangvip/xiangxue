package com.enjoy02.changeskindemo;

import android.content.Context;
import android.content.res.TypedArray;
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
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
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

    static class SkinView {
        View view;
        HashMap<String, String> attrsMap;

        public void changeSkin() {
            if (!TextUtils.isEmpty(attrsMap.get("background"))) {
                int bgId = Integer.parseInt(attrsMap.get("background").substring(1));
                String attrType = view.getResources().getResourceTypeName(bgId);
                if (TextUtils.equals(attrType, "drawable")) {
                    view.setBackgroundDrawable(SkinEngine.getInstance().getDrawable(bgId));
                } else if (TextUtils.equals(attrType, "color")) {
                    view.setBackgroundColor(SkinEngine.getInstance().getColor(bgId));
                }
            }

            if (view instanceof TextView) {
                if (!TextUtils.isEmpty(attrsMap.get("textColor"))) {
                    int textColorId = Integer.parseInt(attrsMap.get("textColor").substring(1));
                    ((TextView) view).setTextColor(SkinEngine.getInstance().getColor(textColorId));
                }
            }
        }
    }

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
//                Log.i("Zero", "clazz: " + clazz);
                if (clazz == null) {
                    return null;
                }
                constructor = clazz.getConstructor(mConstructorSignature);
//                Log.i("Zero", "constructor: " + constructor);
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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Skinable);
        boolean isSupport = a.getBoolean(R.styleable.Skinable_isSupport, false);
        if (isSupport) {
            final int Len = attrs.getAttributeCount();
            HashMap<String, String> attrmap = new HashMap<>();
            for (int i = 0; i < Len; i++) {
                String attrName = attrs.getAttributeName(i);
                String attrValue = attrs.getAttributeValue(i);
                attrmap.put(attrName, attrValue);
            }
            SkinView skinView = new SkinView();
            skinView.view = view;
            skinView.attrsMap = attrmap;
            cacheSkinView.add(skinView);
        }

    }

    public void changeSkin() {
        for (SkinView skinView : cacheSkinView) {
            skinView.changeSkin();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
