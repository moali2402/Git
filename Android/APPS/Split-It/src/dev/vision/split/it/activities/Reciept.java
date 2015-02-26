package dev.vision.split.it.activities;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.AdapterView.OnItemClickListener;
import it.sephiroth.android.library.widget.HListView;

import java.util.List;
import java.util.Random;

import com.github.siyamed.shapeimageview.HexagonImageView;

import dev.vision.split.it.R;
import dev.vision.split.it.Table;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.user.Customer;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Reciept extends ActionBarActivity implements OnItemClickListener {

    private TestAdapter mAdapter;
	private HListView listView;
	private Table t;
	//TableOrder or;
	private TextView tooltip;
	private TextView tips;
	private TextView payable;
	private TextView remaining;
	private TextView billtotal;
	private TextView share;
	
	//Orders class to have table customers order

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciept);
		//t = (Table) getIntent().getExtras().getSerializable("Table");
        
		final SeekBar sb = (SeekBar) findViewById(R.id.seekBar1);

        billtotal = (TextView) findViewById(R.id.textView2);
        remaining = (TextView) findViewById(R.id.TextView01);
        share = (TextView) findViewById(R.id.TextView04);
        tips = (TextView) findViewById(R.id.TextView05);
        //TextView spcredit = (TextView) findViewById(R.id.spcredit);
        payable = (TextView) findViewById(R.id.TextView10);
		tooltip = (TextView) findViewById(R.id.tooltip);
		listView = (HListView) findViewById( R.id.hListView1 );
        
		
		
		setUpDemo();
		
		
		billtotal.setText("L.E "+Stringfy(getOrderPTotal(),100));
		remaining.setText("L.E "+Stringfy(getRest(),100));
		share.setText("L.E "+Stringfy(getShare(),100));
		//tips.setText("L.E "+Stringfy(sb.getProgress(), 1000));
		//payable.setText("L.E "+Stringfy((sb.getProgress()/10f) +(sb.getProgress()),100));
		
		setValues(sb.getProgress());
		
		
		/*
		RangeSeekBar<Integer> rangeSeekBar = (RangeSeekBar<Integer>) findViewById(R.id.rangeseek);
        // Set the range
        rangeSeekBar.setRangeValues(0, 29999);
        rangeSeekBar.setNormalizedMaxValue(16099);

        rangeSeekBar.invalidate();
        */
        mAdapter = new TestAdapter( this, R.layout.reciept_list_item, R.id.userPic, null );
		listView.setOnItemClickListener( this );
		listView.setAdapter( mAdapter );
		
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				tooltip.setVisibility(View.INVISIBLE);
				final Animation animationFadeIn = AnimationUtils.loadAnimation(Reciept.this, android.R.anim.fade_out);
			    tooltip.startAnimation(animationFadeIn);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {				
			}
			
			@SuppressLint("NewApi") @Override
			public void onProgressChanged(SeekBar mSeekBar, int v, boolean arg2) {
				int MIN = getShare();
				if (v >= MIN ) {

					tooltip.setText("L.E "+String.format("%.2f",(v/100f)));
					float w = sb.getThumb().getBounds().centerX();     

				    //Get the thumb bound and get its left value
				    //float x = mSeekBar.getThumb().getBounds().left + (w/2f) ;
				    //((RelativeLayout.LayoutParams)mSeekBar.getLayoutParams()).leftMargin
				    w-=(tooltip.getMeasuredWidth()/2);
				    tooltip.setTranslationX(((LinearLayout.LayoutParams)sb.getLayoutParams()).leftMargin+w);				
				  
				    tooltip.setVisibility(View.VISIBLE);
					setValues(v);
					
					
				}else{
					sb.setProgress(MIN);
				}
			}
		});
		sb.setMax(getRest());
		tooltip.post(new Runnable() {
			
			@Override
			public void run() {
				sb.setProgress(getShare());
				
			}
		});

    }
	
	private void setUpDemo() {
		t = new Table(APP.ME,String.valueOf(new Random().nextInt()),"20");
		t.createOrder(APP.ME);
		for(String name : APP.names){
    		Customer c = new Customer();
        	c.setId(APP.DEBUG_ID + String.valueOf(new Random().nextInt()));
        	c.setName(name);
        	c.setImage_url("");
        	t.addCustomer(c);
    		t.createOrder(c);
    	}		
	}

	protected void setValues(int v) {
		tips.setText("L.E "+Stringfy(v, 1000));
		payable.setText("L.E "+Stringfy((v/10f) +(v),100));
		
	}

	String Stringfy(float f, float over){
		return 	String.format("%.2f",(f/over));
	}
	
	int getShare(){
		return t.getCustomerOrderPTotal(APP.ME);
	}
	
	int getRest(){
		return (getOrderPTotal() - getPaid());
	}

    private int getPaid() {
		return (int) (166.00 * 100);
	}
    
    private int getOrderPTotal(){
    	return t.getTableOrderPTotal();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    class TestAdapter extends BaseAdapter {
		
		List<String> mItems;
		LayoutInflater mInflater;
		int mResource;
		int mTextResId;
		
		public TestAdapter( Context context, int resourceId, int textViewResourceId, List<String> objects ) {
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
			
			HexagonImageView textView = (HexagonImageView) convertView.findViewById( mTextResId );
			textView.setImageResource((Integer) getItem( position ) );
						
			if(position == 1){
				textView.setOverlayEnabled(true);
			}
			return convertView;
		}

		@Override
		public int getCount() {
			return t.noOfDinersOnTable();
		}

		@Override
		public Object getItem(int arg0) {
			return R.drawable.default_person;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}
}
