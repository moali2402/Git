package dev.vision.rave.fragments;

import java.io.IOException;
import java.io.InputStream;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.github.siyamed.shapeimageview.OctogonImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import dev.vision.rave.user.User;
import dev.vision.rave.views.CircularImageView;
import dev.vision.rave.views.FirstName_Cap;
import dev.vision.rave.views.HexagonMaskView;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.widget.TextView;

public class Profile_Fragment extends Fragment {

	 public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	 public static final String LAYOUT_ID = "Layout_ID";
	 

	 public static final Profile_Fragment newInstance(String message, int layid, User user2, ImageLoader imageLoader)

	 {

	   Profile_Fragment f = new Profile_Fragment();

	   Bundle bdl = new Bundle(1);

	   bdl.putString(EXTRA_MESSAGE, message);
	   f.user = user2;
	   f.imageLoader= imageLoader;
	   bdl.putInt(LAYOUT_ID, layid);
	   
	   f.setArguments(bdl);
	   return f;

	 }


	private User user;
	//private HexagonMaskView profilePic;
	private HexagonImageView profilePic;

	private ImageLoader imageLoader;
	WebView web_right;
	 

	 @SuppressLint("NewApi") 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,

	   Bundle savedInstanceState) {

		 String message = getArguments().getString(EXTRA_MESSAGE);
	     int layoutId = getArguments().getInt(LAYOUT_ID);
		 View v = inflater.inflate(layoutId, container, false);
		 web_right = (WebView) v.findViewById(R.id.web_right);
		 profilePic=(HexagonImageView) v.findViewById(R.id.circularImageView); // CircularImageView
		 if(web_right!= null){
			    setTransparent();
			    SetCharts();
				String webHtml_doughnut= ReadFromfile("Personality.html", getActivity());

				webHtml_doughnut = webHtml_doughnut.replace("$WIDTH_V$",""+ 300).replace("$COLOR_HEX$", String.format("#%06X", (0xFFFFFF & DataHolder.Text_Colour)));
		
				web_right.loadDataWithBaseURL("file:///android_asset", webHtml_doughnut, "text/html", "UTF-8", null);
			    setTransparent();
		 }else{
			 final FirstName_Cap name = (FirstName_Cap) v.findViewById(R.id.userName);
			 name.setText(user.getDisplay_Name());
		 }
		 return v;
	 }
	 
	 private void setTransparent(){
		if (Build.VERSION.SDK_INT >= 11) {
			web_right.setBackgroundColor(0x01000000);
		} else {
			web_right.setBackgroundColor(0x00000000);
		}
	 }
	
	 
	 @SuppressLint("NewApi") 
	 private void SetCharts() {
			 	web_right.setOnTouchListener(new View.OnTouchListener() {

			 	    @Override
					public boolean onTouch(View v, MotionEvent event) {
			 	      return true;
			 	    }
			 	  });	
			 	web_right.setHorizontalScrollBarEnabled(false);
			 	web_right.setVerticalScrollBarEnabled(false);
				web_right.getSettings().setAllowUniversalAccessFromFileURLs(true);
				web_right.getSettings().setAllowFileAccessFromFileURLs(true);
				web_right.getSettings().setJavaScriptEnabled(true);
				
		}

		public String ReadFromfile(String fileName, Context context) {
			 InputStream is = null;
			try {
				is = getActivity().getAssets().open(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			 int size = 0;
			try {
				size = is.available();
			} catch (IOException e) {
				e.printStackTrace();
			}

			 byte[] buffer = new byte[size];
			 try {
				is.read(buffer);
				 is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			 String str = new String(buffer);
			    return str;
			}
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) 
	    {
	        super.onActivityCreated(savedInstanceState);
	        setProfileInfo();
	    }
	    

	    @SuppressLint("NewApi") private void setProfileInfo() {
	    	final DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheOnDisc(true)
			.delayBeforeLoading(100)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();
			final TextView splash = (TextView) getView().findViewById(R.id.TextView03);
			if(splash!= null){
				profilePic.post(new Runnable() {
				    @Override
				    public void run() {
				    	
				    	if(user.getProfile_PicURL() == null)
							profilePic.setImageBitmap(user.getProfile_Pic(getActivity()));
						else
							imageLoader.displayImage(user.getProfile_PicURL(), profilePic,options, new ImageLoadingListener() {
								
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onLoadingFailed(String imageUri, View view,
										FailReason failReason) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
									user.setProfile_Pic(loadedImage);
								}
								
								@Override
								public void onLoadingCancelled(String imageUri, View view) {
									// TODO Auto-generated method stub
									
								}
							});			
					   
				    }
				});
				
				final ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.flip); 
				
				//anim.setRepeatCount(Animation.INFINITE);
				anim.setDuration(1000);
				anim.addListener(new AnimatorListener() {
					
					@Override
					public void onAnimationStart(Animator arg0) {
						splash.setSelected(true);
					}
					
					@Override
					public void onAnimationRepeat(Animator arg0) {
						
					}
					
					@Override
					public void onAnimationEnd(Animator arg0) {
						try {
							Thread.sleep(700);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Animation a = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
			            a.setDuration(200);
			            a.setAnimationListener(new AnimationListener() {
	
			                @Override
							public void onAnimationEnd(Animation animation) {
			                	splash.setVisibility(View.GONE); 
			                }
	
			                @Override
							public void onAnimationRepeat(Animation animation) {
			                        // Do what ever you need, if not remove it.  
			                }
	
			                @Override
							public void onAnimationStart(Animation animation) {
			                        // Do what ever you need, if not remove it.  
			                }
	
			            });
			            splash.startAnimation(a);
					}
					
					@Override
					public void onAnimationCancel(Animator arg0) {
						
					}
				});
				
				//anim.setInterpolator(new LinearInterpolator());
				//anim.setDuration(700);
	
				// Start animating the image
			//	splash.startAnimation(anim);
				anim.setTarget(splash);
				splash.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						anim.start();
						
					}
				});
			}
		}
	    

	    class Rate_User extends AsyncTask<Void, Void, Void>{

			@Override
			protected Void doInBackground(Void... params) {
				String id = user.getId();//"100005805391241";
				//String url = "https://graph.facebook.com/"+id+"/statuses?fields=message";
				
				
				
				return null;

			}
	    	
	    }

}
