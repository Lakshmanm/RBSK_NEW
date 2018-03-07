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

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.FamilyHistoryDisease;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  ScreeningFamilyHistoryNew.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  01-Jun-2015
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
public class ScreeningFamilyHistory extends Activity implements OnClickListener {

	private LinearLayout ll_family_history_layout;
	private Cursor familyHistoryCur;
	private ArrayList<HashMap<String, String>> relations;
	private ArrayList<FamilyHistoryDisease> familyHistoryList;
	private ArrayAdapter<String> adapter;
	private Button btn_family_history_save;
	private Button btn_family_history_close;

	private Spinner[] spn_family_history_diseases;
	private EditText[] et_family_history_diseases_comments;
	private DBHelper dbh;

	@Override
	protected void onStart() {

		super.onStart();
		final Handler handler = new Handler();
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (ScreeningBasicInfoFragment.dialog.isShowing()) {
					ScreeningBasicInfoFragment.dialog.dismiss();
				}
			}
		};
		handler.postDelayed(runnable, 1000);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.screening_family_history);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dbh = DBHelper.getInstance(this);
		findViews();
		relations = new ArrayList<HashMap<String, String>>();
		relations = getRelationsFromDb();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < relations.size(); i++) {
			adapter.add(relations.get(i).get("RelationName"));
		}

		// *** New Student.

		ArrayList<FamilyHistoryDisease> mFamilyHistoryDisease = getFamilyHistoryDataFromDB();
		if (Helper.childScreeningObj.getFamilyHistoryDiseases() != null) {

			familyHistoryList = Helper.childScreeningObj
					.getFamilyHistoryDiseases();
			for (FamilyHistoryDisease familyHistoryDisease : mFamilyHistoryDisease) {

				for (int i = 0; i < familyHistoryList.size(); i++) {
					FamilyHistoryDisease familyHistoryDisease2 = familyHistoryList
							.get(i);
					if (familyHistoryDisease.getDiseaseID() == familyHistoryDisease2
							.getDiseaseID()) {
						familyHistoryDisease
								.setDiseaseComments(familyHistoryDisease2
										.getDiseaseComments());
						familyHistoryDisease.setSelected(familyHistoryDisease2
								.isSelected());
						familyHistoryDisease
								.setRelationID(familyHistoryDisease2
										.getRelationID());
						break;
					}
				}

			}

			Helper.childScreeningObj
					.setFamilyHistoryDiseases(mFamilyHistoryDisease);
		}
		familyHistoryList = mFamilyHistoryDisease;

		inflateToFamilyHistory();

	}

	/**
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getRelationsFromDb() {
		relations = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("RelationID", "0");
		hm.put("RelationName", "Select");
		relations.add(hm);
		String relQuery = "Select FamilyMemberRelationID,DisplayText from FamilyMemberRelations R where  R.IsDeleted!=1 ";
		Cursor cur = dbh.getCursorData(getApplicationContext(), relQuery);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {

					do {
						hm = new HashMap<String, String>();
						hm.put("RelationID", cur.getString(cur
								.getColumnIndex("FamilyMemberRelationID")));
						hm.put("RelationName", cur.getString(cur
								.getColumnIndex("DisplayText")));
						relations.add(hm);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return relations;
	}

	/**
	 * 
	 */
	private ArrayList<FamilyHistoryDisease> getFamilyHistoryDataFromDB() {

		String query_Wards = "select DisplayText,FamilyHistoryID from familyhistory FH where  FH.IsDeleted!=1  ";

		familyHistoryCur = dbh.getCursorData(this, query_Wards);
		return setToFamilyHistoryModel();
	}

	/**
	 * 
	 */
	private ArrayList<FamilyHistoryDisease> setToFamilyHistoryModel() {
		ArrayList<FamilyHistoryDisease> familyHistoryList = new ArrayList<FamilyHistoryDisease>();

		if (familyHistoryCur != null) {
			try {
				if (familyHistoryCur.moveToFirst()) {
					do {
						FamilyHistoryDisease familyHistoryObj = new FamilyHistoryDisease();
						familyHistoryObj
								.setDiseaseID(NumUtil.IntegerParse.parseInt(familyHistoryCur.getString(familyHistoryCur
										.getColumnIndex("FamilyHistoryID"))));

						familyHistoryObj.setDiseaseName(familyHistoryCur
								.getString(familyHistoryCur
										.getColumnIndex("DisplayText")));

						familyHistoryObj.setRelationID(0);
						familyHistoryObj.setDiseaseComments("");

						familyHistoryObj.setSelected(false);

						familyHistoryList.add(familyHistoryObj);

					} while (familyHistoryCur.moveToNext());
				}
			} finally {
				familyHistoryCur.close();
			}
		}
		return familyHistoryList;
		// inflateToFamilyHistory();
	}

	/**
	 * 
	 */
	private void inflateToFamilyHistory() {

		ll_family_history_layout.removeAllViews();
		spn_family_history_diseases = new Spinner[familyHistoryList.size()];
		et_family_history_diseases_comments = new EditText[familyHistoryList
				.size()];
		for (int i = 0; i < familyHistoryList.size(); i++) {

			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			final View view_history = inflator.inflate(
					R.layout.familyhistory_item, null);
			TextView tv_familyhistory_disease_name = (TextView) view_history
					.findViewById(R.id.tv_familyhistory_disease_name);
			final Button btn_family_history_diseases_no = (Button) view_history
					.findViewById(R.id.btn_family_history_diseases_no);
			final Button btn_family_history_diseases_yes = (Button) view_history
					.findViewById(R.id.btn_family_history_diseases_yes);
			spn_family_history_diseases[i] = (Spinner) view_history
					.findViewById(R.id.spn_family_history_diseases);
			et_family_history_diseases_comments[i] = (EditText) view_history
					.findViewById(R.id.et_family_history_diseases_comments);
			for (int j = 0; j < relations.size(); j++) {
				if (StringUtils.equalsNoCase(
						relations.get(j).get("RelationID"), String
								.valueOf(familyHistoryList.get(i)
										.getRelationID()))) {
					spn_family_history_diseases[i].setSelection(j);
				}
			}
			spn_family_history_diseases[i].setAdapter(adapter);
			et_family_history_diseases_comments[i].setText(familyHistoryList
					.get(i).getDiseaseComments());

			tv_familyhistory_disease_name.setText(familyHistoryList.get(i)
					.getDiseaseName());
			spn_family_history_diseases[i].setSelection(familyHistoryList
					.get(i).getRelationID());
			spn_family_history_diseases[i].setId(i);
			et_family_history_diseases_comments[i].setId(i);

			if (familyHistoryList.get(i).isSelected()) {
				btn_family_history_diseases_yes.setBackgroundColor(Color
						.parseColor("#45cfc1"));
				btn_family_history_diseases_no.setBackgroundColor(Color.LTGRAY);
				spn_family_history_diseases[i].setFocusable(true);
				spn_family_history_diseases[i].setEnabled(true);
				et_family_history_diseases_comments[i].setEnabled(true);
			} else {
				btn_family_history_diseases_no.setBackgroundColor(Color
						.parseColor("#ff6b6b"));
				btn_family_history_diseases_yes
						.setBackgroundColor(Color.LTGRAY);
				spn_family_history_diseases[i].setFocusable(false);
				spn_family_history_diseases[i].setEnabled(false);
				et_family_history_diseases_comments[i].setEnabled(false);
			}
			btn_family_history_diseases_yes.setId(i);
			btn_family_history_diseases_no.setId(i);
			btn_family_history_diseases_yes
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							int id = v.getId();
							spn_family_history_diseases[id].setFocusable(true);
							spn_family_history_diseases[id].setEnabled(true);
							et_family_history_diseases_comments[id]
									.setEnabled(true);
							btn_family_history_diseases_yes
									.setBackgroundColor(Color
											.parseColor("#45cfc1"));
							btn_family_history_diseases_no
									.setBackgroundColor(Color.LTGRAY);
							familyHistoryList.get(id).setSelected(true);
						}
					});

			btn_family_history_diseases_no
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							int id = v.getId();
							spn_family_history_diseases[id].setFocusable(false);
							spn_family_history_diseases[id].setEnabled(false);
							spn_family_history_diseases[id].setSelection(0);
							et_family_history_diseases_comments[id]
									.setEnabled(false);
							btn_family_history_diseases_no
									.setBackgroundColor(Color
											.parseColor("#ff6b6b"));
							btn_family_history_diseases_yes
									.setBackgroundColor(Color.LTGRAY);

							familyHistoryList.get(id).setSelected(false);
							et_family_history_diseases_comments[id].setText("");
						}
					});

			ll_family_history_layout.addView(view_history);

		}
	}

	/**
	 * 
	 */
	private void findViews() {

		ll_family_history_layout = (LinearLayout) findViewById(R.id.ll_family_history_layout);
		btn_family_history_save = (Button) findViewById(R.id.btn_family_history_save);
		btn_family_history_save.setOnClickListener(this);
		btn_family_history_close = (Button) findViewById(R.id.btn_family_history_close);
		btn_family_history_close.setOnClickListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_family_history_save:

			for (int j = 0; j < familyHistoryList.size(); j++) {
				if (familyHistoryList.get(j).isSelected()) {
					FamilyHistoryDisease familyHistoryDisease = familyHistoryList
							.get(j);
					if (familyHistoryDisease.isSelected()
							&& spn_family_history_diseases[j]
									.getSelectedItemPosition() == 0) {
						Toast.makeText(
								this,
								"Please update "
										+ familyHistoryDisease.getDiseaseName()
										+ " person name", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				}
			}

			updateToFamilyHistoryModel();
			finish();
			break;

		case R.id.btn_family_history_close:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 */
	private void updateToFamilyHistoryModel() {

		for (int i = 0; i < familyHistoryList.size(); i++) {
			familyHistoryList.get(i).setRelationID(
					NumUtil.IntegerParse.parseInt(relations.get(
							spn_family_history_diseases[i]
									.getSelectedItemPosition()).get(
							"RelationID")));
			familyHistoryList.get(i)
					.setDiseaseComments(
							et_family_history_diseases_comments[i].getText()
									.toString());

		}
		Helper.childScreeningObj.setFamilyHistoryDiseases(familyHistoryList);
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
