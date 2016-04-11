package com.example.watch.health;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.watch.MainActivity;
import com.example.watch.R;
import com.example.watch.ble.ScanningActivity;
import com.example.watch.fragment.CallFragment;
import com.example.watch.fragment.DeviceManagerActivity;
import com.example.watch.home.HomeActivity;
import com.example.watch.home.MyGridAdapter;
import com.example.watch.home.MyGridView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com>
 * 2016-3-7ÉÏÎç11:05:05	
 */

public class CommonActivity extends Activity implements OnClickListener {

	private MyGridView mGridView;

	private ImageView backView;
	private TextView titleView;

	public String[] img_text = { "½¡¿µÊÖ±í", "ÑªÑ¹¼Æ","ÑªÌÇÒÇ","ÑªÖ¬ÒÇ"};
	public int[] img_icon = { R.drawable.bp_watch,
			R.drawable.eq_hearrate_icon,R.drawable.eq_bloodgslsucose,R.drawable.eq_bloodlipid};

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
		titleView = (TextView) findViewById(R.id.title_text);
		titleView.setText("½¡¿µ²âÁ¿");
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
			case 0:
				intent.setClass(CommonActivity.this, HealthMainActivity.class);
				startActivity(intent);
				break;
			case 1:
				intent.setClass(CommonActivity.this, ScanningActivity.class);
				startActivity(intent);
				break;
			case 2:
				intent.setClass(CommonActivity.this, ScanningActivity.class);
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
			finish();
			break;
		default:
			break;
		}
	}
}

