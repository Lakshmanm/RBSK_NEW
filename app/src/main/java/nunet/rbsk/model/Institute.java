package nunet.rbsk.model;

import java.util.ArrayList;
import java.util.Calendar;

import com.nunet.utils.DateUtil;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  Institute

//* Type    : Model Class

//* Description     : Model class for Institute
//* References     :                                                        
//* Author    :kiruthika.ganesan

//* Created Date       : 21-04-2015
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

public class Institute {

	int LocalInstituteID = 0;
	String InstituteName = "";
	String InstituteNCode = "";
	int InstituteTypeId;
	String InstituteTypeName = "";
	String DiseCode = "";
	int SchoolCategoryID = 0;
	String CategoryCode = "";
	int InstituteCategoryID = 0;
	String InstituteCategoryName = "";
	int DepartmentID = 0;
	// int AddressID=0 ;
	int ContactID = 0;
	String Longitude = "";
	String Latitude = "";
	String SchoolWebsite = "";
	int StatusID = 0;
	int RBSKCalenarYearID = 0;
	private String scheduledPlanDate = "";

	private long InstitutePlanDetailID;

	private long LocalInstitutePlanDetailID;

	private long PlanStatusID;

	private double ScreenedPercentage;

	public double getScreenedPercentage() {
		return ScreenedPercentage;
	}

	public void setScreenedPercentage(double screenedPercentage) {
		ScreenedPercentage = screenedPercentage;
	}

	public long getInstitutePlanDetailID() {
		return InstitutePlanDetailID;
	}

	public void setInstitutePlanDetailID(long institutePlanDetailID) {
		InstitutePlanDetailID = institutePlanDetailID;
	}

	public int getRBSKCalenarYearID() {
		return RBSKCalenarYearID;
	}

	public void setRBSKCalenarYearID(int rBSKCalenarYearID) {
		RBSKCalenarYearID = rBSKCalenarYearID;
	}

	public int getScreeningRoundID() {
		return ScreeningRoundID;
	}

	public void setScreeningRoundID(int screeningRoundID) {
		ScreeningRoundID = screeningRoundID;
	}

	public int getInstitutePlanID() {
		return InstitutePlanID;
	}

	public void setInstitutePlanID(int institutePlanID) {
		InstitutePlanID = institutePlanID;
	}

	int ScreeningRoundID = 0;
	int InstitutePlanID = 0;
	int InstituteServerID = 0;

	public int getInstituteServerID() {
		return InstituteServerID;
	}

	public void setInstituteServerID(int instituteServerID) {
		InstituteServerID = instituteServerID;
	}

	Address address;

	ArrayList<Contacts> contacts;

	public ArrayList<Contacts> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contacts> contacts) {
		this.contacts = contacts;
	}

	public int getLocalInstituteID() {
		return LocalInstituteID;
	}

	public void setLocalInstituteID(int instituteID) {
		LocalInstituteID = instituteID;
	}

	public String getInstituteName() {
		return InstituteName;
	}

	public void setInstituteName(String instituteName) {
		InstituteName = instituteName;
	}

	public String getInstituteNCode() {
		return InstituteNCode;
	}

	public void setInstituteNCode(String instituteNCode) {
		InstituteNCode = instituteNCode;
	}

	public int getInstituteTypeId() {
		return InstituteTypeId;
	}

	public void setInstituteTypeId(int instituteTypeId) {
		InstituteTypeId = instituteTypeId;
	}

	public String getInstituteTypeName() {
		return InstituteTypeName;
	}

	public void setInstituteTypeName(String instituteTypeName) {
		InstituteTypeName = instituteTypeName;
	}

	public String getCategoryCode() {
		return CategoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		CategoryCode = categoryCode;
	}

	public String getInstituteCategoryName() {
		return InstituteCategoryName;
	}

	public void setInstituteCategoryName(String instituteCategoryName) {
		InstituteCategoryName = instituteCategoryName;
	}

	public String getDiseCode() {
		return DiseCode;
	}

	public void setDiseCode(String diseCode) {
		DiseCode = diseCode;
	}

	public int getSchoolCategoryID() {
		return SchoolCategoryID;
	}

	public void setSchoolCategoryID(int schoolCategoryID) {
		SchoolCategoryID = schoolCategoryID;
	}

	public int getInstituteCategoryID() {
		return InstituteCategoryID;
	}

	public void setInstituteCategoryID(int instituteCategoryID) {
		InstituteCategoryID = instituteCategoryID;
	}

	public int getDepartmentID() {
		return DepartmentID;
	}

	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
	}

	public int getContactID() {
		return ContactID;
	}

	public void setContactID(int contactID) {
		ContactID = contactID;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getSchoolWebsite() {
		return SchoolWebsite;
	}

	public void setSchoolWebsite(String schoolWebsite) {
		SchoolWebsite = schoolWebsite;
	}

	public int getStatusID() {
		return StatusID;
	}

	public void setStatusID(int statusID) {
		StatusID = statusID;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getCurrentDateTime() {
		return (String) DateUtil.format("yyyy-MM-dd HH:mm:ss",
				Calendar.getInstance());
	}

	public String getScheduledPlanDate() {
		return scheduledPlanDate;
	}

	public void setScheduledPlanDate(String scheduledPlanDate) {
		this.scheduledPlanDate = scheduledPlanDate;
	}

	public long getLocalInstitutePlanDetailID() {
		return LocalInstitutePlanDetailID;
	}

	public void setLocalInstitutePlanDetailID(long localInstitutePlanDetailID) {
		LocalInstitutePlanDetailID = localInstitutePlanDetailID;
	}

	public long getPlanStatusID() {
		return PlanStatusID;
	}

	public void setPlanStatusID(long planStatusID) {
		PlanStatusID = planStatusID;
	}

}
