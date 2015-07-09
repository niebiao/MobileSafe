package com.niebiao.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.niebiao.mobilesafe.R;
import com.niebiao.mobilesafe.receiver.MyWidget;
import com.niebiao.mobilesafe.utils.SystemInfo;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.sax.EndElementListener;
import android.text.format.Formatter;
import android.view.ViewStub;
import android.widget.RemoteViews;
/*
 * widget已经不再是mobilesafe的进程，而是桌面程序的进程
 */
public class UpdateWidgetService extends Service {
	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		awm=AppWidgetManager.getInstance(this);
		
		timer=new Timer();
		task=new TimerTask() {
			
			@Override
			public void run() {
				ComponentName provider=new ComponentName(UpdateWidgetService.this,MyWidget.class);
				//连接两个进程，帮助一个桌面进程的widget拿到mobliesafe里的layout
				RemoteViews  views=new RemoteViews(getPackageName(),R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "正在运行的进程："+
				SystemInfo.getRunningProcessCount(getApplicationContext())+"个");
				
				views.setTextViewText(R.id.process_memory, "可用内存："+
				Formatter.formatFileSize(getApplicationContext(),
						SystemInfo.getAvailMem(getApplicationContext())));
				//描述一个动作，这个动作由另一个应用程序执行
				//自定义广播时间，杀死后台进程
				Intent intent=new Intent();
				intent.setAction("com.niebiao.mobilesafe.killall");
				PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);
			}
		};

		timer.schedule(task, 0, 3000); //第一次启动不延时，3秒执行一次task
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		timer.cancel();
		task.cancel();
		timer=null;
		task=null;
		super.onDestroy();
	}
}
 