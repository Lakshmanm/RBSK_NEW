//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

import java.util.ArrayList;
import java.util.HashMap;

//*****************************************************************************
//* Name   :  Referral.java

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
public class Referral {

	HealthConditionModel healthConditonReferred;
	int healthCondtionId;
	String healthCondtionName = "";
	int referalPlaceId;
	String referralPlaceName = "";
	ArrayList<HashMap<String, String>> investigations;
	String investigationsStr = "";
	String comments = "";
	private int FacilityID;
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInvestigationsStr() {
		return investigationsStr;
	}

	public void setInvestigationsStr(String investigationsStr) {
		this.investigationsStr = investigationsStr;
	}

	public int getHealthCondtionId() {
		return healthCondtionId;
	}

	public void setHealthCondtionId(int healthCondtionId) {
		this.healthCondtionId = healthCondtionId;
	}

	public String getHealthCondtionName() {
		return healthCondtionName;
	}

	public void setHealthCondtionName(String healthCondtionName) {
		this.healthCondtionName = healthCondtionName;
	}

	public int getReferalPlaceId() {
		return referalPlaceId;
	}

	public void setReferalPlaceId(int referalPlaceId) {
		this.referalPlaceId = referalPlaceId;
	}

	public String getReferralPlaceName() {
		return referralPlaceName;
	}

	public void setReferralPlaceName(String referralPlaceName) {
		this.referralPlaceName = referralPlaceName;
	}

	

	public ArrayList<HashMap<String, String>> getInvestigations() {
		return investigations;
	}

	public void setInvestigations(ArrayList<HashMap<String, String>> investigations) {
		this.investigations = investigations;
	}

	public HealthConditionModel getHealthConditonReferred() {
		return healthConditonReferred;
	}

	public void setHealthConditonReferred(
			HealthConditionModel healthConditonReferred) {
		this.healthConditonReferred = healthConditonReferred;
	}

	public int getFacilityID() {
		return FacilityID;
	}

	public void setFacilityID(int facilityID) {
		FacilityID = facilityID;
	}

}
