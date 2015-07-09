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
		//读取之前保存的sim卡信息
		String saveSim = sp.getString("sim", null);
		//读取当前的sim卡信息
		 String currentSim = tm.getSimSerialNumber();
		 System.out.println(currentSim);
		//比较是否一样
		 if (TextUtils.isEmpty(saveSim)||TextUtils.isEmpty(currentSim)) {
			return;
		}else {
			if(currentSim.equals(saveSim)){
				//一样，没有变更
				System.out.println("没有变更");
				Toast.makeText(context, "没有变更", 1).show();
			}else {
				System.out.println("号码已变");
				Toast.makeText(context, "号码已变", 1).show();
				SmsManager.getDefault().sendTextMessage(saveSim, null, "SIM change...", null, null);
			}
		}
	}

}
