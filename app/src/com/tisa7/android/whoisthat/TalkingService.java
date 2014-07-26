package com.tisa7.android.whoisthat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class TalkingService extends IntentService {

	protected static final String TAG = "TalkingService";
	private SpeechSynthesizer mTTS;
	private AudioManager mAudioManager;

	public TalkingService() {
		super("TalkingService");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mTTS = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		// 设置参数
		mTTS.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		mTTS.setParameter(SpeechConstant.STREAM_TYPE, ""
				+ AudioManager.STREAM_MUSIC);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		if ("com.tisa7.android.whoisthat.start_talking".equals(action)) {
			String text = getContactByNumber(getApplicationContext(),
					intent.getStringExtra("number"))
					+ "，来电话了";
			text = text + "……" + text;
			say(text);
		} else if ("com.tisa7.android.whoisthat.stop_talking".equals(action)) {
			stop();
		}
	}

	private void say(String text) {
		setRingVolume(this, 0);
		setMusicVolume(this, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

		mTTS.startSpeaking(text, new SynthesizerListener() {

			@Override
			public void onSpeakResumed() {

			}

			@Override
			public void onSpeakProgress(int arg0, int arg1, int arg2) {

			}

			@Override
			public void onSpeakPaused() {

			}

			@Override
			public void onSpeakBegin() {

			}

			@Override
			public void onCompleted(SpeechError arg0) {
				setRingVolume(TalkingService.this, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
			}

			@Override
			public void onBufferProgress(int arg0, int arg1, int arg2,
					String arg3) {

			}
		});
	}

	private void stop() {
		mTTS.stopSpeaking();
	}

	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
		}
	};

	private void clearRingtone(Context context) {
		Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context,
				RingtoneManager.TYPE_RINGTONE);
		RingtoneManager.setActualDefaultRingtoneUri(context,
				RingtoneManager.TYPE_RINGTONE, null);
		Log.d(TAG, "onIncomingCallStarted set ringtone = null");
	}

	private void restoreRingtone(Context context, Uri uri) {
		RingtoneManager.setActualDefaultRingtoneUri(context,
				RingtoneManager.TYPE_RINGTONE, uri);
		Log.d(TAG, "onIncomingCallStarted set ringtone = " + uri.toString());
	}

	private void setRingVolume(Context context, int v) {
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_RING, v, 0);
	}

	private void setMusicVolume(Context context, int v) {
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, v, 0);
	}

	private String getContactByNumber(Context context, String number) {
		// define the columns I want the query to return
		String[] projection = new String[] {
				ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.PhoneLookup._ID };

		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));

		// query time
		Cursor cursor = context.getContentResolver().query(contactUri,
				projection, null, null, null);

		if (cursor.moveToFirst()) {
			// Get values from contacts database:
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
			return name;
		} else {
			return number;
		}
	}

}
