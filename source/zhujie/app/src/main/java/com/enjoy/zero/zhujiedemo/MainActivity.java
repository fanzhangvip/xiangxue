package com.enjoy.zero.zhujiedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.button8)
    Button button8;
    @InjectView(R.id.button9)
    Button button9;
    @InjectView(R.id.button10)
    Button button10;
    @InjectView(R.id.button11)
    Button button11;
    @InjectView(R.id.button12)
    Button button12;
    @InjectView(R.id.button13)
    Button button13;
    @InjectView(R.id.button14)
    Button button14;
    @InjectView(R.id.button15)
    Button button15;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.button)
    Button button;
    @InjectView(R.id.button1)
    Button button1;
    @InjectView(R.id.button2)
    Button button2;
    @InjectView(R.id.button3)
    Button button3;
    @InjectView(R.id.button4)
    Button button4;
    @InjectView(R.id.button5)
    Button button5;
    @InjectView(R.id.button6)
    Button button6;
    @InjectView(R.id.button7)
    Button button7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InjectViewUtils.injectView(this);
        InjectViewUtils.injectEvent(this);

    }


//    @Override
    public void test(){}

    @Onclick({R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.textView, R.id.button, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7})
    public void onViewClicked(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;
            textView.setText(button.getText());
        }
        Log.i("Zero", "bind view Click");
    }
}
