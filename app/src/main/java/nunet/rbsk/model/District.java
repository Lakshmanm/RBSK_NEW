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
//* Name   :  District

//* Type    : Model Class

//* Description     : Model class for District
//* References     :                                                        
//* Author    :kiruthika.ganesan

//* Created Date       : 22-04-2015
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


public class District {

	int DistrictID = 0;
	// int StateID =0;
	String DistrictNCode = "";
	String DistrictName = "";
	String Capital = "";
	int LocalVersionNo = 0;
	int GlobalVersionNo = 0;

	ArrayList<Mandal> mandalList;

	public int getDistrictID() {
		return DistrictID;
	}

	public void setDistrictID(int districtID) {
		DistrictID = districtID;
	}

	public String getDistrictNCode() {
		return DistrictNCode;
	}

	public void setDistrictNCode(String districtNCode) {
		DistrictNCode = districtNCode;
	}

	public String getDistrictName() {
		return DistrictName;
	}

	public void setDistrictName(String districtName) {
		DistrictName = districtName;
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

	public ArrayList<Mandal> getMandalList() {
		return mandalList;
	}

	public void setMandalList(ArrayList<Mandal> mandalList) {
		this.mandalList = mandalList;
	}

}
