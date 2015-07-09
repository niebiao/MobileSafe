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
	//���ݶ��ŵĻص��ӿ�
	//������pd,pd1,pd2,�ӿڿ���ʹ�ú���֮�����ϼ�С
	public interface BackupCallBack{
		////��ʼ����ʱ�����������ֵ
		public void beforeBackup(int max);
		//���ݹ��������ӽ���
		public void onSmsBackup(int progress);
	}
	
	
	//���ű���
	public static void backupSms(Context context,BackupCallBack callBack) throws Exception{
		ContentResolver resolver=context.getContentResolver();
		File file=new File(Environment.getExternalStorageDirectory(),"backupsms.xml");
		FileOutputStream fos=new FileOutputStream(file);
		//����һ���ĸ�ʽ�����û�����д��xml�ļ���
		XmlSerializer serializer=Xml.newSerializer();//��ȡxml�ļ��������������л�����
		//��ʼ��������
		serializer.setOutput(fos,"utf-8");
		serializer.startDocument("utf-9", true);//�ļ�ͷ<?  ?>,�Ƿ���������Ǹ��������ļ�
		serializer.startTag(null, "smss");//������
		Uri uri = Uri.parse("content://sms/");//��ѯ���������ṩ��Դ�룬Ҫ��ѯ���ж���
		Cursor cursor=resolver.query(uri, new String[]{"body", "address", "type", "date"},null,null,null);
		//��ʼ����ʱ�����������ֵ
		int max=cursor.getCount();
		serializer.attribute(null, "max", max+"");//�������������

//		pd.setMax(max);
		callBack.beforeBackup(max);
		int process=0;
		while(cursor.moveToNext()){
			String body=cursor.getString(0);
			String address=cursor.getString(1);
			String type=cursor.getString(2); //1 ����  2����
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
			//���ݹ��������ӽ���
			process++;
//			pd.setProgress(process);
			callBack.onSmsBackup(process);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
		
	}
	//���Ż�ԭ
	public static void restoreSms(Context context) throws Exception{
		Uri uri = Uri.parse("content://sms/");
		//ɾ���Ѵ��ڵĶ���
//		context.getContentResolver().delete(uri, null, null);
		//1 ��ȡsd����xml�ļ�
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
		// 2 ��ȡmax����
		
		// 3 ��ȡÿһ�����ţ�
		
		// 4 �Ѷ��Ų��뵽ϵͳ����Ӧ��
		 
		 
//		 values.put("body", "woshi");
//		 values.put("address", "131");
//		 values.put("type", "1");
//		 values.put("date", "1435063446839");
//		 context.getContentResolver().insert(uri, values);
	}
}
