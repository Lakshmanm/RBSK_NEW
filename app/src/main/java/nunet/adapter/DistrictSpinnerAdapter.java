/**
 * 
 */
package nunet.adapter;

import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.model.District;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  DistrictSpinnerAdapter

//* Type    : Adpter

//* Description     : Custom adapter to add elements to district spinner
//* References     :                                                        
//* Author    :kiruthika.ganesan

//* Created Date       : 24-04-2015
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
@SuppressWarnings("rawtypes")
public class DistrictSpinnerAdapter extends ArrayAdapter<String> {

	private Context activity;
	private ArrayList data;
	private LayoutInflater inflater;
	private District stateModel;

	@SuppressWarnings("unchecked")
	public DistrictSpinnerAdapter(Context spinnerActivity, int spinnerRows,
			ArrayList stateList) {
		// TODO Auto-generated constructor stub
		super(spinnerActivity, spinnerRows, stateList);
		activity = spinnerActivity;
		data = stateList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	// This funtion called for each row ( Called data.size() times )
	public View getCustomView(final int position, View convertView,
			ViewGroup parent) {

		/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
		View row = inflater.inflate(R.layout.spinner_rows, parent, false);

		/***** Get each Model object from Arraylist ********/
		stateModel = (District) data.get(position);

		TextView spn_custom_value = (TextView) row
				.findViewById(R.id.spn_custom_value);
		spn_custom_value.setText(stateModel.getDistrictName());
		return row;
	}
}