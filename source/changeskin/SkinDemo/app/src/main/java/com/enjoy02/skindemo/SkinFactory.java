package com.enjoy02.skindemo;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * [享学课堂]
 * 学无止境，让学习成为一种享受
 * TODO:主讲Zero老师QQ 2124346685
 * TODO:咨询伊娜老师QQ 2133576719
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    private AppCompatDelegate mDelegate;
    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    final Object[] mConstructorArgs = new Object[2];

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    static final String[] prefixs = new String[]{
            "android.widget.", "android.view."
    };

    public void setDelegate(AppCompatDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = mDelegate.createView(parent, name, context, attrs);

        
//        mConstructorArgs[0] = context;
//        View view;
//        Log.i("Zero", "name: " + name);
//        // TODO: zidingyi
//        if (-1 == name.indexOf('.')) {
//            view = createView(context, name, prefixs, attrs);
//        } else {
//            view = createView(context, name, null, attrs);
//        }
//        Log.i("Zero", "name: " + name + " view: " + view);
        return view;
    }

    public final View createView(Context context, String name, String[] prefixs, AttributeSet attrs) {

        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz = null;

        if (constructor == null) {
            try {
                for (String prefix : prefixs) {
                    clazz = context.getClassLoader().loadClass(
                            prefix != null ? (prefix + name) : name).asSubclass(View.class);
                    if (clazz != null) break;
                }
                Log.i("Zero", "clazz: " + clazz);
                constructor = clazz.getConstructor(mConstructorSignature);
            } catch (Exception e) {
                e.printStackTrace();
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

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
