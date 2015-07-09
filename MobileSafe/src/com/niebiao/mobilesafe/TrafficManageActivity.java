package com.niebiao.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
/*
 * 流量统计
 */
public class TrafficManageActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_traffic_manage);
		//1 获取一个包管理器
		PackageManager pm = getPackageManager();
		List<ApplicationInfo> applications=pm.getInstalledApplications(0);
		for (ApplicationInfo applicationInfo : applications) {
			int uid=applicationInfo.uid;
			//proc/uid_stat/10083/tcp_rcv
			long tx=TrafficStats.getUidTxBytes(uid);//上传总流量
			long rx=TrafficStats.getUidRxBytes(uid);//下载总流量
			//返回-1代表没用产生流量
		}
		TrafficStats.getMobileRxBytes();////手机3g/2g下载总流量
		TrafficStats.getMobileTxBytes();
		TrafficStats.getTotalRxBytes();//wifi,3g/2g
		
	}
}
