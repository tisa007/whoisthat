package com.tisa7.android.whoisthat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends ActionBarActivity {

	public static final String PREF = "pref";
	public static final String IS_BLUETOOTH_ONLY = "is_bluetooth_only";
	public static final String IS_TURN_ON = "is_turn_on";
	
	private ApkInstaller mInstaller;
	private Switch mSwAll;
	private CheckBox mCbBluetooth;

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSwAll = (Switch) findViewById(R.id.sw_all);
		mCbBluetooth = (CheckBox)findViewById(R.id.ck_bluetooth);
		
		findViewById(R.id.ll_feedback).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendFeedback();
			}
		});
		
		findViewById(R.id.ll_help).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openHelp();
			}
		});
		
		SharedPreferences sp = getSharedPreferences(PREF,
				Context.MODE_PRIVATE);
		
		mSwAll.setChecked(sp.getBoolean(IS_TURN_ON, true));
		mSwAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				savePref(IS_TURN_ON, isChecked);
			}
		});
		
		mCbBluetooth.setChecked(sp.getBoolean(IS_BLUETOOTH_ONLY, false));
		mCbBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				savePref(IS_BLUETOOTH_ONLY, isChecked);
			}
		});

		mInstaller = new ApkInstaller(this);
		
		UmengUpdateAgent.update(this);
	}
	
	private void sendFeedback() {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	            "mailto","wp.tisa@gmail.com", null));
		intent.putExtra(Intent.EXTRA_SUBJECT, "意见反馈 爪爪报号");

		startActivity(Intent.createChooser(intent, "发送邮件…"));
	}
	
	private void openHelp() {
		startActivity(new Intent(this, HelpActivity.class));
	}

	private void savePref(String key, boolean value) {
		Editor er = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
		er.putBoolean(key, value).commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (!SpeechUtility.getUtility().checkServiceInstalled()) {
			mInstaller.install();
		} else {
			
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
