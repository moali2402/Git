package dev.vision.rave;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import dev.vision.rave.Constants.Extra;
import dev.vision.rave.views.CustomViewPager;


public class ImagePagerActivity extends Activity {

	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	Bundle savedInstanceState;
	CustomViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		this.savedInstanceState = savedInstanceState;

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
		int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.color.ProfileBack)
			.showImageOnFail(R.color.ProfileBack)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();

		pager = (CustomViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(imageUrls));
		pager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}
	@Override
	public void onResume(){
		super.onResume();
		
		
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.copyitem_list_image, view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		/*	imageView.setOnTouchListener(new OnTouchListener() {
				String name;
				
				@SuppressLint("NewApi")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
						final RelativeLayout parent =(RelativeLayout) v.getParent();
						final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DataHolder.qssize, DataHolder.qssize);

						//DataHolder.qs = QuickShare.getQuickShare();
						final Handler h = new Handler();

						if(DataHolder.qs == null)
						//DataHolder.qs = QuickShare.NewInstance(ImagePagerActivity.this, savedInstanceState);
						if(DataHolder.stopped){
							
							if (event.getAction() == MotionEvent.ACTION_DOWN) {

								DataHolder.stopped = false;
			
								float x= event.getX();
								float y= event.getY();
								DataHolder.qs.setX(x-(DataHolder.qssize/2));
								DataHolder.qs.setY(y-(DataHolder.qssize/2));
								parent.addView(DataHolder.qs,lp);

								
								h.postDelayed(new Runnable() {
			
									@Override
									public void run() {
										
										if(name==null && !DataHolder.stopped){
											pager.setPagingEnabled(false);

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
											h.postDelayed(this, 40);
										else name= null;
										
			
									}
								},1000);
								
							}
						}
						if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
							DataHolder.stopped=true;
							h.removeCallbacksAndMessages(null);
							DataHolder.qs.dispatchTouchEvent(event);
							parent.removeView(DataHolder.qs);
							pager.setPagingEnabled(true);

						}
						
					
						return true;
				}
			});
			*/
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.progress);

			imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
				}
			},new ImageLoadingProgressListener() {
				
				@Override
				public void onProgressUpdate(String imageUri, View view, int current,
						int total) {
					 Toast.makeText(getApplicationContext(), ""+current, Toast.LENGTH_SHORT).show();
					 spinner.setProgress(Math.round(100.0f * current / total));					
				}
			});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}