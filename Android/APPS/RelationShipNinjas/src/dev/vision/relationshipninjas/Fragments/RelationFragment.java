package dev.vision.relationshipninjas.Fragments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import dev.vision.relationshipninjas.Activity_History;
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
import dev.vision.relationshipninjas.Classes.Relationship.TYPE;
import dev.vision.relationshipninjas.R.color;
import dev.vision.relationshipninjas.R.id;
import dev.vision.relationshipninjas.R.layout;
import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.views.CircularImage;
import dev.vision.relationshipninjas.views.CircularImageResizable;
import dev.vision.relationshipninjas.views.InScrollableListView;
import dev.vision.relationshipninjas.views.RoundedImage;


public class RelationFragment extends Fragment implements OnRefreshListener{

	ViewTreeObserver.OnGlobalLayoutListener vto = new ViewTreeObserver.OnGlobalLayoutListener() {
		@SuppressLint("NewApi") @Override
		public void onGlobalLayout() {
			mHeaderHeight = mHeader.getMeasuredHeight();
			//mPlaceHolder.getLayoutParams().height = mQuickReturnHeight;
			
			mListView.computeScrollY();
			
			mCachedVerticalScrollRange = mListView.getListHeight();

			placeHolderHeight = mPlaceHolder.getMeasuredHeight();
			mListView.setOnScrollListener(new onPauseScroll(imageLoader , true, true));  
			
			if(Build.VERSION.SDK_INT >15)
				mListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			else
				mListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

		}
	};
	
	Event ev ;
	Relationship relation;

	private ProgressDialog relationPd;
	

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
	private TextView tapheretxt;
	protected boolean fadable = true;
	private int mScrollY;
	private int placeHolderHeight;

	protected boolean relations;
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

	private Events_Adapter eventsAdapter;

	private int pos;

	private SwipeRefreshLayout swipeLayout;

	static String TAG = "rel";
	

	public static RelationFragment NewInstance(int pos) {
		RelationFragment d =new RelationFragment();
		Bundle bun = new Bundle();
		bun.putInt("pos", pos);
		d.setArguments(bun);
		
		
		return d;
	}

	public static RelationFragment NewInstance() {
		RelationFragment d =new RelationFragment();
	
		return d;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v= inflater.inflate(R.layout.new_main_p, null);

		mQuickReturnView = (RelativeLayout) v.findViewById(R.id.sticky);
        
		actionBar = (RelativeLayout) v.findViewById(R.id.actionBar);
		actionImg = ((CircularImage) v.findViewById(R.id.action_image));
		actionTxt = ((TextView) v.findViewById(R.id.actionTxt));

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
        tapheretxt = (TextView) v.findViewById(R.id.taphere);
        mListView = (InScrollableListView) v.findViewById(android.R.id.list);
		
        v.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((Main_Activity) getActivity()).toggle();
			}
		});
        
        v.findViewById(R.id.history).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent().setClass(getActivity(), Activity_History.class);
				i.putExtra("rId", relation.getId());
				startActivity(i);
			}
		});
        

		
        tapheretxt.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi") @Override
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
        
       					        
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if(pos>1){
					Event ev = (Event) mListView.getAdapter().getItem(pos);
					Intent i = new Intent(getActivity(), Gift_Activity.class);
					i.putExtra("rId", ev.getRelationshipid());
					i.putExtra("eId", ev.getId());
					startActivity(i);
				}else if (pos == 1){
					Event ev = relation.getJustBecause();
					Intent i = new Intent(getActivity(), Gift_Activity.class);
					i.putExtra("rId", ev.getRelationshipid());
					i.putExtra("eId", ev.getId());
					startActivity(i);
				}
				
			}
		});
        
        
        v.findViewById(R.id.nextevent).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(getActivity(), Gift_Activity.class);
				 i.putExtra("rId", ev.getRelationshipid());
				 i.putExtra("eId", ev.getId());
				 startActivity(i);

				
			}
		});

        
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
		
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
 

	

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState); 
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(android.R.color.transparent)
		.showImageForEmptyUri(android.R.color.transparent)
		.showImageOnFail(android.R.color.transparent)
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
		
		Bundle b = getArguments();
		if(b!=null){
			pos = b.getInt("pos");
			relation = Static.rs.get(pos);
		    //ONLY CHANGE HERE
			//show();

	        new EVENT_RELATIONSHIP_API().execute(relation.getId()); //Uncomment and remove next line to Always refresh
		}else{
	        new DEFAULT_RELATIONSHIP_API().execute();

		}
		

	}
	
	
	void show(){
		
		getFragments();
        mAdapter = new TestFragmentAdapter(getChildFragmentManager(), fList);
        setAdapter(mAdapter);
       
        mHeader = getActivity().getLayoutInflater().inflate(R.layout.header, null);
        mPlaceHolder = mHeader.findViewById(R.id.placeholder);
		
        
		if (runPager != null) handler.post(runPager);
	        mCreated = true;
		
		if(!relation.getEvents().isEmpty()){
			ev = relation.getEvents().get(0);
			((TextView) v.findViewById(R.id.number)).setText(""+ev.getNumber());
			((TextView) v.findViewById(R.id.eventTxt)).setText("until her " +ev.getName());
			((TextView) v.findViewById(R.id.unit)).setText(ev.getUnit().toString());
		}
		
		if(mListView.getHeaderViewsCount() == 0){
			mListView.addHeaderView(mHeader);
		
			eventsAdapter = new Events_Adapter(getActivity(),relation, relation.getEvents(), imageLoader);

			mListView.setAdapter(eventsAdapter);
		}
		eventsAdapter.setRelation(relation);
		eventsAdapter.notifyDataSetChanged();
		mListView.getViewTreeObserver().addOnGlobalLayoutListener(vto);				

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
		MyFragment profile_info = MyFragment.newInstance("profile_info",R.layout.profile_info, relation, mPager, actionImg, actionTxt, imageLoader);
		ChartFragment profile_info_charts = ChartFragment.newInstance("profile_info_charts",R.layout.new_profile_info_charts, null , null, null , null, null);

		
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
	

	
	public class DEFAULT_RELATIONSHIP_API extends AsyncTask<Object, Void, Boolean>{
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			relationPd = ProgressDialog.show(getActivity(), null, "Loading Relationship!!");
			relationPd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			boolean success = false;
			try{
				// add request header
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			    nameValuePairs.add(new BasicNameValuePair("operation", "getdefault")); 
			    nameValuePairs.add(new BasicNameValuePair("auth_email", API.user.email)); 
			    nameValuePairs.add(new BasicNameValuePair("auth_password", API.user.pass)); 

			    String extraParam = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs));
				
			    HttpGet request = new HttpGet(API.DASHBOARD +"&"+ extraParam);
				request.setHeader("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
				
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
						JSONObject data = js.getJSONObject("data");
						if(data!=null){
							JSONObject r = data.getJSONObject("relationship");
							
							relation= new Relationship();
							relation.setId(r.optString("id"));
							relation.setName(r.optString("name"));
							relation.setType(TYPE.Match(r.optString("type")));
							relation.setRating(r.optString("rating"));
							relation.setFirstname(r.optString("firstname"));
							relation.setLastname(r.optString("lastname"));
							relation.setMiddlename(r.optString("middlename"));
							relation.setEmail(r.optString("email"));
							relation.setKids(r.optString("kids"));
							relation.setMyersbriggs(r.optString("myersbriggs"));
							relation.setGender(r.optString("gender"));
							relation.setFacebookid(r.optString("facebookid"));
							relation.setFinalizedstatus(r.optBoolean("finalizedstatus"));
							relation.setCover(r.optString("cover"));
							relation.setDefaultcover(r.optBoolean("isdefaultcover"));
							relation.setImage(r.optString("image"));
							relation.setDefaultimage(r.optBoolean("isdefaultimage"));
							relation.setProgress(r.optInt("progress"));
							
							
							JSONArray jsa = data.getJSONArray("upcomingevents");
							for(int i= 0; i < jsa.length(); i++){
								JSONObject e = (JSONObject) jsa.get(i);
								Event event = new Event();
															
								event.setId(e.optString("id"));
								event.setDate(e.optString("date"));
								event.setPassed(e.optBoolean("passed"));
								event.setName(e.optString("name"));
								event.setType(dev.vision.relationshipninjas.Classes.Event.TYPE.valueOf(e.optString("type")));
								event.setRating(e.optString("rating"));
								event.setOriginaldate(e.optString("originaldate"));
								event.setRelationshipid(e.optString("relationshipid"));
								
								JSONObject remaining = (JSONObject) e.opt("remaining");
								if(remaining != null)
								{
									event.setNumber(remaining.optInt("number"));
									event.setAlertlevel(ALERTLEVEL.valueOf(remaining.optString("alertlevel")));
									event.setUnit(DATEUNIT.valueOf(remaining.optString("unit")));
								}
								event.setStatus(STATUS.valueOf(e.optString("status")));

								relation.addEvent(event);
							}
							
							JSONObject ev = data.getJSONObject("justbecauseevent");

							Event justbe = new Event();
							
							justbe.setId(ev.optString("id"));
							justbe.setName(ev.optString("name"));
							justbe.setType(dev.vision.relationshipninjas.Classes.Event.TYPE.other);
							justbe.setRating(ev.optString("rating"));
							justbe.setRelationshipid(ev.optString("relationshipid"));
							JSONObject remain = (JSONObject) ev.opt("remaining");
							if(remain != null)
							{
								justbe.setNumber(-1);
								justbe.setAlertlevel(ALERTLEVEL.now);
								justbe.setUnit(DATEUNIT.valueOf(remain.optString("unit")));
							}
							justbe.setStatus(STATUS.valueOf(ev.optString("status")));

							relation.setJustBecause(justbe);
							/*
							JSONObject shippingaddresses = data.getJSONObject("shippingaddresses");

							JSONArray shipping_address = shippingaddresses.getJSONArray("relationship");
							for(int y= 0; y < shipping_address.length(); y++){
								JSONObject e = (JSONObject) shipping_address.get(y);
								Address ad = new Address();
								
								ad.setId(e.optString("id"));
								ad.setName(e.optString("name"));
								ad.setAddressee(e.optString("addressee"));
								ad.setAddress1(e.optString("address1"));
								ad.setAddress2(e.optString("address2"));
								ad.setCity(e.optString("city"));
								ad.setCountry(e.optString("country"));
								ad.setDefaultshipping(e.optBoolean("defaultshipping"));
								ad.setPhone(e.optString("phone"));
								ad.setState(e.optString("state"));
								ad.setZip(e.optString("zip"));
								
								relation.addShippingAddress(ad);
							}
							*/
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
			if(result){
				show();
			}
			 if(relationPd.isShowing())
		       relationPd.dismiss();

			
		}
	}

	
	public class EVENT_RELATIONSHIP_API extends AsyncTask<Object, Void, Boolean>{
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			relationPd = ProgressDialog.show(getActivity(), null, "Loading Relationship!!");
			relationPd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			boolean success = false;
			try{
				// add request header
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			    nameValuePairs.add(new BasicNameValuePair("operation", "get")); 
			    nameValuePairs.add(new BasicNameValuePair("auth_email", API.user.email)); 
			    nameValuePairs.add(new BasicNameValuePair("auth_password", API.user.pass)); 
			    nameValuePairs.add(new BasicNameValuePair("id", (String) params[0]));


			    String extraParam = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs));
				
			    HttpGet request = new HttpGet(API.DASHBOARD +"&"+ extraParam);
				request.setHeader("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
				
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
						JSONObject data = js.getJSONObject("data");
						if(data!=null){
							JSONObject r = data.getJSONObject("relationship");
							
							relation= new Relationship();
							relation.setId(r.optString("id"));
							relation.setName(r.optString("name"));
							relation.setType(TYPE.Match(r.optString("type")));
							relation.setRating(r.optString("rating"));
							relation.setFirstname(r.optString("firstname"));
							relation.setLastname(r.optString("lastname"));
							relation.setMiddlename(r.optString("middlename"));
							relation.setEmail(r.optString("email"));
							relation.setKids(r.optString("kids"));
							relation.setMyersbriggs(r.optString("myersbriggs"));
							relation.setGender(r.optString("gender"));
							relation.setFacebookid(r.optString("facebookid"));
							relation.setFinalizedstatus(r.optBoolean("finalizedstatus"));
							relation.setCover(r.optString("cover"));
							relation.setDefaultcover(r.optBoolean("isdefaultcover"));
							relation.setImage(r.optString("image"));
							relation.setDefaultimage(r.optBoolean("isdefaultimage"));
							relation.setProgress(r.optInt("progress"));
							
							
							JSONArray jsa = data.getJSONArray("upcomingevents");
							for(int i= 0; i < jsa.length(); i++){
								JSONObject e = (JSONObject) jsa.get(i);
								Event event = new Event();
															
								event.setId(e.optString("id"));
								event.setDate(e.optString("date"));
								event.setPassed(e.optBoolean("passed"));
								event.setName(e.optString("name"));
								event.setType(dev.vision.relationshipninjas.Classes.Event.TYPE.valueOf(e.optString("type")));
								event.setRating(e.optString("rating"));
								event.setOriginaldate(e.optString("originaldate"));
								event.setRelationshipid(e.optString("relationshipid"));
								JSONObject remaining = (JSONObject) e.opt("remaining");
								if(remaining != null)
								{
									event.setNumber(remaining.optInt("number"));
									event.setAlertlevel(ALERTLEVEL.valueOf(remaining.optString("alertlevel")));
									event.setUnit(DATEUNIT.valueOf(remaining.optString("unit")));
								}
								event.setStatus(STATUS.valueOf(e.optString("status")));

								relation.addEvent(event);
							}
							
							JSONObject ev = data.getJSONObject("justbecauseevent");

							Event justbe = new Event();
							
							justbe.setId(ev.optString("id"));
							justbe.setName(ev.optString("name"));
							justbe.setType(dev.vision.relationshipninjas.Classes.Event.TYPE.other);
							justbe.setRating(ev.optString("rating"));
							justbe.setRelationshipid(ev.optString("relationshipid"));
							JSONObject remain = (JSONObject) ev.opt("remaining");
							if(remain != null)
							{
								justbe.setNumber(-1);
								justbe.setAlertlevel(ALERTLEVEL.now);
								justbe.setUnit(DATEUNIT.valueOf(remain.optString("unit")));
							}
							justbe.setStatus(STATUS.valueOf(ev.optString("status")));

							relation.setJustBecause(justbe);
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
			if(result){
				show();
			}
			 if(relationPd.isShowing())
		       relationPd.dismiss();

			
		}
	}
	
	/*
	public class EVENT_UPCOMING_API extends AsyncTask<Object, Void, Boolean>{
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			relationPd = ProgressDialog.show(getActivity(), null, "Loading Relationship!!");
			relationPd.show();
			relation.clearEvents();
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
							event.setType(dev.vision.relationshipninjas.Classes.Event.TYPE.valueOf(r.optString("type")));
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

							relation.addEvent(event);
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
			if(!relation.getEvents().isEmpty()){
				ev = relation.getEvents().get(0);
				((TextView) v.findViewById(R.id.number)).setText(""+ev.getNumber());
				((TextView) v.findViewById(R.id.eventTxt)).setText("until her " +ev.getName());
				((TextView) v.findViewById(R.id.unit)).setText(ev.getUnit().toString());
				show();
			}
			 if(relationPd.isShowing())
		       relationPd.dismiss();
	       //new Load_Gifts().execute(ev.getRelationshipid(), ev.getId());
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
	*/
	@Override
	public void onResume(){
		super.onResume();
		
	}

	@Override
	public void onRefresh() {
		
	}
	
}
