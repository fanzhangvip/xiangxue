package com.enjoy.zero.cachedemo.ZImage;

import android.widget.ImageView;

public class ImageViewTarget implements Target {

    private ImageView  imageView;

    public ImageViewTarget(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    public void setSource(Source source) {
        imageView.setImageBitmap(source.asBitmap());
    }

    @Override
    public int getWidth() {
        return imageView.getWidth();
    }

    @Override
    public int getHeight() {
        return imageView.getHeight();
    }
}
