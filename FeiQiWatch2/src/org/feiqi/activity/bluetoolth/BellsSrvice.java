package org.feiqi.activity.bluetoolth;


import org.json.JSONException;

import com.android.test.login.R;
import com.feiqi.DaBaoJieBao.Json;
import com.feiqi.DaBaoJieBao.NoteTitle;
import com.feiqi.debug.DebugUtils;

import android.app.Notification;
import android.app.NotificationManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;


public class BellsSrvice extends Service {

	private CommandReceiver BellReceiver;
	private String Buffer;
	NoteTitle noteTitle=new NoteTitle();
	Json json=new Json();
	
	
	//private PendingIntent pd;
	private NotificationManager nm;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		BellReceiver=new CommandReceiver();
		DebugUtils.MyLogD("----d-------BellsSrvice ------onStartCommand");
		IntentFilter filter = new IntentFilter();
		filter.addAction(Util.ANDROID_INTENT_ACTION_2);
		BellsSrvice.this.registerReceiver(BellReceiver, filter);
		return super.onStartCommand(intent, flags, startId);
	}
	public class CommandReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			DebugUtils.MyLogD("----d-------BellsSrvice ------CommandReceiver");
			
			if (intent.getAction().equals(Util.ANDROID_INTENT_ACTION_2)){
				Buffer=new String();
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");
				if(cmd==Util.READ_MSG_SUCCESS_CMD){
					Buffer=bundle.getString(Util.Buffer);
					try {
						noteTitle=json.Json_data_unpacage(Buffer);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(noteTitle.getData_type()==0x4){
						BellsClock();
					}
				}
			}
		}
		
	}
	//响铃函数
	public void BellsClock(){
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//Intent intent = new Intent(this,BellsSrvice.class);
		//pd = PendingIntent.getActivity(BellsSrvice.this, 0, intent, 0);
		 Notification baseNF;
		 int Notification_ID_BASE = 110;
		//新建状态栏通知
		baseNF = new Notification();
		 
		//设置通知在状态栏显示的图标
		baseNF.icon = R.drawable.ic_launcher;
		
		//通知时在状态栏显示的内容
		baseNF.tickerText = "You clicked BaseNF!";
		
		//通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS. 
		//如果要全部采用默认值, 用 DEFAULT_ALL.
		//此处采用默认声音
		baseNF.defaults |= Notification.DEFAULT_SOUND;
		baseNF.defaults |= Notification.DEFAULT_VIBRATE;
		baseNF.defaults |= Notification.DEFAULT_LIGHTS;
		
		//让声音、振动无限循环，直到用户响应
		baseNF.flags |= Notification.FLAG_INSISTENT;
		
		//通知被点击后，自动消失
		baseNF.flags |= Notification.FLAG_AUTO_CANCEL;
		
		//点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个)
		baseNF.flags |= Notification.FLAG_NO_CLEAR;
		
//		
//		//第二个参数 ：下拉状态栏时显示的消息标题 expanded message title
//		//第三个参数：下拉状态栏时显示的消息内容 expanded message text
//		//第四个参数：点击该通知时执行页面跳转
//		baseNF.setLatestEventInfo(BellsSrvice.this, "Title01", "Content01", pd);
		
		//发出状态栏通知
		//The first parameter is the unique ID for the Notification 
		// and the second is the Notification object.
		nm.notify(Notification_ID_BASE, baseNF);
	}
}
