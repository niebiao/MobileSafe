package com.niebiao.mobilesafe.test;

import com.niebiao.mobilesafe.db.BlackNumberDBOpenHelper;
import com.niebiao.mobilesafe.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {
	public void testCreateDB()throws Exception {
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
		helper.getWritableDatabase();
	}
	public void  testAdd() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.add("123456789", "call");
		dao.add("00000000", "sms");
		dao.add("111111111", "call");
		dao.add("22222222", "call");
		
	}
public void  testFind() throws Exception{
	BlackNumberDao dao = new BlackNumberDao(getContext());
	if (dao.find("123456789")) {
		System.out.println("在blacknumber表中有：123456789");
	}else {
		System.out.println("没有你想要的号码");
	}

	}
public void  testUpdate() throws Exception{
	BlackNumberDao dao = new BlackNumberDao(getContext());
	dao.update("123456789", "sms");

}
public void  testDelete() throws Exception{
	BlackNumberDao dao = new BlackNumberDao(getContext());
	dao.delete("123456789");

}
}
