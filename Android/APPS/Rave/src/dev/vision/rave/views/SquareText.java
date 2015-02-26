package dev.vision.rave.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SquareText extends TextView {
int bH = 80;
	public SquareText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SquareText(Context context) {
		super(context);
	}
	
	public SquareText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    
	    int parentWidth =getMeasuredWidth();
	    int parentHeight = getMeasuredHeight();
	    
	    parentHeight = parentHeight>parentWidth ? parentHeight : parentWidth;
	    parentWidth = parentHeight>parentWidth ? parentHeight : parentWidth;
	    parentHeight = parentHeight<=bH ? parentHeight : bH;
	    
	    setMeasuredDimension(parentWidth, parentHeight);
	    
	    
	}
}
