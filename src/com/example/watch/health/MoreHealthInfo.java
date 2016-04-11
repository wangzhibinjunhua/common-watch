package com.example.watch.health;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.watch.R;
import com.example.watch.util.HttpManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2016-2-25上午11:19:42
 */
public class MoreHealthInfo extends Activity implements OnClickListener
		/*OnItemClickListener */{
	private ListView lvListView = null;

	private ImageView backView;
	private TextView titleView;

	private HealthInfoAdapter adpAdapter = null;

	public String[] availableInfo = { "null", "null", "null", "null", "null" };

	List<HealthInfoBean> infoBeans = new ArrayList<HealthInfoBean>();

	Thread mThread;
	private HttpManager hm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_main_activity);
		hm = new HttpManager(this);
		initView();

	}

	private void initView() {

		backView = (ImageView) findViewById(R.id.title_back);
		backView.setOnClickListener(this);
		titleView = (TextView) findViewById(R.id.title_text);
		titleView.setText("历史记录");

		lvListView = (ListView) findViewById(R.id.lvListView);
		//lvListView.setOnItemClickListener(this);

	}

	/**
	 * 当ListView 子项点击的时候
	 */
//	@Override
//	public void onItemClick(AdapterView<?> listView, View itemLayout,
//			int position, long id) {
//		Toast.makeText(this, "" + position, 2000).show();
//		adpAdapter.notifyDataSetChanged();
//
//	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		adpAdapter.removeAll();

		new Thread(getInfoThread).start();
	}

	/**
	 * 初始化视图
	 */
	private void initData() {

		adpAdapter = new HealthInfoAdapter(MoreHealthInfo.this, infoBeans);

		lvListView.setAdapter(adpAdapter);
		adpAdapter.notifyDataSetChanged();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0000:
				Log.d("wzb", "00000000000");
				adpAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	private Thread getInfoThread = new Thread() {

		public void run() {
			try {
				// List<HealthInfoBean> infoBeans = new
				// ArrayList<HealthInfoBean>();
				// 获取xml存储数据
				SharedPreferences sp = getSharedPreferences("DEVICE_INFO",
						Activity.MODE_PRIVATE);
				for (int i = 0; i < 1; i++) {
					String str = sp.getString("user_" + String.valueOf(i),
							"000,000,000");
					Log.d("wzb", "str=" + str);
					if (!str.equals("000,000,000")) {
						String tokens[] = str.trim().split("\\,");
						String data[] = { "1", "1", "1", "1" };// imei
																// //bp//bpm//time
						String getResult = hm.getHttpData(tokens[2], "1", 101);
						if (!getResult.equals("0xffffffff")) {
							JSONArray jArray = new JSONArray(getResult);
							JSONObject jsonObject = null;
							for (int j = 0; j < getResult.length(); j++) {
								jsonObject = jArray.getJSONObject(j);
								data[0] = jsonObject.getString("imei");
								data[1] = jsonObject.getString("bp");
								data[2] = jsonObject.getString("bpm");
								data[3] = jsonObject.getString("time");
								
								Log.d("wzb", "data:" + data[1] + "," + data[2] + ","
										+ data[3]);
								if (!data[2].equals("1")) {
									infoBeans.add(new HealthInfoBean(tokens[0],
											tokens[1], tokens[2], data[1], data[2],
											data[3]));
								} else {
									infoBeans.add(new HealthInfoBean(tokens[0],
											tokens[1], tokens[2], "1", "1", "1"));
								}
							}
						}
						else{
							infoBeans.add(new HealthInfoBean(tokens[0],
									tokens[1], tokens[2], "1", "1", "1"));
						}
						// for (int j = 0; j < jArray.length(); j++) {
						// jsonObject = jArray.getJSONObject(j);
						// data[0] = jsonObject.getString("imei");
						// data[1] = jsonObject.getString("bp");
						// data[2] = jsonObject.getString("bpm");
						// data[3] = jsonObject.getString("time");
						//
						// }
//						Log.d("wzb", "data:" + data[1] + "," + data[2] + ","
//								+ data[3]);
//						if (!data[2].equals("1")) {
//							infoBeans.add(new HealthInfoBean(tokens[0],
//									tokens[1], tokens[2], data[1], data[2],
//									data[3]));
//						} else {
//							infoBeans.add(new HealthInfoBean(tokens[0],
//									tokens[1], tokens[2], "1", "1", "1"));
//						}
					}

				}

			} catch (Exception e) {
				Log.d("wzb", "thread err");
				e.printStackTrace();
			}
			mHandler.sendEmptyMessage(0000);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backView) {
			finish();

		}
	}

}
