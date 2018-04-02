//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.LoginActivity;
import nunet.rbsk.login.UserLoginActivity;
import nunet.services.IncrementalService;

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
    public String strResponse = "";
    DBHelper dbHelper;

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
                String TokenID = sharedpreferences.getString("DeviceCode", "");
                Intent serviceIntent = new Intent(
                        Register_download.this, IncrementalService.class);
                //stopService(serviceIntent);
                startService(serviceIntent);

                //webConn(UrlUtils.URL_INTITAL_SETUP, TokenID);

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


    // -------------------GET METHOD CALL-----------------
    private void webConn(final String url, final String str) {
        System.out.println("Sending URL :" + url);
        System.out.println("Sending String :" + str + File.separator + "20000101000000");
        final ProgressDialog progDailog = ProgressDialog.show(this,
                "Please Wait...", "Setting up Device...", true);
        new Thread() {
            public void run() {
                try {
                    // strResponse = postData(url + str + File.separator + Helper.getTodayDateTime1());
                    strResponse = postData(url + str + File.separator + "20000101000000");
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progDailog.dismiss();
            }
        }.start();
    }

    public String postData(String s_url) {
        // Create a new HttpClient and get Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(s_url);
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
            strResponse = sb.toString();
        } catch (ClientProtocolException e) {
            strResponse = "ClientProtocolException";
        } catch (IOException e) {
            strResponse = "There is no network";
        } catch (Exception e) {
            strResponse = "Exception";
        }
        return strResponse;
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            System.out.println("Response String :" + strResponse);

            try {
                if (strResponse.indexOf("{") != -1) {
                    strResponse = strResponse.replace("\\", "");
                    strResponse = strResponse.substring(1, strResponse.length() - 1);
                    JSONObject mJsonObject = new JSONObject(strResponse);
                    if (mJsonObject.has("ADDRESS")) {
                        JSONArray ADDRESS = mJsonObject.getJSONArray("ADDRESS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "ADDRESS", ADDRESS);
                    }

                    if (mJsonObject.has("USERS")) {
                        JSONArray USERS = mJsonObject.getJSONArray("USERS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "USERS", USERS);
                    }
                    if (mJsonObject.has("INSTITUTES")) {
                        JSONArray INSTITUTES = mJsonObject.getJSONArray("INSTITUTES");
                        dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTES", INSTITUTES);

                    }
                    if (mJsonObject.has("INSTITUTEPICTURES")) {
                        JSONArray INSTITUTEPICTURES = mJsonObject.getJSONArray("INSTITUTEPICTURES");
                        dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTEPICTURES", INSTITUTEPICTURES);
                    }
                    if (mJsonObject.has("INSTITUTEPLANS")) {
                        JSONArray INSTITUTEPLANS = mJsonObject.getJSONArray("INSTITUTEPLANS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTEPLANS", INSTITUTEPLANS);
                    }

                    if (mJsonObject.has("INSTITUTEPLANDETAILS")) {
                        JSONArray INSTITUTEPLANDETAILS = mJsonObject.getJSONArray("INSTITUTEPLANDETAILS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTEPLANDETAILS", INSTITUTEPLANDETAILS);
                    }

                    if (mJsonObject.has("INSTITUTESTAFF")) {
                        JSONArray INSTITUTESTAFF = mJsonObject.getJSONArray("INSTITUTESTAFF");
                        dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESTAFF", INSTITUTESTAFF);
                    }
                    if (mJsonObject.has("INSTITUTESCREENING")) {
                        JSONArray INSTITUTESCREENING = mJsonObject.getJSONArray("INSTITUTESCREENING");
                        dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESCREENING", INSTITUTESCREENING);
                    }
                    if (mJsonObject.has("INSTITUTESCREENINGDETAILS")) {
                        JSONArray INSTITUTESCREENINGDETAILS = mJsonObject.getJSONArray("INSTITUTESCREENINGDETAILS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESCREENINGDETAILS", INSTITUTESCREENINGDETAILS);
                    }

                    if (mJsonObject.has("CHILDREN")) {
                        JSONArray CHILDREN = mJsonObject.getJSONArray("CHILDREN");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDREN", CHILDREN);
                    }
                    if (mJsonObject.has("CHILDRENSCREENING")) {
                        JSONArray CHILDRENSCREENING = mJsonObject.getJSONArray("CHILDRENSCREENING");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENING", CHILDRENSCREENING);
                    }
                    if (mJsonObject.has("CHILDRENSCREENINGREFERRALS")) {
                        JSONArray CHILDRENSCREENINGREFERRALS = mJsonObject.getJSONArray("CHILDRENSCREENINGREFERRALS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGREFERRALS", CHILDRENSCREENINGREFERRALS);
                    }
                    if (mJsonObject.has("CHILDRENPICTURES")) {
                        JSONArray CHILDRENPICTURES = mJsonObject.getJSONArray("CHILDRENPICTURES");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENPICTURES", CHILDRENPICTURES);
                    }

                    if (mJsonObject.has("CHILDRENSCREENINGALLERGIES")) {
                        JSONArray CHILDRENSCREENINGALLERGIES = mJsonObject.getJSONArray("CHILDRENSCREENINGALLERGIES");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGALLERGIES", CHILDRENSCREENINGALLERGIES);
                    }
                    if (mJsonObject.has("CHILDRENSCREENINGHISTORY")) {
                        JSONArray CHILDRENSCREENINGHISTORY = mJsonObject.getJSONArray("CHILDRENSCREENINGHISTORY");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGHISTORY", CHILDRENSCREENINGHISTORY);
                    }
                    if (mJsonObject.has("CHILDRENSCREENINGINVESTIGATIONS")) {
                        JSONArray CHILDRENSCREENINGINVESTIGATIONS = mJsonObject.getJSONArray("CHILDRENSCREENINGINVESTIGATIONS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGINVESTIGATIONS", CHILDRENSCREENINGINVESTIGATIONS);
                    }
                    if (mJsonObject.has("CHILDRENSCREENINGLOCALTREATMENT")) {
                        JSONArray CHILDRENSCREENINGLOCALTREATMENT = mJsonObject.getJSONArray("CHILDRENSCREENINGLOCALTREATMENT");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGLOCALTREATMENT", CHILDRENSCREENINGLOCALTREATMENT);
                    }
                    if (mJsonObject.has("CHILDRENSCREENINGPE")) {
                        JSONArray CHILDRENSCREENINGPE = mJsonObject.getJSONArray("CHILDRENSCREENINGPE");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGPE", CHILDRENSCREENINGPE);
                    }
                    if (mJsonObject.has("CHILDRENFAMILYHISTORY")) {
                        JSONArray CHILDRENFAMILYHISTORY = mJsonObject.getJSONArray("CHILDRENFAMILYHISTORY");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENFAMILYHISTORY", CHILDRENFAMILYHISTORY);
                    }
                    if (mJsonObject.has("CHILDRENMEDICALHISTORY")) {
                        JSONArray CHILDRENMEDICALHISTORY = mJsonObject.getJSONArray("CHILDRENMEDICALHISTORY");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENMEDICALHISTORY", CHILDRENMEDICALHISTORY);
                    }
                    if (mJsonObject.has("CHILDRENPARENTS")) {
                        JSONArray CHILDRENPARENTS = mJsonObject.getJSONArray("CHILDRENPARENTS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENPARENTS", CHILDRENPARENTS);
                    }
                    if (mJsonObject.has("CHILDRENALLERGIESHISTORY")) {
                        JSONArray CHILDRENALLERGIESHISTORY = mJsonObject.getJSONArray("CHILDRENALLERGIESHISTORY");
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
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENDISABILITIES", CHILDRENDISABILITIES);
                    }
                    if (mJsonObject.has("CHILDRENSCREENINGSURGICALS")) {
                        JSONArray CHILDRENSCREENINGSURGICALS = mJsonObject.getJSONArray("CHILDRENSCREENINGSURGICALS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGSURGICALS", CHILDRENSCREENINGSURGICALS);
                    }
                    if (mJsonObject.has("CHILDRENSCREENINGVITALS")) {
                        JSONArray CHILDRENSCREENINGVITALS = mJsonObject.getJSONArray("CHILDRENSCREENINGVITALS");
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
                        dbHelper.bulkinsertintoTable(Register_download.this, "CHILDSCREENINGFH", CHILDSCREENINGFH);
                    }

                    if (mJsonObject.has("CONTACTS")) {
                        JSONArray CONTACTS = mJsonObject.getJSONArray("CONTACTS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CONTACTS", CONTACTS);
                    }
                    if (mJsonObject.has("CONTACTDETAILS")) {
                        JSONArray CONTACTDETAILS = mJsonObject.getJSONArray("CONTACTDETAILS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "CONTACTDETAILS", CONTACTDETAILS);
                    }
                    if (mJsonObject.has("DEVICESETTINGS")) {
                        JSONArray DEVICESETTINGS = mJsonObject.getJSONArray("DEVICESETTINGS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "DEVICESETTINGS", DEVICESETTINGS);
                    }
                    if (mJsonObject.has("FACILITIES")) {
                        JSONArray FACILITIES = mJsonObject.getJSONArray("FACILITIES");
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
                        dbHelper.bulkinsertintoTable(Register_download.this, "MHTSTAFF", MHTSTAFF);
                    }
                    if (mJsonObject.has("MOBILEHEALTHTEAMS")) {
                        JSONArray MOBILEHEALTHTEAMS = mJsonObject.getJSONArray("MOBILEHEALTHTEAMS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "MOBILEHEALTHTEAMS", MOBILEHEALTHTEAMS);
                    }
                    if (mJsonObject.has("USERCREDENTIALS")) {
                        JSONArray USERCREDENTIALS = mJsonObject.getJSONArray("USERCREDENTIALS");
                        dbHelper.bulkinsertintoTable(Register_download.this, "USERCREDENTIALS", USERCREDENTIALS);
                    }
                    if (mJsonObject.has("VILLAGES")) {
                        JSONArray VILLAGES = mJsonObject.getJSONArray("VILLAGES");
                        dbHelper.bulkinsertintoTable(Register_download.this, "VILLAGES", VILLAGES);
                    }

                    SharedPreferences sharedpreferences = getSharedPreferences(
                            "LoginMain", Context.MODE_PRIVATE);
                    sharedpreferences = getSharedPreferences(
                            UserLoginActivity.UserLogin, Context.MODE_PRIVATE);

                    sharedpreferences.edit().putString("DB", "Yes").commit();
                    btn_register_continue.setVisibility(View.VISIBLE);
                    btn_register_checkSetupStatus.setVisibility(View.VISIBLE);
                    btn_register_setup.setVisibility(View.GONE);


                } else {
                    Helper.showShortToast(Register_download.this,
                            strResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    };

}
