package com.example.watch.home;

import com.example.watch.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com>
 * 2016-2-23下午02:43:13	
 */
public class MyGridAdapter extends BaseAdapter{
	
	private Context mContext;
	
	public String[] img_text={"健康监测","定位","添加设备",
								"预约挂号","咨询医生","论坛交流",
								"商城",
								"通话","联系人","历史轨迹",
								"设置","更多"};
	public int[] img_icon={R.drawable.healthmanager_icon,R.drawable.map_location,R.drawable.image_add,
			R.drawable.guahao,R.drawable.zixun,R.drawable.luntan,
			R.drawable.shangchen,
			R.drawable.call_icon,R.drawable.user,R.drawable.historical_location_icon,
			R.drawable.settings_icon,R.drawable.more_icon};
	

	
	public MyGridAdapter(Context context){
		super();
		this.mContext=context;
	}
	
	public MyGridAdapter(Context context,String[] img_text,int[] img_icon){
		super();
		this.mContext=context;
		this.img_icon=img_icon;
		this.img_text=img_text;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null){
			
			convertView=LayoutInflater.from(mContext).inflate(R.layout.home_grid_item, parent,false);
		}
		
		TextView tv=BaseViewHolder.get(convertView,R.id.tv_item);
		ImageView iv=BaseViewHolder.get(convertView,R.id.iv_item);
		
		iv.setBackgroundResource(img_icon[position]);
		tv.setText(img_text[position]);
		return convertView;
	}
	
	

}
