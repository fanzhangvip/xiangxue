package com.mxnavi.cube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.mxnavi.view.CubeSurfaceView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout openLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CubeSurfaceView cubeSurfaceView = new CubeSurfaceView(this);
        setContentView(R.layout.activity_main);
        openLayout = findViewById(R.id.open_layout);
        CubeSurfaceView cubeSurfaceView = new CubeSurfaceView(this);
        openLayout.addView(cubeSurfaceView);
    }

}
