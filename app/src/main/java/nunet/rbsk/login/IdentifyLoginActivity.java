//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.login;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
//* Name   :  IdentifyLogin.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  Jun 15, 2015
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
public class IdentifyLoginActivity extends Activity implements OnClickListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private LinearLayout ll_identify_login_user;
	private Button btn_identify_return;
	private TextView tv_identify_not_you;
	private SharedPreferences sharedpreferences;
	private LinearLayout[] ll_lay;
	private TextView[] tv_user_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.identify_login);
		findViews();
		sharedpreferences = getSharedPreferences(UserLoginActivity.UserLogin,
				Context.MODE_PRIVATE);
		String usedOne = sharedpreferences.getString(UserLoginActivity.noOfUsers, "");
		String[] users = usedOne.split(",");
		int noOfUsers = users.length;
		ll_identify_login_user.removeAllViews();
		ll_lay = new LinearLayout[noOfUsers];
		tv_user_name = new TextView[noOfUsers];
		for (int i = 0; i < noOfUsers; i++) {

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(R.layout.login_no_of_users,
					null);
			tv_user_name[i] = (TextView) mCustomView
					.findViewById(R.id.tv_name_user);
			tv_user_name[i].setText(users[i]);
			ll_lay[i] = (LinearLayout) mCustomView
					.findViewById(R.id.ll_name_user);
			ll_lay[i].setId(i);
			ll_lay[i].setOnClickListener(this);
			ll_identify_login_user.addView(mCustomView);
		}
	}

	/**
	 * 
	 */
	private void findViews() {
		ll_identify_login_user = (LinearLayout) findViewById(R.id.ll_identify_login_user);

		btn_identify_return = (Button) findViewById(R.id.btn_identify_return);
		btn_identify_return.setOnClickListener(this);
		tv_identify_not_you = (TextView) findViewById(R.id.tv_identify_not_you);
		tv_identify_not_you.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (v == btn_identify_return) {
			Intent in = new Intent(IdentifyLoginActivity.this, LoginActivity.class);
			startActivity(in);
		} else if (v == tv_identify_not_you) {
			Intent in = new Intent(IdentifyLoginActivity.this, UserLoginActivity.class);
			startActivity(in);
		} else if (v == ll_lay[id]) {
			String userName = tv_user_name[id].getText().toString().trim();
			Intent in = new Intent(IdentifyLoginActivity.this, FinalLoginActivity.class);
			in.putExtra("user", userName);
			startActivity(in);
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		// super.onBackPressed();
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
