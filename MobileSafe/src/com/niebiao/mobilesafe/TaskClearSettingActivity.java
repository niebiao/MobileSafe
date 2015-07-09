package com.niebiao.mobilesafe;

import com.niebiao.mobilesafe.service.AutoClearService;
import com.niebiao.mobilesafe.utils.ServerUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskClearSettingActivity extends Activity {
	private CheckBox cb_show_sysapp;
	private CheckBox cb_auto_clear;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);
		cb_show_sysapp=(CheckBox) findViewById(R.id.cb_show_sysapp);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		//设置是否显示系统进程
		cb_show_sysapp.setChecked(sp.getBoolean("showSystemApp", false));
		cb_show_sysapp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				Editor editor=sp.edit();
				editor.putBoolean("showSystemApp", isChecked);
				editor.commit();
			}
		});
		//设置锁屏清理程序
		cb_auto_clear=(CheckBox) findViewById(R.id.cb_time_clear);
		cb_auto_clear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent intent=new Intent(TaskClearSettingActivity.this, AutoClearService.class);
				if (isChecked) {
					startService(intent);
				}else {
					stopService(intent);
				}
				Editor editor=sp.edit();
				editor.putBoolean("screenoffclear", isChecked);
				editor.commit();
			}
		});
	}
	@Override
	protected void onStart() {
		boolean isRunning=ServerUtils.isRunning(this, "com.niebiao.mobilesafe.service.AutoClearService");
		cb_auto_clear.setChecked(isRunning);
		
		super.onStart();
	}
}
