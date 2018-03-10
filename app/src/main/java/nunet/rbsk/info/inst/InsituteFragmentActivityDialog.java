/**
 *
 */
package nunet.rbsk.info.inst;

import nunet.rbsk.R;
import nunet.rbsk.SettingsPlanActivity;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.LoginActivity;
import nunet.rbsk.planoffline.PlanOffLineActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  InsituteFragment

//* Type    : Activity for Fragments

//* Description     : Activity for loading the comments
//* References     :
//* Author    :promodh.munjeti

//* Created Date       : 25-04-2015
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

public class InsituteFragmentActivityDialog extends Activity implements
  OnClickListener {

  private TextView tv_editInst_basicInfo;
  private TextView tv_editInst_address;
  private TextView tv_editInst_staff;
  private TextView tv_editInst_coveredby;
  private TextView tv_editInst_signoff;
  private Button btn_editInst_address_close;

  public int selectedInstituteId, index;

  int count = 0;
  private boolean[] isInit;

  @Override
  protected void onStart() {
    super.onStart();
    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        if (PlanOffLineActivity.dialog != null) {
          if (PlanOffLineActivity.dialog.isShowing()) {
            PlanOffLineActivity.dialog.dismiss();
          }
        }

      }
    };
    handler.postDelayed(runnable, 1000);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setFinishOnTouchOutside(false);

    setContentView(R.layout.insitute_header);
    initFragments();
    findViews();

    tv_editInst_basicInfo.setOnClickListener(this);
    tv_editInst_address.setOnClickListener(this);
    tv_editInst_staff.setOnClickListener(this);
    tv_editInst_coveredby.setOnClickListener(this);
    btn_editInst_address_close.setOnClickListener(this);
    tv_editInst_signoff.setOnClickListener(this);
    selectedInstituteId = getIntent().getIntExtra("instituteID", 0);
    index = getIntent().getIntExtra("index", 0);
    if (index == 0) {
      isInit = new boolean[]{true, false, false, false, false};
    } else {
      isInit = new boolean[]{false, false, false, false, true};
    }
    if (savedInstanceState == null) {
      // on first time display view for first nav item
      if (index == 0)
        displayView(tv_editInst_basicInfo);
      else
        displayView(tv_editInst_signoff);
    }

  }

  /**
   * To get the view id's from R.java
   */
  private void findViews() {

    tv_editInst_basicInfo = (TextView) findViewById(R.id.tv_editInst_basicInfo);
    tv_editInst_address = (TextView) findViewById(R.id.tv_editInst_address);
    tv_editInst_staff = (TextView) findViewById(R.id.tv_editInst_staff);
    tv_editInst_coveredby = (TextView) findViewById(R.id.tv_editInst_coveredby);
    tv_editInst_signoff = (TextView) findViewById(R.id.tv_editInst_signoff);
    btn_editInst_address_close = (Button) findViewById(R.id.btn_editInst_address_close);


  }

  /**
   * To change colors of header based on the user selection
   *
   * @param view
   */

  public void displayView(View view) {

    if (count == 0) {


      if (index == 0) {
        switch (view.getId()) {

          case R.id.tv_editInst_basicInfo:
            count++;
            updateHeaderColors(R.drawable.headerbg_selectced,
              R.drawable.headerbg, R.drawable.headerbg,
              R.drawable.headerbg, R.drawable.headerbg);
            break;


        }
        if (fragmentArr[0] != null) {
          android.app.FragmentManager fragmentManager = getFragmentManager();
          fragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentArr[0]).commit();

        } else {
          // error in creating fragment
          Log.e("MainActivity", "Error in creating fragment");
        }
      } else {
        switch (view.getId()) {

          case R.id.tv_editInst_signoff:
            count++;
            updateHeaderColors(R.drawable.headerbg,
              R.drawable.headerbg, R.drawable.headerbg,
              R.drawable.headerbg, R.drawable.headerbg_selectced);
            break;


        }
        if (fragmentArr[4] != null) {
          android.app.FragmentManager fragmentManager = getFragmentManager();
          fragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentArr[4]).commit();

        } else {
          // error in creating fragment
          Log.e("MainActivity", "Error in creating fragment");
        }
      }

    } else {
      confirmationAlert(view.getId());
    }

  }

  Fragment[] fragmentArr = new Fragment[5];

  private void initFragments() {

    for (int i = 0; i < fragmentArr.length; i++) {
      if (i == 0)
        fragmentArr[0] = new EditInstBasicInfo();
      else if (i == 1)
        fragmentArr[1] = new EditInstituteEditAddress();
      else if (i == 2)
        fragmentArr[2] = new EditInstituteStaffInfo();
      else if (i == 3)
        fragmentArr[3] = new EditInstituteCovredBy();
      else if (i == 4)
        fragmentArr[4] = new EditInstituteSignOff();
    }

  }

  public void confirmationAlert(final int viewId) {

    Fragment mFragment = null;

    switch (viewId) {
      case R.id.tv_editInst_basicInfo:

        updateHeaderColors(R.drawable.headerbg_selectced,
          R.drawable.headerbg, R.drawable.headerbg,
          R.drawable.headerbg, R.drawable.headerbg);

        mFragment = fragmentArr[0];
        break;
      case R.id.tv_editInst_address:

        updateHeaderColors(R.drawable.headerbg,
          R.drawable.headerbg_selectced, R.drawable.headerbg,
          R.drawable.headerbg, R.drawable.headerbg);

        mFragment = fragmentArr[1];

        break;
      case R.id.tv_editInst_staff:
        updateHeaderColors(R.drawable.headerbg, R.drawable.headerbg,
          R.drawable.headerbg_selectced, R.drawable.headerbg, R.drawable.headerbg);

        mFragment = fragmentArr[2];
        break;
      case R.id.tv_editInst_coveredby:
        updateHeaderColors(R.drawable.headerbg, R.drawable.headerbg,
          R.drawable.headerbg, R.drawable.headerbg_selectced, R.drawable.headerbg);

        mFragment = fragmentArr[3];
        break;
      case R.id.tv_editInst_signoff:
        updateHeaderColors(R.drawable.headerbg, R.drawable.headerbg,
          R.drawable.headerbg, R.drawable.headerbg, R.drawable.headerbg_selectced);

        mFragment = fragmentArr[4];
        break;

    }
    if (mFragment != null) {
      replaceFragment(mFragment);

    } else {
      Log.e("MainActivity", "Error in creating fragment");
    }
    // }
    // })
    // .setNegativeButton(android.R.string.no,
    // new DialogInterface.OnClickListener() {
    // public void onClick(DialogInterface dialog,
    // int which) {
    // // do nothing
    // }
    // }).setIcon(android.R.drawable.ic_dialog_alert).show();
  }


  public void replaceFragment(Fragment fragment) {

    for (int i = 0; i < fragmentArr.length; i++) {
      if (fragmentArr[i] == fragment) {

        if (!isInit[i]) {
          isInit[i] = true;
          android.app.FragmentManager fragmentManager = getFragmentManager();
          fragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in,
              android.R.animator.fade_out)
            .add(R.id.frame_container, fragmentArr[i])
            .show(fragmentArr[i]).commit();
        } else
          getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in,
              android.R.animator.fade_out)
            .show(fragmentArr[i]).commit();
      } else if (fragmentArr[i] != null)

        getFragmentManager().beginTransaction()
          // .setCustomAnimations(android.R.animator.fade_out,
          // android.R.animator.fade_in)
          .hide(fragmentArr[i]).commit();
    }

  }

  /**
   * To change colors of header based on the user selection
   *
   * @param headerbg
   * @param headerbgSelectced
   * @param headerbg2
   * @param headerbg3
   */
  public void updateHeaderColors(int basicInfoHeader, int addressHeader,
                                 int staffHeader, int coveredByHeader, int signoffHeader) {

    tv_editInst_basicInfo.setBackgroundDrawable(getResources().getDrawable(
      basicInfoHeader));
    tv_editInst_address.setBackgroundDrawable(getResources().getDrawable(
      addressHeader));
    tv_editInst_staff.setBackgroundDrawable(getResources().getDrawable(
      staffHeader));
    tv_editInst_coveredby.setBackgroundDrawable(getResources().getDrawable(
      coveredByHeader));
    tv_editInst_signoff.setBackgroundDrawable(getResources().getDrawable(
      signoffHeader));
  }

  /**
   * onclick listener for the views
   */
  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.tv_editInst_basicInfo:
        displayView(tv_editInst_basicInfo);
        break;
      case R.id.tv_editInst_address:
        displayView(tv_editInst_address);
        break;
      case R.id.tv_editInst_staff:
        displayView(tv_editInst_staff);
        break;
      case R.id.tv_editInst_coveredby:
        displayView(tv_editInst_coveredby);
        break;
      case R.id.tv_editInst_signoff:
        displayView(tv_editInst_signoff);
        break;
      case R.id.btn_editInst_address_close:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data entered will be lost")
          .setTitle("Are you sure you want to close ?")
          .setCancelable(false)
          .setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog,
                                  int id) {
                InsituteFragmentActivityDialog.this
                  .finish();
              }
            })
          .setNegativeButton("No",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog,
                                  int id) {
                dialog.cancel();
              }
            });
        AlertDialog alert = builder.create();
        alert.show();
        // finish();
        break;

      default:
        break;
    }
  }

	/*
   * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.actionsoverlay, menu); MenuItem item =
	 * menu.findItem(R.id.menu_userName); SharedPreferences sharedpreferences =
	 * getSharedPreferences("RbskPref", Context.MODE_PRIVATE);
	 * item.setTitle(sharedpreferences.getString("userKey", "")); return true; }
	 */

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    switch (item.getItemId()) {
      case R.id.settings:
        Intent in = new Intent(InsituteFragmentActivityDialog.this,
          SettingsPlanActivity.class);
        startActivity(in);
        break;
      case R.id.logout:
        new AlertDialog.Builder(this)
          .setTitle("Confirmation Alert")
          .setMessage(
            "Are you sure you want to Logout from the Application")
          .setPositiveButton(android.R.string.yes,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog,
                                  int which) {
                Intent logout = new Intent(
                  InsituteFragmentActivityDialog.this,
                  LoginActivity.class);
                startActivity(logout);

              }
            })
          .setNegativeButton(android.R.string.no,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog,
                                  int which) {
                // do nothing
              }
            }).setIcon(android.R.drawable.ic_dialog_alert)
          .show();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
  @Override
  public void onBackPressed() {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Data entered will be lost")
      .setTitle("Are you sure you want to close ?")
      .setCancelable(false)
      .setPositiveButton("Yes",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            InsituteFragmentActivityDialog.this.finish();
          }
        })
      .setNegativeButton("No", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          dialog.cancel();
        }
      });
    AlertDialog alert = builder.create();
    alert.show();
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
