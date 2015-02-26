package dev.vision.rave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class NewsAnimationLayout extends RelativeLayout
{

public NewsAnimationLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    // TODO Auto-generated constructor stub
}


public NewsAnimationLayout(Context context, AttributeSet attrs,
        int defStyle) {
    super(context, attrs, defStyle);
    // TODO Auto-generated constructor stub
}


public NewsAnimationLayout(Context context) {
    super(context);
    // TODO Auto-generated constructor stub
}

    @Override
    protected void onDraw(Canvas canvas) {
    	
    	
       //#AE87C9
    	/*
    	 * float radius = 200;
    	final RectF oval = new RectF();
    	
    	//TopLeft Point 1
    	Point topLeft1 = new Point();
    	topLeft1.x = (int) radius+10;
    	topLeft1.y = (int) getHeight()/5;
    	
    	//TopLeft Point 2
    	Point topLeft2 = new Point();
    	topLeft2.x = (int) radius;
    	topLeft2.y = (int) getHeight()/4;
    	
    	//TopRight Point 1
    	Point topRight1 = new Point();
    	topRight1.x = (int) getWidth();
    	topRight1.y = (int) radius;
    	
    	//TopRight Point 2
    	Point topRight2 = new Point();
    	topRight2.x = (int) radius;
    	topRight2.y = (int) radius;
    	 	
    	
    	float start_Angle = (float) (180 / Math.PI * Math.atan2(topLeft2.y - topLeft1.y, topLeft2.x - topLeft1.x));

    	oval.set(topLeft2.x, topLeft2.y, topLeft1.x, topLeft1.y);
    	//Path myPath = new Path();
    	//myPath.arcTo(oval, start_Angle, -(float) 360, true);

    	Paint p = new Paint();
    	p.setStrokeWidth(5f);
    	p.setColor(Color.BLACK);
    	//canvas.drawPath(myPath, p);
    	canvas.drawArc(oval, 200, 270, false, p);
    	 */
    	Paint p = new Paint();
        RectF rectF = new RectF(50, 20, 100, 80);
        p.setColor(Color.BLACK);
        canvas.drawArc (rectF, 90, 45, true, p);
       // super.onDraw(canvas);

    }

 };