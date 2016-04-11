package com.example.watch.ble;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.example.watch.ble.BleWrapper.OnServiceDiscoverListener;

import com.example.watch.R;
import com.example.watch.ble.BleWrapper.OnDataAvailableListener;
import com.example.watch.util.CommunicationUtil;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PeripheralActivity extends Activity implements BleWrapperUiCallbacks {	
    public static final String EXTRAS_DEVICE_NAME    = "BLE_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "BLE_DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI    = "BLE_DEVICE_RSSI";

    
    public enum ListType {
    	GATT_SERVICES,
    	GATT_CHARACTERISTICS,
    	GATT_CHARACTERISTIC_DETAILS
    }
    
    private ListType mListType = ListType.GATT_SERVICES;
    private String mDeviceName;
    private String mDeviceAddress;
    private String mDeviceRSSI;

    private BleWrapper mBleWrapper;
    
    private TextView mDeviceNameView;
    private TextView mDeviceAddressView;
    private TextView mDeviceRssiView;
    private TextView mDeviceStatus;
    private ListView mListView;
    private View     mListViewHeader;
    private TextView mHeaderTitle;
    private TextView mHeaderBackButton;
    private ServicesListAdapter mServicesListAdapter = null;
    private CharacteristicsListAdapter mCharacteristicsListAdapter = null; 
    private CharacteristicDetailsAdapter mCharDetailsAdapter = null;  
    
    public void uiDeviceConnected(final BluetoothGatt gatt,
			                      final BluetoothDevice device)
    {
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mDeviceStatus.setText("connected");
				invalidateOptionsMenu();
			}
    	});
    }
    
    public void uiDeviceDisconnected(final BluetoothGatt gatt,
			                         final BluetoothDevice device)
    {
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mDeviceStatus.setText("disconnected");
				mServicesListAdapter.clearList();
				mCharacteristicsListAdapter.clearList();
				mCharDetailsAdapter.clearCharacteristic();
				
				invalidateOptionsMenu();
				
				mHeaderTitle.setText("");
				mHeaderBackButton.setVisibility(View.INVISIBLE);
				mListType = ListType.GATT_SERVICES;
				mListView.setAdapter(mServicesListAdapter);
			}
    	});    	
    }
    
    public void uiNewRssiAvailable(final BluetoothGatt gatt,
    							   final BluetoothDevice device,
    							   final int rssi)
    {
    	runOnUiThread(new Runnable() {
	    	@Override
			public void run() {
				mDeviceRSSI = rssi + " db";
				mDeviceRssiView.setText(mDeviceRSSI);
			}
		});    	
    }
    
    public void uiAvailableServices(final BluetoothGatt gatt,
    						        final BluetoothDevice device,
    							    final List<BluetoothGattService> services)
    {
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mServicesListAdapter.clearList();
				mListType = ListType.GATT_SERVICES;
				mListView.setAdapter(mServicesListAdapter);
				mHeaderTitle.setText(mDeviceName + "\'s services:");
				mHeaderBackButton.setVisibility(View.INVISIBLE);
				
    			for(BluetoothGattService service : mBleWrapper.getCachedServices()) {
            		mServicesListAdapter.addService(service);
            	}				
    			mServicesListAdapter.notifyDataSetChanged();
			}    		
    	});
    }
   
    public void uiCharacteristicForService(final BluetoothGatt gatt,
    				 					   final BluetoothDevice device,
    									   final BluetoothGattService service,
    									   final List<BluetoothGattCharacteristic> chars)
    {
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mCharacteristicsListAdapter.clearList();
		    	mListType = ListType.GATT_CHARACTERISTICS;
		    	mListView.setAdapter(mCharacteristicsListAdapter);
		    	mHeaderTitle.setText(BleNamesResolver.resolveServiceName(service.getUuid().toString().toLowerCase(Locale.getDefault())) + "\'s characteristics:");
		    	mHeaderBackButton.setVisibility(View.VISIBLE);
		    	
		    	for(BluetoothGattCharacteristic ch : chars) {
		    		mCharacteristicsListAdapter.addCharacteristic(ch);
		    	}
		    	mCharacteristicsListAdapter.notifyDataSetChanged();
			}
    	});
    }
    
    public void uiCharacteristicsDetails(final BluetoothGatt gatt,
					 					 final BluetoothDevice device,
										 final BluetoothGattService service,
										 final BluetoothGattCharacteristic characteristic)
    {
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mListType = ListType.GATT_CHARACTERISTIC_DETAILS;
				mListView.setAdapter(mCharDetailsAdapter);
		    	mHeaderTitle.setText(BleNamesResolver.resolveCharacteristicName(characteristic.getUuid().toString().toLowerCase(Locale.getDefault())) + "\'s details:");
		    	mHeaderBackButton.setVisibility(View.VISIBLE);
		    	
		    	mCharDetailsAdapter.setCharacteristic(characteristic);
		    	mCharDetailsAdapter.notifyDataSetChanged();
			}
    	});
    }

    public void uiNewValueForCharacteristic(final BluetoothGatt gatt,
											final BluetoothDevice device,
											final BluetoothGattService service,
											final BluetoothGattCharacteristic characteristic,
											final String strValue,
											final int intValue,
											final byte[] rawValue,
											final String timestamp)
    {
    	if(mCharDetailsAdapter == null || mCharDetailsAdapter.getCharacteristic(0) == null) return;
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mCharDetailsAdapter.newValueForCharacteristic(characteristic, strValue, intValue, rawValue, timestamp);
				mCharDetailsAdapter.notifyDataSetChanged();
			}
    	});
    }
 
	public void uiSuccessfulWrite(final BluetoothGatt gatt,
            					  final BluetoothDevice device,
            					  final BluetoothGattService service,
            					  final BluetoothGattCharacteristic ch,
            					  final String description)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Toast.makeText(getApplicationContext(), "Writing to " + description + " was finished successfully!", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void uiFailedWrite(final BluetoothGatt gatt,
							  final BluetoothDevice device,
							  final BluetoothGattService service,
							  final BluetoothGattCharacteristic ch,
							  final String description)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Toast.makeText(getApplicationContext(), "Writing to " + description + " FAILED!", Toast.LENGTH_LONG).show();
			}
		});	
	}

	
	public void uiGotNotification(final BluetoothGatt gatt,
								  final BluetoothDevice device,
								  final BluetoothGattService service,
								  final BluetoothGattCharacteristic ch)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// at this moment we only need to send this "signal" do characteristic's details view
				mCharDetailsAdapter.setNotificationEnabledForService(ch);
			}			
		});
	}

	@Override
	public void uiDeviceFound(BluetoothDevice device, int rssi, byte[] record) {
		// no need to handle that in this Activity (here, we are not scanning)
	}  	
	
    private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			--position; // we have header so we need to handle this by decrementing by one
			if(position < 0) { // user have clicked on the header - action: BACK
				if(mListType.equals(ListType.GATT_SERVICES)) return;
				if(mListType.equals(ListType.GATT_CHARACTERISTICS)) {
					uiAvailableServices(mBleWrapper.getGatt(), mBleWrapper.getDevice(), mBleWrapper.getCachedServices());
					mCharacteristicsListAdapter.clearList();
					return;
				}
				if(mListType.equals(ListType.GATT_CHARACTERISTIC_DETAILS)) {
					mBleWrapper.getCharacteristicsForService(mBleWrapper.getCachedService());
					mCharDetailsAdapter.clearCharacteristic();
					return;
				}
			}
			else { // user is going deeper into the tree (device -> services -> characteristics -> characteristic's details) 
				if(mListType.equals(ListType.GATT_SERVICES)) {
					BluetoothGattService service = mServicesListAdapter.getService(position);
					mBleWrapper.getCharacteristicsForService(service);
				}
				else if(mListType.equals(ListType.GATT_CHARACTERISTICS)) {
					BluetoothGattCharacteristic ch = mCharacteristicsListAdapter.getCharacteristic(position);
					uiCharacteristicsDetails(mBleWrapper.getGatt(), mBleWrapper.getDevice(), mBleWrapper.getCachedService(), ch);
				} 
			}
		}     	
	};  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peripheral);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mListViewHeader = (View) getLayoutInflater().inflate(R.layout.peripheral_list_services_header, null, false);
		
		connectViewsVariables();
		
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceRSSI = intent.getIntExtra(EXTRAS_DEVICE_RSSI, 0) + " db";
        mDeviceNameView.setText(mDeviceName);
        mDeviceAddressView.setText(mDeviceAddress);
        mDeviceRssiView.setText(mDeviceRSSI);
        getActionBar().setTitle(mDeviceName);
        
        //add by wzb 
        testButton=(Button)findViewById(R.id.bp_btn);
        testButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//setNoification(true);
				writeData();
			}
		});
        testButton.setText("测量 (设备准备中...)");
        testButton.setClickable(false);
        
        testTextView=(TextView)findViewById(R.id.bp_tv);
        
        mListView.addHeaderView(mListViewHeader);
        mListView.setOnItemClickListener(listClickListener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mBleWrapper == null) mBleWrapper = new BleWrapper(this, this);
		
		if(mBleWrapper.initialize() == false) {
			finish();
		}
		
		if(mServicesListAdapter == null) mServicesListAdapter = new ServicesListAdapter(this);
		if(mCharacteristicsListAdapter == null) mCharacteristicsListAdapter = new CharacteristicsListAdapter(this);
		if(mCharDetailsAdapter == null) mCharDetailsAdapter = new CharacteristicDetailsAdapter(this, mBleWrapper);
		
		mListView.setAdapter(mServicesListAdapter);
		mListType = ListType.GATT_SERVICES;
		mHeaderBackButton.setVisibility(View.INVISIBLE);
		mHeaderTitle.setText("");
		
		//add by wzb 
		mListView.setVisibility(View.GONE);
		
		// start automatically connecting to the device
    	mDeviceStatus.setText("connecting ...");
    	mBleWrapper.connect(mDeviceAddress);
    	
    	mBleWrapper.setOnDataAvailableListener(mOnDataAvailable);
    	mBleWrapper.setOnServiceDiscoverListener(mOnServiceDiscover);
    	
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mServicesListAdapter.clearList();
		mCharacteristicsListAdapter.clearList();
		mCharDetailsAdapter.clearCharacteristic();
		
		setNoification(false);
		mBleWrapper.stopMonitoringRssiValue();
		mBleWrapper.diconnect();
		mBleWrapper.close();
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.peripheral, menu);
		if (mBleWrapper.isConnected()) {
	        menu.findItem(R.id.device_connect).setVisible(false);
	        menu.findItem(R.id.device_disconnect).setVisible(true);
	    } else {
	        menu.findItem(R.id.device_connect).setVisible(true);
	        menu.findItem(R.id.device_disconnect).setVisible(false);
	    }		
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.device_connect:
            	mDeviceStatus.setText("connecting ...");
            	mBleWrapper.connect(mDeviceAddress);
                return true;
            case R.id.device_disconnect:
            	mBleWrapper.diconnect();
                return true;
            case android.R.id.home:
            	mBleWrapper.diconnect();
            	mBleWrapper.close();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }	

    
    private void connectViewsVariables() {
    	mDeviceNameView = (TextView) findViewById(R.id.peripheral_name);
		mDeviceAddressView = (TextView) findViewById(R.id.peripheral_address);
		mDeviceRssiView = (TextView) findViewById(R.id.peripheral_rssi);
		mDeviceStatus = (TextView) findViewById(R.id.peripheral_status);
		mListView = (ListView) findViewById(R.id.listView);
		mHeaderTitle = (TextView) mListViewHeader.findViewById(R.id.peripheral_service_list_title);
		mHeaderBackButton = (TextView) mListViewHeader.findViewById(R.id.peripheral_list_service_back);
    }
    
    //add by wzb 
	static String rdata="";
	static String ndata="";
	String infoString="";
	int test_i=0;
	int offset=0;
	TextView testTextView;
	Button testButton;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 123456:
				testTextView.setText(infoString);
				break;
			case 666:
				testButton.setText("测量 (设备就绪)");
				testButton.setClickable(true);
				break;
			case 1234567:
				writeData();
				break;
			case 12345678:
				String bpData=infoString;
				int sys=Integer.valueOf(bpData.substring(16,18),16);
				int bpm=Integer.valueOf(bpData.substring(22,24),16);
				int dia=Integer.valueOf(bpData.substring(20,22),16);
				testTextView.setText("\n"+"     高压:"+"  "+sys+"\n"+"     低压:"+"  "+dia+"\n"+"     心率:"+"  "+bpm);
				testButton.setText("测量 (测试完成)");
				break;
			case 123456789:
				String bgData=infoString;
				int bg=Integer.valueOf(bgData.substring(20,22)+bgData.substring(18,20),16);;
				DecimalFormat df = new DecimalFormat("#.0"); 
				String bg_value=df.format((double)bg/18);
				testTextView.setText("\n"+"    血糖: "+bg_value+" mmol/L");
				testButton.setText("测量 (测试完成)");
				break;
			}
		};
	};
	
	void setNoification(Boolean state){
		BluetoothGatt mBluetoothGatt = mBleWrapper.getGatt();
		Log.d("wzb","mBluetoothGatt="+mBluetoothGatt);
		if(mBluetoothGatt!=null){
			BluetoothGattService readService = mBluetoothGatt.getService(UUID.fromString("00001000-0000-1000-8000-00805f9b34fb"));
			if(readService!=null){
				Log.d("wzb","readService="+readService);
				BluetoothGattCharacteristic read_bgc=readService.getCharacteristic(UUID.fromString("00001002-0000-1000-8000-00805f9b34fb"));
				boolean ret=mBleWrapper.setCharacteristicNotification(read_bgc,state);
				Log.d("wzb","set notify 222 =="+ret);
				if(ret){
					mHandler.sendEmptyMessage(666);
				}
			}
		}
	}
	
	public byte[] parseHexStringToBytes(final String hex) {
		String tmp = hex.substring(2).replaceAll("[^[0-9][a-f]]", "");
		Log.d("wzb","tmp="+tmp);
		byte[] bytes = new byte[tmp.length() / 2]; // every two letters in the string are one byte finally
		
		String part = "";
		
		for(int i = 0; i < bytes.length; ++i) {
			part = "0x" + tmp.substring(i*2, i*2+2);
			bytes[i] = Long.decode(part).byteValue();
			//Log.d("wzb","byte["+i+"]="+bytes[i]);
		}
		//Log.d("wzb","bytes lenght="+bytes.length);
		return bytes;
	}
	
	private void writeData() {
		

			BluetoothGatt mBluetoothGatt = mBleWrapper.getGatt();
			if(mBluetoothGatt!=null){
				Log.d("wzb","write mBluetoothGatt="+mBluetoothGatt);
				BluetoothGattService sendService = mBluetoothGatt.getService(UUID.fromString("00001000-0000-1000-8000-00805f9b34fb"));
				//int data_16 = Integer.parseInt(data, 16);
				//byte[] data=new byte[]{0x5a,0x0b,0x05,0x0f,0x06,0x01,0x08,0x38,(byte) 0xc0,0x00,0x00};
				if (sendService != null) {
					Log.d("wzb","write sendService="+sendService);
					BluetoothGattCharacteristic bgc = sendService.getCharacteristic(UUID.fromString("00001001-0000-1000-8000-00805f9b34fb"));
					
					//String newValue =  "0x5AH0BH05H0FH06H01H08H38HC0H00H00H".toLowerCase(Locale.getDefault());
					String newValue =  "0x5AH0BH05H0FH06H01H08H38HC0H00H00H".toLowerCase(Locale.getDefault());
					//String newValue =  "0x5AH0BH05H0EH0BH08H0CH12HA9H00H00H".toLowerCase(Locale.getDefault());
					byte[] data = parseHexStringToBytes(newValue);
					//bgc.setValue(new byte[] { (byte) data_16 });
					bgc.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
					bgc.setValue(data);
					mBluetoothGatt.writeCharacteristic(bgc);
				}
			}
	}
	
	/**
	 * 搜索到BLE终端服务的事件
	 */
	private BleWrapper.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener() {

		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			Log.d("wzb","get service succues");
			setNoification(true);
		}
	};
    /**
	 * 收到BLE终端数据交互的事件
	 */
	private BleWrapper.OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener() {

		/**
		 * BLE终端数据被读的事件
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, final int status) {
			if (status == BluetoothGatt.GATT_SUCCESS)
				Log.d("wzb",
						"onCharRead "
								+ gatt.getDevice().getName()
								+ " read "
								+ characteristic.getUuid().toString()
								+ " -> "
								+ CommunicationUtil
										.bytesToHexString(characteristic
												.getValue()));
		}

		/**
		 * 收到BLE终端写入数据回调
		 */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
//			Log.d("wzb",
//					"onCharWrite "
//							+ gatt.getDevice().getName()
//							+ " write "
//							+ characteristic.getUuid().toString()
//							+ " -> "
//							+ CommunicationUtil.bytesToHexString(characteristic
//									.getValue()));
//			mBluetoothGattCharacteristic = characteristic;
//			//mHandler.sendEmptyMessage(REFRESH_BATTERY);
			//testTextView.setText(CommunicationUtil.bytesToHexString(characteristic.getValue()));
			infoString=CommunicationUtil.bytesToHexString(characteristic.getValue());
			//if(infoString.equals("55100028060100001506571000061100")){//55100028060100001506571000061100
			if(infoString.startsWith("551000")){
				Log.d("wzb","11111111111111111111111111");
				mHandler.sendEmptyMessage(1234567);
				//writeData();
			}
			
//			if(infoString.equals("55100028030208000e06001100ae1100")){//55100028060100001506571000061100
//				Log.d("wzb","11111111111111111111111111");
//				mHandler.sendEmptyMessage(1234567);
//				//writeData();
//			}
			rdata+=CommunicationUtil.bytesToHexString(characteristic.getValue());
			if(infoString.startsWith("550601")){
				Log.d("wzb","2222222222222222");
				//writeData();
				mHandler.sendEmptyMessage(1234567);
				rdata="";
				ndata="";
				test_i=1;
			}

			//rdata+=CommunicationUtil.bytesToHexString(characteristic.getValue());
//			Log.d("wzb","test_i="+test_i+" ndata="+ndata+" rdata="+rdata);
//			if(rdata.contains("550802")&& (offset==0)){
//				offset=rdata.indexOf("550802");
//				rdata=rdata.substring(offset,rdata.length());
//			}
//			if(rdata.length()>=16*test_i){
//				ndata+=rdata.substring(12+(test_i-1)*16,14+(test_i-1)*16);
//				test_i++;
//			}
//			
//			Log.d("wzb","new ndata="+ndata);

			//mBluetoothGattCharacteristic = characteristic;
			mHandler.sendEmptyMessage(123456);
			
			if(infoString.startsWith("550f03")){
				mHandler.sendEmptyMessage(12345678);
				return;
			}
			
			if(infoString.startsWith("550e03")){
				mHandler.sendEmptyMessage(123456789);
				return;
			}
			
			
			
		}
	};

}
