package com.niebiao.mobilesafe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.niebiao.mobilesafe.db.dao.BlackNumberDao;
import com.niebiao.mobilesafe.domain.BlackNumberInfo;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {
	private ListView lv_callsms_safe;
	private BlackNumberInfo info;
	private BlackNumberDao dao;
	private  CallSmsSafeAdapter mAdapter;
	private List<BlackNumberInfo> infoList;
	private LinearLayout ll_loading;
	private int offset=0;
	private int maxnumber=20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_call_sms_safe);
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		lv_callsms_safe=(ListView) findViewById(R.id.lv_callsms_safe);
		dao=new BlackNumberDao(this);
		ll_loading.setVisibility(View.VISIBLE);
		fillData();
//		infoList=dao.findAll();
//		mAdapter=new CallSmsSafeAdapter();
//		lv_callsms_safe.setAdapter(mAdapter);
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			//滚动状态改变
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE: //空闲状态
					//获取最后一个可见条目在集合的位子，并加载剩余item
					int lastposition=lv_callsms_safe.getLastVisiblePosition();
					if (lastposition==(infoList.size()-1)) {
						fillData();
					}
					
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: //手指触摸滑动
					
					break;
				case OnScrollListener.SCROLL_STATE_FLING:        //惯性滑动
	
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	private void fillData() {
		//当查找数据库需要大量时间时，就不能放到主程序中
		new Thread(){
			public void run() {
				if (infoList==null) {
					//分页加载数据  覆盖其他加载数据
					infoList=dao.findAll(offset,maxnumber);
					offset+=maxnumber;
				}else {
					//分批加载数据 ，在已经加载数据后面再加
					infoList.addAll(dao.findAll(offset,maxnumber));
				}
				
				runOnUiThread(new  Runnable() {
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						//更新界面
						if (mAdapter==null) {
							mAdapter=new CallSmsSafeAdapter();
							lv_callsms_safe.setAdapter(mAdapter);
						}else {
							mAdapter.notifyDataSetChanged();
						}
					}
				});
			};
		}.start();
	}
	class CallSmsSafeAdapter extends BaseAdapter{
		
		
		@Override
		public int getCount() {
			return infoList.size();
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
			View view;
			final int i=position;
			ViewHoder hoder = null;
			if (convertView==null) {
				view = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				hoder = new ViewHoder();
				hoder.tv_number=(TextView) view.findViewById(R.id.tv_black_number);
				hoder.tv_mode=(TextView) view.findViewById(R.id.tv_black_mode);
				hoder.iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
				view.setTag(hoder);
			}else {
				view=convertView;
				hoder=(ViewHoder) view.getTag();
			}
			
			hoder.tv_number.setText(infoList.get(i).getNumber());
			String mode =infoList.get(i).getMode();
			if ("1".equals(mode)) {
				hoder.tv_mode.setText("电话拦截");
			}else if("2".equals(mode)) {
				hoder.tv_mode.setText("短信拦截");
			}else if ("3".equals(mode)) {
				hoder.tv_mode.setText("全部拦截");
			}
			//删除item
			hoder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder= new Builder(CallSmsSafeActivity.this);
					builder.setTitle("警告");
					builder.setMessage("是否确定要删除？");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dao.delete(infoList.get(i).getNumber());
							infoList.remove(i);
							mAdapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			});
			return view;
		}
		}
	static  class ViewHoder{
		 TextView tv_number;
		 TextView tv_mode;
		 ImageView iv_delete;
	}
	private EditText et_blackNumber;
	private CheckBox cb_call;
	private CheckBox cb_sms;
	private Button  btn_ok;
	private Button  btn_cancel;
	
	public void addBlackNumber (View view){
		AlertDialog.Builder builder=new Builder(this);
		final AlertDialog dialog=builder.create();
		view=View.inflate(this, R.layout.dialog_add_black_number, null);
		et_blackNumber=(EditText) view.findViewById(R.id.et_add_number);
		cb_call=(CheckBox) view.findViewById(R.id.cb_call);
		cb_sms=(CheckBox) view.findViewById(R.id.cb_sms);
		btn_ok=(Button) view.findViewById(R.id.btn_blackadd_ok);
		btn_cancel=(Button) view.findViewById(R.id.btn_blackadd_cancel);
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String addBlackNumber=et_blackNumber.getText().toString().trim();
				if(TextUtils.isEmpty(addBlackNumber)){
					Toast.makeText(CallSmsSafeActivity.this, "黑名单号码不能为空", 0).show();
					return;
				}
				String mode;
				if (cb_sms.isChecked()&& cb_call.isChecked()) {
					mode="3";
				}else if(cb_sms.isChecked()) {
					mode="2";
				}else if(cb_call.isChecked()) {
					mode="1";
				}else {
					Toast.makeText(CallSmsSafeActivity.this, "必须选择拦截模式", 0).show();
					return;
				}
			//	BlackNumberDao dao = new BlackNumberDao(CallSmsSafeActivity.this);
				dao.add(addBlackNumber, mode);
				BlackNumberInfo info = new BlackNumberInfo();
				info.setNumber(addBlackNumber);
				info.setMode(mode);
				infoList.add(0,info);
				mAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
    
}
