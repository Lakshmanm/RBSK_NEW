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
//* Name   :  ScreenVitalTemp.java

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
public class ScreenVitalTemp extends DialogFragment implements OnClickListener {
	private ImageView iv_screen_vitals_temp_nd_plus;
	private ImageView iv_screen_vitals_temp_nd_minus;
	private ImageView iv_screen_vitals_temp_d_plus;
	private ImageView iv_screen_vitals_temp_d_minus;
	private SeekBar sb_screen_vital_temp_nd;
	private SeekBar sb_screen_vital_temp_d;
	private EditText et_screen_vitals_temp_nd;
	private EditText et_screen_vitals_temp_d;
	private Button btn_screen_vitals_temp_save;
	private Button btn_screen_vitals_temp_cancel;
	private int age;
	public float tempDB;
	public static Cursor cur;
	public String type_ofinstitute;
	public int genderId;
	private int instType;
	private DBHelper dbh;
	private float temp;
	private int temp_kg;
	private int temp_gm;

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
		View view = inflater.inflate(R.layout.screening_vitals_temp, container);
		age = Helper.childrenObject.getAge();
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if (instType == 1) {
			type_ofinstitute = "awc";
		} else {
			type_ofinstitute = "school";
		}
		genderId = Helper.childrenObject.getGender().getGenderID();
		if (StringUtils.equalsNoCase(
				ScreeningVitalsFragment.vitalsObj.getTemperatureIndication(),
				"enter")) {
			temp = (float) 98.6;
		} else {
			temp = Float.parseFloat(ScreeningVitalsFragment.vitalsObj
					.getTemperatureIndication());
		}
		temp_gm = (int) (((float) Math
				.round(((float) (temp - Math.floor(temp))) * 100) / 100) * 10);
		temp_kg = (int) (temp - (temp_gm / 10));
		findViews(view);
		return view;

	}

	/**
	 * @param age2
	 * @return
	 */
	@SuppressWarnings("unused")
	private float gettempDb(int age2) {
		float temp = 0;
		String tempQuery = "Select Median from tempcircumference0to5yrs where IsDeleted!=1 and   AgeinMonths ="
				+ age;
		cur = dbh.getCursorData(getActivity(), tempQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {

					do {

						temp = cur.getFloat(cur.getColumnIndex("Median"));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}
		return temp;
	}

	/**
	 * @param view
	 * 
	 */
	private void findViews(View view) {
		iv_screen_vitals_temp_nd_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_temp_nd_plus);
		iv_screen_vitals_temp_nd_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_temp_nd_minus);
		iv_screen_vitals_temp_d_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_temp_d_plus);
		iv_screen_vitals_temp_d_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_temp_d_minus);
		sb_screen_vital_temp_nd = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_temp_nd);
		sb_screen_vital_temp_d = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_temp_d);
		et_screen_vitals_temp_nd = (EditText) view
				.findViewById(R.id.et_screen_vitals_temp_nd);
		et_screen_vitals_temp_d = (EditText) view
				.findViewById(R.id.et_screen_vitals_temp_d);
		btn_screen_vitals_temp_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_temp_save);
		btn_screen_vitals_temp_save.setOnClickListener(this);
		btn_screen_vitals_temp_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_temp_cancel);
		btn_screen_vitals_temp_cancel.setOnClickListener(this);

		settingValues();
	}

	/**
	 * 
	 */
	private void settingValues() {
		sb_screen_vital_temp_nd.setProgress(temp_kg);
		et_screen_vitals_temp_nd.setText("" + temp_kg);
		sb_screen_vital_temp_nd
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
						et_screen_vitals_temp_nd.setText("".trim() + progress);

					}
				});
		iv_screen_vitals_temp_nd_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cm_string = et_screen_vitals_temp_nd.getText()
						.toString();
				int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
				if (cm_int < 200) {
					et_screen_vitals_temp_nd.setText("".trim() + (cm_int + 1));
					int sb_cm = sb_screen_vital_temp_nd.getProgress();
					sb_screen_vital_temp_nd.setProgress(sb_cm + 1);
				} else {

				}
			}
		});
		iv_screen_vitals_temp_nd_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_temp_nd.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vitals_temp_nd.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_temp_nd.getProgress();
							sb_screen_vital_temp_nd.setProgress(sb_cm - 1);
						} else {
						}
					}
				});

		sb_screen_vital_temp_d.setProgress(temp_gm);
		et_screen_vitals_temp_d.setText("0." + temp_gm);
		sb_screen_vital_temp_d
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
						et_screen_vitals_temp_d.setText("0." + progress);

					}
				});
		iv_screen_vitals_temp_d_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String mm_string = et_screen_vitals_temp_d.getText().toString()
						.trim();

				float mm_int = Float.parseFloat(mm_string);
				if (mm_int < 0.9) {
					if ((mm_int + 0.1) < 0.9999) {
						et_screen_vitals_temp_d.setText("".trim()
								+ String.format("%.1f", (mm_int + 0.1)));
					} else {
						et_screen_vitals_temp_d.setText("0.".trim() + 9);
					}
					int sb_mm = sb_screen_vital_temp_d.getProgress();
					sb_screen_vital_temp_d.setProgress(sb_mm + 1);
				}
			}
		});
		iv_screen_vitals_temp_d_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mm_string = et_screen_vitals_temp_d.getText().toString();
				float mm_int = Float.parseFloat(mm_string);
				if (mm_int > 0.0) {
					et_screen_vitals_temp_d.setText(""
							+ String.format("%.1f", (mm_int - 0.1)));
					int sb_mm = sb_screen_vital_temp_d.getProgress();
					sb_screen_vital_temp_d.setProgress(sb_mm - 1);
				}
			}
		});
		btn_screen_vitals_temp_cancel.setOnClickListener(this);
		btn_screen_vitals_temp_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_screen_vitals_temp_cancel) {
			dismiss();
		} else if (v == btn_screen_vitals_temp_save) {
			float temp_value = Float.parseFloat(et_screen_vitals_temp_nd
					.getText().toString())
					+ Float.parseFloat(et_screen_vitals_temp_d.getText()
							.toString());
			ScreeningVitalsFragment.setToTextView("" + temp_value, "temp",
					this.getActivity());
		}
	}

}
