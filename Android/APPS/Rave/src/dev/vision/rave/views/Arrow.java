package dev.vision.rave.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class Arrow extends View{

	private int width;
	private int height;

	public Arrow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	public Arrow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public Arrow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas){
	    Paint p = new Paint();
	    p.setColor(Color.parseColor("#ffffff"));
	    p.setStyle(Style.STROKE);
		p.setAntiAlias(true);
		p.setStrokeWidth(5);
		

        Path path2 = new Path();
  //     path2.addCircle(getWidth()/2, getHeight()/2, circleCenter, Direction.CCW);
        path2.moveTo(0, 0);
        path2.lineTo(getMeasuredWidth(), 0);
        path2.lineTo(getWidth(), getHeight());
        path2.close();
        

        canvas.drawPath(path2, p);
        super.onDraw(canvas);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		
	}
}
