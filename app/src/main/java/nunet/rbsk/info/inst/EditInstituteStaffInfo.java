package nunet.rbsk.info.inst;

import java.util.ArrayList;

import nunet.adapter.ExpandableStaffListAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.CustomDialog;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.Institutestaff;
import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  EditInstituteStaffInfo

//* Type    : Frgament

//* Description     : To Edit Staff info of an Institute
//* References     :
//* Author    :Deepika.chevvakula

//* Created Date       : 24-04-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG
//*****************************************************************************
//* Ver        Date                Code Review By            Observations

//*****************************************************************************

public class EditInstituteStaffInfo extends Fragment implements OnClickListener {
	// Button btn_editInst_staff_close;
	private Button btn_edit_institute_staff_previous;
	private Button btn_edit_institute_staff_next;
	private TextView tv_editInst_address;
	private TextView tv_editInst_staff;
	private TextView tv_editInst_coveredby;
	private Cursor staffCur;

	private ExpandableListView stafflistview;

	private ArrayList<Institutestaff> staffListObject;
	private ExpandableStaffListAdapter mAdapter;
	private DBHelper dbh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.editinst_staff, container,
				false);
		dbh = DBHelper.getInstance(this.getActivity());
		findViews(rootView);
		btn_edit_institute_staff_previous.setOnClickListener(this);
		btn_edit_institute_staff_next.setOnClickListener(this);
		stafflistview.setGroupIndicator(null);
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
				ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
				ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.EXACTLY);
		stafflistview.measure(widthMeasureSpec, heightMeasureSpec);

		Helper.contactModelObject = new ArrayList<Contacts>();
		getStaffInfoFromDB();

		stafflistview.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					stafflistview.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});
		return rootView;
	}

	/**
	 * @param rootView
	 */
	private void findViews(View rootView) {
		btn_edit_institute_staff_previous = (Button) rootView
				.findViewById(R.id.btn_edit_institute_staff_previous);
		btn_edit_institute_staff_next = (Button) rootView
				.findViewById(R.id.btn_edit_institute_staff_next);
		tv_editInst_address = (TextView) getActivity().findViewById(
				R.id.tv_editInst_address);
		tv_editInst_staff = (TextView) getActivity().findViewById(
				R.id.tv_editInst_staff);
		tv_editInst_coveredby = (TextView) getActivity().findViewById(
				R.id.tv_editInst_coveredby);

		stafflistview = (ExpandableListView) rootView.findViewById(R.id.list);
	}

	/**
	 * Method to get all Staff details from DB Kiruthika
	 *
	 */
	private void getStaffInfoFromDB() {

		new AsyncTask<Void, Void, Void>() {


			protected void onPreExecute() {
			Helper.showProgressDialog(getActivity());
			};

			@Override
			protected Void doInBackground(Void... params) {
				String query_Wards = "select INS.LocalUserID,U.FirstName, U.MiddleName,U.LastName,U.DesignationID,"
						+ " DS.DisplayText,U.LocalContactID,U.DepartmentID,D.DisplayText,INS.IsHeadMaster,"
						+ " INS.IsHealthCoordinator , INS.IsSchoolHealthCoordinator from institutestaff INS"
						+ " inner join users U on U.LocalUserID=INS.LocalUserID "
						+ " inner join departments D on D.DepartmentID=U.DepartmentID"
						+ " inner join designations DS on U.DesignationID= DS.DesignationID"
						+ " inner join Institutes I on I.LocalInstituteID=INS.LocalInstituteID"
						+ " where  I.IsDeleted!=1 AND  U.IsDeleted!=1 AND   INS.IsDeleted!=1 AND  I.InstituteID="
						+ ((InsituteFragmentActivityDialog) getActivity()).selectedInstituteId;
				staffCur = dbh.getCursorData(getActivity(), query_Wards);
				setToStaffModel(staffCur);
				return null;
			}

			protected void onPostExecute(Void result) {
				mAdapter = new ExpandableStaffListAdapter(getActivity(),
						staffListObject);
				loadParentList(staffListObject);
				Helper.progressDialog.dismiss();
			};
		}.execute();

	}

	/**
	 * Method to set Data from DB to Staff Model class kiruthika 27/04/2015
	 *
	 * @param staffCur2
	 */
	private void setToStaffModel(Cursor staffCur2) {
		if (staffCur2 != null) {
			staffListObject = new ArrayList<Institutestaff>();
			try {
				if (staffCur2.moveToFirst()) {
					do {
						Institutestaff staffObject = new Institutestaff();
						staffObject.setUserID(NumUtil.IntegerParse
								.parseInt(staffCur2.getString(staffCur2
										.getColumnIndex("LocalUserID"))));
						staffObject.setFirstName(staffCur2.getString(staffCur2
								.getColumnIndex("FirstName")));
						staffObject.setMiddleName(staffCur2.getString(staffCur2
								.getColumnIndex("MiddleName")));
						staffObject.setLastName(staffCur2.getString(staffCur2
								.getColumnIndex("LastName")));
						staffObject.setDesignationID(NumUtil.IntegerParse
								.parseInt(staffCur2.getString(staffCur2
										.getColumnIndex("DesignationID"))));

						staffObject.setStaffAssignHeadMaster(staffCur2
								.getInt(staffCur2
										.getColumnIndex("IsHeadMaster")) != 0);

						staffObject
								.setStaffAssignHealthCoord(staffCur2.getInt(staffCur2
										.getColumnIndex("IsHealthCoordinator")) != 0);

						staffObject
								.setStaffAssignSchoolCoord(staffCur2.getInt(staffCur2
										.getColumnIndex("IsSchoolHealthCoordinator")) != 0);

						staffObject.setDesignationName(staffCur2
								.getString(staffCur2
										.getColumnIndex("DisplayText")));
						staffObject.setDepartmentID(NumUtil.IntegerParse
								.parseInt(staffCur2.getString(staffCur2
										.getColumnIndex("DepartmentID"))));
						staffObject.setDepartmentName(staffCur2
								.getString(staffCur2
										.getColumnIndex("DisplayText")));
						if (!TextUtils.isEmpty(staffCur2.getString(staffCur2
								.getColumnIndex("LocalContactID")))) {
							staffObject
									.setContactID(NumUtil.IntegerParse.parseInt(staffCur2.getString(staffCur2
											.getColumnIndex("LocalContactID"))));
						}

						Helper.contactModelObject = getContactsFromDB(staffObject
								.getContactID());
						staffObject.setContacts(Helper.contactModelObject);
						staffListObject.add(staffObject);

					} while (staffCur2.moveToNext());
				}
			} finally {
				staffCur2.close();
			}
		}

	}

	/**
	 * @param staffListObject2
	 */
	private void loadParentList(ArrayList<Institutestaff> staffListObject2) {
		if (staffListObject2 != null) {
			staffListObject = staffListObject2;
			stafflistview.setAdapter(mAdapter);
		}
		if (staffListObject2 == null)
			return;
	}

	/**
	 * @param contactID
	 * @return
	 */
	private ArrayList<Contacts> getContactsFromDB(int contactID) {
		// String query_Contacts =
		// "select contacts.ContactID,contactdetails.contact,"
		// +
		// " contacttypes.ContactTypeID,contacttypes.ContactTypeName,contactcategories.ContactCategoryID,"
		// + " contactcategories.ContactCategoryName from contacts "
		// +
		// " inner join contactdetails on contactdetails.ContactID=contacts.ContactID "
		// +
		// " inner join contacttypes on contacttypes.ContactTypeID=contactdetails.ContactTypeID "
		// +
		// " inner join contactcategories on contactcategories.ContactCategoryID=contacttypes.ContactCategoryID "
		// + " where  contacts.ContactID=" + contactID;

		String query_Contacts = "select contacts.ContactID,contactdetails.contact, contacttypes.ContactTypeID,contacttypes.DisplayText as ContactTypeName,contactcategories.ContactCategoryID, contactcategories.DisplayText  as ContactCategoryName from contacts  inner join contactdetails on contactdetails.LocalContactID=contacts.LocalContactID  inner join contacttypes on contacttypes.ContactTypeID=contactdetails.ContactTypeID  inner join contactcategories on contactcategories.ContactCategoryID=contacttypes.ContactCategoryID "
				+ " where  contacts.IsDeleted!=1 AND  contacts.ContactID="
				+ contactID;

		staffCur = dbh.getCursorData(getActivity(), query_Contacts);
		Helper.contactModelObject = Helper.getAllContacts(staffCur);
		return Helper.contactModelObject;
	}

	@Override
	public void onClick(View v) {
		InsituteFragmentActivityDialog activity = (InsituteFragmentActivityDialog) getActivity();
		if (v == btn_edit_institute_staff_previous) {
			Helper.updateHeaderFromNext(getActivity(), tv_editInst_staff,
					tv_editInst_address, R.drawable.headerbg,
					R.drawable.headerbg_selectced);
			activity.replaceFragment(activity.fragmentArr[1]);
		} else if (v == btn_edit_institute_staff_next) {
			Helper.updateHeaderFromNext(getActivity(), tv_editInst_staff,
					tv_editInst_coveredby, R.drawable.headerbg,
					R.drawable.headerbg_selectced);
			activity.replaceFragment(activity.fragmentArr[3]);
		}
	}
}
