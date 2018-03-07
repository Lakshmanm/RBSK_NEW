//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

import java.util.ArrayList;

//*****************************************************************************
//* Name   :  DaySchedule.java

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
public class DaySchedule {
	private int day;
	private String noOfSchools;
	private String weekday;
	private String holiday;
	private boolean isSelected;
	private ArrayList<Institute> institutes;
	
	
	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}
	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the institutes
	 */
	public ArrayList<Institute> getInstitutes() {
		return institutes;
	}
	/**
	 * @param institutes the institutes to set
	 */
	public void setInstitutes(ArrayList<Institute> institutes) {
		this.institutes = institutes;
	}
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	/**
	 * @return the noOfSchools
	 */
	public String getNoOfSchools() {
		return noOfSchools;
	}
	/**
	 * @param noOfSchools the noOfSchools to set
	 */
	public void setNoOfSchools(String noOfSchools) {
		this.noOfSchools = noOfSchools;
	}
	/**
	 * @return the weekday
	 */
	public String getWeekday() {
		return weekday;
	}
	/**
	 * @param weekday the weekday to set
	 */
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	
	/**
	 * @return the holiday
	 */
	public String getHoliday() {
		return holiday;
	}
	/**
	 * @param day the day to set
	 */
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
}
