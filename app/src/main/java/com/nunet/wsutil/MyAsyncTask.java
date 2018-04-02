package com.nunet.wsutil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class MyAsyncTask extends AsyncTask<AsyncData, Integer, AsyncData> {

	private Context mContext;
	private NetworkUtils mNetworkUtils;

	private onJsonResult mJsonResult;

	private ProgressDialog dialog;

	private boolean showProgres = false;

	public MyAsyncTask(Context mContext) {
		this.mContext = mContext;
		mNetworkUtils = new NetworkUtils();

		if (mContext instanceof onJsonResult)
			mJsonResult = (onJsonResult) mContext;

	}

	public MyAsyncTask(Context mContext, onJsonResult mJsonResult) {
		this.mContext = mContext;
		mNetworkUtils = new NetworkUtils();
		this.mJsonResult = mJsonResult;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowProgres()) {
			dialog = new ProgressDialog(mContext);
			dialog.setMessage("Please wait...");
			dialog.show();
		}
	}

	@Override
	protected AsyncData doInBackground(AsyncData... params) {
		for (AsyncData data : params) {

			if (mNetworkUtils.isOnline(mContext)) {

				switch (data.getType()) {
				case GET:

					return getData(data);
				case POST:

					return postData(data);

				}
			} else {
				data.setNetwork_status(false);

				return data;
			}
		}

		return null;

	}

	@SuppressLint("NewApi")
	public void executeWs(AsyncData... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			execute(params);
		}
	}

	public AsyncData getData(AsyncData mData) {
		String url = mData.getBaseUrl();

		if (!url.endsWith("?"))
			url += "?";
		JSONObject mJsonObject = mData.getParams();
		int count = 0;
		if (mJsonObject != null) {
			@SuppressWarnings("unchecked")
			Iterator<String> iter = mJsonObject.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				try {
					if (count != 0)
						url += "&";
					url += key + "=" + mJsonObject.getString(key);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				count++;

			}
		}

		HttpClient client = new DefaultHttpClient();

		HttpGet httppost = new HttpGet(url);
		httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
		Log.e("url...", url);

		try {

			HttpResponse response = client.execute(httppost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				String JsonResult = EntityUtils.toString(entity);
				mData.setRespoanseJson(new JSONObject(JsonResult));
			}
		} catch (Exception e) {
			mData.setNetwork_status(false);
			e.printStackTrace();
		}

		return mData;

	}

	public AsyncData postData(AsyncData mData) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		Log.i("url b4 : ", mData.getBaseUrl());
		HttpPost httppost = new HttpPost(mData.getBaseUrl());
		String mResultData = null;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			JSONObject mJsonObject = mData.getParams();
			if (mJsonObject != null) {
				@SuppressWarnings("unchecked")
				Iterator<String> iter = mJsonObject.keys();
				while (iter.hasNext()) {
					String key = iter.next();
					try {
						String string = mJsonObject.getString(key);
						nameValuePairs.add(new BasicNameValuePair(key, string));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			Log.i("input in post",new UrlEncodedFormEntity(nameValuePairs).toString());
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			mResultData = EntityUtils.toString(response.getEntity());
			mData.setRespoanseJson(new JSONObject(mResultData));

		} catch (Exception e) {
			mData.setRespoanseString(mResultData);
			mData.setException(e);
			mData.setNetwork_status(false);
			e.printStackTrace();
		}
		return mData;

	}

	@Override
	protected void onPostExecute(AsyncData result) {
		super.onPostExecute(result);

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (result != null && result.isNetwork_status()) {
			try {
				if (mJsonResult != null){
					Log.i("Response : ",result.getRespoanseJson().toString());
					mJsonResult.onJsonResut(result.getRespoanseJson(),
							result.getUrlId());
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			if (mJsonResult != null)
				mJsonResult.onResponseError(result.getException(),
						result.getRespoanseString(), result.getUrlId());
			Toast.makeText(mContext, "Network Failure", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public boolean isShowProgres() {
		return showProgres;
	}

	public void setShowProgres(boolean showProgres) {
		this.showProgres = showProgres;
	}

}
