package com.feiqi.DaBaoJieBao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.feiqi.debug.DebugUtils;

import android.util.Log;

public class Json {
	
	 private String TAG="TextdemoActivity";
    
    JSONObject ID=new JSONObject();
   
    JSONObject noteTitle_json=null;
	public String Json_pacage(NoteTitle noteTile,int id)
	{
		 Log.d(TAG, "Json_pacage(NoteTitle noteTile,int id)\n");
		noteTitle_json=new JSONObject();
		try {
			if(null!=noteTile.getName())
			noteTitle_json.put("name", noteTile.getName());
			
			if(null!=noteTile.getSmsbody())
			noteTitle_json.put("phoneNumber", noteTile.getPhoneNumber());
			
			if(null!=noteTile.getPhoneNumber())
			noteTitle_json.put("body", noteTile.getSmsbody());
			
			if(null!=noteTile.getDate())
			noteTitle_json.put("smsbody", noteTile.getDate());
			
			if(null!=noteTile.getDate())
			noteTitle_json.put("date", noteTile.getDate());
			
			if(null!=noteTile.getType())
			noteTitle_json.put("type", noteTile.getType());
			
			//ID可以用来计数
			//ID.put("ID="+id, noteTitle_json);
			 Log.d(TAG, "noteTitle_json.put(noteTile.getType())\n");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		JSONArray array = new JSONArray();
//		      array.put(noteTitle_json);
		      String jsonStr = noteTitle_json.toString();
		return jsonStr;
	}
	
	public NoteTitle Json_unpacage(String buffer) throws JSONException{
		NoteTitle notetile =new NoteTitle();
		DebugUtils.MyLogD("----d-------Json ------Json_unpacage"+buffer);
		if(buffer.length()!=0){
			StringBuilder builder = new StringBuilder();
			builder.append(buffer);
			JSONObject jsonObject = new JSONObject(builder.toString());
			if(jsonObject.getString("bluetoolthName").length()!=0)
			notetile.setBluetoolthName(jsonObject.getString("bluetoolthName"));
			DebugUtils.MyLogD("----d-------Json ------onActivityResult"+notetile.getBluetoolthName());
			if(null!=jsonObject.getString("bluetoolthAddress"))
			notetile.setBluetoolthAddress(jsonObject.getString("bluetoolthAddress"));
			DebugUtils.MyLogD("----d-------Json ------onActivityResult"+notetile.getBluetoolthAddress());
			if(null!=jsonObject.getString("MY_UUID"))
			notetile.setMY_UUID(jsonObject.getString("MY_UUID"));
		}
		
		return notetile;
	}
}
