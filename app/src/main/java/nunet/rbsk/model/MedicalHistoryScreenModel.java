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
//* Name   :  MedicalHistoryScreenModel.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  Jun 10, 2015
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
public class MedicalHistoryScreenModel {
	
	ArrayList<String> medications;
	ArrayList<Allergy> allergies;
	ArrayList<Surgery> surgeries;
	/**
	 * @return the medications
	 */
	public ArrayList<String> getMedications() {
		return medications;
	}
	/**
	 * @param medications the medications to set
	 */
	public void setMedications(ArrayList<String> medications) {
		this.medications = medications;
	}
	/**
	 * @return the allergies
	 */
	public ArrayList<Allergy> getAllergies() {
		return allergies;
	}
	/**
	 * @param allergies the allergies to set
	 */
	public void setAllergies(ArrayList<Allergy> allergies) {
		this.allergies = allergies;
	}
	/**
	 * @return the surgeries
	 */
	public ArrayList<Surgery> getSurgeries() {
		return surgeries;
	}
	/**
	 * @param surgeries the surgeries to set
	 */
	public void setSurgeries(ArrayList<Surgery> surgeries) {
		this.surgeries = surgeries;
	}
	

}
