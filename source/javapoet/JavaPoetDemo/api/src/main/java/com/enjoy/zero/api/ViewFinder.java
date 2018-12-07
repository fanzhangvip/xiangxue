package com.enjoy.zero.api;

import android.view.View;

/**
 * ui查找器接口
 */
public interface ViewFinder {

    /**
     * 从 object中查找一个id的控件
     * @param object
     * @param id
     * @return
     */
    View findView(Object object, int id);
}
