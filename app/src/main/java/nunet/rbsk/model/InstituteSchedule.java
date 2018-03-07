//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  InstituteSchedule.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  07-May-2015
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
public class InstituteSchedule extends Event {
	private int PlannedCount;
	private boolean isDeleted = false;
	private int ScreeningRoundId;
	private int InstitutePlanStatusID;
	private Institute institute;
	

	public int getPlannedCount() {
		return PlannedCount;
	}

	public void setPlannedCount(int plannedCount) {
		PlannedCount = plannedCount;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
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
