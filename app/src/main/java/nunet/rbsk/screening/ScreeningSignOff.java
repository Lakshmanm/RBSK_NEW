//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.screening;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunet.utils.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.UserLoginActivity;
import nunet.rbsk.model.Allergy;
import nunet.rbsk.model.Category;
import nunet.rbsk.model.Children;
import nunet.rbsk.model.ChildrenScreeningModel;
import nunet.rbsk.model.FamilyHistoryDisease;
import nunet.rbsk.model.Question;
import nunet.rbsk.model.Recommendations;
import nunet.rbsk.model.Referral;
import nunet.rbsk.model.ScreeningVitals;
import nunet.rbsk.model.SignOffScreenModel;
import nunet.rbsk.model.Surgery;

//*****************************************************************************
//* Name   :  ScreeningSignOff.java

//* Type    :

//* Description     :
//* References     :              actionsoverlay.xml
//* Author    : deepika.chevvakula

//* Created Date       :  01-Jun-2015
//*****************************************************************************
//*                             MODIFICATION LOG
//*****************************************************************************
//* Ver Date

//*
//*
//*                             Code Review LOG
//*****************************************************************************
//* Ver        Date                Code Review By            Observations
// 3.0			06-08-2015	           Promodh					No Comments
//*****************************************************************************
public class ScreeningSignOff extends Fragment implements OnClickListener {

    private Spinner spn_sign_off_screened_by;
    private Spinner spn_sign_off_inst_coordinator;
    private ArrayAdapter<String> adp_screenedBy, adp_instCoordinator;
    private Button btn_sign_off_save_proceed;
    private DBHelper dbh;
    private TextView tv_screening_basic_student_name;
    private String gender;
    // private ImageView iv_screening_sign_off_cam;
    private TextView tv_screening_basic_sign_off_sex;
    private ImageView iv_screening_sign_off_image;
    private boolean tempImages;
    private String tempName;
    private LinearLayout ll_sign_off_cam;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.screening_sign_off,
                container, false);
        dbh = DBHelper.getInstance(this.getActivity());
    /*
     * if (Helper.childScreeningObj.getScreeningID() != 0) {
		 * getScreenedSignoffDataFromDB(); } if
		 * (Helper.childScreeningObj.getSignOffModel() == null) {
		 * SignOffScreenModel signOffModel = new SignOffScreenModel();
		 * Helper.childScreeningObj.setSignOffModel(signOffModel); }
		 */

        findViews(rootView);
        String path = checkDbforImage();
        if (TextUtils.isEmpty(path)) {
            ll_sign_off_cam.setVisibility(View.VISIBLE);
            btn_sign_off_save_proceed.setVisibility(View.VISIBLE);
        } else {
            ll_sign_off_cam.setVisibility(View.VISIBLE);
            btn_sign_off_save_proceed.setVisibility(View.VISIBLE);
        }
        // updateRecommendations();
        // updateDoctorComments();
        // updateLocalTreatment();

        updateScrennedBy();
        getScreendBY();

        updateInsttituteCoordinator();
        if (Helper.childrenObject.getGender() != null)
            gender = Helper.childrenObject.getGender().getGenderName();
        else
            gender = "";

        int ageInMonths = Helper.childrenObject.getAgeInMonths();
        String message = "";
        message += (ageInMonths / 12) == 0 ? "" : (ageInMonths / 12)
                + " Years ";
        message += (ageInMonths % 12) == 0 ? "" : (ageInMonths % 12)
                + " Months, ";
        tv_screening_basic_sign_off_sex.setText(message + gender);

        return rootView;
    }

    // private boolean isScreendByUpdate = false;

    private void getScreendBY() {
        String query = "SELECT ScreenedBy FROM childrenscreening cs where  cs.IsDeleted!=1 AND  LocalChildrenID="
                + Helper.childrenObject.getChildrenID();

        Cursor cursor = dbh.getCursorData(this.getActivity(), query);
        // isScreendByUpdate = false;

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex("ScreenedBy");
            while (cursor.moveToNext()) {
                screenedByVal = cursor.getString(columnIndex);
            }
        }
        if (TextUtils.isEmpty(screenedByVal) || cursor == null) {
            SharedPreferences sharedpreferences = getActivity()
                    .getSharedPreferences(UserLoginActivity.UserLogin,
                            Context.MODE_PRIVATE);
            screenedByVal = sharedpreferences.getString("LoginUserID", null);
        }

        for (int i = 0; i < mListSingoffUserId.size(); i++) {

            if (TextUtils.equals(screenedByVal, mListSingoffUserId.get(i))) {
                spn_sign_off_screened_by.setSelection(i);
                // isScreendByUpdate = true;
                break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String type_add = "";
        if (requestCode == 0) {
            type_add = "add";
        } else {
            type_add = "edit";
        }

        if (resultCode != 0) {
            if (data != null) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                addImageToGallery(bp, type_add);
                if (bp != null) {
                    Bitmap croppedBitmap = RoundedImageView.getCroppedBitmap(
                            bp, 65);
                    Helper.childImage = croppedBitmap;
                    iv_screening_sign_off_image.setImageBitmap(croppedBitmap);
                }
                if (Helper.childScreeningObj == null) {
                    Helper.childScreeningObj = new ChildrenScreeningModel();
                }

                // this.getActivity().finish();
                // String path = checkDbforImage();
                // String root = Environment.getExternalStorageDirectory()
                // .toString();
                // File imgFile = new File(root + "/DCIM/myCapturedImages/" +
                // path);
                // if (imgFile.exists()) {
                // Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
                // .getAbsolutePath());
                // iv_screening_sign_off_image.setImageBitmap(myBitmap);
                // }
            }
        }
    }

    /**
     * @param
     */
    private void addImageToGallery(Bitmap bmp, String type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DCIM/myCapturedImages");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "FILENAME-" + n + ".jpg";
        addImageToDB(fname, type);
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
    private void addImageToDB(String fname, String type) {
        if (type.equals("add")) {
            if (Helper.childScreeningObj.getScreeningID() != 0) {
                dbh.insertintoTable(getActivity(), "childrenscreeningpictures",
                        new String[]{"LocalChildrenScreeningID", "ImageName",
                                "ImagePath"}, new String[]{
                                Helper.childScreeningObj.getScreeningID() + "",
                                fname, "/DCIM/myCapturedImages"});
            } else {
        /*
         * dbh.insertintoTable(getActivity(),
				 * "childrenscreeningpicturesTemp", new String[] {
				 * "LocalChildrenID", "ImageName", "ImagePath" }, new String[] {
				 * Helper.childrenObject.getChildrenID() + "", fname,
				 * "/DCIM/myCapturedImages" });
				 */
                tempImages = true;
                tempName = fname;
            }
        } else {
            dbh.updateROW(getActivity(), "childrenscreeningpictures",
                    new String[]{"ImageName"}, new String[]{fname},
                    "LocalChildrenScreeningID",
                    Helper.childScreeningObj.getScreeningID() + "");
        }
    }

	/*
   * public void getScreenedSignoffDataFromDB() { SignOffScreenModel
	 * signOffModel; if (Helper.childScreeningObj.getSignOffModel() == null) {
	 * signOffModel = new SignOffScreenModel();
	 * Helper.childScreeningObj.setSignOffModel(signOffModel); } signOffModel =
	 * Helper.childScreeningObj.getSignOffModel(); // *** Recommendations
	 *
	 * String reccomandationsQuery =
	 * "select * from childrenscreeningrecommendations where LocalChildrenScreeningID='"
	 * + Helper.childScreeningObj.getScreeningID() + "';"; Cursor
	 * recommendationsCursor = dbh.getCursorData(this.getActivity(),
	 * reccomandationsQuery); if (recommendationsCursor != null) { if
	 * (recommendationsCursor.moveToFirst()) { do { Recommendations
	 * signOffRecommendations = signOffModel .getRecommendations(); if
	 * (signOffRecommendations == null) { signOffRecommendations = new
	 * Recommendations(); } signOffRecommendations.setDiet(recommendationsCursor
	 * .getString(recommendationsCursor .getColumnIndex("Diet")));
	 * signOffRecommendations .setPersonalHygine(recommendationsCursor
	 * .getString(recommendationsCursor .getColumnIndex("personalHygiene")));
	 * signOffRecommendations.setOralHygine(recommendationsCursor
	 * .getString(recommendationsCursor .getColumnIndex("OralHygience")));
	 * signOffRecommendations.setMedications(recommendationsCursor
	 * .getString(recommendationsCursor
	 * .getColumnIndex("PresribedMedication")));
	 * signOffRecommendations.setOthers(recommendationsCursor
	 * .getString(recommendationsCursor .getColumnIndex("OtherComments"))); //
	 * *** Doctor Comments
	 *
	 * signOffModel.setDoctorComments(recommendationsCursor
	 * .getString(recommendationsCursor .getColumnIndex("DoctorComments")));
	 * signOffModel.setRecommendations(signOffRecommendations); } while
	 * (recommendationsCursor.moveToNext());
	 *
	 * } }
	 *
	 * // *** Local Treatment. String localTreatmentQuery =
	 * "select * from childrenscreeninglocaltreatment where LocalChildrenScreeningID='"
	 * + Helper.childScreeningObj.getScreeningID() + "';"; Cursor
	 * localTreatCursor = dbh.getCursorData(this.getActivity(),
	 * localTreatmentQuery); if (localTreatCursor != null) { if
	 * (localTreatCursor.moveToFirst()) { do {
	 *
	 * signOffModel.setMedicationsGiven(localTreatCursor
	 * .getString(localTreatCursor .getColumnIndex("MedicationGiven")));
	 * signOffModel.setDiagnosis(localTreatCursor .getString(localTreatCursor
	 * .getColumnIndex("Diagnosis"))); } while (localTreatCursor.moveToNext());
	 * } } recommendationsCursor.close(); // localTreatCursor.close();
	 * Helper.childScreeningObj.setSignOffModel(signOffModel);
	 *
	 * }
	 */

    /**
     * to update spinner values of Institute coordinators
     */
    private void updateScrennedBy() {
        adp_screenedBy = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item);
        adp_screenedBy
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp_screenedBy.add("Select Doctor");
        mListSingoffUserId.add("-1");
        // String query =
        // "select users.UserID ,FirstName ,users.LastName from usercredentials inner join users on users.LocalUserID=usercredentials.localuserId";
        String query = "select u.UserID ,u.FirstName ,u.LastName from mhtstaff m"
                + " inner join users u on u.localuserid=m.localuserid where  u.IsDeleted!=1 AND  m.IsDeleted!=1  ";

        Cursor cursor = dbh.getCursorData(this.getActivity(), query);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex("FirstName");
            int columnIndex2 = cursor.getColumnIndex("LastName");
            int ciUserid = cursor.getColumnIndex("UserID");
            while (cursor.moveToNext()) {

                String FirstName = cursor.getString(columnIndex);
                String LastName = cursor.getString(columnIndex2);
                String userid = cursor.getString(ciUserid);

                adp_screenedBy
                        .add(TextUtils.isEmpty(FirstName) ? "NA" : FirstName
                                + " "
                                + (TextUtils.isEmpty(LastName) ? "" : LastName));

                mListSingoffUserId.add(userid);
            }
            cursor.close();
        }
        spn_sign_off_screened_by.setAdapter(adp_screenedBy);
        // spn_sign_off_screened_by
        // .setOnItemSelectedListener(new OnItemSelectedListener() {
        //
        // @Override
        // public void onItemSelected(AdapterView<?> parent,
        // View view, int position, long id) {
        // mListSingoffSelectedPos = position;
        // }
        //
        // @Override
        // public void onNothingSelected(AdapterView<?> parent) {
        //
        // }
        // });
    }

    // public static int mListSingoffSelectedPos;
    ArrayList<String> mListSingoffUserId = new ArrayList<String>();

    ArrayList<String> mListInstCoordinatorUserId = new ArrayList<String>();
    private String screenedByVal;

    /**
     * to update spinner values of Screened by
     */
    private void updateInsttituteCoordinator() {
        adp_instCoordinator = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item);
        adp_instCoordinator
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp_instCoordinator.add("Select Institute Coordinator");
        mListInstCoordinatorUserId.add("-1");
        String query = "";
        if (Helper.childrenObject.getChildrenInsitute().getInstituteTypeId() == 1) {// AWC
            query = "select users.FirstName ,users.LastName from institutestaff"
                    + " inner join users on users.LocalUserID=institutestaff.localuserId"
                    + " where institutestaff.IsSchoolHealthCoordinator=1 AND  institutestaff.IsDeleted!=1  AND  users.IsDeleted!=1  and institutestaff.LocalInstituteID='"
                    + Helper.childrenObject.getChildrenInsitute()
                    .getInstituteID() + "';";
        } else {// School
            query = "select users.FirstName ,users.LastName from institutestaff "
                    + " inner join users on users.LocalUserID=institutestaff.localuserId"
                    + " where institutestaff.IsAWCCoordinator=1 AND institutestaff.IsDeleted!=1 AND  users.IsDeleted!=1  and institutestaff.LocalInstituteID='"
                    + Helper.childrenObject.getChildrenInsitute()
                    .getInstituteID() + "';";
        }

        Cursor cursor = dbh.getCursorData(this.getActivity(), query);
        if (cursor != null) {
            int ciUserid = cursor.getColumnIndex("UserID");
            while (cursor.moveToNext()) {
                adp_instCoordinator.add(cursor.getString(cursor
                        .getColumnIndex("FirstName"))
                        + " "
                        + cursor.getString(cursor.getColumnIndex("LastName")));

                mListInstCoordinatorUserId.add(cursor.getString(ciUserid));
            }
            cursor.close();
        }
        spn_sign_off_inst_coordinator.setAdapter(adp_instCoordinator);
    }

    /**
     * to update recommendations data
     */
  /*
   * private void updateRecommendations() { Recommendations
	 * recommendationsObj;
	 *
	 * if (Helper.childScreeningObj.getSignOffModel().getRecommendations() ==
	 * null) { recommendationsObj = new Recommendations();
	 * Helper.childScreeningObj.getSignOffModel().setRecommendations(
	 * recommendationsObj); } else { recommendationsObj =
	 * Helper.childScreeningObj.getSignOffModel() .getRecommendations(); }
	 * String recommendationsStr = ""; if
	 * (!recommendationsObj.getDiet().trim().equals("")) { recommendationsStr +=
	 * "Diet" + ","; } if
	 * (!recommendationsObj.getPersonalHygine().trim().equals("")) {
	 * recommendationsStr += "Personal Hygiene" + ","; } if
	 * (!recommendationsObj.getOralHygine().trim().equals("")) {
	 * recommendationsStr += "Oral Hygiene" + ","; } if
	 * (!recommendationsObj.getMedications().trim().equals("")) {
	 * recommendationsStr += "Medications" + ","; } if
	 * (!recommendationsObj.getOthers().trim().equals("")) { recommendationsStr
	 * += "Others" + ","; } if (!recommendationsStr.trim().equals("")) {
	 * recommendationsStr = recommendationsStr.substring(0,
	 * recommendationsStr.lastIndexOf(",")); }
	 * tv_sign_off_recommendations.setText(recommendationsStr); // } }
	 */

    /**
     * to find views from R.java
     */
    private void findViews(View rootView) {
        ll_sign_off_cam = (LinearLayout) rootView
                .findViewById(R.id.ll_sign_off_cam);
        ll_sign_off_cam.setOnClickListener(this);
        tv_screening_basic_sign_off_sex = (TextView) rootView
                .findViewById(R.id.tv_screening_basic_sign_off_sex);

        tv_screening_basic_student_name = (TextView) rootView
                .findViewById(R.id.tv_screening_sign_off_name);
        if (Helper.childrenObject != null)
            tv_screening_basic_student_name
                    .setText(Helper.childrenObject.getFirstName() + " "
                            + Helper.childrenObject.getLastName());

		/*
     * iv_screening_sign_off_cam = (ImageView) rootView
		 * .findViewById(R.id.iv_screening_sign_off_cam);
		 * iv_screening_sign_off_cam.setOnClickListener(this);
		 */
        spn_sign_off_screened_by = (Spinner) rootView
                .findViewById(R.id.spn_sign_off_screened_by);
        spn_sign_off_inst_coordinator = (Spinner) rootView
                .findViewById(R.id.spn_sign_off_inst_coordinator);

        btn_sign_off_save_proceed = (Button) rootView
                .findViewById(R.id.btn_sign_off_save_proceed);
        btn_sign_off_save_proceed.setOnClickListener(this);
        iv_screening_sign_off_image = (ImageView) rootView
                .findViewById(R.id.iv_screening_sign_off_image);

        iv_screening_sign_off_image.setImageBitmap(Helper.childImage);
    }

    /*
       * click events for views
       */
    @Override
    public void onClick(View v) {
        if (v == btn_sign_off_save_proceed) {
            if (spn_sign_off_screened_by.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please select Screened By",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            new Loader(this.getActivity()).execute();
        } else if (v == ll_sign_off_cam) {

            String path = checkDbforImage();
            if (TextUtils.isEmpty(path)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission(0);
                } else {
                    Intent imageIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(imageIntent, 0);
                }


            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this.getActivity());
                alertDialogBuilder
                        .setMessage("Do you want to replace profile picture?");

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    checkPermission(1);
                                } else {
                                    Intent imageIntent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(imageIntent, 1);
                                }

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            ll_sign_off_cam.setVisibility(View.VISIBLE);
            btn_sign_off_save_proceed.setVisibility(View.VISIBLE);
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


    /**
     * @return
     */
    private String checkDbforImage() {
        String path = "";
        String query_Wards = "SELECT ImageName FROM  childrenscreeningpictures  where    IsDeleted!=1 and  LocalChildrenScreeningID='"
                + Helper.childScreeningObj.getScreeningID() + "'";
        Cursor dataCursor = dbh.getCursorData(this.getActivity(), query_Wards);
        if (dataCursor != null && dataCursor.moveToFirst()) {
            path = dataCursor.getString(dataCursor.getColumnIndex("ImageName"));
        } else {
            path = "";
        }
        return path;
    }

    public class Loader extends AsyncTask<String, Context, Void> {


        private Context targetCtx;

        public Loader(Context context) {
            this.targetCtx = context;
            progressDialog = new ProgressDialog(targetCtx);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Updating Screening data...");
            progressDialog.setTitle("Please wait");
            progressDialog.setIndeterminate(true);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            // Do Your WORK here
            insertDataToDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                final ListView localListView = ScreeningActivity.ll_screening_list_students;
                final View v;
                final int lvpos = ScreeningActivity.listSelectedPosition + 1;// Modified
                // By
                // Thriveni

                v = localListView.getChildAt(lvpos);

                if (ScreeningActivity.childrenList.size() > ScreeningActivity.listSelectedPosition + 1) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            targetCtx);
                    alertDialogBuilder.setTitle("Information!");
                    alertDialogBuilder
                            .setMessage(
                                    "Child Screened Successfully...Proceeding to next child")
                            .setCancelable(false)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {

                                            dialog.cancel();
                                            ((ScreeningActivity) getActivity()).getStudentDataFromDBFromsignof(((ScreeningActivity) getActivity()).instituteID, "");
                                            int nextPosition = (ScreeningActivity.listSelectedPosition + 1);
                                            localListView
                                                    .performItemClick(
                                                            localListView
                                                                    .getAdapter()
                                                                    .getView(
                                                                            nextPosition,
                                                                            null,
                                                                            null),
                                                            nextPosition,
                                                            localListView
                                                                    .getAdapter()
                                                                    .getItemId(
                                                                            nextPosition));

                                            localListView.getAdapter().getView(
                                                    lvpos, v, localListView);

                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            targetCtx);
                    alertDialogBuilder.setTitle("Information!");
                    alertDialogBuilder
                            .setMessage("Child Screened Successfully...")
                            .setCancelable(false)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Intent in = new Intent(getActivity(), ScreeningActivity.class);
                                            Bundle mBundle = getActivity().getIntent().getExtras();
                                            getActivity().finish();
                                            in.putExtras(mBundle);
                                            startActivity(in);

                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            }

            // ((ScreeningActivity) getActivity()).getStudentDataFromDB(((ScreeningActivity) getActivity()).instituteID, "");

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     *
     */
    private void insertDataToDB() {
        ScreeningActivity mScreeningActivity = (ScreeningActivity) getActivity();

        String query = "select LocalChildrenScreeningID from childrenscreening where   IsDeleted!=1 AND LocalChildrenScreeningID='"
                + Helper.childScreeningObj.getScreeningID() + "';";
        Cursor dataCursor = dbh.getCursorData(this.getActivity(), query);

        int childScreenStatusID = 0;

        if ((Helper.childScreeningObj.getReferrals() != null)
                && (Helper.childScreeningObj.getReferrals().size() > 3)) {
            childScreenStatusID = 4;
        } else {
            childScreenStatusID = 1;
        }
//        if (Helper.childScreeningObj.getSignOffModel() != null) {
//            if (Helper.childScreeningObj.getSignOffModel().getMedicationsGiven() != null)
//                childScreenStatusID = 6;
//        }

        Children childrenObject = Helper.childrenObject;
        if (dataCursor != null && dataCursor.getCount() > 0) {
            String localChildScreeningID = Helper.childScreeningObj
                    .getScreeningID() + "".trim();
            // already screened--update data
            // -----------Family History---------------

            boolean updatechildscreen = dbh.updateROW(
                    this.getActivity(),
                    "childrenscreening",
                    new String[]{"ScreeningTemplateTypeID",
                            // "ScreeningStartDateTime",
                            "ScreeningEndDateTime", "ChildrenScreenStatusID",
                            "ScreeningComments", "ScreenedBy","LastCommitedDate"},
                    new String[]{
                            childrenObject.getChildrenInsitute()
                                    .getInstituteTypeId() + "".trim(),
                            // childrenObject.getScreningStartTime(),
                            childrenObject.getScreningEndTime(),
                            childScreenStatusID + "".trim(),
                            childrenObject.getScreeningComments(),
                            mListSingoffUserId.get(spn_sign_off_screened_by
                                    .getSelectedItemPosition()),Helper.getTodayDateTime1()},
                    "LocalChildrenScreeningID", localChildScreeningID);

            long localChildScreeningID1 = 0;
            if (!updatechildscreen) {
                localChildScreeningID1 = dbh
                        .insertintoTable(
                                this.getActivity(),
                                "childrenscreening",
                                new String[]{
                                        "LocalInstituteScreeningDetailID",
                                        "LocalChildrenID",
                                        "ScreeningTemplateTypeID",
                                        "ScreeningStartDateTime",
                                        "ScreeningEndDateTime",
                                        "ChildrenScreenStatusID",
                                        "ScreeningComments", "ScreenedBy","LastCommitedDate"},
                                new String[]{
                                        String.valueOf(mScreeningActivity.locInsScreeningDetailID),
                                        childrenObject.getChildrenID()
                                                + "".trim(),
                                        childrenObject.getChildrenInsitute()
                                                .getInstituteTypeId()
                                                + "".trim(),
                                        childrenObject.getScreningStartTime(),
                                        childrenObject.getScreningEndTime(),
                                        childScreenStatusID + "".trim(),
                                        childrenObject.getScreeningComments(),
                                        mListSingoffUserId.get(spn_sign_off_screened_by
                                                .getSelectedItemPosition()),Helper.getTodayDateTime1()});

                localChildScreeningID = String.valueOf(localChildScreeningID1);
                Helper.childScreeningObj
                        .setScreeningID((int) localChildScreeningID1);
            }

            if (Helper.childScreeningObj.getFamilyHistoryDiseases() != null) {
                ArrayList<FamilyHistoryDisease> familyHistoryDiseases = Helper.childScreeningObj
                        .getFamilyHistoryDiseases();

                dbh.updateROW(getActivity(), "childscreeningfh",
                        new String[]{"IsDeleted","LastCommitedDate"}, new String[]{"1",Helper.getTodayDateTime1()},
                        "LocalChildrenScreeningID",
                        String.valueOf(localChildScreeningID));
                for (int i = 0; i < familyHistoryDiseases.size(); i++) {
                    FamilyHistoryDisease fhDisease = familyHistoryDiseases
                            .get(i);
                    if (fhDisease.isSelected()) {
                        boolean flag = dbh.updateROWByValues(
                                this.getActivity(),
                                "childscreeningfh",
                                new String[]{"HasHistory",
                                        "FamilyMemberRelationID", "Notes",
                                        "IsDeleted","LastCommitedDate"},
                                new String[]{

                                        valueOf(fhDisease.isSelected()),
                                        fhDisease.getRelationID() + "".trim(),
                                        fhDisease.getDiseaseComments(), "0",Helper.getTodayDateTime1()},
                                new String[]{"LocalChildrenScreeningID",
                                        "FamilyHistoryID",},
                                new String[]{localChildScreeningID,
                                        fhDisease.getDiseaseID() + "".trim()});
                        if (!flag) {
                            dbh.insertintoTable(
                                    this.getActivity(),
                                    "childscreeningfh",
                                    new String[]{"LocalChildrenScreeningID",
                                            "FamilyHistoryID", "HasHistory",
                                            "FamilyMemberRelationID", "Notes",
                                            "IsDeleted","LastCommitedDate"},
                                    new String[]{
                                            localChildScreeningID + "".trim(),
                                            fhDisease.getDiseaseID()
                                                    + "".trim(),
                                            valueOf(fhDisease.isSelected()),
                                            fhDisease.getRelationID()
                                                    + "".trim(),
                                            fhDisease.getDiseaseComments(), "0",Helper.getTodayDateTime1()});
                        }
                    }
                }
            }

            // ------------------Medical History-------------------------

            if (Helper.childScreeningObj.getMedicalHistoryScreenModel() != null) {
                // ------------------Allergy--------------------
                if (Helper.childScreeningObj.getMedicalHistoryScreenModel()
                        .getAllergies() != null) {
                    ArrayList<Allergy> allergyAry = new ArrayList<Allergy>();
                    allergyAry = Helper.childScreeningObj
                            .getMedicalHistoryScreenModel().getAllergies();
                    dbh.updateROW(this.getActivity(),
                            "childrenscreeningallergies",
                            new String[]{"IsDeleted","LastCommitedDate"}, new String[]{"1",Helper.getTodayDateTime1()},
                            "LocalChildrenScreeningID", localChildScreeningID
                                    + "".trim());
                    for (int i = 0; i < allergyAry.size(); i++) {

                        dbh.insertintoTable(this.getActivity(),
                                "childrenscreeningallergies", new String[]{
                                        "LocalChildrenScreeningID",
                                        "AllergyID", "Comments", "IsDeleted","LastCommitedDate"},
                                new String[]{localChildScreeningID,
                                        allergyAry.get(i).getId() + "".trim(),
                                        allergyAry.get(i).getComments(), "0",Helper.getTodayDateTime1()});
                    }
                }

                // ------------------surgical-------------------
                if (Helper.childScreeningObj.getMedicalHistoryScreenModel()
                        .getSurgeries() != null) {
                    ArrayList<Surgery> surgeryAry = new ArrayList<Surgery>();
                    surgeryAry = Helper.childScreeningObj
                            .getMedicalHistoryScreenModel().getSurgeries();
                    dbh.updateROW(this.getActivity(),
                            "childrenscreeningsurgicals",
                            new String[]{"IsDeleted","LastCommitedDate"}, new String[]{"1",Helper.getTodayDateTime1()},
                            "LocalChildrenScreeningID", localChildScreeningID
                                    + "".trim());
                    for (int i = 0; i < surgeryAry.size(); i++) {
                        dbh.insertintoTable(
                                this.getActivity(),
                                "childrenscreeningsurgicals",
                                new String[]{"LocalChildrenScreeningID",
                                        "SurgicalID", "Comments", "IsDeleted","LastCommitedDate"},
                                new String[]{localChildScreeningID,
                                        surgeryAry.get(i).getId() + "".trim(),
                                        surgeryAry.get(i).getComments(), "0",Helper.getTodayDateTime1()});

                    }
                }
            }
            // -----------------------Vitals-----------------------------

            ScreeningVitals vitals = Helper.childScreeningObj.getVitals();

            // should pass based on insttute templete id
            // int screenTemplatesTypeID = 1;
            boolean updateViatls = false;
            if (vitals != null) {
                updateViatls = dbh.updateROW(
                        this.getActivity(),
                        "childrenscreeningvitals",
                        new String[]{"Height", "HeightIndication", "Weight",
                                "WeightIndication", "BMI", "BMIIndication",
                                "AcuityOfVisionLefteye",
                                "AcuityOfVisionRighteye", "BP", "BPIndication",
                                "BloodGroupID", "BloodGroupNotes",
                                "TemperatureID", "TemperatureIndication",
                                "HemoGlobinID", "HemoGlobinIndication",
                                "MUACInCms", "MUACIndication",
                                "HeadCircumferenceInCms",
                                "HeadCircumferenceIndication","LastCommitedDate"},
                        new String[]{
                                vitals.getHeight() + "".trim(),
                                vitals.getHeightIndication() + "".trim(),
                                vitals.getWeight() + "".trim(),
                                vitals.getWeightIndication() + "".trim(),
                                vitals.getBmi() + "".trim(),
                                vitals.getBmiIndication() + "".trim(),
                                vitals.getAcutyVisionLeft() + "".trim(),
                                vitals.getAcutyVisionRight() + "".trim(),
                                vitals.getBp() + "".trim(),
                                vitals.getBpIndication() + "".trim(),
                                vitals.getBloodGroupId() + "".trim(),
                                vitals.getBloodGroupNotes() + "".trim(),
                                vitals.getTemperatureId() + "".trim(),
                                vitals.getTemperatureIndication() + "".trim(),
                                vitals.getHemoglobinId() + "".trim(),
                                vitals.getHemoglobinIndication() + "".trim(),
                                vitals.getMuacCm() + "".trim(),
                                vitals.getMuacIndication() + "".trim(),
                                vitals.getHeadCircumferenceCm() + "".trim(),
                                vitals.getHeadCircumferenceIndication()
                                        + "".trim(),Helper.getTodayDateTime1()},
                        "LocalChildrenScreeningID", localChildScreeningID);
            }

            if (!updateViatls && vitals != null) {

                dbh.insertintoTable(
                        this.getActivity(),
                        "childrenscreeningvitals",
                        new String[]{"Height", "HeightIndication", "Weight",
                                "WeightIndication", "BMI", "BMIIndication",
                                "AcuityOfVisionLefteye",
                                "AcuityOfVisionRighteye", "BP", "BPIndication",
                                "BloodGroupID", "BloodGroupNotes",
                                "TemperatureID", "TemperatureIndication",
                                "HemoGlobinID", "HemoGlobinIndication",
                                "MUACInCms", "MUACIndication",
                                "HeadCircumferenceInCms",
                                "HeadCircumferenceIndication",
                                "LocalChildrenScreeningID","LastCommitedDate"},
                        new String[]{
                                vitals.getHeight() + "".trim(),
                                vitals.getHeightIndication() + "".trim(),
                                vitals.getWeight() + "".trim(),
                                vitals.getWeightIndication() + "".trim(),
                                vitals.getBmi() + "".trim(),
                                vitals.getBmiIndication() + "".trim(),
                                vitals.getAcutyVisionLeft() + "".trim(),
                                vitals.getAcutyVisionRight() + "".trim(),
                                vitals.getBp() + "".trim(),
                                vitals.getBpIndication() + "".trim(),
                                vitals.getBloodGroupId() + "".trim(),
                                vitals.getBloodGroupNotes() + "".trim(),
                                vitals.getTemperatureId() + "".trim(),
                                vitals.getTemperatureIndication() + "".trim(),
                                vitals.getHemoglobinId() + "".trim(),
                                vitals.getHemoglobinIndication() + "".trim(),
                                vitals.getMuacCm() + "".trim(),
                                vitals.getMuacIndication() + "".trim(),
                                vitals.getHeadCircumferenceCm() + "".trim(),
                                vitals.getHeadCircumferenceIndication()
                                        + "".trim(),
                                localChildScreeningID + "".trim(),Helper.getTodayDateTime1()});
            }

            // --------------------Physical examinations-------------
            Category[] categories = Helper.childScreeningObj.getCategories();
            if (categories != null) {
                for (int i = 0; i < categories.length - 1; i++) {
                    Category category = categories[i];
                    ArrayList<Question> questions = category.getQuestions();
                    if (questions != null) {
                        for (Question question : questions) {
                            @SuppressWarnings("unused")
                            boolean updateflag = dbh.updateROWByValues(
                                    this.getActivity(),
                                    "childrenscreeningpe",
                                    new String[]{"Answer","LastCommitedDate"},
                                    new String[]{question.getAnswer(),Helper.getTodayDateTime1()},
                                    new String[]{"LocalChildrenScreeningID",
                                            "ScreeningQuestionID"},
                                    new String[]{
                                            localChildScreeningID,
                                            question.getScreenQuestionID()
                                                    + "".trim()});
                        }
                    }
                }
            }

            // ------------------------Referal----------------------Update
            // Comments
            // and
            // labinvestagation Id
            if (Helper.childScreeningObj.getReferrals() != null) {
                ArrayList<Referral> referrals = Helper.childScreeningObj
                        .getReferrals();
                int size = referrals.size();
                referrals.remove(size - 1);
                referrals.remove(size - 2);
                referrals.remove(size - 3);

                dbh.updateROW(this.getActivity(), "childrenscreeningreferrals",
                        new String[]{"IsDeleted","LastCommitedDate"}, new String[]{"1",Helper.getTodayDateTime1()},
                        "LocalChildrenScreeningID", localChildScreeningID);
                for (Referral referral : referrals) {
                    String referralQuery = "Select LocalChildrenScreeningReferralID from childrenscreeningreferrals where    IsDeleted!=1 and  LocalChildrenScreeningID='"
                            + localChildScreeningID
                            + "' and HealthConditonID='"
                            + referral.getHealthCondtionId() + "';";
                    Cursor cursor = dbh.getCursorData(this.getActivity(),
                            referralQuery);
                    if (cursor != null) {
                        cursor.moveToNext();
                        String ChildrenScreeningReferralID = cursor
                                .getString(cursor
                                        .getColumnIndex("LocalChildrenScreeningReferralID"));

                        // *** Deleting existing lab investigations.
                        // dbh.updateROW(context, tablename, columnNames,
                        // columnValues, whereColumn, whereValue)
                        dbh.updateROW(this.getActivity(),
                                "childrenscreeninginvestigations",
                                new String[]{"IsDeleted","LastCommitedDate"},
                                new String[]{"1",Helper.getTodayDateTime1()},
                                "LocalChildrenScreeningReferralID",
                                ChildrenScreeningReferralID);

                        ArrayList<HashMap<String, String>> investigations = referral
                                .getInvestigations();

                        if (investigations != null) {
                            for (int i = 0; i < investigations.size(); i++) {
                                dbh.insertintoTable(
                                        this.getActivity(),
                                        "childrenscreeninginvestigations",
                                        new String[]{
                                                "LocalChildrenScreeningID",
                                                "LocalChildrenScreeningReferralID",
                                                "LabInvestigationID",
                                                "Comments", "IsDeleted","LastCommitedDate"},
                                        new String[]{
                                                localChildScreeningID
                                                        + "".trim(),
                                                ChildrenScreeningReferralID,
                                                investigations.get(i).get("id")
                                                        + "".trim(), "", "0",Helper.getTodayDateTime1()});
                            }
                        }
                        // --------Update comments and facilities to
                        // childrenscreeningreferrals----------
                        dbh.updateROWByValues(
                                this.getActivity(),
                                "childrenscreeningreferrals",
                                new String[]{"ReferredFacilityID",
                                        "Comments", "IsDeleted","LastCommitedDate"},
                                new String[]{"" + referral.getFacilityID(),
                                        referral.getComments(), "0",Helper.getTodayDateTime1()},
                                new String[]{"LocalChildrenScreeningReferralID"},
                                new String[]{ChildrenScreeningReferralID});

                    } else {

                        long referalId = dbh.insertintoTable(
                                this.getActivity(),
                                "childrenscreeningreferrals",
                                new String[]{"LocalChildrenScreeningID",
                                        "HealthConditonID", "WasReferred",
                                        "ReferredFacilityID", "Comments",
                                        "ReferredDateTime", "IsDeleted","LastCommitedDate"},
                                new String[]{
                                        localChildScreeningID + "".trim(),
                                        referral.getHealthConditonReferred()
                                                .getHealthConditionID()
                                                + "".trim(), "1",
                                        "" + referral.getFacilityID(),
                                        referral.getComments(),
                                        Helper.getTodayDateTime(), "0",Helper.getTodayDateTime1()});

                        ArrayList<HashMap<String, String>> investigations = referral
                                .getInvestigations();

                        if (investigations != null) {
                            for (int i = 0; i < investigations.size(); i++) {
                                dbh.insertintoTable(
                                        this.getActivity(),
                                        "childrenscreeninginvestigations",
                                        new String[]{
                                                "LocalChildrenScreeningID",
                                                "LocalChildrenScreeningReferralID",
                                                "LabInvestigationID",
                                                "Comments","LastCommitedDate"},
                                        new String[]{
                                                localChildScreeningID
                                                        + "".trim(),
                                                referalId + "".trim(),
                                                investigations.get(i).get("id")
                                                        + "".trim(), "",Helper.getTodayDateTime1()});
                            }
                        }
                    }
                }
            }
            // -------------------Sign off----------------------

            SignOffScreenModel signOffscreenModel = Helper.childScreeningObj
                    .getSignOffModel();
            if (signOffscreenModel != null) {
                if (signOffscreenModel.getRecommendations() != null) {
                    Recommendations recommendation = signOffscreenModel
                            .getRecommendations();
                    dbh.updateROW(this.getActivity(),
                            "childrenscreeningrecommendations", new String[]{
                                    "Diet", "personalHygiene", "OralHygience",
                                    "PresribedMedication", "OtherComments",
                                    "DoctorComments","LastCommitedDate"},
                            new String[]{recommendation.getDiet(),
                                    recommendation.getPersonalHygine(),
                                    recommendation.getOralHygine(),
                                    recommendation.getMedications(),
                                    recommendation.getOthers(),
                                    signOffscreenModel.getDoctorComments(),Helper.getTodayDateTime1()},
                            "LocalChildrenScreeningID", localChildScreeningID);
                }
                dbh.updateROW(this.getActivity(),
                        "childrenscreeninglocaltreatment", new String[]{
                                "LocalChildrenScreeningID", "Diagnosis",
                                "MedicationGiven","LastCommitedDate"}, new String[]{
                                localChildScreeningID + "".trim(),
                                signOffscreenModel.getDiagnosis(),
                                signOffscreenModel.getMedicationsGiven(),Helper.getTodayDateTime1()},
                        "LocalChildrenScreeningID", localChildScreeningID);
            }

            ScreeningActivity.childrenList.get(
                    ScreeningActivity.listSelectedPosition)
                    .setChildScreenStatusID(childScreenStatusID);

        } else {// new student---insert
            // ----------------------Family Details-------------------------

            ScreeningActivity.childrenList.get(
                    ScreeningActivity.listSelectedPosition)
                    .setChildScreenStatusID(childScreenStatusID);
            // ScreenedBy
            long localChildScreeningID = -1;

            localChildScreeningID = dbh
                    .insertintoTable(
                            this.getActivity(),
                            "childrenscreening",
                            new String[]{"LocalInstituteScreeningDetailID",
                                    "LocalChildrenID",
                                    "ScreeningTemplateTypeID",
                                    "ScreeningStartDateTime",
                                    "ScreeningEndDateTime",
                                    "ChildrenScreenStatusID",
                                    "ScreeningComments", "ScreenedBy","LastCommitedDate"},
                            new String[]{
                                    String.valueOf(mScreeningActivity.locInsScreeningDetailID),
                                    childrenObject.getChildrenID() + "".trim(),
                                    childrenObject.getChildrenInsitute()
                                            .getInstituteTypeId() + "".trim(),
                                    childrenObject.getScreningStartTime(),
                                    childrenObject.getScreningEndTime(),
                                    childScreenStatusID + "".trim(),
                                    childrenObject.getScreeningComments(),
                                    mListSingoffUserId
                                            .get(spn_sign_off_screened_by
                                            .getSelectedItemPosition()),Helper.getTodayDateTime1()});

            // *** ChildrenScreningFH
            Helper.childScreeningObj
                    .setScreeningID((int) localChildScreeningID);
            if (tempImages == true) {
                dbh.insertintoTable(getActivity(), "childrenscreeningpictures",
                        new String[]{"LocalChildrenScreeningID", "ImageName",
                                "ImagePath","LastCommitedDate"}, new String[]{
                                localChildScreeningID + "", tempName,
                                "/DCIM/myCapturedImages",Helper.getTodayDateTime1()});
            }

            dbh.updateROW(getActivity(), "childscreeningfh",
                    new String[]{"IsDeleted","LastCommitedDate"}, new String[]{"1",Helper.getTodayDateTime1()},
                    "LocalChildrenScreeningID",
                    String.valueOf(localChildScreeningID));
            if (Helper.childScreeningObj.getFamilyHistoryDiseases() != null) {
                ArrayList<FamilyHistoryDisease> familyHistoryDiseases = Helper.childScreeningObj
                        .getFamilyHistoryDiseases();

                for (int i = 0; i < familyHistoryDiseases.size(); i++) {
                    FamilyHistoryDisease fhDisease = familyHistoryDiseases
                            .get(i);
                    if (fhDisease.isSelected())
                        dbh.insertintoTable(this.getActivity(),
                                "childscreeningfh", new String[]{
                                        "LocalChildrenScreeningID",
                                        "FamilyHistoryID", "HasHistory",
                                        "FamilyMemberRelationID", "Notes",
                                        "IsDeleted","LastCommitedDate"}, new String[]{
                                        localChildScreeningID + "".trim(),
                                        fhDisease.getDiseaseID() + "".trim(),
                                        valueOf(fhDisease.isSelected()),
                                        fhDisease.getRelationID() + "".trim(),
                                        fhDisease.getDiseaseComments(), "0",Helper.getTodayDateTime1()});
                }
            }
            // ------------------Medical History-------------------------

            if (Helper.childScreeningObj.getMedicalHistoryScreenModel() != null) {
                // ------------------Allergy--------------------
                if (Helper.childScreeningObj.getMedicalHistoryScreenModel()
                        .getAllergies() != null) {
                    ArrayList<Allergy> allergyAry = new ArrayList<Allergy>();
                    allergyAry = Helper.childScreeningObj
                            .getMedicalHistoryScreenModel().getAllergies();
                    for (int i = 0; i < allergyAry.size(); i++) {
                        dbh.insertintoTable(
                                this.getActivity(),
                                "childrenscreeningallergies",
                                new String[]{"LocalChildrenScreeningID",
                                        "AllergyID", "Comments", "IsDeleted","LastCommitedDate"},
                                new String[]{
                                        localChildScreeningID + "".trim(),
                                        allergyAry.get(i).getId() + "".trim(),
                                        allergyAry.get(i).getComments(), "0",Helper.getTodayDateTime1()});
                    }
                }

                // ------------------surgical-------------------
                if (Helper.childScreeningObj.getMedicalHistoryScreenModel()
                        .getSurgeries() != null) {
                    ArrayList<Surgery> surgeryAry = new ArrayList<Surgery>();
                    surgeryAry = Helper.childScreeningObj
                            .getMedicalHistoryScreenModel().getSurgeries();
                    for (int i = 0; i < surgeryAry.size(); i++) {
                        dbh.insertintoTable(
                                this.getActivity(),
                                "childrenscreeningsurgicals",
                                new String[]{"LocalChildrenScreeningID",
                                        "SurgicalID", "Comments", "IsDeleted","LastCommitedDate"},
                                new String[]{
                                        localChildScreeningID + "".trim(),
                                        surgeryAry.get(i).getId() + "".trim(),
                                        surgeryAry.get(i).getComments(), "0",Helper.getTodayDateTime1()});
                    }
                }
            }

            // -----------------------Vitals-----------------------------

            ScreeningVitals vitals = Helper.childScreeningObj.getVitals();

            // should pass based on insttute templete id
            // int screenTemplatesTypeID = 1;

            dbh.insertintoTable(
                    this.getActivity(),
                    "childrenscreeningvitals",
                    new String[]{"Height", "HeightIndication", "Weight",
                            "WeightIndication", "BMI", "BMIIndication",
                            "AcuityOfVisionLefteye", "AcuityOfVisionRighteye",
                            "BP", "BPIndication", "BloodGroupID",
                            "BloodGroupNotes", "TemperatureID",
                            "TemperatureIndication", "HemoGlobinID",
                            "HemoGlobinIndication", "MUACInCms",
                            "MUACIndication", "HeadCircumferenceInCms",
                            "HeadCircumferenceIndication",
                            "LocalChildrenScreeningID","LastCommitedDate"},
                    new String[]{
                            vitals.getHeight() + "".trim(),
                            vitals.getHeightIndication() + "".trim(),
                            vitals.getWeight() + "".trim(),
                            vitals.getWeightIndication() + "".trim(),
                            vitals.getBmi() + "".trim(),
                            vitals.getBmiIndication() + "".trim(),
                            vitals.getAcutyVisionLeft() + "".trim(),
                            vitals.getAcutyVisionRight() + "".trim(),
                            vitals.getBp() + "".trim(),
                            vitals.getBpIndication() + "".trim(),
                            vitals.getBloodGroupId() + "".trim(),
                            vitals.getBloodGroupNotes() + "".trim(),
                            vitals.getTemperatureId() + "".trim(),
                            vitals.getTemperatureIndication() + "".trim(),
                            vitals.getHemoglobinId() + "".trim(),
                            vitals.getHemoglobinIndication() + "".trim(),
                            vitals.getMuacCm() + "".trim(),
                            vitals.getMuacIndication() + "".trim(),
                            vitals.getHeadCircumferenceCm() + "".trim(),
                            vitals.getHeadCircumferenceIndication() + "".trim(),
                            localChildScreeningID + "".trim(),Helper.getTodayDateTime1()});

            // --------------------Physical examinations-------------
            Category[] categories = Helper.childScreeningObj.getCategories();
            if (categories != null)
                for (int i = 0; i < categories.length - 1; i++) {
                    Category category = categories[i];
                    ArrayList<Question> questions = category.getQuestions();
                    if (questions != null)
                        for (Question question : questions) {

                            dbh.insertintoTable(
                                    this.getActivity(),
                                    "childrenscreeningpe",
                                    new String[]{"LocalChildrenScreeningID",
                                            "ScreeningQuestionID", "Question",
                                            "Answer", "IsReferredWhenYes",
                                            "HealthConditionID","LastCommitedDate"},
                                    new String[]{
                                            localChildScreeningID + "".trim(),
                                            question.getScreenQuestionID() + "".trim(),
                                            "",// question.getQuestion(),
                                            question.getAnswer(),
                                            question.getIsReferedWhen() + "".trim(),
                                            question.getHealthConditionID() + "".trim(),
                                    Helper.getTodayDateTime1()});
                        }
                }
            // ------------------------Referal----------------------Update
            // Comments
            // and
            // labinvestagation Id

            if (Helper.childScreeningObj.getReferrals() != null) {

                dbh.updateROW(getActivity(), "childrenscreeningreferrals",
                        new String[]{"IsDeleted","LastCommitedDate"}, new String[]{"1",Helper.getTodayDateTime1()},
                        "LocalChildrenScreeningID",
                        String.valueOf(localChildScreeningID));

                ArrayList<Referral> referrals = Helper.childScreeningObj
                        .getReferrals();
                int size = referrals.size();
                referrals.remove(size - 1);
                referrals.remove(size - 2);
                referrals.remove(size - 3);
                for (Referral referral : referrals) {
                    long referalId = dbh
                            .insertintoTable(
                                    this.getActivity(),
                                    "childrenscreeningreferrals",
                                    new String[]{"LocalChildrenScreeningID",
                                            "HealthConditonID", "WasReferred",
                                            "ReferredFacilityID", "Comments",
                                            "ReferredDateTime", "IsDeleted","LastCommitedDate"},
                                    new String[]{
                                            localChildScreeningID + "".trim(),
                                            referral.getHealthConditonReferred()
                                                    .getHealthConditionID()
                                                    + "".trim(), "1",
                                            "" + referral.getFacilityID(),
                                            referral.getComments(),
                                            Helper.getTodayDateTime(), "0",Helper.getTodayDateTime1()});

                    ArrayList<HashMap<String, String>> investigations = referral
                            .getInvestigations();

                    // String[] investigations = referral.getInvestigations();
                    if (investigations != null) {
                        for (int i = 0; i < investigations.size(); i++) {
                            dbh.insertintoTable(
                                    this.getActivity(),
                                    "childrenscreeninginvestigations",
                                    new String[]{"LocalChildrenScreeningID",
                                            "LocalChildrenScreeningReferralID",
                                            "LabInvestigationID", "Comments","LastCommitedDate"},
                                    new String[]{
                                            localChildScreeningID + "".trim(),
                                            referalId + "".trim(),
                                            investigations.get(i).get("id")
                                                    + "".trim(), "",Helper.getTodayDateTime1()});
                        }
                    }
                }
            }

            // -------------------Sign off----------------------

            SignOffScreenModel signOffscreenModel = Helper.childScreeningObj
                    .getSignOffModel();
            if (signOffscreenModel.getRecommendations() != null) {
                Recommendations recommendation = signOffscreenModel
                        .getRecommendations();
                dbh.insertintoTable(
                        this.getActivity(),
                        "childrenscreeningrecommendations",
                        new String[]{"LocalChildrenScreeningID", "Diet",
                                "personalHygiene", "OralHygience",
                                "PresribedMedication", "OtherComments",
                                "DoctorComments","LastCommitedDate"},
                        new String[]{localChildScreeningID + "".trim(),
                                recommendation.getDiet(),
                                recommendation.getPersonalHygine(),
                                recommendation.getOralHygine(),
                                recommendation.getMedications(),
                                recommendation.getOthers(),
                                signOffscreenModel.getDoctorComments(),Helper.getTodayDateTime1()});
            }

            dbh.insertintoTable(this.getActivity(),
                    "childrenscreeninglocaltreatment", new String[]{
                            "LocalChildrenScreeningID", "Diagnosis",
                            "MedicationGiven","LastCommitedDate"}, new String[]{
                            localChildScreeningID + "".trim(),
                            signOffscreenModel.getDiagnosis(),
                            signOffscreenModel.getMedicationsGiven(),Helper.getTodayDateTime1()});
        }
    }

    /**
     * to update doctor comments data
     */
  /*
   * private void updateDoctorComments() {
	 * tv_sign_off_doc_comments.setText(Helper.childScreeningObj
	 * .getSignOffModel().getDoctorComments()); }
	 */

    /**
     * to update local treatments data
     */
  /*
   * private void updateLocalTreatment() { if
	 * (!Helper.childScreeningObj.getSignOffModel().getDiagnosis().trim()
	 * .equals("")) {
	 * tv_sign_off_local_treatment.setText(Helper.childScreeningObj
	 * .getSignOffModel().getDiagnosis() + " : " +
	 * Helper.childScreeningObj.getSignOffModel() .getMedicationsGiven()); } }
	 */

	/*
   * will be called on resume of the activity
	 */
    @Override
    public void onResume() {
        // updateRecommendations();
        // updateDoctorComments();
        // updateLocalTreatment();
        super.onResume();
    }

    public static String valueOf(boolean boolValue) {
        return (boolValue) ? "1" : "0";
    }
}
