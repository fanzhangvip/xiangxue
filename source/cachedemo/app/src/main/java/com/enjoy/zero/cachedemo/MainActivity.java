package com.enjoy.zero.cachedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.addItemDecoration(new GridDivider(this, 5, this.getResources().getColor(R.color.colorPrimary)));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new MyAdapter(this));


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
            BitmapThread bitmapThread = new BitmapThread(url);
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

        private ImageLoadListener mImageLoadListener;

        public void setImageLoadListener(ImageLoadListener loadListener) {
            mImageLoadListener = loadListener;
        }

        interface ImageLoadListener {
            void imageLoad(String bitmapUrl, Bitmap bitmap);
        }

        BitmapThread(String bitmapUrl) {
            this.bitmapUrl = bitmapUrl;
        }

        @Override
        public void run() {
            Log.i("Zero", "run: " + Thread.currentThread().getName());
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
