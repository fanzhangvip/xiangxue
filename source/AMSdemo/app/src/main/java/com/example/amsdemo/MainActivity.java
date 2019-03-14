package com.example.amsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

//    TextView mShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main1);
//        mShow = findViewById(R.id.tv_show);


//        new Thread(){
//            public void run(){
//                mShow.setText("onCreate中子线程更新UI");
//            }
//        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        new Thread(){
//            public void run(){
//                mShow.setText("onStart中子线程更新UI");
//            }
//        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Thread(){
//            public void run(){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mShow.setText("onResume中子线程更新UI");
//            }
//        }.start();
    }
}
