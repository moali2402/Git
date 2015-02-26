package dev.vision.split.it.activities;

import java.util.ArrayList;
import java.util.List;

import com.github.siyamed.shapeimageview.HexagonImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import dev.vision.split.it.R;
import dev.vision.split.it.Restaurant;
import dev.vision.split.it.Table;
import dev.vision.split.it.activities.Reciept.TestAdapter;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.extras.PullScrollView;
import dev.vision.split.it.user.Customer;

public class Activity_Restaurant extends Activity implements PullScrollView.OnTurnListener {
    private PullScrollView mScrollView;
    private View mHeadImg;
	Restaurant rs = new Restaurant(APP.DEBUG_ID, APP.DEBUG_ID, 5, 1000, null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_pull);

        initView();

        showTables();
    }

    protected void initView() {
        mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
        mHeadImg = (View) findViewById(R.id.background_img);


        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);
    }
    
    

    public void showTables() {
    	ListView lv = (ListView) findViewById(R.id.table_layout);
    
		List<Table> tables = new ArrayList<Table>();

    	
    	Table t = new Table();
    	t.setCreator(APP.ME);
    	t.setID(rs.getRestaurant_id(), "");
    	t.setNumber("");
    	t.getCreator().setName("MO");

    	int i = 0;
		while(i<6){
    		Customer c = new Customer();
        	c.setId(APP.DEBUG_ID);
        	c.setName("Keira Mcelligott");
        	c.setImage_url("");
        	t.addCustomer(c);
        	i++;
    	}
    	

    	tables.add(t);
    	tables.add(t);
    	tables.add(t);
    	
    	tables.add(t);
    	tables.add(t);
    	tables.add(t);
    	
    	tables.add(t);
    	tables.add(t);
    	tables.add(t);
    	
    	tables.add(t);
    	tables.add(t);
    	tables.add(t);
    	
    	tables.add(t);
    	tables.add(t);
    	tables.add(t);
    	
    	tables.add(t);
    	tables.add(t);
    	tables.add(t);


		TestAdapter mAdapter = new TestAdapter( this, R.layout.list_item, R.id.userPic, tables );

		lv.setAdapter(mAdapter);
    	mScrollView.setList(lv);
    }

    @Override
    public void onTurn() {

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

			creatorPic.setImageBitmap( ((BitmapDrawable)getResources().getDrawable(R.drawable.default_person)).getBitmap() );
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
}