//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nunet.adapter.CustomDayAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.DaySchedule;
import nunet.rbsk.model.Event;
import nunet.rbsk.model.Institute;
import nunet.rbsk.model.InstituteSchedule;

import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nunet.utils.StringUtils;

//*****************************************************************************
//* Name   :  DayViewCalendar.java

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
//* 3.0        08-05-2015			Kiruthika					Remove unwanted variables, remove unused method getDayDataFromDb()
//*****************************************************************************
public class DayViewCalendar extends Fragment {
    public static ListView lv_day_calendar;
    private Calendar _calendar;
    private String dayOfTheWeek = "";
    private int current_date;
    private int monthSelected = 0;
    private int daySelected = 0;
    private int yearSelected;
    public static LinearLayout ll_schlist;
    private String[] days_month = {"31", "28", "31", "30", "31", "30", "31",
            "31", "30", "31", "30", "31"};
    CustomDayAdapter dayAdapter;
    private ArrayList<InstituteSchedule> scheduleList;
    private ArrayList<Event> eventList;
    private DBHelper dbh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateCalendarView = null;
        View rootView = inflater.inflate(R.layout.dayview_layout, container,
                false);
        findViews(rootView);
        monthSelected = getArguments().getInt("Month");
        yearSelected = getArguments().getInt("Year");
        daySelected = getArguments().getInt("Day");

        scheduleList = Helper.scheduleList;// Get Schedule list
        eventList = Helper.eventList;// Get event List
        dbh = DBHelper.getInstance(this.getActivity());

        _calendar = Calendar.getInstance(Locale.getDefault());
        current_date = _calendar.get(Calendar.DATE);

        if (daySelected == 0) {
            // if (CustomDayAdapter.lastSelectedPosition != 99) {
            // daySelected = CustomDayAdapter.lastSelectedPosition + 1;
            // } else {
            daySelected = current_date;
            // }
        }
        loadSildMenuData();
        // else {
        // daySelected = daySelected;
        // }

        return rootView;
    }

    private ArrayList<DaySchedule> updateCalendarView;

    public void loadSildMenuData() {
        new AsyncTask<Void, Void, ArrayList<DaySchedule>>() {

            @Override
            protected ArrayList<DaySchedule> doInBackground(Void... params) {
                if (updateCalendarView == null)
                    updateCalendarView = getUpdateCalendarView(monthSelected,
                            yearSelected);
                return updateCalendarView;
            }

            @Override
            protected void onPostExecute(ArrayList<DaySchedule> result) {
                super.onPostExecute(result);
                if (getActivity() == null)
                    return;
                dayAdapter = new CustomDayAdapter(getActivity(), result,
                        monthSelected, yearSelected, daySelected);
                lv_day_calendar.setAdapter(dayAdapter);
                lv_day_calendar.setSelector(R.drawable.listselector);
                lv_day_calendar.setSelection(daySelected - 1);

            }
        }.execute();
    }

    /**
     * To update the calender view for holidays and evnts
     */

    private ArrayList<DaySchedule> getUpdateCalendarView(int month, int year) {
        ArrayList<DaySchedule> daySchedule = new ArrayList<DaySchedule>();
        int noofDays = NumUtil.IntegerParse.parseInt(days_month[month - 1]);
        if (month == 2) {
            if (year % 4 == 0) {
                noofDays = 29;
            }
        }
        int saturdayCount = 0;
        @SuppressWarnings("unused")
        int secondSaturdayDay = 1;
        for (int i = 0; i < noofDays; i++) {
            DaySchedule daySchedulea = new DaySchedule();
            daySchedulea.setDay(i + 1);

            daySchedulea.setWeekday(getFirstWeekDay(month, year, i + 1));
            String currentMonthString = String.valueOf(month);
            if (currentMonthString.length() == 1) {
                currentMonthString = "0" + currentMonthString;
            }
            String currentdayString = String.valueOf(i + 1);
            if (currentdayString.length() == 1) {
                currentdayString = "0" + currentdayString;
            }
            String date_in_loop = year + "-" + currentMonthString + "-"
                    + currentdayString;

            daySchedulea.setSelected(false);
            if (!eventList.isEmpty()) {
                for (int k = 0; k < eventList.size(); k++) {
                    if (StringUtils.equalsNoCase(eventList.get(k)
                            .getScheduleDate(), date_in_loop)) {
                        String scheduleDescription = eventList.get(k)
                                .getScheduleDescription();
                        daySchedulea.setHoliday(scheduleDescription);
                        break;
                    } else {

                        if (StringUtils.equalsNoCase(daySchedulea.getWeekday(),
                                "Saturday")) {
                            saturdayCount++;
                            if (saturdayCount == 2) {

                                secondSaturdayDay = daySchedulea.getDay();
                                daySchedulea.setHoliday("Second Saturday");
                            } else {
                                daySchedulea.setHoliday("");
                            }
                        } else {
                            daySchedulea.setHoliday("");
                        }

                    }
                }
            } else {
                if (StringUtils.equalsNoCase(daySchedulea.getWeekday(),
                        "Saturday")) {
                    saturdayCount++;
                    if (saturdayCount == 2) {

                        secondSaturdayDay = daySchedulea.getDay();
                        daySchedulea.setHoliday("Second Saturday");
                    } else {
                        daySchedulea.setHoliday("");
                    }
                } else {
                    daySchedulea.setHoliday("");
                }
            }
            if (!scheduleList.isEmpty()) {
                for (int s = 0; s < scheduleList.size(); s++) {
                    if (StringUtils.equalsNoCase(scheduleList.get(s)
                            .getScheduleDate(), date_in_loop)) {
                        ArrayList<Institute> instCount = getNumberofInstitutes(date_in_loop);
                        if (instCount.size() != 0) {
                            daySchedulea.setInstitutes(instCount);
                            daySchedulea.setNoOfSchools(instCount.size()
                                    + " Schools");
                        }
                        break;
                    }
                    // else {
                    // daySchedulea.setNoOfSchools("");
                    // }
                }
            } else {
                daySchedulea.setNoOfSchools("");
            }
            daySchedule.add(daySchedulea);

        }
        return daySchedule;

    }

    /**
     * To get number of scheduled institutes
     *
     * @param date_in_loop
     */
    private ArrayList<Institute> getNumberofInstitutes(String date_in_loop) {
        String query_no_of_institutes = "Select RBSKCalendarYearID,ScreeningRoundID,LocalInstitutePlanDetailID,InstituteID from instituteplandetails IPD"
                + " inner join instituteplans IP on IP.LocalInstitutePlanID=IPD.LocalInstitutePlanID where   IPD.IsDeleted!=1  AND   IP.IsDeleted!=1  AND scheduleDate='"
                + date_in_loop + "' and (PlanStatusID=1 or PlanStatusID=2) ";
        Cursor scheduleCur = dbh.getCursorData(this.getActivity(),
                query_no_of_institutes);
        ArrayList<Institute> insList = new ArrayList<Institute>();
        // int instituteCount = 0;
        if (scheduleCur != null) {
            try {
                if (scheduleCur.moveToFirst()) {
                    do {
                        Institute insObj = new Institute();
                        insObj.setInstituteServerID(NumUtil.IntegerParse
                                .parseInt(scheduleCur.getString(scheduleCur
                                        .getColumnIndex("InstituteID"))));
                        insObj.setLocalInstitutePlanDetailID(NumUtil.IntegerParse.parseInt(scheduleCur.getString(scheduleCur
                                .getColumnIndex("LocalInstitutePlanDetailID"))));
                        insObj.setRBSKCalenarYearID(NumUtil.IntegerParse
                                .parseInt(scheduleCur.getString(scheduleCur
                                        .getColumnIndex("RBSKCalendarYearID"))));
                        insObj.setScreeningRoundID(NumUtil.IntegerParse
                                .parseInt(scheduleCur.getString(scheduleCur
                                        .getColumnIndex("ScreeningRoundId"))));
                        insList.add(insObj);
                    } while (scheduleCur.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                scheduleCur.close();
            }
        }
        return insList;
    }

    /**
     * To check the day of the week
     *
     * @param date_in_loop
     */

    private String getFirstWeekDay(int month, int year, int dd) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Calendar cal = Calendar.getInstance();
        System.out.println("month::" + month);
        System.out.println("year::" + year);
        cal.set(year, month - 1, dd);
        Date date = cal.getTime();
        dayOfTheWeek = sdf.format(date);
        return dayOfTheWeek;
    }

    /**
     * to find views from R.java
     */
    private void findViews(View rootView) {
        lv_day_calendar = (ListView) rootView
                .findViewById(R.id.lv_day_calendar);
        ll_schlist = (LinearLayout) rootView.findViewById(R.id.ll_schlist);

    }

}
