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
 * widget�Ѿ�������mobilesafe�Ľ��̣������������Ľ���
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
				//�����������̣�����һ��������̵�widget�õ�mobliesafe���layout
				RemoteViews  views=new RemoteViews(getPackageName(),R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "�������еĽ��̣�"+
				SystemInfo.getRunningProcessCount(getApplicationContext())+"��");
				
				views.setTextViewText(R.id.process_memory, "�����ڴ棺"+
				Formatter.formatFileSize(getApplicationContext(),
						SystemInfo.getAvailMem(getApplicationContext())));
				//����һ�������������������һ��Ӧ�ó���ִ��
				//�Զ���㲥ʱ�䣬ɱ����̨����
				Intent intent=new Intent();
				intent.setAction("com.niebiao.mobilesafe.killall");
				PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);
			}
		};

		timer.schedule(task, 0, 3000); //��һ����������ʱ��3��ִ��һ��task
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
 