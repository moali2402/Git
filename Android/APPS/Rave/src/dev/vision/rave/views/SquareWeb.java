package dev.vision.rave.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

    public class SquareWeb extends ImageView {
    	Context c;
    public SquareWeb(Context context) {
        super(context);
        c=context;
    }

    public SquareWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
        c=context;

    }

    public SquareWeb(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        c=context;
    }


    @SuppressLint("NewApi")
	@Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		int height = size.y/4;
		if(height==0)
			height=heightMeasureSpec;

        //int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();
       // getLayoutParams().height= w;
        setMeasuredDimension(w, height);
        //int curSquareDim = Math.min(w,h);

        //if(curSquareDim < squareDim)
        //{
        //    squareDim = curSquareDim;
       // }

        //Log.d("MyApp", "h "+h+"w "+w+"squareDim "+squareDim);

        
        //setMeasuredDimension(squareDim, squareDim);

    }
}