package com.example.watch;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2015-12-14下午07:37:45
 */
public class MyApplication extends Application {

	public static Context mContext;
	public BMapManager mBMapManager = null;

	private static MyApplication mInstance;

	public static MyApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this.getApplicationContext();
		SDKInitializer.initialize(getApplicationContext());
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(mContext);
		}
		if (!mBMapManager.init(new MyGeneralListener())) {
			Toast.makeText(
					MyApplication.getInstance().getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
//				// 授权Key错误：
//				Toast.makeText(
//						BaseApplication.getInstance().getApplicationContext(),
//						"请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: "
//								+ iError, Toast.LENGTH_LONG).show();
			} else {
//				Toast.makeText(
//						BaseApplication.getInstance().getApplicationContext(),
//						"key认证成功", Toast.LENGTH_LONG).show();
			}
		}
	}

}
