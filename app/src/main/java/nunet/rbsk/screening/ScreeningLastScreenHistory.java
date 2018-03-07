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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

//*****************************************************************************
//* Name   :  ScrreningLastScreenHistory.java

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
public class ScreeningLastScreenHistory extends Activity implements
		OnClickListener {
	private ImageView iv_last_screened_history_previous;
	private ImageView iv_last_screened_history_next;

//	private TextView tv_last_screened_history_date;
//	private TextView tv_last_screened_history_doctor;
//	private TextView tv_last_screened_history_round_no;
//	private TextView tv_last_screened_history_age_at_screening;
//	private TextView tv_last_screened_history_referred;
//	private LinearLayout ll_screen_history_list_view;
	/*private TextView tv_last_screened_history_diagnosed;
	private TextView tv_last_screened_history_referred_to;
	private TextView tv_last_screened_history_status;
	private TextView tv_last_screened_history_type_of_treatment;*/

	private Button btn_last_screened_history_close;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.screening_last_screened_history);
		findViews();

	}

	/**
	 * 
	 */
	private void findViews() {
		iv_last_screened_history_previous = (ImageView) findViewById(R.id.iv_last_screened_history_previous);
		iv_last_screened_history_previous.setOnClickListener(this);
		iv_last_screened_history_next = (ImageView) findViewById(R.id.iv_last_screened_history_next);
		iv_last_screened_history_next.setOnClickListener(this);

//		tv_last_screened_history_date = (TextView) findViewById(R.id.tv_last_screened_history_date);
//		tv_last_screened_history_doctor = (TextView) findViewById(R.id.tv_last_screened_history_doctor);
//		tv_last_screened_history_round_no = (TextView) findViewById(R.id.tv_last_screened_history_round_no);
//		tv_last_screened_history_age_at_screening = (TextView) findViewById(R.id.tv_last_screened_history_age_at_screening);
//		tv_last_screened_history_referred = (TextView) findViewById(R.id.tv_last_screened_history_referred);
//		ll_screen_history_list_view=(LinearLayout)findViewById(R.id.ll_screen_history_list_view);
		/*tv_last_screened_history_diagnosed = (TextView) findViewById(R.id.tv_last_screened_history_diagnosed);
		tv_last_screened_history_referred_to = (TextView) findViewById(R.id.tv_last_screened_history_referred_to);
		tv_last_screened_history_status = (TextView) findViewById(R.id.tv_last_screened_history_status);
		tv_last_screened_history_type_of_treatment = (TextView) findViewById(R.id.tv_last_screened_history_type_of_treatment);
*/
		btn_last_screened_history_close = (Button) findViewById(R.id.btn_last_screened_history_close);
		btn_last_screened_history_close.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == iv_last_screened_history_previous) {
		} else if (v == iv_last_screened_history_next) {
		} else if (v == btn_last_screened_history_close) {
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
