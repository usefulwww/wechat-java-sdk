package com.usefulwww.core.wechat;

public class Setting {
	private String id;

	private String appId;

	private String appCode;

	private String appSecret;
	
	private String appMsgSecret;

	public String getAppMsgSecret() {
		return appMsgSecret;
	}

	public void setAppMsgSecret(String appMsgSecret) {
		this.appMsgSecret = appMsgSecret;
	}

	private String mchId;

	private String mchKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchKey() {
		return mchKey;
	}

	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

}
