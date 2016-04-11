package com.example.watch.fragment;

import com.example.watch.R;
import com.example.watch.datashow.DataActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com>
 * 2015-12-14ÏÂÎç03:16:33	
 */
public class LeftFragment extends Fragment implements OnClickListener{
	
	//private View locationView;
	private View callView;
	private View recordView;
	private View historicalLocationView;
	private View bleView;
	private View settingsView;
	private View moreView;
	private View healthManagerView;
	
	private TextView deviceTextView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		return view;
	}
	
	public void findViews(View view){
		callView=view.findViewById(R.id.callView);
		recordView=view.findViewById(R.id.recordView);
		historicalLocationView=view.findViewById(R.id.historicalLocationView);
		bleView=view.findViewById(R.id.bleView);
		settingsView=view.findViewById(R.id.settingsView);
		moreView=view.findViewById(R.id.moreView);
		healthManagerView=view.findViewById(R.id.healthManagerView);
		deviceTextView=(TextView) view.findViewById(R.id.device_manager);
		
		deviceTextView.setOnClickListener(this);
		callView.setOnClickListener(this);
		recordView.setOnClickListener(this);
		historicalLocationView.setOnClickListener(this);
		bleView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
		moreView.setOnClickListener(this);
		healthManagerView.setOnClickListener(this);
		
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		switch (v.getId()) {
		case R.id.callView:
			intent.setClass(getActivity(), CallFragment.class);
			break;
		case R.id.recordView:
			intent.setClass(getActivity(), RecordFragment.class);
			break;
		case R.id.historicalLocationView:
			intent.setClass(getActivity(), HistoricalLocationFragment.class);
			break;
		case R.id.bleView:
			intent.setClass(getActivity(), BlelFragment.class);
			break;
		case R.id.settingsView:
			intent.setClass(getActivity(), SettingsFragment.class);
			break;
		case R.id.moreView:
			intent.setClass(getActivity(), MoreFragment.class);
			break;
		case R.id.healthManagerView:
			//intent.setClass(getActivity(), HealthManagerActivity.class);
			intent.setClass(getActivity(), DataActivity.class);
			break;
		case R.id.device_manager:
			intent.setClass(getActivity(), DeviceManagerActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	

}
