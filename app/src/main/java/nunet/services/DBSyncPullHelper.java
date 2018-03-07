package nunet.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.ObjectTableModel;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.nunet.utils.CaseInsensitiveMap;
import com.nunet.utils.StringUtils;

public class DBSyncPullHelper {

	private Context mContext;
	private CaseInsensitiveMap<String, CaseInsensitiveMap<String, String>> mHashMapTable;

	public static DBSyncPullHelper mDbPullSyncHelper;

	public static DBSyncPullHelper getInstance(
			Context mContext,
			CaseInsensitiveMap<String, CaseInsensitiveMap<String, String>> mHashMapTable) {

		if (mDbPullSyncHelper == null)
			mDbPullSyncHelper = new DBSyncPullHelper(mContext, mHashMapTable);

		return mDbPullSyncHelper;

	}

	private DBSyncPullHelper(
			Context mContext,
			CaseInsensitiveMap<String, CaseInsensitiveMap<String, String>> mHashMapTable) {
		this.mContext = mContext;
		this.mHashMapTable = mHashMapTable;
	}

	public JSONObject addLocalIDValues(String mTableName, JSONObject mObject) {
		try {

			mObject.put("LastCommitedDate", Helper.getTodayDateTime());
			if (StringUtils.equalsNoCase(mTableName, "ScreenQuestions")) {
				mObject.remove("Order");
			} else if (StringUtils.equalsNoCase(mTableName, "ContactDetails")) {

				String LocalContactID = getLocalID("Contacts",
						"LocalContactID", "ContactID",
						mObject.getString("ContactID"));
				mObject.remove("ContactID");
				mObject.put("LocalContactID", LocalContactID);

			} else if (StringUtils.equalsNoCase(mTableName, "users")
					|| StringUtils.equalsNoCase(mTableName, "Facilities")) {

				String LocalAddressID = getLocalID("Address", "LocalAddressID",
						"AddressID", mObject.getString("AddressID"));
				mObject.remove("AddressID");
				mObject.put("LocalAddressID", LocalAddressID);

				String LocalContactID = getLocalID("Contacts",
						"LocalContactID", "ContactID",
						mObject.getString("ContactID"));
				mObject.remove("ContactID");
				mObject.put("LocalContactID", LocalContactID);

			} else if (StringUtils.equalsNoCase(mTableName, "Institutes")) {

				String LocalAddressID = getLocalID("Address", "LocalAddressID",
						"AddressID", mObject.getString("AddressID"));
				mObject.remove("AddressID");
				mObject.put("LocalAddressID", LocalAddressID);

				String LocalContactID = getLocalID("Contacts",
						"LocalContactID", "ContactID",
						mObject.getString("ContactID"));
				mObject.remove("ContactID");
				mObject.put("LocalContactID", LocalContactID);

			} else if (StringUtils.equalsNoCase(mTableName, "usercredentials")
					|| StringUtils.equalsNoCase(mTableName, "mhtstaff")) {

				String LocalUserID = getLocalID("users", "LocalUserID",
						"UserID", mObject.getString("UserID"));
				mObject.remove("UserID");
				mObject.put("LocalUserID", LocalUserID);

			} else if (StringUtils.equalsNoCase(mTableName, "institutestaff")) {

				String LocalInstituteID = getLocalID("institutes",
						"LocalInstituteID", "InstituteID",
						mObject.getString("InstituteID"));
				mObject.remove("InstituteID");
				mObject.put("LocalInstituteID", LocalInstituteID);

				String LocalUserID = getLocalID("users", "LocalUserID",
						"UserID", mObject.getString("UserID"));
				mObject.remove("UserID");
				mObject.put("LocalUserID", LocalUserID);

			} else if (StringUtils
					.equalsNoCase(mTableName, "institutepictures")) {

				String LocalInstituteID = getLocalID("institutes",
						"LocalInstituteID", "InstituteID",
						mObject.getString("InstituteID"));
				mObject.remove("InstituteID");
				mObject.put("LocalInstituteID", LocalInstituteID);

			} else if (StringUtils.equalsNoCase(mTableName, "children")) {

				String LocalUserID = getLocalID("users", "LocalUserID",
						"UserID", mObject.getString("UserID"));
				mObject.remove("UserID");
				mObject.put("LocalUserID", LocalUserID);

				String LocalInstituteID = getLocalID("institutes",
						"LocalInstituteID", "InstituteID",
						mObject.getString("InstituteID"));
				mObject.remove("InstituteID");
				mObject.put("LocalInstituteID", LocalInstituteID);

				String LocalAddressID = getLocalID("Address", "LocalAddressID",
						"AddressID", mObject.getString("PermanentAddressID"));
				mObject.remove("PermanentAddressID");
				mObject.put("LocalPermanentAddressID", LocalAddressID);

			} else if (StringUtils.equalsNoCase(mTableName,
					"childrendisabilities")) {

				String LocalChildrenID = getLocalID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("ChildrenID"));
				mObject.remove("ChildrenID");
				mObject.put("LocalChildrenID", LocalChildrenID);

			} else if (StringUtils.equalsNoCase(mTableName, "childrenparents")) {

				String LocalChildrenID = getLocalID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("ChildrenID"));
				mObject.remove("ChildrenID");
				mObject.put("LocalChildrenID", LocalChildrenID);

				String LocalUserID = getLocalID("users", "LocalUserID",
						"UserID", mObject.getString("UserID"));
				mObject.remove("UserID");
				mObject.put("LocalUserID", LocalUserID);

			} else if (StringUtils.equalsNoCase(mTableName, "childrenpictures")) {

				String LocalChildrenID = getLocalID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("ChildrenID"));
				mObject.remove("ChildrenID");
				mObject.put("LocalChildrenID", LocalChildrenID);

			} else if (StringUtils.equalsNoCase(mTableName,
					"instituteplandetails")) {

				String LocalInstitutePlanID = getLocalID("instituteplans",
						"LocalInstitutePlanID", "InstitutePlanID",
						mObject.getString("InstitutePlanID"));
				mObject.remove("InstitutePlanID");
				mObject.put("LocalInstitutePlanID", LocalInstitutePlanID);

				String mDate = mObject.getString("scheduleDate");

				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MM-yyyy HH:mm:ss");
				Date scheduleDate = sdf.parse(mDate);
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				mObject.put("scheduleDate", sdf.format(scheduleDate));

			} else if (StringUtils.equalsNoCase(mTableName, "instituteplans")) {

			} else if (StringUtils.equalsNoCase(mTableName,
					"institutescreeningdetails")) {

				String LocalInstituteScreeningID = getLocalID(
						"institutescreening", "LocalInstituteScreeningID",
						"InstituteScreeningID",
						mObject.getString("InstituteScreeningID"));
				mObject.remove("InstituteScreeningID");
				mObject.put("LocalInstituteScreeningID",
						LocalInstituteScreeningID);

				String LocalInstitutePlanID = getLocalID("InstitutePlans",
						"LocalInstitutePlanID", "InstitutePlanID",
						mObject.getString("InstitutePlanID"));
				mObject.remove("InstitutePlanID");
				mObject.put("LocalInstitutePlanID", LocalInstitutePlanID);

			} else if (StringUtils
					.equalsNoCase(mTableName, "childrenscreening")) {

				String LocalChildrenID = getLocalID("children",
						"LocalChildrenID", "ChildrenID",
						mObject.getString("ChildrenID"));
				mObject.remove("ChildrenID");
				mObject.put("LocalChildrenID", LocalChildrenID);

				String LocalInstituteScreeningDetailID = getLocalID(
						"institutescreeningdetails",
						"LocalInstituteScreeningDetailID",
						"InstituteScreeningDetailID",
						mObject.getString("InstituteScreeningDetailID"));
				mObject.remove("InstituteScreeningDetailID");
				mObject.put("LocalInstituteScreeningDetailID",
						LocalInstituteScreeningDetailID);

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
					|| StringUtils.equalsNoCase(mTableName, "ChildScreeningFH")) {

				String LocalChildrenScreeningID = getLocalID(
						"childrenscreening", "LocalChildrenScreeningID",
						"ChildrenScreeningID",
						mObject.getString("ChildrenScreeningID"));
				mObject.remove("ChildrenScreeningID");
				mObject.put("LocalChildrenScreeningID",
						LocalChildrenScreeningID);

			} else if (StringUtils.equalsNoCase(mTableName,
					"ChildrenScreeningInvestigations")) {

				String LocalChildrenScreeningID = getLocalID(
						"childrenscreening", "LocalChildrenScreeningID",
						"ChildrenScreeningID",
						mObject.getString("ChildrenScreeningID"));
				mObject.remove("ChildrenScreeningID");
				mObject.put("LocalChildrenScreeningID",
						LocalChildrenScreeningID);

				String LocalChildrenScreeningReferralID = getLocalID(
						"ChildrenScreeningReferrals",
						"LocalChildrenScreeningReferralID",
						"ChildrenScreeningReferralID",
						mObject.getString("ChildrenScreeningReferralID"));
				mObject.remove("ChildrenScreeningReferralID");
				mObject.put("LocalChildrenScreeningReferralID",
						LocalChildrenScreeningReferralID);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mObject;
	}

	public String getLocalID(String mServerID,
			ObjectTableModel mObjectTableModel) {

		if (TextUtils.isEmpty(mObjectTableModel.getObjectLocalKey()))
			return null;
		String query = "Select " + mObjectTableModel.getObjectLocalKey()
				+ " from " + mObjectTableModel.getObjectName() + " where "
				+ mObjectTableModel.getObjectKey() + "='" + mServerID + "'";
		DBHelper mDbHelper = DBHelper.getInstance(mContext);
		Cursor mCursor = mDbHelper.getCursorData(mContext, query);

		if (mCursor == null)
			return null;
		mCursor.moveToFirst();
		return mCursor.getString(0);
	}

	public String getLocalID(String TableName, String mLocalKeyName,
			String mServerKey, String mServerValue) {

		if (mHashMapTable.containsKey(TableName)) {
			String LocalID = mHashMapTable.get(TableName).get(mServerValue);
			if (!TextUtils.isEmpty(LocalID))
				return LocalID;
		}
		String query = "Select " + mLocalKeyName + " from " + TableName
				+ " where " + mServerKey + "='" + mServerValue + "'";
		DBHelper mDbHelper = DBHelper.getInstance(mContext);
		Cursor mCursor = mDbHelper.getCursorData(mContext, query);

		if (mCursor == null)
			return null;
		mCursor.moveToFirst();
		return mCursor.getString(0);
	}
}
