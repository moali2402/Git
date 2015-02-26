package dev.vision.relationshipninjas.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class UnderLinedLinear extends LinearLayout{

	private int index = 0;

	public UnderLinedLinear(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public UnderLinedLinear(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("NewApi") 
	public UnderLinedLinear(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("DrawAllocation") @Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
	    float bwidth = ((float)getMeasuredWidth())/4f;
		float width = ((float)index)*bwidth;
		if(index != 4) width-=10;
		int h = getMeasuredHeight();
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.parseColor("#ff8380"));
		canvas.drawRect(0, h-10, width, h, p);
	}

	public void setSelectedItem(int i){
		index = i+2;
		invalidate();
	}
}
