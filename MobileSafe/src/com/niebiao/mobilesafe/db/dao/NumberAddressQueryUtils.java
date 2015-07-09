package com.niebiao.mobilesafe.db.dao;

import javax.security.auth.PrivateCredentialPermission;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {
	//号码数据库的路径
	private static String path="/data/data/com.niebiao.mobilesafe/files/address.db";

	//查询号码
	public static String queryNumber(String number) {
		//没有结果返回电话号码
		String address=number;
		//把数据库拷贝到/data/data/com.niebiao.mobilesafe/files/address.db
		//SpalashAtivity.copyDB
		SQLiteDatabase database=SQLiteDatabase.openDatabase
				(path, null, SQLiteDatabase.OPEN_READONLY);
		//手机号码 13 14 15 16 18
		//手机号码正则表达式 ^1[34568]\d{9}$ --^1开头为1第二个数字是34568中的一个
		//\d{9}后面的9个可以是0~9中的随机数,$结尾
		if (number.matches("^1[34568]\\d{9}$")) {//\\d为转义\d
			Cursor cursor =database.rawQuery
					("select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[]{number.substring(0,7)});//截留前七位
			while(cursor.moveToNext()){
				String location = cursor.getString(0);
				address=location;
			}
			cursor.close();
		}else {
			//其他电话号码
			switch (number.length()) {
			case 3:
				//110
				address="警匪号码";
				break;
			case 4:
				//110
				address="模拟器";
				break;
			case 5:
				//110
				address="客服号码";
				break;
			case 7:
				//110
				address="本地号码";
				break;
			case 8:
				//110
				address="本地号码";
				break;
				
			default:
				//长途电话 0开头，大于10位
				if (number.length()>10&&number.startsWith("0")) {
					Cursor cursor =database.rawQuery
							("select location from data2 where area = ?",
									new String[]{number.substring(1,3)});//第一个到第三个2,3 010-5768975
					while(cursor.moveToNext()){
						String location = cursor.getString(0);
						address=location.substring(0,location.length()-2);//上海移动，去掉移动
					}
					cursor.close();
					//0556-7359505
					cursor =database.rawQuery
							("select location from data2 where area = ?",
									new String[]{number.substring(1,4)});
					while(cursor.moveToNext()){
						String location = cursor.getString(0);
						address=location.substring(0,location.length()-2);
					}
					cursor.close();
				}
				break;
				
			}
		}
		return address;

	}
}
