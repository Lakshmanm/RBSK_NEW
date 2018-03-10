package nunet.rbsk.info.inst;

import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.helpers.CustomDialog;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Address;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.Institute;
import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  EditInstBasicInfo

//* Type    : Frgament

//* Description     : To add basic info of an Institute
//* References     :
//* Author    :Promodh.munjeti

//* Created Date       : 23-04-2015
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

public class EditInstBasicInfo extends Fragment implements OnClickListener {

	private Button btn_edit_institute_basic_info_view_gallery;
	private Button btn_edit_institute_basic_next;
	private EditText et_edit_institute_basic_info_name;
	private EditText et_edit_institute_basic_info_dise_code;
	private EditText et_edit_institute_basic_info_institute_type;
	private EditText et_edit_institute_basic_info_institute_category;
	private EditText et_edit_institute_basic_info_school_category;
	private EditText et_edit_institute_basic_contact;
	private EditText et_edit_institute_basic_info_contact_no;
	private EditText et_edit_institute_basic_email;
	private Cursor cur;
	private DBHelper dbh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.editinst_basicinfo,
				container, false);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dbh = DBHelper.getInstance(this.getActivity());
		findViews(rootView);
		Helper.insModelObject = new Institute();
		Helper.addModelObject = new Address();
		Helper.contactModelObject = new ArrayList<Contacts>();
		getBasicInfoFromDB();
		btn_edit_institute_basic_info_view_gallery.setOnClickListener(this);
		btn_edit_institute_basic_next.setOnClickListener(this);

		return rootView;
	}

	/**
	 * Method to get All basic info from DB Kiruthika 22/04/2015
	 */
	private void getBasicInfoFromDB() {

		new AsyncTask<Void, Void, Void>() {

			CustomDialog mCustomDialog = new CustomDialog(getActivity());

			protected void onPreExecute() {
				mCustomDialog.setCancelable(false);
				mCustomDialog.show();
			};

			@Override
			protected Void doInBackground(Void... params) {
				String query = "select institutes.LocalInstituteID,institutes.InstituteID,institutes.InstituteName,institutes.InstituteTypeID,"
						+ " institutetypes.DisplayText as institutetypeName, institutes.DiseCode, "
						+ " institutes.SchoolCategoryID,schoolcategories.DisplayText as schoolcategoryName, institutes.InstituteCategoryID , institutecategories.DisplayText as institutecategoryName,"
						+ " institutes.LocalContactID,institutes.Latitude,institutes.Longitude, "
						+ " address.AddressName,address.LocalAddressID,address.AddressLine1,address.AddressLine2,address.LandMark,address.PINCode,"
						+ " address.Post,address.HabitationID,habitatas.DisplayText as habitationName,address.VillageID,villages.DisplayText as villageName, "
						+ " address.PanchayatID,panchayats.DisplayText as panchayatName,address.MandalID,mandals.DisplayText as mandalName,address.DistrictID,"
						+ " districts.DisplayText districtName,address.StateID,states.DisplayText as stateName"
						+ " from institutes inner join institutetypes on institutes.InstituteTypeID=institutetypes.InstituteTypeID"
						+ " inner join schoolcategories on institutes.SchoolCategoryID=schoolcategories.SchoolCategoryID"
						+ " left join address on address.LocalAddressID=institutes.LocalAddressID"
						+ " left join habitatas on address.HabitationID=habitatas.HabitationID"
						+ " left join villages on villages.VillageID=address.VillageID "
						+ " left join panchayats on panchayats.PanchayatID=address.PanchayatID "
						+ " left join mandals on mandals.MandalID=address.MandalID "
						+ " left join districts on districts.DistrictID=address.DistrictID "
						+ " left join states on states.StateID=address.StateID "
						+ " inner join institutecategories on institutecategories.InstituteCategoryID = "
						+ " institutes.InstituteCategoryID where  institutes.IsDeleted!=1 "
						+
						// " AND address.IsDeleted!=1 " +//address change to
						// left join
						" AND  institutes.InstituteID="
						+ +((InsituteFragmentActivityDialog) getActivity()).selectedInstituteId
						+ " Limit 1";
				cur = dbh.getCursorData(getActivity(), query);
				setToInstituteModel(cur);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				setTextToBasicInfo();
				mCustomDialog.cancel();

			}
		}.execute();

	}

	/**
	 * Method to Set data from db to Institute Model class kiruthika 22/04/2015
	 *
	 * @param instituteCursor
	 *            from DB
	 *
	 */
	private void setToInstituteModel(Cursor insCur) {
		if (insCur != null) {
			try {
				while (insCur.moveToNext()) {
					Helper.insModelObject
							.setInstituteServerID(NumUtil.IntegerParse
									.parseInt(insCur.getString(insCur
											.getColumnIndex("InstituteID"))));
					Helper.insModelObject.setInstituteName(insCur
							.getString(insCur.getColumnIndex("InstituteName")));
					Helper.insModelObject
							.setInstituteTypeId(NumUtil.IntegerParse.parseInt(insCur
									.getString(insCur
											.getColumnIndex("InstituteTypeId"))));
					Helper.insModelObject.setInstituteTypeName(insCur
							.getString(insCur
									.getColumnIndex("institutetypeName")));

					Helper.insModelObject.setDiseCode(insCur.getString(insCur
							.getColumnIndex("DiseCode")));
					Helper.insModelObject
							.setSchoolCategoryID(NumUtil.IntegerParse.parseInt(insCur.getString(insCur
									.getColumnIndex("SchoolCategoryID"))));
					Helper.insModelObject.setCategoryCode(insCur
							.getString(insCur
									.getColumnIndex("institutecategoryName")));
					Helper.insModelObject
							.setInstituteCategoryID(NumUtil.IntegerParse.parseInt(insCur.getString(insCur
									.getColumnIndex("InstituteCategoryID"))));
					Helper.insModelObject.setInstituteCategoryName(insCur
							.getString(insCur
									.getColumnIndex("schoolcategoryName")));
					String LocalContactID = insCur.getString(insCur
							.getColumnIndex("LocalContactID"));
					if (!TextUtils.isEmpty(LocalContactID)) {
						Helper.insModelObject.setContactID(NumUtil.IntegerParse
								.parseInt(LocalContactID.trim()));
					}
					// Helper.insModelObject
					// .setContactID(IntUtil.Integer.parseInt(insCur
					// .getString(insCur
					// .getColumnIndex("LocalContactID"))));
					Helper.insModelObject.setLatitude(insCur.getString(insCur
							.getColumnIndex("Latitude")));
					Helper.insModelObject.setLongitude(insCur.getString(insCur
							.getColumnIndex("Longitude")));
					Helper.addModelObject = Helper.getAllAddress(insCur);
					Helper.contactModelObject = getDbContacts(Helper.insModelObject
							.getContactID());
					// contactModelObject=Helper.getAllContacts(Helper.insModelObject.getContactID());
					Helper.insModelObject.setAddress(Helper.addModelObject);
					Helper.insModelObject
							.setContacts(Helper.contactModelObject);

				}
			} finally {
				insCur.close();
			}
		}

	}

	/**
	 * Method to get contact details from contact table based on the contact id
	 * from institute table
	 *
	 * @param contactID
	 * @return
	 */
	private ArrayList<Contacts> getDbContacts(long contactID) {
		String contect = null;
		ArrayList<Contacts> dbContacts = new ArrayList<Contacts>();
		String contact_query = "Select ct.ContactTypeID,cd.Contact,cd.LocalContactID,DisplayText  from contactdetails cd "
				+ " left join contacttypes ct on ct.contacttypeid =cd.contacttypeid  where cd.IsDeleted!=1 AND cd.localcontactid="
				+ contactID;
		Cursor contactCur = dbh
				.getCursorData(this.getActivity(), contact_query);
		if (contactCur != null) {
			try {
				if (contactCur.moveToFirst()) {
					do {
						Contacts contactUser = new Contacts();
						contactUser.setContactTypeID(NumUtil.IntegerParse
								.parseInt(contactCur.getString(contactCur
										.getColumnIndex("ContactTypeID"))));
						contect = contactCur.getString(contactCur
								.getColumnIndex("Contact"));
						contactUser.setContact(contect);
						contactUser.setContactID(NumUtil.IntegerParse
								.parseInt(contactCur.getString(contactCur
										.getColumnIndex("LocalContactID"))));
						contactUser.setContactCategoryID(3);
						contactUser.setContactCategoryName(contactCur
								.getString(contactCur
										.getColumnIndex("DisplayText")));
						dbContacts.add(contactUser);
					} while (contactCur.moveToNext());

				}
			} finally {
				contactCur.close();
			}

			return dbContacts;
		} else {
			return null;
		}
	}

	/**
	 * To set data to views from the models
	 */
	private void setTextToBasicInfo() {
		et_edit_institute_basic_info_name.setText(Helper.insModelObject
				.getInstituteName());

		TextView tv_editInst_instName;

		tv_editInst_instName = (TextView) getActivity().findViewById(
				R.id.tv_editInst_instName);

		Institute insModelObject = Helper.insModelObject;
		if (insModelObject != null) {
			tv_editInst_instName.setText(insModelObject.getInstituteName());
		}

		et_edit_institute_basic_info_dise_code.setText(Helper.insModelObject
				.getDiseCode());
		et_edit_institute_basic_info_institute_type
				.setText(Helper.insModelObject.getInstituteTypeName());
		et_edit_institute_basic_info_institute_category
				.setText(Helper.insModelObject.getInstituteCategoryName());
		et_edit_institute_basic_info_school_category
				.setText(Helper.insModelObject.getCategoryCode());
		if (Helper.contactModelObject != null) {

			for (int i = 0; i < Helper.contactModelObject.size(); i++) {
				if (Helper.contactModelObject.get(i).getContactTypeID() != 1) {
					et_edit_institute_basic_contact
							.setText(Helper.contactModelObject.get(0)
									.getContactCategoryName());
					et_edit_institute_basic_info_contact_no
							.setText(Helper.contactModelObject.get(0)
									.getContact());
				} else {
					et_edit_institute_basic_email
							.setText(Helper.contactModelObject.get(1)
									.getContact());
				}
			}

		}
	}

	/**
	 *
	 * To get the view id's from R.java
	 *
	 */
	private void findViews(View rootView) {

		et_edit_institute_basic_info_name = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_info_name);
		et_edit_institute_basic_info_dise_code = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_info_dise_code);
		et_edit_institute_basic_info_institute_type = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_info_institute_type);
		et_edit_institute_basic_info_institute_category = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_info_institute_category);
		et_edit_institute_basic_info_school_category = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_info_school_category);
		et_edit_institute_basic_contact = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_contact);
		et_edit_institute_basic_info_contact_no = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_info_contact_no);
		et_edit_institute_basic_email = (EditText) rootView
				.findViewById(R.id.et_edit_institute_basic_email);
		btn_edit_institute_basic_info_view_gallery = (Button) rootView
				.findViewById(R.id.btn_edit_institute_basic_info_view_gallery);
		btn_edit_institute_basic_next = (Button) rootView
				.findViewById(R.id.btn_edit_institute_basic_next);

	}

	/**
	 * onclick listener for the views
	 */
	@Override
	public void onClick(View v) {
		if (v == btn_edit_institute_basic_info_view_gallery) {
		} else if (v == btn_edit_institute_basic_next) {

			InsituteFragmentActivityDialog insituteFragment = (InsituteFragmentActivityDialog) getActivity();
			insituteFragment.updateHeaderColors(R.drawable.headerbg,
					R.drawable.headerbg_selectced, R.drawable.headerbg,
					R.drawable.headerbg,R.drawable.headerbg_selectced);
			insituteFragment.replaceFragment(insituteFragment.fragmentArr[1]);
		}
	}

}
