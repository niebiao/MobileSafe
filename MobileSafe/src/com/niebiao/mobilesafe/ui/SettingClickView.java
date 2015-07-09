package com.niebiao.mobilesafe.ui;

import com.niebiao.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*
 * �Զ�����Ͽؼ�
 * ������TextView
 * һ��ͼƬ
 * һ��View--һ��ֱ��
 */
public class SettingClickView extends RelativeLayout {
	private ImageView cb_status;
	private TextView tv_desc;
	private TextView tv_update;
	private String title;
	private String desc_on;
	private String desc_off;


	private void initView(Context context) {
		//��һ�������ļ�--��view�����Ҽ�����settingItemView��
		View.inflate(context, R.layout.setting_click_view, SettingClickView.this);
		cb_status =  (ImageView) this.findViewById(R.id.cb_status);
		tv_desc= (TextView) findViewById(R.id.tv_desc);
		tv_update = (TextView) findViewById(R.id.tv_update);
	}
	
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		title = attrs .getAttributeValue("http://schemas.android.com/apk/res/com.niebiao.mobilesafe", "title");
		desc_on = attrs .getAttributeValue("http://schemas.android.com/apk/res/com.niebiao.mobilesafe", "desc_on");
		desc_off = attrs .getAttributeValue("http://schemas.android.com/apk/res/com.niebiao.mobilesafe", "desc_off");
		tv_update.setText(title);
		setDesc(desc_off);
	}

	public SettingClickView(Context context) {
		super(context);
		initView(context);
	}
	
	//������Ͽؼ���״̬
	public void setCheck(boolean checked) {
		if (checked) {
			setDesc(desc_on);
		}else {
			setDesc(desc_off);
		}
		
	//	cb_status.setChecked(checked);
	}
	//��Ͽؼ��и���״̬����
	public void setDesc(String text){
		tv_desc.setText(text);
	}
	public void setTitle(String title) {
		tv_update.setText(title);
	}

	
}
