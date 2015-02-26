package dev.vision.rave.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

    public class CoverPic extends ImageView {

    public CoverPic(Context context) {
        super(context);
    }

    public CoverPic(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CoverPic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = this.getMeasuredWidth();

        setMeasuredDimension(w, (int) (w/1.75)-100);
    }

}