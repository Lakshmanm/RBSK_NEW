package nunet.rbsk.info.child;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  Add student Fragment

//* Type    : Activity

//* Description     : Add Student Activity
//* References     :                                                        
//* Author    :promodh.munjeti

//* Created Date       : 28-04-2015
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
import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.screening.ScreeningActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddStudentActivityDialog extends Activity implements
		OnClickListener {

	private static TextView tv_editstu_basicInfo;
	private static TextView tv_editstu_family_details;
	private static TextView tv_editstu_Address;
	private static TextView tv_editstu_associate_inst;
	private static TextView tv_editstu_disability;
	private Button btn_editstu_close;
	// private int selectedInstituteId;
	private LinearLayout ll_add_student;
	private TextView tv_editstu_stuName;
	private FrameLayout frame_container;

	public static boolean[] tabFlags = { false, false, false, false, false };

	@Override
	protected void onStart() {
		super.onStart();
		ScreeningActivity.dialoga.dismiss();
	}
	
	@Override
	protected void onDestroy() {
		setResult(108, null);
		super.onDestroy();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentArr = new Fragment[5];
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.add_student_fragment);
		findViews();
		// selectedInstituteId = Helper.selectedInstituteId;
		tv_editstu_basicInfo.setOnClickListener(this);
		tv_editstu_family_details.setOnClickListener(this);
		tv_editstu_Address.setOnClickListener(this);
		tv_editstu_associate_inst.setOnClickListener(this);
		tv_editstu_disability.setOnClickListener(this);
		btn_editstu_close.setOnClickListener(this);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(tv_editstu_basicInfo);
		}
		
	}
	
	

	// Method to find id of all views
	private void findViews() {
		tv_editstu_basicInfo = (TextView) findViewById(R.id.tv_editstu_basicInfo);
		tv_editstu_family_details = (TextView) findViewById(R.id.tv_editstu_family_details);
		tv_editstu_Address = (TextView) findViewById(R.id.tv_editstu_Address);
		tv_editstu_associate_inst = (TextView) findViewById(R.id.tv_editstu_associate_inst);
		tv_editstu_disability = (TextView) findViewById(R.id.tv_editstu_disability);
		btn_editstu_close = (Button) findViewById(R.id.btn_editstu_close);
		tv_editstu_stuName = (TextView) findViewById(R.id.tv_editstu_stuName);
		ll_add_student = (LinearLayout) findViewById(R.id.ll_add_student);
		ll_add_student.setOnClickListener(this);
		frame_container = (FrameLayout) findViewById(R.id.frame_container);
		frame_container.setOnClickListener(this);
		if (!TextUtils.isEmpty(Helper.childrenObject.getFirstName())) {
			tv_editstu_stuName.setText(Helper.childrenObject.getFirstName()
					+ " " + Helper.childrenObject.getLastName());
		} else {
			tv_editstu_stuName.setText("Add Child");
			tv_editstu_family_details.setOnClickListener(this);
		}
	}

	public static Fragment[] fragmentArr;
	Fragment mFragment;

	public void displayView(View view) {
		// if (count == 0) {
		switch (view.getId()) {
		case R.id.tv_editstu_basicInfo:
			tabFlags[0] = true;
			enableHeaderClick1(tabFlags);
			/* bundle.putInt("InsituteID", selectedInstituteId); */
			updateHeaderColorsStu(R.drawable.headerbg_selectced,
					R.drawable.headerbg, R.drawable.headerbg,
					R.drawable.headerbg, R.drawable.headerbg);

			if (fragmentArr[0] == null)
				mFragment = fragmentArr[0] = new EditStuBasicInfo();
			else
				mFragment = fragmentArr[0];
			/* fragment.setArguments(bundle); */
			break;
		case R.id.tv_editstu_disability:
			// tabFlags[1] = true;//thriveni
			enableHeaderClick1(tabFlags);
			updateHeaderColorsStu(R.drawable.headerbg,
					R.drawable.headerbg_selectced, R.drawable.headerbg,
					R.drawable.headerbg, R.drawable.headerbg);
			if (fragmentArr[1] == null)
				mFragment = fragmentArr[1] = new EditStuDisability();
			else
				mFragment = fragmentArr[1];
			break;
		case R.id.tv_editstu_family_details:
			// tabFlags[2] = true;//thriveni
			enableHeaderClick1(tabFlags);
			updateHeaderColorsStu(R.drawable.headerbg, R.drawable.headerbg,
					R.drawable.headerbg_selectced, R.drawable.headerbg,
					R.drawable.headerbg);
			if (fragmentArr[2] == null)
				mFragment = fragmentArr[2] = new EditStuFamilyDetails();
			else
				mFragment = fragmentArr[2];
			break;
		case R.id.tv_editstu_Address:
			// tabFlags[3] = true;//thriveni
			enableHeaderClick1(tabFlags);
			updateHeaderColorsStu(R.drawable.headerbg, R.drawable.headerbg,
					R.drawable.headerbg, R.drawable.headerbg_selectced,
					R.drawable.headerbg);
			if (fragmentArr[3] == null)
				mFragment = fragmentArr[3] = new EditStuAddress();
			else
				mFragment = fragmentArr[3];

			break;
		case R.id.tv_editstu_associate_inst:
			// tabFlags[4] = true;//thriveni
			enableHeaderClick1(tabFlags);
			updateHeaderColorsStu(R.drawable.headerbg, R.drawable.headerbg,
					R.drawable.headerbg, R.drawable.headerbg,
					R.drawable.headerbg_selectced);
			// Helper.selectedInstituteId = selectedInstituteId;
			if (fragmentArr[4] == null)
				mFragment = fragmentArr[4] = new EditStuAssocInst();
			else
				mFragment = fragmentArr[4];

			break;
		}
		if (mFragment != null) {
			android.app.FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, mFragment).commit();
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		// } else {
		// confirmationAlert(view.getId());
		// }

	}

	public static void enableHeaderClick1(boolean[] tabFlags2) {
		tv_editstu_basicInfo.setEnabled(tabFlags2[0]);
		tv_editstu_disability.setEnabled(tabFlags2[1]);
		tv_editstu_family_details.setEnabled(tabFlags2[2]);
		tv_editstu_Address.setEnabled(tabFlags2[3]);
		tv_editstu_associate_inst.setEnabled(tabFlags2[4]);
	}

	private void updateHeaderColorsStu(int basicInfoHeader, int disability,
			int addressHeader, int staffHeader, int coveredByHeader) {
		tv_editstu_basicInfo.setBackgroundDrawable(getResources().getDrawable(
				basicInfoHeader));
		tv_editstu_disability.setBackgroundDrawable(getResources().getDrawable(
				disability));
		tv_editstu_family_details.setBackgroundDrawable(getResources()
				.getDrawable(addressHeader));
		tv_editstu_Address.setBackgroundDrawable(getResources().getDrawable(
				staffHeader));
		tv_editstu_associate_inst.setBackgroundDrawable(getResources()
				.getDrawable(coveredByHeader));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_editstu_basicInfo:
			displayView(tv_editstu_basicInfo);
			break;
		case R.id.tv_editstu_family_details:
			displayView(tv_editstu_family_details);
			break;
		case R.id.tv_editstu_Address:
			displayView(tv_editstu_Address);
			break;
		case R.id.tv_editstu_associate_inst:
			displayView(tv_editstu_associate_inst);
			break;
		case R.id.tv_editstu_disability:
			displayView(tv_editstu_disability);
			break;
		case R.id.btn_editstu_close:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Data entered will be lost")
					.setTitle("Are you sure you want to close ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									AddStudentActivityDialog.this.finish();
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
			break;
		default:
			break;
		}
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
								AddStudentActivityDialog.this.finish();
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
