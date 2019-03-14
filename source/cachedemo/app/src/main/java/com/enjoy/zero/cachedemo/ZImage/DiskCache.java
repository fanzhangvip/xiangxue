package com.enjoy.zero.cachedemo.ZImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class DiskCache implements Cache {
    private static volatile DiskCache mDiskCache;
    //缓存路径
    private String mCacheDir = "Image";
    //MB
    private static final int MB = 1024 * 1024;
    //jackwharton的杰作
    private DiskLruCache mDiskLruCache;

    private DiskCache(Context context)
    {
        iniDiskCache(context);
    }
    public static DiskCache getInstance(Context context) {
        if(mDiskCache==null)
        {
            synchronized (DiskCache.class)
            {
                if(mDiskCache==null)
                {
                    mDiskCache=new DiskCache(context);
                }
            }
        }
        return mDiskCache;
    }
    private void iniDiskCache(Context context) {
        //得到缓存的目录  android/data/data/com.xxx/cache/Image
        File directory=getDiskCache(mCacheDir,context);
        if(!directory.exists())
        {
            directory.mkdirs();
        }
        try {
            //最后一个参数 指定缓存容量
            mDiskLruCache=DiskLruCache.open(directory,1,1,50*MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCache(String mCacheDir, Context context) {
        //默认缓存路径
        return new File(context.getCacheDir(),mCacheDir);
        //return new File(Environment.getExternalStorageDirectory(),mCacheDir);
    }

    @Override
    public void put(Request request, Source source) {
        if (mDiskLruCache == null) return;
        DiskLruCache.Editor edtor=null;
        OutputStream os=null;
        try {
            //路径必须是合法字符
            edtor=mDiskLruCache.edit(request.getImageUrlKey());
            os=edtor.newOutputStream(0);
            if(persistBitmap2Disk(source.asBitmap(),os))
            {
                edtor.commit();
            }else {
                edtor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean persistBitmap2Disk(Bitmap bitmap, OutputStream os) {
        BufferedOutputStream bos=new BufferedOutputStream(os);

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();

        }finally {
            IOUtil.closeQuietly(bos);
        }
        return true;

    }

    @Override
    public Source get(Request request) {
        if (mDiskLruCache == null) return null;
        try {
            DiskLruCache.Snapshot snapshot=mDiskLruCache.get(request.getImageUrlKey());
            if(snapshot!=null)
            {
                InputStream inputStream=snapshot.getInputStream(0);
                return new BitmapSource(BitmapFactory.decodeStream(inputStream));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(Request request) {
        if (mDiskLruCache == null) return;
        try {
            mDiskLruCache.remove(request.getImageUrlKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
