package dev.vision.split.it.activities;

import java.util.ArrayList;
import java.util.List;

import com.github.siyamed.shapeimageview.HexagonImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import dev.vision.split.it.FragmentAdapter;
import dev.vision.split.it.R;
import dev.vision.split.it.Restaurant;
import dev.vision.split.it.Table;
import dev.vision.split.it.activities.Reciept.TestAdapter;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.extras.BoldGillsansTextView;
import dev.vision.split.it.extras.GillsansTextView;
import dev.vision.split.it.extras.PagerSlidingTabStrip;
import dev.vision.split.it.extras.PullScrollView;
import dev.vision.split.it.extras.RectangleView;
import dev.vision.split.it.fragments.About_Us_Fragment;
import dev.vision.split.it.fragments.Menu_Category_Fragment;
import dev.vision.split.it.fragments.Menu_Fragment;
import dev.vision.split.it.fragments.Open_Fragment;
import dev.vision.split.it.fragments.Review_Fragment;
import dev.vision.split.it.fragments.TitleFragment;
import dev.vision.split.it.user.Customer;
import dev.vision.split.it.user.ME;

public class PulldownViewActivity extends FragmentActivity implements PullScrollView.OnTurnListener {
    private PullScrollView mScrollView;
    private View mHeadImg;
	Restaurant rs;// = new Restaurant(APP.DEBUG_ID, "NANDOS", 5, 1000, null);
	ViewPager vp;
	private FragmentAdapter mainAdapter;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_pull);
        rs =(Restaurant) getIntent().getExtras().getSerializable("rs");
		
        initRestaurant();
        initView();
    }

    private void initRestaurant() {
    	BoldGillsansTextView name = (BoldGillsansTextView) findViewById(R.id.name);
    	GillsansTextView extra_info = (GillsansTextView) findViewById(R.id.extra_info);
    	RatingBar rating = (RatingBar) findViewById(R.id.ratingBar1);
    	ImageView background = (ImageView) findViewById(R.id.image);

    	background.setImageBitmap(rs.getImage());
    	name.setText(rs.getName());
    	
    	rating.setRating(rs.getRating());
    	extra_info.setText("Address : Galleria Outlet Centre,Comet Way, Hatfield, Hertfordshire AL10 0XR \n"
    			+ "Openning Hours :\n Sun - Thur : 11:00 - 22:30 - Fri - Sat : 11:00 - 23:00");

	}
    
	protected void initView() {
        vp = (ViewPager) findViewById(R.id.pager);
        mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
        mHeadImg = (View) findViewById(R.id.background_img);
	    mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.user_name);
	    final List<TitleFragment> lf = getFragments();
        mainAdapter = new FragmentAdapter(getSupportFragmentManager(), lf);

        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);
        
        vp.setAdapter(mainAdapter);
        vp.setOffscreenPageLimit(lf.size());

	    mPagerSlidingTabStrip.setViewPager(vp);
		mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {
					
			@Override
			public void onPageSelected(int p) {
				if(p ==0)
				((Menu_Fragment) lf.get(p)).setCurrentQuickReturnForPos();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
    }
    
	private void call(){
    	String url = "tel:"+ rs.getNumber();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivity(intent);
    }
	
    public List<TitleFragment> getFragments(){
    	List<TitleFragment> lf = new ArrayList<TitleFragment>();
    	Menu_Fragment mf = new Menu_Fragment("Menu");
    	lf.add(mf);
    	Open_Fragment of = new Open_Fragment("Open Tables");
    	lf.add(of);
    	Review_Fragment rf = new Review_Fragment("Reviews");
     	lf.add(rf);
     	
     	About_Us_Fragment af = new About_Us_Fragment("About Us");
     	lf.add(af);
     	
    	return lf;
    }
    

    

    @Override
    public void onTurn() {

    }

 
}