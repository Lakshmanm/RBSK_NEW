//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.screening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nunet.adapter.CustomGridAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Category;
import nunet.rbsk.model.Question;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  ScreeningExaminations.java

//* Type    :

//* Description     :
//* References     :
//* Author    : deepika.chevvakula

//* Created Date       :  25-May-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG
//*****************************************************************************
//* Ver        Date                Code Review By            Observations
//	3.0			30-05-2015				Kiruthika			No Comments
//*****************************************************************************
public class ScreeningExaminations extends Fragment implements OnClickListener {

	private GridView grid_exams;
	private TextView tv_screening_basic_student_name;
	private TextView tv_screening_basic_student_age_sex;
	private int instType;
	private ImageView iv_screening_examination_student_image;

	// private String[] viewClrs;
	// String[] cats, catIdAry;
	Category[] categories;
	// int templeteTypeId = 2;
	DBHelper dbh;
	ImageView iv_screening_examination_next;

	ScreeningActivity mScreeningActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mScreeningActivity = (ScreeningActivity) getActivity();
		dbh = DBHelper.getInstance(this.getActivity());
		instType = Helper.childrenObject.getChildrenInsitute()
				.getInstituteTypeId();
		if ((Helper.childScreeningObj.getScreeningID() != 0)) {
			if (Helper.childScreeningObj.getCategories() == null) {
				getScreenedCategories();
			}
		}
		getExamDetailsFromDB();
		View rootView = inflater.inflate(R.layout.screengrid, container, false);
		// *** Compare with Screening ID

		grid_exams = (GridView) rootView
				.findViewById(R.id.grid_examCategerious);

		iv_screening_examination_next = (ImageView) rootView
				.findViewById(R.id.iv_screening_examination_next);
		iv_screening_examination_next.setOnClickListener(this);
		tv_screening_basic_student_name = (TextView) rootView
				.findViewById(R.id.tv_screening_basic_student_name);
		tv_screening_basic_student_name.setText(Helper.childrenObject
				.getFirstName() + " " + Helper.childrenObject.getLastName());
		tv_screening_basic_student_age_sex = (TextView) rootView
				.findViewById(R.id.tv_screening_basic_student_age_sex);

		int ageInMonths = Helper.childrenObject.getAgeInMonths();
		String message = "";
		message += (ageInMonths / 12) == 0 ? "" : (ageInMonths / 12)
				+ " Years ";
		message += (ageInMonths % 12) == 0 ? "" : (ageInMonths % 12)
				+ " Months, ";
		String text="";
		if( Helper.childrenObject.getGender()!=null){
		  text=message
        + Helper.childrenObject.getGender().getGenderName();
    }else{
      text=message;
    }

		tv_screening_basic_student_age_sex.setText(text);

		iv_screening_examination_student_image = (ImageView) rootView
				.findViewById(R.id.iv_screening_examination_student_image);
		iv_screening_examination_student_image
				.setImageBitmap(Helper.childImage);

		return rootView;
	}

	public void getScreenedCategories() {
		getExamDetailsFromDB();
		ArrayList<Question> screenedQuestions = getQuestionsFromDB();
		String query = "select ScreenQuestionID,ScreenCategoryID from screenquestions SQ where  SQ.IsDeleted!=1 AND " +
      " ScreenTemplateTypeID="
				+ instType;
		Cursor cursor = dbh.getCursorData(this.getActivity(), query);
		HashMap<String, String> hashMap = new HashMap<String, String>();
		while (cursor.moveToNext()) {
			hashMap.put(cursor.getString(0), cursor.getString(1));
		}
		Category[] screenedCategories = Helper.childScreeningObj
				.getCategories();
		for(Category screenedCategory : screenedCategories){
			screenedCategory.setIsVerified(false);
		}
		if (screenedQuestions == null) {
		} else {
			for (Question screenedQuestion : screenedQuestions) {
				String screenCategoryID = hashMap.get(String
						.valueOf(screenedQuestion.getScreenQuestionID()));
				for (Category screenedCategory : screenedCategories) {
					screenedCategory.setIsVerified(true);
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
						break;
					}
				}
			}
		}
		Helper.childScreeningObj.setCategories(screenedCategories);
	}

	public ArrayList<Question> getQuestionsFromDB() {
		// *** Check with the reference of ScreeningSignOff
		String query = "select ScreeningQuestionID,sq.Question,csp.Answer,sq.IsReferredWhenYes,sq.HealthConditionID from childrenscreeningpe CSP inner join screenquestions sq on sq.screenquestionid=CSp.screeningquestionid where  CSP.IsDeleted!=1 AND  LocalChildrenScreeningID='"				+ Helper.childScreeningObj.getScreeningID() + "';";
//		String query = "select * from (SELECT S.ScreenQuestionID as ScreeningQuestionID,S.Question as Question,C.Answer as Answer,S.IsReferredWhenYes as IsReferredWhenYes,S.HealthConditionID as HealthConditionID FROM screenquestions S LEFT OUTER JOIN childrenscreeningpe C ON S .ScreenQuestionID= C.ScreeningQuestionID Where LocalChildrenScreeningID='"
//				+ Helper.childScreeningObj.getScreeningID() + "' GROUP BY S.ScreenQuestionID,S.Question,C.Answer,S.IsReferredWhenYes,S.HealthConditionID UNION SELECT S.ScreenQuestionID,S.Question,C.Answer,S.IsReferredWhenYes,S.HealthConditionID FROM childrenscreeningpe C LEFT OUTER JOIN screenquestions S ON S .ScreenQuestionID= C.ScreeningQuestionID GROUP BY S.ScreenQuestionID,S.Question,C.Answer,S.IsReferredWhenYes,S.HealthConditionID ORDER BY S.ScreenQuestionID DESC) as tab where Answer!='' AND Answer is not null and ScreeningQuestionID is not null";
		Cursor cursor = dbh.getCursorData(this.getActivity(), query);
		if (cursor != null) {
			ArrayList<Question> screenedQuestions = new ArrayList<Question>();
			if (cursor.moveToFirst()) {
				do {
					Question question = new Question();
					question.setScreenQuestionID(NumUtil.IntegerParse.parseInt(cursor
							.getString(cursor
									.getColumnIndex("ScreeningQuestionID"))));
					question.setQuestion(cursor.getString(cursor
							.getColumnIndex("Question")));
					question.setAnswer(cursor.getString(cursor
							.getColumnIndex("Answer")));
					question.setIsReferedWhen(NumUtil.IntegerParse.parseInt(cursor
							.getString(cursor
									.getColumnIndex("IsReferredWhenYes"))));
					question.setHealthConditionID(NumUtil.IntegerParse.parseInt(cursor
							.getString(cursor
									.getColumnIndex("HealthConditionID"))));
					screenedQuestions.add(question);
				} while (cursor.moveToNext());
			}
			cursor.close();
			return screenedQuestions;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		grid_exams.setAdapter(new CustomGridAdapter(this.getActivity()));
		super.onResume();
	}

	public void getExamDetailsFromDB() {
		if (Helper.childScreeningObj.getCategories() == null) {

			String query = "select ScreenCategoryID,DisplayText from screencategories SC where  SC.IsDeleted!=1 AND  ScreenTemplateTypeID='"
					+ instType + "' order by cast (DisplaySequence as integer);";
			List<String[]> data = dbh.getQueryData(this.getActivity(), query);

			// *** Set Categories from Database
			categories = new Category[data.size()];
			for (int i = 0; i < data.size(); i++) {
				Category category = new Category();
				category.setCategoryName(data.get(i)[1]);
				category.setCategoryID(NumUtil.IntegerParse.parseInt(data.get(i)[0]));
				category.setIsVerified(false);
				categories[i] = category;
			}

			/*
			 * //*** Created Other's Category Category category = new
			 * Category(); category.setCategoryName("Others");
			 * category.setCategoryID(99); category.setIsVerified(false);
			 * categories[data.size()]= category;
			 */
			Helper.childScreeningObj.setCategories(categories);
		} else {
			categories = Helper.childScreeningObj.getCategories();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == iv_screening_examination_next) {

			int lastCatPosition = categories.length - 2;
			// ***
			Category lastCategory = categories[lastCatPosition];
			if (lastCategory.getIsVerified()) {
				// *** Send to next Screen.
				// Helper.showShortToast(getActivity(), "Enjoy");
				ScreeningActivity.tabFlags[4] = true;
				ScreeningActivity
						.enableHeaderClick(ScreeningActivity.tabFlags);
				ScreeningActivity.view_screen.setBackgroundColor(Color
						.parseColor("#45cfc1"));
				mScreeningActivity.displayView(ScreeningActivity.tv_referral,
						this.getActivity());
			} else {
				Helper.showShortToast(getActivity(),
						"Please verify all the Examination Categories");
			}
		}
	}
}
