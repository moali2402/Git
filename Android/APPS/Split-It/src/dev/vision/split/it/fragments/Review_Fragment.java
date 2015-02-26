package dev.vision.split.it.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.github.siyamed.shapeimageview.HexagonImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar;
import dev.vision.split.it.R;
import dev.vision.split.it.Review;
import dev.vision.split.it.Table;
import dev.vision.split.it.activities.Reciept;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.extras.BoldGillsansTextView;
import dev.vision.split.it.extras.GillsansTextView;
import dev.vision.split.it.user.Customer;

public class Review_Fragment extends TitleFragment implements OnItemClickListener {
	View v;
	
	public Review_Fragment(String title){
		super(title);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_list, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	    showTables();
	}
	

	
	public void showTables() {
    	ListView lv = (ListView) v.findViewById(R.id.listView);
    	lv.setOnItemClickListener(this);
		List<Review> reviews = new ArrayList<Review>();
		Customer c = APP.ME;

		for(int i= 0; i<5; i++){
			String time = Calendar.getInstance().getTime().toLocaleString();
			int rating = i+1;
			String feedback = "if you love chicken then there's is no other place that can top this place! The best go to fast food restaurant by far! Would deffo recommend to everyone. Perfection and extremely yum!";
			
			Review r = new Review(c,time,rating,feedback);
			
			reviews.add(r);
		}

		ReviewAdapter mAdapter = new ReviewAdapter( getActivity(), R.layout.review_item, reviews );

		lv.setAdapter(mAdapter);
    }
	
	   class ReviewAdapter extends BaseAdapter {
			
			List<Review> mItems;
			LayoutInflater mInflater;
			int mResource;
			
			public ReviewAdapter( Context context, int resourceId, List<Review> objects ) {
				mInflater = LayoutInflater.from( context );
				mResource = resourceId;
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
				
				BoldGillsansTextView name = (BoldGillsansTextView) convertView.findViewById(R.id.name);
				HexagonImageView userpic = (HexagonImageView) convertView.findViewById(R.id.hexagonImageView1);
				RatingBar rating = (RatingBar) convertView.findViewById(R.id.review_ratingbar);
				GillsansTextView time = (GillsansTextView) convertView.findViewById(R.id.time);
				GillsansTextView feedback = (GillsansTextView) convertView.findViewById(R.id.feedback);

				name.setText(getItem(position).getCustomerName());
				time.setText(getItem(position).getTime());
				rating.setRating(getItem(position).getRating());
				feedback.setText(getItem(position).getFeedback());
				userpic.setImageBitmap(getItem(position).getCustomerImage());
				return convertView;
			}

			@Override
			public int getCount() {
				return mItems.size();
			}
		

			@Override
			public Review getItem(int pos) {
				return mItems.get(pos);
			}
		}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		startActivity(new Intent(getActivity(), Reciept.class));
	}


}
