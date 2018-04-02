package nunet.rbsk.login;

import java.util.ArrayList;
import java.util.List;

import nunet.rbsk.R;
import nunet.rbsk.dashboard.DashBoardActivity;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends Activity implements OnClickListener {
    private Spinner spn_user_login_username;
    private TextView et_user_login_password;
    private Button btn_user_login;
    private Button btn_user_return;
    private TextView tv_user_forgot_password;
    public static final String UserLogin = "UserLogin";
    public static final String noOfUsers = "noOfUsers";
    private SharedPreferences sharedpreferences;
    private Editor editor;
    private static Cursor cur;
    private DBHelper dbh;
    private ArrayAdapter<String> loginAdapter;
    private String usedOne = null;
    private int noOfUsersa;
    private String[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_login);
        dbh = DBHelper.getInstance(this);
        List<String> list = getCredentialsFromDb();
        loginAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        loginAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sharedpreferences = getSharedPreferences(UserLogin,
                Context.MODE_PRIVATE);
        usedOne = sharedpreferences.getString(noOfUsers, "");
        users = usedOne.split(",");
        noOfUsersa = users.length;

        /*
         * list.add("Select User"); list.add("User 1"); list.add("User 2");
         */
        /*
         * userAdapter = new ArrayAdapter<String>(this,
         * android.R.layout.simple_spinner_item, list); userAdapter
         * .setDropDownViewResource
         * (android.R.layout.simple_spinner_dropdown_item);
         */

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViews();
    }

    /**
     * @return
     */
    ArrayList<String> LoginUserIDs = new ArrayList<String>();

    private List<String> getCredentialsFromDb() {
        ArrayList<String> loginId = new ArrayList<String>();
        LoginUserIDs.clear();
        loginId.add("Select");
        LoginUserIDs.add("-1");
       // String relQuery = "Select * from UserCredentials";

        String relQuery = "Select distinct u.UserID, Login from UserCredentials uc"
                + " inner join users u on u.UserID = uc.UserCredentialID"
                + " inner join mhtstaff m on m.MHTStaffID=u.UserID where  uc.IsDeleted!=1 AND " +
                " u.IsDeleted!=1 AND   m.IsDeleted!=1 ";

//		String relQuery = "Select distinct u.UserID, Login from UserCredentials uc"
//				+ " inner join users u on u.localuserid = uc.localuserid"
//				+ " inner join mhtstaff m on m.localuserid=u.localuserid where  uc.IsDeleted!=1 AND   u.IsDeleted!=1 AND   m.IsDeleted!=1 ";
        cur = dbh.getCursorData(this, relQuery);
//        System.out.println("user credentails count...." + cur.getCount());
//        String relQuery1 = "Select * from users";
//        cur = dbh.getCursorData(this, relQuery1);
//        System.out.println("user count...." + cur.getCount());
//        cur.close();
//        String relQuery2 = "Select * from mhtstaff";
//        cur = dbh.getCursorData(this, relQuery2);
//        System.out.println("mht staff count...." + cur.getCount());
      //  cur.close();
        if (cur != null) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        loginId.add(cur.getString(cur.getColumnIndex("Login")));
                        LoginUserIDs.add(cur.getString(cur
                                .getColumnIndex("UserID")));
                    } while (cur.moveToNext());
                }
            } finally {
                cur.close();
            }
            // getUpdate_view("", 0);
        }
        return loginId;

    }

    /**
     *
     */
    private void findViews() {
        btn_user_login = (Button) findViewById(R.id.btn_user_login);
        btn_user_login.setOnClickListener(this);
        spn_user_login_username = (Spinner) findViewById(R.id.spn_user_login_username);
        spn_user_login_username.setAdapter(loginAdapter);
        et_user_login_password = (EditText) findViewById(R.id.et_user_login_password);
        btn_user_return = (Button) findViewById(R.id.btn_user_return);
        btn_user_return.setOnClickListener(this);
        tv_user_forgot_password = (TextView) findViewById(R.id.tv_user_forgot_password);
        et_user_login_password.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClick(btn_user_login);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v == btn_user_login) {
            if (spn_user_login_username.getSelectedItemPosition() == 0
                    || et_user_login_password.getText().toString().isEmpty()) {
                Toast.makeText(UserLoginActivity.this, "Enter Credentials",
                        Toast.LENGTH_SHORT).show();
            } else {
                String password = getPasswordFromDb(spn_user_login_username
                        .getSelectedItem().toString());
                if (password
                        .equals(et_user_login_password.getText().toString())) {
                    sharedpreferences = getSharedPreferences(UserLogin,
                            Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();

                    if (noOfUsersa == 1 && users[0].equals("")) {
                        editor.putString(noOfUsers,
                                spn_user_login_username.getSelectedItem() + "");
                    } else {
                        boolean a = true;
                        for (int z = 0; z < noOfUsersa; z++) {
                            if (users[z].equals(spn_user_login_username
                                    .getSelectedItem() + "")) {
                                a = false;
                            } else {

                            }

                        }
                        if (a == true) {
                            editor.putString(noOfUsers, usedOne + ","
                                    + spn_user_login_username.getSelectedItem()
                                    + "");
                        }
                    }

                    editor.putString("LoginUserID", LoginUserIDs
                            .get(spn_user_login_username
                                    .getSelectedItemPosition()));
                    editor.commit();
                    Intent basic_info = new Intent(UserLoginActivity.this,
                            DashBoardActivity.class);
                    startActivity(basic_info);
                } else {
                    Toast.makeText(UserLoginActivity.this, "Wrong Password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == btn_user_return) {
            Intent basic_info = new Intent(UserLoginActivity.this,
                    LoginActivity.class);
            startActivity(basic_info);
        } else if (v == tv_user_forgot_password) {
        }
    }

    /**
     * @param selectedItemPosition
     * @return
     */
    private String getPasswordFromDb(String selectedItemPosition) {
        String password = null;
        String relQuery = "Select Password from usercredentials uc where  uc.IsDeleted!=1 AND   Login='"
                + selectedItemPosition + "'";
        cur = dbh.getCursorData(this, relQuery);
        if (cur != null) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        password = (cur.getString(cur
                                .getColumnIndex("Password")));
                    } while (cur.moveToNext());
                }
            } finally {
                cur.close();
            }
            // getUpdate_view("", 0);
        }
        return password;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        Intent in = new Intent(UserLoginActivity.this, LoginActivity.class);
        startActivity(in);
        // super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null
                && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText
                && !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop()
                    || y > v.getBottom())
                Helper.hideSoftKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

}
