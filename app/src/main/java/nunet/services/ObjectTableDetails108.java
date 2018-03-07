package nunet.services;

import java.util.ArrayList;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.model.ObjectTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.AsyncTask;

public class ObjectTableDetails108 extends
		AsyncTask<Void, JSONObject, JSONArray> {

	private IncrementalService mContext;
	private ArrayList<ObjectTableModel> mObjecModels;
	private onObjectsList mObjectsList;
	private JSONArray result;

	public interface onObjectsList {
		void onObjectsListPrepare(JSONArray mArray,
				ArrayList<ObjectTableModel> mObjecModels);
	}

	public ObjectTableDetails108(IncrementalService mContext,
			onObjectsList mObjectsList) {
		this.mContext = mContext;
		this.mObjectsList = mObjectsList;

	}

	public void execute() {
		if (mObjecModels == null || mObjecModels.size() == 0 || result == null)
			super.execute();
		else
			mObjectsList.onObjectsListPrepare(result, mObjecModels);
	}

	@Override
	protected JSONArray doInBackground(Void... params) {
		return getObjectsData();
	}

	@Override
	protected void onPostExecute(JSONArray result) {
		super.onPostExecute(result);
		this.result = result;
		mObjectsList.onObjectsListPrepare(result, mObjecModels);
	}

	public JSONArray getObjectsData() {

		try {

			mObjecModels = new ArrayList<ObjectTableModel>();
			String mQuery = "SELECT ServerObjectName,ObjectID,ObjectName,LastSyncDate FROM objects O inner join transactionscopes T on t.transactionscopeid=O.transactionscopeid  order by T.[Order],O.[Order]";
			DBHelper mDbHelper = DBHelper.getInstance(mContext);
			Cursor mCursor = mDbHelper.getCursorData(
					mContext.getApplicationContext(), mQuery);

			JSONArray mArray = new JSONArray();
			if (mCursor != null) {
				mCursor.moveToFirst();
				int idxObjectID = mCursor.getColumnIndex("ObjectID");
				int idxObjectName = mCursor.getColumnIndex("ObjectName");
				int idxLastSyncDate = mCursor.getColumnIndex("LastSyncDate");
				int idxServerObjectName = mCursor
						.getColumnIndex("ServerObjectName");

				do {
					JSONObject mDataJsonObject = new JSONObject();
					ObjectTableModel model = new ObjectTableModel();
					model.setObjectID(mCursor.getString(idxObjectID));
					model.setObjectName(mCursor.getString(idxObjectName));
					String string = mCursor.getString(idxLastSyncDate);
					model.setLastSyncDate(string);
					model.setServerObjectName(mCursor
							.getString(idxServerObjectName));
					try {
						mDataJsonObject.put("ObjectID",
								mCursor.getString(idxObjectID));
						mDataJsonObject.put("LastSyncDate", string);
						mArray.put(mDataJsonObject);
					} catch (Exception e) {
						e.printStackTrace();
					}

					mObjecModels.add(model);

					DBHelper mDbHelper2 = DBHelper.getInstance(mContext);

					String[] colname = mDbHelper2.getColNames(mContext,
							model.getObjectName());
					String objectKey = null;
					String objectLocalKey = null;

					if (colname[0].contains("Local")) {
						objectLocalKey = colname[0];
						objectKey = colname[1];
					} else {
						objectLocalKey = null;
						objectKey = colname[0];
					}
					model.setObjectKey(objectKey);
					model.setObjectLocalKey(objectLocalKey);
				} while (mCursor.moveToNext());
			}

			return mArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}