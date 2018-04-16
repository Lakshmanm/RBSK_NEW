//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.nunet.wsutil.UrlUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.LoginActivity;
import nunet.rbsk.login.UserLoginActivity;

//*****************************************************************************
//* Name   :  Register_download.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

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
public class Register_download extends Activity implements OnClickListener {
    private Button btn_register_setup;
    private Button btn_register_checkSetupStatus;
    private Button btn_register_continue;
    DBHelper dbHelper;
    String TokenID = "";
    int navIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_download);
        dbHelper = new DBHelper(Register_download.this);
        btn_register_setup = (Button) findViewById(R.id.btn_register_setup);
        btn_register_setup.setOnClickListener(this);

        btn_register_checkSetupStatus = (Button) findViewById(R.id.btn_register_checkSetupStatus);
        btn_register_checkSetupStatus.setOnClickListener(this);

        btn_register_continue = (Button) findViewById(R.id.btn_register_continue);
        btn_register_continue.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

//		SharedPreferences sharedpreferences = getSharedPreferences("LoginMain",
//				Context.MODE_PRIVATE);
//		String DeviceID = sharedpreferences.getString("DeviceID", "");
//		String UnlockID = sharedpreferences.getString("UnlockID", "");
//		String UnlockPassword = sharedpreferences.getString("UnlockPassword",
//				"");
//		String HealthBlockID = sharedpreferences.getString("HealthBlockID", "");
//		String TokenID = sharedpreferences.getString("TokenID", "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register_setup:
                SharedPreferences sharedpreferences = getSharedPreferences(
                        "LoginMain", Context.MODE_PRIVATE);
                TokenID = sharedpreferences.getString("DeviceCode", "");
                // webConn(UrlUtils.URL_INTITAL_SETUP, TokenID);
                navIndex = 0;
                new WebConn().execute(UrlUtils.URL_INTITAL_SETUP + TokenID + "/" + Helper.syncDate + "/1");
//                Toast.makeText(this, "Downloading DBfile", Toast.LENGTH_LONG)
//                        .show();
//                v.setVisibility(View.GONE);
//                btn_register_continue.setVisibility(View.VISIBLE);
//                btn_register_checkSetupStatus.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_register_checkSetupStatus:
                Toast.makeText(this, "DB set up done, you can continue..",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_register_continue:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            default:
                break;
        }

    }

    public String UpdateTable(String strResponse) {
        String retstr = "";
        try {
            if (strResponse.indexOf("{") != -1) {
                strResponse = strResponse.replace("\\", "");
                strResponse = strResponse.substring(1, strResponse.length() - 1);
                JSONObject mJsonObject = new JSONObject(strResponse);
                if (mJsonObject.has("ADDRESS")) {
                    JSONArray ADDRESS = mJsonObject.getJSONArray("ADDRESS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "ADDRESS", ADDRESS);
                }
                if (mJsonObject.has("CONTACTS")) {
                    JSONArray CONTACTS = mJsonObject.getJSONArray("CONTACTS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "CONTACTS", CONTACTS);
                }
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String Query1 = "Select DISTINCT addressID , LocalAddressID from Address";
                Cursor c1 = db.rawQuery(Query1, null);
                List<String> addressID = new ArrayList<>();
                List<String> LocalAddressID = new ArrayList<>();
                while (c1.moveToNext()) {
                    addressID.add(c1.getString(0));
                    LocalAddressID.add(c1.getString(1));
                }
                c1.close();
                String Query2 = "Select DISTINCT contactID, LocalContactID from Contacts";
                Cursor c2 = db.rawQuery(Query2, null);
                List<String> contactID = new ArrayList<>();
                List<String> LocalContactID = new ArrayList<>();
                while (c2.moveToNext()) {
                    contactID.add(c2.getString(0));
                    LocalContactID.add(c2.getString(1));
                }
                c2.close();


                if (mJsonObject.has("USERS")) {

                    JSONArray USERS = mJsonObject.getJSONArray("USERS");
                    for (int i = 0; i < USERS.length(); i++) {
                        JSONObject j = USERS.getJSONObject(i);
                        String insplanID = j.getString("AddressID");
                        if (addressID.contains(insplanID)) {
                            j.put("LocalAddressID", LocalAddressID.get(addressID.indexOf(insplanID)));
                            USERS.put(i, j);
                        }
                        String contID = j.getString("ContactID");
                        if (contactID.contains(contID)) {
                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(contID)));
                            USERS.put(i, j);
                        }
                    }

                    dbHelper.bulkinsertintoTable(Register_download.this, "USERS", USERS);
                }
                String Query6 = "Select DISTINCT userID, LocalUserID from Users";
                Cursor c6 = db.rawQuery(Query6, null);
                List<String> userID = new ArrayList<>();
                List<String> LocalUserID = new ArrayList<>();
                while (c6.moveToNext()) {
                    userID.add(c6.getString(0));
                    LocalUserID.add(c6.getString(1));
                }
                c6.close();
                db.close();
                if (mJsonObject.has("INSTITUTES")) {
                    JSONArray INSTITUTES = mJsonObject.getJSONArray("INSTITUTES");

                    for (int i = 0; i < INSTITUTES.length(); i++) {
                        JSONObject j = INSTITUTES.getJSONObject(i);
                        String insplanID = j.getString("AddressID");
                        if (addressID.contains(insplanID)) {
                            j.put("LocalAddressID", LocalAddressID.get(addressID.indexOf(insplanID)));
                            INSTITUTES.put(i, j);
                        }
                        String contID = j.getString("ContactID");
                        if (contactID.contains(contID.trim())) {
                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(contID)));
                            INSTITUTES.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTES", INSTITUTES);


                }
                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                String Query3 = "Select DISTINCT InstituteID , LocalInstituteID from Institutes";
                Cursor c3 = db2.rawQuery(Query3, null);
                List<String> InstituteID = new ArrayList<>();
                List<String> LocalInstituteID = new ArrayList<>();
                while (c3.moveToNext()) {
                    InstituteID.add(c3.getString(0));
                    LocalInstituteID.add(c3.getString(1));
                }
                c3.close();
                db2.close();
                if (mJsonObject.has("INSTITUTEPICTURES")) {
                    JSONArray INSTITUTEPICTURES = mJsonObject.getJSONArray("INSTITUTEPICTURES");
                    for (int i = 0; i < INSTITUTEPICTURES.length(); i++) {
                        JSONObject j = INSTITUTEPICTURES.getJSONObject(i);
                        String insplanID = j.getString("InstituteID");
                        if (InstituteID.contains(insplanID.trim())) {
                            j.put("LocalInstituteID", LocalInstituteID.get(InstituteID.indexOf(insplanID)));
                            INSTITUTEPICTURES.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTEPICTURES", INSTITUTEPICTURES);
                }
                if (mJsonObject.has("INSTITUTEPLANS")) {
                    JSONArray INSTITUTEPLANS = mJsonObject.getJSONArray("INSTITUTEPLANS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTEPLANS", INSTITUTEPLANS);
                }

                if (mJsonObject.has("INSTITUTEPLANDETAILS")) {
                    JSONArray INSTITUTEPLANDETAILS = mJsonObject.getJSONArray("INSTITUTEPLANDETAILS");
                    SQLiteDatabase dbin = dbHelper.getWritableDatabase();
                    String Query5 = "Select DISTINCT InstitutePlanID , localinstituteplanid from INSTITUTEPLANS";
                    Cursor c5 = dbin.rawQuery(Query5, null);
                    List<String> InstitutePlanID = new ArrayList<>();
                    List<String> localinstituteplanid = new ArrayList<>();
                    while (c5.moveToNext()) {
                        InstitutePlanID.add(c5.getString(0));
                        localinstituteplanid.add(c5.getString(1));
                    }
                    c5.close();
                    dbin.close();
                    for (int i = 0; i < INSTITUTEPLANDETAILS.length(); i++) {
                        JSONObject j = INSTITUTEPLANDETAILS.getJSONObject(i);
                        String insplanID = j.getString("InstitutePlanID");
                        if (InstitutePlanID.contains(insplanID.trim())) {
                            j.put("LocalInstitutePlanID", localinstituteplanid.get(InstitutePlanID.indexOf(insplanID)));
                            INSTITUTEPLANDETAILS.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTEPLANDETAILS", INSTITUTEPLANDETAILS);

                }

                if (mJsonObject.has("INSTITUTESTAFF")) {
                    JSONArray INSTITUTESTAFF = mJsonObject.getJSONArray("INSTITUTESTAFF");

                    for (int i = 0; i < INSTITUTESTAFF.length(); i++) {
                        JSONObject j = INSTITUTESTAFF.getJSONObject(i);
                        String insplanID = j.getString("InstituteID");
                        if (InstituteID.contains(insplanID.trim())) {
                            j.put("LocalInstituteID", LocalInstituteID.get(InstituteID.indexOf(insplanID)));
                            INSTITUTESTAFF.put(i, j);
                        }
                        String contID = j.getString("UserID");
                        if (userID.contains(contID.trim())) {
                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(contID)));
                            INSTITUTESTAFF.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESTAFF", INSTITUTESTAFF);
                }
                if (mJsonObject.has("INSTITUTESCREENING")) {
                    JSONArray INSTITUTESCREENING = mJsonObject.getJSONArray("INSTITUTESCREENING");
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESCREENING", INSTITUTESCREENING);
                }
                if (mJsonObject.has("INSTITUTESCREENINGDETAILS")) {
                    JSONArray INSTITUTESCREENINGDETAILS = mJsonObject.getJSONArray("INSTITUTESCREENINGDETAILS");
                    SQLiteDatabase db4 = dbHelper.getWritableDatabase();
                    String Query14 = "Select DISTINCT institutescreeningid , localinstitutescreeningid from INSTITUTESCREENING";
                    Cursor c14 = db4.rawQuery(Query14, null);
                    List<String> institutescreeningid = new ArrayList<>();
                    List<String> localinstitutescreeningid = new ArrayList<>();
                    while (c14.moveToNext()) {
                        institutescreeningid.add(c14.getString(0));
                        localinstitutescreeningid.add(c14.getString(1));
                    }
                    c14.close();
                    db4.close();
                    for (int i = 0; i < INSTITUTESCREENINGDETAILS.length(); i++) {
                        JSONObject j = INSTITUTESCREENINGDETAILS.getJSONObject(i);
                        String insplanID = j.getString("InstituteScreeningID");
                        if (institutescreeningid.contains(insplanID.trim())) {
                            j.put("LocalInstituteScreeningID", localinstitutescreeningid.get(institutescreeningid.indexOf(insplanID)));
                            INSTITUTESCREENINGDETAILS.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESCREENINGDETAILS", INSTITUTESCREENINGDETAILS);
                }


                if (mJsonObject.has("CONTACTDETAILS")) {

                    JSONArray CONTACTDETAILS = mJsonObject.getJSONArray("CONTACTDETAILS");
                    for (int i = 0; i < CONTACTDETAILS.length(); i++) {
                        JSONObject j = CONTACTDETAILS.getJSONObject(i);
                        String insplanID = j.getString("ContactID");
                        if (contactID.contains(insplanID)) {
                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(insplanID)));
                            CONTACTDETAILS.put(i, j);
                        }

                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CONTACTDETAILS", CONTACTDETAILS);
                }
                if (mJsonObject.has("DEVICESETTINGS")) {
                    JSONArray DEVICESETTINGS = mJsonObject.getJSONArray("DEVICESETTINGS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "DEVICESETTINGS", DEVICESETTINGS);
                }
                if (mJsonObject.has("FACILITIES")) {
                    JSONArray FACILITIES = mJsonObject.getJSONArray("FACILITIES");

                    for (int i = 0; i < FACILITIES.length(); i++) {
                        JSONObject j = FACILITIES.getJSONObject(i);
                        String insplanID = j.getString("AddressID");
                        if (addressID.contains(insplanID)) {
                            j.put("LocalAddressID", LocalAddressID.get(addressID.indexOf(insplanID)));
                            FACILITIES.put(i, j);
                        }
                        String contID = j.getString("ContactID");
                        if (contactID.contains(contID)) {
                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(contID)));
                            FACILITIES.put(i, j);
                        }
                    }

                    dbHelper.bulkinsertintoTable(Register_download.this, "FACILITIES", FACILITIES);
                }
                if (mJsonObject.has("FACILITYCOVERAGE")) {
                    JSONArray FACILITYCOVERAGE = mJsonObject.getJSONArray("FACILITYCOVERAGE");
                    dbHelper.bulkinsertintoTable(Register_download.this, "FACILITYCOVERAGE", FACILITYCOVERAGE);
                }
                if (mJsonObject.has("HABITATS")) {
                    JSONArray HABITATS = mJsonObject.getJSONArray("HABITATS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "HABITATS", HABITATS);
                }
                if (mJsonObject.has("MHTSTAFF")) {
                    JSONArray MHTSTAFF = mJsonObject.getJSONArray("MHTSTAFF");

                    for (int i = 0; i < MHTSTAFF.length(); i++) {
                        JSONObject j = MHTSTAFF.getJSONObject(i);
                        String insplanID = j.getString("UserID");
                        if (userID.contains(insplanID)) {
                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(insplanID)));
                            MHTSTAFF.put(i, j);
                        }

                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "MHTSTAFF", MHTSTAFF);
                }
                if (mJsonObject.has("MOBILEHEALTHTEAMS")) {
                    JSONArray MOBILEHEALTHTEAMS = mJsonObject.getJSONArray("MOBILEHEALTHTEAMS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "MOBILEHEALTHTEAMS", MOBILEHEALTHTEAMS);
                }
                if (mJsonObject.has("USERCREDENTIALS")) {
                    JSONArray USERCREDENTIALS = mJsonObject.getJSONArray("USERCREDENTIALS");

                    for (int i = 0; i < USERCREDENTIALS.length(); i++) {
                        JSONObject j = USERCREDENTIALS.getJSONObject(i);
                        String insplanID = j.getString("UserID");
                        if (userID.contains(insplanID)) {
                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(insplanID)));
                            USERCREDENTIALS.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "USERCREDENTIALS", USERCREDENTIALS);
                }
                if (mJsonObject.has("VILLAGES")) {
                    JSONArray VILLAGES = mJsonObject.getJSONArray("VILLAGES");
                    dbHelper.bulkinsertintoTable(Register_download.this, "VILLAGES", VILLAGES);
                }

                retstr = "200";


            } else {
                retstr = strResponse;

            }
        } catch (Exception e) {
            e.printStackTrace();
            retstr = "";
        }

        return retstr;
    }

    public String UpdateTable1(String strResponse) {
        String retStr = "";
        try {
            if (strResponse.indexOf("{") != -1) {
                strResponse = strResponse.replace("\\", "");
                strResponse = strResponse.substring(1, strResponse.length() - 1);
                JSONObject mJsonObject = new JSONObject(strResponse);
                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                String Query3 = "Select DISTINCT InstituteID , LocalInstituteID from Institutes";
                Cursor c3 = db2.rawQuery(Query3, null);
                List<String> InstituteID = new ArrayList<>();
                List<String> LocalInstituteID = new ArrayList<>();
                while (c3.moveToNext()) {
                    InstituteID.add(c3.getString(0));
                    LocalInstituteID.add(c3.getString(1));
                }
                c3.close();

                String Query6 = "Select DISTINCT userID, LocalUserID from Users";
                Cursor c6 = db2.rawQuery(Query6, null);
                List<String> userID = new ArrayList<>();
                List<String> LocalUserID = new ArrayList<>();
                while (c6.moveToNext()) {
                    userID.add(c6.getString(0));
                    LocalUserID.add(c6.getString(1));
                }
                c6.close();
                db2.close();


                if (mJsonObject.has("CHILDREN")) {
                    JSONArray CHILDREN = mJsonObject.getJSONArray("CHILDREN");

                    for (int i = 0; i < CHILDREN.length(); i++) {
                        JSONObject j = CHILDREN.getJSONObject(i);
                        String insplanID = j.getString("InstituteID");
                        if (InstituteID.contains(insplanID.trim())) {
                            j.put("LocalInstituteID", LocalInstituteID.get(InstituteID.indexOf(insplanID)));
                            CHILDREN.put(i, j);
                        }
                        String contID = j.getString("UserID");
                        if (userID.contains(contID.trim())) {
                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(contID)));
                            CHILDREN.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDREN", CHILDREN);
                }
                SQLiteDatabase db8 = dbHelper.getWritableDatabase();
                String Query18 = "Select DISTINCT childrenID , LocalChildrenID from Children";
                Cursor c18 = db8.rawQuery(Query18, null);
                List<String> childrenID = new ArrayList<>();
                List<String> LocalChildrenID = new ArrayList<>();
                while (c18.moveToNext()) {
                    childrenID.add(c18.getString(0));
                    LocalChildrenID.add(c18.getString(1));
                }
                c18.close();
                db8.close();
                if (mJsonObject.has("CHILDRENSCREENING")) {
                    JSONArray CHILDRENSCREENING = mJsonObject.getJSONArray("CHILDRENSCREENING");

                    for (int i = 0; i < CHILDRENSCREENING.length(); i++) {
                        JSONObject j = CHILDRENSCREENING.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENSCREENING.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENING", CHILDRENSCREENING);
                }
                SQLiteDatabase db9 = dbHelper.getWritableDatabase();
                String Query19 = "Select DISTINCT childrenscreeningID , LocalChildrenScreeningID from ChildrenScreening";
                Cursor c19 = db9.rawQuery(Query19, null);
                List<String> childrenscreeningID = new ArrayList<>();
                List<String> LocalChildrenScreeningID = new ArrayList<>();
                while (c19.moveToNext()) {
                    childrenscreeningID.add(c19.getString(0));
                    LocalChildrenScreeningID.add(c19.getString(1));
                }
                c19.close();
                db9.close();
                if (mJsonObject.has("CHILDRENSCREENINGREFERRALS")) {
                    JSONArray CHILDRENSCREENINGREFERRALS = mJsonObject.getJSONArray("CHILDRENSCREENINGREFERRALS");

                    for (int i = 0; i < CHILDRENSCREENINGREFERRALS.length(); i++) {
                        JSONObject j = CHILDRENSCREENINGREFERRALS.getJSONObject(i);
                        String insplanID = j.getString("ChildrenScreeningID");
                        if (childrenscreeningID.contains(insplanID.trim())) {
                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
                            CHILDRENSCREENINGREFERRALS.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGREFERRALS", CHILDRENSCREENINGREFERRALS);
                }
                if (mJsonObject.has("CHILDRENSCREENINGINVESTIGATIONS")) {
                    JSONArray CHILDRENSCREENINGINVESTIGATIONS = mJsonObject.getJSONArray("CHILDRENSCREENINGINVESTIGATIONS");
                    SQLiteDatabase db90 = dbHelper.getWritableDatabase();
                    String Query190 = "Select DISTINCT ChildrenScreeningReferralID , LocalChildrenScreeningReferralID from CHILDRENSCREENINGREFERRALS";
                    Cursor c190 = db90.rawQuery(Query190, null);
                    List<String> ChildrenScreeningReferralID = new ArrayList<>();
                    List<String> LocalChildrenScreeningReferralID = new ArrayList<>();
                    while (c190.moveToNext()) {
                        ChildrenScreeningReferralID.add(c190.getString(0));
                        LocalChildrenScreeningReferralID.add(c190.getString(1));
                    }
                    c190.close();
                    db90.close();
                    for (int i = 0; i < CHILDRENSCREENINGINVESTIGATIONS.length(); i++) {
                        JSONObject j = CHILDRENSCREENINGINVESTIGATIONS.getJSONObject(i);
                        String insplanID = j.getString("ChildrenScreeningReferralID");
                        if (ChildrenScreeningReferralID.contains(insplanID.trim())) {
                            j.put("LocalChildrenScreeningReferralID", LocalChildrenScreeningReferralID.get(ChildrenScreeningReferralID.indexOf(insplanID)));
                            CHILDRENSCREENINGINVESTIGATIONS.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGINVESTIGATIONS", CHILDRENSCREENINGINVESTIGATIONS);
                }
                if (mJsonObject.has("CHILDRENPICTURES")) {
                    JSONArray CHILDRENPICTURES = mJsonObject.getJSONArray("CHILDRENPICTURES");

                    for (int i = 0; i < CHILDRENPICTURES.length(); i++) {
                        JSONObject j = CHILDRENPICTURES.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENPICTURES.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENPICTURES", CHILDRENPICTURES);
                }

                if (mJsonObject.has("CHILDRENSCREENINGALLERGIES")) {
                    JSONArray CHILDRENSCREENINGALLERGIES = mJsonObject.getJSONArray("CHILDRENSCREENINGALLERGIES");

                    for (int i = 0; i < CHILDRENSCREENINGALLERGIES.length(); i++) {
                        JSONObject j = CHILDRENSCREENINGALLERGIES.getJSONObject(i);
                        String insplanID = j.getString("ChildrenScreeningID");
                        if (childrenscreeningID.contains(insplanID.trim())) {
                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
                            CHILDRENSCREENINGALLERGIES.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGALLERGIES", CHILDRENSCREENINGALLERGIES);
                }
                if (mJsonObject.has("CHILDRENSCREENINGHISTORY")) {
                    JSONArray CHILDRENSCREENINGHISTORY = mJsonObject.getJSONArray("CHILDRENSCREENINGHISTORY");

                    for (int i = 0; i < CHILDRENSCREENINGHISTORY.length(); i++) {
                        JSONObject j = CHILDRENSCREENINGHISTORY.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENSCREENINGHISTORY.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGHISTORY", CHILDRENSCREENINGHISTORY);
                }
                if (mJsonObject.has("CHILDRENSCREENINGLOCALTREATMENT")) {
                    JSONArray CHILDRENSCREENINGLOCALTREATMENT = mJsonObject.getJSONArray("CHILDRENSCREENINGLOCALTREATMENT");

                    for (int i = 0; i < CHILDRENSCREENINGLOCALTREATMENT.length(); i++) {
                        JSONObject j = CHILDRENSCREENINGLOCALTREATMENT.getJSONObject(i);
                        String insplanID = j.getString("ChildrenScreeningID");
                        if (childrenscreeningID.contains(insplanID.trim())) {
                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
                            CHILDRENSCREENINGLOCALTREATMENT.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGLOCALTREATMENT", CHILDRENSCREENINGLOCALTREATMENT);
                }
                if (mJsonObject.has("CHILDRENSCREENINGPE")) {
                    JSONArray CHILDRENSCREENINGPE = mJsonObject.getJSONArray("CHILDRENSCREENINGPE");

                    for (int i = 0; i < CHILDRENSCREENINGPE.length(); i++) {
                        JSONObject j = CHILDRENSCREENINGPE.getJSONObject(i);
                        String insplanID = j.getString("ChildrenScreeningID");
                        if (childrenscreeningID.contains(insplanID.trim())) {
                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
                            CHILDRENSCREENINGPE.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGPE", CHILDRENSCREENINGPE);
                }
                if (mJsonObject.has("CHILDRENFAMILYHISTORY")) {
                    JSONArray CHILDRENFAMILYHISTORY = mJsonObject.getJSONArray("CHILDRENFAMILYHISTORY");

                    for (int i = 0; i < CHILDRENFAMILYHISTORY.length(); i++) {
                        JSONObject j = CHILDRENFAMILYHISTORY.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENFAMILYHISTORY.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENFAMILYHISTORY", CHILDRENFAMILYHISTORY);
                }
                if (mJsonObject.has("CHILDRENMEDICALHISTORY")) {
                    JSONArray CHILDRENMEDICALHISTORY = mJsonObject.getJSONArray("CHILDRENMEDICALHISTORY");

                    for (int i = 0; i < CHILDRENMEDICALHISTORY.length(); i++) {
                        JSONObject j = CHILDRENMEDICALHISTORY.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENMEDICALHISTORY.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENMEDICALHISTORY", CHILDRENMEDICALHISTORY);
                }
                if (mJsonObject.has("CHILDRENPARENTS")) {
                    JSONArray CHILDRENPARENTS = mJsonObject.getJSONArray("CHILDRENPARENTS");

                    for (int i = 0; i < CHILDRENPARENTS.length(); i++) {
                        JSONObject j = CHILDRENPARENTS.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENPARENTS.put(i, j);
                        }
                        String contID = j.getString("UserID");
                        if (userID.contains(contID.trim())) {
                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(contID)));
                            CHILDRENPARENTS.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENPARENTS", CHILDRENPARENTS);
                }
                if (mJsonObject.has("CHILDRENALLERGIESHISTORY")) {
                    JSONArray CHILDRENALLERGIESHISTORY = mJsonObject.getJSONArray("CHILDRENALLERGIESHISTORY");

                    for (int i = 0; i < CHILDRENALLERGIESHISTORY.length(); i++) {
                        JSONObject j = CHILDRENALLERGIESHISTORY.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENALLERGIESHISTORY.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENALLERGIESHISTORY", CHILDRENALLERGIESHISTORY);
                }
                if (mJsonObject.has("CHILDRENSCREENINGPICTURES")) {
                    JSONArray CHILDRENSCREENINGPICTURES = mJsonObject.getJSONArray("CHILDRENSCREENINGPICTURES");
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGPICTURES", CHILDRENSCREENINGPICTURES);
                }
                if (mJsonObject.has("CHILDRENSCREENINGRECOMMENDATIONS")) {
                    JSONArray CHILDRENSCREENINGRECOMMENDATIONS = mJsonObject.getJSONArray("CHILDRENSCREENINGRECOMMENDATIONS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGRECOMMENDATIONS", CHILDRENSCREENINGRECOMMENDATIONS);
                }

                if (mJsonObject.has("CHILDRENDISABILITIES")) {
                    JSONArray CHILDRENDISABILITIES = mJsonObject.getJSONArray("CHILDRENDISABILITIES");

                    for (int i = 0; i < CHILDRENDISABILITIES.length(); i++) {
                        JSONObject j = CHILDRENDISABILITIES.getJSONObject(i);
                        String insplanID = j.getString("ChildrenID");
                        if (childrenID.contains(insplanID.trim())) {
                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
                            CHILDRENDISABILITIES.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENDISABILITIES", CHILDRENDISABILITIES);
                }
                if (mJsonObject.has("CHILDRENSCREENINGSURGICALS")) {
                    JSONArray CHILDRENSCREENINGSURGICALS = mJsonObject.getJSONArray("CHILDRENSCREENINGSURGICALS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGSURGICALS", CHILDRENSCREENINGSURGICALS);
                }
                if (mJsonObject.has("CHILDRENSCREENINGVITALS")) {
                    JSONArray CHILDRENSCREENINGVITALS = mJsonObject.getJSONArray("CHILDRENSCREENINGVITALS");

                    for (int i = 0; i < CHILDRENSCREENINGVITALS.length(); i++) {
                        JSONObject j = CHILDRENSCREENINGVITALS.getJSONObject(i);
                        String insplanID = j.getString("ChildrenScreeningID");
                        if (childrenscreeningID.contains(insplanID.trim())) {
                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
                            CHILDRENSCREENINGVITALS.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGVITALS", CHILDRENSCREENINGVITALS);
                }
                if (mJsonObject.has("CHILDRENSCREENINGSTATUSES")) {
                    JSONArray CHILDRENSCREENINGSTATUSES = mJsonObject.getJSONArray("CHILDRENSCREENINGSTATUSES");
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGSTATUSES", CHILDRENSCREENINGSTATUSES);
                }
                if (mJsonObject.has("CHILDRENSURGICALHISTORY")) {
                    JSONArray CHILDRENSURGICALHISTORY = mJsonObject.getJSONArray("CHILDRENSURGICALHISTORY");
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSURGICALHISTORY", CHILDRENSURGICALHISTORY);
                }
                if (mJsonObject.has("CHILDSCREENINGFH")) {
                    JSONArray CHILDSCREENINGFH = mJsonObject.getJSONArray("CHILDSCREENINGFH");

                    for (int i = 0; i < CHILDSCREENINGFH.length(); i++) {
                        JSONObject j = CHILDSCREENINGFH.getJSONObject(i);
                        String insplanID = j.getString("ChildrenScreeningID");
                        if (childrenscreeningID.contains(insplanID.trim())) {
                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
                            CHILDSCREENINGFH.put(i, j);
                        }
                    }
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDSCREENINGFH", CHILDSCREENINGFH);
                }
                if (mJsonObject.has("SyncDate")) {
                    Log.e("sync dt frm service 2", mJsonObject.getString("SyncDate"));
                    Helper.syncDate1 = mJsonObject.getString("SyncDate");
                    SharedPreferences sharedpreferences = getSharedPreferences(
                            UserLoginActivity.UserLogin, Context.MODE_PRIVATE);

                    sharedpreferences.edit().putString("SyncDate1", Helper.syncDate1).commit();
                }
                retStr = "200";
            } else {
                retStr = strResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            retStr = "";
        }


        return retStr;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Helper.progressDialog != null && !Helper.progressDialog.isShowing())
//            Helper.showProgressDialog(Register_download.this);
    }

    public class WebConn extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            if (navIndex == 0)
                Helper.showProgressDialog(Register_download.this);

        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            Log.e("url in intitial setup", params[0]);
            HttpGet httppost = new HttpGet(params[0]);
            String strResponse = "";
            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "UTF8"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine());
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                System.out.println("output post...." + sb.toString());
                if (navIndex == 0)
                    strResponse = UpdateTable(sb.toString());
                else
                    strResponse = UpdateTable1(sb.toString());

            } catch (ClientProtocolException e) {
                strResponse = "ClientProtocolException";
            } catch (IOException e) {
                strResponse = "There is no network";
            } catch (Exception e) {
                strResponse = "Exception";
            }
            return strResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            try {

                System.out.println("in post...." + response);

                if (response.trim().length() == 0) {
                    if (Helper.progressDialog != null && Helper.progressDialog.isShowing())
                        Helper.progressDialog.dismiss();
                    Helper.showShortToast(Register_download.this, "There is a problem in setup process so please try again...");
                } else if (response.trim().equalsIgnoreCase("200")) {
                    if (navIndex == 0) {
                        navIndex = 1;
                        new WebConn().execute(UrlUtils.URL_INTITAL_SETUP + TokenID + "/" + Helper.syncDate +"/2");
                    } else {
                        if (Helper.progressDialog != null && Helper.progressDialog.isShowing())
                            Helper.progressDialog.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(
                                "LoginMain", Context.MODE_PRIVATE);
                        sharedpreferences = getSharedPreferences(
                                UserLoginActivity.UserLogin, Context.MODE_PRIVATE);

                        sharedpreferences.edit().putString("DB", "Yes").commit();
                        btn_register_continue.setVisibility(View.VISIBLE);
                        btn_register_checkSetupStatus.setVisibility(View.VISIBLE);
                        btn_register_setup.setVisibility(View.GONE);
                    }

                } else {
                    if (Helper.progressDialog != null && Helper.progressDialog.isShowing())
                        Helper.progressDialog.dismiss();
                    Helper.showShortToast(Register_download.this, response);
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


}
