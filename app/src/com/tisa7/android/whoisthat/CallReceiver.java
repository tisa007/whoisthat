package com.tisa7.android.whoisthat;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;

public class CallReceiver extends AbsCallReceiver {

	private static final String TAG = "CallReceiver";

	@Override
	protected void onIncomingCallStarted(Context context, String number,
			Date start) {
		SharedPreferences sp = context.getSharedPreferences(MainActivity.PREF,
				Context.MODE_PRIVATE);
		// 如果未打开报号，忽略
		if (!sp.getBoolean(MainActivity.IS_TURN_ON, true)) {
			return;
		}
		// 如果仅蓝牙可用，则检测是否已连接蓝牙
		if (!sp.getBoolean(MainActivity.IS_BLUETOOTH_ONLY, true)) {
			AudioManager am = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (!am.isBluetoothA2dpOn()) {
				return;
			}
		}
		// 启动报号服务
		Intent itn = new Intent("com.tisa7.android.whoisthat.start_talking");
		itn.putExtra("number", number);
		context.startService(itn);
	}

	@Override
	protected void onIncomingCallEnded(Context context, String number,
			Date start, Date end) {
		stopTalking(context);
	}

	@Override
	protected void onOutgoingCallStarted(Context context, String number,
			Date start) {
		stopTalking(context);
	}

	@Override
	protected void onMissedCall(Context context, String number, Date start) {
		stopTalking(context);
	}

	private void stopTalking(Context context) {
		Intent itn = new Intent("com.tisa7.android.whoisthat.stop_talking");
		context.startService(itn);
	}

	@Override
	protected void onOutgoingCallEnded(Context context, String number,
			Date start, Date end) {
	}

}
