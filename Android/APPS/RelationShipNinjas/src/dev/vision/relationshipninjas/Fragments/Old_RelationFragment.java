package dev.vision.relationshipninjas.Fragments;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
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

import dev.vision.relationshipninjas.Gift_Activity;
import dev.vision.relationshipninjas.GiftsAdapter;
import dev.vision.relationshipninjas.Main_Activity;
import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Static;
import dev.vision.relationshipninjas.TestFragmentAdapter;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.Classes.Events;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.Classes.Event.ALERTLEVEL;
import dev.vision.relationshipninjas.Classes.Event.DATEUNIT;
import dev.vision.relationshipninjas.Classes.Event.STATUS;
import dev.vision.relationshipninjas.R.color;
import dev.vision.relationshipninjas.R.id;
import dev.vision.relationshipninjas.R.layout;
import dev.vision.relationshipninjas.views.CircularImage;
import dev.vision.relationshipninjas.views.CircularImageResizable;
import dev.vision.relationshipninjas.views.InScrollableListView;
import dev.vision.relationshipninjas.views.RoundedImage;

public class Old_RelationFragment extends Fragment{
	Event ev ;

	ViewTreeObserver.OnGlobalLayoutListener vto = new ViewTreeObserver.OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			mHeaderHeight = mHeader.getMeasuredHeight();
			//mPlaceHolder.getLayoutParams().height = mQuickReturnHeight;
			
			mListView.computeScrollY();
			
			mCachedVerticalScrollRange = mListView.getListHeight();

			placeHolderHeight = mPlaceHolder.getMeasuredHeight();
			mListView.setOnScrollListener(new onPauseScroll(imageLoader , true, true));  

		}
	};
	ProgressDialog relationPd;

	private List<Fragment> fList;
	private View mHeader;
	private InScrollableListView mListView;
	private View mPlaceHolder;
	private RelativeLayout mQuickReturnView;
	protected Object viewH;
	private int mCachedVerticalScrollRange;
	private DisplayImageOptions options;
	private RelativeLayout actionBar;
	protected int mHeaderHeight;
	private ImageView taphereim;
	private TextView tapheretxt;
	protected boolean fadable = true;
	private int mScrollY;
	private int placeHolderHeight;
	private Events events =new Events();
	int pos=0;

	protected boolean relations;
	public GiftsAdapter giftsAdapter;
	protected ArrayList<Item> gifts;
	private CircularImage actionImg;

	
	private TextView actionTxt;


	private View v;

	private ViewPager mPager;

	private CirclePageIndicator mIndicator;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private TestFragmentAdapter mAdapter;

	private int viewWidth;

	private int viewHeight;
	private Runnable runPager;
	private boolean mCreated;
	private Handler handler = new Handler();

	static String TAG = "rel";
	

	public static Old_RelationFragment NewInstance(int pos) {
		Old_RelationFragment d =new Old_RelationFragment();
		Bundle bun = new Bundle();
		bun.putInt("pos", pos);
		d.setArguments(bun);
		
		
		return d;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v= inflater.inflate(R.layout.main_p, null);
		mQuickReturnView = (RelativeLayout) v.findViewById(R.id.sticky);
        actionBar = (RelativeLayout) v.findViewById(R.id.actionBar);
		actionImg = ((CircularImage) v.findViewById(R.id.action_image));
		actionTxt = ((TextView) v.findViewById(R.id.actionTxt));

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
        taphereim = (ImageView) v.findViewById(R.id.imageView2);
        tapheretxt = (TextView) v.findViewById(R.id.taphere);
        mListView = (InScrollableListView) v.findViewById(android.R.id.list);
		
		
		return v;
	}
	
	
	public void onViewCreated(View v, Bundle savedInstanceState){
		super.onViewCreated(v, savedInstanceState);
		this.v = v;
		
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
		pos = getArguments().getInt("pos");
		final Relationship re = Static.rs.get(pos);
		
		getFragments();
        mAdapter = new TestFragmentAdapter(getChildFragmentManager(), fList);
        setAdapter(mAdapter);

        options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.color.vpi__background_holo_dark)
		.showImageForEmptyUri(R.color.vpi__background_holo_dark)
		.showImageOnFail(R.color.vpi__background_holo_dark)
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
		
		
        mHeader = getActivity().getLayoutInflater().inflate(R.layout.header, null);
        mPlaceHolder = mHeader.findViewById(R.id.placeholder);
		//if(!relations){

	       //}
        
		if (runPager != null) handler.post(runPager);
	        mCreated = true;
				
       
        //gifts.clear();
        gifts = new ArrayList<Item>();
        giftsAdapter = new GiftsAdapter(getActivity(),gifts, imageLoader, viewWidth, viewHeight);
        if(mListView.getHeaderViewsCount() == 0)
			mListView.addHeaderView(mHeader);
	        
        mListView.setAdapter(giftsAdapter);

		mListView.getViewTreeObserver().addOnGlobalLayoutListener(vto);

		
		
        
        taphereim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View vi) {
				if(tapheretxt.getVisibility() == View.VISIBLE){
					tapheretxt.setVisibility(View.GONE);
					mListView.setSelectionFromTop(0, 0);;
		        	mPager.setVisibility(View.INVISIBLE);
		        	mIndicator.setVisiblity(View.INVISIBLE);
					v.findViewById(R.id.holder_tap).setVisibility(View.VISIBLE);
		        	mListView.setPagingEnabled(false);
		        	fadable = false;
		        	actionBar.setAlpha(1);

				}else{
		        	mListView.setPagingEnabled(true);
					v.findViewById(R.id.holder_tap).setVisibility(View.GONE);
		        	mPager.setVisibility(View.VISIBLE);
		        	mIndicator.setVisiblity(View.VISIBLE);
					tapheretxt.setVisibility(View.VISIBLE);
		        	actionBar.setAlpha(0);
		        	fadable = true;

				}
			}
		});
        
        tapheretxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View vi) {
				if(v.getVisibility() == View.VISIBLE){
					tapheretxt.setVisibility(View.GONE);
					mListView.setSelectionFromTop(0, 0);;
		        	mPager.setVisibility(View.INVISIBLE);
		        	mIndicator.setVisiblity(View.INVISIBLE);
					v.findViewById(R.id.holder_tap).setVisibility(View.VISIBLE);
		        	mListView.setPagingEnabled(false);
		        	fadable = false;
		        	actionBar.setAlpha(1);
				}				
			}
		});
        
        /*
        mCachedVerticalScrollRange = 0;
        mListView.reset();
        mListView.getViewTreeObserver().removeOnGlobalLayoutListener(vto);
        mListView.setOnScrollListener(null);
        mListView.removeAllViewsInLayout();
        */
        
		
        
        mPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
					((Main_Activity)getActivity()).menu.setSlidingEnabled(true);
				else
					((Main_Activity)getActivity()).menu.setSlidingEnabled(false);
				return false;
			}
		});
        
        	/*					        
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Fragment gift = GiftFragment.NewInstance(ev.getRelationshipid(), ev.getId());
				switchFragment(gift);
				
			}
		});
        */
        
        v.findViewById(R.id.nextevent).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(getActivity(), Gift_Activity.class);
				 i.putExtra("rId", ev.getRelationshipid());
				 i.putExtra("eId", ev.getId());
				 startActivity(i);

				//Fragment gift = GiftFragment.NewInstance(ev.getRelationshipid(), ev.getId());
				//switchFragment(gift);
			}
		});
        
        
       
        
        

        new EVENT_UPCOMING_API().execute(re.getId());
       // new Load_Gifts().execute();
	
		

	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	

	private void switchFragment(Fragment fragment) {
		Activity ac = getActivity();
		if (ac == null)
			return;

		if (ac instanceof Main_Activity) {
			Main_Activity ra = (Main_Activity) ac;
			ra.switchContent(fragment);
		}
	}
	
    public class onPauseScroll extends PauseOnScrollListener{

		int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
		public onPauseScroll(ImageLoader imageLoader, boolean pauseOnScroll,
				boolean pauseOnFling) {
			super(imageLoader, pauseOnScroll, pauseOnFling);
		}
		
		 @Override
		    public void onScrollStateChanged(AbsListView view, int scrollState) {
		        // Store the state to avoid re-laying out in IDLE state.
		        mScrollState = scrollState;
		        super.onScrollStateChanged(view, scrollState);

		    }
		
		@SuppressLint("NewApi")
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {			
			if(mScrollState != OnScrollListener.SCROLL_STATE_IDLE)
				imageLoader.pause();
			
			    int stop = getActionBarHeight();
				mScrollY = 0;
				int translationY = 0;
				
				if (mListView.scrollYIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}

				final int rawY = mPlaceHolder.getTop()- Math.min(mCachedVerticalScrollRange- mListView.getMeasuredHeight(), mScrollY);
				//Toast.makeText(getActivity(), mCachedVerticalScrollRange +" / " +mListView.getMeasuredHeight(), Toast.LENGTH_SHORT).show();

				if(rawY<stop)
						translationY = stop;
				else {
						translationY = rawY;
				}
				
				int tran = translationY-mPager.getMeasuredHeight();
				
				/** this can be used if the build is below honeycomb **/
				
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					TranslateAnimation anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(tran);
				}
	        	
				if(fadable){
					//ActionBar Transparency
			        int headerHeight = mHeaderHeight - placeHolderHeight - stop;
			        float ratio = (float) Math.min(Math.max(mScrollY, 0), headerHeight) / headerHeight;
			        int newAlpha = (int) (ratio * 255);
			        float alpha = (float) newAlpha/ (float)255;
			        if(actionBar!=null)
			        	actionBar.setAlpha(alpha);
				}     
		   	 
		}

		private int getActionBarHeight() {
			return actionBar.getMeasuredHeight();
		}		
	}
    
	private List<Fragment> getFragments(){
		MyFragment profile_info = MyFragment.newInstance("profile_info",R.layout.profile_info, Static.rs.get(pos), mPager, actionImg, actionTxt, imageLoader);
		MyFragment profile_info_charts = MyFragment.newInstance("profile_info_charts",R.layout.profile_info_charts, null , null, null , null, null);

		
	    fList = new ArrayList<Fragment>();

	    fList.add(profile_info);
	    fList.add(profile_info_charts);
	    return fList;
	}
	
	@SuppressLint("NewApi") @Override
	public void onAttach(Activity ac){
		super.onAttach(ac);
		final View v = ac.getLayoutInflater().inflate(R.layout.list_row, null);
	
		v.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 700));//.height=200;
		Display display = ac.getWindowManager().getDefaultDisplay();

		//final RoundedImage ri =((RoundedImage) v.findViewById(R.id.imageView1));
		Point p = new Point();
		display.getSize(p);

		viewWidth = p.x-300;
		viewHeight = 660;
	}
	

	public class EVENT_UPCOMING_API extends AsyncTask<Object, Void, Boolean>{
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			relationPd = ProgressDialog.show(getActivity(), null, "Loading Relationship!!");
			relationPd.show();
			events =new Events();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.EVENT);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
				json.put("operation", "getupcoming");
				json.put("relationshipid", params[0]);

				

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
						JSONArray jsa = js.getJSONArray("events");
						for(int i= 0; i < jsa.length(); i++){
							JSONObject r = (JSONObject) jsa.get(i);
							Event event = new Event();
														
							event.setId(r.optString("id"));
							event.setDate(r.optString("date"));
							event.setPassed(r.optBoolean("passed"));
							event.setName(r.optString("name"));
							//event.setType(r.optString("type"));
							event.setRating(r.optString("rating"));
							event.setOriginaldate(r.optString("originaldate"));
							event.setRelationshipid(r.optString("relationshipid"));
							JSONObject remaining = (JSONObject) r.get("remaining");
							{
								event.setNumber(remaining.optInt("number"));
								event.setAlertlevel(ALERTLEVEL.valueOf(remaining.optString("alertlevel")));
								event.setUnit(DATEUNIT.valueOf(remaining.optString("unit")));
							}
							event.setStatus(STATUS.valueOf(r.optString("status")));

							events.add(event);
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
			super.onPostExecute(result);
			 ev = events.get(0);
			((TextView) v.findViewById(R.id.number)).setText(""+ev.getNumber());
			((TextView) v.findViewById(R.id.eventTxt)).setText("until her " +ev.getName());
			((TextView) v.findViewById(R.id.unit)).setText(ev.getUnit().toString());

	       new Load_Gifts().execute(ev.getRelationshipid(), ev.getId());
		}
	}


	class Load_Gifts extends AsyncTask<Object, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Object... params) {
			gifts.clear();
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.PAST);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
				json.put("operation", "getpassed");
				json.put("relationshipid", params[0]);
				//json.put("eventid", params[1]);
				//json.put("startdate", "1900-01-02");
				//json.put("max", "2");

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
						JSONArray events = js.getJSONArray("events");
						for(int j= 0; j < events.length(); j++){
							JSONObject event = (JSONObject) events.get(j);
							JSONArray jsa = event.getJSONArray("items");
							for(int i= 0; i < jsa.length(); i++){
								JSONObject r = (JSONObject) jsa.get(i);
								Item it = new Item(r);
								gifts.add(it);
							}
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
			if(mListView.getAdapter() instanceof WrapperListAdapter) {
				giftsAdapter = (GiftsAdapter)((WrapperListAdapter)mListView.getAdapter()).getWrappedAdapter();
    		} else if (mListView.getAdapter() instanceof GiftsAdapter) {
    			giftsAdapter = (GiftsAdapter) mListView.getAdapter();
    		}
			
			giftsAdapter.clear(gifts);
	        if(relationPd.isShowing())
	        	relationPd.dismiss();
 
		}
		   
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
}
