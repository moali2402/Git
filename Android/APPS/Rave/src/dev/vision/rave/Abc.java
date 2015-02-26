package dev.vision.rave;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import dev.vision.rave.listeners.ScrollableListView;
import dev.vision.rave.user.User;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


/**
 * 
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class Abc extends FragmentActivity {
   
    protected TestFragmentAdapter mainAdapter;
    protected ViewPager main;
    
	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

	protected ScrollableListView mListView;

	protected boolean pauseOnScroll = true;
	protected boolean pauseOnFling = true;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected SlidingMenu menu;
	public static User user;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, true);
		pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
	}

	@Override
	public void onResume() {
		super.onResume();
		//applyScrollListener();
	}

	/*
	private void setUpSlidingMenu() {
		menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setBehindOffset(200);
        if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			menu.setSlidingEnabled(true);
		}		
	}
	*/
	
	private void applyScrollListener() {
		//mListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
		
	}
}