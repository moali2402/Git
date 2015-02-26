package com.applidium.headerlistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class InternalListView extends ListView {

    private boolean enabled;
	private int mItemCount;
	private int mItemOffsetY[];
	private boolean scrollIsComputed = false;
	private int mHeight;
	
  
    
	public InternalListView(Context context) {
        super(context);
        enabled=true; 
    }

	
	public InternalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        enabled=true; 

	}

	public InternalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        enabled=true; 

	}

	
	
    @Override
    protected int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }      
    
    public int getListHeight() {
		return mHeight;
	}
	
	
	public void computeScrollY() {
		mHeight = 0;
		mItemCount = getAdapter().getCount();
		if (mItemOffsetY == null) {
			mItemOffsetY = new int[mItemCount];
		}
		for (int i = 0; i < mItemCount; ++i) {
			View view = getAdapter().getView(i, null, this);
			view.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			mItemOffsetY[i] = mHeight;
			mHeight += view.getMeasuredHeight();
			System.out.println(mHeight);
		}
		scrollIsComputed = true;
	}

	public boolean scrollYIsComputed() {
		return scrollIsComputed;
	}

	public int getComputedScrollY() {
		int pos, nScrollY, nItemY;
		View view = null;
		pos = getFirstVisiblePosition();
		view = getChildAt(0);
		nItemY = view.getTop();
		nScrollY = mItemOffsetY[pos] - nItemY;
		return nScrollY;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (this.enabled) {
	        return super.onTouchEvent(event);
	    }
	
	    return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
	    if (this.enabled) {
	        return super.onInterceptTouchEvent(event);
	    }
	
	    return false;
	}
	
	public void setPagingEnabled(boolean enabled) {
	    this.enabled = enabled;
	}
}