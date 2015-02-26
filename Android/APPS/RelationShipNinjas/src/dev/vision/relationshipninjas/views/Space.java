package dev.vision.relationshipninjas.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Space extends View{
	
	public Space(Context context) {
		super(context);
		this.context=context;
	}

	public Space(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context=context;
	}
	
	
	public Space(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context=context;
	}

	Context context;

	@SuppressLint("NewApi") @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
        int width = (int) (size.y/2.5);//3


		if(width==0)
			width=200;
        //getLayoutParams().height=width;
    	setMeasuredDimension(size.x, width);
    	//setFitsSystemWindows(true);
	}
	
	
}
