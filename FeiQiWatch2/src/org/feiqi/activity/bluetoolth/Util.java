package org.feiqi.activity.bluetoolth;

public class Util {

	public static String ANDROID_INTENT_ACTION_1 = "android.intent.action.SupportService";
	public static String ANDROID_INTENT_ACTION_2 = "android.intent.action.Bluetooth_service";
	public static String ANDROID_INTENT_ACTION_3 = "android.intent.action.BluetoothActivity_service";
	//public static String ANDROID_INTENT_ACTION_4 = "android.intent.action.BluetoothActivity_service";
	
	
	public static String Buffer = "str";
	public static String ADDRESS = "bluetoolthAddress";
	public static String mUUID = "uuid";
	public static String NAME = "bluetoolthName";
	public static String CMD = "cmd";

	public static int CMD_SHOW_MSM_CMD = 0x1;
	public static int MY_CALL_STATE_CMD = 0X2;
	public static int CMD_BIND_DATA_CMD = 0x3;
	private final int CMD_SYSTEM_EXIT = 0x4;
	public static int CONNECT_SUCCESS_CMD = 0x5;
	public static int CONNECT_FALSE_CMD = 0x6;
	public static int READ_MSG_FALSE_CMD = 0x7;
	public static int READ_MSG_NULL_CMD = 0x8;
	public static int READ_MSG_SUCCESS_CMD = 0x9;
	
	
	public static int READ_MSG_SUCCESS = 0x8;
	public static String CONNETC_SUCCESS = "链接成功！可以开始通信！";
	public static String CONNETC_FALSE = "链接失败！";
	public static String CONNETC_NULL = "数据为空！";

	public static String strPsw = "0000";

	public String getBuffer() {
		return Buffer;
	}

	public String getANDROID_INTENT_ACTION_2() {
		return ANDROID_INTENT_ACTION_2;
	}


	public String getADDRESS() {
		return ADDRESS;
	}

	public String getmUUID() {
		return mUUID;
	}

	public String getNAME() {
		return NAME;
	}

	public String getCMD() {
		return CMD;
	}

	public int getCMD_SYSTEM_EXIT() {
		return CMD_SYSTEM_EXIT;
	}
}
