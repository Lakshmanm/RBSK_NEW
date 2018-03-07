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
import android.text.TextUtils;
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
//* Name   :  ScreenVitalHemoglobin.java

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
public class ScreenVitalHemoglobin extends DialogFragment implements
		OnClickListener {
	private ImageView iv_screen_vitals_hemo_nd_plus;
	private ImageView iv_screen_vitals_hemo_nd_minus;
	private ImageView iv_screen_vitals_hemo_d_plus;
	private ImageView iv_screen_vitals_hemo_d_minus;
	private SeekBar sb_screen_vital_hemo_nd;
	private SeekBar sb_screen_vital_hemo_d;
	private EditText et_screen_vitals_hemo_nd;
	private EditText et_screen_vitals_hemo_d;
	private Button btn_screen_vitals_hemo_save;
	private Button btn_screen_vitals_hemo_cancel;
	private int age;
	public float hemoDB;
	public static Cursor cur;
	public String type_ofinstitute;
	public int genderId;
	private int instType;
	/*
	 * private int hemo_d; private int hemo_nd;
	 */
	private DBHelper dbh;
	private float hemoObj;
	private int hemo_gm;
	private int hemo_kg;

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
		View view = inflater.inflate(R.layout.screening_vital_hemoglobin,
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
		String hemoglobinIndication = ScreeningVitalsFragment.vitalsObj
				.getHemoglobinIndication();
		if (StringUtils.equalsNoCase(hemoglobinIndication, "enter")) {
			hemoObj = (float) 14.60;
		} else {
			String HemoglobinIndication = hemoglobinIndication;
			if (!TextUtils.isEmpty(HemoglobinIndication))
				hemoObj = Float.parseFloat(HemoglobinIndication);
		}
		hemo_gm = (int) ((hemoObj - Math.floor(hemoObj)) * 10);
		hemo_kg = (int) Math.floor(hemoObj);
		findViews(view);
		return view;

	}

	/**
	 * @param age2
	 * @return
	 */
	// *** Pass age to get Hemoglobin Median value
	@SuppressWarnings("unused")
	private float gethemoDb(int age2) {
		float hemo = 0;
		String hemoQuery = "Select Median from hemocircumference0to5yrs where IsDeleted!=1 and   AgeinMonths ="
				+ age;
		cur = dbh.getCursorData(getActivity(), hemoQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {

					do {

						hemo = cur.getFloat(cur.getColumnIndex("Median"));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}
		return hemo;
	}

	/**
	 * @param view
	 * 
	 */
	private void findViews(View view) {
		iv_screen_vitals_hemo_nd_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_hemo_nd_plus);
		iv_screen_vitals_hemo_nd_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_hemo_nd_minus);
		iv_screen_vitals_hemo_d_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_hemo_d_plus);
		iv_screen_vitals_hemo_d_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_hemo_d_minus);
		sb_screen_vital_hemo_nd = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_hemo_nd);
		sb_screen_vital_hemo_d = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_hemo_d);
		et_screen_vitals_hemo_nd = (EditText) view
				.findViewById(R.id.et_screen_vitals_hemo_nd);
		et_screen_vitals_hemo_d = (EditText) view
				.findViewById(R.id.et_screen_vitals_hemo_d);
		btn_screen_vitals_hemo_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_hemo_save);
		btn_screen_vitals_hemo_save.setOnClickListener(this);
		btn_screen_vitals_hemo_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_hemo_cancel);
		btn_screen_vitals_hemo_cancel.setOnClickListener(this);

		settingValues();
	}

	/**
	 * 
	 */
	// *** Setting values for FindViews
	private void settingValues() {
		sb_screen_vital_hemo_nd.setProgress((int) hemo_kg);
		// et_screen_vitals_hemo_nd.setText("" + hemo_kg);
		et_screen_vitals_hemo_nd.setText("" + hemo_kg);
		sb_screen_vital_hemo_nd
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
						et_screen_vitals_hemo_nd.setText("".trim() + progress);

					}
				});
		iv_screen_vitals_hemo_nd_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cm_string = et_screen_vitals_hemo_nd.getText()
						.toString();
				int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
				if (cm_int < 200) {
					et_screen_vitals_hemo_nd.setText("".trim() + (cm_int + 1));
					int sb_cm = sb_screen_vital_hemo_nd.getProgress();
					sb_screen_vital_hemo_nd.setProgress(sb_cm + 1);
				} else {

				}
			}
		});
		iv_screen_vitals_hemo_nd_minus
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cm_string = et_screen_vitals_hemo_nd.getText()
								.toString();
						int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
						if (cm_int > 0) {
							et_screen_vitals_hemo_nd.setText("".trim()
									+ (cm_int - 1));
							int sb_cm = sb_screen_vital_hemo_nd.getProgress();
							sb_screen_vital_hemo_nd.setProgress(sb_cm - 1);
						} else {
						}
					}
				});

		sb_screen_vital_hemo_d.setProgress((int) hemo_gm);
		et_screen_vitals_hemo_d.setText("0." + hemo_gm);
		sb_screen_vital_hemo_d
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
						et_screen_vitals_hemo_d.setText("0." + progress);

					}
				});
		iv_screen_vitals_hemo_d_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String mm_string = et_screen_vitals_hemo_d.getText().toString()
						.trim();
				float mm_int = 0f;
				if (!TextUtils.isEmpty(mm_string))
					mm_int = Float.parseFloat(mm_string);
				if (mm_int < 0.9) {
					if ((mm_int + 0.1) < 0.9999) {
						et_screen_vitals_hemo_d.setText("".trim()
								+ String.format("%.1f", (mm_int + 0.1)));
					} else {
						et_screen_vitals_hemo_d.setText("0.".trim() + 9);
					}
					int sb_mm = sb_screen_vital_hemo_d.getProgress();
					sb_screen_vital_hemo_d.setProgress(sb_mm + 1);
				}
			}
		});
		iv_screen_vitals_hemo_d_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mm_string = et_screen_vitals_hemo_d.getText().toString();
				float mm_int = 0f;
				if (!TextUtils.isEmpty(mm_string))
					mm_int = Float.parseFloat(mm_string);

				if (mm_int > 0.0) {
					et_screen_vitals_hemo_d.setText(""
							+ String.format("%.1f", (mm_int - 0.1)));
					int sb_mm = sb_screen_vital_hemo_d.getProgress();
					sb_screen_vital_hemo_d.setProgress(sb_mm - 1);
				}
			}
		});
		btn_screen_vitals_hemo_cancel.setOnClickListener(this);
		btn_screen_vitals_hemo_save.setOnClickListener(this);
	}

	// *** Method for On click events for views
	@Override
	public void onClick(View v) {
		if (v == btn_screen_vitals_hemo_cancel) {
			dismiss();
		} else if (v == btn_screen_vitals_hemo_save) {
			String string = et_screen_vitals_hemo_nd.getText().toString();

			float hemo_value = Float.parseFloat(string)
					+ Float.parseFloat(et_screen_vitals_hemo_d.getText()
							.toString());
			ScreeningVitalsFragment.setToTextView("" + hemo_value, "hemo",
					this.getActivity());
		}
	}

}
