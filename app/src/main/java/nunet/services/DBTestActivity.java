package nunet.services;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import nunet.rbsk.R;
import nunet.rbsk.helpers.Helper;

public class DBTestActivity extends FragmentActivity implements OnClickListener {

	// public static StringBuffer logString = new StringBuffer();

	public static StringBuffer step1 = new StringBuffer();
	public static StringBuffer step2 = new StringBuffer();
	public static StringBuffer step3 = new StringBuffer();
	public static StringBuffer step4 = new StringBuffer();
	public static StringBuffer step5 = new StringBuffer();

	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logviewr);
		setTitle(IncrementalService.TitleMsg);
		DBTestActivity = this;
		ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollView);
		findViewById(R.id.step1).setOnClickListener(this);
		findViewById(R.id.step2).setOnClickListener(this);
		findViewById(R.id.step4).setOnClickListener(this);
		findViewById(R.id.step5).setOnClickListener(this);
		findViewById(R.id.step6).setOnClickListener(this);
		mTextView = new TextView(this);
		mTextView.setTextSize(20);
		mTextView.setText(step1);
		mScrollView.addView(mTextView);

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(IncrementalService.TitleMsg);
	}

	static DBTestActivity DBTestActivity;

	public static DBTestActivity getInstance() {
		return DBTestActivity;
	}

	@Override
	public void onClick(View v) {
		Helper.showProgressDialog(this);
		switch (v.getId()) {
		case R.id.step1:
			mTextView.setText(step1);
			break;
		case R.id.step2:
			mTextView.setText(step2);
			break;
		case R.id.step4:
			mTextView.setText(step3);
			break;
		case R.id.step5:
			mTextView.setText(step4);
			break;
		case R.id.step6:
			mTextView.setText(step5);
			break;

		default:
			break;
		}

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				Helper.progressDialog.dismiss();
			}
		});
	}

}
