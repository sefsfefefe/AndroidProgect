package org.feiqi.activity.zhinengshoubiao;


import java.util.HashMap;
import java.util.Map;


import org.feiqi.activity.bluetoolth.BluetoothActivity_service;

import org.json.JSONException;

import com.android.test.login.R;
import com.dtr.zxing.activity.CaptureActivity;
import com.feiqi.DaBaoJieBao.Json;
import com.feiqi.DaBaoJieBao.NoteTitle;
import com.feiqi.debug.DebugUtils;

import com.feiqi.setting.PreferenceSetting;
import com.feiqi.setting.SettingKey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ZhiNengShouBiaoActivity extends Activity {

	private Button bangDing_bt;
	private Button erweima_bt;
	private PreferenceSetting setting=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------onCreate");
		setContentView(R.layout.zhinengshoubiao_activity);
		Init();
	}
	
	
	OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bangDing_bt:
				Intent intent = new Intent();
				intent.setClass(ZhiNengShouBiaoActivity.this, BluetoothActivity_service.class);
				DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------startActivity(intent);");
				startActivity(intent);
				break;
			case R.id.erweima_bt:
				DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------erweima_bt");
				Intent intent2 = new Intent();
				intent2.setClass(ZhiNengShouBiaoActivity.this, CaptureActivity.class);
				startActivityForResult(intent2, 0);
			default:
				break;
			}
		}
	};
	private void Init() {
		bangDing_bt=(Button) findViewById(R.id.bangDing_bt);
		bangDing_bt.setOnClickListener(listener);
		
		erweima_bt=(Button) findViewById(R.id.erweima_bt);
		erweima_bt.setOnClickListener(listener);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		NoteTitle notetile = new NoteTitle();
		setting=new PreferenceSetting(ZhiNengShouBiaoActivity.this,PreferenceSetting.FILE_NAME_BLUETOOTH);
		Map<String, Object> inputMap = new HashMap<String, Object>();
		DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------onActivityResult");
		if (resultCode == RESULT_OK) {
			Json bluetooth_Json = new Json();
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			Toast.makeText(ZhiNengShouBiaoActivity.this, result,
					Toast.LENGTH_SHORT).show();
			DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------onActivityResult"+result);
			try {
				notetile = bluetooth_Json.Json_unpacage(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null!=notetile.getBluetoolthAddress()&&null!=notetile.getBluetoolthName())
			{inputMap.put(SettingKey.bluetoolthAddress,
					notetile.getBluetoolthAddress());
			DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------onActivityResult"+notetile.getBluetoolthAddress());
			inputMap.put(SettingKey.bluetoolthName,
					notetile.getBluetoolthName());
			DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------onActivityResult"+notetile.getMY_UUID());
			inputMap.put(SettingKey.uuid, notetile.getMY_UUID());
			DebugUtils.MyLogD("----d-------ZhiNengShouBiaoActivity ------onActivityResult"+true);
			inputMap.put(SettingKey.isBluetoothMark, true);
			setting.saveAllSetting(inputMap);
			}
			finishActivity(requestCode);
		}
	}
}
