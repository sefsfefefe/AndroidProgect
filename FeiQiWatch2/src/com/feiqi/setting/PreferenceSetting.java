package com.feiqi.setting;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceSetting {

	public static String FILE_NAME="setting";
	public static String FILE_NAME_BLUETOOTH="bluetooth_setting";
	private SharedPreferences preferences=null;
	private SharedPreferences.Editor editor=null;
	private Context mContext =null;
	
	
	public PreferenceSetting(Context context,String fileName){
		mContext=context;
		if(preferences==null)
		{
			preferences=mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			editor=preferences.edit();
		}
	}
	public Map<String,?>getSetting(){
		Map<String, ?>map=null;
		if(preferences!=null){
			map=preferences.getAll();
		}
		return map;
	}
	public void saveAllSetting(Map<String,Object>map)
	{
		if(editor !=null){
			for(Map.Entry<String, Object>entry:map.entrySet()){
				String key=entry.getKey();
				Object value=entry.getValue();
				if(value instanceof Integer){
					editor.putInt(key, (Integer) value);
				}
				if(value instanceof Float){
					editor.putFloat(key, (Float) value);
				}
				if(value instanceof Long){
					editor.putLong(key, (Long) value);
				}
				if(value instanceof Boolean){
					editor.putBoolean(key, (Boolean) value);
				}
				if(value instanceof String){
					editor.putString(key, (String) value);
				}
			}
		}
		editor.commit();
	}
}
