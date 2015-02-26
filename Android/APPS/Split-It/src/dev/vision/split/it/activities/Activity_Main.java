/**
 * 
 */
package dev.vision.split.it.activities;

import java.lang.ref.WeakReference;

import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.models.CardBuilder;

import dev.vision.split.it.R;
import dev.vision.split.it.displayingbitmaps.provider.Images;
import dev.vision.split.it.displayingbitmaps.ui.ImageDetailFragment;
import dev.vision.split.it.fragments.Fragment_Login;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Mo
 *
 */
public class Activity_Main extends FragmentActivity {
	private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";

	private ViewPager mPager;
    private LruCache<String, Bitmap> mMemoryCache;
	private ImagePagerAdapter mAdapter;
	String CLIENT_TOKEN_FROM_SERVER;

    public final static Integer[] imageResIds = new Integer[] {
         R.drawable.nexus_w3, R.drawable.nexus_w};


	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		/*
		Braintree braintree = Braintree.getInstance(this, CLIENT_TOKEN_FROM_SERVER);
		braintree.addListener(new Braintree.PaymentMethodNonceListener() {
		  public void onPaymentMethodNonce(String paymentMethodNonce) {
		    // Communicate the nonce to your server
		  }
		});

		CardBuilder cardBuilder = new CardBuilder()
		    .cardNumber("4111111111111111")
		    .expirationDate("09/2018")
		    .cvv("");

		braintree.tokenize(cardBuilder);
		 */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
        
	        
	       
        // Set up ViewPager and backing adapter
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), Images.images.length);
        mPager = (ViewPager) findViewById(R.id.bottom);
        mPager.setAdapter(mAdapter);
        //mPager.setPageMargin((int) getResources().getDimension(R));
        
        
        final LayerDrawable background = (LayerDrawable) ((View) mPager.getParent()).getBackground();
        mPager.setOffscreenPageLimit(background.getNumberOfLayers());

        background.getDrawable(0).setAlpha(0); // this is the lowest drawable
        background.getDrawable(1).setAlpha(0);
        background.getDrawable(2).setAlpha(0);
        background.getDrawable(3).setAlpha(0);
        background.getDrawable(4).setAlpha(255); // this is the upper one 
		final ImageView im = (ImageView) findViewById(R.id.imageView1);

        mPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
	            int index = (Integer) view.getTag();

	                Drawable currentDrawableInLayerDrawable;
	                currentDrawableInLayerDrawable = background.getDrawable(index);
	
	                if(position <= -1 || position >= 1) {
	                    currentDrawableInLayerDrawable.setAlpha(0);
	                } else if( position == 0 ) {
	                    currentDrawableInLayerDrawable.setAlpha(255);
	                } else { 
	                    currentDrawableInLayerDrawable.setAlpha((int)(255 - Math.abs(position*255)));
	                    if(index == 0){
			                im.setScaleX(Math.max(0.5f ,1 - Math.abs(position*1)));
			                im.setScaleY(Math.max(0.5f, 1 - Math.abs(position*1)));			                
			                im.setTranslationY(Math.max(-600, -500 - Math.abs(position*600)));

	                    }
	                }
            }
        });
        


		ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(im, "scaleX", 0.5f);
		ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(im, "scaleY", 0.5f);
        ObjectAnimator translation = ObjectAnimator.ofFloat(im, "translationY", 0,-600);
		ObjectAnimator alpha = ObjectAnimator.ofFloat(mPager, "alpha", 0,1);
		
		alpha.setStartDelay(1300);
        //translation.setDuration(2000);

		AnimatorSet as =new AnimatorSet();
		as.playTogether(scaleDownX,scaleDownY,translation, alpha);
		as.setStartDelay(1500);
		as.setDuration(1500);
		//as.setTarget(im);
		as.start();

	 	
    }
	
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	    private int data = 0;

	    public BitmapWorkerTask(ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	        data = params[0];
	        return decodeSampledBitmapFromResource(getResources(), data, 400, 300);
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
	
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}
	
	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	
	public void loadImage(int resId, ImageView imageView) {
	    final String imageKey = String.valueOf(resId);

	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	    	imageView.setImageBitmap(bitmap);
	    } else {
	    	imageView.setImageResource(0);
	        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
	        task.execute(resId);
	    }
	}
    
    /**
     * The main adapter that backs the ViewPager. A subclass of FragmentStatePagerAdapter as there
     * could be a large number of items in the ViewPager and we don't want to retain them all in
     * memory at once but create/destroy them on the fly.
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;

        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public int getCount() {
            return mSize+1;
        }

        @Override
        public Fragment getItem(int position) {
        	if(position==getCount()-1)
                return Fragment_Login.newInstance();
        	else
                return ImageDetailFragment.newInstance(position);

        }
        
        @Override
        public boolean isViewFromObject(View view, Object object) {
        	 if(object instanceof Fragment_Login){
                 view.setTag(0);
             }else {
            	 int tag = 0;
            	 switch (((ImageDetailFragment) object).getPos()) {
				case 0:
					tag= 4;
					break;
				case 1:
					tag= 3;
					break;
				case 2:
					tag= 2;
					break;
				case 3:
					tag= 1;	
					break;
				}
                 view.setTag(tag);
             }
        	 return super.isViewFromObject(view, object);
        }  
    }


	
	
    
}
