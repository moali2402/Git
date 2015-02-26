package dev.vision.rave.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path.Direction;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.TextView;

public class Chat_textView extends TextView {

	public Chat_textView(Context context) {
		super(context);
	}
	
	public Chat_textView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public Chat_textView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas){

		float radius = 75;
		final RectF oval = new RectF();
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setAntiAlias(true);
		Point point = new Point(75, 150);
		Point p1 = new Point(75, 75);

		oval.set(p1.x - radius, p1.y - radius, p1.x + radius, p1.y+ radius);
		Path myPath = new Path();
		myPath.addRect(new RectF(0, 0, 75, 150),Direction.CW);
		int startAngle = (int) (180 / Math.PI * Math.atan2(point.y - p1.y, point.x - p1.x));
		myPath.moveTo(75, 0);
		myPath.arcTo(oval, startAngle, -(float) 180, true);
		canvas.clipPath(myPath, Region.Op.DIFFERENCE);
		canvas.drawColor(Color.RED);
		super.onDraw(canvas);

	}
}
