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
//* Name   :  ChildrenScreeningModel.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  13-Jun-2015
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
public class ChildrenScreeningModel {

	long screeningID = 0;

	// *** For ChildrenScreeningFamilyHistory
	private ArrayList<FamilyHistoryDisease> familyHistoryDiseases = null;

	// *** For ChildrenScreeningMedicalHistory
	private MedicalHistoryScreenModel medicalHistoryScreenModel;

	// *** For ChildrenScreeningVitals
	private ScreeningVitals vitals;

	// *** For ChildrenScreeningPE
	private Category[] categories;

	// *** For Referral
	private ArrayList<Referral> referrals = null;

	// *** For SignOff
	private SignOffScreenModel signOffModel;

	public long getScreeningID() {
		return screeningID;
	}

	public void setScreeningID(long localChildScreeningID) {
		this.screeningID = localChildScreeningID;
	}

	public ArrayList<FamilyHistoryDisease> getFamilyHistoryDiseases() {
		return familyHistoryDiseases;
	}

	public void setFamilyHistoryDiseases(
			ArrayList<FamilyHistoryDisease> familyHistoryDiseases) {
		this.familyHistoryDiseases = familyHistoryDiseases;
	}

	public MedicalHistoryScreenModel getMedicalHistoryScreenModel() {
		return medicalHistoryScreenModel;
	}

	public void setMedicalHistoryScreenModel(
			MedicalHistoryScreenModel medicalHistoryScreenModel) {
		this.medicalHistoryScreenModel = medicalHistoryScreenModel;
	}

	public ScreeningVitals getVitals() {
		return vitals;
	}

	public void setVitals(ScreeningVitals vitals) {
		this.vitals = vitals;
	}

	public Category[] getCategories() {
		return categories;
	}

	public void setCategories(Category[] categories) {
		this.categories = categories;
	}

	public ArrayList<Referral> getReferrals() {
		return referrals;
	}

	public void setReferrals(ArrayList<Referral> referrals) {
		this.referrals = referrals;
	}

	public SignOffScreenModel getSignOffModel() {
		return signOffModel;
	}

	public void setSignOffModel(SignOffScreenModel signOffModel) {
		this.signOffModel = signOffModel;
	}
}
