//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  Gender.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  30-Apr-2015
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
public class Gender {
	int GenderID;
	String GenderCode = "";
	int GenderNCode;
	String GenderName = "";

	public int getGenderID() {
		return GenderID;
	}

	public void setGenderID(int genderID) {
		GenderID = genderID;
	}

	public String getGenderCode() {
		return GenderCode;
	}

	public void setGenderCode(String genderCode) {
		GenderCode = genderCode;
	}

	public int getGenderNCode() {
		return GenderNCode;
	}

	public void setGenderNCode(int genderNCode) {
		GenderNCode = genderNCode;
	}

	public String getGenderName() {
		return GenderName;
	}

	public void setGenderName(String genderName) {
		GenderName = genderName;
	}

}
