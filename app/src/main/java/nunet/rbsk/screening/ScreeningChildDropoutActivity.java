//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening;

import java.util.Calendar;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  ScreeningChildDropout.java

//* Type    :

//* Description     :
//* References     :
//* Author    : promodh.munjeti

//* Created Date       :  Jun 11, 2015
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
public class ScreeningChildDropoutActivity extends Activity implements
  OnClickListener {
  /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
  private EditText et_screening_dropout_date;
  // private EditText et_screening_dropout_comments;
  private Button btn_screening_dropout_close;
  private Button btn_screening_dropout_save;
  private TextView tv_dropout_title;
  private DBHelper dbh;
  private int droputValue = 0, childID = 0;
  ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setFinishOnTouchOutside(false);
    setContentView(R.layout.screening_dropout);
    findViews();
    dbh = DBHelper.getInstance(this);
    droputValue = NumUtil.IntegerParse.parseInt(getIntent().getStringExtra(
      "value"));
    childID = NumUtil.IntegerParse.parseInt(getIntent().getStringExtra(
      "childID"));
    if (droputValue == 2) {// drop out
      tv_dropout_title.setText("Drop Out");
    } else if (droputValue == 3) {// transffeered
      tv_dropout_title.setText("Transferred");
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    ScreeningBasicInfoFragment.spn_screening_basic_student_active
      .setSelection(0);
  }

  /**
   *
   */
  private void findViews() {
    et_screening_dropout_date = (EditText) findViewById(R.id.et_screening_dropout_date);
    // et_screening_dropout_comments = (EditText)
    // findViewById(R.id.et_screening_dropout_comments);
    btn_screening_dropout_close = (Button) findViewById(R.id.btn_screening_dropout_close);
    btn_screening_dropout_close.setOnClickListener(this);
    btn_screening_dropout_save = (Button) findViewById(R.id.btn_screening_dropout_save);
    btn_screening_dropout_save.setOnClickListener(this);

    et_screening_dropout_date.setOnClickListener(this);
    et_screening_dropout_date.setText(Helper.getTodayDate("yyyy-MM-dd"));
    tv_dropout_title = (TextView) findViewById(R.id.tv_dropout_title);
  }

  /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
  @Override
  public void onClick(View v) {
    if (v == et_screening_dropout_date) {
      DialogFragment newFragment = new SelectDateFragment();
      newFragment.show(getFragmentManager(), "DatePicker");
    } else if (v == btn_screening_dropout_save) {
      childStatusUpdateToDB();
      ScreeningActivity.resumeFlag = true;
      finish();
      Helper.showShortToast(this, "Child Status Updated Successfully");
    } else if (v == btn_screening_dropout_close) {
      ScreeningBasicInfoFragment.spn_screening_basic_student_active
        .setSelection(0);
      finish();
    }
  }

  public void childStatusUpdateToDB() {
    // boolean statusUpdated =
    dbh.updateROWByValues(this, "children",
      new String[]{"ChildrenStatusID"}, new String[]{droputValue
        + "".trim()}, new String[]{"LocalChildrenID"},
      new String[]{childID + "".trim()});
    // if (statusUpdated)
    // Helper.childrenObject.setChildrenStatusID(droputValue);

//    final ListView localListView = ScreeningActivity.ll_screening_list_students;
//    final int lvpos = ScreeningActivity.listSelectedPosition + 1;// Modified
//    final View v = localListView.getChildAt(lvpos);
//    if (ScreeningActivity.childrenList.size() > ScreeningActivity.listSelectedPosition + 1) {
//      int nextPosition = (ScreeningActivity.listSelectedPosition + 1);
//      localListView
//        .performItemClick(
//          localListView
//            .getAdapter()
//            .getView(
//              nextPosition,
//              null,
//              null),
//          nextPosition,
//          localListView
//            .getAdapter()
//            .getItemId(
//              nextPosition));
//
//      localListView.getAdapter().getView(
//        lvpos, v, localListView);
//    }

  }

  public class SelectDateFragment extends DialogFragment implements
    DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar calendar = Calendar.getInstance();
      int yy = calendar.get(Calendar.YEAR);
      int mm = calendar.get(Calendar.MONTH);
      int dd = calendar.get(Calendar.DAY_OF_MONTH);
      return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
      populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
      et_screening_dropout_date.setText(day + "/" + month + "/" + year);
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
