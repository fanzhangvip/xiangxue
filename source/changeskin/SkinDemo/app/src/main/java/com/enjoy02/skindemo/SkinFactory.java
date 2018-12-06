package com.enjoy02.skindemo;

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

        // TODO: 关键点1：
        View view = mDelegate.createView(parent, name, context, attrs);
        if (view == null) {
            mConstructorArgs[0] = context;
            try {//替代系统来创建view
                if (-1 == name.indexOf('.')) {//TextView
                    // 如果View的name中不包含 '.' 则说明是系统控件，会在接下来的调用链在name前面加上 'android.view.'
                    view = createView(context, name, prefixs, attrs);
                } else {
                    // 如果name中包含 '.' 则直接调用createView方法，onCreateView 后续也是调用了createView
                    view = createView(context, name, null, attrs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //TODO: 关键点2 收集需要换肤的View
        collectSkinView(context,attrs,view);

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
                if (clazz == null) {
                    return null;
                }
                constructor = clazz.getConstructor(mConstructorSignature);
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
            //通过反射创建View对象
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

        // 获取我们自己定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Skinable);
        boolean isSupport = a.getBoolean(R.styleable.Skinable_isSupport,false);
        if(isSupport){
            final int Len = attrs.getAttributeCount();
            HashMap<String,String> attrmap = new HashMap<>();
            for(int i = 0; i < Len;i++){
                String attrName = attrs.getAttributeName(i);
                String attrValue = attrs.getAttributeValue(i);
                Log.i("Zero","attrName: " + attrName + " attrValue: " + attrValue);
                attrmap.put(attrName,attrValue);
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

static class SkinView {
    View view;
    HashMap<String, String> attrsMap;

    /**
     * TODO: 应用换肤
     */
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
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
