//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk;

import nunet.rbsk.login.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

//*****************************************************************************
//* Name   :  Register_download.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  29-Jul-2015
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
public class Register_download extends Activity implements OnClickListener {
	private Button btn_register_setup;
	private Button btn_register_checkSetupStatus;
	private Button btn_register_continue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_download);

		btn_register_setup = (Button) findViewById(R.id.btn_register_setup);
		btn_register_setup.setOnClickListener(this);

		btn_register_checkSetupStatus = (Button) findViewById(R.id.btn_register_checkSetupStatus);
		btn_register_checkSetupStatus.setOnClickListener(this);

		btn_register_continue = (Button) findViewById(R.id.btn_register_continue);
		btn_register_continue.setOnClickListener(this);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

//		SharedPreferences sharedpreferences = getSharedPreferences("LoginMain",
//				Context.MODE_PRIVATE);
//		String DeviceID = sharedpreferences.getString("DeviceID", "");
//		String UnlockID = sharedpreferences.getString("UnlockID", "");
//		String UnlockPassword = sharedpreferences.getString("UnlockPassword",
//				"");
//		String HealthBlockID = sharedpreferences.getString("HealthBlockID", "");
//		String TokenID = sharedpreferences.getString("TokenID", "");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_register_setup:
			Toast.makeText(this, "Downloading DBfile", Toast.LENGTH_LONG)
					.show();
			v.setVisibility(View.GONE);
			btn_register_continue.setVisibility(View.VISIBLE);
			btn_register_checkSetupStatus.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_register_checkSetupStatus:
			Toast.makeText(this, "DB set up done, you can continue..",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.btn_register_continue:
			startActivity(new Intent(this, LoginActivity.class));
			break;

		default:
			break;
		}

	}

}
