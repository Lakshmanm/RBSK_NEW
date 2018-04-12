//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.info.child;

import java.util.ArrayList;

import nunet.adapter.CustomDisablityAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.ChildrenDisabilities;
import nunet.rbsk.model.DisabilityTypes;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  EditStuDisability.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  Apr 29, 2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations
//  3.0			08-05-2015			Promodh						Missing comments for methods
//3.0			30-05-2015			Deepika						No Comments
//*****************************************************************************
public class EditStuDisability extends Fragment implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tv_editstu_family_details;
	private TextView tv_editstu_disability;
	private Button btn_edit_student_disability_next;
	private EditText et_edit_student_disability_pwd_uid;
	private Spinner spn_edit_student_disability_type;
	private EditText et_edit_student_disability_percentage;
	private EditText et_edit_student_disability_specific_condition;
	private Fragment fragment = null;
	private Cursor cur;
	private ImageView iv_edit_student_disability_add_more;
	private CheckBox cb_disability_has_disability;
	private ArrayList<DisabilityTypes> disabilityAryList;
	private ArrayList<ChildrenDisabilities> disabilityAddMoreList;
	CustomDisablityAdapter disabilityAdapter;
	private ListView lv_edit_student_disability_list;
	private DBHelper dbh;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.editstu_disability,
				container, false);
		dbh = DBHelper.getInstance(this.getActivity());
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		disabilityAddMoreList = new ArrayList<ChildrenDisabilities>();
		findViews(rootView);
		et_edit_student_disability_pwd_uid.setEnabled(false);
		et_edit_student_disability_pwd_uid.setFocusable(false);
		et_edit_student_disability_percentage.setEnabled(false);
		et_edit_student_disability_percentage.setFocusable(false);
		et_edit_student_disability_specific_condition.setEnabled(false);
		et_edit_student_disability_specific_condition.setFocusable(false);
		spn_edit_student_disability_type.setEnabled(false);
		spn_edit_student_disability_type.setClickable(false);
		
		if (Helper.childrenObject == null) {
			Helper.childrenObject = new Children();
		} else {
			et_edit_student_disability_pwd_uid.setText(Helper.childrenObject
					.getPWDCardNumber());
			if (Helper.childrenObject.getHasDisability() == 1) {
				cb_disability_has_disability.setChecked(true);
			} else {
				cb_disability_has_disability.setChecked(false);
			}
			if (Helper.childrenObject.getChildDisabilities() != null) {
				disabilityAddMoreList = Helper.childrenObject
						.getChildDisabilities();
				disabilityAdapter = new CustomDisablityAdapter(
						this.getActivity(), disabilityAddMoreList);
				lv_edit_student_disability_list.setAdapter(disabilityAdapter);
				et_edit_student_disability_pwd_uid.setEnabled(true);
				et_edit_student_disability_pwd_uid.setFocusable(true);
				et_edit_student_disability_percentage.setEnabled(true);
				et_edit_student_disability_percentage.setFocusable(true);
				et_edit_student_disability_specific_condition.setEnabled(true);
				et_edit_student_disability_specific_condition
						.setFocusable(true);
				spn_edit_student_disability_type.setEnabled(true);
				spn_edit_student_disability_type.setClickable(true);
			}

			// et_edit_student_disability_percentage.setText(Helper.childrenObject
			// .getDisabilityPercentage());
			// et_edit_student_disability_specific_condition
			// .setText(Helper.childrenObject.getSpecificCondition());
			// spn_edit_student_disability_type.setSelection(Helper.childrenObject
			// .getDisabilityTypeID());
		}

		return rootView;

	}

	/**
	 * to get id's from R.java
	 */
	private void findViews(View rootView) {
		
		iv_edit_student_disability_add_more = (ImageView) rootView
				.findViewById(R.id.iv_edit_student_disability_add_more);
		iv_edit_student_disability_add_more.setOnClickListener(this);
		btn_edit_student_disability_next = (Button) rootView
				.findViewById(R.id.btn_edit_student_disability_next);
		et_edit_student_disability_pwd_uid = (EditText) rootView
				.findViewById(R.id.et_edit_student_disability_pwd_uid);
		spn_edit_student_disability_type = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_disability_type);
		et_edit_student_disability_percentage = (EditText) rootView
				.findViewById(R.id.et_edit_student_disability_percentage);
		et_edit_student_disability_specific_condition = (EditText) rootView
				.findViewById(R.id.et_edit_student_disability_specific_condition);

		tv_editstu_family_details = (TextView) getActivity().findViewById(
				R.id.tv_editstu_family_details);
		tv_editstu_disability = (TextView) getActivity().findViewById(
				R.id.tv_editstu_disability);

		lv_edit_student_disability_list = (ListView) rootView
				.findViewById(R.id.lv_edit_student_disability_list);
		setListViewHeightBasedOnChildren(lv_edit_student_disability_list);
		lv_edit_student_disability_list
				.setOnTouchListener(new OnTouchListener() {
					// Setting on Touch Listener for handling the touch inside
					// ScrollView
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// Disallow the touch request for parent scroll on touch
						// of
						// child view
						v.getParent().requestDisallowInterceptTouchEvent(true);
						return false;
					}
				});

		btn_edit_student_disability_next.setOnClickListener(this);
		getGenderDataFromDB();
		ArrayAdapter<String> adp_spnDisabilityType = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item);
		adp_spnDisabilityType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < disabilityAryList.size(); i++) {
			adp_spnDisabilityType.add(disabilityAryList.get(i)
					.getDisabilityName());
		}
		spn_edit_student_disability_type.setAdapter(adp_spnDisabilityType);
		cb_disability_has_disability = (CheckBox) rootView
				.findViewById(R.id.cb_disability_has_disability);
		cb_disability_has_disability.setOnCheckedChangeListener(this);
	}

	/****
	 * Method for Setting the Height of the ListView dynamically. Hack to fix
	 * the issue of not showing all the items of the ListView when placed inside
	 * a ScrollView
	 ****/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * get gender data from DB
	 */
	private void getGenderDataFromDB() {
		
		String query = "select * from disabilitytypes DT Where  DT.IsDeleted!=1 ";
		cur = dbh.getCursorData(this.getActivity(), query);
		DisabilityTypes disableObj;
		disabilityAryList = new ArrayList<DisabilityTypes>();
		disableObj = new DisabilityTypes();
		disableObj.setDisabilityTypeID(0);
		disableObj.setDisabilityName("--Select--");
		disabilityAryList.add(disableObj);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {
					do {
						disableObj = new DisabilityTypes();
						disableObj.setDisabilityTypeID(NumUtil.IntegerParse
								.parseInt(cur.getString(cur
										.getColumnIndex("DisabilityTypeID"))));
						disableObj.setDisabilityName(cur.getString(cur
								.getColumnIndex("DisplayText")));

						disabilityAryList.add(disableObj);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}
	}

	/**
	 * click event for views
	 */
	@Override
	public void onClick(View v) {
		int errorCount = 0;

		if (cb_disability_has_disability.isChecked()) {

			if (et_edit_student_disability_pwd_uid.isEnabled() == true
					&& TextUtils.isEmpty(et_edit_student_disability_pwd_uid
							.getText().toString().trim())) {
				errorCount++;
				Helper.displayErrorMsg(et_edit_student_disability_pwd_uid,
						"PWD/UID card no is mandatory");
			}

			if (v == iv_edit_student_disability_add_more) {

				if (spn_edit_student_disability_type.getSelectedItemPosition() == 0) {
					errorCount++;
					Helper.setErrorForSpinner(spn_edit_student_disability_type,
							"Select a Disability Type");
				} else if (TextUtils
						.isEmpty(et_edit_student_disability_percentage
								.getText().toString().trim())) {
					errorCount++;
					Helper.displayErrorMsg(
							et_edit_student_disability_percentage,
							"Disability percentage is mandatory");
				} else if (NumUtil.IntegerParse
						.parseInt(et_edit_student_disability_percentage
								.getText().toString().trim()) > 100) {
					errorCount++;
					Helper.displayErrorMsg(
							et_edit_student_disability_percentage,
							"Percentage cannot be more than 100");
				} else if (TextUtils
						.isEmpty(et_edit_student_disability_specific_condition
								.getText().toString().trim())) {
					errorCount++;
					Helper.displayErrorMsg(
							et_edit_student_disability_specific_condition,
							"Specific condition is mandatory");
				}

				if (et_edit_student_disability_pwd_uid.isEnabled() == true
						&& TextUtils.isEmpty(et_edit_student_disability_pwd_uid
								.getText().toString().trim())) {
					errorCount++;
					Helper.displayErrorMsg(et_edit_student_disability_pwd_uid,
							"PWD/UID card no is mandatory");
				}

				if (errorCount == 0) {
					ChildrenDisabilities childDisability = new ChildrenDisabilities();
					DisabilityTypes disablity = new DisabilityTypes();
					Helper.childrenObject
							.setPWDCardNumber(et_edit_student_disability_pwd_uid
									.getText().toString().trim());
					Helper.childrenObject.setHasDisability(1);
					childDisability
							.setDisabilityPercentage(et_edit_student_disability_percentage
									.getText().toString().trim());
					disablity.setDisabilityName(disabilityAryList.get(
							spn_edit_student_disability_type
									.getSelectedItemPosition())
							.getDisabilityName());
					disablity.setDisabilityTypeID(disabilityAryList.get(
							spn_edit_student_disability_type
									.getSelectedItemPosition())
							.getDisabilityTypeID());
					childDisability.setDisabilityType(disablity);

					childDisability
							.setSpecificCondition(et_edit_student_disability_specific_condition
									.getText().toString().trim());
					disabilityAddMoreList.add(childDisability);
					disabilityAdapter = new CustomDisablityAdapter(
							this.getActivity(), disabilityAddMoreList);

					lv_edit_student_disability_list
							.setAdapter(disabilityAdapter);

					et_edit_student_disability_percentage.setText("");
					spn_edit_student_disability_type.setSelection(0);
					et_edit_student_disability_specific_condition.setText("");
				}
			} else if (v == btn_edit_student_disability_next) {
				// if (et_edit_student_disability_pwd_uid.getText().toString()
				// .trim().equals("")) {
				// errorCount++;
				// Helper.displayErrorMsg(et_edit_student_disability_pwd_uid,
				// "PWD/UID card number is mandatory");
				// }
				if (disabilityAddMoreList.size() == 0) {
					errorCount++;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							this.getActivity());
					builder.setMessage("Please enter disability details")
							.setCancelable(false)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});

					AlertDialog alert = builder.create();
					alert.show();
					// Helper.showShortToast(this,
					// "Add any disability if any or disable Has Disability")}
				}
				if (errorCount == 0) {
					AddStudentActivityDialog.tabFlags[1] = true;
					AddStudentActivityDialog
							.enableHeaderClick1(AddStudentActivityDialog.tabFlags);
					Helper.childrenObject
							.setChildDisabilities(disabilityAddMoreList);
					Helper.updateHeaderFromNext(getActivity(),
							tv_editstu_disability, tv_editstu_family_details,
							R.drawable.headerbg, R.drawable.headerbg_selectced);

					if (AddStudentActivityDialog.fragmentArr[2] == null)
						AddStudentActivityDialog.fragmentArr[2] = fragment = new EditStuFamilyDetails();
					else
						fragment = AddStudentActivityDialog.fragmentArr[2];

					if (fragment != null) {
						android.app.FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.commitAllowingStateLoss();

					}
				}
				final InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			}
		} else {
			if (v == btn_edit_student_disability_next) {
				Helper.childrenObject.setHasDisability(0);
				Helper.updateHeaderFromNext(getActivity(),
						tv_editstu_disability, tv_editstu_family_details,
						R.drawable.headerbg, R.drawable.headerbg_selectced);
				if (AddStudentActivityDialog.fragmentArr[2] == null)
					AddStudentActivityDialog.fragmentArr[2] = fragment = new EditStuFamilyDetails();
				else
					fragment = AddStudentActivityDialog.fragmentArr[2];

				if (fragment != null) {
					android.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commitAllowingStateLoss();

				}
				final InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			}
		}
	}

	/*
	 * check listener for disablity check box
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if (isChecked) {
			et_edit_student_disability_pwd_uid.setEnabled(true);
			et_edit_student_disability_pwd_uid.setFocusable(true);
			et_edit_student_disability_pwd_uid.setFocusableInTouchMode(true);
			et_edit_student_disability_percentage.setEnabled(true);
			et_edit_student_disability_percentage.setFocusable(true);
			et_edit_student_disability_percentage.setFocusableInTouchMode(true);
			et_edit_student_disability_specific_condition.setEnabled(true);
			et_edit_student_disability_specific_condition.setFocusable(true);
			et_edit_student_disability_specific_condition
					.setFocusableInTouchMode(true);
			spn_edit_student_disability_type.setEnabled(true);
			spn_edit_student_disability_type.setClickable(true);
		} else {
			et_edit_student_disability_pwd_uid.setEnabled(false);
			et_edit_student_disability_pwd_uid.setFocusable(false);
			et_edit_student_disability_percentage.setEnabled(false);
			et_edit_student_disability_percentage.setFocusable(false);
			et_edit_student_disability_specific_condition.setEnabled(false);
			et_edit_student_disability_specific_condition.setFocusable(false);
			spn_edit_student_disability_type.setEnabled(false);
			spn_edit_student_disability_type.setClickable(false);
		}
	}
}