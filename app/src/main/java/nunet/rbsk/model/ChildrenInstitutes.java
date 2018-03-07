//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  ChildrenInstitutes.java

//* Type    : Model Class

//* Description     : Model class for ChildrenInstitutes
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
public class ChildrenInstitutes {

	int ChildrenInstitutesID;
	int ChildrenID;
	int InstituteID;
	String AdmissionDate;
	String AdmissionNumber;
	int ClassID;
	int SectionID;
	String RollNumber = "";
	int instituteTypeId;
	

	/**
	 * @return the instituteTypeId
	 */
	public int getInstituteTypeId() {
		return instituteTypeId;
	}

	/**
	 * @param instituteTypeId the instituteTypeId to set
	 */
	public void setInstituteTypeId(int instituteTypeId) {
		this.instituteTypeId = instituteTypeId;
	}

	public int getChildrenInstitutesID() {
		return ChildrenInstitutesID;
	}

	public void setChildrenInstitutesID(int childrenInstitutesID) {
		ChildrenInstitutesID = childrenInstitutesID;
	}

	public int getChildrenID() {
		return ChildrenID;
	}

	public void setChildrenID(int childrenID) {
		ChildrenID = childrenID;
	}

	public int getInstituteID() {
		return InstituteID;
	}

	public void setInstituteID(int instituteID) {
		InstituteID = instituteID;
	}

	public String getAdmissionDate() {
		return AdmissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		AdmissionDate = admissionDate;
	}

	public String getAdmissionNumber() {
		return AdmissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		AdmissionNumber = admissionNumber;
	}

	public int getClassID() {
		return ClassID;
	}

	public void setClassID(int classID) {
		ClassID = classID;
	}

	public int getSectionID() {
		return SectionID;
	}

	public void setSectionID(int sectionID) {
		SectionID = sectionID;
	}

	public String getRollNumber() {
		return RollNumber;
	}

	public void setRollNumber(String rollNumber) {
		RollNumber = rollNumber;
	}

}
