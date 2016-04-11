package com.example.watch.home;

import com.example.watch.MainActivity;
import com.example.watch.R;
import com.example.watch.fragment.CallFragment;
import com.example.watch.fragment.DeviceManagerActivity;
import com.example.watch.health.CommonActivity;
import com.example.watch.health.HealthMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2016-2-23下午02:09:42
 */
public class HomeActivity extends Activity implements OnClickListener {

	private MyGridView mGridView;

	private ImageView backView;
	private TextView titleView;

	public String[] img_text = { "健康监测", "定位", "添加设备", "预约挂号", "咨询医生", "论坛交流",
			"商城", "通话", "联系人", "历史轨迹", "设置", "更多" };
	public int[] img_icon = { R.drawable.healthmanager_icon,
			R.drawable.map_location, R.drawable.image_add, R.drawable.guahao,
			R.drawable.zixun, R.drawable.luntan, R.drawable.shangchen,
			R.drawable.call_icon, R.drawable.user,
			R.drawable.historical_location_icon, R.drawable.settings_icon,
			R.drawable.more_icon };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity_main);

		initView();

	}

	public void initView() {

		mGridView = (MyGridView) findViewById(R.id.gridview);
		// mGridView.setAdapter(new MyGridAdapter(this));
		mGridView.setAdapter(new MyGridAdapter(this,img_text,img_icon));
		mGridView.setOnItemClickListener(new MyItemClickListener());

		backView = (ImageView) findViewById(R.id.title_back);
		backView.setOnClickListener(this);
		backView.setVisibility(View.GONE);
		titleView = (TextView) findViewById(R.id.title_text);
		titleView.setText(getString(R.string.home_page));
	}

	private class MyItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
//			Toast.makeText(HomeActivity.this, "" + arg2, Toast.LENGTH_SHORT)
//					.show();
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			switch (arg2) {
			case 2:
				intent.setClass(HomeActivity.this, DeviceManagerActivity.class);
				startActivity(intent);
				break;
			case 7:
				intent.setClass(HomeActivity.this, CallFragment.class);
				startActivity(intent);
				break;
			case 1:
				intent.setClass(HomeActivity.this, MainActivity.class);
				startActivity(intent);
				break;
			case 0:
				intent.setClass(HomeActivity.this, CommonActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			// finish();
			break;
		default:
			break;
		}
	}

	private long firstTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, getString(R.string.exit_dialog), 666)
						.show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else {// 两次按键小于2秒时，退出应用
				System.exit(0);
				System.gc();
				return false;
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
