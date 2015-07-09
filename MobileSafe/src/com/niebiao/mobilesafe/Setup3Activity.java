package com.niebiao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_phone = (EditText) findViewById(R.id.et_setup3_phone);
		et_phone.setText(sp.getString("safenumber", ""));
	}
	
	//响应按钮
		public void next(View view) {
			showNext();
		}
		public void back(View view) {
			 showBack();
		}
		//响应滑动
	@Override
	public void showNext() {
		String phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "还没设置安全号码", 1).show();
			return;
		}
		//保存安全号码
		Editor editor = sp.edit();
		editor.putString("safenumber", phone);
		editor.commit();
		
		Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_out, R.anim.tran_in);
		
	}
	@Override
	public void showBack() {
		Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);
		
	}
	//选择联系人
	public void select_contact(View view){
		Intent intent = new Intent(Setup3Activity.this, SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}
	//获取联系人列表返回的结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data==null) {
			return ;
		}
		//获取传递回来的数据，并且把系统自动添加的电话号码中间的  -  去掉
		String phone = data.getStringExtra("phone").replace("-","");
		et_phone.setText(phone);
	}
	
}
