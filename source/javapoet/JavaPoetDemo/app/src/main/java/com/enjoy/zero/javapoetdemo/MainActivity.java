package com.enjoy.zero.javapoetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.enjoy.zero.annotations.BindView;
import com.enjoy.zero.annotations.MyAnnotation;
import com.enjoy.zero.api.LCJViewBinder;


@MyAnnotation("Hello World")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.test)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LCJViewBinder.bind(this);
        mTextView.setText("hahahahahah.....");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LCJViewBinder.unBind(this);
    }
}
