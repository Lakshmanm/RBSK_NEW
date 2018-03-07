//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

import java.util.ArrayList;

//*****************************************************************************
//* Name   :  HealthConditionQuestions.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  02-Jun-2015
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
public class HealthConditionQuestions {
	int HealthConditionID;
	String HealthConditionName;
	ArrayList<Question> questions;

	public int getHealthConditionID() {
		return HealthConditionID;
	}

	public void setHealthConditionID(int healthConditionID) {
		HealthConditionID = healthConditionID;
	}

	public String getHealthConditionName() {
		return HealthConditionName;
	}

	public void setHealthConditionName(String healthConditionName) {
		HealthConditionName = healthConditionName;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
}
