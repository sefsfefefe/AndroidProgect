package com.feiqi.debug;

import android.util.Log;

public class DebugUtils {
	public static final String TAG = "feiqiDemo";
	public static final int Debug_level = Log.VERBOSE;
	
	
	public static void MyLogD(String msg){
		if(Debug_level <= Log.DEBUG){
			Log.d(TAG, msg);
		}
	}
	
	public static void MyLogI(String msg){
		if(Debug_level <= Log.INFO){
			Log.i(TAG, msg);
		}
	}
	
	public static void MyLogW(String msg){
		if(Debug_level <= Log.WARN){
			Log.w(TAG, msg);
		}
	}
	
	
	public static void MyLogE(String msg){
		if(Debug_level <= Log.ERROR){
			Log.e(TAG, msg);
		}
	}
	
	
	
}
