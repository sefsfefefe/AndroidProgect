package org.feiqi.activity.bluetoolth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.feiqi.debug.DebugUtils;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

public class BluetoothService_jieshou extends Service {

	CommandReceiver cmdReceiver;
	public boolean threadFlag = true;
	MyThread myThread;
	/************** service 命令 *********/
	static final int CMD_STOP_SERVICE = 0x01;
	static final int CMD_SEND_DATA = 0x02;

	static final int CMD_SHOW_TOAST = 0x04;
	static final int CMD_SHOW_READ = 0x05;
	static final int CMD_BIND_DATA = 0x6;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private InputStream inStream = null;
	public boolean bluetoothFlag = true;
	private BluetoothDevice device;
	private byte[] readBuffer = null;
	private String bluetoolthAddress = null;
	private static UUID MY_UUID;

	private Util util = new Util();
	Handler mHandler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		DebugUtils
				.MyLogD("----d-------BluetoothService_jieshou ------onCreate");
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		cmdReceiver = new CommandReceiver();
		DebugUtils
				.MyLogD("----d-------BluetoothService_jieshou ------onStartCommand");
		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		// 注册一个广播，用于接收Activity传送过来的命令，控制Service的行为，如：发送数据，停止服务等
		filter.addAction(util.getANDROID_INTENT_ACTION_3());// 接收BluetoothActivity_service命令
		filter.addAction(util.getANDROID_INTENT_ACTION_1());// 接收SupportService命令

		// 注册Broadcast Receiver
		BluetoothService_jieshou.this.registerReceiver(cmdReceiver, filter);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(cmdReceiver);// 取消注册的CommandReceiver
		threadFlag = false;
		boolean retry = true;
		while (retry) {
			try {
				myThread.join();
				retry = false;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			connectDevice();
			while (threadFlag) {
				int value = readByte();
				if (value != -1) {
					DisplayToast(value + "");
				}
			}

		}

	}

	public void doJob() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			DisplayToast("蓝牙设备不可用，请打开蓝牙！");
			bluetoothFlag = false;
			return;
		}
		if (!mBluetoothAdapter.isEnabled()) {
			DisplayToast("请打开蓝牙并重新运行程序！");
			bluetoothFlag = false;
			stopService();
			showToast("请打开蓝牙并重新运行程序！");
			return;
		}
		threadFlag = true;
		myThread = new MyThread();
		myThread.start();
	}

	public void connectDevice() {
		DisplayToast("正在尝试连接蓝牙设备，请稍后····");
		if (null != bluetoolthAddress) {
			device = mBluetoothAdapter.getRemoteDevice(bluetoolthAddress);
			if(device!=null)
			showToast("搜索到蓝牙设备!");
		} else {
			DisplayToast("地址为空，请重新用二维码扫描获取！····");
			showToast("地址为空，请重新用二维码扫描获取！");
			threadFlag = false;// 停止线程
			BrodCastMsg(null, util.getANDROID_INTENT_ACTION_2(),
					util.getCMD_SYSTEM_EXIT());
			return;
		}
		try {
			if (MY_UUID != null)
				btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			DisplayToast("套接字创建失败！");
			bluetoothFlag = false;
			return;
		}
		DisplayToast("成功连接蓝牙设备！");
		mBluetoothAdapter.cancelDiscovery();
		try {
			btSocket.connect();
			DisplayToast("连接成功建立，可以开始操控了!");
			showToast("连接成功建立，可以开始操控了!");
			bluetoothFlag = true;
		} catch (IOException e) {
			try {
				btSocket.close();
				bluetoothFlag = false;
			} catch (IOException e1) {
				DisplayToast("连接没有建立，无法关闭套接字！");
			}
		}
		if (bluetoothFlag) {
			try {
				inStream = btSocket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			} // 绑定读接口

			try {
				outStream = btSocket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			} // 绑定写接口

		}
	}

	public void sendCmd(byte[] buffer)// 串口发送数据
	{
		if (!bluetoothFlag) {
			return;
		}
		try {
			outStream.write(buffer);
			outStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int readByte() {// return -1 if no data
		int ret = -1;

		if (!bluetoothFlag) {
			return ret;
		}
		try {
			ret = inStream.read(readBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		readMessagae(readBuffer);
		return ret;
	}

	private void readMessagae(byte[] buffer) {
		String stringBuffer = buffer.toString();
		Intent intent = new Intent();
		intent.putExtra("cmd", CMD_SHOW_READ);
		intent.putExtra("str", stringBuffer);
		intent.setAction("android.intent.action.lxx");
		sendBroadcast(intent);
	}

	public void stopService() {// 停止服务
		threadFlag = false;// 停止线程
		stopSelf();// 停止服务
	}

	public void showToast(String str) {// 显示提示信息
		DisplayToast("showToast！····");
		Intent intent = new Intent();
		intent.putExtra("cmd", CMD_SHOW_TOAST);
		intent.putExtra("str", str);
		intent.setAction("android.intent.action.cmd.back");
		sendBroadcast(intent);
	}

	public void BrodCastMsg(String str, String intentAction, int cmd) {
		Intent intent = new Intent();
		intent.putExtra("cmd", cmd);
		intent.putExtra("str", str);
		intent.setAction(intentAction);
		sendBroadcast(intent);
	}

	public void DisplayToast(String str) {
		DebugUtils.MyLogD("----d-------DisplayToast -----" + str);
	}

	private class CommandReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			DebugUtils
					.MyLogD("----d-------DisplayToast -----BroadcastReceiver");
			Intent_action_jieshou_BluetoothActivity_service(
					util.getANDROID_INTENT_ACTION_3(), intent);

			Intent_Action_jieshou_SupportService(
					util.getANDROID_INTENT_ACTION_1(), intent);
		}
	}

	// ==============================================================================================================
	private void Intent_Action_jieshou_SupportService(String action_buffer,
			Intent intent) {
		if (intent.getAction().equals(action_buffer)) {
			Bundle bundle = intent.getExtras();
			int cmd = bundle.getInt("cmd");

			DebugUtils
					.MyLogD("----d-------BluetoothService_jieshou -----Intent_Action_jieshou_SupportService");
			if (cmd == util.getMY_CALL_STATE()) {
				byte[] send = null;
				getData(intent, util, send);
				DebugUtils
						.MyLogD("----d-------BluetoothService_jieshou -----Intent_Action_jieshou_SupportService>>>"
								+ send);
				sendCmd(send);
			}
			if (cmd == util.getCMD_SHOW_MSM()) {
				byte[] send = null;
				getData(intent, util, send);
				DebugUtils
						.MyLogD("----d-------BluetoothService_jieshou -----Intent_Action_jieshou_SupportService>>>"
								+ send);
				sendCmd(send);
			}
		}
	}

	private void Intent_action_jieshou_BluetoothActivity_service(
			String action_buffer, Intent intent) {
		if (intent.getAction().equals(action_buffer)) {
			Bundle bundle = intent.getExtras();
			int cmd = bundle.getInt("cmd");
			DebugUtils
					.MyLogD("----d-------BluetoothService_jieshou -----Intent_action_jieshou_BluetoothActivity_service");
			if (cmd == util.getCMD_BIND_DATA()) {
				bluetoolthAddress = bundle.getString(util.getADDRESS());
				DebugUtils
						.MyLogD("----d-------DisplayToast -----CommandReceiver>"
								+ bluetoolthAddress);

				MY_UUID = UUID.fromString(bundle.getString(util.getmUUID()));
				DebugUtils
						.MyLogD("----d-------DisplayToast -----CommandReceiver>"
								+ MY_UUID);
				doJob();
			}
		}
	}

	public void getData(Intent intent, Util tit, byte[] send) {
		String buffer = intent.getStringExtra(tit.getBuffer());
		if (buffer.length() > 0)
			send = buffer.getBytes();

	}

}
