package nunet.services;

import nunet.rbsk.helpers.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.nunet.utils.StringUtils;

public class DBSyncPushHelper {
	private static DBHelper dbh;
	private static Context ctx;

	public static JSONArray getTableDataForJSON(DBHelper dbh, Context ctx,
			String tableName, String serverIDColumnName,
			String localIdColumnName) {
		DBSyncPushHelper.dbh = dbh;
		DBSyncPushHelper.ctx = ctx;
		JSONArray jsonArray = new JSONArray();
		String query = "select * from " + tableName + " where "
		// +"("
		// + serverIDColumnName + " is null or " + serverIDColumnName
		// + "='') or"
				+ " (LastCommitedDate is null or LastCommitedDate ='');";
		Cursor cursor = dbh.getCursorData(ctx, query);
		if (cursor != null) {

			try {
				String[] colNames = cursor.getColumnNames();
				while (cursor.moveToNext()) {
					JSONObject jsonObj = new JSONObject();
					for (int i = 0; i < colNames.length; i++) {
						String dataStr = cursor.getString(cursor
								.getColumnIndex(colNames[i]));
						if (TextUtils.isEmpty(dataStr)) {
							jsonObj.put(colNames[i], "");
						} else if (dataStr.trim().equalsIgnoreCase("null")) {
							jsonObj.put(colNames[i], "");
						} else {
							jsonObj.put(colNames[i], dataStr);
						}
					}
					addServerIDValues(tableName, jsonObj);
					// if (StringUtils.equalsNoCase(tableName,
					// "childrenscreeningpe")) {
					// if (TextUtils.equals(jsonObj.getString("Answer"), "1")) {
					// jsonArray.put(jsonObj);
					// }
					// continue;
					// }

					jsonArray.put(jsonObj);
				}
				cursor.close();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		System.out.println("jsonStr:::::" + jsonArray.toString());
		return jsonArray;
	}

	public static JSONObject addServerIDValues(String mTableName,
			JSONObject mObject) {
		try {

			if (StringUtils.equalsNoCase(mTableName, "ContactDetails")) {

				String ServerContactID = getServerID("Contacts",
						"LocalContactID", "ContactID",
						mObject.getString("LocalContactID"));
				mObject.put("ContactID", ServerContactID);

			} else if (StringUtils.equalsNoCase(mTableName, "users")
					|| StringUtils.equalsNoCase(mTableName, "Facilities")) {

				String ServerAddressID = getServerID("Address",
						"LocalAddressID", "AddressID",
						mObject.getString("LocalAddressID"));
				mObject.put("AddressID", ServerAddressID);

				String ServerContactID = getServerID("Contacts",
						"LocalContactID", "ContactID",
						mObject.getString("LocalContactID"));
				mObject.put("ContactID", ServerContactID);

			} else if (StringUtils.equalsNoCase(mTableName, "Institutes")) {

				String ServerAddressID = getServerID("Address",
						"LocalAddressID", "AddressID",
						mObject.getString("LocalAddressID"));
				mObject.put("AddressID", ServerAddressID);

				String ServerContactID = getServerID("Contacts",
						"LocalContactID", "ContactID",
						mObject.getString("LocalContactID"));
				mObject.put("ContactID", ServerContactID);

			} else if (StringUtils.equalsNoCase(mTableName, "usercredentials")
					|| StringUtils.equalsNoCase(mTableName, "mhtstaff")) {

				String ServerUserID = getServerID("users", "LocalUserID",
						"UserID", mObject.getString("LocalUserID"));
				mObject.put("UserID", ServerUserID);

			} else if (StringUtils.equalsNoCase(mTableName, "institutestaff")) {

				String ServerInstituteID = getServerID("institutes",
						"LocalInstituteID", "InstituteID",
						mObject.getString("LocalInstituteID"));
				mObject.put("InstituteID", ServerInstituteID);

				String LocalUserID = getServerID("users", "LocalUserID",
						"UserID", mObject.getString("LocalUserID"));
				mObject.put("UserID", LocalUserID);

			} else if (StringUtils
					.equalsNoCase(mTableName, "institutepictures")) {

				String ServerInstituteID = getServerID("institutes",
						"LocalInstituteID", "InstituteID",
						mObject.getString("LocalInstituteID"));
				mObject.put("InstituteID", ServerInstituteID);

			} else if (StringUtils.equalsNoCase(mTableName, "children")) {

				String ServerUserID = getServerID("users", "LocalUserID",
						"UserID", mObject.getString("LocalUserID"));
				mObject.put("UserID", ServerUserID);

				String ServerInstituteID = getServerID("institutes",
						"LocalInstituteID", "InstituteID",
						mObject.getString("LocalInstituteID"));
				mObject.put("InstituteID", ServerInstituteID);

				String ServerAddressID = getServerID("Address",
						"LocalAddressID", "AddressID",
						mObject.getString("LocalPermanentAddressID"));
				mObject.put("PermanentAddressID", ServerAddressID);

			} else if (StringUtils.equalsNoCase(mTableName,
					"childrendisabilities")) {

				String ServerChildrenID = getServerID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("LocalChildrenID"));
				mObject.put("ChildrenID", ServerChildrenID);

			} else if (StringUtils.equalsNoCase(mTableName, "childrenparents")) {

				String ServerChildrenID = getServerID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("LocalChildrenID"));
				mObject.put("ChildrenID", ServerChildrenID);

				String ServerUserID = getServerID("users", "LocalUserID",
						"UserID", mObject.getString("LocalUserID"));
				mObject.put("UserID", ServerUserID);

			} else if (StringUtils.equalsNoCase(mTableName, "childrenpictures")) {

				String ServerChildrenID = getServerID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("LocalChildrenID"));
				mObject.put("ChildrenID", ServerChildrenID);

			} else if (StringUtils.equalsNoCase(mTableName,
					"instituteplandetails")) {

				String ServerInstitutePlanID = getServerID("instituteplans",
						"LocalInstitutePlanID", "InstitutePlanID",
						mObject.getString("LocalInstitutePlanID"));
				mObject.put("InstitutePlanID", ServerInstitutePlanID);

			} else if (StringUtils.equalsNoCase(mTableName, "instituteplans")) {

			} else if (StringUtils.equalsNoCase(mTableName,
					"institutescreeningdetails")) {

				String ServerInstituteScreeningID = getServerID(
						"institutescreening", "LocalInstituteScreeningID",
						"InstituteScreeningID",
						mObject.getString("LocalInstituteScreeningID"));
				mObject.put("InstituteScreeningID", ServerInstituteScreeningID);

				String ServerInstitutePlanID = getServerID("InstitutePlans",
						"LocalInstitutePlanID", "InstitutePlanID",
						mObject.getString("LocalInstitutePlanID"));
				mObject.put("InstitutePlanID", ServerInstitutePlanID);

			} else if (StringUtils
					.equalsNoCase(mTableName, "childrenscreening")) {

				String ServerChildrenID = getServerID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("LocalChildrenID"));
				mObject.put("ChildrenID", ServerChildrenID);

				String ServerInstituteScreeningDetailID = getServerID(
						"institutescreeningdetails",
						"LocalInstituteScreeningDetailID",
						"InstituteScreeningDetailID",
						mObject.getString("LocalInstituteScreeningDetailID"));
				mObject.put("InstituteScreeningDetailID",
						ServerInstituteScreeningDetailID);

			} else if (StringUtils.equalsNoCase(mTableName,
					"ChildrenScreeningAllergies")

					|| StringUtils.equalsNoCase(mTableName,
							"ChildrenScreeningLocalTreatment")
					|| StringUtils.equalsNoCase(mTableName,
							"ChildrenScreeningPE")
					|| StringUtils.equalsNoCase(mTableName,
							"ChildrenScreeningPictures")
					|| StringUtils.equalsNoCase(mTableName,
							"ChildrenScreeningRecommendations")
					|| StringUtils.equalsNoCase(mTableName,
							"ChildrenScreeningReferrals")
					|| StringUtils.equalsNoCase(mTableName,
							"ChildrenScreeningSurgicals")
					|| StringUtils.equalsNoCase(mTableName,
							"ChildrenScreeningVitals")
					|| StringUtils.equalsNoCase(mTableName,
							"ChildScreeningFH")) {

				String ServerChildrenScreeningID = getServerID(
						"childrenscreening", "LocalChildrenScreeningID",
						"ChildrenScreeningID",
						mObject.getString("LocalChildrenScreeningID"));
				mObject.put("ChildrenScreeningID", ServerChildrenScreeningID);

			} else if (StringUtils.equalsNoCase(mTableName,
					"ChildrenScreeningInvestigations")) {

				String ServerChildrenScreeningID = getServerID(
						"childrenscreening", "LocalChildrenScreeningID",
						"ChildrenScreeningID",
						mObject.getString("LocalChildrenScreeningID"));
				mObject.put("ChildrenScreeningID", ServerChildrenScreeningID);

				String ServerChildrenScreeningReferralID = getServerID(
						"ChildrenScreeningReferrals",
						"LocalChildrenScreeningReferralID",
						"ChildrenScreeningReferralID",
						mObject.getString("LocalChildrenScreeningReferralID"));
				mObject.put("ChildrenScreeningReferralID",
						ServerChildrenScreeningReferralID);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mObject;
	}

	public static String getServerID(String TableName, String mLocalKeyName,
			String mServerKey, String mLocalKeyValue) {
		String query = "Select " + mServerKey + " from " + TableName
				+ " where " + mLocalKeyName + "='" + mLocalKeyValue + "'";
		Cursor mCursor = dbh.getCursorData(ctx, query);
		if (mCursor == null)
			return null;
		mCursor.moveToFirst();
		return mCursor.getString(0);
	}

}
