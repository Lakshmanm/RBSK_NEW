//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening.cv;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

//*****************************************************************************
//* Name   :  ScreenVitalAcuity.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  May 30, 2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations
// 3.0			08/06/2015			Anil Kumar Botsa		No Comments
//*****************************************************************************
public class ScreenVitalAcuity extends DialogFragment implements
		OnClickListener {
	private Button btn_screen_vital_vision_save;
	private Button btn_screen_vital_vision_cancel;
	private SeekBar sb_screen_vital_vision_right;
	private SeekBar sb_screen_vital_vision_left;
	private ImageView iv_screen_vital_vision_left_plus;
	private ImageView iv_screen_vital_vision_left_minus;
	private ImageView iv_screen_vital_vision_right_plus;
	private ImageView iv_screen_vital_vision_right_minus;
	private EditText et_screen_vital_vision_left;
	private EditText et_screen_vital_vision_right;
	public String type_ofinstitute;
	public int genderId;
	private int instType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		View view = inflater.inflate(R.layout.screening_vitals_vision,
				container);
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if (instType == 1) {
			type_ofinstitute = "awc";
		} else {
			type_ofinstitute = "school";
		}
		genderId = Helper.childrenObject.getGender().getGenderID();
		findViews(view);
		return view;

	}

	/**
	 * @param view
	 * 
	 */
	private void findViews(View view) {
		et_screen_vital_vision_left = (EditText) view
				.findViewById(R.id.et_screen_vital_vision_left);
		et_screen_vital_vision_right = (EditText) view
				.findViewById(R.id.et_screen_vital_vision_right);
		btn_screen_vital_vision_save = (Button) view
				.findViewById(R.id.btn_screen_vital_vision_save);
		btn_screen_vital_vision_save.setOnClickListener(this);
		btn_screen_vital_vision_cancel = (Button) view
				.findViewById(R.id.btn_screen_vital_vision_cancel);
		btn_screen_vital_vision_cancel.setOnClickListener(this);
		sb_screen_vital_vision_right = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_vision_right);
		sb_screen_vital_vision_left = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_vision_left);
		iv_screen_vital_vision_left_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vital_vision_left_plus);

		iv_screen_vital_vision_left_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vital_vision_left_minus);

		iv_screen_vital_vision_right_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vital_vision_right_plus);

		iv_screen_vital_vision_right_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vital_vision_right_minus);
		settingValues();
	}

	// *** Setting values to find Views
	private void settingValues() {
		sb_screen_vital_vision_left.setProgress(NumUtil.IntegerParse
				.parseInt(ScreeningVitalsFragment.vitalsObj
						.getAcutyVisionLeft()));
		et_screen_vital_vision_left.setText(""
				+ ScreeningVitalsFragment.vitalsObj.getAcutyVisionLeft());
		sb_screen_vital_vision_left
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					int progress = 0;

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progresValue, boolean fromUser) {
						progress = progresValue;
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						et_screen_vital_vision_left.setText("".trim()
								+ progress);

					}
				});
		iv_screen_vital_vision_left_plus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vital_vision_left
								.getText().toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int < 6) {
							et_screen_vital_vision_left.setText("".trim()
									+ (cm_int + 1));
							int sb_cm = sb_screen_vital_vision_left
									.getProgress();
							sb_screen_vital_vision_left.setProgress(sb_cm + 1);
						} else {

						}
					}
				});
		iv_screen_vital_vision_left_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vital_vision_left
								.getText().toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vital_vision_left.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_vision_left
									.getProgress();
							sb_screen_vital_vision_left.setProgress(sb_cm - 1);
						} else {
						}
					}
				});

		sb_screen_vital_vision_right.setProgress(NumUtil.IntegerParse
				.parseInt(ScreeningVitalsFragment.vitalsObj
						.getAcutyVisionRight()));
		et_screen_vital_vision_right.setText(""
				+ ScreeningVitalsFragment.vitalsObj.getAcutyVisionRight());
		sb_screen_vital_vision_right
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					int progress = 0;

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progresValue, boolean fromUser) {
						progress = progresValue;
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						et_screen_vital_vision_right.setText("" + progress);

					}
				});
		iv_screen_vital_vision_right_plus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String mm_string = et_screen_vital_vision_right
								.getText().toString();
						int mm_int = NumUtil.IntegerParse.parseInt(mm_string);
						if (mm_int < 6) {
							et_screen_vital_vision_right.setText("".trim()
									+ (mm_int + 1));
							int sb_cm = sb_screen_vital_vision_right
									.getProgress();
							sb_screen_vital_vision_right.setProgress(sb_cm + 1);
						} else {

						}
					}
				});
		iv_screen_vital_vision_right_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vital_vision_right
								.getText().toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vital_vision_right.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_vision_right
									.getProgress();
							sb_screen_vital_vision_right.setProgress(sb_cm - 1);
						} else {
						}
					}
				});
	}

	// *** Method for On Click Event for Views
	@Override
	public void onClick(View v) {
		if (v == btn_screen_vital_vision_cancel) {
			dismiss();
		} else if (v == btn_screen_vital_vision_save) {
			String eye_value = et_screen_vital_vision_left.getText().toString()
					+ "," + et_screen_vital_vision_right.getText().toString();
			ScreeningVitalsFragment.setToTextView(eye_value, "eye",
					this.getActivity());
		}
	}

}
