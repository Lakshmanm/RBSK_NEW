//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  ChildrenDisabilities.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  27-May-2015
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
public class ChildrenDisabilities {

	int ChildrenID;
	String DisabilityPercentage = "";
	String SpecificCondition = "";
	DisabilityTypes disabilityType;
	int disabilityId;
	

	/**
	 * @return the disabilityId
	 */
	public int getDisabilityId() {
		return disabilityId;
	}

	/**
	 * @param disabilityId the disabilityId to set
	 */
	public void setDisabilityId(int disabilityId) {
		this.disabilityId = disabilityId;
	}

	public DisabilityTypes getDisabilityType() {
		return disabilityType;
	}

	public void setDisabilityType(DisabilityTypes disabilityType) {
		this.disabilityType = disabilityType;
	}

	public int getChildrenID() {
		return ChildrenID;
	}

	public void setChildrenID(int childrenID) {
		ChildrenID = childrenID;
	}

	public String getDisabilityPercentage() {
		return DisabilityPercentage;
	}

	public void setDisabilityPercentage(String disabilityPercentage) {
		DisabilityPercentage = disabilityPercentage;
	}

	public String getSpecificCondition() {
		return SpecificCondition;
	}

	public void setSpecificCondition(String specificCondition) {
		SpecificCondition = specificCondition;
	}

}
