//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.LoginActivity;
import nunet.rbsk.login.UserLoginActivity;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
                String TokenID = sharedpreferences.getString("TokenID", "");


                //  webConn(UrlUtils.URL_INTITAL_SETUP, TokenID);
                try {
                    String string = "{\"ADDRESS\": [\n" +
                            "    {\n" +
                            "      \"AddressID\": 20836,\n" +
                            "      \"AddressName\": \"null\",\n" +
                            "      \"AddressLine1\": \"Uddandapuram\",\n" +
                            "      \"AddressLine2\": \"null\",\n" +
                            "      \"LandMark\": \"null\",\n" +
                            "      \"HabitatID\": 14085,\n" +
                            "      \"VillageID\": 7215,\n" +
                            "      \"PanchayatID\": 2914,\n" +
                            "      \"MandalID\": 108,\n" +
                            "      \"DistrictID\": 3,\n" +
                            "      \"StateID\": 1,\n" +
                            "      \"PINCode\": \"531126\",\n" +
                            "      \"Post\": \"null\",\n" +
                            "      \"DriveInstructions\": \"null\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"AddressID\": 20837,\n" +
                            "      \"AddressName\": \"null\",\n" +
                            "      \"AddressLine1\": \"Uddandapuram\",\n" +
                            "      \"AddressLine2\": \"null\",\n" +
                            "      \"LandMark\": \"null\",\n" +
                            "      \"HabitatID\": 14085,\n" +
                            "      \"VillageID\": 7215,\n" +
                            "      \"PanchayatID\": 2914,\n" +
                            "      \"MandalID\": 108,\n" +
                            "      \"DistrictID\": 3,\n" +
                            "      \"StateID\": 1,\n" +
                            "      \"PINCode\": \"531126\",\n" +
                            "      \"Post\": \"null\",\n" +
                            "      \"DriveInstructions\": \"null\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"AddressID\": 20822,\n" +
                            "      \"AddressName\": \"null\",\n" +
                            "      \"AddressLine1\": \"NELLIPUDI\",\n" +
                            "      \"AddressLine2\": \"null\",\n" +
                            "      \"LandMark\": \"null\",\n" +
                            "      \"HabitatID\": 14086,\n" +
                            "      \"VillageID\": 7216,\n" +
                            "      \"PanchayatID\": 2915,\n" +
                            "      \"MandalID\": 108,\n" +
                            "      \"DistrictID\": 3,\n" +
                            "      \"StateID\": 1,\n" +
                            "      \"PINCode\": \"531081\",\n" +
                            "      \"Post\": \"null\",\n" +
                            "      \"DriveInstructions\": \"null\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"AddressID\": 20804,\n" +
                            "      \"AddressName\": \"null\",\n" +
                            "      \"AddressLine1\": \"Gunupudi\",\n" +
                            "      \"AddressLine2\": \"null\",\n" +
                            "      \"LandMark\": \"null\",\n" +
                            "      \"HabitatID\": 14088,\n" +
                            "      \"VillageID\": 7218,\n" +
                            "      \"PanchayatID\": 2916,\n" +
                            "      \"MandalID\": 108,\n" +
                            "      \"DistrictID\": 3,\n" +
                            "      \"StateID\": 1,\n" +
                            "      \"PINCode\": \"531081\",\n" +
                            "      \"Post\": \"null\",\n" +
                            "      \"DriveInstructions\": \"null\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"AddressID\": 20803,\n" +
                            "      \"AddressName\": \"null\",\n" +
                            "      \"AddressLine1\": \"BANGARAMMAPETA\",\n" +
                            "      \"AddressLine2\": \"null\",\n" +
                            "      \"LandMark\": \"null\",\n" +
                            "      \"HabitatID\": 14089,\n" +
                            "      \"VillageID\": 7219,\n" +
                            "      \"PanchayatID\": 2917,\n" +
                            "      \"MandalID\": 108,\n" +
                            "      \"DistrictID\": 3,\n" +
                            "      \"StateID\": 1,\n" +
                            "      \"PINCode\": \"531081\",\n" +
                            "      \"Post\": \"null\",\n" +
                            "      \"DriveInstructions\": \"null\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"AddressID\": 20789,\n" +
                            "      \"AddressName\": \"null\",\n" +
                            "      \"AddressLine1\": \"Donivanilakshmipuram-II\",\n" +
                            "      \"AddressLine2\": \"null\",\n" +
                            "      \"LandMark\": \"null\",\n" +
                            "      \"HabitatID\": 14090,\n" +
                            "      \"VillageID\": 7220,\n" +
                            "      \"PanchayatID\": 2918,\n" +
                            "      \"MandalID\": 108,\n" +
                            "      \"DistrictID\": 3,\n" +
                            "      \"StateID\": 1,\n" +
                            "      \"PINCode\": \"531081\",\n" +
                            "      \"Post\": \"null\",\n" +
                            "      \"DriveInstructions\": \"null\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"AddressID\": 20790,\n" +
                            "      \"AddressName\": \"null\",\n" +
                            "      \"AddressLine1\": \"Donivanilakshmipuram-I\",\n" +
                            "      \"AddressLine2\": \"null\",\n" +
                            "      \"LandMark\": \"null\",\n" +
                            "      \"HabitatID\": 14090,\n" +
                            "      \"VillageID\": 7220,\n" +
                            "      \"PanchayatID\": 2918,\n" +
                            "      \"MandalID\": 108,\n" +
                            "      \"DistrictID\": 3,\n" +
                            "      \"StateID\": 1,\n" +
                            "      \"PINCode\": \"531081\",\n" +
                            "      \"Post\": \"null\",\n" +
                            "      \"DriveInstructions\": \"null\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"CHILDREN\": [\n" +
                            "    {\n" +
                            "      \"ChildrenID\": 525136,\n" +
                            "      \"MCTSID\": \"null\",\n" +
                            "      \"IdentificationMark1\": \"null\",\n" +
                            "      \"IdentificationMark2\": \"null\",\n" +
                            "      \"IdentificationMark3\": \"null\",\n" +
                            "      \"HasDisability\": \"null\",\n" +
                            "      \"PWDCardNumber\": \"null\",\n" +
                            "      \"StatusID\": \"null\",\n" +
                            "      \"ChildrenStatusID\": 1,\n" +
                            "      \"AdmissionDate\": \"2009-07-01\",\n" +
                            "      \"AdmissionNumber\": \"1298\",\n" +
                            "      \"ClassID\": 6,\n" +
                            "      \"SectionID\": 0,\n" +
                            "      \"RollNumber\": 0,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenID\": 525137,\n" +
                            "      \"MCTSID\": 0,\n" +
                            "      \"IdentificationMark1\": 0,\n" +
                            "      \"IdentificationMark2\": 0,\n" +
                            "      \"IdentificationMark3\": 0,\n" +
                            "      \"HasDisability\": 0,\n" +
                            "      \"PWDCardNumber\": 0,\n" +
                            "      \"StatusID\": \"null\",\n" +
                            "      \"ChildrenStatusID\": 1,\n" +
                            "      \"AdmissionDate\": \"2009-10-05\",\n" +
                            "      \"AdmissionNumber\": \"1302\",\n" +
                            "      \"ClassID\": 6,\n" +
                            "      \"SectionID\": 0,\n" +
                            "      \"RollNumber\": 0,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenID\": 525135,\n" +
                            "      \"MCTSID\": 0,\n" +
                            "      \"IdentificationMark1\": \"null\",\n" +
                            "      \"IdentificationMark2\": \"null\",\n" +
                            "      \"IdentificationMark3\": 0,\n" +
                            "      \"HasDisability\": \"null\",\n" +
                            "      \"PWDCardNumber\": \"null\",\n" +
                            "      \"StatusID\": 0,\n" +
                            "      \"ChildrenStatusID\": 1,\n" +
                            "      \"AdmissionDate\": \"2009-06-12\",\n" +
                            "      \"AdmissionNumber\": 1295,\n" +
                            "      \"ClassID\": 6,\n" +
                            "      \"SectionID\": 0,\n" +
                            "      \"RollNumber\": 0,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"CHILDRENPICTURES\": [\n" +
                            "    {\n" +
                            "      \"ChildrenPictureID\": 2,\n" +
                            "      \"PictureName\": \"7233321Chrysanthemum130120161054.jpg\",\n" +
                            "      \"PictureTags\": 0,\n" +
                            "      \"Path\": \"/Pictures/Children/7233321/201601/7233321Chrysanthemum130120161054.jpg\",\n" +
                            "      \"IsProfilePicture\": false,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenPictureID\": 3,\n" +
                            "      \"PictureName\": \"7233321Tulips130120161054.jpg\",\n" +
                            "      \"PictureTags\": 0,\n" +
                            "      \"Path\": \"/Pictures/Children/7233321/201601/7233321Tulips130120161054.jpg\",\n" +
                            "      \"IsProfilePicture\": true,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENING\": [\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 4,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 4,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 4,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreenStatusID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGALLERGIES\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGHISTORY\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGINVESTIGATIONS\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGLOCALTREATMENT\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGPE\": [\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningPEID\": 33,\n" +
                            "      \"ScreeningQuestionID\": 110,\n" +
                            "      \"QuestionID\": \"null\",\n" +
                            "      \"Question\": \"null\",\n" +
                            "      \"IsNegaiveQuestion\": \"null\",\n" +
                            "      \"Order\": \"null\",\n" +
                            "      \"Answer\": \"null\",\n" +
                            "      \"IsReferredWhenYes\": true,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningPEID\": 33,\n" +
                            "      \"ScreeningQuestionID\": 110,\n" +
                            "      \"QuestionID\": \"null\",\n" +
                            "      \"Question\": \"null\",\n" +
                            "      \"IsNegaiveQuestion\": \"null\",\n" +
                            "      \"Order\": \"null\",\n" +
                            "      \"Answer\": \"null\",\n" +
                            "      \"IsReferredWhenYes\": true,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"CHILDRENFAMILYHISTORY\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENMEDICALHISTORY\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENPARENTS\": [\n" +
                            "    {\n" +
                            "      \"ChildrenParentID\": 4,\n" +
                            "      \"RelationID\": 1,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenParentID\": 5,\n" +
                            "      \"RelationID\": 2,\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenParentID\": 6,\n" +
                            "      \"RelationID\": 10,\n" +
                            "      \"IsDeleted\":false\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"CHILDRENALLERGIESHISTORY\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGPICTURES\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGRECOMMENDATIONS\": [\n" +
                            "    \n" +
                            "  ],\n" +
                            "  \"CHILDRENSCREENINGREFERRALS\": [\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 33,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 200,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 10318,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 414,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 410,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 30,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 10313,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 10310,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 382,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 346,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 383,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 385,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ChildrenScreeningReferralID\": 10558,\n" +
                            "      \"WasReferred\": true,\n" +
                            "      \"ReferredFacilityID\": 11,\n" +
                            "      \"Comments\": \"\",\n" +
                            "      \"IsDeleted\": false\n" +
                            "    }],\n" +
                            "    \"CHILDRENSCREENINGSURGICALS\": [\n" +
                            "      \n" +
                            "    ],\n" +
                            "    \"CHILDRENSCREENINGVITALS\": [\n" +
                            "      {\n" +
                            "        \"ChildrenScreeningVitalsID\": 170473,\n" +
                            "        \"ChildrenScreeningID\": 66388,\n" +
                            "        \"Height\": 115.00,\n" +
                            "        \"HeightIndication\": 0,\n" +
                            "        \"Weight\": 14.00,\n" +
                            "        \"WeightIndication\": 0,\n" +
                            "        \"BMI\": 0,\n" +
                            "        \"BMIIndication\": 0,\n" +
                            "        \"AcuityOfVisionLefteye\": 0,\n" +
                            "        \"AcuityOfVisionRighteye\": 0,\n" +
                            "        \"BP\": 0,\n" +
                            "        \"BPIndication\": 0,\n" +
                            "        \"BloodGroupID\": 0,\n" +
                            "        \"BloodGroupNotes\": 0,\n" +
                            "        \"TemperatureID\": 0,\n" +
                            "        \"TemperatureIndication\": \"98.7\",\n" +
                            "        \"HemoGlobinID\": 0,\n" +
                            "        \"HemoGlobinIndication\": 0,\n" +
                            "        \"MUACINCms\": 0,\n" +
                            "        \"MUACIndication\": 0,\n" +
                            "        \"HeadCircumferenceInCms\": 0,\n" +
                            "        \"HeadCircumferenceIndication\": 0,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"ChildrenScreeningVitalsID\": 170473,\n" +
                            "        \"ChildrenScreeningID\": 66388,\n" +
                            "        \"Height\": 115.00,\n" +
                            "        \"HeightIndication\": 0,\n" +
                            "        \"Weight\": 14.00,\n" +
                            "        \"WeightIndication\": 0,\n" +
                            "        \"BMI\": 0,\n" +
                            "        \"BMIIndication\": 0,\n" +
                            "        \"AcuityOfVisionLefteye\": 0,\n" +
                            "        \"AcuityOfVisionRighteye\": 0,\n" +
                            "        \"BP\": 0,\n" +
                            "        \"BPIndication\": 0,\n" +
                            "        \"BloodGroupID\": 0,\n" +
                            "        \"BloodGroupNotes\": 0,\n" +
                            "        \"TemperatureID\": 0,\n" +
                            "        \"TemperatureIndication\": \"98.7\",\n" +
                            "        \"HemoGlobinID\": 0,\n" +
                            "        \"HemoGlobinIndication\": 0,\n" +
                            "        \"MUACIncms\": 0,\n" +
                            "        \"MUACIndication\": 0,\n" +
                            "        \"HeadCircumferenceInCms\": \"null\",\n" +
                            "        \"HeadCircumferenceIndication\": \"null\",\n" +
                            "        \"IsDeleted\": false\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"INSTITUTESCREENING\": [\n" +
                            "      {\n" +
                            "        \"InstituteScreeningID\": 241,\n" +
                            "        \"RBSKCalendarYearID\":2 ,\"ScreeningRoundID\":2,\"InstituteID\":33267,\"IsDeleted\":false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteScreeningID\": 244,\n" +
                            "        \"RBSKCalendarYearID\": 2,\n" +
                            "        \"ScreeningRoundID\": 2,\n" +
                            "        \"InstituteID\": 33271,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteScreeningID\": 245,\n" +
                            "        \"RBSKCalendarYearID\": 2,\n" +
                            "        \"ScreeningRoundID\": 2,\n" +
                            "        \"InstituteID\": 33235,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteScreeningID\": 246,\n" +
                            "        \"RBSKCalendarYearID\": 2,\n" +
                            "        \"ScreeningRoundID\": 2,\n" +
                            "        \"InstituteID\": 33234,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteScreeningID\": 247,\n" +
                            "        \"RBSKCalendarYearID\": 2,\n" +
                            "        \"ScreeningRoundID\": 2,\n" +
                            "        \"InstituteID\": 27621,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"INSTITUTESCREENINGDETAILS\": [\n" +
                            "      {\n" +
                            "        \"InstituteScreeningDetailID\": 1151,\n" +
                            "        \"CalendarDate\": \"2015-02-06T00: 00: 00\",\n" +
                            "        \"ScreenStatusID\": 1,\n" +
                            "        \"ScreeningStartDateTime\": \"2015-02-06T00: 00: 00\",\n" +
                            "        \"ScreeningEndDateTime\":\"\", \"IsDeleted\": false\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"INSTITUTEPLANS\": [\n" +
                            "      {\n" +
                            "        \"InstitutePlanID\": 2453,\n" +
                            "        \"InstituteID\": 73582,\n" +
                            "        \"RBSKCalendarYearID\": 1,\n" +
                            "        \"ScreeningRoundID\": 1,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstitutePlanID\": 2444,\n" +
                            "        \"InstituteID\": 60827,\n" +
                            "        \"RBSKCalendarYearID\": 1,\n" +
                            "        \"ScreeningRoundID\": 1,\n" +
                            "         \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstitutePlanID\": 2443,\n" +
                            "        \"InstituteID\": 60826,\n" +
                            "        \"RBSKCalendarYearID\": 1,\n" +
                            "        \"ScreeningRoundID\": 1,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstitutePlanID\": 2446,\n" +
                            "        \"InstituteID\": 60862,\n" +
                            "        \"RBSKCalendarYearID\": 1,\n" +
                            "        \"ScreeningRoundID\": 1,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstitutePlanID\": 2445,\n" +
                            "        \"InstituteID\": 60848,\n" +
                            "        \"RBSKCalendarYearID\": 1,\n" +
                            "        \"ScreeningRoundID\": 1,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstitutePlanID\": 2102,\n" +
                            "        \"InstituteID\": 42209,\n" +
                            "        \"RBSKCalendarYearID\": 1,\n" +
                            "        \"ScreeningRoundID\": 1,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstitutePlanID\": 1553,\n" +
                            "        \"InstituteID\": 33265,\n" +
                            "        \"RBSKCalendarYearID\": 1,\n" +
                            "        \"ScreeningRoundID\": 1,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"INSTITUTESTAFF\": [\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 10,\n" +
                            "        \"DepartmentLevelRoleID\": 51,\n" +
                            "\"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 11,\n" +
                            "        \"DepartmentLevelRoleID\": \"null\",\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 12,\n" +
                            "        \"DepartmentLevelRoleID\": \"null\",\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 13,\n" +
                            "        \"DepartmentLevelRoleID\": 0,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 14,\n" +
                            "        \"DepartmentLevelRoleID\": 0,\n" +
                            "       \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 15,\n" +
                            "        \"DepartmentLevelRoleID\": \"null\",\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 28,\n" +
                            "        \"DepartmentLevelRoleID\": \"null\",\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 29,\n" +
                            "        \"DepartmentLevelRoleID\": 0,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 22,\n" +
                            "        \"DepartmentLevelRoleID\": \"null\",\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 23,\n" +
                            "        \"DepartmentLevelRoleID\": \"null\",\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"InstituteStaffID\": 24,\n" +
                            "        \"DepartmentLevelRoleID\": 0,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"USERS\": [\n" +
                            "      {\n" +
                            "        \"UserID\": 77,\n" +
                            "        \"UserTypeID\": 3,\n" +
                            "        \"FirstName\": \"Harikrishna\",\n" +
                            "        \"MiddleName\": \"G\",\n" +
                            "        \"LastName\": \"DR\",\n" +
                            "        \"GenderID\": 1,\n" +
                            "        \"DateOfBirth\": \"null\",\n" +
                            "        \"EmployeeID\": \"\",\n" +
                            "        \"DesignationID\": 13,\n" +
                            "        \"DepartmentID\": 1,\n" +
                            "        \"AadharNumber\": \"\",\n" +
                            "        \"HealthCardNumber\": 0,\n" +
                            "        \"RationCardNumber\": \"null\",\n" +
                            "        \"SalutationID\": \"null\",\n" +
                            "        \"NationalityID\": \"null\",\n" +
                            "        \"LanguageID\": 0,\n" +
                            "        \"CasteID\": 0,\n" +
                            "        \"ReligionID\": 0,\n" +
                            "        \"Age\": 0,\n" +
                            "        \"StatusID\": 2,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"UserID\": 78,\n" +
                            "        \"UserTypeID\": 3,\n" +
                            "        \"FirstName\": \"Dr.M.RamaLakshmi\",\n" +
                            "        \"MiddleName\": 0,\n" +
                            "        \"LastName\": \"\",\n" +
                            "        \"GenderID\": 2,\n" +
                            "        \"DateOfBirth\": \"null\",\n" +
                            "        \"EmployeeID\": \"null\",\n" +
                            "        \"DesignationID\": 13,\n" +
                            "        \"DepartmentID\": 1,\n" +
                            "        \"AadharNumber\": 0,\n" +
                            "        \"HealthCardNumber\": \"null\",\n" +
                            "        \"RationCardNumber\": \"null\",\n" +
                            "        \"SalutationID\": \"null\",\n" +
                            "        \"NationalityID\": \"null\",\n" +
                            "        \"LanguageID\": \"null\",\n" +
                            "        \"CasteID\": 0,\n" +
                            "        \"ReligionID\": 0,\n" +
                            "        \"Age\": 0,\n" +
                            "        \"StatusID\": 2,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"UserID\": 79,\n" +
                            "        \"UserTypeID\": 3,\n" +
                            "        \"FirstName\": \"K.Bhavani\",\n" +
                            "        \"MiddleName\": 0,\n" +
                            "        \"LastName\": \"\",\n" +
                            "        \"GenderID\": 2,\n" +
                            "        \"DateOfBirth\": \"null\",\n" +
                            "        \"EmployeeID\": \"null\",\n" +
                            "        \"DesignationID\": 18,\n" +
                            "        \"DepartmentID\": 1,\n" +
                            "        \"AadharNumber\": \"null\",\n" +
                            "        \"HealthCardNumber\": 0,\n" +
                            "        \"RationCardNumber\": 0,\n" +
                            "        \"SalutationID\": \"null\",\n" +
                            "        \"NationalityID\": \"null\",\n" +
                            "        \"LanguageID\": \"null\",\n" +
                            "        \"CasteID\": \"null\",\n" +
                            "        \"ReligionID\": 0,\n" +
                            "        \"Age\": 0,\n" +
                            "        \"StatusID\": 2,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"UserID\": 80,\n" +
                            "        \"UserTypeID\": 3,\n" +
                            "        \"FirstName\": \"CH.Reetaratnam\",\n" +
                            "        \"MiddleName\": 0,\n" +
                            "        \"LastName\": \"\",\n" +
                            "        \"GenderID\": 1,\n" +
                            "        \"DateOfBirth\": \"null\",\n" +
                            "        \"EmployeeID\": \"null\",\n" +
                            "        \"DesignationID\": 14,\n" +
                            "        \"DepartmentID\": 1,\n" +
                            "        \"AadharNumber\": \"null\",\n" +
                            "        \"HealthCardNumber\": 0,\n" +
                            "        \"RationCardNumber\": 0,\n" +
                            "        \"SalutationID\": \"null\",\n" +
                            "        \"NationalityID\": \"null\",\n" +
                            "        \"LanguageID\": \"null\",\n" +
                            "        \"CasteID\": \"null\",\n" +
                            "        \"ReligionID\": 0,\n" +
                            "        \"Age\": \"null\",\n" +
                            "        \"StatusID\": 2,\n" +
                            "        \"IsDeleted\": false\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }";

                    JSONObject mJsonObject = new JSONObject(string);
                    JSONArray ADDRESS = mJsonObject.getJSONArray("ADDRESS");
                    JSONArray CHILDREN = mJsonObject.getJSONArray("CHILDREN");
                    JSONArray CHILDRENPICTURES = mJsonObject.getJSONArray("CHILDRENPICTURES");
                    JSONArray CHILDRENSCREENING = mJsonObject.getJSONArray("CHILDRENSCREENING");
                    JSONArray CHILDRENSCREENINGALLERGIES = mJsonObject.getJSONArray("CHILDRENSCREENINGALLERGIES");
                    JSONArray CHILDRENSCREENINGHISTORY = mJsonObject.getJSONArray("CHILDRENSCREENINGHISTORY");
                    JSONArray CHILDRENSCREENINGINVESTIGATIONS = mJsonObject.getJSONArray("CHILDRENSCREENINGINVESTIGATIONS");
                    JSONArray CHILDRENSCREENINGLOCALTREATMENT = mJsonObject.getJSONArray("CHILDRENSCREENINGLOCALTREATMENT");
                    JSONArray CHILDRENSCREENINGPE = mJsonObject.getJSONArray("CHILDRENSCREENINGPE");
                    JSONArray CHILDRENFAMILYHISTORY = mJsonObject.getJSONArray("CHILDRENFAMILYHISTORY");
                    JSONArray CHILDRENMEDICALHISTORY = mJsonObject.getJSONArray("CHILDRENMEDICALHISTORY");
                    JSONArray CHILDRENPARENTS = mJsonObject.getJSONArray("CHILDRENPARENTS");
                    JSONArray CHILDRENALLERGIESHISTORY = mJsonObject.getJSONArray("CHILDRENALLERGIESHISTORY");
                    JSONArray CHILDRENSCREENINGPICTURES = mJsonObject.getJSONArray("CHILDRENSCREENINGPICTURES");
                    JSONArray CHILDRENSCREENINGRECOMMENDATIONS = mJsonObject.getJSONArray("CHILDRENSCREENINGRECOMMENDATIONS");
                    JSONArray CHILDRENSCREENINGREFERRALS = mJsonObject.getJSONArray("CHILDRENSCREENINGREFERRALS");
                    JSONArray INSTITUTESTAFF = mJsonObject.getJSONArray("INSTITUTESTAFF");
                    JSONArray INSTITUTESCREENING = mJsonObject.getJSONArray("INSTITUTESCREENING");
                    JSONArray INSTITUTESCREENINGDETAILS = mJsonObject.getJSONArray("INSTITUTESCREENINGDETAILS");
                    JSONArray INSTITUTEPLANS = mJsonObject.getJSONArray("INSTITUTEPLANS");
                    JSONArray USERS = mJsonObject.getJSONArray("USERS");
                    dbHelper.bulkinsertintoTable(Register_download.this, "ADDRESS", ADDRESS);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDREN", CHILDREN);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENPICTURES", CHILDRENPICTURES);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENING", CHILDRENSCREENING);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGALLERGIES", CHILDRENSCREENINGALLERGIES);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGHISTORY", CHILDRENSCREENINGHISTORY);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGINVESTIGATIONS", CHILDRENSCREENINGINVESTIGATIONS);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGLOCALTREATMENT", CHILDRENSCREENINGLOCALTREATMENT);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGPE", CHILDRENSCREENINGPE);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENFAMILYHISTORY", CHILDRENFAMILYHISTORY);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENMEDICALHISTORY", CHILDRENMEDICALHISTORY);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENPARENTS", CHILDRENPARENTS);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENALLERGIESHISTORY", CHILDRENALLERGIESHISTORY);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGPICTURES", CHILDRENSCREENINGPICTURES);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGRECOMMENDATIONS", CHILDRENSCREENINGRECOMMENDATIONS);
                    dbHelper.bulkinsertintoTable(Register_download.this, "CHILDRENSCREENINGREFERRALS", CHILDRENSCREENINGREFERRALS);
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESTAFF", INSTITUTESTAFF);
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESCREENING", INSTITUTESCREENING);
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTESCREENINGDETAILS", INSTITUTESCREENINGDETAILS);
                    dbHelper.bulkinsertintoTable(Register_download.this, "INSTITUTEPLANS", INSTITUTEPLANS);
                    dbHelper.bulkinsertintoTable(Register_download.this, "USERS", USERS);
                    sharedpreferences = getSharedPreferences(
                            UserLoginActivity.UserLogin, Context.MODE_PRIVATE);

                    sharedpreferences.edit().putString("DB", "Yes").commit();
                    btn_register_continue.setVisibility(View.VISIBLE);
                    btn_register_checkSetupStatus.setVisibility(View.VISIBLE);
                    v.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
        System.out.println("Sending String :" + str);
        final ProgressDialog progDailog = ProgressDialog.show(this,
                "Please Wait...", "Setting up Device...", true);
        new Thread() {
            public void run() {
                try {
                    strResponse = postData(url + str);
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
                JSONObject mJsonObject = new JSONObject(strResponse);
                String mData = mJsonObject.getString("Data");
                if (mData.equalsIgnoreCase("0")
                        || mData.equalsIgnoreCase("-1")) {
                    Helper.showShortToast(Register_download.this,
                            "Device is not identified by Server");
                } else {
                    JSONArray ADDRESS = mJsonObject.getJSONArray("ADDRESS");
                    DBHelper dbHelper = new DBHelper(Register_download.this);
                    dbHelper.bulkinsertintoTable(Register_download.this, "ADDRESS", ADDRESS);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    };

}
