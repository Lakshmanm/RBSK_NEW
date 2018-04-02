package nunet.rbsk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.nunet.utils.StringUtils;

import java.io.IOException;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.login.IdentifyLoginActivity;
import nunet.rbsk.login.LoginActivity;
import nunet.rbsk.login.RegisterActivity;
import nunet.rbsk.login.UserLoginActivity;

public class SpalshScreenActivity extends Activity {
    private SharedPreferences sharedpreferences;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

//         Intent serviceIntent = new Intent(
//         SpalshScreenActivity.this, IncrementalService.class);
//         stopService(serviceIntent);
//         startService(serviceIntent);
        DBHelper dbh = DBHelper.getInstance(SpalshScreenActivity.this);

        try {
            dbh.createDataBase();
            dbh.openDataBase();
            dbh.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // reading the values from the Local storage
                sharedpreferences = getSharedPreferences(
                        UserLoginActivity.UserLogin, Context.MODE_PRIVATE);
                String usedOne = sharedpreferences.getString(
                        UserLoginActivity.noOfUsers, null);
                // Praveen Hack code Start
               // sharedpreferences.edit().putString("DB", "Yes").commit();
                // Praveen Hack code ends

                if (sharedpreferences.getString("DB", "").equals("Yes")) {
                    // Intent serviceIntent = new Intent(
                    // SpalshScreenActivity.this, IncrementalService.class);
                    // startService(serviceIntent);

                    // DB Test Code Start - Praveen

                    //System.out.println("trying for a Query 1");
//					DBHelper dbh = DBHelper.getInstance(SpalshScreenActivity.this);
//					//System.out.println("trying for a Query 2");
//					String relQuery = "select * from Allergies";
//					//System.out.println("trying for a Query 3");
//					Cursor cur = dbh.getCursorData(SpalshScreenActivity.this, relQuery);
//					//System.out.println("trying for a Query 4");
//					if (cur!=null)
//					{
//						System.out.println("DB is working ");
//					}
//					else
//					{
//						System.out.println("DB is not working ");
//					}

                    //String[] strColumns = new String[]{"InsertCheckName"};
                    //String[] strValues = new String[]{"Praveen"};
                    //dbh.insertintoTable(SpalshScreenActivity.this, "InsertCheck",strColumns, strValues  );


                    // DB Test Code Ends - Praveen


                    Intent in = new Intent(SpalshScreenActivity.this,
                            LoginActivity.class);
                    startActivity(in);
                } else {
                    if (usedOne != null) {
                        Intent in = new Intent(SpalshScreenActivity.this,
                                IdentifyLoginActivity.class);
                        startActivity(in);
                    } else {
                        if (StringUtils.equalsNoCase(
                                sharedpreferences.getString("DB", ""), "Yes")) {
                            Intent in = new Intent(SpalshScreenActivity.this,
                                    LoginActivity.class);
                            startActivity(in);
                        } else {
                            Intent in = new Intent(SpalshScreenActivity.this,
                                    RegisterActivity.class);
                            startActivity(in);
                        }
                    }
                }

                finish(); // Activity closure
            }
        }, SPLASH_TIME_OUT);
    }
}
