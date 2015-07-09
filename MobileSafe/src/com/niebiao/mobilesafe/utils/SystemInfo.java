package com.niebiao.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class SystemInfo {
	public static int  getRunningProcessCount(Context context) {
		//PackageManager  �������� �൱��������Ա ��̬������ 
		//ActivityManage   ���̹����� �����ֻ��Ļ��Ϣ ��̬������ 
		ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		return infos.size();
	}
	public static long  getAvailMem(Context context) {
		ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	public static long  getTotalMem(Context context) {
		//ֻ�ʺ�api android16����
//		ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo=new MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
		//��linuxϵͳ�ļ�
		try {
			File file=new File("/proc/meminfo");
			FileInputStream fis=new FileInputStream(file);
			BufferedReader reader=new BufferedReader(new InputStreamReader(fis));
			String line=reader.readLine();
			//MemTotal:       513000kb
			StringBuilder sb=new StringBuilder();
			for (char c: line.toCharArray()) {
				if (c>='0'&&c<='9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString())*1024; //kb->b
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
} 
