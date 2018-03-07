//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk;

import java.util.ArrayList;
import java.util.HashMap;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Category;
import nunet.rbsk.model.Parent;
import nunet.rbsk.model.Question;

import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  ExpandableQuestions.java

//* Type    :

//* Description     :
//* References     :
//* Author    : deepika.chevvakula

//* Created Date       :  10-Jun-2015
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
public class ExpandableQuestions extends ExpandableListActivity {
  private static ArrayList<HashMap<String, String>> healthCondList;
  private int ParentClickStatus = -1;
  private int ChildClickStatus = -1;
  private ArrayList<Parent> parents;
  private DBHelper dbh;
  ImageView[] iv_expand_display;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setFinishOnTouchOutside(false);

    dbh = DBHelper.getInstance(this);
    getHealthConditionData();
    getExpandableListView().setGroupIndicator(null);
    // /getExpandableListView().setDivider(devider);
    // getExpandableListView().setChildDivider(devider);
    getExpandableListView().setDividerHeight(1);
    getExpandableListView().setPadding(0, 0, 0, 200);
    registerForContextMenu(getExpandableListView());
    ;
    // to open only one item at a time
    getExpandableListView().setOnGroupExpandListener(
      new OnGroupExpandListener() {
        int previousGroup = -1;

        @Override
        public void onGroupExpand(int groupPosition) {
          if (groupPosition != previousGroup)
            getExpandableListView()
              .collapseGroup(previousGroup);
          previousGroup = groupPosition;
        }
      });
    // Creating static data in arraylist
    final ArrayList<Parent> dummyList = buildDummyData();

    // Adding ArrayList data to ExpandableListView values
    iv_expand_display = new ImageView[dummyList.size()];
    loadHosts(dummyList);
  }

  /**
   * here should come your data service implementation
   *
   * @return
   */
  private ArrayList<Parent> buildDummyData() {
    // Creating ArrayList of type parent class to store parent class objects
    final ArrayList<Parent> list = new ArrayList<Parent>();
    int parentCount = 0;
    for (int i = 0; i < healthCondList.size(); i++) {
      final Parent parent = new Parent();

      // Set values in parent class object
      HashMap<String, String> hm = healthCondList.get(i);
      parent.setName((parentCount + 1) + ". "
        + hm.get("HealthConditionName"));
      // parent.setChildren(new ArrayList<Child>());
      System.out.println("Parent Name:::::::::" + parent.getName());
      ArrayList<Question> childAryList = new ArrayList<Question>();
      ArrayList<Question> questionsAry = getQuestions(NumUtil.IntegerParse
        .parseInt(hm.get("HealthConditionID")));
      parent.setChecked(false);
      if (questionsAry.size() > 0) {
        System.out.println("size:::::::::" + questionsAry.size());
        for (int j = 0; j < questionsAry.size(); j++) {
          final Question questionobj = questionsAry.get(j);
          if (NumUtil.IntegerParse.parseInt(questionobj.getAnswer()) == questionobj
            .getIsReferedWhen()) {
            parent.setChecked(true);// update left image of parent
            // to tick.
          }
          childAryList.add(questionobj);
        }

      }
      parent.setChildren(childAryList);
      // Add Child class object to parent class object
      list.add(parent);
      parentCount = parentCount + 1;
    }
    return list;
  }

  public ArrayList<Question> getQuestions(int healthConditionId) {
    ArrayList<Question> allQuestionsList = new ArrayList<Question>();

    Category[] categoriesArray = Helper.childScreeningObj.getCategories();
    for (Category iteratedCategory : categoriesArray) {
      if (iteratedCategory.getQuestions() != null) {
        for (Question iteratedQuestion : iteratedCategory
          .getQuestions()) {
          if (healthConditionId == iteratedQuestion
            .getHealthConditionID()) {
            if (!allQuestionsList.contains(iteratedQuestion)) {
              allQuestionsList.add(iteratedQuestion);
            }
          }
        }
      }
    }
    return allQuestionsList;
  }

  private void loadHosts(final ArrayList<Parent> newParents) {
    if (newParents == null)
      return;

    parents = newParents;

    // Check for ExpandableListAdapter object
    if (this.getExpandableListAdapter() == null) {
      // Create ExpandableListAdapter Object
      final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

      // Set Adapter to ExpandableList Adapter
      this.setListAdapter(mAdapter);
    } else {
      // Refresh ExpandableListView data
      ((MyExpandableListAdapter) getExpandableListAdapter())
        .notifyDataSetChanged();
    }
  }

  /**
   * A Custom adapter to create Parent view (Used grouprow.xml) and Child
   * View((Used childrow.xml).
   */
  private class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;

    public MyExpandableListAdapter() {
      // Create Layout Inflator
      inflater = LayoutInflater.from(ExpandableQuestions.this);
    }

    // This Function used to inflate parent rows view

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parentView) {
      final Parent parent = parents.get(groupPosition);

      // Inflate grouprow.xml file for parent rows
      convertView = inflater.inflate(R.layout.questionparent, parentView,
        false);
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
        900, 700);
      parentView.setLayoutParams(layoutParams);

      LinearLayout ll_expand_title = (LinearLayout) convertView
        .findViewById(R.id.ll_expand_title);
      if (groupPosition == 0) {
        ll_expand_title.setVisibility(View.VISIBLE);
      } else {
        ll_expand_title.setVisibility(View.GONE);
      }
      // Get grouprow.xml file elements and set values
      ((TextView) convertView.findViewById(R.id.tv_healthConditionName))
        .setText(parent.getName());
      ImageView image = (ImageView) convertView
        .findViewById(R.id.iv_expand_parent);
      if (isExpanded) {
        image.setBackgroundDrawable(getResources().getDrawable(
          R.drawable.down_arrow));
      } else {
        image.setBackgroundDrawable(getResources().getDrawable(
          R.drawable.right_arrow));
      }

      iv_expand_display[groupPosition] = (ImageView) convertView
        .findViewById(R.id.iv_expand_display);
      if (parent.isChecked()) {
        iv_expand_display[groupPosition]
          .setBackgroundDrawable(getResources().getDrawable(
            R.drawable.tick));
        iv_expand_display[groupPosition].getLayoutParams().width = 30;
        iv_expand_display[groupPosition].getLayoutParams().height = 30;
      } else {
        iv_expand_display[groupPosition]
          .setBackgroundDrawable(getResources().getDrawable(
            R.drawable.close));
        iv_expand_display[groupPosition].getLayoutParams().width = 30;
        iv_expand_display[groupPosition].getLayoutParams().height = 30;
      }
      // Change right check image on parent at runtime
      return convertView;
    }

    // This Function used to inflate child rows view
    @Override
    public View getChildView(final int groupPosition,
                             final int childPosition, boolean isLastChild, View convertView,
                             final ViewGroup parentView) {
      final Parent parent = parents.get(groupPosition);
      final Question questionObj = parent.getChildren()
        .get(childPosition);

      // Inflate childrow.xml file for child rows
      convertView = inflater.inflate(R.layout.healthquestionitem,
        parentView, false);

      // Get childrow.xml file elements and set values
      ((TextView) convertView.findViewById(R.id.tv_question))
        .setText(questionObj.getQuestion());

      final Button btn_dynamic_yes = (Button) convertView
        .findViewById(R.id.btn_dynamic_yes);
      final Button btn_dynamic_no = (Button) convertView
        .findViewById(R.id.btn_dynamic_no);

      int answer = NumUtil.IntegerParse.parseInt(questionObj.getAnswer());

      if (answer == 1) {
        btn_dynamic_yes.setBackgroundColor(Color.parseColor("#45cfc1"));
        btn_dynamic_no.setBackgroundColor(Color.LTGRAY);
      } else {
        btn_dynamic_no.setBackgroundColor(Color.parseColor("#ff6b6b"));
        btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
      }

      btn_dynamic_yes.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {


          questionObj.setAnswer("1");
          updateQuestion(questionObj);
          btn_dynamic_yes.setBackgroundColor(Color
            .parseColor("#45cfc1"));
          btn_dynamic_no.setBackgroundColor(Color.LTGRAY);
          parent.setChecked(fetchStatusOfHealthCondition(parent));
          if (parent.isChecked()) {
            iv_expand_display[groupPosition]
              .setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.tick));
            iv_expand_display[groupPosition].getLayoutParams().width = 30;
            iv_expand_display[groupPosition].getLayoutParams().height = 30;
          } else {
            iv_expand_display[groupPosition]
              .setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.close));
            iv_expand_display[groupPosition].getLayoutParams().width = 30;
            iv_expand_display[groupPosition].getLayoutParams().height = 30;
          }
        }
      });

      // btn_dynamic_no.setId(count);
      btn_dynamic_no.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {

          updateQuestion(questionObj);
          btn_dynamic_no.setBackgroundColor(Color
            .parseColor("#ff6b6b"));
          btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
          questionObj.setAnswer("0");
          parent.setChecked(fetchStatusOfHealthCondition(parent));
          if (parent.isChecked()) {
            iv_expand_display[groupPosition]
              .setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.tick));
            iv_expand_display[groupPosition].getLayoutParams().width = 30;
            iv_expand_display[groupPosition].getLayoutParams().height = 30;
          } else {
            iv_expand_display[groupPosition]
              .setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.close));
            iv_expand_display[groupPosition].getLayoutParams().width = 30;
            iv_expand_display[groupPosition].getLayoutParams().height = 30;
          }

        }
      });
      return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
      // Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
      return parents.get(groupPosition).getChildren().get(childPosition);
    }

    // Call when child row clicked
    @Override
    public long getChildId(int groupPosition, int childPosition) {
      /****** When Child row clicked then this function call *******/

      // Log.i("Noise",
      // "parent == "+groupPosition+"=  child : =="+childPosition);
      if (ChildClickStatus != childPosition) {
        ChildClickStatus = childPosition;
      }
      return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      int size = 0;
      if (parents.get(groupPosition).getChildren() != null)
        size = parents.get(groupPosition).getChildren().size();
      return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
      Log.i("Parent", groupPosition + "=  getGroup ");

      return parents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
      return parents.size();
    }

    // Call when parent row clicked
    @Override
    public long getGroupId(int groupPosition) {
      Log.i("Parent", groupPosition + "=  getGroupId "
        + ParentClickStatus);

      if (groupPosition == 2 && ParentClickStatus != groupPosition) {

        // Alert to user
        // Toast.makeText(getApplicationContext(),
        // "Parent :" + groupPosition, Toast.LENGTH_LONG).show();
      }

      ParentClickStatus = groupPosition;
      if (ParentClickStatus == 0)
        ParentClickStatus = -1;

      return groupPosition;
    }

    @Override
    public void notifyDataSetChanged() {
      // Refresh List rows
      super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
      return ((parents == null) || parents.isEmpty());
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
      return true;
    }

  }

  private boolean fetchStatusOfHealthCondition(Parent parentObj) {

    for (Question quesObj : parentObj.getChildren()) {
      if (NumUtil.IntegerParse.parseInt(quesObj.getAnswer()) == quesObj
        .getIsReferedWhen()) {
        return true;
      }
    }

    return false;
  }

  public void updateQuestion(Question updatedQuestion) {
    Category[] categoriesArray = Helper.childScreeningObj.getCategories();
    for (Category iteratedCategory : categoriesArray) {
      if (iteratedCategory.getQuestions() != null) {
        for (Question iteratedQuestion : iteratedCategory
          .getQuestions()) {
          if (iteratedQuestion.getScreenQuestionID() == updatedQuestion
            .getScreenQuestionID()) {
            iteratedQuestion = updatedQuestion;
            Helper.childScreeningObj.setCategories(categoriesArray);
            break;
          }
        }
      }
    }
  }

  public void getHealthConditionData() {
    String healthCondQuery = "Select HealthConditionID,DisplayText from healthconditions where IsDeleted!=1 ";
    Cursor instCursor = dbh.getCursorData(this, healthCondQuery);
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
                .getColumnIndex("DisplayText")));
            healthCondList.add(hm);
          } while (instCursor.moveToNext());
        }
      } finally {
        instCursor.close();
      }
    }
  }

}
