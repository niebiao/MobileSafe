package com.niebiao.mobilesafe.ui;

import com.niebiao.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*
 * �Զ�����Ͽؼ�
 * ������TextView
 * һ��CheckBox
 * һ��View--һ��ֱ��
 */
public class SettingItemView extends RelativeLayout {
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_update;
	private String title;
	private String desc_on;
	private String desc_off;


	private void initView(Context context) {
		//��һ�������ļ�--��view�����Ҽ�����settingItemView��
		View.inflate(context, R.layout.setting_item_view, SettingItemView.this);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		tv_desc= (TextView) findViewById(R.id.tv_desc);
		tv_update = (TextView) findViewById(R.id.tv_update);
	}
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		title = attrs .getAttributeValue("http://schemas.android.com/apk/res/com.niebiao.mobilesafe", "title");
		desc_on = attrs .getAttributeValue("http://schemas.android.com/apk/res/com.niebiao.mobilesafe", "desc_on");
		desc_off = attrs .getAttributeValue("http://schemas.android.com/apk/res/com.niebiao.mobilesafe", "desc_off");
		tv_update.setText(title);
		setDesc(desc_off);
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}
	//У����Ͽؼ��Ƿ�ѡ��
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	//������Ͽؼ���״̬
	public void setCheck(boolean checked) {
		if (checked) {
			setDesc(desc_on);
		}else {
			setDesc(desc_off);
		}
		
		cb_status.setChecked(checked);
	}
	//��Ͽؼ��и���״̬����
	public void setDesc(String text){
		tv_desc.setText(text);
	}
}
