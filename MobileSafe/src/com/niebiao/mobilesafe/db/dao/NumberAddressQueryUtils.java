package com.niebiao.mobilesafe.db.dao;

import javax.security.auth.PrivateCredentialPermission;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {
	//�������ݿ��·��
	private static String path="/data/data/com.niebiao.mobilesafe/files/address.db";

	//��ѯ����
	public static String queryNumber(String number) {
		//û�н�����ص绰����
		String address=number;
		//�����ݿ⿽����/data/data/com.niebiao.mobilesafe/files/address.db
		//SpalashAtivity.copyDB
		SQLiteDatabase database=SQLiteDatabase.openDatabase
				(path, null, SQLiteDatabase.OPEN_READONLY);
		//�ֻ����� 13 14 15 16 18
		//�ֻ�����������ʽ ^1[34568]\d{9}$ --^1��ͷΪ1�ڶ���������34568�е�һ��
		//\d{9}�����9��������0~9�е������,$��β
		if (number.matches("^1[34568]\\d{9}$")) {//\\dΪת��\d
			Cursor cursor =database.rawQuery
					("select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[]{number.substring(0,7)});//����ǰ��λ
			while(cursor.moveToNext()){
				String location = cursor.getString(0);
				address=location;
			}
			cursor.close();
		}else {
			//�����绰����
			switch (number.length()) {
			case 3:
				//110
				address="���˺���";
				break;
			case 4:
				//110
				address="ģ����";
				break;
			case 5:
				//110
				address="�ͷ�����";
				break;
			case 7:
				//110
				address="���غ���";
				break;
			case 8:
				//110
				address="���غ���";
				break;
				
			default:
				//��;�绰 0��ͷ������10λ
				if (number.length()>10&&number.startsWith("0")) {
					Cursor cursor =database.rawQuery
							("select location from data2 where area = ?",
									new String[]{number.substring(1,3)});//��һ����������2,3 010-5768975
					while(cursor.moveToNext()){
						String location = cursor.getString(0);
						address=location.substring(0,location.length()-2);//�Ϻ��ƶ���ȥ���ƶ�
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
