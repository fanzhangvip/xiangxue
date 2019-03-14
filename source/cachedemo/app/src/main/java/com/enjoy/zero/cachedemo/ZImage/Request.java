package com.enjoy.zero.cachedemo.ZImage;

public interface Request {
    DisplayConfig getDisplayConfig();
    RequestPolicy getRequestPolicy();

    int getLevel();

    void setLevel(int lev);

    Target getTarget();

    String getImageUrl();
    String getImageUrlKey();



}
