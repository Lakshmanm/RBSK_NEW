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
import android.app.DialogFragment;
import android.database.Cursor;
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

import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  ScreeningVitalsWeight.java

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
//3.0			30-05-2015			Kiruthika			Remove unused imports
//3.0			30-05-2015			Kiruthika			Methods comments missing
//*****************************************************************************
public class ScreeningVitalsWeight extends DialogFragment implements
		OnClickListener {
	private ImageView iv_screen_vitals_weight_kg_plus;
	private ImageView iv_screen_vitals_weight_kg_minus;
	private ImageView iv_screen_vitals_weight_gm_plus;
	private ImageView iv_screen_vitals_weight_gm_minus;
	private SeekBar sb_screen_vital_weight_kg;
	private SeekBar sb_screen_vital_weight_gm;
	private EditText et_screen_vitals_weight_kg;
	private EditText et_screen_vitals_weight_gm;
	private Button btn_screen_vitals_weight_save;
	private Button btn_screen_vitals_weight_cancel;
	private int age;
	private float weightDB;
	static Cursor cur;
	private int weight_gm;
	private int weight_kg;
	private String type_ofinstitute;
	private int genderId;
	private int instType;
	private static DBHelper dbh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		View view = inflater.inflate(R.layout.screening_vitals_weight,
				container);
		dbh = DBHelper.getInstance(this.getActivity());
		age = Helper.childrenObject.getAge();
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if (instType == 1) {
			type_ofinstitute = "awc";
		} else {
			type_ofinstitute = "school";
		}
		genderId = Helper.childrenObject.getGender().getGenderID();
		weightDB = ScreeningVitalsFragment.vitalsObj.getWeight();
		weight_gm = (int) (((float) Math.round(((float) (weightDB - Math
				.floor(weightDB))) * 100) / 100) * 10);
		weight_kg = (int) (weightDB - (weight_gm / 10));
		findViews(view);
		return view;
	}

	/**
	 * To get weights from DB
	 */
	@SuppressWarnings("unused")
	private float getWeightDb(int age2) {
		float height = 0;
		String weightQuery;
		if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
			weightQuery = "Select Median from 0to5yearsweightchart where IsDeleted!=1 and   AgeinMonths ="
					+ (age * 12) + " and GenderID=" + genderId;
		} else {
			weightQuery = "Select MedianWeightInKGs from 6to18yearsweightchart IsDeleted!=1 and   where AgeinYears ="
					+ age + " and GenderID=" + genderId;
		}
		cur = dbh.getCursorData(getActivity(), weightQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {

					do {
						if (StringUtils.equalsNoCase(type_ofinstitute, "awc")) {
							height = cur.getFloat(cur.getColumnIndex("Median"));
						} else {
							height = cur.getFloat(cur
									.getColumnIndex("MedianWeightInKGs"));
						}
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}
		return height;
	}

	/**
	 * to find views from R.java
	 * 
	 * @param view
	 */
	private void findViews(View view) {
		iv_screen_vitals_weight_kg_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_weight_kg_plus);
		iv_screen_vitals_weight_kg_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_weight_kg_minus);
		iv_screen_vitals_weight_gm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_weight_gm_plus);
		iv_screen_vitals_weight_gm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_weight_gm_minus);
		sb_screen_vital_weight_kg = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_weight_kg);
		sb_screen_vital_weight_gm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_weight_gm);
		et_screen_vitals_weight_kg = (EditText) view
				.findViewById(R.id.et_screen_vitals_weight_kg);
		et_screen_vitals_weight_gm = (EditText) view
				.findViewById(R.id.et_screen_vitals_weight_gm);
		btn_screen_vitals_weight_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_weight_save);
		btn_screen_vitals_weight_save.setOnClickListener(this);
		btn_screen_vitals_weight_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_weight_cancel);
		btn_screen_vitals_weight_cancel.setOnClickListener(this);

		settingValues();
	}

	/**
	 * to set values to sliders
	 */
	private void settingValues() {
		sb_screen_vital_weight_kg.setProgress(weight_kg);
		et_screen_vitals_weight_kg.setText("" + weight_kg);
		sb_screen_vital_weight_kg
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
						et_screen_vitals_weight_kg.setText("".trim() + progress);

					}
				});
		iv_screen_vitals_weight_kg_plus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_weight_kg.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int < 200) {
							et_screen_vitals_weight_kg.setText("".trim()
									+ (cm_int + 1));
							int sb_cm = sb_screen_vital_weight_kg.getProgress();
							sb_screen_vital_weight_kg.setProgress(sb_cm + 1);
						} else {

						}
					}
				});
		iv_screen_vitals_weight_kg_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_weight_kg.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vitals_weight_kg.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_weight_kg.getProgress();
							sb_screen_vital_weight_kg.setProgress(sb_cm - 1);
						} else {
						}
					}
				});

		sb_screen_vital_weight_gm.setProgress(weight_gm);
		et_screen_vitals_weight_gm.setText("." + weight_gm);
		sb_screen_vital_weight_gm
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
						et_screen_vitals_weight_gm.setText("." + progress);

					}
				});
		iv_screen_vitals_weight_gm_plus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						String mm_string = et_screen_vitals_weight_gm.getText()
								.toString().trim();

						float mm_int = Float.parseFloat(mm_string);
						if (mm_int < .9) {
							if ((mm_int + .1) < .9999) {
								et_screen_vitals_weight_gm.setText("".trim()
										+ String.format("%.1f", (mm_int + .1)));
							} else {
								et_screen_vitals_weight_gm.setText(".".trim() + 9);
							}
							int sb_mm = sb_screen_vital_weight_gm.getProgress();
							sb_screen_vital_weight_gm.setProgress(sb_mm + 1);
						}
					}
				});
		iv_screen_vitals_weight_gm_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String mm_string = et_screen_vitals_weight_gm.getText()
								.toString();
						float mm_int = Float.parseFloat(mm_string);
						if (mm_int > .0) {
							et_screen_vitals_weight_gm.setText(""
									+ String.format("%.1f", (mm_int - .1)));
							int sb_mm = sb_screen_vital_weight_gm.getProgress();
							sb_screen_vital_weight_gm.setProgress(sb_mm - 1);
						}
					}
				});
		btn_screen_vitals_weight_cancel.setOnClickListener(this);
		btn_screen_vitals_weight_save.setOnClickListener(this);
	}

	/**
	 * clicl event for views
	 */
	@Override
	public void onClick(View v) {
		if (v == btn_screen_vitals_weight_cancel) {
			dismiss();
		} else if (v == btn_screen_vitals_weight_save) {
			float weight_value = Float.parseFloat(et_screen_vitals_weight_kg
					.getText().toString())
					+ Float.parseFloat(et_screen_vitals_weight_gm.getText()
							.toString());
			ScreeningVitalsFragment.setToTextView("" + weight_value, "weight",
					this.getActivity());
		}
	}

}
