package com.example.watch.health;

import java.util.List;

import com.example.watch.R;
import com.example.watch.fragment.DeviceBean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2016-2-24ÉÏÎç10:38:58
 */
public class HealthInfoAdapter extends BaseAdapter {

	private Context mContext;

	private List<HealthInfoBean> datas;

	public HealthInfoAdapter(Context context, List<HealthInfoBean> hib) {
		this.mContext = context;
		this.datas = hib;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		Log.d("wzb", "xxx=" + datas.size());
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewGroup layout = null;
		if (convertView == null) {
			layout = (ViewGroup) LayoutInflater.from(mContext).inflate(
					R.layout.health_listview_item_layout, parent,false);
		} else {
			layout = (ViewGroup) convertView;
		}
		HealthInfoBean bean = datas.get(position);

		TextView tv = (TextView) layout.findViewById(R.id.health_item_info);
		tv.setText(bean.getHealthInfo());

		return layout;
	}
	
	public void add(HealthInfoBean bean) {
		this.datas.add(0, bean);

	}

	public void remove(int position) {
		this.datas.remove(position);
	}
	
	public void removeAll(){
		this.datas.removeAll(datas);
	}

}
