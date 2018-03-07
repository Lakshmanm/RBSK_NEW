//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  Institutestaff.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  11-May-2015
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
public class Institutestaff extends Users {

	int InstituteID;
	int DepartmentLevelRoleID;
	boolean staffAssignHeadMaster;
	boolean staffAssignSchoolCoord;
	boolean staffAssignHealthCoord;

	public int getInstituteID() {
		return InstituteID;
	}

	public void setInstituteID(int instituteID) {
		InstituteID = instituteID;
	}

	public int getDepartmentLevelRoleID() {
		return DepartmentLevelRoleID;
	}

	public void setDepartmentLevelRoleID(int departmentLevelRoleID) {
		DepartmentLevelRoleID = departmentLevelRoleID;
	}

	public boolean isStaffAssignHeadMaster() {
		return staffAssignHeadMaster;
	}

	public void setStaffAssignHeadMaster(boolean staffAssignHeadMaster) {
		this.staffAssignHeadMaster = staffAssignHeadMaster;
	}

	public boolean isStaffAssignSchoolCoord() {
		return staffAssignSchoolCoord;
	}

	public void setStaffAssignSchoolCoord(boolean staffAssignSchoolCoord) {
		this.staffAssignSchoolCoord = staffAssignSchoolCoord;
	}

	public boolean isStaffAssignHealthCoord() {
		return staffAssignHealthCoord;
	}

	public void setStaffAssignHealthCoord(boolean staffAssignHealthCoord) {
		this.staffAssignHealthCoord = staffAssignHealthCoord;
	}

}
