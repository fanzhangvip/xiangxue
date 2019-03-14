/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.enjoy.zero.cachedemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.enjoy.zero.cachedemo.diskcache.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ImageCache {
    private static final String TAG = "Zero";

    //默认缓存大小
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB

    // 默认磁盘缓存大小
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    // 缓存到磁盘的参数
    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;
    private static final int DISK_CACHE_INDEX = 0;

    //默认缓存配置参数
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false;

    private DiskLruCache mDiskLruCache;
    private LruCache<String, Bitmap> mMemoryCache;
    private ImageCacheParams mCacheParams;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;


    private ImageCache(ImageCacheParams cacheParams) {
        init(cacheParams);
    }

    /**
     * 缓存单例
     */
    public static ImageCache getInstance(
            FragmentManager fragmentManager, ImageCacheParams cacheParams) {

        final RetainFragment mRetainFragment = findOrCreateRetainFragment(fragmentManager);

        ImageCache imageCache = (ImageCache) mRetainFragment.getObject();

        if (imageCache == null) {
            imageCache = new ImageCache(cacheParams);
            mRetainFragment.setObject(imageCache);
        }
        return imageCache;
    }

    /**
     * 根据缓存参数，初始化缓存
     *
     * @param cacheParams 缓存参数
     */
    private void init(ImageCacheParams cacheParams) {
        mCacheParams = cacheParams;

        if (mCacheParams.memoryCacheEnabled) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "缓存大小: (size = " + (int) (mCacheParams.memCacheSize / 1024) + "M )");
            }

            //初始化MemoryCache
            mMemoryCache = new LruCache<String, Bitmap>(mCacheParams.memCacheSize) {

                @Override
                protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                    if (evicted) {
                        Log.i(TAG, "该条目被删除空间");
                    } else {
                        if (newValue != null) {
                            Log.i(TAG, "被put()或get()调用");
                        }
                    }
                }

                @Override
                protected int sizeOf(String key, Bitmap value) {
                    final int bitmapSize = getBitmapSize(value) / 1024;
                    return bitmapSize == 0 ? 1 : bitmapSize;
                }
            };
        }

        //初始化DiskCache
        if (cacheParams.initDiskCacheOnCreate) {
            initDiskCache();
        }
    }

    /**
     * 初始化磁盘缓存
     */
    public void initDiskCache() {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
                File diskCacheDir = mCacheParams.diskCacheDir;
                if (mCacheParams.diskCacheEnabled && diskCacheDir != null) {
                    if (!diskCacheDir.exists()) {
                        diskCacheDir.mkdirs();
                    }
                    if (getUsableSpace(diskCacheDir) > mCacheParams.diskCacheSize) {
                        try {
                            mDiskLruCache = DiskLruCache.open(
                                    diskCacheDir, 1, 1, mCacheParams.diskCacheSize);
                            Log.d(TAG, "磁盘缓存初始化");
                        } catch (final IOException e) {
                            mCacheParams.diskCacheDir = null;
                            Log.e(TAG, "磁盘缓存初始化失败 - " + e);
                        }
                    }
                }
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }

    public void addBitmapToMemoryCache(String data, Bitmap value) {
        if (data == null || value == null) {
            return;
        }
        if (mMemoryCache != null) {
            mMemoryCache.put(data, value);
        }
    }

    /**
     * 添加数据到磁盘缓存
     *
     * @param data  URL
     * @param value 要缓存的数据
     */
    public void addBitmapToDiskCache(String data, Bitmap value) {
        if (data == null || value == null) {
            return;
        }
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                final String key = hashKeyForDisk(data);
                OutputStream out = null;
                try {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot == null) {
                        final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null) {
                            out = editor.newOutputStream(DISK_CACHE_INDEX);
                            value.compress(
                                    mCacheParams.compressFormat, mCacheParams.compressQuality, out);
                            editor.commit();
                            out.close();
                        }
                    } else {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                    }
                } catch (final IOException e) {
                    Log.e(TAG, "添加到磁盘缓存IO异常 - " + e);
                } catch (Exception e) {
                    Log.e(TAG, "添加到磁盘缓存异常 - " + e);
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
     * 获取内存缓存
     *
     * @param data URL
     * @return 如果找到 返回bitmap,否则返回null
     */
    public Bitmap getBitmapFromMemCache(String data) {
        Bitmap memValue = null;
        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(data);
        }
        if (memValue == null) {
            Log.d(TAG, "内存缓存miss");
        } else {
            Log.d(TAG, "内存缓存命中");
        }
        return memValue;
    }

    /**
     * 从磁盘缓存获取
     *
     * @param data URL
     * @return 如果找到 返回bitmap,否则返回null
     */
    public Bitmap getBitmapFromDiskCache(String data) {
        final String key = hashKeyForDisk(data);
        Bitmap bitmap = null;

        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskLruCache != null) {
                InputStream inputStream = null;
                try {
                    final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null) {
                        inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
                        if (inputStream != null) {
                            FileDescriptor fd = ((FileInputStream) inputStream).getFD();
                            bitmap = decodeSampledBitmapFromDescriptor(
                                    fd, Integer.MAX_VALUE, Integer.MAX_VALUE);
                        }
                    }
                } catch (final IOException e) {
                    Log.e(TAG, "从磁盘中读取缓存IO异常 - " + e);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
            if (bitmap != null) {
                Log.d(TAG, "磁盘缓存命中");
            } else {
                Log.d(TAG, "磁盘缓存miss");
            }
            return bitmap;
        }
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(
            FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // 计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        // 尝试复用
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options);
        }

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }


            long totalPixels = width * height / inSampleSize;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

    private static void addInBitmapOptions(BitmapFactory.Options options) {
        options.inMutable = true;
    }

    /**
     * 清空缓存， 不可在UI主线程执行
     */
    public void clearCache() {
        if (isMainThread()) {
            throw new RuntimeException("this should not be executed on the main/UI thread");
        }
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "清除内存缓存");
            }
        }

        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;
            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                try {
                    mDiskLruCache.delete();
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "清除磁盘缓存");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "清除IO异常 - " + e);
                }
                mDiskLruCache = null;
                initDiskCache();
            }
        }
    }

    /**
     * 刷新缓存，不可在UI主线程执行
     */
    public void flush() {
        if (isMainThread()) {
            throw new RuntimeException("this should not be executed on the main/UI thread");
        }
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                try {
                    mDiskLruCache.flush();
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "磁盘缓存刷新");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "磁盘缓存刷新IO异常 - " + e);
                }
            }
        }
    }

    /**
     * 关闭磁盘缓存，不可在UI主线程执行
     */
    public void close() {
        if (isMainThread()) {
            throw new RuntimeException("this should not be executed on the main/UI thread");
        }
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                try {
                    if (!mDiskLruCache.isClosed()) {
                        mDiskLruCache.close();
                        mDiskLruCache = null;
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "磁盘缓存关闭");
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "磁盘缓存关闭IO异常 - " + e);
                }
            }
        }
    }

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 缓存参数类
     */
    public static class ImageCacheParams {
        public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
        public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        public File diskCacheDir;
        public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
        public int compressQuality = DEFAULT_COMPRESS_QUALITY;
        public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
        public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
        public boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;

        /**
         * 创建一个缓存目录
         *
         * @param context                可用的上下文
         * @param diskCacheDirectoryName 缓存目录名称
         */
        public ImageCacheParams(Context context, String diskCacheDirectoryName) {
            diskCacheDir = getDiskCacheDir(context, diskCacheDirectoryName);
            Log.i(TAG,"diskCacheDir: " + diskCacheDir);
        }

        /**
         * 设置内存缓存占当期app可用内存的百分比
         *
         * @param percent 可用app内存的百分比
         */
        public void setMemCacheSizePercent(float percent) {
            if (percent < 0.01f || percent > 0.8f) {
                throw new IllegalArgumentException("内存大小必须在 "
                        + "0.01 到0.8 (保护)");
            }
            memCacheSize = Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
        }
    }


    /**
     * 返回不同Config下每像素所占的字节数
     *
     * @param config Bitmap的Config
     * @return 每像素所占的字节数
     */
    private static int getBytesPerPixel(Config config) {
        if (config == Config.ARGB_8888) {
            return 4;
        } else if (config == Config.RGB_565) {
            return 2;
        } else if (config == Config.ARGB_4444) {
            return 2;
        } else if (config == Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    /**
     * 获取缓存目录
     *
     * @param context    上下文
     * @param uniqueName 一个唯一的缓存目录名称
     * @return 缓存目录
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {

         String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();
        cachePath = "/sdcard";

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 计算图片URL经过MD5加密后的值，用于DiskCache的key
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 返回Bitmap占用的内存
     *
     * @param bitmap
     * @return size in bytes
     */
    @TargetApi(VERSION_CODES.KITKAT)
    public static int getBitmapSize(Bitmap bitmap) {
        // 一个BitMap位图占用的内存=图片长度*图片宽度*单位像素占用的字节数
        if (Utils.hasKitKat()) {
            //getAllocationByteCount()返回的是复用图像所占内存的大小
            //getAllocationByteCount()与getByteCount()的返回值一般情况下都是相等的。只是在图片
            // 复用的时候，getAllocationByteCount()返回的是复用图像所占内存的大小
            return bitmap.getAllocationByteCount();
        }

        if (Utils.hasHoneycombMR1()) {
            //getByteCount()返回的是新解码图片占用内存的大小
            return bitmap.getByteCount();
        }
        //getRowBytes()返回的是每行的像素值，乘以高度就是总的像素数，也就是占用内存的大小
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 检查sdcard挂载
     */
    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * 获取缓存目录
     */
    @TargetApi(VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalCacheDir();
        }

        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * 检查sdcard剩余可用空间
     */
    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (Utils.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }


    private static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
        RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainFragment, TAG).commitAllowingStateLoss();
        }

        return mRetainFragment;
    }

    public static class RetainFragment extends Fragment {
        private Object mObject;


        public RetainFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void setObject(Object object) {
            mObject = object;
        }

        public Object getObject() {
            return mObject;
        }
    }

}
