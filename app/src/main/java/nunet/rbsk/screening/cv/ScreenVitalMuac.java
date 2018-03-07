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

//*****************************************************************************
//* Name   :  ScreenVitalMuac.java

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

//*****************************************************************************
public class ScreenVitalMuac extends DialogFragment implements OnClickListener {
	private ImageView iv_screen_vitals_muac_cm_plus;
	private ImageView iv_screen_vitals_muac_cm_minus;
	private ImageView iv_screen_vitals_muac_mm_plus;
	private ImageView iv_screen_vitals_muac_mm_minus;
	private SeekBar sb_screen_vital_muac_cm;
	private SeekBar sb_screen_vital_muac_mm;
	private EditText et_screen_vitals_muac_cm;
	private EditText et_screen_vitals_muac_mm;
	private Button btn_screen_vitals_muac_save;
	private Button btn_screen_vitals_muac_cancel;
	private int age;
	private float muacDB;
	static Cursor cur;
	private int muac_mm;
	private int muac_cm;
	public String type_ofinstitute;
	private int genderId;
	private int instType;
	private DBHelper dbh;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		dbh=DBHelper.getInstance(this.getActivity());
		View view = inflater.inflate(R.layout.screening_vitals_muac, container);
		age = Helper.childrenObject.getAge();
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if(instType==1){type_ofinstitute="awc";}
		else{type_ofinstitute="school";}
		genderId = Helper.childrenObject.getGender().getGenderID();
		muacDB = ScreeningVitalsFragment.vitalsObj.getMuacCm();
		muac_mm = (int) (((float) Math.round(((float) (muacDB - Math
				.floor(muacDB))) * 100) / 100) * 10);
		muac_cm = (int) (muacDB - (muac_mm / 10));
		findViews(view);
		return view;

	}

	/**
	 * @param age2
	 * @return
	 */
	@SuppressWarnings("unused")
	private float getMuacDb(int age2) {
		float muac = 0;
		String muacQuery = "Select Median from muacreferences where IsDeleted!=1 and   AgeinMonths ="
				+ (age * 12) + " and GenderID=" + genderId;
		cur = dbh.getCursorData(getActivity(), muacQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {

					do {

						muac = cur.getFloat(cur.getColumnIndex("Median"));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}
		return muac;
	}

	/**
	 * @param view
	 * 
	 */
	private void findViews(View view) {
		iv_screen_vitals_muac_cm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_muac_cm_plus);
		iv_screen_vitals_muac_cm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_muac_cm_minus);
		iv_screen_vitals_muac_mm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_muac_mm_plus);
		iv_screen_vitals_muac_mm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_muac_mm_minus);
		sb_screen_vital_muac_cm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_muac_cm);
		sb_screen_vital_muac_mm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_muac_mm);
		et_screen_vitals_muac_cm = (EditText) view
				.findViewById(R.id.et_screen_vitals_muac_cm);
		et_screen_vitals_muac_mm = (EditText) view
				.findViewById(R.id.et_screen_vitals_muac_mm);
		btn_screen_vitals_muac_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_muac_save);
		btn_screen_vitals_muac_save.setOnClickListener(this);
		btn_screen_vitals_muac_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_muac_cancel);
		btn_screen_vitals_muac_cancel.setOnClickListener(this);

		settingValues();
	}

	/**
	 * 
	 */
	private void settingValues() {
		sb_screen_vital_muac_cm.setProgress(muac_cm);
		et_screen_vitals_muac_cm.setText("" + muac_cm);
		sb_screen_vital_muac_cm
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
						et_screen_vitals_muac_cm.setText("".trim() + progress);

					}
				});
		iv_screen_vitals_muac_cm_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cm_string = et_screen_vitals_muac_cm.getText()
						.toString();
				int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
				if (cm_int < 200) {
					et_screen_vitals_muac_cm.setText("".trim() + (cm_int + 1));
					int sb_cm = sb_screen_vital_muac_cm.getProgress();
					sb_screen_vital_muac_cm.setProgress(sb_cm + 1);
				} else {

				}
			}
		});
		iv_screen_vitals_muac_cm_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_muac_cm.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vitals_muac_cm.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_muac_cm.getProgress();
							sb_screen_vital_muac_cm.setProgress(sb_cm - 1);
						} else {
						}
					}
				});

		sb_screen_vital_muac_mm.setProgress(muac_mm);
		et_screen_vitals_muac_mm.setText("0." + muac_mm);
		sb_screen_vital_muac_mm
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
						et_screen_vitals_muac_mm.setText("0." + progress);

					}
				});
		iv_screen_vitals_muac_mm_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String mm_string = et_screen_vitals_muac_mm.getText()
						.toString().trim();

				float mm_int = Float.parseFloat(mm_string);
				if (mm_int < 0.9) {
					if ((mm_int + 0.1) < 0.9999) {
						et_screen_vitals_muac_mm.setText("".trim()
								+ String.format("%.1f", (mm_int + 0.1)));
					} else {
						et_screen_vitals_muac_mm.setText("0.".trim() + 9);
					}
					int sb_mm = sb_screen_vital_muac_mm.getProgress();
					sb_screen_vital_muac_mm.setProgress(sb_mm + 1);
				}
			}
		});
		iv_screen_vitals_muac_mm_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String mm_string = et_screen_vitals_muac_mm.getText()
								.toString();
						float mm_int = Float.parseFloat(mm_string);
						if (mm_int > 0.0) {
							et_screen_vitals_muac_mm.setText(""
									+ String.format("%.1f", (mm_int - 0.1)));
							int sb_mm = sb_screen_vital_muac_mm.getProgress();
							sb_screen_vital_muac_mm.setProgress(sb_mm - 1);
						}
					}
				});
		btn_screen_vitals_muac_cancel.setOnClickListener(this);
		btn_screen_vitals_muac_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_screen_vitals_muac_cancel) {
			dismiss();
		} else if (v == btn_screen_vitals_muac_save) {
			float muac_value = Float.parseFloat(et_screen_vitals_muac_cm
					.getText().toString())
					+ Float.parseFloat(et_screen_vitals_muac_mm.getText()
							.toString());
			ScreeningVitalsFragment.setToTextView("".trim() + muac_value,
					"muac", this.getActivity());
		}
	}

}
