package com.example.watch.health;

import android.R.string;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com>
 * 2016-2-24上午10:21:07	
 */
public class HealthInfoBean {
	
	private String name;
	private String phoneNumber;
	private String imei;
	private String bp;
	private String bpm;
	private String time;
	public HealthInfoBean(String name,String phoneNumber,String imei,String bp,String bpm,String time){
		this.name=name;
		this.phoneNumber=phoneNumber;
		this.imei=imei;
		this.bp=bp;
		this.bpm=bpm;
		this.time=time;
	}
	
	public String getHealthInfo(){
		if(bp.equals("1")){
			return " "+name+"("+phoneNumber+")"+"\n"+" 无测量数据";
		}else{
			return " "+time+"\n"+" "+name+"("+phoneNumber+")"+"\n"+" 血压:"+bp+"\n"+" 心率:"+bpm;
		}
	}
	
	
}
