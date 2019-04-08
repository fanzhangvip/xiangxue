package com.enjoy.zero.zhujiedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {


    private TextView mTextView;

     Button mButton;
     Button mButton1;
     Button mButton2;
     Button mButton3;
     Button mButton4;
     Button mButton5;
     Button mButton6;
     Button mButton7;
     Button mButton8;
     Button mButton9;
     Button mButton10;
     Button mButton11;
     Button mButton12;
     Button mButton13;
     Button mButton14;
     Button mButton15;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化UI
        initUI();
        //设置监听器
        setListener();
    }

    private void initUI() {
        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button);
        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton4 = findViewById(R.id.button4);
        mButton5 = findViewById(R.id.button5);
        mButton6 = findViewById(R.id.button6);
        mButton7 = findViewById(R.id.button7);
        mButton8 = findViewById(R.id.button8);
        mButton9 = findViewById(R.id.button9);
        mButton10 = findViewById(R.id.button10);
        mButton11 = findViewById(R.id.button11);
        mButton12 = findViewById(R.id.button12);
        mButton13 = findViewById(R.id.button13);
        mButton14 = findViewById(R.id.button14);
        mButton15 = findViewById(R.id.button15);
    }

    private void setListener() {
        mButton.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton10.setOnClickListener(this);
        mButton11.setOnClickListener(this);
        mButton12.setOnClickListener(this);
        mButton13.setOnClickListener(this);
        mButton14.setOnClickListener(this);
        mButton15.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button button = (Button) v;
            mTextView.setText(button.getText());
        }
    }
}
