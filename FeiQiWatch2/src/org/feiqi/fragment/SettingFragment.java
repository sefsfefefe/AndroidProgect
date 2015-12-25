package org.feiqi.fragment;

import org.feiqi.activity.MainActivity;
import org.feiqi.activity.zhinengshoubiao.ZhiNengShouBiaoActivity;
import org.feiqi.constant.Constant;

import com.android.test.login.R;
import com.feiqi.debug.DebugUtils;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;

public class SettingFragment extends BaseFragment {

	private Button ZhiNengShouBiao_bt;
	
	private View settingLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		settingLayout = inflater.inflate(R.layout.setting_layout,
				container, false);
		
		Init(settingLayout);
		return settingLayout;
	}
	
	OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ZhiNengShouBiao_bt:
				Intent intent = new Intent();
				intent.setClass(getActivity(), ZhiNengShouBiaoActivity.class);
				startActivity(intent);
				break;
			
			default:
				break;
			}
			
		}
	};
	private void Init(View view) {
		DebugUtils.MyLogD("----d-------Init(View view)");
		ZhiNengShouBiao_bt=(Button) view.findViewById(R.id.ZhiNengShouBiao_bt);
		ZhiNengShouBiao_bt.setOnClickListener(listener);
	}


	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		MainActivity.currFragTag = Constant.FRAGMENT_FLAG_SETTING;
		
	}
	

}
