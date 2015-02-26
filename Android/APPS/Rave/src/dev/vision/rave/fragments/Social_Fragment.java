package dev.vision.rave.fragments;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.graphics.PorterDuff;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import com.devspark.progressfragment.ProgressListFragment;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.WrapperListAdapter;

import com.echo.holographlibrary.PieGraph;
import com.f2prateek.progressbutton.ProgressButton;
import com.github.siyamed.shapeimageview.HexagonImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import de.passsy.holocircularprogressbar.HoloCircularProgressBar;
import dev.vision.rave.API;
import dev.vision.rave.Constants;
import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import dev.vision.rave.TestFragmentAdapter;
import dev.vision.rave.camera.CameraPreviewSampleActivity;
import dev.vision.rave.camera.Main_CameraPreview;
import dev.vision.rave.listeners.BogusRemoteService;
import dev.vision.rave.listeners.DemoListAdapter;
import dev.vision.rave.listeners.ScrollableListView;
import dev.vision.rave.listeners.DemoListAdapter.NewPageListener;
import dev.vision.rave.listeners.ScrollableListView.LoadingMode;
import dev.vision.rave.listeners.ScrollableListView.StopPosition;
import dev.vision.rave.progresswheel.HeartChart;
import dev.vision.rave.user.Likes;
import dev.vision.rave.user.Post;
import dev.vision.rave.user.User;
import dev.vision.rave.views.BlurredImageView;
import dev.vision.rave.views.CPageIndicator;
import dev.vision.rave.views.CircularImageResizable;
import dev.vision.rave.views.CustomViewPager;
import dev.vision.rave.views.QuickShareTouch;
import dev.vision.rave.views.RectangleView;

public class Social_Fragment extends ProgressListFragment implements OnItemClickListener, OnClickListener {
	
	
	int viewH=0;
	protected float progress;
	protected boolean running;
	private long burned;
	 View backgroundimage;
	 Drawable background;

	HashMap<String, Post> PostsMap = new HashMap<String, Post>();
	List<Post> Posts = Collections.synchronizedList(new ArrayList<Post>());
	ArrayList<Post> DownloadedPosts = new ArrayList<Post>();

	List<Fragment> fList;

    OnPageChangeListener pl;
 
    protected TestFragmentAdapter mAdapter;
    protected ViewPager mPager;
    protected CPageIndicator mIndicator;
    
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ScrollableListView mListView;
	ImageView coverPic;
	int translationY = 0;
	CustomViewPager vp;
	private FrameLayout cameraContainer;
	private ImageView cameraHide;
	private ImageView cameraShow;
	
	private Main_CameraPreview mPreview;
	private int CURRENT_CAM;
	private ImageView cameraSwitch;
	boolean colorOverlay;
	ImageView fina_pic;
	

	private int size = 0;
	private int max= 10;
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	public static final String LAYOUT_ID = "Layout_ID";
	View v;
	LayoutInflater inflater;
	public Uri uri;
	String[] imageUrls;
	DisplayImageOptions options;
	Bundle savedInstanceState;
	User user;
	private View mHeader;
	private RelativeLayout mQuickReturnView;
	private View mPlaceHolder;
	
	private int mCachedVerticalScrollRange;
	private int mQuickReturnHeight;
	
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	
	private TranslateAnimation anim;
	private ImageLoadingListener profileListener = new ProfileDisplayListener();
	private ImageLoadingListener coverListener = new CoverDisplayListener();
	

	 
    String[] imagesToShow = Constants.IMAGES;
	ArrayList<Drawable> darray= new ArrayList<Drawable>();
	int i=0;
	private int falsee =0;

	//View HolderBack;
	Handler h;
	public boolean done;
	
	

	private Handler handler;
	private AsyncTask<Void, Void, List<Post>> fetchAsyncTask;

	private LoadingMode loadingMode = LoadingMode.SCROLL_TO_BOTTOM;
	private StopPosition stopPosition = StopPosition.REMAIN_UNCHANGED;
	private DemoListAdapter demoListAdapter;
	private BogusRemoteService bogusRemoteService;
	private ImageView scrollToTop;
	private ImageView camerafScreen;
	private static final int SEVER_SIDE_BATCH_SIZE = 10;
	private float trans;


	
	public class OnPauseScrollListener extends PauseOnScrollListener  {
			private int mScrollState;
			private boolean visible = true;
			private boolean StickyHeader;

			public OnPauseScrollListener(ImageLoader imageLoader,
					boolean pauseOnScroll, boolean pauseOnFling) {
				super(imageLoader, pauseOnScroll, pauseOnFling);
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(mScrollState != OnScrollListener.SCROLL_STATE_IDLE)
				imageLoader.pause();
				demoListAdapter.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

				int stop=getActionBarHeight();
						
				if (mListView.scrollYIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}
				
				

				translationY = -mScrollY+mHeader.getHeight()-stop;

				if(translationY<0)
					translationY = 0;
				
				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
					if(firstVisibleItem > 5 && !visible){
						translate(-trans);
						visible= true;
					}else if(firstVisibleItem < 5 && visible){
						translate(0);
						visible=false;
					}
				
				}
				 
				// Nothing to do in IDLE state.
		        if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE && StickyHeader){
		         //   return;

			        for (int i=0; i < visibleItemCount; i++) {
			            View listItem = view.getChildAt(i);
			            if (listItem == null)
			                break;
			            
			            RelativeLayout title = (RelativeLayout) listItem.findViewById(R.id.above);
			            if(title!=null)
			            {	
			            			
				            float topMargin = 0;
			                int top = listItem.getTop();
			                int height = listItem.getHeight();
			 
			              	if (top < stop) //viewH+stop		//down here -viewH ?  -viewH : nothing
				                    topMargin = title.getHeight() < (top + height-stop) ? -(top-stop) : (height - title.getHeight());
			                //}
			 
				       
				            // request Android to layout again.
				            title.setTranslationY(topMargin);
			            } 
			        }
		        }
			}

			private void translate(float trans) {
				scrollToTop.setTranslationY(trans);				
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
		       super.onScrollStateChanged(view, scrollState);
				mScrollState = scrollState;

				if(mScrollState != OnScrollListener.SCROLL_STATE_IDLE)
				imageLoader.pause();
			}
		
		
	}


    
	public static final Social_Fragment newInstance(String message, int layid, User user2)
	{
		
	   Social_Fragment f = new Social_Fragment();

	   Bundle bdl = new Bundle();
	   f.user = user2;
	   
	   bdl.putString(EXTRA_MESSAGE, message);

	   bdl.putInt(LAYOUT_ID, layid);
	   
	   f.setArguments(bdl);
	   return f;

	 }


	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		 
		 bogusRemoteService = new BogusRemoteService();
		 View layout = super.onCreateView(inflater, container,
		            savedInstanceState);
	     ListView lv = (ListView) layout.findViewById(android.R.id.list);
	     ViewGroup parent = (ViewGroup) lv.getParent();

	     // Remove ListView and add CustomView  in its place
	     int lvIndex = parent.indexOfChild(lv);
	     parent.removeViewAt(lvIndex);
		    
		 
		 
	     colorOverlay=true;
		 
		 this.savedInstanceState= savedInstanceState;
		 this.inflater=inflater;

		 
	     int layoutId = getArguments().getInt(LAYOUT_ID);
		 View v = inflater.inflate(layoutId, container,false);
		
		 parent.addView(v,lvIndex);
		 scrollToTop = (ImageView) v.findViewById(R.id.scrolltotop);
		 
		 mQuickReturnView = (RelativeLayout) v.findViewById(R.id.profile_sticky);
		 cameraShow = (ImageView) v.findViewById(R.id.camera_show);
		 //guideMe = (ImageView) v.findViewById(R.id.profile_sticky);
		 //mQuickReturnView = (RelativeLayout) v.findViewById(R.id.profile_sticky);

		 //Header inflate and initialize
		 mHeader = inflater.inflate(R.layout.headers, null);
	     mPager = (ViewPager) mHeader.findViewById(R.id.profile_pager);
		 coverPic =(BlurredImageView) mHeader.findViewById(R.id.coverImage);
		 mPlaceHolder = mHeader.findViewById(R.id.headerContainer);
	     mIndicator = (CPageIndicator) mHeader.findViewById(R.id.indicator);
	     fina_pic = (ImageView) mHeader.findViewById(R.id.fina_pic);
	     //camera-view
	     cameraContainer = (FrameLayout) mHeader.findViewById(R.id.camera_main_container);
	    // cameraSurface = (SurfaceView) mHeader.findViewById(R.id.camera_main_surface);
	     cameraHide = (ImageView) mHeader.findViewById(R.id.camera_main_hide);
	     cameraSwitch = (ImageView) mHeader.findViewById(R.id.camera_main_switch);
	     camerafScreen = (ImageView) mHeader.findViewById(R.id.camera_main_fullscreen);
	     //mHeader.setLayerType(View.LAYER_TYPE_HARDWARE, null);

	     
	 	 scrollToTop.setOnClickListener(this);
	     cameraShow.setOnClickListener(this);
	     cameraHide.setOnClickListener(this);
	     cameraSwitch.setOnClickListener(this);
	     camerafScreen.setOnClickListener(this);
		 return layout;
	 }
	 

		
		
	  OnGlobalLayoutListener vto =new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mQuickReturnHeight = mQuickReturnView.getMeasuredHeight();
				mPlaceHolder.getLayoutParams().height = mQuickReturnHeight;
				mListView.computeScrollY();
				mCachedVerticalScrollRange = mListView.getListHeight();
				viewH = mQuickReturnHeight-getActionBarHeight();

			}
		};
	
		
	 public void ZoomOut(){
		 ScaleAnimation anim = new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
 	     anim.setDuration(500);
 	     anim.setFillAfter(true);
 	     getActivity().getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(anim);
 	     //zoomedOut = true;   
	 }
	 
	 protected void Init() {
		 	trans = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());

	 		mListView.getViewTreeObserver().addOnGlobalLayoutListener(vto);		
	 		mListView.setOnScrollListener(new OnPauseScrollListener(imageLoader, true, true));
	 		
	 		/*
			//ActionBar
			backgroundimage = (v.findViewById(R.id.actionbar));
	        
			if(backgroundimage!=null)
	        background = backgroundimage.getBackground();

	        if(background!=null)
	        background.setAlpha(100);
	        */

	 }

	@Override
	 public void onViewCreated(View view, Bundle savedInstanceState) {
		 super.onViewCreated(view, savedInstanceState);
		 v=view;
	 }
	

	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
        super.onActivityCreated(savedInstanceState);
		this.savedInstanceState=savedInstanceState;

	     mAdapter = new TestFragmentAdapter(getActivity().getSupportFragmentManager(), getFragments());
         if(mPager.getAdapter()==null)
         mPager.setAdapter(mAdapter);
         mIndicator.setViewPager(mPager);
         vp=(CustomViewPager) getActivity().findViewById(R.id.main);
         options = new DisplayImageOptions.Builder()
		.showImageOnLoading(new ColorDrawable(DataHolder.APP_BACKGROUND))
		.showImageForEmptyUri(null)
		.showImageOnFail(null)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.delayBeforeLoading(500)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
		.threadPoolSize(2)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.defaultDisplayImageOptions(options)
		.build();
		imageLoader.init(config);

		setListShown(false);


		final int get=6;
		for(i = 0; i<get; i++){
			 //= loadBitmap(getResources().getDrawable(imagesToShow[i]));
			imageLoader.loadImage(imagesToShow[i], new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					
				}
				
				
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					falsee++;
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					Bitmap bit = loadedImage;
					if(bit != null){
						if(!bit.isMutable())
						bit= bit.copy(Bitmap.Config.RGB_565, true);
						bit =Constants.blurfast(bit, 8);
						Drawable d = new BitmapDrawable(getResources(),bit);
						Random rnd = new Random();
						//if(colorOverlay){
						//	int color = Color.argb(120, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
						//	d.setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
						//}
						darray.add(d);
						if(darray.size()==2)
							CoverLoad(darray);

							//i=0;
						//}
					}else{
						falsee ++;
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					falsee++;
				}
		});
			//if(user.getCover_PicURL() == null)
			//	bit = user.getCover_Pic(getActivity());
			//else
			//	bit = imageLoader.loadImageSync(user.getCover_PicURL(), options);
			
		}
		
        mListView = (ScrollableListView) getListView();
       
        mListView.setCacheColorHint(Color.parseColor("#000000"));
		mListView.setVerticalFadingEdgeEnabled(true);
		mListView.setFadingEdgeLength(5);

        
        
		
		mListView.addHeaderView(mHeader);
		new LoadData().execute();
		
	
		

	}
    
	private Bitmap loadBitmap(Drawable d)
	{
		if(d!=null){
			
				 Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(), Config.RGB_565);
			     Canvas canvas = new Canvas(bitmap); 
			     d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			     d.draw(canvas);
			     return Constants.blurfast(bitmap, 8);
			
		}
		return null;
	}
	   
    void CoverLoad(){
		final TransitionDrawable td = new TransitionDrawable(darray.toArray(new Drawable[0]));
		td.setCrossFadeEnabled(true);
		final Random rnd = new Random(); 
		//falsee=0;
		coverPic.setImageDrawable(td);
		new Handler().postDelayed(new Runnable() {
			
			private boolean reversed;

			@Override
			public void run() {
				int color = Color.argb(120, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
				if(!reversed){
					td.getDrawable(1).clearColorFilter();
					td.getDrawable(1).setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
					td.startTransition(7000);
					reversed=true;
				}else{
					td.getDrawable(0).clearColorFilter();
					td.getDrawable(0).setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
					td.reverseTransition(7000);
					reversed=false;
				}
				new Handler().postDelayed(this, 7000*2);
			}
		}, 7000*2);

	}
    int end=1;
    /*
    void CoverLoad(ArrayList<Drawable> darray){
		final TransitionDrawable td = new TransitionDrawable(darray.toArray(new Drawable[0]));
		td.setCrossFadeEnabled(true);
		final Random rnd = new Random(); 
		//falsee=0;
		coverPic.setImageDrawable(td);
		int color = Color.argb(120, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
		td.getDrawable(1).clearColorFilter();
		td.getDrawable(1).setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
		td.startTransition(7000);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ArrayList<Drawable> da;
				if(Social_Fragment.this.darray.size() > end+1){
					da = new ArrayList<Drawable>();
					da.add(td.getDrawable(1));
					da.add(Social_Fragment.this.darray.get(end++));
				}else{
					da = new ArrayList<Drawable>();
					da.add(td.getDrawable(1));
					da.add(Social_Fragment.this.darray.get(0));
					end=0;
				}
				CoverLoad(da);

			}
		}, 7000*2);

	}
    */
    
    void CoverLoad(ArrayList<Drawable> darray){
		final TransitionDrawable td = new TransitionDrawable(darray.toArray(new Drawable[0]));
		td.setCrossFadeEnabled(true);
		//falsee=0;
		coverPic.setImageDrawable(td);
		td.startTransition(7000);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ArrayList<Drawable> da;
				if(Social_Fragment.this.darray.size() > end+1){
					da = new ArrayList<Drawable>();
					da.add(td.getDrawable(1));
					da.add(Social_Fragment.this.darray.get(end++));
				}else{
					da = new ArrayList<Drawable>();
					da.add(td.getDrawable(1));
					da.add(Social_Fragment.this.darray.get(0));
					end=0;
				}
				CoverLoad(da);

			}
		}, 7000*2);

	}
    
	private int getActionBarHeight() {
		return mQuickReturnHeight;//backgroundimage.getHeight();
	}

    private List<Fragment> getFragments(){

		Profile_Fragment	profile_info = Profile_Fragment.newInstance("profile_info",R.layout.profile_info,user,imageLoader);
		Profile_Fragment	profile_info_charts = Profile_Fragment.newInstance("profile_info_charts",R.layout.profile_info_charts,user,imageLoader);

		
	    fList = new ArrayList<Fragment>();

	    fList.add(profile_info);
	    fList.add(profile_info_charts);

	    return fList;

	}



	private class CoverDisplayListener extends SimpleImageLoadingListener {

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				user.setCover_Pic(loadedImage);
			}
		}
	}

	private class ProfileDisplayListener extends SimpleImageLoadingListener {


		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				user.setProfile_Pic(loadedImage);
			}
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	
	public void setUser(User u){
		user = u;
	}
	
	 private class LoadData extends AsyncTask<String, Void, List<Post>> {
	        @Override
	        protected List<Post> doInBackground(String... params) {
	        

                String jsonString = "";
	        	try { 
	        		HttpGet httpGet = new HttpGet(API.GET_USER+user.getId());
	                // Execute the request and fetch Http response
	        	    DefaultHttpClient httpclient = new DefaultHttpClient();
				
	        	    HttpResponse httpResponse = httpclient.execute(httpGet);
	                // Extract the result from the response
	                HttpEntity httpEntity = httpResponse.getEntity();
	                // Open the result as an input stream for parsing
	                InputStream httpResponseStream;
					
					httpResponseStream = httpEntity.getContent();
				
              
                    // Create buffered reader for the httpResponceStream
                    BufferedReader httpResponseReader = new BufferedReader(
                            new InputStreamReader(httpResponseStream, "iso-8859-1"), 8);
                    // String to hold current line from httpResponseReader
                    String line = null;
                    // Clear jsonString
                    
                    // While there is still more response to read
                    while ((line = httpResponseReader.readLine()) != null) {
                        // Add line to jsonString
                        jsonString += (line + "\n");
                    }
                    // Close Response Stream
                    httpResponseStream.close();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                try {
					JSONArray jz = new JSONArray(jsonString);
					int len = jz.length();
					imageUrls = new String[len];
					
					   for (int i=0;i<len;i++){ 
				           String ss = String.format(API.CONTENTS, user.getId(),jz.get(i).toString());

						   imageUrls[i]= ss;
					   } 
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        	
                imageUrls = imagesToShow;
		   		for(int i = 0; i<imageUrls.length; i++){
	   				Post p = new Post();
	   				p.setType("video");
	   				p.setType("image");
	   				p.setImageUrl(imageUrls[i]);
	   				p.setID("1121");
	   				p.setDate("20/10/2013");
	   				p.like(user);
	   				
	   				Posts.add(p);
	   				PostsMap.put(p.getID(), p);
		   		}
		   		

	            return Posts;
	        }

	        @Override
	        protected void onPostExecute(List<Post> result) {
	        	//if(mListView.getAdapter() == null){
	        		//mListView.addFooterView(mHeader);
	        		bogusRemoteService.setEntries(result);
	        		mListView.setLoadingMode(loadingMode);
	        		mListView.setLoadingView(inflater.inflate(R.layout.loading_view, null));
	           		demoListAdapter = new DemoListAdapter(npl);
	        		mListView.setAdapter(demoListAdapter);
	        		
	        		demoListAdapter.onScrollNext();
	        		Init();

	            	setListShownNoAnimation(true);
	        	
	        }

	        @Override
	        protected void onPreExecute() {}

	    }
	 

	 
	


    private void animate(final BlurredImageView imageView, final Bitmap images[], final int imageIndex, final boolean forever) {

	      //imageView <-- The View which displays the images
	      //images[] <-- Holds R references to the images to display
	      //imageIndex <-- index of the first image to show in images[] 
	      //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

	        int fadeInDuration = 500; // Configure time values here
	        int timeBetween = 3000;
	        int fadeOutDuration = 1000;

	 //       imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
	        imageView.setImageBitmap(images[imageIndex]);

	        Animation fadeIn = new AlphaAnimation(0, 1);
	        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
	        fadeIn.setDuration(fadeInDuration);

	        Animation fadeOut = new AlphaAnimation(1, 0);
	        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
	        fadeOut.setStartOffset(fadeInDuration + timeBetween);
	        fadeOut.setDuration(fadeOutDuration);

	        AnimationSet animation = new AnimationSet(false); // change to false
	        animation.addAnimation(fadeIn);
	        animation.addAnimation(fadeOut);
	        animation.setRepeatCount(1);
	        imageView.setAnimation(animation);

	        animation.setAnimationListener(new AnimationListener() {
	            @Override
				public void onAnimationEnd(Animation animation) {
	                if (images.length - 1 > imageIndex) {
	                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
	                }
	                else {
	                    if (forever == true){
	                    animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
	                    }
	                }
	            }
	            @Override
				public void onAnimationRepeat(Animation animation) {
	            }
	            @Override
				public void onAnimationStart(Animation animation) {
	            }
	        });
	    }
	    
	    private void animate(final BlurredImageView imageView, final int images[], final int imageIndex, final boolean forever) {

		      //imageView <-- The View which displays the images
		      //images[] <-- Holds R references to the images to display
		      //imageIndex <-- index of the first image to show in images[] 
		      //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

		        int fadeInDuration = 500; // Configure time values here
		        int timeBetween = 3000;
		        int fadeOutDuration = 1000;

		        //imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
		        imageView.setImageResource(images[imageIndex]);

		        Animation fadeIn = new AlphaAnimation(0, 1);
		        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		        fadeIn.setDuration(fadeInDuration);

		        Animation fadeOut = new AlphaAnimation(1, 0);
		        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		        fadeOut.setStartOffset(fadeInDuration + timeBetween);
		        fadeOut.setDuration(fadeOutDuration);

		        AnimationSet animation = new AnimationSet(false); // change to false
		        animation.addAnimation(fadeIn);
		        animation.addAnimation(fadeOut);
		        animation.setRepeatCount(1);
		        imageView.setAnimation(animation);

		        animation.setAnimationListener(new AnimationListener() {
		            @Override
					public void onAnimationEnd(Animation animation) {
		                if (images.length - 1 > imageIndex) {
		                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
		                }
		                else {
		                    if (forever == true){
		                    animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
		                    }
		                }
		            }
		            @Override
					public void onAnimationRepeat(Animation animation) {
		            }
		            @Override
					public void onAnimationStart(Animation animation) {
		            }
		        });
		    }
	    
	    private void animatee(final BlurredImageView imageView, final int images[], final int imageIndex, final boolean forever) {

		      //imageView <-- The View which displays the images
		      //images[] <-- Holds R references to the images to display
		      //imageIndex <-- index of the first image to show in images[] 
		      //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

		        int fadeInDuration = 1000; // Configure time values here
		        int timeBetween = 1500;
		        int fadeOutDuration = 500;

		        //imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
		        imageView.setImageResource(images[imageIndex]);

		        Animation fadeIn = new AlphaAnimation(0, 1);
		        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		        fadeIn.setDuration(fadeInDuration);

		        Animation fadeOut = new AlphaAnimation(1, 0);
		        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		        fadeOut.setStartOffset(fadeInDuration + timeBetween);
		        fadeOut.setDuration(fadeOutDuration);

		        AnimationSet animation = new AnimationSet(false); // change to false
		        animation.addAnimation(fadeIn);
		        animation.addAnimation(fadeOut);
		        animation.setRepeatCount(1);
		        imageView.setAnimation(animation);

		        animation.setAnimationListener(new AnimationListener() {
		            @Override
					public void onAnimationEnd(Animation animation) {
		                if (images.length - 1 > imageIndex) {
		                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
		                }
		                else {
		                    if (forever == true){
		                    animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
		                    }
		                }
		            }
		            @Override
					public void onAnimationRepeat(Animation animation) {
		            }
		            @Override
					public void onAnimationStart(Animation animation) {
		            }
		        });
		    }


		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.camera_main_fullscreen:
		 		startActivity(new Intent(getActivity(),CameraPreviewSampleActivity.class));				
				break;
			case R.id.scrolltotop:
		 		mListView.smoothScrollToPosition(0);					
				break;
			case R.id.camera_main_hide:
				HideCamera();		
				break;
			case R.id.camera_show:
				//cameraShow.setEnabled(false);
				cameraContainer.getLayoutParams().height = mQuickReturnView.getMeasuredWidth();
				if(mPreview== null)
					showCamera();
				else if(mPreview.getTag() == null){
					mPreview.onClick();
					fina_pic.setVisibility(View.VISIBLE);
					mPreview.setTag("captured");
				}else if(mPreview.getTag() != null && mPreview.getTag().equals("captured")){
					SaveImages();
					mPreview.setTag(null);
					fina_pic.setImageDrawable(null);
					fina_pic.setVisibility(View.GONE);
				    mPreview.startPreview();
				}
				break;
			case R.id.camera_main_switch:
				SwitchCamera();		
				break;
			default:
				break;
			}
		}
		

	    private void SaveImages() {
	  		Bitmap bmp = null;
	  	//	bmp = ((BitmapDrawable)fina_pic.getDrawable()).getBitmap();
	  		bmp = DataHolder.picTaken;
	  		if(bmp == null)
	           return;
	  		else Save(bmp);

	    }
		
	    private void Save(Bitmap last) {
				final File pictureFileDir = getDir();
				
				if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
				
				    Log.d("PIC_Capture", "Can't create directory to save image.");
				   return;
				
				}
				
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
			    String date = dateFormat.format(new Date());
			    final String photoFile = "Picture_" + date + ".jpg";
			    String imPath =  pictureFileDir.getPath(); 
			    final String filename = imPath + File.separator + photoFile;
			
			    File pictureFile = new File(filename);
			
			    try {
			      FileOutputStream fos = new FileOutputStream(pictureFile);
			      last.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			      fos.close();
			    } catch (Exception error) {
			      Log.d("PIC_Capture", "File" + filename + "not saved: "
			          + error.getMessage());
			    }finally{
			    	Post p =new Post();
			        p.setType("image");
			        p.setImage(last);
			        p.setFrom(user);
			        p.setImagePath(imPath);
			        p.setImageName(photoFile);
			        p.setID("1121");
	   				p.setDate("20/10/2013");
			        Posts.add(0, p);
	        		if(mListView.getAdapter() instanceof WrapperListAdapter) {
	        			demoListAdapter = (DemoListAdapter)((WrapperListAdapter)mListView.getAdapter()).getWrappedAdapter();
	        		} else if (mListView.getAdapter() instanceof DemoListAdapter) {
	        			demoListAdapter = (DemoListAdapter) mListView.getAdapter();
	        		}
	   
			        bogusRemoteService.updateEntries(p);
			        demoListAdapter.notifyDataSetChanged();
			        demoListAdapter.onScrollNext();
			    }
	        		/*
	        		if(demoListAdapter!=null)
	        			demoListAdapter.clearEntries();
	        		
			        bogusRemoteService.setEntries(Posts);
			        demoListAdapter.onScrollNext();
			    }
			    */
			    
		  }

		  private File getDir() {
			    File sdDir = Environment
			      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			    return new File(sdDir, "Selfie");
		  }


		private void SwitchCamera() {
			mPreview.getHolder().removeCallback(mPreview);
		    mPreview.mCamera.release();
		    
		    cameraContainer.removeView(mPreview); // This is necessary.
	        CURRENT_CAM = CURRENT_CAM == 0 ? 1 : 0;

			mPreview = new Main_CameraPreview(getActivity(), CURRENT_CAM, Main_CameraPreview.LayoutMode.NoBlank,null);
		   
	        LayoutParams previewLayoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			cameraContainer.addView(mPreview, 0, previewLayoutParams);    
		}


		private void HideCamera() {
			Animation animation = new TranslateAnimation(
			          Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
			          Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f
			      );
			      animation.setDuration(150);
			      
			      cameraContainer.startAnimation(animation);
			      if(mPreview != null){
				      mPreview.stop();
					  cameraContainer.removeView(mPreview); // This is necessary.
					  mPreview = null;
			      }
			      cameraContainer.setVisibility(View.GONE);				
		}
		
		private void showCamera() {
			Animation animation = new TranslateAnimation(
			          Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
			          Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
			      );
			      animation.setDuration(150);
			      animation.setAnimationListener(new AnimationListener() {
			    	
					@Override
					public void onAnimationStart(Animation arg0) {
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						CURRENT_CAM=0;
				        mPreview = new Main_CameraPreview(getActivity(), CURRENT_CAM, Main_CameraPreview.LayoutMode.NoBlank, fina_pic);
				        LayoutParams previewLayoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				        cameraContainer.addView(mPreview, 0, previewLayoutParams);					
					}

				    
				});
			      cameraContainer.startAnimation(animation);
			      cameraContainer.setVisibility(View.VISIBLE);				
		}

		
		/*
		 * 
					@Override
					public void onClick(View v) {
						if(v.getTag() == null){
						  registerReceiver(new CameraSwitch(), new IntentFilter("dev.vision.rave.camera.switch"));
						  mPreview.onClick();
						  if(second)
							  v.setTag("save");
						}else{
							SaveImages();
						}
					}

					private void SaveImages() {
						FrameLayout vTop = (FrameLayout) Top.findViewById(R.id.containner);
						FrameLayout vBottom = (FrameLayout) Bottom.findViewById(R.id.containner);

						for(int i=0; i<vTop.getChildCount(); ++i){
							View view = vTop.getChildAt(i);
							if (view instanceof ImageView) {
					            ImageView imageView = (ImageView) view;
					            TOP = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
					            if(TOP == null)
					            	return;
					        }
						}
						for(int i=0; i<vBottom.getChildCount(); ++i){
							View view = vBottom.getChildAt(i);
							if (view instanceof ImageView) {
					            ImageView imageView = (ImageView) view;
					            BOTTOM = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
					            if(BOTTOM == null)
					            	return;
					        }
						}		
						Bitmap last = combineImages(TOP, BOTTOM);
				        if(last != null)
						Save(last);
					}

					private void Save(Bitmap last) {
						File pictureFileDir = getDir();
						
						if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
						
						    Log.d("PIC_Capture", "Can't create directory to save image.");
						    Toast.makeText(this, "Can't create directory to save image.",
						          Toast.LENGTH_LONG).show();
						    return;
						
						}
						
					    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
					    String date = dateFormat.format(new Date());
					    String photoFile = "Picture_" + date + ".jpg";
					
					    String filename = pictureFileDir.getPath() + File.separator + photoFile;
					
					    File pictureFile = new File(filename);
					
					    try {
					      FileOutputStream fos = new FileOutputStream(pictureFile);
					      last.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					      fos.close();
					      Toast.makeText(this, "New Image saved:" + photoFile,
					          Toast.LENGTH_LONG).show();
					    } catch (Exception error) {
					      Log.d("PIC_Capture", "File" + filename + "not saved: "
					          + error.getMessage());
					      Toast.makeText(this, "Image could not be saved.",
					          Toast.LENGTH_LONG).show();
					    }		
					}

					private File getDir() {
					    File sdDir = Environment
					      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
					    return new File(sdDir, "Selfie");
					}
					
					public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
					    Bitmap cs = null; 
					 
					    int width, height = 0; 
					     
					    if(c.getWidth() > s.getWidth()) { 
					      width = c.getWidth(); 
					    } else { 
					      width = s.getWidth(); 
					    } 
					    
					    height = c.getHeight() + s.getHeight(); 

					    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
					 
					    Canvas comboImage = new Canvas(cs); 
					 
					    comboImage.drawBitmap(c, 0f, 0f, null); 
					    comboImage.drawBitmap(s, 0f, c.getHeight(), null); 
					    
					    return cs; 
				    } 

					void ExhchangeViews(){     
					    //mPreview.getHolder().removeCallback(mPreview);
				        mPreview.stop();
				    	if(rect.equals(new Rect()))
				    		mPreview.getGlobalVisibleRect(rect);

				        ViewGroup parent1 = (ViewGroup) view1.getParent();
						ViewGroup parent2 = (ViewGroup) view2.getParent();

						if (parent1 != null) {
							parent1.removeView(view1);
						}
						
						if (parent2 != null) {
							parent2.removeView(view2);
						}
						
						//mPreview = new CameraPreview(CameraPreviewSampleActivity.this, CURRENT_CAM, CameraPreview.LayoutMode.NoBlank,  CURRENT_CAM == 0? (ImageView) findViewById(R.id.back) :(ImageView) findViewById(R.id.front));

						parent1.addView(view2, FrameLayoutParams);
						parent2.addView(view1, FrameLayoutParams);
						mPreview.setLayoutParams(previewLayoutParams);
						//current.addView(mPreview, 0, previewLayoutParams);
					}
					
					public class CameraSwitch extends BroadcastReceiver{

						@Override
						public void onReceive(Context arg0, Intent intent) {
							if(intent.getAction() == "dev.vision.rave.camera.switch"){
								//mCamera.stopPreview(); 
							    if(!second)
							    {	
								    mPreview.getHolder().removeCallback(mPreview);
								    mPreview.mCamera.release();
								    
							        current.removeView(mPreview); // This is necessary.
					      		    current = view2;
					      		    CURRENT_CAM = CURRENT_CAM == 0 ? 1 : 0;
					
									mPreview = new CameraPreview(CameraPreviewSampleActivity.this, CURRENT_CAM, CameraPreview.LayoutMode.NoBlank,  (ImageView) findViewById(R.id.front));
								   
					
								    current.addView(mPreview, 0, previewLayoutParams);
								    second = true;
							    }
							    unregisterReceiver(this);
							}
						}
						
					}
					*/
	@Override
	public void onPause() {
	    super.onPause();
	    HideCamera();
	}
	
	
	NewPageListener npl = new NewPageListener() {
		Handler h;
		Context c;
	    private LayoutInflater inflater;
		private SparseArray<Bitmap> drawableMap = new SparseArray<Bitmap>();

		protected boolean clicked;
		boolean done;

		protected float x;

		protected float y;
		private AsyncTask<Object, Integer, Boolean> up;
		
	
		@Override
		public void onScrollNext() {
			fetchAsyncTask = new AsyncTask<Void, Void, List<Post>>() {
				@Override
				protected void onPreExecute() {
					// Loading lock to allow only one instance of loading
                	demoListAdapter.lock();
				}
				@Override
                protected List<Post> doInBackground(Void ... params) {
                	List<Post> result;
                	// Mimic loading data from a remote service
                	if (loadingMode == LoadingMode.SCROLL_TO_TOP) {
						result = bogusRemoteService.getNextBatch(SEVER_SIDE_BATCH_SIZE);
					} else {
						result = bogusRemoteService.getNextBatch(SEVER_SIDE_BATCH_SIZE);
					}
					return result;
                }
                @Override
                protected void onPostExecute(List<Post> result) {
    				if (isCancelled() || result == null || result.isEmpty()) {
    					demoListAdapter.notifyEndOfList();
    				} else {
    					// Add data to the placeholder
    					if (loadingMode == LoadingMode.SCROLL_TO_TOP) {
    						demoListAdapter.addEntriesToTop(result);
    					} else {
        					demoListAdapter.addEntriesToBottom(result);
    					}
    					// Add or remove the loading view depend on if there might be more to load
    					if (result.size() < SEVER_SIDE_BATCH_SIZE) {
    						demoListAdapter.notifyEndOfList();
    					} else {
    						demoListAdapter.notifyHasMore();
    					}
    					// Get the focus to the specified position when loading completes
    					if (loadingMode == LoadingMode.SCROLL_TO_TOP) {
    						switch(stopPosition) {
    							case REMAIN_UNCHANGED:
            						mListView.setSelection(result.size());
            						break;
    							case START_OF_LIST:
    								mListView.setSelection(result.size() < SEVER_SIDE_BATCH_SIZE ? 0 : 1);
                					break;
    							case END_OF_LIST:
            						mListView.setSelection(1);
	        						mListView.smoothScrollToPosition(demoListAdapter.getCount());
    						}
    					} else {
    						if (stopPosition != StopPosition.REMAIN_UNCHANGED) {
    							mListView.smoothScrollToPosition(stopPosition == StopPosition.START_OF_LIST ? 0 : (result.size() < SEVER_SIDE_BATCH_SIZE ? demoListAdapter.getCount() : demoListAdapter.getCount() - 2));
    						}else{
    							mListView.smoothScrollBy(20, 0);
    						}
    					}
    				}
                };
                @Override
                protected void onCancelled() {
                	// Tell the adapter it is end of the list when task is cancelled
					demoListAdapter.notifyEndOfList();
                }
			}.execute();
		}
		
		
		/*
		if (loadingMode == LoadingMode.SCROLL_TO_TOP) {
			rowPhoto.setImageResource(position % 2 == 0 ? R.drawable.conversation_driver : R.drawable.conversation_officer);
		} else {
			rowPhoto.setImageResource(sushiMappings.get(name));
		}*/
		
		
	    class ViewHolder {
			public TextView likes;
			public RectangleView image;
			//public TextView maleTxt;
			//public TextView femaleTxt;
			//ImageView male;
			//ImageView female;
			public TextView displayName;
			public TextView time;
			public ViewStub include;
	    	public PieGraph pg;
			public HeartChart pw_two;
			public ImageView likeshape;
			public ImageView commentbox;
			public HoloCircularProgressBar progressBar;
			public HexagonImageView userPic;
			public ProgressButton progressButton;

		}

	    public void UploadFile(String sourceFileUri, String filename , Post post, User user,ProgressButton f){
	 		String fileName = sourceFileUri+filename;

			if(!DataHolder.ToUpload.contains(fileName)){
		         DataHolder.ToUpload.add(fileName);

				if(up!= null){
					if(up.getStatus() == AsyncTask.Status.FINISHED)
						 up = new Uploader(sourceFileUri,filename ,post,user,f).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
				}
				else up = new Uploader(sourceFileUri,filename ,post,user,f).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
			}
		}
		
		
	

	@Override
	public View getInfiniteScrollListView(int position, View convertView, ViewGroup group) {
			final ViewHolder holder;
			c = getActivity();
	        inflater = LayoutInflater.from(c);
	        h = new Handler();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_image,group,false);
			holder = new ViewHolder();

			//User Details
			holder.displayName= (TextView) convertView.findViewById(R.id.displayName);
			holder.userPic= (HexagonImageView) convertView.findViewById(R.id.userPic);

			//Post
			holder.time= (TextView) convertView.findViewById(R.id.time);

			
			holder.image = (RectangleView) convertView.findViewById(R.id.image);
			holder.likeshape = (ImageView) convertView.findViewById(R.id.likesshape);
			holder.commentbox = (ImageView) convertView.findViewById(R.id.commentbox);
			
			//ProgressBar
			holder.progressBar = (HoloCircularProgressBar) convertView.findViewById(R.id.holoCircularProgressBar1);

		    holder.progressButton = (ProgressButton) convertView.findViewById(R.id.pin_progress_4);

		 

			holder.likes= (TextView) convertView.findViewById(R.id.likestxt);

			holder.include = (ViewStub) convertView.findViewById(R.id.graphLike);
			//holder.maleTxt = (TextView) view.findViewById(R.id.maleTxt);
			//holder.femaleTxt = (TextView) view.findViewById(R.id.femaleTxt);
			//holder.pg = (PieGraph) view.findViewById(R.id.graph);
			//holder.male = (ImageView) view.findViewById(R.id.male);
			//holder.female = (ImageView) view.findViewById(R.id.female);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final View view= convertView;
		final Post post = Posts.get(position);
		final Likes likes = post.getLikes();
		
		//Post Sender details
		holder.displayName.setText(user.getDisplay_Name());
		if(user.hasProfile_Pic()){
			holder.userPic.setImageBitmap(user.getProfile_Pic(c));
		}else if(user.getProfile_PicURL() == null || user.getProfile_PicURL() == ""){
			holder.userPic.setImageBitmap(user.getDefaultP_Pic(c));
		}else{
			imageLoader.displayImage(user.getProfile_PicURL(), holder.userPic, options, profileListener);
		}			

		holder.time.setText(post.getDate());			
		holder.likes.setText(String.valueOf(likes.size()));
		//holder.image.setImageBitmap(post.getImage());
		holder.likeshape.setSelected(post.isliked(user));
		
		//imageLoader.displayImage(post.getImageUrl(), holder.image, options, animateFirstListener);
		
		
		holder.likeshape.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!v.isSelected()){
					v.setSelected(true);
					post.like(user);
				}else{
					v.setSelected(false);
					post.unlike(user);
				}
				holder.likes.setText(String.valueOf(post.getLikesSize()));
			}
		});
		
		holder.commentbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		
		
		holder.likes.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    long total = likes.size();
			    if(total > 0){

		    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		if(!done){
		    			
					    DataHolder.stopped = false;
				    	holder.include.setVisibility(View.VISIBLE);
						holder.pw_two = (HeartChart) view.findViewById(R.id.progressBarTwo);

				    	progress = 0;
				    	holder.pw_two.resetCount();
					    	
					    
				    	long female = likes.getFEMALE_LIKES();
					   
					    //long male = likes.getMALE_LIKES();
					    //long addd = male + female;      
					    //long total = addd - start;
					    
				        long burn = female;

				        burned = burn*360/total;  
						Thread s = new Thread(new Runnable() {

							@Override
							public void run() {
								holder.pw_two.setBurned(burned);
								running = true;
								while(progress<360) {
									holder.pw_two.incrementProgress();
									progress++;

									try {
										Thread.sleep(2);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								running = false;
							}
				        });
						s.start();
						//DataHolder.DrawLikes3(holder.pg, 50, 50, holder.maleTxt, holder.femaleTxt);								
				    
			        	done=true;
		    		}
				}
				if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
					holder.include.setVisibility(View.GONE);
					DataHolder.stopped = true;
					progress = 0;
					holder.pw_two.stopSpinning();
					holder.pw_two.resetCount();
					//holder.pg.removeSlices();
					//holder.pg.postInvalidate();
		        	done=false;

				}
				return true;

			    }
			    return false;
			}
			
		});


		
		holder.image.setOnLongClickListener(new OnLongClickListener() {
			private String name;

			@SuppressLint("NewApi") @Override
			public boolean onLongClick(View v) {
				clicked=true;

				 final RelativeLayout parent =(RelativeLayout) v.getParent();
				 final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) DataHolder.pixelsFromDp(getActivity(),200), (int)  DataHolder.pixelsFromDp(getActivity(),200));

				
				if(DataHolder.stopped){
					
				 	DataHolder.stopped = false;
					DataHolder.qs = QuickShareTouch.getQuickShare();

					if(DataHolder.qs == null)
					DataHolder.qs = QuickShareTouch.NewInstance(getActivity(), savedInstanceState);
					
					DataHolder.qs.setTranslationX(x-(DataHolder.pixelsFromDp(getActivity(), 200)/2));
					DataHolder.qs.setTranslationY(y-(DataHolder.pixelsFromDp(getActivity(), 200)/2));
					parent.addView(DataHolder.qs,lp);
					h= new Handler();
					h.post(new Runnable() {


						@Override
						public void run() {
							
							if(name==null && !DataHolder.stopped){

								mListView.setPagingEnabled(false);
								vp.setPagingEnabled(false);
								DataHolder.qs.AddShare("facebook");
								name= "facebook";

							}else if (name!=null && name.equals("facebook") && !DataHolder.stopped){
								DataHolder.qs.AddShare("twitter");
								name= "twitter";

							}else if (name!=null && name.equals("twitter") && !DataHolder.stopped){
								DataHolder.qs.AddShare("other");
								name= "other";

							}else if (name!=null && name.equals("other") && !DataHolder.stopped){
								DataHolder.qs.AddShare("email");
								name= null;
								return;
							}
							if(!DataHolder.stopped)
								h.post(this);
							else name= null;

						}
					});
				}
				
				return false;
			}
		});


		holder.image.setOnTouchListener(new OnTouchListener() {
		 
			@Override
			public boolean onTouch(View v, MotionEvent event) {	
				 

					 final RelativeLayout parent =(RelativeLayout) v.getParent();
					 x= event.getX();
					 y= event.getY()+DataHolder.pixelsFromDp(getActivity(), (float) 33.33);
				 if (clicked) {
					 if (event.getAction() == MotionEvent.ACTION_UP) {
							DataHolder.stopped=true;
							clicked=false;

							h.removeCallbacksAndMessages(null);
							if(DataHolder.qs!=null){
								DataHolder.qs.dispatchTouchEvent(event);
								parent.removeView(DataHolder.qs);
					 		}
							mListView.setPagingEnabled(true);
							vp.setPagingEnabled(true);
							return false;
					 }
					 //return true; wasn't commented
				 }				
				 return false;
			}
		});

		holder.image.setTag(position);
		if(post.getImage() == null){
			holder.image.setImageBitmap(null);
			imageLoader.displayImage(imageUrls[position], holder.image, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
            	 holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            	holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            	int pos = (Integer) view.getTag();
            	post.setImage(loadedImage);
            	Posts.set(pos, post);
            	holder.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            	holder.progressBar.setVisibility(View.GONE);

            }
        }, new ImageLoadingProgressListener( ) {

            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
            	
            	holder.progressBar.setProgress( current/total);
            }
        });
		}else{
			if(post.getImageUrl() == null){
				if(post.progress<100)
        			holder.progressButton.setVisibility(View.VISIBLE);
    			else holder.progressButton.setVisibility(View.GONE);
        			
				holder.progressButton.setProgress(post.progress);
        		
	  			final String pictureFileDir = post.getImagePath();
	  			final String photoFile = post.getImageName();
			  	UploadFile(pictureFileDir  + File.separator, photoFile, post, user, holder.progressButton);			
				
			}else{
				holder.progressButton.setVisibility(View.GONE);
			}
		    holder.progressBar.setVisibility(View.GONE);
			holder.image.setImageBitmap(post.getImage());
		}
		/*if(drawableMap.get(position) == null){
			Bitmap img = imageLoader.loadImageSync(post.getImageUrl());
			if(img!=null){
				drawableMap.put(position, img);
			}
		}else{
			holder.image.setImageBitmap(drawableMap.get(position));
		}
		*/

		return convertView;
	}
	};

}
