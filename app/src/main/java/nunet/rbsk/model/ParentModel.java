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
//* Name   :  ParentModel

//* Type    : Model Class

//* Description     : Model class for parent details
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

public class ParentModel {
	
	int Id;
	String Name="";
	
	public int getId() {
		return Id;
	}
	public void setId(int districtId) {
		this.Id = districtId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String districtName) {
		this.Name = districtName;
	}
	
}
