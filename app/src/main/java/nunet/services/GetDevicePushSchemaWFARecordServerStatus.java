package nunet.services;

import static com.nunet.wsutil.UrlUtils.GetDevicePushSchemaWFARecordServerStatus;
import static com.nunet.wsutil.UrlUtils.URL_GetDevicePushSchemaWFARecordServerStatus;
import nunet.rbsk.helpers.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.StringUtils;
import com.nunet.wsutil.AsyncData;
import com.nunet.wsutil.AsyncData.Type;
import com.nunet.wsutil.MyAsyncTask;
import com.nunet.wsutil.onJsonResult;

public class GetDevicePushSchemaWFARecordServerStatus implements onJsonResult {

	private IncrementalService mContext;
	private ObjectTableDetails mTableDetails;

	public GetDevicePushSchemaWFARecordServerStatus(
			IncrementalService mIncrementalService, ObjectTableDetails mObject) {
		this.mContext = mIncrementalService;
		this.mTableDetails = mObject;
		logcat("IncrementalService", "GetDevicePushSchemaWFARecordServerStatus");
	}

	public void init() {
		new PushStatusAsync().execute();
	}

	public class PushStatusAsync extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... params) {
			try {
				logcat("IncrementalService",
						"**********************************************");
				// do {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("TokenID", mContext.getTokenID());
				mJsonObject.put("DeviceID", mContext.getDeviceID());
				mJsonObject.put("DeviceSyncLogID",
						mContext.getDeviceSyncLogID());
				mJsonObject.put("ObjectID", mTableDetails.getObjectID());
				logcat("IncrementalService",
						"GetDevicePushSchemaWFARecordServerStatus");
				logcat("IncrementalService",
						"ObjectID--->" + mTableDetails.getObjectID());
				DBHelper mDbHelper = DBHelper.getInstance(mContext);
				String mTableLocalIDs = "select "
						+ mTableDetails.getLocalIdColumnName() + " from "
						+ mTableDetails.getObjectName()
						+ " where PushStatus is null or PushStatus=0";
				Cursor mCursor = mDbHelper.getCursorData(mContext,
						mTableLocalIDs);

				JSONArray mArray = new JSONArray();
				// String jArr = "[";
				if (mCursor != null) {
					mCursor.moveToFirst();
					do {
						JSONObject testObj = new JSONObject();
						testObj.put("DeviceLocalID", mCursor.getString(0));
						mArray.put(testObj);
					} while (mCursor.moveToNext());
				}
				mJsonObject.put("DeviceLocalIDs", mArray);
				return mJsonObject;
			} catch (Exception e) {
				this.e = e;
				e.printStackTrace();
				return null;
			}
		}

		Exception e = new Exception();

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (result == null) {
				onResponseError(e, mTableDetails.getObjectName(),
						mTableDetails.getObjectID());
				return;
			}
			getDevicePushSchemaWFARecordServerStatus(result);
		}

	}

	private void getDevicePushSchemaWFARecordServerStatus(JSONObject mJsonObject) {
		try {
			logcat("IncrementalService",
					URL_GetDevicePushSchemaWFARecordServerStatus);
			logcat("IncrementalService", mJsonObject.toString());
			AsyncData mAsyncData = new AsyncData(Type.POST);
			mAsyncData.setBaseUrl(URL_GetDevicePushSchemaWFARecordServerStatus);
			mAsyncData.setUrlId(GetDevicePushSchemaWFARecordServerStatus);
			mAsyncData.setParams(mJsonObject);
			MyAsyncTask myAsyncTask = new MyAsyncTask(mContext, this);
			myAsyncTask.setShowProgres(false);
			myAsyncTask.execute(mAsyncData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onJsonResut(JSONObject mObject, int callbackurl) {
		try {
			logcat("IncrementalService", "\nresult\n" + mObject.toString());
			if (StringUtils.equalsNoCase(mObject.getString("Status"), "1")) {
				JSONArray mArrary = mObject.getJSONArray("Data");
				if (mArrary.length() == 0) {
					if (mTableDetails.getNext()) {
						new PushStatusAsync().execute();
					} else {
						mContext.onGetDevicePushSchemaWFARecordServerStatus();
					}
				} else {
					new UpdatePushStatus(mArrary).execute();
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

	private class UpdatePushStatus extends AsyncTask<Void, Void, Boolean> {
		private JSONArray mArray;

		private UpdatePushStatus(JSONArray mArray) {
			this.mArray = mArray;

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {

				logcat("IncrementalService", "UpdatePushStatus");
				for (int i = 0; i < mArray.length(); i++) {
					JSONObject mJsonObject = mArray.getJSONObject(i);
					DBHelper mDbHelper = DBHelper.getInstance(mContext);

					String serverID = mJsonObject.getString("ServerID");
					String ServerPullSyncStatusID = mJsonObject
							.getString("ServerPullSyncStatusID");

					if (TextUtils.equals(ServerPullSyncStatusID, "2")) {
						mDbHelper.updateROW(mContext,
								mTableDetails.getObjectName(), new String[] {
										mTableDetails.getServerIdColumnName(),
										"PushStatus" }, new String[] {
										serverID, "1" },
								mTableDetails.getLocalIdColumnName(),
								mJsonObject.getString("DeviceLocalID"), false);
					} else if (TextUtils.equals(ServerPullSyncStatusID, "3")) {

						logcat("IncrementalService",
								"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
						logcat("IncrementalService",
								"XXXXXXXXXXXX-----Failure Case-----XXXXXXXXXXXXXXXXXXXXX");
						logcat("IncrementalService", mJsonObject.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.e = e;
				return false;

			}
			return true;
		}

		Exception e = new Exception();

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {
				if (mTableDetails.getNext()) {
					new PushStatusAsync().execute();
				} else {
					mContext.onGetDevicePushSchemaWFARecordServerStatus();
				}
			} else {
				onResponseError(e,
						"UpdatePushStatus -->" + mTableDetails.getObjectName(),
						mTableDetails.getObjectID());
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

		for (int i = 0; i < 20; i++) {
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
		DBTestActivity.step3.append("\n" + msg);
	}

}
