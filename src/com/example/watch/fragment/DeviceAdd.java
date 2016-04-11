package com.example.watch.fragment;

import com.example.watch.R;
import com.example.watch.R.string;

import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com>
 * 2015-12-22ÏÂÎç07:40:36	
 */
public class DeviceAdd extends Activity implements OnClickListener{
	
	private ImageView backView;
	private TextView titleView;
	
	private EditText nameEditText;
	private EditText phoneEditText;
	private EditText imeiEditText;
	
	private String deviceName="";
	private String deviceIMEI="";
	private String devicePhoneNumber="";
	
	private Button btnCancel;
	private Button btnOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_deviceadd);
		initView();
	}
	
	void initView(){
		backView=(ImageView)findViewById(R.id.title_back);
		backView.setOnClickListener(this);
		titleView=(TextView)findViewById(R.id.title_text);
		titleView.setText(getString(R.string.device_info));
		titleView.setMovementMethod(LinkMovementMethod.getInstance());
		nameEditText=(EditText)findViewById(R.id.name_et);
		phoneEditText=(EditText)findViewById(R.id.phonenumber_et);
		imeiEditText=(EditText)findViewById(R.id.imei_et);
		
		btnCancel=(Button)findViewById(R.id.device_cancel);
		btnCancel.setOnClickListener(this);
		btnOk=(Button)findViewById(R.id.device_ok);
		btnOk.setOnClickListener(this);
		
	}
	
	void saveDeviceInfo(String name,String phoneNumber,String imei){
		String deviceinfo=name+","+phoneNumber+","+imei;
		SharedPreferences sp=getSharedPreferences("DEVICE_INFO", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		int i=4;
		while(i>0){
			editor.putString("user_"+String.valueOf(i),sp.getString("user_"+String.valueOf(i-1), "000,000,000") );
			i--;
		}
		editor.putString("user_"+String.valueOf(0), deviceinfo);
		editor.commit();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.title_back:
			finish();
			break;
		case R.id.device_cancel:
			nameEditText.setText("");
			imeiEditText.setText("");
			phoneEditText.setText("");
			break;
		case R.id.device_ok:
			deviceIMEI=imeiEditText.getText().toString();
			deviceName=nameEditText.getText().toString();
			devicePhoneNumber=phoneEditText.getText().toString();
			saveDeviceInfo(deviceName,devicePhoneNumber,deviceIMEI);
			finish();
			break;
		default:
			break;
		}
		
	}

}
