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

//*****************************************************************************
//* Name   :  SignOffLocalTreatment.java

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
public class SignOffLocalTreatment extends Activity implements OnClickListener {

    private Button btn_recommendations_close, btn_recommendations_save;
    private EditText et_local_treatment_diagnosis,
            et_local_treatment_medication_given;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);

        setContentView(R.layout.screening_local_treatment);
        findViews();
        Bundle getIntent = getIntent().getExtras();
        position = getIntent.getInt("position");

        et_local_treatment_diagnosis.setText(Helper.childScreeningObj
                .getSignOffModel().getDiagnosis());
        et_local_treatment_medication_given.setText(Helper.childScreeningObj
                .getSignOffModel().getMedicationsGiven());
    }

    private void findViews() {

        et_local_treatment_diagnosis = (EditText) findViewById(R.id.et_local_treatment_diagnosis);
        et_local_treatment_medication_given = (EditText) findViewById(R.id.et_local_treatment_medication_given);

        btn_recommendations_close = (Button) findViewById(R.id.btn_local_treatment_close);
        btn_recommendations_close.setOnClickListener(this);
        btn_recommendations_save = (Button) findViewById(R.id.btn_local_treatment_save);
        btn_recommendations_save.setOnClickListener(this);
    }

    /*
     *
     */
    @Override
    public void onClick(View v) {

        if (v == btn_recommendations_save) {
            Helper.childScreeningObj.getSignOffModel().setDiagnosis(
                    et_local_treatment_diagnosis.getText().toString().trim());
            Helper.childScreeningObj.getSignOffModel().setMedicationsGiven(
                    et_local_treatment_medication_given.getText().toString()
                            .trim());
            Helper.childScreeningObj.getReferrals().get(position)
                    .getHealthConditonReferred().setUpdate(true);
            Helper.childScreeningObj.setScreeningID(6);

            finish();
        } else if (v == btn_recommendations_close) {
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
