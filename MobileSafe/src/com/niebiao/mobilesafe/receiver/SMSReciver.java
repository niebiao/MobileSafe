package com.niebiao.mobilesafe.receiver;

import com.niebiao.mobilesafe.R;
import com.niebiao.mobilesafe.service.GPSService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SMSReciver extends BroadcastReceiver {
	private static final String TAG = "SMSReciver";
	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		String safeNumber = sp.getString("safenumber", null);
		for(Object b:objs){
			SmsMessage sms =SmsMessage.createFromPdu((byte []) b);
			String sender = sms.getOriginatingAddress();
			String body = sms.getMessageBody();
			if (sender.equals(safeNumber)) {
				if("#*location*#".equals(body)){
					//�õ��ֻ���GPS
					Log.i(TAG, "�õ��ֻ���GPS");
					//��������
					Intent i = new Intent(context,GPSService.class);
					context.startService(i);
					SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
					String lastlocation = sp.getString("lastlocation", null);
					if(TextUtils.isEmpty(lastlocation)){
						//λ��û�еõ�
						SmsManager.getDefault().sendTextMessage(sender, null, "geting loaction.....", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					
					
					//������㲥��ֹ��
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					//���ű���Ӱ��
					Log.i(TAG, "���ű���Ӱ��");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(false);//ѭ��
					player.setVolume(1.0f, 1.0f);//������� 
					player.start();
					
					abortBroadcast();
				}
				else if("#*wipedata*#".equals(body)){
					//Զ���������
					Log.i(TAG, "Զ���������");
					abortBroadcast();
				}
				else if("#*lockscreen*#".equals(body)){
					//Զ������
					Log.i(TAG, "Զ������");
					abortBroadcast();
				}
			}
			
		}
		
	}

}
