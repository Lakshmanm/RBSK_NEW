package nunet.rbsk.helpers;

import nunet.rbsk.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

/**
 * This is sub class of {@link Dialog} which is used to show the
 * {@link CustomDialog} to the user while performing any background operations.
 * 
 * 
 * @version 1.0
 */

public class CustomDialog extends Dialog {

	/**
	 * Constructor for instantiating the {@link CustomDialog}
	 * 
	 * @param context
	 *            {@link Context} of the screen where to show the
	 *            {@link CustomDialog}
	 */

	public CustomDialog(Context context) {
		super(context);
	}// constructor

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cutom_progress_bar);

	}// onCreate()

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// CustomDialog.this.dismiss();
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}// onKeyDown()
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { return false;
	 * //return super.onTouchEvent(event); }
	 */

	@Override
	public void dismiss() {

		super.dismiss();
	}
}// CustomDialog-class
