package com.niebiao.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsUtils {
	//备份短信的回调接口
	//可以有pd,pd1,pd2,接口可以使得函数之间的耦合减小
	public interface BackupCallBack{
		////开始备份时进度条的最大值
		public void beforeBackup(int max);
		//备份过程中增加进度
		public void onSmsBackup(int progress);
	}
	
	
	//短信备份
	public static void backupSms(Context context,BackupCallBack callBack) throws Exception{
		ContentResolver resolver=context.getContentResolver();
		File file=new File(Environment.getExternalStorageDirectory(),"backupsms.xml");
		FileOutputStream fos=new FileOutputStream(file);
		//按照一定的格式，把用户短信写到xml文件里
		XmlSerializer serializer=Xml.newSerializer();//获取xml文件的生成器（序列化器）
		//初始化生成器
		serializer.setOutput(fos,"utf-8");
		serializer.startDocument("utf-9", true);//文件头<?  ?>,是否独立，这是个独立的文件
		serializer.startTag(null, "smss");//短信组
		Uri uri = Uri.parse("content://sms/");//查询短信内容提供者源码，要查询所有短信
		Cursor cursor=resolver.query(uri, new String[]{"body", "address", "type", "date"},null,null,null);
		//开始备份时进度条的最大值
		int max=cursor.getCount();
		serializer.attribute(null, "max", max+"");//设置最大量属性

//		pd.setMax(max);
		callBack.beforeBackup(max);
		int process=0;
		while(cursor.moveToNext()){
			String body=cursor.getString(0);
			String address=cursor.getString(1);
			String type=cursor.getString(2); //1 发送  2接收
			String date=cursor.getString(3);
			serializer.startTag(null, "sms");
			
			serializer.startTag(null,"body");
			serializer.text(body);
			serializer.endTag(null, "body");
			
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");
			
			serializer.startTag(null,"type");
			serializer.text(type);
			serializer.endTag(null,"type");
			
			serializer.startTag(null,"date");
			serializer.text(date);
			serializer.endTag(null, "date");
			
			serializer.endTag(null, "sms");
			//备份过程中增加进度
			process++;
//			pd.setProgress(process);
			callBack.onSmsBackup(process);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
		
	}
	//短信还原
	public static void restoreSms(Context context) throws Exception{
		Uri uri = Uri.parse("content://sms/");
		//删除已存在的短信
//		context.getContentResolver().delete(uri, null, null);
		//1 读取sd卡的xml文件
		File file=new File(Environment.getExternalStorageDirectory(),"backupsms.xml");
		FileInputStream fis=new FileInputStream(file);
		XmlPullParser parser=Xml.newPullParser();
		parser.setInput(fis, "utf-8");
		int eventType=parser.getEventType();
		String body=null;
		String address=null;
		String type=null;
		String date=null;
		ContentValues values=null;
		 while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					          if ("body".equals(tagName)) {
					        	  body=parser.nextText();
							}else if ("address".equals(tagName)) {
								address=parser.nextText();
							}else if ("type".equals(tagName)) {
								type=parser.nextText();
							}else if ("date".equals(tagName)) {
								date=parser.nextText();
							}
					break;

				case XmlPullParser.END_TAG:
					if ("sms".equals(tagName)) {
						values=new ContentValues();
						values.put("body", body);
						values.put("address", address);
						values.put("type", type);
						values.put("date", date);
					   context.getContentResolver().insert(uri, values);
					}
					break;
				}
				eventType = parser.next();
			}
		 fis.close();
		// 2 读取max属性
		
		// 3 读取每一条短信，
		
		// 4 把短信插入到系统短信应用
		 
		 
//		 values.put("body", "woshi");
//		 values.put("address", "131");
//		 values.put("type", "1");
//		 values.put("date", "1435063446839");
//		 context.getContentResolver().insert(uri, values);
	}
}
