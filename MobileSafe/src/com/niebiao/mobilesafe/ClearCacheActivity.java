package com.niebiao.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ClearCacheActivity extends Activity {
	private ProgressBar pb_clear_cache;
	private TextView tv_scan_status;
	private LinearLayout ll_container;
	private Button btn_clearAll;
	
	private  PackageManager pm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_clear_cache);
		pb_clear_cache=(ProgressBar) findViewById(R.id.pb_clear_cache);
		tv_scan_status=(TextView) findViewById(R.id.tv_scan_status);
		ll_container=(LinearLayout) findViewById(R.id.ll_cache_container);
		btn_clearAll=(Button) findViewById(R.id.btn_clearall_cache);
		cacheScan();
	}
	private void  cacheScan(){
		pm = getPackageManager();
		new Thread(){
			public void run() {
				List<PackageInfo> infos=	pm.getInstalledPackages(0);
				pb_clear_cache.setMax(infos.size());
				int  progress=0;
				for (PackageInfo packageInfo : infos) {
					try {
						Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
						method.invoke(pm, packageInfo.packageName,new MyStatsObserver());
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
					progress++;
					pb_clear_cache.setProgress(progress);
				}
				runOnUiThread(new  Runnable() {
					public void run() {
						tv_scan_status.setText("扫描完毕。。。");
					}
				});
			};
		}.start();
	}
	private class MyStatsObserver extends IPackageStatsObserver.Stub{
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			final long cache=pStats.cacheSize;
			long code=pStats.codeSize;
			long data=pStats.dataSize;
			final String packname=pStats.packageName;
//			System.out.println("code"+Formatter.formatFileSize(getApplicationContext(), code));
//			System.out.println("data"+Formatter.formatFileSize(getApplicationContext(), data));
//			System.out.println("cache"+Formatter.formatFileSize(getApplicationContext(), cache));
	//		System.out.println(packname);
	//		System.out.println("----------------------");
			final ApplicationInfo appinfoInfo;
			try {
				appinfoInfo=pm.getApplicationInfo(packname, 0);
				
					runOnUiThread(new Runnable() {
						String name=(String) appinfoInfo.loadLabel(pm);
						public void run() {
							tv_scan_status.setText("正在扫描："+name);
							if (cache>0) {
								View view =View.inflate(getApplicationContext(), R.layout.list_item_cacheinfo, null);
								ImageView iv_icon=(ImageView) view.findViewById(R.id.iv_cache_icon);
								iv_icon.setImageDrawable(appinfoInfo.loadIcon(pm));
								TextView tv_name=(TextView) view.findViewById(R.id.tv_cache_name);
								tv_name.setText(name);
								TextView tv_size=(TextView) view.findViewById(R.id.tv_cache_size);
								tv_size.setText(Formatter.formatFileSize(getApplicationContext(), cache));
								ImageView iv_delete=(ImageView) view.findViewById(R.id.iv_cache_delete);
								iv_delete.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										try {
											Method method=PackageManager.class.getMethod("deleteApplicationCacheFiles", String.class,
													IPackageDataObserver.class);
											method.invoke(pm, packname,new MyDataObserver());
											Toast.makeText(getApplicationContext(), "缓存清理完成", 0).show();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											Toast.makeText(getApplicationContext(), "缓存清理失败", 0).show();
											e.printStackTrace();
											
										}
									}
								});
								ll_container.addView(view);
							}	
						}
					}); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	private class MyDataObserver extends   IPackageDataObserver.Stub{

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			if (succeeded) {
				System.out.println(packageName+"--succeeded");
			}else {
				System.out.println(packageName+"--failed");
			}
			
			
		}
		
	}
	//清除所有缓存
//	public void  clearAll(View view) {
//		try {
//			Method method=PackageManager.class.getMethod("freeStorageAndNotify", String.class,
//					IPackageDataObserver.class);
//			method.invoke(pm, Integer.MAX_VALUE,new MyDataObserver());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public void clearAll(View view){
//		/freeStorageAndNotify
		Method[] methods = PackageManager.class.getMethods();
		for(Method method:methods){
			if("freeStorageAndNotify".equals(method.getName())){
				try {
					method.invoke(pm, Integer.MAX_VALUE, new IPackageDataObserver.Stub() {
						@Override
						public void onRemoveCompleted(String packageName,
								boolean succeeded) throws RemoteException {
							System.out.println(succeeded);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
}
