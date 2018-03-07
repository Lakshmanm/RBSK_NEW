//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  SignOffScreenModel.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  03-Jun-2015
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
public class SignOffScreenModel {

	Recommendations recommendations;
	String doctorComments = "";
	String diagnosis = "";
	String medicationsGiven = "";

	public Recommendations getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(Recommendations recommendations) {
		this.recommendations = recommendations;
	}

	public String getDoctorComments() {
		return doctorComments;
	}

	public void setDoctorComments(String doctorComments) {
		this.doctorComments = doctorComments;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getMedicationsGiven() {
		return medicationsGiven;
	}

	public void setMedicationsGiven(String medicationsGiven) {
		this.medicationsGiven = medicationsGiven;
	}

}
