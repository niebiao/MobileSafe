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
	//��������������ز�ѯҳ��
	public void  numberQuery(View view) {
		Intent intent = new Intent(AtoolActivity.this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}
	//���ű���
	public void smsBackup(View view) {
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//������
		pd.setMessage("���ڱ��ݶ���");
		pd.show();
		//ִ��ʱ��Ƚϳ�ʱ��Ӧ���ؿ����߳�
		new Thread(){
			public void run() {
				try {
					SmsUtils.backupSms(AtoolActivity.this,new BackupCallBack() {
						
						@Override
						public void onSmsBackup(int progress) {
							pd.setProgress(progress);
							//������pd,pd1,pd2,�ӿڿ���ʹ�ú���֮�����ϼ�С
						}
						
						@Override
						public void beforeBackup(int max) {
							pd.setMax(max);
						}
					});
					pd.dismiss();
					//�򵥵��޸�ui,����ʹ��handler
					runOnUiThread(new  Runnable() {
						public void run() {
							Toast.makeText(AtoolActivity.this, "���ű��ݳɹ�", 0).show();
						}
					});
					
				} catch (Exception e) {
					runOnUiThread(new  Runnable() {
						public void run() {
							Toast.makeText(AtoolActivity.this, "���ű���ʧ��", 0).show();
						}
					});
					e.printStackTrace();
				}
			};
		}.start();
	}
	//���Ż�ԭ
	public void smsRestore(View view) {
		try {
			SmsUtils.restoreSms(AtoolActivity.this);
			Toast.makeText(AtoolActivity.this, "���Ż�ԭ�ɹ�", 0).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
