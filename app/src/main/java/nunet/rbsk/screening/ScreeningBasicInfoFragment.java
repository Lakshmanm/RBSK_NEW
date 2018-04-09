//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import nunet.adapter.CustomStudentAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.CustomDialog;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.info.child.AddStudentActivityDialog;
import nunet.rbsk.model.Address;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.ChildrenDisabilities;
import nunet.rbsk.model.ChildrenInstitutes;
import nunet.rbsk.model.ChildrenScreeningModel;
import nunet.rbsk.model.Childrenparents;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.DisabilityTypes;
import nunet.rbsk.model.District;
import nunet.rbsk.model.FamilyHistoryDisease;
import nunet.rbsk.model.Gender;
import nunet.rbsk.model.Habitation;
import nunet.rbsk.model.Mandal;
import nunet.rbsk.model.Panchayat;
import nunet.rbsk.model.Salutation;
import nunet.rbsk.model.State;
import nunet.rbsk.model.Users;
import nunet.rbsk.model.Village;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.RoundedImageView;
import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  ScreeningBasicInfoFragment.java

//* Type    :

//* Description     :
//* References     :
//* Author    : kiruthika.ganesan

//* Created Date       :  22-May-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG
//*****************************************************************************
//* Ver        Date                Code Review By            Observations
// 3.0        30-05-2015			Promodh					Method comments are missing
//*****************************************************************************
public class ScreeningBasicInfoFragment extends Fragment implements
  OnClickListener, OnItemSelectedListener {

  int childID;

  private TextView tv_screening_basic_student_roll_no;
  private TextView tv_screening_basic_student_aadhar_no;
  private TextView tv_screening_basic_student_ntr_vidya_no;
  private TextView tv_screening_basic_student_name;
  private TextView tv_screening_basic_student_has_disability;
  private TextView tv_screening_basic_student_age_sex;
  private TextView tv_screening_basic_student_pwd_cardno;
  private TextView tv_screening_basic_student_parent_guardian;
  private TextView tv_screening_basic_student_emergency_contact;
  private TextView tv_screening_basic_student_mcts_no;
  private ImageView iv_screening_basic_student_family_history;
  public static Spinner spn_screening_basic_student_active;
  private LayoutInflater inflator;
  private LinearLayout ll_screening_disability_layout;
  private ImageView iv_screening_basic_student_edit;
  private ArrayList<String> chidStatus;
  private ArrayAdapter<String> csadapter;
  ArrayList<ChildrenDisabilities> childList;
  private ImageView iv_screening_basic_student_next;
  private TextView tv_screening_basic_student_diseases_history;
  private ImageView iv_screening_basic_student_image;
  private String gender;
  // private int instType;
  private DBHelper dbh;
  public static CustomDialog dialog;

  ScreeningActivity mScreeningActivity;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    mScreeningActivity = (ScreeningActivity) getActivity();

    View rootView = inflater.inflate(R.layout.screening_basic_info,
      container, false);

    dbh = DBHelper.getInstance(this.getActivity());
    childID = getArguments().getInt("ChildrenID");
    chidStatus = new ArrayList<String>();
    chidStatus = getChildStatusFromDb();
    chidStatus.add("Absent");

    csadapter = new ArrayAdapter<String>(getActivity(),
      android.R.layout.simple_spinner_item, chidStatus);
    csadapter
      .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    if (Helper.childScreeningObj == null) {
      Helper.childScreeningObj = new ChildrenScreeningModel();
    }

//		String path = checkDbforImage();
//		String root = Environment.getExternalStorageDirectory().toString();
//		File imgFile = new File(root + "/DCIM/myCapturedImages/" + path);
    Bitmap decodeFile = ScreeningActivity.getiImageofChild(getActivity(), childID);
    if (decodeFile != null)
      Helper.childImage = RoundedImageView.getCroppedBitmap(decodeFile,
        65);
    else
      Helper.childImage = BitmapFactory.decodeResource(getResources(),
        R.drawable.kid_image);

    findViews(rootView);
    // disabilityInflator();
    if (Helper.childrenObject != null) {
      if (childID != 0) {
        new Thread(new Runnable() {

          @Override
          public void run() {
            getChildDetailsFromDB();
            getDisabiltyDetailsFromDB();
            getScreeningStatus();
            if (Helper.childrenObject.getGender() != null) {
              gender = Helper.childrenObject.getGender()
                .getGenderName();
            } else {
              gender = "";
            }

            final int ageInMonths = Helper.childrenObject
              .getAgeInMonths();
            getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                String message = "";
                message += (ageInMonths / 12) == 0 ? ""
                  : (ageInMonths / 12) + " Years ";
                message += (ageInMonths % 12) == 0 ? ""
                  : (ageInMonths % 12) + " Months, ";
                message += gender;
                tv_screening_basic_student_age_sex
                  .setText(message);

                spn_screening_basic_student_active
                  .setOnItemSelectedListener(null);

                if (Helper.childrenObject
                  .getChildScreenStatusID() == 3) {
                  spn_screening_basic_student_active
                    .setSelection(3);
                }

                new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    spn_screening_basic_student_active
                      .setOnItemSelectedListener(ScreeningBasicInfoFragment.this);
                  }
                }, 200);
              }
            });
          }

        }).start();
        // getParentDetailsFromDB();

      }

    }
    return rootView;
  }

  private void getScreeningStatus() {
    DBHelper dbh = new DBHelper(this.getActivity().getApplicationContext());
    String query = "select CS.ChildrenScreenStatusID,CS.ScreeningComments  from childrenscreening CS" +
      " where CS.IsDeleted!=1 AND  LocalChildrenID='"
      + childID
      + "' and LocalInstituteScreeningDetailID='"
      + ((ScreeningActivity) (getActivity())).locInsScreeningDetailID
      + "'";
    Cursor mCursor = dbh.getCursorData(getActivity(), query);
    if (mCursor != null) {
      mCursor.moveToFirst();
      try {
        String ChildrenScreenStatusID = mCursor.getString(0);
        Helper.childrenObject
          .setScreeningComments(mCursor.getString(1));
        if (!TextUtils.isEmpty(ChildrenScreenStatusID)) {
          if (Helper.childrenObject != null)
            Helper.childrenObject
              .setChildScreenStatusID(NumUtil.IntegerParse
                .parseInt(ChildrenScreenStatusID));
        } else {
          if (Helper.childrenObject != null)
            Helper.childrenObject.setChildScreenStatusID(2);
        }

      } finally {
        mCursor.close();
      }
    }

  }

  /**
   * @return
   */
  private ArrayList<String> getChildStatusFromDb() {
    ArrayList<String> childrenstatus = new ArrayList<String>();
    String relQuery = "Select DisplayText from childrenstatus CS where  CS.IsDeleted!=1 ";
    Cursor cur = dbh.getCursorData(getActivity(), relQuery);
    if (cur != null) {

      try {
        if (cur.moveToFirst()) {

          do {

            childrenstatus.add(cur.getString(cur
              .getColumnIndex("DisplayText")));
          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
      // getUpdate_view("", 0);
    }
    return childrenstatus;
  }

  private String checkDbforImage() {

    String path = "";
    String query_Wards = "SELECT ImageName FROM  childrenscreeningpictures CSP where  CSP.IsDeleted!=1 AND  LocalChildrenScreeningID='"
      + Helper.childScreeningObj.getScreeningID() + "'";
    Cursor dataCursor = dbh.getCursorData(this.getActivity(), query_Wards);
    if (dataCursor != null && dataCursor.moveToFirst()) {
      path = dataCursor.getString(dataCursor.getColumnIndex("ImageName"));
    } else {
      path = "";
    }
    return path;
  }

  /**
   * method to get parent name and contact from DB
   */
  private void getParentDetailsFromDB() {

		/*
     * String query_Wards =
		 * " select childrenparents.LocalUserID,childrenparents.RelationID,users.FirstName,users.MiddleName,users.LastName,"
		 * +
		 * "users.AadharNumber,users.SalutationID,users.LocalContactID,contactdetails.contact,contactdetails.contacttypeid"
		 * + " from childrenparents " +
		 * " inner join users on childrenparents.LocalUserID=users.LocalUserID "
		 * +
		 * " inner join contactdetails on contactdetails.localcontactid=users.localcontactid "
		 * + " childrenparents.LocalChildrenID='" + 1 + "' order by RelationID";
		 */

    String query_Wards = " select childrenparents.LocalUserID,childrenparents.RelationID,users.FirstName,users.MiddleName,users.LastName,"
      + "users.AadharNumber,users.SalutationID,users.LocalContactID,contactdetails.Contact, "
      + "contactdetails.ContactTypeID,contactcategories.DisplayText,contacttypes.ContactCategoryID,contacttypes.DisplayText "
      + " from childrenparents "
      + " inner join users on childrenparents.LocalUserID=users.LocalUserID "
      + " left join contactdetails on contactdetails.localcontactid=users.localcontactid  "
      + " left join  contacttypes on contactdetails.ContactTypeID=contacttypes.ContactTypeID "
      + " left join  contactcategories on contactcategories.ContactCategoryID=contacttypes.ContactCategoryID "
      + " where users.UserTypeID=2 and  users.isDeleted!=1 and childrenparents.isDeleted!=1 and childrenparents.LocalChildrenID='"
      + Helper.childrenObject.getChildrenID()
      + "' order by RelationID";

    Cursor childCur = dbh.getCursorData(this.getActivity(), query_Wards);
    setDataToChildrenParentModel(childCur);

  }

  /**
   * set to child parent model
   */
  private void setDataToChildrenParentModel(Cursor childCur) {
    if (childCur != null) {

      ArrayList<Childrenparents> childParentList = new ArrayList<Childrenparents>();

      ArrayList<Contacts> childContactList = null;
      Childrenparents childparent = null;
      Users childUserModel = null;

      Contacts childparentContacts = null;
      Salutation sal = null;

      int tempRelationID = -1;

      boolean addNewParent = false;

      try {
        if (childCur.moveToFirst()) {
          do {

            // Contacts con = new Contacts();

            int currentRelationID = childCur.getInt(childCur
              .getColumnIndex("RelationID"));
            if (tempRelationID == currentRelationID) {
              childparentContacts = new Contacts();
              // continue;
              addNewParent = false;
            } else {
              childparent = new Childrenparents();
              childUserModel = new Users();
              childparentContacts = new Contacts();
              sal = new Salutation();
              childContactList = new ArrayList<Contacts>();
              tempRelationID = currentRelationID;
              addNewParent = true;
            }
            childparent.setRelationID(currentRelationID);
            childUserModel.setFirstName(childCur.getString(childCur
              .getColumnIndex("FirstName")));
            childUserModel.setMiddleName(childCur
              .getString(childCur
                .getColumnIndex("MiddleName")));
            childUserModel.setLastName(childCur.getString(childCur
              .getColumnIndex("LastName")));
            childUserModel.setAadharNumber(childCur
              .getString(childCur
                .getColumnIndex("AadharNumber")));
            childUserModel.setUserID(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("LocalUserID"))));
            String SalutationID = childCur.getString(childCur
              .getColumnIndex("SalutationID"));
            if (!TextUtils.isEmpty(SalutationID)) {
              sal.setSalutationID(NumUtil.IntegerParse
                .parseInt(SalutationID));
              childparent.setSalutationID(NumUtil.IntegerParse
                .parseInt(SalutationID));
              childUserModel.setSalutation(sal);
            }

            String LocalContactID = childCur.getString(childCur
              .getColumnIndex("LocalContactID"));

            childUserModel.setContactID(NumUtil.IntegerParse
              .parseInt(LocalContactID));

            childparentContacts.setContact(childCur
              .getString(childCur.getColumnIndex("Contact")));

            String ContactCategoryID = childCur.getString(childCur
              .getColumnIndex("ContactCategoryID"));
            if (!TextUtils.isEmpty(ContactCategoryID))
              childparentContacts
                .setContactCategoryID(NumUtil.IntegerParse
                  .parseInt(ContactCategoryID));

            childparentContacts.setContactCategoryName(childCur
              .getString(childCur
                .getColumnIndex("ContactTypeID")));

            childUserModel.setContacts(childContactList);
            // Set SALUTATION //////

						/*
             * childContact.setContact(childCur.getString(childCur
						 * .getColumnIndex("Contact")));
						 * childContactList.add(childContact);
						 * childUserModel.setContacts(childContactList);
						 */
            childContactList.add(childparentContacts);
            childparent.setUser(childUserModel);
            if (addNewParent) {
              childParentList.add(childparent);
            }
          } while (childCur.moveToNext());
          Helper.childrenObject.setParentAry(childParentList);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        childCur.close();
      }

    }

  }

  /**
   * get disablity list from DB
   */
  private void getDisabiltyDetailsFromDB() {

    String query_Wards = " select childrendisabilities.DisabilityPercentage,childrendisabilities.SpecificCondition,"
      + "childrendisabilities.DisabilityTypeID,disabilitytypes.DisplayText,childrendisabilities.ChildrenDisabilityID"
      + " from childrendisabilities  "
      + " inner join disabilitytypes on disabilitytypes.DisabilityTypeID=childrendisabilities.DisabilityTypeID "
      + " where childrendisabilities.LocalChildrenID='"
      + childID
      + "' and childrendisabilities.IsDeleted='0'";
    Cursor childCur = dbh.getCursorData(this.getActivity(), query_Wards);
    setDataToDisablityModel(childCur);
  }

  /**
   * set to disbality model class
   */
  private void setDataToDisablityModel(Cursor childCur) {

    childList = new ArrayList<ChildrenDisabilities>();
    if (childCur != null) {

      try {
        if (childCur.moveToFirst()) {
          do {
            ChildrenDisabilities childDisablity = new ChildrenDisabilities();
            DisabilityTypes types = new DisabilityTypes();
            // Users childUserModel=new Users();
            childDisablity.setChildrenID(Helper.childrenObject
              .getChildrenID());
            childDisablity
              .setDisabilityPercentage(childCur.getString(childCur
                .getColumnIndex("DisabilityPercentage")));
            // childDisablity
            // .setDisabilityId(IntUtil.Integer.parseInt(childCur.getString(childCur
            // .getColumnIndex("ChildrenDisabilityID"))));

            childDisablity.setSpecificCondition(childCur
              .getString(childCur
                .getColumnIndex("SpecificCondition")));
            types.setDisabilityName(childCur.getString(childCur
              .getColumnIndex("DisplayText")));
            types.setDisabilityTypeID(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("DisabilityTypeID"))));
            childDisablity.setDisabilityType(types);
            childList.add(childDisablity);
            Helper.childrenObject.setChildDisabilities(childList);

          } while (childCur.moveToNext());
        }
      } finally {
        childCur.close();
      }
    }
    if (getActivity() != null)
      getActivity().runOnUiThread(new Runnable() {

        @Override
        public void run() {
          disabilityInflator();
        }
      });

  }

  /**
   * update rows to UI
   */
  @SuppressLint("InflateParams")
  private void disabilityInflator() {
    /* ll_screening_disability_layout.removeAllViews(); */
    inflator = (LayoutInflater) this.getActivity().getSystemService(
      Activity.LAYOUT_INFLATER_SERVICE);
    View view = inflator.inflate(R.layout.disability_screening_basicinfo,
      null);

    ll_screening_disability_layout.addView(view);
    if (childList != null) {
      for (int i = 0; i < childList.size(); i++) {
        inflator = (LayoutInflater) getActivity().getSystemService(
          Activity.LAYOUT_INFLATER_SERVICE);
        view = inflator.inflate(
          R.layout.disability_screening_basicinfo, null);

        TextView tv_screening_disablity_type = (TextView) view
          .findViewById(R.id.tv_screening_disablity_type);
        TextView tv_screening_basic_student_percentage = (TextView) view
          .findViewById(R.id.tv_screening_basic_student_percentage);
        TextView tv_screening_basic_student_specific_condition = (TextView) view
          .findViewById(R.id.tv_screening_basic_student_specific_condition);

        tv_screening_disablity_type.setTypeface(null, Typeface.NORMAL);
        tv_screening_basic_student_percentage.setTypeface(null,
          Typeface.NORMAL);
        tv_screening_basic_student_specific_condition.setTypeface(null,
          Typeface.NORMAL);

        tv_screening_disablity_type.setText(childList.get(i)
          .getDisabilityType().getDisabilityName());
        tv_screening_basic_student_percentage.setText(childList.get(i)
          .getDisabilityPercentage());
        tv_screening_basic_student_specific_condition.setText(childList
          .get(i).getSpecificCondition());
        ll_screening_disability_layout.addView(view);
      }
    }

  }

  /**
   * to find the views
   */
  private void findViews(View view) {

    tv_screening_basic_student_mcts_no = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_mcts_no);
    tv_screening_basic_student_roll_no = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_roll_no);
    tv_screening_basic_student_aadhar_no = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_aadhar_no);
    tv_screening_basic_student_ntr_vidya_no = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_ntr_vidya_no);
    tv_screening_basic_student_name = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_name);
    tv_screening_basic_student_age_sex = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_age_sex);
    ll_screening_disability_layout = (LinearLayout) view
      .findViewById(R.id.ll_screening_disability_layout);
    iv_screening_basic_student_family_history = (ImageView) view
      .findViewById(R.id.iv_screening_basic_student_family_history);
    iv_screening_basic_student_family_history.setOnClickListener(this);
    tv_screening_basic_student_has_disability = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_has_disability);
    tv_screening_basic_student_pwd_cardno = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_pwd_cardno);

    tv_screening_basic_student_parent_guardian = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_parent_guardian);
    tv_screening_basic_student_emergency_contact = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_emergency_contact);

    tv_screening_basic_student_diseases_history = (TextView) view
      .findViewById(R.id.tv_screening_basic_student_diseases_history);

    iv_screening_basic_student_edit = (ImageView) view
      .findViewById(R.id.iv_screening_basic_student_edit);
    iv_screening_basic_student_edit.setOnClickListener(this);
    iv_screening_basic_student_next = (ImageView) view
      .findViewById(R.id.iv_screening_basic_student_next);
    iv_screening_basic_student_next.setOnClickListener(this);

    spn_screening_basic_student_active = (Spinner) view
      .findViewById(R.id.spn_screening_basic_student_active);
    spn_screening_basic_student_active.setAdapter(csadapter);
    iv_screening_basic_student_image = (ImageView) view
      .findViewById(R.id.iv_screening_basic_student_image);
    iv_screening_basic_student_image.setImageBitmap(Helper.childImage);
    String familyHistoryStr = "";

    // *** isScreened & isNull
    if (Helper.childrenObject != null) {
      if ((Helper.childrenObject.isScreenedForCurrentRound())
        && (Helper.childScreeningObj.getFamilyHistoryDiseases() == null)) {

        // *** getID's & displayName from FamilyHistoryDB

        HashMap<String, String> fhData = getFamilyHistoryDataFromDB();

        ArrayList<FamilyHistoryDisease> familyHistoryList = new ArrayList<FamilyHistoryDisease>();
        String fhQuery = "select * from childscreeningfh CSF where  CSF.IsDeleted!=1 AND  LocalChildrenScreeningID='"
          + Helper.childScreeningObj.getScreeningID() + "';";
        Cursor familyHistoryCur = dbh.getCursorData(this.getActivity(),
          fhQuery);
        if (familyHistoryCur != null) {
          if (familyHistoryCur.moveToFirst()) {
            do {
              FamilyHistoryDisease familyHistoryObj = new FamilyHistoryDisease();
              familyHistoryObj
                .setDiseaseID(NumUtil.IntegerParse.parseInt(familyHistoryCur.getString(familyHistoryCur
                  .getColumnIndex("FamilyHistoryID"))));
              String diseaseName = fhData
                .get(familyHistoryCur.getString(familyHistoryCur
                  .getColumnIndex("FamilyHistoryID")));
              familyHistoryObj.setDiseaseName(diseaseName);
              String selectedHistory = familyHistoryCur
                .getString(familyHistoryCur
                  .getColumnIndex("HasHistory"));
              if (selectedHistory != null) {
                if (StringUtils.equalsNoCase(selectedHistory,
                  "1")) {
                  familyHistoryObj.setSelected(true);
                } else {
                  familyHistoryObj.setSelected(false);
                }
              } else {
                familyHistoryObj.setSelected(false);

              }
              familyHistoryObj.setDiseaseName(diseaseName);
              String relationId = familyHistoryCur
                .getString(familyHistoryCur
                  .getColumnIndex("FamilyMemberRelationID"));
              if (!TextUtils.isEmpty(relationId)) {
                familyHistoryObj
                  .setRelationID(NumUtil.IntegerParse
                    .parseInt(relationId));
              }
              String commentsStr = familyHistoryCur
                .getString(familyHistoryCur
                  .getColumnIndex("Notes"));

              familyHistoryObj.setDiseaseComments(commentsStr);

              familyHistoryList.add(familyHistoryObj);

            } while (familyHistoryCur.moveToNext());
            Helper.childScreeningObj
              .setFamilyHistoryDiseases(familyHistoryList);
          }
          familyHistoryCur.close();
        }
      } else if (Helper.childScreeningObj.getFamilyHistoryDiseases() != null) {
        ArrayList<FamilyHistoryDisease> familyHistoryDiseases = Helper.childScreeningObj
          .getFamilyHistoryDiseases();
        for (FamilyHistoryDisease disease : familyHistoryDiseases) {
          if (disease.isSelected() == true) {
            familyHistoryStr += disease.getDiseaseName() + ",";
          }
        }
        if (familyHistoryStr.endsWith(",")) {
          familyHistoryStr = familyHistoryStr.substring(0,
            familyHistoryStr.lastIndexOf(","));
        }
      }
      tv_screening_basic_student_diseases_history
        .setText(familyHistoryStr);
    }
  }

  /**
   *
   */
  private HashMap<String, String> getFamilyHistoryDataFromDB() {

    String query = "select * from familyhistory FH where  FH.IsDeleted!=1";
    Cursor cursor = dbh.getCursorData(this.getActivity(), query);
    HashMap<String, String> dataHash = new HashMap<String, String>();
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor
        .getColumnIndex("FamilyHistoryID"));
      String name = cursor
        .getString(cursor.getColumnIndex("DisplayText"));
      dataHash.put(id, name);
    }
    return dataHash;
  }

  /**
   * get child data from DB
   */
  protected void getChildDetailsFromDB() {

		/*
     * String query_Wards =
		 * "select users.FirstName,users.MiddleName,users.LastName,users.DateOfBirth,"
		 * +
		 * "users.GenderID,users.HealthCardNumber,users.LocalAddressID,gender.DisplayText,"
		 * + "children.MCTSID,children.PermanentAddressID," +
		 * "children.IdentificationMark1,children.LocalChildrenID," +
		 * "users.LocalUserID,childreninstitutes.LocalInstituteID,users.LocalContactID,  "
		 * + "childreninstitutes.RollNumber,childreninstitutes.ClassID," +
		 * "childreninstitutes.SectionID," +
		 * "childreninstitutes.AdmissionNumber,childreninstitutes.AdmissionDate,users.CasteID,users.ReligionID, "
		 * +
		 * "users.AadharNumber,institutes.InstituteTypeId,users.HealthCardNumber,"
		 * +
		 * "users.RationCardNumber,children.PWDCardNumber,children.HasDisability,"
		 * +
		 * "childrenparents.LocalUserID as parentID from users inner join children on children.LocalUserID=users.LocalUserID "
		 * + "inner join gender on gender.GenderID=users.GenderID  " +
		 * "inner join childrenparents on childrenparents.LocalChildrenID=children.LocalChildrenID "
		 * +
		 * " inner join institutes on institutes.LocalInstituteID=childreninstitutes.LocalInstituteID  "
		 * +
		 * "inner join childreninstitutes on childreninstitutes.LocalChildrenID=children.LocalChildrenID where children.LocalChildrenID='"
		 * + childID + "'";
		 */

    String query = "select users.FirstName,users.MiddleName,users.LastName,users.DateOfBirth,"
      + "users.GenderID,users.HealthCardNumber,users.LocalAddressID,gender.DisplayText,"
      + "children.MCTSID,children.LocalPermanentAddressID,"
      + "children.IdentificationMark1,children.LocalChildrenID,"
      + "users.LocalUserID,children.LocalInstituteID,users.LocalContactID,  "
      + "children.RollNumber,children.ClassID,children.ChildrenStatusID,"
      + "children.SectionID,"
      + "children.AdmissionNumber,children.AdmissionDate,users.CasteID,users.ReligionID, "
      + "users.AadharNumber,institutes.InstituteTypeId,users.HealthCardNumber,"
      + "users.RationCardNumber,children.PWDCardNumber,children.HasDisability"
      + " from users inner join children on children.LocalUserID=users.LocalUserID "
      + "inner join gender on gender.GenderID=users.GenderID  "
      // +
      // "inner join childrenparents on childrenparents.LocalChildrenID=children.LocalChildrenID "
      + " inner join institutes on institutes.LocalInstituteID=children.LocalInstituteID  "
    //  + " inner join  childrenscreening on childrenscreen"
      // +
      // "inner join childreninstitutes on childreninstitutes.LocalChildrenID=children.LocalChildrenID "
      + "where  institutes.IsDeleted!=1 AND  children.IsDeleted!=1 AND  users.IsDeleted!=1 AND  children.LocalChildrenID='"
      + childID + "'";
    // AND  childrenscreen.ChildrenScreenStatusID!=3"

    Cursor childCur = dbh.getCursorData(this.getActivity(), query);
    setDataToModel(childCur);
  }

  /**
   * Set Child data to model
   */
  private void setDataToModel(Cursor childCur) {
    if (childCur != null) {
      try {
        if (childCur.moveToFirst()) {
          do {
            Children childModelObj = new Children();
            ChildrenInstitutes childInstModel = new ChildrenInstitutes();
            Address address = new Address();
            Address permanentAddress = new Address();

            // Users childUserModel=new Users();
            childModelObj.setChildrenID(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("LocalChildrenID"))));

            childModelObj.setChildrenStatusID(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("ChildrenStatusID"))));

            childModelObj.setMCTSID(childCur.getString(childCur
              .getColumnIndex("MCTSID")));
            childModelObj.setDateOfBirth(childCur
              .getString(childCur
                .getColumnIndex("DateOfBirth")));

            String dOB = childCur.getString(childCur
              .getColumnIndex("DateOfBirth"));
            if (!TextUtils.isEmpty(dOB)) {
              int age = 0;
              int ageInMonths = 0;
              try {
                age = Helper.getAge(dOB);
                ageInMonths = Helper.getAgeInMonths(dOB);
              } catch (Exception e) {
                e.printStackTrace();
              }
              childModelObj.setAge(age);
              childModelObj.setAgeInMonths(ageInMonths);
            }
            childModelObj.setAadharNumber(childCur
              .getString(childCur
                .getColumnIndex("AadharNumber")));
            childModelObj.setUserID(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("LocalUserID"))));
            childModelObj.setRationCardNumber(childCur
              .getString(childCur
                .getColumnIndex("RationCardNumber")));
            childModelObj.setHealthCardNumber(childCur
              .getString(childCur
                .getColumnIndex("HealthCardNumber")));
            childModelObj
              .setIdentificationMark1(childCur.getString(childCur
                .getColumnIndex("IdentificationMark1")));
            childModelObj.setPWDCardNumber(childCur
              .getString(childCur
                .getColumnIndex("PWDCardNumber")));

            childModelObj.setHasDisability(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("HasDisability"))));

            childModelObj.setReligionID(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("ReligionID"))));
            String castIDStr = childCur.getString(childCur
              .getColumnIndex("CasteID"));
            if (!TextUtils.isEmpty(castIDStr)) {
              childModelObj.setCasteID(NumUtil.IntegerParse
                .parseInt(castIDStr));
            }
            childInstModel.setInstituteID(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("LocalInstituteID"))));
            childInstModel.setInstituteTypeId(NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("InstituteTypeId"))));
            Helper.instTypeId = NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("InstituteTypeId")));
            childInstModel.setAdmissionDate(childCur
              .getString(childCur
                .getColumnIndex("AdmissionDate")));
            childInstModel.setRollNumber(childCur
              .getString(childCur
                .getColumnIndex("RollNumber")));
            if (!TextUtils.isEmpty(childCur.getString(childCur
              .getColumnIndex("ClassID")))) {
              childInstModel.setClassID(NumUtil.IntegerParse
                .parseInt(childCur.getString(childCur
                  .getColumnIndex("ClassID"))));
            }
            if (!TextUtils.isEmpty(childCur.getString(childCur
              .getColumnIndex("SectionID")))) {
              childInstModel.setSectionID(NumUtil.IntegerParse
                .parseInt(childCur.getString(childCur
                  .getColumnIndex("SectionID"))));
            }

            childInstModel.setAdmissionNumber(childCur
              .getString(childCur
                .getColumnIndex("AdmissionNumber")));
            childModelObj.setFirstName(childCur.getString(childCur
              .getColumnIndex("FirstName")));
            childModelObj.setLastName(childCur.getString(childCur
              .getColumnIndex("LastName")));
            childModelObj.setMiddleName(childCur.getString(childCur
              .getColumnIndex("MiddleName")));

            String localContactStr = childCur.getString(childCur
              .getColumnIndex("LocalContactID"));
            if (!TextUtils.isEmpty(localContactStr)) {
              childModelObj.setContactID(NumUtil.IntegerParse
                .parseInt(localContactStr));
              childModelObj
                .setEmgContact(getDbContacts(childModelObj
                  .getContactID()));
              childModelObj.setEmgContactID(NumUtil.IntegerParse
                .parseInt(localContactStr));
            }

            Gender localGender = new Gender();
            localGender.setGenderName(childCur.getString(childCur
              .getColumnIndex("DisplayText")));
            int GenderID = NumUtil.IntegerParse
              .parseInt(childCur.getString(childCur
                .getColumnIndex("GenderID")));
            childModelObj.setGenderID(GenderID);
            localGender.setGenderID(GenderID);
            // childParentID = Integer
            // .parseInt(childCur.getString(childCur
            // .getColumnIndex("parentID")));

            childModelObj.setGender(localGender);

            childModelObj.setChildrenInsitute(childInstModel);

            String localAddressIdStr = childCur.getString(childCur
              .getColumnIndex("LocalAddressID"));
            if (!TextUtils.isEmpty(localAddressIdStr)) {
              address = getAddress(address, localAddressIdStr);

              childModelObj.setAddress(address);
              childModelObj.setLocalAddressID(localAddressIdStr);
            }

            String PermanentAddressID = childCur.getString(childCur
              .getColumnIndex("LocalPermanentAddressID"));

            if (!TextUtils.isEmpty(PermanentAddressID)) {
              permanentAddress = getAddress(permanentAddress,
                PermanentAddressID);
              childModelObj
                .setPermanentAddressID(PermanentAddressID);
              childModelObj.setPermanentAddress(permanentAddress);
            }

            Helper.childrenObject = childModelObj;
          } while (childCur.moveToNext());
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        childCur.close();
      }
    }
    getParentDetailsFromDB();
    if (getActivity() != null)
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          setBasicInfotoView();
        }
      });
  }

  private String getDbContacts(int contactID) {
    String contect = null;
    ArrayList<Contacts> dbContacts = new ArrayList<Contacts>();
    String contact_query = "Select * from contactdetails  CD where  CD.IsDeleted!=1 AND  LocalContactID="
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

            dbContacts.add(contactUser);
          } while (contactCur.moveToNext());

        }
      } finally {
        contactCur.close();
      }

      return contect;
    } else {
      return null;
    }
  }

  /**
   * @param address
   * @param string
   */
  private Address getAddress(Address address, String addressgetID) {
    String address_query = "Select AddressName,AddressLine1,AddressLine2,LandMark,PINCode,Post,DriveInstructions,HabitatID,VillageID,PanchayatID,MandalID,DistrictID,StateID from address A where   A.IsDeleted!=1 AND  LocalAddressID="
      + addressgetID;

    Cursor childCur = dbh.getCursorData(this.getActivity(), address_query);
    if (childCur != null) {
      try {
        if (childCur.moveToFirst()) {
          do {
            address.setAddressID(NumUtil.IntegerParse
              .parseInt(addressgetID));
            address.setAddressName(childCur.getString(childCur
              .getColumnIndex("AddressName")));
            address.setAddressLine1(childCur.getString(childCur
              .getColumnIndex("AddressLine1")));
            address.setAddressLine2(childCur.getString(childCur
              .getColumnIndex("AddressLine2")));
            address.setLandMark(childCur.getString(childCur
              .getColumnIndex("LandMark")));
            address.setPINCode(childCur.getString(childCur
              .getColumnIndex("PINCode")));
            address.setPost(childCur.getString(childCur
              .getColumnIndex("Post")));
            address.setDriveInstructions(childCur
              .getString(childCur
                .getColumnIndex("DriveInstructions")));

            Habitation habittate = new Habitation();
            habittate = gethabitationDetails(habittate,
              childCur.getString(childCur
                .getColumnIndex("HabitatID")));
            address.setHabitation(habittate);

            Village village = new Village();
            village = getVillageDetails(village,
              childCur.getString(childCur
                .getColumnIndex("VillageID")));
            address.setVillage(village);

            Panchayat panchayat = new Panchayat();
            panchayat = getPanchayatDetails(panchayat,
              childCur.getString(childCur
                .getColumnIndex("PanchayatID")));
            address.setPanchayat(panchayat);

            Mandal mandal = new Mandal();
            mandal = getMandalDetails(mandal,
              childCur.getString(childCur
                .getColumnIndex("MandalID")));
            address.setMandal(mandal);

            District district = new District();
            district = getDistrictDetails(district,
              childCur.getString(childCur
                .getColumnIndex("DistrictID")));
            address.setDistrict(district);

            State state = new State();
            state = getStateDetails(state,
              childCur.getString(childCur
                .getColumnIndex("StateID")));
            address.setState(state);
          } while (childCur.moveToNext());
        }
      } finally {
        childCur.close();
      }
    }
    return address;

  }

  /**
   * @param state
   * @param string
   * @return
   */
  private State getStateDetails(State state, String stateGetId) {

    String stateQuery = "Select * from states S where  S.IsDeleted!=1 AND  StateID="
      + stateGetId;

    Cursor childCur = dbh.getCursorData(this.getActivity(), stateQuery);
    if (childCur != null) {
      try {
        if (childCur.moveToFirst()) {
          do {
            state.setStateID(NumUtil.IntegerParse
              .parseInt(stateGetId));
            /*
						 * state.setCapital(childCur.getString(childCur
						 * .getColumnIndex("Capital")));
						 * state.setGlobalVersionNo
						 * (IntUtil.Integer.parseInt(childCur.getString(childCur
						 * .getColumnIndex("GlobalVersionNo"))));
						 */
            state.setStateName(childCur.getString(childCur
              .getColumnIndex("DisplayText")));

          } while (childCur.moveToNext());
        }
      } finally {
        childCur.close();
      }
    }

    return state;
  }

  /**
   * @param district
   * @param string
   * @return
   */
  private District getDistrictDetails(District district, String districtGetId) {

    String DistrictQuery = "Select * from districts D  where  D.IsDeleted!=1 AND  DistrictID="
      + districtGetId;

    Cursor childCur = dbh.getCursorData(this.getActivity(), DistrictQuery);
    if (childCur != null) {
      try {
        if (childCur.moveToFirst()) {
          do {
            district.setDistrictID(NumUtil.IntegerParse
              .parseInt(districtGetId));
						/*
						 * district.setCapital(childCur.getString(childCur
						 * .getColumnIndex("Capital")));
						 * district.setGlobalVersionNo
						 * (IntUtil.Integer.parseInt(childCur.getString(childCur
						 * .getColumnIndex("GlobalVersionNo"))));
						 */
            district.setDistrictName(childCur.getString(childCur
              .getColumnIndex("DisplayText")));

          } while (childCur.moveToNext());
        }
      } finally {
        childCur.close();
      }
    }

    return district;
  }

  /**
   * @param mandal
   * @param string
   * @return
   */
  private Mandal getMandalDetails(Mandal mandal, String mandalGetId) {

    String panchayat_query = "Select * from mandals M where  M.IsDeleted!=1 AND  MandalID="
      + mandalGetId;

    Cursor childCur = dbh
      .getCursorData(this.getActivity(), panchayat_query);
    if (childCur != null) {
      try {
        if (childCur.moveToFirst()) {
          do {
            mandal.setMandalID(NumUtil.IntegerParse
              .parseInt(mandalGetId));
						/*
						 * mandal.setCapital(childCur.getString(childCur
						 * .getColumnIndex("Capital")));
						 * mandal.setGlobalVersionNo
						 * (IntUtil.Integer.parseInt(childCur.getString(childCur
						 * .getColumnIndex("GlobalVersionNo"))));
						 */
            mandal.setMandalName(childCur.getString(childCur
              .getColumnIndex("DisplayText")));

          } while (childCur.moveToNext());
        }
      } finally {
        childCur.close();
      }
    }

    return mandal;
  }

  /**
   * @param panchayat
   * @param string
   * @return
   */
  private Panchayat getPanchayatDetails(Panchayat panchayat,
                                        String panchayatGetId) {

    String panchayat_query = "Select * from panchayats P where  P.IsDeleted!=1 AND  PanchayatID="
      + panchayatGetId;

    Cursor childCur = dbh
      .getCursorData(this.getActivity(), panchayat_query);
    if (childCur != null) {
      try {
        if (childCur.moveToFirst()) {
          do {
            panchayat.setPanchayatID(NumUtil.IntegerParse
              .parseInt(panchayatGetId));
						/*
						 * panchayat.setCapital(childCur.getString(childCur
						 * .getColumnIndex("Capital")));
						 * panchayat.setGlobalVersionNo
						 * (IntUtil.Integer.parseInt(childCur.getString(childCur
						 * .getColumnIndex("GlobalVersionNo"))));
						 */
            panchayat.setPanchayatName(childCur.getString(childCur
              .getColumnIndex("DisplayText")));

          } while (childCur.moveToNext());
        }
      } finally {
        childCur.close();
      }
    }

    return panchayat;
  }

  /**
   * @param village
   * @param string
   * @return
   */
  private Village getVillageDetails(Village village, String villagegetid) {

    String village_query = "Select * from villages V where  V.IsDeleted!=1 AND  VillageID="
      + villagegetid;

    Cursor childCur = dbh.getCursorData(this.getActivity(), village_query);
    if (childCur != null) {
      try {
        if (childCur.moveToFirst()) {
          do {
            village.setVillageID(NumUtil.IntegerParse
              .parseInt(villagegetid));
						/*
						 * village.setCapital(childCur.getString(childCur
						 * .getColumnIndex("Capital")));
						 * village.setGlobalVersionNo
						 * (IntUtil.Integer.parseInt(childCur.getString(childCur
						 * .getColumnIndex("GlobalVersionNo"))));
						 */
            village.setVillageName(childCur.getString(childCur
              .getColumnIndex("DisplayText")));
						/*
						 * village.setVillageNCode(childCur.getString(childCur
						 * .getColumnIndex("VillageNCode")));
						 */

          } while (childCur.moveToNext());
        }
      } finally {
        childCur.close();
      }
    }

    return village;
  }

  /**
   * @param habittate
   * @param string
   * @return
   */
  private Habitation gethabitationDetails(Habitation habittate,
                                          String habitategetId) {

    String habitate_query = "Select * from habitats H where  H.IsDeleted!=1 AND  HabitatID="
      + habitategetId;
    if (!TextUtils.isEmpty(habitategetId)) {
      Cursor childCur = dbh.getCursorData(this.getActivity(),
        habitate_query);
      if (childCur != null) {
        try {
          if (childCur.moveToFirst()) {
            do {
              habittate.setHabitatID(NumUtil.IntegerParse
                .parseInt(habitategetId));
							/*
							 * habittate.setCapital(childCur.getString(childCur
							 * .getColumnIndex("Capital")));
							 * habittate.setGlobalVersionNo
							 * (IntUtil.Integer.parseInt
							 * (childCur.getString(childCur
							 * .getColumnIndex("GlobalVersionNo"))));
							 */
              habittate.setHabitatName(childCur
                .getString(childCur
                  .getColumnIndex("DisplayText")));
							/*
							 * habittate.setHabitatNCode(childCur.getString(childCur
							 * .getColumnIndex("HabitatNCode")));
							 */

            } while (childCur.moveToNext());
          }
        } finally {
          childCur.close();
        }
      }
    }
    return habittate;
  }

  /**
   * Update data to textview
   */
  private void setBasicInfotoView() {

    tv_screening_basic_student_roll_no.setText(Helper.childrenObject
      .getChildrenInsitute().getRollNumber());
    tv_screening_basic_student_aadhar_no.setText(Helper.childrenObject
      .getAadharNumber());
    tv_screening_basic_student_ntr_vidya_no.setText(Helper.childrenObject
      .getHealthCardNumber());
    tv_screening_basic_student_name.setText(Helper.childrenObject
      .getFirstName() + " " + Helper.childrenObject.getLastName());

    tv_screening_basic_student_mcts_no.setText(Helper.childrenObject
      .getMCTSID());
    if (Helper.childrenObject.getHasDisability() == 1) {
      tv_screening_basic_student_has_disability.setText("Yes");
    } else {
      tv_screening_basic_student_has_disability.setText("No");
    }
    tv_screening_basic_student_pwd_cardno.setText(Helper.childrenObject
      .getPWDCardNumber());

    tv_screening_basic_student_emergency_contact
      .setText(Helper.childrenObject.getEmgContact());

    ArrayList<Childrenparents> parentAry = Helper.childrenObject
      .getParentAry();
    if (parentAry != null)
      for (int i = 0; i < parentAry.size(); i++) {
        String firstName = parentAry.get(i).getUser().getFirstName();
        if (!TextUtils.isEmpty(firstName)) {
          tv_screening_basic_student_parent_guardian
            .setText(firstName);
          break;
        }
      }

  }

  @Override
  public void onClick(View v) {

    if (v == iv_screening_basic_student_family_history) {
      iv_screening_basic_student_family_history.setEnabled(false);
      iv_screening_basic_student_family_history.setClickable(false);
      dialog = new CustomDialog(getActivity());
      dialog.setCancelable(false);
      dialog.show();
      getChildDetailsFromDB();
      Intent family_history = new Intent(getActivity(),
        ScreeningFamilyHistory.class);
      startActivity(family_history);
    } else if (v == iv_screening_basic_student_next) {

      if (Helper.childrenObject.getChildScreenStatusID() == 3) {
        Toast.makeText(getActivity(), "You Cant screen absent child",
          Toast.LENGTH_SHORT).show();
      } else {
        getChildDetailsFromDB();
        ScreeningActivity.tabFlags[1] = true;
        ScreeningActivity.enableHeaderClick(ScreeningActivity.tabFlags);
        ScreeningActivity.view_basicinfo.setBackgroundColor(Color
          .parseColor("#45cfc1"));
        mScreeningActivity.displayView(
          ScreeningActivity.tv_medical_history,
          this.getActivity());

      }

    } else if (v == iv_screening_basic_student_edit) {
      AddStudentActivityDialog.tabFlags[0] = false;
      AddStudentActivityDialog.tabFlags[1] = false;
      AddStudentActivityDialog.tabFlags[2] = false;
      AddStudentActivityDialog.tabFlags[3] = false;
      AddStudentActivityDialog.tabFlags[4] = false;
			/*
			 * getChildDetailsFromDB(); getDisabiltyDetailsFromDB();
			 * getParentDetailsFromDB();
			 */
      Helper.addChildFlag = false;
      Intent in = new Intent(getActivity(),
        AddStudentActivityDialog.class);
      startActivityForResult(in, 108);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (CustomStudentAdapter.view != null)
      CustomStudentAdapter.view.performClick();

  }

  /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
     * .widget.AdapterView, android.view.View, int, long)
     */
  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position,
                             long id) {

    if (position == 3) {
      new AlertDialog.Builder(getActivity())
        .setTitle("Confirm")
        .setMessage(
          "Do you really want to Make Absent and move to next Child")
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(android.R.string.yes,
          new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,
                                int whichButton) {
              insertAbsent();
            }
          })
        .setNegativeButton(android.R.string.no,
          new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
              spn_screening_basic_student_active
                .setSelection(0);

            }
          }).show();

    } else if (position != 0) {
      Intent in = new Intent(this.getActivity(),
        ScreeningChildDropoutActivity.class);
      // note value is postion + 1
      in.putExtra("value", (position + 1) + "".trim());
      in.putExtra("childID", childID + "".trim());
      startActivity(in);
    } else {
      if (Helper.childrenObject != null)
        Helper.childrenObject.setChildScreenStatusID(2);
    }
  }

  public void insertAbsent() {
    Children childrenObject = Helper.childrenObject;

    if (Helper.childScreeningObj.getScreeningID() == 0) {
      long localChildScreeningID = dbh
        .insertintoTable(
          this.getActivity(),
          "childrenscreening",
          new String[]{"LocalInstituteScreeningDetailID",
            "LocalChildrenID",
            "ScreeningTemplateTypeID",
            "ScreeningStartDateTime",
            "ScreeningEndDateTime",
            "ChildrenScreenStatusID","LastCommitedDate",},
          new String[]{
            String.valueOf(((ScreeningActivity) getActivity()).locInsScreeningDetailID),
            childrenObject.getChildrenID() + "".trim(),
            childrenObject.getChildrenInsitute()
              .getInstituteTypeId() + "".trim(),
            childrenObject.getScreningStartTime(),
            childrenObject.getScreningEndTime(), "3",Helper.getTodayDateTime1(),});
      Helper.childScreeningObj.setScreeningID(localChildScreeningID);


    } else {
      dbh.updateROW(getActivity(), "childrenscreening",
        new String[]{"ChildrenScreenStatusID","LastCommitedDate"},
        new String[]{"3",Helper.getTodayDateTime1()}, "LocalChildrenScreeningID",
        String.valueOf(Helper.childScreeningObj.getScreeningID()));

    }


    if (Helper.childrenObject != null)
      Helper.childrenObject.setChildScreenStatusID(3);
    final ListView localListView = ScreeningActivity.ll_screening_list_students;
    final int lvpos = ScreeningActivity.listSelectedPosition + 1;// Modified
    final View v = localListView.getChildAt(lvpos);
    if (ScreeningActivity.childrenList.size() > ScreeningActivity.listSelectedPosition + 1) {
      int nextPosition = (ScreeningActivity.listSelectedPosition + 1);
      localListView
        .performItemClick(
          localListView
            .getAdapter()
            .getView(
              nextPosition,
              null,
              null),
          nextPosition,
          localListView
            .getAdapter()
            .getItemId(
              nextPosition));

      localListView.getAdapter().getView(
        lvpos, v, localListView);
    }
  }

  /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
     * .widget.AdapterView)
     */
  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onResume()
     */
  @Override
  public void onResume() {
    super.onResume();

    iv_screening_basic_student_family_history.setEnabled(true);
    iv_screening_basic_student_family_history.setClickable(true);
    String familyHistoryStr = "";
    if (Helper.childScreeningObj != null) {
      if (Helper.childScreeningObj.getFamilyHistoryDiseases() != null) {
        ArrayList<FamilyHistoryDisease> familyHistoryDiseases = Helper.childScreeningObj
          .getFamilyHistoryDiseases();
        for (FamilyHistoryDisease disease : familyHistoryDiseases) {
          if (disease.isSelected() == true) {
            familyHistoryStr += disease.getDiseaseName() + ",";
          }
        }
        if (familyHistoryStr.endsWith(",")) {
          familyHistoryStr = familyHistoryStr.substring(0,
            familyHistoryStr.lastIndexOf(","));
        }
      }
      tv_screening_basic_student_diseases_history
        .setText(familyHistoryStr);
    }
  }

}
