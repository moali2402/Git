package dev.vision.engizny;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import dev.vision.engizny.fragments.Fragment_Engizny;
import dev.vision.engizny.fragments.Fragment_Menu;


public class CustomAnimation extends SlidingFragmentActivity {
	private SlidingMenu menu ;
	private CanvasTransformer mTransformer;
	private Fragment mContent;
	
    public CustomAnimation(CanvasTransformer canvasTransformer) {
    		mTransformer = canvasTransformer;
    }

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
	        Window w = getWindow();
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);   
        }
        
        setContentView(R.layout.content_frame);
        menu = getSlidingMenu();
        
     // set the Above View Fragment
 		if (savedInstanceState != null)
 			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
 		if (mContent == null)
 			mContent = Fragment_Engizny.newInstance();	
 		getSupportFragmentManager()
 		.beginTransaction()
 		.replace(R.id.content_frame, mContent)
 		.commit();


    	// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			menu.setSlidingEnabled(true);
		}
		
		// set the Behind View Fragment
		
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setBehindScrollScale(0.0f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setShadowWidth(50);
		menu.setFadeEnabled(true);
		menu.setFadeDegree((float) 0.5);
		menu.setBehindCanvasTransformer(mTransformer);
		menu.setBehindWidth((int) (Static.SCREEN_WIDTH/3.2));
        menu.setFitsSystemWindows(true);
        menu.setBackgroundColor(Color.parseColor("#e3ed1c24"));
        
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new Fragment_Menu())
		.commit();	
        
        
    }
	
	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.addToBackStack(null)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				showContent();
			}
		}, 50);
	}
}