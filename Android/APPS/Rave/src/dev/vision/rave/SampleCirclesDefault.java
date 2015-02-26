package dev.vision.rave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.Window;
import android.widget.Toast;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import dev.vision.rave.fragments.Main_Fragment;
import dev.vision.rave.fragments.Messaging_Fragment;
import dev.vision.rave.fragments.Social_Fragment;


public class SampleCirclesDefault extends Abc implements OnClickListener {
	
	public Uri uri;
	String[] imageUrls;
	DisplayImageOptions options;
	Bundle savedInstanceState;

	List<Fragment> fList;

	List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<View> backgrounds = new ArrayList<View>();
    View social;
    View mess;

	private ColorPicker picker;
	private SVBar svBar;
	private OpacityBar opacityBar;
	public Rect rect = new Rect();
	private Fragment mContent;
	
	private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(SampleCirclesDefault.this, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(SampleCirclesDefault.this, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };
    
	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		//DataHolder.ChangeColors(getResources().getColor(R.color.white));

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
        

        
        /*
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuFragment())
		.commit();
        */
        
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, Main_Fragment.newInstance("", R.layout.main_page, user, imageLoader))
		.commit();
		

        
        //menu.setShadowWidth(20);
        //menu.setFadeDegree(0.55f);
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //menu.setBackgroundColor(Color.BLACK);
		
        setUpMenu();
        //setUpSlidingMenu();
    }
	
	

	public void switchCoontent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.addToBackStack(null)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				menu.showContent();
			}
		}, 50);
	}
	
	 // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
	
    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.backgrad);
        resideMenu.attachToActivity(this);
        //resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "Profile");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");
        itemCalendar = new ResideMenuItem(this, R.drawable.logout, "Sign Out");

        
        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        /*
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
        */
        showColorPickerDialog();
        
    }

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
    */
    
	private void showColorPickerDialog()
	{
		AlertDialog.Builder colorDialogBuilder = new AlertDialog.Builder(
		this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = inflater.inflate(R.layout.color_picker, null);
		picker = (ColorPicker) dialogview.findViewById(R.id.picker);
		svBar = (SVBar) dialogview.findViewById(R.id.svbar);
		opacityBar = (OpacityBar) dialogview.findViewById(R.id.opacitybar);
		picker.addSVBar(svBar);
		picker.addOpacityBar(opacityBar);
		picker.setColor(DataHolder.APP_BACKGROUND);
        picker.setOldCenterColor(DataHolder.APP_BACKGROUND);
        picker.setNewCenterColor(DataHolder.APP_BACKGROUND);

		picker.setOnColorChangedListener(new OnColorChangedListener() 
		{
	
	        @Override
	        public void onColorChanged(int color) {
                picker.setOldCenterColor(picker.getColor());
               // mListView.setBackgroundColor(picker.getColor());

	        }
	    });
	    colorDialogBuilder.setTitle("Choose Color");
	    colorDialogBuilder.setView(dialogview);
	    colorDialogBuilder.setPositiveButton("OK",
	            new DialogInterface.OnClickListener() {
	                @SuppressLint("NewApi") @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    //Log.d("l", "Color :" + picker.getColor());
	                    DataHolder.ChangeColors(picker.getColor());
	                    startActivity(getIntent()); 
	                    finish();
	                   // mListView.setBackground(null);

	                }
	            });
	    colorDialogBuilder.setNegativeButton("Cancel",
	            new DialogInterface.OnClickListener() {
	
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog.cancel();
	                }
	            });
	    AlertDialog colorPickerDialog = colorDialogBuilder.create();
	    colorPickerDialog.show();
	}
	
	
	/** (non-Javadoc)
    * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
    */
   @Override
   public void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
   }

	
	
	private void setColours() {
		
		for(View v : backgrounds){
			v.setBackgroundColor(DataHolder.APP_BACKGROUND);
		}
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
		
	}

}