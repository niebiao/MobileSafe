package com.niebiao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends BaseSetupActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	public void next(View view) {
		showNext();
	}
	
	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();
		//要求在finish()或startActivity(intent)后面
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	@Override
	public void showBack() {
		// TODO Auto-generated method stub
		
	}
}
