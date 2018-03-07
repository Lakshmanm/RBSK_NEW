package com.nunet.wsutil;

import org.json.JSONException;
import org.json.JSONObject;

public interface onJsonResult {
	
	
	/**@param
	 * */
	public void onJsonResut(JSONObject mObject, int callbackurl) throws JSONException;
	public void onResponseError(Exception e, String mData,int callbackurl);

}
