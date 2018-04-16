//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.dashboard;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nunet.custom.CustomCalendar;
import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Event;
import nunet.rbsk.model.InstituteSchedule;

//*****************************************************************************
//* Name   :  MonthViewCalender.java

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

//*****************************************************************************
public class MonthViewCalender extends Fragment {


    private CustomCalendar customCalendar;
    private ArrayList<InstituteSchedule> scheduleList;
    private ArrayList<Event> eventList;
    private int monthSelected;
    private int yearSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.month_view_calendar,
                container, false);

        monthSelected = getArguments().getInt("Month");// Get selected Month
        yearSelected = getArguments().getInt("Year");// Get selected Year
        scheduleList = Helper.scheduleList;// Get Schedule list
        eventList = Helper.eventList;// Get event List
        findViews(rootView);
        main();

        return rootView;
    }

    /**
     * Method to get id of all views Kiruthika 05/05/2015
     */
    private void findViews(View rootView) {
        customCalendar = (CustomCalendar) rootView
                .findViewById(R.id.custom_calendar);
    }

    private void main() {
        updateCalendar();
    }

    /**
     * Method to Display custom calendar Kiruthika 06/05/2015
     */
    private void updateCalendar() {

        customCalendar.generateDays(yearSelected, monthSelected, scheduleList,
                eventList);

        if (Helper.progressDialog != null && Helper.progressDialog.isShowing()) {
            Helper.progressDialog.dismiss();
        }


    }


}
