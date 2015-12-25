package com.feiqi.login;

import java.lang.ref.WeakReference;

import com.android.test.login.R;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

/*
 * 闪屏页作用：
 * 1.提供欢迎界面
 * 2.初始化工作
 * */
public class SplashActivity extends Activity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				starMainActivity();
			}
		}, 3000);
    }
    private void starMainActivity(){
    	Intent intent=new Intent(this,LoginActivity.class);
    	startActivity(intent);
    }
    private Handler handler = new Handler();
//    private final MyHandler handler=new MyHandler(this);
//    private static class MyHandler extends Handler{
//    	private WeakReference<SplashActivity> weakReference;
//    	public MyHandler(SplashActivity activity){
//    		weakReference=new WeakReference<SplashActivity>(activity);
//    	}
//    	@Override
//    	public void handleMessage(Message msg) {
//    		super.handleMessage(msg);
//    	}
//    }
}
