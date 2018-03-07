package nunet.rbsk.model;

import java.util.ArrayList;


//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  Panchayat

//* Type    : Model Class

//* Description     : Model class for Panchayat
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


public class Panchayat {

	int PanchayatID = 0;
	String PanchayatNCode = "";
	String PanchayatName = "";
	String Capital = "";
	int LocalVersionNo = 0;
	int GlobalVersionNo = 0;

	ArrayList<Village> villageList;

	public int getPanchayatID() {
		return PanchayatID;
	}

	public void setPanchayatID(int panchayatID) {
		PanchayatID = panchayatID;
	}

	public String getPanchayatNCode() {
		return PanchayatNCode;
	}

	public void setPanchayatNCode(String panchayatNCode) {
		PanchayatNCode = panchayatNCode;
	}

	public String getPanchayatName() {
		return PanchayatName;
	}

	public void setPanchayatName(String panchayatName) {
		PanchayatName = panchayatName;
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

	public ArrayList<Village> getVillageList() {
		return villageList;
	}

	public void setVillageList(ArrayList<Village> villageList) {
		this.villageList = villageList;
	}

}
