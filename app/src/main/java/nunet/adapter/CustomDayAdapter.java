//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nunet.rbsk.R;
import nunet.rbsk.dashboard.DayViewCalendar;
import nunet.rbsk.dashboard.SkipInstituteActivityDialog;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.info.inst.InsituteFragmentActivityDialog;
import nunet.rbsk.model.DaySchedule;
import nunet.rbsk.model.Institute;
import nunet.rbsk.screening.ScreeningActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.DateUtil;
import com.nunet.utils.StringUtils;

import static NumUtil.IntegerParse.parseInt;
//*****************************************************************************
//* Name   :  CustomDayAdapter.java

//* Type    :

//* Description     :
//* References     :
//* Author    : promodh.munjeti

//* Created Date       :  May 6, 2015
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
public class CustomDayAdapter extends BaseAdapter {

    /*
       * (non-Javadoc)
       *
       * @see android.widget.Adapter#getCount()
       */
    private Context contex;
    private ArrayList<DaySchedule> dayAdapterObj;
    private LayoutInflater inflater;
    public int lastSelectedPosition = 99;
    private int month;
    private int year;
    private String finalDateString = "";
    private DBHelper dbh;
    private int daySelected = 0;
    private String rBSKCalenarYearID;
    private String screeningRoundID;
    SimpleDateFormat sdf;

    // public int InstitutePlanStatusID = 0;
    // private long LocalInstitutePlanDetailID;

    /**
     * Constructor
     *
     * @param activity
     * @param daySchedule
     * @param current_date
     * @param yearSelected
     */
    // sand: day selected changed to static and update in on click
    public CustomDayAdapter(Context ctx, ArrayList<DaySchedule> daySchedule,
                            int monthSelected, int yearSelected, int daySelected) {
        this.contex = ctx;
        this.dayAdapterObj = daySchedule;
        this.month = monthSelected;
        this.year = yearSelected;
        inflater = (LayoutInflater) contex
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbh = DBHelper.getInstance(ctx);

        this.daySelected = daySelected;
    }

    private boolean initIsFstime = true;

    @Override
    public int getCount() {
        return dayAdapterObj.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    /*
       * (non-Javadoc)
       *
       * @see android.widget.Adapter#getItemId(int)
       */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Override method to add view based on list of items
     */

    public class ViewHolder {
        TextView tv_day_cal_date_day, tv_day_cal_date_weekday,
                tv_day_cal_school_no;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int selectedDay = dayAdapterObj.get(position).getDay();
        ViewHolder mHolder;
        convertView = inflater.inflate(R.layout.day_calendar, parent, false);
        mHolder = new ViewHolder();

        mHolder.tv_day_cal_date_day = (TextView) convertView
                .findViewById(R.id.tv_day_cal_date_day);
        mHolder.tv_day_cal_date_day.setText(""
                + dayAdapterObj.get(position).getDay());
        mHolder.tv_day_cal_date_weekday = (TextView) convertView
                .findViewById(R.id.tv_day_cal_date_weekday);
        mHolder.tv_day_cal_school_no = (TextView) convertView
                .findViewById(R.id.tv_day_cal_school_no);
        convertView.setTag(mHolder);

        mHolder.tv_day_cal_date_weekday.setText(dayAdapterObj.get(position)
                .getWeekday());
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (lastSelectedPosition == 99) {

            Helper.currentDate = sdf.format(new Date());
            String positionDateString = String.valueOf(dayAdapterObj.get(
                    position).getDay()
                    + "/" + month + "/" + year);
            try {
                Date newDate = sdf.parse(positionDateString);
                positionDateString = sdf.format(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // if (Helper.currentDate.equalsIgnoreCase(positionDateString)) {
            // row.setBackgroundColor(Color.parseColor("#45cfc1"));
            // tv_day_cal_school_no.setTextColor(Color.parseColor("#ffffff"));
            // tv_day_cal_date_weekday.setTextColor(Color
            // .parseColor("#ffffff"));
            // tv_day_cal_date_day.setTextColor(Color.parseColor("#ffffff"));
            // dayAdapterObj.get(position).setSelected(true);
            // lastSelectedPosition = position;
            // row.setFocusable(true);
            // row.setFocusableInTouchMode(true);
            // row.requestFocus();
            // }

        }

        if (StringUtils.equalsNoCase(dayAdapterObj.get(position).getWeekday(),
                "Sunday")) {
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            convertView.setBackgroundColor(Color.parseColor("#815cc5"));
            mHolder.tv_day_cal_school_no.setText("Weekend");
        } else if (!TextUtils.isEmpty(dayAdapterObj.get(position).getHoliday())) {
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            convertView.setBackgroundColor(Color.parseColor("#815cc5"));
            mHolder.tv_day_cal_school_no.setText(dayAdapterObj.get(position)
                    .getHoliday());
        }
        if (dayAdapterObj.get(position).getNoOfSchools() != null) {
            if (StringUtils.equalsNoCase(dayAdapterObj.get(position)
                    .getNoOfSchools(), "")) {
            } else {
                mHolder.tv_day_cal_school_no.setText(dayAdapterObj
                        .get(position).getNoOfSchools());
            }
        }
        if (lastSelectedPosition != 99) {

            if (dayAdapterObj.get(position).isSelected()) {
                convertView.setBackgroundColor(Color.parseColor("#45cfc1"));
                mHolder.tv_day_cal_school_no.setTextColor(Color
                        .parseColor("#ffffff"));
                mHolder.tv_day_cal_date_weekday.setTextColor(Color
                        .parseColor("#ffffff"));
                mHolder.tv_day_cal_date_day.setTextColor(Color
                        .parseColor("#ffffff"));
            }
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                getUpdateSchedulesView(position, v);
            }
        });
        // String selectedDate = daySelected + "/" + month + "/" + year;
        if (selectedDay == daySelected && initIsFstime) {
            initIsFstime = false;
            ClickView = convertView;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    ClickView.performClick();

                }
            }, 100);
        }
        return convertView;
    }

    public View ClickView;

    @SuppressLint("InflateParams")
    public void getUpdateSchedulesView(int position, View row) {

        if (lastSelectedPosition != 99) {
            dayAdapterObj.get(lastSelectedPosition).setSelected(false);
        }
        finalDateString = String.valueOf(dayAdapterObj.get(position).getDay()
                + "/" + month + "/" + year);

        Date newDate = null;
        try {
            newDate = sdf.parse(finalDateString);
            finalDateString = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String scheduleDate = DateUtil.format("yyyy-MM-dd",
                Calendar.getInstance());

        row.setBackgroundColor(Color.parseColor("#45cfc1"));
        ((TextView) row.findViewById(R.id.tv_day_cal_school_no))
                .setTextColor(Color.parseColor("#ffffff"));
        ((TextView) row.findViewById(R.id.tv_day_cal_date_weekday))
                .setTextColor(Color.parseColor("#ffffff"));
        ((TextView) row.findViewById(R.id.tv_day_cal_date_day))
                .setTextColor(Color.parseColor("#ffffff"));
        DayViewCalendar.ll_schlist.removeAllViews();

        final DaySchedule currentDaySchedule = dayAdapterObj.get(position);
        currentDaySchedule.setSelected(true);
        ArrayList<Institute> institutes = currentDaySchedule.getInstitutes();
        if (institutes != null) {
            for (int i = 0; i < institutes.size(); i++) {
                LayoutInflater inflator = (LayoutInflater) contex
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View view = inflator.inflate(R.layout.schedule_school, null);
                final TextView tv_school_name = (TextView) view
                        .findViewById(R.id.tv_school_name);
                final TextView tv_school_skipped = (TextView) view
                        .findViewById(R.id.tv_school_skipped);
                Button btn_startScreen = (Button) view
                        .findViewById(R.id.btn_startScreen);
                Button btn_skip_institute = (Button) view
                        .findViewById(R.id.btn_skip_institute);
                Button btn_location = (Button) view
                        .findViewById(R.id.btn_location);
                Button btn_instit_signoff = (Button) view.
                        findViewById(R.id.btn_instit_signoff);

                TextView tv_area = (TextView) view.findViewById(R.id.tv_area);

                final int instId = institutes.get(i).getInstituteServerID();

                final long LocalInstitutePlanDetailID = institutes.get(i)
                        .getLocalInstitutePlanDetailID();

                final Institute insObj = getInstituteDataFromDB(
                        LocalInstitutePlanDetailID, scheduleDate);
                btn_startScreen.setTag(insObj);
                btn_skip_institute.setTag(insObj);
                tv_school_name.setText(insObj.getInstituteName());
                if (insObj.getPlanStatusID() == 2) {
                    row.setBackgroundColor(0x787878);
                    tv_school_skipped.setVisibility(View.VISIBLE);
                    btn_location.setVisibility(View.GONE);
                    btn_instit_signoff.setVisibility(View.GONE);
                    btn_skip_institute.setVisibility(View.GONE);
                    btn_startScreen.setVisibility(View.GONE);
                } else {
                    tv_school_skipped.setVisibility(View.GONE);
                    btn_instit_signoff.setVisibility(View.VISIBLE);
                    btn_location.setVisibility(View.VISIBLE);
                    btn_skip_institute.setVisibility(View.VISIBLE);
                    btn_startScreen.setVisibility(View.VISIBLE);
                }
                // tv_area.setText(insObj.getAddress().getVillage().getVillageName());
                DayViewCalendar.ll_schlist.addView(view);
                final TextView tv_total_students = (TextView) view
                        .findViewById(R.id.tv_total_students);
                TextView tv_male_count = (TextView) view
                        .findViewById(R.id.tv_male_count);
                TextView tv_female_count = (TextView) view
                        .findViewById(R.id.tv_female_count);
                TextView tv_screened_count = (TextView) view
                        .findViewById(R.id.tv_screened_count);
                TextView tv_referred_count = (TextView) view
                        .findViewById(R.id.tv_referred_count);
                // TextView tv_locally_count = (TextView) view
                // .findViewById(R.id.tv_locally_count);

                updateMaleFemale(instId, tv_total_students, tv_male_count,
                        tv_female_count);

                updateReferDetails(institutes, i, instId, tv_screened_count,
                        tv_referred_count);

                updateAreaName(tv_area, instId);

                btn_startScreen.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                insertInstituteScreeningAndDetails(
                                        tv_school_name, instId, v);
                            }
                        }).start();

                    }

                    private void insertInstituteScreeningAndDetails(
                            final TextView tv_school_name, final int instId,
                            View v) {
                        Institute mInstitute = (Institute) v.getTag();
                        String validatingInstituteScreeningQuery = "SELECT LocalInstituteScreeningID FROM institutescreening INS where RBSKCalendarYearID='"
                                + mInstitute.getRBSKCalenarYearID()
                                + "' and ScreeningRoundId='"
                                + mInstitute.getScreeningRoundID()
                                + "' and instituteID='"
                                + mInstitute.getInstituteServerID() + "'";
                        long localIntstitueScreeningID = 0;
                        Cursor cursor = dbh.getCursorData(contex,
                                validatingInstituteScreeningQuery);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                int columnIndex = cursor
                                        .getColumnIndex("LocalInstituteScreeningID");
                                localIntstitueScreeningID = cursor
                                        .getLong(columnIndex);
                            } while (cursor.moveToNext());
                        }
                        rBSKCalenarYearID = String.valueOf(mInstitute
                                .getRBSKCalenarYearID());
                        screeningRoundID = String.valueOf(mInstitute
                                .getScreeningRoundID());
                        if (localIntstitueScreeningID == 0) {

                            localIntstitueScreeningID = dbh
                                    .insertintoTable(
                                            contex,
                                            "InstituteScreening",
                                            new String[]{
                                                    "InstituteScreeningStatusID",
                                                    "RBSKCalendarYearID",
                                                    "ScreeningRoundID",
                                                    "InstituteID"},
                                            new String[]{
                                                    "2",
                                                    rBSKCalenarYearID,
                                                    screeningRoundID,
                                                    String.valueOf(mInstitute
                                                            .getInstituteServerID()),

                                            });
                        }
                        String validatingScreeningDetail = "SELECT LocalInstituteScreeningDetailID FROM InstituteScreeningDetails IPD "
                                + " inner join instituteplans IP on IP.LocalInstitutePlanID=IPD.LocalInstitutePlanID where  IP.LocalInstitutePlanID='"
                                + mInstitute.getInstitutePlanID()
                                + "' and LocalInstituteScreeningID='"
                                + localIntstitueScreeningID + "'";
                        Cursor curScreeningDetail = dbh.getCursorData(contex,
                                validatingScreeningDetail);
                        if (curScreeningDetail != null) {
                            curScreeningDetail.moveToFirst();
                            do {
                                localIntstitueScreeningDetailID = curScreeningDetail.getLong(curScreeningDetail
                                        .getColumnIndex("LocalInstituteScreeningDetailID"));
                            } while (curScreeningDetail.moveToNext());
                        }
                        if (localIntstitueScreeningDetailID == 0) {
                            localIntstitueScreeningDetailID = dbh
                                    .insertintoTable(
                                            contex,
                                            "InstituteScreeningDetails",
                                            new String[]{
                                                    "LocalInstituteScreeningID",
                                                    "LocalInstitutePlanID",
                                                    "CalendarDate",
                                                    "ScreenStatusID",
                                                    "ScreeningStartDateTime",},
                                            new String[]{
                                                    String.valueOf(localIntstitueScreeningID),
                                                    String.valueOf(mInstitute
                                                            .getInstitutePlanID()),
                                                    mInstitute
                                                            .getScheduledPlanDate(),
                                                    "2",
                                                    mInstitute
                                                            .getCurrentDateTime(),

                                            });

                        }

                        ((Activity) (contex)).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Intent i = new Intent(contex,
                                        ScreeningActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putInt("InstituteID", instId);
                                mBundle.putInt("LocalInstituteID",
                                        insObj.getLocalInstituteID());
                                mBundle.putInt("InstituteTypeId",
                                        insObj.getInstituteTypeId());
                                mBundle.putString("InstituteName",
                                        tv_school_name.getText().toString()
                                                .trim());
                                mBundle.putLong("LocInsScreeningDetailID",
                                        localIntstitueScreeningDetailID);
                                mBundle.putString("RBSKCalenarYearID",
                                        rBSKCalenarYearID);
                                mBundle.putString("ScreeningRoundID",
                                        screeningRoundID);
                                mBundle.putString("TotalStudentsCount",
                                        tv_total_students.getText().toString());
                                i.putExtras(mBundle);
                                contex.startActivity(i);
                            }
                        });
                    }

                });


                btn_instit_signoff.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(contex,
                                InsituteFragmentActivityDialog.class);
                        i.putExtra("instituteID", instId);
                        i.putExtra("index", 1);
                        contex.startActivity(i);
                    }
                });
                btn_location.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(contex,
                                InsituteFragmentActivityDialog.class);
                        i.putExtra("instituteID", instId);
                        i.putExtra("index", 0);
                        contex.startActivity(i);
                    }
                });

                if (!StringUtils.equalsNoCase(Helper.currentDate,
                        finalDateString)) {
                    btn_skip_institute.setVisibility(View.GONE);
                }

                btn_skip_institute.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Institute insObj = (Institute) v.getTag();
                        if (StringUtils.equalsNoCase(Helper.currentDate,
                                finalDateString)) {
                            Intent i = new Intent(contex,
                                    SkipInstituteActivityDialog.class);
                            i.putExtra("LocalInstitutePlanDetailID",
                                    insObj.getLocalInstitutePlanDetailID());
                            contex.startActivity(i);

                        } else {
                            Toast.makeText(contex,
                                    "You can skip plan only for today",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        } else {

            LayoutInflater inflator = (LayoutInflater) contex
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflator.inflate(R.layout.no_records, null);
            DayViewCalendar.ll_schlist.addView(view);
        }
        lastSelectedPosition = position;

        notifyDataSetChanged();
    }

    private void updateAreaName(final TextView tv_area, final int instId) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String queryInstituteArea = "SELECT V.DisplayText FROM institutes I inner join Address A on I.LocalAddressID=A.LocalAddressID inner join Villages v on V.VillageID=A.VillageID where I.InstituteID='"
                        + instId + "'";
                Cursor mCursor = dbh.getCursorData(contex, queryInstituteArea);
                if (mCursor != null) {
                    mCursor.moveToFirst();
                    return mCursor.getString(0);
                }
                return null;
            }

            protected void onPostExecute(String result) {
                tv_area.setText(result);
            }

            ;

        }.execute();

    }

    private void updateMaleFemale(final int instId,
                                  final TextView tv_total_students, final TextView tv_male_count,
                                  final TextView tv_female_count) {

        new AsyncTask<Void, Void, Void>() {
            int maleCount = 0;
            int femaleCount = 0;

            @Override
            protected Void doInBackground(Void... params) {
                String query = "select U.GenderID, C.ChildrenStatusID from users U "
                        + " inner join  children C on (U.LocalUserID=C.LocalUserID) "
                        + " inner join institutes I on I.LocalInstituteID=C.LocalInstituteID "
                        + " where I.InstituteID='"
                        + instId
                        + "'  AND U.IsDeleted!=1 AND I.IsDeleted!=1 AND C.IsDeleted!=1 ;";

                Cursor cursor = dbh.getCursorData(contex, query);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String userGenderStr = cursor.getString(cursor
                                .getColumnIndex("GenderID"));
                        if (!TextUtils.isEmpty(userGenderStr))
                            if (parseInt(userGenderStr) == 1) {
                                maleCount++;
                            } else if (parseInt(userGenderStr) == 2) {
                                femaleCount++;
                            }

                    }

                    cursor.close();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                tv_total_students
                        .setText((maleCount + femaleCount) + "".trim());
                tv_male_count.setText(maleCount + "".trim());
                tv_female_count.setText(femaleCount + "".trim());
            }

            ;

        }.execute();

    }

    private void updateReferDetails(final ArrayList<Institute> institutes,
                                    final int i, final int instId, final TextView tv_screened_count,
                                    final TextView tv_referred_count) {
        new AsyncTask<Void, Void, Void>() {
            int screenedCount = 0;
            int referredCount = 0;

            @Override
            protected Void doInBackground(Void... params) {
                String query1 = "select CS.ChildrenScreenStatusID from childrenscreening CS "
                        + " inner join children C on (C.LocalChildrenID=CS.LocalChildrenID)"
                        + " inner join institutes I on I.LocalInstituteID=C.LocalInstituteID"
                        + " inner join institutescreeningdetails isd on isd.localinstitutescreeningdetailid=cs.localinstitutescreeningdetailid"
                        + " inner join institutescreening ins on ins.localinstitutescreeningid=isd.localinstitutescreeningid"
                        + " where I.InstituteID='"
                        + instId
                        + "' and CS.IsDeleted!=1 AND C.IsDeleted!=1 AND I.IsDeleted!=1 and RBSKCalendarYearID='"
                        + institutes.get(i).getRBSKCalenarYearID()
                        + "' and SCREENINGROUNDID='"
                        + institutes.get(i).getScreeningRoundID() + "'";

                Cursor cursor1 = dbh.getCursorData(contex, query1);
                if (cursor1 != null) {
                    while (cursor1.moveToNext()) {
                        String statusStr = cursor1.getString(0);
                        if (StringUtils.equalsNoCase(statusStr.trim(), "1")) {
                            screenedCount++;
                        }
                        if (StringUtils.equalsNoCase(statusStr, "4")) {
                            referredCount++;
                        }
                    }
                    cursor1.close();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                tv_screened_count.setText((screenedCount + referredCount)
                        + "".trim());
                tv_referred_count.setText(referredCount + "".trim());
            }

            ;

        }.execute();

    }

    long localIntstitueScreeningDetailID = 0;

    /**
     *
     */
    protected Institute getInstituteDataFromDB(long LocalInstitutePlanDetailID,
                                               String scheduleDate) {
        String instituteQuery = "select I.LocalInstituteID,IPD.LocalInstitutePlanDetailID, I.InstituteID,I.InstituteName,I.InstituteTypeId,"
                + " IPD.PlanStatusID,IP.RBSKCalendarYearID,IP.ScreeningRoundID,IP.LocalInstitutePlanID,IPD.scheduleDate "
                + " from institutes I inner join instituteplans IP on IP.InstituteID=I.InstituteID "
                + " inner join instituteplandetails IPD on IPD.LocalInstitutePlanID=IP.LocalInstitutePlanID "
                + " where   IP.IsDeleted!=1  AND IPD.IsDeleted!=1  AND I.IsDeleted!=1 AND IPD.LocalInstitutePlanDetailID='"
                + LocalInstitutePlanDetailID + "'";
        Cursor instCursor = dbh.getCursorData(contex, instituteQuery);
        Institute insModelObject = new Institute();

        if (instCursor != null) {
            try {

                if (instCursor.moveToFirst()) {
                    int LocalInstituteID = instCursor
                            .getColumnIndex("LocalInstituteID");
                    int idxInstituteID = instCursor
                            .getColumnIndex("InstituteID");
                    int idxPlanStatusID = instCursor
                            .getColumnIndex("PlanStatusID");
                    int idxInstituteName = instCursor
                            .getColumnIndex("InstituteName");

                    int idxRBSKCalendarYearID = instCursor
                            .getColumnIndex("RBSKCalendarYearID");
                    int idxScreeningRoundId = instCursor
                            .getColumnIndex("ScreeningRoundId");
                    int idxLocalInstitutePlanID = instCursor
                            .getColumnIndex("LocalInstitutePlanID");

                    int idxLocalInstitutePlanDetailID = instCursor
                            .getColumnIndex("LocalInstitutePlanDetailID");

                    int idxInstituteTypeId = instCursor
                            .getColumnIndex("InstituteTypeId");
                    int idxScheduleDate = instCursor
                            .getColumnIndex("scheduleDate");
                    do {

                        insModelObject.setLocalInstituteID(parseInt(instCursor
                                .getString(LocalInstituteID)));

                        insModelObject.setInstituteServerID(parseInt(instCursor
                                .getString(idxInstituteID)));
                        insModelObject.setPlanStatusID(parseInt(instCursor
                                .getString(idxPlanStatusID)));

                        insModelObject.setInstituteName(instCursor
                                .getString(idxInstituteName));
                        // insModelObject.setInstituteName(instCursor
                        // .getString(instCursor
                        // .getColumnIndex("InstituteName")));

                        insModelObject.setRBSKCalenarYearID(instCursor
                                .getInt(idxRBSKCalendarYearID));

                        insModelObject.setScreeningRoundID(instCursor
                                .getInt(idxScreeningRoundId));

                        insModelObject.setInstitutePlanID(instCursor
                                .getInt(idxLocalInstitutePlanID));

                        insModelObject.setScheduledPlanDate(instCursor
                                .getString(idxScheduleDate));

                        insModelObject.setLocalInstitutePlanDetailID(instCursor
                                .getLong(idxLocalInstitutePlanDetailID));

                        insModelObject.setInstituteTypeId(parseInt(instCursor
                                .getString(idxInstituteTypeId)));
                        // addModelObject = Helper.getAllAddress(instCursor);
                        // insModelObject.setAddress(addModelObject);
                    } while (instCursor.moveToNext());

                }

            } finally {
                instCursor.close();
            }
        }
        return insModelObject;
    }
}
