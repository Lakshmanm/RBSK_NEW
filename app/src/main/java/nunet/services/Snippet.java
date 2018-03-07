//package nunet.services;
//
//public class Snippet {
//	public static JSONArray getTableDataForJSON(DBHelper dbh, Context ctx,
//				String tableName, String serverIDColumnName,
//				String localIdColumnName) {
//			DBSyncPushHelper.dbh = dbh;
//			DBSyncPushHelper.ctx = ctx;
//			JSONArray jsonArray = new JSONArray();
//			String query = "select * from " + tableName + " where "
//			// +"("
//			// + serverIDColumnName + " is null or " + serverIDColumnName
//			// + "='') or"
//					+ " (LastCommitedDate is null or LastCommitedDate ='');";
//			Cursor cursor = dbh.getCursorData(ctx, query);
//			if (cursor != null) {
//	
//				try {
//					String[] colNames = cursor.getColumnNames();
//					while (cursor.moveToNext()) {
//						JSONObject jsonObj = new JSONObject();
//						for (int i = 0; i < colNames.length; i++) {
//							String dataStr = cursor.getString(cursor
//									.getColumnIndex(colNames[i]));
//							if (TextUtils.isEmpty(dataStr)) {
//								jsonObj.put(colNames[i], "");
//							} else if (dataStr.trim().equalsIgnoreCase("null")) {
//								jsonObj.put(colNames[i], "");
//							} else {
//								jsonObj.put(colNames[i], dataStr);
//							}
//						}
//	
//						if (StringUtils
//								.equalsNoCase(tableName, "childrenscreening")) {
//							query = "SELECT ChildrenID FROM children  C Where  C.IsDeleted!=1  AND LocalChildrenID='"
//									+ jsonObj.getString("LocalChildrenID") + "'";
//	
//							Cursor mCursor = dbh.getCursorData(ctx, query);
//							if (mCursor != null) {
//								mCursor.moveToFirst();
//								jsonObj.put("ChildrenID", mCursor.getString(0));
//							}
//						} else if (StringUtils.equalsNoCase(tableName, "Children")) {
//							query = "SELECT InstituteID FROM Institutes I where  I.IsDeleted!=1  AND  LocalInstituteID='"
//									+ jsonObj.getString("LocalInstituteID") + "'";
//	
//							Cursor mCursor = dbh.getCursorData(ctx, query);
//							if (mCursor != null) {
//								mCursor.moveToFirst();
//								jsonObj.put("InstituteID", mCursor.getString(0));
//							}
//	
//						} else if (StringUtils.equalsNoCase(tableName,
//								"InstituteScreeningDetails")) {
//							query = "SELECT InstitutePlanID FROM Instituteplans I where  I.IsDeleted!=1  AND  LocalInstitutePlanID='"
//									+ jsonObj.getString("LocalInstitutePlanID")
//									+ "'";
//	
//							Cursor mCursor = dbh.getCursorData(ctx, query);
//							if (mCursor != null) {
//								mCursor.moveToFirst();
//								jsonObj.put("InstitutePlanID", mCursor.getString(0));
//							}
//	
//						} else if (StringUtils.equalsNoCase(tableName,
//								"childrenscreeningpe")) {
//							if (TextUtils.equals(jsonObj.getString("Answer"), "1")) {
//								jsonArray.put(jsonObj);
//							}
//							continue;
//						}
//	
//						jsonArray.put(jsonObj);
//					}
//					cursor.close();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//	
//			}
//			System.out.println("jsonStr:::::" + jsonArray.toString());
//			return jsonArray;
//		}
//}
//
