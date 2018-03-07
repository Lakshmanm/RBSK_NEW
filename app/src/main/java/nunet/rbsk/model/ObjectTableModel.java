package nunet.rbsk.model;

public class ObjectTableModel {

	String ObjectID;
	String objectLocalKey;
	String objectKey;
	String ObjectName;
	String LastSyncDate;
	private String ServerObjectName;
	
	public String getObjectLocalKey() {
		return objectLocalKey;
	}

	public void setObjectLocalKey(String objectLocalKey) {
		this.objectLocalKey = objectLocalKey;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public String getObjectID() {
		return ObjectID;
	}

	public void setObjectID(String objectID) {
		ObjectID = objectID;
	}

	public String getObjectName() {
		return ObjectName;
	}

	public void setObjectName(String objectName) {
		ObjectName = objectName;
	}

	public String getLastSyncDate() {
		return LastSyncDate;
	}

	public void setLastSyncDate(String lastSyncDate) {
		LastSyncDate = lastSyncDate;
	}

	public String getServerObjectName() {
		return ServerObjectName;
	}

	public void setServerObjectName(String serverObjectName) {
		ServerObjectName = serverObjectName;
	}


}
