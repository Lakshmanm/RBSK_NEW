//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.nunet.wsutil.UrlUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nunet.rbsk.R;
import nunet.rbsk.Register_download;
import nunet.rbsk.helpers.Helper;

//*****************************************************************************
//* Name   :  Register.java

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
public class RegisterActivity extends Activity implements OnClickListener {
    private EditText et_register_deviceCode, et_register_unlockId,
            et_register_unlockPassword, et_register_healthBlockId;
    private Button btn_register;

    private int navigationIndex = 0;
    private String strResponse = "";
    private String deviceId = "", tokenId = "";

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        findViews();
        et_register_deviceCode.setText("");
        et_register_unlockId.setText("");
        et_register_unlockPassword.setText("");
        et_register_healthBlockId.setText("");

    }

    /**
     *
     */
    private void findViews() {
        et_register_deviceCode = (EditText) findViewById(R.id.et_register_deviceCode);
        et_register_unlockId = (EditText) findViewById(R.id.et_register_unlockId);
        et_register_unlockPassword = (EditText) findViewById(R.id.et_register_unlockPassword);
        et_register_healthBlockId = (EditText) findViewById(R.id.et_register_healthBlockId);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            // {devicecode}/{unlockid}/{unlockpassword}/{health block id}

            String registerStr = et_register_deviceCode.getText().toString()
                    .trim()
                    + "/"
                    + et_register_unlockId.getText().toString().trim()
                    + "/"
                    + et_register_unlockPassword.getText().toString().trim()
                    + "/"
                    + et_register_healthBlockId.getText().toString().trim();
            navigationIndex = 1;
            webConn(UrlUtils.URL_Register, registerStr);
        }
    }

    // -------------------GET METHOD CALL-----------------
    private void webConn(final String url, final String str) {
        System.out.println("Sending URL :" + url);
        System.out.println("Sending String :" + str);
        Helper.showProgressDialog(RegisterActivity.this);
        new Thread() {
            public void run() {
                try {
                    strResponse = postData(url + str);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Helper.progressDialog.dismiss();
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
            if (navigationIndex == 1) {
                try {
                    if (strResponse.trim().indexOf("{") != -1) {
                        JSONObject mJsonObject = new JSONObject(strResponse);
                        String mData = mJsonObject.getString("Data");
                        if (mData.equalsIgnoreCase("0")
                                || mData.equalsIgnoreCase("-1")) {
                            Helper.showShortToast(RegisterActivity.this,
                                    "Device is not identified by Server");
                        } else {
                            // {deviceID}/{unlockid}/{unlockpassword}
                            deviceId = mData;
                            navigationIndex = 2;
                            String helloStr = deviceId
                                    + "/"
                                    + et_register_unlockId.getText().toString()
                                    .trim()
                                    + "/"
                                    + et_register_unlockPassword.getText()
                                    .toString().trim();
                            webConn(UrlUtils.URL_Hello, helloStr);
                        }
                    } else {
                        Helper.showShortToast(RegisterActivity.this,
                                strResponse);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (navigationIndex == 2) {
                JSONObject mJsonObject;
                try {
                    if (strResponse.trim().indexOf("{") != -1) {
                        mJsonObject = new JSONObject(strResponse);
                        String mData = mJsonObject.getString("Data");
                        if (mData.equalsIgnoreCase("-1")
                                || mData.equalsIgnoreCase("0")) {
                            Helper.showShortToast(RegisterActivity.this,
                                    "Device is not identified by Server");
                        } else {
                            tokenId = mData;
                            // Helper.showShortToast(Register.this, "tokenId" +
                            // tokenId);
                            SharedPreferences sharedpreferences = getSharedPreferences(
                                    "LoginMain", Context.MODE_PRIVATE);
                            Editor editor = sharedpreferences.edit();
                            editor.putString("DeviceCode", et_register_deviceCode
                                    .getText().toString().trim());
                            editor.putString("DeviceID", deviceId);
                            editor.putString("UnlockID", et_register_unlockId
                                    .getText().toString().trim());
                            editor.putString("UnlockPassword",
                                    et_register_unlockPassword.getText().toString()
                                            .trim());
                            editor.putString("HealthBlockID",
                                    et_register_healthBlockId.getText().toString()
                                            .trim());
                            editor.putString("TokenID", tokenId);
                            editor.commit();

                            finish();
                            Intent intent = new Intent(RegisterActivity.this,
                                    Register_download.class);
                            startActivity(intent);
                        }
                    } else {
                        Helper.showShortToast(RegisterActivity.this,
                                strResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };
}
