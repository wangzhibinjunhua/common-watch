package com.example.watch.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.watch.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2015-12-14下午04:04:06
 */
public class CallFragment extends Activity implements OnClickListener {

	private ImageView backView;
	private TextView titleView;

	private DeviceAdapter adpAdapter = null;
	/**
	 * ListView列表
	 */
	private ListView lvListView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frag_call);
		initView();
	}

	private void initView() {
		backView = (ImageView) findViewById(R.id.title_back);
		backView.setOnClickListener(this);
		titleView = (TextView) findViewById(R.id.title_text);
		titleView.setText(getString(R.string.call));

		lvListView = (ListView) findViewById(R.id.lvListView);
		// lvListView.setOnItemClickListener(this);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
	
	private void initData() {
		List<DeviceBean> deviceDatas = new ArrayList<DeviceBean>();
		//获取xml存储数据
		SharedPreferences sp=getSharedPreferences("DEVICE_INFO", Activity.MODE_PRIVATE);
		//SharedPreferences.Editor editor=sp.edit();
		for(int i=0;i<5;i++){
			String str=sp.getString("user_"+String.valueOf(i), "000,000,000");
			Log.d("wzb","str="+str);
			if(!str.equals("000,000,000")){
				String tokens[] = str.trim().split("\\,");
				Log.d("wzb","len="+tokens.length+" tokens="+tokens);
				deviceDatas.add(new DeviceBean(tokens[0],tokens[1],tokens[2],true));
			}
		}

		adpAdapter = new DeviceAdapter(this, deviceDatas,2);

		lvListView.setAdapter(adpAdapter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
