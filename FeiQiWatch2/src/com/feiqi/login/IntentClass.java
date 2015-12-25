package com.feiqi.login;

import android.content.Context;
import android.content.Intent;

public class IntentClass {

	public IntentClass(Context context, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	}

}
