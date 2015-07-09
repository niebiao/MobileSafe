package com.niebiao.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.niebiao.mobilesafe.db.BlackNumberDBOpenHelper;
import com.niebiao.mobilesafe.domain.BlackNumberInfo;

//数据库的增删改查
public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}
	//查
	public boolean find(String number) {
		boolean result =false;
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number = ?", new String [] {number});
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	//查拦截模式
		public String findMode(String number) {
			String result =null;
			SQLiteDatabase db=helper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select mode from blacknumber where number = ?", new String [] {number});
			if (cursor.moveToNext()) {
				result = cursor.getString(0);
			}
			cursor.close();
			db.close();
			return result;
		}
	//查找所有 分批查找 开始下载的位置 最大下载量
		public List<BlackNumberInfo> findAll(int offset,int maxnumber) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<BlackNumberInfo> result =new ArrayList<BlackNumberInfo>();
			SQLiteDatabase db=helper.getReadableDatabase();
	//		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc", null);
			//从offset位置开始查询，查询maxnumber
			Cursor cursor=db.rawQuery("select number,mode from blacknumber order by _id desc limit ? offset ?", 
					new String[]{String.valueOf(maxnumber),String.valueOf(offset)});
			while (cursor.moveToNext()) {
				BlackNumberInfo bni=new BlackNumberInfo();
				String number=cursor.getString(0);
				String  mode=cursor.getString(1);
				bni.setNumber(number);
				bni.setMode(mode);
				result.add(bni);
			}
			cursor.close();
			db.close();
			return result;
		}
	//增
	public void  add(String number ,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
		db.close();
		
	}
	//改 改拦截模式
	public void  update(String number ,String newmode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values, "number=?", new String[]{number});
		db.close();
	}
	//删
	public void  delete(String number ){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber", "number=?", new String[]{number});
		db.close();
	}
//	//删所有
//		public void  deleteAll(){
//			SQLiteDatabase db = helper.getWritableDatabase();
//			db.de
//			db.close();
//		}
}
