package dev.vision.rave.fragments;
/*
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.f2prateek.progressbutton.ProgressButton;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import de.passsy.holocircularprogressbar.HoloCircularProgressBar;
import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import dev.vision.rave.progresswheel.HeartChart;
import dev.vision.rave.user.Likes;
import dev.vision.rave.user.Post;
import dev.vision.rave.views.CircularImageResizable;
import dev.vision.rave.views.QuickShareTouch;
import dev.vision.rave.views.RectangleView;


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
		public ViewStub include;
    	public PieGraph pg;
		public HeartChart pw_two;
		public ImageView likeshape;
		public ImageView commentbox;
		public HoloCircularProgressBar progressBar;
		public CircularImageResizable userPic;
		public ProgressButton progressButton;

	}

	@Override
	public int getCount() {
		return Posts.size();
	}

	@Override
	public Object getItem(int position) {
		return Posts.get(position);
	}


	

	@Override
	public View getView(final int position, View convertView, ViewGroup group) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_image,group,false);
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
		holder.progressButton.setVisibility(View.GONE);
		holder.progressButton.setProgress(0);
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
					 //
					return true;
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
	  			holder.progressButton.setVisibility(View.VISIBLE);
	  			final String pictureFileDir = post.getImagePath();
	  			final String photoFile = post.getImageName();
	  			//up.UploadFile(pictureFileDir  + File.separator, photoFile, post, user, holder.progressButton);			
			}else{
				holder.progressButton.setVisibility(View.GONE);
			}
		    holder.progressBar.setVisibility(View.GONE);
			holder.image.setImageBitmap(post.getImage());
		}
		
		// was here the one down
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
*/
/*if(drawableMap.get(position) == null){
Bitmap img = imageLoader.loadImageSync(post.getImageUrl());
if(img!=null){
	drawableMap.put(position, img);
}
}else{
holder.image.setImageBitmap(drawableMap.get(position));
}
*/

