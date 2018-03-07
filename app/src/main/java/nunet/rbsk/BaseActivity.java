package nunet.rbsk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.login.LoginActivity;
import nunet.services.DBTestActivity;
import nunet.services.IncrementalService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
			Toast.makeText( this, "This feature is not available in this version",
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
			Toast.makeText( this, "Backup is started, Please don't do any operation for few minutes",
					Toast.LENGTH_SHORT).show();
            copyDatabase();
			//backupdb(this);
			// Praveen Code Ends

			break;

		case R.id.logsync:
			// Praveen Code Start
			Toast.makeText( this, "This feature is not available in this version",
					Toast.LENGTH_SHORT).show();
			//startActivity(new Intent(this, DBTestActivity.class));
			// Praveen Code Ends
			break;
		case R.id.hybrid_DB:
			// Praveen Code Start
			Toast.makeText( this, "This feature is not available in this version",
					Toast.LENGTH_SHORT).show();
			//copyDatabase(DB_NAME_HYB);
			// Praveen Code Ends
			break;
		case R.id.qa_DB:
			// Praveen Code Start
			Toast.makeText( this, "This feature is not available in this version",
					Toast.LENGTH_SHORT).show();
			//copyDatabase(DB_NAME_QA);
			// Praveen Code Ends
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	public static final String DB_NAME_HYB = "RBSK_V3";
	public static final String DB_NAME_QA = "RBSK_QA";

	@SuppressLint("SdCardPath")
	private final String DATABASE_PATH = "/data/data/nunet.rbsk/databases/";

	//private void copyDatabase(String DB_NAME) { // praveen modified this code.
		private void copyDatabase() {
			 String DB_NAME = "RBSK_V3.3il";
			 String DATABASE_PATH = "/data/data/nunet.rbsk/databases/";
			String BACKUP_DB_NAME = "";
			//
		try {
			String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Calendar.getInstance().getTime());
			BACKUP_DB_NAME = "RBSK_V3_"+timeStamp+".3il";
			final FileInputStream  input = new FileInputStream(Environment.getExternalStorageDirectory() + "/nunet.rbsk/DB/"+DB_NAME);
			final String outFileName = Environment.getExternalStorageDirectory() + "/nunet.rbsk/DB/"+BACKUP_DB_NAME;
			final OutputStream output = new FileOutputStream(outFileName);
			// Transfer bytes from the input file to the output file
			final byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
            Toast.makeText( this, "Exception occured during Backup",
                    Toast.LENGTH_SHORT).show();
		}

            Toast.makeText( this, "Backup is Completed , Backup File Name : " + Environment.getExternalStorageDirectory() + "/nunet.rbsk/DB/"+BACKUP_DB_NAME,
                    Toast.LENGTH_SHORT).show();

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
						String currentDBPath = "//data//nunet.rbsk//databases//"
								+ DBHelper.DB_NAME;
						String backupDBPath = "sandeep.sqlite";

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
