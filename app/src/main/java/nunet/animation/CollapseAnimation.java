//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================

package nunet.animation;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
//*****************************************************************************
//* Name   :  CollapseAnimation.java

//* Type    : 

//* Description     : 
//* References     :                                                        
//* Author    : kiruthika.ganesan

//* Created Date       :  21-May-2015
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

//Collapse Animation
//Created on 21-05-2015
//Animation Class
public class CollapseAnimation extends TranslateAnimation implements TranslateAnimation.AnimationListener{
	
	private final LinearLayout slidingLayout;
	int panelWidth;

	public CollapseAnimation(final LinearLayout layout, final int width, final int fromXType, final float fromXValue, final int toXType,
			final float toXValue, final int fromYType, final float fromYValue, final int toYType, final float toYValue) {
		
		super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue);
		
		//Initialize
		slidingLayout = layout;
  	    panelWidth = width;
		setDuration(400);
	    setFillAfter(false);
	    setInterpolator(new AccelerateDecelerateInterpolator());
	    setAnimationListener(this);
	    
	    //Clear left and right margins
	    final LayoutParams params = (LayoutParams) slidingLayout.getLayoutParams();
  	   	params.rightMargin = 0;
  	   	params.leftMargin = 0;
  	   	slidingLayout.setLayoutParams(params);
  	   	slidingLayout.requestLayout();       
  	   	slidingLayout.startAnimation(this);
  	   	 
	}
	public void onAnimationEnd(final Animation animation) {
	
	}

	public void onAnimationRepeat(final Animation animation) {
		
	}

	public void onAnimationStart(final Animation animation) {
		
	}
	
}
