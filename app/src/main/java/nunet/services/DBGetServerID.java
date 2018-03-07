package nunet.services;

import static com.nunet.wsutil.UrlUtils.GetServerID;
import static com.nunet.wsutil.UrlUtils.URL_GetServerIDs;
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

public class DBGetServerID extends AsyncTask<Void, JSONObject, JSONObject>
		implements onJsonResult {

	private IncrementalService mContext;
	private ObjectTableDetails mTableDetails;

	public DBGetServerID(IncrementalService mContext,
			ObjectTableDetails mTableDetails) {
		this.mContext = mContext;
		this.mTableDetails = mTableDetails;
		logcat("IncrementalService", "DBGetServerID");
		logcat("IncrementalService", "Step 1");
	}

	@Override
	protected JSONObject doInBackground(Void... params) {

		try {
			// do {
			JSONObject mJsonObject = new JSONObject();
			mJsonObject.put("TokenID", mContext.getTokenID());
			mJsonObject.put("DeviceID", mContext.getDeviceID());
			mJsonObject.put("DeviceSyncLogID", mContext.getDeviceSyncLogID());
			mJsonObject.put("ObjectID", mTableDetails.getObjectID());

			logcat("IncrementalService",
					"*************************Start*****************************");
			logcat("IncrementalService",
					"\nObjectID---> " + mTableDetails.getObjectID());
			DBHelper mDbHelper = DBHelper.getInstance(mContext);
			String mTableLocalIDs = "select "
					+ mTableDetails.getLocalIdColumnName()
					+ " from "
					+ mTableDetails.getObjectName()
					+ " where "
					+ mTableDetails.getServerIdColumnName()
					+ " is null  or "
					+ mTableDetails.getServerIdColumnName()
					+ "='' AND (LastCommitedDate is not null or LastCommitedDate!=\"\")";
			Cursor mCursor = mDbHelper.getCursorData(mContext, mTableLocalIDs);

			JSONArray mArray = new JSONArray();
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
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		if (result == null) {
			onResponseError(new Exception(), "DBGetServerID -> "
					+ mTableDetails.toString(), mTableDetails.getObjectID());
			return;
		}

		logcat("IncrementalService", URL_GetServerIDs);
		logcat("IncrementalService", result.toString());

		AsyncData mAsyncData = new AsyncData(Type.POST);
		mAsyncData.setBaseUrl(URL_GetServerIDs);
		mAsyncData.setUrlId(GetServerID);
		mAsyncData.setParams(result);
		MyAsyncTask myAsyncTask = new MyAsyncTask(mContext, this);
		myAsyncTask.setShowProgres(false);
		myAsyncTask.execute(mAsyncData);
	}

	@Override
	public void onJsonResut(JSONObject mObject, int callbackurl) {
		try {
			logcat("IncrementalService", "----------Result------------");
			logcat("IncrementalService", "\n" + mTableDetails.getObjectName());
			logcat("IncrementalService", mObject.toString());
			if (StringUtils.equalsNoCase(mObject.getString("Status"), "1")) {
				JSONArray jsonArray = mObject.getJSONArray("Data");
				if (jsonArray.length() == 0) {
					logcat("EamptyData", mTableDetails.getObjectName());
					if (mTableDetails.getNext()) {
						new DBGetServerID(mContext, mTableDetails).execute();
					} else {
						mContext.onGetServerIDCompleted();
					}
				} else {
					new UpdateServerIDs(jsonArray).execute();
				}

			} else {
				onResponseError(new Exception(), mObject.toString(),
						callbackurl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			onResponseError(new Exception(), mObject.toString(), callbackurl);
		}

	}

	public class UpdateServerIDs extends AsyncTask<Void, Boolean, Boolean> {
		private JSONArray jsonArray;

		UpdateServerIDs(JSONArray jsonArray) {
			this.jsonArray = jsonArray;

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {

				logcat("IncrementalService", "\n UpdateServerIDs"
						+ mTableDetails.getObjectName());

				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject mLocalObjects = jsonArray.getJSONObject(i);
					DBHelper mDbHelper = DBHelper.getInstance(mContext);
					String ServerID = String.valueOf(mLocalObjects
							.get("ServerID"));
					if (TextUtils.equals(ServerID, "0")) {
						boolean result = mDbHelper.updateROW(mContext,
								mTableDetails.getObjectName(),
								new String[] { mTableDetails
										.getServerIdColumnName() },
								new String[] { ServerID }, mTableDetails
										.getLocalIdColumnName(), String
										.valueOf(mLocalObjects
												.get("DeviceLocalID")), false);
						if (!result) {
							throw new Exception();
						}
					}

				}
				return true;
			} catch (Exception e) {
				this.e = e;
				return false;
			}
		}

		Exception e = new Exception();

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {
				if (mTableDetails.getNext()) {
					new DBGetServerID(mContext, mTableDetails).execute();
				} else {
					mContext.onGetServerIDCompleted();
				}
			} else {
				onResponseError(e,
						"UpdateServerIDs -> " + mTableDetails.toString(),
						mTableDetails.getObjectID());
				return;
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
			mTextView.append("DBGetServerID\n" + e.getMessage());
			logcat("IncrementalService", "Error Message --> " + e.getMessage());
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
		logcat("IncrementalService", "responce error \n" + mData);
	}

	public void logcat(String tag, String msg) {
		Log.e(tag, msg);
		DBTestActivity.step1.append("\n" + msg);
	}

}
