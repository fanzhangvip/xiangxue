package com.zero.enjoy01.launchmodedemo01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SingleTaskActivity extends BaseActivity {

    private static int sCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sCount++;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    public void onStandard(View view){

        Intent intent = new Intent(this,StandardActivity.class);
        startActivity(intent);

    }

    public void onSingleTop(View view){
        Intent intent = new Intent(this,SingleTopActivity.class);
        startActivity(intent);

    }

    public void onSingleTask(View view){
        Intent intent = new Intent(this,SingleTaskActivity.class);
        startActivity(intent);
    }

    public void onSingleInstance(View view){
        Intent intent = new Intent(this,SingleInstanceActivity.class);
        startActivity(intent);
    }

    public void onActionSingleTask(View view){
        Intent intent = new Intent("com.zero.enjoy.action.TEST_SINGLETASK2");
        startActivity(intent);
    }

    public void onActionSingleInstance(View view){
        Intent intent = new Intent("com.zero.enjoy.action.TEST_SINGLEINSTANCE2");
        startActivity(intent);
    }


}

