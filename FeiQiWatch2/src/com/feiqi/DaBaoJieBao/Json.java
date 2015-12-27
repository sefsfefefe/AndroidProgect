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
		JSONObject data_json=new JSONObject();
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
			
			data_json.put("long", noteTitle_json.length());
			data_json.put("data_type", noteTitle_json);
			
			 Log.d(TAG, "noteTitle_json.put(noteTile.getType())\n");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		JSONArray array = new JSONArray();
//		      array.put(noteTitle_json);
		      String jsonStr = data_json.toString();
		return jsonStr;
	}
	
	
	public NoteTitle Json_data_unpacage(String buffer) throws JSONException{
		NoteTitle notetile =new NoteTitle();
		if(!buffer.isEmpty()){
			StringBuilder builder = new StringBuilder();
			builder.append(buffer);
			JSONObject jsonObject = new JSONObject(builder.toString());
			notetile.setData_type(jsonObject.getInt("data_type"));
			notetile.setLong(jsonObject.getInt("long"));
			if(notetile.getLong()==jsonObject.getJSONObject("content").length()&&!(jsonObject.isNull("content"))){
				StringBuilder builder_content = new StringBuilder();
				builder_content.append(jsonObject.getJSONObject("content").toString());
				JSONObject jsonObject_content=new JSONObject(builder_content.toString());
			}
		}
		return notetile;
	}
	
	public NoteTitle Json_unpacage(String buffer) throws JSONException{
		NoteTitle notetile =new NoteTitle();
		DebugUtils.MyLogD("----d-------Json ------Json_unpacage"+buffer);
		if(buffer.length()!=0){
			StringBuilder builder = new StringBuilder();
			builder.append(buffer);
			JSONObject jsonObject = new JSONObject(builder.toString());
			if(!jsonObject.getString("bluetoolthName").isEmpty())
			notetile.setBluetoolthName(jsonObject.getString("bluetoolthName"));
			DebugUtils.MyLogD("----d-------Json ------onActivityResult"+notetile.getBluetoolthName());
			if(!jsonObject.getString("bluetoolthAddress").isEmpty())
			notetile.setBluetoolthAddress(jsonObject.getString("bluetoolthAddress"));
			DebugUtils.MyLogD("----d-------Json ------onActivityResult"+notetile.getBluetoolthAddress());
			if(!jsonObject.getString("MY_UUID").isEmpty())
			notetile.setMY_UUID(jsonObject.getString("MY_UUID"));
		}
		
		return notetile;
	}
}
