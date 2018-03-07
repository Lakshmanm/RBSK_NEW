//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  FamilyHistoryDisease.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  01-Jun-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver 		Date

//*
//*
//*                             Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations

//*****************************************************************************
public class FamilyHistoryDisease {
	int DiseaseID;//FamilyHistoryID
	String DiseaseName;
	boolean isSelected;
	String DiseaseComments;
	int RelationID;//Family Member RelationID

	public String getDiseaseName() {
		return DiseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		DiseaseName = diseaseName;
	}

	public int getDiseaseID() {
		return DiseaseID;
	}

	public void setDiseaseID(int diseaseID) {
		DiseaseID = diseaseID;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getDiseaseComments() {
		return DiseaseComments;
	}

	public void setDiseaseComments(String diseaseComments) {
		DiseaseComments = diseaseComments;
	}

	public int getRelationID() {
		return RelationID;
	}

	public void setRelationID(int relationID) {
		RelationID = relationID;
	}
}
