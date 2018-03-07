//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

//*****************************************************************************
//* Name   :  ScreeningFilterStudent.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  Jun 12, 2015
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
public class ScreeningFilterStudent extends Activity implements OnClickListener {
	private ImageView iv_screening_filter_reset;
	private Spinner spn_screening_filter_status;
	private Spinner spn_screening_filter_gender;
	private Spinner spn_screening_filter_class;
	private Spinner spn_screening_filter_section;
	private Button btn_screening_filter_save;
	private Button btn_screening_filter_cancel;
	private ArrayList<String> chidStatus;
	private ArrayAdapter<String> csadapter;

	private ArrayList<String> chidgender;
	private ArrayAdapter<String> genderadapter;

	private ArrayList<String> chidclass;
	private ArrayAdapter<String> classadapter;

	private ArrayList<String> chidsection;
	private ArrayAdapter<String> sectionadapter;
	private DBHelper dbh;
	private static Cursor cur;
	private Vector<Integer> vec_status = new Vector<Integer>(),
			vec_class = new Vector<Integer>(),
			vec_gender = new Vector<Integer>(),
			vec_section = new Vector<Integer>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		// getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screening_filter_student);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.getInt("InstituteTypeId") == 1) {
				findViewById(R.id.school_group).setVisibility(View.GONE);
			}
		}
		dbh = DBHelper.getInstance(this);
		chidStatus = new ArrayList<String>();
		chidStatus = getChildStatusFromDb();
		csadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, chidStatus);
		csadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		chidgender = new ArrayList<String>();
		chidgender = getGenderFromDb();
		genderadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, chidgender);
		genderadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		chidclass = new ArrayList<String>();
		chidclass = getClassFromDb();
		classadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, chidclass);
		classadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		chidsection = new ArrayList<String>();
		chidsection = getSectionFromDb();
		sectionadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, chidsection);
		sectionadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		findViews();

		if (ScreeningActivity.filterData != null) {
			if (ScreeningActivity.filterData.get("StatusID") != null) {
				int StatusID = ScreeningActivity.filterData.get("StatusID");
				spn_screening_filter_status.setSelection(vec_status
						.indexOf(StatusID) + 1);
			}
			if (ScreeningActivity.filterData.get("GenderID") != null) {
				int GenderID = ScreeningActivity.filterData.get("GenderID");
				spn_screening_filter_gender.setSelection(vec_gender
						.indexOf(GenderID) + 1);
			}
			if (ScreeningActivity.filterData.get("ClassID") != null) {
				int ClassID = ScreeningActivity.filterData.get("ClassID");
				spn_screening_filter_class.setSelection(vec_class
						.indexOf(ClassID) + 1);

			}
			if (ScreeningActivity.filterData.get("SectionID") != null) {
				int SectionID = ScreeningActivity.filterData.get("SectionID");
				spn_screening_filter_section.setSelection(vec_section
						.indexOf(SectionID) + 1);
			}
		}
	}

	/**
	 * @return
	 */
	private ArrayList<String> getSectionFromDb() {
		vec_section.removeAllElements();
		ArrayList<String> bgroup = new ArrayList<String>();
		bgroup.add("Select");
		String relQuery = "Select SectionID,DisplayText from sections S where S.IsDeleted!=1 ";
		cur = dbh.getCursorData(this, relQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {
					do {
						bgroup.add(cur.getString(cur
								.getColumnIndex("DisplayText")));
						vec_section.add(NumUtil.IntegerParse.parseInt(cur
								.getString(cur.getColumnIndex("SectionID"))));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return bgroup;
	}

	/**
	 * @return
	 */
	private ArrayList<String> getClassFromDb() {
		vec_class.removeAllElements();
		ArrayList<String> bgroup = new ArrayList<String>();
		bgroup.add("Select");
		String relQuery = "Select ClassID,DisplayText from classes c where  c.IsDeleted!=1  ";
		cur = dbh.getCursorData(this, relQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {
					do {
						bgroup.add(cur.getString(cur
								.getColumnIndex("DisplayText")));
						vec_class.add(NumUtil.IntegerParse.parseInt(cur
								.getString(cur.getColumnIndex("ClassID"))));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return bgroup;
	}

	/**
	 * @return
	 */
	private ArrayList<String> getGenderFromDb() {
		vec_gender.removeAllElements();
		ArrayList<String> bgroup = new ArrayList<String>();
		bgroup.add("Select");
		String relQuery = "Select GenderID,DisplayText from gender g where  g.IsDeleted!=1 ";
		cur = dbh.getCursorData(this, relQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {
					do {
						bgroup.add(cur.getString(cur
								.getColumnIndex("DisplayText")));
						vec_gender.add(NumUtil.IntegerParse.parseInt(cur
								.getString(cur.getColumnIndex("GenderID"))));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return bgroup;
	}

	/**
	 * @return
	 */
	private ArrayList<String> getChildStatusFromDb() {
		vec_status.removeAllElements();
		ArrayList<String> bgroup = new ArrayList<String>();
		bgroup.add("Select");
		String relQuery = "Select ChildrenScreenStatusID,DisplayText from childrenscreenstatuses cs where  cs.IsDeleted!=1 ";
		cur = dbh.getCursorData(this, relQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {
					do {
						bgroup.add(cur.getString(cur
								.getColumnIndex("DisplayText")));
						vec_status
								.add(NumUtil.IntegerParse.parseInt(cur.getString(cur
										.getColumnIndex("ChildrenScreenStatusID"))));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return bgroup;
	}

	/**
	 * @param view
	 */
	private void findViews() {
		iv_screening_filter_reset = (ImageView) findViewById(R.id.iv_screening_filter_reset);
		iv_screening_filter_reset.setOnClickListener(this);
		spn_screening_filter_status = (Spinner) findViewById(R.id.spn_screening_filter_status);
		spn_screening_filter_status.setAdapter(csadapter);
		spn_screening_filter_gender = (Spinner) findViewById(R.id.spn_screening_filter_gender);
		spn_screening_filter_gender.setAdapter(genderadapter);
		spn_screening_filter_class = (Spinner) findViewById(R.id.spn_screening_filter_class);
		spn_screening_filter_class.setAdapter(classadapter);
		spn_screening_filter_section = (Spinner) findViewById(R.id.spn_screening_filter_section);
		spn_screening_filter_section.setAdapter(sectionadapter);
		btn_screening_filter_save = (Button) findViewById(R.id.btn_screening_filter_save);
		btn_screening_filter_save.setOnClickListener(this);
		btn_screening_filter_cancel = (Button) findViewById(R.id.btn_screening_filter_cancel);
		btn_screening_filter_cancel.setOnClickListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == btn_screening_filter_save) {
			ScreeningActivity.filterData = new HashMap<String, Integer>();
			if (spn_screening_filter_class.getSelectedItemPosition() != 0) {
				ScreeningActivity.filterData.put("ClassID", vec_class
						.elementAt(spn_screening_filter_class
								.getSelectedItemPosition() - 1));
			}
			if (spn_screening_filter_gender.getSelectedItemPosition() != 0) {
				ScreeningActivity.filterData.put("GenderID", vec_class
						.elementAt(spn_screening_filter_gender
								.getSelectedItemPosition() - 1));
			}
			if (spn_screening_filter_section.getSelectedItemPosition() != 0) {
				ScreeningActivity.filterData.put("SectionID", vec_class
						.elementAt(spn_screening_filter_section
								.getSelectedItemPosition() - 1));
			}
			if (spn_screening_filter_status.getSelectedItemPosition() != 0) {
				ScreeningActivity.filterData.put("StatusID", vec_class
						.elementAt(spn_screening_filter_status
								.getSelectedItemPosition() - 1));
			}
			ScreeningActivity.resumeFlag = true;
			finish();
		} else if (v == btn_screening_filter_cancel) {
			ScreeningActivity.resumeFlag = false;
			finish();
			return;
		} else if (v == iv_screening_filter_reset) {
			spn_screening_filter_status.setSelection(0);
			spn_screening_filter_section.setSelection(0);
			spn_screening_filter_gender.setSelection(0);
			spn_screening_filter_class.setSelection(0);
			ScreeningActivity.filterData.put("StatusID", null);
			ScreeningActivity.filterData.put("ClassID", null);
			ScreeningActivity.filterData.put("SectionID", null);
			ScreeningActivity.filterData.put("GenderID", null);
			return;

		}

		Intent in = new Intent(this, ScreeningActivity.class);
		Bundle mBundle = getIntent().getExtras();
		mBundle.putBoolean("ApplyFilter", true);
		in.putExtras(mBundle);
		startActivity(in);
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
