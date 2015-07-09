package com.niebiao.mobilesafe.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServerUtils {
	/*
	 * 检查某个服务是否还活着
	 */
	public static boolean isRunning(Context context,String serverName) {
		ActivityManager am = (ActivityManager) context.getSystemService
				(Activity.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infoList=am.getRunningServices(100);//从系统中获取100个服务
		for(RunningServiceInfo info:infoList){
			String className=info.service.getClassName();
			if (className.equals(serverName)) {
				return true;
			}
		}
		return false;
	}
}
