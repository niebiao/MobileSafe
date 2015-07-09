package com.niebiao.mobilesafe;

import com.niebiao.mobilesafe.db.dao.NumberAddressQueryUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("ShowToast")
public class NumberAddressQueryActivity extends Activity {
	private EditText et_number;
	private TextView tv_address;
	private String number;
	//震动效果
	private Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		et_number=(EditText) findViewById(R.id.et_number);
		tv_address = (TextView) findViewById(R.id.tv_address);
		number=et_number.getText().toString().trim();
		vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
		et_number.addTextChangedListener(new TextWatcher() {
			//文本发生变化，及时的做出反应
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (arg0.length()>2) {
					String addres=NumberAddressQueryUtils.queryNumber(number);
					tv_address.setText(addres);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@SuppressLint("ShowToast")
	public void query(View view) {
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(NumberAddressQueryActivity.this, "号码为空", 0).show();
			//输入框抖动效果
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number.startAnimation(shake);
			//手机震动效果
			vibrator.vibrate(1000);
			return;
		}else {
			String addres=NumberAddressQueryUtils.queryNumber(number);
			tv_address.setText(addres);
			Toast.makeText(NumberAddressQueryActivity.this, number, 0).show();
		}
	}
}
