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
import nunet.rbsk.model.Allergy;
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
public class CustomAllergyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Allergy> allergyAry;
	private LayoutInflater inflater;

	/**
	 * @param activity
	 * @param disabilityAddMoreList
	 */
	public CustomAllergyAdapter(Context context, ArrayList<Allergy> allergyAry) {

		this.mContext = context;
		this.allergyAry = allergyAry;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {

		return allergyAry.size();
	}

	/*
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {

		return null;
	}

	/*
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {

		return 0;
	}

	/*
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */

	public class ViewHOlder {
		TextView tv_allergy_year;
		TextView tv_allergy_alleryName;
		TextView tv_allergy_comments;
		ImageView iv_allergy_delete;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHOlder mHOlder = null;
		if (convertView == null) {
			mHOlder = new ViewHOlder();
			convertView = inflater.inflate(R.layout.item_allergies, parent,
					false);
			mHOlder.tv_allergy_year = (TextView) convertView
					.findViewById(R.id.tv_allergy_year);
			mHOlder.tv_allergy_alleryName = (TextView) convertView
					.findViewById(R.id.tv_allergy_alleryName);
			mHOlder.tv_allergy_comments = (TextView) convertView
					.findViewById(R.id.tv_allergy_comments);
			mHOlder.iv_allergy_delete = (ImageView) convertView
					.findViewById(R.id.iv_allergy_delete);
			convertView.setTag(mHOlder);
		} else {
			mHOlder = (ViewHOlder) convertView.getTag();
		}

		final Allergy allergyObj = allergyAry.get(position);

		mHOlder.tv_allergy_year.setText(allergyObj.getScreeningYear() + "-"
				+ allergyObj.getScreeningRound());
		mHOlder.tv_allergy_alleryName.setText(allergyObj.getName());
		mHOlder.tv_allergy_comments.setText(allergyObj.getComments());
		mHOlder.iv_allergy_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage(
						"Are you sure you want to delete this allergy?")

						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										allergyAry.remove(position);
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
		return convertView;
	}

}
