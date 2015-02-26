package uk.co.senab.photoview;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

public class Map extends ImageView {

	float rationX;
	float rationY;

	private Paint paint;
	private Paint paintBorder;
	Context c;
	

	public Map(Context context) {
		super(context);
		c=context;

		setup();
	}

	public Map(Context context, AttributeSet attrs) {
		super(context, attrs);
		c=context;

		setup();
	}

	public Map(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		c=context;
		
		setup();
	}

	@SuppressLint("NewApi")
	private void setup()
	{
		// init paint
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#FF0000"));
		paint.setStrokeWidth(3);
		paint.setStyle(Style.STROKE);
		setLayerType(LAYER_TYPE_HARDWARE, paintBorder);

	}


	

	public void setBorderColor(int borderColor)
	{		
		if(paintBorder != null)
			paintBorder.setColor(borderColor);
		this.invalidate();
	}
	
	Path p = new Path();
	Region r = new Region();
	private float length;
	
	@SuppressLint("DrawAllocation")
	@Override
    public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		boolean first = true;
		length = 0;
		p = new Path();
		for(Point po : path){
			if(first){
				p.moveTo(po.x/rationX +drawLeft, po.y/rationY + drawTop);
				first = false; 
			}else
			p.lineTo(po.x/rationX +drawLeft, po.y/rationY + drawTop);

		}
		
		canvas.drawPath(p, paint);
		
		
		
		
	}
	
	static Point path1= new Point(161, 715);
	static Point path2= new Point(527, 674);
	static Point path3= new Point(604, 696);
	static Point path4= new Point(637, 670);
	static Point path5= new Point(818, 645);
	static Point path6= new Point(1124, 390);
	static ArrayList<Point> path = new ArrayList<Point>();
	static{
		path.add(path1);
		path.add(path2);
		path.add(path3);
		path.add(path4);
		path.add(path5);
		path.add(path6);

	}
	private float drawLeft;
	private float drawTop;
	private float  drawWidth;
	private float drawHeight;

	public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
	
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);		
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int wi = getDrawable().getIntrinsicWidth();
		int hi = getDrawable().getIntrinsicHeight();
		drawWidth = getMeasuredWidth();
		drawHeight = getMeasuredHeight();
		
		double bitmapRatio  = ((double) wi )/hi;
		double imageViewRatio  = ((double) getMeasuredWidth())/ getMeasuredHeight();
		drawLeft = 0;
		drawTop = 0;
		
		if(bitmapRatio > imageViewRatio)
		{
			drawHeight = (float) ((imageViewRatio/bitmapRatio) * getMeasuredHeight());
			drawTop = ((getMeasuredHeight() - drawHeight)/2);
		}else{
			
			drawWidth = (float) ((bitmapRatio/imageViewRatio) * getMeasuredWidth());
			drawLeft = (getMeasuredWidth() - drawWidth)/2;
		}
		
		rationX = (float) wi / (float) drawWidth;
		rationY = (float) hi / (float) drawHeight;
	}
	
	public void calculateLength(){
		PathMeasure pm = new PathMeasure(p, false);
		int pathCont=0;
		while(pm.nextContour()){
		  pathCont++;
		  length += pm.getLength();
		}
	}
	
}