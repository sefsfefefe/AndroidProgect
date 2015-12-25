package com.feiqi.show;

import com.android.test.login.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ShowUtils {

	
	//显示简单的toast，并且带位置
	public static Toast showSimpleToast(Context context, String content){
		Toast t = Toast.makeText(context, content, 1000);
		t.setGravity(Gravity.CENTER, 0, 0);	
		t.show();	
		return t;
	}
	
	
	//显示复杂的toast, 参数2是xml文件id
	public static void showCustomToast(Context context, int layoutResId){
		//向系统申请初始化xml文件的服务的功能
		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		//context是从LoginActivity传递过来, context是没有findViewById()
		//通过强制类型装换，就可以用findViewById()
				// findViewById()返回的是一个View
				//View v = ((LoginActivity)context).findViewById(R.id.layout_templete);
				//infate（）直接返回的是一个容器view
		View layout = inflater.inflate(layoutResId, null);
		//通过layout对象可以获取到imageview和textview
		ImageView imageView = (ImageView) layout.findViewById(R.id.image_head);
		imageView.setImageResource(R.drawable.head2);
		TextView tv = (TextView) layout.findViewById(R.id.tv_content);
		tv.setText("这个是一个定制化的toast");
		
		//通过toast的方式将xml文件中所有的view显示处理
		Toast t = new Toast(context);
		t.setDuration(Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, -10, 10);
		t.setView(layout);
		t.show();
	}
	
	
	//显示dailog
	
	public static void showExitAlertDialog(Context context,String message,  String postiveMsg, String negativeMsg){
		
		AlertDialog.Builder builer = new AlertDialog.Builder(context);
		builer.setIcon(R.drawable.head2).setTitle("退出对话框");
		builer.setMessage(message);
		builer.setPositiveButton(postiveMsg, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		builer.setNegativeButton(negativeMsg, null);
		builer.create().show();
		
	}
}
