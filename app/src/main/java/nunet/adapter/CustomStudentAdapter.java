//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================
package nunet.adapter;

import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.helpers.CustomDialog;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.ChildrenScreeningModel;
import nunet.rbsk.screening.ScreeningActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  CustomStudentAdapter.java

//* Type    : 

//* Description     : Class for Collapse student List Animation
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  21-May-2015
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
public class CustomStudentAdapter extends BaseAdapter implements
		OnItemClickListener {

	private Context mContext;
	private ArrayList<Children> studList = new ArrayList<Children>();
	private LayoutInflater inflater;
	public static int lastSelectedPosition = -1;
	public static int childID;
	public static CustomDialog dialog;

	/**
	 * Constructor
	 * 
	 * @param screeningActivity
	 * @param childrenList
	 */

	ScreeningActivity mScreeningActivity;

	public CustomStudentAdapter(Context context,
			ArrayList<Children> childrenList) {
		mScreeningActivity = (ScreeningActivity) context;
		this.mContext = context;
		this.studList = childrenList;
		dialog = new CustomDialog(mContext);
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return studList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	//

	public class ViewHolder {
		TextView tv_screening_student_lv_name;
		TextView iv_screening_student_lv_rool_no;
		ImageView iv_screening_student_lv_image;
		View view;
	}

	int c1 = Color.parseColor("#815CC5");
	int c2 = Color.parseColor("#ff0000");
	int c3 = Color.parseColor("#eeeeee");

	int selcol = Color.parseColor("#45cfc1");
	int defaultcol = Color.parseColor("#625388");

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// Log.e("msg", position + "'" + studList.size());
		if (position >= studList.size()) {
			if (!mScreeningActivity.isLoading) {
				View inflate = inflater.inflate(R.layout.cell_progress, parent,
						false);
				mScreeningActivity.getStudentDataView(inflate);
				return inflate;
			} else {
				View emtpyView = new View(mScreeningActivity);
				emtpyView.setId(R.layout.cell_progress);
				return emtpyView;
			}
		}

		ViewHolder mHolder;// = new ViewHolder();
		if (convertView == null || convertView.getTag() == null) {
			convertView = inflater.inflate(R.layout.screening_student_listview,
					parent, false);
			mHolder = new ViewHolder();
			mHolder.tv_screening_student_lv_name = (TextView) convertView
					.findViewById(R.id.tv_screening_student_lv_name);
			mHolder.iv_screening_student_lv_rool_no = (TextView) convertView
					.findViewById(R.id.iv_screening_student_lv_rool_no);
			mHolder.iv_screening_student_lv_image = (ImageView) convertView
					.findViewById(R.id.iv_screening_student_lv_image);

			mHolder.view = (View) convertView
					.findViewById(R.id.vw_screening_student_lv_highlight);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		Children children = studList.get(position);
		String text = children.getFirstName() + " " + children.getLastName();
		mHolder.tv_screening_student_lv_name.setText(text.toUpperCase());
		mHolder.iv_screening_student_lv_rool_no.setText(children.getMCTSID());

		if (children.getChildimage() != null) {
			mHolder.iv_screening_student_lv_image.setImageBitmap(children
					.getChildimage());
		} else {
			mHolder.iv_screening_student_lv_image.setImageBitmap(null);
			mHolder.iv_screening_student_lv_image
					.setBackgroundResource(R.drawable.kid_image);
		}

		int studentStatus = children.getChildScreenStatusID();
		if (studentStatus == 1) {// screened
			mHolder.view.setBackgroundColor(c1);
		} else if (studentStatus == 4) {// referred
			mHolder.view.setBackgroundColor(c2);
		} else {// unscreened
			mHolder.view.setBackgroundColor(c3);
		}

		// if (lastSelectedPosition != -1) {
		if (children.isSelected()) {
			convertView.setBackgroundColor(selcol);
		} else {
			convertView.setBackgroundColor(defaultcol);
		}

		convertView.setId(position);
		convertView.setOnClickListener(new myClick(position));

		return convertView;
	}

	public class myClick implements OnClickListener {
		private int position;

		public myClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(final View v) {
			CustomStudentAdapter.view = v;
			if (v.findViewById(R.layout.cell_progress) != null)
				return;
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					
					ScreeningActivity.listSelectedPosition = v.getId();
					performClick(position);
				}
			}, 200);

		}
	}

	View row;
	public static View view;

	boolean preventClick = true;

	public void performClick(int selectedPositon) {
		// Helper.childScreeningObj = null;
		if (preventClick) {
			preventClick = false;
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					preventClick = true;
				}
			}, 1000);

		} else
			return;
		if (selectedPositon >= studList.size())
			return;
		if (lastSelectedPosition != -1) {
			studList.get(lastSelectedPosition).setSelected(false);
		}
		studList.get(selectedPositon).setSelected(true);

		// row.setBackgroundColor(Color.parseColor("#45cfc1"));
		final Children currentChildObj = studList.get(selectedPositon);
		currentChildObj.setSelected(true);
		childID = studList.get(selectedPositon).getChildrenID();
		lastSelectedPosition = selectedPositon;
		Helper.childrenObject = currentChildObj;

		if (!checkChildFromChildrenScreening(childID)) {// new Student
			boolean[] tabFlagslocal = { false, false, false, false, false,
					false };
			ScreeningActivity.tabFlags = tabFlagslocal;
			ScreeningActivity.enableHeaderClick(ScreeningActivity.tabFlags);
			ScreeningActivity.view_basicinfo.setBackgroundColor(Color
					.parseColor("#c9c9c9"));
			ScreeningActivity.view_medical_history.setBackgroundColor(Color
					.parseColor("#c9c9c9"));
			ScreeningActivity.view_capture_vitals.setBackgroundColor(Color
					.parseColor("#c9c9c9"));
			ScreeningActivity.view_referral.setBackgroundColor(Color
					.parseColor("#c9c9c9"));
			ScreeningActivity.view_signoff.setBackgroundColor(Color
					.parseColor("#c9c9c9"));
			ScreeningActivity.view_screen.setBackgroundColor(Color
					.parseColor("#c9c9c9"));
			ScreeningActivity.tabFlags[0] = true;
			mScreeningActivity.displayView(
					ScreeningActivity.tv_screening_basicinfo, mContext);

		} else {// already screened student
			boolean[] tabFlagslocal = { true, true, true, true, true, true };
			ScreeningActivity.tabFlags = tabFlagslocal;
			ScreeningActivity.enableHeaderClick(ScreeningActivity.tabFlags);
			ScreeningActivity.view_basicinfo.setBackgroundColor(Color
					.parseColor("#45cfc1"));
			ScreeningActivity.view_medical_history.setBackgroundColor(Color
					.parseColor("#45cfc1"));
			ScreeningActivity.view_capture_vitals.setBackgroundColor(Color
					.parseColor("#45cfc1"));
			ScreeningActivity.view_referral.setBackgroundColor(Color
					.parseColor("#45cfc1"));
			ScreeningActivity.view_signoff.setBackgroundColor(Color
					.parseColor("#45cfc1"));
			ScreeningActivity.view_screen.setBackgroundColor(Color
					.parseColor("#45cfc1"));
			ScreeningActivity.tabFlags[0] = true;
			mScreeningActivity.displayView(
					ScreeningActivity.tv_screening_basicinfo, mContext);

		}
		notifyDataSetChanged();
		// bundle.putInt("ChildrenID", childID);
		// Fragment fragment = new ScreeningBasicInfoFragment();
		// fragment.setArguments(bundle);
		// ScreeningActivity.listSelectedPosition = selectedPositon;
		// if (fragment != null) {
		// android.app.FragmentManager fragmentManager = ((Activity) mContext)
		// .getFragmentManager();
		// fragmentManager.beginTransaction()
		// .replace(R.id.frame_container, fragment).commit();
		// }

	}

	public boolean checkChildFromChildrenScreening(int childId) {
		DBHelper dbh = DBHelper.getInstance(mContext);
		String query = "select CS.ChildrenScreeingStatusID,CS.LocalChildrenScreeningID  from childrenscreening CS where CS.IsDeleted!=1 AND  LocalChildrenID='"
				+ childId
				+ "' "
				+ "and LocalInstituteScreeningDetailID='"
				+ ((ScreeningActivity) (mContext)).locInsScreeningDetailID
				+ "'";
		Cursor cursor = dbh.getCursorData(mContext, query);
		if (cursor == null) {// new Student
			Helper.childScreeningObj = null;
			Helper.childrenObject.setScreenedForCurrentRound(false);
			return false;
		} else {// screened student
			boolean isScreened = true;
			Helper.childScreeningObj = new ChildrenScreeningModel();
			cursor.moveToFirst();
			int ChildrenScreeingStatusID = NumUtil.IntegerParse.parseInt(cursor
					.getString(cursor
							.getColumnIndex("ChildrenScreeingStatusID")));

			if (ChildrenScreeingStatusID == 3||ChildrenScreeingStatusID == 0) {
				isScreened = false;
				Helper.childrenObject.setScreenedForCurrentRound(false);
			} else {
				Helper.childrenObject.setScreenedForCurrentRound(true);
			}

			Helper.childrenObject
					.setChildScreenStatusID(ChildrenScreeingStatusID);

			String childrenScreeningIDFromDB = cursor.getString(cursor
					.getColumnIndex("LocalChildrenScreeningID"));
			Helper.childScreeningObj.setScreeningID(NumUtil.IntegerParse
					.parseInt(childrenScreeningIDFromDB));
			cursor.close();
			return isScreened;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ScreeningActivity.listSelectedPosition = position;
		if (CustomStudentAdapter.view != null) {
			CustomStudentAdapter.view.setBackgroundColor(defaultcol);
			if (view.findViewById(R.layout.cell_progress) != null)
				return;
		}
		CustomStudentAdapter.view = view;
		performClick(position);

	}

	public void notifyDataSetChanged(ArrayList<Children> childrenList) {
		synchronized (studList) {
			studList = childrenList;
			notifyDataSetChanged();
		}

	}

}
