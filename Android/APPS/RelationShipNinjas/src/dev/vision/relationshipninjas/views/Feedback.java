package dev.vision.relationshipninjas.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Feedback extends View {
	int value =0;
	public Feedback(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public Feedback(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Feedback(Context context) {
		super(context);
	}
	
	@Override
	public void onDraw(Canvas c){
		super.onDraw(c);
		int gabs = 4;
		int width = getMeasuredWidth();
		float gabSize =2;
		
		float start = 0;
		float boxSize = (width-(gabSize*gabs))/ 5;
		
		Paint p = new Paint();
		p.setAntiAlias(true);

		p.setColor(Color.BLACK);
		c.drawRoundRect(new RectF(0,0,getMeasuredWidth(), getMeasuredHeight()), 10, 10, p);
		
		
		p.setColor(Color.GRAY);

		if(value>0)
			p.setColor(Color.parseColor("#fed9d8"));
		
		c.drawRoundRect(new RectF(start,(int)  0,start+=boxSize, getMeasuredHeight()), 10, 10, p);
		c.drawRect(10, 0, start, getMeasuredHeight(), p);

		p.setColor(Color.GRAY);

		if(value>1)
			p.setColor(Color.parseColor("#ffc0be"));
		
		c.drawRect(start+gabSize, 0, start+=(boxSize+gabSize), getMeasuredHeight(), p);

		p.setColor(Color.GRAY);

		if(value>2)
			p.setColor(Color.parseColor("#ff9794"));
		
		c.drawRect(start+gabSize, 0, start+=(boxSize+gabSize), getMeasuredHeight(), p);

		p.setColor(Color.GRAY);

		if(value>3)
			p.setColor(Color.parseColor("#fe7471"));
		
		c.drawRect(start+gabSize, 0, start+=(boxSize+gabSize), getMeasuredHeight(), p);

		p.setColor(Color.GRAY);

		if(value>4)
			p.setColor(Color.parseColor("#ff6460"));
		
		RectF rectF = new RectF(start+gabSize, 0,start+=(boxSize+gabSize),  getMeasuredHeight());
		c.drawRoundRect(rectF, 10, 10, p);
		c.drawRect(start-(boxSize), 0, start-10, getMeasuredHeight(), p);

	}

	public void setRating(int i) {
		value = i;
		invalidate();
	}

}
//c.drawRect(start+gabSize, 0, start+=(boxSize+gabSize), getMeasuredHeight(), p);
