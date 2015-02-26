package dev.vision.split.it.fragments;


import java.util.ArrayList;
import java.util.List;

import com.github.siyamed.shapeimageview.HexagonImageView;

import dev.vision.split.it.R;
import dev.vision.split.it.Table;
import dev.vision.split.it.activities.PulldownViewActivity;
import dev.vision.split.it.activities.Reciept;
import dev.vision.split.it.activities.ShowMapActivity;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.user.Customer;
import dev.vision.split.it.user.ME;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Open_Fragment extends TitleFragment implements OnItemClickListener {
	

	View v;
	
	public Open_Fragment(String title) {
		super(title);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.open_tables_list, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        showTables();

	}
	public void showTables() {
    	ListView lv = (ListView) v.findViewById(R.id.table_layout);
    	lv.setOnItemClickListener(this);
		List<Table> tables = new ArrayList<Table>();

    	for(String n : APP.names){
    		ME me = new ME();

	    	Table t = new Table();
	    	t.setCreator(me);
	    	t.setID(APP.DEBUG_ID, "");
	    	t.setNumber("");
	    	t.getCreator().setName(n);
	    	t.getCreator().setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.default_person)).getBitmap());
	    	
	    	
			for(String name : APP.names){
	    		Customer c = new Customer();
	        	c.setId(APP.DEBUG_ID);
	        	c.setName(name);
	        	c.setImage_url("");
	        	t.addCustomer(c);
	    	}
	    	
	    	tables.add(t);    	
    	}
		TestAdapter mAdapter = new TestAdapter( getActivity(), R.layout.list_item, R.id.userPic, tables );

		lv.setAdapter(mAdapter);
    }
	
	   class TestAdapter extends BaseAdapter {
			
			List<Table> mItems;
			LayoutInflater mInflater;
			int mResource;
			int mTextResId;
			
			public TestAdapter( Context context, int resourceId, int textViewResourceId, List<Table> objects ) {
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
				Table t = getItem( position );
				
				HexagonImageView creatorPic = (HexagonImageView) convertView.findViewById( mTextResId );
				TextView creatorTxt = (TextView) convertView.findViewById( R.id.TableName );
				TextView invitee = (TextView) convertView.findViewById( R.id.invitee );

				creatorPic.setImageBitmap( t.getCreatorImage() );
				creatorTxt.setText( t.getCreatorName() );;
				invitee.setText( t.getListOfInvited() );
				
				return convertView;
			}

			@Override
			public int getCount() {
				return mItems.size();
			}
		

			@Override
			public Table getItem(int pos) {
				return mItems.get(pos);
			}
		}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		//APP.ME.setMarker((HexagonImageView) view.findViewById( R.id.userPic));

		startActivity(new Intent(getActivity(), Reciept.class));
	}
}
