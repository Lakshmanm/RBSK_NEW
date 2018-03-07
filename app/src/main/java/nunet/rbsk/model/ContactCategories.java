//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  ContactType.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  01-May-2015
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
public class ContactCategories {

	int ContactCategoryID;
	String ContactCategoryCode = "";
	String ContactCategoryNCode = "";
	String ContactCategoryName = "";

	public int getContactCategoryID() {
		return ContactCategoryID;
	}

	public void setContactCategoryID(int contactCategoryID) {
		ContactCategoryID = contactCategoryID;
	}

	public String getContactCategoryCode() {
		return ContactCategoryCode;
	}

	public void setContactCategoryCode(String contactCategoryCode) {
		ContactCategoryCode = contactCategoryCode;
	}

	public String getContactCategoryNCode() {
		return ContactCategoryNCode;
	}

	public void setContactCategoryNCode(String contactCategoryNCode) {
		ContactCategoryNCode = contactCategoryNCode;
	}

	public String getContactCategoryName() {
		return ContactCategoryName;
	}

	public void setContactCategoryName(String contactCategoryName) {
		ContactCategoryName = contactCategoryName;
	}

}
