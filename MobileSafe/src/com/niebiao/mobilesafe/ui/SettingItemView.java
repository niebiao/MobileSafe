package com.niebiao.mobilesafe.ui;

import com.niebiao.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*
 * 自定义组合控件
 * 有两个TextView
 * 一个CheckBox
 * 一个View--一条直线
 */
public class SettingItemView extends RelativeLayout {
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_update;
	private String title;
	private String desc_on;
	private String desc_off;


	private void initView(Context context) {
		//把一个布局文件--》view，并且加载在settingItemView上
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
	//校验组合控件是否选中
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	//设置组合控件的状态
	public void setCheck(boolean checked) {
		if (checked) {
			setDesc(desc_on);
		}else {
			setDesc(desc_off);
		}
		
		cb_status.setChecked(checked);
	}
	//组合控件中更新状态描述
	public void setDesc(String text){
		tv_desc.setText(text);
	}
}
