//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  Caste.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

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
public class Caste {
	int CasteID;
	String CasteName = "";
	String CasteOrder = "";

	public int getCasteID() {
		return CasteID;
	}

	public void setCasteID(int casteID) {
		CasteID = casteID;
	}

	public String getCasteName() {
		return CasteName;
	}

	public void setCasteName(String casteCode) {
		CasteName = casteCode;
	}

	public String getCasteOrder() {
		return CasteOrder;
	}

	public void setCasteOrder(String casteNCode) {
		CasteOrder = casteNCode;
	}
}
