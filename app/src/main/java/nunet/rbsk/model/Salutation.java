//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  Salutation.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  30-Apr-2015
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
public class Salutation {

	int SalutationID;
	String SalutationCode = "";
	String SalutationName = "";
	String SalutaionDisplayName = "";

	public int getSalutationID() {
		return SalutationID;
	}

	public void setSalutationID(int salutationID) {
		SalutationID = salutationID;
	}

	public String getSalutationCode() {
		return SalutationCode;
	}

	public void setSalutationCode(String salutationCode) {
		SalutationCode = salutationCode;
	}

	public String getSalutationName() {
		return SalutationName;
	}

	public void setSalutationName(String salutationName) {
		SalutationName = salutationName;
	}

	public String getSalutaionDisplayName() {
		return SalutaionDisplayName;
	}

	public void setSalutaionDisplayName(String salutaionDisplayName) {
		SalutaionDisplayName = salutaionDisplayName;
	}

}
