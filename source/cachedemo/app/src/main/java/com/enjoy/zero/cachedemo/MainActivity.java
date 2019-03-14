package com.enjoy.zero.cachedemo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.enjoy.zero.cachedemo.decoration.GridDividerItemDecoration;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};



    public static void verifyStoragePermissions(AppCompatActivity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String IMAGE_CACHE_DIR = "image_cache";

    RecyclerView recyclerView;
    private ImageWorker mImageWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        //缓存设置
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(MainActivity.this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.01f);
        cacheParams.memoryCacheEnabled = true;//内存缓存设置
        cacheParams.diskCacheEnabled = true;//磁盘缓存设置


        mImageWork = new ImageWorker(MainActivity.this);
        mImageWork.setLoadingImage(R.drawable.empty_photo);
        mImageWork.addImageCache(MainActivity.this.getSupportFragmentManager(), cacheParams);

        recyclerView.addItemDecoration(new GridDividerItemDecoration(this));
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
            mImageWork.loadImage(url, vHolder.imageView);
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

}
