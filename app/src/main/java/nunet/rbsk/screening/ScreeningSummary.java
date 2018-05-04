//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.screening;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nunet.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nunet.adapter.CustomGridAdapterHC;
import nunet.rbsk.ExpandableQuestions;
import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Category;
import nunet.rbsk.model.HealthConditionModel;
import nunet.rbsk.model.HealthConditionModel.HCTypes;
import nunet.rbsk.model.Question;
import nunet.rbsk.model.Recommendations;
import nunet.rbsk.model.Referral;
import nunet.rbsk.model.SignOffScreenModel;

//*****************************************************************************
//* Name   :  ScreeningSummary.java

//* Type    :

//* Description     :
//* References     :
//* Author    : deepika.chevvakula

//* Created Date       :  29-May-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG
//*****************************************************************************
//* Ver        Date                Code Review By            Observations
//3.0			06-08-2015	           Promodh					Method Comments are missing

//*****************************************************************************
public class ScreeningSummary extends Fragment implements OnClickListener {

    DBHelper dbh;
    private ArrayList<Integer> healthConditions;
    public static ArrayAdapter<String> adp_spnreferto, adp_referral;
    private ImageView iv_referral_healthconditions;
    private ArrayList<Referral> investAry;
    private List<String[]> data;
    private static ArrayList<HealthConditionModel> updatedHealthConditionsArray;
    private static ArrayList<HashMap<String, String>> healthCondList;
    private ImageView iv_examinations_next;
    private TextView tv_screening_basic_student_name;
    private TextView tv_summary_age;
    private String gender;
    private int instType;
    ArrayList<HashMap<String, String>> masterInvestigationsArrayList;
    private ImageView iv_summary_student_image;
    private GridView grid_hcDetected;

    ScreeningActivity mScreeningActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mScreeningActivity = (ScreeningActivity) getActivity();
        dbh = DBHelper.getInstance(this.getActivity());

        View rootView = inflater.inflate(R.layout.screening_summary, container,
                false);
        findViews(rootView);
        if (Helper.childrenObject.getGender() != null)
            gender = Helper.childrenObject.getGender().getGenderName();
        else
            gender = "";

        instType = Helper.childrenObject.getChildrenInsitute()
                .getInstituteTypeId();
        String message = "";
        int ageInMonths = Helper.childrenObject.getAgeInMonths();
        message += (ageInMonths / 12) == 0 ? "" : (ageInMonths / 12)
                + " Years ";
        message += (ageInMonths % 12) == 0 ? "" : (ageInMonths % 12)
                + " Months, ";

        tv_summary_age.setText(message + gender);
        getMasterLabInvestigationsFromDB();

        updatedHealthConditionsArray = new ArrayList<HealthConditionModel>();
        updateScreenUI();
        if (Helper.childScreeningObj.getScreeningID() != 0) {
            getScreenedSignoffDataFromDB();
        }
        if (Helper.childScreeningObj.getSignOffModel() == null) {
            SignOffScreenModel signOffModel = new SignOffScreenModel();
            Helper.childScreeningObj.setSignOffModel(signOffModel);
        }
        updateRecommendations();
        return rootView;
    }

    private void updateRecommendations() {
        Recommendations recommendationsObj;

        if (Helper.childScreeningObj.getSignOffModel().getRecommendations() == null) {
            recommendationsObj = new Recommendations();
            Helper.childScreeningObj.getSignOffModel().setRecommendations(
                    recommendationsObj);
        } else {
            recommendationsObj = Helper.childScreeningObj.getSignOffModel()
                    .getRecommendations();
        }
        String recommendationsStr = "";
        if (!TextUtils.isEmpty(recommendationsObj.getDiet())) {
            recommendationsStr += "Diet" + ",";
        }
        if (!TextUtils.isEmpty(recommendationsObj.getPersonalHygine())) {
            recommendationsStr += "Personal Hygiene" + ",";
        }
        if (!TextUtils.isEmpty(recommendationsObj.getOralHygine())) {
            recommendationsStr += "Oral Hygiene" + ",";
        }
        if (!TextUtils.isEmpty(recommendationsObj.getMedications())) {
            recommendationsStr += "Medications" + ",";
        }
        if (!TextUtils.isEmpty(recommendationsObj.getOthers())) {
            recommendationsStr += "Others" + ",";
        }
        if (!TextUtils.isEmpty(recommendationsStr)) {
            recommendationsStr = recommendationsStr.substring(0,
                    recommendationsStr.lastIndexOf(","));
        }
    }

    private void getScreenedSignoffDataFromDB() {
        SignOffScreenModel signOffModel;
        if (Helper.childScreeningObj.getSignOffModel() == null) {
            signOffModel = new SignOffScreenModel();
            Helper.childScreeningObj.setSignOffModel(signOffModel);
        }
        signOffModel = Helper.childScreeningObj.getSignOffModel();
        // *** Recommendations

        String reccomandationsQuery = "select * from childrenscreeningrecommendations where   IsDeleted!=1 and  LocalChildrenScreeningID='"
                + Helper.childScreeningObj.getScreeningID() + "';";
        Cursor recommendationsCursor = dbh.getCursorData(this.getActivity(),
                reccomandationsQuery);
        if (recommendationsCursor != null) {
            if (recommendationsCursor.moveToFirst()) {
                do {
                    Recommendations signOffRecommendations = signOffModel
                            .getRecommendations();
                    if (signOffRecommendations == null) {
                        signOffRecommendations = new Recommendations();
                    }
                    signOffRecommendations.setDiet(recommendationsCursor
                            .getString(recommendationsCursor
                                    .getColumnIndex("Diet")));
                    signOffRecommendations
                            .setPersonalHygine(recommendationsCursor
                                    .getString(recommendationsCursor
                                            .getColumnIndex("personalHygiene")));
                    signOffRecommendations.setOralHygine(recommendationsCursor
                            .getString(recommendationsCursor
                                    .getColumnIndex("OralHygience")));
                    signOffRecommendations.setMedications(recommendationsCursor
                            .getString(recommendationsCursor
                                    .getColumnIndex("PresribedMedication")));
                    signOffRecommendations.setOthers(recommendationsCursor
                            .getString(recommendationsCursor
                                    .getColumnIndex("OtherComments")));
                    // *** Doctor Comments

                    signOffModel.setDoctorComments(recommendationsCursor
                            .getString(recommendationsCursor
                                    .getColumnIndex("DoctorComments")));
                    signOffModel.setRecommendations(signOffRecommendations);
                } while (recommendationsCursor.moveToNext());

            }
            recommendationsCursor.close();
        }

        // *** Local Treatment.
        String localTreatmentQuery = "select * from childrenscreeninglocaltreatment where IsDeleted!=1 and   LocalChildrenScreeningID='"
                + Helper.childScreeningObj.getScreeningID() + "';";
        Cursor localTreatCursor = dbh.getCursorData(this.getActivity(),
                localTreatmentQuery);
        if (localTreatCursor != null) {
            if (localTreatCursor.moveToFirst()) {
                do {
                    signOffModel.setMedicationsGiven(localTreatCursor
                            .getString(localTreatCursor
                                    .getColumnIndex("MedicationGiven")));
                    signOffModel.setDiagnosis(localTreatCursor
                            .getString(localTreatCursor
                                    .getColumnIndex("Diagnosis")));
                } while (localTreatCursor.moveToNext());
            }
        }

        Helper.childScreeningObj.setSignOffModel(signOffModel);

    }

    private void updateScreenUI() {
        if (Helper.childScreeningObj.getScreeningID() != 0) {
            // *** Categories = null
            if (Helper.childScreeningObj.getCategories() == null) {
                // on direct refferal access
                getScreenedCategories();
                getHealthCondtionsDetected();
                investAry = new ArrayList<Referral>();
                getScreenedReferrals();
                Helper.childScreeningObj.setReferrals(investAry);
            } else {
                if (Helper.childScreeningObj.getReferrals() == null) {
                    investAry = new ArrayList<Referral>();
                    getScreenedReferrals();
                    Helper.childScreeningObj.setReferrals(investAry);
                } else {
                    investAry = Helper.childScreeningObj.getReferrals();
                }
                getHealthCondtionsDetected();

                // getScreenedCategories();
                // getHealthCondtionsDetected();
                // investAry = new ArrayList<Referral>();
                // getScreenedReferrals();
                // Helper.childScreeningObj.setReferrals(investAry);
            }
        } else {
            if (Helper.childScreeningObj.getReferrals() == null) {// first time
                investAry = new ArrayList<Referral>();
                Helper.childScreeningObj.setReferrals(investAry);
            } else {
                investAry = Helper.childScreeningObj.getReferrals();
            }
            getHealthCondtionsDetected();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        updateScreenUI();

        super.onResume();
    }

    public void getScreenedCategories() {
        getExamDetailsFromDB();
        ArrayList<Question> screenedQuestions = getQuestionsFromDB();
        String query = "select ScreenQuestionID,ScreenCategoryID,Question  from screenquestions where IsDeleted!=1 and   ScreenTemplateTypeID="
                + instType;
        Cursor cursor = dbh.getCursorData(this.getActivity(), query);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HashMap<String, String> hashMapQns = new HashMap<String, String>();
        while (cursor.moveToNext()) {
            hashMap.put(cursor.getString(0), cursor.getString(1));
            hashMapQns.put(cursor.getString(0), cursor.getString(2));
        }
        Category[] screenedCategories = Helper.childScreeningObj
                .getCategories();
        if (screenedCategories != null) {
            if (screenedQuestions != null)
                for (Question screenedQuestion : screenedQuestions) {
                    if (hashMap.size() > 0) {
                        String screenCategoryID = hashMap.get(String
                                .valueOf(screenedQuestion.getScreenQuestionID()));
                        for (Category screenedCategory : screenedCategories) {
                            if (NumUtil.IntegerParse.parseInt(screenCategoryID) == screenedCategory
                                    .getCategoryID()) {
                                ArrayList<Question> questions;
                                if (screenedCategory.getQuestions() != null) {
                                    questions = screenedCategory.getQuestions();
                                } else {
                                    screenedCategory
                                            .setQuestions(new ArrayList<Question>());
                                    questions = screenedCategory.getQuestions();
                                }

                                questions.add(screenedQuestion);
                                screenedCategory.setQuestions(questions);
                                screenedCategory.setIsVerified(true);
                                break;
                            }
                        }
                    }

                }
        }

        Helper.childScreeningObj.setCategories(screenedCategories);
    }

    public void getExamDetailsFromDB() {

        String query = "select ScreenCategoryID,DisplayText from screencategories where IsDeleted!=1 and   ScreenTemplateTypeID='"
                + instType + "' order by DisplaySequence;";
        List<String[]> data = dbh.getQueryData(this.getActivity(), query);

        // *** Set Categories from Database
        Category[] categories = new Category[data.size()];
        for (int i = 0; i < data.size(); i++) {
            Category category = new Category();
            category.setCategoryName(data.get(i)[1]);
            category.setCategoryID(NumUtil.IntegerParse.parseInt(data.get(i)[0]));
            category.setIsVerified(false);
            categories[i] = category;
        }

        Helper.childScreeningObj.setCategories(categories);

    }

    public ArrayList<Question> getQuestionsFromDB() {
        // *** Check with the reference of ScreeningSignOff
        String query = "select sq.Question , ScreeningQuestionID, csp.Answer, csp.IsReferredWhenYes,"
                + " csp.HealthConditionID from childrenscreeningpe csp"
                + " inner join screenquestions sq on sq.ScreenQuestionID=csp.ScreeningQuestionID"
                + " where csp.IsDeleted!=1 and sq.IsDeleted!=1 and   LocalChildrenScreeningID='"
                + Helper.childScreeningObj.getScreeningID() + "';";
        // String query =
        // "select * from (SELECT S.ScreenQuestionID as ScreeningQuestionID,S.Question as Question,C.Answer as Answer,S.IsReferredWhenYes as IsReferredWhenYes,S.HealthConditionID as HealthConditionID FROM screenquestions S LEFT OUTER JOIN childrenscreeningpe C ON S .ScreenQuestionID= C.ScreeningQuestionID Where LocalChildrenScreeningID='"
        // + Helper.childScreeningObj.getScreeningID() +
        // "' GROUP BY S.ScreenQuestionID,S.Question,C.Answer,S.IsReferredWhenYes,S.HealthConditionID UNION SELECT S.ScreenQuestionID,S.Question,C.Answer,S.IsReferredWhenYes,S.HealthConditionID FROM childrenscreeningpe C LEFT OUTER JOIN screenquestions S ON S .ScreenQuestionID= C.ScreeningQuestionID GROUP BY S.ScreenQuestionID,S.Question,C.Answer,S.IsReferredWhenYes,S.HealthConditionID ORDER BY S.ScreenQuestionID) as tab where Answer!='' AND Answer is not null and ScreeningQuestionID is not null";

        Cursor cursor = dbh.getCursorData(this.getActivity(), query);
        if (cursor != null) {
            ArrayList<Question> screenedQuestions = new ArrayList<Question>();
            if (cursor.moveToFirst()) {
                do {
                    Question question = new Question();
                    question.setScreenQuestionID(NumUtil.IntegerParse
                            .parseInt(cursor.getString(cursor
                                    .getColumnIndex("ScreeningQuestionID"))));
                    question.setQuestion(cursor.getString(cursor
                            .getColumnIndex("Question")));
                    question.setAnswer(cursor.getString(cursor
                            .getColumnIndex("Answer")));
                    question.setIsReferedWhen(NumUtil.IntegerParse
                            .parseInt(cursor.getString(cursor
                                    .getColumnIndex("IsReferredWhenYes"))));
                    question.setHealthConditionID(NumUtil.IntegerParse
                            .parseInt(cursor.getString(cursor
                                    .getColumnIndex("HealthConditionID"))));
                    screenedQuestions.add(question);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return screenedQuestions;
        }

        return null;
    }

    public void getMasterLabInvestigationsFromDB() {
        String query = "select * from labinvestigations where IsDeleted!=1";
        Cursor cursor = dbh.getCursorData(this.getActivity(), query);
        masterInvestigationsArrayList = new ArrayList<HashMap<String, String>>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", cursor.getString(cursor
                        .getColumnIndex("LabInvestigationID")));

                hashMap.put("value",
                        cursor.getString(cursor.getColumnIndex("DisplayText")));
                masterInvestigationsArrayList.add(hashMap);

            }
            cursor.close();
        } else {
        }
    }

    /**
     *
     */

    public void findViews(View rootView) {

        tv_summary_age = (TextView) rootView.findViewById(R.id.tv_summary_age);

        tv_screening_basic_student_name = (TextView) rootView
                .findViewById(R.id.tv_summary_child_name);
        tv_screening_basic_student_name.setText(Helper.childrenObject
                .getFirstName() + " " + Helper.childrenObject.getLastName());
        iv_referral_healthconditions = (ImageView) rootView
                .findViewById(R.id.iv_referral_healthconditions);
        iv_referral_healthconditions.setOnClickListener(this);

        iv_examinations_next = (ImageView) rootView
                .findViewById(R.id.iv_examinations_next);
        iv_summary_student_image = (ImageView) rootView
                .findViewById(R.id.iv_summary_student_image);
        iv_summary_student_image.setImageBitmap(Helper.childImage);
        grid_hcDetected = (GridView) rootView
                .findViewById(R.id.grid_hcDetected);

        iv_examinations_next = (ImageView) rootView
                .findViewById(R.id.iv_examinations_next);
        iv_examinations_next.setOnClickListener(this);

    }

    public void getHealthCondtionsDetected() {
        healthConditions = new ArrayList<Integer>();
        Category[] allCategories = Helper.childScreeningObj.getCategories();
        for (int i = 0; i < (allCategories.length - 1); i++) {
            Category category = allCategories[i];
            ArrayList<Question> questions = category.getQuestions();
            if (questions != null) {
                for (Question question : questions) {
                    if (NumUtil.IntegerParse.parseInt(question.getAnswer()) == question
                            .getIsReferedWhen()) {
                        healthConditions.add(question.getHealthConditionID());
                    }
                }
            }
        }
        System.out.println("healthConditions:::" + healthConditions);
        updateHealthCondtionsData();
    }

    /**
     *
     */
    // public void updatePlaceOfReferral(Context ctx) {
    //
    // adp_referral = new ArrayAdapter<String>(ctx,
    // android.R.layout.simple_spinner_item);
    // adp_referral
    // .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // adp_referral.add("Select Place of Referral");
    // String query =
    // "SELECT Facilities.FacilityID,Facilities.DisplayText as FacilitiesDT,FacilityTypes.DisplayText as  FacilityTypesDT FROM HealthConditionFaclities inner join Facilities on Facilities.FacilityID=HealthConditionFaclities.FacilityID inner join FacilityTypes on FacilityTypes.FacilityTypeID = Facilities.FacilityTypeID";
    // Cursor cursor = dbh.getCursorData(ctx, query);
    //
    // if (cursor != null) {
    //
    // while (cursor.moveToNext()) {
    //
    // adp_referral.add(""
    // + cursor.getString(cursor
    // .getColumnIndex("FacilityTypesDT"))
    // + ", "
    // + cursor.getString(cursor
    // .getColumnIndex("FacilitiesDT")));
    // // int FacilityID = cursor.getInt(cursor
    // // .getColumnIndex("FacilityID"));
    //
    // }
    // }
    //
    // }
    private void getScreenedReferrals() {

        String referralQuery = "select * from childrenscreeningreferrals where IsDeleted!=1 and   LocalChildrenScreeningID='"
                + Helper.childScreeningObj.getScreeningID() + "';";
        Cursor referralCursor = dbh.getCursorData(this.getActivity(),
                referralQuery);
        if (referralCursor != null) {
            while (referralCursor.moveToNext()) {
                String childrenScreeningReferralID = referralCursor
                        .getString(referralCursor
                                .getColumnIndex("LocalChildrenScreeningReferralID"));

                Referral screenedReferral = new Referral();
                HealthConditionModel healthCondition = new HealthConditionModel();
                int hID = NumUtil.IntegerParse.parseInt(referralCursor
                        .getString(referralCursor
                                .getColumnIndex("HealthConditonID")));
                healthCondition.setHealthConditionID(hID);
                healthCondition.setHCType(HCTypes.HealthCondition);
                healthCondition.setName(getHealthConditionNameFromMaster(hID));
                screenedReferral.setHealthConditonReferred(healthCondition);
                screenedReferral.setHealthCondtionName(healthCondition
                        .getName());
                screenedReferral.setHealthCondtionId(hID);
                screenedReferral.setComments(referralCursor
                        .getString(referralCursor.getColumnIndex("Comments")));

                String ReferredFacilityID = referralCursor
                        .getString(referralCursor
                                .getColumnIndex("ReferredFacilityID"));
                if (!TextUtils.isEmpty(ReferredFacilityID))
                    screenedReferral.setFacilityID(NumUtil.IntegerParse
                            .parseInt(ReferredFacilityID));

                screenedReferral.setReferralPlaceName("");
                // *** to indicate that is already referred from DB
                screenedReferral.getHealthConditonReferred().setUpdate(true);

                String investigationsQuery = "select * from childrenscreeninginvestigations where IsDeleted!=1 and   LocalChildrenScreeningReferralID='"
                        + childrenScreeningReferralID + "';";
                Cursor investigationsCursor = dbh.getCursorData(
                        this.getActivity(), investigationsQuery);
                String investigationsString = "";
                ArrayList<HashMap<String, String>> investigations = new ArrayList<HashMap<String, String>>();

                if (investigationsCursor != null) {
                    // *** Loop start
                    while (investigationsCursor.moveToNext()) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        String id = investigationsCursor
                                .getString(investigationsCursor
                                        .getColumnIndex("LabInvestigationID"));
                        hashMap.put("id", id);
                        hashMap.put("value", getInvestigationNameFromMaster(id));
                        investigations.add(hashMap);
                        investigationsString += hashMap.get("value") + ",";

                    }
                    investigationsCursor.close();
                    if (investigationsString.endsWith(",")) {
                        investigationsString = investigationsString.substring(
                                0, investigationsString.lastIndexOf(","));
                    }
                }

                screenedReferral.setInvestigationsStr(investigationsString);
                screenedReferral.setInvestigations(investigations);
                investAry.add(screenedReferral);
            }
            referralCursor.close();
        }

        // if (updatedHealthConditionsArray.size() > 0) {
        // updatedHealthConditionsArray.clear();
        // }
    }

    public String getInvestigationNameFromMaster(String id) {
        String investigationName = "";
        for (int i = 0; i < masterInvestigationsArrayList.size(); i++) {
            HashMap<String, String> hashMap = masterInvestigationsArrayList
                    .get(i);
            if (StringUtils.equalsNoCase(hashMap.get("id"), id)) {
                investigationName = hashMap.get("value");
                break;
            }
        }
        return investigationName;
    }

    public String getHealthConditionNameFromMaster(int HealthConditionID) {
        String query = "select * from healthconditions where IsDeleted!=1 and   HealthConditionID='"
                + HealthConditionID + "';";
        Cursor cursor = dbh.getCursorData(this.getActivity(), query);
        String healthConditionName = "";
        if (cursor != null) {
            cursor.moveToNext();
            healthConditionName = cursor.getString(cursor
                    .getColumnIndex("DisplayText"));
            cursor.close();
        }

        return healthConditionName;
    }

    public void updateHealthCondtionsData() {
        /* ll_summary_health_conditions.removeAllViews(); */
        String healthStr = "";
        ArrayList<Referral> referrals_array = new ArrayList<Referral>();

        for (int i = 0; i < healthConditions.size(); i++) {
            healthStr += healthConditions.get(i) + ",";
        }
        if (healthStr.indexOf(",") != -1) {
            healthStr = healthStr.substring(0, healthStr.lastIndexOf(","));
        }

        String query = "select healthconditiongroups.DisplayText,healthconditions.DisplayText,"
                + "healthconditions.HealthConditionGroupID,healthconditions.HealthConditionID from healthconditiongroups inner join healthconditions on"
                + " healthconditions.HealthConditionGroupID=healthconditiongroups.HealthConditionGroupID where healthconditions.IsDeleted!=1 and  healthconditiongroups.IsDeleted!=1 and    healthconditions.HealthConditionID IN ("
                + healthStr + ")";
        if (Helper.childScreeningObj.getScreeningID() == 0)
            query += " or  healthconditions.HealthConditionID IN ("
                    + "select HealthConditonID from ChildrenScreeningReferrals where LocalChildrenScreeningID="
                    + Helper.childScreeningObj.getScreeningID()
                    + " and IsDeleted=0);";
        // String query =
        // "select healthconditiongroups.DisplayText,healthconditions.DisplayText,"
        // +
        // "healthconditions.HealthConditionGroupID,healthconditions.HealthConditionID from healthconditiongroups inner join healthconditions on"
        // +
        // " healthconditions.HealthConditionGroupID=healthconditiongroups.HealthConditionGroupID where healthconditions.IsDeleted!=1 and  healthconditiongroups.IsDeleted!=1 and    healthconditions.HealthConditionID IN ("
        // +
        // "select HealthConditonID from ChildrenScreeningReferrals where LocalChildrenScreeningID="
        // + Helper.childScreeningObj.getScreeningID() + " and IsDeleted=0);";

        data = dbh.getQueryData(this.getActivity(), query);
        updatedHealthConditionsArray.clear();
        if (data != null) {
            // update health conditions spinner

            for (int i = 0; i < data.size(); i++) {
                // TextView tv = new TextView(this.getActivity());

                Referral localReferral = new Referral();
                HealthConditionModel healthConditionObj = new HealthConditionModel();
                healthConditionObj.setHealthConditionID(NumUtil.IntegerParse
                        .parseInt(data.get(i)[3].trim()));
                healthConditionObj.setHCType(HCTypes.HealthCondition);
                healthConditionObj.setName(data.get(i)[1].trim());
                healthConditionObj.setGroupName(data.get(i)[0].trim());
                // tv.setText(healthConditionObj.getName());
                updatedHealthConditionsArray.add(healthConditionObj);
                localReferral.setHealthConditonReferred(healthConditionObj);
                // ll_summary_health_conditions.addView(tv);
                referrals_array.add(localReferral);
            }
        }

        HealthConditionModel healthConditionObj = new HealthConditionModel();
        healthConditionObj.setHealthConditionID(77);
        healthConditionObj.setHCType(HCTypes.Recommendations);
        healthConditionObj.setName("Recommendations");
        healthConditionObj.setGroupName("Recommendations");
        Referral recommReferral = new Referral();
        updatedHealthConditionsArray.add(healthConditionObj);
        recommReferral.setHealthConditonReferred(healthConditionObj);
        referrals_array.add(recommReferral);

        healthConditionObj = new HealthConditionModel();
        healthConditionObj.setHealthConditionID(88);

        healthConditionObj.setHCType(HCTypes.DoctorComments);

        healthConditionObj.setName("Doctors Comments");
        healthConditionObj.setGroupName("Doctors Comments");
        updatedHealthConditionsArray.add(healthConditionObj);
        Referral docComments = new Referral();
        docComments.setHealthConditonReferred(healthConditionObj);
        referrals_array.add(docComments);

        healthConditionObj = new HealthConditionModel();
        healthConditionObj.setHealthConditionID(99);
        healthConditionObj.setHCType(HCTypes.LocalTreatment);
        healthConditionObj.setName("Local Treatment");
        healthConditionObj.setGroupName("Local Treatment");
        updatedHealthConditionsArray.add(healthConditionObj);
        Referral locTreatment = new Referral();
        locTreatment.setHealthConditonReferred(healthConditionObj);
        referrals_array.add(locTreatment);

        if (investAry != null) {
            for (Referral iterated_referral : referrals_array) {
                for (Referral _referral : investAry) {
                    if (_referral.getHealthConditonReferred()
                            .getHealthConditionID() == iterated_referral
                            .getHealthConditonReferred().getHealthConditionID()) {
                        iterated_referral.setHealthConditonReferred(_referral
                                .getHealthConditonReferred());
                        iterated_referral.setComments(_referral.getComments());
                        iterated_referral.setHealthCondtionId(_referral
                                .getHealthCondtionId());

                        iterated_referral.setHealthCondtionId(_referral
                                .getHealthCondtionId());

                        iterated_referral.setFacilityID(_referral
                                .getFacilityID());
                        iterated_referral.setHealthCondtionName(_referral
                                .getHealthCondtionName());
                        iterated_referral.setInvestigations(_referral
                                .getInvestigations());
                        iterated_referral.setInvestigationsStr(_referral
                                .getInvestigationsStr());
                        iterated_referral.setReferalPlaceId(_referral
                                .getReferalPlaceId());
                        iterated_referral.setReferralPlaceName(_referral
                                .getReferralPlaceName());

                    }
                }
            }
        }
        investAry = referrals_array;

        Helper.childScreeningObj.setReferrals(investAry);
        grid_hcDetected.setAdapter(new CustomGridAdapterHC(this.getActivity()));

    }

    /*
     * click event for views
     */

    @Override
    public void onClick(View v) {
        if (v == iv_referral_healthconditions) {
            /*
             * getHealthConditionData(this.getActivity()); ArrayList<Question>
             * allQuestionsList = new ArrayList<Question>(); for (int i = 0; i <
             * Helper.categoryAry.length - 1; i++) { Category iteratedCategory =
             * Helper.categoryAry[i];
             *
             * for (int j = 0; j < iteratedCategory.getQuestions().size(); j++)
             * { Question iteratedQuestion = iteratedCategory.getQuestions()
             * .get(j);
             *
             * allQuestionsList.add(iteratedQuestion); } }
             */

            Intent intent = new Intent(this.getActivity(),
                    ExpandableQuestions.class);
            startActivity(intent);

        } /*
         * else if (v == iv_add_refer) { int errorcount = 0; if
         * (spn_summary_health_condition.getSelectedItemPosition() == 0) {
         * errorcount++; Helper.setErrorForSpinner(spn_summary_health_condition,
         * "Select Health Condition"); } if
         * (spn_summary_place_referral.getSelectedItemPosition() == 0) {
         * errorcount++; Helper.setErrorForSpinner(spn_summary_place_referral,
         * "Select Place of Referral"); } if (errorcount == 0) {
         * addRowToInvestigations();
         *
         * for (int i = 0; i < cb_labInvestigationsAry.length; i++) {
         * cb_labInvestigationsAry[i].setChecked(false); }
         * et_summary_investigate.setText("".trim()); } }
         */ else if (v == iv_examinations_next) {
            /*
             * if (spn_summary_health_condition.getCount() > 1) { new
             * AlertDialog.Builder(this.getActivity()) .setMessage(
             * "One or more identified Health Conditions have not been referred, Please refer the Health Condition(s) to proceed further"
             * ) .setPositiveButton("OK", new DialogInterface.OnClickListener()
             * { public void onClick(DialogInterface dialog, int which) { //
             * continue with delete dialog.cancel(); } }).show(); } else {
             */
            int errorCount = 0;

            for (int i = 0; i < Helper.childScreeningObj.getReferrals().size() - 3; i++) {
                if (Helper.childScreeningObj.getReferrals().get(i)
                        .getHealthConditonReferred().isUpdate()) {
                } else {
                    errorCount = errorCount + 1;
                }
            }
            if (errorCount == 0) {
                Helper.childScreeningObj.setReferrals(investAry);

                ScreeningActivity.tabFlags[5] = true;
                ScreeningActivity.enableHeaderClick(ScreeningActivity.tabFlags);
                ScreeningActivity.view_referral.setBackgroundColor(Color
                        .parseColor("#45cfc1"));
                mScreeningActivity.displayView(ScreeningActivity.tv_signoff,
                        this.getActivity());
            } else {
                Helper.showShortToast(getActivity(),
                        "All referrals are to be referred");
            }
        }
    }

    /**
     *
     */
    public void getHealthConditionData(Context ctx) {
        String healthCondQuery = "Select HealthConditionID,HealthConditionName from healthconditions where IsDeleted!=1  ";
        Cursor instCursor = dbh.getCursorData(ctx, healthCondQuery);
        if (instCursor != null) {
            healthCondList = new ArrayList<HashMap<String, String>>();
            try {
                if (instCursor.moveToFirst()) {
                    do {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("HealthConditionID", instCursor
                                .getString(instCursor
                                        .getColumnIndex("HealthConditionID")));
                        hm.put("HealthConditionName", instCursor
                                .getString(instCursor
                                        .getColumnIndex("HealthConditionName")));
                        healthCondList.add(hm);
                    } while (instCursor.moveToNext());
                }
            } finally {
                instCursor.close();
            }
        }
    }

}
