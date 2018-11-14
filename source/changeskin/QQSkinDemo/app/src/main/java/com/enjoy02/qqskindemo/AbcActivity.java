package com.enjoy02.qqskindemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * http://blog.csdn.net/cc_want/article/details/49179109
 * @author CSDN id:cc_want
 */
public class AbcActivity extends Activity{
	
	private Button mBtnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_abc);
		
		mBtnBack=(Button)findViewById(R.id.btn);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		
	}

}
