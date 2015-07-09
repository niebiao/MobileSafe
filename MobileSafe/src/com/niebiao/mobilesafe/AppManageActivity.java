package com.niebiao.mobilesafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.niebiao.mobilesafe.domain.AppInfo;
import com.niebiao.mobilesafe.engine.AppInfoProvider;
import com.niebiao.mobilesafe.utils.DisplayUtils;

import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppManageActivity extends Activity implements OnClickListener {
	private TextView tv_neicun;
	private TextView tv_sdcar;
	private ListView lv_appinfo;
	private List<AppInfo> appInfos;
	private AppManageAdapter mAdapter;
	private LinearLayout ll_process;
	private TextView tv_appcoun;
	private PopupWindow popupWindow;
	
	private LinearLayout ll_pop_uninstal;
	private LinearLayout ll_pop_start;
	private LinearLayout ll_pop_share;
	
	private List<AppInfo> userApp;
	private List<AppInfo> systemApp;
	private AppInfo appInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_app_manage);
		tv_neicun=(TextView) findViewById(R.id.tv_neicun);
		tv_sdcar=(TextView) findViewById(R.id.tv_sdcar);
		
		long sdsize=getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		long romsize=getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
		tv_sdcar.setText("sd卡可用："+Formatter.formatFileSize(this, sdsize));
		tv_neicun.setText("内存可以："+Formatter.formatFileSize(this, romsize));
		
		lv_appinfo=(ListView) findViewById(R.id.lv_appinfo);
		ll_process=(LinearLayout) findViewById(R.id.ll_app_process);
		ll_process.setVisibility(View.VISIBLE);
		tv_appcoun=(TextView) findViewById(R.id.tv_appcount);
		
		fillData();
		lv_appinfo.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE: //空闲状态
					
					break;

				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: //触摸滑动
					
					break;
				case OnScrollListener.SCROLL_STATE_FLING: //惯性滑动
	
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (userApp!=null&&systemApp!=null) {
					if (firstVisibleItem<systemApp.size()) {
						tv_appcoun.setText("系统程序 "+systemApp.size()+"个");
					}else {
						tv_appcoun.setText("用户程序 "+userApp.size()+"个");
					}
					//滚动是隐藏掉popwindow
					dismissPopwindow();
				}
			}
		});
		lv_appinfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int newposition;
				
				if (position==0) {
					return;
				}else if (position==(systemApp.size()+1)) {
					return;
				}else if (position<(systemApp.size()+1)) {
					newposition=position-1;
					appInfo=systemApp.get(newposition);
				}else{
					newposition=position-systemApp.size()-1-1;
					appInfo=userApp.get(newposition);
				} 
//				System.out.println(appInfo.getName());
//				TextView contentView = new TextView(getApplicationContext());
//				contentView.setText(appInfo.getName());
//				contentView.setTextColor(Color.BLACK);
				View contentView =view.inflate(AppManageActivity.this, R.layout.popwindow_app_item, null);
				ll_pop_start=(LinearLayout) contentView.findViewById(R.id.ll_pop_start);
				ll_pop_share=(LinearLayout) contentView.findViewById(R.id.ll_pop_share);
				ll_pop_uninstal=(LinearLayout) contentView.findViewById(R.id.ll_pop_unstall);
				ll_pop_start.setOnClickListener(AppManageActivity.this);
				ll_pop_share.setOnClickListener(AppManageActivity.this);
				ll_pop_uninstal.setOnClickListener(AppManageActivity.this);
				
				dismissPopwindow();
				popupWindow= new PopupWindow(contentView, -2, -2);//大小包裹父窗体
				//播放动画必须要求窗体有背景颜色
				//透明色也是颜色
				popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				int []location=new int[2];
				view.getLocationInWindow(location);
	//			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ff0000")));
				int dip=60;
				int px=DisplayUtils.dip2px(getApplicationContext(), dip);
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, px, location[1]);//x 距离屏幕左边的距离
				ScaleAnimation sa=new ScaleAnimation(0.3f,1.0f,0.3f,1.0f,Animation.RELATIVE_TO_SELF,
						0,Animation.RELATIVE_TO_SELF,0.5f);
				sa.setDuration(300);
				AlphaAnimation aa = new  AlphaAnimation(0.5f,1.0f);
				aa.setDuration(300);
				AnimationSet set=new AnimationSet(false);
				set.addAnimation(aa);
				set.addAnimation(sa);
				contentView.startAnimation(set);
				
			}
			
			
		});
	}
	
	private void fillData() {
		new Thread(){
			public void run() {
				appInfos=AppInfoProvider.getAppInfos(AppManageActivity.this);
				userApp=new ArrayList<AppInfo>();
				systemApp=new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUserApp()) {
						userApp.add(appInfo);
					}
					else {
						systemApp.add(appInfo);
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ll_process.setVisibility(View.INVISIBLE);
						if (mAdapter==null) {
							mAdapter=new AppManageAdapter();
							lv_appinfo.setAdapter(mAdapter);
						}else {
							mAdapter.notifyDataSetChanged();
						}
						
					}
				});
			};
		}.start();
	}
	private void dismissPopwindow() {
		//把旧的的隐藏掉
		if (popupWindow!=null&&popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow=null;
		}
	}
	//获取某个目录的内存大小
	private long getAvailSpace(String path){
		StatFs statFs=new StatFs(path);
		statFs.getBlockCount(); //获取分区个数
		long size=statFs.getBlockSize();   //获取分区大小
		statFs.getFreeBlocks(); ////获取可以的区块个数
		long count=statFs.getAvailableBlocks(); //获取可以的区块个数

		return count*size;
	}
	private  class AppManageAdapter extends BaseAdapter{
		
		
		@Override
		public int getCount() {
			return 1+systemApp.size()+1+userApp.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder=null;
			AppInfo appInfo;
			if (position==0) {
				TextView tv=new TextView(AppManageActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统程序 "+systemApp.size()+"个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else if (position==(1+systemApp.size())) {
				TextView tv=new TextView(AppManageActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户程序 "+userApp.size()+"个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else if(position<=(systemApp.size())){
				int newpositsion=position-1;//多一个textview 应该减掉
				appInfo =systemApp.get(newpositsion);
			}else {
				int newpositsion=position-1-1-systemApp.size();
				appInfo =userApp.get(newpositsion);
			}
				if (convertView!=null&& convertView instanceof RelativeLayout) {
					
					view=convertView;
					holder=(ViewHolder) view.getTag();
				}else{
					view=View.inflate(getApplicationContext(), R.layout.list_item_appinfo, null);
					holder=new ViewHolder();
					holder.tv_name=(TextView) view.findViewById(R.id.tv_appname);
					holder.tv_date=(TextView) view.findViewById(R.id.tv_packdate);
					holder.iv_packicon=(ImageView) view.findViewById(R.id.iv_packicon);
					view.setTag(holder);
				}
				
				holder.tv_name.setText(appInfo.getName());
				holder.iv_packicon.setImageDrawable(appInfo.getIcon());
				return view;
		}
	}
	class ViewHolder {
		private ImageView iv_packicon;
		private TextView tv_name;
		private TextView tv_date;
	}
	//在 关闭activity之前必须发依附在activity上的popwindow注销掉
	@Override
	protected void onDestroy() {
		dismissPopwindow();
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_pop_start:
			StartApplication();
			break;
		case R.id.ll_pop_share:
			ShareApplication();
			break;
		case R.id.ll_pop_unstall:
			if (appInfo.isUserApp()) {
				UninstallApplication();
			}else {
				Toast.makeText(AppManageActivity.this, "系统程序必须root才能卸载", 0).show();
			}
			break;
		}
	}
	private void ShareApplication() {
		Intent intent=new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件，名字叫："+appInfo.getName());
		startActivity(intent);
		dismissPopwindow();
	}

	private void UninstallApplication() {
		Intent intent=new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
		startActivityForResult(intent, 0);
		dismissPopwindow();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//刷新界面，就是从新定义数据
		fillData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void StartApplication() {
		//查询这个程序的入口activity把他启动起来
		PackageManager pm=getPackageManager();
//		Intent intent=new Intent();
//		intent.setAction("android.intent.action.MAIN");
//		intent.addCategory("android.intent.category.LAUNCHER");
		//查询手机上所有可执行的activity
		//List<ResolveInfo> infos=pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
		Intent intent =pm.getLaunchIntentForPackage(appInfo.getPackageName());
		if (intent==null) {
			Toast.makeText(AppManageActivity.this, "该程序不可启动", 0).show();
			dismissPopwindow();
			return;
		}
		dismissPopwindow();
		startActivity(intent);
	}
}
