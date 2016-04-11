package com.example.watch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.navisdk.ui.widget.NewerGuideDialog;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2015-12-15ÏÂÎç01:37:17
 */
public class HttpManager {
	private Context mContext;

	JSONArray jArray;
	String result = null;
	InputStream is = null;
	String strResult = "";
	StringBuilder sb = null;
	String serviceAddr = "http://120.24.36.177/fota/getLocation.php";

	// String info[] = { "1", "1", "1", "1" };

	public HttpManager(Context context) {
		mContext = context;

	}

	public String[] getHttpData() {
		String info[] = { "1", "1", "1", "1" };
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(serviceAddr);
			HttpResponse response;
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Log.d("wzb", "result=" + result);
			jArray = new JSONArray(result);
			JSONObject jsonObject = null;

			for (int i = 0; i < jArray.length(); i++) {
				jsonObject = jArray.getJSONObject(i);
				info[0] = jsonObject.getString("userid");
				info[1] = jsonObject.getString("longitude");
				info[2] = jsonObject.getString("latitude");
				info[3] = jsonObject.getString("datetime");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 4; i++)
			Log.d("wzb", "info[" + i + "]=" + info[i]);
		return info;
	}

	// type "101" bp_demo

	@SuppressLint("NewApi")
	public String getHttpData(String imei, String time, int type) {
		ArrayList nameValuePairs = new ArrayList();
		Log.d("wzb", "imei=" + imei + " time=" + time + " type=" + type);
		nameValuePairs.add(new BasicNameValuePair("c_datetime", time));
		nameValuePairs.add(new BasicNameValuePair("c_userid", imei));
		HttpPost httpPost;
		String info[] = { "1", "1", "1", "1" };
		try {
			HttpClient httpClient = new DefaultHttpClient();
			// HttpGet httpGet = new HttpGet(serviceAddr);
			if (type == 101) {
				Log.d("wzb", "type:101");
				httpPost = new HttpPost(
						"http://huajunsz.com/fota/get_bp_demo.php");
			} else {
				httpPost = new HttpPost(serviceAddr);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response;

			response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();
	
			is = entity.getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			sb = new StringBuilder();
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			if(sb!=null){
				result = sb.toString();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
		}

		// try {
		// Log.d("wzb", "result=" + result);
		// jArray = new JSONArray(result);
		// JSONObject jsonObject = null;
		//
		// for (int i = 0; i < jArray.length(); i++) {
		// jsonObject = jArray.getJSONObject(i);
		// if (type == 101) {
		// Log.d("wzb","type:101");
		// info[0] = jsonObject.getString("imei");
		// info[1] = jsonObject.getString("bp");
		// info[2] = jsonObject.getString("bpm");
		// info[3] = jsonObject.getString("time");
		// } else {
		// info[0] = jsonObject.getString("userid");
		// info[1] = jsonObject.getString("longitude");
		// info[2] = jsonObject.getString("latitude");
		// info[3] = jsonObject.getString("datetime");
		// }
		//
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// for (int i = 0; i < 4; i++)
		// Log.d("wzb", "info[" + i + "]=" + info[i]);
		// return info;

		// return result;
		Log.d("wzb", "result=" + result +" length="+result.length());
		return (result == null || result.length()<10)? "0xffffffff":result;
	}

	public String[] getHttpData(String imei, String time) {
		ArrayList nameValuePairs = new ArrayList();
		Log.d("wzb", "imei=" + imei + " time=" + time);
		String info[] = { "1", "1", "1", "1" };
		nameValuePairs.add(new BasicNameValuePair("c_datetime", time));
		nameValuePairs.add(new BasicNameValuePair("c_userid", imei));

		try {
			HttpClient httpClient = new DefaultHttpClient();
			// HttpGet httpGet = new HttpGet(serviceAddr);
			HttpPost httpPost = new HttpPost(serviceAddr);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response;

			response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Log.d("wzb", "result=" + result);
			jArray = new JSONArray(result);
			JSONObject jsonObject = null;
			for (int i = 0; i < jArray.length(); i++) {
				jsonObject = jArray.getJSONObject(i);
				info[0] = jsonObject.getString("userid");
				info[1] = jsonObject.getString("longitude");
				info[2] = jsonObject.getString("latitude");
				info[3] = jsonObject.getString("datetime");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 4; i++)
			Log.d("wzb", "info[" + i + "]=" + info[i]);
		return info;
	}

	// ·ÏÆú
	public String[] gpsToBaidu(String lat, String lon) {
		Log.d("wzb", "lat=" + lat + " lon=" + lon);
		String info[] = { "1", "1", "1", "1" };
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(
					"http://api.map.baidu.com/geoconv/v1/?coords="
							+ lat
							+ ","
							+ lon
							+ "&from=1&to=5&ak=4rtql443cDcAw5Ow4a1Cmmpa"
							+ "&mcode=AF:B8:38:F5:BD:66:1B:AC:24:67:B4:99:14:BD:5B:30:3C:FC:E7:78;com.example.watch");
			HttpResponse response;
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Log.d("wzb", "result=" + result);
			jArray = new JSONArray(result);
			JSONObject jsonObject = null;
			for (int i = 0; i < jArray.length(); i++) {
				jsonObject = jArray.getJSONObject(i);
				info[0] = jsonObject.getString("status");
				info[1] = jsonObject.getString("result");
				// info[2]=jsonObject.getString("latitude");
				// info[3]=jsonObject.getString("datetime");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 2; i++)
			Log.d("wzb", "info[" + i + "]=" + info[i]);
		return info;
	}

}
