package com.flavienlaurent.notboringactionbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

    public class SquareRelative extends RelativeLayout {

	public SquareRelative(Context context) {
        super(context);
    }

    public SquareRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelative(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight()/2, MeasureSpec.EXACTLY));

    }

}