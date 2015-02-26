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
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.WrapperListAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.viewpagerindicator.CirclePageIndicator;

import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.Classes.Events;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.Classes.Event.ALERTLEVEL;
import dev.vision.relationshipninjas.Classes.Event.DATEUNIT;
import dev.vision.relationshipninjas.Classes.Event.STATUS;
import dev.vision.relationshipninjas.purchase.Purchase_Activity;
import dev.vision.relationshipninjas.views.CircularImage;
import dev.vision.relationshipninjas.views.CircularImageResizable;
import dev.vision.relationshipninjas.views.ExpandableHeightGridView;
import dev.vision.relationshipninjas.views.Feedback;
import dev.vision.relationshipninjas.views.InScrollableListView;
import dev.vision.relationshipninjas.views.RoundedImage;
import dev.vision.relationshipninjas.views.UnderlineTextView;


public class GiftFragment extends Fragment{
	

	private View v;
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

	public static GiftFragment NewInstance(String string, String string2) {
		GiftFragment d =new GiftFragment();
		Bundle bun = new Bundle();
		bun.putString("rId", string);
		bun.putString("eId", string2);

		d.setArguments(bun);
		return d;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v= inflater.inflate(R.layout.gift, null);

		
		return v;
	}
	
	
	public void onViewCreated(View v, Bundle savedInstanceState){
		super.onViewCreated(v, savedInstanceState);
		mGridView = (ExpandableHeightGridView) v.findViewById(R.id.gridView1);
		moreDetails = (UnderlineTextView) v.findViewById(R.id.textView9);
		extraInfo = (RelativeLayout) v.findViewById(R.id.relativeLayout2);
		rating = (RatingBar) v.findViewById(R.id.ratingBar1);
		rating.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		title = (TextView) v.findViewById(R.id.TextView01);
		price = (TextView) v.findViewById(R.id.textView1);

		ratingtxt = (TextView) v.findViewById(R.id.textView2);
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
 

	

	@SuppressLint("NewApi") 
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		pd= ProgressDialog.show(getActivity(), null, "Loading Gifts!!");
		rId = getArguments().getString("rId");
		eId = getArguments().getString("eId");
		

        options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(null)
        .showImageOnFail(null)
        .showImageOnLoading(null)
        .resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
		.threadPoolSize(2)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.defaultDisplayImageOptions(options)
		.build();
		imageLoader.init(config);
		
		giftsAdapter = new ItemGridAdapter(getActivity(), gifts);
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

		v.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent().setClass(getActivity(), Purchase_Activity.class));
			}
		});
		

	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
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

	        if(pd != null && pd.isShowing())
	        	pd.dismiss();
		}
		   
	}

	public void populateWithGift(int i) {
		main= gifts.get(i);
		title.setText(main.getName());
		price.setText(main.getPrice());
		imageLoader.displayImage(main.getImage(), (ImageView) v.findViewById(R.id.imageView1), options);
        ((TextView) v.findViewById(R.id.textView4)).setText(main.getDetaileddescription());

		((Feedback) v.findViewById(R.id.RatingBar01)).setRating(main.getChic().length()> 0 ? Integer.valueOf(main.getChic()) : 0);
        ((Feedback) v.findViewById(R.id.RatingBar02)).setRating(main.getCharm().length()> 0 ? Integer.valueOf(main.getCharm()) : 0);
        ((Feedback) v.findViewById(R.id.RatingBar03)).setRating(main.getShock().length()> 0 ? Integer.valueOf(main.getShock()) : 0);
     
        rating.setProgress(main.getRating().length()> 0 ? Integer.valueOf(main.getRating()) : 0);
			
	}

	

	
}
