//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  HealthConditionModel.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  03-Jun-2015
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
public class HealthConditionModel {
	private int mHealthConditionID;
	String name = "";
	String groupName = "";
	boolean update = false;
	
	private HCTypes mHCType ;

	public enum HCTypes {
		DoctorComments, Recommendations, LocalTreatment, HealthCondition
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public int getHealthConditionID() {
		return mHealthConditionID;
	}

	public void setHealthConditionID(int mHealthConditionID) {
		this.mHealthConditionID = mHealthConditionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public HCTypes getHCType() {
		return mHCType;
	}

	public void setHCType(HCTypes mHCType) {
		this.mHCType = mHCType;
	}

}
