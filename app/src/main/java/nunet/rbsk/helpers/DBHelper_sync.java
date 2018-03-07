package nunet.rbsk.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  DBHelper

//* Type    : Helper class

//* Description     : All DB funtions are called from this class
//* References     :                                                        
//* Author    :Deepika.Chevvakula

//* Created Date       : 22-04-2015
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

public class DBHelper_sync extends SQLiteOpenHelper {

	public String DB_NAME = "BulkDB";
	public static int DB_VERSION = 1;
	public static final String TAG = "Sample";
	public static SQLiteDatabase mDb;

	public DBHelper_sync(Context ctx, String DB_NAME) {
		super(ctx, DB_NAME, null, DB_VERSION);
		this.DB_NAME = DB_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// createTables(db);
	}

	public DBHelper_sync open() throws SQLException {
		mDb = getReadableDatabase();
		return this;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		onCreate(db);
	}

	public void getCreateDynamicTables(Context context, String DB_NAME,
			String tableName, ArrayList<String> coloumnData) {
		DBHelper_sync dbhelper = new DBHelper_sync(context, DB_NAME);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String str = "";
		for (int i = 0; i < coloumnData.size(); i++) {
			str += "[" + coloumnData.get(i) + "] text, ";
		}
		if (!TextUtils.isEmpty(str)) {
			str = str.substring(0, str.lastIndexOf(","));
			String tableCreate = "create table [" + tableName + "] (" + str
					+ ",status text );";
			db.execSQL(tableCreate);
		}
		if (db.isOpen()) {
			db.close();
		}
	}

	public void createTables(SQLiteDatabase database) {
		try {
			Log.d(TAG, "Tables created!");
		} catch (Exception ex) {
			Log.d(TAG, "Error in DBHelper.createTables() : " + ex.getMessage());
		}
	}

	public Cursor getCursorData(Context context, String query) {
		System.out.println("query :" + query);
		DBHelper_sync dbhelper = new DBHelper_sync(context, DB_NAME);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cur = db.rawQuery(query, null);
		System.out.println("cur.getCount() :" + cur.getCount());

		if (cur.getCount() > 0) {
			return cur;
		} else {
			return null;
		}

		// finally {
		// db.close();
		// dbhelper.close();
		// }
	}

	public List<String> cursortoList(Cursor c) {
		List<String> list = new ArrayList<String>();
		while (c.moveToNext()) {

			list.add(c.getString(0));
		}
		c.close();
		return list;
	}

	public List<String[]> cursortoListArr(Cursor c) {
		List<String[]> rowList = new ArrayList<String[]>();
		while (c.moveToNext()) {
			String[] arr = new String[c.getColumnCount()];
			for (int i = 0; i < c.getColumnCount(); i++) {
				arr[i] = c.getString(i);
			}
			rowList.add(arr);
		}
		c.close();
		return rowList;
	}

}
