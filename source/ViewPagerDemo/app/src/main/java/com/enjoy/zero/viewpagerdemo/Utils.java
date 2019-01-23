package com.enjoy.zero.viewpagerdemo;

import android.graphics.Color;
import android.support.v4.view.ViewPager;

import java.util.Random;

public class Utils {

    public static int getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }

    public static int getRandom(final int min, final int max) {
        // randNumber 将被赋值为一个 MIN 和 MAX 范围内的随机数
        Random random = new Random();
        int bound = Math.abs(max - min) + 1;
        int randNumber = random.nextInt(bound) + min;
        return randNumber;
    }


    public static String pagerScrollStateToString(final int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                return "SCROLL_STATE_IDLE";
            case ViewPager.SCROLL_STATE_DRAGGING:
                return "SCROLL_STATE_DRAGGING";
            case ViewPager.SCROLL_STATE_SETTLING:
                return "SCROLL_STATE_SETTLING";
            default:
                return "UnKnown";
        }
    }
}
