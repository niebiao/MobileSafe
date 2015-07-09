package com.niebiao.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.niebiao.mobilesafe.R;
import com.niebiao.mobilesafe.domain.TaskInfo;

/*
 * 进程信息提供者
 */
public class TaskInfoProvider {
	public static List<TaskInfo> getTaskInfos(Context context) {
		
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm=context.getPackageManager();
		List<RunningAppProcessInfo> processInfos=am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : processInfos) {
			TaskInfo taskInfo=new TaskInfo();
			String packName=runningAppProcessInfo.processName;
			taskInfo.setPackName(packName);
			android.os.Debug.MemoryInfo[] memoryInfos=am.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
			long memsize=memoryInfos[0].getTotalPrivateDirty()*1024l;
			taskInfo.setMemsize(memsize);
			try {
				ApplicationInfo applicationInfo=	pm.getApplicationInfo(packName, 0);
				Drawable icon=applicationInfo.loadIcon(pm);
				String name=applicationInfo.loadLabel(pm).toString();
				int flags=applicationInfo.flags;
				if (0==(flags&ApplicationInfo.FLAG_SYSTEM)) {
					taskInfo.setUserApp(true);
				}else {
					taskInfo.setUserApp(false);
				}
				taskInfo.setName(name);
				taskInfo.setIcon(icon);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
				taskInfo.setName(packName);
			}
			infos.add(taskInfo);
		}
		return infos;
	}
}
