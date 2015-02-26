package dev.vision.split.it.fragments;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.siyamed.shapeimageview.HexagonImageView;

import dev.vision.split.it.R;
import dev.vision.split.it.Table;
import dev.vision.split.it.activities.Reciept;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.extras.GillsansTextView;
import dev.vision.split.it.extras.QuickReturnListView;
import dev.vision.split.it.menu.Item;
import dev.vision.split.it.user.Customer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Menu_Category_Fragment extends TitleFragment implements OnItemClickListener, OnScrollListener, OnGlobalLayoutListener {
	View v;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	private TranslateAnimation anim;
	private View mQuickReturnView;
	private int mQuickReturnHeight;
	QuickReturnListView lv;

	public Menu_Category_Fragment(String title){
		super(title);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_q_list, null);
    	lv = (QuickReturnListView) v.findViewById(R.id.listView);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	    showTables();
	}
	

	
	public void showTables() {
    	lv.setOnItemClickListener(this);
		List<Item> tables = new ArrayList<Item>();

		TestAdapter mAdapter = new TestAdapter( getActivity(), R.layout.menu_item, R.id.userPic, tables );

		lv.setAdapter(mAdapter);
		setQListTrackers();
    }
	
	
   class TestAdapter extends BaseAdapter {
		
		List<Item> mItems;
		LayoutInflater mInflater;
		int mResource;
		int mTextResId;
		
		public TestAdapter( Context context, int resourceId, int textViewResourceId, List<Item> objects ) {
			//super( context, resourceId, textViewResourceId, objects );
			mInflater = LayoutInflater.from( context );
			mResource = resourceId;
			mTextResId = textViewResourceId;
			mItems = objects;
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
		
		@Override
		public long getItemId( int position ) {
			return position;
		}
		
		@Override
		public int getViewTypeCount() {
			return 3;
		}
		
		@Override
		public int getItemViewType( int position ) {
			return position%3;
		}
		
		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			
			if( null == convertView ) {
				convertView = mInflater.inflate( mResource, parent, false );
			}
			
			GillsansTextView name = (GillsansTextView) convertView.findViewById(R.id.gillsansTextView1);
			GillsansTextView descr = (GillsansTextView) convertView.findViewById(R.id.gillsansTextView2);
			GillsansTextView price = (GillsansTextView) convertView.findViewById(R.id.GillsansTextView01);
			RatingBar rating = (RatingBar) convertView.findViewById(R.id.ratingBar1);


			name.setText("Demo " + position);
			descr.setText("Description for item "+ position);
			price.setText("$"+position + ".00");
			rating.setRating((float) (new Random().nextInt(10)*0.5));
			return convertView;
		}

		@Override
		public int getCount() {
			return 7;
		}
	

		@Override
		public Item getItem(int pos) {
			return mItems.get(pos);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		startActivity(new Intent(getActivity(), Reciept.class));
	}
	public void setQuickReturn(ImageView mQuickReturnView2) {
		this.mQuickReturnView = mQuickReturnView2;
		setQListTrackers();

	}
	
	public void setQListTrackers(){
		if( lv!=null && mQuickReturnView!=null ){

			lv.setOnScrollListener(null);
			lv.getViewTreeObserver().addOnGlobalLayoutListener(this);
			lv.setOnScrollListener(this);
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		mScrollY = 0;
		int translationY = 0;

		if (lv.scrollYIsComputed()) {
			mScrollY = lv.getComputedScrollY();
		}

		int rawY = mScrollY;

		switch (mState) {
		case STATE_OFFSCREEN:
			if (rawY >= mMinRawY) {
				mMinRawY = rawY;
			} else {
				mState = STATE_RETURNING;
			}
			translationY = rawY;
			break;

		case STATE_ONSCREEN:
			if (rawY > mQuickReturnHeight) {
				mState = STATE_OFFSCREEN;
				mMinRawY = rawY;
			}
			translationY = rawY;
			break;

		case STATE_RETURNING:

			translationY = (rawY - mMinRawY) + mQuickReturnHeight;

			System.out.println(translationY);
			if (translationY < 0) {
				translationY = 0;
				mMinRawY = rawY + mQuickReturnHeight;
			}

			if (rawY == 0) {
				mState = STATE_ONSCREEN;
				translationY = 0;
			}

			if (translationY > mQuickReturnHeight) {
				mState = STATE_OFFSCREEN;
				mMinRawY = rawY;
			}
			break;
		}

		/** this can be used if the build is below honeycomb **/
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
			anim = new TranslateAnimation(0, 0, translationY,
					translationY);
			anim.setFillAfter(true);
			anim.setDuration(0);
			mQuickReturnView.startAnimation(anim);
		} else {
			mQuickReturnView.setTranslationY(translationY);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	
	@Override
	public void onGlobalLayout() {
		mQuickReturnHeight = mQuickReturnView.getHeight();
		lv.computeScrollY();
		lv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
	}

}
