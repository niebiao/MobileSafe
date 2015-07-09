package com.niebiao.mobilesafe.service;

import java.util.zip.Inflater;

import com.niebiao.mobilesafe.R;
import com.niebiao.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.StrictMode.VmPolicy;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater.Filter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast") 
public class AddressService extends Service{
	protected static final String TAG ="AddressService";
	private TelephonyManager tm;
	private MyPhoneStateListener mListener;
	private OutCallReceiver ocr;
	private WindowManager wm;
	private View view;
	private SharedPreferences sp;
	
	private long firstClickTime ;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		tm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener=new  MyPhoneStateListener();
		tm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
		
		wm=(WindowManager) getSystemService(WINDOW_SERVICE);
		
		ocr=new OutCallReceiver();
		//使用代码注册广播监听者
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(ocr, filter);
		
		sp=getSharedPreferences("config", MODE_PRIVATE);
		
	}
	@Override
	//注销监听
	public void onDestroy() {
		tm.listen(mListener, PhoneStateListener.LISTEN_NONE);
		mListener=null;
		
		//使用代码注销注册广播接听者
		unregisterReceiver(ocr);
		ocr=null;
	}
	private class MyPhoneStateListener extends PhoneStateListener {
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String address = NumberAddressQueryUtils.queryNumber(incomingNumber);
	//			Toast.makeText(getApplicationContext(), address, 1).show();
				myToast(address);
				break;
				case TelephonyManager.CALL_STATE_IDLE: //来电拒绝或挂电话
					//注销自定义toast里的iew
					if (view!=null) {
						wm.removeView(view);
					}
					break;
					
			default:
				break;
			}
		}
	}
	//去电监听
	 class OutCallReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				//拿到播出去的电话号码
				String phone = getResultData();
				String address=NumberAddressQueryUtils.queryNumber(phone);
		//		Toast.makeText(context, address, 1).show();
				myToast(address);
			}
		
	 }
		//自定义土司
		private void myToast(String address) {
			final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
			
			view = View.inflate(this, R.layout.activity_address_show, null);
//			view.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//					if (firstClickTime>0) {
//						long secondClickTime =SystemClock.uptimeMillis();
//						long dTime=secondClickTime-firstClickTime;
//						if (dTime<500) {
//							params.gravity=Gravity.CENTER;
//							Toast.makeText(getApplicationContext(), "双击了", 0).show();
//						}else {
//							firstClickTime=0;
//						}
//						return;
//					}
//					firstClickTime=SystemClock.uptimeMillis();
//					//保证单击后双击绝对有效
//					new Thread(){
//						public void run() {
//							try {
//								Thread.sleep(500);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						};
//					}.start();
//				}
//			});
			//第二种方法
			final long[] mHits = new long[2];
		    view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 // 双击事件响应
			        /**
			         * arraycopy,拷贝数组
			         * src 要拷贝的源数组
			         * srcPos 源数组开始拷贝的下标位置
			         * dst 目标数组
			         * dstPos 开始存放的下标位置
			         * length 要拷贝的长度（元素的个数）
			         *
			         */
			        //实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
			        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
			        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
			        //双击事件的时间间隔500ms
			        if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
			            //双击后具体的操作
			        	Toast.makeText(getApplicationContext(), "双击了", 0).show();
			        	params.x=wm.getDefaultDisplay().getWidth()/2-view.getWidth()/2;
			        	params.y=wm.getDefaultDisplay().getHeight()/2-view.getHeight()/2;
			        	wm.updateViewLayout(view, params);
			        	Editor editor =sp.edit();
						editor.putInt("lastx", params.x);
						editor.putInt("lasty", params.y);
						editor.commit();
			        }
				}
			});
			int []addressbg={R.drawable.call_locate_white,
					R.drawable.call_locate_orange,R.drawable.call_locate_green,
					R.drawable.call_locate_blue,R.drawable.call_locate_blue};
			int which=sp.getInt("which", 0);
			//view 的触摸监听器
			view.setOnTouchListener(new OnTouchListener() {
				int startX;
				int startY;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:   //按下
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						Log.i(TAG, "startx="+startX+"          "+"starty="+startY);
						break;
					case MotionEvent.ACTION_MOVE:      //移动
						int newX = (int) event.getRawX();
						int newY = (int) event.getRawY();
						
						int dx = newX - startX;
						int dy = newY - startY;
						Log.i(TAG, "dx"+dx+"          "+"dy"+dy);
						//更新位置
						params.x+=dx;
						params.y+=dy;
						//边界问题
						if (params.x<0) {
							params.x=0;
						}
						if (params.y<0) {
							params.y=0;
						}
						//移动距离大于屏幕的宽度-控件的宽度
						if (params.x>wm.getDefaultDisplay().getWidth()-view.getWidth()) {
							params.x=wm.getDefaultDisplay().getWidth()-view.getWidth();
						}
						if (params.y>wm.getDefaultDisplay().getHeight()-view.getHeight()) {
							params.y=wm.getDefaultDisplay().getHeight()-view.getHeight();
						}
						wm.updateViewLayout(view, params);
						break;
					case MotionEvent.ACTION_UP:           //离开
						Editor editor =sp.edit();
						editor.putInt("lastx", params.x);
						editor.putInt("lasty", params.y);
						editor.commit();
						break;
					}
		//			return true;//事件处理完毕，不让父控件父布局view响应touch
					return false;//父控件还可以被点击
				}
			});
			
			TextView tv_address_show=(TextView) view.findViewById(R.id.tv_address_show);
			view.setBackgroundResource(addressbg[which]);
			tv_address_show.setText(address);
			//窗体
//			WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                 //   | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //不可触摸
	                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; //不锁屏
	         params.format = PixelFormat.TRANSLUCENT; //半透明
	         //android系统里面具有电话优先级的一种窗体类型，必须添加权限SYSTEM_ALERT_WINDOW
	         params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
	         
	         params.gravity=Gravity.LEFT+Gravity.TOP;
	         //指定窗体距离居中上100；
	         	params.x=sp.getInt("lastx", 0);
	         params.y=sp.getInt("lasty", 0);
			wm.addView(view, params);
		}
}
