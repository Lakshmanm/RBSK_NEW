package nunet.rbsk.info.inst;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Address;
import nunet.rbsk.model.Contacts;
import nunet.rbsk.model.Institute;

//=============================================================================
//All rights reserved to Nunet Cube Software Solutions.
//THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
//OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
//FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

//*****************************************************************************
//* Name   :  EditInstituteCovredBy

//* Type    : Frgament

//* Description     : To add covered by of an Institute
//* References     :
//* Author    :Deepika.chevvakula

//* Created Date       : 23-04-2015
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

public class EditInstituteSignOff extends Fragment implements OnClickListener {
    private Button btn_edit_institute_signof_previous;
    private Button btn_edit_institute_signof_next;
    private TextView tv_instit_sign_off_name;
    private TextView tv_editInst_coveredby;
    private TextView tv_editInst_signoff;
    private TextView tv_images_count;
    LinearLayout ll_instit_signoff_images, ll_sign_off_cam;
    ImageView iv_inst_signof_image_1, iv_inst_signof_image_2, iv_inst_signof_image_3,
            iv_inst_signof_image_4, iv_inst_signof_image_5;
    ImageView iv_delete_1, iv_delete_2, iv_delete_3,
            iv_delete_4, iv_delete_5;
    RelativeLayout rl_inst_signof_image_1, rl_inst_signof_image_2, rl_inst_signof_image_3,
            rl_inst_signof_image_4, rl_inst_signof_image_5;

    private DBHelper dbh;
    int image_count = 0;
    List<String> imagenames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.editinst_signoff,
                container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dbh = DBHelper.getInstance(this.getActivity());
        Helper.insModelObject = new Institute();
        Helper.addModelObject = new Address();
        Helper.contactModelObject = new ArrayList<Contacts>();
        findViews(rootView);

        getBasicInfoFromDB();

        //  getSignoffImagesFromDB();

        btn_edit_institute_signof_previous.setOnClickListener(this);
        btn_edit_institute_signof_next.setOnClickListener(this);
        iv_delete_1.setOnClickListener(this);
        iv_delete_2.setOnClickListener(this);
        iv_delete_3.setOnClickListener(this);
        iv_delete_4.setOnClickListener(this);
        iv_delete_5.setOnClickListener(this);

        ll_sign_off_cam.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Helper.progressDialog != null && Helper.progressDialog.isShowing()) {
            Helper.progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Helper.progressDialog != null && Helper.progressDialog.isShowing()) {
            Helper.progressDialog.dismiss();
        }
    }


    /**
     * Method to get All basic info from DB Kiruthika 22/04/2015
     */
    public void getBasicInfoFromDB() {

        new AsyncTask<Void, Void, Void>() {


            protected void onPreExecute() {
               Helper.showProgressDialog(getActivity());
            }

            ;

            @Override
            protected Void doInBackground(Void... params) {
                String query = "select institutes.LocalInstituteID,institutes.InstituteID,institutes.InstituteName,institutes.InstituteTypeID,"
                        + " institutetypes.DisplayText as institutetypeName, institutes.DiseCode, "
                        + " institutes.SchoolCategoryID,schoolcategories.DisplayText as schoolcategoryName, institutes.InstituteCategoryID , institutecategories.DisplayText as institutecategoryName,"
                        + " institutes.LocalContactID,institutes.Latitude,institutes.Longitude, "
                        + " address.AddressName,address.LocalAddressID,address.AddressLine1,address.AddressLine2,address.LandMark,address.PINCode,"
                        + " address.Post,address.HabitatID,habitats.DisplayText as habitationName,address.VillageID,villages.DisplayText as villageName, "
                        + " address.PanchayatID,panchayats.DisplayText as panchayatName,address.MandalID,mandals.DisplayText as mandalName,address.DistrictID,"
                        + " districts.DisplayText districtName,address.StateID,states.DisplayText as stateName"
                        + " from institutes inner join institutetypes on institutes.InstituteTypeID=institutetypes.InstituteTypeID"
                        + " inner join schoolcategories on institutes.SchoolCategoryID=schoolcategories.SchoolCategoryID"
                        + " left join address on address.LocalAddressID=institutes.LocalAddressID"
                        + " left join habitats on address.HabitatID=habitats.HabitatID"
                        + " left join villages on villages.VillageID=address.VillageID "
                        + " left join panchayats on panchayats.PanchayatID=address.PanchayatID "
                        + " left join mandals on mandals.MandalID=address.MandalID "
                        + " left join districts on districts.DistrictID=address.DistrictID "
                        + " left join states on states.StateID=address.StateID "
                        + " inner join institutecategories on institutecategories.InstituteCategoryID = "
                        + " institutes.InstituteCategoryID where  institutes.IsDeleted!=1 "
                        +
                        // " AND address.IsDeleted!=1 " +//address change to
                        // left join
                        " AND  institutes.InstituteID="
                        + +((InsituteFragmentActivityDialog) getActivity()).selectedInstituteId
                        + " Limit 1";
                final Cursor cur = dbh.getCursorData(getActivity(), query);
                setToInstituteModel(cur);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                TextView tv_editInst_instName;

                tv_editInst_instName = (TextView) getActivity().findViewById(
                        R.id.tv_editInst_instName);

                Institute insModelObject = Helper.insModelObject;
                if (insModelObject != null) {
                    tv_editInst_instName.setText(insModelObject.getInstituteName());
                    tv_instit_sign_off_name.setText(insModelObject.getInstituteName());
                }
                getSignoffImagesFromDB();

               Helper.progressDialog.dismiss();

            }
        }.execute();

    }

    /**
     * Method to Set data from db to Institute Model class kiruthika 22/04/2015
     *
     * @param
     */
    private void setToInstituteModel(Cursor insCur) {
        if (insCur != null) {
            try {
                while (insCur.moveToNext()) {
                    Helper.insModelObject
                            .setLocalInstituteID(NumUtil.IntegerParse
                                    .parseInt(insCur.getString(insCur
                                            .getColumnIndex("LocalInstituteID"))));
                    Helper.insModelObject
                            .setInstituteServerID(NumUtil.IntegerParse
                                    .parseInt(insCur.getString(insCur
                                            .getColumnIndex("InstituteID"))));
                    Helper.insModelObject.setInstituteName(insCur
                            .getString(insCur.getColumnIndex("InstituteName")));
                    Helper.insModelObject
                            .setInstituteTypeId(NumUtil.IntegerParse.parseInt(insCur
                                    .getString(insCur
                                            .getColumnIndex("InstituteTypeId"))));
                    Helper.insModelObject.setInstituteTypeName(insCur
                            .getString(insCur
                                    .getColumnIndex("institutetypeName")));

                    Helper.insModelObject.setDiseCode(insCur.getString(insCur
                            .getColumnIndex("DiseCode")));
                    Helper.insModelObject
                            .setSchoolCategoryID(NumUtil.IntegerParse.parseInt(insCur.getString(insCur
                                    .getColumnIndex("SchoolCategoryID"))));
                    Helper.insModelObject.setCategoryCode(insCur
                            .getString(insCur
                                    .getColumnIndex("institutecategoryName")));
                    Helper.insModelObject
                            .setInstituteCategoryID(NumUtil.IntegerParse.parseInt(insCur.getString(insCur
                                    .getColumnIndex("InstituteCategoryID"))));
                    Helper.insModelObject.setInstituteCategoryName(insCur
                            .getString(insCur
                                    .getColumnIndex("schoolcategoryName")));
                    String LocalContactID = insCur.getString(insCur
                            .getColumnIndex("LocalContactID"));
                    if (!TextUtils.isEmpty(LocalContactID)) {
                        Helper.insModelObject.setContactID(NumUtil.IntegerParse
                                .parseInt(LocalContactID.trim()));
                    }
                    // Helper.insModelObject
                    // .setContactID(IntUtil.Integer.parseInt(insCur
                    // .getString(insCur
                    // .getColumnIndex("LocalContactID"))));
                    Helper.insModelObject.setLatitude(insCur.getString(insCur
                            .getColumnIndex("Latitude")));
                    Helper.insModelObject.setLongitude(insCur.getString(insCur
                            .getColumnIndex("Longitude")));
                    Helper.addModelObject = Helper.getAllAddress(insCur);
                    Helper.contactModelObject = getDbContacts(Helper.insModelObject
                            .getContactID());
                    // contactModelObject=Helper.getAllContacts(Helper.insModelObject.getContactID());
                    Helper.insModelObject.setAddress(Helper.addModelObject);
                    Helper.insModelObject
                            .setContacts(Helper.contactModelObject);

                }
            } finally {
                insCur.close();
            }
        }

    }

    private ArrayList<Contacts> getDbContacts(long contactID) {
        String contect = null;
        ArrayList<Contacts> dbContacts = new ArrayList<Contacts>();
        String contact_query = "Select ct.ContactTypeID,cd.Contact,cd.LocalContactID,DisplayText  from contactdetails cd "
                + " left join contacttypes ct on ct.contacttypeid =cd.contacttypeid  where cd.IsDeleted!=1 AND cd.localcontactid="
                + contactID;
        Cursor contactCur = dbh
                .getCursorData(this.getActivity(), contact_query);
        if (contactCur != null) {
            try {
                if (contactCur.moveToFirst()) {
                    do {
                        Contacts contactUser = new Contacts();
                        contactUser.setContactTypeID(NumUtil.IntegerParse
                                .parseInt(contactCur.getString(contactCur
                                        .getColumnIndex("ContactTypeID"))));
                        contect = contactCur.getString(contactCur
                                .getColumnIndex("Contact"));
                        contactUser.setContact(contect);
                        contactUser.setContactID(NumUtil.IntegerParse
                                .parseInt(contactCur.getString(contactCur
                                        .getColumnIndex("LocalContactID"))));
                        contactUser.setContactCategoryID(3);
                        contactUser.setContactCategoryName(contactCur
                                .getString(contactCur
                                        .getColumnIndex("DisplayText")));
                        dbContacts.add(contactUser);
                    } while (contactCur.moveToNext());

                }
            } finally {
                contactCur.close();
            }

            return dbContacts;
        } else {
            return null;
        }
    }


    private void getSignoffImagesFromDB() {
        String Query = "Select ImageName , ImagePath FROM  InstituteSignoffPictures " +
                " where IsDeleted!=1 and  LocalInstituteID="
                + Helper.insModelObject.getLocalInstituteID()
                + "";
        Cursor cursor = dbh.getCursorData(this.getActivity(), Query);
        if (cursor != null) {

            image_count = 0;
            imagenames = new ArrayList<>();
            while (cursor.moveToNext()) {

                String ImageName = cursor.getString(cursor
                        .getColumnIndex("ImageName"));
                image_count = image_count + 1;
                imagenames.add(ImageName);
            }
            cursor.close();
        }
        if (imagenames.size() == 1) {
            ll_instit_signoff_images.setVisibility(View.VISIBLE);
            rl_inst_signof_image_1.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(0), iv_inst_signof_image_1);

        } else if (imagenames.size() == 2) {
            ll_instit_signoff_images.setVisibility(View.VISIBLE);
            rl_inst_signof_image_1.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(0), iv_inst_signof_image_1);
            rl_inst_signof_image_2.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(1), iv_inst_signof_image_2);
        } else if (imagenames.size() == 3) {
            ll_instit_signoff_images.setVisibility(View.VISIBLE);
            rl_inst_signof_image_1.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(0), iv_inst_signof_image_1);
            rl_inst_signof_image_2.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(1), iv_inst_signof_image_2);
            rl_inst_signof_image_3.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(2), iv_inst_signof_image_3);
        } else if (imagenames.size() == 4) {
            ll_instit_signoff_images.setVisibility(View.VISIBLE);
            rl_inst_signof_image_1.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(0), iv_inst_signof_image_1);
            rl_inst_signof_image_2.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(1), iv_inst_signof_image_2);
            rl_inst_signof_image_3.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(2), iv_inst_signof_image_3);
            rl_inst_signof_image_4.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(3), iv_inst_signof_image_4);
        } else if (imagenames.size() == 5) {
            ll_instit_signoff_images.setVisibility(View.VISIBLE);
            rl_inst_signof_image_1.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(0), iv_inst_signof_image_1);
            rl_inst_signof_image_2.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(1), iv_inst_signof_image_2);
            rl_inst_signof_image_3.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(2), iv_inst_signof_image_3);
            rl_inst_signof_image_4.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(3), iv_inst_signof_image_4);
            rl_inst_signof_image_5.setVisibility(View.VISIBLE);
            getInstitueImage(imagenames.get(4), iv_inst_signof_image_5);
        }

        if (image_count == 0)
            tv_images_count.setText("Capture Sign Off Images (5)");
        else if (image_count == 1)
            tv_images_count.setText("Capture Sign Off Images (4)");
        else if (image_count == 2)
            tv_images_count.setText("Capture Sign Off Images (3)");
        else if (image_count == 3)
            tv_images_count.setText("Capture Sign Off Images (2)");
        else if (image_count == 4)
            tv_images_count.setText("Capture Sign Off Images (1)");
        else if (image_count == 5)
            tv_images_count.setText("Capture Sign Off Images (0)");
    }


    /**
     * To get the view id's from R.java
     *
     * @param rootView
     */
    private void findViews(View rootView) {
        btn_edit_institute_signof_previous = (Button) rootView
                .findViewById(R.id.btn_edit_institute_signof_previous);
        btn_edit_institute_signof_next = (Button) rootView
                .findViewById(R.id.btn_edit_institute_signof_next);
        tv_instit_sign_off_name = (TextView) rootView.findViewById(
                R.id.tv_instit_sign_off_name);
        tv_editInst_coveredby = (TextView) getActivity().findViewById(
                R.id.tv_editInst_coveredby);
        tv_editInst_signoff = (TextView) getActivity().findViewById(
                R.id.tv_editInst_signoff);
        ll_sign_off_cam = (LinearLayout) rootView
                .findViewById(R.id.ll_sign_off_cam);
        ll_instit_signoff_images = (LinearLayout) rootView
                .findViewById(R.id.ll_instit_signoff_images);
        iv_inst_signof_image_1 = (ImageView) rootView.findViewById(R.id.iv_inst_signof_image_1);
        iv_inst_signof_image_2 = (ImageView) rootView.findViewById(R.id.iv_inst_signof_image_2);
        iv_inst_signof_image_3 = (ImageView) rootView.findViewById(R.id.iv_inst_signof_image_3);
        iv_inst_signof_image_4 = (ImageView) rootView.findViewById(R.id.iv_inst_signof_image_4);
        iv_inst_signof_image_5 = (ImageView) rootView.findViewById(R.id.iv_inst_signof_image_5);
        iv_delete_1 = (ImageView) rootView.findViewById(R.id.iv_delete_1);
        iv_delete_2 = (ImageView) rootView.findViewById(R.id.iv_delete_2);
        iv_delete_3 = (ImageView) rootView.findViewById(R.id.iv_delete_3);
        iv_delete_4 = (ImageView) rootView.findViewById(R.id.iv_delete_4);
        iv_delete_5 = (ImageView) rootView.findViewById(R.id.iv_delete_5);

        rl_inst_signof_image_1 = (RelativeLayout) rootView.findViewById(R.id.rl_inst_signof_image_1);
        rl_inst_signof_image_2 = (RelativeLayout) rootView.findViewById(R.id.rl_inst_signof_image_2);
        rl_inst_signof_image_3 = (RelativeLayout) rootView.findViewById(R.id.rl_inst_signof_image_3);
        rl_inst_signof_image_4 = (RelativeLayout) rootView.findViewById(R.id.rl_inst_signof_image_4);
        rl_inst_signof_image_5 = (RelativeLayout) rootView.findViewById(R.id.rl_inst_signof_image_5);

        tv_images_count = (TextView) rootView.findViewById(R.id.tv_images_count);
    }

    /**
     * onclick listener for the views
     */
    @Override
    public void onClick(View v) {
        InsituteFragmentActivityDialog activity = (InsituteFragmentActivityDialog) getActivity();

        if (v == btn_edit_institute_signof_previous) {
            Helper.updateHeaderFromNext(getActivity(), tv_editInst_signoff,
                    tv_editInst_coveredby, R.drawable.headerbg,
                    R.drawable.headerbg_selectced);
            activity.replaceFragment(activity.fragmentArr[3]);
        } else if (v == btn_edit_institute_signof_next) {
            getActivity().finish();
        } else if (v == ll_sign_off_cam) {
            if (image_count > 4) {
                Helper.showShortToast(getActivity(), "Only 5 Images can be captured");
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission(0);
                } else {
                    Intent imageIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(imageIntent, 0);
                }
            }

        } else if (v == iv_delete_1) {
            deleteImageToDB(imagenames.get(0));
            image_count = image_count - 1;
            if (image_count < 0) {
                image_count = 0;
            }
            rl_inst_signof_image_1.setVisibility(View.GONE);
            if (image_count == 0) {
                ll_instit_signoff_images.setVisibility(View.GONE);
            }
            if (image_count == 0)
                tv_images_count.setText("Capture Sign Off Images (5)");
            else if (image_count == 1)
                tv_images_count.setText("Capture Sign Off Images (4)");
            else if (image_count == 2)
                tv_images_count.setText("Capture Sign Off Images (3)");
            else if (image_count == 3)
                tv_images_count.setText("Capture Sign Off Images (2)");
            else if (image_count == 4)
                tv_images_count.setText("Capture Sign Off Images (1)");
            else if (image_count == 5)
                tv_images_count.setText("Capture Sign Off Images (0)");

        } else if (v == iv_delete_2) {
            deleteImageToDB(imagenames.get(1));
            image_count = image_count - 1;
            if (image_count < 0) {
                image_count = 0;
            }
            if (image_count == 0) {
                ll_instit_signoff_images.setVisibility(View.GONE);
            }
            rl_inst_signof_image_2.setVisibility(View.GONE);
            if (image_count == 0)
                tv_images_count.setText("Capture Sign Off Images (5)");
            else if (image_count == 1)
                tv_images_count.setText("Capture Sign Off Images (4)");
            else if (image_count == 2)
                tv_images_count.setText("Capture Sign Off Images (3)");
            else if (image_count == 3)
                tv_images_count.setText("Capture Sign Off Images (2)");
            else if (image_count == 4)
                tv_images_count.setText("Capture Sign Off Images (1)");
            else if (image_count == 5)
                tv_images_count.setText("Capture Sign Off Images (0)");

        } else if (v == iv_delete_3) {
            deleteImageToDB(imagenames.get(2));
            image_count = image_count - 1;
            if (image_count < 0) {
                image_count = 0;
            }
            if (image_count == 0) {
                ll_instit_signoff_images.setVisibility(View.GONE);
            }
            rl_inst_signof_image_3.setVisibility(View.GONE);
            if (image_count == 0)
                tv_images_count.setText("Capture Sign Off Images (5)");
            else if (image_count == 1)
                tv_images_count.setText("Capture Sign Off Images (4)");
            else if (image_count == 2)
                tv_images_count.setText("Capture Sign Off Images (3)");
            else if (image_count == 3)
                tv_images_count.setText("Capture Sign Off Images (2)");
            else if (image_count == 4)
                tv_images_count.setText("Capture Sign Off Images (1)");
            else if (image_count == 5)
                tv_images_count.setText("Capture Sign Off Images (0)");

        } else if (v == iv_delete_4) {
            deleteImageToDB(imagenames.get(3));
            image_count = image_count - 1;
            if (image_count < 0) {
                image_count = 0;
            }
            if (image_count == 0) {
                ll_instit_signoff_images.setVisibility(View.GONE);
            }
            rl_inst_signof_image_4.setVisibility(View.GONE);
            if (image_count == 0)
                tv_images_count.setText("Capture Sign Off Images (5)");
            else if (image_count == 1)
                tv_images_count.setText("Capture Sign Off Images (4)");
            else if (image_count == 2)
                tv_images_count.setText("Capture Sign Off Images (3)");
            else if (image_count == 3)
                tv_images_count.setText("Capture Sign Off Images (2)");
            else if (image_count == 4)
                tv_images_count.setText("Capture Sign Off Images (1)");
            else if (image_count == 5)
                tv_images_count.setText("Capture Sign Off Images (0)");

        } else if (v == iv_delete_5) {
            deleteImageToDB(imagenames.get(4));
            image_count = image_count - 1;
            if (image_count < 0) {
                image_count = 0;
            }
            if (image_count == 0) {
                ll_instit_signoff_images.setVisibility(View.GONE);
            }
            rl_inst_signof_image_5.setVisibility(View.GONE);
            if (image_count == 0)
                tv_images_count.setText("Capture Sign Off Images (5)");
            else if (image_count == 1)
                tv_images_count.setText("Capture Sign Off Images (4)");
            else if (image_count == 2)
                tv_images_count.setText("Capture Sign Off Images (3)");
            else if (image_count == 3)
                tv_images_count.setText("Capture Sign Off Images (2)");
            else if (image_count == 4)
                tv_images_count.setText("Capture Sign Off Images (1)");
            else if (image_count == 5)
                tv_images_count.setText("Capture Sign Off Images (0)");

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission(int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (index == 0) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
            }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allgranted = false;
        if (requestCode == 101) {
            //check if all permissions are granted

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;

                    break;
                }
            }
            if (allgranted) {
                Intent imageIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(imageIntent, 0);
            } else {
                checkPermission(0);
            }
        } else if (requestCode == 102) {
            //check if all permissions are granted

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;

                    break;
                }
            }
            if (allgranted) {
                Intent imageIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(imageIntent, 1);
            } else {
                checkPermission(0);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (data != null) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                addImageToGallery(bp);
                ll_instit_signoff_images.setVisibility(View.VISIBLE);
                if (bp != null) {
                    if (image_count == 0) {
                        tv_images_count.setText("Capture Sign Off Images (" + (image_count + 4) + ")");
                        image_count = image_count + 1;
                        rl_inst_signof_image_1.setVisibility(View.VISIBLE);
                        iv_inst_signof_image_1.setImageBitmap(bp);
                        iv_inst_signof_image_1.setVisibility(View.VISIBLE);
                    } else if (image_count == 1) {
                        tv_images_count.setText("Capture Sign Off Images (" + (image_count + 2) + ")");
                        image_count = image_count + 1;
                        rl_inst_signof_image_2.setVisibility(View.VISIBLE);
                        iv_inst_signof_image_2.setImageBitmap(bp);
                        iv_inst_signof_image_2.setVisibility(View.VISIBLE);
                    } else if (image_count == 2) {
                        tv_images_count.setText("Capture Sign Off Images (" + (image_count) + ")");
                        image_count = image_count + 1;
                        rl_inst_signof_image_3.setVisibility(View.VISIBLE);
                        iv_inst_signof_image_3.setImageBitmap(bp);
                        iv_inst_signof_image_3.setVisibility(View.VISIBLE);
                    } else if (image_count == 3) {
                        tv_images_count.setText("Capture Sign Off Images (" + (image_count - 2) + ")");
                        image_count = image_count + 1;
                        rl_inst_signof_image_4.setVisibility(View.VISIBLE);
                        iv_inst_signof_image_4.setImageBitmap(bp);
                        iv_inst_signof_image_4.setVisibility(View.VISIBLE);
                    } else if (image_count == 4) {
                        tv_images_count.setText("Capture Sign Off Images (" + (image_count - 4) + ")");
                        image_count = image_count + 1;
                        rl_inst_signof_image_5.setVisibility(View.VISIBLE);
                        iv_inst_signof_image_5.setImageBitmap(bp);
                        iv_inst_signof_image_5.setVisibility(View.VISIBLE);
                    }
                }

            }
        }
    }

    /**
     * @param
     */
    private void addImageToGallery(Bitmap bmp) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DCIM/myCapturedImages");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "FILENAME-" + n + ".jpg";
        if (!imagenames.contains(fname))
            imagenames.add(fname);
        addImageToDB(fname);
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }

    /**
     * @param fname
     */
    private void addImageToDB(String fname) {


        dbh.insertintoTable(getActivity(), "InstituteSignoffPictures",
                new String[]{"LocalInstituteID", "InstituteID", "ImageName",
                        "ImagePath"}, new String[]{
                        String.valueOf(Helper.insModelObject.getLocalInstituteID()),
                        String.valueOf(Helper.insModelObject.getInstituteServerID()),
                        fname, "/DCIM/myCapturedImages"});


    }

    private void deleteImageToDB(String fname) {
        dbh.deleteRowsByCondition(getActivity(), "InstituteSignoffPictures",
                new String[]{"LocalInstituteID", "InstituteID", "ImageName"},
                new String[]{String.valueOf(Helper.insModelObject.getLocalInstituteID()), String.valueOf(Helper.insModelObject.getInstituteServerID()), fname});


    }

    public void getInstitueImage(String fname, ImageView imageView) {

        Bitmap abc = null;
        String root = Environment.getExternalStorageDirectory().toString();
        System.out.println("path in child.........." + root + "/DCIM/myCapturedImages/" + fname);
        File imgFile = new File(root + "/DCIM/myCapturedImages/" + fname);
        if (imgFile.exists()) {
            Bitmap decodeFile = BitmapFactory.decodeFile(imgFile
                    .getAbsolutePath());
            abc = decodeFile;
        }
        if (abc != null)
            imageView.setImageBitmap(abc);
        // ////get Image end////////////////

        System.out.println("bitmap in child.........." + abc);

    }


}
