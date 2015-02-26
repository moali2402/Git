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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
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
import dev.vision.rave.QuickReturnType;
import dev.vision.rave.R;
import dev.vision.rave.TestFragmentAdapter;
import dev.vision.rave.camera.CameraPreviewSampleActivity;
import dev.vision.rave.camera.Main_CameraPreview;
import dev.vision.rave.listeners.BogusRemoteService;
import dev.vision.rave.listeners.DemoListAdapter;
import dev.vision.rave.listeners.QuickReturnListViewOnScrollListener;
import dev.vision.rave.listeners.ScrollableListView;
import dev.vision.rave.listeners.DemoListAdapter.NewPageListener;
import dev.vision.rave.listeners.ScrollableListView.LoadingMode;
import dev.vision.rave.listeners.ScrollableListView.StopPosition;
import dev.vision.rave.progresswheel.HeartChart;
import dev.vision.rave.user.Likes;
import dev.vision.rave.user.Post;
import dev.vision.rave.user.User;
import dev.vision.rave.user.User.Type;
import dev.vision.rave.views.BlurredImageView;
import dev.vision.rave.views.CPageIndicator;
import dev.vision.rave.views.CircularImageResizable;
import dev.vision.rave.views.CustomViewPager;
import dev.vision.rave.views.QuickShareTouch;
import dev.vision.rave.views.RectangleView;

public class Social_Activity_Fragment extends ProgressListFragment implements OnItemClickListener, OnClickListener {
	
	
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
	private FrameLayout cameraContainer;
	private Main_CameraPreview mPreview;
	private int CURRENT_CAM;
	boolean colorOverlay;
	ImageView fina_pic;
	CustomViewPager vp;
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

	private int mCachedVerticalScrollRange;
	private int mQuickReturnHeight;
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mScrollY;	
	private TranslateAnimation anim;
	private ImageLoadingListener profileListener = new ProfileDisplayListener();
    String[] imagesToShow = Constants.IMAGES;
	ArrayList<Drawable> darray= new ArrayList<Drawable>();
	int i=0;
	Handler h;
	public boolean done;
	private AsyncTask<Void, Void, List<Post>> fetchAsyncTask;
	private LoadingMode loadingMode = LoadingMode.SCROLL_TO_BOTTOM;
	private StopPosition stopPosition = StopPosition.REMAIN_UNCHANGED;
	private DemoListAdapter demoListAdapter;
	private BogusRemoteService bogusRemoteService;
	private ImageView scrollToTop;
	private static final int SEVER_SIDE_BATCH_SIZE = 10;
	private float trans;
	public int mState = STATE_ONSCREEN;
	private int mMinRawY = 0;

	public class OnPauseScrollListener extends PauseOnScrollListener  {
			private int mScrollState;
			private boolean visible = true;
			QuickReturnListViewOnScrollListener sc;
			public OnPauseScrollListener(QuickReturnListViewOnScrollListener scrollListener, ImageLoader imageLoader,
					boolean pauseOnScroll, boolean pauseOnFling) {
				super(imageLoader, pauseOnScroll, pauseOnFling);
				sc = scrollListener;
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(mScrollState != OnScrollListener.SCROLL_STATE_IDLE)
				imageLoader.pause();
				sc.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				demoListAdapter.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				
				
				if(firstVisibleItem > 3 && !visible){
					translate(-trans);
					visible= true;
				}else if(firstVisibleItem < 4 && visible){
					translate(0);
					visible=false;
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


    
	public static final Social_Activity_Fragment newInstance(String message, int layid, User user2)
	{
		
	   Social_Activity_Fragment f = new Social_Activity_Fragment();

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
	     BlurLayout.setGlobalDefaultDuration(450);

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
		 
		 mHeader = inflater.inflate(R.layout.activity_header, null);

	     
	 	 scrollToTop.setOnClickListener(this);
		 return layout;
	 }
	 

		
		
	  OnGlobalLayoutListener vto =new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mQuickReturnHeight = mQuickReturnView.getMeasuredHeight();
				//mPlaceHolder.getLayoutParams().height = mQuickReturnHeight;
				mListView.computeScrollY();
				mCachedVerticalScrollRange = mListView.getListHeight();
				viewH = mQuickReturnHeight-getActionBarHeight();

			}
		};
	
	 
	 protected void Init() {
		 	trans = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());

	 		//mListView.getViewTreeObserver().addOnGlobalLayoutListener(vto);		
	 		//mListView.setOnScrollListener(new OnPauseScrollListener(imageLoader, true, true));
	 		int headerHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.shadeHeight);
	        QuickReturnListViewOnScrollListener scrollListener = new QuickReturnListViewOnScrollListener(QuickReturnType.HEADER,
	                mQuickReturnView, -headerHeight, null, 0);
	        // Setting to true will slide the header and/or footer into view or slide out of view based 
	        // on what is visible in the idle scroll state
	        scrollListener.setCanSlideInIdleScrollState(true);
	        mListView.setOnScrollListener(new OnPauseScrollListener(scrollListener,imageLoader, true, true));
	    
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
        
         options = new DisplayImageOptions.Builder()
		.showImageOnLoading(new ColorDrawable(DataHolder.APP_BACKGROUND))
		.showImageForEmptyUri(null)
		.showImageOnFail(null)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.delayBeforeLoading(500)
		.resetViewBeforeLoading(true)
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
		
        vp=(CustomViewPager) getActivity().findViewById(R.id.main);

        mListView = (ScrollableListView) getListView();
       
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setScrollingCacheEnabled(false);
		//mListView.setVerticalFadingEdgeEnabled(true);
		//mListView.setFadingEdgeLength(5);

		mListView.addHeaderView(mHeader);
		new LoadData().execute();
	}
    
    
	private int getActionBarHeight() {
		return mQuickReturnHeight;//backgroundimage.getHeight();
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
                User s = user.clone();
                User b = user.clone();

                for(int i = 0; i<imageUrls.length; i++){
		   			
	   				Post p = new Post();
	   				p.setType("video");
	   				p.setType("image");
	   				p.setImageUrl(imageUrls[i]);
	   				p.setID("1121");
	   				p.setDate("20/10/2013");

		   			s.setSex(Type.FEMALE);
		   			s.setUsername("KI");
	   				p.like(s);

		   			b.setSex(Type.MALE);
		   			b.setUsername("MO");
	   				p.like(b);

	   				p.setFrom(user);
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
	    
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.camera_main_fullscreen:
		 		startActivity(new Intent(getActivity(),CameraPreviewSampleActivity.class));				
				break;
			case R.id.scrolltotop:
		 		mListView.smoothScrollToPosition(0);					
				break;
			default:
				break;
			}
		}
		

	
		
	@Override
	public void onPause() {
	    super.onPause();
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
		
		
	    class ViewHolder {
			public RectangleView image;
			//public TextView maleTxt;
			//public TextView femaleTxt;
			//ImageView male;
			//ImageView female;
			public TextView displayName;
			public TextView time;
			public ViewStub include;
			public HeartChart pw_two;
			public TextView likes;
			public TextView commentbox;
			public HoloCircularProgressBar progressBar;
			public HexagonImageView userPic;
			protected BlurLayout mSampleLayout4;
			protected View hover4;
		}
		
		private boolean longClicked;
	
	

	@Override
	public View getInfiniteScrollListView(int position, View convertView, ViewGroup group) {
			final ViewHolder holder;
			c = getActivity();
	        inflater = LayoutInflater.from(c);
	        h = new Handler();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_item,group,false);

			holder = new ViewHolder();
			holder.mSampleLayout4 = (BlurLayout)convertView.findViewById(R.id.blur_layout4);

	        holder.hover4 = inflater.inflate(R.layout.hover_sample4,null);

			//User Details
			holder.displayName= (TextView) convertView.findViewById(R.id.displayName);
			holder.userPic= (HexagonImageView) convertView.findViewById(R.id.userPic);

			//Post
			holder.time= (TextView) convertView.findViewById(R.id.time);

			
			holder.image = (RectangleView) convertView.findViewById(R.id.image);
			
			//ProgressBar
			holder.progressBar = (HoloCircularProgressBar) convertView.findViewById(R.id.holoCircularProgressBar1); 

			holder.likes= (TextView) convertView.findViewById(R.id.likes);
			holder.commentbox = (TextView) convertView.findViewById(R.id.comments);

			
			holder.include = (ViewStub) convertView.findViewById(R.id.graphLike);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
        holder.mSampleLayout4.setHoverView(holder.hover4);
        
        holder.mSampleLayout4.addChildAppearAnimator(holder.hover4, R.id.content, Techniques.SlideInRight);
        holder.mSampleLayout4.addChildDisappearAnimator(holder.hover4, R.id.content, Techniques.SlideOutRight);
        
        
        if(holder.mSampleLayout4.isHover())
        holder.mSampleLayout4.dismissHover();

       
		
		final View view= convertView;
		final Post post = Posts.get(position);
		final Likes likes = post.getLikes();
		final User user = post.getFrom();
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
		holder.likes.setActivated(post.isliked(user));
		
		//holder.image.setImageBitmap(null);

		holder.image.setTag(position);
		//if(post.getImage() == null){
			imageLoader.displayImage(post.getImageUrl(), holder.image, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
             	 holder.progressBar.setProgress(0);
            	 holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            	holder.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            	holder.progressBar.setProgress(1);
            	int pos = (Integer) view.getTag();
            	Post p = Posts.get(pos);
            	p.setImage(loadedImage);
            	Posts.set(pos, p);
            	holder.progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            	holder.progressBar.setVisibility(View.INVISIBLE);

            }
        }, new ImageLoadingProgressListener( ) {

            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
            	
            	holder.progressBar.setProgress( current/total);
            }
        });
		//}else{
		//	holder.progressBar.setVisibility(View.INVISIBLE);
		//	holder.image.setImageBitmap(post.getImage());
		//}
		
		//imageLoader.displayImage(post.getImageUrl(), holder.image, options, animateFirstListener);
		
		
		holder.likes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!v.isActivated()){
					v.setActivated(true);
					post.like(user);
				}else{
					v.setActivated(false);
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
		
		holder.likes.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				longClicked = true;
				long total = likes.size();
			    if(total > 0){
			    	if(!done){
		    			
					    DataHolder.stopped = false;
				    	holder.include.setVisibility(View.VISIBLE);
						holder.pw_two = (HeartChart) view.findViewById(R.id.progressBarTwo);

				    	progress = 0;
				    	holder.pw_two.resetCount();
					    	
					    
				    	long female = likes.getFEMALE_LIKES();
					   
					    
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
				return false;
			}
		});
		holder.likes.setOnTouchListener(new OnTouchListener() {
			

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(longClicked){
				    
	
			    	
					if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
						holder.include.setVisibility(View.GONE);
						DataHolder.stopped = true;
						progress = 0;
						longClicked=false;
			        	done=false;
						assert holder.pw_two!=null;
						holder.pw_two.stopSpinning();
						holder.pw_two.resetCount();
						//holder.pg.removeSlices();
						//holder.pg.postInvalidate();
						
	
					}
					return true;
				    
				}
			    return false;
			}
			
		});


		
		holder.mSampleLayout4.setOnLongClickListener(new OnLongClickListener() {
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


		holder.mSampleLayout4.setOnTouchListener(new OnTouchListener() {
		 
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
					 return true; //wasn't commented
				 }				
				 return false;
			}
		});

		

		return convertView;
	}
	};

}
