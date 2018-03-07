package nunet.rbsk.login;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
//NOT IMPLEMENTED CLASS BASED ON THE REQUIREMENT.THIS IS USED TO GIVE A START UP TO PROJECT
//*****************************************************************************
//* Name   :  Login class

//* Type    : Login activity

//* Description     : LOGIN 
//* References     :                                                        
//* Author    :promodh.munjeti

//* Created Date       : 22-04-2015
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

public class LoginActivity extends Activity implements OnClickListener {
	private Button btn_login;
	private EditText et_login_username;
	private EditText et_login_password;
	private TextView tv_login_forgot_password;
	public static final String LoginMain = "LoginMain";
	public static final String Username = "Username";
	public static final String Password = "Password";
	private SharedPreferences sharedpreferences;
	// private Editor editor;
	private String unlockId = "", unlockPassword = "";

	// DBHelper dbh = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.login);
		LinearLayout ll_login = (LinearLayout) findViewById(R.id.ll_login);

		ll_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Helper.hideSoftKeyboard(LoginActivity.this);
			}
		});
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		findViews();

		sharedpreferences = getSharedPreferences(LoginMain,
				Context.MODE_PRIVATE);
		unlockId = sharedpreferences.getString("UnlockID", "");
		unlockPassword = sharedpreferences.getString("UnlockPassword", "");

		et_login_username.setText("");
		et_login_password.setText("");

	}

	/**
	 * 
	 */
	private void findViews() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		tv_login_forgot_password = (TextView) findViewById(R.id.tv_login_forgot_password);
		tv_login_forgot_password.setOnClickListener(this);
		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		et_login_password.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					onClick(btn_login);
					return true;
				} else {
					return false;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {

		if (v == btn_login) {
			if (et_login_password.getText().toString().isEmpty()
					|| et_login_username.getText().toString().isEmpty()) {
				Helper.showShortToast(this, "Please enter the Credentials");
			} else
				{
					// Praveen Hack Code Start
					final ProgressDialog progDailog = ProgressDialog.show(this,
							"Processing ", "Please Wait", true);
					new Thread() {
						public void run() {
							try {
								Intent basic_info = new Intent(
										LoginActivity.this,
										UserLoginActivity.class);
								startActivity(basic_info);
							} catch (Exception e) {

							}
							handler.sendEmptyMessage(0);
							progDailog.dismiss();
						}
					}.start();
					// Praveen Hack Code End

					/* //commented by praveen
				if (et_login_username.getText().toString().trim()
						.equals(unlockId)
						&& et_login_password.getText().toString().trim()
								.equals(unlockPassword)) {
					final ProgressDialog progDailog = ProgressDialog.show(this,
							"Processing ", "Please Wait", true);
					new Thread() {
						public void run() {
							try {
								Intent basic_info = new Intent(
										LoginActivity.this,
										UserLoginActivity.class);
								startActivity(basic_info);
							} catch (Exception e) {

							}
							handler.sendEmptyMessage(0);
							progDailog.dismiss();
						}
					}.start();

				} else {
					Helper.showShortToast(this, "Wrong Credentials");
				}
				*/

			}

		} else if (v == tv_login_forgot_password) {
		}
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
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
