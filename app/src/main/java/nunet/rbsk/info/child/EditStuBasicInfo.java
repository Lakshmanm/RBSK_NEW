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
//* Name   :  EditStuBasicInfo

//* Type    : Fragment

//* Description     : Functionality to add  Basic information of student
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
//	3.0		   01-05-2015	        Self Review(Promodh)		No Obeservation
//  3.0		   02-05-2015 			Deepika(Peer review)		Comments missing for some methods
//																Remove unwanted imports and codes
// 3.0		   02-05-2015 			Anil(Peer review)			1.	Remove unused imports.
//																2.	Comments missing for some methods.
//																3.	Remove unused code.
//*****************************************************************************  

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Caste;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.Gender;
import nunet.rbsk.model.Religions;

public class EditStuBasicInfo extends Fragment implements OnClickListener {
    private TextView tv_editstu_basicInfo;
    private TextView tv_editstu_disability;
    private EditText et_edit_student_basic_info_student_firstname;
    private EditText et_edit_student_basic_info_middle_name;
    private EditText et_edit_student_basic_info_last_name;
    private EditText et_edit_student_basic_info_aadhaar_no;
    private EditText et_edit_student_basic_info_dob;
    private EditText et_edit_student_basic_info_ntr_vidya_no;
    private EditText et_edit_student_basic_ration_card;
    private EditText et_edit_student_basic_info_id_marks;
    private EditText et_edit_student_basic_emergency;
    private Spinner spn_edit_student_basic_info_gender;
    private Spinner spn_edit_student_basic_info_religion;
    private Spinner spn_edit_student_basic_info_caste;
    private Button btn_edit_stu_basic_info_view_gallery;
    private Button btn_edit_student_basic_next;
    private Fragment fragment = null;
    private Cursor cur;
    private ArrayList<Caste> castAryList;
    private ArrayList<Gender> genderAryList;
    private ArrayList<Religions> religionAryList;
    private DBHelper dbh;
    ImageView iv_edit_stu_basic_info_child_image;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.editstu_basic_info,
                container, false);
        dbh = DBHelper.getInstance(this.getActivity());
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViews(rootView);
        // caste details
        getCasteDataFromDB();
        ArrayAdapter<String> adp_spnCaste = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item);
        adp_spnCaste
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < castAryList.size(); i++) {
            adp_spnCaste.add(castAryList.get(i).getCasteName());
        }
        spn_edit_student_basic_info_caste.setAdapter(adp_spnCaste);

        // genderDetails
        getGenderDataFromDB();
        ArrayAdapter<String> adp_spnGender = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item);
        adp_spnGender
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < genderAryList.size(); i++) {
            adp_spnGender.add(genderAryList.get(i).getGenderName());
        }
        spn_edit_student_basic_info_gender.setAdapter(adp_spnGender);

        // Religions Details
        getReligionsDataFromDB();
        ArrayAdapter<String> adp_spnReligions = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item);
        adp_spnReligions
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < religionAryList.size(); i++) {
            adp_spnReligions.add(religionAryList.get(i).getReligionName());
        }
        spn_edit_student_basic_info_religion.setAdapter(adp_spnReligions);

        // checked if student obj is null or not//
        Children childUser = Helper.childrenObject;
        // if (Helper.childrenObject == null || Helper.addChildFlag)
        // Helper.childrenObject = new Children();

        Bitmap decodeFile = getiImageofChild(this.getActivity(), childUser.getChildrenID());
        System.out.print("bitmap...."+decodeFile);
        if (decodeFile != null)
            iv_edit_stu_basic_info_child_image.setImageBitmap(decodeFile);
        else
            iv_edit_stu_basic_info_child_image.setImageResource(R.drawable.kid_image);

        et_edit_student_basic_info_student_firstname.setText(childUser
                .getFirstName());
        et_edit_student_basic_info_middle_name.setText(childUser
                .getMiddleName());
        et_edit_student_basic_info_last_name.setText(childUser.getLastName());

        et_edit_student_basic_info_aadhaar_no.setText(childUser
                .getAadharNumber());

        et_edit_student_basic_info_dob.setText(childUser.getDateOfBirth());
        et_edit_student_basic_info_ntr_vidya_no.setText(childUser
                .getHealthCardNumber());
        et_edit_student_basic_ration_card.setText(childUser
                .getRationCardNumber());
        et_edit_student_basic_info_id_marks.setText(Helper.childrenObject
                .getIdentificationMark1());

        if (!TextUtils.isEmpty(childUser.getEmgContact())) {
            et_edit_student_basic_emergency.setText(childUser.getEmgContact());
        } else {
            et_edit_student_basic_emergency.setText("");
        }
        if (childUser.getGender() == null) {
            // if (childUser.getGenderID() != 0) {
            // spn_edit_student_basic_info_gender.setSelection(childUser
            // .getGenderID());
            // spn_edit_student_basic_info_religion.setSelection(childUser
            // .getReligionID());
            // spn_edit_student_basic_info_caste.setSelection(childUser
            // .getCasteID());
            // }

        } else {
            spn_edit_student_basic_info_gender.setSelection(childUser
                    .getGender().getGenderID());
            spn_edit_student_basic_info_religion.setSelection(childUser
                    .getReligionID());
            spn_edit_student_basic_info_caste.setSelection(childUser
                    .getCasteID());
        }

        // if (childUser != null) {
        // spn_edit_student_basic_info_gender.setSelection(childUser
        // .getGenderID());
        // spn_edit_student_basic_info_religion.setSelection(childUser
        // .getReligionID());
        // spn_edit_student_basic_info_caste.setSelection(childUser
        // .getCasteID());
        // }
        return rootView;
    }

    public  Bitmap getiImageofChild(Context ctx, int childrenID) {
        String query = "select ChildrenScreeingStatusID,LocalChildrenScreeningID from childrenscreening CS where  CS.IsDeleted!=1 AND  LocalChildrenID='"
                + childrenID + "';";
        DBHelper dbh = new DBHelper(ctx);
        Cursor cursor = dbh.getCursorData(ctx, query);
        String LocalChildrenScreeningID = "";
        Bitmap abc = null;
        if (cursor != null) {
            cursor.moveToNext();
            LocalChildrenScreeningID = (cursor.getString(cursor
                    .getColumnIndex("LocalChildrenScreeningID")));

        }
        if (!TextUtils.isEmpty(LocalChildrenScreeningID)) {
            String path = "";
            String query_Wards = "SELECT ImageName FROM  childrenscreeningpictures CSP where  CSP.IsDeleted!=1 AND  LocalChildrenScreeningID='"
                    + LocalChildrenScreeningID + "'";
            Cursor dataCursor = dbh.getCursorData(ctx, query_Wards);
            if (dataCursor != null && dataCursor.moveToFirst()) {
                path = dataCursor.getString(dataCursor
                        .getColumnIndex("ImageName"));
            } else {
                path = "";
            }
            String root = Environment.getExternalStorageDirectory().toString();
            System.out.println("path in child.........."+root + "/DCIM/myCapturedImages/" + path);
            File imgFile = new File(root + "/DCIM/myCapturedImages/" + path);
            if (imgFile.exists()) {
                Bitmap decodeFile = BitmapFactory.decodeFile(imgFile
                        .getAbsolutePath());
              abc=decodeFile;
            }
            // ////get Image end////////////////
        } else {
        }
        System.out.println("bitmap in child.........."+abc);
        return abc;
    }

    /**
     * To get the view id's from R.java
     */
    private void findViews(View rootView) {

        iv_edit_stu_basic_info_child_image = (ImageView) rootView.findViewById(
                R.id.iv_edit_stu_basic_info_child_image);
        tv_editstu_basicInfo = (TextView) rootView.findViewById(
                R.id.tv_editstu_basicInfo);
        tv_editstu_disability = (TextView) rootView.findViewById(
                R.id.tv_editstu_disability);
        et_edit_student_basic_info_student_firstname = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_info_student_firstname);
        et_edit_student_basic_info_middle_name = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_info_middle_name);
        et_edit_student_basic_info_last_name = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_info_last_name);
        et_edit_student_basic_info_aadhaar_no = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_info_aadhaar_no);
        et_edit_student_basic_info_dob = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_info_dob);
        et_edit_student_basic_info_ntr_vidya_no = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_info_ntr_vidya_no);
        et_edit_student_basic_ration_card = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_ration_card);
        et_edit_student_basic_info_id_marks = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_info_id_marks);
        et_edit_student_basic_emergency = (EditText) rootView
                .findViewById(R.id.et_edit_student_basic_emergency);
        spn_edit_student_basic_info_gender = (Spinner) rootView
                .findViewById(R.id.spn_edit_student_basic_info_gender);
        spn_edit_student_basic_info_religion = (Spinner) rootView
                .findViewById(R.id.spn_edit_student_basic_info_religion);
        spn_edit_student_basic_info_caste = (Spinner) rootView
                .findViewById(R.id.spn_edit_student_basic_info_caste);
        btn_edit_stu_basic_info_view_gallery = (Button) rootView
                .findViewById(R.id.btn_edit_stu_basic_info_view_gallery);

        btn_edit_student_basic_next = (Button) rootView
                .findViewById(R.id.btn_edit_student_basic_next);
        btn_edit_stu_basic_info_view_gallery.setOnClickListener(this);
        btn_edit_student_basic_next.setOnClickListener(this);
        et_edit_student_basic_info_dob.setOnClickListener(this);
        et_edit_student_basic_info_dob.setText(Helper
                .getTodayDate("yyyy-MM-dd"));

    }

    /*
     * on click evnt for views
     */
    @Override
    public void onClick(View v) {
        if (v == btn_edit_student_basic_next) {
            int errorCount = 0;
            if (TextUtils.isEmpty(et_edit_student_basic_info_student_firstname
                    .getText().toString().trim())) {
                errorCount++;
                Helper.displayErrorMsg(
                        et_edit_student_basic_info_student_firstname,
                        "First name is mandatory");
            }
            if (TextUtils.isEmpty(et_edit_student_basic_info_last_name
                    .getText().toString().trim())) {
                errorCount++;
                Helper.displayErrorMsg(et_edit_student_basic_info_last_name,
                        "Last  name is mandatory");
            }
            String dOB = et_edit_student_basic_info_dob.getText().toString()
                    .trim();
            int ageInMonths = 0;
            if (TextUtils.isEmpty(et_edit_student_basic_info_dob.getText()
                    .toString().trim())) {
                errorCount++;
                Helper.displayErrorMsg(et_edit_student_basic_info_dob,
                        "Enter Date of Birth.");
            }

            if (!TextUtils.isEmpty(et_edit_student_basic_info_dob.getText()
                    .toString().trim())) {
                ageInMonths = Helper.getAgeInMonths(dOB);
                if (Helper.instTypeId == 1) {

                    if ((ageInMonths / 12) > 6) {
                        errorCount++;
                        Helper.displayErrorMsg(et_edit_student_basic_info_dob,
                                "Age cannot be greater than 6 years");
                        Helper.showShortToast(getActivity(),
                                "Age cannot be greater than 6 years");
                    }
                    // else if (ageInMonths <= 12) {
                    // errorCount++;
                    // Helper.displayErrorMsg(et_edit_student_basic_info_dob,
                    // "Age cannot be less than 1 year");
                    // Helper.showShortToast(getActivity(),
                    // "Age cannot be less than 1 year");
                    // }
                } else if (Helper.instTypeId == 2) {
                    if ((ageInMonths / 12) < 6 || (ageInMonths / 12) > 18) {
                        errorCount++;
                        Helper.displayErrorMsg(et_edit_student_basic_info_dob,
                                "Age cannot be less than 6 years and greater than 18 years");

                        Helper.showShortToast(getActivity(),
                                "Age cannot be less than 6 years and greater than 18 years");
                    }
                }
            }

            if (spn_edit_student_basic_info_caste.getSelectedItemPosition() == 0) {
                errorCount++;
                Helper.setErrorForSpinner(spn_edit_student_basic_info_caste,
                        "Select Caste");
            }

            if (spn_edit_student_basic_info_gender.getSelectedItemPosition() == 0) {
                errorCount++;
                Helper.setErrorForSpinner(spn_edit_student_basic_info_gender,
                        "Select Gender");
            }

            if (spn_edit_student_basic_info_religion.getSelectedItemPosition() == 0) {
                errorCount++;
                Helper.setErrorForSpinner(spn_edit_student_basic_info_religion,
                        "Select Religion");
            }

            if (et_edit_student_basic_info_aadhaar_no.getText().toString()
                    .trim().length() > 0
                    && et_edit_student_basic_info_aadhaar_no.getText()
                    .toString().trim().length() < 12) {
                errorCount++;
                Helper.displayErrorMsg(et_edit_student_basic_info_aadhaar_no,
                        "Enter Valid Aadhaar No.");
            }

            if (et_edit_student_basic_info_ntr_vidya_no.getText().toString()
                    .trim().length() > 0
                    && et_edit_student_basic_info_ntr_vidya_no.getText()
                    .toString().trim().length() < 10) {
                errorCount++;
                Helper.displayErrorMsg(et_edit_student_basic_info_ntr_vidya_no,
                        "Enter Valid NTR Vidya Seva No.");
            }

            if (et_edit_student_basic_ration_card.getText().toString().trim()
                    .length() > 0
                    && et_edit_student_basic_ration_card.getText().toString()
                    .trim().length() < 15) {
                errorCount++;
                Helper.displayErrorMsg(et_edit_student_basic_ration_card,
                        "Enter Valid Ration Card No.");
            }

            if (et_edit_student_basic_emergency.getText().toString().trim()
                    .length() > 0
                    && et_edit_student_basic_emergency.getText().toString()
                    .trim().length() < 10) {
                errorCount++;
                Helper.displayErrorMsg(et_edit_student_basic_emergency,
                        "Emergency contact number is not valid");
            }

            if (errorCount == 0) {
                AddStudentActivityDialog.tabFlags[1] = true;
                AddStudentActivityDialog
                        .enableHeaderClick1(AddStudentActivityDialog.tabFlags);

                Helper.updateHeaderFromNext(getActivity(),
                        tv_editstu_basicInfo, tv_editstu_disability,
                        R.drawable.headerbg, R.drawable.headerbg_selectced);
                // bundle.putInt("InsituteID", instituteId);
                if (AddStudentActivityDialog.fragmentArr[1] == null)
                    AddStudentActivityDialog.fragmentArr[1] = fragment = new EditStuDisability();
                else
                    fragment = AddStudentActivityDialog.fragmentArr[1];

                // fragment.setArguments(bundle);
                if (fragment != null) {
                    Children childUser = Helper.childrenObject;
                    if (Helper.addChildFlag) {

                    } else {
                        childUser.setUserID(Helper.childrenObject.getUserID());
                    }
                    Gender gen = new Gender();
                    childUser
                            .setFirstName(et_edit_student_basic_info_student_firstname
                                    .getText().toString().trim());
                    childUser
                            .setMiddleName(et_edit_student_basic_info_middle_name
                                    .getText().toString().trim());
                    childUser.setLastName(et_edit_student_basic_info_last_name
                            .getText().toString().trim());
                    childUser
                            .setAadharNumber(et_edit_student_basic_info_aadhaar_no
                                    .getText().toString().trim());
                    childUser.setDateOfBirth(et_edit_student_basic_info_dob
                            .getText().toString().trim());

                    // gender
                    childUser.setGenderID(genderAryList.get(
                            spn_edit_student_basic_info_gender
                                    .getSelectedItemPosition()).getGenderID());
                    gen.setGenderID(genderAryList.get(
                            spn_edit_student_basic_info_gender
                                    .getSelectedItemPosition()).getGenderID());
                    childUser.setGender(gen);
                    // religion
                    childUser
                            .setReligionID(religionAryList.get(
                                    spn_edit_student_basic_info_religion
                                            .getSelectedItemPosition())
                                    .getReligionID());
                    // caste
                    childUser.setCasteID(castAryList.get(
                            spn_edit_student_basic_info_caste
                                    .getSelectedItemPosition()).getCasteID());

                    childUser
                            .setHealthCardNumber(et_edit_student_basic_info_ntr_vidya_no
                                    .getText().toString().trim());
                    childUser
                            .setRationCardNumber(et_edit_student_basic_ration_card
                                    .getText().toString().trim());

                    Helper.childrenObject
                            .setIdentificationMark1(et_edit_student_basic_info_id_marks
                                    .getText().toString().trim());
                    // Helper.childrenObject.setContactID(Helper.childrenObject
                    // .getContactID());
                    // emergency contact
                    Helper.childrenObject
                            .setEmgContact(et_edit_student_basic_emergency
                                    .getText().toString().trim());
                    // Helper.childrenObject.setUser(user);
                    System.out.println("basicoj:::::" + Helper.childrenObject);
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();

                }
            }
            final InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        } else if (v == btn_edit_stu_basic_info_view_gallery) {
        } else if (v == et_edit_student_basic_info_dob) {
            // this.getActivity().showDialog(0);
            DialogFragment newFragment = new Helper.SelectDateFragment(
                    et_edit_student_basic_info_dob);
            newFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    /**
     * to get caste details from DB
     */
    public void getCasteDataFromDB() {
        String query = "select * from caste C Where  C.IsDeleted!=1 ";
        cur = dbh.getCursorData(this.getActivity(), query);
        Caste casteModelObject;
        castAryList = new ArrayList<Caste>();
        casteModelObject = new Caste();
        casteModelObject.setCasteID(0);
        casteModelObject.setCasteName("--Select--");
        castAryList.add(casteModelObject);
        if (cur != null) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        casteModelObject = new Caste();
                        casteModelObject.setCasteID(NumUtil.IntegerParse
                                .parseInt(cur.getString(cur
                                        .getColumnIndex("CasteID"))));
                        casteModelObject.setCasteName(cur.getString(cur
                                .getColumnIndex("DisplayText")));
                        casteModelObject.setCasteOrder(cur.getString(cur
                                .getColumnIndex("DisplaySequence")));
                        castAryList.add(casteModelObject);
                    } while (cur.moveToNext());
                }
            } finally {
                cur.close();
            }
        }
    }

    /**
     * to get gender details from DB
     */
    private void getGenderDataFromDB() {
        String query = "select * from gender G Where  G.IsDeleted!=1 ";
        cur = dbh.getCursorData(this.getActivity(), query);
        Gender genderModelObject;
        genderAryList = new ArrayList<Gender>();
        genderModelObject = new Gender();
        genderModelObject.setGenderID(0);
        genderModelObject.setGenderName("--Select--");
        genderAryList.add(genderModelObject);
        if (cur != null) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        genderModelObject = new Gender();
                        genderModelObject.setGenderID(NumUtil.IntegerParse
                                .parseInt(cur.getString(cur
                                        .getColumnIndex("GenderID"))));
                        genderModelObject.setGenderName(cur.getString(cur
                                .getColumnIndex("DisplayText")));

                        genderAryList.add(genderModelObject);
                    } while (cur.moveToNext());
                }
            } finally {
                cur.close();
            }
        }
    }

    /**
     * get religions from DB
     */
    private void getReligionsDataFromDB() {

        String query = "select * from religions R Where  R.IsDeleted!=1 ";
        cur = dbh.getCursorData(this.getActivity(), query);
        Religions relModelObject;
        religionAryList = new ArrayList<Religions>();
        relModelObject = new Religions();
        relModelObject.setReligionID(0);
        relModelObject.setReligionName("--Select--");
        religionAryList.add(relModelObject);
        if (cur != null) {

            try {
                if (cur.moveToFirst()) {
                    do {
                        relModelObject = new Religions();
                        relModelObject.setReligionID(NumUtil.IntegerParse
                                .parseInt(cur.getString(cur
                                        .getColumnIndex("ReligionID"))));
                        relModelObject.setReligionName(cur.getString(cur
                                .getColumnIndex("DisplayText")));
                        religionAryList.add(relModelObject);
                    } while (cur.moveToNext());
                }
            } finally {
                cur.close();
            }
        }
    }

    /**
     * display date picker for DOB
     */
}
