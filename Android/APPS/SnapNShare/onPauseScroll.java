package com.dev.vision.selfie.user;

public class onPauseScroll extends PauseOnScrollListener{

	int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	public onPauseScroll(ImageLoader imageLoader, boolean pauseOnScroll,
			boolean pauseOnFling) {
		super(imageLoader, pauseOnScroll, pauseOnFling);
	}
	
	 @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState) {
	        // Store the state to avoid re-laying out in IDLE state.
	        mScrollState = scrollState;
	        super.onScrollStateChanged(view, scrollState);

	    }
	
	@SuppressLint("NewApi")
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {			
			//super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		
		
		
			int stop = getActionBarHeight();
			/*
			if(imageLoader.isInited()){
	            if (firstVisibleItem == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
	                imageLoader.stop();
	                Log.i("Stop", "Stoped");
	              } else {
	                  imageLoader.resume();
	                Log.i("Resume", "Resume");
	              }
	            }
			*/
			if (mListView.scrollYIsComputed()) {
				mScrollY = mListView.getComputedScrollY();
			}
			
			//int rawY = -mScrollY;
			int rawY = mPlaceHolder.getTop()
					- Math.min(
							mCachedVerticalScrollRange
									- mListView.getHeight()-42, mScrollY);

			

		
			/*
			if(rawY<0)
				translationY = 0;
			else {
				translationY = rawY;
				done=false;
			}
			*/

			//}
			/*
			//ActionBar Transparency
	        int headerHeight = viewH - stop - stop;
	        float ratio = (float) Math.min(Math.max(mScrollY, 0), headerHeight) / headerHeight;
	        int newAlpha = (int) (ratio * 255);
	       // if(background!=null)
	        	//background.setAlpha(newAlpha);
	        
	        
	        // Nothing to do in IDLE state.
	        if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE)
	            return;
	        
	        for (int i=0; i < visibleItemCount; i++) {
	            View listItem = view.getChildAt(i);
	            if (listItem == null)
	                break;
	            
	            RelativeLayout title = (RelativeLayout) listItem.findViewById(R.id.above);
	            if(title!=null)
	            {	
	            			
	            float topMargin = 0;
	          //  if (i == 0) {
	                int top = listItem.getTop();
	                int height = listItem.getHeight();
	 
	                // if top is negative, the list item has scrolled up.
	                // if the title view falls within the container's visible portion,
	                //     set the top margin to be the (inverse) scrolled amount of the container.
	                // else
	                //     set the top margin to be the difference between the heights.
	                if(mState == STATE_RETURNING){
	                	if (top < translationY+viewH+stop){
		                    topMargin = title.getHeight() < (top + height-(translationY+viewH)-stop) ? -(top-(translationY+viewH)-stop) : (height - title.getHeight());
	                	}
	                }
	                else{
	                	if (top < stop) //viewH+stop		//down here -viewH ?  -viewH : nothing
		                    topMargin = title.getHeight() < (top + height-stop) ? -(top-stop) : (height - title.getHeight());
	                }
		                    //  }
	 
	       
	            // request Android to layout again.
	            title.setTranslationY(topMargin);
	            } 
	            
	        }
	   	 
		 */
		}

	
}
