//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.model;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.nunet.utils.DateUtil;

//*****************************************************************************
//* Name   :  Children.java

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
public class Children extends Users  {
	int ChildrenID;
	// Users user;
	private String MCTSID = "";
	private String IdentificationMark1 = "";
	private String IdentificationMark2 = "";
	private String IdentificationMark3 = "";
	private int HasDisability;
	private String PWDCardNumber = "";
	private int StatusID;
	private int ChildrenStatusID;
	private int childScreenStatusID = 2;
	private ChildrenInstitutes childrenInsitute;
	private boolean isScreenedForCurrentRound = false;
	private Bitmap childimage;

	private String PermanentAddressID = "0";
	private String LocalAddressID;
	private boolean isSameAddress;

	private String screningStartTime;
	
	private String ScreeningComments;

	public Children() {
		screningStartTime = (String) DateUtil.format("yyyy-MM-dd HH:mm:ss",
				Calendar.getInstance());
	}

	public String getPermanentAddressID() {
		return PermanentAddressID;
	}

	public void setPermanentAddressID(String permanentAddressID) {
		PermanentAddressID = permanentAddressID;
	}

	public String getLocalAddressID() {
		return LocalAddressID;
	}

	public void setLocalAddressID(String localAddressID) {
		LocalAddressID = localAddressID;
	}

	/**
	 * @return the childimage
	 */
	public Bitmap getChildimage() {
		return childimage;
	}

	/**
	 * @param childimage
	 *            the childimage to set
	 */
	public void setChildimage(Bitmap childimage) {
		this.childimage = childimage;
	}

	ArrayList<ChildrenDisabilities> childDisabilities;

	Address PermanentAddress = null;

	boolean isSelected = false;

	ArrayList<Childrenparents> parentAry;
	private Drawable roundedImageDrawable;
	private int emgContectID;

	public int getChildScreenStatusID() {
		return childScreenStatusID;
	}

	public void setChildScreenStatusID(int childScreenStatusID) {
		this.childScreenStatusID = childScreenStatusID;
	}

	public boolean isScreenedForCurrentRound() {
		return isScreenedForCurrentRound;
	}

	public void setScreenedForCurrentRound(boolean isScreenedForCurrentRound) {
		this.isScreenedForCurrentRound = isScreenedForCurrentRound;
	}

	public ArrayList<ChildrenDisabilities> getChildDisabilities() {
		return childDisabilities;
	}

	public void setChildDisabilities(
			ArrayList<ChildrenDisabilities> childDisabilities) {
		this.childDisabilities = childDisabilities;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ChildrenInstitutes getChildrenInsitute() {
		return childrenInsitute;
	}

	public void setChildrenInsitute(ChildrenInstitutes childrenInsitute) {
		this.childrenInsitute = childrenInsitute;
	}

	public ArrayList<Childrenparents> getParentAry() {
		return parentAry;
	}

	public void setParentAry(ArrayList<Childrenparents> parentAry) {
		this.parentAry = parentAry;
	}

	public int getChildrenID() {
		return ChildrenID;
	}

	public void setChildrenID(int childrenID) {
		ChildrenID = childrenID;
	}

	// public Users getUser() {
	// return user;
	// }
	//
	// public void setUser(Users child) {
	// this.user = child;
	// }
	//

	public String getMCTSID() {
		return MCTSID;
	}

	public void setMCTSID(String mCTSID) {
		MCTSID = mCTSID;
	}

	public String getIdentificationMark1() {
		return IdentificationMark1;
	}

	public void setIdentificationMark1(String identificationMark1) {
		IdentificationMark1 = identificationMark1;
	}

	public String getIdentificationMark2() {
		return IdentificationMark2;
	}

	public void setIdentificationMark2(String identificationMark2) {
		IdentificationMark2 = identificationMark2;
	}

	public String getIdentificationMark3() {
		return IdentificationMark3;
	}

	public void setIdentificationMark3(String identificationMark3) {
		IdentificationMark3 = identificationMark3;
	}

	public Address getPermanentAddress() {
		return PermanentAddress;
	}

	public void setPermanentAddress(Address permanentAddress) {
		PermanentAddress = permanentAddress;
	}

	public int getHasDisability() {
		return HasDisability;
	}

	public void setHasDisability(int hasDisability) {
		HasDisability = hasDisability;
	}

	public String getPWDCardNumber() {
		return PWDCardNumber;
	}

	public void setPWDCardNumber(String pWDCardNumber) {
		PWDCardNumber = pWDCardNumber;
	}

	public int getStatusID() {
		return StatusID;
	}

	public void setStatusID(int statusID) {
		StatusID = statusID;
	}

	public int getChildrenStatusID() {
		return ChildrenStatusID;
	}

	public void setChildrenStatusID(int childrenStatusID) {
		ChildrenStatusID = childrenStatusID;
	}

	public void setChildimage(Drawable getiImageofChild) {
		this.setGetiImageofChild(getiImageofChild);

	}

	public Drawable getGetiImageofChild() {
		return roundedImageDrawable;
	}

	public void setGetiImageofChild(Drawable getiImageofChild) {
		this.roundedImageDrawable = getiImageofChild;
	}

	public boolean isSameAddress() {
		return isSameAddress;
	}

	public void setSameAddress(boolean isSameAddress) {
		this.isSameAddress = isSameAddress;
	}

	public String getScreningStartTime() {
		return screningStartTime;
	}

	public String getScreningEndTime() {
		return (String) DateUtil.format("yyyy-MM-dd HH:mm:ss",
				Calendar.getInstance());
	}

	public void setScreningStartTime(String screningStartTime) {
		this.screningStartTime = screningStartTime;
	}

	public void setEmgContactID(int EmgContectID) {
		emgContectID = EmgContectID;
	}

	public int getEmgContactID() {
		return emgContectID;
	}

	public String getScreeningComments() {
		return ScreeningComments;
	}

	public void setScreeningComments(String screeningComments) {
		ScreeningComments = screeningComments;
	}
}
