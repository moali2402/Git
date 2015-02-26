package dev.vision.rave.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class FullHeightImage extends ImageView {


	Context c;


	public FullHeightImage(Context context) {
		super(context);
		c=context;

	}

	public FullHeightImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		c=context;

	}

	
	public FullHeightImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		c=context;
	    
	}
	
	@SuppressLint("DrawAllocation")
	@Override
    public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
	}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    try {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            setMeasuredDimension(0, 0);
        } else {
            float imageSideRatio = (float)drawable.getIntrinsicWidth() / (float)drawable.getIntrinsicHeight();
            float viewSideRatio = (float)MeasureSpec.getSize(widthMeasureSpec) / (float)MeasureSpec.getSize(heightMeasureSpec);
           
                // Image is taller than the display (ratio)
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int width = (int)(height * imageSideRatio);
            setMeasuredDimension(width, height);
            
        }
    } catch (Exception e) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }}
}