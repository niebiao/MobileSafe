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
					//得到手机的GPS
					Log.i(TAG, "得到手机的GPS");
					//启动服务
					Intent i = new Intent(context,GPSService.class);
					context.startService(i);
					SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
					String lastlocation = sp.getString("lastlocation", null);
					if(TextUtils.isEmpty(lastlocation)){
						//位置没有得到
						SmsManager.getDefault().sendTextMessage(sender, null, "geting loaction.....", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					
					
					//把这个广播终止掉
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					//播放报警影音
					Log.i(TAG, "播放报警影音");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(false);//循环
					player.setVolume(1.0f, 1.0f);//声音最大 
					player.start();
					
					abortBroadcast();
				}
				else if("#*wipedata*#".equals(body)){
					//远程清除数据
					Log.i(TAG, "远程清除数据");
					abortBroadcast();
				}
				else if("#*lockscreen*#".equals(body)){
					//远程锁屏
					Log.i(TAG, "远程锁屏");
					abortBroadcast();
				}
			}
			
		}
		
	}

}
