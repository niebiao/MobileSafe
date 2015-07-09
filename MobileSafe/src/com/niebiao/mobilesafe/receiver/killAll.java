package com.niebiao.mobilesafe.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class killAll extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("自定义的广播接收到里。。。。。。");
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningInfos=am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : runningInfos) {
			am.killBackgroundProcesses(info.processName);
		}
	}

}
