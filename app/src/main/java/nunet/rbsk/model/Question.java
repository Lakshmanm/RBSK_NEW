//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

//*****************************************************************************
//* Name   :  Question.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  27-May-2015
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
public class Question {

	private int screenQuestionID;
	private String question = "";
	private int order;
	private int isReferedWhen; // 1=YES & 0=NO
	private String answer = "";
	private int healthConditionID;
	private String healthConditionName = "";

	public int getScreenQuestionID() {
		return screenQuestionID;
	}

	public void setScreenQuestionID(int screenQuestionID) {
		this.screenQuestionID = screenQuestionID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getIsReferedWhen() {
		return isReferedWhen;
	}

	public void setIsReferedWhen(int isReferedWhen) {
		this.isReferedWhen = isReferedWhen;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getHealthConditionID() {
		return healthConditionID;
	}

	public void setHealthConditionID(int healthConditionID) {
		this.healthConditionID = healthConditionID;
	}

	public String getHealthConditionName() {
		return healthConditionName;
	}

	public void setHealthConditionName(String healthConditionName) {
		this.healthConditionName = healthConditionName;
	}

}
