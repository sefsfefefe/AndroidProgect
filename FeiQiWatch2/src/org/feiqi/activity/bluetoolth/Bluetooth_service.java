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
import android.util.Log;

public class Bluetooth_service extends Service {

	private CommandReceiver cmdReceiver;
	// 输入输出流
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private InputStream inStream = null;
	private Util util = new Util();
	private static BluetoothAdapter bluetoothAdapter = null;
	private static BluetoothDevice remoteDevice = null;

	private boolean bluetoothFlag = false;

	protected static final int CONNECT_SUCCESS = 0;
	protected static final int WRITE_MSG = 1;

	private static String WriteBuffer = null;// 用来存储发送的数据

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
		DebugUtils.MyLogD("----d-------Bluetooth_service ------onStartCommand");
		IntentFilter filter = new IntentFilter();

		filter.addAction(Util.ANDROID_INTENT_ACTION_3);// 接收BluetoothActivity_service命令
		filter.addAction(Util.ANDROID_INTENT_ACTION_1);// 接收SupportService命令
		// 注册Broadcast Receiver
		Bluetooth_service.this.registerReceiver(cmdReceiver, filter);
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
			WriteBuffer=new String();
			BrodcastDataFenxi brod=new BrodcastDataFenxi();
			WriteBuffer=brod.BrodcastDataFenxi_string(Util.ANDROID_INTENT_ACTION_1, intent);
			DebugUtils
			.MyLogD("----d-------DisplayToast ---(CommandReceiver)--WriteBuffer>>>>"+ WriteBuffer);
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
			if(WriteBuffer==null);
			if (WriteBuffer!=null) {
				DebugUtils
				.MyLogD("----d-------DisplayToast ---(CommandReceiver)--WriteBuffer>>>>"+ WriteBuffer);
		sendCmd(WriteBuffer.getBytes());
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != cmdReceiver)
			unregisterReceiver(cmdReceiver);
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

	public static boolean pair(String strAddr, String strPsw) {
		boolean result = false;
		// 蓝牙设备适配器
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// 取消发现当前设备的过程
		bluetoothAdapter.cancelDiscovery();
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}
		if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // 检查蓝牙地址是否有效
			Log.d("mylog", "devAdd un effient!");
		}
		// 由蓝牙设备地址获得另一蓝牙设备对象
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				Log.d("mylog", "NOT BOND_BONDED");
				if (ClsUtils.setPin(device.getClass(), device, strPsw)) {// 手机和蓝牙采集器配对

					if (ClsUtils.createBond(device.getClass(), device)) {
						// ClsUtils.cancelPairingUserInput(device.getClass(),
						// device);

						remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
						Log.d("mylog", "setPiN SUCCESS!");
						result = true;
					}
					return false;
				}
				return false;
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			} //

		} else {
			Log.d("mylog", "HAS BOND_BONDED");
			try {
				// ClsUtils这个类的的以下静态方法都是通过反射机制得到需要的方法
				ClsUtils.createBond(device.getClass(), device);// 创建绑定
				ClsUtils.setPin(device.getClass(), device, strPsw);// 手机和蓝牙采集器配对
				ClsUtils.createBond(device.getClass(), device);
				// ClsUtils.cancelPairingUserInput(device.getClass(), device);
				remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			}
		}
		return result;
	}

	// 链接服务端
	public void connectDevice(final String bluetoolthAddress,
			final String my_UUID) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				//if (pair(bluetoolthAddress, Util.strPsw)&& (remoteDevice != null)) {
					DebugUtils
							.MyLogD("----d-------Bluetooth_service --(connectDevice)---bluetoolthAddress"
									+ bluetoolthAddress);
					bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					remoteDevice = bluetoothAdapter.getRemoteDevice(bluetoolthAddress);
					// 根据UUID 创建一个并返回一个BluetoothSocket
					try {
						btSocket = remoteDevice
								.createRfcommSocketToServiceRecord(UUID
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
							bluetoothAdapter.cancelDiscovery();
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
						try {
							btSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						bluetoothFlag = false;
					}
				} 
			/*else {
					DebugUtils
							.MyLogD("----d-------Bluetooth_service --(connectDevice)---pair false");
//					if (null != cmdReceiver)
//						Bluetooth_service.this.unregisterReceiver(cmdReceiver);
					stopSelf();
				}
		//	}*/
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
		DebugUtils
		.MyLogD("----d-------Bluetooth_service --(connectDevice)---sendCmd"
				+ buffer.toString());
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
		DebugUtils
		.MyLogD("----d-------Bluetooth_service --(connectDevice)---sendCmd  success");
	}

	public class Readthread extends Thread {
		@Override
		public void run() {
			super.run();
			// 读数据

			int ret = -1;
			int count = 0;
			byte[] buffer = new byte[100];
			try {
				while (bluetoothFlag) {
					ret = inStream.read(buffer);
					count += ret;
					if (ret < 0) {
						BrodCastMsg(buffer.toString(),
								Util.ANDROID_INTENT_ACTION_2,
								Util.READ_MSG_FALSE_CMD);
						inStream.close();
						btSocket.close();
						break;
					}
					if (ret == 0) {
						BrodCastMsg(Util.CONNETC_NULL,
								Util.ANDROID_INTENT_ACTION_2,
								Util.READ_MSG_NULL_CMD);
						inStream.close();
						btSocket.close();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (count == buffer.length)
				BrodCastMsg(buffer.toString(), Util.ANDROID_INTENT_ACTION_2,
						Util.READ_MSG_SUCCESS_CMD);
			else {
				try {
					inStream.close();
					btSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
