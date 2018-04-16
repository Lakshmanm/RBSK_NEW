//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.screening;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Recommendations;

//*****************************************************************************
//* Name   :  SignoffRecommendations.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

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
public class SignoffRecommendations extends Activity implements OnClickListener {
	private EditText et_recommendation_diet, et_recommendation_personel_hygine,
			et_recommendation_oral_hygiene,
			et_recommendation_prescribed_medications, et_recommendation_others;
	private Button btn_recommendations_close, btn_recommendations_save;
	private int position;
	Recommendations recommendationsObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);

		setContentView(R.layout.screening_recommendations);

		findViews();
		Bundle getIntent = getIntent().getExtras();
		position = getIntent.getInt("position");
		recommendationsObj = Helper.childScreeningObj.getSignOffModel().getRecommendations();
		
		et_recommendation_diet.setText(recommendationsObj.getDiet());
		et_recommendation_personel_hygine.setText(recommendationsObj.getPersonalHygine());
		et_recommendation_oral_hygiene.setText(recommendationsObj.getOralHygine());
		et_recommendation_prescribed_medications.setText(recommendationsObj.getMedications());
		et_recommendation_others.setText(recommendationsObj.getOthers());
	}

	/**
	 * 
	 */
	private void findViews() {
		
		et_recommendation_diet = (EditText) findViewById(R.id.et_recommendation_diet);
		et_recommendation_personel_hygine = (EditText) findViewById(R.id.et_recommendation_personel_hygine);
		et_recommendation_oral_hygiene = (EditText) findViewById(R.id.et_recommendation_oral_hygiene);
		et_recommendation_prescribed_medications = (EditText) findViewById(R.id.et_recommendation_prescribed_medications);
		et_recommendation_others = (EditText) findViewById(R.id.et_recommendation_others);

		btn_recommendations_close = (Button) findViewById(R.id.btn_recommendations_close);
		btn_recommendations_close.setOnClickListener(this);
		btn_recommendations_save = (Button) findViewById(R.id.btn_recommendations_save);
		btn_recommendations_save.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		if (v == btn_recommendations_save) {
			recommendationsObj.setDiet(et_recommendation_diet.getText()
					.toString().trim());
			recommendationsObj
					.setPersonalHygine(et_recommendation_personel_hygine
							.getText().toString().trim());
			recommendationsObj.setOralHygine(et_recommendation_oral_hygiene
					.getText().toString().trim());
			recommendationsObj
					.setMedications(et_recommendation_prescribed_medications
							.getText().toString().trim());
			recommendationsObj.setOthers(et_recommendation_others.getText()
					.toString().trim());
			Helper.childScreeningObj.getSignOffModel().setRecommendations(recommendationsObj);
			Helper.childScreeningObj.getReferrals().get(position).getHealthConditonReferred().setUpdate(true);
			SignoffRecommendations.this.finish();
		} else if (v == btn_recommendations_close) {
			SignoffRecommendations.this.finish();
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
