/**
 * 
 */
package nunet.rbsk.info.child;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  EditStuAddress

//* Type    : Fragment

//* Description     : Functionality to add Address for Student
//* References     :                                                         
//* Author    :promodh.munjeti

//* Created Date       : 28-04-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations
//3.0		   01-05-2015	        Self Review(Deepika)		No Observation
//3.0		   02-05-2015 			Kiruthika(Peer review)		Permanent Address Validation.
//3.0		   02-05-2015 			Anil(Peer review)			1.	Please provide appropriate comments to the method in Line 258.
//																2.	Remove the commented code from Line 412.

//*****************************************************************************  

import java.util.ArrayList;

import nunet.adapter.CustomSpinnerAdapter;
import nunet.adapter.DistrictSpinnerAdapter;
import nunet.adapter.HabitationSpinnerAdapter;
import nunet.adapter.MandalSpinnerAdapter;
import nunet.adapter.VillageAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Address;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.District;
import nunet.rbsk.model.Habitation;
import nunet.rbsk.model.Mandal;
import nunet.rbsk.model.Panchayat;
import nunet.rbsk.model.State;
import nunet.rbsk.model.Village;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class EditStuAddress extends Fragment implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tv_editstu_Address;
	private TextView tv_editstu_associate_inst;
	private Spinner spn_edit_student_address_state;
	private Spinner spn_edit_student_address_district;
	private Spinner spn_edit_student_address_mandal;
	private Spinner spn_edit_student_address_village;
	private Spinner spn_edit_student_address_habitation;
	private EditText et_edit_student_address_pincode;
	private EditText et_edit_student_address_address1;
	private EditText et_edit_student_address_address2;
	private Button btn_edit_student_address_next;
	private Spinner spn_edit_student_address_Pstate;
	private Spinner spn_edit_student_address_Pdistrict;
	private Spinner spn_edit_student_address_Pmandal;
	private Spinner spn_edit_student_address_Pvillage;
	private Spinner spn_edit_student_address_Phabitation;
	private EditText et_edit_student_address_Ppincode;
	private EditText et_edit_student_address_Paddress1;
	private EditText et_edit_student_address_Paddress2;
	private int stateID;
	private int districtID;
	private int mandalID;
	private int panchayatID;
	private int villageID;
	private int habitationID;
	private int permanentstateID;
	private int permanentdistrictID;
	private int permanentmandalID;
	private int permanentpanchayatID;
	private int permanentvillageID;
	private int permanenthabitationID;
	private ArrayList<State> stateList;
	private ArrayList<District> districtList;
	private ArrayList<Mandal> mandalList;
	private ArrayList<Village> villageList;
	private ArrayList<Habitation> habitationList;
	private CustomSpinnerAdapter adapter;
	private DistrictSpinnerAdapter disAdapter;
	private MandalSpinnerAdapter mandalAdapter;
	private VillageAdapter villageAdapter;
	private HabitationSpinnerAdapter habitationAdapter;
	private boolean setFlag = false;
	private Fragment fragment = null;
	// private Address address;
	private LinearLayout ll_present_address;
	private CheckBox cb_edit_student_address_permanent_y_n;

	// public CustomDialog addStuDialod;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		/*
		 * addStuDialod = new CustomDialog(getActivity());
		 * addStuDialod.setCancelable(false); addStuDialod.show();
		 */

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.editstu_address, container,
				false);

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		findViews(rootView);
		cb_edit_student_address_permanent_y_n.setChecked(true);

		String permanentAddressID = Helper.childrenObject
				.getPermanentAddressID();
		String localAddressID = Helper.childrenObject.getLocalAddressID();

		if (Helper.childrenObject == null) {
			Helper.childrenObject = new Children();
		}
		// If address model is null
		if (Helper.childrenObject.getAddress() == null) {
			@SuppressWarnings("unused")
			Address childrenAddress = new Address();
			// Helper.childrenObject.setAddress(childrenAddress);
			/*
			 * state = new State(); district = new District(); mandal = new
			 * Mandal(); habitation = new Habitation(); village = new Village();
			 * panchayat = new Panchayat();
			 */} else {
			Address childrenAddress = Helper.childrenObject.getAddress();
			/*
			 * state = address.getState(); district = address.getDistrict();
			 * mandal = address.getMandal(); habitation =
			 * address.getHabitation(); village = address.getVillage();
			 * panchayat = address.getPanchayat();
			 */setFlag = true;
			stateID = childrenAddress.getState().getStateID();
			districtID = childrenAddress.getDistrict().getDistrictID();
			mandalID = childrenAddress.getMandal().getMandalID();
			panchayatID = childrenAddress.getPanchayat().getPanchayatID();
			villageID = childrenAddress.getVillage().getVillageID();
			habitationID = childrenAddress.getHabitation().getHabitatID();
			et_edit_student_address_address1.setText(childrenAddress
					.getAddressLine1());
			et_edit_student_address_address2.setText(childrenAddress
					.getAddressLine2());
			et_edit_student_address_pincode.setText(childrenAddress
					.getPINCode());
		}

		if (Helper.childrenObject.getPermanentAddress() == null) {
			// Address permanentAddress = new Address();
			// Helper.childrenObject.setPermanentAddress(permanentAddress);
			// Helper.childrenObject.setPermanentAddress(permanentAddress);
			/*
			 * state = new State(); district = new District(); mandal = new
			 * Mandal(); habitation = new Habitation(); village = new Village();
			 * panchayat = new Panchayat();
			 */} else {

			Address permanentAddress = Helper.childrenObject
					.getPermanentAddress();
			if (permanentAddress.getAddressID() != 0) {

				ll_present_address.setVisibility(View.VISIBLE);
				/*
				 * state = address.getState(); district = address.getDistrict();
				 * mandal = address.getMandal(); habitation =
				 * address.getHabitation(); village = address.getVillage();
				 * panchayat = address.getPanchayat();
				 */setFlag = true;
				permanentstateID = permanentAddress.getState().getStateID();
				permanentdistrictID = permanentAddress.getDistrict()
						.getDistrictID();
				permanentmandalID = permanentAddress.getMandal().getMandalID();
				permanentpanchayatID = permanentAddress.getPanchayat()
						.getPanchayatID();
				permanentvillageID = permanentAddress.getVillage()
						.getVillageID();
				permanenthabitationID = permanentAddress.getHabitation()
						.getHabitatID();
				et_edit_student_address_Paddress1.setText(permanentAddress
						.getAddressLine1());
				et_edit_student_address_Paddress2.setText(permanentAddress
						.getAddressLine2());
				et_edit_student_address_Ppincode.setText(permanentAddress
						.getPINCode());
			}

		}

		spinnerListners();
		spinnerListnersPermanent();
		setDataToSpinners(0);
		setDataToSpinnersPermanent(0);
		cb_edit_student_address_permanent_y_n.setOnCheckedChangeListener(this);
		if (!TextUtils.isEmpty(permanentAddressID)
				&& !TextUtils.equals(localAddressID, permanentAddressID)
				&& !TextUtils.equals("0", permanentAddressID))
			cb_edit_student_address_permanent_y_n.setChecked(false);
		// Hide after some seconds
		/*
		 * final Handler handler = new Handler(); final Runnable runnable = new
		 * Runnable() {
		 * 
		 * @Override public void run() { if (addStuDialod.isShowing()) {
		 * addStuDialod.dismiss(); } } }; handler.postDelayed(runnable, 3000);
		 */
		return rootView;

	}

	/**
	 * Method for permanent address Spinner listener
	 */
	private void spinnerListnersPermanent() {
		spn_edit_student_address_Pstate
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						permanentstateID = stateList.get(position).getStateID();
						setDataToSpinnersPermanent(1);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_Pdistrict
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						permanentdistrictID = districtList.get(position)
								.getDistrictID();
						setDataToSpinnersPermanent(2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_Pmandal
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						permanentmandalID = mandalList.get(position)
								.getMandalID();
						/*
						 * panchayatList =
						 * Helper.getPanchayatList(getActivity(), panchayatList,
						 * permanentmandalID);
						 * 
						 * 
						 * if (position == 0) { permanentpanchayatID =
						 * panchayatList.get(0) .getPanchayatID(); } else {
						 * permanentpanchayatID = panchayatList.get(1)
						 * .getPanchayatID(); } permanentpanchayatID =
						 * panchayatList.get(1) .getPanchayatID();
						 */
						setDataToSpinnersPermanent(3);
						// }
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_Pvillage
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						permanentvillageID = villageList.get(position)
								.getVillageID();
						setDataToSpinnersPermanent(4);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_Phabitation
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						permanenthabitationID = habitationList.get(position)
								.getHabitatID();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

	}

	/**
	 * Method for address Spinner listener
	 */
	private void spinnerListners() {
		spn_edit_student_address_state
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						stateID = stateList.get(position).getStateID();
						setDataToSpinners(1);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_district
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						districtID = districtList.get(position).getDistrictID();
						setDataToSpinners(2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_mandal
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						mandalID = mandalList.get(position).getMandalID();
						/*
						 * panchayatList =
						 * Helper.getPanchayatList(getActivity(), panchayatList,
						 * mandalID); if (position == 0) { panchayatID =
						 * panchayatList.get(0).getPanchayatID(); } else {
						 * panchayatID = panchayatList.get(1).getPanchayatID();
						 * }
						 */
						setDataToSpinners(3);
						// }
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_village
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						villageID = villageList.get(position).getVillageID();
						setDataToSpinners(4);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spn_edit_student_address_habitation
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						habitationID = habitationList.get(position)
								.getHabitatID();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

	}

	/**
	 * Set data from db and set to Spinners
	 */
	private void setDataToSpinners(int index) {

		if (index == 0) {// load state data
			stateList = Helper.getStateDataFromDB(getActivity(), stateList);
			adapter = new CustomSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, stateList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_state.setAdapter(adapter);
			if (setFlag) {
				// set state position to spinner
				for (int i = 0; i < stateList.size(); i++) {
					if (stateList.get(i).getStateID() == stateID) {
						spn_edit_student_address_state.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 1) {
			districtList = Helper.getDistrictDataFromDB(getActivity(),
					districtList, stateID);
			disAdapter = new DistrictSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, districtList);
			disAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_district.setAdapter(disAdapter);
			if (setFlag) {
				// set district position to spinner
				for (int i = 0; i < districtList.size(); i++) {
					if (districtList.get(i).getDistrictID() == districtID) {
						spn_edit_student_address_district.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 2) {
			mandalList = Helper.getMandalList(getActivity(), mandalList,
					districtID);
			mandalAdapter = new MandalSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, mandalList);
			mandalAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_mandal.setAdapter(mandalAdapter);
			if (setFlag) {
				// set mandal position to spinner
				for (int i = 0; i < mandalList.size(); i++) {
					if (mandalList.get(i).getMandalID() == mandalID) {
						spn_edit_student_address_mandal.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 3) {
			villageList = Helper.getVillageList(getActivity(), villageList,
					mandalID);
			villageAdapter = new VillageAdapter(getActivity(),
					R.layout.spinner_rows, villageList);
			villageAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_village.setAdapter(villageAdapter);
			if (setFlag) {
				// set village position to spinner
				for (int i = 0; i < villageList.size(); i++) {
					if (villageList.get(i).getVillageID() == villageID) {
						spn_edit_student_address_village.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 4) {
			habitationList = Helper.getHabitationList(getActivity(),
					habitationList, villageID);
			habitationAdapter = new HabitationSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, habitationList);
			habitationAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_habitation.setAdapter(habitationAdapter);
			if (setFlag) {
				// set hab position to spinner
				for (int i = 0; i < habitationList.size(); i++) {
					if (habitationList.get(i).getHabitatID() == habitationID) {
						spn_edit_student_address_habitation.setSelection(i,
								true);
						break;
					}
				}
			}
		}
	}

	/**
	 * Method to set data to spinner
	 */

	private void setDataToSpinnersPermanent(int index) {

		if (index == 0) {// load state data
			stateList = Helper.getStateDataFromDB(getActivity(), stateList);
			adapter = new CustomSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, stateList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_Pstate.setAdapter(adapter);
			if (setFlag) {
				// set state position to spinner
				for (int i = 0; i < stateList.size(); i++) {
					if (stateList.get(i).getStateID() == permanentstateID) {
						spn_edit_student_address_Pstate.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 1) {
			districtList = Helper.getDistrictDataFromDB(getActivity(),
					districtList, permanentstateID);
			disAdapter = new DistrictSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, districtList);
			disAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_Pdistrict.setAdapter(disAdapter);
			if (setFlag) {
				// set district position to spinner
				for (int i = 0; i < districtList.size(); i++) {
					if (districtList.get(i).getDistrictID() == permanentdistrictID) {
						spn_edit_student_address_Pdistrict
								.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 2) {
			mandalList = Helper.getMandalList(getActivity(), mandalList,
					permanentdistrictID);
			mandalAdapter = new MandalSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, mandalList);
			mandalAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_Pmandal.setAdapter(mandalAdapter);
			if (setFlag) {
				// set mandal position to spinner
				for (int i = 0; i < mandalList.size(); i++) {
					if (mandalList.get(i).getMandalID() == permanentmandalID) {
						spn_edit_student_address_Pmandal.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 3) {
			villageList = Helper.getVillageList(getActivity(), villageList,
					permanentmandalID);
			villageAdapter = new VillageAdapter(getActivity(),
					R.layout.spinner_rows, villageList);
			villageAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_Pvillage.setAdapter(villageAdapter);
			if (setFlag) {
				// set village position to spinner
				for (int i = 0; i < villageList.size(); i++) {
					if (villageList.get(i).getVillageID() == permanentvillageID) {
						spn_edit_student_address_Pvillage.setSelection(i, true);
						break;
					}
				}
			}
		} else if (index == 4) {
			habitationList = Helper.getHabitationList(getActivity(),
					habitationList, permanentvillageID);
			habitationAdapter = new HabitationSpinnerAdapter(getActivity(),
					R.layout.spinner_rows, habitationList);
			habitationAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_edit_student_address_Phabitation.setAdapter(habitationAdapter);
			if (setFlag) {
				// set hab position to spinner
				for (int i = 0; i < habitationList.size(); i++) {
					if (habitationList.get(i).getHabitatID() == permanenthabitationID) {
						spn_edit_student_address_Phabitation.setSelection(i,
								true);
						break;
					}
				}
			}
		}

	}

	/**
	 * get id's from R.java
	 */
	private void findViews(View rootView) {
		tv_editstu_associate_inst = (TextView) getActivity().findViewById(
				R.id.tv_editstu_associate_inst);
		tv_editstu_Address = (TextView) getActivity().findViewById(
				R.id.tv_editstu_Address);
		spn_edit_student_address_state = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_state);
		spn_edit_student_address_mandal = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_mandal);
		spn_edit_student_address_district = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_district);
		spn_edit_student_address_village = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_village);
		spn_edit_student_address_habitation = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_habitation);
		et_edit_student_address_pincode = (EditText) rootView
				.findViewById(R.id.et_edit_student_address_pincode);
		et_edit_student_address_address1 = (EditText) rootView
				.findViewById(R.id.et_edit_student_address_address1);
		et_edit_student_address_address2 = (EditText) rootView
				.findViewById(R.id.et_edit_student_address_address2);

		spn_edit_student_address_Pstate = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_Pstate);
		spn_edit_student_address_Pmandal = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_Pmandal);
		spn_edit_student_address_Pdistrict = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_Pdistrict);
		spn_edit_student_address_Pvillage = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_Pvillage);
		spn_edit_student_address_Phabitation = (Spinner) rootView
				.findViewById(R.id.spn_edit_student_address_Phabitation);
		et_edit_student_address_Ppincode = (EditText) rootView
				.findViewById(R.id.et_edit_student_address_Ppincode);
		et_edit_student_address_Paddress1 = (EditText) rootView
				.findViewById(R.id.et_edit_student_address_Paddress1);
		et_edit_student_address_Paddress2 = (EditText) rootView
				.findViewById(R.id.et_edit_student_address_Paddress2);

		btn_edit_student_address_next = (Button) rootView
				.findViewById(R.id.btn_edit_student_address_next);
		btn_edit_student_address_next.setOnClickListener(this);
		cb_edit_student_address_permanent_y_n = (CheckBox) rootView
				.findViewById(R.id.cb_edit_student_address_permanent_y_n);
		ll_present_address = (LinearLayout) rootView
				.findViewById(R.id.ll_present_address);
	}

	/*
	 * click event
	 */
	@Override
	public void onClick(View v) {
		int errorcount = 0;
		if (v == btn_edit_student_address_next) {

			Helper.childrenObject
					.setSameAddress(cb_edit_student_address_permanent_y_n
							.isChecked());

			if (spn_edit_student_address_state.getSelectedItemPosition() == 0) {
				errorcount++;
				View view = spn_edit_student_address_state.getSelectedView();
				Helper.setErrorForCustomSpinner(view, "Select State");

				// adapter.setErrorForSpinner1(view,"Select State Name");

				// spn_edit_student_address_state.setFocusable(true);
				// spn_edit_student_address_state.setFocusableInTouchMode(true);
				// spn_edit_student_address_state.requestFocus();
				// Toast.makeText(this.getActivity(), "Please select State",
				// Toast.LENGTH_SHORT).show();
			}
			if (spn_edit_student_address_district.getSelectedItemPosition() == 0) {
				errorcount++;
				View view = spn_edit_student_address_district.getSelectedView();
				Helper.setErrorForCustomSpinner(view, "Select District");
			}
			if (spn_edit_student_address_mandal.getSelectedItemPosition() == 0) {
				errorcount++;
				View view = spn_edit_student_address_mandal.getSelectedView();
				Helper.setErrorForCustomSpinner(view, "Select Mandal");
			}
			if (spn_edit_student_address_village.getSelectedItemPosition() == 0) {
				errorcount++;
				View view = spn_edit_student_address_village.getSelectedView();
				Helper.setErrorForCustomSpinner(view, "Select Village");
			}
			if (!TextUtils.isEmpty(et_edit_student_address_pincode.getText()
					.toString().trim())
					&& (et_edit_student_address_pincode.getText().toString()
							.trim().length() < 6)) {
				errorcount++;
				Helper.displayErrorMsg(et_edit_student_address_pincode,
						"Enter a valid pincode");
			}
			if (errorcount == 0) {
				AddStudentActivityDialog.tabFlags[3] = true;
				AddStudentActivityDialog
						.enableHeaderClick1(AddStudentActivityDialog.tabFlags);
				Address address = new Address();
				if (Helper.addChildFlag) {

				} else {
					if (Helper.childrenObject.getAddress() != null) {
						address.setAddressID(Helper.childrenObject.getAddress()
								.getAddressID());
					}
				}
				address.setAddressLine1(et_edit_student_address_address1
						.getText().toString().trim());
				address.setAddressLine2(et_edit_student_address_address2
						.getText().toString().trim());
				address.setPINCode(et_edit_student_address_pincode.getText()
						.toString().trim());
				State state = new State();
				District district = new District();
				Mandal mandal = new Mandal();
				Habitation habitation = new Habitation();
				Village village = new Village();
				Panchayat panchayat = new Panchayat();
				state.setStateID(stateID);
				district.setDistrictID(districtID);
				village.setVillageID(villageID);
				mandal.setMandalID(mandalID);
				panchayat.setPanchayatID(panchayatID);
				habitation.setHabitatID(habitationID);

				address.setState(state);
				address.setDistrict(district);
				address.setVillage(village);
				address.setMandal(mandal);
				address.setPanchayat(panchayat);
				address.setHabitation(habitation);
				// addressList.add(address);
				Helper.childrenObject.setAddress(address);
				Helper.updateHeaderFromNext(getActivity(), tv_editstu_Address,
						tv_editstu_associate_inst, R.drawable.headerbg,
						R.drawable.headerbg_selectced);
				// bundle.putInt("InsituteID", Helper.selectedInstituteId);
				if (AddStudentActivityDialog.fragmentArr[4] == null)
					AddStudentActivityDialog.fragmentArr[4] = fragment = new EditStuAssocInst();
				else
					fragment = AddStudentActivityDialog.fragmentArr[4];
				// fragment.setArguments(bundle);
				if (fragment != null) {
					android.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();

				}

			}

			if (!cb_edit_student_address_permanent_y_n.isChecked()) {
				if (spn_edit_student_address_Pstate.getSelectedItemPosition() == 0) {
					errorcount++;
					View view = spn_edit_student_address_Pstate
							.getSelectedView();
					Helper.setErrorForCustomSpinner(view, "Select State");

				}
				if (spn_edit_student_address_Pdistrict
						.getSelectedItemPosition() == 0) {
					errorcount++;
					View view = spn_edit_student_address_Pdistrict
							.getSelectedView();
					Helper.setErrorForCustomSpinner(view, "Select District");

				}
				if (spn_edit_student_address_Pmandal.getSelectedItemPosition() == 0) {
					errorcount++;
					View view = spn_edit_student_address_Pmandal
							.getSelectedView();
					Helper.setErrorForCustomSpinner(view, "Select Mandal");

				}
				if (spn_edit_student_address_Pvillage.getSelectedItemPosition() == 0) {
					errorcount++;
					View view = spn_edit_student_address_Pvillage
							.getSelectedView();
					Helper.setErrorForCustomSpinner(view, "Select Village");

				}
				if (!TextUtils.isEmpty(et_edit_student_address_Ppincode
						.getText().toString().trim())
						&& (et_edit_student_address_Ppincode.getText()
								.toString().trim().length() < 6)) {
					errorcount++;
					Helper.displayErrorMsg(et_edit_student_address_Ppincode,
							"Enter a valid pincode");
				}
				if (errorcount == 0) {
					AddStudentActivityDialog.tabFlags[3] = true;
					AddStudentActivityDialog
							.enableHeaderClick1(AddStudentActivityDialog.tabFlags);
					Address address = new Address();
					address.setAddressLine1(et_edit_student_address_Paddress1
							.getText().toString().trim());
					address.setAddressLine2(et_edit_student_address_Paddress2
							.getText().toString().trim());
					address.setPINCode(et_edit_student_address_Ppincode
							.getText().toString().trim());
					State state = new State();
					District district = new District();
					Mandal mandal = new Mandal();
					Habitation habitation = new Habitation();
					Village village = new Village();
					Panchayat panchayat = new Panchayat();

					state.setStateID(permanentstateID);
					district.setDistrictID(permanentdistrictID);
					village.setVillageID(permanentvillageID);
					mandal.setMandalID(permanentmandalID);
					panchayat.setPanchayatID(permanentpanchayatID);
					habitation.setHabitatID(permanenthabitationID);

					address.setState(state);
					address.setDistrict(district);
					address.setVillage(village);
					address.setMandal(mandal);
					address.setPanchayat(panchayat);
					address.setHabitation(habitation);
					// addressList.add(address);
					Helper.childrenObject.setPermanentAddress(address);
					Helper.updateHeaderFromNext(getActivity(),
							tv_editstu_Address, tv_editstu_associate_inst,
							R.drawable.headerbg, R.drawable.headerbg_selectced);
					// bundle.putInt("InsituteID",
					// Helper.selectedInstituteId);
					if (AddStudentActivityDialog.fragmentArr[4] == null)
						AddStudentActivityDialog.fragmentArr[4] = fragment = new EditStuAssocInst();
					else
						fragment = AddStudentActivityDialog.fragmentArr[4];
					// fragment.setArguments(bundle);
					if (fragment != null) {
						android.app.FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.commit();

					}

				}
			} else {
				Helper.childrenObject.setPermanentAddress(null);
			}
			final InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		}

		// if (!et_edit_student_address_pincode.getText().toString()
		// .trim().equals("")
		// && (et_edit_student_address_pincode.getText()
		// .toString().trim().length() < 6)) {
		// Helper.displayErrorMsg(et_edit_student_address_pincode,
		// "Enter a valid pincode");
		// } else {
		//
		// address.setAddressLine1(et_edit_student_address_address1
		// .getText().toString().trim());
		// address.setAddressLine2(et_edit_student_address_address2
		// .getText().toString().trim());
		// address.setPINCode(et_edit_student_address_pincode
		// .getText().toString().trim());
		// state.setStateID(stateID);
		// district.setDistrictID(districtID);
		// village.setVillageID(villageID);
		// mandal.setMandalID(mandalID);
		// panchayat.setPanchayatID(panchayatID);
		// habitation.setHabitatID(habitationID);
		//
		// address.setState(state);
		// address.setDistrict(district);
		// address.setVillage(village);
		// address.setMandal(mandal);
		// address.setPanchayat(panchayat);
		// address.setHabitation(habitation);
		//
		// Helper.childrenObject.getUser().setAddress(address);
		// Helper.updateHeaderFromNext(getActivity(),
		// tv_editstu_Address, tv_editstu_associate_inst,
		// R.drawable.headerbg, R.drawable.headerbg_selectced);
		// // bundle.putInt("InsituteID", Helper.selectedInstituteId);
		// fragment = new EditStuAssocInst();
		// // fragment.setArguments(bundle);
		// if (fragment != null) {
		// android.app.FragmentManager fragmentManager = getFragmentManager();
		// fragmentManager.beginTransaction()
		// .replace(R.id.frame_container, fragment)
		// .commit();
		//
		// }
		//
		// }
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			ll_present_address.setVisibility(View.GONE);
		} else {
			ll_present_address.setVisibility(View.VISIBLE);
		}
	}

}
