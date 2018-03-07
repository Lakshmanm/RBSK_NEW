//package nunet.rbsk;
//
//import java.util.ArrayList;
//
//import nunet.rbsk.helpers.DBHelper;
//import nunet.rbsk.helpers.Helper;
//import nunet.rbsk.info.inst.InsituteFragment;
//import nunet.rbsk.model.Children;
//import nunet.rbsk.model.Institute;
//import android.app.ActionBar;
//import android.app.Activity;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
////=============================================================================
////All rights reserved to Nunet Cube Software Solutions.
////THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
////OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
////LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
////FITNESS FOR A PARTICULAR PURPOSE.
////=============================================================================
//
////*****************************************************************************
////* Name   :  Login class
//
////* Type    : Login activity
//
////* Description     : Activity for loading the comments
////* References     :                                                        
////* Author    :Deepika.chevvakula
//
////* Created Date       : 23-04-2015
////*****************************************************************************
////*                             MODIFICATION LOG
////*****************************************************************************
////* Ver Date
//
////*
////*
////*                             Code Review LOG                    
////*****************************************************************************                    
////* Ver        Date                Code Review By            Observations
//
////*****************************************************************************  
//
//public class SearchInstitute extends Activity implements OnClickListener {
//	private Button btn_search_institute_searchGo;
//	private LinearLayout ll_inst_search_result;
//	private TextView[] tv_inst_search_dise_code;
//	private TextView[] tv_inst_search_inst_name;
//	private TextView[] tv_inst_search_screening_per;
//	private TextView[] tv_inst_search_last_screened;
//	private ImageView[] iv_inst_search_info;
//	private ImageView[] iv_inst_search_students;
//	private ImageView[] iv_inst_search_add;
//	private Cursor instCursor;
//	private ArrayList<Institute> insList;
//	private EditText et_search_institute_search;
//	private DBHelper dbh;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.search_institute);
//		dbh=DBHelper.getInstance(this);
//		LinearLayout ll_login = (LinearLayout) findViewById(R.id.ll_search_inst);
//		ll_login.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				
//				Helper.hideSoftKeyboard(SearchInstitute.this);
//			}
//		});
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		findViews();
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		// actionBar.setIcon(R.drawable.icon);
//		LayoutInflater mInflater = LayoutInflater.from(this);
//
//		View mCustomView = mInflater.inflate(R.layout.action_bar_home, null);
////		TextView mTitleTextView = (TextView) mCustomView
////				.findViewById(R.id.tv_schoolName);
////		mTitleTextView.setText(ConstantClass.selected_SchoolName);
//		actionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
//				ActionBar.LayoutParams.MATCH_PARENT,
//				ActionBar.LayoutParams.MATCH_PARENT));
//		actionBar.setIcon(R.drawable.icon);
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setBackgroundDrawable(new ColorDrawable(Color
//				.parseColor("#815CC5")));
//		getInstituteDataFromDB();
//		et_search_institute_search.addTextChangedListener(new TextWatcher() {
//
//			public void afterTextChanged(Editable s) {
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (et_search_institute_search.getText().toString().trim()
//						.length() == 0) {
//					getUpdate_view("", 0);
//				} else if (et_search_institute_search.getText().toString()
//						.trim().length() >= 3) {
//					et_search_institute_search.setError(null);
//				}
//
//			}
//		});
//	}
//
//	/**
//	 * Kiruthika 22-04-2015 Method to get Institute List from DB
//	 */
//	private void getInstituteDataFromDB() {
//		
//		String instituteQuery = "Select InstituteID,InstituteName,DiseCode from institutes";
//		instCursor = dbh.getCursorData(this, instituteQuery);
//		if (instCursor != null) {
//			insList = new ArrayList<Institute>();
//			try {
//
//				if (instCursor.moveToFirst()) {
//
//					do {
//						Institute insModelObject = new Institute();
//						insModelObject.setInstituteID(IntUtil.Integer
//								.parseInt(instCursor.getString(instCursor
//										.getColumnIndex("InstituteID"))));
//						insModelObject.setInstituteName(instCursor
//								.getString(instCursor
//										.getColumnIndex("InstituteName")));
//						insModelObject.setDiseCode(instCursor
//								.getString(instCursor
//										.getColumnIndex("DiseCode")));
//						insList.add(insModelObject);
//					} while (instCursor.moveToNext());
//
//				}
//
//			} finally {
//				instCursor.close();
//			}
//			getUpdate_view("", 0);
//		}
//	}
//
//	/**
//	 * Method to get update the Institute List from DB
//	 */
//
//	public void getUpdate_view(String txtData, int searchByFlagIndex) {
//		ll_inst_search_result.removeAllViews();
//		tv_inst_search_dise_code = new TextView[insList.size()];
//		tv_inst_search_inst_name = new TextView[insList.size()];
//		tv_inst_search_screening_per = new TextView[insList.size()];
//		tv_inst_search_last_screened = new TextView[insList.size()];
//		iv_inst_search_info = new ImageView[insList.size()];
//		iv_inst_search_students = new ImageView[insList.size()];
//		iv_inst_search_add = new ImageView[insList.size()];
//		if (!insList.isEmpty()) {
//			int resultCount = 0;
//			for (int i = 0; i < insList.size(); i++) {
//				LayoutInflater mInflater = LayoutInflater.from(this);
//				View mCustomView = mInflater.inflate(
//						R.layout.institute_search_result, null);
//				tv_inst_search_dise_code[i] = (TextView) mCustomView
//						.findViewById(R.id.tv_inst_search_dise_code);
//				tv_inst_search_inst_name[i] = (TextView) mCustomView
//						.findViewById(R.id.tv_inst_search_inst_name);
//				tv_inst_search_screening_per[i] = (TextView) mCustomView
//						.findViewById(R.id.tv_inst_search_screening_per);
//				tv_inst_search_last_screened[i] = (TextView) mCustomView
//						.findViewById(R.id.tv_inst_search_last_screened);
//				iv_inst_search_info[i] = (ImageView) mCustomView
//						.findViewById(R.id.iv_inst_search_info);
//				iv_inst_search_students[i] = (ImageView) mCustomView
//						.findViewById(R.id.iv_inst_search_students);
//				iv_inst_search_add[i] = (ImageView) mCustomView
//						.findViewById(R.id.iv_inst_search_add);
//				if (searchByFlagIndex == 0) {// no need to search or update view
//					iv_inst_search_info[i].setId(i);
//					iv_inst_search_info[i].setOnClickListener(this);
//
//					iv_inst_search_add[i].setId(i);
//					iv_inst_search_add[i].setOnClickListener(this);
//
//					iv_inst_search_students[i].setId(i);
//					iv_inst_search_students[i].setOnClickListener(this);
//					ll_inst_search_result.addView(mCustomView);
//					tv_inst_search_dise_code[i].setText(insList.get(i)
//							.getDiseCode());
//					tv_inst_search_inst_name[i].setText(insList.get(i)
//							.getInstituteName());
//					resultCount++;
//				} else if (searchByFlagIndex == 1) {// serach by Inst Name
//					String instName = insList.get(i).getInstituteName();
//					if (instName.trim().toLowerCase()
//							.contains(txtData.trim().toLowerCase())) {
//						iv_inst_search_info[i].setId(i);
//						iv_inst_search_info[i].setOnClickListener(this);
//
//						iv_inst_search_add[i].setId(i);
//						iv_inst_search_add[i].setOnClickListener(this);
//
//						iv_inst_search_students[i].setId(i);
//						iv_inst_search_students[i].setOnClickListener(this);
//						ll_inst_search_result.addView(mCustomView);
//						tv_inst_search_dise_code[i].setText(insList.get(i)
//								.getDiseCode());
//						tv_inst_search_inst_name[i].setText(instName);
//						resultCount++;
//					}
//				} else if (searchByFlagIndex == 2) {// serach by DISC code
//					String diseCode = insList.get(i).getDiseCode();
//					if (diseCode.trim().contains(txtData.trim())) {
//						iv_inst_search_info[i].setId(i);
//						iv_inst_search_info[i].setOnClickListener(this);
//
//						iv_inst_search_add[i].setId(i);
//						iv_inst_search_add[i].setOnClickListener(this);
//
//						iv_inst_search_students[i].setId(i);
//						iv_inst_search_students[i].setOnClickListener(this);
//						ll_inst_search_result.addView(mCustomView);
//						tv_inst_search_dise_code[i].setText(diseCode);
//						tv_inst_search_inst_name[i].setText(insList.get(i)
//								.getInstituteName());
//						resultCount++;
//					}
//				}
//
//			}
//			if (resultCount==0) {
//				TextView myText = new TextView(this);
//				myText.setText("No Records Found");
//				myText.setGravity(Gravity.CENTER);
//				myText.setTextColor(Color.parseColor("#e6e6e6"));
//				myText.setTextSize(28);
//				ll_inst_search_result.addView(myText);
//			}
//		}
//	}
//
//	/**
//	 * onclick listener for the views
//	 */
//	@Override
//	public void onClick(View v) {
//		
//		if (v == btn_search_institute_searchGo) {
//			String txtData = et_search_institute_search.getText().toString()
//					.trim();
//			if (txtData.length() < 3) {
//				et_search_institute_search
//						.setError("Please enter atleast 3 characters to start search");
//			} else {
//				searchWithText(txtData);
//			}
//		} else if (v == iv_inst_search_info[v.getId()]) {
//			Intent i = new Intent(SearchInstitute.this, InsituteFragment.class);
//			i.putExtra("InsituteID", insList.get(v.getId()).getInstituteID());
//			startActivity(i);
//		} else if (v == iv_inst_search_add[v.getId()]) {
//			Intent s = new Intent(SearchInstitute.this,
//					AddStudentFragment.class);
//			Helper.childrenObject = new Children();
//			Helper.selectedInstituteId=insList.get(v.getId()).getInstituteID();
//			//s.putExtra("InsituteID",
//			// insList.get(v.getId()).getInstituteID());
//			startActivity(s);
//		}
//	}
//
//	/**
//	 * to search based on the text Deepika 22-04-2015
//	 */
//	private void searchWithText(String txtData) {
//		
//		if (isNumber(txtData)) {// search with DISE code
//			getUpdate_view(txtData, 2);
//		} else {// search with instName
//			getUpdate_view(txtData, 1);
//		}
//	}
//
//	/**
//	 * To check the text entered in search
//	 */
//	public static boolean isNumber(String string) {
//		try {
//			Long.parseLong(string);
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * To get the view id's from R.java Deepika 22-04-2015
//	 */
//	private void findViews() {
//		
//		btn_search_institute_searchGo = (Button) findViewById(R.id.btn_search_institute_searchGo);
//		btn_search_institute_searchGo.setOnClickListener(this);
//		ll_inst_search_result = (LinearLayout) findViewById(R.id.ll_inst_search_result);
//		et_search_institute_search = (EditText) findViewById(R.id.et_search_institute_search);
//	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev)
//	{
//	    View v = getCurrentFocus();
//	     
//	    if (v != null && 
//	            (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && 
//	            v instanceof EditText && 
//	            !v.getClass().getName().startsWith("android.webkit."))
//	    {
//	        int scrcoords[] = new int[2];
//	        v.getLocationOnScreen(scrcoords);
//	        float x = ev.getRawX() + v.getLeft() - scrcoords[0];
//	        float y = ev.getRawY() + v.getTop() - scrcoords[1];
//	 
//	        if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
//	            Helper.hideSoftKeyboard(this);
//	    }
//	    return super.dispatchTouchEvent(ev);
//	}
//}
