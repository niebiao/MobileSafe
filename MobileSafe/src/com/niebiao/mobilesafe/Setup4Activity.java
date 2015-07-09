package com.niebiao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
//	private SharedPreferences sp;
	private CheckBox cb_protected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		cb_protected = (CheckBox) findViewById(R.id.cb_setup4_protected);
		boolean protecting=sp.getBoolean("protecting", false);
		if (protecting) {
			cb_protected.setText("手机防盗已开启");
			cb_protected.setChecked(true);
		}else {
			cb_protected.setText("手机防盗已关闭");
			cb_protected.setChecked(false);
		}
		cb_protected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cb_protected.setText("手机防盗已开启");
				}else {
					cb_protected.setText("手机防盗已关闭");
				}
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
	}
	//响应按钮
	public void back(View view) {
		 showBack();
	}
	public void finished(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent = new Intent(Setup4Activity.this, LostFindActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void showNext() {
		
	}

	@Override
	public void showBack() {
		Intent intent = new Intent(Setup4Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);		
	}
	
}
