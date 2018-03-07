//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.rbsk.screening;

import nunet.custom.TableMainLayout;
import nunet.rbsk.helpers.Helper;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

//*****************************************************************************
//* Name   :  MH_HealthConditionsTable.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : deepika.chevvakula

//* Created Date       :  11-Jun-2015
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
public class MH_HealthConditionsTable extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(new TableMainLayout(this));
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
	    View v = getCurrentFocus();
	     
	    if (v != null && 
	            (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && 
	            v instanceof EditText && 
	            !v.getClass().getName().startsWith("android.webkit."))
	    {
	        int scrcoords[] = new int[2];
	        v.getLocationOnScreen(scrcoords);
	        float x = ev.getRawX() + v.getLeft() - scrcoords[0];
	        float y = ev.getRawY() + v.getTop() - scrcoords[1];
	 
	        if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
	            Helper.hideSoftKeyboard(this);
	    }
	    return super.dispatchTouchEvent(ev);
	}

}
