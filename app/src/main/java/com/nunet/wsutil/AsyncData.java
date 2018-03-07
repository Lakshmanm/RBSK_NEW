package com.nunet.wsutil;

import org.json.JSONObject;

public class AsyncData {

	private String baseUrl;
	private int urlId;
	private boolean isCacheEnable;
	private JSONObject params;
	private boolean network_status=true;
	
	
	private Type mType;
	
	private String respoanseString;
	private JSONObject respoanseJson;
	
	private Exception exception;
	
	public enum Type{
		POST,GET
	}
	
	public AsyncData(Type mType){
		this.setType(mType);
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public boolean isNetwork_status() {
		return network_status;
	}

	public void setNetwork_status(boolean network_status) {
		this.network_status = network_status;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public int getUrlId() {
		return urlId;
	}

	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}

	public boolean isCacheEnable() {
		return isCacheEnable;
	}

	public void setCacheEnable(boolean isCacheEnable) {
		this.isCacheEnable = isCacheEnable;
	}


	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}



	public Type getType() {
		return mType;
	}

	public void setType(Type mType) {
		this.mType = mType;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getRespoanseString() {
		return respoanseString;
	}

	public void setRespoanseString(String respoanseString) {
		this.respoanseString = respoanseString;
	}

	public JSONObject getRespoanseJson() {
		return respoanseJson;
	}

	public void setRespoanseJson(JSONObject respoanseJson) {
		this.respoanseJson = respoanseJson;
	}

}
