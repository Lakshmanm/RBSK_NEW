/**
 * 
 */
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
//* Name   :  Staff

//* Type    : Model Class

//* Description     : Model class for staff details
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

public class Users {

	long UserID = 0;
	int UserTypeID = 0;
	String FirstName = "";
	String MiddleName = "";
	String LastName = "";
	int DesignationID;
	String DesignationName = "";
	int DepartmentID;
	String DepartmentName = "";
	int ContactID;

	int genderID;
	Gender gender;

	ArrayList<Contacts> contacts;
	Address address;
	// User Table update

	String AadharNumber = "";
	String HealthCardNumber = "";
	String RationCardNumber = "";
	int Age;
	String DateOfBirth = "";

	Salutation salutation;
	int casteID;
	int religionID;

	// Gender gender;

	public int getUserTypeID() {
		return UserTypeID;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setUserTypeID(int userTypeID) {
		UserTypeID = userTypeID;
	}

	public int getDepartmentID() {
		return DepartmentID;
	}

	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
	}

	public String getDepartmentName() {
		return DepartmentName;
	}

	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}

	public int getContactID() {
		return ContactID;
	}

	public void setContactID(int contactID) {
		ContactID = contactID;
	}

	public long getUserID() {
		return UserID;
	}

	public void setUserID(long userID) {
		UserID = userID;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getMiddleName() {
		return MiddleName;
	}

	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public int getDesignationID() {
		return DesignationID;
	}

	public void setDesignationID(int designationID) {
		DesignationID = designationID;
	}

	public String getDesignationName() {
		return DesignationName;
	}

	public void setDesignationName(String designationName) {
		DesignationName = designationName;
	}

	public ArrayList<Contacts> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contacts> contacts) {
		this.contacts = contacts;
	}

	private String EmgContact;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getAadharNumber() {
		return AadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		AadharNumber = aadharNumber;
	}

	public String getHealthCardNumber() {
		return HealthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		HealthCardNumber = healthCardNumber;
	}

	public String getRationCardNumber() {
		return RationCardNumber;
	}

	public void setRationCardNumber(String rationCardNumber) {
		RationCardNumber = rationCardNumber;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public int getAgeInMonths() {
		return Age;
	}

	public void setAgeInMonths(int age) {
		Age = age;
	}

	public Salutation getSalutation() {
		return salutation;
	}

	public void setSalutation(Salutation salutation) {
		this.salutation = salutation;
	}

	public int getCasteID() {
		return casteID;
	}

	public void setCasteID(int casteID) {
		this.casteID = casteID;
	}

	public int getReligionID() {
		return religionID;
	}

	public void setReligionID(int religionID) {
		this.religionID = religionID;
	}

	public int getGenderID() {
		return genderID;
	}

	public void setGenderID(int genderID) {
		this.genderID = genderID;
	}

	public String getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	public String getEmgContact() {
		return EmgContact;
	}

	public void setEmgContact(String emgContact) {
		EmgContact = emgContact;
	}

}
