package com.enjoy02.enjoyfragmentdemo02.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.enjoy02.enjoyfragmentdemo02.R;
import com.enjoy02.enjoyfragmentdemo02.fragment.Bug31Fragment;
import com.enjoy02.enjoyfragmentdemo02.fragment.Bug3Fragment;

/**
 * TODO: Fragment重叠异常
 */
public class Bug41Activity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug41);

        String param = getIntent().getStringExtra("param");

        TextView show = findViewById(R.id.showparam);
        show.setText(param);

        findViewById(R.id.bntReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("returnParam", "我是从41返回的");
                setResult(100, data);
                finish();
            }
        });

    }

}
