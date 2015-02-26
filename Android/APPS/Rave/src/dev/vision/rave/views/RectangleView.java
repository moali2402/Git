package dev.vision.rave.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

    public class RectangleView extends ImageView {

        public RectangleView(Context context) {
        super(context);


        // TODO Auto-generated constructor stub
    }

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

    }

    public RectangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        //int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();
       // getLayoutParams().height= w;
        setMeasuredDimension(w, (int) (w/1.5));
        //int curSquareDim = Math.min(w,h);

        //if(curSquareDim < squareDim)
        //{
        //    squareDim = curSquareDim;
       // }

        //Log.d("MyApp", "h "+h+"w "+w+"squareDim "+squareDim);

        
        //setMeasuredDimension(squareDim, squareDim);

    }

}