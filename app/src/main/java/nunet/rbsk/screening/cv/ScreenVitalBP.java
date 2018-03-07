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
//* Name   :  ScreenVitalBP.java

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
//3.0			08/06/2015			Anil Kumar Botsa		No Comments
//*****************************************************************************
public class ScreenVitalBP extends DialogFragment implements OnClickListener {
	private ImageView iv_screen_vitals_bp_cm_plus;
	private ImageView iv_screen_vitals_bp_cm_minus;
	private ImageView iv_screen_vitals_bp_mm_plus;
	private ImageView iv_screen_vitals_bp_mm_minus;
	private SeekBar sb_screen_vital_bp_cm;
	private SeekBar sb_screen_vital_bp_mm;
	private EditText et_screen_vitals_bp_cm;
	private EditText et_screen_vitals_bp_mm;
	private Button btn_screen_vitals_bp_save;
	private Button btn_screen_vitals_bp_cancel;
	public static int genderId;
	public String type_ofinstitute;
	public float bpDB;
	static Cursor cur;
	public int height;
	private int instType;
	private DBHelper dbh;
	private int bpDB_gm;
	private int bpDB_kg;

	/*
	 * private float bpDB; static Cursor cur; private int bp_mm; private int
	 * bp_cm;
	 */

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
		View view = inflater.inflate(R.layout.screening_vitals_bp, container);
		height = Math.round(ScreeningVitalsFragment.vitalsObj.getHeight());
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if (instType == 1) {
			type_ofinstitute = "awc";
		} else {
			type_ofinstitute = "school";
		}
		genderId = Helper.childrenObject.getGender().getGenderID();
		if (ScreeningVitalsFragment.vitalsObj.getBp() != null) {
			if (ScreeningVitalsFragment.vitalsObj.getBp().equalsIgnoreCase(
					"null")) {
				bpDB = (float) 82.06;
			} else {
				bpDB = Float.parseFloat(ScreeningVitalsFragment.vitalsObj
						.getBp());
			}
		} else {
			bpDB = (float) 82.06;
		}
		// bpDB_gm = (int) (((float) Math
		// .round(((float) (bpDB - Math.floor(bpDB))) * 100) / 100) * 10);
		// bpDB_kg = (int) (bpDB - (bpDB_gm / 10));

		bpDB_kg = NumUtil.IntegerParse
				.parseInt(ScreeningVitalsFragment.vitalsObj.getBp());
		bpDB_gm = NumUtil.IntegerParse
				.parseInt(ScreeningVitalsFragment.vitalsObj.getBpIndication());

		if (bpDB_kg == 0)
			bpDB_kg = 120;
		if (bpDB_gm == 0)
			bpDB_gm = 80;

		// give age in
		// months//**********************Check
		// This****************************//
		/*
		 * bpDB = getbpDb(age); bp_mm=(int) (((float) Math.round(((float) (bpDB
		 * - Math.floor(bpDB)))* 100) / 100)*10); bp_cm=(int) (bpDB-(bp_mm/10));
		 */
		findViews(view);
		return view;

	}

	/**
	 * @param age2
	 * @param height
	 * @param genderId2
	 * @return
	 */
	// *** Pass age, height and gender to get BP Classification ID
	@SuppressWarnings("unused")
	private int getBPDb(int age2, int heighta, int genderId2) {
		int bp = 0;
		String bpQuery;

		bpQuery = "select  BPClassificationID  from bloodpressurereferences where IsDeleted!=1 and   GenderID = "
				+ genderId2
				+ "  and HeightInCMs = "
				+ heighta
				+ "    and age="
				+ age2 + "  order by SystolicBPInmmHg desc LIMIT 1";

		cur = dbh.getCursorData(getActivity(), bpQuery);
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

	/**
	 * @param age2
	 * @return
	 */
	/*
	 * private float getbpDb(int age2) { float bp = 0; String bpQuery =
	 * "Select Median from bpcircumference0to5yrs where AgeinMonths =" + age;
	 * cur = DBHelper.getCursorData(getApplicationContext(), bpQuery); if (cur
	 * != null) { try { if (cur.moveToFirst()) {
	 * 
	 * do {
	 * 
	 * bp = cur.getFloat(cur .getColumnIndex("Median")); } while
	 * (cur.moveToNext()); } } finally { cur.close(); } } return bp; }
	 */
	/**
	 * @param view
	 * 
	 */
	private void findViews(View view) {
		iv_screen_vitals_bp_cm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_bp_cm_plus);
		iv_screen_vitals_bp_cm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_bp_cm_minus);
		iv_screen_vitals_bp_mm_plus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_bp_mm_plus);
		iv_screen_vitals_bp_mm_minus = (ImageView) view
				.findViewById(R.id.iv_screen_vitals_bp_mm_minus);
		sb_screen_vital_bp_cm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_bp_cm);
		sb_screen_vital_bp_mm = (SeekBar) view
				.findViewById(R.id.sb_screen_vital_bp_mm);
		et_screen_vitals_bp_cm = (EditText) view
				.findViewById(R.id.et_screen_vitals_bp_cm);
		et_screen_vitals_bp_mm = (EditText) view
				.findViewById(R.id.et_screen_vitals_bp_mm);
		btn_screen_vitals_bp_save = (Button) view
				.findViewById(R.id.btn_screen_vitals_bp_save);
		btn_screen_vitals_bp_save.setOnClickListener(this);
		btn_screen_vitals_bp_cancel = (Button) view
				.findViewById(R.id.btn_screen_vitals_bp_cancel);
		btn_screen_vitals_bp_cancel.setOnClickListener(this);

		settingValues();
	}

	/**
	 * 
	 */
	// *** Setting values for find Views
	private void settingValues() {

		final int minRange = 1;

		sb_screen_vital_bp_cm.setProgress(bpDB_kg);
		et_screen_vitals_bp_cm.setText("" + bpDB_kg);
		sb_screen_vital_bp_cm.incrementProgressBy(10);
		sb_screen_vital_bp_cm.setMax(200 - minRange);
		sb_screen_vital_bp_cm
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					int progress = 0;

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progresValue, boolean fromUser) {
						progress = progresValue ;//- (progresValue % 10);
						sb_screen_vital_bp_cm.setOnSeekBarChangeListener(null);
						sb_screen_vital_bp_cm.setProgress(progress);
						sb_screen_vital_bp_cm.setOnSeekBarChangeListener(this);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						et_screen_vitals_bp_cm.setText(""
								+ (progress + minRange));

					}
				});
		iv_screen_vitals_bp_cm_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cm_string = et_screen_vitals_bp_cm.getText().toString();
				int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
				if (cm_int < 200) {
					int val = cm_int + minRange;
					et_screen_vitals_bp_cm.setText("" + (val));
					sb_screen_vital_bp_cm.setProgress(val);
				} else {

				}
			}
		});
		iv_screen_vitals_bp_cm_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cm_string = et_screen_vitals_bp_cm.getText().toString();
				int cm_int = NumUtil.IntegerParse.parseInt(cm_string);
				if (cm_int > minRange) {
					int val = cm_int - minRange;
					et_screen_vitals_bp_cm.setText("" + (val));
					sb_screen_vital_bp_cm.setProgress(val);
				} else {

				}
			}
		});

		sb_screen_vital_bp_mm.setProgress(bpDB_gm);
		et_screen_vitals_bp_mm.setText("" + bpDB_gm);
		sb_screen_vital_bp_mm.incrementProgressBy(10);
		sb_screen_vital_bp_mm.setMax(200 - minRange);

		sb_screen_vital_bp_mm
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					int progress = 0;

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progresValue, boolean fromUser) {
						progress = progresValue;// - (progresValue % 10);
						sb_screen_vital_bp_mm.setOnSeekBarChangeListener(null);
						sb_screen_vital_bp_mm.setProgress(progress);
						sb_screen_vital_bp_mm.setOnSeekBarChangeListener(this);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						et_screen_vitals_bp_mm.setText(""
								+ (progress + minRange));
					}
				});
		iv_screen_vitals_bp_mm_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String mm_string = et_screen_vitals_bp_mm.getText().toString()
						.trim();

				int mm_int = NumUtil.IntegerParse.parseInt(mm_string);
				if (mm_int < 200) {
					int val = mm_int + minRange;
					et_screen_vitals_bp_mm.setText("" + (val));
					sb_screen_vital_bp_mm.setProgress(val);
				}
			}
		});
		iv_screen_vitals_bp_mm_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mm_string = et_screen_vitals_bp_mm.getText().toString();
				int mm_int = NumUtil.IntegerParse.parseInt(mm_string);
				if (mm_int > minRange) {
					int val = mm_int - minRange;
					et_screen_vitals_bp_mm.setText("" + (val));
					sb_screen_vital_bp_mm.setProgress(val);
				}
			}
		});
		btn_screen_vitals_bp_cancel.setOnClickListener(this);
		btn_screen_vitals_bp_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_screen_vitals_bp_cancel) {
			dismiss();
		} else if (v == btn_screen_vitals_bp_save) {
			int bp_value = NumUtil.IntegerParse.parseInt(et_screen_vitals_bp_cm
					.getText().toString());
			int bp_indicate = NumUtil.IntegerParse
					.parseInt(et_screen_vitals_bp_mm.getText().toString());
			ScreeningVitalsFragment.setToTextView("" + Math.round(bp_value)
					+ "," + bp_indicate, "bp", this.getActivity());
		}
	}

}
