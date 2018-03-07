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
//* Name   :  Mandal

//* Type    : Model Class

//* Description     : Model class for Mandal
//* References     :                                                        
//* Author    :kiruthika.ganesan

//* Created Date       : 24-04-2015
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


public class Mandal {

	int MandalID = 0;
	String MandalNCode = "";
	String MandalName = "";
	String Capital = "";
	int LocalVersionNo = 0;
	int GlobalVersionNo = 0;

	ArrayList<Panchayat> panchayatList;

	public int getMandalID() {
		return MandalID;
	}

	public void setMandalID(int mandalID) {
		MandalID = mandalID;
	}

	public String getMandalNCode() {
		return MandalNCode;
	}

	public void setMandalNCode(String mandalNCode) {
		MandalNCode = mandalNCode;
	}

	public String getMandalName() {
		return MandalName;
	}

	public void setMandalName(String mandalName) {
		MandalName = mandalName;
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

	public ArrayList<Panchayat> getPanchayatList() {
		return panchayatList;
	}

	public void setPanchayatList(ArrayList<Panchayat> panchayatList) {
		this.panchayatList = panchayatList;
	}

}
