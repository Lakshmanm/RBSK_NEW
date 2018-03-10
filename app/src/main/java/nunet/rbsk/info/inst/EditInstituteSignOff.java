package nunet.rbsk.info.inst;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nunet.utils.RoundedImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.ChildrenScreeningModel;
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

  private DBHelper dbh;
  int image_count = 0;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.editinst_signoff,
      container, false);
    getActivity().getWindow().setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    dbh = DBHelper.getInstance(this.getActivity());
    findViews(rootView);

    getInstituteName();

    //  getSignoffImagesFromDB();

    btn_edit_institute_signof_previous.setOnClickListener(this);
    btn_edit_institute_signof_next.setOnClickListener(this);
    ll_sign_off_cam.setOnClickListener(this);
    return rootView;
  }

  private void getInstituteName() {
    String query = "select InstituteName"
      + " from Institutes InstituteID = "
      + +((InsituteFragmentActivityDialog) getActivity()).selectedInstituteId
      + "";
    Cursor cursor = dbh.getCursorData(this.getActivity(), query);
    if (cursor != null) {


      while (cursor.moveToNext()) {
        tv_instit_sign_off_name.setText(cursor.getString(cursor
          .getColumnIndex("InstituteName")));
      }
      cursor.close();
    }
  }


  private void getSignoffImagesFromDB() {
    String query = "select i.InstituteID,a.MandalID,fc.FacilityID,f.DisplayText as FacilityName,f.FacilityTypeID,ft.DisplayText as FacilityTypecode ,"
      + "s.DisplayText as StateName,d.DisplayText as DistrictName,m.DisplayText as MandalName,p.DisplayText as PanchayatName,"
      + "v.DisplayText as VillageName,h.DisplayText as HabitatName,ad.AddressLine1,ad.AddressLine2,ad.AddressName,ad.LandMark ,ad.PINCode ,ad.Post"
      + " from Institutes i inner join Address a on i.LocalAddressID = a.LocalAddressID inner join FacilityCoverage fc on (fc.MandalID = a.MandalID or fc.VillageID = a.VillageID) and fc.DoesCover = 1 "
      + "inner join Facilities f on fc.FacilityID = f.FacilityID inner join FacilityTypes ft on ft.FacilityTypeID = f.FacilityTypeID"
      + " left join Address ad on ad.LocalAddressID = f.LocalAddressID left join States s on s.StateID = ad.StateID"
      + " left join Districts d on d.DistrictID = ad.DistrictID left join Mandals m on m.MandalID =  ad.MandalID "
      + "left join Panchayats p on p.PanchayatID = ad.PanchayatID left join Villages v on v.VillageID = ad.VillageID"
      + " left join habitatas h on h.HabitationID = ad.HabitationID where f.IsDeleted!=1 AND fc.DoesCover=1 AND i.InstituteID = "
      + +((InsituteFragmentActivityDialog) getActivity()).selectedInstituteId
      + "";
    Cursor cursor = dbh.getCursorData(this.getActivity(), query);
    if (cursor != null) {

      int count = 0;
      while (cursor.moveToNext()) {
        String addressStr = "";
        String facilityName = cursor.getString(cursor
          .getColumnIndex("FacilityName"));


        if (cursor.getString(cursor.getColumnIndex("AddressLine1")) != null) {
          String addressline1 = cursor.getString(cursor
            .getColumnIndex("AddressLine1"));
          addressStr += addressline1 + ",";

        }
        if (cursor.getString(cursor.getColumnIndex("AddressLine2")) != null) {
          String addressline2 = cursor.getString(cursor
            .getColumnIndex("AddressLine2"));
          addressStr += addressline2 + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("AddressName")) != null) {
          String AddressName = cursor.getString(cursor
            .getColumnIndex("AddressName"));
          addressStr += AddressName + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("LandMark")) != null) {
          String LandMark = cursor.getString(cursor
            .getColumnIndex("LandMark"));
          addressStr += LandMark + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("HabitatName")) != null) {
          String HabitatName = cursor.getString(cursor
            .getColumnIndex("HabitatName"));
          addressStr += HabitatName + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("VillageName")) != null) {
          String VillageName = cursor.getString(cursor
            .getColumnIndex("VillageName"));
          addressStr += VillageName + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("PanchayatName")) != null) {
          String PanchayatName = cursor.getString(cursor
            .getColumnIndex("PanchayatName"));
          addressStr += PanchayatName + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("MandalName")) != null) {
          String MandalName = cursor.getString(cursor
            .getColumnIndex("MandalName"));
          addressStr += MandalName + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("DistrictName")) != null) {
          String DistrictName = cursor.getString(cursor
            .getColumnIndex("DistrictName"));
          addressStr += DistrictName + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("StateName")) != null) {
          String StateName = cursor.getString(cursor
            .getColumnIndex("StateName"));
          addressStr += StateName + ",";
        }
        if (cursor.getString(cursor.getColumnIndex("PINCode")) != null) {
          String PINCode = cursor.getString(cursor
            .getColumnIndex("PINCode"));
          addressStr += PINCode + ",";
        }


        count++;
      }
      cursor.close();
    }
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
        //  addImageToGallery(bp);
        ll_instit_signoff_images.setVisibility(View.VISIBLE);
        if (bp != null) {
          if (image_count == 0) {
            tv_images_count.setText("Capture Sign Off Images (" + (image_count + 4) + ")");
            image_count = image_count + 1;
            iv_inst_signof_image_1.setImageBitmap(bp);
            iv_inst_signof_image_1.setVisibility(View.VISIBLE);
          } else if (image_count == 1) {
            tv_images_count.setText("Capture Sign Off Images (" + (image_count + 2) + ")");
            image_count = image_count + 1;
            iv_inst_signof_image_2.setImageBitmap(bp);
            iv_inst_signof_image_2.setVisibility(View.VISIBLE);
          } else if (image_count == 2) {
            tv_images_count.setText("Capture Sign Off Images (" + (image_count) + ")");
            image_count = image_count + 1;
            iv_inst_signof_image_3.setImageBitmap(bp);
            iv_inst_signof_image_3.setVisibility(View.VISIBLE);
          } else if (image_count == 3) {
            tv_images_count.setText("Capture Sign Off Images (" + (image_count - 2) + ")");
            image_count = image_count + 1;
            iv_inst_signof_image_4.setImageBitmap(bp);
            iv_inst_signof_image_4.setVisibility(View.VISIBLE);
          } else if (image_count == 4) {
            tv_images_count.setText("Capture Sign Off Images (" + (image_count - 4) + ")");
            image_count = image_count + 1;
            iv_inst_signof_image_5.setImageBitmap(bp);
            iv_inst_signof_image_5.setVisibility(View.VISIBLE);
          } else {
            image_count = image_count - 1;
            if (image_count <= 0)
              image_count = 0;
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


    dbh.insertintoTable(getActivity(), "institutesignoffpictures",
      new String[]{"InstituteID", "ImageName",
        "ImagePath"}, new String[]{
        Helper.childScreeningObj.getScreeningID() + "",
        fname, "/DCIM/myCapturedImages"});


  }

}
