package com.niebiao.mobilesafe.domain;

import android.R.integer;
import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable icon;
	private String name;
	private String packageName;
	private boolean inRom;
	private boolean isUserApp;
	private String appsize;
	private int uid;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getAppsize() {
		return appsize;
	}
	public void setAppsize(String appsize) {
		this.appsize = appsize;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isInRom() {
		return inRom;
	}
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	public boolean isUserApp() {
		return isUserApp;
	}
	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}
	
}
