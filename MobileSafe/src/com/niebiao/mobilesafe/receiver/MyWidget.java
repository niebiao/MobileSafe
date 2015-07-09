package com.niebiao.mobilesafe.receiver;

import com.niebiao.mobilesafe.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		//开启更新服务
		Intent intent=new Intent(context, UpdateWidgetService.class);
		context.startService(intent);
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		//关闭更新服务
		Intent intent=new Intent(context, UpdateWidgetService.class);
		context.stopService(intent);
		super.onDisabled(context);
	}
	
}
