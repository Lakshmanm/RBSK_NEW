package nunet.custom;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  Add student Fragment

//* Type    : Custom calendar

//* Description     : Add Custom calendar
//* References     :                                                        
//* Author    :Kiruthika. Ganesan

//* Created Date       : 06-05-2015
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

import java.util.ArrayList;
import java.util.Calendar;

import nunet.rbsk.R;
import nunet.rbsk.dashboard.DashBoardActivity;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Event;
import nunet.rbsk.model.InstituteSchedule;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nunet.utils.StringUtils;

public class CustomCalendar extends LinearLayout {
	// instance variables

	private Context context;
	private Resources resources;

	// colors
	private int colorTextToday;
	private int colorBgToday;
	private int colorTextCurrMonthWeekday;
	private int colorBgCurrMonthWeekday;
	private int colorTextCurrMonthWeekend;
	private int colorTextNotCurrMonthWeekday;
	private int colorBgNotCurrMonthWeekday;
	private int colorTextNotCurrMonthWeekend;

	// calendars
	private Calendar currMontCal;
	private Calendar prevMontCal;

	// today
	private int todayYear;
	private int todayMonth;
	private int todayDay;
	private DBHelper dbh;

	public CustomCalendar(Context context) {
		// this.mContext = context;
		this(context, null);
		this.context = context;
		dbh = DBHelper.getInstance(context);
	}

	public CustomCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		dbh = DBHelper.getInstance(context);
		init();
	}

	/**
	 * Initiates all instance variables.
	 * */
	private void init() {
		// inflates a view from an XML resource
		View.inflate(context, R.layout.custom_calendar, this);

		resources = context.getResources();

		currMontCal = Calendar.getInstance();

		prevMontCal = Calendar.getInstance();
		prevMontCal.add(Calendar.MONTH, -1);

		todayYear = currMontCal.get(Calendar.YEAR);
		todayMonth = currMontCal.get(Calendar.MONTH);
		todayDay = currMontCal.get(Calendar.DAY_OF_MONTH);

		initColors();
	}

	/**
	 * Initiates all colors.
	 * */
	private void initColors() {
		colorTextToday = resources.getColor(R.color.text_today);
		colorBgToday = resources.getColor(R.color.bg_today);
		colorTextCurrMonthWeekday = resources
				.getColor(R.color.text_curr_month_weekday);
		colorBgCurrMonthWeekday = resources
				.getColor(R.color.bg_curr_month_weekday);
		colorTextCurrMonthWeekend = resources
				.getColor(R.color.text_curr_month_weekend);
		// colorBgCurrMonthWeekend = resources
		// .getColor(R.color.bg_curr_month_weekend);
		colorTextNotCurrMonthWeekday = resources
				.getColor(R.color.text_not_curr_month_weekday);
		colorBgNotCurrMonthWeekday = resources
				.getColor(R.color.bg_not_curr_month_weekday);
		colorTextNotCurrMonthWeekend = resources
				.getColor(R.color.text_not_curr_month_weekend);
		// colorBgNotCurrMonthWeekend = resources
		// .getColor(R.color.bg_not_curr_month_weekend);
	}

	/**
	 * Generates the days of a particular month from a particular year.
	 * 
	 * @param eventList
	 * @param scheduleList
	 * */
	public void generateDays(final int year, final int month,
			ArrayList<InstituteSchedule> scheduleList,
			ArrayList<Event> eventList) {
		int firstDayOfWeek;
		int lastDayPrevMonth;
		int lastDayCurrMonth;
		int satCount = 0;

		// sets a Calendar object for the current month
		currMontCal.set(Calendar.YEAR, year);
		currMontCal.set(Calendar.MONTH, month);
		currMontCal.set(Calendar.DAY_OF_MONTH, 1);

		// sets a Calendar object for the previous month
		prevMontCal.set(Calendar.YEAR, year);
		prevMontCal.set(Calendar.MONTH, month);
		prevMontCal.add(Calendar.MONTH, -1);

		// gets last day of the current and previous month
		lastDayPrevMonth = prevMontCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		lastDayCurrMonth = currMontCal.getActualMaximum(Calendar.DAY_OF_MONTH);

		// gets the first day of the current month as day of a week i.e. Monday,
		// Tuesday etc.
		firstDayOfWeek = currMontCal.get(Calendar.DAY_OF_WEEK);

		// converts the first day of the current month to Integer between 0 and
		// 6
		switch (firstDayOfWeek) {
		case Calendar.MONDAY: {
			firstDayOfWeek = 0;

			break;
		}
		case Calendar.TUESDAY: {
			firstDayOfWeek = 1;

			break;
		}
		case Calendar.WEDNESDAY: {
			firstDayOfWeek = 2;

			break;
		}
		case Calendar.THURSDAY: {
			firstDayOfWeek = 3;

			break;
		}
		case Calendar.FRIDAY: {
			firstDayOfWeek = 4;

			break;
		}
		case Calendar.SATURDAY: {
			firstDayOfWeek = 5;

			break;
		}
		case Calendar.SUNDAY: {
			firstDayOfWeek = 6;

			break;
		}
		}

		// loops through all 42 days
		for (int i = 0; i < 42; i++) {
			final TextView tvCurrDay, tvCurrSchedule;
			boolean isToday;
			boolean isPrevMonth;
			boolean isNextMonth;
			boolean isWeekend;
			final int row;
			final int index;
			int dayNumber;
			String strDayNumber;

			row = i / 7;
			index = i % 7;
			isToday = false;
			isPrevMonth = false;
			isNextMonth = false;
			isWeekend = false;

			// calculates the current day number and the status of the current
			// day
			if (i < firstDayOfWeek) {
				// previous month
				isPrevMonth = true;
				dayNumber = lastDayPrevMonth - (firstDayOfWeek - i) + 1;
			} else if (i >= firstDayOfWeek
					&& i < (lastDayCurrMonth + firstDayOfWeek)) {
				// current month
				dayNumber = (i - firstDayOfWeek) + 1;

				if (todayDay == dayNumber
						&& todayMonth == currMontCal.get(Calendar.MONTH)
						&& todayYear == currMontCal.get(Calendar.YEAR)) {
					isToday = true;
				}
			} else {
				// next month
				isNextMonth = true;

				dayNumber = (i % (lastDayCurrMonth + firstDayOfWeek)) + 1;
			}

			if (i % 7 >= 6) {
				isWeekend = true;
			}

			// gets a TextView for the current day
			tvCurrDay = getTextView(row, index);
			tvCurrSchedule = getScheduleTextView(row, index);
			strDayNumber = dayNumber + "";
			modifyTextViewSchedule(tvCurrSchedule, colorTextCurrMonthWeekday,
					colorBgCurrMonthWeekday, "");

			// To set schedule to the Calendar
			if (scheduleList != null) {
				String currentMonthString = String.valueOf(month + 1);
				if (currentMonthString.length() == 1) {
					currentMonthString = "0" + currentMonthString;
				}

				String currentDayString = String.valueOf(strDayNumber);
				if (currentDayString.length() == 1) {
					currentDayString = "0" + currentDayString;
				}
				String dateString = year + "-" + currentMonthString + "-"
						+ currentDayString;
				for (InstituteSchedule schedule : scheduleList) {
					String scheduleDate = schedule.getScheduleDate();
					if (StringUtils.equalsNoCase(dateString, scheduleDate)) {
						int count = getNumberofStudents(dateString);
						if ((!isNextMonth) && (!isPrevMonth)) {
							modifyTextViewSchedule(tvCurrSchedule,
									colorTextCurrMonthWeekday,
									colorBgCurrMonthWeekday, "Target - "
											+ count);
						}
					}
				}
			}

			// To set Event(Holiday) to the Calendar
			if (eventList != null) {
				String currentMonthString = String.valueOf(month + 1);
				if (currentMonthString.length() == 1) {
					currentMonthString = "0" + currentMonthString;
				}

				String currentDayString = String.valueOf(strDayNumber);
				if (currentDayString.length() == 1) {
					currentDayString = "0" + currentDayString;
				}
				String dateString = year + "-" + currentMonthString + "-"
						+ currentDayString;
				for (Event eventObj : eventList) {
					String scheduleDate = eventObj.getScheduleDate();
					if (StringUtils.equalsNoCase(dateString, scheduleDate)) {
						if ((!isNextMonth) && (!isPrevMonth)) {
							isWeekend = false;
							modifyTextViewSchedule(tvCurrSchedule,
									colorTextCurrMonthWeekday,
									colorBgCurrMonthWeekday,
									eventObj.getScheduleDescription());
						}
					}

				}
			}

			// To set Second Saturday as holiday to the Calendar
			if (i % 7 == 5) {
				if (!isPrevMonth) {
					satCount++;
					if (satCount == 2) {
						if ((!isNextMonth) && (!isPrevMonth)) {
							isWeekend = true;
							modifyTextViewSchedule(tvCurrSchedule,
									colorTextCurrMonthWeekday,
									colorBgCurrMonthWeekday, "Second Saturday");
						}
						// satCount=0;
					}
				}
			}

			// modifies the TextView properties
			if (isToday) {
				modifyToday(tvCurrDay, tvCurrSchedule, strDayNumber);
			} else if (isPrevMonth || isNextMonth) {
				if (isWeekend) {
					tvCurrDay.setEnabled(false);
					modifyNotCurrMonthWeekend(tvCurrDay, strDayNumber);
				} else {
					tvCurrDay.setEnabled(true);
					modifyNotCurrMonthWeekday(tvCurrDay, strDayNumber);
				}
			} else {
				if (isWeekend) {
					tvCurrDay.setEnabled(false);
					modifyCurrMonthWeekend(tvCurrDay, strDayNumber);
				} else {
					tvCurrDay.setEnabled(true);
					modifyCurrMonthWeekday(tvCurrDay, strDayNumber);
				}
			}

			// Click Listener for Calendar View
			View parent = (View) tvCurrDay.getParent();
			if (tvCurrDay.isEnabled())
				parent.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						DashBoardActivity.btn_schedule_monthview
								.setVisibility(View.VISIBLE);
						DashBoardActivity.btn_schedule_dayview
								.setVisibility(View.GONE);
						DashBoardActivity.isMonthView = false;
						DashBoardActivity.isDayView = true;
						((DashBoardActivity) context).displayView(
								DashBoardActivity.btn_schedule_dayview,
								getContext(), NumUtil.IntegerParse
										.parseInt(tvCurrDay.getText()
												.toString()));
					}
				});
		}
	}

	/**
	 * To get number of students to screened on Particular day
	 * 
	 * @param dateString
	 *            Kiruthika 07/05/2015
	 * @return Number of students to screened
	 */
	private int getNumberofStudents(String dateString) {
		int studentCount = 0;
		String query_no_of_institutes = "Select sum(PlannedCount) as StudentCount from instituteplandetails IPD "
				+ " where   IPD.IsDeleted!=1  AND scheduleDate='"
				+ dateString
				+ "'";
		Cursor scheduleCur = dbh.getCursorData(context, query_no_of_institutes);
		if (scheduleCur != null)

		{
			try {
				if (scheduleCur.moveToFirst()) {
					do {
						String StudentCount = scheduleCur.getString(scheduleCur
								.getColumnIndex("StudentCount"));
						if (!TextUtils.isEmpty(StudentCount)) {
							studentCount = NumUtil.IntegerParse
									.parseInt(StudentCount);
						}
					} while (scheduleCur.moveToNext());
				}
			} finally {
				scheduleCur.close();
			}
		}
		return studentCount;
	}

	/**
	 * Method to set text for textview for event and schedule Kiruthika
	 * 07/05/2015
	 * 
	 * @param tvCurrSchedule
	 * @param colorTextCurrMonthWeekday2
	 * @param colorBgCurrMonthWeekday2
	 * @param scheduleDescription
	 */
	private void modifyTextViewSchedule(TextView tvCurrSchedule,
			int colorTextCurrMonthWeekday2, int colorBgCurrMonthWeekday2,
			String scheduleDescription) {
		tvCurrSchedule.setTextColor(colorTextCurrMonthWeekday2);
		tvCurrSchedule.setBackgroundColor(colorBgCurrMonthWeekday2);
		tvCurrSchedule.setText(scheduleDescription);
		tvCurrSchedule.setTextSize(17);
	}

	/**
	 * Method to get Schedule TextView Kiruthika 07/05/2015
	 * 
	 * @param row
	 * @param index
	 * @return
	 */
	private TextView getScheduleTextView(int row, int index) {
		int rowIndex;
		int childIndex;

		rowIndex = (row + 1) * 2;
		childIndex = index * 2;

		LinearLayout ll_single = (LinearLayout) getChildAt(0);
		LinearLayout ll_row = (LinearLayout) (ll_single).getChildAt(rowIndex);
		LinearLayout ll_full = (LinearLayout) (ll_row).getChildAt(childIndex);
		TextView tv_view = (TextView) ll_full.getChildAt(1);
		return tv_view;
	}

	private void modifyToday(TextView tv, TextView tvCurrSchedule, String text) {
		modifyTodayTextView(tv, tvCurrSchedule, colorTextToday, colorBgToday,
				text);
	}

	/**
	 * @param tv
	 * @param tvCurrSchedule
	 * @param colorTextToday2
	 * @param colorBgToday2
	 * @param text
	 */
	private void modifyTodayTextView(TextView tv, TextView tvCurrSchedule,
			int colorTextToday2, int colorBgToday2, String text) {
		tv.setTextColor(colorTextToday2);
		tv.setBackgroundColor(colorBgToday2);
		tvCurrSchedule.setBackgroundColor(colorBgToday2);
		tv.setTextSize(17);
		tv.setText(text);
		Helper.currentDate = text;
	}

	private void modifyCurrMonthWeekday(TextView tv, String text) {
		modifyTextView(tv, colorTextCurrMonthWeekday, colorBgCurrMonthWeekday,
				text);
	}

	private void modifyCurrMonthWeekend(TextView tv, String text) {
		modifyTextView(tv, colorTextCurrMonthWeekend, colorBgCurrMonthWeekday,
				text);
	}

	private void modifyNotCurrMonthWeekday(TextView tv, String text) {
		modifyTextView(tv, colorTextNotCurrMonthWeekday,
				colorBgNotCurrMonthWeekday, text);
	}

	private void modifyNotCurrMonthWeekend(TextView tv, String text) {
		modifyTextView(tv, colorTextNotCurrMonthWeekend,
				colorBgCurrMonthWeekday, text);
	}

	/**
	 * This method modifies the text color, background color and text of a given
	 * TextView object.
	 * */
	private void modifyTextView(TextView tv, int textColor, int bgColor,
			String text) {
		tv.setTextColor(textColor);
		tv.setBackgroundColor(bgColor);
		tv.setTextSize(17);
		tv.setText(text);
	}

	/**
	 * Returns a TextView object by a given row and index.
	 * */
	private TextView getTextView(int row, int index) {
		int rowIndex;
		int childIndex;

		rowIndex = (row + 1) * 2;
		childIndex = index * 2;

		LinearLayout ll_single = (LinearLayout) getChildAt(0);
		LinearLayout ll_row = (LinearLayout) (ll_single).getChildAt(rowIndex);
		LinearLayout ll_full = (LinearLayout) (ll_row).getChildAt(childIndex);
		TextView tv_view = (TextView) ll_full.getChildAt(0);
		return tv_view;
	}
}