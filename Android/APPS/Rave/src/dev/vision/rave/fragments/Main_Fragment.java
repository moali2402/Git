package dev.vision.rave.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.github.siyamed.shapeimageview.OctogonImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.special.ResideMenu.ResideMenu;

import dev.vision.rave.Constants;
import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import dev.vision.rave.SampleCirclesDefault;
import dev.vision.rave.TestFragmentAdapter;
import dev.vision.rave.user.User;
import dev.vision.rave.views.CircularImageView;
import dev.vision.rave.views.FirstName_Cap;
import dev.vision.rave.views.HexagonMaskView;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.widget.TextView;

public class Main_Fragment extends Fragment implements OnClickListener, OnPageChangeListener {

	 public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	 public static final String LAYOUT_ID = "Layout_ID";
	 

	 public static final Main_Fragment newInstance(String message, int layid, User user2, ImageLoader imageLoader)

	 {

	   Main_Fragment f = new Main_Fragment();

	   Bundle bdl = new Bundle(1);

	   bdl.putString(EXTRA_MESSAGE, message);
	   f.user = user2;
	   f.imageLoader= imageLoader;
	   bdl.putInt(LAYOUT_ID, layid);
	   
	   f.setArguments(bdl);
	   return f;

	 }


	private User user;
	//private HexagonMaskView profilePic;
	private HexagonImageView profilePic;

	private ImageLoader imageLoader;
	WebView web_right;
	private View social;
	private View mess;
	private TestFragmentAdapter mainAdapter;
	private ViewPager main;
	private ArrayList<Fragment> fList;
	private String[] imageUrls;
	 

	 @SuppressLint("NewApi") 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,

	   Bundle savedInstanceState) {

		 String message = getArguments().getString(EXTRA_MESSAGE);
	     int layoutId = getArguments().getInt(LAYOUT_ID);
		 View v = inflater.inflate(layoutId, container, false);
		

			
	    //setColours();
	    
	    imageUrls = Constants.IMAGES;

		//user.setSex("female");

	    social = ((View) v.findViewById(R.id.social_tab));
	    social.setOnClickListener(this);

	    mess = ((View) v.findViewById(R.id.mess_tab));
	    mess.setOnClickListener(this);
	    
	    v.findViewById(R.id.title_bar_left_menu).setOnClickListener(this);

	    mainAdapter = new TestFragmentAdapter(getChildFragmentManager(), getMainFragments());

	    main = (ViewPager)v.findViewById(R.id.main);
	    main.setOffscreenPageLimit(0);
	    main.setLayerType(View.LAYER_TYPE_HARDWARE, null);

	    main.setAdapter(mainAdapter);
	   
	    main.setOnPageChangeListener(this);
	    setActionChoice(0);
	    //((SampleCirclesDefault)getActivity()).getResideMenu().addIgnoredView(main);
		 return v;
	 }
	 
	 
	 private List<Fragment> getMainFragments() {
			//DefaultFragment f = new DefaultFragment();
//			Social_Fragment	f = Social_Fragment.newInstance("prof",R.layout.social, user);
			Social_Activity_Fragment	f = Social_Activity_Fragment.newInstance("prof",R.layout.activity, user);

			Messaging_Fragment	profile_info_charts = Messaging_Fragment.newInstance("messaging",R.layout.messaging);
			//f.setUser(user);
			
			fList = new ArrayList<Fragment>();

		    fList.add(f);
		    fList.add(profile_info_charts);

		    return fList;
	}
	 
	 @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.social_tab:
			main.setCurrentItem(0,false);
			setActionChoice(0);
			break;
		case R.id.mess_tab:
			main.setCurrentItem(1,false);
			setActionChoice(1);
			break;
		case R.id.title_bar_left_menu:
            ((SampleCirclesDefault)getActivity()).getResideMenu().openMenu(ResideMenu.DIRECTION_LEFT);
            break;
		default:
			break;
		}
		
	}
	 

		private void setActionChoice(int pos){
			switch (pos) {
			case 0:
				selectTab(true);
				break;
			case 1:
				selectTab(false);
				break;
			default:
				break;
			}
		}
		
		void selectTab(boolean soc){
			social.setSelected(soc);
			mess.setSelected(!soc);
		}

	 
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) 
	    {
	        super.onActivityCreated(savedInstanceState);
 	    }

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
		 */
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			//this.mTabHost.setCurrentTab(position);
			setActionChoice(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}
}
