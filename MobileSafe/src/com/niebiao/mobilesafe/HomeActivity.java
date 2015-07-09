package com.niebiao.mobilesafe;

import com.niebiao.mobilesafe.utils.MD5Utils;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	protected static final String TAG = "HomeActivity";
	private GridView list_home;
	private MyAdapter mAdapter;
	private SharedPreferences sp;
	private static String []names = {
		"�ֻ�����","ͨѶ��ʿ","�������", 
		"���̹���","����ͳ��","�ֻ�ɱ��",
		"��������","�߼�����","��������"		};
	private static int []ids = {
		R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button btn_ok;
	private Button btn_cancel;
	private AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		list_home= (GridView) findViewById(R.id.gv_function);
		mAdapter = new MyAdapter();
		list_home.setAdapter(mAdapter);
		sp = getSharedPreferences("config", MODE_APPEND);
		list_home.setOnItemClickListener(new OnItemClickListener() {
			Intent intent ;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0: //�ֻ���					showLostFindDialog();
					showLostFindDialog();
					break;
				case 1://����������
					intent = new Intent(HomeActivity.this, CallSmsSafeActivity.class);
					startActivity(intent);
					break;
				case 2://�������
					intent = new Intent(HomeActivity.this, AppManageActivity.class);
					startActivity(intent);
					break;
				case 3://���̹���
					intent = new Intent(HomeActivity.this, TaskManagerActivity.class);
					startActivity(intent);
					break;
				case 4://��������
					intent = new Intent(HomeActivity.this, TrafficManageActivity.class);
					startActivity(intent);
					break;
				case 6://��������
					intent = new Intent(HomeActivity.this, ClearCacheActivity.class);
					startActivity(intent);
					break;
				case 7: //����
					intent = new Intent(HomeActivity.this,AtoolActivity.class);
					startActivity(intent);
					break;
				case 8: //����
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				}
			}


		});
		
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.activity_item, null);
			ImageView iv_item=(ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			iv_item.setImageResource(ids[position]);
			tv_item.setText(names[position]);
			return view;
		}
		
	}
	private void showLostFindDialog() {
		if(isSetupPwd()){
			//�Ѿ����ù�����
			showEnterDialog();
		}else {
			//û�����ù�����
			showSetupPwdDialog();
		}
	}
	//��������Ի���
	private void showSetupPwdDialog() {
		AlertDialog.Builder builder=new Builder(HomeActivity.this);
		View view=View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ȡ������
				String pwd = et_setup_pwd.getText().toString().trim();
				String pwd1 = et_setup_confirm.getText().toString().trim();
				if (TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd1)) {
					Toast.makeText(HomeActivity.this, "����Ϊ��", 1).show();
					return;
				}
				//�ж�һ�� ����
				if (pwd.equals(pwd1)) {
					Editor editor=sp.edit();
					editor.putString("password",MD5Utils.md5Password(pwd));
					editor.commit();
					alertDialog.dismiss();
				}else {
					Toast.makeText(HomeActivity.this, "���벻һ��", 1).show();
					return;
				}
			}
		});
		builder.setView(view);
		alertDialog=builder.show();
	}
	//��������Ի���
	private void showEnterDialog() {
		AlertDialog.Builder builder=new Builder(HomeActivity.this);
		View view=View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ȡ������
				String pwd = et_setup_pwd.getText().toString().trim();
				String savePwd = sp.getString("password", null);
				if (TextUtils.isEmpty(pwd)) {
					Toast.makeText(HomeActivity.this, "����Ϊ��", 1).show();
					return;
				}
				//�ж�һ�� ����
				if (MD5Utils.md5Password(pwd).equals(savePwd)) {
					alertDialog.dismiss();
					Log.i(TAG, "�����ֻ���������");
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				}else {
					Toast.makeText(HomeActivity.this, "�������,����������", 1).show();
					return;
				}
			}
		});
		builder.setView(view);
		alertDialog=builder.show();
	}



	private boolean isSetupPwd(){
		String password = sp.getString("password",null);
		return !TextUtils.isEmpty(password);
	}
	
}
