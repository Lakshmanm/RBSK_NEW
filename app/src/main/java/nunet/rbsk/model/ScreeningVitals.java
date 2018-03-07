//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.rbsk.model;

import java.text.DecimalFormat;

//*****************************************************************************
//* Name   :  ScreeningVitals.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : promodh.munjeti

//* Created Date       :  May 30, 2015
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
public class ScreeningVitals {
	private float height;
	private String heightIndication;
	private float weight;
	private String weightIndication;
	private double bmi = 0;
	private String bmiIndication;
	private String acutyVisionLeft;
	private String acutyVisionRight;
	private String bp;//
	private String bpIndication;//
	private int bloodGroupId;
	private String bloodGroupNotes = "enter";
	private int temperatureId;
	private String temperatureIndication = "enter";
	private int hemoglobinId;//
	private String hemoglobinIndication = "enter";//
	private float muacCm;
	private String muacIndication;
	private float headCircumferenceCm;
	private String headCircumferenceIndication;
	private String hwRatio;

	/**
	 * @return the hwRatio
	 */
	public String getHwRatio() {
		return hwRatio;
	}

	/**
	 * @param hwRatio
	 *            the hwRatio to set
	 */
	public void setHwRatio(String hwRatio) {
		this.hwRatio = hwRatio;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the heightIndication
	 */
	public String getHeightIndication() {
		return heightIndication;
	}

	/**
	 * @param heightIndication
	 *            the heightIndication to set
	 */
	public void setHeightIndication(String heightIndication) {
		this.heightIndication = heightIndication;
	}

	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @return the weightIndication
	 */
	public String getWeightIndication() {
		return weightIndication;
	}

	/**
	 * @param weightIndication
	 *            the weightIndication to set
	 */
	public void setWeightIndication(String weightIndication) {
		this.weightIndication = weightIndication;
	}

	/**
	 * @return the bmi
	 */
	public double getBmi() {
		return bmi;
	}

	/**
	 * @param bmi
	 *            the bmi to set
	 */
	public void setBmi(double bmi) {

		if (Double.isInfinite(bmi) || Double.isNaN(bmi)) {
			this.bmi = 0;
			return;
		}
		DecimalFormat twoDForm = new DecimalFormat("#####.##");
		String format = twoDForm.format(bmi);
		this.bmi = Double.valueOf(format);

	}

	/**
	 * @return the bmiIndication
	 */
	public String getBmiIndication() {
		return bmiIndication;
	}

	/**
	 * @param bmiIndication
	 *            the bmiIndication to set
	 */
	public void setBmiIndication(String bmiIndication) {
		this.bmiIndication = bmiIndication;
	}

	/**
	 * @return the acutyVisionLeft
	 */
	public String getAcutyVisionLeft() {
		return acutyVisionLeft;
	}

	/**
	 * @param acutyVisionLeft
	 *            the acutyVisionLeft to set
	 */
	public void setAcutyVisionLeft(String acutyVisionLeft) {
		this.acutyVisionLeft = acutyVisionLeft;
	}

	/**
	 * @return the acutyVisionRight
	 */
	public String getAcutyVisionRight() {
		return acutyVisionRight;
	}

	/**
	 * @param acutyVisionRight
	 *            the acutyVisionRight to set
	 */
	public void setAcutyVisionRight(String acutyVisionRight) {
		this.acutyVisionRight = acutyVisionRight;
	}

	/**
	 * @return the bp
	 */
	public String getBp() {
		return bp;
	}

	/**
	 * @param bp
	 *            the bp to set
	 */
	public void setBp(String bp) {
		this.bp = bp;
	}

	/**
	 * @return the bpIndication
	 */
	public String getBpIndication() {
		return bpIndication;
	}

	/**
	 * @param bpIndication
	 *            the bpIndication to set
	 */
	public void setBpIndication(String bpIndication) {
		this.bpIndication = bpIndication;
	}

	/**
	 * @return the bloodGroupId
	 */
	public int getBloodGroupId() {
		return bloodGroupId;
	}

	/**
	 * @param bloodGroupId
	 *            the bloodGroupId to set
	 */
	public void setBloodGroupId(int bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	/**
	 * @return the bloodGroupNotes
	 */
	public String getBloodGroupNotes() {
		return bloodGroupNotes;
	}

	/**
	 * @param bloodGroupNotes
	 *            the bloodGroupNotes to set
	 */
	public void setBloodGroupNotes(String bloodGroupNotes) {
		this.bloodGroupNotes = bloodGroupNotes;
	}

	/**
	 * @return the temperatureId
	 */
	public int getTemperatureId() {
		return temperatureId;
	}

	/**
	 * @param temperatureId
	 *            the temperatureId to set
	 */
	public void setTemperatureId(int temperatureId) {
		this.temperatureId = temperatureId;
	}

	/**
	 * @return the temperatureIndication
	 */
	public String getTemperatureIndication() {
		return temperatureIndication;
	}

	/**
	 * @param temperatureIndication
	 *            the temperatureIndication to set
	 */
	public void setTemperatureIndication(String temperatureIndication) {
		this.temperatureIndication = temperatureIndication;
	}

	/**
	 * @return the hemoglobinId
	 */
	public int getHemoglobinId() {
		return hemoglobinId;
	}

	/**
	 * @param hemoglobinId
	 *            the hemoglobinId to set
	 */
	public void setHemoglobinId(int hemoglobinId) {
		this.hemoglobinId = hemoglobinId;
	}

	/**
	 * @return the hemoglobinIndication
	 */
	public String getHemoglobinIndication() {
		return hemoglobinIndication;
	}

	/**
	 * @param hemoglobinIndication
	 *            the hemoglobinIndication to set
	 */
	public void setHemoglobinIndication(String hemoglobinIndication) {
		this.hemoglobinIndication = hemoglobinIndication;
	}

	/**
	 * @return the muacCm
	 */
	public float getMuacCm() {
		return muacCm;
	}

	/**
	 * @param muacCm
	 *            the muacCm to set
	 */
	public void setMuacCm(float muacCm) {
		this.muacCm = muacCm;
	}

	/**
	 * @return the muacIndication
	 */
	public String getMuacIndication() {
		return muacIndication;
	}

	/**
	 * @param muacIndication
	 *            the muacIndication to set
	 */
	public void setMuacIndication(String muacIndication) {
		this.muacIndication = muacIndication;
	}

	/**
	 * @return the headCircumferenceCm
	 */
	public float getHeadCircumferenceCm() {
		return headCircumferenceCm;
	}

	/**
	 * @param headCircumferenceCm
	 *            the headCircumferenceCm to set
	 */
	public void setHeadCircumferenceCm(float headCircumferenceCm) {
		this.headCircumferenceCm = headCircumferenceCm;
	}

	/**
	 * @return the headCircumferenceIndication
	 */
	public String getHeadCircumferenceIndication() {
		return headCircumferenceIndication;
	}

	/**
	 * @param headCircumferenceIndication
	 *            the headCircumferenceIndication to set
	 */
	public void setHeadCircumferenceIndication(
			String headCircumferenceIndication) {
		this.headCircumferenceIndication = headCircumferenceIndication;
	}

}
