package com.flavienlaurent.notboringactionbar;

import java.util.ArrayList;
import java.util.Random;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kmshack.newsstand.R;

/**
 * Created by f.laurent on 21/11/13.
 */
public class KenBurnsView extends FrameLayout {

    private static final String TAG = "KenBurnsView";

    private final Handler mHandler;
    private int[] mResourceIds;
    private Bitmap[] mBitmaps;

    private ImageView[] mImageViews;
    private int mActiveImageIndex = -1;

    private final Random random = new Random();
    private int mSwapMs = 10000;
    private int mFadeInOutMs = 2000;

    private float maxScaleFactor = 1.5F;
    private float minScaleFactor = 1.2F;
	ArrayList<Drawable> da = new ArrayList<Drawable>();
	ArrayList<Drawable> darray = new ArrayList<Drawable>();

    private Runnable mSwapImageRunnable = new Runnable() {

		@Override
        public void run() {
            swapImageR();	
			
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs*2);
        }
    };

	private Drawable[] mDrawables;

    public KenBurnsView(Context context) {
        this(context, null);
    }

    public KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
    }

    public void setResourceIds(int... resourceIds) {
        mResourceIds = resourceIds;
        fillImageViews();
    }

    private void swapImageR() {
        Log.d(TAG, "swapImage active=" + mActiveImageIndex);
        if(mActiveImageIndex == -1) {
            mActiveImageIndex = 1;
            animate(mImageViews[mActiveImageIndex]);
            return;
        }

        int inactiveIndex = mActiveImageIndex;
        mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;
        Log.d(TAG, "new active=" + mActiveImageIndex);

        final ImageView activeImageView = mImageViews[mActiveImageIndex];
        activeImageView.setAlpha(0.0f);
        ImageView inactiveImageView = mImageViews[inactiveIndex];
     
		Random rnd = new Random();
		int red= rnd.nextInt(256), green= rnd.nextInt(256), blue = rnd.nextInt(256);
        int color = Color.argb(120,red,green,blue);
        int colorb = Color.argb(255,red,green,blue);

        activeImageView.setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
        setBackgroundColor(colorb);
        animate(activeImageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mFadeInOutMs);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
                ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f)
        );
        animatorSet.start();
    }
    
    
	 


	Bitmap blurfast(Bitmap bmp, int radius) {
	int w = bmp.getWidth();
	int h = bmp.getHeight();
	int[] pix = new int[w * h];
	
	bmp.getPixels(pix, 0, w, 0, 0, w, h);
	
	for(int r = radius; r >= 1; r /= 2) {
	for(int i = r; i < h - r; i++) {
	  for(int j = r; j < w - r; j++) {
	    int tl = pix[(i - r) * w + j - r];
	    int tr = pix[(i - r) * w + j + r];
	    int tc = pix[(i - r) * w + j];
	    int bl = pix[(i + r) * w + j - r];
	    int br = pix[(i + r) * w + j + r];
	    int bc = pix[(i + r) * w + j];
	    int cl = pix[i * w + j - r];
	    int cr = pix[i * w + j + r];
	
	    pix[(i * w) + j] = 0xFF000000 |
	        (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) + (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3) & 0xFF |
	        (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00) + (br & 0xFF00) + (bc & 0xFF00) + (cl & 0xFF00) + (cr & 0xFF00)) >> 3) & 0xFF00 |
	        (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) + (bl & 0xFF0000) + (br & 0xFF0000) + (bc & 0xFF0000) + (cl & 0xFF0000) + (cr & 0xFF0000)) >> 3) & 0xFF0000;
	  }
	}
	}
	bmp.setPixels(pix, 0, w, 0, 0, w, h);
	return bmp;
	}

    
    private void swapImageB() {
        Log.d(TAG, "swapImage active=" + mActiveImageIndex);
        mActiveImageIndex = 0;
        animate(mImageViews[mActiveImageIndex]);
      
    }

    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
       
    	view.setScaleX(fromScale);
        view.setScaleY(fromScale);
        view.setTranslationX(fromTranslationX);
        view.setTranslationY(fromTranslationY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mFadeInOutMs);
        
        ViewPropertyAnimator propertyAnimator = view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
        propertyAnimator.start();
        //Log.d(TAG, "starting Ken Burns animation " + propertyAnimator);
       
    }

    private float pickScale() {
        return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor - this.minScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
    }

    public void animate(View view) {
        float fromScale = pickScale();
        float toScale = pickScale();
        float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
        float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
        float toTranslationX = pickTranslation(view.getWidth(), toScale);
        float toTranslationY = pickTranslation(view.getHeight(), toScale);
        start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startKenBurnsAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mSwapImageRunnable);
    }

    private void startKenBurnsAnimation() {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
		        mHandler.post(mSwapImageRunnable);
				
			}
		}).start();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.view_kenburns, this);
        
        mImageViews = new ImageView[2];
        mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
        mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
    }

    private void fillImageViews() {
        for (int i = 0; i < mImageViews.length; i++) {
        	Drawable d = getResources().getDrawable(mResourceIds[i]);
    		
        	Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(), Config.RGB_565);
            Canvas canvas = new Canvas(bitmap); 
            d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            d.draw(canvas);
           
            mImageViews[i].setImageBitmap(blurfast(bitmap, 8));
        }
    }
    
    private void fillImageViewsBitmaps() {
        for (int i = 0; i < mImageViews.length; i++) {
            mImageViews[i].setImageBitmap(mBitmaps[i]);
        }
    }

    void CoverLoad(ArrayList<Drawable> darray){
		final TransitionDrawable td = new TransitionDrawable(darray.toArray(new Drawable[0]));
		td.setCrossFadeEnabled(true);

		//falsee=0;
		mImageViews[0].setImageDrawable(td);
		
		
		
		animate(mImageViews[0]);

		td.startTransition(mFadeInOutMs);
	}
    
	public void setImageBitmaps(Bitmap... bitmaps) {
		mBitmaps = bitmaps;
		fillImageViewsBitmaps();
	}

	public void setImageDrawable(Drawable... d) {
		for(Drawable dd : d){
			Random rnd = new Random();
            int color = Color.argb(120, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
			dd.setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
			darray.add(dd);
			da.add(dd);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		Paint p = new Paint();
		//Matrix trans = new Matrix();
		//trans.setRotate(180);
		Shader shader = new LinearGradient(0, getHeight(), 0, (float) (getHeight()/1.5), Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
		//shader.setLocalMatrix(trans);

		p.setShader(shader);
		canvas.drawRect(0,  (float) (getHeight()/1.5), getWidth()*2, getHeight(), p);
	
	}
}
