//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening.mh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nunet.adapter.CustomAllergyAdapter;
import nunet.adapter.CustomSurgicalAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Allergy;
import nunet.rbsk.model.MedicalHistoryScreenModel;
import nunet.rbsk.model.Surgery;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nunet.utils.DateUtil;
import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  ScreenHistPop.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  May 25, 2015
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
public class ScreenHistActivityDialog extends Activity implements OnClickListener {
	private TextView tv_screen_hist_type_name;
	private TextView tv_screen_hist_pop_type_name, tv_allergy_alleryName;
	private Spinner spn_screen_hist_type_name;
	private Button btn_allergies_close;
	private Button btn_allergies_save;
	private EditText et_screen_hist_type_comments;
	private String type;
	static Cursor cur;
	private ArrayAdapter<String> dataAdapter;
	private ArrayList<Allergy> allergies;
	private CustomAllergyAdapter customAdp;
	private ListView lv_allergy_allergies;
	private ImageView iv_allergies_add;
	private DBHelper dbh;
	private List<String[]> allergyData;
	private ArrayList<Surgery> surgeries;
	private CustomSurgicalAdapter customSurgicalAdp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.screening_history_pop);
		dbh = DBHelper.getInstance(this);
		Intent intent = getIntent();
		type = intent.getExtras().getString("Type");
		// List<String> lables = getAllergiesDb();
		findViews();
		updateScreeningRoundAndYear();
		if (StringUtils.equalsNoCase(type, "Allergies")) {
			getAllergiesFromDb();// *** to get Data from Master Allergies for
			// spinner.

			if ((Helper.childScreeningObj.getScreeningID() != 0)
					&& (Helper.childScreeningObj.getMedicalHistoryScreenModel()
							.getAllergies() == null)) {
				ArrayList<Allergy> screenedAllergies = updateAllergyHistoryForScreenedChild();
				if (screenedAllergies != null) {
					Helper.childScreeningObj.getMedicalHistoryScreenModel()
							.setAllergies(screenedAllergies);
				}
			}

			if (Helper.childScreeningObj.getMedicalHistoryScreenModel() != null) {
				MedicalHistoryScreenModel mhScreenModel = Helper.childScreeningObj
						.getMedicalHistoryScreenModel();
				if (mhScreenModel.getAllergies() == null) {
					allergies = new ArrayList<Allergy>();
				} else {
					allergies = mhScreenModel.getAllergies();
					updateListView(allergies);
				}
			} else {
				MedicalHistoryScreenModel mhScreenModel = new MedicalHistoryScreenModel();
				Helper.childScreeningObj
						.setMedicalHistoryScreenModel(mhScreenModel);
				allergies = new ArrayList<Allergy>();
			}
		} else {
			getSurergiesFromDb();// *** to get Data from Master Surgeries for
									// spinner.
			if ((Helper.childScreeningObj.getScreeningID() != 0)
					&& (Helper.childScreeningObj.getMedicalHistoryScreenModel()
							.getSurgeries() == null)) {
				ArrayList<Surgery> screenedsergies = updateSurgeryHistoryForScreenedChild();
				if (screenedsergies != null) {
					Helper.childScreeningObj.getMedicalHistoryScreenModel()
							.setSurgeries(screenedsergies);
				}
			}

			if (Helper.childScreeningObj.getMedicalHistoryScreenModel() != null) {
				MedicalHistoryScreenModel mhScreenModel = Helper.childScreeningObj
						.getMedicalHistoryScreenModel();
				if (mhScreenModel.getSurgeries() == null) {
					surgeries = new ArrayList<Surgery>();
				} else {
					surgeries = mhScreenModel.getSurgeries();
					updatesurgeryListView(surgeries);
				}
			} else {
				MedicalHistoryScreenModel mhScreenModel = new MedicalHistoryScreenModel();
				Helper.childScreeningObj
						.setMedicalHistoryScreenModel(mhScreenModel);
				surgeries = new ArrayList<Surgery>();
			}
		}
		
	}
	
	/**** Method for Setting the Height of the ListView dynamically.
	 **** Hack to fix the issue of not showing all the items of the ListView
	 **** when placed inside a ScrollView  ****/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}

	private int ScreeningRoundID = -1;
	private int ScreeningYear = -1;

	public void updateScreeningRoundAndYear() {
		String query = "SELECT ScreeningRoundID, strftime('%Y',RoundStartDate ) as ScreeningYear FROM ScreeningRounds" +
				" where  IsDeleted!=1 AND  '"
				+ DateUtil.format("yyyy-MM-dd", Calendar.getInstance())
				+ "' BETWEEN RoundStartDate and RoundEndDate";
		Cursor cursor = dbh.getCursorData(this, query);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				ScreeningRoundID = cursor.getInt(cursor.getColumnIndex("ScreeningRoundID"));
				ScreeningYear = cursor.getInt(cursor.getColumnIndex("ScreeningYear"));
				
			}
		}
	}

	public ArrayList<Allergy> updateAllergyHistoryForScreenedChild() {
		String query = "select * from childrenscreeningallergies CSA where  CSA.IsDeleted!=1 AND LocalChildrenScreeningID='"
				+ Helper.childScreeningObj.getScreeningID() + "' and IsDeleted='0';";
		Cursor cursor = dbh.getCursorData(this, query);
		if (cursor != null) {
			ArrayList<Allergy> allergies = new ArrayList<Allergy>();

			if (cursor.moveToFirst()) {
				do {
					Allergy allergyObj = new Allergy();
					allergyObj.setId(NumUtil.IntegerParse.parseInt(cursor.getString(cursor
							.getColumnIndex("AllergyID"))));
					allergyObj.setComments(cursor.getString(cursor
							.getColumnIndex("Comments")));
					allergyObj.setName(getName(NumUtil.IntegerParse.parseInt(cursor
							.getString(cursor.getColumnIndex("AllergyID")))));
					allergyObj.setScreeningRound(ScreeningRoundID);
					allergyObj.setScreeningYear(ScreeningYear);
					allergies.add(allergyObj);

				} while (cursor.moveToNext());
			}
			cursor.close();
			return allergies;
		}

		return null;
	}

	public ArrayList<Surgery> updateSurgeryHistoryForScreenedChild() {
		String query = "select * from childrenscreeningsurgicals CSG where  CSG.IsDeleted!=1 AND  LocalChildrenScreeningID='"
				+ Helper.childScreeningObj.getScreeningID() + "' and IsDeleted='0';";
		Cursor cursor = dbh.getCursorData(this, query);
		if (cursor != null) {
			ArrayList<Surgery> surgergies = new ArrayList<Surgery>();

			if (cursor.moveToFirst()) {
				do {
					Surgery surgeryObj = new Surgery();
					surgeryObj.setId(NumUtil.IntegerParse.parseInt(cursor.getString(cursor
							.getColumnIndex("SurgicalID"))));
					surgeryObj.setComments(cursor.getString(cursor
							.getColumnIndex("Comments")));
					surgeryObj.setName(getName(NumUtil.IntegerParse.parseInt(cursor
							.getString(cursor.getColumnIndex("SurgicalID")))));
					surgeryObj.setScreeningRound(ScreeningRoundID);
					surgeryObj.setScreeningYear(ScreeningYear);
					surgergies.add(surgeryObj);
					
				} while (cursor.moveToNext());
			}
			cursor.close();
			return surgergies;
		}
		return null;
	}

	public String getName(int id) {
		String name = "";
		for (int i = 0; i < allergyData.size(); i++) {
			if (NumUtil.IntegerParse.parseInt(allergyData.get(i)[0]) == id) {
				name = allergyData.get(i)[1];
			}
		}
		return name;
	}

	/**
	 * @return
	 */
	public void getAllergiesFromDb() {
		// List<String> labels = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  AllergyId,DisplayText FROM allergies A where  A.IsDeleted!=1 ";
		allergyData = dbh.getQueryData(this, selectQuery);
		dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.add("Select");
		if(allergyData!=null)
		for (int i = 0; i < allergyData.size(); i++) {
			dataAdapter.add(allergyData.get(i)[1]);
		}
		spn_screen_hist_type_name.setAdapter(dataAdapter);
	}

	/**
	 * @return
	 */
	public void getSurergiesFromDb() {
		// List<String> labels = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  SurgicalID,DisplayText FROM surgicals S where S.IsDeleted!=1 ";
		allergyData = dbh.getQueryData(this, selectQuery);
		dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.add("Select");
		if(allergyData!=null)
		for (int i = 0; i < allergyData.size(); i++) {
			dataAdapter.add(allergyData.get(i)[1]);
		}
		spn_screen_hist_type_name.setAdapter(dataAdapter);
	}
	

	/**
	 * 
	 */
	private void findViews() {
		
		tv_screen_hist_type_name = (TextView) findViewById(R.id.tv_screen_hist_type_name);
		tv_screen_hist_type_name.setText(type);
		tv_screen_hist_pop_type_name = (TextView) findViewById(R.id.tv_screen_hist_pop_type_name);
		spn_screen_hist_type_name = (Spinner) findViewById(R.id.spn_screen_hist_type_name);
		tv_allergy_alleryName = (TextView) findViewById(R.id.tv_allergy_alleryName);
		if (StringUtils.equalsNoCase(type, "Allergies")) {
			tv_screen_hist_pop_type_name.setText("Allergy");
			tv_allergy_alleryName.setText("Allergy");
		} else if (StringUtils.equalsNoCase(type, "Surgical")) {
			tv_screen_hist_pop_type_name.setText("Surgery");
			tv_allergy_alleryName.setText("Surgicals");
		}

		btn_allergies_close = (Button) findViewById(R.id.btn_allergies_close);
		btn_allergies_save = (Button) findViewById(R.id.btn_allergies_save);
		et_screen_hist_type_comments = (EditText) findViewById(R.id.et_screen_hist_type_comments);

		// ll_screen_hist_pop_list_view = (LinearLayout)
		// findViewById(R.id.ll_screen_hist_pop_list_view);
		btn_allergies_close.setOnClickListener(this);
		btn_allergies_save.setOnClickListener(this);
		iv_allergies_add = (ImageView) findViewById(R.id.iv_allergies_add);
		iv_allergies_add.setOnClickListener(this);
		lv_allergy_allergies = (ListView) findViewById(R.id.lv_allergy_allergies);
		setListViewHeightBasedOnChildren(lv_allergy_allergies);
		lv_allergy_allergies.setOnTouchListener(new OnTouchListener() {
		    // Setting on Touch Listener for handling the touch inside ScrollView
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    // Disallow the touch request for parent scroll on touch of child view
		    v.getParent().requestDisallowInterceptTouchEvent(true);
		    return false;
		    }
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		if (v == btn_allergies_close) {
			finish();
		} else if (v == iv_allergies_add) {
			int errorCount = 0;
			if (spn_screen_hist_type_name.getSelectedItemPosition() == 0) {
				if (StringUtils.equalsNoCase(type, "Allergies")) {
					Helper.setErrorForSpinner(spn_screen_hist_type_name,
							"Select Allergy");
				} else {
					Helper.setErrorForSpinner(spn_screen_hist_type_name,
							"Select Surgicals");
				}
				errorCount++;
			} else if (TextUtils.isEmpty(et_screen_hist_type_comments.getText()
					.toString().trim())) {
				errorCount++;
				et_screen_hist_type_comments.setError("Enter Comments");

			}
			if (errorCount == 0) {
				addRowToAllergies();
			}
		} else if (v == btn_allergies_save) {
			if (StringUtils.equalsNoCase(type, "Allergies")) {
				Helper.childScreeningObj.getMedicalHistoryScreenModel()
						.setAllergies(allergies);
			} else {
				Helper.childScreeningObj.getMedicalHistoryScreenModel()
						.setSurgeries(surgeries);
			}
			finish();
		}
	}

	/**
	 * to add rows to the allergy table in the view
	 */
	private void addRowToAllergies() {

		if (StringUtils.equalsNoCase(type, "Allergies")) {
			Allergy allergy = new Allergy();
			String id = allergyData.get(spn_screen_hist_type_name
					.getSelectedItemPosition() - 1)[0];
			allergy.setId(NumUtil.IntegerParse.parseInt(id));
			allergy.setComments(et_screen_hist_type_comments.getText()
					.toString().trim());
			allergy.setName(spn_screen_hist_type_name.getSelectedItem()
					.toString().trim());
			allergy.setScreeningRound(ScreeningRoundID);
			allergy.setScreeningYear(ScreeningYear);
			allergies.add(allergy);
			updateListView(allergies);
		} else {
			Surgery surgery = new Surgery();
			String id = allergyData.get(spn_screen_hist_type_name
					.getSelectedItemPosition() - 1)[0];
			surgery.setId(NumUtil.IntegerParse.parseInt(id));
			surgery.setComments(et_screen_hist_type_comments.getText()
					.toString().trim());
			surgery.setName(spn_screen_hist_type_name.getSelectedItem()
					.toString().trim());
			surgery.setScreeningRound(ScreeningRoundID);
			surgery.setScreeningYear(ScreeningYear);
			surgeries.add(surgery);
			updatesurgeryListView(surgeries);
		}

	}

	private void updateListView(ArrayList<Allergy> _allergies) {

		customAdp = new CustomAllergyAdapter(this, _allergies);

		lv_allergy_allergies.setAdapter(customAdp);
		spn_screen_hist_type_name.setSelection(0);
		et_screen_hist_type_comments.setError(null);
		et_screen_hist_type_comments.setText("");
	}

	private void updatesurgeryListView(ArrayList<Surgery> _surgeries) {
		customSurgicalAdp = new CustomSurgicalAdapter(this, _surgeries);
		lv_allergy_allergies.setAdapter(customSurgicalAdp);
		spn_screen_hist_type_name.setSelection(0);
		et_screen_hist_type_comments.setError(null);
		et_screen_hist_type_comments.setText("");
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
