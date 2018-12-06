package com.enjoy.zero.api;

import android.app.Activity;
import android.view.View;

/**
 * Activity UI查找提供者
 */

public class ActivityViewFinder implements ViewFinder {
    @Override
    public View findView(Object object, int id) {
        return ((Activity) object).findViewById(id);
    }
}
