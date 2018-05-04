//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;

//*****************************************************************************
//* Name   :  SkipInstitute.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  29-May-2015
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
public class SkipInstituteActivityDialog extends Activity implements
		OnClickListener {

	private Spinner spn_plan_skip_institute_reasons;
	private Button btn_plan_skip_save;
	private Button btn_plan_skip_cancel;
	private EditText et_skip_plan_skip_reason;
	private DBHelper dbh;
	private long LocalInstitutePlanDetailID;
	private int skipReasonID;
	public boolean isSkiped = false;
	public String currentDate = "";
	public String plannedDate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.skip_institute_plan);

		dbh = DBHelper.getInstance(this);
		LocalInstitutePlanDetailID = getIntent().getLongExtra(
				"LocalInstitutePlanDetailID", 0);
		plannedDate = getIntent().getStringExtra("PlannedDate");
		findViews();
		getSkipReasonsFromDb();
		currentDate = changeCurrentDateFormat();

	}

	/**
	 * 
	 */
	private void getSkipReasonsFromDb() {

		String query = "select InstitutePlanSkipReasonID,DisplayText from InstitutePlanSkipReasons IPSR where  IPSR.IsDeleted!=1 ";
		final List<String[]> data = dbh.getCursorFromQueryData(this, query);
		ArrayAdapter<String> adp_spnskip = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adp_spnskip
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adp_spnskip.add("--Select--");
		for (int i = 0; i < data.size(); i++) {
			adp_spnskip.add(data.get(i)[1]);
		}
		spn_plan_skip_institute_reasons.setAdapter(adp_spnskip);
		spn_plan_skip_institute_reasons
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						if (spn_plan_skip_institute_reasons
								.getSelectedItemPosition() != 0) {
							skipReasonID = NumUtil.IntegerParse.parseInt(data
									.get(spn_plan_skip_institute_reasons
											.getSelectedItemPosition() - 1)[0]);
							isSkiped = true;
						} else {
							isSkiped = false;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
	}

	private String changeCurrentDateFormat() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		return currentDate;
	}

	/**
	 * 
	 */
	private void findViews() {

		spn_plan_skip_institute_reasons = (Spinner) findViewById(R.id.spn_plan_skip_insitute_reasons);
		btn_plan_skip_save = (Button) findViewById(R.id.btn_plan_skip_save);
		btn_plan_skip_cancel = (Button) findViewById(R.id.btn_plan_skip_cancel);
		et_skip_plan_skip_reason = (EditText) findViewById(R.id.et_skip_plan_skip_reason);
		btn_plan_skip_save.setOnClickListener(this);
		btn_plan_skip_cancel.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		if (v == btn_plan_skip_save) {
			if (spn_plan_skip_institute_reasons.getSelectedItemPosition() == 0) {
				spn_plan_skip_institute_reasons.setFocusableInTouchMode(true);
				spn_plan_skip_institute_reasons.setFocusable(true);
				spn_plan_skip_institute_reasons.requestFocus();
				Helper.showShortToast(this,
						"Select the reason to skip the Institute");
			} else {
				boolean updateFlag = updateSkipReasonToDB();
				if (updateFlag)
					Helper.showShortToast(this,
							"The Institute is skipped for today");
				Intent i = new Intent(SkipInstituteActivityDialog.this,
						DashBoardActivity.class);
				finish();
				startActivity(i);
			}
		} else if (v == btn_plan_skip_cancel) {
			finish();
		}
	}

	/**
	 * 
	 */
	private boolean updateSkipReasonToDB() {
//		return dbh.updateROWByValues(this, "Instituteplandetails",
//				new String[] { "PlanStatusID", "InstitutePlanSkipReasonID",
//						"SkipComments", "PlannedCount" }, new String[] { "2",
//						String.valueOf(skipReasonID),
//						et_skip_plan_skip_reason.getText().toString().trim(),
//						"0" }, new String[] { "LocalInstitutePlanDetailID" },
//				new String[] { String.valueOf(LocalInstitutePlanDetailID) });

		return dbh.updateROWByValues(this, "Instituteplandetails",
				new String[] { "PlanStatusID", "InstitutePlanSkipReasonID",
						"SkipComments"}, new String[] { "2",
						String.valueOf(skipReasonID),
						et_skip_plan_skip_reason.getText().toString().trim() }, new String[] { "LocalInstitutePlanDetailID" },
				new String[] { String.valueOf(LocalInstitutePlanDetailID) });
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		View v = getCurrentFocus();

		if (v != null
				&& (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
				&& v instanceof EditText
				&& !v.getClass().getName().startsWith("android.webkit.")) {
			int scrcoords[] = new int[2];
			v.getLocationOnScreen(scrcoords);
			float x = ev.getRawX() + v.getLeft() - scrcoords[0];
			float y = ev.getRawY() + v.getTop() - scrcoords[1];

			if (x < v.getLeft() || x > v.getRight() || y < v.getTop()
					|| y > v.getBottom())
				Helper.hideSoftKeyboard(this);
		}
		return super.dispatchTouchEvent(ev);
	}

}
