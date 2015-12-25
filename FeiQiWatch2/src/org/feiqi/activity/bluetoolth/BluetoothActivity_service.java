package org.feiqi.activity.bluetoolth;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.android.test.login.R;
import com.feiqi.DaBaoJieBao.NoteTitle;
import com.feiqi.debug.DebugUtils;
import com.feiqi.setting.PreferenceSetting;
import com.feiqi.setting.SettingKey;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class BluetoothActivity_service extends Activity {

	private PreferenceSetting setting = null;

	
	/************** service 命令 *********/
	static final int CMD_STOP_SERVICE = 0x01;
	static final int CMD_SEND_DATA = 0x02;
	static final int CMD_SYSTEM_EXIT = 0x03;
	static final int CMD_SHOW_TOAST = 0x04;
	static final int CMD_BIND_DATA = 0x6;
	private BluetoothService receiver;
	Map<String, Object> inputMap = new HashMap<String, Object>();


	private BluetoothAdapter bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
	private static final long SCAN_PERIOD = 50000;
	private Util util = new Util();
	private NoteTitle noteTitle = new NoteTitle();
	private boolean mScanning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoolth_activity);
		DebugUtils
				.MyLogD("----d-------BluetoothActivity_service ------onCreate");
		getSharedPreferences();
		OpenDevice();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Intent intent2 = new Intent(BluetoothActivity_service.this,
				SupportService.class);
		startService(intent2);
	}

	private class MyThread extends Thread {
		@Override
		public void run() {
			super.run();

			Intent intent1 = new Intent(BluetoothActivity_service.this,
					Bluetooth_service.class);
			startService(intent1);
		}
	}

	// 判断蓝牙是否打开
	public void OpenDevice() {
		if (bluetoothadapter != null) { // 判断是否有蓝牙
			if (!bluetoothadapter.isEnabled()) {// 判断蓝牙 是否开启，未开启则请用户开启
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 1);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (receiver != null) {
			BluetoothActivity_service.this.unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		scanDevice();
		receiver = new BluetoothService();
		IntentFilter filter = new IntentFilter();
		
		filter.addAction("android.intent.action.cmd.back");
		 filter.addAction(BluetoothDevice.ACTION_FOUND);//链接问题，到时再看
		BluetoothActivity_service.this.registerReceiver(receiver, filter);
		super.onResume();

	}

	public void showToast(String str) {// 显示提示信息
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	private void BrodCastMsg(String str, String intentAction, int cmd,
			NoteTitle noteTile_object) {
		Intent intent = new Intent();
		intent.putExtra(util.getCMD(), cmd);
		if (null != str) {
			intent.putExtra(util.getBuffer(), str);
			DebugUtils
					.MyLogD("----d-------BluetoothActivity_service --(BrodCastMsg)---str="
							+ str);
		}
		if (null != noteTile_object.getBluetoolthAddress()) {
			intent.putExtra(util.getADDRESS(),
					noteTile_object.getBluetoolthAddress());
			DebugUtils
					.MyLogD("----d-------BluetoothActivity_service --(BrodCastMsg)---BluetoolthAddress="
							+ noteTile_object.getBluetoolthAddress());
		}
		if (null != noteTile_object.getMY_UUID()) {
			intent.putExtra(util.getmUUID(), noteTile_object.getMY_UUID());
			DebugUtils
					.MyLogD("----d-------BluetoothActivity_service --(BrodCastMsg)---MY_UUID="
							+ noteTile_object.getMY_UUID());
		}
		intent.setAction(intentAction);
		sendBroadcast(intent);
	}

	public class BluetoothService extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			DebugUtils
					.MyLogD("----d-------BluetoothActivity_service ------BluetoothService");
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.cmd.back")) {
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");

				if (cmd == CMD_SHOW_TOAST) {
					String str = bundle.getString("str");
					showToast(str);
					BluetoothActivity_service.this.finish();
				}

				else if (cmd == CMD_SYSTEM_EXIT) {
					stopService(intent);
				}

			}
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)){
				BluetoothDevice device =
						intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(device.getAddress().equals(noteTitle.getBluetoolthAddress())){
				mScanning = true;
				bluetoothadapter.cancelDiscovery();
				MyThread myThread = new MyThread();
				myThread.start();
			}
			}
		}
	}

	Handler mHandler = new Handler();

	// 扫描周围设备，进行配对
	public void scanDevice() {
		Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
		 mHandler.postDelayed(new Runnable() {
		
		 @Override
		 public void run() {
		 mScanning = false;
		 if (receiver != null) {
				BluetoothActivity_service.this.unregisterReceiver(receiver);
			}
		 finish();
		 }
		
		 }, SCAN_PERIOD);
		
		bluetoothadapter.startDiscovery();// 开始扫描设备
		Set<BluetoothDevice> set = bluetoothadapter.getBondedDevices();
		for (BluetoothDevice bd : set) {
			DebugUtils
			.MyLogD("----d-------BluetoothActivity_service --(scanDevice)--for");
			if ((bd.getAddress().equals(noteTitle.getBluetoolthAddress()))) {
				mScanning = true;
				DebugUtils
						.MyLogD("----d-------BluetoothActivity_service --(scanDevice)--BluetoolthName()"
								+ bd.getName()
								+ "BluetoolthAddress()"
								+ bd.getAddress());
				break;
			}
		}
		DebugUtils
				.MyLogD("----d-------BluetoothActivity_service --(scanDevice)--if (mScanning)");
		if (mScanning) {
			bluetoothadapter.startDiscovery();
			DebugUtils
					.MyLogD("----d-------BluetoothActivity_service --(scanDevice)--if (mScanning)");
			MyThread myThread = new MyThread();
			myThread.start();
		}
	}

	// 读取配置文件，获得保存的对方的蓝牙设备名字和MAC地址
	public void getSharedPreferences() {

		setting = new PreferenceSetting(BluetoothActivity_service.this,
				PreferenceSetting.FILE_NAME_BLUETOOTH);
		DebugUtils.MyLogD("----d-------BluetoolthAcvity ------Map<String, ?>");
		Map<String, ?> allPairs = setting.getSetting();
		if (allPairs != null && !allPairs.isEmpty()) {
			DebugUtils
					.MyLogD("----d-------BluetoolthAcvity ------get(SettingKey.isBluetoothMark)");
			boolean isMark = (Boolean) allPairs.get(SettingKey.isBluetoothMark);
			DebugUtils.MyLogD("----d-------BluetoolthAcvity ------" + isMark);
			if (isMark) {

				if (null != (String) allPairs.get(SettingKey.bluetoolthAddress))
					noteTitle.setBluetoolthAddress((String) allPairs
							.get(SettingKey.bluetoolthAddress));
				else {
					showToast("请先扫描二维码，获取手表的MAC地址!!");
					BluetoothActivity_service.this.finish();
				}

				DebugUtils.MyLogD("----d-------BluetoolthAcvity ------"
						+ noteTitle.getBluetoolthAddress());
				if (null != (String) allPairs.get(SettingKey.bluetoolthName))
					noteTitle.setBluetoolthName((String) allPairs
							.get(SettingKey.bluetoolthName));
				else {
					showToast("请先扫描二维码，获取手表的bluetoolthName!!");
					BluetoothActivity_service.this.finish();
				}
				if (null != (String) allPairs.get(SettingKey.uuid))
					noteTitle
							.setMY_UUID((String) allPairs.get(SettingKey.uuid));
				else {
					showToast("请先扫描二维码，获取手表的uuid!!");
					BluetoothActivity_service.this.finish();
				}
				DebugUtils.MyLogD("----d-------BluetoolthAcvity ------"
						+ noteTitle.getMY_UUID());
				BrodCastMsg(null, Util.ANDROID_INTENT_ACTION_3,
						Util.CMD_BIND_DATA_CMD, noteTitle);

			}
		} else {
			inputMap.put(SettingKey.bluetoolthAddress, "");
			inputMap.put(SettingKey.bluetoolthName, "");
			inputMap.put(SettingKey.isBluetoothMark, false);
			inputMap.put(SettingKey.uuid, "");
			setting.saveAllSetting(inputMap);
			Toast.makeText(BluetoothActivity_service.this, "请用二维码扫描获取手表MAC地址",
					Toast.LENGTH_SHORT);
			return;
		}
		return;
	}
}
