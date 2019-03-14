package com.enjoy.zero.cachedemo.ZImage;

public interface RequestPolicy {

    /**
     * 两个请求的优先级比较
     * @param request1
     * @param request2
     * @return
     */
    int compareTo(Request request1, Request request2);
}
