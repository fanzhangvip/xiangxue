package com.enjoy.zero.cachedemo.ZImage;

import android.graphics.Bitmap;

public class BitmapSource implements Source {

    private Bitmap bitmap;

    public BitmapSource(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Override
    public Bitmap asBitmap() {
        return bitmap;
    }

    @Override
    public int getSize() {
        return bitmap.getRowBytes()*bitmap.getHeight();
    }
}
