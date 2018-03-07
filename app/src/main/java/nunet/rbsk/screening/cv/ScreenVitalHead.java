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
//* Name   :  ScreenVitalsHead.java

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
//3.0			08/06/2015			Anil Kumar Botsa		No Comments

//*****************************************************************************
public class ScreenVitalHead extends DialogFragment implements OnClickListener {
	private ImageView iv_screen_vitals_head_cm_plus;
	private ImageView iv_screen_vitals_head_cm_minus;
	private ImageView iv_screen_vitals_head_mm_plus;
	private ImageView iv_screen_vitals_head_mm_minus;
	private SeekBar sb_screen_vital_head_cm;
	private SeekBar sb_screen_vital_head_mm;
	private EditText et_screen_vitals_head_cm;
	private EditText et_screen_vitals_head_mm;
	private Button btn_screen_vitals_head_save;
	private Button btn_screen_vitals_head_cancel;
	private int age;
	private float headDB;
	static Cursor cur;
	private int head_mm;
	private int head_cm;
	public String type_ofinstitute;
	public int genderId;
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
		dbh = DBHelper.getInstance(this.getActivity());
		View view = inflater.inflate(R.layout.screening_vitals_head, container);
		age = Helper.childrenObject.getAge();
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if (instType == 1) {
			type_ofinstitute = "awc";
		} else {
			type_ofinstitute = "school";
		}
		genderId = Helper.childrenObject.getGender().getGenderID();
		headDB = ScreeningVitalsFragment.vitalsObj.getHeadCircumferenceCm();

		head_mm = (int) (((float) Math.round(((float) (headDB - Math
				.floor(headDB))) * 100) / 100) * 10);
		head_cm = (int) (headDB - (head_mm / 10));
		findViews(view);
		return view;

	}

	/**
	 * @param age2
	 * @return
	 */
	@SuppressWarnings("unused")
	private float getheadDb(int age2, int gender) {
		float head = 0;
		String headQuery = "Select Median from headcircumference0to5yrs where IsDeleted!=1 and   AgeinMonths ="
				+ (age * 12) + " and GenderID =" + gender;
		cur = dbh.getCursorData(getActivity(), headQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {

					do {

						head = cur.getFloat(cur.getColumnIndex("Median"));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}
		return head;
	}

	/**
	 * @param view
	 * 
	 */
	private void findViews(View view) {
		iv_screen_vitals_head_cm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_head_cm_plus);
		iv_screen_vitals_head_cm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_head_cm_minus);
		iv_screen_vitals_head_mm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_head_mm_plus);
		iv_screen_vitals_head_mm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_head_mm_minus);
		sb_screen_vital_head_cm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_head_cm);
		sb_screen_vital_head_mm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_head_mm);
		et_screen_vitals_head_cm = (EditText) view
				.findViewById(R.id.et_screen_vitals_head_cm);
		et_screen_vitals_head_mm = (EditText) view
				.findViewById(R.id.et_screen_vitals_head_mm);
		btn_screen_vitals_head_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_head_save);
		btn_screen_vitals_head_save.setOnClickListener(this);
		btn_screen_vitals_head_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_head_cancel);
		btn_screen_vitals_head_cancel.setOnClickListener(this);

		settingValues();
	}

	/**
	 * 
	 */
	// *** Setting values for Find Views
	private void settingValues() {
		sb_screen_vital_head_cm.setProgress(head_cm);
		et_screen_vitals_head_cm.setText("" + head_cm);
		sb_screen_vital_head_cm
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
						et_screen_vitals_head_cm.setText("".trim() + progress);

					}
				});
		iv_screen_vitals_head_cm_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cm_string = et_screen_vitals_head_cm.getText()
						.toString();
				int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
				if (cm_int < 200) {
					et_screen_vitals_head_cm.setText("".trim() + (cm_int + 1));
					int sb_cm = sb_screen_vital_head_cm.getProgress();
					sb_screen_vital_head_cm.setProgress(sb_cm + 1);
				} else {

				}
			}
		});
		iv_screen_vitals_head_cm_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_head_cm.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vitals_head_cm.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_head_cm.getProgress();
							sb_screen_vital_head_cm.setProgress(sb_cm - 1);
						} else {
						}
					}
				});

		sb_screen_vital_head_mm.setProgress(head_mm);
		et_screen_vitals_head_mm.setText("0." + head_mm);
		sb_screen_vital_head_mm
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
						et_screen_vitals_head_mm.setText("0." + progress);

					}
				});
		iv_screen_vitals_head_mm_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String mm_string = et_screen_vitals_head_mm.getText()
						.toString().trim();

				float mm_int = Float.parseFloat(mm_string);
				if (mm_int < 0.9) {
					if ((mm_int + 0.1) < 0.9999) {
						et_screen_vitals_head_mm.setText("".trim()
								+ String.format("%.1f", (mm_int + 0.1)));
					} else {
						et_screen_vitals_head_mm.setText("0.".trim() + 9);
					}
					int sb_mm = sb_screen_vital_head_mm.getProgress();
					sb_screen_vital_head_mm.setProgress(sb_mm + 1);
				}
			}
		});
		iv_screen_vitals_head_mm_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String mm_string = et_screen_vitals_head_mm.getText()
								.toString();
						float mm_int = Float.parseFloat(mm_string);
						if (mm_int > 0.0) {
							et_screen_vitals_head_mm.setText(""
									+ String.format("%.1f", (mm_int - 0.1)));
							int sb_mm = sb_screen_vital_head_mm.getProgress();
							sb_screen_vital_head_mm.setProgress(sb_mm - 1);
						}
					}
				});
		btn_screen_vitals_head_cancel.setOnClickListener(this);
		btn_screen_vitals_head_save.setOnClickListener(this);
	}

	// *** Methods for On Click events to Views
	@Override
	public void onClick(View v) {
		if (v == btn_screen_vitals_head_cancel) {
			dismiss();
		} else if (v == btn_screen_vitals_head_save) {
			float head_value = Float.parseFloat(et_screen_vitals_head_cm
					.getText().toString())
					+ Float.parseFloat(et_screen_vitals_head_mm.getText()
							.toString());
			ScreeningVitalsFragment.setToTextView("" + head_value, "head",
					this.getActivity());
		}
	}

}
