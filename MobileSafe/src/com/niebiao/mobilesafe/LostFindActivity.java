package com.niebiao.mobilesafe;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private String safeNumber;
	private boolean protecting;
	private ImageView iv_protecting;
	private TextView tv_safenumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//判断是否做过设置向导
		sp=getSharedPreferences("config", MODE_PRIVATE);
		boolean configed =sp.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lostfind);
			safeNumber = sp.getString("safenumber", null);
			protecting =  sp.getBoolean("protecting", false);
			tv_safenumber=(TextView) findViewById(R.id.tv_safenumber);
			iv_protecting=(ImageView) findViewById(R.id.iv_protecting);
			tv_safenumber.setText(safeNumber);
			if (protecting) {
				iv_protecting.setImageResource(R.drawable. lock);
			}else {
				iv_protecting.setImageResource(R.drawable.unlock);
			}
		}else {
			Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}
	
	}
	//重新进入防盗设置
	public void reEnterSetup(View view){
		Intent intent = new Intent( LostFindActivity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}
}
