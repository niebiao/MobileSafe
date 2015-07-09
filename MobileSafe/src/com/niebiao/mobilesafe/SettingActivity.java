   package com.niebiao.mobilesafe;

import com.niebiao.mobilesafe.service.AddressService;
import com.niebiao.mobilesafe.service.CallSmsSafeService;
import com.niebiao.mobilesafe.ui.SettingClickView;
import com.niebiao.mobilesafe.ui.SettingItemView;
import com.niebiao.mobilesafe.utils.ServerUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private SettingItemView siv_update;
	private SettingItemView Siv_showAddress;
	private SharedPreferences sp;
	private Intent showAddress;
	private boolean isRunning;
	//���ù�����toast������ɫ
	private SettingClickView scv_setaddressbg;
	//��������������
	private SettingItemView siv_callsmssafe;
	private Intent callsmssafe;
	
	//��Activity�ر�ʱ
	@Override
	protected void onResume() {
		super.onResume();
		showAddress=new Intent(this,AddressService.class);
		isRunning=ServerUtils.isRunning
				(this, "com.niebiao.mobilesafe.service.AddressService");
		if (isRunning) {
			Siv_showAddress.setCheck(true);
		}else {
			Siv_showAddress.setCheck(false);
		}
		//����������
		callsmssafe=new Intent(this,CallSmsSafeService.class);
		isRunning=ServerUtils.isRunning
				(this, "com.niebiao.mobilesafe.service.CallSmsSafeService");
		siv_callsmssafe.setCheck(isRunning);
//		if (isRunning) {
//			siv_callsmssafe.setCheck(true);
//		}else {
//			siv_callsmssafe.setCheck(false);
//		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		sp= getSharedPreferences("config", MODE_PRIVATE);
		boolean isUpdate = sp.getBoolean("isUpdate", false);
		if (isUpdate) {
			siv_update.setCheck(true);
		}else {
			siv_update.setCheck(false);
		}
		siv_update.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				//�Ѿ����Զ�����
					if (siv_update.isChecked()) {
						//����Ϊ������
						siv_update.setCheck(false);
						editor.putBoolean("isUpdate", false);
					}else {
						siv_update.setCheck(true);
						editor.putBoolean("isUpdate", true);
					}
					editor.commit();
			}
		});
		//��������������ʾ
		Siv_showAddress = (SettingItemView) findViewById(R.id.siv_show_address);
		showAddress=new Intent(this,AddressService.class);
		isRunning=ServerUtils.isRunning
				(this, "com.niebiao.mobilesafe.service.AddressService");
		if (isRunning) {
			Siv_showAddress.setCheck(true);
		}else {
			Siv_showAddress.setCheck(false);
		}
		Siv_showAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (Siv_showAddress.isChecked()) {
					//���Ϊ��ѡ��״̬
					Siv_showAddress.setCheck(false);
					stopService(showAddress);
				}else {
					Siv_showAddress.setCheck(true);
					startService(showAddress);
				}
			}
		});
		//���ù�����toast������ɫ
		scv_setaddressbg =(SettingClickView) findViewById(R.id.scv_set_addressbg);
		scv_setaddressbg.setTitle("�����ص���ʾ���");
		scv_setaddressbg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String [] items = {"��͸��","������","��ʿ��","������","ƻ����"};
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("�����ص���ʾ���");
				int whichitem=sp.getInt("which", 0);
				scv_setaddressbg.setDesc(items[whichitem]);
				builder.setSingleChoiceItems(items, whichitem, new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//������ɫ	
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						scv_setaddressbg.setDesc(items[which]);
						//ȡ���Ի���
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						//ȡ���Ի���
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});
		//��������������
		siv_callsmssafe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		callsmssafe=new Intent(this,CallSmsSafeService.class);
		boolean isRunning=ServerUtils.isRunning
				(this, "com.niebiao.mobilesafe.service.CallSmsSafeService");
		if (isRunning) {
			siv_callsmssafe.setCheck(true);
		}else {
			siv_callsmssafe.setCheck(false);
		}
		siv_callsmssafe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (siv_callsmssafe.isChecked()) {
					//���Ϊ��ѡ��״̬
					siv_callsmssafe.setCheck(false);
					stopService(callsmssafe);
				}else {
					siv_callsmssafe.setCheck(true);
					startService(callsmssafe);
				}
			}
		});
	}	
}
