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
import android.view.WindowManager;

public class Custom_viewpager extends ViewPager{
	Context context;
	public Custom_viewpager(Context context) {
		super(context);
		this.context=context;
	}

	public Custom_viewpager(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context=context;
	}
	
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
        getLayoutParams().height=width;
    	setMeasuredDimension(size.x, width);
    	//setFitsSystemWindows(true);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint p = new Paint();
		Matrix trans = new Matrix();
		trans.setRotate(180);
		Shader shader = new LinearGradient(0, getHeight(), 0, (float) (getHeight()/1.5), Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
		//shader.setLocalMatrix(trans);

		p.setShader(shader);
		canvas.drawRect(0,  (float) (getHeight()/1.5), getWidth()*2, getHeight(), p);
	}
}
