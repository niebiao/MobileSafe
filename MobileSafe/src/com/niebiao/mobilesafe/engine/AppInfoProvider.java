package com.niebiao.mobilesafe.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.niebiao.mobilesafe.domain.AppInfo;

/*
 * ҵ�񷽷��������ṩ�ֻ����氲װ�������������Ϣ
 */
public class AppInfoProvider {
	public static  List<AppInfo> getAppInfos(Context context) {
		PackageManager pm = context.getPackageManager();
		//���а�װ��ϵͳ�ϵ�Ӧ�ó������Ϣ��
		List<PackageInfo> packageInfos=pm.getInstalledPackages(0);
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		for(PackageInfo packageInfo:packageInfos){
			//�൱���嵥�ļ�
			String packageName=packageInfo.packageName;
			Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
			String name= packageInfo.applicationInfo.loadLabel(pm).toString();
			int uid=packageInfo.applicationInfo.uid;
			
//			try {
//				File rcvFile=new File("/proc/uid_stat/"+uid+"/tcp_rcv");       //linuxϵͳͳ����������������
//				File sndFile=new File("/proc/uid_stat/"+uid+"/tcp_snd");      //ʵʱ����
//				FileInputStream fis=new FileInputStream(rcvFile);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			AppInfo appInfo=new AppInfo();
			appInfo.setPackageName(packageName);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfo.setUid(uid);
			int flags=packageInfo.applicationInfo.flags;
			if ((flags&ApplicationInfo.FLAG_SYSTEM)==0) {
				//�û�����
				appInfo.setUserApp(true);
			}else {
				appInfo.setUserApp(false);
			}
			if ((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0) {
				appInfo.setInRom(true);
			}else {
				appInfo.setInRom(false);
			}
			appInfos.add(appInfo);
		}
		return appInfos;
	}
}
