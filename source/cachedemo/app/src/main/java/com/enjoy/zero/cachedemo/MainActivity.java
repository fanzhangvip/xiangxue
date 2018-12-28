package com.enjoy.zero.cachedemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_CACHE_DIR = "thumbs";

    RecyclerView recyclerView;
    private ImageWorker mImageWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(MainActivity.this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.1f);
        cacheParams.memoryCacheEnabled = false;
        cacheParams.diskCacheEnabled = false;


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
