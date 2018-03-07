//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.screening.cv;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.ScreeningVitals;
import nunet.rbsk.screening.ScreeningActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  VitalsFragment.java

//* Type    :

//* Description     :
//* References     :
//* Author    : deepika.chevvakula

//* Created Date       :  25-May-2015
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
@SuppressWarnings("unused")
public class ScreeningVitalsFragment extends Fragment implements
  OnClickListener {
  private TextView tv_vitals_student_name;
  private TextView tv_vitals_student_age_sex;
  private RelativeLayout rl_screeing_height;
  private RelativeLayout rl_screeing_weight;
  //
  private RelativeLayout rl_screeing_heightweightratio;
  private RelativeLayout rl_screeing_muac;
  private RelativeLayout rl_screeing_headcircumference;
  //
  private RelativeLayout rl_screeing_temp;
  private RelativeLayout rl_screeing_bloodgroup;
  private RelativeLayout rl_screeing_hemoglobin;
  //
  private RelativeLayout rl_screeing_bmi;
  private RelativeLayout rl_screeing_acuty_vision;
  private RelativeLayout rl_screeing_bp;
  //
  private ImageView iv_vitals_student_next;
  private static ImageView iv_vitalitem_height;
  private static TextView tv_vitalitem_height;
  private static Spinner spn_screen_vital_blood_group;
  private static ImageView iv_vitalitem_weight;
  private static TextView tv_vitalitem_weight;
  private static ImageView iv_vitalitem_hwratio;
  private static TextView tv_vitalitem_hwratio;
  private static ImageView iv_vitalitem_muac;
  private static TextView tv_vitalitem_muac;
  private static ImageView iv_vitalitem_bmi;
  private static TextView tv_vitalitem_bmi;
  private static ImageView iv_vitalitem_head;
  private static TextView tv_vitalitem_head;
  public static TextView tv_vitalitem_l_vision;
  public static TextView tv_vitalitem_r_vision;
  private static String type_ofinstitute;
  // static Cursor cur;

  static ScreenVitalHeight vitalHeightDialog;
  static ScreeningVitalsWeight vitalWeightDialog;
  static ScreenVitalBlood vitalBloodDialog;
  static ScreenVitalBP vitalBP;
  static ScreenVitalHead vitalHead;
  static ScreenVitalHemoglobin vitalHemo;
  static ScreenVitalMuac vitalMuac;
  static ScreenVitalTemp vitalTemp;
  static ScreenVitalAcuity vitalAcuity;
  private static int genderId;

  private int bpIndication;
  private static float[] weight_values = new float[7];
  private static float[] height_values = new float[7];
  private static float[] muac_values = new float[7];
  private static float[] head_values = new float[7];
  private static float[] hemo_values = new float[7];
  private static float[] bmi_values = new float[7];
  private static float[] hw_ratio = new float[7];
  private double bmi;
  private static Activity activity;
  private static DBHelper dbh;

  public static ScreeningVitals vitalsObj;
  private static int age;
  private static int ageInMonths;
  private static int instType;
  private ImageView iv_screening_basic_student_image;
  private static TextView tv_vitalitem_temp;
  private static TextView tv_vitalitem_blood;
  private static TextView tv_vitalitem_hemo;
  private static TextView tv_vitalitem_bp;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    dbh = DBHelper.getInstance(this.getActivity());

    instType = Helper.childrenObject.getChildrenInsitute()
      .getInstituteTypeId();
    if (instType == 1) {
      type_ofinstitute = "awc";
    } else {
      type_ofinstitute = "school";
    }
    if (Helper.childrenObject.getGender() != null) {
      genderId = Helper.childrenObject.getGender().getGenderID();
    } else {
      genderId = 0;
    }

    View rootView;
    if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
      rootView = inflater.inflate(R.layout.vitals_awc, container, false);

    } else {
      rootView = inflater.inflate(R.layout.vitals_school, container,
        false);

    }
    activity = (Activity) rootView.getContext();

    age = Helper.childrenObject.getAgeInMonths() / 12;
    ageInMonths = Helper.childrenObject.getAgeInMonths();

    int childScreenStatusID = Helper.childrenObject
      .getChildScreenStatusID();

    if (Helper.childScreeningObj.getVitals() != null) {
      vitalsObj = Helper.childScreeningObj.getVitals();
    } else if (childScreenStatusID != 0 && childScreenStatusID != 2
      && childScreenStatusID != 3) {
      // 2-not screened,3 absent
      ScreeningVitals screenedVitals = getVitalsDataFromDB();
      if (screenedVitals != null) {
        Helper.childScreeningObj.setVitals(screenedVitals);
      }
    } else {
      vitalsObj = new ScreeningVitals();
      weight_values = getWeightDb();
      height_values = getHeightDb();
      muac_values = getMuacDb();
      head_values = getHeadDb();
      hemo_values = getHemoGlobinDb();
      bmi_values = getBmiDb();

      bmi = (double) (weight_values[3] / (height_values[3]
        * height_values[3] * 0.0001));
      if (Double.isNaN(bmi) || Double.isInfinite(bmi))
        bmi = 0;
      String bmi_String = String.format("%.2f", bmi);
      vitalsObj.setBmi(Float.parseFloat(bmi_String));
      vitalsObj.setAcutyVisionLeft("0");
      vitalsObj.setAcutyVisionRight("0");

      // bpIndication = getBPDb((int) vitalsObj.getHeight(),
      // getActivity());

			/*
       * vitalsObj.setHemoglobinId(0);
			 * if(vitalsObj.getHemoglobinIndication().equals("98.06")){
			 * vitalsObj.setHemoglobinIndication("98.06");}
			 * vitalsObj.setTemperatureId(0);
			 * if(vitalsObj.getTemperatureIndication().equals("98.06")){
			 * vitalsObj.setTemperatureIndication("98.06");}
			 * vitalsObj.setBloodGroupId(0);
			 * if(*vitalsObj.getBloodGroupNotes().equals("Select")){
			 * vitalsObj.setBloodGroupNotes("Select");} bpIndication =
			 * getBPDb(age, Math.round(vitalsObj.getHeight()), genderId,
			 * activity, "0"); if(vitalsObj.getBp()==0){
			 * vitalsObj.setBp(bpIndication);}
			 */

    }

    findViews(rootView);

    String message = "";
    int ageInMonths = Helper.childrenObject.getAgeInMonths();
    message += (ageInMonths / 12) == 0 ? "" : (ageInMonths / 12)
      + " Years ";
    message += (ageInMonths % 12) == 0 ? "" : (ageInMonths % 12)
      + " Months, ";
    if (Helper.childrenObject.getGender() != null)
      message += Helper.childrenObject.getGender().getGenderName();


    tv_vitals_student_age_sex.setText(message);
    return rootView;
  }

  public ScreeningVitals getVitalsDataFromDB() {
    String query = "select * from childrenscreeningvitals where IsDeleted!=1 and   LocalChildrenScreeningID='"
      + Helper.childScreeningObj.getScreeningID() + "';";
    Cursor cursor = dbh.getCursorData(this.getActivity(), query);
    if (cursor != null) {
      ScreeningVitals localVitals = new ScreeningVitals();
      if (cursor.moveToFirst()) {
        do {
          localVitals.setHeight(Float.parseFloat(cursor
            .getString(cursor.getColumnIndex("Height"))));
          localVitals.setHeightIndication(cursor.getString(cursor
            .getColumnIndex("HeightIndication")));

          localVitals.setWeight(Float.parseFloat(cursor
            .getString(cursor.getColumnIndex("Weight"))));
          localVitals.setWeightIndication(cursor.getString(cursor
            .getColumnIndex("WeightIndication")));

          String BMI = cursor.getString(cursor.getColumnIndex("BMI"));
          if (TextUtils.isEmpty(BMI))
            BMI = "0";
          localVitals.setBmi(Float.parseFloat(BMI));
          localVitals.setBmiIndication(cursor.getString(cursor
            .getColumnIndex("BMIIndication")));

          localVitals.setAcutyVisionLeft(cursor.getString(cursor
            .getColumnIndex("AcuityOfVisionLefteye")));
          localVitals.setAcutyVisionRight(cursor.getString(cursor
            .getColumnIndex("AcuityOfVisionRighteye")));

          localVitals.setBp((cursor.getString(cursor
            .getColumnIndex("BP"))));
          localVitals.setBpIndication(cursor.getString(cursor
            .getColumnIndex("BPIndication")));

          localVitals.setBloodGroupId(NumUtil.IntegerParse
            .parseInt(cursor.getString(cursor
              .getColumnIndex("BloodGroupID"))));
          localVitals.setBloodGroupNotes(cursor.getString(cursor
            .getColumnIndex("BloodGroupNotes")));

          localVitals.setTemperatureId(NumUtil.IntegerParse
            .parseInt(cursor.getString(cursor
              .getColumnIndex("TemperatureID"))));
          localVitals.setTemperatureIndication(cursor
            .getString(cursor
              .getColumnIndex("TemperatureIndication")));

          localVitals.setHemoglobinId(NumUtil.IntegerParse
            .parseInt(cursor.getString(cursor
              .getColumnIndex("HemoGlobinID"))));
          localVitals.setHemoglobinIndication(cursor.getString(cursor
            .getColumnIndex("HemoGlobinIndication")));

          localVitals.setMuacCm(Float.parseFloat(cursor
            .getString(cursor.getColumnIndex("MUACInCms"))));
          localVitals.setMuacIndication(cursor.getString(cursor
            .getColumnIndex("MUACIndication")));

          localVitals
            .setHeadCircumferenceCm(Float.parseFloat(cursor.getString(cursor
              .getColumnIndex("HeadCircumferenceInCms"))));
          localVitals
            .setHeadCircumferenceIndication(cursor.getString(cursor
              .getColumnIndex("HeadCircumferenceIndication")));

        } while (cursor.moveToNext());
      }
      cursor.close();
      return localVitals;
    }
    return null;
  }

  /**
   * @param rootView
   */
  private void findViews(View rootView) {
    tv_vitals_student_name = (TextView) rootView
      .findViewById(R.id.tv_vitals_student_name);
    tv_vitals_student_name.setText(Helper.childrenObject.getFirstName()
      + " " + Helper.childrenObject.getLastName());
    tv_vitals_student_age_sex = (TextView) rootView
      .findViewById(R.id.tv_vitals_student_age_sex);
    rl_screeing_height = (RelativeLayout) rootView
      .findViewById(R.id.rl_screeing_height);
    rl_screeing_height.setOnClickListener(this);
    rl_screeing_weight = (RelativeLayout) rootView
      .findViewById(R.id.rl_screeing_weight);
    rl_screeing_weight.setOnClickListener(this);
    iv_vitalitem_height = (ImageView) rootView
      .findViewById(R.id.iv_vitalitem_height);
    tv_vitalitem_height = (TextView) rootView
      .findViewById(R.id.tv_vitalitem_height);
    spn_screen_vital_blood_group = (Spinner) rootView
      .findViewById(R.id.spn_screen_vital_blood_group);

    iv_vitalitem_weight = (ImageView) rootView
      .findViewById(R.id.iv_vitalitem_weight);
    tv_vitalitem_weight = (TextView) rootView
      .findViewById(R.id.tv_vitalitem_weight);

    iv_screening_basic_student_image = (ImageView) rootView
      .findViewById(R.id.iv_screening_basic_student_image);
    iv_screening_basic_student_image.setImageBitmap(Helper.childImage);
    tv_vitalitem_temp = (TextView) rootView
      .findViewById(R.id.tv_vitalitem_temp);
    tv_vitalitem_blood = (TextView) rootView
      .findViewById(R.id.tv_vitalitem_blood);
    tv_vitalitem_hemo = (TextView) rootView
      .findViewById(R.id.tv_vitalitem_hemo);
    if (StringUtils.equalsNoCase(vitalsObj.getHemoglobinIndication(),
      "enter")) {
      tv_vitalitem_hemo.setText("Select Value");
    } else {
      tv_vitalitem_hemo.setText(vitalsObj.getHemoglobinIndication());
    }
    if (StringUtils.equalsNoCase(vitalsObj.getTemperatureIndication(),
      "enter")) {
      tv_vitalitem_temp.setText("Select Value");
    } else {
      tv_vitalitem_temp.setText(vitalsObj.getTemperatureIndication());
    }
    if (vitalsObj.getBloodGroupId() == 0) {
      tv_vitalitem_blood.setText("Select Value");
    } else {
      tv_vitalitem_blood.setText(getBloodGroupName(
        vitalsObj.getBloodGroupId() + "", activity));
    }
    if (StringUtils.equalsNoCase(vitalsObj.getTemperatureIndication(),
      "enter")) {
      tv_vitalitem_temp.setText("Select Value");
    } else {
      tv_vitalitem_temp.setText(vitalsObj.getTemperatureIndication());
    }

    // awc///
    if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
      rl_screeing_heightweightratio = (RelativeLayout) rootView
        .findViewById(R.id.rl_screeing_heightweightratio);
      rl_screeing_heightweightratio.setOnClickListener(this);
      rl_screeing_muac = (RelativeLayout) rootView
        .findViewById(R.id.rl_screeing_muac);
      rl_screeing_muac.setOnClickListener(this);
      rl_screeing_headcircumference = (RelativeLayout) rootView
        .findViewById(R.id.rl_screeing_headcircumference);
      rl_screeing_headcircumference.setOnClickListener(this);

      tv_vitalitem_muac = (TextView) rootView
        .findViewById(R.id.tv_vitalitem_muac);
      iv_vitalitem_muac = (ImageView) rootView
        .findViewById(R.id.iv_vitalitem_muac);
      tv_vitalitem_head = (TextView) rootView
        .findViewById(R.id.tv_vitalitem_head);
      iv_vitalitem_head = (ImageView) rootView
        .findViewById(R.id.iv_vitalitem_head);
      iv_vitalitem_hwratio = (ImageView) rootView
        .findViewById(R.id.iv_vitalitem_hwratio);
      tv_vitalitem_hwratio = (TextView) rootView
        .findViewById(R.id.tv_vitalitem_hwratio);

      tv_vitalitem_hwratio.setText("abc");
      // tv_vitalitem_height.setText(vitalsObj.getHeightIndication());
      setToTextView2("" + vitalsObj.getHeight(), "height", activity);
      // tv_vitalitem_weight.setText(vitalsObj.getWeightIndication());
      setToTextView2("" + vitalsObj.getWeight(), "weight", activity);
      // tv_vitalitem_muac.setText(vitalsObj.getMuacIndication());
      setToTextView2("" + vitalsObj.getMuacCm(), "muac", activity);
      // tv_vitalitem_head.setText(vitalsObj.getHeadCircumferenceIndication());
      setToTextView2("" + vitalsObj.getHeadCircumferenceCm(), "head",
        activity);

    }
    // school///
    else {
      rl_screeing_bmi = (RelativeLayout) rootView
        .findViewById(R.id.rl_screeing_bmi);
      rl_screeing_bmi.setOnClickListener(this);
      rl_screeing_acuty_vision = (RelativeLayout) rootView
        .findViewById(R.id.rl_screeing_acuty_vision);
      rl_screeing_acuty_vision.setOnClickListener(this);
      rl_screeing_bp = (RelativeLayout) rootView
        .findViewById(R.id.rl_screeing_bp);
      rl_screeing_bp.setOnClickListener(this);
      tv_vitalitem_l_vision = (TextView) rootView
        .findViewById(R.id.tv_vitalitem_l_vision);
      tv_vitalitem_l_vision.setText("Normal");
      tv_vitalitem_r_vision = (TextView) rootView
        .findViewById(R.id.tv_vitalitem_r_vision);
      tv_vitalitem_bmi = (TextView) rootView
        .findViewById(R.id.tv_vitalitem_bmi);
      iv_vitalitem_bmi = (ImageView) rootView
        .findViewById(R.id.iv_vitalitem_bmi);
      tv_vitalitem_bp = (TextView) rootView
        .findViewById(R.id.tv_vitalitem_bp);
      if (vitalsObj.getBp() != null) {
        tv_vitalitem_bp.setText(vitalsObj.getBp() + "/"
          + vitalsObj.getBpIndication());
      } else {
        tv_vitalitem_bp.setText("Select Value");
      }
      // tv_vitalitem_l_vision.setText(vitalsObj.getAcutyVisionLeft());
      setToTextView2(vitalsObj.getAcutyVisionLeft(), "eye", activity);
      // tv_vitalitem_r_vision.setText(vitalsObj.getAcutyVisionRight());
      setToTextView2(vitalsObj.getAcutyVisionRight(), "eye", activity);
      // tv_vitalitem_height.setText(vitalsObj.getHeightIndication());
      setToTextView2("" + vitalsObj.getHeight(), "height", activity);
      // tv_vitalitem_weight.setText(vitalsObj.getWeightIndication());
      setToTextView2("" + vitalsObj.getWeight(), "weight", activity);
      // tv_vitalitem_bmi.setText(vitalsObj.getBmiIndication());
      setToTextView2("" + vitalsObj.getBmi(), "bmi", activity);
    }

    rl_screeing_temp = (RelativeLayout) rootView
      .findViewById(R.id.rl_screeing_temp);
    rl_screeing_temp.setOnClickListener(this);
    rl_screeing_bloodgroup = (RelativeLayout) rootView
      .findViewById(R.id.rl_screeing_bloodgroup);
    rl_screeing_bloodgroup.setOnClickListener(this);
    rl_screeing_hemoglobin = (RelativeLayout) rootView
      .findViewById(R.id.rl_screeing_hemoglobin);
    rl_screeing_hemoglobin.setOnClickListener(this);
    iv_vitals_student_next = (ImageView) rootView
      .findViewById(R.id.iv_vitals_student_next);
    iv_vitals_student_next.setOnClickListener(this);
  }

  public static void setToTextView2(String type_value, String type,
                                    Context ctx) {
    float type_value_float = 0;
    if (StringUtils.equalsNoCase(type, "eye")) {

    } else if (StringUtils.equalsNoCase(type, "blood")) {

    } else {
      type_value_float = Float.parseFloat(type_value);
    }
    if (StringUtils.equalsNoCase(type, "height")) {
      double bmi_in = (double) (vitalsObj.getWeight() / (type_value_float
        * type_value_float * 0.0001));
      if (Double.isInfinite(bmi_in) || Double.isNaN(bmi_in))
        bmi_in = 0;
      String bmi_String = String.format("%.2f", bmi_in);
      vitalsObj.setBmi(Double.parseDouble(bmi_String));
      getSD(ctx, type_value, height_values, tv_vitalitem_height,
        iv_vitalitem_height, "height");
      vitalsObj.setHeight(type_value_float);

      if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
        gethwRatio();
      } else {
        getSD(ctx, bmi_String, bmi_values, tv_vitalitem_bmi,
          iv_vitalitem_bmi, "bmi");
      }
    } else if (StringUtils.equalsNoCase(type, "weight")) {
      double bmi_in = (double) (type_value_float / (vitalsObj.getHeight()
        * vitalsObj.getHeight() * 0.0001));
      if (Double.isInfinite(bmi_in) || Double.isNaN(bmi_in))
        bmi_in = 0;
      String bmi_String = String.format("%.2f", bmi_in);
      vitalsObj.setBmi(Double.parseDouble(bmi_String));
      getSD(ctx, type_value, weight_values, tv_vitalitem_weight,
        iv_vitalitem_weight, "weight");
      if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
        gethwRatio();
      } else {
        getSD(ctx, bmi_String, bmi_values, tv_vitalitem_bmi,
          iv_vitalitem_bmi, "bmi");
      }

      vitalsObj.setWeight(type_value_float);
    } else if (StringUtils.equalsNoCase(type, "blood")) {
      int blood_id = getBloodIdDb(type_value, activity);
      tv_vitalitem_blood.setText(type_value);
      // vitalsObj.setBloodGroupNotes(type_value);
      vitalsObj.setBloodGroupId(blood_id);
    } else if (StringUtils.equalsNoCase(type, "bp")) {
      String[] bpval_indcate = type_value.split(",");
      // int bpIndica = getBPDb(age, Math.round(vitalsObj.getHeight()),
      // genderId, activity, type_value);
      tv_vitalitem_bp.setText(bpval_indcate[0] + "/" + bpval_indcate[0]);
      vitalsObj.setBp(bpval_indcate[0]);
      vitalsObj.setBpIndication(bpval_indcate[1]);
      vitalBP.dismiss();
    } else if (StringUtils.equalsNoCase(type, "head")) {
      getSD(ctx, type_value, head_values, tv_vitalitem_head,
        iv_vitalitem_head, "head");
      vitalsObj.setHeadCircumferenceCm(type_value_float);
    } else if (StringUtils.equalsNoCase(type, "hwRatio")) {
      getSD(ctx, type_value, head_values, tv_vitalitem_head,
        iv_vitalitem_head, "hwRatio");
      // vitalsObj.setHeadCircumferenceCm(type_value_float);
    } else if (StringUtils.equalsNoCase(type, "hemo")) {// /////////
      int hemo_id = getHemoIdDb(type_value, activity);
      tv_vitalitem_hemo.setText(type_value + "");
      vitalsObj.setHemoglobinIndication(type_value);
      vitalsObj.setHemoglobinId(hemo_id);
    } else if (StringUtils.equalsNoCase(type, "muac")) {
      getSD(ctx, type_value, muac_values, tv_vitalitem_muac,
        iv_vitalitem_muac, "muac");
    } else if (StringUtils.equalsNoCase(type, "temp")) {// ////////////////////////////////////
      int temp_id = getTempIdDb(type_value, activity);
      tv_vitalitem_temp.setText(type_value);
      vitalsObj.setTemperatureId(temp_id);
      vitalsObj.setTemperatureIndication(type_value);
    } else if (StringUtils.equalsNoCase(type, "acuity")) {
    } else if (StringUtils.equalsNoCase(type, "eye")) {
      if (!TextUtils.isEmpty(type_value)) {
        String[] eye = type_value.split(",");
        if (eye.length >= 1)
          vitalsObj.setAcutyVisionLeft(eye[0]);
        if (eye.length >= 2)
          vitalsObj.setAcutyVisionRight(eye[1]);
      }

      tv_vitalitem_l_vision.setText("L : "
        + vitalsObj.getAcutyVisionLeft());

      tv_vitalitem_r_vision.setText("R : "
        + vitalsObj.getAcutyVisionRight());
      // vitalsObj.setAcutyVisionRight(eye[1]);

    }
  }

  /**
   * @return
   *
   */

  /**
   * @param height_value
   */
  public static void setToTextView(String type_value, String type, Context ctx) {
    float type_value_float = 0;
    if (StringUtils.equalsNoCase(type, "eye")) {

    } else if (StringUtils.equalsNoCase(type, "blood")) {

    } else if (StringUtils.equalsNoCase(type, "bp")) {

    } else {
      type_value_float = Float.parseFloat(type_value);
    }
    if (StringUtils.equalsNoCase(type, "height")) {
      double bmi_in = (double) (vitalsObj.getWeight() / (type_value_float
        * type_value_float * 0.0001));
      if (Double.isInfinite(bmi_in) || Double.isNaN(bmi_in))
        bmi_in = 0;
      String bmi_String = String.format("%.2f", bmi_in);
      vitalsObj.setBmi(Double.parseDouble(bmi_String));
      getSD(ctx, type_value, height_values, tv_vitalitem_height,
        iv_vitalitem_height, "height");
      vitalsObj.setHeight(type_value_float);
      vitalHeightDialog.dismiss();
      if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
        gethwRatio();
      } else {
        getSD(ctx, bmi_String, bmi_values, tv_vitalitem_bmi,
          iv_vitalitem_bmi, "bmi");
      }
      vitalHeightDialog.dismiss();
    } else if (StringUtils.equalsNoCase(type, "weight")) {

      double bmi_in = (double) (type_value_float / (vitalsObj.getHeight()
        * vitalsObj.getHeight() * 0.0001));
      if (Double.isInfinite(bmi_in) || Double.isNaN(bmi_in))
        bmi_in = 0;
      String bmi_String = String.format("%.2f", bmi_in);
      vitalsObj.setBmi(Double.parseDouble(bmi_String));
      getSD(ctx, type_value, weight_values, tv_vitalitem_weight,
        iv_vitalitem_weight, "weight");
      if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
        gethwRatio();
      } else {
        getSD(ctx, bmi_String, bmi_values, tv_vitalitem_bmi,
          iv_vitalitem_bmi, "bmi");
      }

      vitalsObj.setWeight(type_value_float);
      vitalWeightDialog.dismiss();
    } else if (StringUtils.equalsNoCase(type, "blood")) {
      int blood_id = getBloodIdDb(type_value, activity);
      tv_vitalitem_blood.setText(type_value);
      vitalsObj.setBloodGroupId(blood_id);
      // vitalsObj.setBloodGroupNotes(type_value);
      vitalBloodDialog.dismiss();
    } else if (StringUtils.equalsNoCase(type, "bp")) {
      String[] bpval_indcate = type_value.split(",");
      // int bpIndica = getBPDb(age, Math.round(vitalsObj.getHeight()),
      // genderId, activity, type_value);
      tv_vitalitem_bp.setText(bpval_indcate[0] + "/" + bpval_indcate[1]);
      vitalsObj.setBp(bpval_indcate[0]);
      vitalsObj.setBpIndication(bpval_indcate[1]);
      vitalBP.dismiss();
    } else if (type.equals("head")) {
      getSD(ctx, type_value, head_values, tv_vitalitem_head,
        iv_vitalitem_head, "head");
      vitalsObj.setHeadCircumferenceCm(type_value_float);
      vitalHead.dismiss();
    } else if (StringUtils.equalsNoCase(type, "hemo")) {
      int hemo_id = getHemoIdDb(type_value, activity);
      tv_vitalitem_hemo.setText(type_value + "");
      vitalsObj.setHemoglobinId(hemo_id);
      vitalsObj.setHemoglobinIndication(type_value);
      vitalHemo.dismiss();
    } else if (type.equals("muac")) {
      getSD(ctx, type_value, muac_values, tv_vitalitem_muac,
        iv_vitalitem_muac, "muac");
      vitalMuac.dismiss();
    } else if (StringUtils.equalsNoCase(type, "temp")) {
      int temp_id = getTempIdDb(type_value, activity);
      tv_vitalitem_temp.setText(type_value);
      vitalsObj.setTemperatureId(temp_id);
      vitalsObj.setTemperatureIndication(type_value);
      vitalTemp.dismiss();
    } else if (StringUtils.equalsNoCase(type, "acuity")) {

      vitalAcuity.dismiss();
    } else if (StringUtils.equalsNoCase(type, "eye")) {

      if (!TextUtils.isEmpty(type_value)) {
        String[] eye = type_value.split(",");
        if (eye.length >= 1) {
          vitalsObj.setAcutyVisionLeft(eye[0]);
          tv_vitalitem_l_vision.setText("L : " + eye[0]);
        }
        if (eye.length >= 2) {
          vitalsObj.setAcutyVisionRight(eye[1]);
          tv_vitalitem_r_vision.setText("R : " + eye[1]);
        }
      }

      vitalAcuity.dismiss();
    }
  }

  /**
   * @param type_value
   * @param type
   * @param height_values2
   * @param iv_vitalitem_height2
   * @param tv_vitalitem_height2
   */
  private static void getSD(Context ctx, String type_value,
                            float[] db_values, TextView tv_vitalitem, ImageView iv_vitalitem,
                            String type) {
    if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
      float typeValue = Float.parseFloat(type_value);
      if (typeValue < db_values[1]) {
        setToVitalsObj(type_value, "<-3SD", type);
        tv_vitalitem.setText("<-3SD");
        iv_vitalitem.setBackgroundDrawable(ctx.getResources()
          .getDrawable(R.drawable.downarrow));

      } else if (typeValue < db_values[2]) {
        setToVitalsObj(type_value, "<-2SD", type);
        tv_vitalitem.setText("<-2SD");
        iv_vitalitem.setBackgroundDrawable(ctx.getResources()
          .getDrawable(R.drawable.downarrow));
      } else if (typeValue < db_values[3]) {
        setToVitalsObj(type_value, "<-1SD", type);
        tv_vitalitem.setText("<-1SD");
        iv_vitalitem.setBackgroundDrawable(ctx.getResources()
          .getDrawable(R.drawable.downarrow));
      } else if (typeValue < db_values[4]) {
        setToVitalsObj(type_value, "Normal", type);
        tv_vitalitem.setText("Normal");
        iv_vitalitem.setBackgroundDrawable(ctx.getResources()
          .getDrawable(R.drawable.tickmark));
      } else if (typeValue < db_values[5]) {
        setToVitalsObj(type_value, "<1SD", type);
        tv_vitalitem.setText("<1SD");
        iv_vitalitem.setBackgroundDrawable(ctx.getResources()
          .getDrawable(R.drawable.uparrow));
      } else if (typeValue < db_values[6]) {
        setToVitalsObj(type_value, "<2SD", type);
        tv_vitalitem.setText("<2SD");
        iv_vitalitem.setBackgroundDrawable(ctx.getResources()
          .getDrawable(R.drawable.uparrow));
      } else {
        setToVitalsObj(type_value, "<3SD", type);
        tv_vitalitem.setText("<3SD");
        iv_vitalitem.setBackgroundDrawable(ctx.getResources()
          .getDrawable(R.drawable.uparrow));
      }
    } else {
      tv_vitalitem.setText(type_value);
    }

  }

  /**
   * @param type_value
   * @param string
   * @param type
   */
  private static void setToVitalsObj(String type_value, String string,
                                     String type) {
    float type_valuein = Float.parseFloat(type_value);
    if (StringUtils.equalsNoCase(type, "height")) {
      vitalsObj.setHeight(type_valuein);
      vitalsObj.setHeightIndication(string);
    } else if (StringUtils.equalsNoCase(type, "weight")) {
      vitalsObj.setWeight(type_valuein);
      vitalsObj.setWeightIndication(string);
    } else if (StringUtils.equalsNoCase(type, "muac")) {
      vitalsObj.setMuacCm(type_valuein);
      vitalsObj.setMuacIndication(string);
    } else if (StringUtils.equalsNoCase(type, "bmi")) {
      vitalsObj.setBmi(type_valuein);
      vitalsObj.setBmiIndication(string);
    }
    Helper.childScreeningObj.setVitals(vitalsObj);
  }

  /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
  @Override
  public void onClick(View v) {
    if (v == rl_screeing_height) {
      // Intent height = new Intent(getActivity(),
      // ScreenVitalHeight.class);
      // startActivity(height);
      vitalHeightDialog = new ScreenVitalHeight();
      vitalHeightDialog.show(this.getFragmentManager(), "height");
    } else if (v == rl_screeing_weight) {
      /*
			 * Intent weight = new Intent(getActivity(),
			 * ScreeningVitalsWeight.class); startActivity(weight);
			 */
      vitalWeightDialog = new ScreeningVitalsWeight();

      vitalWeightDialog.show(this.getFragmentManager(), "");
    } else if (v == rl_screeing_heightweightratio) {
    } else if (v == rl_screeing_muac) {

      vitalMuac = new ScreenVitalMuac();

      vitalMuac.show(this.getFragmentManager(), "");
    } else if (v == rl_screeing_headcircumference) {

      vitalHead = new ScreenVitalHead();

      vitalHead.show(this.getFragmentManager(), "");
    } else if (v == rl_screeing_temp) {

      vitalTemp = new ScreenVitalTemp();

      vitalTemp.show(this.getFragmentManager(), "");
    } else if (v == rl_screeing_bloodgroup) {
      vitalBloodDialog = new ScreenVitalBlood();

      vitalBloodDialog.show(this.getFragmentManager(), "");
    } else if (v == rl_screeing_hemoglobin) {
      vitalHemo = new ScreenVitalHemoglobin();

      vitalHemo.show(this.getFragmentManager(), "");
    } else if (v == rl_screeing_bmi) {
    } else if (v == rl_screeing_acuty_vision) {
      vitalAcuity = new ScreenVitalAcuity();
      vitalAcuity.show(this.getFragmentManager(), "");
    } else if (v == rl_screeing_bp) {
      vitalBP = new ScreenVitalBP();
      vitalBP.show(this.getFragmentManager(), "");
    } else if (v == iv_vitals_student_next) {

      Helper.childScreeningObj.setVitals(vitalsObj);
      ScreeningActivity.tabFlags[3] = true;
      ScreeningActivity.enableHeaderClick(ScreeningActivity.tabFlags);
      ScreeningActivity.view_capture_vitals.setBackgroundColor(Color
        .parseColor("#45cfc1"));
      ((ScreeningActivity) getActivity()).displayView(
        ScreeningActivity.tv_screen, this.getActivity());

    }
  }

  private static void gethwRatio() {

    String hwQuery;

    hwQuery = "SELECT "
      + "H.SAMReferenceID , H.Length,"
      + "(CASE "
      + " "
      + "WHEN "
      + vitalsObj.getWeight()
      + ">CAST(Negative2SD as decimal) THEN 'Median' "
      + " WHEN "
      + vitalsObj.getWeight()
      + ">CAST(Negative3SD as decimal) AND "
      + vitalsObj.getWeight()
      + "<=CAST(Negative2SD as decimal) THEN 'Negative2SD'"
      + " WHEN "
      + vitalsObj.getWeight()
      + "<=CAST(Negative3SD as decimal) THEN 'Negative3SD' END ) 	AS  HWRATIO "
      + "FROM  samreferences H	WHERE H.IsDeleted!=1 and  	H.GenderID = "
      + genderId + " and  CAST(H.Length as decimal)  >" + vitalsObj.getHeight()
      + " LIMIT 1";

    Cursor cur = dbh.getCursorData(activity, hwQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {

            String hwratio = cur.getString(cur
              .getColumnIndex("HWRATIO"));
            vitalsObj.setHwRatio(hwratio);
            if (StringUtils.equalsNoCase(hwratio, "Negative3SD")) {
              tv_vitalitem_hwratio.setText("<-3SD");
              iv_vitalitem_hwratio.setBackgroundDrawable(activity
                .getResources().getDrawable(
                  R.drawable.downarrow));
            } else if (hwratio.equals("Negative2SD")) {
              tv_vitalitem_hwratio.setText("<-2SD");
              iv_vitalitem_hwratio.setBackgroundDrawable(activity
                .getResources().getDrawable(
                  R.drawable.downarrow));
            } else if (StringUtils.equalsNoCase(hwratio, "Median")) {
              tv_vitalitem_hwratio.setText("Normal");
              iv_vitalitem_hwratio.setBackgroundDrawable(activity
                .getResources().getDrawable(
                  R.drawable.tickmark));
            }

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

  }

  private float[] getWeightDb() {
    float[] weight_array = new float[7];
    String weightQuery;
    if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
      weightQuery = "Select Negative3SD,Negative2SD,Negative1SD,Median,[3SD],[2SD],[1SD] from weightchart0to5years where IsDeleted!=1 and   AgeinMonths ="
        + ageInMonths + " and GenderID=" + genderId;
    } else {
      weightQuery = "Select MedianWeightInKGs from weightchart6to18years where IsDeleted!=1 and   AgeinYears ="
        + age + " and GenderID=" + genderId;
    }
    Cursor cur = dbh.getCursorData(this.getActivity(), weightQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {
            if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
              weight_array[0] = cur.getFloat(cur
                .getColumnIndex("Negative3SD"));
              weight_array[1] = cur.getFloat(cur
                .getColumnIndex("Negative2SD"));
              weight_array[2] = cur.getFloat(cur
                .getColumnIndex("Negative1SD"));
              weight_array[3] = cur.getFloat(cur
                .getColumnIndex("Median"));
              weight_array[4] = cur.getFloat(cur
                .getColumnIndex("1SD"));
              weight_array[5] = cur.getFloat(cur
                .getColumnIndex("2SD"));
              weight_array[6] = cur.getFloat(cur
                .getColumnIndex("3SD"));
              vitalsObj.setWeight(weight_array[3]);
              vitalsObj.setWeightIndication("Normal");
            } else {
              weight_array[0] = cur.getFloat(cur
                .getColumnIndex("MedianWeightInKGs"));
              vitalsObj.setWeight(weight_array[0]);
              vitalsObj.setWeightIndication("Normal");
            }
          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return weight_array;

  }

  private float[] getHeightDb() {
    float[] height_array = new float[7];
    String heightQuery;
    if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
      heightQuery = "Select Negative3SD,Negative2SD,Negative1SD,Median,[3SD],[2SD],[1SD] from heightchart0to5years where  IsDeleted!=1 and  AgeinMonths ="
        + ageInMonths + " and GenderID=" + genderId;
    } else {
      heightQuery = "Select MedianHeightInCms from heightchart6to18years where IsDeleted!=1 and   AgeinMonths"
        + " =" + age + " and GenderID=" + genderId;
    }
    Cursor cur = dbh.getCursorData(this.getActivity(), heightQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {
            if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
              height_array[0] = cur.getFloat(cur
                .getColumnIndex("Negative3SD"));
              height_array[1] = cur.getFloat(cur
                .getColumnIndex("Negative2SD"));
              height_array[2] = cur.getFloat(cur
                .getColumnIndex("Negative1SD"));
              height_array[3] = cur.getFloat(cur
                .getColumnIndex("Median"));
              height_array[4] = cur.getFloat(cur
                .getColumnIndex("1SD"));
              height_array[5] = cur.getFloat(cur
                .getColumnIndex("2SD"));
              height_array[6] = cur.getFloat(cur
                .getColumnIndex("3SD"));
              vitalsObj.setHeight(height_array[3]);
              vitalsObj.setHeightIndication("Normal");
            } else {
              height_array[0] = cur.getFloat(cur
                .getColumnIndex("MedianHeightInCms"));
              vitalsObj.setHeight(height_array[0]);
              vitalsObj.setHeightIndication("Normal");
            }
          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return height_array;

  }

  private float[] getMuacDb() {
    float[] muac_array = new float[7];
    String muacQuery;

    muacQuery = "Select Negative3SD,Negative2SD,Negative1SD,Median,[3SD],[2SD],[1SD] from muacreferences where IsDeleted!=1 and   AgeinMonths ="
      + ageInMonths + " and GenderID=" + genderId;

    Cursor cur = dbh.getCursorData(this.getActivity(), muacQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {
            muac_array[0] = cur.getFloat(cur
              .getColumnIndex("Negative3SD"));
            muac_array[1] = cur.getFloat(cur
              .getColumnIndex("Negative2SD"));
            muac_array[2] = cur.getFloat(cur
              .getColumnIndex("Negative1SD"));
            muac_array[3] = cur.getFloat(cur
              .getColumnIndex("Median"));
            muac_array[4] = cur.getFloat(cur.getColumnIndex("1SD"));
            muac_array[5] = cur.getFloat(cur.getColumnIndex("2SD"));
            muac_array[6] = cur.getFloat(cur.getColumnIndex("3SD"));
            vitalsObj.setMuacCm(muac_array[3]);
            vitalsObj.setMuacIndication("Normal");

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return muac_array;

  }

  private float[] getHeadDb() {
    float[] head_array = new float[7];
    String headQuery;

    headQuery = "Select Negative3SD,Negative2SD,Negative1SD,Median,[3SD],[2SD],[1SD] from headcircumference0to5yrs where IsDeleted!=1 and   AgeinMonths ="
      + ageInMonths + " and GenderID=" + genderId;

    Cursor cur = dbh.getCursorData(this.getActivity(), headQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {
            head_array[0] = cur.getFloat(cur
              .getColumnIndex("Negative3SD"));
            head_array[1] = cur.getFloat(cur
              .getColumnIndex("Negative2SD"));
            head_array[2] = cur.getFloat(cur
              .getColumnIndex("Negative1SD"));
            head_array[3] = cur.getFloat(cur
              .getColumnIndex("Median"));
            head_array[4] = cur.getFloat(cur.getColumnIndex("1SD"));
            head_array[5] = cur.getFloat(cur.getColumnIndex("2SD"));
            head_array[6] = cur.getFloat(cur.getColumnIndex("3SD"));
            vitalsObj.setHeadCircumferenceCm(head_array[3]);
            vitalsObj.setHeadCircumferenceIndication("Normal");

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return head_array;

  }

  private float[] getHemoGlobinDb() {
    float[] hemo_array = new float[7];
    String hemoQuery;
    if (genderId == 1) {
      hemoQuery = "Select MaleValue,HemoGlobinID from hemoglobins where IsDeleted!=1 and   MingAge  <"
        + ageInMonths + " AND " + ageInMonths + "<= MaxAgeValue";
    } else {
      hemoQuery = "Select FemaleValue,HemoGlobinID from hemoglobins where IsDeleted!=1 and   MingAge  <"
        + ageInMonths + " AND " + ageInMonths + "<= MaxAgeValue";

    }

    Cursor cur = dbh.getCursorData(this.getActivity(), hemoQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {
            if (genderId == 1) {
              hemo_array[0] = cur.getFloat(cur
                .getColumnIndex("MaleValue"));
            } else {
              hemo_array[0] = cur.getFloat(cur
                .getColumnIndex("FemaleValue"));
            }

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return hemo_array;

  }

  private float[] getBmiDb() {
    float[] bmi_array = new float[7];
    String bmiQuery;

    bmiQuery = "Select Negative3SD,Negative2SD,Negative1SD,Median,[3SD],[2SD],[1SD] from bmi6mto19y where IsDeleted!=1 and   AgeinMonths ="
      + ageInMonths + " and GenderID=" + genderId;

    Cursor cur = dbh.getCursorData(this.getActivity(), bmiQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {
            bmi_array[0] = cur.getFloat(cur
              .getColumnIndex("Negative3SD"));
            bmi_array[1] = cur.getFloat(cur
              .getColumnIndex("Negative2SD"));
            bmi_array[2] = cur.getFloat(cur
              .getColumnIndex("Negative1SD"));
            bmi_array[3] = cur.getFloat(cur
              .getColumnIndex("Median"));
            bmi_array[4] = cur.getFloat(cur.getColumnIndex("1SD"));
            bmi_array[5] = cur.getFloat(cur.getColumnIndex("2SD"));
            bmi_array[6] = cur.getFloat(cur.getColumnIndex("3SD"));
            vitalsObj.setBmi(bmi_array[3]);
            vitalsObj.setBmiIndication("Normal");

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return bmi_array;

  }

  private static int getBloodIdDb(String value, Context ctx) {// /check////
    int blood_id = 0;
    String bloodQuery;

    bloodQuery = "Select BloodGroupID from bloodgroups where IsDeleted!=1 and   DisplayText =  '"
      + value + "'";

    Cursor cur = dbh.getCursorData(ctx, bloodQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {

            blood_id = cur.getInt(cur
              .getColumnIndex("BloodGroupID"));

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return blood_id;

  }

  private static String getBloodGroupName(String value, Context ctx) {// /check////
    String blood_id = "";
    String bloodQuery;

    bloodQuery = "Select DisplayText from bloodgroups where IsDeleted!=1 and   BloodGroupID =  '"
      + vitalsObj.getBloodGroupId() + "'";

    Cursor cur = dbh.getCursorData(ctx, bloodQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {

            blood_id = cur.getString(cur
              .getColumnIndex("DisplayText"));

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return blood_id;

  }

  private static int getTempIdDb(String value, Context ctx) {// /check////
    float hemo = Float.parseFloat(value);
    int hemo_id = 0;
    String hemoQuery;

    hemoQuery = "Select TemperatureID from temperatures where IsDeleted!=1 and   MinTempratureValue  <"
      + hemo + " AND " + hemo + "<= MaxTempratureValue";

    Cursor cur = dbh.getCursorData(ctx, hemoQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {

            hemo_id = cur.getInt(cur
              .getColumnIndex("TemperatureID"));

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }

    return hemo_id;

  }

  private static int getHemoIdDb(String value, Context ctx) {// /check////
    float hemo = Float.parseFloat(value);
    int hemo_id = 0;
    String hemoQuery = "";
    if (genderId == 1) {
      hemoQuery = "Select HemoGlobinID from hemoglobins where IsDeleted!=1 and   MinMaleValue  <"
        + hemo
        + " AND "
        + hemo
        + "<= MaxMaleValue and MingAge  <"
        + ageInMonths + " AND " + ageInMonths + "<= MaxAgeValue";
    } else {
      hemoQuery = "Select HemoGlobinID from hemoglobins where IsDeleted!=1 and   MinFemaleValue  <"
        + hemo
        + " AND "
        + hemo
        + "<= MaxFemaleValue and MingAge  <"
        + ageInMonths
        + " AND " + ageInMonths + "<= MaxAgeValue";

    }

    Cursor cur = dbh.getCursorData(ctx, hemoQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {
            if (cur != null) {
              int columnIndex = cur
                .getColumnIndex("HemoGLobinID");
              hemo_id = cur.getInt(columnIndex);
            }
          } while (cur.moveToNext());
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {

        cur.close();

      }
    }

    return hemo_id;

  }

  private static int getBPDb(int heighta, Context ctx) {
    int bp = 0;
    String bpQuery;
    if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
      bpQuery = "select  BPClassificationID  from bloodpressurereferences where IsDeleted!=1 and   GenderID = "
        + genderId
        + "  and HeightInCMs = "
        + heighta
        + "    and age="
        + ageInMonths
        + "  order by SystolicBPInmmHg desc LIMIT 1";
    } else {
      bpQuery = "select  BPClassificationID  from bloodpressurereferences where IsDeleted!=1 and   GenderID = "
        + genderId
        + "  and HeightInCMs = "
        + heighta
        + "    and age="
        + age
        + " and SystolicBPInmmHg < "
        + instType + "  order by SystolicBPInmmHg desc LIMIT 1";
    }
    Cursor cur = dbh.getCursorData(ctx, bpQuery);
    if (cur != null) {
      try {
        if (cur.moveToFirst()) {

          do {

            bp = cur.getInt(cur
              .getColumnIndex("BPClassificationID"));

          } while (cur.moveToNext());
        }
      } finally {
        cur.close();
      }
    }
    return bp;
  }
}
