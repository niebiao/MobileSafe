package com.niebiao.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
/*
 * ����ͳ��
 */
public class TrafficManageActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_traffic_manage);
		//1 ��ȡһ����������
		PackageManager pm = getPackageManager();
		List<ApplicationInfo> applications=pm.getInstalledApplications(0);
		for (ApplicationInfo applicationInfo : applications) {
			int uid=applicationInfo.uid;
			//proc/uid_stat/10083/tcp_rcv
			long tx=TrafficStats.getUidTxBytes(uid);//�ϴ�������
			long rx=TrafficStats.getUidRxBytes(uid);//����������
			//����-1����û�ò�������
		}
		TrafficStats.getMobileRxBytes();////�ֻ�3g/2g����������
		TrafficStats.getMobileTxBytes();
		TrafficStats.getTotalRxBytes();//wifi,3g/2g
		
	}
}
