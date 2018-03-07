//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED int AS ISint  WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;


//*****************************************************************************
//* Name   :  Childrenparents.java

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
public class Childrenparents {
	int ChildrenParentID;
	int ChildrenID;
	int RelationID;
	int QualificationID;
	int OccupationID;
	int salutationID;
	
	public int getSalutationID() {
		return salutationID;
	}

	public void setSalutationID(int salutationID) {
		this.salutationID = salutationID;
	}

	Users  user;
	
	

	public int getChildrenParentID() {
		return ChildrenParentID;
	}

	public void setChildrenParentID(int childrenParentID) {
		ChildrenParentID = childrenParentID;
	}

	public int getChildrenID() {
		return ChildrenID;
	}

	public void setChildrenID(int childrenID) {
		ChildrenID = childrenID;
	}


	public int getRelationID() {
		return RelationID;
	}

	public void setRelationID(int relationID) {
		RelationID = relationID;
	}

	public int getQualificationID() {
		return QualificationID;
	}

	public void setQualificationID(int qualificationID) {
		QualificationID = qualificationID;
	}

	public int getOccupationID() {
		return OccupationID;
	}

	public void setOccupationID(int occupationID) {
		OccupationID = occupationID;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}


}
