//=============================================================================
// All rights reserved to Nunet Cube Software Solutions.
// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY
// OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR
// FITNESS FOR A PARTICULAR PURPOSE.
//=============================================================================


package nunet.animation;

import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;


//*****************************************************************************
//* Name   :  ExpandAnimation.java

//* Type    : 

//* Description     :  Class for Expand student List Animation
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

public class ExpandAnimation extends TranslateAnimation implements Animation.AnimationListener{

	private final LinearLayout slidingLayout;
	int panelWidth;
	
	public ExpandAnimation(final LinearLayout layout, final int width, final int fromXType, final float fromXValue, final int toXType,
			final float toXValue, final int fromYType, final float fromYValue, final int toYType, final float toYValue) {
		
		super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue);
		
		//Initialize
		slidingLayout = layout;
		panelWidth = width;
		setDuration(400);
  	    setFillAfter( false );
  	    setInterpolator(new AccelerateDecelerateInterpolator());
  	    setAnimationListener(this);
  	    slidingLayout.startAnimation(this);
	}


	public void onAnimationEnd(final Animation arg0) {
		
		//Create margin and align left
		final LayoutParams params = (LayoutParams) slidingLayout.getLayoutParams();
  	   	params.leftMargin = panelWidth;
  	   	params.gravity = Gravity.LEFT;	   
  	   	slidingLayout.clearAnimation();
  	   	slidingLayout.setLayoutParams(params);
  	   	slidingLayout.requestLayout();
  	  			
	}

	public void onAnimationRepeat(final Animation arg0) {
		
	}

	public void onAnimationStart(final Animation arg0) {
		
	}

}
