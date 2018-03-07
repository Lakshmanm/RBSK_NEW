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
import nunet.rbsk.model.ChildrenDisabilities;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  CustomDisablityAdapter.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  27-May-2015
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
public class CustomDisablityAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ChildrenDisabilities> childDisablityList;
	private LayoutInflater inflater;

	/**
	 * @param activity
	 * @param disabilityAddMoreList
	 */
	public CustomDisablityAdapter(Context context,
			ArrayList<ChildrenDisabilities> disabilityAddMoreList) {

		this.mContext = context;
		this.childDisablityList = disabilityAddMoreList;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {

		return childDisablityList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final View row = inflater.inflate(R.layout.addstud_disability_list,
				parent, false);

		TextView tv_add_stud_disability_type = (TextView) row
				.findViewById(R.id.tv_add_stud_disability_type);
		TextView tv_add_stud_disability_condition = (TextView) row
				.findViewById(R.id.tv_add_stud_disability_condition);
		TextView tv_add_stud_disability_percentage = (TextView) row
				.findViewById(R.id.tv_add_stud_disability_percentage);
		ImageView iv_disability_delete = (ImageView) row
				.findViewById(R.id.iv_disability_delete);
		tv_add_stud_disability_type.setText(""
				+ childDisablityList.get(position).getDisabilityType()
						.getDisabilityName());
		tv_add_stud_disability_condition.setText(childDisablityList.get(
				position).getSpecificCondition());
		tv_add_stud_disability_percentage.setText(childDisablityList.get(
				position).getDisabilityPercentage());
		iv_disability_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage(
						"Are you sure you want to delete this disability?")

						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										childDisablityList.remove(position);
										notifyDataSetChanged();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();

			}
		});
		return row;
	}

}
