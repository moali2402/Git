package dev.vision.rave.listeners;

import dev.vision.rave.listeners.InfiniteScrollListAdapter;
import dev.vision.rave.listeners.InfiniteScrollListPageListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ScrollableListView extends ListView implements InfiniteScrollListPageListener{

	private boolean scrollIsComputed;
	private int[] mItemOffsetY;
	private boolean enabled;
	private int mHeight;
	private int mItemCount;

	public static final String TAG = ScrollableListView.class.getSimpleName();
	// Two conditions allowing loading to happen: either the list view scrolls to the top or bottom
	public static enum LoadingMode {SCROLL_TO_TOP, SCROLL_TO_BOTTOM};
	// Decides where the ending position: either scrolls up to the start of the list, scrolls down to the 
	// end of the list or where remains where it was
	public static enum StopPosition {START_OF_LIST, END_OF_LIST, REMAIN_UNCHANGED};

	private View loadingView;
	private LoadingMode loadingMode = LoadingMode.SCROLL_TO_BOTTOM; 
	private StopPosition stopPosition = StopPosition.REMAIN_UNCHANGED;

	// A flag to prevent loading header or footer more than once
	private boolean loadingViewVisible = false;
	
	public ScrollableListView(Context context) {
        super(context);
       // setVerticalScrollBarEnabled(false); 
       // setHorizontalScrollBarEnabled(false);
        enabled=true; 
    }

	
	public ScrollableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//setVerticalScrollBarEnabled(false); 
       // setHorizontalScrollBarEnabled(false);
        enabled=true; 

	}

	public ScrollableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//setVerticalScrollBarEnabled(false); 
       // setHorizontalScrollBarEnabled(false);
        enabled=true; 

	}
	
    public int getListHeight() {
		return mHeight;
	}
	
    public void setLoadingView(View loadingView) {
		this.loadingView = loadingView;
	}

    public void setLoadingMode(LoadingMode loadingMode) {
    	this.loadingMode = loadingMode;
    }
    
    public void setStopPosition(StopPosition stopPosition) {
		this.stopPosition = stopPosition;
	}

    private void addLoadingView(ListView listView, View loadingView) {
    	if (listView == null || loadingView == null) {
    		return;
    	}
    	// Avoid overlapping the header or footer
    	if (!loadingViewVisible) {
			if (loadingMode == LoadingMode.SCROLL_TO_TOP) {
				// Add loading view to list view header when scroll up to load
				listView.addHeaderView(loadingView);
			} else {
				// Add loading view to list view footer when scroll down to load
				listView.addFooterView(loadingView);
			}
			loadingViewVisible = true;
    	}
    }

    private void removeLoadingView(ListView listView, View loadingView) {
    	if (listView == null || loadingView == null) {
    		return;
    	}
    	// Remove header or footer depending on the loading mode
    	if (loadingViewVisible) {
	    	if (loadingMode == LoadingMode.SCROLL_TO_TOP) {
				listView.removeHeaderView(loadingView);
			} else {
				listView.removeFooterView(loadingView);
			}
	    	loadingViewVisible = false;
    	}
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
    	// Force the list view to accept its own type of adapter
    	if (!(adapter instanceof InfiniteScrollListAdapter)) {
    		throw new IllegalArgumentException(InfiniteScrollListAdapter.class.getSimpleName() + " expected");
    	}
    	// Pass information to adaptor
    	InfiniteScrollListAdapter infiniteListAdapter = (InfiniteScrollListAdapter) adapter;
    	infiniteListAdapter.setLoadingMode(loadingMode);
    	infiniteListAdapter.setStopPosition(stopPosition);
    	infiniteListAdapter.setInfiniteListPageListener(this);
		this.setOnScrollListener(infiniteListAdapter);
		// Workaround to keep spaces for header and footer
		//View dummy = new View(getContext());
		addLoadingView(ScrollableListView.super, loadingView);
    	super.setAdapter(adapter);
    	removeLoadingView(ScrollableListView.super, loadingView);
    }

	@Override
	public void endOfList() {
		// Remove loading view when there is no more to load
		removeLoadingView(this, loadingView);
	}

	@Override
	public void hasMore() {
		// Display loading view when there might be more to load
		addLoadingView(ScrollableListView.this, loadingView);
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
