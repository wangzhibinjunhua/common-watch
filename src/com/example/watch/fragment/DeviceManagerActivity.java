package com.example.watch.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.watch.R;
import com.example.watch.fragment.DeviceAdapter.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2015-12-21下午02:18:56
 */
public class DeviceManagerActivity extends Activity implements OnClickListener,
		OnItemClickListener {


	private Button btnAdd = null;
	private Button btnDelete = null;

	/**
	 * ListView列表
	 */
	private ListView lvListView = null;
	
	private ImageView backView;
	private TextView titleView;

	/**
	 * 适配对象
	 */
	private DeviceAdapter adpAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_devicemanager);

		initView();

	

	}

	/**
	 * 初始化控件
	 */
	private void initView() {


		 btnAdd = (Button) findViewById(R.id.btnAdd);
		 btnAdd.setOnClickListener(this);
		
		backView=(ImageView)findViewById(R.id.title_back);
		backView.setOnClickListener(this);
		titleView=(TextView)findViewById(R.id.title_text);
		titleView.setText(getString(R.string.profile_name));

		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);

		lvListView = (ListView) findViewById(R.id.lvListView);
		lvListView.setOnItemClickListener(this);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	/**
	 * 初始化视图
	 */
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
	

		adpAdapter = new DeviceAdapter(this, deviceDatas,1);

		lvListView.setAdapter(adpAdapter);

	}

	@Override
	public void onClick(View v) {

		if (v == backView) {
			finish();

		}

		if (v == btnAdd) {

			//adpAdapter.add(new DeviceBean("赵六", true));

			//adpAdapter.notifyDataSetChanged();
			startActivity(new Intent(DeviceManagerActivity.this,DeviceAdd.class));
		}

		/*
		 * 当点击删除的时候
		 */
		if (v == btnDelete) {

			/*
			 * 删除算法最复杂,拿到checkBox选择寄存map
			 */
			Map<Integer, Boolean> map = adpAdapter.getCheckMap();

			// 获取当前的数据数量
			int count = adpAdapter.getCount();
			
			int delete_positon=4;
			// 进行遍历
			for (int i = 0; i < count; i++) {

				// 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
				int position = i - (count - adpAdapter.getCount());

				if (map.get(i) != null && map.get(i)) {

					DeviceBean bean = (DeviceBean) adpAdapter.getItem(position);

					if (bean.isCanRemove()) {
						adpAdapter.getCheckMap().remove(i);
						adpAdapter.remove(position);
						delete_positon=position;
					} else {
						map.put(position, false);
					}

				}
			}
			//del xml data
			SharedPreferences sp=getSharedPreferences("DEVICE_INFO", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor=sp.edit();
			while(delete_positon<(count-1)){
				editor.putString("user_"+String.valueOf(delete_positon),sp.getString("user_"+String.valueOf(delete_positon+1), "000,000,000") );
				delete_positon++;
			}
			editor.putString("user_"+String.valueOf(count-1), "000,000,000");
			editor.commit();
			
			//end

			adpAdapter.notifyDataSetChanged();

		}

//		/*
//		 * 当点击全选的时候
//		 */
//		if (v == btnSelectAll) {
//
//			if (btnSelectAll.getText().toString().trim().equals("全选")) {
//
//				// 所有项目全部选中
//				adpAdapter.configCheckMap(true);
//
//				adpAdapter.notifyDataSetChanged();
//
//				btnSelectAll.setText("全不选");
//			} else {
//
//				// 所有项目全部不选中
//				adpAdapter.configCheckMap(false);
//
//				adpAdapter.notifyDataSetChanged();
//
//				btnSelectAll.setText("全选");
//			}
//
//		}
	}

	/**
	 * 当ListView 子项点击的时候
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View itemLayout,
			int position, long id) {

		if (itemLayout.getTag() instanceof ViewHolder) {

			ViewHolder holder = (ViewHolder) itemLayout.getTag();

			// 会自动出发CheckBox的checked事件
			//单选
			adpAdapter.configCheckMap(false);
			adpAdapter.notifyDataSetChanged();
			holder.cbCheck.toggle();

		}

	}

}
