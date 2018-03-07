//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

//*****************************************************************************
//* Name   :  SignOffDocComments.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  03-Jun-2015
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
public class SignOffDocComments extends Activity implements OnClickListener {
	private EditText et_doctors_comments;
	private Button btn_recommendations_close, btn_recommendations_save;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);

		setContentView(R.layout.screening_doctors_comments);

		findViews();
		Bundle getIntent = getIntent().getExtras();
		position = getIntent.getInt("position");

		et_doctors_comments.setText(Helper.childScreeningObj.getSignOffModel()
				.getDoctorComments());
	}

	private void findViews() {
		
		et_doctors_comments = (EditText) findViewById(R.id.et_doctors_comments_doctors_comments);

		btn_recommendations_close = (Button) findViewById(R.id.btn_doctors_comments_close);
		btn_recommendations_close.setOnClickListener(this);
		btn_recommendations_save = (Button) findViewById(R.id.btn_doctors_comments_save);
		btn_recommendations_save.setOnClickListener(this);
	}

	// btn_doctors_comments_save
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		if (v == btn_recommendations_save) {
			Helper.childScreeningObj.getSignOffModel().setDoctorComments(
					et_doctors_comments.getText().toString().trim());
			Helper.childScreeningObj.getReferrals().get(position).getHealthConditonReferred().setUpdate(true);

			finish();
		} else if (v == btn_recommendations_close) {
			finish();
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
	    View v = getCurrentFocus();
	     
	    if (v != null && 
	            (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && 
	            v instanceof EditText && 
	            !v.getClass().getName().startsWith("android.webkit."))
	    {
	        int scrcoords[] = new int[2];
	        v.getLocationOnScreen(scrcoords);
	        float x = ev.getRawX() + v.getLeft() - scrcoords[0];
	        float y = ev.getRawY() + v.getTop() - scrcoords[1];
	 
	        if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
	            Helper.hideSoftKeyboard(this);
	    }
	    return super.dispatchTouchEvent(ev);
	}
}
