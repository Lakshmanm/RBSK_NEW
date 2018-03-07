package nunet.services;

import static com.nunet.wsutil.UrlUtils.RequestIncrementalSync;
import static com.nunet.wsutil.UrlUtils.URL_RequestIncrementalSync;

import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.nunet.utils.StringUtils;
import com.nunet.wsutil.AsyncData;
import com.nunet.wsutil.AsyncData.Type;
import com.nunet.wsutil.MyAsyncTask;
import com.nunet.wsutil.onJsonResult;

public class IncrementalService extends Service implements onUrlTaskCompleted,
		onJsonResult {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.e("IncrementalService",
				"-------------------------------------------------------------------------------------");
		Log.e("IncrementalService", "service on create");
		DBTestActivity.step1 = new StringBuffer();
		DBTestActivity.step2 = new StringBuffer();
		DBTestActivity.step3 = new StringBuffer();
		DBTestActivity.step4 = new StringBuffer();
		DBTestActivity.step5 = new StringBuffer();
		// isSyncRunning= false;
		if (!isSyncRunning || TextUtils.isEmpty(getDeviceSyncLogID())) {
			Toast.makeText(this, "Sync Started", Toast.LENGTH_LONG).show();
			isSyncRunning = true;
			AsyncData mAsyncData = new AsyncData(Type.POST);
			mAsyncData.setBaseUrl(URL_RequestIncrementalSync);
			mAsyncData.setUrlId(RequestIncrementalSync);
			JSONObject result = new JSONObject();
			try {
				SharedPreferences sharedpreferences = getSharedPreferences(
						"LoginMain", Context.MODE_PRIVATE);
				String DeviceCode = sharedpreferences.getString("DeviceID",
						null);
				result.put("DeviceID", DeviceCode);
				result.put("TokenID", getTokenID());
			} catch (Exception e) {
				e.printStackTrace();
				onResponseError(e, URL_RequestIncrementalSync,
						RequestIncrementalSync);
			}
			mAsyncData.setParams(result);
			MyAsyncTask myAsyncTask = new MyAsyncTask(this);
			myAsyncTask.setShowProgres(false);
			myAsyncTask.execute(mAsyncData);
		} else {
			Toast.makeText(this, "Sync in proceess " + getDeviceSyncLogID(),
					Toast.LENGTH_LONG).show();
		}
	}

	ObjectTableDetails mObjectDetails = ObjectTableDetails.getInstance();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("IncrementalService",
				"-------------------------------------------------------------------------------------");
		Log.e("IncrementalService", "service on start");
		return START_NOT_STICKY;
	}

	@Override
	public void onGetServerIDCompleted() {
		setTitle("Step 2,3 : GetServerPushSchemaWFARecords");
		new GetServerPushSchemaWFARecords(this).init();
	}

	@Override
	public void onGetServerPushSchemaWFARecords() {
		setTitle("Step 4 : GetDevicePushSchemaWFARecordServerStatus");
		mObjectDetails.moveToFirst();
		new GetDevicePushSchemaWFARecordServerStatus(this, mObjectDetails)
				.init();
	}

	@Override
	public void onGetDevicePushSchemaWFARecordServerStatus() {
		setTitle("Step 5 : DBSyncPush --> SendObjectIncrementalData");
		mObjectDetails.moveToFirst();
		new DBSyncPush(this, mObjectDetails).execute();

	}

	@Override
	public void onDBSyncPushCompleted() {
		setTitle("Step 6 : DBSyncPull --> GetObjectIncrementalData");
		DBSyncPull dbSyncPull = new DBSyncPull(this);
		dbSyncPull.init();
	}

	@Override
	public void onDBSyncPullCompleted() {
		setTitle("DBSyncPull Complete");
		isSyncRunning = false;
	}

	@Override
	public void onResponseError(Exception e, String mData, int callbackurl) {

	}

	public static String TitleMsg = "";

	public static boolean isSyncRunning = false;

	public class MyRunnable implements Runnable {
		public boolean isCancle = false;

		@Override
		public void run() {
			if (!isCancle)
				isSyncRunning = false;
		}
	}

	MyRunnable mRunnable;

	public void setTitle(String title) {

		if (mRunnable != null)
			mRunnable.isCancle = true;

		mRunnable = new MyRunnable();
		new Handler().postDelayed(mRunnable, 1000 * 60 * 10);

		TitleMsg = title;
		if (DBTestActivity.getInstance() != null) {
			DBTestActivity.getInstance().setTitle(title);
		}
	}

	@Override
	public void onJsonResut(JSONObject mObject, int callbackurl) {
		try {
			if (StringUtils.equalsNoCase(mObject.getString("Status"), "1")) {
				SharedPreferences sharedpreferences = getSharedPreferences(
						"LoginMain", Context.MODE_PRIVATE);
				JSONObject jsonObject = mObject.getJSONObject("Data");
				if (StringUtils.equalsNoCase(jsonObject.getString("Result"),
						"True")) {
					String DeviceSyncLogID = this.DeviceSyncLogID = jsonObject
							.getString("DeviceSyncLogID");
					sharedpreferences.edit()
							.putString("DeviceSyncLogID", DeviceSyncLogID)
							.commit();
					setTitle("Step 1 : Getserverids");
					mObjectDetails.moveToFirst();
					DBGetServerID dbGetServerID = new DBGetServerID(
							IncrementalService.this, mObjectDetails);
					dbGetServerID.execute();
				} else {
					Toast.makeText(this, "Sync Not Allowed", Toast.LENGTH_LONG)
							.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			onResponseError(e, URL_RequestIncrementalSync, callbackurl);
		}

	}

	private String DeviceSyncLogID;

	public String getDeviceSyncLogID() {
		if (TextUtils.isEmpty(DeviceSyncLogID)) {
			SharedPreferences sharedpreferences = getSharedPreferences(
					"LoginMain", Context.MODE_PRIVATE);
			DeviceSyncLogID = sharedpreferences.getString("DeviceSyncLogID",
					null);
		}
		return DeviceSyncLogID;
	}

	private String DeviceID;

	public String getDeviceID() {
		if (TextUtils.isEmpty(DeviceID)) {
			SharedPreferences sharedpreferences = getSharedPreferences(
					"LoginMain", Context.MODE_PRIVATE);
			DeviceID = sharedpreferences.getString("DeviceID", null);
		}
		return DeviceID;
	}

	private String TokenID = "100";

	public String getTokenID() {
		if (TextUtils.isEmpty(TokenID)) {
			SharedPreferences sharedpreferences = getSharedPreferences(
					"LoginMain", Context.MODE_PRIVATE);
			TokenID = sharedpreferences.getString("TokenID", null);
		}
		return TokenID;
	}

}