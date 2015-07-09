package com.niebiao.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.niebiao.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSmsSafeService extends Service {
private InnerSmsReceiver receiver;
private String TAG = "CallSmsSafeService";
private BlackNumberDao dao;

private TelephonyManager tm;
private MyListener mListener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private class InnerSmsReceiver extends BroadcastReceiver{

		

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG,"有短信来了");
			//检查发件人是否所黑名单号码
			Object [] objs=(Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
				SmsMessage sms=SmsMessage.createFromPdu((byte[]) obj);
				String sender=sms.getOriginatingAddress();
				String mode=dao.findMode(sender);
				if ("2".equals(mode) || "3".equals(mode)) {
					Log.i(TAG,"拦截短信");
					abortBroadcast();//屏蔽广播
				}
			}
			
		}
		
	}

	@Override
	public void onCreate() {
		dao= new BlackNumberDao(getApplicationContext());
		receiver= new InnerSmsReceiver();
		IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener=new MyListener();
		tm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	} 
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver=null;
		super.onDestroy();
	}
	private class MyListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:   //零响状态
				String result=dao.findMode(incomingNumber);
				if ("1".equals(result) || "3".equals(result)) {
					endCall();
				}
				break;

			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	/** 
     * 挂断电话 
     */  
    private void endCall()  
    {  
        try  
        {  
        		//加载service的字节码
        		Class cla=CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = cla.getDeclaredMethod("getService", String.class);  
            IBinder iBinder=(IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony.Stub.asInterface(iBinder).endCall();  
        }  
        catch (Exception e)  
        {  
        	e.printStackTrace();
       //     Log.e(TAG, "Fail to answer ring call.", e);  
        }          
    } 
    
}
