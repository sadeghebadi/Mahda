package apl.vada.lib.viewpager.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

public class TabletPageTransformer implements ViewPager.PageTransformer {
	private static final int  ROTATION_Y = -30;
	
    public void transformPage(View view, float position) {        	
    	if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 1) { // [-1,1]
        	view.setRotationY(ROTATION_Y * position);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }        	
    }
}
