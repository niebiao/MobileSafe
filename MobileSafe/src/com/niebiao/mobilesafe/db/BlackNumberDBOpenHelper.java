package com.niebiao.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
	//�������ݿ�
	public BlackNumberDBOpenHelper(Context context) {
		super(context,"blacknumber.db",null,1);//�������α깤��ʹ��ϵͳ�ṩ���汾Ϊ1
	}
	//��һ�δ�������ʼ�����ṹ
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}