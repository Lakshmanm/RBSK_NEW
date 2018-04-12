//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nunet.rbsk.BaseActivity;
import nunet.rbsk.R;
import nunet.rbsk.helpers.CustomDialog;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.IdentifyLoginActivity;
import nunet.rbsk.model.Event;
import nunet.rbsk.model.Institute;
import nunet.rbsk.model.InstituteSchedule;
import nunet.rbsk.planoffline.PlanOffLineActivity;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//*****************************************************************************
//* Name   :  ScheduleFragment.java

//* Type    :

//* Description     :
//* References     :
//* Author    : kiruthika.ganesan

//* Created Date       :  06-May-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG
//*****************************************************************************
//* Ver        Date                Code Review By            Observations
//*	3.0			08-05-2015			Deepika					Remove unwanted imports
//  3.0         22-05-2015          Kiruthika	           1. Action Bar back button is not working.
//*****************************************************************************
public class DashBoardActivity extends BaseActivity {

    private static Fragment fragment = null;
    public static ImageView btn_schedule_monthview;
    private Spinner spn_calendar_month;
    private Spinner spn_calendar_year;
    public static Bundle bundle;
    public static ImageView btn_schedule_dayview;
    public static boolean isMonthView = false;
    public static boolean isDayView = false;
    private static ArrayList<Item> yearItems;
    private static ArrayList<Item> monthItems;
    private static int currYearIndex;
    private static int currMonthIndex;
    private Calendar todayCal;
    private Button btn_schedule_goto_plan;
    private int currentYear;
    @SuppressWarnings("unused")
    private List<NameValuePair> params;
    private String selectedStartDate = "";
    private String selectedEndDate = "";

    private Cursor scheduleCur;
    // private InstituteSchedule insSchedule=null;
    public ArrayList<InstituteSchedule> scheduleList;
    public ArrayList<Event> eventList;
    private DBHelper dbh;
    public static CustomDialog dialog;
    public SharedPreferences sharedpreferences;
    public String UnlockID = "", UnlockPassword = "", HealthBlockID = "",
            DeviceID = "", TokenID = "";

    // inner classes
    private class Item {
        private int id;
        private String title;

        private Item(int id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_view);
        dbh = DBHelper.getInstance(this);

        /*
         * if (!isServiceRunning(IncrementalService.class)) { Intent
         * serviceIntent = new Intent(ScheduleFragment.this,
         * IncrementalService.class); startService(serviceIntent); }
         */

        sharedpreferences = getSharedPreferences("RbskPref",
                Context.MODE_PRIVATE);
        dialog = new CustomDialog(DashBoardActivity.this);
        // String data = Helper.getTableDataForJSON(dbh, this, "children");
        scheduleList = new ArrayList<InstituteSchedule>();
        eventList = new ArrayList<Event>();
        findViews();
        SharedPreferences sp = getSharedPreferences("LoginMain",
                Context.MODE_PRIVATE);
        DeviceID = sp.getString("DeviceID", "");
        UnlockID = sp.getString("UnlockID", "");
        UnlockPassword = sp.getString("UnlockPassword", "");
        HealthBlockID = sp.getString("HealthBlockID", "");
        TokenID = sp.getString("TokenID", "");
        // action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // actionBar.setHomeAsUpIndicator(R.drawable.arrowback);

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar_home, null);
        actionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#815CC5")));
        actionBar.setIcon(R.drawable.icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#815CC5")));
        @SuppressWarnings("unused")
        TextView tv_schoolName = (TextView) mCustomView
                .findViewById(R.id.tv_schoolName);
        initVars();
        loadYears();
        loadMonths();
        loadToSpinner();

        spn_calendar_month
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        if (!Helper.isMonthFirst) {
                            currMonthIndex = spn_calendar_month
                                    .getSelectedItemPosition();
                            updateCalendar();
                        } else {
                            Helper.isMonthFirst = false;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spn_calendar_year
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        if (!Helper.isYearFirst) {

                            currYearIndex = spn_calendar_year
                                    .getSelectedItemPosition();
                            updateCalendar();

                        } else {
                            Helper.isYearFirst = false;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        btn_schedule_monthview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btn_schedule_monthview.setVisibility(View.GONE);
                btn_schedule_dayview.setVisibility(View.VISIBLE);
                isMonthView = true;
                isDayView = false;
                displayView(btn_schedule_monthview, DashBoardActivity.this, 0);
            }
        });
        btn_schedule_dayview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btn_schedule_dayview.setVisibility(View.GONE);
                btn_schedule_monthview.setVisibility(View.VISIBLE);
                isDayView = true;
                isMonthView = false;
                displayView(btn_schedule_dayview, DashBoardActivity.this, 0);
            }
        });

        btn_schedule_goto_plan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isHoliday()) {
                    Intent in = new Intent(DashBoardActivity.this,
                            PlanOffLineActivity.class);
                    finish();
                    startActivity(in);
                } else {
                    Toast.makeText(DashBoardActivity.this,
                            "You can't plan on holiday", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            isDayView = true;
            isMonthView = false;
            currYearIndex = spn_calendar_year.getSelectedItemPosition();
            currMonthIndex = spn_calendar_month.getSelectedItemPosition();
            updateCalendar();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCalendar();
    }

    public boolean isHoliday() {

        Calendar mCalendar = Calendar.getInstance();
        if (Calendar.SUNDAY == mCalendar.get(Calendar.DAY_OF_WEEK)) {
            return true;
        } else if (Calendar.SATURDAY == mCalendar.get(Calendar.DAY_OF_WEEK)) {
            if (mCalendar.get(Calendar.WEEK_OF_MONTH) == 2)
                return true;
        } else {
            String query = "SELECT * FROM events where CalendarDate='"
                    + Helper.getTodayDate() + "'";
            Cursor mCursor = dbh.getCursorData(this, query);
            if (mCursor != null) {
                return true;
            }
        }
        return false;

    }

    protected void updateCalendar() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String currentMonthString = String.valueOf(monthItems
                        .get(currMonthIndex).id + 1);
                if (currentMonthString.length() == 1) {
                    currentMonthString = "0" + currentMonthString;
                }

                selectedStartDate = yearItems.get(currYearIndex).id + "-"
                        + currentMonthString + "-" + "01";
                selectedEndDate = yearItems.get(currYearIndex).id + "-"
                        + currentMonthString + "-" + "31";
                getScheduleDataFromDB();
                getHolidayDataFromDB();
                return null;
            }

            ;

            protected void onPostExecute(Void result) {
                if (isMonthView) {
                    displayView(btn_schedule_monthview, DashBoardActivity.this,
                            0);
                } else {
                    displayView(btn_schedule_dayview, DashBoardActivity.this, 0);
                }
            }
        }.execute();

    }

    /**
     * To get schedule List from db Kiruthika 07/05/2015
     */

    private void getScheduleDataFromDB() {
        String query_Wards = "";

        query_Wards = "SELECT distinct I.InstituteID,I.InstituteName,I.DiseCode,IPD.scheduleDate,"
                + "IPD.PlannedCount FROM InstitutePlanDetails IPD inner join instituteplans IP on "
                + "IP.LocalInstitutePlanID=IPD.LocalInstitutePlanID "
                + "inner join institutes I on I.InstituteID=IP.InstituteId "
                + "WHERE   IPD.IsDeleted!=1  AND   I.IsDeleted!=1  AND   IP.IsDeleted!=1  AND scheduleDate BETWEEN '"
                + selectedStartDate + "' AND '" + selectedEndDate + "'";

        scheduleCur = dbh.getCursorData(this, query_Wards);
        setToScheduleModel();
    }

    /**
     * Method to set to InsituteSchedule Model class Kiruthika 07/05/2015
     */
    private void setToScheduleModel() {
        scheduleList.clear();
        if (scheduleCur != null) {
            try {
                if (scheduleCur.moveToFirst()) {
                    do {
                        InstituteSchedule insSchedule = new InstituteSchedule();
                        Institute instituteModel = new Institute();
                        insSchedule.setScheduleDate(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("scheduleDate")));

                        String plancount = scheduleCur.getString(scheduleCur
                                .getColumnIndex("PlannedCount"));
                        insSchedule.setPlannedCount(NumUtil.IntegerParse
                                .parseInt(plancount));
                        insSchedule.setScheduleDescription(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("PlannedCount")));

                        instituteModel
                                .setInstituteServerID(NumUtil.IntegerParse.parseInt(scheduleCur
                                        .getString(scheduleCur
                                                .getColumnIndex("InstituteID"))));
                        instituteModel.setInstituteName(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("InstituteName")));
                        instituteModel.setDiseCode(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("DiseCode")));
                        insSchedule.setInstitute(instituteModel);
                        scheduleList.add(insSchedule);

                    } while (scheduleCur.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                scheduleCur.close();
            }
        }
    }

    /**
     * Method to get Holiday list from DB Kiruthika 07/05/2015
     */
    private void getHolidayDataFromDB() {
        String query_Wards = "SELECT * FROM events E WHERE    E.IsDeleted!=1  AND CalendarDate BETWEEN '"
                + selectedStartDate + "' AND '" + selectedEndDate + "'";
        scheduleCur = dbh.getCursorData(this, query_Wards);
        setToEventModel();
    }

    /**
     * Method to set Event List to Model class Kiruthika 07/05/2015
     */
    private void setToEventModel() {
        eventList.clear();
        if (scheduleCur != null) {
            try {
                if (scheduleCur.moveToFirst()) {
                    do {
                        Event eventObj = new Event();

                        eventObj.setScheduleDate(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("CalendarDate")));
                        eventObj.setScheduleDescription(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("EventName")));
                        // insSchedule.isDeleted(Boolean.parseBoolean(scheduleCur.getColumnName(scheduleCur.getColumnIndex("PlannedCount"))));
                        eventList.add(eventObj);

                    } while (scheduleCur.moveToNext());
                }
            } finally {
                scheduleCur.close();
            }
        }
    }

    /**
     * Method to Load years and Months to spinner Set current Month and year as
     * default select Kiruthika 06/05/2015
     */
    private void loadToSpinner() {
        ArrayAdapter<String> adp_spnSection = new ArrayAdapter<String>(this,
                R.layout.spinner_text_view, R.id.spinner_view_adpter);

        for (int i = 0; i < yearItems.size(); i++) {
            adp_spnSection.add(yearItems.get(i).title);
        }
        spn_calendar_year.setAdapter(adp_spnSection);
        for (int i = 0; i < yearItems.size(); i++) {
            if (currentYear == yearItems.get(i).id) {
                spn_calendar_year.setSelection(i);
            }
        }

        ArrayAdapter<String> adp_spnMonths = new ArrayAdapter<String>(this,
                R.layout.spinner_text_view, R.id.spinner_view_adpter);

        for (int i = 0; i < monthItems.size(); i++) {
            adp_spnMonths.add(monthItems.get(i).title);
        }
        spn_calendar_month.setAdapter(adp_spnMonths);
        for (int i = 0; i < monthItems.size(); i++) {
            if (currMonthIndex == monthItems.get(i).id) {
                spn_calendar_month.setSelection(currMonthIndex);
            }

        }

    }

    /**
     * To get Id of all views
     */
    private void findViews() {
        btn_schedule_monthview = (ImageView) findViewById(R.id.btn_schedule_monthview);
        btn_schedule_dayview = (ImageView) findViewById(R.id.btn_schedule_dayview);
        spn_calendar_month = (Spinner) findViewById(R.id.spn_calendar_month);
        spn_calendar_year = (Spinner) findViewById(R.id.spn_calendar_year);
        btn_schedule_goto_plan = (Button) findViewById(R.id.btn_schedule_goto_plan_offline);

    }

    /**
     * Method to get current date Kiruthika 06/05/2015
     */
    private void initVars() {
        todayCal = Calendar.getInstance();
    }

    /**
     * Method to Load years Kiruthika 06/05/2015
     */
    private void loadYears() {

        currentYear = todayCal.get(Calendar.YEAR);

        yearItems = new ArrayList<Item>();

        for (int i = currentYear - 1; i <= currentYear + 1; i++) {
            yearItems.add(new Item(i, i + ""));
        }

        currYearIndex = 1;
    }

    /**
     * Method to Load Months Kiruthika 06/05/2015
     */
    private void loadMonths() {
        currMonthIndex = todayCal.get(Calendar.MONTH);
        monthItems = new ArrayList<Item>();
        monthItems.add(new Item(0, "January"));
        monthItems.add(new Item(1, "February"));
        monthItems.add(new Item(2, "March"));
        monthItems.add(new Item(3, "April"));
        monthItems.add(new Item(4, "May"));
        monthItems.add(new Item(5, "June"));
        monthItems.add(new Item(6, "July"));
        monthItems.add(new Item(7, "August"));
        monthItems.add(new Item(8, "September"));
        monthItems.add(new Item(9, "October"));
        monthItems.add(new Item(10, "November"));
        monthItems.add(new Item(11, "December"));
    }

    /**
     * On click listener
     *
     * @param
     */

    // public static int kk = 0;
    public void displayView(View v, Context ctx, int day) {
        if(!((Activity)ctx).isFinishing()) {
            if (dialog != null)
                dialog.show();
        }
        switch (v.getId()) {
            case R.id.btn_schedule_monthview:
                bundle = new Bundle();
                bundle.putInt("Month", monthItems.get(currMonthIndex).id);
                bundle.putInt("Year", yearItems.get(currYearIndex).id);
                Helper.scheduleList = scheduleList;
                Helper.eventList = eventList;
                // /bundle.putSerializable("ScheduleList", scheduleList);
                // bundle.putSerializable("EventList", eventList);
                fragment = new MonthViewCalender();
                fragment.setArguments(bundle);
                break;
            case R.id.btn_schedule_dayview:
                bundle = new Bundle();
                bundle.putInt("Month", monthItems.get(currMonthIndex).id + 1);
                bundle.putInt("Year", yearItems.get(currYearIndex).id);
                bundle.putInt("Day", day);
                Helper.scheduleList = scheduleList;
                Helper.eventList = eventList;
                // bundle.putSerializable("ScheduleList", scheduleList);
                // bundle.putSerializable("EventList", eventList);
                fragment = new DayViewCalendar();
                fragment.setArguments(bundle);
                break;
            default:
                break;
        }

        if (fragment != null) {
            fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commitAllowingStateLoss();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        handler.postDelayed(runnable, 500);
    }

    public static FragmentManager fragmentManager;

    @Override
    public void onBackPressed() {

        if (fragment instanceof MonthViewCalender) {
            dialog.show();
            bundle = new Bundle();
            bundle.putInt("Month", monthItems.get(currMonthIndex).id + 1);
            bundle.putInt("Year", yearItems.get(currYearIndex).id);
            // bundle.putInt("Day", day);
            Helper.scheduleList = scheduleList;
            Helper.eventList = eventList;
            // bundle.putSerializable("ScheduleList", scheduleList);
            // bundle.putSerializable("EventList", eventList);
            fragment = new DayViewCalendar();
            fragment.setArguments(bundle);
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            };
            handler.postDelayed(runnable, 500);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent logout = new Intent(
                                        DashBoardActivity.this,
                                        IdentifyLoginActivity.class);
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
