package com.tisa7.android.whoisthat;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

public class App extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		SpeechUtility.createUtility(this, "appid=53c767b6");
	}

}
