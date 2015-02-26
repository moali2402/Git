/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.vision.rave.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.echo.holographlibrary.PieGraph;

import dev.vision.rave.Constants;
import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import dev.vision.rave.progresswheel.HeartChart;
import dev.vision.rave.user.Likes;
import dev.vision.rave.user.Post;
import dev.vision.rave.user.User;
import dev.vision.rave.views.CircularImageResizable;
import dev.vision.rave.views.InScrollableListView;
import dev.vision.rave.views.QuickShareTouch;
import dev.vision.rave.views.RectangleView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class DefaultFragment extends ListFragment{
	
	private InScrollableListView mListView;
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
	private String[] imageUrls;
	private User user;
	private ArrayList<Post> Posts = new ArrayList<Post>();
	private HashMap<String, Post> PostsMap = new HashMap<String, Post>();
	private int progress = 0;
	private long burned;
	private boolean running;
	private Bundle savedInstanceState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.social, null);
		mHeader = inflater.inflate(R.layout.headers, null);
		mQuickReturnView = (RelativeLayout) view.findViewById(R.id.profile_sticky);
		mPlaceHolder = mHeader.findViewById(R.id.headerContainer);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.savedInstanceState=savedInstanceState;
		imageUrls = Constants.IMAGES;
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
		mListView = (InScrollableListView) getListView();
		
		mListView.addHeaderView(mHeader);
		MyAdapter adapter = new MyAdapter(getActivity());
		
		setListAdapter(adapter);
		
		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mQuickReturnHeight = mQuickReturnView.getMeasuredHeight();
						mPlaceHolder.getLayoutParams().height = mQuickReturnHeight;

						mListView.computeScrollY();
						mCachedVerticalScrollRange = mListView.getListHeight();
					}
				});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				mScrollY = 0;
				int translationY = 0;
				
				if (mListView.scrollYIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}

				int rawY = mPlaceHolder.getTop()
						- Math.min(
								mCachedVerticalScrollRange
										- mListView.getHeight(), mScrollY);
				
				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY <= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = 0;
					break;

				case STATE_ONSCREEN:
					if (rawY <= 0) {
						mState = STATE_OFFSCREEN;
						rawY =0;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:
					translationY = (rawY - mMinRawY);
					if (translationY > 0) {
						translationY = rawY;
						mMinRawY = rawY;
					}

					if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					if (translationY < 0) {
						translationY=0;
						mState = STATE_OFFSCREEN;
						rawY=0;
						mMinRawY = rawY;
					}
					break;
				}
				
				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}
	
	public class MyAdapter extends BaseAdapter {
		Handler h;
		Context c;
		
	    private LayoutInflater inflater;
		private SparseArray<Bitmap> drawableMap = new SparseArray<Bitmap>();

		protected boolean clicked;
		boolean done;

		protected float x;

		protected float y;
	    public MyAdapter(Context context) {
	    	c = context;
	        inflater = LayoutInflater.from(context);
	        h = new Handler();
	    }

	    private class ViewHolder {
			public TextView likes;
			public RectangleView image;
			//public TextView maleTxt;
			//public TextView femaleTxt;
			//ImageView male;
			//ImageView female;
			public TextView displayName;
			public TextView time;
			public View include;
	    	public PieGraph pg;
			public HeartChart pw_two;
			public ImageView likeshape;
			public ImageView commentbox;
			public ProgressBar progressBar;
			public CircularImageResizable userPic;

		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int position) {
			return Posts.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup group) {
			
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_image, null);
				holder = new ViewHolder();

				//User Details
				holder.displayName= (TextView) convertView.findViewById(R.id.displayName);
				holder.userPic= (CircularImageResizable) convertView.findViewById(R.id.userPic);

				//Post
				holder.time= (TextView) convertView.findViewById(R.id.time);

				
				holder.image = (RectangleView) convertView.findViewById(R.id.image);
				holder.likeshape = (ImageView) convertView.findViewById(R.id.likesshape);
				holder.commentbox = (ImageView) convertView.findViewById(R.id.commentbox);
				
				//ProgressBar
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.holoCircularProgressBar1);

				holder.likes= (TextView) convertView.findViewById(R.id.likestxt);

				holder.include = convertView.findViewById(R.id.graphLike);
				//holder.maleTxt = (TextView) view.findViewById(R.id.maleTxt);
				//holder.femaleTxt = (TextView) view.findViewById(R.id.femaleTxt);
				//holder.pg = (PieGraph) view.findViewById(R.id.graph);
				//holder.male = (ImageView) view.findViewById(R.id.male);
				//holder.female = (ImageView) view.findViewById(R.id.female);
				holder.pw_two = (HeartChart) convertView.findViewById(R.id.progressBarTwo);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			
			final Post post = Posts.get(position);
			final Likes likes = post.getLikes();
			
			//Post Sender details
			holder.displayName.setText(user.getDisplay_Name());
				holder.userPic.setImageBitmap(user.getProfile_Pic(c));

			//holder.userPic.setImageBitmap(user.getProfile_Pic(c));
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

							//		mListView.setPagingEnabled(false);

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
						//		mListView.setPagingEnabled(true);
								return false;
						 }
						 //
						return true;
					 }				
					 return false;
				}
			});
			
			//imageLoader.displayImage(imageUrls[position], holder.image, options, null);
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

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    

	        public class BitmapWrapper
	    {
	        public Bitmap bitmap;
	        public String imageurl;
	    }

	}
	public void setUser(User u){
		user = u;
	}
}