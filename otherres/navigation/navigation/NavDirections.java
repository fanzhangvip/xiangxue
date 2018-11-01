

package com.me94me.example_navigatioin_resource.navigation;

import android.os.Bundle;

/**
 * 描述了导航从操作的接口
 * action的Id、参数
 */
public interface NavDirections {

    /**
     * 返回一个导航的actionId
     *
     * @return id of an action
     */
    int getActionId();

    /**
     * 导航操作携带的参数
     */
    Bundle getArguments();
}
