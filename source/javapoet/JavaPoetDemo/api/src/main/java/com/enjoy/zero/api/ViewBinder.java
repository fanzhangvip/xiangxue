package com.enjoy.zero.api;

/**
 * UI绑定解绑接口
 *
 * @param
 */
public interface ViewBinder<T> {

    void bindView(T host, Object object, ViewFinder finder);

    void unBindView(T host);
}
