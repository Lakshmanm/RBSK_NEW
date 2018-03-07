package nunet.services;

public class ObjectTableDetails {

	private ObjectTableDetails() {
		getObjectDetails(arrPoss++);
	}

	private static ObjectTableDetails mDetails;

	public static synchronized ObjectTableDetails getInstance() {
		if (mDetails == null)
			mDetails = new ObjectTableDetails();
		return mDetails;
	}

	private String objectName;
	private String objectServerName;
	private String localIdColumnName;
	private String serverIdColumnName;

	/** ChildrenInstitutes - 77 removed */
	private int[] inc_objectIdAry = { 58, 61, 62, 63, 71, 72, 75, 76, 78, 79,
			109, 82, 89, 90, 91, 93, 98, 99, 100, 101, 102, 103, 104, 105, 106, };
	private int arrPoss = 0;
	private int objectID = 0;
	public ObjectTableDetails TableDetails;

	public int getSize() {
		return inc_objectIdAry.length;
	}

	public boolean getNext() {
		return getObjectDetails(arrPoss++);
	}

	public boolean moveToFirst() {
		arrPoss = 0;
		return getObjectDetails(arrPoss++);
	}

	private boolean getObjectDetails(int index) {

		if (index >= inc_objectIdAry.length)
			return false;

		int inc_objectId = inc_objectIdAry[index];
		setObjectID(inc_objectId);
		switch (inc_objectId) {
		case 58:
			setObjectName("Address");
			setObjectServerName("Address");
			setLocalIdColumnName("LocalAddressID");
			setServerIdColumnName("AddressID");
			break;
		case 61:
			setObjectName("Contacts");
			setObjectServerName("Contacts");
			setLocalIdColumnName("LocalContactID");
			setServerIdColumnName("ContactID");
			break;
		case 62:
			setObjectName("ContactDetails");
			setObjectServerName("ContactDetails");
			setLocalIdColumnName("LocalContactDetailID");
			setServerIdColumnName("ContactDetailID");
			break;
		case 63:
			setObjectName("Users");
			setObjectServerName("Users");
			setLocalIdColumnName("LocalUserID");
			setServerIdColumnName("UserID");
			break;
		// case 70:
		// setObjectName("Institutes");
		// setLocalIdColumnName("LocalInstituteID");
		// setServerIdColumnName("InstituteID");
		// break;
		case 71:
			setObjectName("InstituteStaff");
			setObjectServerName("InstituteStaff");
			setLocalIdColumnName("LocalInstituteStaffID");
			setServerIdColumnName("InstituteStaffID");
			break;
		case 72:
			setObjectName("InstitutePictures");
			setObjectServerName("InstitutePictures");
			setLocalIdColumnName("LocalInstitutePictureID");
			setServerIdColumnName("InstitutePictureID");
			break;
		case 75:
			setObjectName("Children");
			setObjectServerName("Children");
			setLocalIdColumnName("LocalChildrenID");
			setServerIdColumnName("ChildrenID");
			break;
		case 76:
			setObjectName("ChildrenDisabilities");
			setObjectServerName("ChildrenDisabilities");

			setLocalIdColumnName("LocalChildrenDisabilityID");
			// setLocalIdColumnName("LocalChildrenDisabilityID");
			setServerIdColumnName("ChildrenDisabilityID");
			// setServerIdColumnName("ChildrenDisabilityID");

			break;

		case 78:
			setObjectName("ChildrenParents");
			setObjectServerName("ChildrenParents");
			setLocalIdColumnName("LocalChildrenParentID");
			setServerIdColumnName("ChildrenParentID");
			break;
		case 79:
			setObjectName("ChildrenPictures");
			setObjectServerName("ChildrenPictures");
			setLocalIdColumnName("LocalChildrenPictureID");
			setServerIdColumnName("ChildrenPictureID");
			break;
		case 82:
			setObjectName("InstitutePlanDetails");
			setObjectServerName("InstitutePlanDetails");
			setLocalIdColumnName("LocalInstitutePlanDetailID");
			setServerIdColumnName("InstitutePlanDetailID");
			break;
		case 89:
			setObjectName("InstituteScreening");
			setObjectServerName("InstituteScreening");
			setLocalIdColumnName("LocalInstituteScreeningID");
			setServerIdColumnName("InstituteScreeningID");
			break;
		case 90:
			setObjectName("InstituteScreeningDetails");
			setObjectServerName("InstituteScreeningDetails");
			setLocalIdColumnName("LocalInstituteScreeningDetailID");
			setServerIdColumnName("InstituteScreeningDetailID");
			break;
		case 91:
			setObjectName("ChildrenScreening");
			setObjectServerName("ChildrenScreening");
			setLocalIdColumnName("LocalChildrenScreeningID");
			setServerIdColumnName("ChildrenScreeningID");
			break;

		case 93:
			setObjectName("ChildScreeningFH");
			setObjectServerName("ChildScreeningFH");
			setLocalIdColumnName("LocalChildScreeningFHID");
			setServerIdColumnName("ChildScreeningFHID");
			break;

		case 98:
			setObjectName("ChildrenScreeningAllergies");
			setObjectServerName("ChildrenScreeningAllergies");
			setLocalIdColumnName("LocalChildrenScreeningAllergyID");
			setServerIdColumnName("ChildrenScreeningAllergyID");
			break;
		case 99:
			setObjectName("ChildrenScreeningSurgicals");
			setObjectServerName("ChildrenScreeningSurgicals");
			setLocalIdColumnName("LocalChildrenScreeningSurgicalID");
			setServerIdColumnName("ChildrenScreeningSurgicalID");
			break;
		case 100:
			setObjectName("ChildrenScreeningPE");
			setObjectServerName("ChildrenScreeningPE");
			setLocalIdColumnName("LocalChildrenScreeningPEID");
			setServerIdColumnName("ChildrenScreeningPEID");
			break;
		case 101:
			setObjectName("ChildrenScreeningRecommendations");
			setObjectServerName("ChildrenScreeningRecommendations");
			setLocalIdColumnName("LocalChildrenScreeningRecommendationID");
			setServerIdColumnName("ChildrenScreeningRecommendationID");
			break;
		case 102:
			setObjectName("ChildrenScreeningReferrals");
			setObjectServerName("ChildrenScreeningReferrals");
			setLocalIdColumnName("LocalChildrenScreeningReferralID");
			setServerIdColumnName("ChildrenScreeningReferralID");
			break;
		case 103:
			setObjectName("ChildrenScreeningInvestigations");
			setObjectServerName("ChildrenScreeningInvestigations");
			setLocalIdColumnName("LocalChildrenScreeningInvestigationID");
			setServerIdColumnName("ChildrenScreeningInvestigationID");
			break;
		case 104:
			setObjectName("ChildrenScreeningLocalTreatment");
			setObjectServerName("ChildrenScreeningLocalTreatment");
			setLocalIdColumnName("LocalChildrenScreeningLocalTreatmentID");
			setServerIdColumnName("ChildrenScreeningLocalTreatmentID");
			break;
		case 105:
			setObjectName("ChildrenScreeningVitals");
			setObjectServerName("ChildrenScreeningVitals");
			setLocalIdColumnName("LocalChildrenScreeningVitalsID");
			setServerIdColumnName("ChildrenScreeningVitalsID");
			break;
		case 106:
			setObjectName("ChildrenScreeningPictures");
			setObjectServerName("ChildrenScreeningPictures");
			setLocalIdColumnName("LocalChildrenScreeningPictureID");
			setServerIdColumnName("ChildrenScreeningPictureID");
			break;
		case 109:
			setObjectName("instituteplans");
			setObjectServerName("instituteplans");
			setLocalIdColumnName("LocalInstitutePlanID");
			setServerIdColumnName("InstitutePlanID");
			break;

		default:
			setObjectName("");
			setLocalIdColumnName("");
			setServerIdColumnName("");
			return false;
		}
		return true;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getLocalIdColumnName() {
		return localIdColumnName;
	}

	public void setLocalIdColumnName(String localIdColumnName) {
		this.localIdColumnName = localIdColumnName;
	}

	public String getServerIdColumnName() {
		return serverIdColumnName;
	}

	public void setServerIdColumnName(String serverIdColumnName) {
		this.serverIdColumnName = serverIdColumnName;
	}

	public int getObjectID() {
		return objectID;
	}

	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	public String getObjectServerName() {
		return objectServerName;
	}

	public void setObjectServerName(String objectServerName) {
		this.objectServerName = objectServerName;
	}

}
