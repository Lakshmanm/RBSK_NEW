package nunet.rbsk.planoffline;

import java.text.SimpleDateFormat;
import java.util.Date;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nunet.utils.StringUtils;

public class ModifyPlanActivtyDialog extends Activity implements
        OnClickListener {
    private TextView tv_modify_plan_inst_name;
    private TextView tv_modify_plan_date;
    private TextView tv_modify_plan_inst_count;
    private TextView tv_modify_plan_now_count;
    private TextView tv_modify_plan_error_msg;
    private EditText et_modify_plan_target;
    private Button btn_modify_plan_save;
    private Button btn_modify_plan_cancel;
    private int instituteId;
    private int studStrength;
    private String instituteName = "";
    private SimpleDateFormat sdf;
    private String currentDate = "";
    private int currentDateCount;
    private DBHelper dbh;
    private int RBSKCalendarYearID;
    private int ScreeningRoundId;
    private long localInstitutePlanDetailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.modify_plan);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dbh = DBHelper.getInstance(this);
        findViews();
        instituteName = getIntent().getStringExtra("InstituteName");
        // localInstitutePlanDetailID = getIntent().getIntExtra(
        // "LocalInstitutePlanDetailID", 0);
        instituteId = getIntent().getIntExtra("InstituteID", 0);
        studStrength = getIntent().getIntExtra("Strength", 0);

        localInstitutePlanDetailID = getIntent().getLongExtra(
                "LocalInstitutePlanDetailID", 0);

        RBSKCalendarYearID = getIntent().getIntExtra("RBSKCalendarYearID", 0);
        ScreeningRoundId = getIntent().getIntExtra("ScreeningRoundId", 0);
        sdf = new SimpleDateFormat("EEEE d MMMM, yyyy");
        setDatatoDialog();
    }

    private void setDatatoDialog() {
        tv_modify_plan_inst_name.setText(instituteName);
        Date d = new Date();
        tv_modify_plan_date.setText(sdf.format(d));
        tv_modify_plan_inst_count.setText("" + studStrength);
        currentDate = changeCurrentDateFormat();
        currentDateCount = getNumberofStudents(currentDate);
        tv_modify_plan_now_count.setText("" + currentDateCount);

    }


    /**
     * Method to check validation Kiruthika 20-05-2015
     */
    private void checkCountValue(int currentDateCount) {
        if (!et_modify_plan_target.getText().toString().isEmpty()) {
            if (StringUtils.equalsNoCase(et_modify_plan_target.getText()
                    .toString(), "0")) {
                et_modify_plan_target.setText("");
                tv_modify_plan_error_msg
                        .setText("Please enter valid screening target");
                tv_modify_plan_error_msg.setVisibility(View.VISIBLE);
            } else {
                int todayCount = NumUtil.IntegerParse
                        .parseInt(et_modify_plan_target.getText().toString());
                if (studStrength < todayCount) {
                    et_modify_plan_target.setText("");
                    tv_modify_plan_error_msg
                            .setText("Please Enter correct student count");
                    tv_modify_plan_error_msg.setVisibility(View.VISIBLE);
                } else if (todayCount + currentDateCount > 180) {
                    et_modify_plan_target.setText("");
                    tv_modify_plan_error_msg
                            .setText("Daily Target of MHT exceeded,please enter lower target");
                    tv_modify_plan_error_msg.setVisibility(View.VISIBLE);
                } else {
                    et_modify_plan_target.setText("");
                    tv_modify_plan_error_msg.setVisibility(View.GONE);
                    updateDataToDB(todayCount);

                    // Intent i = new Intent(ModifyPlan.this, SearchPlan.class);
                    finish();
                    // startActivity(i);
                }
            }
        } else {
            tv_modify_plan_error_msg.setText("Please enter screening target");
            tv_modify_plan_error_msg.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method to Update to Database
     */
    private void updateDataToDB(int tdyCount) {

        int count = studStrength - tdyCount;
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select RBSKCalendarYearID from RBSKCalendarYears where year = " + Helper.getyear() + " and IsDeleted =0";
        Cursor c = db.rawQuery(Query, null);
        while (c.moveToNext()) {
            RBSKCalendarYearID = Integer.parseInt(c.getString(0));
        }
        c.close();
        String todayDate = Helper.getTodayDateTime();
        if (todayDate.indexOf(" ") != -1)
            todayDate = todayDate.substring(0, todayDate.indexOf(" ")) + " 00:00:00";
        String Query1 = "Select ScreeningRoundID from ScreeningRounds where RoundStartDate <= '" + todayDate + "' and RoundEndDate >= '" + todayDate + "' and IsDeleted =0";
        Cursor c1 = db.rawQuery(Query1, null);
        while (c1.moveToNext()) {
            ScreeningRoundId = Integer.parseInt(c1.getString(0));
        }
        c1.close();
        db.close();
        if (count != 0) {
            // change institute
            dbh.updateROWByValues(this, "Instituteplandetails", new String[]{
                            "PlannedCount", "PlanStatusID", "LastCommitedDate"},
                    new String[]{String.valueOf(count), "1", Helper.getTodayDateTime1()},
                    new String[]{"LocalInstitutePlanDetailID"},
                    new String[]{String.valueOf(localInstitutePlanDetailID)});
        } else {
            // change institute
            dbh.updateROWByValues(this, "Instituteplandetails", new String[]{
                            "isDeleted", "PlannedCount", "LastCommitedDate"}, new String[]{"1", "0", Helper.getTodayDateTime1()},
                    new String[]{"LocalInstitutePlanDetailID"},
                    new String[]{String.valueOf(localInstitutePlanDetailID)});
        }
        String query = "select  LocalInstitutePlanDetailID, PlannedCount from instituteplandetails IPD"
                + " inner join instituteplans IP on IP.LocalInstitutePlanID=IPD.LocalInstitutePlanID where "
                + " IPD.IsDeleted!=1 AND   IP.IsDeleted!=1 AND   "
                + " InstituteId='"
                + instituteId
                + "' and scheduleDate='"
                + currentDate + "';";
        Cursor cursor = dbh.getCursorData(this, query);
        if (cursor == null) {
            // change institute

            long LocalInstitutePlanID = dbh.insertintoTable(
                    this,
                    "Instituteplans",
                    new String[]{"InstituteId", "RBSKCalendarYearID",
                            "ScreeningRoundId", "InstitutePlanStatusID", "LastCommitedDate"},
                    new String[]{String.valueOf(instituteId),
                            String.valueOf(RBSKCalendarYearID),
                            String.valueOf(ScreeningRoundId), "1", Helper.getTodayDateTime1()});

            dbh.insertintoTable(this, "Instituteplandetails", new String[]{
                            "LocalInstitutePlanID", "scheduleDate", "PlannedCount",
                            "PlanStatusID", "isDeleted", "LastCommitedDate",},
                    new String[]{String.valueOf(LocalInstitutePlanID),
                            currentDate, String.valueOf(tdyCount), "1", "0", Helper.getTodayDateTime1(),});
        } else {
            cursor.moveToFirst();
            int dbPlanCount = NumUtil.IntegerParse
                    .parseInt(cursor.getString(1)) + tdyCount;
            String LocalInstitutePlanDetailID = cursor.getString(0);
            dbh.updateROWByValues(this, "Instituteplandetails", new String[]{
                            "PlannedCount", "PlanStatusID", "LastCommitedDate"}, new String[]{
                            dbPlanCount + "".trim(), "1", Helper.getTodayDateTime1()},
                    new String[]{"LocalInstitutePlanDetailID"},
                    new String[]{LocalInstitutePlanDetailID});
        }

        Helper.showShortToast(getApplicationContext(), "Updated Successfully");
    }

    /**
     * @param currentDate
     * @return
     */
    private String changeCurrentDateFormat() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        return currentDate;
    }

    /**
     * Method to get numbers of planned student for the particular date
     */

    private int getNumberofStudents(String dateString) {
        int studentCount = 0;
        String query_no_of_institutes = "Select sum(PlannedCount) as StudentCount from instituteplandetails IPD "
                + " inner join InstitutePlans IP on IP.LocalInstitutePlanID=IPD.LocalInstitutePlanID "
                + "  where  IPD.IsDeleted!=1 AND  IP.IsDeleted!=1 AND    scheduleDate='"
                + dateString + "' and PlanStatusID in(1,2)";
        Cursor scheduleCur = dbh.getCursorData(this, query_no_of_institutes);
        if (scheduleCur != null)

        {
            try {
                if (scheduleCur.moveToFirst()) {
                    do {
                        String plannedValue = scheduleCur.getString(scheduleCur
                                .getColumnIndex("StudentCount"));
                        if (plannedValue != null) {
                            studentCount = NumUtil.IntegerParse
                                    .parseInt(plannedValue);
                        } else {
                            studentCount = 0;
                        }
                    } while (scheduleCur.moveToNext());
                }
            } finally {
                scheduleCur.close();
            }
        }
        return studentCount;
    }

    /**
     * Method to get all views id's
     */
    private void findViews() {
        tv_modify_plan_inst_name = (TextView) findViewById(R.id.tv_modify_plan_inst_name);
        tv_modify_plan_date = (TextView) findViewById(R.id.tv_modify_plan_date);
        tv_modify_plan_inst_count = (TextView) findViewById(R.id.tv_modify_plan_inst_count);
        tv_modify_plan_now_count = (TextView) findViewById(R.id.tv_modify_plan_now_count);
        et_modify_plan_target = (EditText) findViewById(R.id.et_modify_plan_target);
        tv_modify_plan_error_msg = (TextView) findViewById(R.id.tv_modify_plan_error_msg);
        btn_modify_plan_save = (Button) findViewById(R.id.btn_modify_plan_save);
        btn_modify_plan_save.setOnClickListener(this);
        btn_modify_plan_cancel = (Button) findViewById(R.id.btn_modify_plan_cancel);
        btn_modify_plan_cancel.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.modify_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Onclick Listener for Buttons
    @Override
    public void onClick(View v) {
        if (v == btn_modify_plan_save) {

            checkCountValue(currentDateCount);

        } else if (v == btn_modify_plan_cancel) {
            // Intent i=new Intent(ModifyPlan.this,SearchPlan.class);
            finish();
            // startActivity(i);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ModifyPlanActivtyDialog.this,
                PlanOffLineActivity.class);
        finish();
        startActivity(i);
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
