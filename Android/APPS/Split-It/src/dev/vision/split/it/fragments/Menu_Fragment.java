package dev.vision.split.it.fragments;


import java.util.ArrayList;
import java.util.List;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import dev.vision.split.it.FragmentAdapter;
import dev.vision.split.it.R;
import dev.vision.split.it.Table;
import dev.vision.split.it.activities.Reciept;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.extras.PagerSlidingTabStrip;
import dev.vision.split.it.user.Customer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Menu_Fragment extends TitleFragment implements OnClickListener {
	View v;
	ViewPager vp;
	private TitlePageIndicator mPagerSlidingTabStrip;
	List<TitleFragment>  lf;
	private ImageView mQuickReturnView;

	public Menu_Fragment(){
		super("Menu");
	}
	
	public Menu_Fragment(String title){
		super(title);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.menu_fargment, null);
        vp = (ViewPager) v.findViewById(R.id.menu_pager);
        mQuickReturnView = (ImageView) v.findViewById(R.id.imageView1);
	    mPagerSlidingTabStrip = (TitlePageIndicator) v.findViewById(R.id.menu_slidingstrip);
	    mPagerSlidingTabStrip.setFooterIndicatorStyle(IndicatorStyle.Triangle);
	    mPagerSlidingTabStrip.setFooterColor(Color.WHITE);
	    mPagerSlidingTabStrip.setFooterLineHeight(2);
	    mPagerSlidingTabStrip.setFooterIndicatorHeight(25);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQuickReturnView.setOnClickListener(this);
		FragmentAdapter fa = new FragmentAdapter(getChildFragmentManager(), getFragments());
        vp.setAdapter(fa);
        vp.setOffscreenPageLimit(lf.size());
        mPagerSlidingTabStrip.setViewPager(vp);
        mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int p) {
				setQuickReturnForPos(p);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
        setQuickReturnForPos(0);
	}
	
	
	
	public void setQuickReturnForPos(int p) {
		((Menu_Category_Fragment)lf.get(p)).setQuickReturn(mQuickReturnView);
	}

	public List<TitleFragment> getFragments(){
	    	lf = new ArrayList<TitleFragment>();
	    	Menu_Category_Fragment mf = new Menu_Category_Fragment("Breakfast");
	    	lf.add(mf);
	    	
	    	mf = new Menu_Category_Fragment("Appetizer");
	    	lf.add(mf);
	    	
	    	mf = new Menu_Category_Fragment("Sandwiches");
	    	lf.add(mf);
	    	
	    	mf = new Menu_Category_Fragment("Soup");
	    	lf.add(mf);
		    
	        mf = new Menu_Category_Fragment("Salads");
		    lf.add(mf);
	    	
	    	mf = new Menu_Category_Fragment("Main Dish");
		    lf.add(mf);
		    

	    	mf = new Menu_Category_Fragment("Side");
		    lf.add(mf);
		    
		    mf = new Menu_Category_Fragment("Dessert");
		    lf.add(mf);
		    
		    mf = new Menu_Category_Fragment("Drinks");
		    lf.add(mf);
	    	
	    	return lf;
	 }

	public void setCurrentQuickReturnForPos() {
		setQuickReturnForPos(vp.getCurrentItem());
	}

	@Override
	public void onClick(View arg0) {
	}
	
}
