//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening.cv;
import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  ScreenVitalBlood.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  May 29, 2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations
//3.0			08/06/2015			Anil Kumar Botsa		No Comments
//*****************************************************************************
public class ScreenVitalBlood extends DialogFragment implements OnClickListener {

	private Spinner spn_screen_vital_blood_group;
	private EditText et_screen_vitals_blood_group_comments;
	private Button btn_screen_vitals_blood_group_save;
	private Button btn_screen_vitals_blood_group_cancel;
	private ArrayList<String> bloodgroup;
	private ArrayAdapter<String> bgadapter;
	private static Cursor cur;
	public float heightDB;
	public String type_ofinstitute;
	public int genderId;
	private int instType;
	private DBHelper dbh;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		View view = inflater.inflate(R.layout.screening_vitals_bloodgroup,
				container);
		dbh = DBHelper.getInstance(this.getActivity());
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if (instType == 1) {
			type_ofinstitute = "awc";
		} else {
			type_ofinstitute = "school";
		}
		genderId = Helper.childrenObject.getGender().getGenderID();

		bloodgroup = new ArrayList<String>();
		bloodgroup = getBloodGroupFromDb();
		bgadapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, bloodgroup);
		bgadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		findViews(view);
		int position = ScreeningVitalsFragment.vitalsObj.getBloodGroupId();
		spn_screen_vital_blood_group.setSelection(position);
		if (StringUtils.equalsNoCase(ScreeningVitalsFragment.vitalsObj.getBloodGroupNotes(),
				"enter")) {
		} else {
			et_screen_vitals_blood_group_comments
					.setText(ScreeningVitalsFragment.vitalsObj
							.getBloodGroupNotes());
		}
		/*
		 * getWindow().setSoftInputMode(
		 * WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		 */

		return view;
	}

	/**
	 * @return
	 */
	// *** returns an arraylist of BloodGroupCode from DB
	private ArrayList<String> getBloodGroupFromDb() {
		ArrayList<String> bgroup = new ArrayList<String>();
		bgroup.add("Select");
		String relQuery = "Select DisplayText from bloodgroups where IsDeleted!=1  ";
		cur = dbh.getCursorData(getActivity(), relQuery);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {

					do {

						bgroup.add(cur.getString(cur
								.getColumnIndex("DisplayText")));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return bgroup;
	}

	/**
	 * @param view
	 * 
	 */
	private void findViews(View view) {
		spn_screen_vital_blood_group = (Spinner) view
				.findViewById(R.id.spn_screen_vital_blood_group);
		spn_screen_vital_blood_group.setAdapter(bgadapter);
		et_screen_vitals_blood_group_comments = (EditText) view
				.findViewById(R.id.et_screen_vitals_blood_group_comments);
		btn_screen_vitals_blood_group_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_blood_group_save);
		btn_screen_vitals_blood_group_save.setOnClickListener(this);
		btn_screen_vitals_blood_group_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_blood_group_cancel);
		btn_screen_vitals_blood_group_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_screen_vitals_blood_group_save) {
			String blood_group = spn_screen_vital_blood_group.getSelectedItem()
					.toString();
			ScreeningVitalsFragment.vitalsObj
					.setBloodGroupNotes(et_screen_vitals_blood_group_comments
							.getText().toString().trim());
			ScreeningVitalsFragment.setToTextView(blood_group, "blood",
					this.getActivity());
			final InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		} else if (v == btn_screen_vitals_blood_group_cancel) {
			// getActivity().finish();
			dismiss();
		}
	}

}
