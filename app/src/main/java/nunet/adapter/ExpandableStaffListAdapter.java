/**
 * 
 */
package nunet.adapter;

/**
 * @author Kiruthika.Ganesan
 *ExpandableStaffListAdapter.java27-Apr-2015
 */
import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.Institutestaff;
import nunet.rbsk.model.Users;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  ExpandableStaffListAdapter

//* Type    : Adpter

//* Description     : To add expandable view for staff details
//* References     :                                                        
//* Author    :kiruthika.ganesan

//* Created Date       : 27-04-2015
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

public class ExpandableStaffListAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
	private Context context;
	private ArrayList<Institutestaff> staffParentList;

	private int hcCount = 0, shcCount = 0;

	// This Function used to inflate parent rows view

	public ExpandableStaffListAdapter(Context mContext,
			ArrayList<Institutestaff> parentList) {
		// TODO Auto-generated constructor stub
		this.context = mContext;
		this.staffParentList = parentList;

	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, final ViewGroup parentView) {
		final Institutestaff parent = staffParentList.get(groupPosition);
		inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.staff_group_row, parentView,
				false);
		// Get grouprow.xml file elements and set values
		((TextView) convertView.findViewById(R.id.tv_staff_name))
				.setText(parent.getFirstName());
		((TextView) convertView.findViewById(R.id.tv_staff_department))
				.setText(parent.getDepartmentName());
		((TextView) convertView.findViewById(R.id.tv_staff_designation))
				.setText(parent.getDesignationName());
		ImageView image = (ImageView) convertView
				.findViewById(R.id.iv_staff_info);

		// Get grouprow.xml file checkbox elements
		CheckBox cb_staff_headmaster = (CheckBox) convertView
				.findViewById(R.id.cb_staff_headmaster);
		CheckBox cb_staff_schoolHealthCoord = (CheckBox) convertView
				.findViewById(R.id.cb_staff_schoolHealthCoord);
		CheckBox cb_health_coord = (CheckBox) convertView
				.findViewById(R.id.cb_health_coord);

		boolean hmFlag = parent.isStaffAssignHeadMaster();
		cb_staff_headmaster.setChecked(hmFlag);
		cb_staff_schoolHealthCoord
				.setChecked(parent.isStaffAssignSchoolCoord());
		cb_health_coord.setChecked(parent.isStaffAssignHealthCoord());
		

		if (parent.getDepartmentID() == 1) {// health
			cb_staff_headmaster.setEnabled(false);
			cb_staff_schoolHealthCoord.setEnabled(false);
			cb_health_coord.setEnabled(true);
		} else if (parent.getDepartmentID() == 2) {// Education
			cb_staff_headmaster.setEnabled(true);
			cb_staff_schoolHealthCoord.setEnabled(true);
			cb_health_coord.setEnabled(false);
		}

		if (hmFlag) {
			//cb_staff_headmaster.setEnabled(true);
			cb_staff_headmaster.setEnabled(false);
			cb_staff_schoolHealthCoord.setEnabled(true);
			cb_health_coord.setEnabled(false);
		} else {
			cb_staff_headmaster.setEnabled(false);
		}

		cb_staff_schoolHealthCoord
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						if (isChecked) {
							if (shcCount >= 3) {
								Helper.showShortToast(context,
										"Only 3 persons can be school health coordinatoes");

								buttonView.setChecked(false);
							} else {
								staffParentList.get(groupPosition)
										.setStaffAssignSchoolCoord(true);
								shcCount++;
								buttonView.setChecked(true);
							}
						} else {
							staffParentList.get(groupPosition)
									.setStaffAssignSchoolCoord(false);
							shcCount--;
						}
					}
				});

		cb_health_coord
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						if (isChecked) {
							if (hcCount >= 3) {
								Helper.showShortToast(context,
										"Only 3 persons can be health coordinatoes");
								buttonView.setChecked(false);
							} else {
								staffParentList.get(groupPosition)
										.setStaffAssignHealthCoord(true);
								hcCount++;
								buttonView.setChecked(true);
							}

						} else {
							staffParentList.get(groupPosition)
									.setStaffAssignHealthCoord(false);
							hcCount--;
						}
					}
				});

		parentView.setEnabled(false);

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (isExpanded)
					((ExpandableListView) parentView)
							.collapseGroup(groupPosition);
				else
					((ExpandableListView) parentView).expandGroup(
							groupPosition, true);
			}
		});

		return convertView;
	}

	// This Function used to inflate child rows view
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parentView) {
		final Users parent = staffParentList.get(groupPosition);
		final Contacts child = parent.getContacts().get(childPosition);

		// Inflate childrow.xml file for child rows
		convertView = inflater.inflate(R.layout.staff_child_row, parentView,
				false);

		// Get childrow.xml file elements and set values
		((TextView) convertView.findViewById(R.id.tv_staff_phoneno))
				.setText(child.getContactCategoryName() + " - "
						+ child.getContact());
		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
		return staffParentList.get(groupPosition).getContacts()
				.get(childPosition);
	}

	// Call when child row clicked
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		/****** When Child row clicked then this function call *******/

		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = 0;
		if (staffParentList.get(groupPosition).getContacts() != null)
			size = staffParentList.get(groupPosition).getContacts().size();
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return staffParentList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return staffParentList.size();
	}

	// Call when parent row clicked
	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public void notifyDataSetChanged() {
		// Refresh List rows
		super.notifyDataSetChanged();
	}

	@Override
	public boolean isEmpty() {
		return ((staffParentList == null) || staffParentList.isEmpty());
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
