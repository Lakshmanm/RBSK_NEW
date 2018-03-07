/**
 * 
 */
package nunet.rbsk.model;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  Contacts

//* Type    : Model Class

//* Description     : Model class for Contacts
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

public class Contacts {

	int ContactID;
	String Contact = "";
	int ContactTypeID;
	String ContactTypeName = "";
	int ContactCategoryID;
	String ContactCategoryName = "";

	public int getContactID() {
		return ContactID;
	}

	public void setContactID(int contactID) {
		ContactID = contactID;
	}

	public String getContact() {
		return Contact;
	}

	public void setContact(String contact) {
		Contact = contact;
	}

	public int getContactTypeID() {
		return ContactTypeID;
	}

	public void setContactTypeID(int contactTypeID) {
		ContactTypeID = contactTypeID;
	}

	public String getContactTypeName() {
		return ContactTypeName;
	}

	public void setContactTypeName(String contactTypeName) {
		ContactTypeName = contactTypeName;
	}

	public int getContactCategoryID() {
		return ContactCategoryID;
	}

	public void setContactCategoryID(int contactCategoryID) {
		ContactCategoryID = contactCategoryID;
	}

	public String getContactCategoryName() {
		return ContactCategoryName;
	}

	public void setContactCategoryName(String contactCategoryName) {
		ContactCategoryName = contactCategoryName;
	}

}
