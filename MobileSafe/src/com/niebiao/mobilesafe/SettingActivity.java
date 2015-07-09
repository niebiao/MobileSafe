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
	//设置归属地toast背景颜色
	private SettingClickView scv_setaddressbg;
	//黑名单号码拦截
	private SettingItemView siv_callsmssafe;
	private Intent callsmssafe;
	
	//在Activity关闭时
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
		//黑名单拦截
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
				//已经打开自动更新
					if (siv_update.isChecked()) {
						//设置为不跟新
						siv_update.setCheck(false);
						editor.putBoolean("isUpdate", false);
					}else {
						siv_update.setCheck(true);
						editor.putBoolean("isUpdate", true);
					}
					editor.commit();
			}
		});
		//来电号码归属地显示
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
					//变更为非选中状态
					Siv_showAddress.setCheck(false);
					stopService(showAddress);
				}else {
					Siv_showAddress.setCheck(true);
					startService(showAddress);
				}
			}
		});
		//设置归属地toast背景颜色
		scv_setaddressbg =(SettingClickView) findViewById(R.id.scv_set_addressbg);
		scv_setaddressbg.setTitle("归属地的提示风格");
		scv_setaddressbg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String [] items = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地的提示风格");
				int whichitem=sp.getInt("which", 0);
				scv_setaddressbg.setDesc(items[whichitem]);
				builder.setSingleChoiceItems(items, whichitem, new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//保存颜色	
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						scv_setaddressbg.setDesc(items[which]);
						//取消对话框
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						//取消对话框
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});
		//黑名单号码拦截
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
					//变更为非选中状态
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
