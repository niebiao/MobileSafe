package com.niebiao.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private  TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		tm=(TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		//��ȡ֮ǰ�����sim����Ϣ
		String saveSim = sp.getString("sim", null);
		//��ȡ��ǰ��sim����Ϣ
		 String currentSim = tm.getSimSerialNumber();
		 System.out.println(currentSim);
		//�Ƚ��Ƿ�һ��
		 if (TextUtils.isEmpty(saveSim)||TextUtils.isEmpty(currentSim)) {
			return;
		}else {
			if(currentSim.equals(saveSim)){
				//һ����û�б��
				System.out.println("û�б��");
				Toast.makeText(context, "û�б��", 1).show();
			}else {
				System.out.println("�����ѱ�");
				Toast.makeText(context, "�����ѱ�", 1).show();
				SmsManager.getDefault().sendTextMessage(saveSim, null, "SIM change...", null, null);
			}
		}
	}

}
