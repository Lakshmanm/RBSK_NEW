//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package com.nunet.wsutil;

//*****************************************************************************
//* Name              :  UrlUtils.java
//* Type              : 
//* Description       : 
//* References        :                                                        
//* Author            : deepika.chevvakula

//* Created Date       :  29-Jul-2015
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
public class UrlUtils {

	// -------------------------------------------URL---------------------------------------
	// public static String BASE_URL = "http://192.168.1.82:2222"; // Dev

	// public static String BASE_URL = "http://125.62.194.40:4455"; // Dev
// 182.18.164.29:2233
	public static String BASE_URL = "http://182.18.164.29:2233";
    //public static String BASE_URL_1 = "http://182.18.164.29:2223";
	public static String BASE_URL_1 = "http://192.168.100.3:8022";


	// public static String BASE_URL = "http://192.168.100.3:2222"; // Dev

	// public static String BASE_URL = "http://192.168.100.3:4455";

	// Dev
	// to get Device code
	public static String URL_Register = BASE_URL + "/Service/Device/Register/";
	// to get token
	public static String URL_Hello = BASE_URL + "/Service/Device/Hello/";

	// to initial setup
	public static String URL_INTITAL_SETUP = BASE_URL_1 + "/api/master/";

    // to initial setup
    public static String URL_SYNC = BASE_URL_1 + "/api/sync/post";
	// to request FTS status
	public static String URL_RequestFTSStatus = BASE_URL
			+ "/Service/Device/FTSStatus/";

	// check FTS setup status
	public static String URL_CheckFTSStatus = BASE_URL
			+ "/Service/Device/CheckFTSStatus/";

	// to download FTS file
	public static String URL_fileDownload = BASE_URL
			+ "/Service/Device/DownloadFTS/";

	// to upload File
	public static String URL_fileUpload = BASE_URL
			+ "/Service/Device/UploadFTSResponse";
	// GetQueuedDeviceActions
	// public static String URL_GetQueuedDeviceActions = BASE_URL +
	// "/Service/Device/GetQueuedDeviceActions/";
	// UpdateDeviceAction
	public static String URL_UpdateDeviceAction = BASE_URL
			+ "/Service/Device/UpdateDeviceAction";

	// ----------------Incremental URL's-----------------
	// RequestIncrementalSync
	public static String URL_RequestIncrementalSync = BASE_URL
			+ "/Service/Device/RequestIncrementalSync/";
	public static int RequestIncrementalSync = 190;

	public static String URL_getServerIds = BASE_URL
			+ "/Service/Device/GetServerIDs";

	public static int SendObjectIncrementalID = 108;
	public static String URL_SendObjectIncrementalData = BASE_URL
			+ "/Service/Device/SendObjectIncrementalData";

	public static String URL_GetObjectIncrementalData = BASE_URL
			+ "/Service/Device/GetObjectIncrementalData";
	public static String URL_GetPictures = BASE_URL
			+ "/Service/Device/GetPictures";

	public static String URL_SavePreProcessIncrementalData = BASE_URL
			+ "/Service/Device/SavePreProcessIncrementalData";
	public static int URL_SavePreProcessIncrementalDataID = 1001;

	public static String URL_GetServerIDs = BASE_URL
			+ "/Service/Device/GetServerIDs";
	public static int GetServerID = 1002;

	public static String URL_GetServerPushSchemaWFARecords = BASE_URL
			+ "/Service/Device/GetServerPushSchemaWFARecords";
	public static int GetServerPushSchemaWFARecords = 2001;

	public static String URL_UpdateServerPushRecrodSyncStatus = BASE_URL
			+ "/Service/Device/UpdateServerPushRecrodSyncStatus";
	public static int UpdateServerPushRecrodSyncStatus = 2002;

	public static String URL_GetDevicePushSchemaWFARecordServerStatus = BASE_URL
			+ "/Service/Device/GetDevicePushSchemaWFARecordServerStatus";
	public static int GetDevicePushSchemaWFARecordServerStatus = 3000;

}
