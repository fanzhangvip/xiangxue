package com.enjoy.zero.cachedemo.ZImage;

public interface Cache {

    Source get(Request request);

    void put(Request request,Source source);

    void remove(Request request);
}
