package com.niebiao.mobilesafe;

import com.niebiao.mobilesafe.utils.SmsUtils;
import com.niebiao.mobilesafe.utils.SmsUtils.BackupCallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class AtoolActivity  extends Activity{
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		
	}
	//点击进入号码归属地查询页面
	public void  numberQuery(View view) {
		Intent intent = new Intent(AtoolActivity.this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}
	//短信备份
	public void smsBackup(View view) {
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//进度条
		pd.setMessage("正在备份短信");
		pd.show();
		//执行时间比较长时，应该重开新线程
		new Thread(){
			public void run() {
				try {
					SmsUtils.backupSms(AtoolActivity.this,new BackupCallBack() {
						
						@Override
						public void onSmsBackup(int progress) {
							pd.setProgress(progress);
							//可以有pd,pd1,pd2,接口可以使得函数之间的耦合减小
						}
						
						@Override
						public void beforeBackup(int max) {
							pd.setMax(max);
						}
					});
					pd.dismiss();
					//简单的修改ui,不必使用handler
					runOnUiThread(new  Runnable() {
						public void run() {
							Toast.makeText(AtoolActivity.this, "短信备份成功", 0).show();
						}
					});
					
				} catch (Exception e) {
					runOnUiThread(new  Runnable() {
						public void run() {
							Toast.makeText(AtoolActivity.this, "短信备份失败", 0).show();
						}
					});
					e.printStackTrace();
				}
			};
		}.start();
	}
	//短信还原
	public void smsRestore(View view) {
		try {
			SmsUtils.restoreSms(AtoolActivity.this);
			Toast.makeText(AtoolActivity.this, "短信还原成功", 0).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
