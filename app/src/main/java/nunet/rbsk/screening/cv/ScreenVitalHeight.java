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
//* Name   :  ScreenVitalHeight.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  May 27, 2015
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
public class ScreenVitalHeight extends DialogFragment implements
		OnClickListener {
	private ImageView iv_screen_vitals_height_cm_plus;
	private ImageView iv_screen_vitals_height_cm_minus;
	private ImageView iv_screen_vitals_height_mm_plus;
	private ImageView iv_screen_vitals_height_mm_minus;
	private SeekBar sb_screen_vital_height_cm;
	private SeekBar sb_screen_vital_height_mm;
	private EditText et_screen_vitals_height_cm;
	private EditText et_screen_vitals_height_mm;
	private Button btn_screen_vitals_height_save;
	private Button btn_screen_vitals_height_cancel;
	private int age;
	private float heightDB;
	static Cursor cur;
	private int height_mm;
	private int height_cm;
	private String type_ofinstitute;
	private int genderId;
	private int instType;
	private DBHelper dbh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		dbh = DBHelper.getInstance(this.getActivity());
		View view = inflater.inflate(R.layout.screening_vitals_height,
				container);

		age = Helper.childrenObject.getAge();
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if (instType == 1) {
			type_ofinstitute = "awc";
		} else {
			type_ofinstitute = "school";
		}
		genderId = Helper.childrenObject.getGender().getGenderID();


		heightDB = ScreeningVitalsFragment.vitalsObj.getHeight();
		height_mm = (int) (((float) Math.round(((float) (heightDB - Math
				.floor(heightDB))) * 100) / 100) * 10);
		height_cm = (int) (heightDB - (height_mm / 10));
		findViews(view);
		return view;
	}

	/**
	 * To get heights from database
	 */
	@SuppressWarnings("unused")
	private float getHeightDb(int age2) {
		float height = 0;
		String heightQuery;
		if (StringUtils.equalsNoCase(type_ofinstitute,"awc")) {
			heightQuery = "Select Median from heightchart0to5years where IsDeleted!=1 and   AgeinMonths ='"
					+ (age * 12) + "' and GenderId='" + genderId + "'";
		} else {
			heightQuery = "Select MedianHeightInCms from heightchart6to18years where IsDeleted!=1 and   AgeinYears ="
					+ age + " and GenderId=" + genderId + "";
		}
		cur = dbh.getCursorData(getActivity(), heightQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {

					do {
						if (StringUtils.equalsNoCase(type_ofinstitute,"awc")) {
							height = cur.getFloat(cur.getColumnIndex("Median"));
						} else {
							height = cur.getFloat(cur
									.getColumnIndex("MedianHeightInCms"));
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
	 * find views from R.java
	 */
	private void findViews(View view) {
		iv_screen_vitals_height_cm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_height_cm_plus);
		iv_screen_vitals_height_cm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_height_cm_minus);
		iv_screen_vitals_height_mm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_height_mm_plus);
		iv_screen_vitals_height_mm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_height_mm_minus);
		sb_screen_vital_height_cm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_height_cm);
		sb_screen_vital_height_mm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_height_mm);
		et_screen_vitals_height_cm = (EditText) view
				.findViewById(R.id.et_screen_vitals_height_cm);
		et_screen_vitals_height_mm = (EditText) view
				.findViewById(R.id.et_screen_vitals_height_mm);
		btn_screen_vitals_height_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_height_save);
		btn_screen_vitals_height_save.setOnClickListener(this);
		btn_screen_vitals_height_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_height_cancel);
		btn_screen_vitals_height_cancel.setOnClickListener(this);

		settingValues();
	}

	/**
	 * set values to sliders
	 */
	private void settingValues() {
		sb_screen_vital_height_cm.setProgress(height_cm);
		et_screen_vitals_height_cm.setText("" + height_cm);
		sb_screen_vital_height_cm
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
						et_screen_vitals_height_cm.setText("".trim() + progress);

					}
				});
		iv_screen_vitals_height_cm_plus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_height_cm.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int < 200) {
							et_screen_vitals_height_cm.setText("".trim()
									+ (cm_int + 1));
							int sb_cm = sb_screen_vital_height_cm.getProgress();
							sb_screen_vital_height_cm.setProgress(sb_cm + 1);
						} else {

						}
					}
				});
		iv_screen_vitals_height_cm_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_height_cm.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vitals_height_cm.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_height_cm.getProgress();
							sb_screen_vital_height_cm.setProgress(sb_cm - 1);
						} else {
						}
					}
				});

		sb_screen_vital_height_mm.setProgress(height_mm);
		et_screen_vitals_height_mm.setText("." + height_mm);
		sb_screen_vital_height_mm
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
						et_screen_vitals_height_mm.setText("." + progress);

					}
				});
		iv_screen_vitals_height_mm_plus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						String mm_string = et_screen_vitals_height_mm.getText()
								.toString().trim();

						float mm_int = Float.parseFloat(mm_string);
						if (mm_int < .9) {
							if ((mm_int + .1) < .9999) {
								et_screen_vitals_height_mm.setText("".trim()
										+ String.format("%.1f", (mm_int + .1)));
							} else {
								et_screen_vitals_height_mm.setText(".".trim() + 9);
							}
							int sb_mm = sb_screen_vital_height_mm.getProgress();
							sb_screen_vital_height_mm.setProgress(sb_mm + 1);
						}
					}
				});
		iv_screen_vitals_height_mm_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String mm_string = et_screen_vitals_height_mm.getText()
								.toString();
						float mm_int = Float.parseFloat(mm_string);
						if (mm_int > .0) {
							et_screen_vitals_height_mm.setText(""
									+ String.format("%.1f", (mm_int - .1)));
							int sb_mm = sb_screen_vital_height_mm.getProgress();
							sb_screen_vital_height_mm.setProgress(sb_mm - 1);
						}
					}
				});
		btn_screen_vitals_height_cancel.setOnClickListener(this);
		btn_screen_vitals_height_save.setOnClickListener(this);
	}

	/**
	 * click event for views
	 */
	@Override
	public void onClick(View v) {
				if (v == btn_screen_vitals_height_cancel) {
			dismiss();
		} else if (v == btn_screen_vitals_height_save) {
			float height_value = Float.parseFloat(et_screen_vitals_height_cm
					.getText().toString())
					+ Float.parseFloat(et_screen_vitals_height_mm.getText()
							.toString());
			ScreeningVitalsFragment.setToTextView("" + height_value, "height",
					this.getActivity());
		}
	}

}
