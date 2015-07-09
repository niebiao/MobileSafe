package com.niebiao.mobilesafe.utils;

import java.nio.Buffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;

public class MD5Utils {
	//MD5加密
	public static String md5Password(String password)   {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte [] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			//把每一个byte做一个与运算
			for (byte b:result) {
				//与运算
				int number = b&0xff;
				String str = Integer.toHexString(number);
				if (str.length()==1) {
					buffer.append(0);
				}
				buffer.append(str);
			}
			return buffer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
	}
}
