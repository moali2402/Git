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

public class view extends View{
	Context context;
	int pos=-1;
	int color=Color.parseColor("#ff6460");
	
	public view(Context context) {
		super(context);
		this.context=context;
	}

	public view(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context=context;
	}
	
	@SuppressLint("NewApi") @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	public void setPos(int x){
		pos = x;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setColor(Color.parseColor("#838086"));
	
		p.setStrokeWidth(5);
		canvas.drawLine(getWidth()/2, pos == 0? 35 : 0, getWidth()/2, getHeight(), p);
		p=new Paint();
		p.setColor(color);
		canvas.drawCircle(getWidth()/2, 50, 15, p);

	}
}
