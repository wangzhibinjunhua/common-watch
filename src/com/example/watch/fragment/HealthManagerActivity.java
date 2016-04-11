package com.example.watch.fragment;

import com.example.watch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com>
 * 2015-12-18ÉÏÎç11:15:06	
 */
public class HealthManagerActivity extends Activity implements OnClickListener{
	
	private ImageView backView;
	private TextView titleView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frag_healthmanager);
		initView();
	}
	
	
	private void initView(){
		backView=(ImageView)findViewById(R.id.title_back);
		backView.setOnClickListener(this);
		titleView=(TextView)findViewById(R.id.title_text);
		titleView.setText(getString(R.string.healthmanager));
		
	}
		
	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
	}

}
