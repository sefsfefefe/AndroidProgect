package org.feiqi.activity.bluetoolth;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.feiqi.DaBaoJieBao.Json;
import com.feiqi.DaBaoJieBao.NoteTitle;
import com.feiqi.debug.DebugUtils;



import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SupportService extends Service {

	public static final String TAG = "SupportService";
	private TelephonyManager tm;
	private myListener listener;
	private String buffer = null;

	private MyContentObserver observer;
	private ContentResolver resolver;
	private NoteTitle noteTitle = null;
	private Json json = null;

	
	final String SMS_URI_ALL = "content://sms/";
	final String SMS_URI_INBOX = "content://sms/inbox";
	final String SMS_URI_SEND = "content://sms/sent";
	final String SMS_URI_DRAFT = "content://sms/draft";
	final String ANDROID_INTENT_ACTION_1="android.intent.action.SupportService";
	private int MY_CALL_STATE = 0X2;
	
	private Util util=new Util();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 格式化时间
	 * 
	 * @return
	 */
	public String refFormatNowDate(long milliseconds) {

		Date nowTime = new Date(milliseconds);
		SimpleDateFormat sdFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH-mm-ss");
		String retStrFormatNowDate = sdFormatter.format(nowTime);
		return retStrFormatNowDate;

	}

	@Override
	public void onCreate() {
		DebugUtils.MyLogD("----d-------SupportService ------onCreate");
		super.onCreate();
		DebugUtils.MyLogD("----d-------SupportService ------onCreate");
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);// 获得一个TELEPHONY_SERVICE的系统服务
		listener = new myListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		resolver = getContentResolver();
		observer = new MyContentObserver(new Handler());
		resolver.registerContentObserver(Uri.parse(SMS_URI_ALL), true, observer);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		//注册广播接收器
	}
	@Override
	public void onDestroy() {

		startService(new Intent(this, SupportService.class));
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		// 销毁内容观察者
		resolver.unregisterContentObserver(observer);
		super.onDestroy();

	}

	/**
	 * 短信监听器
	 * 
	 * @author andong 内容观察者
	 */
	class MyContentObserver extends ContentObserver {

		public MyContentObserver(Handler handler) {
			super(handler);
		}

		/**
		 * 当被监听的内容发生改变时回调
		 */
		@Override
		public void onChange(boolean selfChange) {
			int id = 0;
			DebugUtils.MyLogD("----d-------SupportService ------onChange");
			super.onChange(selfChange);
			Log.i(TAG, "短信改变了");
			String[] projection = new String[] { "address", "person", "body",
					"date", "type" };
			Uri uri = Uri.parse(SMS_URI_INBOX);// 短信的路径
			
			Cursor cursor = getContentResolver().query(uri, projection, null,
					null, null);
			if (cursor != null && cursor.getCount() > 0) {
				// 得到个标签的列号
				int nameColumn = cursor.getColumnIndex("person");
				int phoneNumberColumn = cursor.getColumnIndex("address");
				int smsbodyColumn = cursor.getColumnIndex("body");
				int dateColumn = cursor.getColumnIndex("date");
				int typeColumn = cursor.getColumnIndex("type");
				String type;
				
				noteTitle=new NoteTitle();
				if (cursor.moveToFirst()){
					
					DebugUtils
					.MyLogD("----d-------SupportService ------moveToNext"+id);
					 DebugUtils
						.MyLogD("----d-------SupportService ------moveToNext");
					 //如果下一个短信列表不为空，就continue，一直到最后一条。
					
					DebugUtils
							.MyLogD("----d-------SupportService ------moveToNext");

					if (null != cursor.getString(nameColumn))
						noteTitle.setName(cursor.getString(nameColumn));

					DebugUtils
							.MyLogD("----d-------SupportService ------setPhoneNumber");
					if (null != cursor.getString(phoneNumberColumn))
						noteTitle.setPhoneNumber(cursor.getString(phoneNumberColumn));
					DebugUtils
							.MyLogD("----d-------SupportService ------setSmsbody");
					if (null != cursor.getString(smsbodyColumn))
						noteTitle.setSmsbody(cursor.getString(smsbodyColumn));
					DebugUtils
					.MyLogD("----d-------SupportService ------setDate");
						noteTitle.setDate(refFormatNowDate(cursor
								.getLong(dateColumn)));

					int typeId = cursor.getInt(typeColumn);
					if (typeId == 1) {
						type = "接收";
						noteTitle.setType(type);
					} else if (typeId == 2) {
						type = "发送";
						noteTitle.setType(type);
					} else {
						type = "";
						noteTitle.setType(type);
					}
					json = new Json();
					buffer = json.Json_pacage(noteTitle, id);
					id++;
					BackMessageToActivity(buffer, Util.CMD_SHOW_MSM_CMD,Util.ANDROID_INTENT_ACTION_1);
					DebugUtils
							.MyLogD("----d-------SupportService ------BackMessageToActivity||"
									+ buffer);
				}
				cursor.close();
			}
		}
	}

	public void BackMessageToActivity(String str, int cmd,String action) {// 显示提示信息
		Intent intent = new Intent();
		intent.putExtra(util.getCMD(), cmd);
		intent.putExtra(util.getBuffer(), str);
		intent.setAction(action);
		sendBroadcast(intent);
	}
	
	/**
	 * 电话状态监听器
	 * 
	 * @author 飞奇
	 * 
	 */
	private class myListener extends PhoneStateListener {
		private MediaRecorder recod;

		/**
		 * 删除文件夹
		 * 
		 * @param dir
		 * @return
		 */
		public boolean deleteDir(File dir) {
			if (dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {

						return false;
					}
				}
			}
			// 目录此时为空，可以删除
			return dir.delete();
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲
				DebugUtils
						.MyLogD("----d-------SupportService ------CALL_STATE_IDLE");
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃
				DebugUtils
						.MyLogD("----d-------SupportService ------CALL_STATE_RINGING");
				Intent intent = new Intent();
				//mIncomingNumber = intent.getStringExtra("incoming_number");
				// mIncomingName=intent.getStringExtra(name);
				BackMessageToActivity(incomingNumber, Util.MY_CALL_STATE_CMD,Util.ANDROID_INTENT_ACTION_1);
			default:
				break;
			}
		}
	}

}