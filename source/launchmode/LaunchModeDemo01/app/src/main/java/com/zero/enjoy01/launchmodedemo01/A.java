package com.zero.enjoy01.launchmodedemo01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class A extends BaseActivity {

    private static int sCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sCount++;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abcd);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sCount--;
    }

    @Override
    public int getInstacnceCount() {
        return sCount;
    }

    @Override
    public TextView getTextView() {
        return findViewById(R.id.textView);
    }


    public void onGo(View view){

        Intent intent = new Intent(this,B.class);
        startActivity(intent);

    }



}

