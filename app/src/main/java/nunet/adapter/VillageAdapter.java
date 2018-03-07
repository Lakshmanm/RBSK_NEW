/**
 * 
 */
package nunet.adapter;

import java.util.ArrayList;

import nunet.rbsk.R;
import nunet.rbsk.model.Village;
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
//* Name : VillageAdapter

//* Type : Adpter

//* Description : Custom adapter to add elements to Habitation spinner
//* References :
//* Author :kiruthika.ganesan

//* Created Date : 24-04-2015
//*****************************************************************************
//* MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//* Code Review LOG
//*****************************************************************************
//* Ver Date Code Review By Observations

//*****************************************************************************
@SuppressWarnings("rawtypes")
public class VillageAdapter extends ArrayAdapter<String> {

	private Context activity;
	private ArrayList data;
	private LayoutInflater inflater;
	private Village stateModel;

	@SuppressWarnings("unchecked")
	public VillageAdapter(Context spinnerActivity, int spinnerRows,
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
		stateModel = (Village) data.get(position);
		TextView spn_custom_value = (TextView) row
				.findViewById(R.id.spn_custom_value);
		// Set values for spinner each row
		spn_custom_value.setText(stateModel.getVillageName());
		return row;
	}
}
