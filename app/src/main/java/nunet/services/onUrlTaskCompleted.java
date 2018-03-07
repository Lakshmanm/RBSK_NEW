package nunet.services;

public interface onUrlTaskCompleted {

	void onGetServerIDCompleted();

	void onGetServerPushSchemaWFARecords();

	void onGetDevicePushSchemaWFARecordServerStatus();

	void onDBSyncPushCompleted();

	void onDBSyncPullCompleted();

	void onResponseError(Exception e, String mData,int callbackurl);

}
