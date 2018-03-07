package nunet.rbsk.model;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  Address

//* Type    : Model Class

//* Description     : Model class for address
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

public class Address {

	int AddressID = 0;
	String AddressName = "";
	String AddressLine1 = "";
	String AddressLine2 = "";
	String LandMark = "";
	String PINCode = "";
	String Post = "";
	String DriveInstructions = "";
	State state;
	District district;
	Mandal mandal;
	Panchayat panchayat;
	Village village;
	Habitation habitation;

	public int getAddressID() {
		return AddressID;
	}

	public void setAddressID(int addressID) {
		AddressID = addressID;
	}

	public String getAddressName() {
		return AddressName;
	}

	public void setAddressName(String addressName) {
		AddressName = addressName;
	}

	public String getAddressLine1() {
		return AddressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		AddressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return AddressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		AddressLine2 = addressLine2;
	}

	public String getLandMark() {
		return LandMark;
	}

	public void setLandMark(String landMark) {
		LandMark = landMark;
	}

	public String getPINCode() {
		return PINCode;
	}

	public void setPINCode(String pINCode) {
		PINCode = pINCode;
	}

	public String getPost() {
		return Post;
	}

	public void setPost(String post) {
		Post = post;
	}

	public String getDriveInstructions() {
		return DriveInstructions;
	}

	public void setDriveInstructions(String driveInstructions) {
		DriveInstructions = driveInstructions;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Mandal getMandal() {
		return mandal;
	}

	public void setMandal(Mandal mandal) {
		this.mandal = mandal;
	}

	public Panchayat getPanchayat() {
		return panchayat;
	}

	public void setPanchayat(Panchayat panchayat) {
		this.panchayat = panchayat;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public Habitation getHabitation() {
		return habitation;
	}

	public void setHabitation(Habitation habitation) {
		this.habitation = habitation;
	}

}
