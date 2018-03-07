package nunet.rbsk.screening;

import java.util.ArrayList;
import java.util.HashMap;

import nunet.rbsk.R;
import nunet.rbsk.helpers.DBHelper;
import nunet.rbsk.helpers.Helper;
import nunet.rbsk.model.Referral;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nunet.utils.StringUtils;

public class ReferralPopActivityDialog extends Activity implements
		OnClickListener {

	DBHelper dbh;
	private EditText et_referral_pop_comments;
	private Spinner spn_referral_place_of_referral;
	private Button btn_referral_close;
	private Button btn_referral_save;
	public static ArrayAdapter<String> adp_spnreferto, adp_referral;
	private TextView tv_referal_name;
	ArrayList<HashMap<String, String>> masterInvestigationsArrayList;
	private LinearLayout ll_referral_investigations;
	private int position;
	private Referral hc_referral;
	private ArrayList<HashMap<String, String>> selected_investigations = new ArrayList<HashMap<String, String>>();

	// private HashMap<String, String> hm_investigation = new HashMap<String,
	// String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.referral_pop);
		dbh = DBHelper.getInstance(ReferralPopActivityDialog.this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		Bundle getIntent = getIntent().getExtras();
		position = getIntent.getInt("position");

		healthConditonReferred = getIntent.getInt("HealthConditonReferred");
		hc_referral = Helper.childScreeningObj.getReferrals().get(position);
		if (hc_referral.getInvestigations() != null) {
			selected_investigations = hc_referral.getInvestigations();
		}
		findViews();
		getMasterLabInvestigationsFromDB();

		tv_referal_name.setText(hc_referral.getHealthConditonReferred()
				.getName());

		adp_referral = new ArrayAdapter<String>(ReferralPopActivityDialog.this,
				android.R.layout.simple_spinner_item);
		adp_referral
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adp_referral.add("Select Place of Referral");
		FacilityIDArr.add(-1);
		getPlaceOfReferral();
		spn_referral_place_of_referral.setAdapter(adp_referral);
		spn_referral_place_of_referral.setSelection(selectePos);
		et_referral_pop_comments.setText(hc_referral.getComments());
	}

	ArrayList<Integer> FacilityIDArr = new ArrayList<Integer>();
	int selectePos = 0;
	private int healthConditonReferred;

	private void getPlaceOfReferral() {
		String query1 = "SELECT Facilities.FacilityID,Facilities.DisplayText as FacilitiesDT,FacilityTypes.DisplayText as  FacilityTypesDT"
				+ " FROM HealthConditionFaclities inner join Facilities on Facilities.FacilityID=HealthConditionFaclities.FacilityID"
				+ " inner join FacilityTypes on FacilityTypes.FacilityTypeID = Facilities.FacilityTypeID"
				+ " where  Facilities.IsDeleted!=1 AND   HealthConditionFaclities.IsDeleted!=1 AND   FacilityTypes.IsDeleted!=1  "
				+ " and HealthConditionFaclities.HealthConditionID='"
				+ healthConditonReferred + "'";

		Cursor cursor = dbh.getCursorData(ReferralPopActivityDialog.this,
				query1);

		if (cursor == null) {
			String query2 = "SELECT DISTINCT Facilities.FacilityID,Facilities.DisplayText as FacilitiesDT,FacilityTypes.DisplayText as  FacilityTypesDT"
					+ " FROM HealthConditionFaclities inner join Facilities on Facilities.FacilityID=HealthConditionFaclities.FacilityID"
					+ " inner join FacilityTypes on FacilityTypes.FacilityTypeID = Facilities.FacilityTypeID"
					+ " where  Facilities.IsDeleted!=1 AND   HealthConditionFaclities.IsDeleted!=1 AND   FacilityTypes.IsDeleted!=1  ";
			cursor = dbh.getCursorData(ReferralPopActivityDialog.this, query2);
		}

		if (cursor != null) {
			while (cursor.moveToNext()) {
				adp_referral.add(""
						+ cursor.getString(cursor
								.getColumnIndex("FacilityTypesDT"))
						+ ", "
						+ cursor.getString(cursor
								.getColumnIndex("FacilitiesDT")));
				int FacilityID = cursor.getInt(cursor
						.getColumnIndex("FacilityID"));
				FacilityIDArr.add(FacilityID);

				if (hc_referral.getFacilityID() == FacilityID) {
					selectePos = FacilityIDArr.size() - 1;
				}

			}
		}

	}

	public void getMasterLabInvestigationsFromDB() {
		String query = "select * from labinvestigations IL where IL.IsDeleted!=1 ";
		Cursor cursor = dbh
				.getCursorData(ReferralPopActivityDialog.this, query);
		masterInvestigationsArrayList = new ArrayList<HashMap<String, String>>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("id", cursor.getString(cursor
						.getColumnIndex("LabInvestigationID")));

				hashMap.put("value",
						cursor.getString(cursor.getColumnIndex("DisplayText")));
				masterInvestigationsArrayList.add(hashMap);

			}
			cursor.close();
		} else {
		}
		updateLabInvestigationsView(masterInvestigationsArrayList);
	}

	private void updateLabInvestigationsView(
			ArrayList<HashMap<String, String>> investigationsArrayList) {
		// final HashMap<String, String> hm_investigation = new HashMap<String,
		// String>();
		ll_referral_investigations.removeAllViews();

		for (int i = 0; i < investigationsArrayList.size(); i++) {
			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			final View view = inflator.inflate(R.layout.questionitem, null);
			TextView tv_question = (TextView) view
					.findViewById(R.id.tv_question);
			tv_question.setTextColor(Color.parseColor("#6B6B76"));
			final HashMap<String, String> hashMap = investigationsArrayList
					.get(i);
			tv_question.setText(hashMap.get("value"));
			final Button btn_dynamic_yes = (Button) view
					.findViewById(R.id.btn_dynamic_yes);
			final Button btn_dynamic_no = (Button) view
					.findViewById(R.id.btn_dynamic_no);

			if (selected_investigations.size() == 0) {
				// btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
				btn_dynamic_no.setBackgroundColor(Color.parseColor("#ff6b6b"));
			} else {
				btn_dynamic_no.setBackgroundColor(Color.parseColor("#ff6b6b"));
				for (HashMap<String, String> investigation : selected_investigations) {
					if (StringUtils.equalsNoCase(investigation.get("value"),
							hashMap.get("value"))) {
						btn_dynamic_yes.setBackgroundColor(Color
								.parseColor("#45cfc1"));
						btn_dynamic_no.setBackgroundColor(Color.LTGRAY);
						break;
					}
				}
			}
			// if (hc_referral.getInvestigations() == null) {
			// btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
			// btn_dynamic_no.setBackgroundColor(Color.parseColor("#ff6b6b"));
			// } else {
			// if (selected_investigations.size() == 0) {
			// btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
			// btn_dynamic_no.setBackgroundColor(Color
			// .parseColor("#ff6b6b"));
			// } else {
			// for (int a = 0; a < selected_investigations.size(); a++) {
			// if (selected_investigations.get(a).get("value")
			// .equalsIgnoreCase(hashMap.get("value"))) {
			// btn_dynamic_yes.setBackgroundColor(Color
			// .parseColor("#45cfc1"));
			// btn_dynamic_no.setBackgroundColor(Color.LTGRAY);
			// } else {
			// btn_dynamic_no.setBackgroundColor(Color
			// .parseColor("#ff6b6b"));
			// btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
			// }
			// }
			// }
			// }
			/*
			 * if (hc_referral.getInvestigations() == null) {
			 * btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
			 * btn_dynamic_no.setBackgroundColor(Color.parseColor("#ff6b6b")); }
			 * else { if (hc_referral.getInvestigations().size() == 0) {
			 * btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
			 * btn_dynamic_no.setBackgroundColor(Color .parseColor("#ff6b6b"));
			 * } else { for (int a = 0; a <
			 * hc_referral.getInvestigations().size(); a++) { if
			 * (hc_referral.getInvestigations().get(a).get("value")
			 * .equalsIgnoreCase(hashMap.get("value"))) {
			 * btn_dynamic_yes.setBackgroundColor(Color .parseColor("#45cfc1"));
			 * btn_dynamic_no.setBackgroundColor(Color.LTGRAY); } else {
			 * btn_dynamic_no.setBackgroundColor(Color .parseColor("#ff6b6b"));
			 * btn_dynamic_yes.setBackgroundColor(Color.LTGRAY); } } } }
			 */
			btn_dynamic_yes.setId(i);
			btn_dynamic_yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					btn_dynamic_yes.setBackgroundColor(Color
							.parseColor("#45cfc1"));
					btn_dynamic_no.setBackgroundColor(Color.LTGRAY);
					// iterable_question.setAnswer("1");
					// hm_investigation.put("id",btn_dynamic_yes.getId()+
					// "".trim());
					// hm_investigation.put("value", "yes");
					selected_investigations.add(hashMap);

				}
			});

			btn_dynamic_no.setId(i);
			btn_dynamic_no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btn_dynamic_no.setBackgroundColor(Color
							.parseColor("#ff6b6b"));
					btn_dynamic_yes.setBackgroundColor(Color.LTGRAY);
					// iterable_question.setAnswer("0");
					// hm_investigation.put("id", btn_dynamic_no.getId() +
					// "".trim());
					// hm_investigation.put("value", "no");
					selected_investigations.remove(hashMap);

				}
			});

			// tv_labInvestigationsAry[i]
			// .setId(IntUtil.Integer.parseInt(hashMap.get("id")));
			/*
			 * LayoutParams params = new
			 * android.widget.LinearLayout.LayoutParams(
			 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
			 * tv_labInvestigationsAry[i].setLayoutParams(params);
			 */
			ll_referral_investigations.addView(view);
		}
	}

	private void findViews() {
		et_referral_pop_comments = (EditText) findViewById(R.id.et_referral_pop_comments);
		spn_referral_place_of_referral = (Spinner) findViewById(R.id.spn_referral_place_of_referral);
		btn_referral_close = (Button) findViewById(R.id.btn_referral_close);
		btn_referral_close.setOnClickListener(this);
		btn_referral_save = (Button) findViewById(R.id.btn_referral_save);
		btn_referral_save.setOnClickListener(this);
		tv_referal_name = (TextView) findViewById(R.id.tv_referal_name);
		ll_referral_investigations = (LinearLayout) findViewById(R.id.ll_referral_investigations);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_referral_save:
			if (spn_referral_place_of_referral.getSelectedItemPosition() != 0) {
				Referral referral = hc_referral;
				referral.setReferalPlaceId(spn_referral_place_of_referral
						.getSelectedItemPosition());
				Integer facilityID = FacilityIDArr
						.get(spn_referral_place_of_referral
								.getSelectedItemPosition());
				referral.setFacilityID(facilityID);

				referral.setReferralPlaceName(spn_referral_place_of_referral
						.getSelectedItem().toString().trim());
				referral.setInvestigations(selected_investigations);
				referral.setComments(et_referral_pop_comments.getText()
						.toString().trim());
				referral.getHealthConditonReferred().setUpdate(true);

				ArrayList<Referral> savedReferrals = Helper.childScreeningObj
						.getReferrals();
				savedReferrals.set(position, referral);

				Helper.childScreeningObj.setReferrals(savedReferrals);
				finish();
			} else {
				Helper.setErrorForSpinner(spn_referral_place_of_referral,
						"Select Place of Referral");
			}
			break;

		case R.id.btn_referral_close:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		View v = getCurrentFocus();

		if (v != null
				&& (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
				&& v instanceof EditText
				&& !v.getClass().getName().startsWith("android.webkit.")) {
			int scrcoords[] = new int[2];
			v.getLocationOnScreen(scrcoords);
			float x = ev.getRawX() + v.getLeft() - scrcoords[0];
			float y = ev.getRawY() + v.getTop() - scrcoords[1];

			if (x < v.getLeft() || x > v.getRight() || y < v.getTop()
					|| y > v.getBottom())
				Helper.hideSoftKeyboard(this);
		}
		return super.dispatchTouchEvent(ev);
	}
}
