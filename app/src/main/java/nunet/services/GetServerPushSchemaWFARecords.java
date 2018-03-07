package nunet.services;

import static com.nunet.wsutil.UrlUtils.GetServerPushSchemaWFARecords;
import static com.nunet.wsutil.UrlUtils.URL_GetServerPushSchemaWFARecords;
import static com.nunet.wsutil.UrlUtils.URL_UpdateServerPushRecrodSyncStatus;
import static com.nunet.wsutil.UrlUtils.UpdateServerPushRecrodSyncStatus;

import java.util.ArrayList;
import java.util.HashMap;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.model.ObjectTableModel;
import nunet.services.ObjectTableDetails108.onObjectsList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.StringUtils;
import com.nunet.wsutil.AsyncData;
import com.nunet.wsutil.AsyncData.Type;
import com.nunet.wsutil.MyAsyncTask;
import com.nunet.wsutil.onJsonResult;

public class GetServerPushSchemaWFARecords implements onJsonResult {

	private IncrementalService mContext;
	private ArrayList<ObjectTableModel> mObjecModels;
	private ObjectTableModel mTableDetails;

	public GetServerPushSchemaWFARecords(IncrementalService mContext) {
		this.mContext = mContext;

	}

	public void init() {

		logcat("IncrementalService", "ObjectTableDetails108");
		logcat("IncrementalService", "request for all tabels");

		new ObjectTableDetails108(mContext, new onObjectsList() {

			@Override
			public void onObjectsListPrepare(JSONArray mArray,
					ArrayList<ObjectTableModel> mObjecModelsRes) {
				mObjecModels = mObjecModelsRes;
				getServerPushSchemaWfaRecords();

			}
		}).execute();
	}

	int count = 0;

	private void getServerPushSchemaWfaRecords() {

		logcat("IncrementalService",
				"**********************************************");
		logcat("IncrementalService", "getServerPushSchemaWfaRecords");
		logcat("IncrementalService", "count--->" + count);

		try {
			if (count < mObjecModels.size()) {
				mTableDetails = mObjecModels.get(count++);
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("TokenID", mContext.getTokenID());
				mJsonObject.put("DeviceID", mContext.getDeviceID());
				mJsonObject.put("ObjectID", mTableDetails.getObjectID());
				AsyncData mAsyncData = new AsyncData(Type.POST);
				mAsyncData.setBaseUrl(URL_GetServerPushSchemaWFARecords);
				mAsyncData.setUrlId(GetServerPushSchemaWFARecords);
				mAsyncData.setParams(mJsonObject);
				MyAsyncTask myAsyncTask = new MyAsyncTask(mContext,
						GetServerPushSchemaWFARecords.this);
				myAsyncTask.setShowProgres(false);
				logcat("IncrementalService", "\n"
						+ URL_GetServerPushSchemaWFARecords);
				logcat("IncrementalService", mJsonObject.toString());
				myAsyncTask.execute(mAsyncData);
			} else {
				mContext.onGetServerPushSchemaWFARecords();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onJsonResut(JSONObject mObject, int callbackurl) {
		logcat("IncrementalService", "Json Result -> " + mObject.toString());
		try {
			if (StringUtils.equalsNoCase(mObject.getString("Status"), "1")) {
				if (UpdateServerPushRecrodSyncStatus == callbackurl) {
					String data = mObject.getString("Data");
					if (StringUtils.equalsNoCase(data, "True")) {
						getServerPushSchemaWfaRecords();
					} else {
						throw new Exception();
					}
				} else if (GetServerPushSchemaWFARecords == callbackurl) {
					JSONArray jsonArray = mObject.getJSONArray("Data");
					if (jsonArray.length() == 0) {
						logcat("EamptyData", mTableDetails.getObjectName());
						getServerPushSchemaWfaRecords();
					} else {
						new UpdateServerPushRecrodSyncStatus(jsonArray)
								.execute();
					}
				}
			} else {
				onResponseError(new Exception(), mObject.toString(),
						callbackurl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			onResponseError(e, mObject.toString(), callbackurl);
		}
	}

	class UpdateServerPushRecrodSyncStatus extends
			AsyncTask<Void, Void, JSONArray> {
		private JSONArray mResultArray;

		UpdateServerPushRecrodSyncStatus(JSONArray mResultArray) {
			this.mResultArray = mResultArray;

		}

		@Override
		protected JSONArray doInBackground(Void... params) {
			JSONArray mArray = new JSONArray();
			logcat("IncrementalService", "UpdateServerPushRecrodSyncStatus");
			logcat("IncrementalService", "count--->" + count);
			try {
				String arr = "(";
				HashMap<String, String> hashMap = new HashMap<String, String>();
				int length = mResultArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject mJsonObject = mResultArray.getJSONObject(i);
					String mServerID = mJsonObject.getString("ServerID");
					arr += mServerID;
					if (i < length - 1)
						arr += ",";
					hashMap.put(mServerID, "3");
				}

				arr += ")";

				String query = "select " + mTableDetails.getObjectKey()
						+ " from " + mTableDetails.getObjectName() + " where "
						+ mTableDetails.getObjectKey() + " in " + arr;
				DBHelper mDbHelper = DBHelper.getInstance(mContext);
				Cursor mCursor = mDbHelper.getCursorData(mContext, query);

				if (mCursor != null) {
					mCursor.moveToFirst();
					do {
						String serverkey = mCursor.getString(0);
						hashMap.put(serverkey, "2");
					} while (mCursor.moveToNext());
				}

				for (String key : hashMap.keySet()) {
					JSONObject mJsonObject = new JSONObject();
					mJsonObject.put("ServerID", key);
					mJsonObject.put("DevicePullSyncStatusID", hashMap.get(key));
					mArray.put(mJsonObject);
				}
				return mArray;
			} catch (Exception e) {
				this.e = e;
				e.printStackTrace();
				return null;
			}

		}

		Exception e = new Exception();

		@Override
		protected void onPostExecute(JSONArray result) {
			super.onPostExecute(result);
			try {
				if (result == null)
					throw e;
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("TokenID", mContext.getTokenID());
				mJsonObject.put("DeviceID", mContext.getDeviceID());
				mJsonObject.put("ObjectID", mTableDetails.getObjectID());
				mJsonObject.put("ObjectName",
						mTableDetails.getServerObjectName());
				mJsonObject.put("PrimaryKeyColumn",
						mTableDetails.getObjectKey());
				mJsonObject.put("jsonPushObjectData", result);

				AsyncData mAsyncData = new AsyncData(Type.POST);
				mAsyncData.setBaseUrl(URL_UpdateServerPushRecrodSyncStatus);
				mAsyncData.setUrlId(UpdateServerPushRecrodSyncStatus);
				mAsyncData.setParams(mJsonObject);
				MyAsyncTask myAsyncTask = new MyAsyncTask(mContext,
						GetServerPushSchemaWFARecords.this);
				myAsyncTask.setShowProgres(false);

				logcat("IncrementalService", "\n"
						+ URL_UpdateServerPushRecrodSyncStatus);
				logcat("IncrementalService", mJsonObject.toString());
				myAsyncTask.execute(mAsyncData);
			} catch (Exception e) {
				e.printStackTrace();
				onResponseError(e, "UpdateServerPushRecrodSyncStatus", -1);
			}
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

		for (int i = 0; i < 20;i++) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					mToast.show();
				}
			}, 1000 * i);

		}

		logcat("IncrementalService",
				"Error at table " + mTableDetails.getObjectName());
		logcat("IncrementalService", "\n responce error " + mData);
	}

	public void logcat(String tag, String msg) {
		Log.e(tag, msg);
		DBTestActivity.step2.append("\n" + msg);
	}
}
