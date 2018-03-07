package nunet.services;

import static com.nunet.wsutil.UrlUtils.URL_GetObjectIncrementalData;

import java.util.ArrayList;
import java.util.Iterator;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.ObjectTableModel;
import nunet.services.ObjectTableDetails108.onObjectsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.CaseInsensitiveMap;
import com.nunet.utils.StringUtils;
import com.nunet.wsutil.AsyncData;
import com.nunet.wsutil.AsyncData.Type;
import com.nunet.wsutil.MyAsyncTask;
import com.nunet.wsutil.onJsonResult;

public class DBSyncPull implements onJsonResult {

	public int currentItem = 0;
	private IncrementalService mContext;

	public DBSyncPull(IncrementalService mContext) {
		this.mContext = mContext;
		currentItem = 0;
	}

	private ArrayList<ObjectTableModel> mObjecModels;

	DBSyncPullHelper mPullHelper;

	public void init() {
		DBSyncPullHelper.mDbPullSyncHelper = null;
		mPullHelper = DBSyncPullHelper.getInstance(mContext, mHashMapTable);
		new ObjectTableDetails108(mContext, new onObjectsList() {

			@Override
			public void onObjectsListPrepare(JSONArray mArray,
					ArrayList<ObjectTableModel> mObjecModels) {
				DBSyncPull.this.mObjecModels = mObjecModels;
				getObjectIncrementalDataServer(currentItem);

			}
		}).execute();
	}

	private ObjectTableModel mObjectTableModel;

	public void getObjectIncrementalDataServer(int id) {

		if (mObjecModels != null && mObjecModels.size() <= id)
			return;

		mObjectTableModel = mObjecModels.get(id);
		JSONObject mJsonObject = new JSONObject();
		try {

			logcat("IncrementalService",
					"***************************************************************************");

			mJsonObject.put("ObjectID", mObjectTableModel.getObjectID());
			mJsonObject.put("ObjectName",
					mObjectTableModel.getServerObjectName());

			mJsonObject.put("TokenID", mContext.getTokenID());
			mJsonObject.put("DeviceID", mContext.getDeviceID());
			mJsonObject.put("DeviceSyncLogID", mContext.getDeviceSyncLogID());
			mJsonObject.put("lastCommittedDateTime",
					mObjectTableModel.getLastSyncDate());

			logcat("IncrementalService",
					"----------------" + mObjectTableModel.getObjectID()
							+ "----" + mObjectTableModel.getObjectName());
			AsyncData mAsyncData = new AsyncData(Type.POST);
			mAsyncData.setBaseUrl(URL_GetObjectIncrementalData);
			mAsyncData.setUrlId(id);
			mAsyncData.setParams(mJsonObject);
			logcat("IncrementalService", URL_GetObjectIncrementalData);
			logcat("IncrementalService",
					"Sending Data \n" + mJsonObject.toString());
			MyAsyncTask myAsyncTask = new MyAsyncTask(mContext, this);
			myAsyncTask.setShowProgres(false);
			myAsyncTask.execute(mAsyncData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onJsonResut(JSONObject mObject, int callbackurl)
			throws JSONException {

		String responce = "";
		try {
			if (mObject == null || TextUtils.isEmpty(mObject.toString())) {
				throw new Exception();
			}
			logcat("IncrementalService",
					"response-->  " + mObjectTableModel.getObjectName());
			logcat("IncrementalService", "\n" + mObject.toString());

			if (StringUtils.equalsNoCase(mObject.getString("Status"), "1")) {
				JSONArray jsonArray = mObject.getJSONArray("Data");
				if (jsonArray.length() == 0) {
					logcat("EamptyData", mObjectTableModel.getObjectName());
				}
				new InsertIntoDb(callbackurl).execute(jsonArray);
			} else {
				onResponseError(new Exception(), mObject.toString(),
						callbackurl);
			}
		} catch (Exception e) {
			onResponseError(e, responce, callbackurl);
			e.printStackTrace();
		}
	}

	Toast mToast;

	public void toast() {

		TextView mTextView = new TextView(mContext);
		mTextView.setText(mObjectTableModel.getObjectID() + " -->  "
				+ mObjectTableModel.getObjectName());
		mTextView.setBackgroundColor(Color.WHITE);
		mTextView.setTextColor(Color.BLUE);
		mTextView.setTextSize(20);
		mTextView.setPadding(20, 20, 20, 20);

		if (mToast == null) {
			mToast = new Toast(mContext);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.setView(mTextView);
		mToast.show();
	}

	CaseInsensitiveMap<String, CaseInsensitiveMap<String, String>> mHashMapTable = new CaseInsensitiveMap<String, CaseInsensitiveMap<String, String>>();
	CaseInsensitiveMap<String, String> mHashMapKeys;

	public class InsertIntoDb extends AsyncTask<JSONArray, Void, Boolean> {

		private int postion;

		public InsertIntoDb(int callbackurl) {
			this.postion = callbackurl;
		}

		int i = 0;

		@Override
		protected Boolean doInBackground(JSONArray... params) {

			logcat("IncrementalService",
					"InsertIntoDB  " + mObjectTableModel.getObjectName());

			int logInsert = 0;
			int logUpdate = 0;

			try {
				for (JSONArray jsonArray : params) {
					for (int i = 0; i < jsonArray.length(); i++) {
						String mTableName = mObjectTableModel.getObjectName();
						JSONObject mJsonObject = jsonArray.getJSONObject(i);

						JSONObject dubplicate = new JSONObject(
								mJsonObject.toString());
						mJsonObject = mPullHelper.addLocalIDValues(mTableName,
								mJsonObject);

						ArrayList<String> keys = new ArrayList<String>();
						ArrayList<String> values = new ArrayList<String>();
						Iterator<?> jkeys = mJsonObject.keys();

						String serverKeys = null;
						String serverKeyVal = null;

						String serverKey = mObjectTableModel.getObjectKey();
						while (jkeys.hasNext()) {
							String key = (String) jkeys.next();

							if (TextUtils.equals("DisplaySequence", key)) {
								continue;
							} else {
								String _val = mJsonObject.getString(key);
								if (!TextUtils.isEmpty(_val))
									_val = Html.fromHtml(_val).toString();
								if (StringUtils.equalsNoCase(serverKey, key)) {
									serverKeys = key;
									serverKeyVal = _val;
								} else {
									keys.add("[" + key.trim() + "]");
									values.add(_val);
								}
							}
						}

						if (TextUtils.isEmpty(serverKeyVal))
							throw new Exception();

						String[] keyArr = keys.toArray(new String[keys.size()]);
						String[] valArr = values.toArray(new String[values
								.size()]);

						String LocalServerID = mPullHelper.getLocalID(
								serverKeyVal, mObjectTableModel);

						DBHelper mDbHelper = DBHelper.getInstance(mContext);

						boolean isUpdate = mDbHelper.updateROW(mContext,
								mTableName, keyArr, valArr, serverKeys,
								serverKeyVal, false);

						if (!isUpdate) {
							keys.add(serverKeys);
							values.add(serverKeyVal);
							keyArr = keys.toArray(new String[keys.size()]);
							valArr = values.toArray(new String[values.size()]);

							long insertid = mDbHelper.insertintoTable(mContext,
									mTableName,
									keys.toArray(new String[keys.size()]),
									values.toArray(new String[values.size()]),
									false);
							if (insertid != -1)
								++logInsert;
							LocalServerID = String.valueOf(insertid);
						} else {
							++logUpdate;
						}

						if (!mHashMapTable.containsKey(mTableName)) {
							mHashMapKeys = new CaseInsensitiveMap<String, String>();
							mHashMapTable.put(mTableName, mHashMapKeys);
						}
						mHashMapKeys = mHashMapTable.get(mTableName);
						if (!TextUtils.isEmpty(LocalServerID)) {
							String lcKey = dubplicate
									.getString(mObjectTableModel.getObjectKey());
							mHashMapKeys.put(lcKey, LocalServerID);
						}
					}
					logcat("IncrementalService", " #########  Updated "
							+ logUpdate + " ####### Insert " + logInsert
							+ " ##### Fail "
							+ (jsonArray.length() - (logUpdate + logInsert)));
				}

				DBHelper mDbHelper = DBHelper.getInstance(mContext);
				mDbHelper.updateLastSyncDate(mContext, "objects",
						new String[] { "LastSyncDate" },
						new String[] { Helper.getTodayDateTime() }, "ObjectID",
						mObjectTableModel.getObjectID());
			} catch (Exception e) {
				this.e = e;
				e.printStackTrace();
				return false;
			}

			return true;
		}

		Exception e = new Exception();

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {
				toast();
				if (currentItem == mObjecModels.size())
					mContext.onDBSyncPullCompleted();
				else
					getObjectIncrementalDataServer(currentItem++);
			} else
				onResponseError(e, null, postion);
		}

	}

	@Override
	public void onResponseError(Exception e, String mData, int callbackurl) {

		logcat("IncrementalService",
				"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		logcat("IncrementalService",
				"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		logcat("IncrementalService",
				"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

		final Toast mToast = new Toast(mContext);
		final TextView mTextView = new TextView(mContext);

		mTextView.setBackgroundColor(Color.RED);
		mTextView.setTextColor(Color.WHITE);
		mTextView.setTextSize(20);
		mTextView.setPadding(20, 20, 20, 20);
		mToast.setView(mTextView);
		mToast.setDuration(Toast.LENGTH_LONG);
		if (e != null && e.getMessage() != null) {
			mTextView.append("\n" + e.getMessage());
			logcat("IncrementalService", "Error message " + e.getMessage());
		}
		if (mData != null)
			mTextView.append("\n" + mData);

		for (int i = 0; i < 20; i++) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					mToast.show();
				}
			}, 1000 * i);
		}

		logcat("IncrementalService",
				"Error at table " + mObjectTableModel.getObjectName());
		logcat("IncrementalService", "\n responce error " + mData);
	}

	public void logcat(String tag, String msg) {
		Log.e(tag, msg);
		DBTestActivity.step5.append("\n" + msg);
	}
}