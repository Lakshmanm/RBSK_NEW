//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  SettingsPlan class

//* Type    : 

//* Description     : Activity to load settings
//* References     :                                                        
//* Author    :Deepika.chevvakula

//* Created Date       : 21-05-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations
//	3.0 		22-05-2015			Kiruthika 	     Change SeetingName to Setting
//  3.0 	    22-05-2015          Kiruthika        Setting Name Textview is not aligned properly. 
//  3.0         22-05-2015          kiruthika        Give comments 
//  3.0         22-05-2015          Kiruthika        Remove unwanted codes
//*****************************************************************************  

public class SettingsPlanActivity extends Activity implements OnClickListener {
	private Button btn_settings_close;
	private DBHelper dbh;
	private Cursor settingsData;
	private LinearLayout ll_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.settings_plan);
		findViews();
		dbh = DBHelper.getInstance(this);
		updateSettingsDataFromDb();
	}

	/**
	 * Method to get Setting data from Db
	 */
	@SuppressLint("InflateParams") public void updateSettingsDataFromDb() {
		settingsData = dbh.getCursorData(this,
				"Select * from devicesettings where IsDeleted!=1 ");
		ll_data.removeAllViews();
		if (settingsData != null) {
			try {
				if (settingsData.moveToFirst()) {
					int count = 0;
					do {
						LayoutInflater mInflater = LayoutInflater.from(this);
						View mCustomView = mInflater.inflate(
								R.layout.settings_plan_listview, null);
						TextView tv_settings_sno = (TextView) mCustomView
								.findViewById(R.id.tv_settings_sno);
						tv_settings_sno.setText("" + (count + 1));
						TextView tv_settings_name = (TextView) mCustomView
								.findViewById(R.id.tv_settings_name);
						TextView tv_settings_defaultValue = (TextView) mCustomView
								.findViewById(R.id.tv_settings_defaultvalue);
						TextView tv_settings_minVal = (TextView) mCustomView
								.findViewById(R.id.tv_settings_minVal);
						TextView tv_settings_maxVal = (TextView) mCustomView
								.findViewById(R.id.tv_settings_maxVal);
						tv_settings_name.setText(settingsData
								.getString(settingsData
										.getColumnIndex("DisplayText")));
						tv_settings_defaultValue.setText(settingsData
								.getString(settingsData
										.getColumnIndex("ConfigurationValue")));
						tv_settings_minVal.setText(settingsData
								.getString(settingsData
										.getColumnIndex("MinSettingsValue")));
						tv_settings_maxVal.setText(settingsData
								.getString(settingsData
										.getColumnIndex("MaxSettingsValue")));
						ll_data.addView(mCustomView);
						count++;
					} while (settingsData.moveToNext());
				}
			} finally {
				settingsData.close();
			}
		}
	}

	/**
	 * Get id for all views
	 */
	private void findViews() {
		ll_data = (LinearLayout) findViewById(R.id.ll_settings_lists);
		btn_settings_close = (Button) findViewById(R.id.btn_settings_close);
		btn_settings_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_settings_close) {
			finish();
		}
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
