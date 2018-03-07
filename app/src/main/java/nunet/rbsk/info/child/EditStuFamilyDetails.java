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
//* Name   :  EditStuFamilyDetails

//* Type    : Fragment

//* Description     : Functionality to Add family information of student
//* References     :                                                         
//* Author    : Kiruthika.Ganesan

//* Created Date       : 28-04-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//                            Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations
//3.0		   01-05-2015	        Self Review(Kiruthika)		No Observation
//3.0		   02-05-2015 			Promodh(Peer review)		No Observation
//3.0		   02-05-2015 			Anil(Peer review)			1.	Remove unused imports.
//																2.	Provide access specifiers to variables.
//																3.	Contact Model is not updated.
//*****************************************************************************  

import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Childrenparents;
import nunet.rbsk.model.ContactCategories;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.Salutation;
import nunet.rbsk.model.Users;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditStuFamilyDetails extends Fragment implements OnClickListener {
	private Button btn_edit_student_family_details_next;
	private TextView tv_editstu_family_details;
	private TextView tv_editstu_Address;
	private Fragment fragment = null;

	// Variables for Father details
	private Spinner spn_stud_familydet_father_salutation;
	private Spinner spn_stud_familydet_father_contact_type;

	private EditText et_stud_familydet_father_firstname;
	private EditText et_stud_familydet_father_middlename;
	private EditText et_stud_familydet_father_lastname;
	private EditText et_stud_familydet_father_aadhaar_no;
	private EditText et_stud_familydet_father_contact_no;
	private EditText et_stud_familydet_father_email;

	// Variables for Mother details
	private Spinner spn_stud_familydet_mother_salutation;
	private Spinner spn_stud_familydet_mother_contact_type;

	private EditText et_stud_familydet_mother_firstname;
	private EditText et_stud_familydet_mother_middlename;
	private EditText et_stud_familydet_mother_lastname;
	private EditText et_stud_familydet_mother_aadhaar_no;
	private EditText et_stud_familydet_mother_contact_no;
	private EditText et_stud_familydet_mother_email;

	// Variables for Guardian details
	private Spinner spn_stud_familydet_guardian_salutation;
	private Spinner spn_stud_familydet_guardian_contact_type;

	private EditText et_stud_familydet_guardian_firstname;
	private EditText et_stud_familydet_guardian_middlename;
	private EditText et_stud_familydet_guardian_lastname;
	private EditText et_stud_familydet_guardian_aadhaar_no;
	private EditText et_stud_familydet_guardian_contact_no;
	private EditText et_stud_familydet_guardian_email;

	private Button btn_stud_familydetails_fatherdet;
	private Button btn_stud_familydetails_motherdet;
	private LinearLayout ll_addstu_fatherdetails;
	private LinearLayout ll_addstu_motherdetails;
	private LinearLayout ll_stud_family_guardianlayout;
	private CheckBox cb_stud_family_guardiandetails;
	private ArrayList<Salutation> salutationAryList;
	private ArrayList<ContactCategories> contactTypeAryList;
	private boolean guardianCheck = false;
	private Users guardian;
	private ArrayList<Childrenparents> parentsList;
	private DBHelper dbh;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.addstud_familyinfo,
				container, false);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		findViews(rootView);
		dbh = DBHelper.getInstance(getActivity());
		salutationAryList = Helper.getSalutationDataFromDB(getActivity());
		contactTypeAryList = Helper.getContactTypeFromDB(getActivity());
		ArrayAdapter<String> adp_spnSalutation = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item);
		adp_spnSalutation
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < salutationAryList.size(); i++) {
			adp_spnSalutation.add(salutationAryList.get(i)
					.getSalutaionDisplayName());
		}
		spn_stud_familydet_father_salutation.setAdapter(adp_spnSalutation);
		spn_stud_familydet_mother_salutation.setAdapter(adp_spnSalutation);
		spn_stud_familydet_guardian_salutation.setAdapter(adp_spnSalutation);

		ArrayAdapter<String> adp_spnContactCat = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item);
		adp_spnContactCat
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < contactTypeAryList.size(); i++) {
			adp_spnContactCat.add(contactTypeAryList.get(i)
					.getContactCategoryName());
		}
		spn_stud_familydet_father_contact_type.setAdapter(adp_spnContactCat);
		spn_stud_familydet_mother_contact_type.setAdapter(adp_spnContactCat);
		spn_stud_familydet_guardian_contact_type.setAdapter(adp_spnContactCat);

		if (Helper.childrenObject.getParentAry() != null) {
			if (Helper.childrenObject.getParentAry().size() >= 2) {
				ll_addstu_fatherdetails.setVisibility(View.VISIBLE);
				ll_addstu_motherdetails.setVisibility(View.VISIBLE);
				Childrenparents childparent = Helper.childrenObject
						.getParentAry().get(0);
				Users user = childparent.getUser();
				ArrayList<Contacts> fatherContacts = user.getContacts();

				et_stud_familydet_father_firstname.setText(user.getFirstName());
				et_stud_familydet_father_middlename.setText(user
						.getMiddleName());
				et_stud_familydet_father_lastname.setText(user.getLastName());
				et_stud_familydet_father_aadhaar_no.setText(user
						.getAadharNumber());
				// et_stud_familydet_father_contact_no.setText("123123");
				spn_stud_familydet_father_salutation.setSelection(childparent
						.getSalutationID());

				if (fatherContacts != null) {
					for (Contacts contact : fatherContacts) {
						System.out.println("contact category id is:"
								+ contact.getContactCategoryID());
						if (contact.getContactCategoryID() == 1) {
							System.out.println("contact val in loop:"
									+ contact.getContact());
							et_stud_familydet_father_email.setText(""
									+ contact.getContact());

						} else if (contact.getContactCategoryID() == 2
								|| contact.getContactCategoryID() == 3) {
							et_stud_familydet_father_contact_no.setText(contact
									.getContact());

							for (int i = 0; i < contactTypeAryList.size(); i++) {
								if (contact.getContactCategoryID() == contactTypeAryList
										.get(i).getContactCategoryID()) {
									spn_stud_familydet_father_contact_type
											.setSelection(i);
								}
							}

						}
					}
				}

				Childrenparents childmother = Helper.childrenObject
						.getParentAry().get(1);
				Users userMother = childmother.getUser();
				ArrayList<Contacts> motherContact = userMother.getContacts();
				et_stud_familydet_mother_firstname.setText(userMother
						.getFirstName());
				et_stud_familydet_mother_middlename.setText(userMother
						.getMiddleName());
				et_stud_familydet_mother_lastname.setText(userMother
						.getLastName());
				et_stud_familydet_mother_aadhaar_no.setText(userMother
						.getAadharNumber());
				spn_stud_familydet_mother_salutation.setSelection(childmother
						.getSalutationID());
				if (motherContact != null) {
					for (Contacts contact : motherContact) {
						if (contact.getContactCategoryID() == 1) {
							et_stud_familydet_mother_email.setText(contact
									.getContact());
						} else if (contact.getContactCategoryID() == 2
								|| contact.getContactCategoryID() == 3) {
							et_stud_familydet_mother_contact_no.setText(contact
									.getContact());

							for (int i = 0; i < contactTypeAryList.size(); i++) {
								if (contact.getContactCategoryID() == contactTypeAryList
										.get(i).getContactCategoryID()) {
									spn_stud_familydet_mother_contact_type
											.setSelection(i);
								}
							}

						}
					}
				}

				if (Helper.childrenObject.getParentAry().size() == 3) {
					cb_stud_family_guardiandetails.setChecked(true);
					ll_stud_family_guardianlayout.setVisibility(View.VISIBLE);
					Childrenparents childGuardian = Helper.childrenObject
							.getParentAry().get(2);
					Users userGuardian = childGuardian.getUser();
					ArrayList<Contacts> guardianContact = userGuardian
							.getContacts();
					et_stud_familydet_guardian_firstname.setText(userGuardian
							.getFirstName());
					et_stud_familydet_guardian_middlename.setText(userGuardian
							.getMiddleName());
					et_stud_familydet_guardian_lastname.setText(userGuardian
							.getLastName());
					et_stud_familydet_guardian_aadhaar_no.setText(userGuardian
							.getAadharNumber());
					spn_stud_familydet_guardian_salutation
							.setSelection(childGuardian.getSalutationID());
					if (guardianContact != null) {
						for (Contacts contact : guardianContact) {
							if (contact.getContactCategoryID() == 1) {
								et_stud_familydet_guardian_email
										.setText(contact.getContact());
							} else if (contact.getContactCategoryID() == 2
									|| contact.getContactCategoryID() == 3) {
								et_stud_familydet_guardian_contact_no
										.setText(contact.getContact());

								for (int i = 0; i < contactTypeAryList.size(); i++) {
									if (contact.getContactCategoryID() == contactTypeAryList
											.get(i).getContactCategoryID()) {
										spn_stud_familydet_guardian_contact_type
												.setSelection(i);
									}
								}

							}
						}
					}
				}

			} else {
				cb_stud_family_guardiandetails.setChecked(true);
				ll_stud_family_guardianlayout.setVisibility(View.VISIBLE);
				ll_addstu_fatherdetails.setVisibility(View.GONE);
				ll_addstu_motherdetails.setVisibility(View.GONE);
				Childrenparents childGuardian = Helper.childrenObject
						.getParentAry().get(0);
				Users userGuardian = childGuardian.getUser();
				ArrayList<Contacts> guardianContact = userGuardian
						.getContacts();
				// Users userGuardian = childGuardian.getUser();
				et_stud_familydet_guardian_firstname.setText(userGuardian
						.getFirstName());
				et_stud_familydet_guardian_middlename.setText(userGuardian
						.getMiddleName());
				et_stud_familydet_guardian_lastname.setText(userGuardian
						.getLastName());
				et_stud_familydet_guardian_aadhaar_no.setText(userGuardian
						.getAadharNumber());
				// et_stud_familydet_guardian_contact_no.setText("");
				// et_stud_familydet_guardian_email.setText("");
				spn_stud_familydet_guardian_salutation
						.setSelection(childGuardian.getSalutationID());
				if (guardianContact != null) {
					for (Contacts contact : guardianContact) {
						if (contact.getContactCategoryID() == 1) {
							et_stud_familydet_guardian_email.setText(contact
									.getContact());
						} else if (contact.getContactCategoryID() == 2
								|| contact.getContactCategoryID() == 3) {
							et_stud_familydet_guardian_contact_no
									.setText(contact.getContact());
							spn_stud_familydet_guardian_contact_type
									.setSelection(contact
											.getContactCategoryID() - 1);
						}
					}
				}

			}
		} else {
			// ll_addstu_fatherdetails.setVisibility(View.GONE);
			ll_addstu_motherdetails.setVisibility(View.GONE);
			ll_stud_family_guardianlayout.setVisibility(View.GONE);
		}

		return rootView;
	}

	/**
	 * set id's from R.java
	 */
	private void findViews(View rootView) {

		
		tv_editstu_family_details = (TextView) getActivity().findViewById(
				R.id.tv_editstu_family_details);
		tv_editstu_Address = (TextView) getActivity().findViewById(
				R.id.tv_editstu_Address);
		btn_edit_student_family_details_next = (Button) rootView
				.findViewById(R.id.btn_edit_student_family_details_next);

		btn_stud_familydetails_fatherdet = (Button) rootView
				.findViewById(R.id.btn_stud_familydetails_fatherdet);
		btn_stud_familydetails_motherdet = (Button) rootView
				.findViewById(R.id.btn_stud_familydetails_motherdet);
		ll_addstu_fatherdetails = (LinearLayout) rootView
				.findViewById(R.id.ll_addstu_fatherdetails);
		ll_addstu_motherdetails = (LinearLayout) rootView
				.findViewById(R.id.ll_addstu_motherdetails);
		ll_stud_family_guardianlayout = (LinearLayout) rootView
				.findViewById(R.id.ll_stud_family_guardianlayout);
		cb_stud_family_guardiandetails = (CheckBox) rootView
				.findViewById(R.id.cb_stud_family_guardiandetails);

		// Get id for father details
		spn_stud_familydet_father_salutation = (Spinner) rootView
				.findViewById(R.id.spn_stud_familydet_father_salutation);
		spn_stud_familydet_father_contact_type = (Spinner) rootView
				.findViewById(R.id.spn_stud_familydet_father_contact_type);

		et_stud_familydet_father_firstname = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_father_firstname);
		et_stud_familydet_father_middlename = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_father_middlename);
		et_stud_familydet_father_lastname = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_father_lastname);
		et_stud_familydet_father_aadhaar_no = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_father_aadhaar_no);
		et_stud_familydet_father_contact_no = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_father_contact_no);
		et_stud_familydet_father_email = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_father_email);

		// Get id for mother details
		spn_stud_familydet_mother_salutation = (Spinner) rootView
				.findViewById(R.id.spn_stud_familydet_mother_salutation);
		spn_stud_familydet_mother_contact_type = (Spinner) rootView
				.findViewById(R.id.spn_stud_familydet_mother_contact_type);

		et_stud_familydet_mother_firstname = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_mother_firstname);
		et_stud_familydet_mother_middlename = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_mother_middlename);
		et_stud_familydet_mother_lastname = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_mother_lastname);
		et_stud_familydet_mother_aadhaar_no = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_mother_aadhaar_no);
		et_stud_familydet_mother_contact_no = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_mother_contact_no);
		et_stud_familydet_mother_email = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_mother_email);

		// Get id for Guardian details
		spn_stud_familydet_guardian_salutation = (Spinner) rootView
				.findViewById(R.id.spn_stud_familydet_guardian_salutation);
		spn_stud_familydet_guardian_contact_type = (Spinner) rootView
				.findViewById(R.id.spn_stud_familydet_guardian_contact_type);

		et_stud_familydet_guardian_firstname = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_guardian_firstname);
		et_stud_familydet_guardian_middlename = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_guardian_middlename);
		et_stud_familydet_guardian_lastname = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_guardian_lastname);
		et_stud_familydet_guardian_aadhaar_no = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_guardian_aadhaar_no);
		et_stud_familydet_guardian_contact_no = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_guardian_contact_no);
		et_stud_familydet_guardian_email = (EditText) rootView
				.findViewById(R.id.et_stud_familydet_guardian_email);

		btn_edit_student_family_details_next.setOnClickListener(this);
		btn_stud_familydetails_fatherdet.setOnClickListener(this);
		btn_stud_familydetails_motherdet.setOnClickListener(this);

		cb_stud_family_guardiandetails
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						if (isChecked) {
							guardianCheck = true;
							if (ll_addstu_fatherdetails.getVisibility() == View.VISIBLE) {
								collapse(ll_addstu_fatherdetails);
								ll_stud_family_guardianlayout
										.setVisibility(View.VISIBLE);
							} else if (ll_addstu_motherdetails.getVisibility() == View.VISIBLE) {
								collapse(ll_addstu_motherdetails);
								ll_stud_family_guardianlayout
										.setVisibility(View.VISIBLE);
							} else {
								ll_stud_family_guardianlayout
										.setVisibility(View.VISIBLE);
							}
						} else {
							guardianCheck = false;
							ll_stud_family_guardianlayout
									.setVisibility(View.GONE);
						}
					}
				});
	}

	/*
	 * 
	 * click event for views
	 */
	@Override
	public void onClick(View v) {
		

		if (v == btn_edit_student_family_details_next) {
			int count = 0;
			parentsList = new ArrayList<Childrenparents>();
			if ((et_stud_familydet_father_firstname.getText().toString().trim()
					.isEmpty() && et_stud_familydet_mother_firstname.getText()
					.toString().trim().isEmpty())
					&& (!guardianCheck)) {
				Toast.makeText(getActivity(), "Enter Parents Details",
						Toast.LENGTH_SHORT).show();

				ll_addstu_fatherdetails.setVisibility(View.VISIBLE);
				ll_addstu_motherdetails.setVisibility(View.VISIBLE);

				Users father = new Users();
				Childrenparents parentFather = new Childrenparents();
				if (et_stud_familydet_father_firstname.getText().toString()
						.trim().isEmpty()) {
					count++;
					Helper.displayErrorMsg(et_stud_familydet_father_firstname,
							"Enter First Name");
				} else {

					father.setFirstName(et_stud_familydet_father_firstname
							.getText().toString().trim());
				}
				if (et_stud_familydet_father_lastname.getText().toString()
						.trim().isEmpty()) {
					count++;
					Helper.displayErrorMsg(et_stud_familydet_father_lastname,
							"Enter Last Name");
				} else {
					father.setLastName(et_stud_familydet_father_lastname
							.getText().toString().trim());
				}

				if ((!et_stud_familydet_father_aadhaar_no.getText().toString()
						.trim().isEmpty())
						&& (et_stud_familydet_father_aadhaar_no.getText()
								.length() < 12)) {
					count++;
					Helper.displayErrorMsg(et_stud_familydet_father_aadhaar_no,
							"Enter Valid Aadhaar No");
				} else {
					father.setAadharNumber(et_stud_familydet_father_aadhaar_no
							.getText().toString().trim());
				}

				if ((!et_stud_familydet_father_email.getText().toString()
						.trim().isEmpty())) {
					if (!Helper.isEmailAddress(et_stud_familydet_father_email)) {
						count++;
					}
				}
				if ((!et_stud_familydet_father_email.getText().toString()
						.trim().isEmpty())
						|| (!et_stud_familydet_father_contact_no.getText()
								.toString().trim().isEmpty())) {
					ArrayList<Contacts> fatherContacts = new ArrayList<Contacts>();
					if (!et_stud_familydet_father_email.getText().toString()
							.trim().isEmpty()) {
						Contacts emailContact = new Contacts();
						emailContact.setContactCategoryID(1);
						emailContact.setContactTypeID(1);
						emailContact.setContact(et_stud_familydet_father_email
								.getText().toString().trim());
						fatherContacts.add(emailContact);
					}
					if (!et_stud_familydet_father_contact_no.getText()
							.toString().trim().isEmpty()) {
						Contacts phoneContact = new Contacts();
						phoneContact.setContactCategoryID(contactTypeAryList
								.get(spn_stud_familydet_father_contact_type
										.getSelectedItemPosition())
								.getContactCategoryID());

						phoneContact
								.setContact(et_stud_familydet_father_contact_no
										.getText().toString().trim());
						if (phoneContact.getContactCategoryID() == 2) {
							phoneContact.setContactTypeID(3);
						} else {
							phoneContact.setContactTypeID(5);
						}
						fatherContacts.add(phoneContact);
					}
					father.setContacts(fatherContacts);

				}

				father.setMiddleName(et_stud_familydet_father_middlename
						.getText().toString().trim());
				parentFather.setSalutationID(salutationAryList.get(
						spn_stud_familydet_father_salutation
								.getSelectedItemPosition()).getSalutationID());
				String query_Wards = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND LocalChildrenID='"
						+ Helper.childrenObject.getChildrenID()
						+ "' and RelationID = 1";
				Cursor dataCursor = dbh.getCursorData(this.getActivity(),
						query_Wards);
				if (dataCursor != null && dataCursor.moveToFirst()) {
					String parent_userid = dataCursor.getString(dataCursor
							.getColumnIndex("LocalUserID"));
					father.setUserID(NumUtil.IntegerParse.parseInt(parent_userid));
				} else {

				}

				parentFather.setRelationID(1);
				parentFather.setUser(father);
				parentsList.add(parentFather);

				// To get Mother details

				Users mother = new Users();
				Childrenparents parentMother = new Childrenparents();
				if (et_stud_familydet_mother_firstname.getText().toString()
						.trim().isEmpty()) {
					count++;
					Helper.displayErrorMsg(et_stud_familydet_mother_firstname,
							"Enter First Name");
				} else {
					mother.setFirstName(et_stud_familydet_mother_firstname
							.getText().toString().trim());

				}
				if (et_stud_familydet_mother_lastname.getText().toString()
						.trim().isEmpty()) {
					count++;
					Helper.displayErrorMsg(et_stud_familydet_mother_lastname,
							"Enter Last Name");
				} else {
					mother.setLastName(et_stud_familydet_mother_lastname
							.getText().toString().trim());
				}
				mother.setMiddleName(et_stud_familydet_mother_middlename
						.getText().toString().trim());

				if ((!et_stud_familydet_mother_aadhaar_no.getText().toString()
						.trim().isEmpty())
						&& (et_stud_familydet_mother_aadhaar_no.getText()
								.length() < 12)) {
					count++;
					Helper.displayErrorMsg(et_stud_familydet_mother_aadhaar_no,
							"Enter Valid Aadhaar No");
				} else {
					mother.setAadharNumber(et_stud_familydet_mother_aadhaar_no
							.getText().toString().trim());
				}

				if ((!et_stud_familydet_mother_email.getText().toString()
						.trim().isEmpty())) {
					if (!Helper.isEmailAddress(et_stud_familydet_mother_email)) {
						count++;
					}

				}

				if ((!et_stud_familydet_mother_email.getText().toString()
						.trim().isEmpty())
						|| (!et_stud_familydet_mother_contact_no.getText()
								.toString().trim().isEmpty())) {
					ArrayList<Contacts> motherContacts = new ArrayList<Contacts>();
					if (!et_stud_familydet_mother_email.getText().toString()
							.trim().isEmpty()) {
						Contacts emailContact = new Contacts();
						emailContact.setContactCategoryID(1);
						emailContact.setContactTypeID(1);
						emailContact.setContact(et_stud_familydet_mother_email
								.getText().toString().trim());
						motherContacts.add(emailContact);
					}
					if (!et_stud_familydet_mother_contact_no.getText()
							.toString().trim().isEmpty()) {
						Contacts phoneContact = new Contacts();
						phoneContact.setContactCategoryID(contactTypeAryList
								.get(spn_stud_familydet_mother_contact_type
										.getSelectedItemPosition())
								.getContactCategoryID());
						phoneContact
								.setContact(et_stud_familydet_mother_contact_no
										.getText().toString().trim());
						if (phoneContact.getContactCategoryID() == 2) {
							phoneContact.setContactTypeID(3);
						} else {
							phoneContact.setContactTypeID(5);
						}

						motherContacts.add(phoneContact);
					}
					mother.setContacts(motherContacts);
				}

				parentMother.setSalutationID(salutationAryList.get(
						spn_stud_familydet_mother_salutation
								.getSelectedItemPosition()).getSalutationID());

				String query_Wards2 = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND  LocalChildrenID='"
						+ Helper.childrenObject.getChildrenID()
						+ "' and RelationID = 2";
				Cursor dataCursor2 = dbh.getCursorData(this.getActivity(),
						query_Wards2);
				if (dataCursor2 != null && dataCursor2.moveToFirst()) {
					String parent_userid = dataCursor2.getString(dataCursor2
							.getColumnIndex("LocalUserID"));
					mother.setUserID(NumUtil.IntegerParse.parseInt(parent_userid));
				} else {

				}

				parentMother.setRelationID(2);
				parentMother.setUser(mother);
				parentsList.add(parentMother);

			} else if ((!et_stud_familydet_father_firstname.getText()
					.toString().trim().isEmpty() || !et_stud_familydet_father_lastname
					.getText().toString().trim().isEmpty())
					|| (!et_stud_familydet_mother_firstname.getText()
							.toString().trim().isEmpty() || !et_stud_familydet_mother_lastname
							.getText().toString().trim().isEmpty())) {

				if (guardianCheck) {
					ll_addstu_fatherdetails.setVisibility(View.VISIBLE);
					ll_addstu_motherdetails.setVisibility(View.VISIBLE);
					ll_stud_family_guardianlayout.setVisibility(View.VISIBLE);

					Users father = new Users();
					Childrenparents parentFather = new Childrenparents();

					if (et_stud_familydet_father_firstname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_father_firstname,
								"Enter First Name");
					} else {
						father.setFirstName(et_stud_familydet_father_firstname
								.getText().toString().trim());
					}
					if (et_stud_familydet_father_lastname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_father_lastname,
								"Enter Last Name");
					} else {
						father.setLastName(et_stud_familydet_father_lastname
								.getText().toString().trim());
					}
					father.setMiddleName(et_stud_familydet_father_middlename
							.getText().toString().trim());
					if ((!et_stud_familydet_father_aadhaar_no.getText()
							.toString().trim().isEmpty())
							&& (et_stud_familydet_father_aadhaar_no.getText()
									.length() < 12)) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_father_aadhaar_no,
								"Enter Valid Aadhaar No");
					} else {
						father.setAadharNumber(et_stud_familydet_father_aadhaar_no
								.getText().toString().trim());
					}

					if ((!et_stud_familydet_father_email.getText().toString()
							.trim().isEmpty())) {

						if (!Helper
								.isEmailAddress(et_stud_familydet_father_email)) {
							count++;
						}

					}

					if ((!et_stud_familydet_father_email.getText().toString()
							.trim().isEmpty())
							|| (!et_stud_familydet_father_contact_no.getText()
									.toString().trim().isEmpty())) {
						ArrayList<Contacts> fatherContacts = new ArrayList<Contacts>();
						if (!et_stud_familydet_father_email.getText()
								.toString().trim().isEmpty()) {
							Contacts emailContact = new Contacts();
							emailContact.setContactCategoryID(1);
							emailContact
									.setContact(et_stud_familydet_father_email
											.getText().toString().trim());
							emailContact.setContactTypeID(1);
							fatherContacts.add(emailContact);
						}
						if (!et_stud_familydet_father_contact_no.getText()
								.toString().trim().isEmpty()) {
							Contacts phoneContact = new Contacts();
							phoneContact
									.setContactCategoryID(contactTypeAryList
											.get(spn_stud_familydet_father_contact_type
													.getSelectedItemPosition())
											.getContactCategoryID());
							phoneContact
									.setContact(et_stud_familydet_father_contact_no
											.getText().toString().trim());
							if (phoneContact.getContactCategoryID() == 2) {
								phoneContact.setContactTypeID(3);
							} else {
								phoneContact.setContactTypeID(5);
							}

							fatherContacts.add(phoneContact);
						}
						father.setContacts(fatherContacts);
					}

					parentFather.setSalutationID(salutationAryList.get(
							spn_stud_familydet_father_salutation
									.getSelectedItemPosition())
							.getSalutationID());

					String query_Wards = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND  LocalChildrenID='"
							+ Helper.childrenObject.getChildrenID()
							+ "' and RelationID = 1";
					Cursor dataCursor = dbh.getCursorData(this.getActivity(),
							query_Wards);
					if (dataCursor != null && dataCursor.moveToFirst()) {
						String parent_userid = dataCursor.getString(dataCursor
								.getColumnIndex("LocalUserID"));
						father.setUserID(NumUtil.IntegerParse
								.parseInt(parent_userid));
					} else {

					}

					parentFather.setRelationID(1);
					parentFather.setUser(father);
					parentsList.add(parentFather);

					Users mother = new Users();
					Childrenparents parentMother = new Childrenparents();
					if (et_stud_familydet_mother_firstname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_mother_firstname,
								"Enter First Name");
					} else {
						mother.setFirstName(et_stud_familydet_mother_firstname
								.getText().toString().trim());

					}
					if (et_stud_familydet_mother_lastname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_mother_lastname,
								"Enter Last Name");
					} else {
						mother.setLastName(et_stud_familydet_mother_lastname
								.getText().toString().trim());
					}
					mother.setMiddleName(et_stud_familydet_mother_middlename
							.getText().toString().trim());

					if ((!et_stud_familydet_mother_aadhaar_no.getText()
							.toString().trim().isEmpty())
							&& (et_stud_familydet_mother_aadhaar_no.getText()
									.length() < 12)) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_mother_aadhaar_no,
								"Enter Valid Aadhaar No");
					} else {
						mother.setAadharNumber(et_stud_familydet_mother_aadhaar_no
								.getText().toString().trim());
					}

					if ((!et_stud_familydet_mother_email.getText().toString()
							.trim().isEmpty())) {
						if (!Helper
								.isEmailAddress(et_stud_familydet_mother_email)) {
							count++;
						}

					}

					if ((!et_stud_familydet_mother_email.getText().toString()
							.trim().isEmpty())
							|| (!et_stud_familydet_mother_contact_no.getText()
									.toString().trim().isEmpty())) {
						ArrayList<Contacts> motherContacts = new ArrayList<Contacts>();
						if (!et_stud_familydet_mother_email.getText()
								.toString().trim().isEmpty()) {
							Contacts emailContact = new Contacts();
							emailContact.setContactCategoryID(1);
							emailContact
									.setContact(et_stud_familydet_mother_email
											.getText().toString().trim());
							emailContact.setContactTypeID(1);
							motherContacts.add(emailContact);
						}
						if (!et_stud_familydet_mother_contact_no.getText()
								.toString().trim().isEmpty()) {
							Contacts phoneContact = new Contacts();
							phoneContact
									.setContactCategoryID(contactTypeAryList
											.get(spn_stud_familydet_mother_contact_type
													.getSelectedItemPosition())
											.getContactCategoryID());
							phoneContact
									.setContact(et_stud_familydet_mother_contact_no
											.getText().toString().trim());
							if (phoneContact.getContactCategoryID() == 2) {
								phoneContact.setContactTypeID(3);
							} else {
								phoneContact.setContactTypeID(5);
							}

							motherContacts.add(phoneContact);
						}
						mother.setContacts(motherContacts);
					}
					parentMother.setSalutationID(salutationAryList.get(
							spn_stud_familydet_mother_salutation
									.getSelectedItemPosition())
							.getSalutationID());

					String query_Wards2 = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND  LocalChildrenID='"
							+ Helper.childrenObject.getChildrenID()
							+ "' and RelationID = 2";
					Cursor dataCursor2 = dbh.getCursorData(this.getActivity(),
							query_Wards2);
					if (dataCursor2 != null && dataCursor2.moveToFirst()) {
						String parent_userid = dataCursor2
								.getString(dataCursor2
										.getColumnIndex("LocalUserID"));
						mother.setUserID(NumUtil.IntegerParse
								.parseInt(parent_userid));
					} else {

					}

					parentMother.setRelationID(2);
					parentMother.setUser(mother);
					parentsList.add(parentMother);

					Users guardian = new Users();
					Childrenparents parentGuardian = new Childrenparents();
					if (et_stud_familydet_guardian_firstname.getText()
							.toString().trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_guardian_firstname,
								"Enter First Name");
					} else {
						guardian.setFirstName(et_stud_familydet_guardian_firstname
								.getText().toString().trim());
					}
					if (et_stud_familydet_guardian_lastname.getText()
							.toString().trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_guardian_lastname,
								"Enter Last Name");
					} else {
						guardian.setLastName(et_stud_familydet_guardian_lastname
								.getText().toString().trim());
					}
					guardian.setMiddleName(et_stud_familydet_guardian_middlename
							.getText().toString().trim());

					if ((!et_stud_familydet_guardian_aadhaar_no.getText()
							.toString().trim().isEmpty())
							&& (et_stud_familydet_guardian_aadhaar_no.getText()
									.length() < 12)) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_guardian_aadhaar_no,
								"Enter Valid Aadhaar No");
					} else {
						guardian.setAadharNumber(et_stud_familydet_guardian_aadhaar_no
								.getText().toString().trim());
					}

					if ((!et_stud_familydet_guardian_email.getText().toString()
							.trim().isEmpty())) {
						if (!Helper
								.isEmailAddress(et_stud_familydet_guardian_email)) {
							count++;
						}

					}

					if ((!et_stud_familydet_guardian_email.getText().toString()
							.trim().isEmpty())
							|| (!et_stud_familydet_guardian_contact_no
									.getText().toString().trim().isEmpty())) {
						ArrayList<Contacts> guardianContacts = new ArrayList<Contacts>();
						if (!et_stud_familydet_guardian_email.getText()
								.toString().trim().isEmpty()) {
							Contacts emailContact = new Contacts();
							emailContact.setContactCategoryID(1);
							emailContact
									.setContact(et_stud_familydet_guardian_email
											.getText().toString().trim());
							emailContact.setContactTypeID(1);
							guardianContacts.add(emailContact);
						}
						if (!et_stud_familydet_guardian_contact_no.getText()
								.toString().trim().isEmpty()) {
							Contacts phoneContact = new Contacts();
							phoneContact
									.setContactCategoryID(contactTypeAryList
											.get(spn_stud_familydet_guardian_contact_type
													.getSelectedItemPosition())
											.getContactCategoryID());
							phoneContact
									.setContact(et_stud_familydet_guardian_contact_no
											.getText().toString().trim());
							if (phoneContact.getContactCategoryID() == 2) {
								phoneContact.setContactTypeID(3);
							} else {
								phoneContact.setContactTypeID(5);
							}

							guardianContacts.add(phoneContact);
						}
						guardian.setContacts(guardianContacts);
					}

					parentGuardian.setSalutationID(salutationAryList.get(
							spn_stud_familydet_guardian_salutation
									.getSelectedItemPosition())
							.getSalutationID());

					String query_Wards3 = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND  LocalChildrenID='"
							+ Helper.childrenObject.getChildrenID()
							+ "' and RelationID = 10";
					Cursor dataCursor3 = dbh.getCursorData(this.getActivity(),
							query_Wards3);
					if (dataCursor3 != null && dataCursor3.moveToFirst()) {
						String parent_userid = dataCursor3
								.getString(dataCursor3
										.getColumnIndex("LocalUserID"));
						guardian.setUserID(NumUtil.IntegerParse
								.parseInt(parent_userid));
					} else {

					}

					parentGuardian.setRelationID(10);
					parentGuardian.setUser(guardian);
					parentsList.add(parentGuardian);

				} else {
					ll_addstu_fatherdetails.setVisibility(View.VISIBLE);
					ll_addstu_motherdetails.setVisibility(View.VISIBLE);
					Users father = new Users();
					Childrenparents parentFather = new Childrenparents();
					if (et_stud_familydet_father_firstname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_father_firstname,
								"Enter First Name");
					} else {
						father.setFirstName(et_stud_familydet_father_firstname
								.getText().toString().trim());
					}
					if (et_stud_familydet_father_lastname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_father_lastname,
								"Enter Last Name");
					} else {
						father.setLastName(et_stud_familydet_father_lastname
								.getText().toString().trim());
					}
					father.setMiddleName(et_stud_familydet_father_middlename
							.getText().toString().trim());
					if ((!et_stud_familydet_father_aadhaar_no.getText()
							.toString().trim().isEmpty())
							&& (et_stud_familydet_father_aadhaar_no.getText()
									.length() < 12)) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_father_aadhaar_no,
								"Enter Valid Aadhaar No");
					} else {
						father.setAadharNumber(et_stud_familydet_father_aadhaar_no
								.getText().toString().trim());
					}

					if ((!et_stud_familydet_father_email.getText().toString()
							.trim().isEmpty())) {
						if (!Helper
								.isEmailAddress(et_stud_familydet_father_email)) {
							count++;
						}
					}
					if ((!et_stud_familydet_father_email.getText().toString()
							.trim().isEmpty())
							|| (!et_stud_familydet_father_contact_no.getText()
									.toString().trim().isEmpty())) {
						ArrayList<Contacts> fatherContacts = new ArrayList<Contacts>();
						if (!et_stud_familydet_father_email.getText()
								.toString().trim().isEmpty()) {
							Contacts emailContact = new Contacts();
							emailContact.setContactCategoryID(1);
							emailContact
									.setContact(et_stud_familydet_father_email
											.getText().toString().trim());
							emailContact.setContactTypeID(1);
							fatherContacts.add(emailContact);
						}
						if (!et_stud_familydet_father_contact_no.getText()
								.toString().trim().isEmpty()) {
							Contacts phoneContact = new Contacts();
							phoneContact
									.setContactCategoryID(contactTypeAryList
											.get(spn_stud_familydet_father_contact_type
													.getSelectedItemPosition())
											.getContactCategoryID());
							phoneContact
									.setContact(et_stud_familydet_father_contact_no
											.getText().toString().trim());
							if (phoneContact.getContactCategoryID() == 2) {
								phoneContact.setContactTypeID(3);
							} else {
								phoneContact.setContactTypeID(5);
							}
							fatherContacts.add(phoneContact);
						}
						father.setContacts(fatherContacts);
					}
					parentFather.setSalutationID(salutationAryList.get(
							spn_stud_familydet_father_salutation
									.getSelectedItemPosition())
							.getSalutationID());

					String query_Wards2 = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND  LocalChildrenID='"
							+ Helper.childrenObject.getChildrenID()
							+ "' and RelationID = 1";
					Cursor dataCursor2 = dbh.getCursorData(this.getActivity(),
							query_Wards2);
					if (dataCursor2 != null && dataCursor2.moveToFirst()) {
						String parent_userid = dataCursor2
								.getString(dataCursor2
										.getColumnIndex("LocalUserID"));
						father.setUserID(NumUtil.IntegerParse
								.parseInt(parent_userid));
					} else {

					}

					parentFather.setRelationID(1);
					parentFather.setUser(father);
					parentsList.add(parentFather);

					Users mother = new Users();
					Childrenparents parentMother = new Childrenparents();
					if (et_stud_familydet_mother_firstname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_mother_firstname,
								"Enter First Name");
					} else {
						mother.setFirstName(et_stud_familydet_mother_firstname
								.getText().toString().trim());

					}
					if (et_stud_familydet_mother_lastname.getText().toString()
							.trim().isEmpty()) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_mother_lastname,
								"Enter Last Name");
					} else {
						mother.setLastName(et_stud_familydet_mother_lastname
								.getText().toString().trim());
					}
					mother.setMiddleName(et_stud_familydet_mother_middlename
							.getText().toString().trim());

					if ((!et_stud_familydet_mother_aadhaar_no.getText()
							.toString().trim().isEmpty())
							&& (et_stud_familydet_mother_aadhaar_no.getText()
									.length() < 12)) {
						count++;
						Helper.displayErrorMsg(
								et_stud_familydet_mother_aadhaar_no,
								"Enter Valid Aadhaar No");
					} else {
						mother.setAadharNumber(et_stud_familydet_mother_aadhaar_no
								.getText().toString().trim());
					}

					if ((!et_stud_familydet_mother_email.getText().toString()
							.trim().isEmpty())) {
						if (!Helper
								.isEmailAddress(et_stud_familydet_mother_email)) {
							count++;
						}
					}
					if ((!et_stud_familydet_mother_email.getText().toString()
							.trim().isEmpty())
							|| (!et_stud_familydet_mother_contact_no.getText()
									.toString().trim().isEmpty())) {
						ArrayList<Contacts> motherContacts = new ArrayList<Contacts>();
						if (!et_stud_familydet_mother_email.getText()
								.toString().trim().isEmpty()) {
							Contacts emailContact = new Contacts();
							emailContact.setContactCategoryID(1);
							emailContact
									.setContact(et_stud_familydet_mother_email
											.getText().toString().trim());
							emailContact.setContactTypeID(1);
							motherContacts.add(emailContact);
						}
						if (!et_stud_familydet_mother_contact_no.getText()
								.toString().trim().isEmpty()) {
							Contacts phoneContact = new Contacts();
							phoneContact
									.setContactCategoryID(contactTypeAryList
											.get(spn_stud_familydet_mother_contact_type
													.getSelectedItemPosition())
											.getContactCategoryID());
							phoneContact
									.setContact(et_stud_familydet_mother_contact_no
											.getText().toString().trim());
							if (phoneContact.getContactCategoryID() == 2) {
								phoneContact.setContactTypeID(3);
							} else {
								phoneContact.setContactTypeID(5);
							}

							motherContacts.add(phoneContact);
						}
						mother.setContacts(motherContacts);
					}

					parentMother.setSalutationID(salutationAryList.get(
							spn_stud_familydet_mother_salutation
									.getSelectedItemPosition())
							.getSalutationID());
					String query_Wards4 = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND  LocalChildrenID='"
							+ Helper.childrenObject.getChildrenID()
							+ "' and RelationID = 2";
					Cursor dataCursor4 = dbh.getCursorData(this.getActivity(),
							query_Wards4);
					if (dataCursor4 != null && dataCursor4.moveToFirst()) {
						String parent_userid = dataCursor4
								.getString(dataCursor4
										.getColumnIndex("LocalUserID"));
						mother.setUserID(NumUtil.IntegerParse
								.parseInt(parent_userid));
					} else {

					}

					parentMother.setRelationID(2);
					parentMother.setUser(mother);
					parentsList.add(parentMother);

				}
			} else if ((et_stud_familydet_father_firstname.getText().toString()
					.trim().isEmpty() && et_stud_familydet_mother_firstname
					.getText().toString().trim().isEmpty())
					&& (guardianCheck)) {
				ll_stud_family_guardianlayout.setVisibility(View.VISIBLE);
				if (Helper.childrenObject.getParentAry() == null) {
					guardian = new Users();
				} else {
					guardian = Helper.childrenObject.getParentAry().get(0)
							.getUser();
				}
				Childrenparents parentGuardian = new Childrenparents();

				if (et_stud_familydet_guardian_firstname.getText().toString()
						.trim().isEmpty()) {
					count++;
					Helper.displayErrorMsg(
							et_stud_familydet_guardian_firstname,
							"Enter First Name");
				} else {
					guardian.setFirstName(et_stud_familydet_guardian_firstname
							.getText().toString().trim());
				}
				if (et_stud_familydet_guardian_lastname.getText().toString()
						.trim().isEmpty()) {
					count++;
					Helper.displayErrorMsg(et_stud_familydet_guardian_lastname,
							"Enter Last Name");
				} else {
					guardian.setLastName(et_stud_familydet_guardian_lastname
							.getText().toString().trim());
				}
				guardian.setMiddleName(et_stud_familydet_guardian_middlename
						.getText().toString().trim());

				if ((!et_stud_familydet_guardian_aadhaar_no.getText()
						.toString().trim().isEmpty())
						&& (et_stud_familydet_guardian_aadhaar_no.getText()
								.length() < 12)) {
					count++;
					Helper.displayErrorMsg(
							et_stud_familydet_guardian_aadhaar_no,
							"Enter Valid Aadhaar No");
				} else {
					guardian.setAadharNumber(et_stud_familydet_guardian_aadhaar_no
							.getText().toString().trim());
				}

				if ((!et_stud_familydet_guardian_email.getText().toString()
						.trim().isEmpty())) {

					if (!Helper
							.isEmailAddress(et_stud_familydet_guardian_email)) {
						count++;
					}
				}
				if ((!et_stud_familydet_guardian_email.getText().toString()
						.trim().isEmpty())
						|| (!et_stud_familydet_guardian_contact_no.getText()
								.toString().trim().isEmpty())) {
					ArrayList<Contacts> guardianContacts = new ArrayList<Contacts>();
					if (!et_stud_familydet_guardian_email.getText().toString()
							.trim().isEmpty()) {
						Contacts emailContact = new Contacts();
						emailContact.setContactCategoryID(1);
						emailContact
								.setContact(et_stud_familydet_guardian_email
										.getText().toString().trim());
						emailContact.setContactTypeID(1);
						guardianContacts.add(emailContact);
					}
					if (!et_stud_familydet_guardian_contact_no.getText()
							.toString().trim().isEmpty()) {
						Contacts phoneContact = new Contacts();
						phoneContact.setContactCategoryID(contactTypeAryList
								.get(spn_stud_familydet_guardian_contact_type
										.getSelectedItemPosition())
								.getContactCategoryID());
						phoneContact
								.setContact(et_stud_familydet_guardian_contact_no
										.getText().toString().trim());
						if (phoneContact.getContactCategoryID() == 2) {
							phoneContact.setContactTypeID(3);
						} else {
							phoneContact.setContactTypeID(5);
						}

						guardianContacts.add(phoneContact);
					}
					guardian.setContacts(guardianContacts);
				}

				parentGuardian.setSalutationID(salutationAryList.get(
						spn_stud_familydet_guardian_salutation
								.getSelectedItemPosition()).getSalutationID());

				String query_Wards2 = "SELECT LocalUserID FROM  childrenparents CP where  CP.IsDeleted!=1  AND  LocalChildrenID='"
						+ Helper.childrenObject.getChildrenID()
						+ "' and RelationID = 10";
				Cursor dataCursor2 = dbh.getCursorData(this.getActivity(),
						query_Wards2);
				if (dataCursor2 != null && dataCursor2.moveToFirst()) {
					String parent_userid = dataCursor2.getString(dataCursor2
							.getColumnIndex("LocalUserID"));
					guardian.setUserID(NumUtil.IntegerParse.parseInt(parent_userid));
				} else {

				}

				parentGuardian.setRelationID(10);
				parentGuardian.setUser(guardian);
				parentsList.add(parentGuardian);

			}

			if (count == 0) {
				AddStudentActivityDialog.tabFlags[2] = true;
				// AddStudentFragment.tabFlags[3] = true;
				AddStudentActivityDialog
						.enableHeaderClick1(AddStudentActivityDialog.tabFlags);

				if (Helper.childrenObject.getParentAry() != null)
					for (int i = 0; i < Helper.childrenObject.getParentAry()
							.size(); i++) {
						Childrenparents childrenparents = Helper.childrenObject
								.getParentAry().get(i);
						for (int j = 0; j < parentsList.size(); j++) {
							if (childrenparents.getRelationID() == parentsList
									.get(j).getRelationID()) {
								parentsList
										.get(j)
										.getUser()
										.setContactID(
												childrenparents.getUser()
														.getContactID());
							}
						}
					}

				Helper.childrenObject.setParentAry(parentsList);

				Helper.updateHeaderFromNext(getActivity(),
						tv_editstu_family_details, tv_editstu_Address,
						R.drawable.headerbg, R.drawable.headerbg_selectced);
				// bundle.putInt("InsituteID", instituteId);
				if (AddStudentActivityDialog.fragmentArr[3] == null)
					AddStudentActivityDialog.fragmentArr[3] = fragment = new EditStuAddress();
				else
					fragment = AddStudentActivityDialog.fragmentArr[3];
				// fragment.setArguments(bundle);
				if (fragment != null) {
					android.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
				}
				System.out.println("family obj:::::"
						+ Helper.childrenObject.getFirstName());
			}
			final InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		} else if (v == btn_stud_familydetails_fatherdet) {
			if (ll_addstu_fatherdetails.getVisibility() == View.GONE) {
				if (ll_addstu_motherdetails.getVisibility() == View.VISIBLE) {
					collapse(ll_addstu_motherdetails);
					expand(ll_addstu_fatherdetails);
				} else {
					expand(ll_addstu_fatherdetails);
				}
			} else {
				collapse(ll_addstu_fatherdetails);
			}

		} else if (v == btn_stud_familydetails_motherdet) {
			if (ll_addstu_motherdetails.getVisibility() == View.GONE) {
				if (ll_addstu_fatherdetails.getVisibility() == View.VISIBLE) {
					collapse(ll_addstu_fatherdetails);

					expand(ll_addstu_motherdetails);
				} else {
					expand(ll_addstu_motherdetails);
				}
			} else {
				collapse(ll_addstu_motherdetails);
			}

		}
	}

	/**
	 * disable view
	 * 
	 */
	private void collapse(final LinearLayout ll_collapse_layout) {
		ll_collapse_layout.setVisibility(View.GONE);
	}

	/**
	 * enable view
	 * 
	 */
	private void expand(LinearLayout ll_expand_layout) {
		ll_expand_layout.setVisibility(View.VISIBLE);
	}

}
