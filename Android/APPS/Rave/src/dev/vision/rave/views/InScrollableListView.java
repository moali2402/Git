package dev.vision.rave.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class InScrollableListView extends ListView{

	private boolean scrollIsComputed;
	private int[] mItemOffsetY;
	private boolean enabled;
	private int mHeight;
	private int mItemCount;

	public InScrollableListView(Context context) {
        super(context);
       // setVerticalScrollBarEnabled(false); 
       // setHorizontalScrollBarEnabled(false);
        enabled=true; 
    }

	
	public InScrollableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//setVerticalScrollBarEnabled(false); 
       // setHorizontalScrollBarEnabled(false);
        enabled=true; 

	}

	public InScrollableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//setVerticalScrollBarEnabled(false); 
       // setHorizontalScrollBarEnabled(false);
        enabled=true; 

	}
	
    public int getListHeight() {
		return mHeight;
	}
	
	
    public void computeScrollY() {
		mHeight = 0;
		mItemCount = getAdapter().getCount();
		//if (mItemOffsetY == null) {
			mItemOffsetY = new int[mItemCount];
		//}
		for (int i = 0; i < mItemCount; ++i) {
			View view = getAdapter().getView(i, null, this);
			view.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			mItemOffsetY[i] = mHeight;
			mHeight += view.getMeasuredHeight();
			//System.out.println(mHeight);
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
	    if (this.enabled ) {
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
