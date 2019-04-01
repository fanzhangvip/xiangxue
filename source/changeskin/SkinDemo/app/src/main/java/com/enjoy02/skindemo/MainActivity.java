package com.enjoy02.skindemo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


/**
 * [享学课堂]
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 咨询伊娜老师QQ 2133576719
 */
public class MainActivity extends AppCompatActivity {

    ConstraintLayout rootview;
    ZeroView zeroView;

    TextView textView;
    Button button;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private SkinFactory mSkinFactory;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 关键点1：hook系统创建view的过程
        mSkinFactory = new SkinFactory();
        mSkinFactory.setDelegate(getDelegate());
        LayoutInflater.from(this).setFactory2(mSkinFactory);
        super.onCreate(savedInstanceState);
        //setFactory进去
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        rootview = findViewById(R.id.rootview);
        zeroView = findViewById(R.id.zeroView);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSkin();
//                changeSkin01();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void changeSkin01() {
        rootview.setBackground(getResources().getDrawable(R.drawable.bg1));
        zeroView.setBackgroundResource(R.drawable.girl1);
        textView.setTextColor(getResources().getColor(R.color.textview));
        button.setTextColor(getResources().getColor(R.color.button));
//        getResources().getDrawable()
    }

    public void changeSkin() {
        File skinFile = new File(Environment.getExternalStorageDirectory(), "skin.apk");
        SkinEngine.getInstance().load(skinFile.getAbsolutePath());
        mSkinFactory.changeSkin();
    }
}
