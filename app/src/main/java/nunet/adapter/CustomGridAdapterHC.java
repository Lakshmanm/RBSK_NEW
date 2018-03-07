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
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.HealthConditionModel;
import nunet.rbsk.model.Referral;
import nunet.rbsk.screening.ReferralPopActivityDialog;
import nunet.rbsk.screening.SignOffDocComments;
import nunet.rbsk.screening.SignOffLocalTreatment;
import nunet.rbsk.screening.SignoffRecommendations;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//*****************************************************************************
//* Name   :  CustomGridAdapter.java

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

//*****************************************************************************
public class CustomGridAdapterHC extends BaseAdapter {

	// String[] catnames, catIdAry;
	// String[] viewidAry;
	// Category[] categories;
	Context context;
	private LayoutInflater inflater = null;
	private ArrayList<Referral> updatedHealthConditionsArray;

	public CustomGridAdapterHC(Context ctx) {
		context = ctx;
		updatedHealthConditionsArray = Helper.childScreeningObj.getReferrals();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {

		return updatedHealthConditionsArray.size();
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

	public class Holder {
		TextView tv;
		View view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup) To Update Views
	 */
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// View row = inflater.inflate(R.layout.griditem, parent, false);
		// row = new View(context);
		// // int a=position;
		View row = inflater.inflate(R.layout.griditem, null);
		TextView textView = (TextView) row.findViewById(R.id.grid_text);
		View view = (View) row.findViewById(R.id.grid_line);
		// *** Category assigning
		/*
		 * Category [] categoriesArray =
		 * Helper.childScreeningObj.getCategories(); final Category category =
		 * categoriesArray[position];
		 */
		final Referral referral = updatedHealthConditionsArray.get(position);
		textView.setText(referral.getHealthConditonReferred().getName());

		if (referral.getHealthConditonReferred().isUpdate()) {
			view.setBackgroundColor(Color.parseColor("#45Cfc1"));
		} else {
			view.setBackgroundColor(Color.parseColor("#c9c9c9"));
		}

		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HealthConditionModel healthConditonReferred = referral
						.getHealthConditonReferred();
				Intent intent = null;
				switch (healthConditonReferred.getHCType()) {
				case Recommendations:
					intent = new Intent(context, SignoffRecommendations.class);
					intent.putExtra("position", position);
					context.startActivity(intent);
					break;
				case DoctorComments:
					intent = new Intent(context, SignOffDocComments.class);
					intent.putExtra("position", position);
					context.startActivity(intent);
					break;
				case LocalTreatment:
					intent = new Intent(context, SignOffLocalTreatment.class);
					intent.putExtra("position", position);
					context.startActivity(intent);
					break;
				case HealthCondition:
					intent = new Intent(context,
							ReferralPopActivityDialog.class);
					intent.putExtra("position", position);
					intent.putExtra("HealthConditonReferred",
							healthConditonReferred.getHealthConditionID());
					context.startActivity(intent);
					break;
				default:
					break;
				}

			}
		});
		return row;
	}
}
