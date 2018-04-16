//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.screening;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nunet.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Category;
import nunet.rbsk.model.Question;

//*****************************************************************************
//* Name   :  DynamicQuestions.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  26-May-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG                    
//*****************************************************************************                    
//* Ver        Date                Code Review By            Observations
//	3.0			30-05-2015			Kiruthika					No Comments
//*****************************************************************************
public class DynamicQuestions extends Activity implements OnClickListener {

    private TextView tv_dynamic_moduleName;
    private ImageView iv_dynamic_previous, iv_dynamic_next;
    private LinearLayout ll_dynamicQuestions;
    Button btn_questions_close;
    private int catId = 0;
    private int instType;
    DBHelper dbh;
    // private TextView[] tv_quesAry;
    Category selectedCategory;
    int position = 0;
    ArrayList<Question> questions;
    private int genderId;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.dynamicquestions);
        findViews();
        dbh = DBHelper.getInstance(this);
        position = getIntent().getIntExtra("position", 0);
        instType = Helper.childrenObject.getChildrenInsitute()
                .getInstituteTypeId();
        genderId = Helper.childrenObject.getGenderID();
        age = Helper.childrenObject.getAgeInMonths();
        updateView();
    }

    @SuppressLint("InflateParams")
    private void LoadQuestionsFromDb() {
        Helper.showProgressDialog(DynamicQuestions.this);
        int count = 0;
        ll_dynamicQuestions.removeAllViews();
        if (selectedCategory.getQuestions() == null) {
            String genderName;
            if (genderId == 1) {
                genderName = "IsApplicableToMale";
            } else {
                genderName = "IsApplicabletoFemale";
            }
            // String query =
            // "select  s.Question,s.ScreenQuestionID,s.DisplayOrder,s.HealthConditionID,s.IsReferredWhenYes,"
            // + "s.IsReferredWhenNo from screenquestions  s"
            // + " inner join agegroups ag on ag.AgeGroupID = s.AgeGroupID"
            // +
            // " where  s.IsDeleted!=1 AND  ag.IsDeleted!=1 AND  ScreenTemplateTypeID="
            // + instType
            // + " and  ScreenCategoryID="
            // + catId
            // + " and "
            // + genderName
            // + " = 1 and  "
            // + age
            // + " between ag.RangeStartNo and ag.RangeEndNo";
            // RangeStartNoTemp ,RangeEndNoTemp,
            String query = "select Question,ScreenQuestionID,DisplayOrder,HealthConditionID,IsReferredWhenYes,IsReferredWhenNo from "
                    + "(select AgeGroupID,IsDeleted,"
                    + " (case when IsYear=1 THEN RangeStartNo*12 ELSE RangeStartNo END) as RangeStartNoTemp,"
                    + " (case when IsYear=1 THEN RangeEndNo*12 ELSE RangeEndNo END) as RangeEndNoTemp from AgeGroups ) as tab"
                    + " inner join screenquestions s on tab.AgeGroupID = s.AgeGroupID "
                    + "Where s.IsDeleted!=1 AND tab.IsDeleted!=1"
                    + " AND ScreenTemplateTypeID="
                    + instType
                    + " and "
                    + genderName
                    + "=1 "
                    + " and ScreenCategoryID="
                    + catId
                    + " and "
                    + age
                    + " between RangeStartNoTemp and RangeEndNoTemp";

			/*
             * String query =
			 * "select Question,ScreenQuestionID,[Order],HealthConditionID,IsReferredWhenYes,IsReferredWhenNo"
			 * + " from  screenquestions where ScreenCategoryID='" + catId +
			 * "' and ScreenTemplateTypeID='" + instType + "'";
			 */
            List<String[]> quesitonsData = dbh.getCursorFromQueryData(this,
                    query);
            questions = new ArrayList<Question>();
            if (quesitonsData != null) {
                // tv_quesAry = new TextView[quesitonsData.size()];
                for (int i = 0; i < quesitonsData.size(); i++) {
                    Question question = new Question();
                    question.setQuestion(quesitonsData.get(i)[0]);
                    question.setScreenQuestionID(NumUtil.IntegerParse
                            .parseInt(quesitonsData.get(i)[1]));
                    question.setOrder(NumUtil.IntegerParse
                            .parseInt(quesitonsData.get(i)[2]));
                    if (quesitonsData.get(i)[3] == null) {
                        question.setHealthConditionID(0);
                    } else {
                        if (!TextUtils.isEmpty(quesitonsData.get(i)[3])) {
                            question.setHealthConditionID(NumUtil.IntegerParse
                                    .parseInt(quesitonsData.get(i)[3]));
                        } else {
                            question.setHealthConditionID(0);
                        }
                    }
                    // *** Setting answer's default as No for referedYes and as
                    // Yes
                    if (quesitonsData.get(i)[4] != null) {
                        if (!quesitonsData.get(i)[4].equalsIgnoreCase("NULL")) {
                            if (NumUtil.IntegerParse.parseInt(quesitonsData
                                    .get(i)[4]) == 1) {
                                question.setIsReferedWhen(1);
                                question.setAnswer("".trim() + 0);
                            } else {
                                question.setIsReferedWhen(0);
                                question.setAnswer("".trim() + 1);

                            }
                        } else {
                            question.setIsReferedWhen(0);
                            question.setAnswer("".trim() + 1);
                        }
                    } else {
                        question.setIsReferedWhen(0);
                        question.setAnswer("".trim() + 1);
                    }
                    questions.add(question);
                }
            }
        } else {
            questions = selectedCategory.getQuestions();
        }

        // *** set Questions and Answers to UI
        for (final Question iterable_question : questions) {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.questionitem, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_question);
            textView.setText((count + 1) + "."
                    + iterable_question.getQuestion());

            final Button btn_dynamic_yes = (Button) view
                    .findViewById(R.id.btn_dynamic_yes);
            final Button btn_dynamic_no = (Button) view
                    .findViewById(R.id.btn_dynamic_no);
            // *** set the buttons with background colors wrt to answers i.e 0 =
            // No & 1 = Yes

            if (StringUtils.equalsNoCase(iterable_question.getAnswer(), "1")) {
                btn_dynamic_yes.setBackgroundColor(Color.parseColor("#45cfc1"));
                btn_dynamic_no.setBackgroundColor(Color.LTGRAY);
            } else {
                btn_dynamic_no.setBackgroundColor(Color.parseColor("#ff6b6b"));
                btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
            }

            btn_dynamic_yes.setId(count);
            btn_dynamic_yes.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    btn_dynamic_yes.setBackgroundColor(Color
                            .parseColor("#45cfc1"));
                    btn_dynamic_no.setBackgroundColor(Color.LTGRAY);
                    iterable_question.setAnswer("1");
                }
            });

            btn_dynamic_no.setId(count);
            btn_dynamic_no.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    btn_dynamic_no.setBackgroundColor(Color
                            .parseColor("#ff6b6b"));
                    btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
                    iterable_question.setAnswer("0");
                }
            });
            count++;

            ll_dynamicQuestions.addView(view);
        }
        Helper.progressDialog.dismiss();
    }

    /**
     * to find views from R.java
     */
    private void findViews() {
        tv_dynamic_moduleName = (TextView) findViewById(R.id.tv_dynamic_moduleName);
        iv_dynamic_previous = (ImageView) findViewById(R.id.iv_dynamic_previous);
        iv_dynamic_previous.setOnClickListener(this);
        iv_dynamic_next = (ImageView) findViewById(R.id.iv_dyamic_next);
        iv_dynamic_next.setOnClickListener(this);
        ll_dynamicQuestions = (LinearLayout) findViewById(R.id.ll_dynamicQuestions);
        btn_questions_close = (Button) findViewById(R.id.btn_questions_close);
        btn_questions_close.setOnClickListener(this);
    }

    /*
     * click event for listeners
     */
    @Override
    public void onClick(View v) {
        selectedCategory.setIsVerified(true);
        if (v == btn_questions_close) {
            selectedCategory.setQuestions(questions);
            finish();
        } else if (v == iv_dynamic_previous) {
            selectedCategory.setQuestions(questions);
            position--;
            updateView();
        } else if (v == iv_dynamic_next) {
            selectedCategory.setQuestions(questions);
            position++;
            updateView();
        }
    }

    /*
     * To update the view for others,previous and next
     */
    @SuppressLint("InflateParams")
    public void updateView() {
        Category[] categoriesArray = Helper.childScreeningObj.getCategories();
        if (position >= categoriesArray.length)
            return;
        selectedCategory = categoriesArray[position];
        catId = selectedCategory.getCategoryID();
        tv_dynamic_moduleName.setText(selectedCategory.getCategoryName());

        if (position == 0) {
            iv_dynamic_previous.setVisibility(View.GONE);
        } else {
            iv_dynamic_previous.setVisibility(View.VISIBLE);
        }

        if (position == (categoriesArray.length - 1)) {
            iv_dynamic_next.setVisibility(View.GONE);
        } else {
            iv_dynamic_next.setVisibility(View.VISIBLE);
        }

        if (catId == 22 || catId == 23) {
            ll_dynamicQuestions.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View other_view = inflater.inflate(R.layout.examination_others,
                    null);

            EditText et_examination_others = (EditText) other_view
                    .findViewById(R.id.et_examination_others);
            et_examination_others.setText(Helper.childrenObject
                    .getScreeningComments());
            btn_questions_close.setText("Save");
            et_examination_others.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Helper.childrenObject.setScreeningComments(s.toString());
                }
            });
            btn_questions_close.setBackgroundColor(Color.parseColor("#45cfc1"));
            ll_dynamicQuestions.addView(other_view);
        } else {
            LoadQuestionsFromDb();
        }

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
