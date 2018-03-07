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
//* Name   :  State

//* Type    : Model Class

//* Description     : Model class for state
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


public class State {

	int StateID = 0;
	int CountryID = 0;
	String StateNcode = "";
	String StateName = "";
	String Capital = "";
	int LocalVersionNo = 0;
	int GlobalVersionNo = 0;

	ArrayList<District> districtList;

	public int getStateID() {
		return StateID;
	}

	public void setStateID(int stateID) {
		StateID = stateID;
	}

	public int getCountryID() {
		return CountryID;
	}

	public void setCountryID(int countryID) {
		CountryID = countryID;
	}

	public String getStateNcode() {
		return StateNcode;
	}

	public void setStateNcode(String stateNcode) {
		StateNcode = stateNcode;
	}

	public String getStateName() {
		return StateName;
	}

	public void setStateName(String stateName) {
		StateName = stateName;
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

	public ArrayList<District> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(ArrayList<District> districtList) {
		this.districtList = districtList;
	}

}
