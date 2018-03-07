package nunet.rbsk.planoffline;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nunet.rbsk.BaseActivity;
import nunet.rbsk.R;
import nunet.rbsk.dashboard.DashBoardActivity;
import nunet.rbsk.helpers.CustomDialog;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.info.inst.InsituteFragmentActivityDialog;
import nunet.rbsk.model.Institute;
import nunet.rbsk.model.InstituteSchedule;
import nunet.rbsk.planoffline.PopoverView.PopoverViewDelegate;

public class PlanOffLineActivity extends BaseActivity implements
        OnClickListener, PopoverViewDelegate {
    private EditText et_search_plan_search;
    private TextView tv_search_plan_filter;
    private Button btn_plan_search_Go;
    private Cursor instCursor;
    private LinearLayout ll_search_plan_result;
    private TextView[] tv_plan_search_result_dise_code;
    private TextView[] tv_plan_search_result_inst_name;
    private TextView[] tv_plan_search_result_strength;
    private TextView[] tv_plan_search_result_screening;
    private TextView[] tv_plan_search_result_planned_for;
    private ImageView[] iv_plan_search_result_info;
    private ImageView[] iv_plan_search_result_institute;
    private ImageView[] iv_plan_search_result_add;
    private ImageView[] iv_plan_search_result_schedule;
    private ArrayList<InstituteSchedule> scheduleList;
    private ArrayList<InstituteSchedule> instScheduleList;
    boolean isResultFound = false;
    private Spinner spn_search_filter_phc, spn_search_filter_village;
    private Button btn_search_filter_apply, btn_search_filter_cancel;

    ArrayAdapter<String> adp_spnVillage;
    int phcId = 0;
    private String currentDate = "";
    public static CustomDialog dialog;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_plan);
        dbh = DBHelper.getInstance(this);
        findViews();

        // action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar_home, null);
        actionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#815CC5")));
        actionBar.setIcon(R.drawable.icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#815CC5")));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void loadData() {
        currentDate = changeCurrentDateFormat();
        getScheduleDataFromDB();

        et_search_plan_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (et_search_plan_search.getText().toString().trim().length() == 0) {
                    getUpdate_view("", 0, scheduleList);
                } else if (et_search_plan_search.getText().toString().trim()
                        .length() >= 3) {
                    et_search_plan_search.setError(null);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * Method to get Schedule Data from Database Kiruthika 19/05/2015
     */

    private void getScheduleDataFromDB() {
        final CustomDialog mCustomDialog = new CustomDialog(this);
        mCustomDialog.setCancelable(false);
        mCustomDialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {

                String query_Wards = "SELECT IPD.LocalInstitutePlanDetailID,I.InstituteID,I.InstituteName,"
                        + " I.DiseCode,IPD.scheduleDate, IPD.PlannedCount,IPD.PlanStatusID,IP.RBSKCalendarYearID,"
                        + " IP.ScreeningRoundID from InstitutePlanDetails IPD inner join instituteplans IP on "
                        + " IP.LocalInstitutePlanID=IPD.LocalInstitutePlanID "
                        + " inner join institutes I on I.InstituteID=IP.InstituteID where"
                        + " IPD.isDeleted='0' AND IP.IsDeleted!=1 AND  I.IsDeleted!=1 "
                        + " AND IPD.PlanStatusID in(1,2)";
                Cursor scheduleCur = dbh.getCursorData(
                        PlanOffLineActivity.this, query_Wards);
                setToScheduleModel(scheduleCur);
                scheduleList = Helper.scheduleList;
                runOnUiThread(new Runnable() {
                    public void run() {
                        getUpdate_view("", 0, scheduleList);
                        mCustomDialog.cancel();
                    }
                });
            }
        }).start();

    }

    private String changeCurrentDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        return currentDate;
    }

    /**
     * Method to set to instituteSchedule Model class Kiruthika 07/05/2015
     *
     * @param scheduleCur
     */
    private void setToScheduleModel(Cursor scheduleCur) {
        scheduleList = new ArrayList<InstituteSchedule>();
        if (scheduleCur != null) {
            try {
                if (scheduleCur.moveToFirst()) {
                    do {
                        InstituteSchedule insSchedule = new InstituteSchedule();
                        Institute instituteModel = new Institute();
                        insSchedule.setScheduleDate(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("scheduleDate")));

                        insSchedule.setPlannedCount(NumUtil.IntegerParse
                                .parseInt(scheduleCur.getString(scheduleCur
                                        .getColumnIndex("PlannedCount"))));
                        insSchedule.setScheduleDescription(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("PlannedCount")));

                        insSchedule
                                .setInstitutePlanStatusID(NumUtil.IntegerParse.parseInt(scheduleCur.getString(scheduleCur
                                        .getColumnIndex("PlanStatusID"))));

                        instituteModel
                                .setInstituteServerID(NumUtil.IntegerParse.parseInt(scheduleCur
                                        .getString(scheduleCur
                                                .getColumnIndex("InstituteID"))));
                        instituteModel.setInstituteName(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("InstituteName")));
                        instituteModel.setDiseCode(scheduleCur
                                .getString(scheduleCur
                                        .getColumnIndex("DiseCode")));

                        instituteModel.setScreeningRoundID(scheduleCur
                                .getInt(scheduleCur
                                        .getColumnIndex("ScreeningRoundId")));

                        instituteModel.setRBSKCalenarYearID(scheduleCur
                                .getInt(scheduleCur
                                        .getColumnIndex("RBSKCalendarYearID")));
                        instituteModel
                                .setLocalInstitutePlanDetailID(scheduleCur.getInt(scheduleCur
                                        .getColumnIndex("LocalInstitutePlanDetailID")));

                        insSchedule.setInstitute(instituteModel);

                        scheduleList.add(insSchedule);
                    } while (scheduleCur.moveToNext());
                    Helper.scheduleList = scheduleList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                scheduleCur.close();
            }
        }
    }

    private double updatePercentage(int instituteId) {

        double InsituteStudentCount = 0;
        double InsituteScreendStudentCount = 0;
        String query = "select count(*) as ChildrenScreenedCount from childrenscreening cs "
                + "inner join institutescreeningdetails isd on isd.localinstitutescreeningdetailid=cs.localinstitutescreeningdetailid "
                + "inner join institutescreening ins on ins.localinstitutescreeningid=isd.localinstitutescreeningid "
                + "where ins.instituteid='"
                + instituteId
                + "' and RBSKCalendarYearID=3 and ScreeningRoundID=3";
        Cursor mCursor = dbh.getCursorData(PlanOffLineActivity.this, query);
        if (mCursor != null) {
            mCursor.moveToFirst();
            String count = mCursor.getString(0);
            InsituteScreendStudentCount = NumUtil.IntegerParse.parseInt(count);
        }

        query = "select count(*) as InstitutSutntCount from Children C "
                + "inner join Institutes I on I.LocalInstituteID=C.LocalInstituteID where  I.InstituteID='"
                + instituteId + "' and  C.ChildrenStatusID=1";
        mCursor = dbh.getCursorData(PlanOffLineActivity.this, query);
        if (mCursor != null) {
            mCursor.moveToFirst();
            String count = mCursor.getString(0);
            InsituteStudentCount = NumUtil.IntegerParse.parseInt(count);
        }
        if (InsituteStudentCount == 0) {
            return 0d;
        }

        double precent = (InsituteScreendStudentCount / InsituteStudentCount) * 100;
        return Math.round(precent);
        // TextView tv_modify_screen_percent = (TextView)
        // findViewById(R.id.tv_modify_screen_percent);
        // if (tv_modify_screen_percent != null)
        // tv_modify_screen_percent.setText(Math.round(precent)
        // + " % Screened");
    }

    /**
     * Get all id for the views
     */
    private void findViews() {
        et_search_plan_search = (EditText) findViewById(R.id.et_search_plan_search);
        tv_search_plan_filter = (TextView) findViewById(R.id.tv_search_plan_filter);
        tv_search_plan_filter.setOnClickListener(this);
        btn_plan_search_Go = (Button) findViewById(R.id.btn_plan_search_Go);
        btn_plan_search_Go.setOnClickListener(this);
        ll_search_plan_result = (LinearLayout) findViewById(R.id.ll_search_plan_result);
        ll_search_plan_result.setOnClickListener(this);
    }

    /*
     * On click listener for button
     */
    @Override
    public void onClick(View v) {
        if (v == tv_search_plan_filter) {
            tv_search_plan_filter.requestFocus();
            et_search_plan_search.setError(null);
            Helper.hideSoftKeyboard(PlanOffLineActivity.this);
            RelativeLayout rootView = (RelativeLayout) findViewById(R.id.ll_search_plan_full);
            final PopoverView popoverView = new PopoverView(this,
                    R.layout.search_filter_pop);
            popoverView.setContentSizeForViewInPopover(new Point(2000, 300));
            popoverView.setDelegate(this);
            popoverView.showPopoverFromRectInViewGroup(rootView,
                    PopoverView.getFrameForView(v),
                    PopoverView.PopoverArrowDirectionAny, true);

            spn_search_filter_phc = (Spinner) popoverView
                    .findViewById(R.id.spn_search_filter_phc);
            spn_search_filter_village = (Spinner) popoverView
                    .findViewById(R.id.spn_search_filter_village);
            btn_search_filter_apply = (Button) popoverView
                    .findViewById(R.id.btn_search_filter_apply);
            btn_search_filter_cancel = (Button) popoverView
                    .findViewById(R.id.btn_search_filter_cancel);

            // get PHC Data
            updatePHCData();

            btn_search_filter_apply.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (spn_search_filter_phc.getSelectedItemPosition() != 0) {

                        if (spn_search_filter_village.getSelectedItemPosition() != 0) {
                            getFacilityBasedInstituteDataFromDB(spn_search_filter_village
                                    .getSelectedItemPosition());
                            popoverView.dissmissPopover(true);
                        } else {
                            Toast.makeText(PlanOffLineActivity.this,
                                    "Select Village", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(PlanOffLineActivity.this, "Select Phc",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

            btn_search_filter_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popoverView.dissmissPopover(true);
                }
            });

        } else if (v == btn_plan_search_Go) {
            Helper.hideSoftKeyboard(PlanOffLineActivity.this);
            String txtData = et_search_plan_search.getText().toString().trim();
            if (txtData.length() < 3) {
                et_search_plan_search
                        .setError("Please enter atleast 3 characters to start search");
            } else {
                searchWithText(txtData);
            }

        } else if (v == ll_search_plan_result) {
            et_search_plan_search.setError(null);
        } else {
            Institute institute = scheduleList.get(v.getId()).getInstitute();
            if (v == iv_plan_search_result_schedule[v.getId()]) {
                Intent i = new Intent(PlanOffLineActivity.this,
                        ModifyPlanActivtyDialog.class);
                i.putExtra("InstituteID", institute.getInstituteServerID());
                i.putExtra("InstituteName", institute.getInstituteName());

                i.putExtra("LocalInstitutePlanDetailID",
                        institute.getInstitutePlanDetailID());

                i.putExtra("Strength", scheduleList.get(v.getId())
                        .getPlannedCount());
                i.putExtra("PlannedDate", scheduleList.get(v.getId())
                        .getScheduleDate());

                i.putExtra("RBSKCalendarYearID",
                        institute.getRBSKCalenarYearID());

                i.putExtra("ScreeningRoundId", institute.getScreeningRoundID());

                i.putExtra("LocalInstitutePlanDetailID",
                        institute.getLocalInstitutePlanDetailID());
                // finish();
                startActivity(i);
            } else if (v == iv_plan_search_result_institute[v.getId()]) {
                dialog = new CustomDialog(PlanOffLineActivity.this);
                dialog.setCancelable(false);
                dialog.show();
                Intent i = new Intent(PlanOffLineActivity.this,
                        InsituteFragmentActivityDialog.class);
                i.putExtra("instituteID", institute.getInstituteServerID());
                startActivity(i);
            }
        }
    }

    /**
     * Method to get Facility based institute Data from Database Kiruthika
     * 19/05/2015
     */
    protected void getFacilityBasedInstituteDataFromDB(int selVilPos) {

        String[] village = villagesListSpn.get(selVilPos - 1);
        String instituteQuery = "SELECT I.InstituteID,I.InstituteName,I.DiseCode,"
                + " IPD.scheduleDate,IPD.PlannedCount FROM InstitutePlanDetails IPD "
                + " inner join instituteplans IP on IP.LocalInstitutePlanID=IPD.LocalInstitutePlanID"
                + " inner join institutes I on I.InstituteID=IP.InstituteID"
                + " inner join Address A on A.LocalAddressID = I.LocalAddressID where "
                + " IP.IsDeleted!=1 AND   I.IsDeleted!=1 AND  A.IsDeleted!=1 AND   IPD.IsDeleted!=1 AND   VillageID ='"
                + village[0] + "'";

        // SELECT * FROM InstitutePlanDetails inner join institutes on
        // institutes.LocalInstituteID=instituteplandetails.LocalInstituteID
        // inner join Address on Address.LocalAddressID =
        // institutes.LocalInstituteID where VillageID =7278

        instCursor = dbh.getCursorData(this, instituteQuery);
        if (instCursor != null) {
            instScheduleList = new ArrayList<InstituteSchedule>();
            if (instCursor != null) {
                try {
                    if (instCursor.moveToFirst()) {
                        do {
                            InstituteSchedule insSchedule = new InstituteSchedule();
                            Institute instituteModel = new Institute();
                            insSchedule.setScheduleDate(instCursor
                                    .getString(instCursor
                                            .getColumnIndex("scheduleDate")));

                            insSchedule.setPlannedCount(NumUtil.IntegerParse
                                    .parseInt(instCursor.getString(instCursor
                                            .getColumnIndex("PlannedCount"))));
                            insSchedule.setScheduleDescription(instCursor
                                    .getString(instCursor
                                            .getColumnIndex("PlannedCount")));

                            instituteModel
                                    .setInstituteServerID(NumUtil.IntegerParse.parseInt(instCursor.getString(instCursor
                                            .getColumnIndex("InstituteID"))));
                            instituteModel.setInstituteName(instCursor
                                    .getString(instCursor
                                            .getColumnIndex("InstituteName")));
                            instituteModel.setDiseCode(instCursor
                                    .getString(instCursor
                                            .getColumnIndex("DiseCode")));
                            insSchedule.setInstitute(instituteModel);
                            instScheduleList.add(insSchedule);

                        } while (instCursor.moveToNext());
                    }
                } finally {
                    instCursor.close();
                }
            }
            getUpdate_view("", 0, instScheduleList);
        } else {
            Toast.makeText(this, "No Records found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to get Facility Values and added to spinner
     */
    public void updatePHCData() {
        String query = "select FacilityID,F.DisplayText from facilities F inner join facilitytypes ft on ft.facilitytypeid=F.facilitytypeid where  F.IsDeleted!=1 AND   lower(Ft.DisplayText) ='phc'";
        // 12- PHC
        final List<String[]> data = dbh.getCursorFromQueryData(this, query);
        ArrayAdapter<String> adp_spnphc = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adp_spnphc
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp_spnphc.add("--Select PHC--");
        if (data == null) {
        } else {
            for (int i = 0; i < data.size(); i++) {
                String dataVal = data.get(i)[1];
                adp_spnphc.add(TextUtils.isEmpty(dataVal) ? "" : dataVal);
            }
        }
        spn_search_filter_phc.setAdapter(adp_spnphc);

        adp_spnVillage = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adp_spnVillage
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp_spnVillage.add("--Select Village--");
        spn_search_filter_village.setAdapter(adp_spnVillage);
        spn_search_filter_phc
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (spn_search_filter_phc.getSelectedItemPosition() != 0) {
                            phcId = NumUtil.IntegerParse.parseInt(data
                                    .get(spn_search_filter_phc
                                            .getSelectedItemPosition() - 1)[0]);
                            updateVillage(phcId);
                        } else {
                            adp_spnVillage = new ArrayAdapter<String>(
                                    PlanOffLineActivity.this,
                                    android.R.layout.simple_spinner_item);
                            adp_spnVillage
                                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            adp_spnVillage.add("--Select Village--");
                            spn_search_filter_village
                                    .setAdapter(adp_spnVillage);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

    }

    /**
     * Method to get Village based on facility and added to spinner
     */
    List<String[]> villagesListSpn = null;
    private DBHelper dbh;

    public void updateVillage(int phcId) {
        // String query =
        // "select distinct villages.villageName,address.villageId from address "
        // + " inner join villages on villages.villageid=address.villageId "
        // +
        // " inner join institutes on institutes.AddressID=address.AddressID where institutes.facilityid='"
        // + phcId + "'";
        String query = "select V.VillageID ,V.DisplayText as VilDisplayText from Facilities f "
                + "inner join FacilityCoverage fc on f.FacilityID = fc.FacilityID "
                + "INNER JOIN Villages V ON V.VillageID = FC.VillageID "
                + "where  f.IsDeleted!=1 AND  fc.IsDeleted!=1 AND   v.IsDeleted!=1 AND    f.FacilityTypeID = 12 and fc.DoesCover = 1 AND F.FacilityID ="
                + phcId;

        villagesListSpn = dbh.getCursorFromQueryData(this, query);
        if (villagesListSpn != null) {
            adp_spnVillage = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adp_spnVillage
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adp_spnVillage.add("--Select Village--");
            for (int i = 0; i < villagesListSpn.size(); i++) {
                if (villagesListSpn.get(i)[1] != null)
                    adp_spnVillage.add(villagesListSpn.get(i)[1]);
                else
                    adp_spnVillage.add("" + i);
            }
            spn_search_filter_village.setAdapter(adp_spnVillage);
        }
    }

    /**
     * Method to search plan with institute Name
     *
     * @param txtData
     */
    private void searchWithText(String txtData) {
        if (isNumber(txtData)) {// search with DISE code
            getUpdate_view(txtData, 2, scheduleList);
        } else {// search with instName
            getUpdate_view(txtData, 1, scheduleList);
        }
    }

    /**
     * Method to search plan with Dise code
     *
     * @param
     */
    public static boolean isNumber(String string) {
        try {
            Long.parseLong(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Method to Set data from model to view
     *
     * @param txtData ,searchByFlagIndex
     */
    @SuppressLint("InflateParams")
    public void getUpdate_view(String txtData, int searchByFlagIndex,
                               ArrayList<InstituteSchedule> schedList) {
        ll_search_plan_result.removeAllViews();

        if (!schedList.isEmpty()) {
            int resultCount = 0;
            tv_plan_search_result_dise_code = new TextView[schedList.size()];
            tv_plan_search_result_inst_name = new TextView[schedList.size()];
            tv_plan_search_result_strength = new TextView[schedList.size()];
            tv_plan_search_result_screening = new TextView[schedList.size()];
            tv_plan_search_result_planned_for = new TextView[schedList.size()];
            iv_plan_search_result_info = new ImageView[schedList.size()];
            iv_plan_search_result_institute = new ImageView[schedList.size()];
            iv_plan_search_result_add = new ImageView[schedList.size()];
            iv_plan_search_result_schedule = new ImageView[schedList.size()];

            for (int i = 0; i < schedList.size(); i++) {
                if (schedList.get(i).getInstitute().getScreenedPercentage() > 90) {
                    continue;
                }

                LayoutInflater mInflater = LayoutInflater.from(this);
                View mCustomView = mInflater.inflate(
                        R.layout.plan_search_result, null);
                tv_plan_search_result_dise_code[i] = (TextView) mCustomView
                        .findViewById(R.id.tv_plan_search_result_dise_code);
                tv_plan_search_result_inst_name[i] = (TextView) mCustomView
                        .findViewById(R.id.tv_plan_search_result_inst_name);
                tv_plan_search_result_strength[i] = (TextView) mCustomView
                        .findViewById(R.id.tv_plan_search_result_strength);
                tv_plan_search_result_screening[i] = (TextView) mCustomView
                        .findViewById(R.id.tv_plan_search_result_screening);
                tv_plan_search_result_planned_for[i] = (TextView) mCustomView
                        .findViewById(R.id.tv_plan_search_result_planned_for);
                iv_plan_search_result_info[i] = (ImageView) mCustomView
                        .findViewById(R.id.iv_plan_search_result_info);
                iv_plan_search_result_add[i] = (ImageView) mCustomView
                        .findViewById(R.id.iv_plan_search_result_add);
                iv_plan_search_result_institute[i] = (ImageView) mCustomView
                        .findViewById(R.id.iv_plan_search_result_institute);
                iv_plan_search_result_schedule[i] = (ImageView) mCustomView
                        .findViewById(R.id.iv_plan_search_result_schedule);
                if (searchByFlagIndex == 0) {// no need to search or update view

                    iv_plan_search_result_info[i].setId(i);
                    iv_plan_search_result_info[i].setOnClickListener(this);

                    iv_plan_search_result_institute[i].setId(i);
                    iv_plan_search_result_institute[i].setOnClickListener(this);

                    iv_plan_search_result_add[i].setId(i);
                    iv_plan_search_result_add[i].setOnClickListener(this);

                    iv_plan_search_result_schedule[i].setId(i);
                    iv_plan_search_result_schedule[i].setOnClickListener(this);
                    ll_search_plan_result.addView(mCustomView);
                    tv_plan_search_result_dise_code[i].setText(schedList.get(i)
                            .getInstitute().getDiseCode());
                    tv_plan_search_result_inst_name[i].setText(schedList.get(i)
                            .getInstitute().getInstituteName());
                    tv_plan_search_result_planned_for[i].setText(schedList.get(
                            i).getScheduleDate());
                    tv_plan_search_result_strength[i].setText(" "
                            + schedList.get(i).getPlannedCount());

                    updateScreenPercentage(schedList, i);

                    if (schedList.get(i).getInstitutePlanStatusID() == 2
                            && StringUtils.equalsNoCase(schedList.get(i)
                            .getScheduleDate(), currentDate)) {
                        iv_plan_search_result_schedule[i].setEnabled(false);
                        iv_plan_search_result_institute[i].setEnabled(false);
                    } else {
                        iv_plan_search_result_institute[i].setEnabled(true);
                        iv_plan_search_result_schedule[i].setEnabled(true);
                    }
                    resultCount++;

                } else if (searchByFlagIndex == 1) {// serach by Inst Name
                    String instName = schedList.get(i).getInstitute()
                            .getInstituteName();
                    if (instName.trim().toLowerCase()
                            .contains(txtData.trim().toLowerCase())) {
                        iv_plan_search_result_info[i].setId(i);
                        iv_plan_search_result_info[i].setOnClickListener(this);

                        iv_plan_search_result_institute[i].setId(i);
                        iv_plan_search_result_institute[i]
                                .setOnClickListener(this);

                        iv_plan_search_result_add[i].setId(i);
                        iv_plan_search_result_add[i].setOnClickListener(this);
                        iv_plan_search_result_schedule[i].setId(i);
                        iv_plan_search_result_schedule[i]
                                .setOnClickListener(this);
                        ll_search_plan_result.addView(mCustomView);
                        tv_plan_search_result_dise_code[i].setText(schedList
                                .get(i).getInstitute().getDiseCode());
                        tv_plan_search_result_inst_name[i].setText(instName);
                        tv_plan_search_result_planned_for[i].setText(schedList
                                .get(i).getScheduleDate());
                        tv_plan_search_result_strength[i].setText(" "
                                + schedList.get(i).getPlannedCount());

                        updateScreenPercentage(schedList, i);
                        resultCount++;
                        if (schedList.get(i).getInstitutePlanStatusID() == 2
                                && StringUtils.equalsNoCase(schedList.get(i)
                                .getScheduleDate(), currentDate)) {
                            iv_plan_search_result_schedule[i].setEnabled(false);
                            iv_plan_search_result_institute[i].setEnabled(false);
                        } else {
                            iv_plan_search_result_institute[i].setEnabled(true);
                            iv_plan_search_result_schedule[i].setEnabled(true);
                        }
                    }

                } else if (searchByFlagIndex == 2) {// search by DISC code
                    String diseCode = schedList.get(i).getInstitute()
                            .getDiseCode();
                    if (diseCode.trim().contains(txtData.trim())) {
                        iv_plan_search_result_info[i].setId(i);
                        iv_plan_search_result_info[i].setOnClickListener(this);

                        iv_plan_search_result_institute[i].setId(i);
                        iv_plan_search_result_institute[i]
                                .setOnClickListener(this);
                        iv_plan_search_result_schedule[i].setId(i);
                        iv_plan_search_result_schedule[i]
                                .setOnClickListener(this);

                        iv_plan_search_result_add[i].setId(i);
                        iv_plan_search_result_add[i].setOnClickListener(this);
                        ll_search_plan_result.addView(mCustomView);
                        tv_plan_search_result_dise_code[i].setText(diseCode);
                        tv_plan_search_result_inst_name[i].setText(schedList
                                .get(i).getInstitute().getInstituteName());
                        tv_plan_search_result_planned_for[i].setText(schedList
                                .get(i).getScheduleDate());
                        tv_plan_search_result_strength[i].setText(" "
                                + schedList.get(i).getPlannedCount());
                        updateScreenPercentage(schedList, i);
                        resultCount++;
                        if (schedList.get(i).getInstitutePlanStatusID() == 2
                                && StringUtils.equalsNoCase(schedList.get(i)
                                .getScheduleDate(), currentDate)) {
                            iv_plan_search_result_schedule[i].setEnabled(false);
                            iv_plan_search_result_institute[i].setEnabled(false);
                        } else {
                            iv_plan_search_result_institute[i].setEnabled(true);
                            iv_plan_search_result_schedule[i].setEnabled(true);
                        }
                    }

                }
            }

            if (resultCount == 0) {
                TextView myText = new TextView(this);
                myText.setText("No Records Found");
                myText.setGravity(Gravity.CENTER);
                myText.setTextColor(Color.parseColor("#e6e6e6"));
                myText.setTextSize(28);
                ll_search_plan_result.addView(myText);
            }
        }
    }

    private void updateScreenPercentage(
            final ArrayList<InstituteSchedule> schedList, final int i) {

        new AsyncTask<Void, Void, Double>() {
            Institute institute = schedList.get(i).getInstitute();

            @Override
            protected Double doInBackground(Void... params) {

                return updatePercentage(institute.getInstituteServerID());
            }

            protected void onPostExecute(Double result) {
                tv_plan_search_result_screening[i].setText(result + "%");
                institute.setScreenedPercentage(result);

            }

            ;
        }.execute();

    }

    // Method to show Popup for plan filters
    @Override
    public void popoverViewWillShow(PopoverView view) {
        Log.i("POPOVER", "Will show");
    }

    @Override
    public void popoverViewDidShow(PopoverView view) {
        Log.i("POPOVER", "Did show");
    }

    @Override
    public void popoverViewWillDismiss(PopoverView view) {
        Log.i("POPOVER", "Will dismiss");
    }

    @Override
    public void popoverViewDidDismiss(PopoverView view) {
        Log.i("POPOVER", "Did dismiss");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PlanOffLineActivity.this, DashBoardActivity.class);
        finish();
        startActivity(i);
        super.onBackPressed();
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
