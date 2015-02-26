package dev.vision.relationshipninjas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.viewpagerindicator.CirclePageIndicator;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.GiftFragment.Load_Gifts;
import dev.vision.relationshipninjas.purchase.Purchase_Activity;
import dev.vision.relationshipninjas.views.ExpandableHeightGridView;
import dev.vision.relationshipninjas.views.Feedback;
import dev.vision.relationshipninjas.views.UnderlineTextView;

public class oldGift_Activity extends Activity {
	


	Handler handler= new Handler();
	private Runnable runPager;
	private TestFragmentAdapter mAdapter;
	private boolean mCreated;
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;

	protected boolean relations;
	public ItemGridAdapter giftsAdapter;
	protected ArrayList<Item> gifts = new ArrayList<Item>();
	private Item main;
	private ExpandableHeightGridView mGridView;
	private ProgressDialog pd;
	private String rId;
	private String eId;
	private UnderlineTextView moreDetails;
	private RelativeLayout extraInfo;
	private RatingBar rating;
	private TextView ratingtxt;
	private TextView title;
	private TextView price;
	
    @SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
	        Window w = getWindow();
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);   
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gift);
        mGridView = (ExpandableHeightGridView) findViewById(R.id.gridView1);
		moreDetails = (UnderlineTextView) findViewById(R.id.textView9);
		extraInfo = (RelativeLayout) findViewById(R.id.relativeLayout2);
		rating = (RatingBar) findViewById(R.id.ratingBar1);
		rating.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		title = (TextView) findViewById(R.id.TextView01);
		price = (TextView) findViewById(R.id.textView1);

		ratingtxt = (TextView) findViewById(R.id.textView2);
		moreDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View vv) {
				if(vv.getVisibility() == View.VISIBLE){
					vv.setVisibility(View.GONE);
					extraInfo.setVisibility(View.VISIBLE);
					rating.setVisibility(View.VISIBLE);
					ratingtxt.setVisibility(View.VISIBLE);
				}
			}
		});
		
		pd= ProgressDialog.show(this, null, "Loading Gifts!!");
		Bundle b = getIntent().getExtras();
		rId = b.getString("rId", "");
		eId = b.getString("eId", "");
		

        options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(null)
        .showImageOnFail(null)
        .showImageOnLoading(null)
        .resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		.threadPoolSize(5)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.defaultDisplayImageOptions(options)
		.build();
		imageLoader.init(config);
		
		giftsAdapter = new ItemGridAdapter(this, gifts);
		mGridView.setExpanded(true);

		mGridView.setAdapter(giftsAdapter);
	
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				populateWithGift(pos);				
			}
		});
		new Load_Gifts().execute(rId, eId);

		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent().setClass(oldGift_Activity.this, Purchase_Activity.class));
			}
		});
	}
	
 
    /**
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onPause()
    {
        super.onPause();
        handler.removeCallbacks(runPager);
    }
 
    /**
     * Set the ViewPager adapter from this or from a subclass.
     * 
     * @author chris.jenkins
     * @param adapter This is a FragmentStatePagerAdapter due to the way it creates the TAG for the 
     * Fragment.
     */
    protected void setAdapter(FragmentPagerAdapter adapter)
    {
        mAdapter = (TestFragmentAdapter) adapter;
        runPager = new Runnable() {
 
			@Override
            public void run()
            {
            	mPager.setAdapter(mAdapter);
                mIndicator.setViewPager(mPager);	
            }
        };
        if (mCreated)
        {
            handler.post(runPager);
        }
    }
 

	

	
	
	
	class Load_Gifts extends AsyncTask<Object, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Object... params) {
			gifts.clear();
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.ITEM);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
				json.put("operation", "getsuggestions");
				json.put("relationshipid", params[0]);
				json.put("eventid", params[1]);

				request.setEntity(new StringEntity(json.toString()));
				HttpResponse response = client.execute(request);
			 
				
				if(response.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					Log.d("Result", result.toString());
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					if(success){
						JSONArray jsa = js.getJSONArray("items");
						for(int i= 0; i < jsa.length(); i++){
							JSONObject r = (JSONObject) jsa.get(i);
							Item it = new Item(r);
							gifts.add(it);
						}
						
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return success;
		}

		@Override
		public void onPostExecute(Boolean result){
			if(gifts != null && gifts.size() > 0)
			{
				populateWithGift(0);
			}
		    if (mGridView.getAdapter() instanceof ItemGridAdapter) {
    			giftsAdapter = (ItemGridAdapter) mGridView.getAdapter();
    		}
			
			//giftsAdapter.clear(gifts);
		    giftsAdapter.notifyDataSetChanged();
	        if(pd != null && pd.isShowing())
	        	pd.dismiss();
		}
		   
	}

	public void populateWithGift(int i) {
		if(main!=null)
		gifts.add(main);
		main= gifts.get(i);
		gifts.remove(i);
		//giftsAdapter.clear(gifts);
	    giftsAdapter.notifyDataSetChanged();
		title.setText(main.getName());
		price.setText(main.getPrice());
		imageLoader.displayImage(main.getImage(), (ImageView) findViewById(R.id.imageView1), options);
        ((TextView) findViewById(R.id.textView4)).setText(main.getDetaileddescription());

		((Feedback) findViewById(R.id.RatingBar01)).setRating(main.getChic().length()> 0 ? Integer.valueOf(main.getChic()) : 0);
        ((Feedback) findViewById(R.id.RatingBar02)).setRating(main.getCharm().length()> 0 ? Integer.valueOf(main.getCharm()) : 0);
        ((Feedback) findViewById(R.id.RatingBar03)).setRating(main.getShock().length()> 0 ? Integer.valueOf(main.getShock()) : 0);
     
        rating.setProgress(main.getRating().length()> 0 ? Integer.valueOf(main.getRating()) : 0);
			
	}

}