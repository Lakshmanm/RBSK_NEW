//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.login;

import nunet.rbsk.R;
import nunet.rbsk.dashboard.DashBoardActivity;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  FinalLogin.java

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
public class FinalLoginActivity extends Activity implements OnClickListener {

	private TextView tv_final_login_username;
	private EditText et_final_login_password;
	private Button btn_final_login;
	private Button btn_final_return;
	private TextView tv_final_forgot_password;
	private String user;
	private static Cursor cur;
	private DBHelper dbh;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.final_login);
		dbh = DBHelper.getInstance(this);
		findViews();
		user = getIntent().getStringExtra("user");
		tv_final_login_username.setText(user);
	}

	/**
	 * 
	 */
	private void findViews() {
		
		tv_final_login_username = (TextView) findViewById(R.id.tv_final_login_username);
		tv_final_forgot_password = (TextView) findViewById(R.id.tv_final_forgot_password);
		tv_final_forgot_password.setOnClickListener(this);
		et_final_login_password = (EditText) findViewById(R.id.et_final_login_password);
		btn_final_login = (Button) findViewById(R.id.btn_final_login);
		btn_final_login.setOnClickListener(this);
		btn_final_return = (Button) findViewById(R.id.btn_final_return);
		btn_final_return.setOnClickListener(this);
		et_final_login_password.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					onClick(btn_final_login);
					return true;
				} else {
					return false;
				}
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
		
		if (v == btn_final_login) {
			String password = getPasswordFromDb(user);
			if (et_final_login_password.getText().toString().isEmpty()) {
				Helper.showShortToast(this, "Enter Password");
			} else {
				if (StringUtils.equalsNoCase(et_final_login_password.getText().toString().trim()
						,password)) {
					
					Intent in = new Intent(FinalLoginActivity.this,
							DashBoardActivity.class);
					startActivity(in);
				} else {
					Helper.showShortToast(this, "Wrong Password Entered");
				}
			}
		} else if (v == btn_final_return) {
			Intent in = new Intent(FinalLoginActivity.this, LoginActivity.class);
			startActivity(in);
		} else if (v == tv_final_forgot_password) {
		}
	}

	

	private String getPasswordFromDb(String user) {
		
		String password = null;
		String relQuery = "Select Password from usercredentials UC  where  UC.IsDeleted!=1 AND  Login='"
				+ user + "'";
		cur = dbh.getCursorData(this, relQuery);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {

					do {

						password = (cur.getString(cur
								.getColumnIndex("Password")));
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return password;
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
