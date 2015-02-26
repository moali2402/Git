/**
 * 
 */
package dev.vision.split.it.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.daimajia.androidviewhover.BlurLayoutLast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import dev.vision.split.it.GeoPoint;
import dev.vision.split.it.R;
import dev.vision.split.it.Restaurant;
import dev.vision.split.it.Table;
import dev.vision.split.it.activities.PulldownViewActivity;
import dev.vision.split.it.activities.Reciept;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.extras.BoldGillsansTextView;
import dev.vision.split.it.extras.DistanceTextView;
import dev.vision.split.it.extras.GillsansTextView;
import dev.vision.split.it.extras.RectangleView;
import dev.vision.split.it.fragments.Menu_Category_Fragment.TestAdapter;

/**
 * @author Mo
 *
 */
@SuppressLint("NewApi") public class Main_Fragment extends Fragment {

	private View v;
	private ListView lv;
	private ListView featured_lv;

	RestaurantAdapter mAdapter;
	LayoutInflater inflater;
	/**
	 * @param title
	 */
	public Main_Fragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		v = inflater.inflate(R.layout.fragment_list, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

	
	public ListView showTables(List<Restaurant> restaurants) {
    	lv = (ListView) v.findViewById(R.id.listView);
    	//View header = inflater.inflate(R.layout.featured_header_list, null, false);
    	//featured_lv = (ListView) header.findViewById(R.id.listView);
    	//lv.addHeaderView(header, null, false);


    	lv.setDividerHeight(0);
    	lv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(arg1);
			}
		});
			
    	
		mAdapter = new RestaurantAdapter( getActivity(), R.layout.main_item, restaurants);

//		FeaturedAdapter fAdapter = new FeaturedAdapter( getActivity(), R.layout.featured_item, restaurants);

		lv.setAdapter(mAdapter);
//		featured_lv.setAdapter(fAdapter);
		return lv;
    }
	
	   class RestaurantAdapter extends BaseAdapter {
			
			List<Restaurant> mItems;
			LayoutInflater mInflater;
			int mResource;
			private int color;
			private int pad;

			private class ViewHolder{
				
				public BlurLayoutLast bl;
				public View hover;
				public RatingBar rating;
				public BoldGillsansTextView name;
				public ImageView featured;
				public RectangleView back;
				public DistanceTextView distance;
			};
			
			public RestaurantAdapter( Context context, int resourceId, List<Restaurant> objects ) {
				//super( context, resourceId, textViewResourceId, objects );
				mInflater = LayoutInflater.from( context );
				mResource = resourceId;
				mItems = objects;
				color = getResources().getColor(R.color.apptheme_color);
				pad = getDp(3);
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
				ViewHolder holder = null;
				if( null == convertView ) {
					holder = new ViewHolder();
					convertView = mInflater.inflate( mResource, parent, false );
					holder.bl = (BlurLayoutLast) convertView.findViewById(R.id.blur_layout4);
					holder.name = (BoldGillsansTextView) convertView.findViewById(R.id.name);
					holder.featured = (ImageView) convertView.findViewById(R.id.featured);
					holder.rating = (RatingBar) convertView.findViewById(R.id.ratingBar1);
					holder.back = (RectangleView) convertView.findViewById(R.id.image);
					holder.hover = mInflater.inflate(R.layout.hover_view,null);
					holder.distance = (DistanceTextView) convertView.findViewById(R.id.distance);

					convertView.setTag(holder);
				}else{
					holder= (ViewHolder) convertView.getTag();
				}
				
				Restaurant rs = getItem(position);
				
				holder.back.setImageBitmap(rs.getImage());
		       // MarginLayoutParams params = (MarginLayoutParams) holder.back.getLayoutParams();

				if(rs.isfeatured()){
			        holder.featured.setVisibility(View.VISIBLE);
			        //params.leftMargin = pad; params.topMargin = pad; params.bottomMargin = pad; params.rightMargin = pad;
			        //holder.back.setLayoutParams(params);
			        holder.bl.setPadding(pad,pad,pad,pad);
			        holder.bl.setBackgroundColor(color);
				}
			    else{
			    	holder.featured.setVisibility(View.INVISIBLE);
			    	//params.leftMargin = 0; params.topMargin = 0; params.bottomMargin = 0; params.rightMargin = 0;
			    	holder.bl.setPadding(0, 0, 0, 0);
			        holder.bl.setBackgroundResource(0);
			    }
		        //holder.back.setLayoutParams(params);

				holder.distance.getDistance(rs.getGeoPoint(), APP.ME.getGeoPoint(), 'm');
				
				holder.name.setText(rs.getName());
				holder.rating.setRating(rs.getRating());
				holder.bl.setHoverView(holder.hover);

				holder.bl.addChildAppearAnimator(holder.hover, R.id.content, Techniques.SlideInRight);
			    holder.bl.addChildDisappearAnimator(holder.hover, R.id.content, Techniques.SlideOutRight);
		        
		        
		        if(holder.bl.isHover())
		        holder.bl.dismissHover();

		        
				return convertView;
			}

			@Override
			public int getCount() {
				return mItems.size();
			}
		

			@Override
			public Restaurant getItem(int pos) {
				return mItems.get(pos);
			}
		}
	   
	   public int getDp(int sizeInDp){
		   float scale = getResources().getDisplayMetrics().density;
		   int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
		   return dpAsPixels;
	   }
	   
	   
	public void performClick(int pos) {
		Intent i = new Intent(getActivity(), PulldownViewActivity.class);
		i.putExtra("rs", mAdapter.getItem(pos));
		startActivity(i);
	}

	public ListView getList() {
		return lv;
	}

    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new BlurLayoutDetector());

	class BlurLayoutDetector extends GestureDetector.SimpleOnGestureListener {
		

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, 
		        float velocityX, float velocityY) {
			int poss =  lv.pointToPosition((int)event1.getX(),(int) event1.getY());
	  		int pose =  lv.pointToPosition((int)event2.getX(),(int) event2.getY());
	  		
			if(poss != ListView.INVALID_POSITION && pose != ListView.INVALID_POSITION && poss == pose){
				float x = event1.getX() - event2.getX();
				if( x >= 100){
					View child = getView(poss);
					if(child!=null){
						((BlurLayoutLast) child.findViewById(R.id.blur_layout4)).showHover();		
						return true;
					}
				}
			}
			return false;
			
		};

		View getView(int pos){
			int wantedPosition = pos; // Whatever position you're looking for
			int firstPosition = lv.getFirstVisiblePosition() - lv.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			if (wantedChild < 0 || wantedChild >= lv.getChildCount()) {
			  return null;
			}
			// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
			View wantedView = lv.getChildAt(wantedChild);
			return wantedView;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			int pos =  lv.pointToPosition((int) e.getX(),(int) e.getY());
	  		
			//if( pose != ListView.INVALID_POSITION)
			//lv.performItemClick(getView(pose), pose, pose);
			performClick(pos);
			//Toast.makeText(getActivity(), ""+pose, Toast.LENGTH_SHORT).show();
			return false;
		}
	};
	
	

}
