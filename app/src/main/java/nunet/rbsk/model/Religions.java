//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  Religions.java

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
public class Religions {
	int ReligionID;
	String ReligionCode = "";
	String ReligionNCode = "";
	String religionName = "";

	public int getReligionID() {
		return ReligionID;
	}

	public void setReligionID(int religionID) {
		ReligionID = religionID;
	}

	public String getReligionCode() {
		return ReligionCode;
	}

	public void setReligionCode(String religionCode) {
		ReligionCode = religionCode;
	}

	public String getReligionNCode() {
		return ReligionNCode;
	}

	public void setReligionNCode(String religionNCode) {
		ReligionNCode = religionNCode;
	}

	public String getReligionName() {
		return religionName;
	}

	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}

}
