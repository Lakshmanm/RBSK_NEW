//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  SurgicalHistory.java

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
public class Surgery {
	int id;
	String name;
	String comments;
	int screeningRound;
	int screeningYear;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the screeningRound
	 */
	public int getScreeningRound() {
		return screeningRound;
	}

	/**
	 * @param screeningRound
	 *            the screeningRound to set
	 */
	public void setScreeningRound(int screeningRound) {
		this.screeningRound = screeningRound;
	}

	/**
	 * @return the screeningYear
	 */
	public int getScreeningYear() {
		return screeningYear;
	}

	/**
	 * @param screeningYear
	 *            the screeningYear to set
	 */
	public void setScreeningYear(int screeningYear) {
		this.screeningYear = screeningYear;
	}

}
