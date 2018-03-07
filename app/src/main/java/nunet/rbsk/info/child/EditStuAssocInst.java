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
//* Name   :  EditStuAssocInst

//* Type    : Fragment

//* Description     : Functionality to add Institute for student
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
//3.0		   01-05-2015	        Self Review(Kiruthika)		No Observation
//3.0		   02-05-2015 			Deepika(Peer review)		Contact details not updated.
//3.0		   02-05-2015 			Anil(Peer review)			1.	Contacts are not updated to DB.
//*****************************************************************************

import java.util.ArrayList;
import java.util.Calendar;

import nunet.adapter.CustomStudentAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Address;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.ChildrenDisabilities;
import nunet.rbsk.model.ChildrenInstitutes;
import nunet.rbsk.model.Childrenparents;
import nunet.rbsk.model.Classes;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.Institute;
import nunet.rbsk.model.Sections;
import nunet.rbsk.model.Users;
import nunet.rbsk.screening.ScreeningActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class EditStuAssocInst extends Fragment implements OnClickListener {
  private EditText et_edit_student_asso_inst_inst_name;
  private EditText et_edit_student_asso_inst_admission_date;
  private EditText et_edit_student_asso_inst_admission_no;
  private EditText et_edit_student_asso_inst_roll_no;
  private Spinner spn_edit_student_asso_inst_class;
  private Spinner spn_edit_student_asso_inst_section;
  private Button btn_edit_student_asso_inst_save;
  private Cursor cur;
  private Institute instModelObject;
  ArrayList<Classes> classAryList;
  ArrayList<Sections> sectionAryList;
  Sections sectionModelObject;
  Classes classesModelObject;
  int sectionID;
  int classID;
  DBHelper dbh;
  private LinearLayout ll_editstu_ass_hide;

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.editstu_asso_inst, container,
      false);
    getActivity().getWindow().setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    dbh = DBHelper.getInstance(this.getActivity());
    findViews(rootView);
    // instituteId = getArguments().getInt("InsituteID");
    getClassDataFromDB();
    getNameFromDB();
    // et_edit_student_asso_inst_inst_name.setText(instModelObject.getInstituteName());
    ArrayAdapter<String> adp_spnClass = new ArrayAdapter<String>(
      this.getActivity(), android.R.layout.simple_spinner_item);
    adp_spnClass
      .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    for (int i = 0; i < classAryList.size(); i++) {
      adp_spnClass.add(classAryList.get(i).getClassName());
    }
    spn_edit_student_asso_inst_class.setAdapter(adp_spnClass);

    getSectionDataFromDB();
    ArrayAdapter<String> adp_spnSection = new ArrayAdapter<String>(
      this.getActivity(), android.R.layout.simple_spinner_item);
    adp_spnSection
      .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    for (int i = 0; i < sectionAryList.size(); i++) {
      adp_spnSection.add(sectionAryList.get(i).getSectionName());
    }
    spn_edit_student_asso_inst_section.setAdapter(adp_spnSection);

    spn_edit_student_asso_inst_class
      .setOnItemSelectedListener(new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int position, long id) {
          classID = classAryList.get(position).getClassID();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
      });
    spn_edit_student_asso_inst_section
      .setOnItemSelectedListener(new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int position, long id) {
          sectionID = sectionAryList.get(position).getSectionID();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
      });

    if (Helper.childrenObject.getChildrenInsitute() == null) {
      ChildrenInstitutes childInst = new ChildrenInstitutes();
      Helper.childrenObject.setChildrenInsitute(childInst);
    } else {
      ChildrenInstitutes childInst = Helper.childrenObject
        .getChildrenInsitute();
      et_edit_student_asso_inst_admission_date.setText(childInst
        .getAdmissionDate());
      et_edit_student_asso_inst_admission_no.setText(childInst
        .getAdmissionNumber());
      et_edit_student_asso_inst_roll_no
        .setText(childInst.getRollNumber());
      classID = childInst.getClassID();
      sectionID = childInst.getSectionID();
      // spn_edit_student_asso_inst_class.setSelection(position)
      setDataToSpinner(true);
    }
    return rootView;
  }

  /**
   * set data to spinner from DB
   */
  private void setDataToSpinner(boolean b) {
    if (b) {
      for (int i = 0; i < classAryList.size(); i++) {
        if (classAryList.get(i).getClassID() == classID) {
          spn_edit_student_asso_inst_class.setSelection(i, true);
          break;
        }
      }

      for (int i = 0; i < sectionAryList.size(); i++) {
        if (sectionAryList.get(i).getSectionID() == sectionID) {
          spn_edit_student_asso_inst_section.setSelection(i, true);
          break;
        }
      }
    }
  }

  /**
   * get institute name from DB
   */
  private void getNameFromDB() {

    String query_for_name = "select InstituteName, InstituteTypeId from institutes I where  I.IsDeleted!=1  AND  LocalInstituteID = '"
      + Helper.selectedLocalInstituteID + "'";
    cur = dbh.getCursorData(this.getActivity(), query_for_name);

    if (cur != null) {
      try {
        if (cur.moveToFirst()) {
          do {
            instModelObject = new Institute();
            instModelObject.setInstituteName(cur.getString(cur
              .getColumnIndex("InstituteName")));
            instModelObject.setInstituteTypeId(NumUtil.IntegerParse
              .parseInt(cur.getString(cur
                .getColumnIndex("InstituteTypeId"))));

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }

    }

    et_edit_student_asso_inst_inst_name.setText(instModelObject
      .getInstituteName());

    if (instModelObject.getInstituteTypeId() == 1) {
      ll_editstu_ass_hide.setVisibility(View.INVISIBLE);

    }

  }

  /**
   * get data from DB
   */
  private void getSectionDataFromDB() {

    String query = "select * from sections S Where  S.IsDeleted!=1  ";
    cur = dbh.getCursorData(this.getActivity(), query);

    sectionAryList = new ArrayList<Sections>();
    sectionModelObject = new Sections();
    sectionModelObject.setSectionID(0);
    sectionModelObject.setSectionName("--Select--");
    sectionAryList.add(sectionModelObject);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {
          do {
            sectionModelObject = new Sections();
            sectionModelObject.setSectionID(NumUtil.IntegerParse
              .parseInt(cur.getString(cur
                .getColumnIndex("SectionID"))));
            sectionModelObject.setSectionName(cur.getString(cur
              .getColumnIndex("DisplayText")));

            sectionAryList.add(sectionModelObject);
          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

  }

  /**
   * class data from DB
   */
  private void getClassDataFromDB() {
    String query = "select * from classes C Where  C.IsDeleted!=1 ";
    cur = dbh.getCursorData(this.getActivity(), query);

    classAryList = new ArrayList<Classes>();
    classesModelObject = new Classes();
    classesModelObject.setClassID(0);
    classesModelObject.setClassName("--Select--");
    classAryList.add(classesModelObject);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {
          do {
            classesModelObject = new Classes();
            classesModelObject.setClassID(NumUtil.IntegerParse
              .parseInt(cur.getString(cur
                .getColumnIndex("ClassID"))));
            classesModelObject.setClassName(cur.getString(cur
              .getColumnIndex("DisplayText")));
            classAryList.add(classesModelObject);
          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

  }

  /**
   * get Id's from R.java
   */
  private void findViews(View rootView) {
    et_edit_student_asso_inst_inst_name = (EditText) rootView
      .findViewById(R.id.et_edit_student_asso_inst_inst_name);
    et_edit_student_asso_inst_admission_date = (EditText) rootView
      .findViewById(R.id.et_edit_student_asso_inst_admission_date);
    et_edit_student_asso_inst_admission_date.setOnClickListener(this);
    et_edit_student_asso_inst_admission_date.setText(Helper
      .getTodayDate("yyyy-MM-dd"));
    et_edit_student_asso_inst_admission_no = (EditText) rootView
      .findViewById(R.id.et_edit_student_asso_inst_admission_no);
    ll_editstu_ass_hide = (LinearLayout) rootView
      .findViewById(R.id.ll_editstu_ass_hide);

    et_edit_student_asso_inst_roll_no = (EditText) rootView
      .findViewById(R.id.et_edit_student_asso_inst_roll_no);
    spn_edit_student_asso_inst_class = (Spinner) rootView
      .findViewById(R.id.spn_edit_student_asso_inst_class);
    spn_edit_student_asso_inst_section = (Spinner) rootView
      .findViewById(R.id.spn_edit_student_asso_inst_section);

    btn_edit_student_asso_inst_save = (Button) rootView
      .findViewById(R.id.btn_edit_student_asso_inst_save);
    btn_edit_student_asso_inst_save.setOnClickListener(this);

  }

  /*
     * click event for viees
     */
  @Override
  public void onClick(View v) {

    if (v == et_edit_student_asso_inst_admission_date) {
      DialogFragment newFragment = new Helper.SelectDateFragment(
        et_edit_student_asso_inst_admission_date);
      newFragment.show(getFragmentManager(), "DatePicker");
      return;
    }
    try {
      if (v == btn_edit_student_asso_inst_save) {
        int errorCount = 0;
        String string = et_edit_student_asso_inst_admission_no
          .getText().toString();
        int length = string.trim().length();
        if ((!TextUtils.isEmpty(string.trim())) && (length < 1)) {
          errorCount++;
          Helper.displayErrorMsg(
            et_edit_student_asso_inst_admission_no,
            "Enter valid Admission number");
        }

        String string2 = et_edit_student_asso_inst_roll_no.getText()
          .toString();
        if (instModelObject.getInstituteTypeId() != 1) {
          int length2 = string2.trim().length();
          if ((!TextUtils.isEmpty(string2.trim())) && (length2 < 1)) {
            errorCount++;
            Helper.displayErrorMsg(
              et_edit_student_asso_inst_roll_no,
              "Enter valid Roll number");
          }
          // if
          // (spn_edit_student_asso_inst_class.getSelectedItemPosition()
          // == 0) {
          // errorCount++;
          // Helper.setErrorForSpinner(spn_edit_student_asso_inst_class,
          // "Select Class");
          // }
          // if (spn_edit_student_asso_inst_section
          // .getSelectedItemPosition() == 0) {
          // errorCount++;
          // Helper.setErrorForSpinner(
          // spn_edit_student_asso_inst_section,
          // "Select Section");
          // }
        }
        if (errorCount == 0) {
          // AddStudentFragment.tabFlags[4] = true;
          ChildrenInstitutes childInst = Helper.childrenObject
            .getChildrenInsitute();
          childInst
            .setAdmissionDate(et_edit_student_asso_inst_admission_date
              .getText().toString().trim());
          childInst.setAdmissionNumber(string.trim());
          childInst.setRollNumber(string2.trim());
          childInst.setClassID(classID);
          childInst.setSectionID(sectionID);
          Helper.childrenObject.setChildrenInsitute(childInst);

          // insert to DB
          Children childObject = Helper.childrenObject;
          // Users childUser = childObject.getUser();
          Address childUserAddress = childObject.getAddress();
          if (Helper.addChildFlag) {// Editing of Student data

            // ***Children Insertion
            // In basic info contactfni
            long newContactId = childObject.getEmgContactID();

            if (newContactId == 0) {
              newContactId = dbh.insertintoTable(
                this.getActivity(), "contacts",
                new String[]{"isDeleted "},
                new String[]{"0"});
            }
            childObject.setContactID((int) newContactId);
            boolean isEmcUpdate = dbh
              .updateROW(
                getActivity(),
                "contactdetails",
                new String[]{"ContactTypeID",
                  "Contact"},
                new String[]{"6",
                  childObject.getEmgContact()},
                "LocalContactID",
                String.valueOf(newContactId));

            if (!isEmcUpdate)
              dbh.insertintoTable(this.getActivity(),
                "contactdetails", new String[]{
                  "LocalContactID", "ContactTypeID",
                  "Contact"},
                new String[]{
                  String.valueOf(newContactId), "6",
                  childObject.getEmgContact()});

            // *** Address insertion for child(Usertype = 1)

            long addressId = dbh.insertintoTable(
              this.getActivity(),
              "address",
              new String[]{"StateID", "DistrictID",
                "MandalID", "VillageID",
                "HabitationID", "PINCode",
                "AddressLine1", "AddressLine2"},
              new String[]{
                childUserAddress.getState()
                  .getStateID() + "".trim(),
                childUserAddress.getDistrict()
                  .getDistrictID() + "".trim(),
                childUserAddress.getMandal()
                  .getMandalID() + "".trim(),
                childUserAddress.getVillage()
                  .getVillageID() + "".trim(),
                childUserAddress.getHabitation()
                  .getHabitatID() + "".trim(),
                childUserAddress.getPINCode()
                  + "".trim(),
                childUserAddress.getAddressLine1()
                  + "".trim(),
                childUserAddress.getAddressLine2()
                  + "".trim()});
            childObject.setLocalAddressID("" + addressId);
            // *** User Insertion for Usertype = 1 i.e child
            long userId = dbh
              .insertintoTable(
                this.getActivity(),
                "users",
                new String[]{"UserTypeID",
                  "FirstName", "MiddleName",
                  "LastName", "GenderID",
                  "DateOfBirth", "AadharNumber",
                  "ReligionID", "CasteID",
                  "HealthCardNumber",
                  "RationCardNumber", "StatusID",
                  "LocalAddressID",
                  "LocalContactID"},
                new String[]{
                  "1",
                  childObject.getFirstName(),
                  childObject.getMiddleName(),
                  childObject.getLastName(),
                  childObject.getGenderID()
                    + "".trim(),
                  childObject.getDateOfBirth(),
                  childObject.getAadharNumber(),
                  childObject.getReligionID()
                    + "".trim(),
                  childObject.getCasteID()
                    + "".trim(),
                  childObject
                    .getHealthCardNumber(),
                  childObject
                    .getRationCardNumber(),
                  "2", addressId + "".trim(),
                  newContactId + "".trim()});

            childObject.setUserID(userId);

            // Permanent Address insertion
            long permanentAddressID = 0;

            Address permanentAddress = null;
            if (!childObject.isSameAddress()) {
              permanentAddress = childObject
                .getPermanentAddress();

            } else {
              permanentAddress = childObject.getAddress();
            }

            permanentAddressID = dbh.insertintoTable(
              this.getActivity(),
              "address",
              new String[]{"StateID", "DistrictID",
                "MandalID", "VillageID",
                "HabitationID", "PINCode",
                "AddressLine1", "AddressLine2"},
              new String[]{
                permanentAddress.getState()
                  .getStateID() + "".trim(),
                permanentAddress.getDistrict()
                  .getDistrictID() + "".trim(),
                permanentAddress.getMandal()
                  .getMandalID() + "".trim(),
                permanentAddress.getVillage()
                  .getVillageID() + "".trim(),
                permanentAddress.getHabitation()
                  .getHabitatID() + "".trim(),
                permanentAddress.getPINCode()
                  + "".trim(),
                permanentAddress.getAddressLine1()
                  + "".trim(),
                permanentAddress.getAddressLine2()
                  + "".trim()});
            childObject.setPermanentAddressID(""
              + permanentAddressID);

            // children table insertion
            // MCTS id comes from service

            long childId = dbh.insertintoTable(
              this.getActivity(),
              "children",
              new String[]{
                "LocalUserID",
                "IdentificationMark1",
                "LocalPermanentAddressID",
                "HasDisability",
                "PWDCardNumber",
                "StatusID",
                "ChildrenStatusID",
                // sand: added from
                // childreninstitute
                "LocalInstituteID", "AdmissionDate",
                "AdmissionNumber", "ClassID",
                "SectionID", "RollNumber"},
              new String[]{
                userId + "".trim(),
                childObject.getIdentificationMark1(),
                permanentAddressID + "".trim(),
                String.valueOf(Helper.childrenObject
                  .getHasDisability()),
                Helper.childrenObject
                  .getPWDCardNumber(),
                "2",
                "1",

                // sand: added from
                // childreninstitute
                Helper.selectedLocalInstituteID
                  + "".trim(),
                childInst.getAdmissionDate(),
                childInst.getAdmissionNumber(),
                childInst.getClassID() + "".trim(),
                childInst.getSectionID() + "".trim(),
                childInst.getRollNumber()});
            childObject.setChildrenID((int) childId);

            // /setting of local to child id////****************
            /*
						 * dbh.updateROW(this.getActivity(), "children", new
						 * String[] { "LocalChildrenID" }, new String[] { "" +
						 * childId }, "LocalChildrenID", "" + childId);
						 */

            ArrayList<ChildrenDisabilities> childDisabilty = Helper.childrenObject
              .getChildDisabilities();
            if (childDisabilty != null) {
              for (int i = 0; i < childDisabilty.size(); i++) {
                dbh.insertintoTable(
                  this.getActivity(),
                  "childrendisabilities",
                  new String[]{"LocalChildrenID",
                    "DisabilityTypeID",
                    "DisabilityPercentage",
                    "SpecificCondition"},
                  new String[]{
                    childId + "".trim(),
                    String.valueOf(childDisabilty
                      .get(i)
                      .getDisabilityType()
                      .getDisabilityTypeID()),
                    childDisabilty
                      .get(i)
                      .getDisabilityPercentage(),
                    childDisabilty.get(i)
                      .getSpecificCondition(),});
              }
            }
            // as of now permenent address also as present address
            // disability type Yes-1, No-0

            // children institute table insertion

            // sand: commeted bcz childreninstitutes moved to
            // children
            // long a = dbh.insertintoTable(
            // this.getActivity(),
            // "childreninstitutes",
            // new String[] { "LocalChildrenID",
            // "LocalInstituteID", "AdmissionDate",
            // "AdmissionNumber", "ClassID", "SectionID",
            // "RollNumber" },
            // new String[] { childId + "".trim(),
            // Helper.selectedInstituteId + "".trim(),
            // childInst.getAdmissionDate(),
            // childInst.getAdmissionNumber(),
            // childInst.getClassID() + "".trim(),
            // childInst.getSectionID() + "".trim(),
            // childInst.getRollNumber() });

            // add parent details to children parents table
            ArrayList<Childrenparents> parents = Helper.childrenObject
              .getParentAry();
            for (int pitem = 0; pitem < parents.size(); pitem++) {
              Childrenparents parent = parents.get(pitem);
              long parentContactID = 0;
              Users parentUser = parent.getUser();
              if (parentUser.getContacts() != null) {
                ArrayList<Contacts> contactData = parentUser
                  .getContacts();
                if (contactData.size() > 0) {
                  parentContactID = dbh.insertintoTable(
                    this.getActivity(), "contacts",
                    new String[]{"ContactID"},
                    new String[]{""});
                  for (Contacts parentContact : contactData) {
                    dbh.insertintoTable(
                      this.getActivity(),
                      "contactdetails",
                      new String[]{
                        "LocalContactID",
                        "ContactTypeID",
                        "Contact"},
                      new String[]{
                        parentContactID
                          + "".trim(),
                        parentContact
                          .getContactTypeID()
                          + "".trim(),
                        parentContact
                          .getContact()});
                  }
                }
              }
              String GenderID = "1";

              if (pitem == 0)
                GenderID = "1";
              else if (pitem == 1)
                GenderID = "2";

              long parentUserId = dbh.insertintoTable(
                this.getActivity(),
                "users",
                new String[]{"UserTypeID", "FirstName",
                  "MiddleName", "LastName",
                  "AadharNumber", "SalutationID",
                  "StatusID", "LocalContactID",
                  "GenderID"},
                new String[]{
                  "2",
                  parentUser.getFirstName(),
                  parentUser.getMiddleName(),
                  parentUser.getLastName(),
                  parentUser.getAadharNumber(),
                  parent.getSalutationID()
                    + "".trim(), "2",
                  parentContactID + "".trim(),
                  GenderID});

              dbh.insertintoTable(
                this.getActivity(),
                "childrenparents",
                new String[]{"LocalChildrenID",
                  "LocalUserID", "RelationID"},
                new String[]{childId + "".trim(),
                  parentUserId + "".trim(),
                  parent.getRelationID() + "".trim()});
            }
            Helper.showShortToast(getActivity(),
              "Student added successfully!");
            if (CustomStudentAdapter.lastSelectedPosition != -1
              && CustomStudentAdapter.lastSelectedPosition < ScreeningActivity.childrenList
              .size())
              ScreeningActivity.childrenList.get(
                CustomStudentAdapter.lastSelectedPosition)
                .setSelected(false);
            ScreeningActivity.performClickOnZero = true;
            ScreeningActivity.childrenList.add(0, childObject);
          } else {// Update of Student data
            // In basic info contact
            long newContactId = childObject.getEmgContactID();

            if (newContactId == 0) {
              newContactId = dbh.insertintoTable(
                this.getActivity(), "contacts",
                new String[]{"isDeleted "},
                new String[]{"0"});
            }
            childObject.setContactID((int) newContactId);
            boolean isEmcUpdate = dbh
              .updateROW(
                getActivity(),
                "contactdetails",
                new String[]{"ContactTypeID",
                  "Contact"},
                new String[]{"6",
                  childObject.getEmgContact()},
                "LocalContactID",
                String.valueOf(newContactId));

            if (!isEmcUpdate)
              dbh.insertintoTable(this.getActivity(),
                "contactdetails", new String[]{
                  "LocalContactID", "ContactTypeID",
                  "Contact"},
                new String[]{
                  String.valueOf(newContactId), "6",
                  childObject.getEmgContact()});

            // sand: Address insertion for child(Usertype = 1)
            boolean add_flag = dbh.updateROWByValues(
              this.getActivity(),
              "address",
              new String[]{"StateID", "DistrictID",
                "MandalID", "VillageID",
                "HabitationID", "PINCode",
                "AddressLine1", "AddressLine2"},
              new String[]{
                childUserAddress.getState()
                  .getStateID() + "".trim(),
                childUserAddress.getDistrict()
                  .getDistrictID() + "".trim(),
                childUserAddress.getMandal()
                  .getMandalID() + "".trim(),
                childUserAddress.getVillage()
                  .getVillageID() + "".trim(),
                childUserAddress.getHabitation()
                  .getHabitatID() + "".trim(),
                childUserAddress.getPINCode()
                  + "".trim(),
                childUserAddress.getAddressLine1()
                  + "".trim(),
                childUserAddress.getAddressLine2()
                  + "".trim()},
              new String[]{"LocalAddressID"},
              new String[]{Helper.childrenObject
                .getAddress().getAddressID()
                + "".trim()});
            if (!add_flag) {
              long addressId = dbh
                .insertintoTable(
                  this.getActivity(),
                  "address",
                  new String[]{"StateID",
                    "DistrictID", "MandalID",
                    "VillageID",
                    "HabitationID", "PINCode",
                    "AddressLine1",
                    "AddressLine2"},
                  new String[]{
                    childUserAddress.getState()
                      .getStateID()
                      + "".trim(),
                    childUserAddress
                      .getDistrict()
                      .getDistrictID()
                      + "".trim(),
                    childUserAddress
                      .getMandal()
                      .getMandalID()
                      + "".trim(),
                    childUserAddress
                      .getVillage()
                      .getVillageID()
                      + "".trim(),
                    childUserAddress
                      .getHabitation()
                      .getHabitatID()
                      + "".trim(),
                    childUserAddress
                      .getPINCode()
                      + "".trim(),
                    childUserAddress
                      .getAddressLine1()
                      + "".trim(),
                    childUserAddress
                      .getAddressLine2()
                      + "".trim()});
              childUserAddress.setAddressID((int) addressId);
              childObject.setAddress(childUserAddress);
              Helper.childrenObject.setAddress(childUserAddress);
            }

            // *** User Insertion for Usertype = 1 i.e child
            @SuppressWarnings("unused")
            boolean user_flag = dbh
              .updateROWByValues(
                this.getActivity(),
                "users",
                new String[]{"UserTypeID",
                  "FirstName", "MiddleName",
                  "LastName", "GenderID",
                  "DateOfBirth", "AadharNumber",
                  "ReligionID", "CasteID",
                  "HealthCardNumber",
                  "RationCardNumber", "StatusID",
                  "LocalContactID",
                  "LocalAddressID"},
                new String[]{
                  "1",
                  childObject.getFirstName(),
                  childObject.getMiddleName(),
                  childObject.getLastName(),
                  childObject.getGenderID()
                    + "".trim(),
                  childObject.getDateOfBirth(),
                  childObject.getAadharNumber(),
                  childObject.getReligionID()
                    + "".trim(),
                  childObject.getCasteID()
                    + "".trim(),
                  childObject
                    .getHealthCardNumber(),
                  childObject
                    .getRationCardNumber(),
                  "2",
                  childObject.getContactID()
                    + "".trim(),
                  Helper.childrenObject
                    .getAddress()
                    .getAddressID()
                    + "".trim()},
                new String[]{"LocalUserID"},
                new String[]{Helper.childrenObject
                  .getUserID() + "".trim()});

            long permanentAddressID = 0;

            Address permanentAddress = childObject
              .getPermanentAddress();

            if (permanentAddress != null
              && !TextUtils.equals(
              childObject.getPermanentAddressID(),
              "0")) {
              permanentAddressID = permanentAddress
                .getAddressID();
            } else {

              if (childObject.isSameAddress()
                || permanentAddress == null) {
                permanentAddress = childObject.getAddress();
              } else {
                permanentAddress = childObject
                  .getPermanentAddress();
              }
            }

            boolean isPAddressUpdate = false;

            if (permanentAddressID == 0)
              permanentAddressID = NumUtil.IntegerParse
                .parseInt(childObject
                  .getPermanentAddressID());

            if (permanentAddressID != 0)
              isPAddressUpdate = dbh
                .updateROWByValues(
                  this.getActivity(),
                  "address",
                  new String[]{"StateID",
                    "DistrictID", "MandalID",
                    "VillageID",
                    "HabitationID", "PINCode",
                    "AddressLine1",
                    "AddressLine2"},
                  new String[]{
                    permanentAddress.getState()
                      .getStateID()
                      + "".trim(),
                    permanentAddress
                      .getDistrict()
                      .getDistrictID()
                      + "".trim(),
                    permanentAddress
                      .getMandal()
                      .getMandalID()
                      + "".trim(),
                    permanentAddress
                      .getVillage()
                      .getVillageID()
                      + "".trim(),
                    permanentAddress
                      .getHabitation()
                      .getHabitatID()
                      + "".trim(),
                    permanentAddress
                      .getPINCode()
                      + "".trim(),
                    permanentAddress
                      .getAddressLine1()
                      + "".trim(),
                    permanentAddress
                      .getAddressLine2()
                      + "".trim()},
                  new String[]{"LocalAddressID"},
                  new String[]{String
                    .valueOf(permanentAddressID)});

            if (!isPAddressUpdate)
              permanentAddressID = dbh
                .insertintoTable(
                  this.getActivity(),
                  "address",
                  new String[]{"StateID",
                    "DistrictID", "MandalID",
                    "VillageID",
                    "HabitationID", "PINCode",
                    "AddressLine1",
                    "AddressLine2"},
                  new String[]{
                    permanentAddress.getState()
                      .getStateID()
                      + "".trim(),
                    permanentAddress
                      .getDistrict()
                      .getDistrictID()
                      + "".trim(),
                    permanentAddress
                      .getMandal()
                      .getMandalID()
                      + "".trim(),
                    permanentAddress
                      .getVillage()
                      .getVillageID()
                      + "".trim(),
                    permanentAddress
                      .getHabitation()
                      .getHabitatID()
                      + "".trim(),
                    permanentAddress
                      .getPINCode()
                      + "".trim(),
                    permanentAddress
                      .getAddressLine1()
                      + "".trim(),
                    permanentAddress
                      .getAddressLine2()
                      + "".trim()});

            @SuppressWarnings("unused")
            boolean children_flag = dbh
              .updateROW(
                this.getActivity(),
                "children",
                new String[]{"IdentificationMark1",
                  "LocalPermanentAddressID",
                  "HasDisability",
                  "PWDCardNumber", "StatusID",
                  "ChildrenStatusID"},
                new String[]{
                  childObject
                    .getIdentificationMark1(),
                  permanentAddressID + "".trim(),
                  String.valueOf(Helper.childrenObject
                    .getHasDisability()),
                  Helper.childrenObject
                    .getPWDCardNumber(),
                  "2", "1"}, "LocalChildrenID",
                Helper.childrenObject.getChildrenID()
                  + "".trim());
						/*
						 * boolean children_flag = dbh.updateROW(
						 * this.getActivity(), "children", new String[] {
						 * "IdentificationMark1", "PermanentAddressID",
						 * "HasDisability", "PWDCardNumber", "StatusID",
						 * "ChildrenStatusID" }, new String[] {
						 * childObject.getIdentificationMark1(),
						 * childObject.getAddress().getAddressID() + "".trim(),
						 * String.valueOf(Helper.childrenObject
						 * .getHasDisability()),
						 * Helper.childrenObject.getPWDCardNumber(), "2", "1" },
						 * "LocalUserID", Helper.childrenObject.getUserID() +
						 * "".trim());
						 */

            // ------------------------children Disablities--------
            ArrayList<ChildrenDisabilities> childDisabilty = Helper.childrenObject
              .getChildDisabilities();

            if (childDisabilty != null) {
              dbh.updateROW(
                this.getActivity(),
                "childrendisabilities",
                new String[]{"IsDeleted"},
                new String[]{"1"},
                "LocalChildrenID",
                Helper.childrenObject.getChildrenID()
                  + "".trim());
              for (int i = 0; i < childDisabilty.size(); i++) {

                dbh.insertintoTable(
                  this.getActivity(),
                  "childrendisabilities",
                  new String[]{"LocalChildrenID",
                    "DisabilityTypeID",
                    "DisabilityPercentage",
                    "SpecificCondition",
                    "IsDeleted"},
                  new String[]{
                    Helper.childrenObject
                      .getChildrenID()
                      + "".trim(),
                    String.valueOf(childDisabilty
                      .get(i)
                      .getDisabilityType()
                      .getDisabilityTypeID()),
                    childDisabilty
                      .get(i)
                      .getDisabilityPercentage(),
                    childDisabilty.get(i)
                      .getSpecificCondition(),
                    "0"});

              }
            }
            // as of now permenent address also as present address
            // disability type Yes-1, No-0

            // children institute table insertion

            // sand: chageded chlidreninstitutes table to children,
            @SuppressWarnings("unused")
            boolean add_flag1 = dbh.updateROW(
              this.getActivity(),
              "children",
              new String[]{"LocalInstituteID",
                "AdmissionDate", "AdmissionNumber",
                "ClassID", "SectionID", "RollNumber"},
              new String[]{
                Helper.selectedLocalInstituteID
                  + "".trim(),
                childInst.getAdmissionDate(),
                childInst.getAdmissionNumber(),
                childInst.getClassID() + "".trim(),
                childInst.getSectionID() + "".trim(),
                childInst.getRollNumber()},
              "LocalChildrenID",
              Helper.childrenObject.getChildrenID()
                + "".trim());

            // add parent details to children parents table
            ArrayList<Childrenparents> parents = Helper.childrenObject
              .getParentAry();
            if (parents == null) {
            } else {
              for (int pitem = 0; pitem < parents.size(); pitem++) {
                Childrenparents parent = parents.get(pitem);
                long parentContactID = Helper.childrenObject
                  .getParentAry().get(pitem).getUser()
                  .getContactID();
                Users parentUser = parent.getUser();
                if (parentUser.getContacts() != null) {// !=
                  ArrayList<Contacts> contactData = parentUser
                    .getContacts();
                  System.out
                    .println("Number of contacts for parents:"
                      + contactData.size());
                  System.out.println("Relation Type is:"
                    + parent.getRelationID());

                  // *******************Modified By Thriveni
                  // ***********

                  if (contactData.size() > 0) {
                    System.out.println("In updation ");

                    boolean isContactIDinContacts = false;

                    if (parentContactID != 0)
                      isContactIDinContacts = dbh
                        .updateROW(
                          this.getActivity(),
                          "contacts",
                          new String[]{"ContactID"},
                          new String[]{""},
                          "LocalContactID",
                          parentContactID
                            + "".trim());

                    System.out
                      .println("In insertion parent contacts");
                    if (!isContactIDinContacts)
                      parentContactID = dbh
                        .insertintoTable(
                          this.getActivity(),
                          "contacts",
                          new String[]{"ContactID"},
                          new String[]{""});

                    for (Contacts parentContact : contactData) {

                      String contactTypeID = ""
                        + parentContact
                        .getContactTypeID();
                      String contact = parentContact
                        .getContact();

                      boolean isContactDetailUpdated = dbh
                        .updateROWByValues(
                          this.getActivity(),
                          "contactdetails",
                          new String[]{"Contact"},
                          new String[]{contact},
                          new String[]{
                            "LocalContactID",
                            "ContactTypeID"},
                          new String[]{
                            String.valueOf(parentContactID),
                            contactTypeID});

                      if (!isContactDetailUpdated)
                        dbh.insertintoTable(
                          this.getActivity(),
                          "contactdetails",
                          new String[]{
                            "LocalContactID",
                            "ContactTypeID",
                            "Contact"},
                          new String[]{
                            parentContactID
                              + "".trim(),
                            contactTypeID
                              + "".trim(),
                            contact});
                    }

                    // boolean a =
                    // dbh.deleteRows(getActivity(),
                    // "contacts", "LocalContactID",
                    // parentContactID + "".trim());
                    // System.out.println("deleted value is:"
                    // +
                    // a);
                    // boolean b =
                    // dbh.deleteRows(getActivity(),
                    // "contactdetails", "LocalContactID",
                    // parentContactID + "".trim());
                    // System.out
                    // .println("deleted contacts value is:"
                    // + b);

										/*
										 * parentContactID =
										 * dbh.insertintoTable(
										 * this.getActivity(), "contacts", new
										 * String[] { "ContactID" }, new
										 * String[] { "" }); for (Contacts
										 * parentContact : contactData) {
										 * dbh.insertintoTable(
										 * this.getActivity(), "contactdetails",
										 * new String[] { "LocalContactID",
										 * "ContactTypeID", "Contact" }, new
										 * String[] { parentContactID +
										 * "".trim(), parentContact
										 * .getContactTypeID() + "".trim(),
										 * parentContact .getContact() }); }
										 */
                  }
                }
                String GenderID = "1";

                if (pitem == 0)
                  GenderID = "1";
                else if (pitem == 1)
                  GenderID = "2";

                long parentId = parent.getUser().getUserID();
                boolean a = dbh.updateROW(
                  this.getActivity(),
                  "users",
                  new String[]{"UserTypeID",
                    "FirstName", "MiddleName",
                    "LastName", "AadharNumber",
                    "SalutationID", "StatusID",
                    "LocalContactID", "GenderID"},
                  new String[]{
                    "2",
                    parentUser.getFirstName(),
                    parentUser.getMiddleName(),
                    parentUser.getLastName(),
                    parentUser.getAadharNumber(),
                    parent.getSalutationID()
                      + "".trim(), "2",
                    parentContactID + "".trim(),
                    GenderID}, "LocalUserId",
                  parentId + "".trim());
                if (!a) {
                  long parentUserId = dbh
                    .insertintoTable(
                      this.getActivity(),
                      "users",
                      new String[]{
                        "UserTypeID",
                        "FirstName",
                        "MiddleName",
                        "LastName",
                        "AadharNumber",
                        "SalutationID",
                        "StatusID",
                        "LocalContactID",
                        "GenderID"},
                      new String[]{
                        "2",
                        parentUser
                          .getFirstName(),
                        parentUser
                          .getMiddleName(),
                        parentUser
                          .getLastName(),
                        parentUser
                          .getAadharNumber(),
                        parent.getSalutationID()
                          + "".trim(),
                        "2",
                        parentContactID
                          + "".trim(),
                        GenderID});

                  dbh.insertintoTable(
                    this.getActivity(),
                    "childrenparents",
                    new String[]{"LocalChildrenID",
                      "LocalUserID", "RelationID"},
                    new String[]{
                      Helper.childrenObject
                        .getChildrenID()
                        + "".trim(),
                      parentUserId + "".trim(),
                      parent.getRelationID()
                        + "".trim()});
                }
              }
            }
            Helper.showShortToast(getActivity(),
              "Child Details Updated Successfuly. ");
						/*
						 * Intent in = new Intent(this.getActivity(),
						 * ScreeningActivity.class);
						 */
						/*
						 * in.putExtra("InstituteID",
						 * Helper.selectedInstituteId); startActivity(in);
						 */
            if (CustomStudentAdapter.lastSelectedPosition != -1
              && CustomStudentAdapter.lastSelectedPosition < ScreeningActivity.childrenList
              .size()) {
              ScreeningActivity.childrenList
                .remove(CustomStudentAdapter.lastSelectedPosition);
              ScreeningActivity.childrenList.add(
                CustomStudentAdapter.lastSelectedPosition,
                Helper.childrenObject);

            }
          }
        }

        final InputMethodManager imm = (InputMethodManager) getActivity()
          .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

      }
    } catch (Exception e) {
      msg = "error in update or inser chaild";
      if (e != null && e.getMessage() != null)
        msg = e.getMessage();

//      for (int i = 0; i < 30; i++) {
//        new Handler().postDelayed(new Runnable() {
//
//          @Override
//          public void run() {
//            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG)
//              .show();
//
//          }
//        }, 1000 * i);
//      }

      e.printStackTrace();
    } finally {
      // Intent in = new Intent(this.getActivity(),
      // ScreeningActivity.class);
      getActivity().finish();
      // in.putExtra("InstituteID", Helper.selectedInstituteId);
      // in.putExtra("LocInsScreeningDetailID",
      // Helper.selectedlocInsScreeningDetailID);
      // in.putExtra("InstituteName", Helper.selectedInstituteName);
      // in.putExtra("LocalInstituteID", Helper.selectedLocalInstituteID);
      // in.putExtra("RBSKCalenarYearID", Helper.RBSKCalenarYearID);
      // in.putExtra("ScreeningRoundID", Helper.screeningRoundID);
      // startActivity(in);

    }
  }

  String msg;

  /*
     * set click event for date
     */
  public class SelectDateFragment extends DialogFragment implements
    DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar calendar = Calendar.getInstance();
      int yy = calendar.get(Calendar.YEAR);
      int mm = calendar.get(Calendar.MONTH);
      int dd = calendar.get(Calendar.DAY_OF_MONTH);
      return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
      populateSetDate(dd, mm + 1, yy);
    }

    public void populateSetDate(int year, int month, int day) {
      et_edit_student_asso_inst_admission_date.setText(day + "/" + month
        + "/" + year);
    }

  }
}
