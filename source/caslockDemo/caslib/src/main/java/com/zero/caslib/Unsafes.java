package com.zero.caslib;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class Unsafes {

    public static Unsafe getUnsafe(){
        Field f = null;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        Unsafe unsafe = null;
        try {
            unsafe = (Unsafe) f.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return unsafe;
    }


}
