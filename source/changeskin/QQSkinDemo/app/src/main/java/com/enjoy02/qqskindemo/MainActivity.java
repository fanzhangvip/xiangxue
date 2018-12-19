package com.enjoy02.qqskindemo;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;



public class MainActivity extends Activity {

    private Button mBtnStart;
    private Button mBtnJump;
    private ImageView mImg;
    private Button mBtnCancel;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public static void verifyStoragePermissions(Activity activity) {

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
        super.onCreate(savedInstanceState);
        SkinEngine.getInstances().init(getResources());
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        mImg = (ImageView) findViewById(R.id.img1);
        mBtnStart = (Button) findViewById(R.id.btn);
        mBtnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SkinEngine.getInstances().init(getResources());
                SkinEngine.getInstances().addResource(getResources(), R.drawable.girl1);
                SkinEngine.getInstances().run();

                reloadDrawable();
            }
        });
        mBtnCancel = (Button)findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SkinEngine.getInstances().unInit();
                reloadDrawable();
            }
        });
        mBtnJump = (Button) findViewById(R.id.btn_jump);
        mBtnJump.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SkinEngine.getInstances().run();
                Intent intent = new Intent(MainActivity.this, AbcActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 注意：直接修改Resources中的Drawable并不会改变当前Activity控件上的图片
     * 这是因为当前页视图已经被创建，控件将图片已经缓存到控件上，此时我们可以重新加载一下图片即可
     */
    private void reloadDrawable() {
        mImg.setImageDrawable(getResources().getDrawable(R.drawable.girl));
    }


}
