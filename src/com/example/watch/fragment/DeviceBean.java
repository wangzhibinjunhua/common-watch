package com.example.watch.fragment;

public class DeviceBean {

	//private String title;
	private String name;
	private String phoneNumber;
	private String imei;
	/**
	 * 标识是否可以删除
	 */
	private boolean canRemove = true;

	public String getMessage() {
		return "姓名:"+name+"\n"+"电话:"+phoneNumber+"\n"+"设备ID:"+imei;
	}


	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	public DeviceBean(String name,String phoneNumber,String imei, boolean canRemove) {
		this.name = name;
		this.phoneNumber=phoneNumber;
		this.imei=imei;
		this.canRemove = canRemove;
	}

	public DeviceBean() {
	}

}
