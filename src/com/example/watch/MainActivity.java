package com.example.watch;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import junit.framework.Test;

import android.R.integer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.watch.R;
import com.example.watch.fragment.LeftFragment;
import com.example.watch.util.HttpManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import com.baidu.lbsapi.BMapManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.navisdk.ui.widget.NewerGuideDialog;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2015-12-14下午03:00:47
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener, OnGetRoutePlanResultListener, OnMapClickListener,
		OnGetGeoCoderResultListener {

	private ImageView topButton;
	private ImageView topLocationButton;
	private TextView topTextView;
	private boolean flag = false;
	Thread mThread;
	private HttpManager hm;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	BitmapDescriptor mDeviceMarker;

	MapView mMapView;

	BaiduMap mBaiduMap;
	//boolean isFirstLoc = true;// 是否首次定位
	static int isFirstLoc=0;
	// @InjectView(R.id.customicon)
	Button customicon;
	private GeoCoder mGeoSearch;

	// 搜索相关
	RoutePlanSearch mSearch = null;
	private BitmapDescriptor bd;
	AsyncTask<Void, LatLng, String> task;
	
	// test
	double lat = 22.53;
	double lon = 113.95;
	LatLng sourceLatLng=new LatLng(22.5357f, 113.920111f);
	LatLng desLatLng=new LatLng(22.5357f, 113.920111f);
	// test

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init_baidu_map();
		Log.d("wzb", "MainActivity onCreate");
		hm=new HttpManager(this);
		initSlidingMenu(savedInstanceState);
		initView();
		

	}

	private void init_baidu_map() {
		// 先初始化BMapManager
		MyApplication app = (MyApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(app);

			app.mBMapManager.init(new MyApplication.MyGeneralListener());
		}
		initMapDetail();

	}

	private void initMapDetail() {
		mMapView = (MapView) findViewById(R.id.baidumapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0f));

		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);

		mDeviceMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.device_location);

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(null,
				false, null));
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mBaiduMap.setOnMapClickListener(this);

		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		mGeoSearch = GeoCoder.newInstance();
		mGeoSearch.setOnGetGeoCodeResultListener(this);
		bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置

			if (location == null || mMapView == null)
				return;
			// mCity = location.getCity();
			// T.showLong(mContext, mCity);
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			Log.d("wzb", "lati:" + location.getLatitude() + "long:"
					+ location.getLongitude());
			// if (isFirstLoc) {
			if (isFirstLoc==0) {
				isFirstLoc = -1;
				// mStartPt = new LatLng(location.getLatitude(),
				// location.getLongitude());
				

				//LatLng ll = new LatLng(location.getLatitude(),
						//location.getLongitude());
				desLatLng=new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(desLatLng);
				mBaiduMap.animateMapStatus(u);
			}else if(isFirstLoc==1){
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(desLatLng);
				mBaiduMap.animateMapStatus(u);
				isFirstLoc=-1;
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void requestLocation() {
		if (mLocClient != null && mLocClient.isStarted()) {
			isFirstLoc = 1;
			mLocClient.requestLocation();
		}
	}

	private void addMarker(double arg1, double arg2) {
		if (mBaiduMap != null) {
			LatLng deviceLng = new LatLng(arg1, arg2);
			try {
				//mBaiduMap.clear();
				OverlayOptions ooA = new MarkerOptions().position(deviceLng)
				.zIndex(9).icon(mDeviceMarker);
				mBaiduMap.addOverlay(ooA);
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("wzb","baidumap clear error");
			}
			
		
		}
	}
	private long firstTime = 0;
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			long secondTime = System.currentTimeMillis();
//			if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2秒，则不退出
//				Toast.makeText(this,getString(R.string.exit_dialog), 666).show();
//				firstTime = secondTime;// 更新firstTime
//				return true;
//			} else {//两次按键小于2秒时，退出应用
//				System.exit(0);
//				System.gc();
//				return false;
//			}
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
//	}


	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0000:
				lat += 0.0002;
				lon += 0.0003;
				Log.d("wzb", "thread 0000");
				
				Log.d("wzb","xxx :"+desLatLng.latitude+","+desLatLng.longitude);
				addMarker(desLatLng.latitude,desLatLng.longitude);
				
				break;
			default:
				break;
			}
		}
	};
	
	private void gpsTobaidu(String lat,String lon){
		Log.d("wzb","gps to  baidu");
		String lat_temp1=lat.substring(lat.length()-8,lat.length());
		String lat_temp2=lat.substring(1,lat.length()-8);
		String lat_temp3=lat.substring(0,1);
		Double lat_temp4=(Double.valueOf(lat_temp2)+Double.valueOf(lat_temp1)/60);	
		if(lat_temp3.equals("S")) lat_temp4=-lat_temp4;
		
		String lon_temp1=lon.substring(lon.length()-8,lon.length());
		String lon_temp2=lon.substring(1,lon.length()-8);
		String lon_temp3=lon.substring(0,1);
		Double lon_temp4=(Double.valueOf(lon_temp2)+Double.valueOf(lon_temp1)/60);	
		if(lon_temp3.equals("W")) lon_temp4=-lon_temp4;
		Log.d("wzb"," "+lat_temp1+","+lat_temp2+","+lat_temp3);
		Log.d("wzb"," "+lon_temp1+","+lon_temp2+","+lon_temp3);
		Log.d("wzb","lat="+lat_temp4+" lon="+lon_temp4);
		// 将GPS设备采集的原始GPS坐标转换成百度坐标  
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.GPS);  
		// sourceLatLng待转换坐标  
		sourceLatLng=new LatLng(lat_temp4, lon_temp4);
		converter.coord(sourceLatLng);  
		desLatLng = converter.convert();
	}

	private Thread locationThread = new Thread() {
		public void run() {
			while (flag) {
				try {
					sleep(3000);
					String data[]={"1","1","1","1"};
					data=hm.getHttpData("3721","2015");
					gpsTobaidu(data[2],data[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(0000);
			}
		}
	};

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
		flag = false;
		mThread = null;
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		flag = true;
		mThread = new Thread(locationThread);
		mThread.start();
	}

	@Override
	protected void onDestroy() {

		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		flag = false;
		mThread = null;
		super.onDestroy();
	}

	private void initView() {
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		topLocationButton=(ImageView)findViewById(R.id.locationButton);
		topLocationButton.setOnClickListener(this);

	}

	private void initSlidingMenu(Bundle savedInstanceState) {
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();

		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.LEFT);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}
	void test(){
		try {
			Socket socket=new Socket("120.24.36.177",1234);
			String messageString="abcdefg";
			try{
			PrintWriter out = new PrintWriter(  
                    new BufferedWriter(new OutputStreamWriter(  
                            socket.getOutputStream())), true);  
			out.println(messageString); 
			}catch(Exception e){
				Log.d("wzb","111");
			}finally{
				socket.close();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.topButton:
			 toggle();
			break;
		case R.id.locationButton:
			requestLocation();
			break;
		default:
			break;
		}

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

}
