/**
 * 
 */
package nunet.rbsk.helpers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import nunet.rbsk.R;
import nunet.rbsk.model.Address;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.ChildrenScreeningModel;
import nunet.rbsk.model.ContactCategories;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.District;
import nunet.rbsk.model.Event;
import nunet.rbsk.model.Habitation;
import nunet.rbsk.model.Institute;
import nunet.rbsk.model.InstituteSchedule;
import nunet.rbsk.model.Mandal;
import nunet.rbsk.model.Panchayat;
import nunet.rbsk.model.Salutation;
import nunet.rbsk.model.State;
import nunet.rbsk.model.Village;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.StringUtils;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  EditInstBasicInfo

//* Type    : Frgament

//* Description     : To add basic info of an Institute
//* References     :                                                        
//* Author    :Promodh.munjeti

//* Created Date       : 23-04-2015
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

public class Helper {

	public static Address addModelObject = null;
	public static Institute insModelObject = null;
	public static Children childrenObject = null;
	public static boolean addChildFlag = false;
	// public static ArrayList<FamilyHistoryDisease> familyHistoryArray = null;
	public static ArrayList<InstituteSchedule> scheduleList = new ArrayList<InstituteSchedule>();
	public static ArrayList<Event> eventList = new ArrayList<Event>();
	public static ArrayList<Contacts> contactModelObject = null;
	static Cursor cur;
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static String currentDate = "";
	public static Bitmap childImage;
	public static ArrayList<Panchayat> panchayatList;
	// public static Category[] categoryAry;
	// public static MedicalHistoryScreenModel mhScreenModel;
	// public static SignOffScreenModel signOffScreenObj;
	public static ChildrenScreeningModel childScreeningObj;
	public static boolean isMonthFirst = true;
	public static boolean isYearFirst = true;
	public static int instTypeId = 0;

	// public static String selectedInstituteName;
	// public static long selectedlocInsScreeningDetailID;
	public static int selectedLocalInstituteID;
	// public static String RBSKCalenarYearID;
	// public static String screeningRoundID;

	// public static Recommendations recommendationsObj;
	// public static String doctorsComments="";
	// public static String localTreatment ="";

	public static String syncDate="20000101000000";

	public static void updateHeaderFromNext(Context ctx, TextView oldHeader,
			TextView newHeader, int oldId, int newId) {
		oldHeader.setBackgroundDrawable(ctx.getResources().getDrawable(oldId));
		newHeader.setBackgroundDrawable(ctx.getResources().getDrawable(newId));
	}

	// Method to get Address and set to model object
	public static Address getAllAddress(Cursor cur) {
		Address addModelObject = new Address();
		State stateModelObj = new State();
		District districtModelObj = new District();
		Mandal mandalModelObj = new Mandal();
		Panchayat panchayatModelObj = new Panchayat();
		Village villageModelObj = new Village();
		Habitation habitationModelObj = new Habitation();

		addModelObject.setAddressID(NumUtil.IntegerParse.parseInt(cur
				.getString(cur.getColumnIndex("LocalAddressID"))));
		addModelObject.setAddressName(cur.getString(cur
				.getColumnIndex("AddressName")));
		addModelObject.setAddressLine1(cur.getString(cur
				.getColumnIndex("AddressLine1")));
		addModelObject.setAddressLine2(cur.getString(cur
				.getColumnIndex("AddressLine2")));
		addModelObject
				.setLandMark(cur.getString(cur.getColumnIndex("LandMark")));
		addModelObject.setPINCode(cur.getString(cur.getColumnIndex("PINCode")));
		addModelObject.setPost(cur.getString(cur.getColumnIndex("Post")));

		if (!TextUtils.isEmpty(cur.getString(cur.getColumnIndex("StateID")))) {
			stateModelObj.setStateID(NumUtil.IntegerParse.parseInt(cur
					.getString(cur.getColumnIndex("StateID"))));
			stateModelObj.setStateName(cur.getString(cur
					.getColumnIndex("stateName")));
		}

		if (!TextUtils.isEmpty(cur.getString(cur.getColumnIndex("DistrictID")))) {
			districtModelObj.setDistrictID(NumUtil.IntegerParse.parseInt(cur
					.getString(cur.getColumnIndex("DistrictID"))));
			districtModelObj.setDistrictName(cur.getString(cur
					.getColumnIndex("districtName")));
		}

		if (!TextUtils.isEmpty(cur.getString(cur.getColumnIndex("MandalID")))) {
			mandalModelObj.setMandalID(NumUtil.IntegerParse.parseInt(cur
					.getString(cur.getColumnIndex("MandalID"))));
			mandalModelObj.setMandalName(cur.getString(cur
					.getColumnIndex("mandalName")));
		}

		if (!TextUtils
				.isEmpty(cur.getString(cur.getColumnIndex("PanchayatID")))) {
			panchayatModelObj.setPanchayatID(NumUtil.IntegerParse.parseInt(cur
					.getString(cur.getColumnIndex("PanchayatID"))));
			panchayatModelObj.setPanchayatName(cur.getString(cur
					.getColumnIndex("panchayatName")));
		}

		if (!TextUtils.isEmpty(cur.getString(cur.getColumnIndex("VillageID")))) {
			villageModelObj.setVillageID(NumUtil.IntegerParse.parseInt(cur
					.getString(cur.getColumnIndex("VillageID"))));
			villageModelObj.setVillageName(cur.getString(cur
					.getColumnIndex("villageName")));
		}

		if (!TextUtils
				.isEmpty(cur.getString(cur.getColumnIndex("HabitatID")))) {
			habitationModelObj.setHabitatID(NumUtil.IntegerParse.parseInt(cur
					.getString(cur.getColumnIndex("HabitatID"))));
			habitationModelObj.setHabitatName(cur.getString(cur
					.getColumnIndex("habitationName")));
		}

		addModelObject.setState(stateModelObj);
		addModelObject.setDistrict(districtModelObj);
		addModelObject.setMandal(mandalModelObj);
		addModelObject.setPanchayat(panchayatModelObj);
		addModelObject.setVillage(villageModelObj);
		addModelObject.setHabitation(habitationModelObj);

		return addModelObject;
	}

	/**
	 * @param cur
	 * @return contactObject Kiruthika 23-04-15
	 */
	public static ArrayList<Contacts> getAllContacts(Cursor contactCur) {

		ArrayList<Contacts> contactModelArray = new ArrayList<Contacts>();
		if (contactCur != null) {
			// contactModelArray = new ArrayList<Contacts>();
			try {
				if (contactCur.moveToFirst()) {
					do {
						Contacts contactModelObj = new Contacts();
						contactModelObj.setContactID(NumUtil.IntegerParse
								.parseInt(contactCur.getString(contactCur
										.getColumnIndex("ContactID"))));
						contactModelObj
								.setContact(contactCur.getString(contactCur
										.getColumnIndex("Contact")));
						contactModelObj.setContactTypeID(NumUtil.IntegerParse
								.parseInt(contactCur.getString(contactCur
										.getColumnIndex("ContactTypeID"))));
						contactModelObj.setContactTypeName(contactCur
								.getString(contactCur
										.getColumnIndex("ContactTypeName")));
						contactModelObj
								.setContactCategoryID(NumUtil.IntegerParse.parseInt(contactCur.getString(contactCur
										.getColumnIndex("ContactCategoryID"))));
						contactModelObj
								.setContactCategoryName(contactCur.getString(contactCur
										.getColumnIndex("ContactCategoryName")));
						contactModelArray.add(contactModelObj);
					} while (contactCur.moveToNext());
				}
			} finally {
				contactCur.close();
			}
		}
		return contactModelArray;

	}

	/**
	 * Kiruthika 24-04-2015 Method to retrieve All State from DB
	 * 
	 * @param activity
	 * @param stateList
	 * @return
	 */
	public static ArrayList<State> getStateDataFromDB(Context ctx,
			ArrayList<State> stateList) {
		String instituteQuery = "Select StateID,DisplayText from States S Where S.IsDeleted!=1 ";
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, instituteQuery);
		State stateObj;
		stateList = new ArrayList<State>();
		stateObj = new State();
		stateObj.setStateID(0);
		stateObj.setStateName("Select State");
		stateList.add(stateObj);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {
					do {
						stateObj = new State();
						stateObj.setStateID(NumUtil.IntegerParse.parseInt(cur
								.getString(cur.getColumnIndex("StateID"))));
						stateObj.setStateName(cur.getString(cur
								.getColumnIndex("DisplayText")));

						stateList.add(stateObj);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return stateList;
	}

	public static String getTodayDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String getTodayDateTime1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	public static String getyear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(new Date());
	}
	public static String getTodayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}



	/**
	 * Kiruthika 24-04-2015 Method to retrieve All District based on State
	 * 
	 * @param activity
	 * @param districtList
	 * @param stateID
	 * @return
	 */
	public static ArrayList<District> getDistrictDataFromDB(Context ctx,
			ArrayList<District> districtList, int stateID) {
		String districtQuery = "Select DistrictID, DisplayText from Districts D where   D.IsDeleted!=1  AND StateID="
				+ stateID;
		District districtObj;
		districtList = new ArrayList<District>();
		districtObj = new District();
		districtObj.setDistrictID(0);
		districtObj.setDistrictName("Select District");
		districtList.add(districtObj);
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, districtQuery);
		if (cur != null) {
			try {
				if (cur.moveToFirst()) {
					do {
						districtObj = new District();
						districtObj.setDistrictID(NumUtil.IntegerParse
								.parseInt(cur.getString(cur
										.getColumnIndex("DistrictID"))));
						districtObj.setDistrictName(cur.getString(cur
								.getColumnIndex("DisplayText")));

						districtList.add(districtObj);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return districtList;
	}

	/**
	 * Kiruthika 24-04-2015 Method to retrieve All Mandals based on district
	 * 
	 * @param activity
	 * @param mandalList
	 * @param districtID
	 * @return
	 */
	public static ArrayList<Mandal> getMandalList(Context ctx,
			ArrayList<Mandal> mandalList, int districtID) {
		String districtQuery = "Select MandalID, DisplayText from Mandals M where   M.IsDeleted!=1  AND DistrictID="
				+ districtID;
		Mandal mandalObj;
		mandalList = new ArrayList<Mandal>();
		mandalObj = new Mandal();
		mandalObj.setMandalID(0);
		mandalObj.setMandalName("Select Mandal");
		mandalList.add(mandalObj);
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, districtQuery);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {

					do {
						mandalObj = new Mandal();
						mandalObj.setMandalID(NumUtil.IntegerParse.parseInt(cur
								.getString(cur.getColumnIndex("MandalID"))));
						mandalObj.setMandalName(cur.getString(cur
								.getColumnIndex("DisplayText")));

						mandalList.add(mandalObj);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return mandalList;
	}

	/**
	 * Kiruthika 24-04-2015 Method to retrieve All Mandals based on district
	 * 
	 * @param activity
	 * @param mandalList
	 * @param districtID
	 * @return
	 */
	public static ArrayList<Panchayat> getPanchayatList(Context ctx,
			ArrayList<Panchayat> panchayatList, int mandalID) {
		String panchayatQuery = "Select PanchayatID, DisplayText from Panchayats P where   P.IsDeleted!=1  AND MandalID="
				+ mandalID;
		Panchayat panchayatObj;
		panchayatList = new ArrayList<Panchayat>();
		panchayatObj = new Panchayat();
		panchayatObj.setPanchayatID(0);
		panchayatObj.setPanchayatName("Select");
		panchayatList.add(panchayatObj);
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, panchayatQuery);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {

					do {
						panchayatObj = new Panchayat();
						panchayatObj.setPanchayatID(NumUtil.IntegerParse
								.parseInt(cur.getString(cur
										.getColumnIndex("PanchayatID"))));
						panchayatObj.setPanchayatName(cur.getString(cur
								.getColumnIndex("DisplayText")));

						panchayatList.add(panchayatObj);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return panchayatList;
	}

	/**
	 * Kiruthika 24-04-2015 Method to retrieve All Villages based on panchayat
	 * 
	 * @param activity
	 * @param villageList
	 * @param panchayatID
	 * @return
	 */
	public static ArrayList<Village> getVillageList(Context ctx,
			ArrayList<Village> villageList, int mandalID) {
		ArrayList<Panchayat> pan = new ArrayList<Panchayat>();
		String panchayatIds = "";
		pan = getPanchayatList(ctx, panchayatList, mandalID);
		for (int i = 0; i < pan.size(); i++) {
			panchayatIds += pan.get(i).getPanchayatID() + ",";

		}
		if (panchayatIds.endsWith(",")) {
			panchayatIds = panchayatIds.substring(0,
					panchayatIds.lastIndexOf(","));
		}
		String districtQuery = "Select VillageID, DisplayText from Villages V where   V.IsDeleted!=1  AND PanchayatID IN ("
				+ panchayatIds + ")";
		Village villageObj;
		villageList = new ArrayList<Village>();
		villageObj = new Village();
		villageObj.setVillageID(0);
		villageObj.setVillageName("Select Village");
		villageList.add(villageObj);
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, districtQuery);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {

					do {
						villageObj = new Village();
						villageObj.setVillageID(NumUtil.IntegerParse
								.parseInt(cur.getString(cur
										.getColumnIndex("VillageID"))));
						villageObj.setVillageName(cur.getString(cur
								.getColumnIndex("DisplayText")));

						villageList.add(villageObj);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}

		return villageList;
	}

	/**
	 * Kiruthika 24-04-2015 Method to retrieve All Habitation based on village
	 * 
	 * @param activity
	 * @param habitationList
	 * @param villageID
	 * @return
	 */
	public static ArrayList<Habitation> getHabitationList(Context ctx,
			ArrayList<Habitation> habitationList, int villageID) {
		String districtQuery = "Select HabitatID, DisplayText from habitats H where   H.IsDeleted!=1  AND VillageID="
				+ villageID;
		Habitation habitationObj;
		habitationList = new ArrayList<Habitation>();
		habitationObj = new Habitation();
		habitationObj.setHabitatID(0);
		habitationObj.setHabitatName("Select Habitation");
		habitationList.add(habitationObj);
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, districtQuery);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {

					do {
						habitationObj = new Habitation();
						habitationObj.setHabitatID(NumUtil.IntegerParse
								.parseInt(cur.getString(cur
										.getColumnIndex("HabitatID"))));
						habitationObj.setHabitatName(cur.getString(cur
								.getColumnIndex("DisplayText")));

						habitationList.add(habitationObj);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
			// getUpdate_view("", 0);
		}
		return habitationList;
	}

	/**
	 * Kiruthika 01-05-2015 Method to get Salutation List
	 * 
	 */

	public static ArrayList<Salutation> getSalutationDataFromDB(Context ctx) {
		String query = "select * from salutations S Where  S.IsDeleted!=1 ";
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, query);
		Salutation salutationModelObject;
		ArrayList<Salutation> salutationAryList = new ArrayList<Salutation>();
		salutationModelObject = new Salutation();
		salutationModelObject.setSalutationID(0);
		salutationModelObject.setSalutaionDisplayName("--Select--");
		salutationAryList.add(salutationModelObject);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {
					do {
						salutationModelObject = new Salutation();
						salutationModelObject
								.setSalutationID(NumUtil.IntegerParse.parseInt(cur.getString(cur
										.getColumnIndex("SalutationID"))));
						salutationModelObject.setSalutaionDisplayName(cur
								.getString(cur.getColumnIndex("DisplayText")));
						/*
						 * salutationModelObject
						 * .setSalutationCode(cur.getString(cur
						 * .getColumnIndex("SalutationCode")));
						 * salutationModelObject
						 * .setSalutaionDisplayName(cur.getString(cur
						 * .getColumnIndex("SalutaionDisplayName")));
						 */
						salutationAryList.add(salutationModelObject);
					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}

		return salutationAryList;
	}

	/**
	 * Kiruthika 01-05-2015
	 * 
	 * @param activity
	 *            Method to get Contact Type List
	 */
	public static ArrayList<ContactCategories> getContactTypeFromDB(Context ctx) {
		String query = "select * from contactcategories CC Where  CC.IsDeleted!=1 ";
		DBHelper dbh = DBHelper.getInstance(ctx);
		cur = dbh.getCursorData(ctx, query);
		ContactCategories contactCatModelObject;
		ArrayList<ContactCategories> contactCatAryList = new ArrayList<ContactCategories>();
		contactCatModelObject = new ContactCategories();
		contactCatModelObject.setContactCategoryID(0);
		contactCatModelObject.setContactCategoryName("--Select--");
		contactCatAryList.add(contactCatModelObject);
		if (cur != null) {

			try {
				if (cur.moveToFirst()) {
					do {
						contactCatModelObject = new ContactCategories();

						/*
						 * contactCatModelObject.setContactCategoryName(cur
						 * .getString(cur.getColumnIndex("DisplayText")));
						 */
						if (TextUtils.equals(cur.getString(cur
								.getColumnIndex("DisplayText")), "Email")) {

						} else {
							contactCatModelObject.setContactCategoryName(cur
									.getString(cur
											.getColumnIndex("DisplayText")));
							contactCatModelObject
									.setContactCategoryID(NumUtil.IntegerParse.parseInt(cur.getString(cur
											.getColumnIndex("ContactCategoryID"))));
							contactCatAryList.add(contactCatModelObject);
						}
						/*
						 * contactCatModelObject
						 * .setContactCategoryNCode(cur.getString(cur
						 * .getColumnIndex("ContactCategoryNCode")));
						 * contactCatModelObject
						 * .setContactCategoryName(cur.getString(cur
						 * .getColumnIndex("ContactCategoryName")));
						 */

					} while (cur.moveToNext());
				}
			} finally {
				cur.close();
			}
		}

		return contactCatAryList;
	}

	/**
	 * Deepika 29-04-2015 display error message in the forms
	 * 
	 */
	public static void displayErrorMsg(final EditText et, String errorMsg) {
		et.setError(errorMsg);
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				et.setError(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	/**
	 * Deepika 30-04-2015 To check valid phone number
	 * 
	 */
	public static boolean getcheckPhoneno(String str) {
		boolean flag = false;
		str = str.trim();
		if (str.length() < 10) {
			flag = true;
		} else if (!str.startsWith("7") && !str.startsWith("8")
				&& !str.startsWith("9")) {
			flag = true;
		} else {
		}
		return flag;
	}

	/**
	 * Deepika 30-04-2015 display error message in the forms for spinners
	 * 
	 */
	public static void setErrorForSpinner(Spinner spn, String errorAlert) {
		TextView errorText = (TextView) spn.getSelectedView();
		errorText.setError("");
		errorText.setTextColor(Color.RED);// just to highlight that this is an
											// error
		errorText.setText(errorAlert);
	}

	public static void setErrorForCustomSpinner(View v, String errorAlert) {

		TextView errorText = (TextView) v.findViewById(R.id.spn_custom_value);
		errorText.setError("");
		errorText.setTextColor(Color.RED);// just to highlight that this is an
											// error
		errorText.setText(errorAlert);

	}

	public static void showShortToast(Context activity, String str) {
		Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
	}

	public static String getTodayDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * Kiruthika 30-04-2015 Method to Validate Email Text
	 * 
	 */
	public static boolean isEmailAddress(EditText editText) {
		return isValid(editText, EMAIL_REGEX, "Invalid Email format");
	}

	/**
	 * Kiruthika 30-04-2015
	 * 
	 * @param editText
	 * @param emailRegex
	 * @param emailError
	 * @param required
	 * @return
	 */
	private static boolean isValid(EditText editText, String emailRegex,
			String emailError) {
		String text = editText.getText().toString().trim();
		// clearing the error, if it was previously set by some other values
		editText.setError(null);

		// pattern doesn't match so returning false
		if (!Pattern.matches(emailRegex, text)) {
			editText.setError(emailError);
			return false;
		}
		;

		return true;
	}

	// /Hide Keyboard on touch outside////
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	// public static int getAge1(int _year, int _month, int _day) {
	//
	// GregorianCalendar cal = new GregorianCalendar();
	// int y, m, d, a;
	//
	// y = cal.get(Calendar.YEAR);
	// m = cal.get(Calendar.MONTH);
	// d = cal.get(Calendar.DAY_OF_MONTH);
	// cal.set(_year, _month, _day);
	// a = y - cal.get(Calendar.YEAR);
	// if (a == 0) {
	// a = 2;
	// }
	// if ((m < cal.get(Calendar.MONTH))
	// || ((m == cal.get(Calendar.MONTH)) && (d < cal
	// .get(Calendar.DAY_OF_MONTH)))) {
	// --a;
	// }
	// if (a < 0)
	// throw new IllegalArgumentException("Age < 0");
	// return a;
	// }

	public static int getAge(String DOB) {

		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date mDate = mDateFormat.parse(DOB);
			Calendar a = Calendar.getInstance();
			a.setTime(mDate);
			Calendar b = Calendar.getInstance();
			int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
			if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
					|| (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a
							.get(Calendar.DATE) > b.get(Calendar.DATE))) {
				diff--;
			}
			return diff;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static int getAgeInMonths(String DOB) {
		int ageInMonths = 0;
		try {
			SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date mDate = mDateFormat.parse(DOB);
			Calendar ageCal = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			ageCal.setTime(mDate);
			int diffYear = today.get(Calendar.YEAR) - ageCal.get(Calendar.YEAR);
			ageInMonths = diffYear * 12 + today.get(Calendar.MONTH)
					- ageCal.get(Calendar.MONTH);

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return ageInMonths;
	}

	public static class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		public EditText et_day;

		public SelectDateFragment(EditText et_date) {
			et_day = et_date;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,
					yy, mm, dd);
			dialog.getDatePicker().setMaxDate(new Date().getTime());
			return dialog;
			// end changes...

			// return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd);
		}

		public void populateSetDate(int year, int month, int day) {
			long time = System.currentTimeMillis();
			long selected_time = changeToMilli(year + "-" + month + "-" + day);
			if (time >= selected_time) {
				et_day.setText(year + "-" + month + "-" + day);
			} else {

			}
		}

	}

	public static long changeToMilli(String date) {
		String givenDateString = date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long timeInMilliseconds = 0;
		Date mDate = null;
		try {

			try {
				mDate = sdf.parse(givenDateString);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			timeInMilliseconds = mDate.getTime();
			System.out.println("Date in milli :: " + timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return timeInMilliseconds;
	}

	/*
	 * public static class YourAsyncTask extends AsyncTask<Void, Void, Void> {
	 * 
	 * Context context; ProgressDialog dialog;
	 * 
	 * public YourAsyncTask(Context ctx) { this.context = ctx; dialog = new
	 * ProgressDialog(context); }
	 * 
	 * @Override protected void onPreExecute() { // set message of the dialog
	 * dialog.setMessage("Loading..."); // show dialog dialog.show();
	 * super.onPreExecute(); }
	 * 
	 * protected Void doInBackground(Void... args) { // do background work here
	 * return null; }
	 * 
	 * protected void onPostExecute(Void result) { // do UI work here if(dialog
	 * != null && dialog.isShowing()){ dialog.dismiss(); } } }
	 */
	public static String log_globalHeader(String deviceId) {
		// 7/10/2015 01:39 || Validating the GHL
		// 7/10/2015 01:39 || GHL is Available
		// 7/10/2015 01:39 || GHL Values are : RSFTS | Device ID - D001
		String gloabalHeaderStr = "";
		gloabalHeaderStr = getTodayDateTime() + "|| Validating the GHL \r\n";
		gloabalHeaderStr += getTodayDateTime() + "|| GHL is Available \r\n";
		gloabalHeaderStr += getTodayDateTime()
				+ "|| GHL Values are : RSFTS | Device ID - " + deviceId
				+ "\r\n\n";
		return gloabalHeaderStr;
	}

	public static String log_globalFooter(String noOfTransactions, boolean flag) {
		// 7/10/2015 01:39 || Validating the GFL
		// 7/10/2015 01:39 || GFL is Available
		// 7/10/2015 01:39 || GFL Values are : No of Transaction's - 8
		// 7/10/2015 01:39 || GFL Count mismatched with No of Transactions
		String gloabalFooterStr = "";
		gloabalFooterStr = getTodayDateTime() + "|| Validating the GFL \r\n";
		gloabalFooterStr += getTodayDateTime() + "|| GFL is Available \r\n";
		gloabalFooterStr += getTodayDateTime()
				+ "|| GFL Values are : No of Transaction's - "
				+ noOfTransactions + "\r\n\n";
		if (!flag) {
			gloabalFooterStr += getTodayDateTime()
					+ "|| GFL Count mismatched with No of Transactions"
					+ "\r\n\n";
		}
		return gloabalFooterStr;
	}

	public static String log_TransactionHeader(String transactionScopeID,
			String sequenceId) {
		// 7/10/2015 01:39 || Validating the THL
		// 7/10/2015 01:39 || THL is Available
		// 7/10/2015 01:39 || THL Values are : Transaction Scope ID -1 |
		// Sequence No -
		String transactionStr = "";
		transactionStr = getTodayDateTime() + "|| Validating the THL \r\n";
		transactionStr += getTodayDateTime() + "|| THL is Available \r\n";
		transactionStr += getTodayDateTime()
				+ "|| THL Values are : Transaction Scope ID - "
				+ transactionScopeID + "| Sequence No - " + sequenceId
				+ "\r\n\n";
		return transactionStr;
	}

	public static String log_TransactionFooter(String transactionScopeID,
			String noOfObjects, boolean flag) {
		// 7/10/2015 01:39 || Validating TFL for THL with Transaction Scope ID -
		// 1( Individual )
		// 7/10/2015 01:39 || Validated TFL for Transaction Scope ID -1(
		// Individual )
		// 7/10/2015 01:39 || TFL Values for Transaction Scope ID -
		// 1(Individual) are | No of OHL's 45
		// 7/10/2015 01:39 || TFL Count mismatched with No of OHL's
		String transactionStr = "";
		transactionStr = getTodayDateTime()
				+ "||  Validating TFL for THL with Transaction Scope ID - "
				+ transactionScopeID + "\r\n";
		transactionStr += getTodayDateTime()
				+ "|| Validated TFL for Transaction Scope ID -"
				+ transactionScopeID + "\r\n";
		transactionStr += getTodayDateTime()
				+ "|| TFL Values for Transaction Scope ID - "
				+ transactionScopeID + " are | No of OHL's- " + noOfObjects
				+ "\r\n\n";
		if (!flag) {
			transactionStr += getTodayDateTime()
					+ "|| TFL Count mismatched with No of OHL's " + "\r\n\n";
		}
		return transactionStr;
	}

	public static String log_ObjectHeader(String objectId, String sequenceId) {
		// 7/10/2015 01:39 || Validating the THL
		// 7/10/2015 01:39 || THL is Available
		// 7/10/2015 01:39 || THL Values are : Transaction Scope ID -1 |
		// Sequence No -
		String objHeaderStr = "";
		objHeaderStr = getTodayDateTime() + "|| Validating the OHL \r\n";
		objHeaderStr += getTodayDateTime() + "|| OHL is Available \r\n";
		objHeaderStr += getTodayDateTime()
				+ "|| OHL Values are : Transaction Scope ID - " + objectId
				+ "| Sequence No - " + sequenceId + "\r\n\n";
		return objHeaderStr;
	}

	public static String log_ObjectFooter(String objectId, String noOfRecords,
			boolean flag) {
		// 7/10/2015 01:39 || Validating TFL for THL with Transaction Scope ID -
		// 1( Individual )
		// 7/10/2015 01:39 || Validated TFL for Transaction Scope ID -1(
		// Individual )
		// 7/10/2015 01:39 || TFL Values for Transaction Scope ID -
		// 1(Individual) are | No of OHL's 45
		// 7/10/2015 01:39 || OFL Count mismatched with No of OD's
		String objFooterStr = "";
		objFooterStr = getTodayDateTime()
				+ "|| Validating OFL for OFL with Object ID - " + objectId
				+ "\r\n";
		objFooterStr += getTodayDateTime() + "|| Validated OFL for Object ID -"
				+ objectId + "\r\n";
		objFooterStr += getTodayDateTime() + "|| OFL Values for Object ID - "
				+ objectId + " are | No of OHL's- " + noOfRecords + "\r\n\n";
		if (!flag) {
			objFooterStr += getTodayDateTime()
					+ "|| OFL Count mismatched with No of OD's" + "\r\n\n";
		}
		return objFooterStr;
	}

	public static String log_ObjectData(String objectId, String pushId,
			String statusId) {
		// 7/10/2015 01:39 || Validating Status ID for Push Districts ID |2
		// 7/10/2015 01:39 || Validated OD for Object ID -54( Districts ) | Push
		// ID 2 | Status ID - 2( Sync Success )
		String objectDataStr = "";
		objectDataStr = getTodayDateTime()
				+ "||  Validating Status ID for Push ID - " + pushId + "\r\n";
		objectDataStr += getTodayDateTime()
				+ "||  Validated OD for Object ID -" + objectId
				+ "| Push ID - " + pushId + " | Status ID-" + statusId
				+ "\r\n\n";
		return objectDataStr;
	}

	public static String getEmptyServerIds(DBHelper dbh, Context ctx,
			String tableName, String serverIdColoumnName,
			String localIdColoumnName) {
		String jsonStr = "";
		String query = "select " + localIdColoumnName + "  from " + tableName
				+ " where " + serverIdColoumnName + " is null or "
				+ serverIdColoumnName + "='';";
		Cursor cursor = dbh.getCursorData(ctx, query);
		if (cursor != null) {
			JSONArray jsonArray = new JSONArray();
			while (cursor.moveToNext()) {
				JSONObject jsonObj = new JSONObject();
				try {
					jsonObj.put("DeviceLocalID", cursor.getString(cursor
							.getColumnIndex(localIdColoumnName)));
					jsonArray.put(jsonObj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			cursor.close();
			jsonStr += jsonArray.toString();
		}
		return jsonStr;
	}

	public static String getServerIdStatusJSON(Context ctx,
			Vector<String> serverIdsVec, String tableName,
			String serverIdColumnName, DBHelper dbh) {

		String jsonStr = "";
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < serverIdsVec.size(); i++) {
			String serverId = serverIdsVec.elementAt(i).toString().trim();
			String query = "select * from " + tableName + " where "
					+ serverIdColumnName + " ='" + serverId + "';";
			Cursor cursor = dbh.getCursorData(ctx, query);
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("ServerID", serverId);
				if (cursor != null) {
					jsonObj.put("DevicePullSynStatusID", "2");// Server id
																// exists in DB
				} else {
					jsonObj.put("DevicePullSynStatusID", "3");// does not exists
				}

				jsonArray.put(jsonObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		jsonStr += jsonArray.toString();

		return jsonStr;
	}

	public static String getDeviceLocalIdsJSON(String[] deviceLocalIds) {

		String jsonStr = "";
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < deviceLocalIds.length; i++) {
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("DeviceLocalID", deviceLocalIds[i]);
				jsonArray.put(jsonObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		jsonStr += jsonArray.toString();

		return jsonStr;
	}

	public static boolean saveImageToSDCard(Bitmap bitmap) {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/RBSK_V3/");
		if (!myDir.exists()) {
			myDir.mkdir();
		}

		String imageName = Helper.getTodayDate("yyyy-MM-dd HH:mm:ss") + ".jpg";

		File file = new File(myDir, imageName);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Object getKeyFromValue(Map<String, String> hm, Object value) {
		for (Object o : hm.keySet()) {
			if (StringUtils.equalsNoCase(hm.get(o).toString().toLowerCase(),
					(value.toString().toLowerCase()))) {
				return o;
			}
		}
		return null;
	}

	public static byte[] getBytesFromFile(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream is = new FileInputStream(file);
			@SuppressWarnings("resource")
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			buffer = baf.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("bytes" + new String(buffer));
		return buffer;
	}

	public static boolean doesTableExist(Context ctx, DBHelper_sync dbh_sync,
			String tableName) {
		boolean isPresent = false;
		String query = "SELECT name FROM sqlite_master WHERE name='"
				+ tableName + "'";
		Cursor c = dbh_sync.getCursorData(ctx, query);
		if (c != null) {
			return true;
		}
		return isPresent;
	}

	public static String getQueryStr(ArrayList<String> data, int index) {
		String dataStr = "(";
		if (index == 0) {// ( field1, field2,...fieldN )
			for (int i = 0; i < data.size(); i++) {
				dataStr += "[" + data.get(i).trim() + "]" + ",";
			}
			dataStr += "Status" + ")";
		} else {// for values
			for (int i = 0; i < data.size(); i++) {
				dataStr += "?,";
			}
			dataStr += "?" + ")";
		}
		return dataStr;
	}

	public static boolean CheckTableValid(DBHelper dbh, Context ctx,
			String objectId, ArrayList<String> columnData) {
		boolean validFlag = false;
		String query = "select PropertyName from objectproperties where ObjectID='"
				+ objectId + "' order by Position";
		List<String> data = dbh.getColumnDataFromQuery(ctx, query);
		if (data != null) {
			if (columnData.size() != data.size()) {
				validFlag = false;
			} else {
				int count = 0;
				for (int i = 0; i < data.size(); i++) {
					if (!StringUtils.equalsNoCase(data.get(i).trim(),
							(columnData.get(i).trim()))) {
						validFlag = false;
						break;
					} else {
						count++;
					}
				}
				if (count == data.size()) {
					validFlag = true;
				}
			}
		}
		return validFlag;
	}

	public static String getLocalIdForServerId(String tableName,
			String localIdColumnName, String serverIdColumnName,
			String serverIdValue, SQLiteDatabase db) {
		String localId = "";
		String query = "select " + localIdColumnName + " from " + tableName
				+ " where " + serverIdColumnName + "='" + serverIdValue + "';";
		Cursor cur = db.rawQuery(query, null);
		if (cur != null) {
			while (cur.moveToNext()) {
				localId = cur.getString(cur.getColumnIndex(localIdColumnName))
						.trim();
			}
			cur.close();
		}
		/*
		 * while (cur.moveToNext()) { localId =
		 * cur.getString(cur.getColumnIndex(localIdColumnName)) .trim(); }
		 */
		return localId;
	}

	public static String getLocalIdForServerId(String tableName,
			String localIdColumnName, String serverIdColumnName,
			String serverIdValue, DBHelper dbh, Context ctx) {
		String localId = "";
		String query = "select " + localIdColumnName + " from " + tableName
				+ " where " + serverIdColumnName + "='" + serverIdValue + "';";
		Cursor cur = dbh.getCursorData(ctx, query);
		if (cur != null) {
			while (cur.moveToNext()) {
				localId = cur.getString(cur.getColumnIndex(localIdColumnName))
						.trim();
			}
			cur.close();
		}
		/*
		 * while (cur.moveToNext()) { localId =
		 * cur.getString(cur.getColumnIndex(localIdColumnName)) .trim(); }
		 */
		return localId;
	}
}
