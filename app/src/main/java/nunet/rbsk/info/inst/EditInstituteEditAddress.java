package nunet.rbsk.info.inst;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import nunet.adapter.CustomSpinnerAdapter;
import nunet.adapter.DistrictSpinnerAdapter;
import nunet.adapter.HabitationSpinnerAdapter;
import nunet.adapter.MandalSpinnerAdapter;
import nunet.adapter.VillageAdapter;
import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Address;
import nunet.rbsk.model.District;
import nunet.rbsk.model.Habitation;
import nunet.rbsk.model.Mandal;
import nunet.rbsk.model.State;
import nunet.rbsk.model.Village;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  EditInstituteEditAddress

//* Type    : Frgament

//* Description     : To Edit Address info of an Institute
//* References     :
//* Author    :Promodh.munjeti

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

public class EditInstituteEditAddress extends Fragment implements
        OnClickListener {
    private TextView tv_editInst_basicInfo;
    private TextView tv_editInst_address;
    private TextView tv_editInst_staff;
    private Button btn_edit_institute_address_previous;
    private Button btn_edit_institute_address_next;
    private Spinner spn_edit_institute_edit_address_state;
    private Spinner spn_edit_institute_edit_address_district;
    private Spinner spn_edit_institute_edit_address_mandal;
    private Spinner spn_edit_institute_edit_address_village;
    private Spinner spn_edit_institute_edit_address_habitation;
    private EditText et_edit_institute_edit_address_pin_code;
    private EditText et_edit_institute_address_1;
    private EditText et_edit_institute_address_2;
    private Address addressObj;
    private int stateID;
    private int districtID;
    private int mandalID;
    private int villageID;
    private int habitationID;
    private ArrayList<State> stateList;
    private ArrayList<District> districtList;
    private ArrayList<Mandal> mandalList;
    private ArrayList<Village> villageList;
    private ArrayList<Habitation> habitationList;

    private CustomSpinnerAdapter adapter;
    private DistrictSpinnerAdapter disAdapter;
    private MandalSpinnerAdapter mandalAdapter;
    private VillageAdapter villageAdapter;
    private HabitationSpinnerAdapter habitationAdapter;
    private boolean setFlag = false;
    private GoogleMap googleMap;
    private static View rootView;
    private DBHelper dbh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.editinst_address, container,
                    false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dbh = DBHelper.getInstance(this.getActivity());

        getDataFromDB();
        addressObj = Helper.addModelObject;
        findViews(rootView);
        btn_edit_institute_address_previous.setOnClickListener(this);
        btn_edit_institute_address_next.setOnClickListener(this);

        return rootView;
    }

    /*
     * life cycle method of a fragment
     */
    @Override
    public void onAttach(Activity activity) {
        setFlag = false;
        super.onAttach(activity);
    }

    /**
     * To add listeners to spinners
     */
    private void spinnerListners() {
        spn_edit_institute_edit_address_state
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        stateID = stateList.get(position).getStateID();
                        setDataToSpinners(1);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spn_edit_institute_edit_address_district
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        districtID = districtList.get(position).getDistrictID();
                        setDataToSpinners(2);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spn_edit_institute_edit_address_mandal
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        mandalID = mandalList.get(position).getMandalID();
                        /*
						 * panchayatList =
						 * Helper.getPanchayatList(getActivity(), panchayatList,
						 * mandalID); panchayatID = panchayatList.get(position)
						 * .getPanchayatID();
						 */
                        setDataToSpinners(3);
                        // }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spn_edit_institute_edit_address_village
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        villageID = villageList.get(position).getVillageID();
                        setDataToSpinners(4);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spn_edit_institute_edit_address_habitation
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        habitationID = habitationList.get(position)
                                .getHabitatID();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    /**
     * To get the view id's from R.java
     *
     * @param rootView
     */
    private void findViews(View rootView) {
        spn_edit_institute_edit_address_state = (Spinner) rootView
                .findViewById(R.id.spn_edit_institute_edit_address_state);
        spn_edit_institute_edit_address_district = (Spinner) rootView
                .findViewById(R.id.spn_edit_institute_edit_address_district);
        spn_edit_institute_edit_address_mandal = (Spinner) rootView
                .findViewById(R.id.spn_edit_institute_edit_address_mandal);
        spn_edit_institute_edit_address_village = (Spinner) rootView
                .findViewById(R.id.spn_edit_institute_edit_address_village);
        spn_edit_institute_edit_address_habitation = (Spinner) rootView
                .findViewById(R.id.spn_edit_institute_edit_address_habitation);

        spn_edit_institute_edit_address_state.setEnabled(false);
        spn_edit_institute_edit_address_district.setEnabled(false);
        spn_edit_institute_edit_address_mandal.setEnabled(false);
        spn_edit_institute_edit_address_village.setEnabled(false);
        spn_edit_institute_edit_address_habitation.setEnabled(false);

        et_edit_institute_edit_address_pin_code = (EditText) rootView
                .findViewById(R.id.et_edit_institute_edit_address_pin_code);
        et_edit_institute_address_1 = (EditText) rootView
                .findViewById(R.id.et_edit_institute_address_1);
        et_edit_institute_address_2 = (EditText) rootView
                .findViewById(R.id.et_edit_institute_address_2);
        btn_edit_institute_address_previous = (Button) rootView
                .findViewById(R.id.btn_edit_institute_address_previous);
        btn_edit_institute_address_next = (Button) rootView
                .findViewById(R.id.btn_edit_institute_address_next);
        tv_editInst_basicInfo = (TextView) getActivity().findViewById(
                R.id.tv_editInst_basicInfo);
        tv_editInst_address = (TextView) getActivity().findViewById(
                R.id.tv_editInst_address);
        tv_editInst_staff = (TextView) getActivity().findViewById(
                R.id.tv_editInst_staff);

    }

    /**
     * To set Data from DB to Address Model Kiruthika 24-04-2015
     */
    private void setTextToEditAddress() {
        // Set Data to Address Text
        et_edit_institute_address_1.setText(addressObj.getAddressLine1());
        et_edit_institute_address_2.setText(addressObj.getAddressLine2());
        et_edit_institute_edit_address_pin_code
                .setText(addressObj.getPINCode());
        // Get ID from object class
        stateID = addressObj.getState().getStateID();
        districtID = addressObj.getDistrict().getDistrictID();
        mandalID = addressObj.getMandal().getMandalID();
        // panchayatID = addressObj.getPanchayat().getPanchayatID();
        villageID = addressObj.getVillage().getVillageID();
        habitationID = addressObj.getHabitation().getHabitatID();
        setFlag = true;
        double latitude_edit_address = 0;
        double longitude_edit_address = 0;
        String latitude = Helper.insModelObject.getLatitude();
        String longitude = Helper.insModelObject.getLongitude();
        if (!TextUtils.isEmpty(longitude) && !TextUtils.isEmpty(latitude))
            if (latitude.length() == 0 || longitude.length() == 0) {
            } else {
                latitude_edit_address = Double.parseDouble(latitude);
                longitude_edit_address = Double.parseDouble(longitude);
            }
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager()
                        .findFragmentById(
                                R.id.fragment_edit_institute_edit_address_map))
                        .getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(
                    new LatLng(latitude_edit_address, longitude_edit_address))
                    .title(Helper.insModelObject.getInstituteName()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitude_edit_address, longitude_edit_address),
                    17.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDataToSpinners(0);
    }

    /**
     * To add data to spinners and to set the selected default positions from DB
     */
    private void setDataToSpinners(int index) {

        if (index == 0) {// load state data
            stateList = Helper.getStateDataFromDB(getActivity(), stateList);
            adapter = new CustomSpinnerAdapter(getActivity(),
                    R.layout.spinner_rows, stateList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_edit_institute_edit_address_state.setAdapter(adapter);
            if (setFlag) {
                // set state position to spinner
                for (int i = 0; i < stateList.size(); i++) {
                    if (stateList.get(i).getStateID() == stateID) {
                        spn_edit_institute_edit_address_state.setSelection(i,
                                true);
                        break;
                    }
                }
            }
        } else if (index == 1) {
            districtList = Helper.getDistrictDataFromDB(getActivity(),
                    districtList, stateID);
            disAdapter = new DistrictSpinnerAdapter(getActivity(),
                    R.layout.spinner_rows, districtList);
            disAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_edit_institute_edit_address_district.setAdapter(disAdapter);
            if (setFlag) {
                // set district position to spinner
                for (int i = 0; i < districtList.size(); i++) {
                    if (districtList.get(i).getDistrictID() == districtID) {
                        spn_edit_institute_edit_address_district.setSelection(
                                i, true);
                        break;
                    }
                }
            }
        } else if (index == 2) {
            mandalList = Helper.getMandalList(getActivity(), mandalList,
                    districtID);
            mandalAdapter = new MandalSpinnerAdapter(getActivity(),
                    R.layout.spinner_rows, mandalList);
            mandalAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_edit_institute_edit_address_mandal.setAdapter(mandalAdapter);
            if (setFlag) {
                // set mandal position to spinner
                for (int i = 0; i < mandalList.size(); i++) {
                    if (mandalList.get(i).getMandalID() == mandalID) {
                        spn_edit_institute_edit_address_mandal.setSelection(i,
                                true);
                        break;
                    }
                }
            }
        } else if (index == 3) {
            villageList = Helper.getVillageList(getActivity(), villageList,
                    mandalID);
            villageAdapter = new VillageAdapter(getActivity(),
                    R.layout.spinner_rows, villageList);
            villageAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_edit_institute_edit_address_village.setAdapter(villageAdapter);
            if (setFlag) {
                // set village position to spinner
                for (int i = 0; i < villageList.size(); i++) {
                    if (villageList.get(i).getVillageID() == villageID) {
                        spn_edit_institute_edit_address_village.setSelection(i,
                                true);
                        break;
                    }
                }
            }
        } else if (index == 4) {
            habitationList = Helper.getHabitationList(getActivity(),
                    habitationList, villageID);
            habitationAdapter = new HabitationSpinnerAdapter(getActivity(),
                    R.layout.spinner_rows, habitationList);
            habitationAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_edit_institute_edit_address_habitation
                    .setAdapter(habitationAdapter);
            if (setFlag) {
                // set hab position to spinner
                for (int i = 0; i < habitationList.size(); i++) {
                    if (habitationList.get(i).getHabitatID() == habitationID) {
                        spn_edit_institute_edit_address_habitation
                                .setSelection(i, true);
                        break;
                    }
                }
            }
        }
    }

    /**
     * onclick listener for the views
     */
    @Override
    public void onClick(View v) {
        InsituteFragmentActivityDialog activity = (InsituteFragmentActivityDialog) getActivity();
        if (v == btn_edit_institute_address_previous) {

            activity.replaceFragment(activity.fragmentArr[0]);
            Helper.updateHeaderFromNext(getActivity(), tv_editInst_basicInfo,
                    tv_editInst_address, R.drawable.headerbg_selectced,
                    R.drawable.headerbg);

        } else if (v == btn_edit_institute_address_next) {
            // if
            // (spn_edit_institute_edit_address_state.getSelectedItemPosition()
            // == 0) {
            // spn_edit_institute_edit_address_state.setFocusable(true);
            // spn_edit_institute_edit_address_state
            // .setFocusableInTouchMode(true);
            // spn_edit_institute_edit_address_state.requestFocus();
            // Toast.makeText(this.getActivity(), "Please select State",
            // Toast.LENGTH_SHORT).show();
            // } else if (spn_edit_institute_edit_address_district
            // .getSelectedItemPosition() == 0) {
            // spn_edit_institute_edit_address_district.setFocusable(true);
            // spn_edit_institute_edit_address_district
            // .setFocusableInTouchMode(true);
            // spn_edit_institute_edit_address_district.requestFocus();
            // Toast.makeText(this.getActivity(), "Please select District",
            // Toast.LENGTH_SHORT).show();
            // } else if (spn_edit_institute_edit_address_mandal
            // .getSelectedItemPosition() == 0) {
            // spn_edit_institute_edit_address_mandal.setFocusable(true);
            // spn_edit_institute_edit_address_mandal
            // .setFocusableInTouchMode(true);
            // spn_edit_institute_edit_address_mandal.requestFocus();
            // Toast.makeText(this.getActivity(), "Please select Mandal",
            // Toast.LENGTH_SHORT).show();
            // } else if (spn_edit_institute_edit_address_village
            // .getSelectedItemPosition() == 0) {
            // spn_edit_institute_edit_address_village.setFocusable(true);
            // spn_edit_institute_edit_address_village
            // .setFocusableInTouchMode(true);
            // spn_edit_institute_edit_address_village.requestFocus();
            // Toast.makeText(this.getActivity(), "Please select Village",
            // Toast.LENGTH_SHORT).show();
            // } else if (spn_edit_institute_edit_address_habitation
            // .getSelectedItemPosition() == 0) {
            // spn_edit_institute_edit_address_habitation.setFocusable(true);
            // spn_edit_institute_edit_address_habitation
            // .setFocusableInTouchMode(true);
            // spn_edit_institute_edit_address_habitation.requestFocus();
            // Toast.makeText(this.getActivity(), "Please select Habitation",
            // Toast.LENGTH_SHORT).show();
            // } else {
            Helper.updateHeaderFromNext(getActivity(), tv_editInst_address,
                    tv_editInst_staff, R.drawable.headerbg,
                    R.drawable.headerbg_selectced);
            getUpdateDb();
            activity.replaceFragment(activity.fragmentArr[2]);
            // }
        }
    }

    /**
     * Alert to need confirmation from User
     */

    public void confirmationAlert(String message) {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Delete entry")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    /**
     * To update the changes to DB
     */
    public void getUpdateDb() {
        dbh.updateROW(this.getActivity(), "address", new String[]{"StateID",
                        "DistrictID", "MandalID", "VillageID", "HabitatID",
                        "AddressLine1", "AddressLine2 "},
                new String[]{stateID + "".trim(), districtID + "".trim(),
                        mandalID + "".trim(), villageID + "".trim(),
                        habitationID + "".trim(),
                        et_edit_institute_address_1.getText().toString(),
                        et_edit_institute_address_2.getText().toString()},
                "LocalAddressID",
                Helper.addModelObject.getAddressID() + "".trim());
        // new State().setStateID(stateID);
        // new District().setDistrictID(districtID);
        // new Mandal().setMandalID(mandalID);
        // new Panchayat().setPanchayatID(panchayatID);
        // new Village().setVillageID(villageID);
        // new Habitation().setHabitatID(habitationID);
    }

    /**
     * To get the address details of an Institute from DB
     */
    public void getDataFromDB() {

        new AsyncTask<Void, Void, Void>() {


            protected void onPreExecute() {
                Helper.showProgressDialog(getActivity());
            }

            ;

            @Override
            protected Void doInBackground(Void... params) {
                String query = "select  institutes.LocalAddressID,   address.AddressName,address.AddressLine1,"
                        + "address.AddressLine2,address.LandMark,address.PINCode,address.Post,"
                        + "address.HabitatID,habitatas.DisplayText as habitationName ,address.VillageID,villages.DisplayText as villageName,"
                        + "address.PanchayatID,panchayats.DisplayText as panchayatName,address.MandalID, mandals.DisplayText as mandalName,"
                        + "address.DistrictID,districts.DisplayText as districtName,address.StateID,states.DisplayText  as stateName"
                        + " from institutes inner join institutetypes on"
                        + " institutes.InstituteTypeID=institutetypes.InstituteTypeID"
                        + " inner join schoolcategories on institutes.SchoolCategoryID=schoolcategories.SchoolCategoryID"
                        + " inner join address on address.LocalAddressID=institutes.LocalAddressID "
                        + " left join habitatas on address.HabitatID=habitatas.HabitatID"
                        + " left join villages on villages.VillageID=address.VillageID "
                        + " left join panchayats on panchayats.PanchayatID=address.PanchayatID"
                        + " left join mandals on mandals.MandalID=address.MandalID"
                        + " left join districts on districts.DistrictID=address.DistrictID"
                        + " left join states on states.StateID=address.StateID "
                        + " inner join institutecategories on institutecategories.InstituteCategoryID =  institutes.InstituteCategoryID"
                        + " where  institutes.IsDeleted!=1 AND   institutes.InstituteID='"
                        + ((InsituteFragmentActivityDialog) getActivity()).selectedInstituteId
                        + "' LIMIT 1;";
                System.out.println("query::::::" + query);
                Cursor cur = dbh.getCursorData(getActivity(), query);
                if (cur != null) {
                    cur.moveToFirst();
                    Helper.addModelObject = Helper.getAllAddress(cur);
                } else

                    Helper.addModelObject = new Address();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                spinnerListners();
                setTextToEditAddress();
                Helper.progressDialog.dismiss();
            }

        }.execute();

    }
}
