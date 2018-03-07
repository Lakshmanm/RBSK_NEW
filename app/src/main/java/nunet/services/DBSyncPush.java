package nunet.services;

import static com.nunet.wsutil.UrlUtils.URL_SendObjectIncrementalData;
import nunet.rbsk.helpers.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class DBSyncPush extends AsyncTask<Void, Void, JSONObject> implements
		onJsonResult {

	private IncrementalService mContext;
	private ObjectTableDetails mTableDetails;

	public DBSyncPush(IncrementalService mContext,
			ObjectTableDetails mTableDetails) {
		this.mContext = mContext;
		this.mTableDetails = mTableDetails;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected JSONObject doInBackground(Void... params) {

		// do {
		JSONObject mJsonObject = new JSONObject();
		try {

			logcat("IncrementalService",
					"***************************************************************************");

			mJsonObject.put("ObjectID", mTableDetails.getObjectID());
			mJsonObject.put("ObjectName", mTableDetails.getObjectName());

			mJsonObject.put("TokenID", mContext.getTokenID());
			mJsonObject.put("DeviceID", mContext.getDeviceID());
			mJsonObject.put("DeviceSyncLogID", mContext.getDeviceSyncLogID());

			DBHelper dbh = DBHelper.getInstance(mContext);
			JSONArray jsonData = DBSyncPushHelper.getTableDataForJSON(dbh,
					mContext, mTableDetails.getObjectName(),
					mTableDetails.getServerIdColumnName(), "");
			mJsonObject.put("ObjectIncrementalDataFromDevice",
					jsonData.toString());

			logcat("IncrementalService", "----" + mTableDetails.getObjectID()
					+ "----" + mTableDetails.getObjectName());
			logcat("IncrementalService",
					"----------------" + mJsonObject.toString());

		} catch (JSONException e) {
			this.e = e;
			e.printStackTrace();
			return null;

		}
		// } while (mDetails.getNext());
		return mJsonObject;

	}

	Exception e = new Exception();

	@Override
	protected void onPostExecute(JSONObject mJsonObject) {
		super.onPostExecute(mJsonObject);
		if (mJsonObject == null) {
			onResponseError(e, mTableDetails.getObjectName(),
					mTableDetails.getObjectID());
			return;
		}
		AsyncData mAsyncData = new AsyncData(Type.POST);
		mAsyncData.setBaseUrl(URL_SendObjectIncrementalData);
		mAsyncData.setUrlId(mTableDetails.getObjectID());
		mAsyncData.setParams(mJsonObject);
		MyAsyncTask myAsyncTask = new MyAsyncTask(mContext, this);
		myAsyncTask.setShowProgres(false);
		myAsyncTask.execute(mAsyncData);
	}

	@Override
	public void onJsonResut(JSONObject mObject, int callbackurl) {
		String responce = "";
		try {

			if (mObject == null || TextUtils.isEmpty(mObject.toString())) {
				throw new Exception();
			}

			logcat("IncrementalService", "" + mTableDetails.getObjectName());
			logcat("IncrementalService", "result " + mObject.toString());

			Toast.makeText(mContext,
					"synced table " + mTableDetails.getObjectName(),
					Toast.LENGTH_SHORT).show();

			if (StringUtils.equalsNoCase(mObject.getString("Data"), "True")) {

				updatetable(mTableDetails.getObjectName());

				if (mTableDetails.getNext()) {
					new DBSyncPush(mContext, mTableDetails).execute();
				} else {
					mContext.onDBSyncPushCompleted();
				}
			} else {
				onResponseError(new Exception(), mObject.toString(),
						callbackurl);
			}
		} catch (Exception e) {
			onResponseError(e, responce, callbackurl);
			e.printStackTrace();
		}
	}

	public void updatetable(final String tableName) {
		DBHelper dbh = DBHelper.getInstance(mContext);
		dbh.updateLCD(mContext, tableName);
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
		DBTestActivity.step4.append("\n" + msg);
	}
}