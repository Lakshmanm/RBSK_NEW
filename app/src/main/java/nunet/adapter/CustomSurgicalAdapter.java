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
import nunet.rbsk.model.Surgery;
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
//* Name   :  CustomSurgicalAdapter.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : Deepika.chevvakula

//* Created Date       :  11-Jun-2015
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
public class CustomSurgicalAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Surgery> surgeryAry;
	private LayoutInflater inflater;

	/**
	 * @param activity
	 * @param disabilityAddMoreList
	 */
	public CustomSurgicalAdapter(Context context, ArrayList<Surgery> surgeryAry) {

		this.mContext = context;
		this.surgeryAry = surgeryAry;
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

		return surgeryAry.size();
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

		final View row = inflater.inflate(R.layout.item_allergies, parent,
				false);

		TextView tv_allergy_year = (TextView) row
				.findViewById(R.id.tv_allergy_year);
		TextView tv_allergy_alleryName = (TextView) row
				.findViewById(R.id.tv_allergy_alleryName);
		TextView tv_allergy_comments = (TextView) row
				.findViewById(R.id.tv_allergy_comments);
		ImageView iv_allergy_delete = (ImageView) row
				.findViewById(R.id.iv_allergy_delete);
		final Surgery surgeryObj = surgeryAry.get(position);

		tv_allergy_year.setText(surgeryObj.getScreeningYear() + "-"
				+ surgeryObj.getScreeningRound());
		tv_allergy_alleryName.setText(surgeryObj.getName());
		tv_allergy_comments.setText(surgeryObj.getComments());
		iv_allergy_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage(
						"Are you sure you want to delete this surgical details?")

						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										surgeryAry.remove(position);
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
