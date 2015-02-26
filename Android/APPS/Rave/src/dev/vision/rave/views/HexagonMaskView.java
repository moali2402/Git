package dev.vision.rave.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class HexagonMaskView extends ImageView {
	private Path hexagonPath;
	private Path hexagonBorderPath;
	private float radius;
	private float width, height;
	private int maskColor;
	Paint p;
	
	public HexagonMaskView(Context context) {
	    super(context);
	    init();
	}
	
	public HexagonMaskView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init();
	}
	
	public HexagonMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
	    super(context, attrs, defStyleAttr);
	    init();
	}
	
	private void init() {
	
		p = new Paint();
	    p.setAntiAlias(true);
	    p.setStrokeCap(Cap.ROUND);
	    p.setPathEffect(new CornerPathEffect(30));

	    //p.setStrokeJoin(Join.ROUND);
	    p.setColor(Color.WHITE);
	    p.setStyle(Style.STROKE);
	    p.setStrokeWidth(5);
	    //p.setShadowLayer(20, 0, 0, Color.BLACK);
	    hexagonPath = new Path();
	    hexagonBorderPath = new Path();
	    maskColor = 0xFF01FF77;
	}
	
	public void setRadius(float r) {
	    this.radius = r;
	    calculatePath();
	}
	
	public void setMaskColor(int color) {
	    this.maskColor = color;
	    invalidate();
	}
	
	private void calculatePath() {
	    float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
	    float centerX = width / 2;
	    float centerY = height / 2;

	    hexagonPath.reset();
	    hexagonBorderPath.reset();
	    hexagonPath.moveTo(centerX, centerY + radius);
	    hexagonPath.lineTo(centerX - triangleHeight, centerY + radius / 2);
	    hexagonPath.lineTo(centerX - triangleHeight, centerY - radius / 2);
	    hexagonPath.lineTo(centerX, centerY - radius);
	    hexagonPath.lineTo(centerX + triangleHeight, centerY - radius / 2);
	    hexagonPath.lineTo(centerX + triangleHeight, centerY + radius / 2);
	    //hexagonPath.moveTo(centerX, centerY + radius);
	    hexagonPath.close();
	    float radiusBorder = radius - 4f;
	    float triangleBorderHeight = (float) (Math.sqrt(3) * radiusBorder / 2);
	    hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
	    hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY
	            + radiusBorder / 2);
	    hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY
	            - radiusBorder / 2);
	    hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
	    hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY
	            - radiusBorder / 2);
	    hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY
	            + radiusBorder / 2);
	    //hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
	    hexagonBorderPath.close();
	    invalidate();
	}
	@SuppressLint("NewApi") @Override
	public void onDraw(Canvas c) {    
	    //c.clipPath(hexagonPath, Region.Op.INTERSECT);
	    //super.onDraw(c);
	    
	    //c.clipPath(hexagonBorderPath, Region.Op.DIFFERENCE);
	    //c.save();
	    //c.clipPath(hexagonPath, Region.Op.INTERSECT);
	   
	    //p.setShadowLayer(20, 0, 0, Color.BLACK);
	    //c.drawPaint(p);
	    //c.restore();

		//shadow
		//Paint paint = new Paint();
	    //paint.setShadowLayer(20, 0, 0, Color.BLACK);
    	//c.drawPath(hexagonPath,p);
	    c.save();
	    c.clipPath(hexagonBorderPath, Region.Op.INTERSECT);
	    super.onDraw(c);
	    
	    c.restore();
	    c.drawPath(hexagonPath,p);
	   // c.drawLine(width/2, 0, width/2, height, p);
	}
	
	// getting the view size and default radius
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	     int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
	     setMeasuredDimension(size, size);	     
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
	    super.onSizeChanged(w, h, oldw, oldh);



	    height = h;
		width = w;
	    radius = (height / 2);
	    calculatePath();
	}
}