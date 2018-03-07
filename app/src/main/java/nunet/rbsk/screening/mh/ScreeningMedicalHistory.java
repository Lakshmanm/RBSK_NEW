package nunet.rbsk.screening.mh;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.MedicalHistoryScreenModel;
import nunet.rbsk.screening.MH_HealthConditionsTable;
import nunet.rbsk.screening.ScreeningActivity;
import nunet.rbsk.screening.ScreeningLastScreenHistory;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  ScreeningMedicalHistory.java

//* Type    :

//* Description     :
//* References     :
//* Author    : promodh.munjeti

//* Created Date       :  May 25, 2015
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
public class ScreeningMedicalHistory extends Fragment implements
  OnClickListener {
  private TextView tv_screening_medical_child_name;
  private TextView tv_screening_medical_gender;
  private Button btn_screening_medical_screen_history;
  private Button btn_screening_medical_next;
  private Button btn_screening_medical_current_medication;
  private Button btn_screening_medical_allergies;
  private Button btn_screening_medical_surgical;
  private Button btn_screening_medical_healthConditions;
  private String gender;
  // private int instType;
  private ImageView iv_screening_medical_student_image;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    getActivity().getWindow().setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    View rootView = inflater.inflate(R.layout.screening_medical_history,
      container, false);
    if (Helper.childScreeningObj.getMedicalHistoryScreenModel() == null) {
      Helper.childScreeningObj
        .setMedicalHistoryScreenModel(new MedicalHistoryScreenModel());
    }

    findViews(rootView);
    if (Helper.childrenObject.getGender() != null) {
      gender = Helper.childrenObject.getGender()
        .getGenderName();
    } else {
      gender = "";
    }
    // instType = Helper.childrenObject.getChildrenInsitute()
    // .getInstituteTypeId();
    int ageInMonths = Helper.childrenObject.getAgeInMonths();
    String message = "";
    message += (ageInMonths / 12) == 0 ? "" : (ageInMonths / 12)
      + " Years ";
    message += (ageInMonths % 12) == 0 ? "" : (ageInMonths % 12)
      + " Months, ";
    message += gender;
    tv_screening_medical_gender.setText(message);
    return rootView;
  }

  /**
   * @param rootView
   */
  private void findViews(View rootView) {
    tv_screening_medical_child_name = (TextView) rootView
      .findViewById(R.id.tv_screening_medical_child_name);
    tv_screening_medical_child_name.setText(Helper.childrenObject
      .getFirstName() + " " + Helper.childrenObject.getLastName());
    tv_screening_medical_gender = (TextView) rootView
      .findViewById(R.id.tv_screening_medical_gender);
    btn_screening_medical_screen_history = (Button) rootView
      .findViewById(R.id.btn_screening_medical_screen_history);
    btn_screening_medical_screen_history.setOnClickListener(this);
    btn_screening_medical_next = (Button) rootView
      .findViewById(R.id.btn_screening_medical_next);
    btn_screening_medical_next.setOnClickListener(this);
    btn_screening_medical_current_medication = (Button) rootView
      .findViewById(R.id.btn_screening_medical_current_medication);
    btn_screening_medical_current_medication.setOnClickListener(this);
    btn_screening_medical_allergies = (Button) rootView
      .findViewById(R.id.btn_screening_medical_allergies);
    btn_screening_medical_allergies.setOnClickListener(this);
    btn_screening_medical_surgical = (Button) rootView
      .findViewById(R.id.btn_screening_medical_surgical);
    btn_screening_medical_surgical.setOnClickListener(this);

    btn_screening_medical_healthConditions = (Button) rootView
      .findViewById(R.id.btn_screening_medical_healthConditions);
    btn_screening_medical_healthConditions.setOnClickListener(this);
    iv_screening_medical_student_image = (ImageView) rootView
      .findViewById(R.id.iv_screening_medical_student_image);
    iv_screening_medical_student_image.setImageBitmap(Helper.childImage);

  }

  /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
  @Override
  public void onClick(View v) {
    if (v == btn_screening_medical_screen_history) {
      Intent medical_screen_history = new Intent(getActivity(),
        ScreeningLastScreenHistory.class);
      getActivity().startActivity(medical_screen_history);
    } else if (v == btn_screening_medical_next) {
      ScreeningActivity.tabFlags[2] = true;
      ScreeningActivity.enableHeaderClick(ScreeningActivity.tabFlags);
      ScreeningActivity.view_medical_history.setBackgroundColor(Color
        .parseColor("#45cfc1"));
      ((ScreeningActivity) getActivity()).displayView(
        ScreeningActivity.tv_capture_vitals, this.getActivity());
    } else if (v == btn_screening_medical_current_medication) {

			/*
       * Intent medication=new Intent(getActivity(),ScreenHistPop.class);
			 * medication.putExtra("Type", "Medication");
			 * getActivity().startActivity(medication);
			 */

    } else if (v == btn_screening_medical_allergies) {
      Intent allergies = new Intent(getActivity(),
        ScreenHistActivityDialog.class);
      allergies.putExtra("Type", "Allergies");
      getActivity().startActivity(allergies);
    } else if (v == btn_screening_medical_surgical) {
      Intent surgical = new Intent(getActivity(),
        ScreenHistActivityDialog.class);
      surgical.putExtra("Type", "Surgical");
      getActivity().startActivity(surgical);
    } else if (v == btn_screening_medical_healthConditions) {
      Intent intent = new Intent(this.getActivity(),
        MH_HealthConditionsTable.class);
      startActivity(intent);
    }

  }
}
