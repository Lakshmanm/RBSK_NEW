//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.adapter;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Category;
import nunet.rbsk.screening.DynamicQuestions;
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
public class CustomGridAdapter extends BaseAdapter {

	// String[] catnames, catIdAry;
	// String[] viewidAry;
	// Category[] categories;
	Context context;
	private static LayoutInflater inflater = null;

	public CustomGridAdapter(Context ctx) {
		// /this.categories = categories;
		context = ctx;
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
		Category[] categories = Helper.childScreeningObj.getCategories();
		if (categories != null)
			return categories.length;
		else
			return 0;
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

		return position;
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
	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = inflater.inflate(R.layout.griditem, null);
		TextView textView = (TextView) row.findViewById(R.id.grid_text);
		View view = (View) row.findViewById(R.id.grid_line);
		// *** Category assigning
		Category[] categoriesArray = Helper.childScreeningObj.getCategories();
		final Category category = categoriesArray[position];
		textView.setText(category.getCategoryName());

		if (category.getIsVerified()) {
			view.setBackgroundColor(Color.parseColor("#45Cfc1"));
		} else {
			view.setBackgroundColor(Color.parseColor("#c9c9c9"));
		}

		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DynamicQuestions.class);
				if (position != 0) {
					Category[] categoriesArray = Helper.childScreeningObj
							.getCategories();
					Category previousCategory = categoriesArray[position - 1];
					if (previousCategory.getIsVerified()) {
						intent.putExtra("position", position);
						context.startActivity(intent);
					}
				} else {
					intent.putExtra("position", position);
					context.startActivity(intent);
				}
			}
		});
		return row;
	}

}
