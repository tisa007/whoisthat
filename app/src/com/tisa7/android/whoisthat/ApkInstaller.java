package com.tisa7.android.whoisthat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.iflytek.cloud.SpeechUtility;


/**
 * 弹出提示框，下载服务组件
 */
public class ApkInstaller {
	private Activity mActivity ;
	
	public ApkInstaller(Activity activity) {
		mActivity = activity;
	}

	@SuppressWarnings("deprecation")
	public void install(){
		final Dialog dialog=new Dialog(mActivity);
		LayoutInflater inflater = mActivity.getLayoutInflater();
		View alertDialogView = inflater.inflate(R.layout.dialog, null);
		dialog.setContentView(alertDialogView);
		dialog.setTitle("爪爪提示");
		Button okButton = (Button) alertDialogView.findViewById(R.id.ok);
		Button cancelButton = (Button) alertDialogView.findViewById(R.id.cancel);
		//确认
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String url = SpeechUtility.getUtility().getComponentUrl();
				String assetsApk="SpeechService.apk";
				processInstall(mActivity, url,assetsApk);
			}
		});
		//取消
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();			
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //设置宽度
		dialog.getWindow().setAttributes(lp);
		return;
	}
	/**
	 * 如果服务组件没有安装打开语音服务组件下载页面，进行下载后安装。
	 */
	private boolean processInstall(Context context ,String url,String assetsApk){
		//直接下载方式
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
		return true;		
	}
}
