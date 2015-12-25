package org.feiqi.activity.bluetoolth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.UUID;

import com.feiqi.DaBaoJieBao.NoteTitle;
import com.feiqi.debug.DebugUtils;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class Bluetooth_service extends Service {

	private CommandReceiver cmdReceiver;
	// 输入输出流
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private InputStream inStream = null;
	private Util util = new Util();
	private BluetoothAdapter bluetooth = null;
	private BluetoothDevice device;

	private boolean bluetoothFlag = false;

	protected static final int CONNECT_SUCCESS = 0;
	protected static final int WRITE_MSG = 1;

	private String WriteBuffer=null;// 用来存储发送的数据

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * ==========================================================================
	 * ==============================
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		cmdReceiver = new CommandReceiver();
		DebugUtils
				.MyLogD("----d-------Bluetooth_service ------onStartCommand");
		IntentFilter filter_1 = new IntentFilter();
		IntentFilter filter_2 = new IntentFilter();
		filter_1.addAction(Util.ANDROID_INTENT_ACTION_3);// 接收BluetoothActivity_service命令
		filter_2.addAction(Util.ANDROID_INTENT_ACTION_1);// 接收SupportService命令
		// 注册Broadcast Receiver
		Bluetooth_service.this.registerReceiver(cmdReceiver, filter_1);
		Bluetooth_service.this.registerReceiver(cmdReceiver, filter_2);
		return super.onStartCommand(intent, flags, startId);
	}

	public class CommandReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			DebugUtils
					.MyLogD("----d-------DisplayToast -----BroadcastReceiver");
			NoteTitle noteTitle = new NoteTitle();
			new BrodcastDataFenxi(Util.ANDROID_INTENT_ACTION_3, intent,
					noteTitle);

			new BrodcastDataFenxi(Util.ANDROID_INTENT_ACTION_1, intent,
					WriteBuffer);

			if (null != noteTitle.getBluetoolthAddress()
					&& null != noteTitle.getMY_UUID()) {
				DebugUtils
						.MyLogD("----d-------DisplayToast -----(BroadcastReceiver)---BluetoolthAddress++--"
								+ noteTitle.getBluetoolthAddress()
								+ "MY_UUID"
								+ noteTitle.getMY_UUID());
				connectDevice(noteTitle.getBluetoolthAddress(),
						noteTitle.getMY_UUID());
			}
			if (null != WriteBuffer) {
				DebugUtils
						.MyLogD("----d-------DisplayToast ---(CommandReceiver)--WriteBuffer>>>>"
								+ WriteBuffer);
				sendCmd(WriteBuffer.getBytes());
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(null!=cmdReceiver)
		Bluetooth_service.this.unregisterReceiver(cmdReceiver);
	}
	public void BrodCastMsg(String str, String intentAction, int cmd) {
		Intent intent = new Intent();
		intent.putExtra("cmd", cmd);
		intent.putExtra("str", str);
		intent.setAction(intentAction);
		intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		sendBroadcast(intent);
	}

	/*
	 * ==========================================================================
	 * ==============================
	 */
	// 链接服务端
	public void connectDevice(final String bluetoolthAddress,
			final String my_UUID) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				bluetooth = BluetoothAdapter.getDefaultAdapter();
				device = bluetooth.getRemoteDevice(bluetoolthAddress);
				DebugUtils
						.MyLogD("----d-------Bluetooth_service --(connectDevice)---bluetoolthAddress"
								+ bluetoolthAddress);
				// 根据UUID 创建一个并返回一个BluetoothSocket
				try {
					btSocket = device.createRfcommSocketToServiceRecord(UUID
							.fromString(my_UUID));
					DebugUtils
					.MyLogD("----d-------Bluetooth_service --(connectDevice)---btSocket="
							+ btSocket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (btSocket != null) {
					// 链接
					try {
						bluetooth.cancelDiscovery();
						btSocket.connect();
						bluetoothFlag = true;
						outStream = btSocket.getOutputStream();
						inStream = btSocket.getInputStream();
						handler.sendEmptyMessage(CONNECT_SUCCESS);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					BrodCastMsg(Util.CONNETC_FALSE,
							Util.ANDROID_INTENT_ACTION_2,
							Util.CONNECT_FALSE_CMD);
					bluetoothFlag = false;
				}
			}

		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CONNECT_SUCCESS:
				BrodCastMsg(Util.CONNETC_SUCCESS, Util.ANDROID_INTENT_ACTION_2,
						Util.CONNECT_SUCCESS_CMD);
				new Readthread().start();
				break;
			default:
				break;
			}
		}
	};

	// 写数据
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

	public class Readthread extends Thread {
		@Override
		public void run() {
			super.run();
			// 读数据

			int ret = -1;
			byte[] buffer = null;
			if (!bluetoothFlag) {
				return;
			}
			try {
				while (bluetoothFlag) {
					ret = inStream.read(buffer);
					if (ret < 0) {
						BrodCastMsg(buffer.toString(),
								Util.ANDROID_INTENT_ACTION_2,
								Util.READ_MSG_FALSE_CMD);
						break;
					}
					if (ret == 0) {
						BrodCastMsg(Util.CONNETC_NULL,
								Util.ANDROID_INTENT_ACTION_2,
								Util.READ_MSG_NULL_CMD);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			BrodCastMsg(buffer.toString(), Util.ANDROID_INTENT_ACTION_2,
					Util.READ_MSG_SUCCESS_CMD);
		}

	}

}
