package com.example.watch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.example.watch.MainActivity;
/**
 * AUTHER wzb<wangzhibin_x@foxmail.com>
 * 2015-12-15ÏÂÎç03:53:11	
 */
public class WelcomActivity extends Activity{
	
	private static final int SPLASH_TIME = 2 * 1000;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2016:
				goHome();
				break;
				default:
					break;
			}
		};
	};
	
	private void toggleSpeaker(Context context){
    	AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    	am.setMode(AudioManager.MODE_IN_CALL);
    	am.setSpeakerphoneOn(!am.isSpeakerphoneOn());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		mHandler.sendEmptyMessageDelayed(2016, SPLASH_TIME);
	}
	
	private void goHome() {
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}

}
