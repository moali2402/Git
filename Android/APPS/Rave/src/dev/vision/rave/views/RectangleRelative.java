package dev.vision.rave.views;

import dev.vision.rave.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

    public class RectangleRelative extends RelativeLayout {

        private int borderWidth=50;

		public RectangleRelative(Context context) {
        super(context);


        // TODO Auto-generated constructor stub
    }

    public RectangleRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
    	parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.CircularImage));
    }

    public RectangleRelative(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    	parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.CircularImage));
    }
	 private void parseAttributes(TypedArray a) {
		 borderWidth = (int) a.getDimension(R.styleable.CircularImage_borderWidth,
				 borderWidth);
		 a.recycle();
	 }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int w = this.getMeasuredWidth();
        setMeasuredDimension(w, getMeasuredHeight()+borderWidth);//-50 (int) ((w/1.5)-70)
      
    }

}