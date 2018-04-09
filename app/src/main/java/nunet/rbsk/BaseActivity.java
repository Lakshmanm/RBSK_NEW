package nunet.rbsk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAssignedNumbers;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.LoginActivity;

public class BaseActivity extends Activity {

    ProgressDialog progDailog;

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
            progDailog = ProgressDialog.show(BaseActivity.this,
                    "Please Wait...", "Syncing Data...", true);

        }

        @Override
        protected String doInBackground(JSONObject... params) {

            String strResponse = "";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UrlUtils.URL_SYNC);

            String mResultData = null;
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                if (params[0] != null) {
                    @SuppressWarnings("unchecked")

                    Iterator<String> iter = params[0].keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            String string = params[0].getString(key);
                            nameValuePairs.add(new BasicNameValuePair(key, string));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.i("input in post", new UrlEncodedFormEntity(nameValuePairs).toString());
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                mResultData = EntityUtils.toString(response.getEntity());
                strResponse = mResultData;
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
                progDailog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
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
                j.put("LocalInstitutePlanID", c.getString(c.getColumnIndex("LocalInstitutePlanID")));
                j.put("InstitutePlanID", c.getString(c.getColumnIndex("InstitutePlanID")));
                j.put("InstituteID", c.getString(c.getColumnIndex("InstituteID")));
                j.put("RBSKCalendarYearID", c.getString(c.getColumnIndex("RBSKCalendarYearID")));
                j.put("ScreeningRoundID", c.getString(c.getColumnIndex("ScreeningRoundId")));
                j.put("InstitutePlanStatusID", c.getString(c.getColumnIndex("InstitutePlanStatusID")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c.getString(c.getColumnIndex("LastCommitedDate")));
                LocalInstitutePlanID.add(c.getString(c.getColumnIndex("LocalInstitutePlanID")));
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
                    j.put("LocalInstitutePlanDetailID", c1.getString(c1.getColumnIndex("LocalInstitutePlanDetailID")));
                    j.put("InstitutePlanDetailID", c1.getString(c1.getColumnIndex("InstitutePlanDetailID")));
                    j.put("LocalInstitutePlanID", c1.getString(c1.getColumnIndex("LocalInstitutePlanID")));
                    j.put("ScheduledDate", c1.getString(c1.getColumnIndex("scheduleDate")));
                    j.put("PlannedCount", c1.getString(c1.getColumnIndex("PlannedCount")));
                    j.put("InstitutePlanSkipReasonID", c1.getString(c1.getColumnIndex("InstitutePlanSkipReasonID")));
                    j.put("SkipComments", c1.getString(c1.getColumnIndex("SkipComments")));
                    j.put("MobileHealthTeamID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "MHTStaff", "MobileHealthTeamID", "UserID", userID));
                    j.put("UserID", userID);
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
                    j.put("InstituteID", c2.getString(c2.getColumnIndex("InstituteID")));
                    j.put("LocalInstituteID", c2.getString(c2.getColumnIndex("LocalInstituteID")));
                    j.put("InstituteName", c2.getString(c2.getColumnIndex("InstituteName")));
                    j.put("DISECode", c2.getString(c2.getColumnIndex("DiseCode")));
                    j.put("UserID", userID);
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
                    j.put("LocalInstituteScreeningID", c2.getString(c2.getColumnIndex("LocalInstituteScreeningID")));
                    LocalInstituteScreeningID.add(c2.getString(c2.getColumnIndex("LocalInstituteScreeningID")));
                    j.put("InstituteScreeningID", c2.getString(c2.getColumnIndex("InstituteScreeningID")));
                    j.put("InstituteID", c2.getString(c2.getColumnIndex("InstituteID")));
                    j.put("InstituteScreenStatusID", c2.getString(c2.getColumnIndex("InstituteScreeningStatusID")));
                    j.put("UserID", userID);
                    j.put("LastCommitedDateTime", c2.getString(c2.getColumnIndex("LastCommitedDate")));
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
                    j.put("LocalInstituteScreeningDetailID", c2.getString(c2.getColumnIndex("LocalInstituteScreeningDetailID")));
                    j.put("InstituteScreeningDetailID", c2.getString(c2.getColumnIndex("InstituteScreeningDetailID")));
                    j.put("LocalInstituteScreeningID", c2.getString(c2.getColumnIndex("LocalInstituteScreeningID")));
                    j.put("ScreeningStatusID", c2.getString(c2.getColumnIndex("ScreenStatusID")));
                    j.put("ScreeningStartDateTime", c2.getString(c2.getColumnIndex("ScreeningStartDateTime")));
                    j.put("ScreeningEndDateTime", c2.getString(c2.getColumnIndex("ScreeningEndDateTime")));
                    j.put("UserID", userID);
                    j.put("LastCommitedDateTime", c2.getString(c2.getColumnIndex("LastCommitedDate")));
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
                j.put("ChildrenScreeningID", c4.getString(c4.getColumnIndex("ChildrenScreeningID")));
                j.put("LocalChildrenScreeningID", c4.getString(c4.getColumnIndex("LocalChildrenScreeningID")));
                j.put("LocalChildrenID", c4.getString(c4.getColumnIndex("LocalChildrenID")));
                j.put("ChildrenID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "Children", "ChildrenID", "LocalChildrenID", c4.getString(c4.getColumnIndex("LocalChildrenID"))));
                LocalChildrenID.add(c4.getString(c4.getColumnIndex("LocalChildrenID")));
                j.put("ScreeningTemplateTypeID", c4.getString(c4.getColumnIndex("ScreeningTemplateTypeID")));
                j.put("ScreeningStartDateTime", c4.getString(c4.getColumnIndex("ScreeningStartDateTime")));
                j.put("ScreeningEndDateTime", c4.getString(c4.getColumnIndex("ScreeningEndDateTime")));
                j.put("ChildrenScreeingStatusID", c4.getString(c4.getColumnIndex("ChildrenScreenStatusID")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c4.getString(c4.getColumnIndex("LastCommitedDate")));
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
                j.put("ChildrenID", c3.getString(c3.getColumnIndex("ChildrenID")));
                j.put("LocalChildrenID", c3.getString(c3.getColumnIndex("LocalChildrenID")));
                j.put("UserID", c3.getString(c3.getColumnIndex("LocalUserID")));
                j.put("MCTSID", c3.getString(c3.getColumnIndex("MCTSID")));
                j.put("IdentificationMark1", c3.getString(c3.getColumnIndex("IdentificationMark1")));
                j.put("IdentificationMark2", c3.getString(c3.getColumnIndex("IdentificationMark2")));
                j.put("HasDisability", c3.getString(c3.getColumnIndex("HasDisability")));
                j.put("ChildrenStatusID", c3.getString(c3.getColumnIndex("ChildrenStatusID")));
                j.put("AddressID", c3.getString(c3.getColumnIndex("AddressID")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c3.getString(c3.getColumnIndex("LastCommitedDate")));
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
                j.put("ChildrenScreeningVitalsID", c5.getString(c5.getColumnIndex("ChildrenScreeningVitalsID")));
                j.put("LocalChildrenScreeningVitalsID", c5.getString(c5.getColumnIndex("LocalChildrenScreeningVitalsID")));
                j.put("LocalChildrenScreeningID", c5.getString(c5.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c5.getString(c5.getColumnIndex("LocalChildrenScreeningID"))));
                j.put("Height", c5.getString(c5.getColumnIndex("Height")));
                j.put("HeightIndication", c5.getString(c5.getColumnIndex("HeightIndication")));
                j.put("Weight", c5.getString(c5.getColumnIndex("Weight")));
                j.put("WeightIndication", c5.getString(c5.getColumnIndex("WeightIndication")));
                j.put("BMI", c5.getString(c5.getColumnIndex("BMI")));
                j.put("BMIIndication", c5.getString(c5.getColumnIndex("BMIIndication")));
                j.put("AcuityOfVisionLefteye", c5.getString(c5.getColumnIndex("AcuityOfVisionLefteye")));
                j.put("AcuityOfVisionRighteye", c5.getString(c5.getColumnIndex("AcuityOfVisionRighteye")));
                j.put("BP", c5.getString(c5.getColumnIndex("BP")));
                j.put("BPIndication", c5.getString(c5.getColumnIndex("BPIndication")));
                j.put("BloodGroupID", c5.getString(c5.getColumnIndex("BloodGroupID")));
                j.put("BloodGroupNotes", c5.getString(c5.getColumnIndex("BloodGroupNotes")));
                j.put("TemperatureID", c5.getString(c5.getColumnIndex("TemperatureID")));
                j.put("TemperatureIndication", c5.getString(c5.getColumnIndex("TemperatureIndication")));
                j.put("HemoGlobinID", c5.getString(c5.getColumnIndex("HemoGlobinID")));
                j.put("HemoGlobinIndication", c5.getString(c5.getColumnIndex("HemoGlobinIndication")));
                j.put("MUACInCms", c5.getString(c5.getColumnIndex("MUACInCms")));
                j.put("MUACIndication", c5.getString(c5.getColumnIndex("MUACIndication")));
                j.put("HeadCircumferenceInCms", c5.getString(c5.getColumnIndex("HeadCircumferenceInCms")));
                j.put("HeadCircumferenceIndication", c5.getString(c5.getColumnIndex("HeadCircumferenceIndication")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c5.getString(c5.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningVitals.put(j);

            }
            c5.close();
            ret_json.put("ChildrenScreeningVitals", ChildrenScreeningVitals);

            String Query6 = "Select * from ChildrenScreeningReferrals where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c6 = db.rawQuery(Query6, null);
            JSONArray ChildrenScreeningReferrals = new JSONArray();
            while (c6.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningReferralID", c6.getString(c6.getColumnIndex("ChildrenScreeningReferralID")));
                j.put("LocalChildrenScreeningReferralID", c6.getString(c6.getColumnIndex("LocalChildrenScreeningReferralID")));
                j.put("LocalChildrenScreeningID", c6.getString(c6.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", c6.getString(c6.getColumnIndex("ChildrenScreeningID")));
                j.put("HealthConditionID", c6.getString(c6.getColumnIndex("HealthConditonID")));
                j.put("WasReferred", c6.getString(c6.getColumnIndex("WasReferred")));
                j.put("ReferredFacilityID", c6.getString(c6.getColumnIndex("ReferredFacilityID")));
                // j.put("ReferralStatusID", c6.getString(c6.getColumnIndex("ReferralStatusID")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c6.getString(c6.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningReferrals.put(j);

            }
            c6.close();
            ret_json.put("ChildrenScreeningReferrals", ChildrenScreeningReferrals);

            String Query7 = "Select * from ChildrenScreeningInvestigations where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c7 = db.rawQuery(Query7, null);
            JSONArray ChildrenScreeningInvestigations = new JSONArray();
            while (c7.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningInvestigationID", c7.getString(c7.getColumnIndex("ChildrenScreeningInvestigationID")));
                j.put("LocalChildrenScreeningInvestigationID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningInvestigationID")));
                j.put("LocalChildrenScreeningReferralID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningReferralID")));
                j.put("LocalChildrenScreeningID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c7.getString(c7.getColumnIndex("LocalChildrenScreeningID"))));
                j.put("ChildrenScreeningReferralID", c7.getString(c7.getColumnIndex("ChildrenScreeningReferralID")));
                j.put("LabInvestigationID", c7.getString(c7.getColumnIndex("LabInvestigationID")));
                j.put("WasSuggested", c7.getString(c7.getColumnIndex("WasSuggested")));
                j.put("Comments", c7.getString(c7.getColumnIndex("Comments")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c7.getString(c7.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningInvestigations.put(j);

            }
            c7.close();
            ret_json.put("ChildrenScreeningInvestigations", ChildrenScreeningInvestigations);

            String Query8 = "Select * from ChildrenScreeningSurgicals where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c8 = db.rawQuery(Query8, null);
            JSONArray ChildrenScreeningSurgicals = new JSONArray();
            while (c8.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningSurgicalID", c8.getString(c8.getColumnIndex("ChildrenScreeningSurgicalID")));
                j.put("LocalChildrenScreeningSurgicalID", c8.getString(c8.getColumnIndex("LocalChildrenScreeningSurgicalID")));
                j.put("LocalChildrenScreeningID", c8.getString(c8.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c8.getString(c8.getColumnIndex("LocalChildrenScreeningID"))));
                j.put("SurgicalID", c8.getString(c8.getColumnIndex("SurgicalID")));
                j.put("Comments", c8.getString(c8.getColumnIndex("Comments")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c8.getString(c8.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningSurgicals.put(j);

            }
            c8.close();
            ret_json.put("ChildrenScreeningSurgicals", ChildrenScreeningSurgicals);

            String Query9 = "Select * from ChildrenScreeningRecommendations where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c9 = db.rawQuery(Query9, null);
            JSONArray ChildrenScreeningRecommendations = new JSONArray();
            while (c9.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningRecommendationID", c9.getString(c9.getColumnIndex("ChildrenScreeningRecommendationID")));
                j.put("LocalChildrenScreeningRecommendationID", c9.getString(c9.getColumnIndex("LocalChildrenScreeningRecommendationID")));
                j.put("LocalChildrenScreeningID", c9.getString(c9.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "ChildrenScreening", "ChildrenScreeningID", "LocalChildrenScreeningID", c9.getString(c9.getColumnIndex("LocalChildrenScreeningID"))));
                j.put("Diet", c9.getString(c9.getColumnIndex("Diet")));
                j.put("PersonalHygiene", c9.getString(c9.getColumnIndex("personalHygiene")));
                j.put("OralHygiene", c9.getString(c9.getColumnIndex("OralHygience")));
                j.put("PrescribedMeditation", c9.getString(c9.getColumnIndex("PresribedMedication")));
                j.put("OtherComments", c9.getString(c9.getColumnIndex("OtherComments")));
                j.put("DoctorComments", c9.getString(c9.getColumnIndex("DoctorComments")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c9.getString(c9.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningRecommendations.put(j);

            }
            c9.close();
            ret_json.put("ChildrenScreeningRecommendations", ChildrenScreeningRecommendations);

            String Query10 = "Select * from ChildrenScreeningPE where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c10 = db.rawQuery(Query10, null);
            JSONArray ChildrenScreeningPE = new JSONArray();
            while (c10.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningPEID", c10.getString(c10.getColumnIndex("ChildrenScreeningPEID")));
                j.put("LocalChildrenScreeningPEID", c10.getString(c10.getColumnIndex("LocalChildrenScreeningPEID")));
                j.put("LocalChildrenScreeningID", c10.getString(c10.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", c10.getString(c10.getColumnIndex("ChildrenScreeningID")));
                j.put("ScreeningQuestionID", c10.getString(c10.getColumnIndex("ScreeningQuestionID")));
                j.put("QuestionID", c10.getString(c10.getColumnIndex("QuestionID")));
                j.put("Question", c10.getString(c10.getColumnIndex("Question")));
                j.put("IsNegativeQuestion", c10.getString(c10.getColumnIndex("IsNegaiveQuestion")));
                j.put("Order", c10.getString(c10.getColumnIndex("Order")));
                j.put("Answer", c10.getString(c10.getColumnIndex("Answer")));
                j.put("IsReferredWhenYes", c10.getString(c10.getColumnIndex("IsReferredWhenYes")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c10.getString(c10.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningPE.put(j);

            }
            c10.close();
            ret_json.put("ChildrenScreeningPE", ChildrenScreeningPE);

            String Query11 = "Select * from ChildrenScreeningLocalTreatment where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c11 = db.rawQuery(Query11, null);
            JSONArray ChildrenScreeningLocalTreatment = new JSONArray();
            while (c11.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningLocalTreatmentID", c11.getString(c11.getColumnIndex("ChildrenScreeningLocalTreatmentID")));
                j.put("LocalChildrenScreeningLocalTreatmentID", c11.getString(c11.getColumnIndex("LocalChildrenScreeningLocalTreatmentID")));
                j.put("LocalChildrenScreeningID", c11.getString(c11.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", c11.getString(c11.getColumnIndex("ChildrenScreeningID")));
                j.put("Diagnosis", c11.getString(c11.getColumnIndex("Diagnosis")));
                j.put("MedicationGiven", c11.getString(c11.getColumnIndex("MedicationGiven")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c11.getString(c11.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningLocalTreatment.put(j);

            }
            c11.close();
            ret_json.put("ChildrenScreeningLocalTreatment", ChildrenScreeningLocalTreatment);

            String Query12 = "Select * from ChildrenScreeningAllergies where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c12 = db.rawQuery(Query12, null);
            JSONArray ChildrenScreeningAllergies = new JSONArray();
            while (c12.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningAllergyID", c12.getString(c12.getColumnIndex("ChildrenScreeningAllergyID")));
                j.put("LocalChildrenScreeningAllergyID", c12.getString(c12.getColumnIndex("LocalChildrenScreeningAllergyID")));
                j.put("LocalChildrenScreeningID", c12.getString(c12.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", c12.getString(c12.getColumnIndex("ChildrenScreeningID")));
                j.put("AllergyID", c12.getString(c12.getColumnIndex("AllergyID")));
                j.put("Comments", c12.getString(c12.getColumnIndex("Comments")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c12.getString(c12.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningAllergies.put(j);

            }
            c12.close();
            ret_json.put("ChildrenScreeningAllergies", ChildrenScreeningAllergies);

            String Query13 = "Select * from ChildScreeningFH where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c13 = db.rawQuery(Query13, null);
            JSONArray ChildrenScreeningFH = new JSONArray();
            while (c13.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenScreeningFHID", c13.getString(c13.getColumnIndex("ChildScreeningFHID")));
                j.put("LocalChildrenScreeningFHID", c13.getString(c13.getColumnIndex("LocalChildScreeningFHID")));
                j.put("LocalChildrenScreeningID", c13.getString(c13.getColumnIndex("LocalChildrenScreeningID")));
                j.put("ChildrenScreeningID", c13.getString(c13.getColumnIndex("ChildrenScreeningID")));
                j.put("FamilyHistoryID", c13.getString(c13.getColumnIndex("FamilyHistoryID")));
                j.put("Notes", c13.getString(c13.getColumnIndex("Notes")));
                j.put("FamilyMemberRelationID", c13.getString(c13.getColumnIndex("FamilyMemberRelationID")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c13.getString(c13.getColumnIndex("LastCommitedDate")));
                ChildrenScreeningFH.put(j);

            }
            c13.close();
            ret_json.put("ChildrenScreeningFH", ChildrenScreeningFH);

            String Query14 = "Select * from ChildrenDisabilities where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c14 = db.rawQuery(Query14, null);
            JSONArray ChildrenDisabilities = new JSONArray();
            while (c14.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenDisabilityID", c14.getString(c14.getColumnIndex("ChildrenDisabilityID")));
                j.put("LocalChildrenDisabilityID", c14.getString(c14.getColumnIndex("LocalChildrenDisabilityID")));
                j.put("LocalChildrenID", c14.getString(c14.getColumnIndex("LocalChildrenID")));
                j.put("ChildrenID", c14.getString(c14.getColumnIndex("ChildrenID")));
                j.put("PWDCardNumber", DBHelper.getColoumnFromDBonWhere(BaseActivity.this, "Children", "PWDCardNumber", "LocalChildrenID", c14.getString(c14.getColumnIndex("LocalChildrenID"))));
                j.put("DisabilityDetails", c14.getString(c14.getColumnIndex("DisabilityDetails")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c14.getString(c14.getColumnIndex("LastCommitedDate")));
                ChildrenDisabilities.put(j);

            }
            c14.close();
            ret_json.put("ChildrenDisabilities", ChildrenDisabilities);

            String Query15 = "Select * from ChildrenParents where isDeleted =0 and PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c15 = db.rawQuery(Query15, null);
            JSONArray ChildrenParents = new JSONArray();
            while (c15.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenParentID", c15.getString(c15.getColumnIndex("ChildrenParentID")));
                j.put("LocalChildrenParentID", c15.getString(c15.getColumnIndex("LocalChildrenParentID")));
                j.put("LocalChildrenID", c15.getString(c15.getColumnIndex("LocalChildrenID")));
                j.put("ChildrenID", c15.getString(c15.getColumnIndex("ChildrenID")));
                j.put("RelationID", c15.getString(c15.getColumnIndex("RelationID")));
                j.put("QualificationID", "");
                j.put("OccupationID", "");
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c15.getString(c15.getColumnIndex("LastCommitedDate")));
                ChildrenParents.put(j);

            }
            c15.close();
            ret_json.put("ChildrenParents", ChildrenParents);

            String Query16 = "Select * from ChildrenAllergiesHistory where  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c16 = db.rawQuery(Query16, null);
            JSONArray ChildrenAllergiesHistory = new JSONArray();
            while (c16.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenAllergyHistoryID", c16.getString(c16.getColumnIndex("ChildrenAllergiesHistoryID")));
                j.put("LocalChildrenID", c16.getString(c16.getColumnIndex("LocalChildrenID")));
                j.put("ChildrenID", c16.getString(c16.getColumnIndex("ChildrenID")));
                j.put("PlanningYear", c16.getString(c16.getColumnIndex("PlanningYear")));
                j.put("ScreeningRoundID", c16.getString(c16.getColumnIndex("ScreeningRoundID")));
                j.put("ScreeningRoundName", c16.getString(c16.getColumnIndex("ScreeningRoundName")));
                j.put("AllergyID", c16.getString(c16.getColumnIndex("AllergyID")));
                j.put("AllergyName", c16.getString(c16.getColumnIndex("AllergyName")));
                j.put("Comments", c16.getString(c16.getColumnIndex("Comments")));
                j.put("IsDeleted", c16.getString(c16.getColumnIndex("IsDeleted")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c16.getString(c16.getColumnIndex("LastCommitedDate")));
                ChildrenAllergiesHistory.put(j);

            }
            c16.close();
            ret_json.put("ChildrenAllergiesHistory", ChildrenAllergiesHistory);

            String Query17 = "Select * from ChildrenFamilyHistory where isDeleted = 0 and  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c17 = db.rawQuery(Query17, null);
            JSONArray ChildrenFamilyHistory = new JSONArray();
            while (c17.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenFamilyHistoryID", c17.getString(c17.getColumnIndex("ChildrenFamilyHistoryID")));
                j.put("LocalChildrenID", c17.getString(c17.getColumnIndex("LocalChildrenID")));
                j.put("ChildrenID", c17.getString(c17.getColumnIndex("ChildrenID")));
                j.put("PlanningYear", c17.getString(c17.getColumnIndex("PlanningYear")));
                j.put("ScreeningRoundID", c17.getString(c17.getColumnIndex("ScreeningRoundID")));
                j.put("ScreeningRoundName", c17.getString(c17.getColumnIndex("ScreeningRoundName")));
                j.put("FamilyHistoryID", c17.getString(c17.getColumnIndex("FamilyHistoryID")));
                j.put("FamilyHistoryName", c17.getString(c17.getColumnIndex("FamilyHistoryName")));
                j.put("HasHistory", c17.getString(c17.getColumnIndex("HasHistory")));
                j.put("FamilyMemberRelationID", c17.getString(c17.getColumnIndex("FamilyMemberRelationID")));
                j.put("FamilyMemberRelationName", c17.getString(c17.getColumnIndex("FamilyMemberRelationName")));
                j.put("Notes", c17.getString(c17.getColumnIndex("Notes")));
                j.put("Comments", c17.getString(c17.getColumnIndex("Comments")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c17.getString(c17.getColumnIndex("LastCommitedDate")));
                ChildrenFamilyHistory.put(j);

            }
            c17.close();
            ret_json.put("ChildrenFamilyHistory", ChildrenFamilyHistory);

            String Query18 = "Select * from ChildrenMedicalHistory where isDeleted = 0 and  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c18 = db.rawQuery(Query18, null);
            JSONArray ChildrenMedicalHistory = new JSONArray();
            while (c18.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenMedicalHistoryID", c18.getString(c18.getColumnIndex("ChildrenMedicalHistoryID")));
                j.put("LocalChildrenID", c18.getString(c18.getColumnIndex("LocalChildrenID")));
                j.put("ChildrenID", c18.getString(c18.getColumnIndex("ChildrenID")));
                j.put("PlanningYear", c18.getString(c18.getColumnIndex("PlanningYear")));
                j.put("ScreeningRoundID", c18.getString(c18.getColumnIndex("ScreeningRoundID")));
                j.put("ScreeningRoundName", c18.getString(c18.getColumnIndex("ScreeningRoundName")));
                j.put("Medications", c18.getString(c18.getColumnIndex("Medications")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c18.getString(c18.getColumnIndex("LastCommitedDate")));
                ChildrenMedicalHistory.put(j);

            }
            c18.close();
            ret_json.put("ChildrenMedicalHistory", ChildrenMedicalHistory);

            String Query19 = "Select * from ChildrenSurgicalsHistory where isDeleted = 0 and  PushStatus = 0 and LastCommitedDate > '" + Helper.syncDate + "'";
            Cursor c19 = db.rawQuery(Query19, null);
            JSONArray ChildrenSurgicalHistory = new JSONArray();
            while (c19.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenSurgicalsHistoryID", c19.getString(c19.getColumnIndex("ChildrenSurgicalsHistoryID")));
                j.put("LocalChildrenID", c19.getString(c19.getColumnIndex("LocalChildrenID")));
                j.put("ChildrenID", c19.getString(c19.getColumnIndex("ChildrenID")));
                j.put("PlanningYear", c19.getString(c19.getColumnIndex("PlanningYear")));
                j.put("ScreeningRoundID", c19.getString(c18.getColumnIndex("ScreeningRoundID")));
                j.put("ScreeningRoundName", c19.getString(c19.getColumnIndex("ScreeningRoundName")));
                j.put("SurgicalID", c19.getString(c19.getColumnIndex("SurgicalID")));
                j.put("SurgicalName", c19.getString(c19.getColumnIndex("SurgicalName")));
                j.put("Comments", c19.getString(c19.getColumnIndex("Comments")));
                j.put("UserID", userID);
                j.put("LastCommitedDateTime", c19.getString(c19.getColumnIndex("LastCommitedDate")));
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
