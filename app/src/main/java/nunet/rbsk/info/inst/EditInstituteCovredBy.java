package nunet.rbsk.info.inst;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  EditInstituteCovredBy

//* Type    : Frgament

//* Description     : To add covered by of an Institute
//* References     :                                                        
//* Author    :Deepika.chevvakula

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

public class EditInstituteCovredBy extends Fragment implements OnClickListener {
	private Button btn_edit_institute_covered_previous;
	private Button btn_edit_institute_covered_next;
	private TextView tv_editInst_staff;
	private TextView tv_editInst_coveredby;
	// private String[] data_title = new String[] { "NakkavaniPlaem-CHNC",
	// "NakkavaniPlaem-CHN", "Udrajavaram-PHC", "Jaggayyapeta-SubCenter" };
	// private String[] data = new String[] {
	// "36-19-871/1,Opp.Gowri Devi Temple,Chugulla,Choodavaram,Nakkavanipalem,Vizayanagaram,Andhrapradesh,530032",
	// "36-19-871/1,Opp.Gowri Devi Temple,Chugulla,Choodavaram,Nakkavanipalem,Vizayanagaram,Andhrapradesh,530032",
	// "36-19-871/1,Near Police Statation,Choodavaram,Nakkavanipalem,Vizayanagaram,Andhrapradesh,530032",
	// "36-19-871/1,Sujanapuram,Chugulla,Choodavaram,Nakkavanipalem,Vizayanagaram,Andhrapradesh,530032"
	// };
	private String[] data_title, data;
	private LinearLayout ll_coveredByData;
	private ToggleButton[] togbtn_coveredby_down;
	private TextView[] tv_coveredby_address;
	private DBHelper dbh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.editinst_coveredby,
				container, false);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dbh = DBHelper.getInstance(this.getActivity());
		findViews(rootView);
		getFacilitiesDataFromDB();

		btn_edit_institute_covered_previous.setOnClickListener(this);
		btn_edit_institute_covered_next.setOnClickListener(this);
		return rootView;
	}

	private void getFacilitiesDataFromDB() {
		String query = "select i.InstituteID,a.MandalID,fc.FacilityID,f.DisplayText as FacilityName,f.FacilityTypeID,ft.DisplayText as FacilityTypecode ,"
				+ "s.DisplayText as StateName,d.DisplayText as DistrictName,m.DisplayText as MandalName,p.DisplayText as PanchayatName,"
				+ "v.DisplayText as VillageName,h.DisplayText as HabitatName,ad.AddressLine1,ad.AddressLine2,ad.AddressName,ad.LandMark ,ad.PINCode ,ad.Post"
				+ " from Institutes i inner join Address a on i.LocalAddressID = a.LocalAddressID inner join FacilityCoverage fc on (fc.MandalID = a.MandalID or fc.VillageID = a.VillageID) and fc.DoesCover = 1 "
				+ "inner join Facilities f on fc.FacilityID = f.FacilityID inner join FacilityTypes ft on ft.FacilityTypeID = f.FacilityTypeID"
				+ " left join Address ad on ad.LocalAddressID = f.LocalAddressID left join States s on s.StateID = ad.StateID"
				+ " left join Districts d on d.DistrictID = ad.DistrictID left join Mandals m on m.MandalID =  ad.MandalID "
				+ "left join Panchayats p on p.PanchayatID = ad.PanchayatID left join Villages v on v.VillageID = ad.VillageID"
				+ " left join habitatas h on h.HabitationID = ad.HabitationID where f.IsDeleted!=1 AND fc.DoesCover=1 AND i.InstituteID = "
				+ +((InsituteFragmentActivityDialog) getActivity()).selectedInstituteId
				+ "";
		Cursor cursor = dbh.getCursorData(this.getActivity(), query);
		if (cursor != null) {
			data_title = new String[cursor.getCount()];
			data = new String[cursor.getCount()];
			int count = 0;
			while (cursor.moveToNext()) {
				String addressStr = "";
				String facilityName = cursor.getString(cursor
						.getColumnIndex("FacilityName"));
				data_title[count] = facilityName;

				if (cursor.getString(cursor.getColumnIndex("AddressLine1")) != null) {
					String addressline1 = cursor.getString(cursor
							.getColumnIndex("AddressLine1"));
					addressStr += addressline1 + ",";

				}
				if (cursor.getString(cursor.getColumnIndex("AddressLine2")) != null) {
					String addressline2 = cursor.getString(cursor
							.getColumnIndex("AddressLine2"));
					addressStr += addressline2 + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("AddressName")) != null) {
					String AddressName = cursor.getString(cursor
							.getColumnIndex("AddressName"));
					addressStr += AddressName + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("LandMark")) != null) {
					String LandMark = cursor.getString(cursor
							.getColumnIndex("LandMark"));
					addressStr += LandMark + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("HabitatName")) != null) {
					String HabitatName = cursor.getString(cursor
							.getColumnIndex("HabitatName"));
					addressStr += HabitatName + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("VillageName")) != null) {
					String VillageName = cursor.getString(cursor
							.getColumnIndex("VillageName"));
					addressStr += VillageName + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("PanchayatName")) != null) {
					String PanchayatName = cursor.getString(cursor
							.getColumnIndex("PanchayatName"));
					addressStr += PanchayatName + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("MandalName")) != null) {
					String MandalName = cursor.getString(cursor
							.getColumnIndex("MandalName"));
					addressStr += MandalName + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("DistrictName")) != null) {
					String DistrictName = cursor.getString(cursor
							.getColumnIndex("DistrictName"));
					addressStr += DistrictName + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("StateName")) != null) {
					String StateName = cursor.getString(cursor
							.getColumnIndex("StateName"));
					addressStr += StateName + ",";
				}
				if (cursor.getString(cursor.getColumnIndex("PINCode")) != null) {
					String PINCode = cursor.getString(cursor
							.getColumnIndex("PINCode"));
					addressStr += PINCode + ",";
				}

				if (addressStr.endsWith(",")) {
					data[count] = addressStr.substring(0,
							addressStr.lastIndexOf(","));
				} else {
					data[count] = addressStr;
				}
				count++;
			}
			cursor.close();
			getUpdateView();
		}
	}

	/**
	 * Update the data to views
	 */
	public void getUpdateView() {
		ll_coveredByData.removeAllViews();
		togbtn_coveredby_down = new ToggleButton[data.length];
		tv_coveredby_address = new TextView[data.length];
		for (int i = 0; i < data.length; i++) {
			LayoutInflater mInflater = LayoutInflater.from(this.getActivity());
			View mCustomView = mInflater.inflate(R.layout.item_coveredby, null);
			TextView tv_coveredby_Name = (TextView) mCustomView
					.findViewById(R.id.tv_coveredby_Name);
			tv_coveredby_address[i] = (TextView) mCustomView
					.findViewById(R.id.tv_coveredby_address);
			togbtn_coveredby_down[i] = (ToggleButton) mCustomView
					.findViewById(R.id.togbtn_coveredby_down);

			tv_coveredby_Name.setText(data_title[i]);
			tv_coveredby_address[i].setText(data[i]);
			tv_coveredby_address[i].setId(i);
			togbtn_coveredby_down[i].setId(i);
			togbtn_coveredby_down[i].setText(null);
			togbtn_coveredby_down[i].setTextOn(null);
			togbtn_coveredby_down[i].setTextOff(null);
			togbtn_coveredby_down[i]
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int id = buttonView.getId();
							if (isChecked) {
								togbtn_coveredby_down[id]
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.infogreen));
								tv_coveredby_address[id].setSingleLine(false);
								tv_coveredby_address[id].setEllipsize(null);
							} else {
								togbtn_coveredby_down[id]
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.ic_action_info));

								tv_coveredby_address[id].setSingleLine(true);
								tv_coveredby_address[id]
										.setEllipsize(TruncateAt.END);
							}
						}
					});
			ll_coveredByData.addView(mCustomView);
		}
	}

	/**
	 * To get the view id's from R.java
	 * 
	 * @param rootView
	 */
	private void findViews(View rootView) {
		btn_edit_institute_covered_previous = (Button) rootView
				.findViewById(R.id.btn_edit_institute_covered_previous);
		btn_edit_institute_covered_next = (Button) rootView
				.findViewById(R.id.btn_edit_institute_covered_next);
		tv_editInst_staff = (TextView) getActivity().findViewById(
				R.id.tv_editInst_staff);
		tv_editInst_coveredby = (TextView) getActivity().findViewById(
				R.id.tv_editInst_coveredby);
		ll_coveredByData = (LinearLayout) rootView
				.findViewById(R.id.ll_coveredByData);
	}

	/**
	 * onclick listener for the views
	 */
	@Override
	public void onClick(View v) {
		InsituteFragmentActivityDialog activity = (InsituteFragmentActivityDialog) getActivity();

		if (v == btn_edit_institute_covered_previous) {
			Helper.updateHeaderFromNext(getActivity(), tv_editInst_coveredby,
					tv_editInst_staff, R.drawable.headerbg,
					R.drawable.headerbg_selectced);
			activity.replaceFragment(activity.fragmentArr[2]);
		} else if (v == btn_edit_institute_covered_next) {
			this.getActivity().finish();
		}
	}
}
