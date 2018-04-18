package nunet.rbsk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.LoginActivity;
import nunet.rbsk.login.UserLoginActivity;

public class BaseActivity extends Activity {

    int navIndex = 0;
    String syncDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(true);

    }

    /**
     * to creare action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionsoverlay, menu);
        MenuItem item = menu.findItem(R.id.menu_userName);
        SharedPreferences sharedpreferences = getSharedPreferences("RbskPref",
                Context.MODE_PRIVATE);
        item.setTitle(sharedpreferences.getString("userKey", ""));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.settings:
                Intent in = new Intent(BaseActivity.this,
                        SettingsPlanActivity.class);
                startActivity(in);
                break;
            case R.id.sendInc:
                // Praveen Code Start
                Toast.makeText(this, "This feature is not available in this version",
                        Toast.LENGTH_SHORT).show();
            /*in = new Intent(BaseActivity.this, IncrementalService.class);
            stopService(in);
			startService(in);
			*/
                // Praveen Code Ends
                break;

            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation Alert")
                        .setMessage(
                                "Are you sure you want to Logout from the Application")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent logout = new Intent(
                                                BaseActivity.this,
                                                LoginActivity.class);
                                        startActivity(logout);

                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // do nothing
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.backupdb:
                // Praveen Code Start
                Toast.makeText(this, "Backup is started, Please don't do any operation for few minutes",
                        Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                } else {
                    copyDatabase();
                    // backupdb(this);
                }

                // backupdb(this);
                // Praveen Code Ends

                break;

            case R.id.logsync:
                // Praveen Code Start
                new WebConn().execute(syncData());
//                Toast.makeText(this, "This feature is not available in this version",
//                        Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this, DBTestActivity.class));
                // Praveen Code Ends
                break;
            case R.id.hybrid_DB:
                // Praveen Code Start
                Toast.makeText(this, "This feature is not available in this version",
                        Toast.LENGTH_SHORT).show();
                //copyDatabase(DB_NAME_HYB);
                // Praveen Code Ends
                break;
            case R.id.qa_DB:
                // Praveen Code Start
                Toast.makeText(this, "This feature is not available in this version",
                        Toast.LENGTH_SHORT).show();
                //copyDatabase(DB_NAME_QA);
                // Praveen Code Ends
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public class WebConn extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected void onPreExecute() {
            Helper.showProgressDialog(BaseActivity.this);

        }

        @Override
        protected String doInBackground(JSONObject... params) {

            String mResultData = "", strResponse = "";
            try {
                URL url = new URL(UrlUtils.URL_SYNC);
                URLConnection urlConn = url.openConnection();
                String j = params[0].toString();

                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setConnectTimeout((3 * 60 * 1000));
                httpConn.setReadTimeout((3 * 60 * 1000));
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                httpConn.setRequestProperty("Content-Type",
                        "application/json;charset=utf-8");
                httpConn.setRequestMethod("POST");
                httpConn.connect();

                OutputStream os = new BufferedOutputStream(
                        httpConn.getOutputStream());
                os.write(j.getBytes());
                os.flush();
                os.close();
                InputStream content = httpConn.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(content, "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                content.close();

                mResultData = sb.toString();
                Log.e("response in sync...", mResultData);
                strResponse = updateSyncTables(mResultData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                strResponse = "ClientProtocolException";
            } catch (IOException e) {
                e.printStackTrace();
                strResponse = "There is no network";
            } catch (Exception e) {
                e.printStackTrace();
                strResponse = "Exception";
            }
            return strResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            try {

                System.out.println("in post...." + response);


                if (response.trim().equalsIgnoreCase("200")) {
                    SharedPreferences sharedpreferences = getSharedPreferences(
                            "LoginMain", Context.MODE_PRIVATE);
                    String TokenID = sharedpreferences.getString("DeviceCode", "");
                    // webConn(UrlUtils.URL_INTITAL_SETUP, TokenID);
                    navIndex = 0;
                    new WebConn1().execute(UrlUtils.URL_INTITAL_SETUP + TokenID + "/" + Helper.syncDate1 + "/1");
                } else {
                    Helper.progressDialog.dismiss();
                    Helper.showShortToast(BaseActivity.this, response);
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public String UpdateTable(String strResponse) {
        String retstr = "";
        try {
            if (strResponse.indexOf("{") != -1) {
                DBHelper dbHelper = new DBHelper(BaseActivity.this);
                strResponse = strResponse.replace("\\", "");
                strResponse = strResponse.substring(1, strResponse.length() - 1);
                JSONObject mJsonObject = new JSONObject(strResponse);
//                if (mJsonObject.has("ADDRESS")) {
//                    JSONArray ADDRESS = mJsonObject.getJSONArray("ADDRESS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "ADDRESS", ADDRESS);
//                }
//                if (mJsonObject.has("CONTACTS")) {
//                    JSONArray CONTACTS = mJsonObject.getJSONArray("CONTACTS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CONTACTS", CONTACTS);
//                }
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                String Query1 = "Select DISTINCT addressID , LocalAddressID from Address";
//                Cursor c1 = db.rawQuery(Query1, null);
//                List<String> addressID = new ArrayList<>();
//                List<String> LocalAddressID = new ArrayList<>();
//                while (c1.moveToNext()) {
//                    addressID.add(c1.getString(0));
//                    LocalAddressID.add(c1.getString(1));
//                }
//                c1.close();
//                String Query2 = "Select DISTINCT contactID, LocalContactID from Contacts";
//                Cursor c2 = db.rawQuery(Query2, null);
//                List<String> contactID = new ArrayList<>();
//                List<String> LocalContactID = new ArrayList<>();
//                while (c2.moveToNext()) {
//                    contactID.add(c2.getString(0));
//                    LocalContactID.add(c2.getString(1));
//                }
//                c2.close();
//
//
//                if (mJsonObject.has("USERS")) {
//
//                    JSONArray USERS = mJsonObject.getJSONArray("USERS");
//                    for (int i = 0; i < USERS.length(); i++) {
//                        JSONObject j = USERS.getJSONObject(i);
//                        String insplanID = j.getString("AddressID");
//                        if (addressID.contains(insplanID)) {
//                            j.put("LocalAddressID", LocalAddressID.get(addressID.indexOf(insplanID)));
//                            USERS.put(i, j);
//                        }
//                        String contID = j.getString("ContactID");
//                        if (contactID.contains(contID)) {
//                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(contID)));
//                            USERS.put(i, j);
//                        }
//                    }
//
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "USERS", USERS);
//                }
//                String Query6 = "Select DISTINCT userID, LocalUserID from Users";
//                Cursor c6 = db.rawQuery(Query6, null);
//                List<String> userID = new ArrayList<>();
//                List<String> LocalUserID = new ArrayList<>();
//                while (c6.moveToNext()) {
//                    userID.add(c6.getString(0));
//                    LocalUserID.add(c6.getString(1));
//                }
//                c6.close();
//                db.close();
//                if (mJsonObject.has("INSTITUTES")) {
//                    JSONArray INSTITUTES = mJsonObject.getJSONArray("INSTITUTES");
//
//                    for (int i = 0; i < INSTITUTES.length(); i++) {
//                        JSONObject j = INSTITUTES.getJSONObject(i);
//                        String insplanID = j.getString("AddressID");
//                        if (addressID.contains(insplanID)) {
//                            j.put("LocalAddressID", LocalAddressID.get(addressID.indexOf(insplanID)));
//                            INSTITUTES.put(i, j);
//                        }
//                        String contID = j.getString("ContactID");
//                        if (contactID.contains(contID.trim())) {
//                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(contID)));
//                            INSTITUTES.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "INSTITUTES", INSTITUTES);
//
//
//                }
//                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
//                String Query3 = "Select DISTINCT InstituteID , LocalInstituteID from Institutes";
//                Cursor c3 = db2.rawQuery(Query3, null);
//                List<String> InstituteID = new ArrayList<>();
//                List<String> LocalInstituteID = new ArrayList<>();
//                while (c3.moveToNext()) {
//                    InstituteID.add(c3.getString(0));
//                    LocalInstituteID.add(c3.getString(1));
//                }
//                c3.close();
//                db2.close();
//                if (mJsonObject.has("INSTITUTEPICTURES")) {
//                    JSONArray INSTITUTEPICTURES = mJsonObject.getJSONArray("INSTITUTEPICTURES");
//                    for (int i = 0; i < INSTITUTEPICTURES.length(); i++) {
//                        JSONObject j = INSTITUTEPICTURES.getJSONObject(i);
//                        String insplanID = j.getString("InstituteID");
//                        if (InstituteID.contains(insplanID.trim())) {
//                            j.put("LocalInstituteID", LocalInstituteID.get(InstituteID.indexOf(insplanID)));
//                            INSTITUTEPICTURES.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "INSTITUTEPICTURES", INSTITUTEPICTURES);
//                }
//                if (mJsonObject.has("INSTITUTEPLANS")) {
//                    JSONArray INSTITUTEPLANS = mJsonObject.getJSONArray("INSTITUTEPLANS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "INSTITUTEPLANS", INSTITUTEPLANS);
//                }
//
//                if (mJsonObject.has("INSTITUTEPLANDETAILS")) {
//                    JSONArray INSTITUTEPLANDETAILS = mJsonObject.getJSONArray("INSTITUTEPLANDETAILS");
//                    SQLiteDatabase dbin = dbHelper.getWritableDatabase();
//                    String Query5 = "Select DISTINCT InstitutePlanID , localinstituteplanid from INSTITUTEPLANS";
//                    Cursor c5 = dbin.rawQuery(Query5, null);
//                    List<String> InstitutePlanID = new ArrayList<>();
//                    List<String> localinstituteplanid = new ArrayList<>();
//                    while (c5.moveToNext()) {
//                        InstitutePlanID.add(c5.getString(0));
//                        localinstituteplanid.add(c5.getString(1));
//                    }
//                    c5.close();
//                    dbin.close();
//                    for (int i = 0; i < INSTITUTEPLANDETAILS.length(); i++) {
//                        JSONObject j = INSTITUTEPLANDETAILS.getJSONObject(i);
//                        String insplanID = j.getString("InstitutePlanID");
//                        if (InstitutePlanID.contains(insplanID.trim())) {
//                            j.put("LocalInstitutePlanID", localinstituteplanid.get(InstitutePlanID.indexOf(insplanID)));
//                            INSTITUTEPLANDETAILS.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "INSTITUTEPLANDETAILS", INSTITUTEPLANDETAILS);
//
//                }
//
//                if (mJsonObject.has("INSTITUTESTAFF")) {
//                    JSONArray INSTITUTESTAFF = mJsonObject.getJSONArray("INSTITUTESTAFF");
//
//                    for (int i = 0; i < INSTITUTESTAFF.length(); i++) {
//                        JSONObject j = INSTITUTESTAFF.getJSONObject(i);
//                        String insplanID = j.getString("InstituteID");
//                        if (InstituteID.contains(insplanID.trim())) {
//                            j.put("LocalInstituteID", LocalInstituteID.get(InstituteID.indexOf(insplanID)));
//                            INSTITUTESTAFF.put(i, j);
//                        }
//                        String contID = j.getString("UserID");
//                        if (userID.contains(contID.trim())) {
//                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(contID)));
//                            INSTITUTESTAFF.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "INSTITUTESTAFF", INSTITUTESTAFF);
//                }
//                if (mJsonObject.has("INSTITUTESCREENING")) {
//                    JSONArray INSTITUTESCREENING = mJsonObject.getJSONArray("INSTITUTESCREENING");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "INSTITUTESCREENING", INSTITUTESCREENING);
//                }
//                if (mJsonObject.has("INSTITUTESCREENINGDETAILS")) {
//                    JSONArray INSTITUTESCREENINGDETAILS = mJsonObject.getJSONArray("INSTITUTESCREENINGDETAILS");
//                    SQLiteDatabase db4 = dbHelper.getWritableDatabase();
//                    String Query14 = "Select DISTINCT institutescreeningid , localinstitutescreeningid from INSTITUTESCREENING";
//                    Cursor c14 = db4.rawQuery(Query14, null);
//                    List<String> institutescreeningid = new ArrayList<>();
//                    List<String> localinstitutescreeningid = new ArrayList<>();
//                    while (c14.moveToNext()) {
//                        institutescreeningid.add(c14.getString(0));
//                        localinstitutescreeningid.add(c14.getString(1));
//                    }
//                    c14.close();
//                    db4.close();
//                    for (int i = 0; i < INSTITUTESCREENINGDETAILS.length(); i++) {
//                        JSONObject j = INSTITUTESCREENINGDETAILS.getJSONObject(i);
//                        String insplanID = j.getString("InstituteScreeningID");
//                        if (institutescreeningid.contains(insplanID.trim())) {
//                            j.put("LocalInstituteScreeningID", localinstitutescreeningid.get(institutescreeningid.indexOf(insplanID)));
//                            INSTITUTESCREENINGDETAILS.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "INSTITUTESCREENINGDETAILS", INSTITUTESCREENINGDETAILS);
//                }
//
//
//                if (mJsonObject.has("CONTACTDETAILS")) {
//
//                    JSONArray CONTACTDETAILS = mJsonObject.getJSONArray("CONTACTDETAILS");
//                    for (int i = 0; i < CONTACTDETAILS.length(); i++) {
//                        JSONObject j = CONTACTDETAILS.getJSONObject(i);
//                        String insplanID = j.getString("ContactID");
//                        if (contactID.contains(insplanID)) {
//                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(insplanID)));
//                            CONTACTDETAILS.put(i, j);
//                        }
//
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CONTACTDETAILS", CONTACTDETAILS);
//                }
//                if (mJsonObject.has("DEVICESETTINGS")) {
//                    JSONArray DEVICESETTINGS = mJsonObject.getJSONArray("DEVICESETTINGS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "DEVICESETTINGS", DEVICESETTINGS);
//                }
//                if (mJsonObject.has("FACILITIES")) {
//                    JSONArray FACILITIES = mJsonObject.getJSONArray("FACILITIES");
//
//                    for (int i = 0; i < FACILITIES.length(); i++) {
//                        JSONObject j = FACILITIES.getJSONObject(i);
//                        String insplanID = j.getString("AddressID");
//                        if (addressID.contains(insplanID)) {
//                            j.put("LocalAddressID", LocalAddressID.get(addressID.indexOf(insplanID)));
//                            FACILITIES.put(i, j);
//                        }
//                        String contID = j.getString("ContactID");
//                        if (contactID.contains(contID)) {
//                            j.put("LocalContactID", LocalContactID.get(contactID.indexOf(contID)));
//                            FACILITIES.put(i, j);
//                        }
//                    }
//
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "FACILITIES", FACILITIES);
//                }
//                if (mJsonObject.has("FACILITYCOVERAGE")) {
//                    JSONArray FACILITYCOVERAGE = mJsonObject.getJSONArray("FACILITYCOVERAGE");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "FACILITYCOVERAGE", FACILITYCOVERAGE);
//                }
//                if (mJsonObject.has("HABITATS")) {
//                    JSONArray HABITATS = mJsonObject.getJSONArray("HABITATS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "HABITATS", HABITATS);
//                }
//                if (mJsonObject.has("MHTSTAFF")) {
//                    JSONArray MHTSTAFF = mJsonObject.getJSONArray("MHTSTAFF");
//
//                    for (int i = 0; i < MHTSTAFF.length(); i++) {
//                        JSONObject j = MHTSTAFF.getJSONObject(i);
//                        String insplanID = j.getString("UserID");
//                        if (userID.contains(insplanID)) {
//                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(insplanID)));
//                            MHTSTAFF.put(i, j);
//                        }
//
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "MHTSTAFF", MHTSTAFF);
//                }
//                if (mJsonObject.has("MOBILEHEALTHTEAMS")) {
//                    JSONArray MOBILEHEALTHTEAMS = mJsonObject.getJSONArray("MOBILEHEALTHTEAMS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "MOBILEHEALTHTEAMS", MOBILEHEALTHTEAMS);
//                }
//                if (mJsonObject.has("USERCREDENTIALS")) {
//                    JSONArray USERCREDENTIALS = mJsonObject.getJSONArray("USERCREDENTIALS");
//
//                    for (int i = 0; i < USERCREDENTIALS.length(); i++) {
//                        JSONObject j = USERCREDENTIALS.getJSONObject(i);
//                        String insplanID = j.getString("UserID");
//                        if (userID.contains(insplanID)) {
//                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(insplanID)));
//                            USERCREDENTIALS.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "USERCREDENTIALS", USERCREDENTIALS);
//                }
//                if (mJsonObject.has("VILLAGES")) {
//                    JSONArray VILLAGES = mJsonObject.getJSONArray("VILLAGES");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "VILLAGES", VILLAGES);
//                }

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
                DBHelper dbHelper = new DBHelper(BaseActivity.this);
                strResponse = strResponse.replace("\\", "");
                strResponse = strResponse.substring(1, strResponse.length() - 1);
                JSONObject mJsonObject = new JSONObject(strResponse);
//                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
//                String Query3 = "Select DISTINCT InstituteID , LocalInstituteID from Institutes";
//                Cursor c3 = db2.rawQuery(Query3, null);
//                List<String> InstituteID = new ArrayList<>();
//                List<String> LocalInstituteID = new ArrayList<>();
//                while (c3.moveToNext()) {
//                    InstituteID.add(c3.getString(0));
//                    LocalInstituteID.add(c3.getString(1));
//                }
//                c3.close();
//
//                String Query6 = "Select DISTINCT userID, LocalUserID from Users";
//                Cursor c6 = db2.rawQuery(Query6, null);
//                List<String> userID = new ArrayList<>();
//                List<String> LocalUserID = new ArrayList<>();
//                while (c6.moveToNext()) {
//                    userID.add(c6.getString(0));
//                    LocalUserID.add(c6.getString(1));
//                }
//                c6.close();
//                db2.close();
//
//
//                if (mJsonObject.has("CHILDREN")) {
//                    JSONArray CHILDREN = mJsonObject.getJSONArray("CHILDREN");
//
//                    for (int i = 0; i < CHILDREN.length(); i++) {
//                        JSONObject j = CHILDREN.getJSONObject(i);
//                        String insplanID = j.getString("InstituteID");
//                        if (InstituteID.contains(insplanID.trim())) {
//                            j.put("LocalInstituteID", LocalInstituteID.get(InstituteID.indexOf(insplanID)));
//                            CHILDREN.put(i, j);
//                        }
//                        String contID = j.getString("UserID");
//                        if (userID.contains(contID.trim())) {
//                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(contID)));
//                            CHILDREN.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDREN", CHILDREN);
//                }
//                SQLiteDatabase db8 = dbHelper.getWritableDatabase();
//                String Query18 = "Select DISTINCT childrenID , LocalChildrenID from Children";
//                Cursor c18 = db8.rawQuery(Query18, null);
//                List<String> childrenID = new ArrayList<>();
//                List<String> LocalChildrenID = new ArrayList<>();
//                while (c18.moveToNext()) {
//                    childrenID.add(c18.getString(0));
//                    LocalChildrenID.add(c18.getString(1));
//                }
//                c18.close();
//                db8.close();
//                if (mJsonObject.has("CHILDRENSCREENING")) {
//                    JSONArray CHILDRENSCREENING = mJsonObject.getJSONArray("CHILDRENSCREENING");
//
//                    for (int i = 0; i < CHILDRENSCREENING.length(); i++) {
//                        JSONObject j = CHILDRENSCREENING.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENSCREENING.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENING", CHILDRENSCREENING);
//                }
//                SQLiteDatabase db9 = dbHelper.getWritableDatabase();
//                String Query19 = "Select DISTINCT childrenscreeningID , LocalChildrenScreeningID from ChildrenScreening";
//                Cursor c19 = db9.rawQuery(Query19, null);
//                List<String> childrenscreeningID = new ArrayList<>();
//                List<String> LocalChildrenScreeningID = new ArrayList<>();
//                while (c19.moveToNext()) {
//                    childrenscreeningID.add(c19.getString(0));
//                    LocalChildrenScreeningID.add(c19.getString(1));
//                }
//                c19.close();
//                db9.close();
//                if (mJsonObject.has("CHILDRENSCREENINGREFERRALS")) {
//                    JSONArray CHILDRENSCREENINGREFERRALS = mJsonObject.getJSONArray("CHILDRENSCREENINGREFERRALS");
//
//                    for (int i = 0; i < CHILDRENSCREENINGREFERRALS.length(); i++) {
//                        JSONObject j = CHILDRENSCREENINGREFERRALS.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenScreeningID");
//                        if (childrenscreeningID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
//                            CHILDRENSCREENINGREFERRALS.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGREFERRALS", CHILDRENSCREENINGREFERRALS);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGINVESTIGATIONS")) {
//                    JSONArray CHILDRENSCREENINGINVESTIGATIONS = mJsonObject.getJSONArray("CHILDRENSCREENINGINVESTIGATIONS");
//                    SQLiteDatabase db90 = dbHelper.getWritableDatabase();
//                    String Query190 = "Select DISTINCT ChildrenScreeningReferralID , LocalChildrenScreeningReferralID from CHILDRENSCREENINGREFERRALS";
//                    Cursor c190 = db90.rawQuery(Query190, null);
//                    List<String> ChildrenScreeningReferralID = new ArrayList<>();
//                    List<String> LocalChildrenScreeningReferralID = new ArrayList<>();
//                    while (c190.moveToNext()) {
//                        ChildrenScreeningReferralID.add(c190.getString(0));
//                        LocalChildrenScreeningReferralID.add(c190.getString(1));
//                    }
//                    c190.close();
//                    db90.close();
//                    for (int i = 0; i < CHILDRENSCREENINGINVESTIGATIONS.length(); i++) {
//                        JSONObject j = CHILDRENSCREENINGINVESTIGATIONS.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenScreeningReferralID");
//                        if (ChildrenScreeningReferralID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenScreeningReferralID", LocalChildrenScreeningReferralID.get(ChildrenScreeningReferralID.indexOf(insplanID)));
//                            CHILDRENSCREENINGINVESTIGATIONS.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGINVESTIGATIONS", CHILDRENSCREENINGINVESTIGATIONS);
//                }
//                if (mJsonObject.has("CHILDRENPICTURES")) {
//                    JSONArray CHILDRENPICTURES = mJsonObject.getJSONArray("CHILDRENPICTURES");
//
//                    for (int i = 0; i < CHILDRENPICTURES.length(); i++) {
//                        JSONObject j = CHILDRENPICTURES.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENPICTURES.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENPICTURES", CHILDRENPICTURES);
//                }
//
//                if (mJsonObject.has("CHILDRENSCREENINGALLERGIES")) {
//                    JSONArray CHILDRENSCREENINGALLERGIES = mJsonObject.getJSONArray("CHILDRENSCREENINGALLERGIES");
//
//                    for (int i = 0; i < CHILDRENSCREENINGALLERGIES.length(); i++) {
//                        JSONObject j = CHILDRENSCREENINGALLERGIES.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenScreeningID");
//                        if (childrenscreeningID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
//                            CHILDRENSCREENINGALLERGIES.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGALLERGIES", CHILDRENSCREENINGALLERGIES);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGHISTORY")) {
//                    JSONArray CHILDRENSCREENINGHISTORY = mJsonObject.getJSONArray("CHILDRENSCREENINGHISTORY");
//
//                    for (int i = 0; i < CHILDRENSCREENINGHISTORY.length(); i++) {
//                        JSONObject j = CHILDRENSCREENINGHISTORY.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENSCREENINGHISTORY.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGHISTORY", CHILDRENSCREENINGHISTORY);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGLOCALTREATMENT")) {
//                    JSONArray CHILDRENSCREENINGLOCALTREATMENT = mJsonObject.getJSONArray("CHILDRENSCREENINGLOCALTREATMENT");
//
//                    for (int i = 0; i < CHILDRENSCREENINGLOCALTREATMENT.length(); i++) {
//                        JSONObject j = CHILDRENSCREENINGLOCALTREATMENT.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenScreeningID");
//                        if (childrenscreeningID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
//                            CHILDRENSCREENINGLOCALTREATMENT.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGLOCALTREATMENT", CHILDRENSCREENINGLOCALTREATMENT);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGPE")) {
//                    JSONArray CHILDRENSCREENINGPE = mJsonObject.getJSONArray("CHILDRENSCREENINGPE");
//
//                    for (int i = 0; i < CHILDRENSCREENINGPE.length(); i++) {
//                        JSONObject j = CHILDRENSCREENINGPE.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenScreeningID");
//                        if (childrenscreeningID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
//                            CHILDRENSCREENINGPE.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGPE", CHILDRENSCREENINGPE);
//                }
//                if (mJsonObject.has("CHILDRENFAMILYHISTORY")) {
//                    JSONArray CHILDRENFAMILYHISTORY = mJsonObject.getJSONArray("CHILDRENFAMILYHISTORY");
//
//                    for (int i = 0; i < CHILDRENFAMILYHISTORY.length(); i++) {
//                        JSONObject j = CHILDRENFAMILYHISTORY.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENFAMILYHISTORY.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENFAMILYHISTORY", CHILDRENFAMILYHISTORY);
//                }
//                if (mJsonObject.has("CHILDRENMEDICALHISTORY")) {
//                    JSONArray CHILDRENMEDICALHISTORY = mJsonObject.getJSONArray("CHILDRENMEDICALHISTORY");
//
//                    for (int i = 0; i < CHILDRENMEDICALHISTORY.length(); i++) {
//                        JSONObject j = CHILDRENMEDICALHISTORY.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENMEDICALHISTORY.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENMEDICALHISTORY", CHILDRENMEDICALHISTORY);
//                }
//                if (mJsonObject.has("CHILDRENPARENTS")) {
//                    JSONArray CHILDRENPARENTS = mJsonObject.getJSONArray("CHILDRENPARENTS");
//
//                    for (int i = 0; i < CHILDRENPARENTS.length(); i++) {
//                        JSONObject j = CHILDRENPARENTS.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENPARENTS.put(i, j);
//                        }
//                        String contID = j.getString("UserID");
//                        if (userID.contains(contID.trim())) {
//                            j.put("LocalUserID", LocalUserID.get(userID.indexOf(contID)));
//                            CHILDRENPARENTS.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENPARENTS", CHILDRENPARENTS);
//                }
//                if (mJsonObject.has("CHILDRENALLERGIESHISTORY")) {
//                    JSONArray CHILDRENALLERGIESHISTORY = mJsonObject.getJSONArray("CHILDRENALLERGIESHISTORY");
//
//                    for (int i = 0; i < CHILDRENALLERGIESHISTORY.length(); i++) {
//                        JSONObject j = CHILDRENALLERGIESHISTORY.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENALLERGIESHISTORY.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENALLERGIESHISTORY", CHILDRENALLERGIESHISTORY);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGPICTURES")) {
//                    JSONArray CHILDRENSCREENINGPICTURES = mJsonObject.getJSONArray("CHILDRENSCREENINGPICTURES");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGPICTURES", CHILDRENSCREENINGPICTURES);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGRECOMMENDATIONS")) {
//                    JSONArray CHILDRENSCREENINGRECOMMENDATIONS = mJsonObject.getJSONArray("CHILDRENSCREENINGRECOMMENDATIONS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGRECOMMENDATIONS", CHILDRENSCREENINGRECOMMENDATIONS);
//                }
//
//                if (mJsonObject.has("CHILDRENDISABILITIES")) {
//                    JSONArray CHILDRENDISABILITIES = mJsonObject.getJSONArray("CHILDRENDISABILITIES");
//
//                    for (int i = 0; i < CHILDRENDISABILITIES.length(); i++) {
//                        JSONObject j = CHILDRENDISABILITIES.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenID");
//                        if (childrenID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenID", LocalChildrenID.get(childrenID.indexOf(insplanID)));
//                            CHILDRENDISABILITIES.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENDISABILITIES", CHILDRENDISABILITIES);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGSURGICALS")) {
//                    JSONArray CHILDRENSCREENINGSURGICALS = mJsonObject.getJSONArray("CHILDRENSCREENINGSURGICALS");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGSURGICALS", CHILDRENSCREENINGSURGICALS);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGVITALS")) {
//                    JSONArray CHILDRENSCREENINGVITALS = mJsonObject.getJSONArray("CHILDRENSCREENINGVITALS");
//
//                    for (int i = 0; i < CHILDRENSCREENINGVITALS.length(); i++) {
//                        JSONObject j = CHILDRENSCREENINGVITALS.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenScreeningID");
//                        if (childrenscreeningID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
//                            CHILDRENSCREENINGVITALS.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGVITALS", CHILDRENSCREENINGVITALS);
//                }
//                if (mJsonObject.has("CHILDRENSCREENINGSTATUSES")) {
//                    JSONArray CHILDRENSCREENINGSTATUSES = mJsonObject.getJSONArray("CHILDRENSCREENINGSTATUSES");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSCREENINGSTATUSES", CHILDRENSCREENINGSTATUSES);
//                }
//                if (mJsonObject.has("CHILDRENSURGICALHISTORY")) {
//                    JSONArray CHILDRENSURGICALHISTORY = mJsonObject.getJSONArray("CHILDRENSURGICALHISTORY");
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDRENSURGICALHISTORY", CHILDRENSURGICALHISTORY);
//                }
//                if (mJsonObject.has("CHILDSCREENINGFH")) {
//                    JSONArray CHILDSCREENINGFH = mJsonObject.getJSONArray("CHILDSCREENINGFH");
//
//                    for (int i = 0; i < CHILDSCREENINGFH.length(); i++) {
//                        JSONObject j = CHILDSCREENINGFH.getJSONObject(i);
//                        String insplanID = j.getString("ChildrenScreeningID");
//                        if (childrenscreeningID.contains(insplanID.trim())) {
//                            j.put("LocalChildrenScreeningID", LocalChildrenScreeningID.get(childrenscreeningID.indexOf(insplanID)));
//                            CHILDSCREENINGFH.put(i, j);
//                        }
//                    }
//                    dbHelper.bulkinsertintoTable(BaseActivity.this, "CHILDSCREENINGFH", CHILDSCREENINGFH);
//                }
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
        if (Helper.syncDate1.length() == 0) {
            SharedPreferences sharedpreferences = getSharedPreferences(
                    UserLoginActivity.UserLogin, Context.MODE_PRIVATE);
            Helper.syncDate1 = sharedpreferences.getString("SyncDate1", "");
        }

    }

    public class WebConn1 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {


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
                    Helper.showShortToast(BaseActivity.this, "There is a problem in sync process so please try again...");
                } else if (response.trim().equalsIgnoreCase("200")) {
                    if (navIndex == 0) {
                        navIndex = 1;
                        SharedPreferences sharedpreferences = getSharedPreferences(
                                "LoginMain", Context.MODE_PRIVATE);
                        String TokenID = sharedpreferences.getString("DeviceCode", "");
                        new WebConn1().execute(UrlUtils.URL_INTITAL_SETUP + TokenID + "/" + Helper.syncDate1 + "/2");
                    } else {
                        if (Helper.progressDialog != null && Helper.progressDialog.isShowing())
                            Helper.progressDialog.dismiss();
                        Helper.syncDate = syncDate;
                        SharedPreferences sharedpreferences = getSharedPreferences(
                                UserLoginActivity.UserLogin, Context.MODE_PRIVATE);

                        sharedpreferences.edit().putString("SyncDate", Helper.syncDate).commit();
                        Helper.showShortToast(BaseActivity.this, "Sync Process Completed");
                    }

                } else {
                    if (Helper.progressDialog != null && Helper.progressDialog.isShowing())
                        Helper.progressDialog.dismiss();
                    Helper.showShortToast(BaseActivity.this, response);
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    public String updateSyncTables(String response) {
        String str = "";
        try {
            response = response.replace("\\", "");
            response = response.substring(1, response.length() - 1);
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("Status");
            if (status.trim().equalsIgnoreCase("success")) {
                syncDate = jsonObject.getString("SyncDate");
                DBHelper dbh = new DBHelper(BaseActivity.this);
                dbh.updateTableROWS(BaseActivity.this, "InstitutePlans", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "InstitutePlanDetails", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "Institutes", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "InstituteScreening", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "InstituteScreeningDetails", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreening", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "Children", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningvitals", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningReferrals", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningInvestigations", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningSurgicals", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningRecommendations", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningPE", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningLocalTreatment", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenScreeningAllergies", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildScreeningFH", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenDisabilities", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenParents", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenAllergiesHistory", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenFamilyHistory", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenMedicalHistory", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});
                dbh.updateTableROWS(BaseActivity.this, "ChildrenSurgicalsHistory", new String[]{"PushStatus", "LastCommitedDate"},
                        new String[]{"1", syncDate}, new String[]{"PushStatus", "LastCommitedDate"}, new String[]{"0", Helper.syncDate});

                str = "200";
            } else if (status.trim().equalsIgnoreCase("fail")) {
                str = jsonObject.getString("Message");
            } else {
                str = response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            str = "";
        }
        return str;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);


        }
    }

    public JSONObject syncData() {
        DBHelper dbh = new DBHelper(this);
        JSONObject ret_json = new JSONObject();
        try {
            SharedPreferences sharedpreferences = getSharedPreferences(
                    "LoginMain", Context.MODE_PRIVATE);
            SharedPreferences sharedpreferences1 = getSharedPreferences(
                    "UserLogin", Context.MODE_PRIVATE);
            String TokenID = sharedpreferences.getString("DeviceCode", "");
            String userID = sharedpreferences1.getString("LoginUserID", "");
            ret_json.put("devicecode", TokenID);
            SQLiteDatabase db = dbh.getReadableDatabase();

            JSONArray InstitutePlan = new JSONArray();
            String Query = "Select * from InstitutePlans where IsDeleted = 0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c = db.rawQuery(Query, null);
            ArrayList<String> LocalInstitutePlanID = new ArrayList<>();
            ArrayList<String> LocalInstituteID = new ArrayList<>();
            while (c.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c.isNull(c.getColumnIndex("LocalInstitutePlanID")))
                    j.put("LocalInstitutePlanID", "");
                else
                    j.put("LocalInstitutePlanID", c.getString(c.getColumnIndex("LocalInstitutePlanID")));
                if (c.isNull(c.getColumnIndex("InstitutePlanID")))
                    j.put("InstitutePlanID", "");
                else
                    j.put("InstitutePlanID", c.getString(c.getColumnIndex("InstitutePlanID")));

                if (c.isNull(c.getColumnIndex("InstituteID")))
                    j.put("InstituteID", "");
                else
                    j.put("InstituteID", c.getString(c.getColumnIndex("InstituteID")));

                if (c.isNull(c.getColumnIndex("RBSKCalendarYearID")))
                    j.put("RBSKCalendarYearID", "");
                else
                    j.put("RBSKCalendarYearID", c.getString(c.getColumnIndex("RBSKCalendarYearID")));

                if (c.isNull(c.getColumnIndex("ScreeningRoundId")))
                    j.put("ScreeningRoundID", "");
                else
                    j.put("ScreeningRoundID", c.getString(c.getColumnIndex("ScreeningRoundId")));

                if (c.isNull(c.getColumnIndex("InstitutePlanStatusID")))
                    j.put("InstitutePlanStatusID", "");
                else
                    j.put("InstitutePlanStatusID", c.getString(c.getColumnIndex("InstitutePlanStatusID")));
                j.put("UserID", userID);
                if (c.isNull(c.getColumnIndex("LastCommitedDate")))
                    j.put("LastCommitedDateTime", "");
                else
                    j.put("LastCommitedDateTime", c.getString(c.getColumnIndex("LastCommitedDate")));
                if (!c.isNull(c.getColumnIndex("LocalInstitutePlanID")))
                    LocalInstitutePlanID.add(c.getString(c.getColumnIndex("LocalInstitutePlanID")));

                if (!c.isNull(c.getColumnIndex("InstituteID")))
                    LocalInstituteID.add(c.getString(c.getColumnIndex("InstituteID")));
                InstitutePlan.put(j);

            }
            c.close();
            ret_json.put("InstitutePlan", InstitutePlan);
            JSONArray InstitutePlanDetails = new JSONArray();
            if (!LocalInstitutePlanID.isEmpty()) {
                String str = "";
                for (int i = 0; i < LocalInstitutePlanID.size(); i++) {
                    str += LocalInstitutePlanID.get(i) + ",";
                }
                if (str.length() > 0)
                    str = str.substring(0, str.lastIndexOf(','));
                String Query1 = "Select * from InstitutePlanDetails where LocalInstitutePlanID in (" + str + ") and isDeleted = 0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
                Cursor c1 = db.rawQuery(Query1, null);
                while (c1.moveToNext()) {
                    JSONObject j = new JSONObject();
                    if (c1.isNull(c1.getColumnIndex("LocalInstitutePlanDetailID")))
                        j.put("LocalInstitutePlanDetailID", "");
                    else
                        j.put("LocalInstitutePlanDetailID", c1.getString(c1.getColumnIndex("LocalInstitutePlanDetailID")));

                    if (c1.isNull(c1.getColumnIndex("InstitutePlanDetailID")))
                        j.put("InstitutePlanDetailID", "");
                    else
                        j.put("InstitutePlanDetailID", c1.getString(c1.getColumnIndex("InstitutePlanDetailID")));

                    if (c1.isNull(c1.getColumnIndex("LocalInstitutePlanID")))
                        j.put("LocalInstitutePlanID", "");
                    else
                        j.put("LocalInstitutePlanID", c1.getString(c1.getColumnIndex("LocalInstitutePlanID")));

                    if (c1.isNull(c1.getColumnIndex("ScheduledDate")))
                        j.put("ScheduledDate", "");
                    else
                        j.put("ScheduledDate", c1.getString(c1.getColumnIndex("ScheduledDate")));

                    if (c1.isNull(c1.getColumnIndex("PlannedCount")))
                        j.put("PlannedCount", "");
                    else
                        j.put("PlannedCount", c1.getString(c1.getColumnIndex("PlannedCount")));

                    if (c1.isNull(c1.getColumnIndex("InstitutePlanSkipReasonID")))
                        j.put("InstitutePlanSkipReasonID", "");
                    else
                        j.put("InstitutePlanSkipReasonID", c1.getString(c1.getColumnIndex("InstitutePlanSkipReasonID")));
                    if (c1.isNull(c1.getColumnIndex("SkipComments")))
                        j.put("SkipComments", "");
                    else
                        j.put("SkipComments", c1.getString(c1.getColumnIndex("SkipComments")));

                    j.put("MobileHealthTeamID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "MHTStaff", "MobileHealthTeamID", "UserID", userID));
                    j.put("UserID", userID);
                    if (c1.isNull(c1.getColumnIndex("LastCommitedDate")))
                        j.put("LastCommitedDateTime", "");
                    else
                        j.put("LastCommitedDateTime", c1.getString(c1.getColumnIndex("LastCommitedDate")));
                    InstitutePlanDetails.put(j);
                }
                c1.close();
            }
            ret_json.put("InstitutePlanDetails", InstitutePlanDetails);


            JSONArray Institute = new JSONArray();
            if (!LocalInstituteID.isEmpty()) {
                String str = "";
                for (int i = 0; i < LocalInstituteID.size(); i++) {
                    str += LocalInstituteID.get(i) + ",";
                }
                if (str.length() > 0)
                    str = str.substring(0, str.lastIndexOf(','));
                String Query2 = "Select * from Institutes where InstituteID in(" + str + ") and isDeleted =0 and PushStatus = 0 ";
                Cursor c2 = db.rawQuery(Query2, null);
                while (c2.moveToNext()) {
                    JSONObject j = new JSONObject();
                    if (c2.isNull(c2.getColumnIndex("InstituteID")))
                        j.put("InstituteID", "");
                    else
                        j.put("InstituteID", c2.getString(c2.getColumnIndex("InstituteID")));
                    if (c2.isNull(c2.getColumnIndex("LocalInstituteID")))
                        j.put("LocalInstituteID", "");
                    else
                        j.put("LocalInstituteID", c2.getString(c2.getColumnIndex("LocalInstituteID")));
                    if (c2.isNull(c2.getColumnIndex("InstituteName")))
                        j.put("InstituteName", "");
                    else
                        j.put("InstituteName", c2.getString(c2.getColumnIndex("InstituteName")));
                    if (c2.isNull(c2.getColumnIndex("DISECode")))
                        j.put("DISECode", "");
                    else
                        j.put("DISECode", c2.getString(c2.getColumnIndex("DISECode")));

                    j.put("UserID", userID);
                    if (c2.isNull(c2.getColumnIndex("LastCommitedDate")))
                        j.put("LastCommitedDateTime", "");
                    else
                        j.put("LastCommitedDateTime", c2.getString(c2.getColumnIndex("LastCommitedDate")));

                    Institute.put(j);

                }
                c2.close();
            }

            ret_json.put("Institute", Institute);
            JSONArray InstituteScreening = new JSONArray();
            ArrayList<String> LocalInstituteScreeningID = new ArrayList<>();
            if (!LocalInstituteID.isEmpty()) {
                String str = "";
                for (int i = 0; i < LocalInstituteID.size(); i++) {
                    str += LocalInstituteID.get(i) + ",";
                }
                if (str.length() > 0)
                    str = str.substring(0, str.lastIndexOf(','));
                String Query2 = "Select * from InstituteScreening where InstituteID in(" + str + ") and isDeleted =0 and PushStatus = 0 ";
                Cursor c2 = db.rawQuery(Query2, null);
                while (c2.moveToNext()) {
                    JSONObject j = new JSONObject();
                    if (c2.isNull(c2.getColumnIndex("LocalInstituteScreeningID"))) {
                        j.put("LocalInstituteScreeningID", "");
                    } else {
                        LocalInstituteScreeningID.add(c2.getString(c2.getColumnIndex("LocalInstituteScreeningID")));
                        j.put("LocalInstituteScreeningID", c2.getString(c2.getColumnIndex("LocalInstituteScreeningID")));
                    }
                    if (c2.isNull(c2.getColumnIndex("InstituteScreeningID"))) {
                        j.put("InstituteScreeningID", "");
                    } else {
                        j.put("InstituteScreeningID", c2.getString(c2.getColumnIndex("InstituteScreeningID")));
                    }
                    if (c2.isNull(c2.getColumnIndex("InstituteID"))) {
                        j.put("InstituteID", "");
                    } else {
                        j.put("InstituteID", c2.getString(c2.getColumnIndex("InstituteID")));
                    }

                    if (c2.isNull(c2.getColumnIndex("InstituteScreeningStatusID"))) {
                        j.put("InstituteScreeningStatusID", "");
                    } else {
                        j.put("InstituteScreeningStatusID", c2.getString(c2.getColumnIndex("InstituteScreeningStatusID")));
                    }

                    j.put("UserID", userID);
                    if (c2.isNull(c2.getColumnIndex("LastCommitedDate"))) {
                        j.put("LastCommitedDateTime", "");
                    } else {
                        j.put("LastCommitedDateTime", c2.getString(c2.getColumnIndex("LastCommitedDate")));
                    }

                    InstituteScreening.put(j);

                }
                c2.close();
            }
            ret_json.put("InstituteScreening", InstituteScreening);

            JSONArray InstituteScreeningDetails = new JSONArray();
            if (!LocalInstituteScreeningID.isEmpty()) {
                String str = "";
                for (int i = 0; i < LocalInstituteScreeningID.size(); i++) {
                    str += LocalInstituteScreeningID.get(i) + ",";
                }
                if (str.length() > 0)
                    str = str.substring(0, str.lastIndexOf(','));
                String Query2 = "Select * from InstituteScreeningDetails where LocalInstituteScreeningID in(" + str + ") and isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
                Cursor c2 = db.rawQuery(Query2, null);
                while (c2.moveToNext()) {
                    JSONObject j = new JSONObject();
                    if (c2.isNull(c2.getColumnIndex("LocalInstituteScreeningDetailID"))) {
                        j.put("LocalInstituteScreeningDetailID", "");
                    } else {
                        j.put("LocalInstituteScreeningDetailID", c2.getString(c2.getColumnIndex("LocalInstituteScreeningDetailID")));
                    }

                    if (c2.isNull(c2.getColumnIndex("InstituteScreeningDetailID"))) {
                        j.put("InstituteScreeningDetailID", "");
                    } else {
                        j.put("InstituteScreeningDetailID", c2.getString(c2.getColumnIndex("InstituteScreeningDetailID")));
                    }

                    if (c2.isNull(c2.getColumnIndex("LocalInstituteScreeningID"))) {
                        j.put("LocalInstituteScreeningID", "");
                    } else {
                        j.put("LocalInstituteScreeningID", c2.getString(c2.getColumnIndex("LocalInstituteScreeningID")));
                    }

                    if (c2.isNull(c2.getColumnIndex("ScreenStatusID"))) {
                        j.put("ScreeningStatusID", "");
                    } else {
                        j.put("ScreeningStatusID", c2.getString(c2.getColumnIndex("ScreenStatusID")));
                    }

                    if (c2.isNull(c2.getColumnIndex("ScreeningStartDateTime"))) {
                        j.put("ScreeningStartDateTime", "");
                    } else {
                        j.put("ScreeningStartDateTime", c2.getString(c2.getColumnIndex("ScreeningStartDateTime")));
                    }
                    if (c2.isNull(c2.getColumnIndex("ScreeningEndDateTime"))) {
                        j.put("ScreeningEndDateTime", "");
                    } else {
                        j.put("ScreeningEndDateTime", c2.getString(c2.getColumnIndex("ScreeningEndDateTime")));
                    }
                    j.put("UserID", userID);
                    if (c2.isNull(c2.getColumnIndex("LastCommitedDate"))) {
                        j.put("LastCommitedDateTime", "");
                    } else {
                        j.put("LastCommitedDateTime", c2.getString(c2.getColumnIndex("LastCommitedDate")));
                    }
                    InstituteScreeningDetails.put(j);

                }
                c2.close();
            }
            ret_json.put("InstituteScreeningDetails", InstituteScreeningDetails);


            String Query4 = "Select * from ChildrenScreening where isDeleted =0 and PushStatus =0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c4 = db.rawQuery(Query4, null);
            JSONArray ChildrenScreening = new JSONArray();
            ArrayList<String> LocalChildrenID = new ArrayList<>();
            while (c4.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c4.isNull(c4.getColumnIndex("ChildrenScreeningID"))) {
                    j.put("ChildrenScreeningID", "");
                } else {
                    j.put("ChildrenScreeningID", c4.getString(c4.getColumnIndex("ChildrenScreeningID")));
                }

                if (c4.isNull(c4.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c4.getString(c4.getColumnIndex("LocalChildrenScreeningID")));
                }
                if (c4.isNull(c4.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    LocalChildrenID.add(c4.getString(c4.getColumnIndex("LocalChildrenID")));
                    j.put("LocalChildrenID", c4.getString(c4.getColumnIndex("LocalChildrenID")));
                }

                j.put("ChildrenID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "Children", "ChildrenID", "LocalChildrenID", c4.getString(c4.getColumnIndex("LocalChildrenID"))));

                if (c4.isNull(c4.getColumnIndex("ScreeningTemplateTypeID"))) {
                    j.put("ScreeningTemplateTypeID", "");
                } else {
                    j.put("ScreeningTemplateTypeID", c4.getString(c4.getColumnIndex("ScreeningTemplateTypeID")));
                }
                if (c4.isNull(c4.getColumnIndex("ScreeningStartDateTime"))) {
                    j.put("ScreeningStartDateTime", "");
                } else {
                    j.put("ScreeningStartDateTime", c4.getString(c4.getColumnIndex("ScreeningStartDateTime")));
                }
                if (c4.isNull(c4.getColumnIndex("ScreeningEndDateTime"))) {
                    j.put("ScreeningEndDateTime", "");
                } else {
                    j.put("ScreeningEndDateTime", c4.getString(c4.getColumnIndex("ScreeningEndDateTime")));
                }
                if (c4.isNull(c4.getColumnIndex("ChildrenScreenStatusID"))) {
                    j.put("ChildrenScreeingStatusID", "");
                } else {
                    j.put("ChildrenScreeingStatusID", c4.getString(c4.getColumnIndex("ChildrenScreenStatusID")));
                }
                j.put("UserID", userID);
                if (c4.isNull(c4.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c4.getString(c4.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreening.put(j);

            }
            c4.close();
            ret_json.put("ChildrenScreening", ChildrenScreening);
            JSONArray Children = new JSONArray();
//            if (!LocalChildrenID.isEmpty()) {
//                String str = "";
//                for (int i = 0; i < LocalChildrenID.size(); i++) {
//                    str += LocalChildrenID.get(i) + ",";
//                }
//                if (str.length() > 0)
//                    str = str.substring(0, str.lastIndexOf(','));
//                String Query3 = "Select * from Children where LocalChildrenID in (" + str + ") and isDeleted =0 and PushStatus = 0";
//                Cursor c3 = db.rawQuery(Query3, null);
//
//                while (c3.moveToNext()) {
//                    JSONObject j = new JSONObject();
//                    j.put("ChildrenID", c3.getString(c3.getColumnIndex("ChildrenID")));
//                    j.put("LocalChildrenID", c3.getString(c3.getColumnIndex("LocalChildrenID")));
//                    j.put("UserID", c3.getString(c3.getColumnIndex("LocalUserID")));
//                    j.put("MCTSID", c3.getString(c3.getColumnIndex("MCTSID")));
//                    j.put("IdentificationMark1", c3.getString(c3.getColumnIndex("IdentificationMark1")));
//                    j.put("IdentificationMark2", c3.getString(c3.getColumnIndex("IdentificationMark2")));
//                    j.put("HasDisability", c3.getString(c3.getColumnIndex("HasDisability")));
//                    j.put("ChildrenStatusID", c3.getString(c3.getColumnIndex("ChildrenStatusID")));
//                    j.put("AddressID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "Users", "AddressID", "LocalUserID", c3.getString(c3.getColumnIndex("LocalUserID"))));
//                    j.put("UserID", userID);
//                    j.put("LastCommitedDateTime", c3.getString(c3.getColumnIndex("LastCommitedDate")));
//                    Children.put(j);
//                }
//                c3.close();
//
//            }

            String Query3 = "Select * from Children where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c3 = db.rawQuery(Query3, null);

            while (c3.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c3.isNull(c3.getColumnIndex("ChildrenID"))) {
                    j.put("ChildrenID", "");
                } else {
                    j.put("ChildrenID", c3.getString(c3.getColumnIndex("ChildrenID")));
                }

                if (c3.isNull(c3.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    j.put("LocalChildrenID", c3.getString(c3.getColumnIndex("LocalChildrenID")));
                }
                if (c3.isNull(c3.getColumnIndex("LocalUserID"))) {
                    j.put("UserID", "");
                } else {
                    j.put("UserID", c3.getString(c3.getColumnIndex("LocalUserID")));
                }
                if (c3.isNull(c3.getColumnIndex("MCTSID"))) {
                    j.put("MCTSID", "");
                } else {
                    j.put("MCTSID", c3.getString(c3.getColumnIndex("MCTSID")));
                }
                if (c3.isNull(c3.getColumnIndex("IdentificationMark1"))) {
                    j.put("IdentificationMark1", "");
                } else {
                    j.put("IdentificationMark1", c3.getString(c3.getColumnIndex("IdentificationMark1")));
                }
                if (c3.isNull(c3.getColumnIndex("IdentificationMark2"))) {
                    j.put("IdentificationMark2", "");
                } else {
                    j.put("IdentificationMark2", c3.getString(c3.getColumnIndex("IdentificationMark2")));
                }
                if (c3.isNull(c3.getColumnIndex("HasDisability"))) {
                    j.put("HasDisability", "");
                } else {
                    j.put("HasDisability", c3.getString(c3.getColumnIndex("HasDisability")));
                }
                if (c3.isNull(c3.getColumnIndex("ChildrenStatusID"))) {
                    j.put("ChildrenStatusID", "");
                } else {
                    j.put("ChildrenStatusID", c3.getString(c3.getColumnIndex("ChildrenStatusID")));
                }
                if (c3.isNull(c3.getColumnIndex("AddressID"))) {
                    j.put("AddressID", "");
                } else {
                    j.put("AddressID", c3.getString(c3.getColumnIndex("AddressID")));
                }
                j.put("UserID", userID);
                if (c3.isNull(c3.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c3.getString(c3.getColumnIndex("LastCommitedDate")));
                }
                Children.put(j);
            }
            c3.close();
            ret_json.put("Children", Children);
//
            String Query5 = "Select * from ChildrenScreeningvitals where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c5 = db.rawQuery(Query5, null);
            JSONArray ChildrenScreeningVitals = new JSONArray();
            while (c5.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c5.isNull(c5.getColumnIndex("ChildrenScreeningVitalsID"))) {
                    j.put("ChildrenScreeningVitalsID", "");
                } else {
                    j.put("ChildrenScreeningVitalsID", c5.getString(c5.getColumnIndex("ChildrenScreeningVitalsID")));
                }
                if (c5.isNull(c5.getColumnIndex("LocalChildrenScreeningVitalsID"))) {
                    j.put("LocalChildrenScreeningVitalsID", "");
                } else {
                    j.put("LocalChildrenScreeningVitalsID", c5.getString(c5.getColumnIndex("LocalChildrenScreeningVitalsID")));
                }
                if (c5.isNull(c5.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c5.getString(c5.getColumnIndex("LocalChildrenScreeningID")));
                }

                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c5.getString(c5.getColumnIndex("LocalChildrenScreeningID"))));
                if (c5.isNull(c5.getColumnIndex("Height"))) {
                    j.put("Height", "");
                } else {
                    j.put("Height", c5.getString(c5.getColumnIndex("Height")));
                }
                if (c5.isNull(c5.getColumnIndex("HeightIndication"))) {
                    j.put("HeightIndication", "");
                } else {
                    j.put("HeightIndication", c5.getString(c5.getColumnIndex("HeightIndication")));
                }
                if (c5.isNull(c5.getColumnIndex("Weight"))) {
                    j.put("Weight", "");
                } else {
                    j.put("Weight", c5.getString(c5.getColumnIndex("Weight")));
                }
                if (c5.isNull(c5.getColumnIndex("WeightIndication"))) {
                    j.put("WeightIndication", "");
                } else {
                    j.put("WeightIndication", c5.getString(c5.getColumnIndex("WeightIndication")));
                }
                if (c5.isNull(c5.getColumnIndex("BMI"))) {
                    j.put("BMI", "");
                } else {
                    j.put("BMI", c5.getString(c5.getColumnIndex("BMI")));
                }
                if (c5.isNull(c5.getColumnIndex("BMIIndication"))) {
                    j.put("BMIIndication", "");
                } else {
                    j.put("BMIIndication", c5.getString(c5.getColumnIndex("BMIIndication")));
                }
                if (c5.isNull(c5.getColumnIndex("AcuityOfVisionLefteye"))) {
                    j.put("AcuityOfVisionLefteye", "");
                } else {
                    j.put("AcuityOfVisionLefteye", c5.getString(c5.getColumnIndex("AcuityOfVisionLefteye")));
                }
                if (c5.isNull(c5.getColumnIndex("AcuityOfVisionRighteye"))) {
                    j.put("AcuityOfVisionRighteye", "");
                } else {
                    j.put("AcuityOfVisionRighteye", c5.getString(c5.getColumnIndex("AcuityOfVisionRighteye")));
                }
                if (c5.isNull(c5.getColumnIndex("BP"))) {
                    j.put("BP", "");
                } else if (c5.getString(c5.getColumnIndex("BP")).trim().equalsIgnoreCase("null")) {
                    j.put("BP", "0");
                } else {
                    j.put("BP", c5.getString(c5.getColumnIndex("BP")));
                }
                if (c5.isNull(c5.getColumnIndex("BPIndication"))) {
                    j.put("BPIndication", "");
                } else {
                    j.put("BPIndication", c5.getString(c5.getColumnIndex("BPIndication")));
                }
                if (c5.isNull(c5.getColumnIndex("BloodGroupID"))) {
                    j.put("BloodGroupID", "");
                } else {
                    j.put("BloodGroupID", c5.getString(c5.getColumnIndex("BloodGroupID")));
                }
                if (c5.isNull(c5.getColumnIndex("BloodGroupNotes"))) {
                    j.put("BloodGroupNotes", "");
                } else {
                    j.put("BloodGroupNotes", c5.getString(c5.getColumnIndex("BloodGroupNotes")));
                }
                if (c5.isNull(c5.getColumnIndex("TemperatureID"))) {
                    j.put("TemperatureID", "");
                } else {
                    j.put("TemperatureID", c5.getString(c5.getColumnIndex("TemperatureID")));
                }

                if (c5.isNull(c5.getColumnIndex("TemperatureIndication"))) {
                    j.put("TemperatureIndication", "");
                } else {
                    j.put("TemperatureIndication", c5.getString(c5.getColumnIndex("TemperatureIndication")));
                }
                if (c5.isNull(c5.getColumnIndex("HemoGlobinID"))) {
                    j.put("HemoGlobinID", "");
                } else {
                    j.put("HemoGlobinID", c5.getString(c5.getColumnIndex("HemoGlobinID")));
                }
                if (c5.isNull(c5.getColumnIndex("HemoGlobinIndication"))) {
                    j.put("HemoGlobinIndication", "");
                } else {
                    j.put("HemoGlobinIndication", c5.getString(c5.getColumnIndex("HemoGlobinIndication")));
                }
                if (c5.isNull(c5.getColumnIndex("MUACInCms"))) {
                    j.put("MUACInCms", "");
                } else {
                    j.put("MUACInCms", c5.getString(c5.getColumnIndex("MUACInCms")));
                }
                if (c5.isNull(c5.getColumnIndex("MUACIndication"))) {
                    j.put("MUACIndication", "");
                } else {
                    j.put("MUACIndication", c5.getString(c5.getColumnIndex("MUACIndication")));
                }
                if (c5.isNull(c5.getColumnIndex("HeadCircumferenceInCms"))) {
                    j.put("HeadCircumferenceInCms", "");
                } else {
                    j.put("HeadCircumferenceInCms", c5.getString(c5.getColumnIndex("HeadCircumferenceInCms")));
                }
                if (c5.isNull(c5.getColumnIndex("HeadCircumferenceIndication"))) {
                    j.put("HeadCircumferenceIndication", "");
                } else {
                    j.put("HeadCircumferenceIndication", c5.getString(c5.getColumnIndex("HeadCircumferenceIndication")));
                }
                j.put("UserID", userID);
                if (c5.isNull(c5.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c5.getString(c5.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningVitals.put(j);

            }
            c5.close();
            ret_json.put("ChildrenScreeningVitals", ChildrenScreeningVitals);

            String Query6 = "Select * from ChildrenScreeningReferrals where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c6 = db.rawQuery(Query6, null);
            JSONArray ChildrenScreeningReferrals = new JSONArray();
            while (c6.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c6.isNull(c6.getColumnIndex("ChildrenScreeningReferralID"))) {
                    j.put("ChildrenScreeningReferralID", "");
                } else {
                    j.put("ChildrenScreeningReferralID", c6.getString(c6.getColumnIndex("ChildrenScreeningReferralID")));
                }
                if (c6.isNull(c6.getColumnIndex("LocalChildrenScreeningReferralID"))) {
                    j.put("LocalChildrenScreeningReferralID", "");
                } else {
                    j.put("LocalChildrenScreeningReferralID", c6.getString(c6.getColumnIndex("LocalChildrenScreeningReferralID")));
                }
                if (c6.isNull(c6.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c6.getString(c6.getColumnIndex("LocalChildrenScreeningID")));
                }
                if (c6.isNull(c6.getColumnIndex("ChildrenScreeningID"))) {
                    j.put("ChildrenScreeningID", "");
                } else {
                    j.put("ChildrenScreeningID", c6.getString(c6.getColumnIndex("ChildrenScreeningID")));
                }
                if (c6.isNull(c6.getColumnIndex("HealthConditonID"))) {
                    j.put("HealthConditionID", "");
                } else {
                    j.put("HealthConditionID", c6.getString(c6.getColumnIndex("HealthConditonID")));
                }

                if (c6.isNull(c6.getColumnIndex("WasReferred"))) {
                    j.put("WasReferred", "");
                } else {
                    j.put("WasReferred", c6.getString(c6.getColumnIndex("WasReferred")));
                }
                if (c6.isNull(c6.getColumnIndex("ReferredFacilityID"))) {
                    j.put("ReferredFacilityID", "");
                } else {
                    j.put("ReferredFacilityID", c6.getString(c6.getColumnIndex("ReferredFacilityID")));
                }
                // j.put("ReferralStatusID", c6.getString(c6.getColumnIndex("ReferralStatusID")));
                j.put("UserID", userID);
                if (c6.isNull(c6.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c6.getString(c6.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningReferrals.put(j);

            }
            c6.close();
            ret_json.put("ChildrenScreeningReferrals", ChildrenScreeningReferrals);

            String Query7 = "Select * from ChildrenScreeningInvestigations where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c7 = db.rawQuery(Query7, null);
            JSONArray ChildrenScreeningInvestigations = new JSONArray();
            while (c7.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c7.isNull(c7.getColumnIndex("ChildrenScreeningInvestigationID"))) {
                    j.put("ChildrenScreeningInvestigationID", "");
                } else {
                    j.put("ChildrenScreeningInvestigationID", c7.getString(c7.getColumnIndex("ChildrenScreeningInvestigationID")));
                }
                if (c7.isNull(c7.getColumnIndex("LocalChildrenScreeningInvestigationID"))) {
                    j.put("LocalChildrenScreeningInvestigationID", "");
                } else {
                    j.put("LocalChildrenScreeningInvestigationID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningInvestigationID")));
                }
                if (c7.isNull(c7.getColumnIndex("LocalChildrenScreeningReferralID"))) {
                    j.put("LocalChildrenScreeningReferralID", "");
                } else {
                    j.put("LocalChildrenScreeningReferralID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningReferralID")));
                }
                if (c7.isNull(c7.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningID")));
                }

                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningID"))));
                if (c7.isNull(c7.getColumnIndex("ChildrenScreeningReferralID"))) {
                    j.put("ChildrenScreeningReferralID", "");
                } else {
                    j.put("ChildrenScreeningReferralID", c7.getString(c7.getColumnIndex("ChildrenScreeningReferralID")));
                }
                if (c7.isNull(c7.getColumnIndex("LabInvestigationID"))) {
                    j.put("LabInvestigationID", "");
                } else {
                    j.put("LabInvestigationID", c7.getString(c7.getColumnIndex("LabInvestigationID")));
                }
                if (c7.isNull(c7.getColumnIndex("WasSuggested"))) {
                    j.put("WasSuggested", "");
                } else {
                    j.put("WasSuggested", c7.getString(c7.getColumnIndex("WasSuggested")));
                }
                if (c7.isNull(c7.getColumnIndex("Comments"))) {
                    j.put("Comments", "");
                } else {
                    j.put("Comments", c7.getString(c7.getColumnIndex("Comments")));
                }
                j.put("UserID", userID);
                if (c7.isNull(c7.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c7.getString(c7.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningInvestigations.put(j);

            }
            c7.close();
            ret_json.put("ChildrenScreeningInvestigations", ChildrenScreeningInvestigations);

            String Query8 = "Select * from ChildrenScreeningSurgicals where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c8 = db.rawQuery(Query8, null);
            JSONArray ChildrenScreeningSurgicals = new JSONArray();
            while (c8.moveToNext()) {
                JSONObject j = new JSONObject();

                if (c8.isNull(c8.getColumnIndex("ChildrenScreeningSurgicalID"))) {
                    j.put("ChildrenScreeningSurgicalID", "");
                } else {
                    j.put("ChildrenScreeningSurgicalID", c8.getString(c8.getColumnIndex("ChildrenScreeningSurgicalID")));
                }
                j.put("", c8.getString(c8.getColumnIndex("ChildrenScreeningSurgicalID")));
                if (c8.isNull(c8.getColumnIndex("LocalChildrenScreeningSurgicalID"))) {
                    j.put("LocalChildrenScreeningSurgicalID", "");
                } else {
                    j.put("LocalChildrenScreeningSurgicalID", c8.getString(c8.getColumnIndex("LocalChildrenScreeningSurgicalID")));
                }
                if (c8.isNull(c8.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c8.getString(c8.getColumnIndex("LocalChildrenScreeningID")));
                }
                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c8.getString(c8.getColumnIndex("LocalChildrenScreeningID"))));
                if (c8.isNull(c8.getColumnIndex("SurgicalID"))) {
                    j.put("SurgicalID", "");
                } else {
                    j.put("SurgicalID", c8.getString(c8.getColumnIndex("SurgicalID")));
                }
                if (c8.isNull(c8.getColumnIndex("Comments"))) {
                    j.put("Comments", "");
                } else {
                    j.put("Comments", c8.getString(c8.getColumnIndex("Comments")));
                }
                j.put("UserID", userID);
                if (c8.isNull(c8.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c8.getString(c8.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningSurgicals.put(j);

            }
            c8.close();
            ret_json.put("ChildrenScreeningSurgicals", ChildrenScreeningSurgicals);

            String Query9 = "Select * from ChildrenScreeningRecommendations where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c9 = db.rawQuery(Query9, null);
            JSONArray ChildrenScreeningRecommendations = new JSONArray();
            while (c9.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c9.isNull(c9.getColumnIndex("ChildrenScreeningRecommendationID"))) {
                    j.put("ChildrenScreeningRecommendationID", "");
                } else {
                    j.put("ChildrenScreeningRecommendationID", c9.getString(c9.getColumnIndex("ChildrenScreeningRecommendationID")));
                }
                if (c9.isNull(c9.getColumnIndex("LocalChildrenScreeningRecommendationID"))) {
                    j.put("LocalChildrenScreeningRecommendationID", "");
                } else {
                    j.put("LocalChildrenScreeningRecommendationID", c9.getString(c9.getColumnIndex("LocalChildrenScreeningRecommendationID")));
                }

                if (c9.isNull(c9.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c9.getString(c9.getColumnIndex("LocalChildrenScreeningID")));
                }

                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c9.getString(c9.getColumnIndex("LocalChildrenScreeningID"))));

                if (c9.isNull(c9.getColumnIndex("Diet"))) {
                    j.put("Diet", "");
                } else {
                    j.put("Diet", c9.getString(c9.getColumnIndex("Diet")));
                }

                if (c9.isNull(c9.getColumnIndex("personalHygiene"))) {
                    j.put("PersonalHygiene", "");
                } else {
                    j.put("PersonalHygiene", c9.getString(c9.getColumnIndex("personalHygiene")));
                }
                if (c9.isNull(c9.getColumnIndex("OralHygience"))) {
                    j.put("OralHygiene", "");
                } else {
                    j.put("OralHygiene", c9.getString(c9.getColumnIndex("OralHygience")));
                }
                if (c9.isNull(c9.getColumnIndex("PresribedMedication"))) {
                    j.put("PrescribedMeditation", "");
                } else {
                    j.put("PrescribedMeditation", c9.getString(c9.getColumnIndex("PresribedMedication")));
                }
                if (c9.isNull(c9.getColumnIndex("OtherComments"))) {
                    j.put("OtherComments", "");
                } else {
                    j.put("OtherComments", c9.getString(c9.getColumnIndex("OtherComments")));
                }
                if (c9.isNull(c9.getColumnIndex("DoctorComments"))) {
                    j.put("DoctorComments", "");
                } else {
                    j.put("DoctorComments", c9.getString(c9.getColumnIndex("DoctorComments")));
                }
                j.put("UserID", userID);
                if (c9.isNull(c9.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c9.getString(c9.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningRecommendations.put(j);

            }
            c9.close();
            ret_json.put("ChildrenScreeningRecommendations", ChildrenScreeningRecommendations);

            String Query10 = "Select * from ChildrenScreeningPE where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c10 = db.rawQuery(Query10, null);
            JSONArray ChildrenScreeningPE = new JSONArray();
            while (c10.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c10.isNull(c10.getColumnIndex("ChildrenScreeningPEID"))) {
                    j.put("ChildrenScreeningPEID", "");
                } else {
                    j.put("ChildrenScreeningPEID", c10.getString(c10.getColumnIndex("ChildrenScreeningPEID")));
                }
                if (c10.isNull(c10.getColumnIndex("LocalChildrenScreeningPEID"))) {
                    j.put("LocalChildrenScreeningPEID", "");
                } else {
                    j.put("LocalChildrenScreeningPEID", c10.getString(c10.getColumnIndex("LocalChildrenScreeningPEID")));
                }
                if (c10.isNull(c10.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c10.getString(c10.getColumnIndex("LocalChildrenScreeningID")));
                }
                if (c10.isNull(c10.getColumnIndex("ChildrenScreeningID"))) {
                    j.put("ChildrenScreeningID", "");
                } else {
                    j.put("ChildrenScreeningID", c10.getString(c10.getColumnIndex("ChildrenScreeningID")));
                }
                if (c10.isNull(c10.getColumnIndex("ScreeningQuestionID"))) {
                    j.put("ScreeningQuestionID", "");
                } else {
                    j.put("ScreeningQuestionID", c10.getString(c10.getColumnIndex("ScreeningQuestionID")));
                }
                if (c10.isNull(c10.getColumnIndex("QuestionID"))) {
                    j.put("QuestionID", "");
                } else {
                    j.put("QuestionID", c10.getString(c10.getColumnIndex("QuestionID")));
                }
                if (c10.isNull(c10.getColumnIndex("Question"))) {
                    j.put("Question", "");
                } else {
                    j.put("Question", c10.getString(c10.getColumnIndex("Question")));
                }
                if (c10.isNull(c10.getColumnIndex("IsNegaiveQuestion"))) {
                    j.put("IsNegativeQuestion", "");
                } else {
                    j.put("IsNegativeQuestion", c10.getString(c10.getColumnIndex("IsNegaiveQuestion")));
                }

                if (c10.isNull(c10.getColumnIndex("Order"))) {
                    j.put("Order", "");
                } else {
                    j.put("Order", c10.getString(c10.getColumnIndex("Order")));
                }

                if (c10.isNull(c10.getColumnIndex("Answer"))) {
                    j.put("Answer", "");
                } else {
                    j.put("Answer", c10.getString(c10.getColumnIndex("Answer")));
                }

                if (c10.isNull(c10.getColumnIndex("IsReferredWhenYes"))) {
                    j.put("IsReferredWhenYes", "");
                } else {
                    j.put("IsReferredWhenYes", c10.getString(c10.getColumnIndex("IsReferredWhenYes")));
                }
                j.put("UserID", userID);
                if (c10.isNull(c10.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c10.getString(c10.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningPE.put(j);

            }
            c10.close();
            ret_json.put("ChildrenScreeningPE", ChildrenScreeningPE);

            String Query11 = "Select * from ChildrenScreeningLocalTreatment where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c11 = db.rawQuery(Query11, null);
            JSONArray ChildrenScreeningLocalTreatment = new JSONArray();
            while (c11.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c11.isNull(c11.getColumnIndex("ChildrenScreeningLocalTreatmentID"))) {
                    j.put("ChildrenScreeningLocalTreatmentID", "");
                } else {
                    j.put("ChildrenScreeningLocalTreatmentID", c11.getString(c11.getColumnIndex("ChildrenScreeningLocalTreatmentID")));
                }
                if (c11.isNull(c11.getColumnIndex("LocalChildrenScreeningLocalTreatmentID"))) {
                    j.put("LocalChildrenScreeningLocalTreatmentID", "");
                } else {
                    j.put("LocalChildrenScreeningLocalTreatmentID", c11.getString(c11.getColumnIndex("LocalChildrenScreeningLocalTreatmentID")));
                }
                if (c11.isNull(c11.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c11.getString(c11.getColumnIndex("LocalChildrenScreeningID")));
                }
                if (c11.isNull(c11.getColumnIndex("ChildrenScreeningID"))) {
                    j.put("ChildrenScreeningID", "");
                } else {
                    j.put("ChildrenScreeningID", c11.getString(c11.getColumnIndex("ChildrenScreeningID")));
                }
                if (c11.isNull(c11.getColumnIndex("Diagnosis"))) {
                    j.put("Diagnosis", "");
                } else {
                    j.put("Diagnosis", c11.getString(c11.getColumnIndex("Diagnosis")));
                }

                if (c11.isNull(c11.getColumnIndex("MedicationGiven"))) {
                    j.put("MedicationGiven", "");
                } else {
                    j.put("MedicationGiven", c11.getString(c11.getColumnIndex("MedicationGiven")));
                }
                j.put("UserID", userID);
                if (c11.isNull(c11.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c11.getString(c11.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningLocalTreatment.put(j);

            }
            c11.close();
            ret_json.put("ChildrenScreeningLocalTreatment", ChildrenScreeningLocalTreatment);

            String Query12 = "Select * from ChildrenScreeningAllergies where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c12 = db.rawQuery(Query12, null);
            JSONArray ChildrenScreeningAllergies = new JSONArray();
            while (c12.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c12.isNull(c12.getColumnIndex("ChildrenScreeningAllergyID"))) {
                    j.put("ChildrenScreeningAllergyID", "");
                } else {
                    j.put("ChildrenScreeningAllergyID", c12.getString(c12.getColumnIndex("ChildrenScreeningAllergyID")));
                }
                if (c12.isNull(c12.getColumnIndex("LocalChildrenScreeningAllergyID"))) {
                    j.put("LocalChildrenScreeningAllergyID", "");
                } else {
                    j.put("LocalChildrenScreeningAllergyID", c12.getString(c12.getColumnIndex("LocalChildrenScreeningAllergyID")));
                }
                if (c12.isNull(c12.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c12.getString(c12.getColumnIndex("LocalChildrenScreeningID")));
                }
                if (c12.isNull(c12.getColumnIndex("ChildrenScreeningID"))) {
                    j.put("ChildrenScreeningID", "");
                } else {
                    j.put("LastCommitedDateTime", c12.getString(c12.getColumnIndex("ChildrenScreeningID")));
                }
                if (c12.isNull(c12.getColumnIndex("AllergyID"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("AllergyID", c12.getString(c12.getColumnIndex("AllergyID")));
                }
                if (c12.isNull(c12.getColumnIndex("Comments"))) {
                    j.put("Comments", "");
                } else {
                    j.put("Comments", c12.getString(c12.getColumnIndex("Comments")));
                }
                j.put("UserID", userID);
                if (c12.isNull(c12.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c12.getString(c12.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningAllergies.put(j);

            }
            c12.close();
            ret_json.put("ChildrenScreeningAllergies", ChildrenScreeningAllergies);

            String Query13 = "Select * from ChildScreeningFH where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c13 = db.rawQuery(Query13, null);
            JSONArray ChildrenScreeningFH = new JSONArray();
            while (c13.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c13.isNull(c13.getColumnIndex("ChildScreeningFHID"))) {
                    j.put("ChildrenScreeningFHID", "");
                } else {
                    j.put("ChildrenScreeningFHID", c13.getString(c13.getColumnIndex("ChildScreeningFHID")));
                }
                if (c13.isNull(c13.getColumnIndex("LocalChildScreeningFHID"))) {
                    j.put("LocalChildrenScreeningFHID", "");
                } else {
                    j.put("LocalChildrenScreeningFHID", c13.getString(c13.getColumnIndex("LocalChildScreeningFHID")));
                }
                if (c13.isNull(c13.getColumnIndex("LocalChildrenScreeningID"))) {
                    j.put("LocalChildrenScreeningID", "");
                } else {
                    j.put("LocalChildrenScreeningID", c13.getString(c13.getColumnIndex("LocalChildrenScreeningID")));
                }
                if (c13.isNull(c13.getColumnIndex("ChildrenScreeningID"))) {
                    j.put("ChildrenScreeningID", "");
                } else {
                    j.put("ChildrenScreeningID", c13.getString(c13.getColumnIndex("ChildrenScreeningID")));
                }
                if (c13.isNull(c13.getColumnIndex("FamilyHistoryID"))) {
                    j.put("FamilyHistoryID", "");
                } else {
                    j.put("FamilyHistoryID", c13.getString(c13.getColumnIndex("FamilyHistoryID")));
                }
                if (c13.isNull(c13.getColumnIndex("Notes"))) {
                    j.put("Notes", "");
                } else {
                    j.put("Notes", c13.getString(c13.getColumnIndex("Notes")));
                }
                if (c13.isNull(c13.getColumnIndex("FamilyMemberRelationID"))) {
                    j.put("FamilyMemberRelationID", "");
                } else {
                    j.put("FamilyMemberRelationID", c13.getString(c13.getColumnIndex("FamilyMemberRelationID")));
                }
                j.put("UserID", userID);
                if (c13.isNull(c13.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c13.getString(c13.getColumnIndex("LastCommitedDate")));
                }
                ChildrenScreeningFH.put(j);

            }
            c13.close();
            ret_json.put("ChildrenScreeningFH", ChildrenScreeningFH);

            String Query14 = "Select * from ChildrenDisabilities where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c14 = db.rawQuery(Query14, null);
            JSONArray ChildrenDisabilities = new JSONArray();
            while (c14.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c14.isNull(c14.getColumnIndex("ChildrenDisabilityID"))) {
                    j.put("ChildrenDisabilityID", "");
                } else {
                    j.put("ChildrenDisabilityID", c14.getString(c14.getColumnIndex("ChildrenDisabilityID")));
                }
                if (c14.isNull(c14.getColumnIndex("LocalChildrenDisabilityID"))) {
                    j.put("LocalChildrenDisabilityID", "");
                } else {
                    j.put("LocalChildrenDisabilityID", c14.getString(c14.getColumnIndex("LocalChildrenDisabilityID")));
                }
                if (c14.isNull(c14.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    j.put("LocalChildrenID", c14.getString(c14.getColumnIndex("LocalChildrenID")));
                }
                if (c14.isNull(c14.getColumnIndex("ChildrenID"))) {
                    j.put("ChildrenID", "");
                } else {
                    j.put("ChildrenID", c14.getString(c14.getColumnIndex("ChildrenID")));
                }

                j.put("PWDCardNumber", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "Children", "PWDCardNumber", "LocalChildrenID", c14.getString(c14.getColumnIndex("LocalChildrenID"))));
                if (c14.isNull(c14.getColumnIndex("DisabilityDetails"))) {
                    j.put("DisabilityDetails", "");
                } else {
                    j.put("DisabilityDetails", c14.getString(c14.getColumnIndex("DisabilityDetails")));
                }
                j.put("UserID", userID);
                if (c14.isNull(c14.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c14.getString(c14.getColumnIndex("LastCommitedDate")));
                }
                ChildrenDisabilities.put(j);

            }
            c14.close();
            ret_json.put("ChildrenDisabilities", ChildrenDisabilities);

            String Query15 = "Select * from ChildrenParents where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c15 = db.rawQuery(Query15, null);
            JSONArray ChildrenParents = new JSONArray();
            while (c15.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c15.isNull(c15.getColumnIndex("ChildrenParentID"))) {
                    j.put("ChildrenParentID", "");
                } else {
                    j.put("ChildrenParentID", c15.getString(c15.getColumnIndex("ChildrenParentID")));
                }
                if (c15.isNull(c15.getColumnIndex("LocalChildrenParentID"))) {
                    j.put("LocalChildrenParentID", "");
                } else {
                    j.put("LocalChildrenParentID", c15.getString(c15.getColumnIndex("LocalChildrenParentID")));
                }
                if (c15.isNull(c15.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    j.put("LocalChildrenID", c15.getString(c15.getColumnIndex("LocalChildrenID")));
                }
                if (c15.isNull(c15.getColumnIndex("ChildrenID"))) {
                    j.put("ChildrenID", "");
                } else {
                    j.put("ChildrenID", c15.getString(c15.getColumnIndex("ChildrenID")));
                }
                if (c15.isNull(c15.getColumnIndex("RelationID"))) {
                    j.put("RelationID", "");
                } else {
                    j.put("RelationID", c15.getString(c15.getColumnIndex("RelationID")));
                }
                j.put("QualificationID", "");
                j.put("OccupationID", "");
                j.put("UserID", userID);
                if (c15.isNull(c15.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c15.getString(c15.getColumnIndex("LastCommitedDate")));
                }
                ChildrenParents.put(j);

            }
            c15.close();
            ret_json.put("ChildrenParents", ChildrenParents);

            String Query16 = "Select * from ChildrenAllergiesHistory where  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c16 = db.rawQuery(Query16, null);
            JSONArray ChildrenAllergiesHistory = new JSONArray();
            while (c16.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c16.isNull(c16.getColumnIndex("ChildrenAllergyHistoryID"))) {
                    j.put("ChildrenAllergyHistoryID", "");
                } else {
                    j.put("ChildrenAllergyHistoryID", c16.getString(c16.getColumnIndex("ChildrenAllergyHistoryID")));
                }
                if (c16.isNull(c16.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    j.put("LocalChildrenID", c16.getString(c16.getColumnIndex("LocalChildrenID")));
                }
                if (c16.isNull(c16.getColumnIndex("ChildrenID"))) {
                    j.put("ChildrenID", "");
                } else {
                    j.put("ChildrenID", c16.getString(c16.getColumnIndex("ChildrenID")));
                }
                if (c16.isNull(c16.getColumnIndex("PlanningYear"))) {
                    j.put("PlanningYear", "");
                } else {
                    j.put("PlanningYear", c16.getString(c16.getColumnIndex("PlanningYear")));
                }
                if (c16.isNull(c16.getColumnIndex("ScreeningRoundID"))) {
                    j.put("ScreeningRoundID", "");
                } else {
                    j.put("ScreeningRoundID", c16.getString(c16.getColumnIndex("ScreeningRoundID")));
                }
                if (c16.isNull(c16.getColumnIndex("ScreeningRoundName"))) {
                    j.put("ScreeningRoundName", "");
                } else {
                    j.put("ScreeningRoundName", c16.getString(c16.getColumnIndex("ScreeningRoundName")));
                }
                if (c16.isNull(c16.getColumnIndex("AllergyID"))) {
                    j.put("AllergyID", "");
                } else {
                    j.put("AllergyID", c16.getString(c16.getColumnIndex("AllergyID")));
                }
                if (c16.isNull(c16.getColumnIndex("AllergyName"))) {
                    j.put("AllergyName", "");
                } else {
                    j.put("AllergyName", c16.getString(c16.getColumnIndex("AllergyName")));
                }
                if (c16.isNull(c16.getColumnIndex("Comments"))) {
                    j.put("Comments", "");
                } else {
                    j.put("Comments", c16.getString(c16.getColumnIndex("Comments")));
                }
                if (c16.isNull(c16.getColumnIndex("IsDeleted"))) {
                    j.put("IsDeleted", "");
                } else {
                    j.put("IsDeleted", c16.getString(c16.getColumnIndex("IsDeleted")));
                }
                j.put("UserID", userID);
                if (c16.isNull(c16.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c16.getString(c16.getColumnIndex("LastCommitedDate")));
                }
                ChildrenAllergiesHistory.put(j);

            }
            c16.close();
            ret_json.put("ChildrenAllergiesHistory", ChildrenAllergiesHistory);

            String Query17 = "Select * from ChildrenFamilyHistory where isDeleted = 0 and  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c17 = db.rawQuery(Query17, null);
            JSONArray ChildrenFamilyHistory = new JSONArray();
            while (c17.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c17.isNull(c17.getColumnIndex("ChildrenFamilyHistoryID"))) {
                    j.put("ChildrenFamilyHistoryID", "");
                } else {
                    j.put("ChildrenFamilyHistoryID", c17.getString(c17.getColumnIndex("ChildrenFamilyHistoryID")));
                }
                if (c17.isNull(c17.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    j.put("LocalChildrenID", c17.getString(c17.getColumnIndex("LocalChildrenID")));
                }
                if (c17.isNull(c17.getColumnIndex("ChildrenID"))) {
                    j.put("ChildrenID", "");
                } else {
                    j.put("ChildrenID", c17.getString(c17.getColumnIndex("ChildrenID")));
                }
                if (c17.isNull(c17.getColumnIndex("PlanningYear"))) {
                    j.put("PlanningYear", "");
                } else {
                    j.put("PlanningYear", c17.getString(c17.getColumnIndex("PlanningYear")));
                }
                if (c17.isNull(c17.getColumnIndex("ScreeningRoundID"))) {
                    j.put("ScreeningRoundID", "");
                } else {
                    j.put("ScreeningRoundID", c17.getString(c17.getColumnIndex("ScreeningRoundID")));
                }
                if (c17.isNull(c17.getColumnIndex("ScreeningRoundName"))) {
                    j.put("ScreeningRoundName", "");
                } else {
                    j.put("ScreeningRoundName", c17.getString(c17.getColumnIndex("ScreeningRoundName")));
                }
                if (c17.isNull(c17.getColumnIndex("FamilyHistoryID"))) {
                    j.put("FamilyHistoryID", "");
                } else {
                    j.put("FamilyHistoryID", c17.getString(c17.getColumnIndex("FamilyHistoryID")));
                }
                if (c17.isNull(c17.getColumnIndex("FamilyHistoryName"))) {
                    j.put("FamilyHistoryName", "");
                } else {
                    j.put("FamilyHistoryName", c17.getString(c17.getColumnIndex("FamilyHistoryName")));
                }
                if (c17.isNull(c17.getColumnIndex("HasHistory"))) {
                    j.put("HasHistory", "");
                } else {
                    j.put("HasHistory", c17.getString(c17.getColumnIndex("HasHistory")));
                }
                if (c17.isNull(c17.getColumnIndex("FamilyMemberRelationID"))) {
                    j.put("FamilyMemberRelationID", "");
                } else {
                    j.put("FamilyMemberRelationID", c17.getString(c17.getColumnIndex("FamilyMemberRelationID")));
                }
                if (c17.isNull(c17.getColumnIndex("FamilyMemberRelationName"))) {
                    j.put("FamilyMemberRelationName", "");
                } else {
                    j.put("FamilyMemberRelationName", c17.getString(c17.getColumnIndex("FamilyMemberRelationName")));
                }
                if (c17.isNull(c17.getColumnIndex("Notes"))) {
                    j.put("Notes", "");
                } else {
                    j.put("Notes", c17.getString(c17.getColumnIndex("Notes")));
                }
                if (c17.isNull(c17.getColumnIndex("Comments"))) {
                    j.put("Comments", "");
                } else {
                    j.put("Comments", c17.getString(c17.getColumnIndex("Comments")));
                }
                j.put("UserID", userID);
                if (c17.isNull(c17.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c17.getString(c17.getColumnIndex("LastCommitedDate")));
                }
                ChildrenFamilyHistory.put(j);

            }
            c17.close();
            ret_json.put("ChildrenFamilyHistory", ChildrenFamilyHistory);

            String Query18 = "Select * from ChildrenMedicalHistory where isDeleted = 0 and  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c18 = db.rawQuery(Query18, null);
            JSONArray ChildrenMedicalHistory = new JSONArray();
            while (c18.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c18.isNull(c18.getColumnIndex("ChildrenMedicalHistoryID"))) {
                    j.put("ChildrenMedicalHistoryID", "");
                } else {
                    j.put("ChildrenMedicalHistoryID", c18.getString(c18.getColumnIndex("ChildrenMedicalHistoryID")));
                }
                if (c18.isNull(c18.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    j.put("LocalChildrenID", c18.getString(c18.getColumnIndex("LocalChildrenID")));
                }
                if (c18.isNull(c18.getColumnIndex("ChildrenID"))) {
                    j.put("ChildrenID", "");
                } else {
                    j.put("ChildrenID", c18.getString(c18.getColumnIndex("ChildrenID")));
                }
                if (c18.isNull(c18.getColumnIndex("PlanningYear"))) {
                    j.put("PlanningYear", "");
                } else {
                    j.put("PlanningYear", c18.getString(c18.getColumnIndex("PlanningYear")));
                }
                if (c18.isNull(c18.getColumnIndex("ScreeningRoundID"))) {
                    j.put("ScreeningRoundID", "");
                } else {
                    j.put("ScreeningRoundID", c18.getString(c18.getColumnIndex("ScreeningRoundID")));
                }
                if (c18.isNull(c18.getColumnIndex("ScreeningRoundName"))) {
                    j.put("ScreeningRoundName", "");
                } else {
                    j.put("ScreeningRoundName", c18.getString(c18.getColumnIndex("ScreeningRoundName")));
                }
                if (c18.isNull(c18.getColumnIndex("Medications"))) {
                    j.put("Medications", "");
                } else {
                    j.put("Medications", c18.getString(c18.getColumnIndex("Medications")));
                }
                j.put("UserID", userID);
                if (c18.isNull(c18.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c18.getString(c18.getColumnIndex("LastCommitedDate")));
                }
                ChildrenMedicalHistory.put(j);

            }
            c18.close();
            ret_json.put("ChildrenMedicalHistory", ChildrenMedicalHistory);

            String Query19 = "Select * from ChildrenSurgicalsHistory where isDeleted = 0 and  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c19 = db.rawQuery(Query19, null);
            JSONArray ChildrenSurgicalHistory = new JSONArray();
            while (c19.moveToNext()) {
                JSONObject j = new JSONObject();
                if (c19.isNull(c19.getColumnIndex("ChildrenSurgicalsHistoryID"))) {
                    j.put("ChildrenSurgicalsHistoryID", "");
                } else {
                    j.put("ChildrenSurgicalsHistoryID", c19.getString(c19.getColumnIndex("ChildrenSurgicalsHistoryID")));
                }
                if (c19.isNull(c19.getColumnIndex("LocalChildrenID"))) {
                    j.put("LocalChildrenID", "");
                } else {
                    j.put("LocalChildrenID", c19.getString(c19.getColumnIndex("LocalChildrenID")));
                }
                if (c19.isNull(c19.getColumnIndex("ChildrenID"))) {
                    j.put("ChildrenID", "");
                } else {
                    j.put("ChildrenID", c19.getString(c19.getColumnIndex("ChildrenID")));
                }
                if (c19.isNull(c19.getColumnIndex("PlanningYear"))) {
                    j.put("PlanningYear", "");
                } else {
                    j.put("PlanningYear", c19.getString(c19.getColumnIndex("PlanningYear")));
                }
                if (c19.isNull(c19.getColumnIndex("ScreeningRoundID"))) {
                    j.put("ScreeningRoundID", "");
                } else {
                    j.put("ScreeningRoundID", c19.getString(c19.getColumnIndex("ScreeningRoundID")));
                }
                if (c19.isNull(c19.getColumnIndex("ScreeningRoundName"))) {
                    j.put("ScreeningRoundName", "");
                } else {
                    j.put("ScreeningRoundName", c19.getString(c19.getColumnIndex("ScreeningRoundName")));
                }
                if (c19.isNull(c19.getColumnIndex("SurgicalID"))) {
                    j.put("SurgicalID", "");
                } else {
                    j.put("SurgicalID", c19.getString(c19.getColumnIndex("SurgicalID")));
                }
                if (c19.isNull(c19.getColumnIndex("SurgicalName"))) {
                    j.put("SurgicalName", "");
                } else {
                    j.put("SurgicalName", c19.getString(c19.getColumnIndex("SurgicalName")));
                }
                if (c19.isNull(c19.getColumnIndex("Comments"))) {
                    j.put("Comments", "");
                } else {
                    j.put("Comments", c19.getString(c19.getColumnIndex("Comments")));
                }
                j.put("UserID", userID);
                if (c19.isNull(c19.getColumnIndex("LastCommitedDate"))) {
                    j.put("LastCommitedDateTime", "");
                } else {
                    j.put("LastCommitedDateTime", c19.getString(c19.getColumnIndex("LastCommitedDate")));
                }
                ChildrenSurgicalHistory.put(j);

            }
            c19.close();
            ret_json.put("ChildrenSurgicalHistory", ChildrenSurgicalHistory);
            db.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sync json.....", ret_json.toString());
        return ret_json;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allgranted = false;
        if (requestCode == 101) {
            //check if all permissions are granted

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;

                    break;
                }
            }
            if (allgranted) {
                copyDatabase();
                // backupdb(this);
            } else {
                checkPermission();
            }
        }


    }


    //private void copyDatabase(String DB_NAME) { // praveen modified this code.
    public void copyDatabase() {
        String BACKUP_DB_NAME = "";
        //
        try {
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").format(java.util.Calendar.getInstance().getTime());
            BACKUP_DB_NAME = "RBSK-V3-" + timeStamp + ".3il";
            final String outFileName = Environment.getExternalStorageDirectory() + "/RBSK/DB/";
            File directory = new File(Environment.getExternalStorageDirectory(), "RBSK");
            if (!directory.exists())
                directory.mkdir();
            File directorydbFile = new File(directory, BACKUP_DB_NAME);
            File input = new File(DBHelper.DATABASE_PATH + DBHelper.DB_NAME);
            FileChannel source = null;
            FileChannel destination = null;

            source = new FileInputStream(input).getChannel();
            destination = new FileOutputStream(directorydbFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception occured during Backup",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public static void backupdb(final Context mContext) {
        new Thread(new Runnable() {
            File backupDB = null;

            @SuppressWarnings("resource")
            @Override
            public void run() {
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = DBHelper.DATABASE_PATH
                                + DBHelper.DB_NAME;
                        String backupDBPath = "RBSKDBBACKUP" + Helper.getTodayDateTime() + ".sqlite";

                        File currentDB = new File(data, currentDBPath);
                        backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {

                            FileChannel src = new FileInputStream(currentDB)
                                    .getChannel();
                            FileChannel dst = new FileOutputStream(backupDB)
                                    .getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }
                    }
                    class SingleMediaScanner implements
                            MediaScannerConnectionClient {

                        private MediaScannerConnection mMs;
                        private File mFile;

                        public SingleMediaScanner(Context context, File f) {
                            mFile = f;
                            mMs = new MediaScannerConnection(context, this);
                            mMs.connect();
                        }

                        @Override
                        public void onMediaScannerConnected() {
                            mMs.scanFile(mFile.getAbsolutePath(), null);
                        }

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            mMs.disconnect();
                        }

                    }
                    new SingleMediaScanner(mContext, backupDB);
                    mContext.sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory())));

                    if (mContext instanceof Activity)
                        ((Activity) mContext).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(
                                        mContext,
                                        "File moved to\n"
                                                + backupDB.getAbsolutePath(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
