package org.feiqi.activity.bluetoolth;



import com.feiqi.DaBaoJieBao.NoteTitle;
import com.feiqi.debug.DebugUtils;

import android.content.Intent;
import android.os.Bundle;

public class BrodcastDataFenxi {

	public BrodcastDataFenxi(){
		
	}
	public BrodcastDataFenxi(String action_buffer, Intent intent,
			NoteTitle noteTitle) {
		if (intent.getAction().equals(action_buffer)) {
			Bundle bundle = intent.getExtras();
			int cmd = bundle.getInt("cmd");
			DebugUtils
					.MyLogD("---d-------BrodcastDataFenxi -----Intent_action_jieshou_BluetoothActivity_service");
			if (cmd == Util.CMD_BIND_DATA_CMD) {
				noteTitle.setBluetoolthAddress(bundle.getString(Util.ADDRESS));
				DebugUtils
						.MyLogD("----d-------DisplayToast -----CommandReceiver>"
								+ bundle.getString(Util.ADDRESS));

				noteTitle.setMY_UUID(bundle.getString(Util.mUUID));
				DebugUtils
						.MyLogD("----d-------DisplayToast -----CommandReceiver>"
								+ bundle.getString(Util.mUUID));
			}
		}
	}

	public String BrodcastDataFenxi_string(String action_buffer, Intent intent) {
		Bundle bundle = intent.getExtras();
		int cmd = bundle.getInt("cmd");
		String buffer=new String();
		DebugUtils
				.MyLogD("----d-------BrodcastDataFenxi -----");
		if (cmd == Util.CMD_SHOW_MSM_CMD) {
			buffer = bundle.getString(Util.Buffer);
			DebugUtils
					.MyLogD("----d-------BrodcastDataFenxi ----->>>"
							+ buffer);
			return buffer;
		}
		if (cmd == Util.MY_CALL_STATE_CMD) {
			buffer = bundle.getString(Util.Buffer);
			DebugUtils
					.MyLogD("----d-------BrodcastDataFenxi ----->>>"
							+ buffer);
			return buffer;
		}
		return buffer;
	}
}
