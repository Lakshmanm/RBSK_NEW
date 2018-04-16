//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.screening;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nunet.utils.RoundedImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import nunet.adapter.CustomStudentAdapter;
import nunet.animation.CollapseAnimation;
import nunet.animation.ExpandAnimation;
import nunet.rbsk.BaseActivity;
import nunet.rbsk.NoRecords;
import nunet.rbsk.R;
import nunet.rbsk.dashboard.DashBoardActivity;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.info.child.AddStudentActivityDialog;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.ChildrenInstitutes;
import nunet.rbsk.screening.cv.ScreeningVitalsFragment;
import nunet.rbsk.screening.mh.ScreeningMedicalHistory;

//*****************************************************************************
//* Name   :  ScreeningActivity.java

//* Type    :

//* Description     :
//* References     :
//* Author    : kiruthika.ganesan

//* Created Date       :  21-May-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG
//*****************************************************************************
//* Ver        Date                Code Review By            Observations
//3.0			22-05-2015			Deepika					Remove unused imports
//3.0			22-05-2015			Deepika					Provide the access specifies for all variables
//3.0			22-05-2015			Deepika					Update comments for all methods
//3.0           22-05-2015          Kiruthika               Action Bar back button is not working.
//*****************************************************************************
public class ScreeningActivity extends BaseActivity implements OnClickListener {

    private LinearLayout ll_infoContainer;
    private LinearLayout ll_studentContainer;

    private ImageView drawerImage;

    private Button btn_screening_list_filter;
    private Button btn_screening_list_add_child;

    private EditText et_screening_list_student_search;
    public static ListView ll_screening_list_students;
    public static LinearLayout ll_student_list_hide;

    public static TextView tv_screening_basicinfo, tv_medical_history,
            tv_capture_vitals, tv_screen, tv_referral, tv_signoff;

    // For Navigation Drawer
    private FrameLayout.LayoutParams menuPanelParameters;
    private FrameLayout.LayoutParams slidingPanelParameters;
    private DisplayMetrics metrics;
    private int panelWidth;
    private boolean isExpanded = false;
    public CustomStudentAdapter adapter;
    public int instituteID;
    public long locInsScreeningDetailID;
    public static ArrayList<Children> childrenList;
    private Fragment fragment = null;
    public static boolean[] tabFlags = {false, false, false, false, false,
            false};
    public static View view_basicinfo, view_medical_history,
            view_capture_vitals, view_screen, view_referral, view_signoff;
    public static int listSelectedPosition = 0;
    private DBHelper dbh;
    public static HashMap<String, Integer> filterData = new HashMap<String, Integer>();
    public static boolean resumeFlag = false;
    public Button btn_screening_list_searchGo;
    private int TotalStudentsCount;

    ScreeningActivity currentCtx;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentCtx = this;
        setContentView(R.layout.screening_main);

        Bundle mBundle = getIntent().getExtras();
        if (!mBundle.containsKey("ApplyFilter")) {
            filterData.put("StatusID", null);
            filterData.put("ClassID", null);
            filterData.put("SectionID", null);
            filterData.put("GenderID", null);
        }


        dbh = DBHelper.getInstance(this);

        findViews();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar_home, null);
        actionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        actionBar.setIcon(R.drawable.icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#815CC5")));
        TextView tv_schoolName = (TextView) mCustomView
                .findViewById(R.id.tv_schoolName);

        instituteID = mBundle.getInt("InstituteID", 0);
        locInsScreeningDetailID = mBundle.getLong("LocInsScreeningDetailID", 0);

        Helper.selectedLocalInstituteID = mBundle.getInt("LocalInstituteID", 0);

        RBSKCalenarYearID = mBundle.getString("RBSKCalenarYearID");
        screeningRoundID = mBundle.getString("ScreeningRoundID");
        TotalStudentsCount = NumUtil.IntegerParse.parseInt(mBundle
                .getString("TotalStudentsCount"));
        String InstituteName = mBundle.getString("InstituteName");

        tv_schoolName.setText(InstituteName);

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = inflator.inflate(R.layout.screening_student_list,
                null);
        et_screening_list_student_search = (EditText) view
                .findViewById(R.id.et_screening_list_student_search);
        // btn_screening_list_searchGo = (Button) view
        // .findViewById(R.id.btn_screening_list_searchGo);

        et_screening_list_student_search
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (MyRunnable != null)
                            MyRunnable.cancle();
                        MyRunnable = new MyRunnable(s.toString());
                        new Handler().postDelayed(MyRunnable, 1200);
                    }
                });

        // btn_screening_list_searchGo.setOnClickListener(this);
        // et_screening_list_student_search.setFocusable(false);
        ll_screening_list_students = (ListView) view
                .findViewById(R.id.ll_screening_list_students);
        ll_student_list_hide = (LinearLayout) view
                .findViewById(R.id.ll_student_list_hide);
        btn_screening_list_add_child = (Button) view
                .findViewById(R.id.btn_screening_list_add_child);
        btn_screening_list_filter = (Button) view
                .findViewById(R.id.btn_screening_list_filter);
        btn_screening_list_filter.setOnClickListener(this);
        btn_screening_list_add_child.setOnClickListener(this);
        ll_studentContainer.addView(view);

        // For Navigation drawer
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        panelWidth = (int) ((metrics.widthPixels) * 0.40);

        menuPanelParameters = (FrameLayout.LayoutParams) ll_infoContainer
                .getLayoutParams();
        menuPanelParameters.width = panelWidth;

        slidingPanelParameters = (FrameLayout.LayoutParams) ll_studentContainer
                .getLayoutParams();
        slidingPanelParameters.width = metrics.widthPixels;
        ll_studentContainer.setLayoutParams(menuPanelParameters);
        ll_infoContainer.setLayoutParams(slidingPanelParameters);

        // sand: commented getStudentDataFromDB; already code existed in
        // onResume
        // getStudentDataFromDB(instituteID, et_screening_list_student_search
        // .getText().toString().trim());
        // new ExpandAnimation(ll_infoContainer, panelWidth,
        // Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
        // 0.40f, 0, 0.0f, 0, 0.0f);
        isExpanded = false;

        drawerImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isExpanded && !prevetnClick) {
                    prevetnClick = true;

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            prevetnClick = false;
                        }
                    }, 2000);

                    // for (int i = 0; i < ll_student_list_hide.getChildCount();
                    // i++) {
                    // View view = ll_student_list_hide.getChildAt(i);
                    // view.setVisibility(View.VISIBLE); // Or whatever you
                    // // want to do with
                    // // the view.
                    // }
                    ll_student_list_hide.animate().alpha(1f).setDuration(700)
                            .start();
                    isExpanded = true;
                    // Expand
                    et_screening_list_student_search
                            .setVisibility(View.VISIBLE);
                    new ExpandAnimation(ll_infoContainer, panelWidth,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.40f, 0, 0.0f, 0, 0.0f);

                    getStudentDataFromDB(instituteID,
                            et_screening_list_student_search.getText()
                                    .toString().trim());
                    // getStudentDataFromDB(instituteID);

                } else {
                    // for (int i = 0; i < ll_student_list_hide.getChildCount();
                    // i++) {
                    // View view = ll_student_list_hide.getChildAt(i);
                    // view.setVisibility(View.GONE); // Or whatever you want
                    // // to do with the view.
                    // }
                    ll_student_list_hide.animate().alpha(.1f).setDuration(300)
                            .start();
                    isExpanded = false;
                    // getStudentDataFromDB(instituteID);
                    // Collapse
                    new CollapseAnimation(ll_infoContainer, panelWidth,
                            TranslateAnimation.RELATIVE_TO_SELF, 0.40f,
                            TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f,
                            0, 0.0f);

                }

            }

        });

        // if (savedInstanceState == null) {
        // // on first time display view for first nav item
        // displayView(tv_screening_basicinfo);
        // }
        resumeFlag = true;
        if (childrenList != null)
            childrenList.clear();

    }

    boolean prevetnClick = false;
    MyRunnable MyRunnable;


    private class MyRunnable implements Runnable {
        String mString;

        MyRunnable(String s) {
            super();
            this.mString = s;
        }

        boolean isCanceld = false;

        public void cancle() {
            isCanceld = true;
        }

        @Override
        public void run() {
            if (isCanceld)
                return;

            noMoreUpdate = false;

            if (mString.length() >= 3 || TextUtils.isEmpty(mString)) {
                startingRang = 0;
                rangeCount = 1;
                mArrayListRang.clear();
                // if (childrenList != null)
                // childrenList = new ArrayList<Children>();
                childrenList.clear();
                if (adapter != null)
                    adapter.notifyDataSetChanged();

                getStudentDataFromDB(instituteID, mString.trim());
            }
        }
    }

    public static boolean performClickOnZero = false;

    @Override
    protected void onPause() {
        super.onPause();
        performClickOnZero = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (resumeFlag) {
            //listSelectedPosition = 0;
            startingRang = 0;
            rangeCount = 1;
            if (mArrayListRang != null)
                mArrayListRang.clear();
            noMoreUpdate = false;

            if (childrenList != null)
                childrenList.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
            getStudentDataFromDB(instituteID, et_screening_list_student_search
                    .getText().toString().trim());

            final ListView localListView = ll_screening_list_students;
            final int lvpos = listSelectedPosition + 1;// Modified
            final View v = localListView.getChildAt(lvpos);
            if (childrenList.size() > (listSelectedPosition + 1)) {
                int nextPosition = (listSelectedPosition + 1);
                localListView
                        .performItemClick(
                                localListView
                                        .getAdapter()
                                        .getView(
                                                nextPosition,
                                                null,
                                                null),
                                nextPosition,
                                localListView
                                        .getAdapter()
                                        .getItemId(
                                                nextPosition));

                localListView.getAdapter().getView(
                        lvpos, v, localListView);
            }


        }
        if (adapter != null && performClickOnZero) {
            adapter.performClick(0);
        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void enableHeaderClick(boolean[] tabFlags2) {
        tv_screening_basicinfo.setEnabled(tabFlags2[0]);
        tv_medical_history.setEnabled(tabFlags2[1]);
        tv_capture_vitals.setEnabled(tabFlags2[2]);
        tv_screen.setEnabled(tabFlags2[3]);
        tv_referral.setEnabled(tabFlags2[4]);
        tv_signoff.setEnabled(tabFlags2[4]);
    }

    /**
     * to display views
     */
    public void displayView(View v, Context context) {
        Bundle bundle = new Bundle();
    Helper.showProgressDialog(ScreeningActivity.this);
        if (childrenList == null || childrenList.size() == 0) {
            if (Helper.progressDialog.isShowing()) {
                Helper.progressDialog.dismiss();
            }
            return;
        }
        switch (v.getId()) {
            case R.id.tv_screening_basicinfo:

                if (tabFlags[0]) {
                    updateHeaderColorsScreening(R.drawable.headerbg_selectced,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg);
                    bundle.putInt("ChildrenID", CustomStudentAdapter.childID);
                    fragment = new ScreeningBasicInfoFragment();
                    fragment.setArguments(bundle);
                }

                break;
            case R.id.tv_medical_history:
                if (tabFlags[1]) {
                    updateHeaderColorsScreening(R.drawable.headerbg,
                            R.drawable.headerbg_selectced, R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg);
                    fragment = new ScreeningMedicalHistory();
                }
                break;
            case R.id.tv_capture_vitals:
                if (tabFlags[2]) {
                    updateHeaderColorsScreening(R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg_selectced,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg);
                    fragment = new ScreeningVitalsFragment();
                }
                break;
            case R.id.tv_screen:
                if (tabFlags[3]) {
                    updateHeaderColorsScreening(R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg_selectced, R.drawable.headerbg,
                            R.drawable.headerbg);
                    fragment = new ScreeningExaminations();
                }
                break;
            case R.id.tv_referral:
                if (tabFlags[4]) {
                    updateHeaderColorsScreening(R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg_selectced,
                            R.drawable.headerbg);
                    fragment = new ScreeningSummary();
                }
                break;
            case R.id.tv_signoff:
                if (tabFlags[5]) {
                    updateHeaderColorsScreening(R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg, R.drawable.headerbg,
                            R.drawable.headerbg_selectced);
                    fragment = new ScreeningSignOff();
                }
                break;
            default:
                break;

        }

        if (fragment != null) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            resumeFlag = false;
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commitAllowingStateLoss();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
//    if (dialoga.isShowing()) {
//      dialoga.dismiss();
//    }
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (Helper.progressDialog.isShowing()) {
                    Helper.progressDialog.dismiss();
                }
            }
        };
        handler.postDelayed(runnable, 500);
    }

    /**
     * to data from DB
     */

    // Sand: added thread and ;adapter notify; added paging concept
    public int rangeCount = 1;
    public static int startingRang = 0;
    public boolean isLoading = false;
    public View inflateLoading;
    public boolean noMoreUpdate = false;

    public static ArrayList<Integer> mArrayListRang = new ArrayList<Integer>();
    private String RBSKCalenarYearID;
    private String screeningRoundID;

    public void getStudentDataView(View inflate) {
        if (isLoading == true)
            return;

        inflateLoading = inflate;
        if (inflateLoading != null)
            inflateLoading.setVisibility(View.VISIBLE);
        isLoading = true;
        if (rangeCount == 1) {
            rangeCount = 30;
            startingRang = 1;
        } else

            startingRang = startingRang + rangeCount;

        getStudentDataFromDB(instituteID, et_screening_list_student_search
                .getText().toString().trim());

    }

    public void getStudentDataFromDBFromsignof(final int instId,
                                               final String searchString) {

//        if (mArrayListRang.contains(startingRang) || noMoreUpdate) {
//            if (inflateLoading != null)
//                inflateLoading.setVisibility(View.INVISIBLE);
//            return;
//        }
//        if (startingRang == 0)
//            CustomStudentAdapter.lastSelectedPosition = -1;
//        mArrayListRang.add(startingRang);

        // if (childrenList == null) {
        childrenList = new ArrayList<Children>();
        //   }

        new Thread(new Runnable() {

            @Override
            public void run() {

                String query_students = "select u.FirstName,u.MiddleName,u.LastName, u.DateOfBirth,u.GenderID,c.MCTSID,c.ChildrenStatusID, "
                        + "(select ChildrenScreenStatusID from childrenscreening cs "
                        + "inner join institutescreeningdetails isd on isd.localinstitutescreeningdetailid=cs.localinstitutescreeningdetailid "
                        + "inner join institutescreening ins on ins.localinstitutescreeningid=isd.localinstitutescreeningid "
                        + "where RBSKCalendarYearID='"
                        + RBSKCalenarYearID
                        + "' and SCREENINGROUNDID='"
                        + screeningRoundID
                        + "' and cs.LocalChildrenID =c.LocalChildrenID ) as ChildrenScreenStatusID "
                        + ",c.LocalChildrenID,u.LocalUserID,c.LocalInstituteID,"
                        + " i.InstituteTypeId, c.ClassID, c.SectionID from "
                        + "users u inner join children c on c.LocalUserID=u.LocalUserID "
                        + "inner join institutes i on i.LocalInstituteID=c.LocalInstituteID "
                        + "where u.IsDeleted!=1 AND i.IsDeleted!=1 AND c.isdeleted !=1 "
                        + "AND i.InstituteID='"
                        + instId
                        + "' and c.ChildrenStatusID='1'";

                Integer ClassID = filterData.get("ClassID");
                if (ClassID != null)
                    query_students += " and c.ClassID='" + ClassID + "' ";

                Integer SectionID = filterData.get("SectionID");
                if (SectionID != null)
                    query_students += " and c.SectionID='" + SectionID + "' ";

                Integer GenderID = filterData.get("GenderID");
                if (GenderID != null)
                    query_students += " and u.GenderID='" + GenderID + "' ";

                Integer ChildrenScreenStatusID = filterData.get("StatusID");
                if (ChildrenScreenStatusID != null
                        && ChildrenScreenStatusID.intValue() != 2)
                    query_students += " and ChildrenScreenStatusID="
                            + ChildrenScreenStatusID;

                if (searchString.length() > 0) {
                    query_students += " and (u.firstname like '%" + searchString
                            + "%' or u.lastname like '%" + searchString + "%')"
                            + " order by lower(firstname) limit " + startingRang
                            + " , " + rangeCount;
                } else {
                    query_students += " order by lower(firstname) limit " + startingRang
                            + " , " + rangeCount;
                }


                Log.e("array pos", startingRang + " , " + rangeCount);
                Log.w("array pos", query_students);
                // --ChildrenStatusID=1 means active student
                final Cursor studentCur = dbh.getCursorData(ScreeningActivity.this,
                        query_students);
                if (studentCur != null)
                    Log.e("Cursor pos", "" + studentCur.getCount());
                setToChildrenModel(studentCur, 1);

            }
        }).start();

    }


    public void getStudentDataFromDB(final int instId,
                                     final String searchString) {

        if (mArrayListRang.contains(startingRang) || noMoreUpdate) {
            if (inflateLoading != null)
                inflateLoading.setVisibility(View.INVISIBLE);
            return;
        }
        if (startingRang == 0)
            CustomStudentAdapter.lastSelectedPosition = -1;
        mArrayListRang.add(startingRang);

        if (childrenList == null) {
            childrenList = new ArrayList<Children>();
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                String query_students = "select u.FirstName,u.MiddleName,u.LastName, u.DateOfBirth,u.GenderID,c.MCTSID,c.ChildrenStatusID, "
                        + "(select ChildrenScreenStatusID from childrenscreening cs "
                        + "inner join institutescreeningdetails isd on isd.localinstitutescreeningdetailid=cs.localinstitutescreeningdetailid "
                        + "inner join institutescreening ins on ins.localinstitutescreeningid=isd.localinstitutescreeningid "
                        + "where RBSKCalendarYearID='"
                        + RBSKCalenarYearID
                        + "' and SCREENINGROUNDID='"
                        + screeningRoundID
                        + "' and cs.LocalChildrenID =c.LocalChildrenID ) as ChildrenScreenStatusID "
                        + ",c.LocalChildrenID,u.LocalUserID,c.LocalInstituteID,"
                        + " i.InstituteTypeId, c.ClassID, c.SectionID from "
                        + "users u inner join children c on c.LocalUserID=u.LocalUserID "
                        + "inner join institutes i on i.LocalInstituteID=c.LocalInstituteID "
                        + "where u.IsDeleted!=1 AND i.IsDeleted!=1 AND c.isdeleted !=1 "
                        + "AND i.InstituteID='"
                        + instId
                        + "' and c.ChildrenStatusID='1'";

                Integer ClassID = filterData.get("ClassID");
                if (ClassID != null)
                    query_students += " and c.ClassID='" + ClassID + "' ";

                Integer SectionID = filterData.get("SectionID");
                if (SectionID != null)
                    query_students += " and c.SectionID='" + SectionID + "' ";

                Integer GenderID = filterData.get("GenderID");
                if (GenderID != null)
                    query_students += " and u.GenderID='" + GenderID + "' ";

                Integer ChildrenScreenStatusID = filterData.get("StatusID");
                if (ChildrenScreenStatusID != null
                        && ChildrenScreenStatusID.intValue() != 2)
                    query_students += " and ChildrenScreenStatusID="
                            + ChildrenScreenStatusID;

                if (searchString.length() > 0) {
                    query_students += " and (u.firstname like '%" + searchString
                            + "%' or u.lastname like '%" + searchString + "%')"
                            + " order by lower(firstname) limit " + startingRang
                            + " , " + rangeCount;
                } else {
                    query_students += " order by lower(firstname) limit " + startingRang
                            + " , " + rangeCount;
                }


                Log.e("array pos", startingRang + " , " + rangeCount);
                Log.w("array pos", query_students);
                // --ChildrenStatusID=1 means active student
                final Cursor studentCur = dbh.getCursorData(ScreeningActivity.this,
                        query_students);
                if (studentCur != null)
                    Log.e("Cursor pos", "" + studentCur.getCount());
                setToChildrenModel(studentCur, 0);

            }
        }).start();

    }

    /**
     * to set data to children
     */
    private void setToChildrenModel(final Cursor studentCur, final int index) {
        final ArrayList<Children> childCopy = new ArrayList<Children>();

        if (studentCur != null) {
            try {
                final int LocalChildrenID = studentCur
                        .getColumnIndex("LocalChildrenID");
                final int MCTSID = studentCur.getColumnIndex("MCTSID");

                final int LocalInstituteID = studentCur
                        .getColumnIndex("LocalInstituteID");
                final int FirstName = studentCur.getColumnIndex("FirstName");
                final int LastName = studentCur.getColumnIndex("LastName");
                final int MiddleName = studentCur.getColumnIndex("MiddleName");
                final int DateOfBirth = studentCur
                        .getColumnIndex("DateOfBirth");
                final int GenderID = studentCur.getColumnIndex("GenderID");
                final int ClassID = studentCur.getColumnIndex("ClassID");
                final int SectionID = studentCur.getColumnIndex("SectionID");
                final int ChildrenScreenStatusID = studentCur
                        .getColumnIndex("ChildrenScreenStatusID");
                final int ChildrenStatusID = studentCur
                        .getColumnIndex("ChildrenStatusID");

                if (studentCur.moveToFirst()) {
                    do {
                        Children childModelObj = new Children();

                        String mScreeingStatusID = studentCur
                                .getString(ChildrenScreenStatusID);

                        if (!TextUtils.isEmpty(mScreeingStatusID)) {
                            childModelObj
                                    .setChildScreenStatusID(NumUtil.IntegerParse
                                            .parseInt(mScreeingStatusID));
                        } else {
                            // if null children is not screened
                            childModelObj.setChildScreenStatusID(2);
                        }

                        Integer ChildrenScreenStatusIDVal = filterData
                                .get("StatusID");
                        if (ChildrenScreenStatusIDVal != null
                                && ChildrenScreenStatusIDVal.intValue() == 2) {

                            if (childModelObj.getChildScreenStatusID() != 2)
                                continue;
                        }

                        childModelObj.setChildrenStatusID(NumUtil.IntegerParse
                                .parseInt(studentCur
                                        .getString(ChildrenStatusID)));

                        ChildrenInstitutes childInstModel = new ChildrenInstitutes();

                        childModelObj
                                .setChildrenID(NumUtil.IntegerParse
                                        .parseInt(studentCur
                                                .getString(LocalChildrenID)));

                        childModelObj
                                .setChildimage(getiImageofChild(ScreeningActivity.this, childModelObj
                                        .getChildrenID()));

                        childModelObj.setMCTSID(studentCur.getString(MCTSID));

                        childInstModel.setInstituteID(NumUtil.IntegerParse
                                .parseInt(studentCur
                                        .getString(LocalInstituteID)));

                        childModelObj.setFirstName(studentCur
                                .getString(FirstName));

                        childModelObj.setLastName(studentCur
                                .getString(LastName));

                        childModelObj.setMiddleName(studentCur
                                .getString(MiddleName));

                        childModelObj.setDateOfBirth(studentCur
                                .getString(DateOfBirth));
                        childModelObj.setGenderID(NumUtil.IntegerParse
                                .parseInt(studentCur.getString(GenderID)));

                        ChildrenInstitutes childInstitute = new ChildrenInstitutes();

                        String mClassID = studentCur.getString(ClassID);
                        if (!TextUtils.isEmpty(mClassID)) {
                            childInstitute.setClassID(NumUtil.IntegerParse
                                    .parseInt(mClassID));

                            String mSectionID = studentCur.getString(SectionID);
                            if (!TextUtils.isEmpty(mSectionID)) {
                                childInstitute
                                        .setSectionID(NumUtil.IntegerParse
                                                .parseInt(mSectionID));
                            }
                        }

                        childModelObj.setChildrenInsitute(childInstitute);
                        childModelObj.setChildrenInsitute(childInstModel);

                        childCopy.add(childModelObj);

                    } while (studentCur.moveToNext());
                }
            } finally {
                studentCur.close();
            }
        } else {

            noMoreUpdate = !(startingRang <= TotalStudentsCount);
            if (!noMoreUpdate) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }
                });

            }
        }
        if (currentCtx != null)
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    isLoading = false;
                    if (inflateLoading != null)
                        inflateLoading.setVisibility(View.INVISIBLE);
                    // Integer StatusID = filterData.get("StatusID");
                    synchronized (childrenList) {
                        for (Children children : childCopy) {
                            // if (StatusID != null) {
                            // if (children.getChildScreenStatusID() != StatusID
                            // .intValue()) {
                            // continue;
                            // }
                            // }
                            childrenList.add(children);
                        }
                    }
                    if (childrenList.size() != 0) {

                        if (adapter != null) {
                            if (ll_screening_list_students.getAdapter() == null)
                                ll_screening_list_students.setAdapter(adapter);
                            adapter.notifyDataSetChanged(childrenList);
                            if (index == 0) {
                                if (childrenList.size() == 1) {
                                    adapter.performClick(0);
                                }
                            } else {
                                android.app.FragmentManager fragmentManager = getFragmentManager();

                                updateHeaderColorsScreening(R.drawable.headerbg_selectced,
                                        R.drawable.headerbg, R.drawable.headerbg,
                                        R.drawable.headerbg, R.drawable.headerbg,
                                        R.drawable.headerbg);
                                Bundle bundle = new Bundle();
                                bundle.putInt("ChildrenID", CustomStudentAdapter.childID);
                                fragment = new ScreeningBasicInfoFragment();
                                fragment.setArguments(bundle);
                                resumeFlag = false;
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame_container, fragment).commitAllowingStateLoss();

                            }


                        } else {
                            adapter = new CustomStudentAdapter(
                                    ScreeningActivity.this, childrenList);
                            ll_screening_list_students.setAdapter(adapter);
                            ll_screening_list_students
                                    .setFocusableInTouchMode(true);
                            ll_screening_list_students
                                    .setOnItemClickListener(adapter);

                            ll_screening_list_students.performItemClick(
                                    ll_screening_list_students.getAdapter()
                                            .getView(listSelectedPosition,
                                                    null, null),
                                    listSelectedPosition,
                                    ll_screening_list_students.getAdapter()
                                            .getItemId(listSelectedPosition));

                        }
                    } else {
                        ll_screening_list_students.setAdapter(null);
                        // if (fragment != null) {
                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        resumeFlag = false;

                        if (startingRang <= TotalStudentsCount) {
                            getStudentDataView(null);
                            fragment = new NoRecords("Loading ...");
                        } else {
                            fragment = new NoRecords();
                        }

                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment)
                                .commitAllowingStateLoss();

                        // }
                        // else {
                        // // error in creating fragment
                        // Log.e("MainActivity", "Error in creating fragment");
                        // }
                    }


                }
            });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
                "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public static Bitmap getiImageofChild(Context ctx, int childrenID) {
        String query = "select ChildrenScreenStatusID,LocalChildrenScreeningID from childrenscreening CS where  CS.IsDeleted!=1 AND  LocalChildrenID='"
                + childrenID + "';";
        DBHelper dbh = new DBHelper(ctx);
        Cursor cursor = dbh.getCursorData(ctx, query);
        String LocalChildrenScreeningID = "";
        Bitmap abc = null;
        if (cursor != null) {
            cursor.moveToNext();
            LocalChildrenScreeningID = (cursor.getString(cursor
                    .getColumnIndex("LocalChildrenScreeningID")));

        }
        if (!TextUtils.isEmpty(LocalChildrenScreeningID)) {
            String path = "";
            String query_Wards = "SELECT ImageName FROM  childrenscreeningpictures CSP where  CSP.IsDeleted!=1 AND  LocalChildrenScreeningID='"
                    + LocalChildrenScreeningID + "'";
            Cursor dataCursor = dbh.getCursorData(ctx, query_Wards);
            if (dataCursor != null && dataCursor.moveToFirst()) {
                path = dataCursor.getString(dataCursor
                        .getColumnIndex("ImageName"));
            } else {
                path = "";
            }
            String root = Environment.getExternalStorageDirectory().toString();
            System.out.println("path.........." + root + "/DCIM/myCapturedImages/" + path);
            File imgFile = new File(root + "/DCIM/myCapturedImages/" + path);
            if (imgFile.exists()) {
                Bitmap decodeFile = BitmapFactory.decodeFile(imgFile
                        .getAbsolutePath());
                if (decodeFile != null)
                    abc = RoundedImageView.getCroppedBitmap(decodeFile, 60);
            }
            // ////get Image end////////////////
        } else {
        }
        return abc;
    }

    protected void disableStudentdata() {
        et_screening_list_student_search.setVisibility(View.VISIBLE);
    }

    protected void enableStudentData() {
        et_screening_list_student_search.setFocusable(true);
    }

    private void findViews() {
        drawerImage = (ImageView) findViewById(R.id.drawer_button);
        ll_infoContainer = (LinearLayout) findViewById(R.id.ll_infocontainer);
        ll_studentContainer = (LinearLayout) findViewById(R.id.ll_studentcontainer);

        tv_screening_basicinfo = (TextView) findViewById(R.id.tv_screening_basicinfo);
        tv_medical_history = (TextView) findViewById(R.id.tv_medical_history);
        tv_capture_vitals = (TextView) findViewById(R.id.tv_capture_vitals);
        tv_screen = (TextView) findViewById(R.id.tv_screen);
        tv_referral = (TextView) findViewById(R.id.tv_referral);
        tv_signoff = (TextView) findViewById(R.id.tv_signoff);
        tv_screening_basicinfo.setOnClickListener(this);
        tv_medical_history.setOnClickListener(this);
        tv_capture_vitals.setOnClickListener(this);
        tv_screen.setOnClickListener(this);
        tv_referral.setOnClickListener(this);
        tv_signoff.setOnClickListener(this);

        view_basicinfo = (View) findViewById(R.id.view_basicinfo);
        view_medical_history = (View) findViewById(R.id.view_medical_history);
        view_capture_vitals = (View) findViewById(R.id.view_capture_vitals);
        view_screen = (View) findViewById(R.id.view_screen);
        view_referral = (View) findViewById(R.id.view_referral);
        view_signoff = (View) findViewById(R.id.view_signoff);
    }

    /**
     * onclick listener for views
     */
    @Override
    public void onClick(View v) {
        if (v == btn_screening_list_add_child) {
          Helper.showProgressDialog(ScreeningActivity.this);
            Helper.addChildFlag = true;
            Helper.childrenObject = new Children();
            Intent s = new Intent(ScreeningActivity.this,
                    AddStudentActivityDialog.class);
            startActivityForResult(s, 108);
        } else if (v == btn_screening_list_filter) {

            Intent filter = new Intent(ScreeningActivity.this,
                    ScreeningFilterStudent.class);
            filter.putExtras(getIntent().getExtras());
            startActivity(filter);

        } else if (v == tv_screening_basicinfo) {
            if (Helper.childScreeningObj != null) {
                tabFlags[0] = true;
                enableHeaderClick(tabFlags);
                displayView(tv_screening_basicinfo, this);
            } else {
                tabFlags[0] = false;
                enableHeaderClick(tabFlags);
            }

        } else if (v == tv_medical_history) {
            displayView(tv_medical_history, this);
        } else if (v == tv_capture_vitals) {
            displayView(tv_capture_vitals, this);
        } else if (v == tv_screen) {
            displayView(tv_screen, this);
        } else if (v == tv_referral) {
            displayView(tv_referral, this);
        } else if (v == tv_signoff) {
            displayView(tv_signoff, this);
        }
        // else if (v == btn_screening_list_searchGo) {
        //
        // }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CustomStudentAdapter.view != null)
            CustomStudentAdapter.view.performClick();
    }

    // Method to update header color.
    public void updateHeaderColorsScreening(int basicInfoHeader, int medical,
                                            int vitals, int screen, int refferal, int signoff) {
        tv_screening_basicinfo.setBackgroundDrawable(getResources()
                .getDrawable(basicInfoHeader));
        tv_medical_history.setBackgroundDrawable(getResources().getDrawable(
                medical));
        tv_capture_vitals.setBackgroundDrawable(getResources().getDrawable(
                vitals));
        tv_screen.setBackgroundDrawable(getResources().getDrawable(screen));
        tv_referral.setBackgroundDrawable(getResources().getDrawable(refferal));
        tv_signoff.setBackgroundDrawable(getResources().getDrawable(signoff));

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit from screening?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SimpleDateFormat mDateFormat = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss");

                                dbh.updateROW(
                                        ScreeningActivity.this,
                                        "InstituteScreeningDetails",
                                        new String[]{"ScreeningEndDateTime"},
                                        new String[]{mDateFormat
                                                .format(Calendar.getInstance()
                                                .getTime())},
                                        "LocalInstituteScreeningDetailID",
                                        String.valueOf(locInsScreeningDetailID));

                                Intent logout = new Intent(
                                        ScreeningActivity.this,
                                        DashBoardActivity.class);
                                startActivity(logout);
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

    // @Override
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
