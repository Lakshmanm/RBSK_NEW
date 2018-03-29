package nunet.rbsk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.login.LoginActivity;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(true);

    }

    /**
     * to creare action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionsoverlay, menu);
        MenuItem item = menu.findItem(R.id.menu_userName);
        SharedPreferences sharedpreferences = getSharedPreferences("RbskPref",
                Context.MODE_PRIVATE);
        item.setTitle(sharedpreferences.getString("userKey", ""));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.settings:
                Intent in = new Intent(BaseActivity.this,
                        SettingsPlanActivity.class);
                startActivity(in);
                break;
            case R.id.sendInc:
                // Praveen Code Start
                Toast.makeText(this, "This feature is not available in this version",
                        Toast.LENGTH_SHORT).show();
            /*in = new Intent(BaseActivity.this, IncrementalService.class);
            stopService(in);
			startService(in);
			*/
                // Praveen Code Ends
                break;

            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation Alert")
                        .setMessage(
                                "Are you sure you want to Logout from the Application")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent logout = new Intent(
                                                BaseActivity.this,
                                                LoginActivity.class);
                                        startActivity(logout);

                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // do nothing
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.backupdb:
                // Praveen Code Start
                Toast.makeText(this, "Backup is started, Please don't do any operation for few minutes",
                        Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                } else {
                    copyDatabase();
                }

                // backupdb(this);
                // Praveen Code Ends

                break;

            case R.id.logsync:
                // Praveen Code Start
                Toast.makeText(this, "This feature is not available in this version",
                        Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this, DBTestActivity.class));
                // Praveen Code Ends
                break;
            case R.id.hybrid_DB:
                // Praveen Code Start
                Toast.makeText(this, "This feature is not available in this version",
                        Toast.LENGTH_SHORT).show();
                //copyDatabase(DB_NAME_HYB);
                // Praveen Code Ends
                break;
            case R.id.qa_DB:
                // Praveen Code Start
                Toast.makeText(this, "This feature is not available in this version",
                        Toast.LENGTH_SHORT).show();
                //copyDatabase(DB_NAME_QA);
                // Praveen Code Ends
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);


        }
    }

    public JSONObject syncData() {
        DBHelper dbh = new DBHelper(this);
        JSONObject ret_json = new JSONObject();
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            String Query = "Select * from InstitutePlans";
            Cursor c = db.rawQuery(Query, null);
            JSONArray InstitutePlan = new JSONArray();
            while (c.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("LocalInstitutePlanID", c.getString(c.getColumnIndex("LocalInstitutePlanID")));
                j.put("InstitutePlanID", c.getString(c.getColumnIndex("InstitutePlanID")));
                j.put("LocalInstituteID", c.getString(c.getColumnIndex("LocalInstituteID")));
                j.put("RBSKCalendarYearID", c.getString(c.getColumnIndex("RBSKCalendarYearID")));
                j.put("ScreeningRoundID", c.getString(c.getColumnIndex("ScreeningRoundId")));
                j.put("InstitutePlanStatusID", c.getString(c.getColumnIndex("InstitutePlanStatusID")));
                InstitutePlan.put(j);

            }
            c.close();

            String Query1 = "Select * from InstitutePlanDetails";
            Cursor c1 = db.rawQuery(Query1, null);
            JSONArray InstitutePlanDetails = new JSONArray();
            while (c1.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("LocalInstitutePlanDetailID", c1.getString(c1.getColumnIndex("LocalInstitutePlanDetailID")));
                j.put("InstitutePlanDetailID", c1.getString(c1.getColumnIndex("InstitutePlanDetailID")));
                j.put("LocalInstitutePlanID", c1.getString(c1.getColumnIndex("LocalInstitutePlanID")));
                j.put("ScheduledDate", c1.getString(c1.getColumnIndex("scheduleDate")));
                j.put("PlannedCount", c1.getString(c1.getColumnIndex("PlannedCount")));
                j.put("InstitutePlanSkipReasonID", c1.getString(c1.getColumnIndex("InstitutePlanSkipReasonID")));
                j.put("SkipComments", c1.getString(c1.getColumnIndex("SkipComments")));
                InstitutePlanDetails.put(j);

            }
            c1.close();
            String Query2 = "Select * from Institute";
            Cursor c2 = db.rawQuery(Query2, null);
            JSONArray Institute = new JSONArray();
            while (c2.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("InstituteID", c2.getString(c2.getColumnIndex("InstituteID")));
                j.put("LocalInstituteID", c2.getString(c2.getColumnIndex("LocalInstituteID")));
                j.put("InstituteName", c2.getString(c2.getColumnIndex("InstituteName")));
                j.put("DISECode", c2.getString(c2.getColumnIndex("DiseCode")));
                Institute.put(j);

            }
            c2.close();
            ret_json.put("Institute", Institute);
            String Query3 = "Select * from Children";
            Cursor c3 = db.rawQuery(Query3, null);
            JSONArray Children = new JSONArray();
            while (c1.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenID", c3.getString(c3.getColumnIndex("ChildrenID")));
                j.put("LocalChildrenID", c3.getString(c3.getColumnIndex("LocalChildrenID")));
                j.put("UserID", c3.getString(c3.getColumnIndex("LocalUserID")));
                Children.put(j);

            }
            c3.close();
            String Query4 = "Select * from ChildrenScreening";
            Cursor c4 = db.rawQuery(Query4, null);
            JSONArray ChildrenScreening = new JSONArray();
            while (c1.moveToNext()) {
                JSONObject j = new JSONObject();
                j.put("ChildrenID", c3.getString(c3.getColumnIndex("ChildrenID")));
                j.put("LocalChildrenID", c3.getString(c3.getColumnIndex("LocalChildrenID")));
                j.put("UserID", c3.getString(c3.getColumnIndex("LocalUserID")));
                Children.put(j);

            }
            c3.close();
            ret_json.put("Children", Children);
            db.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret_json;
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
                copyDatabase();
            } else {
                checkPermission();
            }
        }


    }


    //private void copyDatabase(String DB_NAME) { // praveen modified this code.
    private void copyDatabase() {
        String BACKUP_DB_NAME = "";
        //
        try {
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Calendar.getInstance().getTime());
            BACKUP_DB_NAME = "RBSK_V3_" + timeStamp + ".3il";
            final String outFileName = Environment.getExternalStorageDirectory() + "/RBSK/DB/";
            File directory = new File(Environment.getExternalStorageDirectory(), "RBSK/DB");
            if (!directory.exists())
                directory.mkdir();
            File directorydbFile = new File(directory, BACKUP_DB_NAME);
            File input = new File(DBHelper.DATABASE_PATH + DBHelper.DB_NAME);
            FileChannel source = null;
            FileChannel destination = null;

            source = new FileInputStream(input).getChannel();
            destination = new FileOutputStream(directorydbFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception occured during Backup",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public static void backupdb(final Context mContext) {
        new Thread(new Runnable() {
            File backupDB = null;

            @SuppressWarnings("resource")
            @Override
            public void run() {
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = DBHelper.DATABASE_PATH
                                + DBHelper.DB_NAME;
                        String backupDBPath = "RBSKDBBACKUP" + Helper.getTodayDateTime() + ".sqlite";

                        File currentDB = new File(data, currentDBPath);
                        backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {

                            FileChannel src = new FileInputStream(currentDB)
                                    .getChannel();
                            FileChannel dst = new FileOutputStream(backupDB)
                                    .getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }
                    }
                    class SingleMediaScanner implements
                            MediaScannerConnectionClient {

                        private MediaScannerConnection mMs;
                        private File mFile;

                        public SingleMediaScanner(Context context, File f) {
                            mFile = f;
                            mMs = new MediaScannerConnection(context, this);
                            mMs.connect();
                        }

                        @Override
                        public void onMediaScannerConnected() {
                            mMs.scanFile(mFile.getAbsolutePath(), null);
                        }

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            mMs.disconnect();
                        }

                    }
                    new SingleMediaScanner(mContext, backupDB);
                    mContext.sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory())));

                    if (mContext instanceof Activity)
                        ((Activity) mContext).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(
                                        mContext,
                                        "File moved to\n"
                                                + backupDB.getAbsolutePath(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
