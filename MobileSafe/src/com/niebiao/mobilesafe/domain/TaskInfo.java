package com.niebiao.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/*
 * 进程实体信息
 */
public class TaskInfo {
	private Drawable icon;
	private String name;
	private String packName;
	private  long memsize;
	private boolean isUserApp;
	private boolean isChecked;
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
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
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public long getMemsize() {
		return memsize;
	}
	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}
	public boolean isUserApp() {
		return isUserApp;
	}
	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}
	@Override
	public String toString() {
		return "TaskInfo [name=" + name + ", packName=" + packName
				+ ", memsize=" + memsize + ", isUserApp=" + isUserApp + "]";
	}
	
}
