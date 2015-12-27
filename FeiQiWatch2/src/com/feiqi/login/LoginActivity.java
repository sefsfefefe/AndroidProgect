package com.feiqi.login;





import java.util.HashMap;
import java.util.Map;

import org.feiqi.activity.MainActivity;

import com.android.test.login.R;
import com.feiqi.debug.DebugUtils;
import com.feiqi.setting.PreferenceSetting;
import com.feiqi.setting.SettingKey;
import com.feiqi.show.ShowUtils;
import com.feiqi.string.StringValues;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
   
	
	private Button login_btn_login;
	private ImageButton ImageButton02;
	private EditText login_edit_account;
	private EditText login_edit_pwd;
	
	private CheckBox login_cb_savepwd;//保存密码
	private CheckBox login_cb_autoLogin;//自动登入
	private CheckBox login_cb_cogradient;//同步手机信息
	private CheckBox login_cb_openvibra;//防丢查找
	private CheckBox login_cb_Cloud;//连接云端
	
	private PreferenceSetting setting = null;
	OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn_login:
				DebugUtils.MyLogD("----d-------login_btn_login 点击");
				String passwd=login_edit_pwd.getText().toString().trim();
				String username=login_edit_account.getText().toString().trim();
				if(passwd.isEmpty()||username.isEmpty()){
					Toast.makeText(LoginActivity.this,StringValues.hintString_1 , Toast.LENGTH_SHORT).show();
				}else{
					if(passwd.equals("123456")&&username.equals("root")){
						DebugUtils.MyLogD("----d-------showCustomToast");
						//Toast.makeText(LoginActivity.this,StringValues.hintString_2 , Toast.LENGTH_SHORT).show();
						ShowUtils.showCustomToast(LoginActivity.this, R.layout.toast_show);
						DebugUtils.MyLogD("----d-------setting != null");
						//保存
						if(setting != null){
							Map<String, Object> inputMap = new HashMap<String, Object>();
							//写要用editor
							if(login_cb_savepwd.isChecked()){
								inputMap.put(SettingKey.userName, username);
								inputMap.put(SettingKey.passwd, passwd);
							}else{
								inputMap.put(SettingKey.userName, "");
								inputMap.put(SettingKey.passwd, "");
							}
							inputMap.put(SettingKey.isAuto, login_cb_autoLogin.isChecked());
							inputMap.put(SettingKey.isMark, login_cb_savepwd.isChecked());
							inputMap.put(SettingKey.isLost, login_cb_openvibra.isChecked());
							setting.saveAllSetting(inputMap);
						}
						DebugUtils.MyLogD("----d-------Intent");
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						startActivity(intent);
					}else{
						ShowUtils.showSimpleToast(LoginActivity.this, StringValues.hintString_3);
					}
				}
				
				break;
			default:
				break;
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DebugUtils.MyLogD("----d-------onCreate");
    	Log.d("feiqiDemo", "----d-------onCreate");
        super.onCreate(savedInstanceState);
        DebugUtils.MyLogD("----d-------onCreate");
       setContentView(R.layout.loginpage_activity);
       
       initUI();
       
       setting=new PreferenceSetting(LoginActivity.this, PreferenceSetting.FILE_NAME);
      
       //读取配置文件
       Map<String, ?> allPairs = setting.getSetting();
     
       //如果有xml文件，并且里面的内容不为空
       if(allPairs != null && !allPairs.isEmpty()){
    	  
	   boolean isMark = (Boolean) allPairs.get(SettingKey.isMark);
    	   if(isMark){
    		   String userName=(String) allPairs.get(SettingKey.userName);
    		   String passwd=(String) allPairs.get(SettingKey.passwd);
    		   Boolean isAuto = (Boolean) allPairs.get(SettingKey.isAuto);
    		   
    		   login_edit_account.setText(userName);
    		   login_edit_pwd.setText(passwd);
    		   login_cb_savepwd.setChecked(isMark);
    		   login_cb_autoLogin.setChecked(isAuto);
    	   }
       }
    }


	private void initUI() {
		login_btn_login=(Button) findViewById(R.id.login_btn_login);
		login_btn_login.setOnClickListener(listener);
		ImageButton02=(ImageButton) findViewById(R.id.ImageButton02);
		ImageButton02.setOnClickListener(listener);
		
		login_edit_account=(EditText) findViewById(R.id.login_edit_account);
		login_edit_pwd=(EditText) findViewById(R.id.login_edit_pwd);
		
		login_cb_savepwd=(CheckBox) findViewById(R.id.login_cb_savepwd);
		login_cb_autoLogin=(CheckBox) findViewById(R.id.login_cb_autoLogin);
		login_cb_cogradient=(CheckBox) findViewById(R.id.login_cb_cogradient);
		login_cb_openvibra=(CheckBox) findViewById(R.id.login_cb_openvibra);
		login_cb_Cloud=(CheckBox) findViewById(R.id.login_cb_Cloud);
	}
}