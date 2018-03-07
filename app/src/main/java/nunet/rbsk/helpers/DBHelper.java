package nunet.rbsk.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.os.Environment;
import android.app.AlertDialog.Builder;


import com.nunet.utils.DateUtil;
import com.nunet.utils.StringUtils;

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

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "RBSK_V3.3il";
	@SuppressLint("SdCardPath")
	private static String DATABASE_PATH = "/data/data/nunet.rbsk/databases/";
	public static int DB_VERSION = 1;
	public static final String TAG = "Sample";
	private final Context mContext;
	@SuppressWarnings("unused")
	private ReentrantLock lock = new ReentrantLock();

	// copy DB from assets//

	public DBHelper(Context ctx) {
		// hack to read the DB from External Storage - Praveen
		//super(ctx, Environment.getExternalStorageDirectory() + "/nunet.rbsk/DB/"+DB_NAME, null, DB_VERSION);
       // super(ctx, ctx.getFilesDir().getPath() + "/DB/"+DB_NAME, null, DB_VERSION);
		// Previous code  - Praveen
		 super(ctx, DB_NAME, null, DB_VERSION);

		this.mContext = ctx;
		// Praveen Code Start
		//DATABASE_PATH = ctx.getFilesDir().getPath()  + "/DB/";
		//DATABASE_PATH = Environment.getExternalStorageDirectory() + "/nunet.rbsk/DB/";
		// Praveen Code End
	}

	private static DBHelper mDbHelper = null;

	public static synchronized DBHelper getInstance(Context mContext) {
		// if (mDbHelper == null)
		{
			mDbHelper = new DBHelper(mContext.getApplicationContext());
		}
		return mDbHelper;

	}

	public final void createDataBase() throws IOException {
		final boolean dbExist = checkDataBase();
        Toast.makeText(mContext,
                "DB Exists ? >> " + dbExist,
                Toast.LENGTH_LONG).show();
		SQLiteDatabase db_Read = null;
		if (!dbExist) {

			// commented by Praveen

			db_Read = this.getReadableDatabase();
			db_Read.close();
			try {
				copyDatabase();
			} catch (final IOException e) {
				throw new Error("Error copying database");
			}




		}
	}

	// Method to check database is exists or not
	private boolean checkDataBase() {
		final File dbFile = new File(DATABASE_PATH + DB_NAME);
		SQLiteDatabase checkDB =null;
		// Praveen Code Start
		Toast.makeText(mContext,
				"DB File Path <<>> " + (DATABASE_PATH + DB_NAME),
				Toast.LENGTH_LONG).show();

			if (dbFile.exists()) {

				try {

					String myPath = DATABASE_PATH + DB_NAME;
					checkDB = SQLiteDatabase.openDatabase(myPath, null,
							SQLiteDatabase.OPEN_READWRITE);

				} catch (SQLiteException e) {

					// database does't exist yet.

				}

				if (checkDB != null) {

					checkDB.close();

				}



				/*
				try {

					System.out.println("Trying to set the DB as Executable 1");
					// allow the app to read and modify the file in the SD Card
					dbFile.setExecutable(true);
					System.out.println("Trying to set the DB as Executable 2");

				} catch (Exception ex) {
					ex.printStackTrace();
					Log.d(TAG, "Error in setExecutable : " + ex.getMessage());
				}
				*/
			} else {
				Log.e("DB_PATH",(DATABASE_PATH + DB_NAME ));
				Toast.makeText(mContext,
						"DB File Not Exists at >> " + (DATABASE_PATH + DB_NAME ),
						Toast.LENGTH_LONG).show();
			}
			// Praveen Code End
//			return dbFile.exists();
		return checkDB != null ? true : false;
		}



	// Method to copy database from assets to app folder.
	private void copyDatabase() throws IOException {
		close();
		final InputStream input = mContext.getAssets().open(DB_NAME);
		final String outFileName = DATABASE_PATH + DB_NAME;
		final OutputStream output = new FileOutputStream(outFileName);
		// Transfer bytes from the input file to the output file
		final byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		output.flush();
		output.close();
		input.close();
		getWritableDatabase().close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// createTables(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
			db.execSQL("PRAGMA encoding=utf8");
		}
	}

	// open databse and set foreign keys on
	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DATABASE_PATH + DB_NAME;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		db.execSQL("PRAGMA encoding = 'UTF-8' ;");
		//myDataBase.execSQL("PRAGMA foreign_keys=ON");
		// myDataBase.execSQL("PRAGMA foreign_keys=ON");

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

		onCreate(db);
	}

	public void createTables(SQLiteDatabase database) {
		try {

			Log.d(TAG, "Tables created!");
		} catch (Exception ex) {
			Log.d(TAG, "Error in DBHelper.createTables() : " + ex.getMessage());
		}
	}

	// Method to get count of data from table
	public int getCount(Context context, final String tableName) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		int cnt = 0;
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
		cursor.moveToFirst();
		String count = cursor.getString(0);
		if (!TextUtils.isEmpty(count))
			cnt = NumUtil.IntegerParse.parseInt(count);
		cursor.close();
		db.close();
		return cnt;
	}

	// Method to get data from table
	public List<String[]> getTableData(Context context, String TableName) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + TableName, null);
		List<String[]> list = null;
		if (cur.getCount() > 0)
			list = cursortoListArr(cur);
		cur.close();
		db.close();
		dbhelper.close();
		return list;
	}

	public List<String[]> getQueryData(Context context, String query) {
		System.out.println("query :" + query);
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cur = db.rawQuery(query, null);
		List<String[]> list = new ArrayList<>();
		System.out.println("cur.getCount() :" + cur.getCount());
		if (cur.getCount() > 0)
			list = cursortoListArr(cur);
		cur.close();
		db.close();
		dbhelper.close();
		return list;
	}

	// Method to get Cursor data from DB
	// Kiruthika 22-04-2015
	public Cursor getCursorData(Context context, String query) {
		DBHelper dbhelper = getInstance(mContext);
		// lock.lock();
		//Praveen Code Start
		SQLiteDatabase db = dbhelper.getReadableDatabase();

//		String myPath = DATABASE_PATH + DB_NAME;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(myPath, null,
//				SQLiteDatabase.OPEN_READWRITE);

		// Praveen Code End
		try {
		Cursor cur = db.rawQuery(query, null);


			if (cur != null && cur.getCount() > 0) {
				return cur;
			} else {
				return null;
			}
		}
		catch (Exception ex) {
			Log.d(TAG, "Error in DBHelper : " + ex.getMessage());
			return null;
		}
		finally {
			// lock.unlock();
			db.close();
			dbhelper.close();
		}

	}

	public List<String[]> getCursorFromQueryData(Context context, String query) {
		System.out.println("query :" + query);
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cur = db.rawQuery(query, null);
		System.out.println("cur.getCount() :" + cur.getCount());
		List<String[]> list = null;
		if (cur.getCount() > 0) {
			list = cursortoListArr(cur);
		} else {
			list = null;
		}
		cur.close();
		db.close();
		dbhelper.close();
		return list;
	}

	public List<String> getColumnData(Context context, String TableName,
			String columnName) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT " + columnName + " FROM " + TableName,
				null);

		List<String> list = null;
		if (cur.getCount() > 0)
			list = cursortoList(cur);

		cur.close();
		db.close();
		dbhelper.close();
		return list;
	}

	public List<String> getColumnDataFromQuery(Context context, String query) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cur = db.rawQuery(query, null);

		List<String> list = null;
		if (cur.getCount() > 0)
			list = cursortoList(cur);

		cur.close();
		db.close();
		dbhelper.close();
		return list;
	}

	public long insertintoTable(Context context, final String tableName,
			String[] colNames, String[] values) {
		return insertintoTable(context, tableName, colNames, values, true);
	}

	public long insertintoTable(Context context, final String tableName,
			String[] columnNames, String[] columnValues,
			boolean isLastCommtedDateNull) {

		System.out.println("colNames.length :" + columnNames.length
				+ "    values.length :" + columnValues.length);
		DBHelper dbhelper = getInstance(mContext);

		SQLiteDatabase db = dbhelper.getReadableDatabase();
		ContentValues cv = new ContentValues();

		boolean hasDeleted = false;
		for (int i = 0; i < columnNames.length; i++) {

			String value = null;
			if (!TextUtils.isEmpty(columnValues[i]))
				value = columnValues[i].trim();

			String key = columnNames[i];
			if (key.toUpperCase().endsWith("ID")
					&& (TextUtils.isEmpty(value) || value.equals("0"))) {
				cv.putNull(key);
			} else
				cv.put(key, value);
			if (StringUtils.equalsNoCase(key, "IsDeleted")) {
				hasDeleted = true;
			}
		}

		if (isLastCommtedDateNull) {
			cv.put("LastCommitedDate", "");
			cv.put("PushStatus", "0");
		} else {
			cv.put("PushStatus", "1");
		}

		if (!hasDeleted) {
			cv.put("IsDeleted", "0");
		}

		long cnt = db.insert(tableName, null, cv);
		db.close();
		if (cnt == -1) {
			Toast.makeText(mContext,
					"PRAGMA foreign_keys effects >> " + tableName,
					Toast.LENGTH_LONG).show();
		}

		return cnt;
	}

	public boolean deleteRows(Context context, final String tablename,
			String columnName, String value) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		boolean flag = db.delete(tablename, columnName + "='" + value + "'",
				null) > 0;
		db.close();
		return flag;
	}

	public boolean deleteRowsByCondition(Context context,
			final String tablename, String[] whereColumn, String[] whereValue) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String condition = "";

		for (int i = 0; i < whereColumn.length; i++)
			condition = condition + whereColumn[i] + "='" + whereValue[i]
					+ "' AND ";
		condition = condition.substring(0, condition.lastIndexOf("'") + 1);
		boolean flag = db.delete(tablename, condition, null) > 0;
		db.close();
		return flag;
	}

	public boolean deleteAllRows(Context context, final String tablename) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		boolean flag = db.delete(tablename, null, null) > 0;
		db.close();
		return flag;

	}

	public boolean updateLastSyncDate(Context context, final String tablename,
			String[] columnNames, String[] columnValues, String whereColumn,
			String whereValue) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		ContentValues cv = new ContentValues();
		for (int i = 0; i < columnNames.length; i++) {
			String value = null;
			if (!TextUtils.isEmpty(columnValues[i]))
				value = columnValues[i].trim();

			cv.put(columnNames[i], value);
		}

		if (whereColumn != null)
			whereColumn += "='" + whereValue + "'";

		int effectdRows = db.update(tablename, cv, whereColumn, null);
		boolean flag = effectdRows > 0;
		db.close();
		return flag;

	}

	public boolean updateROW(Context context, final String tablename,
			String[] columnNames, String[] columnValues, String whereColumn,
			String whereValue) {

		return updateROW(context, tablename, columnNames, columnValues,
				whereColumn, whereValue, true);
	}

	public boolean updateROW(Context context, final String tablename,
			String[] columnNames, String[] columnValues, String whereColumn,
			String whereValue, boolean isLastCommtedDateNull) {
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		ContentValues cv = new ContentValues();
		for (int i = 0; i < columnNames.length; i++) {
			String value = null;

			if (!TextUtils.isEmpty(columnValues[i]))
				value = columnValues[i].trim();

			if (columnNames[i].toUpperCase().endsWith("ID")
					&& (TextUtils.isEmpty(value) || value.equals("0"))) {
				if (TextUtils.isEmpty(value) || value.equals("0"))
					cv.putNull(columnNames[i]);
			} else
				cv.put(columnNames[i], value);

		}
		cv.put("PushStatus", "0");

		if (isLastCommtedDateNull) {
			cv.put("LastCommitedDate", "");
			cv.put("PushStatus", "0");
		} else {
			cv.put("PushStatus", "1");
		}

		if (whereColumn != null)
			whereColumn += "='" + whereValue + "'";

		int effectdRows = db.update(tablename, cv, whereColumn, null);
		boolean flag = effectdRows > 0;
		if (!flag) {
			Log.e("DbLog", "No Rows Effected");
		}
		db.close();
		return flag;

	}

	public boolean updateROWByValues(Context context, final String tablename,
			String[] columnNames, String[] columnValues, String[] whereColumn,
			String[] whereValue) {
		DBHelper cdbh = getInstance(mContext);
		SQLiteDatabase db = cdbh.getReadableDatabase();
		ContentValues cv = new ContentValues();
		for (int i = 0; i < columnNames.length; i++) {

			String value = null;
			if (!TextUtils.isEmpty(columnValues[i]))
				value = columnValues[i].trim();

			if (columnNames[i].toUpperCase().endsWith("ID")
					&& (TextUtils.isEmpty(value) || value.equals("0"))) {
				if (TextUtils.isEmpty(value) || value.equals("0"))
					cv.putNull(columnNames[i]);
			} else
				cv.put(columnNames[i], value);
		}
		cv.put("PushStatus", "0");
		cv.put("LastCommitedDate", "");
		String query = "";
		for (int i = 0; i < whereColumn.length; i++)
			query = query + whereColumn[i] + "='" + whereValue[i] + "' AND ";
		query = query.substring(0, query.length() - 5);
		boolean flag = db.update(tablename, cv, query, null) > 0;
		db.close();
		cdbh.close();
		return flag;
	}

	public boolean updateLCD(Context context, final String tablename) {
		DBHelper cdbh = getInstance(mContext);
		SQLiteDatabase db = cdbh.getReadableDatabase();
		ContentValues cv = new ContentValues();

		cv.put("LastCommitedDate",
				(String) DateUtil.format("yyyy-MM-dd HH:mm:ss",
						Calendar.getInstance()));
		String query = " LastCommitedDate is null or LastCommitedDate=?";
		boolean flag = db.update(tablename, cv, query, new String[] { "" }) > 0;
		db.close();
		cdbh.close();
		return flag;
	}

	// public boolean delete(Context context, final String tablename) {
	// DBHelper cdbh = getInstance(mContext);
	// SQLiteDatabase db = cdbh.getReadableDatabase();
	// ContentValues cv = new ContentValues();
	// cv.put("LastCommitedDate",
	// (String) DateUtil.format("yyyy-MM-dd HH:mm:ss",
	// Calendar.getInstance()));
	// String query = "where LastCommitedDate=? or LastCommitedDate=?";
	// query = query.substring(0, query.length() - 5);
	// boolean flag = db.update(tablename, cv, query,
	// new String[] { null, "" }) > 0;
	// db.close();
	// cdbh.close();
	// return flag;
	// }

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

	public String[] getDBNames(Context context) {
		String[] result = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT name FROM sqlite_master ");
			sb.append("WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%' ");
			DBHelper dbhelper = getInstance(mContext);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor c = db.rawQuery(sb.toString(), null);
			System.out.println("size here...." + c.getCount());
			result = new String[c.getCount()];
			int i = 0;
			while (c.moveToNext()) {
				result[i] = c.getString(c.getColumnIndex("name"));
				i++;
			}
			c.close();
			db.close();
		} catch (SQLiteException e) {
			Log.e("OOPS", e.toString());
		}
		return result;
	}

	public String[] getColNames(Context context, String table) {
		String arr[] = null;
		DBHelper dbhelper = getInstance(mContext);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + table + "", null);
		c.moveToFirst();
		arr = c.getColumnNames();
		c.close();
		db.close();
		return arr;
	}

	public List<String[]> getTableColumnsData(Context context,
			String TableName, String dcolNames[]) {
		DBHelper cdbh = getInstance(mContext);
		SQLiteDatabase db = cdbh.getReadableDatabase();
		String query = "SELECT ";
		for (int g = 0; g < dcolNames.length; g++)
			query = query + dcolNames[g] + ",";
		query = query.substring(0, query.length() - 1);
		query = query + " FROM " + TableName;
		System.out.println("Query:" + query);
		Cursor cur = db.rawQuery(query, null);
		List<String[]> list = null;
		if (cur.getCount() > 0)
			list = cursortoListArr(cur);
		cur.close();
		db.close();
		cdbh.close();
		return list;
	}

	public List<String[]> getTableColumnsDataByValues(Context context,
			String TableName, String dcolNames[], String columname[],
			String columnValue[]) {
		DBHelper cdbh = getInstance(mContext);
		SQLiteDatabase db = cdbh.getReadableDatabase();
		String query = "SELECT ";
		for (int g = 0; g < dcolNames.length; g++)
			query = query + dcolNames[g] + ",";
		query = query.substring(0, query.length() - 1);
		query = query + " FROM " + TableName + " WHERE ";
		for (int k = 0; k < columname.length; k++)
			query = query + columname[k] + "='" + columnValue[k] + "'"
					+ " AND ";
		query = query.substring(0, query.length() - 5);
		System.out.println("Query:" + query);
		Cursor cur = db.rawQuery(query, null);
		List<String[]> list = new ArrayList<String[]>();
		if (cur.getCount() > 0)
			list = cursortoListArr(cur);
		cur.close();
		db.close();
		cdbh.close();
		return list;
	}

}
