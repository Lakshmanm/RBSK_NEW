//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  Instituteplandetails.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

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
public class Instituteplandetails {
	String scheduleDate = "";
	int PlannedCount;
	int isDeleted;
	int ScreeningRoundId;
	int InstitutePlanStatusID;
	Institute institute;

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public int getPlannedCount() {
		return PlannedCount;
	}

	public void setPlannedCount(int plannedCount) {
		PlannedCount = plannedCount;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getScreeningRoundId() {
		return ScreeningRoundId;
	}

	public void setScreeningRoundId(int screeningRoundId) {
		ScreeningRoundId = screeningRoundId;
	}

	public int getInstitutePlanStatusID() {
		return InstitutePlanStatusID;
	}

	public void setInstitutePlanStatusID(int institutePlanStatusID) {
		InstitutePlanStatusID = institutePlanStatusID;
	}

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}
}
