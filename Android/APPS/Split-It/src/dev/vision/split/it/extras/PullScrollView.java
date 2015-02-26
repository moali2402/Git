package dev.vision.split.it.extras;

import dev.vision.split.it.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * ???ScrollView
 *
 * @author markmjw
 * @date 2013-09-13
 */
public class PullScrollView extends ScrollView {
    /** ????,???????. */
    private static final float SCROLL_RATIO = 0.5f;

    /** ????????. */
    private static final int TURN_DISTANCE = 100;

    /** ??view. */
    private View mHeader;

    /** ??view??. */
    private int mHeaderHeight;

    /** ??view????. */
    private int mHeaderVisibleHeight;

    /** ScrollView?content view. */
    private View mContentView;

    /** ScrollView?content view??. */
    private Rect mContentRect = new Rect();

    /** ?????Y??. */
    private float mTouchDownY;

    /** ????ScrollView???. */
    private boolean mEnableTouch = false;

    /** ??????. */
    private boolean isMoving = false;

    /** ?????????. */
    private boolean isTop = false;

    /** ???????????. */
    private int mInitTop, mInitBottom;

    /** ????????????. */
    private int mCurrentTop, mCurrentBottom;

    /** ?????????. */
    private OnTurnListener mOnTurnListener;

    private enum State {
        /**??*/
        UP,
        /**??*/
        DOWN,
        /**??*/
        NORMAL
    }

    /** ??. */
    private State mState = State.NORMAL;

	private boolean reached;

	private int oCurrentTop;

	private int oCurrentBottom;

	private View sheader;

	private ListView lv;

	private float startY;

    public PullScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // set scroll mode
        setOverScrollMode(OVER_SCROLL_NEVER);

        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullScrollView);

            if (ta != null) {
                mHeaderHeight = (int) ta.getDimension(R.styleable.PullScrollView_headerHeight, -1);
                mHeaderVisibleHeight = (int) ta.getDimension(R.styleable
                        .PullScrollView_headerVisibleHeight, -1);
                ta.recycle();
            }
        }
    }

    /**
     * ??Header
     *
     * @param view
     */
    public void setHeader(View view) {
        mHeader = view;
        sheader = findViewById(R.id.scroll_view_head);
    }

    /**
     * ???????????
     *
     * @param turnListener
     */
    public void setOnTurnListener(OnTurnListener turnListener) {
        mOnTurnListener = turnListener;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
    }

    @Override
    @SuppressLint("NewApi") 
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        sheader.setTranslationY(t);
        
        if (getScrollY() == 0) {
            isTop = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchDownY = ev.getY();
            mCurrentTop = mInitTop = mHeader.getTop();
            mCurrentBottom = mInitBottom = mHeader.getBottom();
        }
       
        return super.onInterceptTouchEvent(ev);

   }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView != null) {
            doTouchEvent(ev);
        }

        // ?????????.
        return mEnableTouch || super.onTouchEvent(ev);
    }

    /**
     * ??????
     *
     * @param event
     */
    private void doTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
            	if(!reached)
                doActionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                // ????
                if (isNeedAnimation()) {
                    rollBackAnimation();
                }

                if (getScrollY() == 0) {
                    mState = State.NORMAL;
                }

                isMoving = false;
                mEnableTouch = false;
                break;

            default:
                break;
        }
    }

    /**
     * ??????
     *
     * @param event
     */
    private void doActionMove(MotionEvent event) {
    	
        // ???????????????????????????????????????????
        if (getScrollY() == 0) {
            mState = State.NORMAL;

            // ??????????????Touch down??????Touch????
            if (isTop) {
                isTop = false;
                mTouchDownY = event.getY();
            }
        }

        float deltaY = event.getY() - mTouchDownY;

        // ????Touch????????UP OR DOWN
        if (deltaY < 0 && mState == State.NORMAL) {
            mState = State.UP;
        } else if (deltaY > 0 && mState == State.NORMAL) {
            mState = State.DOWN;
        }

        if (mState == State.UP) {
            deltaY = deltaY < 0 ? deltaY : 0;

            isMoving = false;
            mEnableTouch = false;

        } else if (mState == State.DOWN) {
            if (getScrollY() <= deltaY) {
                mEnableTouch = true;
                isMoving = true;
            }
            deltaY = deltaY < 0 ? 0 : deltaY;
        }

        if (isMoving) {
            // ???content view??
            if (mContentRect.isEmpty()) {
                // ?????????
                mContentRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(),
                        mContentView.getBottom());
            }

            // ??header????(???????*????*0.5)
            float headerMoveHeight = deltaY * 0.5f * SCROLL_RATIO;
            mCurrentTop = (int) (mInitTop + headerMoveHeight);
            mCurrentBottom = (int) (mInitBottom + headerMoveHeight);

            // ??content????(???????*????)
            float contentMoveHeight = deltaY * SCROLL_RATIO;

            // ??content??????????header????
            int headerBottom = mCurrentBottom - mHeaderVisibleHeight;
            int top = (int) (mContentRect.top + contentMoveHeight);
            int bottom = (int) (mContentRect.bottom + contentMoveHeight);

            if (top + getStatusBarHeight()  <= headerBottom  ) {
                // ??content view
            	oCurrentTop = mCurrentTop;
            	oCurrentBottom = mCurrentBottom;
                mContentView.layout(mContentRect.left, top, mContentRect.right, bottom);

                // ??header view
                mHeader.layout(mHeader.getLeft(), mCurrentTop, mHeader.getRight(), mCurrentBottom);
            }else{
            	mCurrentTop = oCurrentTop;
            	mCurrentBottom = oCurrentBottom;
            	reached=true;

            }
        }

    }
    
    public int getStatusBarHeight() {
    	  int result = 0;
    	  int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    	  if (resourceId > 0) {
    	      result = getResources().getDimensionPixelSize(resourceId);
    	  }
    	  return result;
    	}

    private void rollBackAnimation() {
        TranslateAnimation tranAnim = new TranslateAnimation(0, 0,
                Math.abs(mInitTop - mCurrentTop), 0);
        tranAnim.setDuration(200);
        mHeader.startAnimation(tranAnim);

        mHeader.layout(mHeader.getLeft(), mInitTop, mHeader.getRight(), mInitBottom);

        // ??????
        TranslateAnimation innerAnim = new TranslateAnimation(0, 0, mContentView.getTop(), mContentRect.top);
        innerAnim.setDuration(200);
        mContentView.startAnimation(innerAnim);
        mContentView.layout(mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom);

        mContentRect.setEmpty();

        // ?????
        if (mCurrentTop > mInitTop + TURN_DISTANCE && mOnTurnListener != null){
            mOnTurnListener.onTurn();
        }
    	reached=false;

    }

    /**
     * ????????
     */
    private boolean isNeedAnimation() {
        return !mContentRect.isEmpty() && isMoving;
    }

    /**
     * ???????
     *
     * @author markmjw
     */
    public interface OnTurnListener {
        /**
         * ??????
         */
        public void onTurn();
    }

	public void setList(ListView lv) {
		this.lv = lv;
	}
}