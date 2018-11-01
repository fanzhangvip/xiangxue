package com.me94me.example_navigatioin_resource.navigation;

import android.os.Bundle;
import android.view.View;

/**
 * NavHost通过{@link NavController}进行上下文的导航
 *
 * Navigation做了两件事:
 * 1、处理controller的状态({@link NavController#saveState()} and {@link NavController#restoreState(Bundle)})
 * 2、在root view上调用了{@link Navigation#setViewNavController(View, NavController)}
 */
public interface NavHost {

    /**
     * 返回了host的controller{@link NavController}
     */
    NavController getNavController();
}
