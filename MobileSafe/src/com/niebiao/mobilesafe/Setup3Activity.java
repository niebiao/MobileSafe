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
	
	//��Ӧ��ť
		public void next(View view) {
			showNext();
		}
		public void back(View view) {
			 showBack();
		}
		//��Ӧ����
	@Override
	public void showNext() {
		String phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "��û���ð�ȫ����", 1).show();
			return;
		}
		//���氲ȫ����
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
	//ѡ����ϵ��
	public void select_contact(View view){
		Intent intent = new Intent(Setup3Activity.this, SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}
	//��ȡ��ϵ���б��صĽ��
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data==null) {
			return ;
		}
		//��ȡ���ݻ��������ݣ����Ұ�ϵͳ�Զ���ӵĵ绰�����м��  -  ȥ��
		String phone = data.getStringExtra("phone").replace("-","");
		et_phone.setText(phone);
	}
	
}
