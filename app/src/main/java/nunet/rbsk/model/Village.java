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
//* Name   :  Village

//* Type    : Model Class

//* Description     : Model class for Village
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


public class Village {

	int VillageID = 0;
	String VillageNCode = "";
	String VillageName = "";
	String Capital = "";
	int LocalVersionNo = 0;
	int GlobalVersionNo = 0;
	ArrayList<Habitation> habitationList;

	public int getVillageID() {
		return VillageID;
	}

	public void setVillageID(int villageID) {
		VillageID = villageID;
	}

	public String getVillageNCode() {
		return VillageNCode;
	}

	public void setVillageNCode(String villageNCode) {
		VillageNCode = villageNCode;
	}

	public String getVillageName() {
		return VillageName;
	}

	public void setVillageName(String villageName) {
		VillageName = villageName;
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

	

	public ArrayList<Habitation> getHabitationList() {
		return habitationList;
	}

	public void setHabitationList(ArrayList<Habitation> habitationList) {
		this.habitationList = habitationList;
	}

}
