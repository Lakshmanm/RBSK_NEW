package nunet.rbsk.model;


//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  Habitation

//* Type    : Model Class

//* Description     : Model class for habitation
//* References     :                                                        
//* Author    :kiruthika.ganesan

//* Created Date       : 23-04-2015
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


public class Habitation {
	int HabitatID = 0;
	String HabitatNCode = "";
	String HabitatName = "";
	String Capital = "";
	int LocalVersionNo = 0;
	int GlobalVersionNo = 0;

	public int getHabitatID() {
		return HabitatID;
	}

	public void setHabitatID(int habitatID) {
		HabitatID = habitatID;
	}

	public String getHabitatNCode() {
		return HabitatNCode;
	}

	public void setHabitatNCode(String habitatNCode) {
		HabitatNCode = habitatNCode;
	}

	public String getHabitatName() {
		return HabitatName;
	}

	public void setHabitatName(String habitatName) {
		HabitatName = habitatName;
	}

	public String getCapital() {
		return Capital;
	}

	public void setCapital(String capital) {
		Capital = capital;
	}

	public int getLocalVersionNo() {
		return LocalVersionNo;
	}

	public void setLocalVersionNo(int localVersionNo) {
		LocalVersionNo = localVersionNo;
	}

	public int getGlobalVersionNo() {
		return GlobalVersionNo;
	}

	public void setGlobalVersionNo(int globalVersionNo) {
		GlobalVersionNo = globalVersionNo;
	}

}
