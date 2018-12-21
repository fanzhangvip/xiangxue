package com.enjoy.zero.cachedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.addItemDecoration(new GridDivider(this, 5, this.getResources().getColor(R.color.colorPrimary)));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new MyAdapter(this));


    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public String hashKeyForDisk(String key) {
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

    private String bytesToHexString(byte[] bytes) {
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


    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            RecyclerView.ViewHolder holder = null;
            View view = inflater.inflate(R.layout.recycler_item, viewGroup, false);
            holder = new VHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder t, int i) {
            String url = Images.imageThumbUrls[i];
            final VHolder vHolder = (VHolder) t;
            vHolder.imageView.setTag(url);
            BitmapThread bitmapThread = new BitmapThread(MainActivity.this, url);
            bitmapThread.setImageLoadListener(new BitmapThread.ImageLoadListener() {
                @Override
                public void imageLoad(String bitmapUrl, Bitmap bitmap) {
                    String myUrl = (String) vHolder.imageView.getTag();
                    if (TextUtils.equals(myUrl, bitmapUrl)) {
                        vHolder.imageView.setImageBitmap(bitmap);
                    }
                }
            });
            bitmapThread.start();
        }

        @Override
        public int getItemCount() {
            return Images.imageThumbUrls.length;
        }

        class VHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }


    static class BitmapThread extends Thread {
        private String bitmapUrl;

        private Handler handler = new Handler();

        private MainActivity mainActivity;

        private ImageLoadListener mImageLoadListener;

        public void setImageLoadListener(ImageLoadListener loadListener) {
            mImageLoadListener = loadListener;
        }

        interface ImageLoadListener {
            void imageLoad(String bitmapUrl, Bitmap bitmap);
        }

        BitmapThread(MainActivity activity, String bitmapUrl) {
            this.bitmapUrl = bitmapUrl;
            mainActivity = activity;
        }

        @Override
        public void run() {
            Log.i("Zero", "run: " + Thread.currentThread().getName());

            //存入内存缓存
            final Bitmap bitmapFromMemCache = mainActivity.getBitmapFromMemCache(bitmapUrl);
            if (bitmapFromMemCache != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageLoadListener.imageLoad(bitmapUrl, bitmapFromMemCache);
                    }
                });
                return;
            }

            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(bitmapUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
//                imageDownloader.addBitmapToMemory("bitmap", bitmap);
                final Bitmap resultBitmap = bitmap;
                if (resultBitmap == null) {
                    return;
                }
                mainActivity.addBitmapToMemoryCache(bitmapUrl, resultBitmap);
                Log.i("Zero", "resultBitmap: " + resultBitmap);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageLoadListener.imageLoad(bitmapUrl, resultBitmap);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Zero", "Exception: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Zero", "IOException: " + e.getMessage());
                    }
                }
            }
        }
    }
}
