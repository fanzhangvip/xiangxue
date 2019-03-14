package com.enjoy.zero.cachedemo.ZImage;

public class SimpleRequestPolicy implements RequestPolicy {

    @Override
    public int compareTo(Request request1, Request request2) {
        return request2.getLevel() - request1.getLevel();
    }
}
