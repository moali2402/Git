package dev.vision.rave.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

    public class SquareLinear extends LinearLayout {

        public SquareLinear(Context context) {
        super(context);


        // TODO Auto-generated constructor stub
    }

    public SquareLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

    }

    public SquareLinear(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);       
    }
    
    @Override 
    public void onSizeChanged(int w, int h, int oldw, int oldh){
    	super.onSizeChanged(h, h, oldw, oldh);
    	android.view.ViewGroup.LayoutParams l =getLayoutParams();
    	l.width=h;
    	setLayoutParams(l);
    }

}