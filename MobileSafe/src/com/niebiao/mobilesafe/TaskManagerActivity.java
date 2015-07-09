package com.niebiao.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.niebiao.mobilesafe.domain.TaskInfo;
import com.niebiao.mobilesafe.engine.TaskInfoProvider;
import com.niebiao.mobilesafe.utils.SystemInfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class TaskManagerActivity extends Activity {
	private TextView tv_process_count;
	private TextView tv_mem;
	private ListView lv_task;
	private TextView tv_processcoun;
	private LinearLayout ll_process;
	
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userApp;
	private List<TaskInfo> systemApp;
	
	private TaskInfoAdapter mAdapter;
	private int processcount;
	private long totalMen;
	private long avilMem;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_task_manage);
		tv_process_count=(TextView) findViewById(R.id.tv_task_count);
		tv_mem=(TextView) findViewById(R.id.tv_task_rom);
		ll_process=(LinearLayout) findViewById(R.id.ll_task_process);
		ll_process.setVisibility(View.VISIBLE);
		lv_task=(ListView) findViewById(R.id.lv_task);
		tv_processcoun=(TextView) findViewById(R.id.tv_taskcount);
		
		sp=getSharedPreferences("config", MODE_PRIVATE);
		lv_task.setOnScrollListener(new OnScrollListener() {
			
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
					if (firstVisibleItem<=userApp.size()) {
						tv_processcoun.setText("用户程序 "+userApp.size()+"个");
					}else {
						tv_processcoun.setText("系统程序 "+systemApp.size()+"个");
						
					}
				}
			}
		});
		
		fillData();
		//listview item点击选中checkbox
		lv_task.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TaskInfo taskInfo;
				if (position==0) {
					return ;
				}else if (position==(1+userApp.size())) {
					return ;
				}else if(position<=(userApp.size())){
					int newpositsion=position-1;//多一个textview 应该减掉
					taskInfo =userApp.get(newpositsion);
				}else {
					int newpositsion=position-1-1-userApp.size();
					taskInfo =systemApp.get(newpositsion);
				} 
				if (getPackageName().equals(taskInfo.getPackName())) {
					return;
				}
				ViewHolder holder=(ViewHolder) view.getTag();
				if (taskInfo.isChecked()) {
					//勾选了，再点击就是取消勾选
					taskInfo.setChecked(false);
					holder.cb_status.setChecked(false);
				}else {
					taskInfo.setChecked(true);
					holder.cb_status.setChecked(true);
				}
			}
		});
	}
	private void setTitle() {
		processcount = SystemInfo.getRunningProcessCount(getApplicationContext());
		totalMen = SystemInfo.getTotalMem(getApplicationContext());
		avilMem=SystemInfo.getAvailMem(getApplicationContext());
		tv_process_count.setText("运行中的进程 "+processcount
		+"个");
		tv_mem.setText("剩余/总内存："+Formatter.formatFileSize(this, avilMem)
		+"/"+Formatter.formatFileSize(this, totalMen));
	}
	//填充数据
	private void fillData() {
		new Thread(){
			public void run() {
				new Thread(){
					public void run() {
						taskInfos=TaskInfoProvider.getTaskInfos(getApplicationContext());
						userApp=new ArrayList<TaskInfo>();
						systemApp=new ArrayList<TaskInfo>();
						for (TaskInfo taskInfo : taskInfos) {
							if (taskInfo.isUserApp()) {
								userApp.add(taskInfo);
							}
							else {
								systemApp.add(taskInfo);
							}
						}
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								ll_process.setVisibility(View.INVISIBLE);
								if (mAdapter==null) {
									mAdapter=new TaskInfoAdapter();
									lv_task.setAdapter(mAdapter);
								}else {
									mAdapter.notifyDataSetChanged();
								}
								setTitle();
							}
						});
					};
				}.start();
			};
		}.start();
	}
	private class TaskInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			if (sp.getBoolean("showSystemApp", false)) {
				return 1+systemApp.size()+1+userApp.size();
			}else {
				return 1+userApp.size();
			}
			
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
			
			
			TaskInfo taskInfo;
			if (position==0) {
				TextView tv=new TextView(TaskManagerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户程序 "+userApp.size()+"个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else if (position==(1+userApp.size())) {
				TextView tv=new TextView(TaskManagerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统程序 "+systemApp.size()+"个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else if(position<=(userApp.size())){
				int newpositsion=position-1;//多一个textview 应该减掉
				taskInfo =userApp.get(newpositsion);
			}else {
				int newpositsion=position-1-1-userApp.size();
				taskInfo =systemApp.get(newpositsion);
			} 
			
			View view;
			ViewHolder holder;
			if (convertView!=null&& convertView instanceof RelativeLayout) {
					
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}else{
				view=View.inflate(getApplicationContext(), R.layout.list_item_taskinfo, null);
				holder=new ViewHolder();
				holder.tv_name=(TextView) view.findViewById(R.id.tv_task_name);
				holder.tv_memsize=(TextView) view.findViewById(R.id.tv_task_mem);
				holder.iv_icon=(ImageView) view.findViewById(R.id.iv_task_icon);		
				holder.cb_status=(CheckBox) view.findViewById(R.id.cb_task);
				view.setTag(holder);
			}
			holder.tv_name.setText(taskInfo.getName());
			holder.tv_memsize.setText(Formatter.formatFileSize(getApplicationContext(), taskInfo.getMemsize()));
			holder.iv_icon.setImageDrawable(taskInfo.getIcon());
			holder.cb_status.setChecked(taskInfo.isChecked());
			if (getPackageName().equals(taskInfo.getPackName())) {
				holder.cb_status.setVisibility(View.INVISIBLE);
			}else {
				holder.cb_status.setVisibility(View.VISIBLE);
			}
			return view;
		}
		}
	static class ViewHolder {
		 ImageView iv_icon;
		 TextView tv_name;
		 TextView tv_memsize;	
		 CheckBox cb_status;
	}  
	//一键清除
	public void killAll(View view) {
//		if (processcount<=0) {
//			Toast.makeText(TaskManagerActivity.this, "恭喜您，清空完毕", 1).show();
//			Intent intent =new Intent(TaskManagerActivity.this, HomeActivity.class);
//			startActivity(intent);
//			return;
//		}
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int count=0;
		long freedMem = 0;
		List<TaskInfo> killInfos=new ArrayList<TaskInfo>();
		for(TaskInfo taskInfo:taskInfos){
			if (taskInfo.isChecked()) {
				am.killBackgroundProcesses(taskInfo.getPackName());
				count++;
				freedMem+=taskInfo.getMemsize();
				if (taskInfo.isUserApp()) {
					userApp.remove(taskInfo);
				}else {
					systemApp.remove(taskInfo);
				}
				killInfos.add(taskInfo);
		    }
			 
		} 
		taskInfos.removeAll(killInfos);
		mAdapter.notifyDataSetChanged();
		Toast.makeText(TaskManagerActivity.this, 
				"恭喜您清除了 "+count+" 个进程,释放了"+
				Formatter.formatFileSize(getApplicationContext(), freedMem)
				+" 内存", 1).show();
		processcount-=count;
		avilMem+=freedMem;
		tv_process_count.setText("运行中的进程 "+processcount
				+"个");
		tv_mem.setText("剩余/总内存："+Formatter.formatFileSize(this, avilMem)
				+"/"+Formatter.formatFileSize(this, totalMen));
	}
	//全选
	public void selectAll(View view) {
			for (TaskInfo taskInfo : taskInfos) {
				if (getPackageName().equals(taskInfo.getPackName())) {
					continue;
				}
				taskInfo.setChecked(true);
				mAdapter.notifyDataSetChanged();
			}
	}
	//反选
	public void selectOppo(View view) {
		for (TaskInfo taskInfo : taskInfos) {
//			if (taskInfo.isChecked()) {
//				taskInfo.setChecked(false);
//			}else {
//				taskInfo.setChecked(true);
//			}
			if (getPackageName().equals(taskInfo.getPackName())) {
				continue;
			}
			taskInfo.setChecked(!taskInfo.isChecked());
			mAdapter.notifyDataSetChanged();
		}
	}
	//设置
	public void enterSetting(View view) {
		Intent intent=new Intent(TaskManagerActivity.this, TaskClearSettingActivity.class);
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mAdapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
  
}

   