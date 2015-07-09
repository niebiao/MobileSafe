package com.niebiao.mobilesafe;

import com.niebiao.mobilesafe.ui.SettingItemView;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {
	private SettingItemView siv_band;
	//��ȡ�ֻ�SIM������Ϣ
	private TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		siv_band=(SettingItemView) findViewById(R.id.siv_setup2_band);
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String sim = sp.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			siv_band.setCheck(false);
		}else {
			siv_band.setCheck(true);
		}
		siv_band.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (siv_band.isChecked()) {
					siv_band.setCheck(false);
					editor.putString("sim", null);
				}else {
					siv_band.setCheck(true);
					//����SIM�����к�
					String sim=tm.getSimSerialNumber();
					editor.putString("sim", sim);
				}
				editor.commit();
			}
		});
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
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			//û�ð�SIM�������ܽ�����һ��
			Toast.makeText(this, "SIM��û�ð�", 1).show();
			return ;
		}
		Intent intent = new Intent(Setup2Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}
	@Override
	public void showBack() {
		Intent intent = new Intent(Setup2Activity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);
		
	}
	
}
