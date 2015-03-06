package dev.vision.voom.views;

import dev.vision.voom.classes.STATUS;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HexContactStatusIcon extends ImageView {
	private Path hexagonPath;
	private Path hexagonBorderPath;
	private float radius;
	private float width, height;
	private int maskColor;
	Paint p;
	private int status = Color.WHITE;
	private Paint borderColor;
	private Paint outerColor;

	
	public HexContactStatusIcon(Context context) {
	    super(context);
	    init();
	}
	
	public HexContactStatusIcon(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init();
	}
	
	public HexContactStatusIcon(Context context, AttributeSet attrs, int defStyleAttr) {
	    super(context, attrs, defStyleAttr);
	    init();
	}
	
	private void init() {
	
		p = new Paint();
	    p.setAntiAlias(true);
	    p.setStrokeCap(Cap.ROUND);
	    //p.setStrokeJoin(Join.ROUND);
	    p.setColor(status);
	    p.setStyle(Style.STROKE);
	    p.setStrokeWidth(4);
	    //p.setShadowLayer(20, 0, 0, Color.BLACK);
	    p.setPathEffect(new CornerPathEffect(10));
	    hexagonPath = new Path();
	    hexagonBorderPath = new Path();
	    maskColor = 0xFF01FF77;
	    
	    

		borderColor = new Paint();
		borderColor.setAntiAlias(true);
		borderColor.setStrokeCap(Cap.ROUND);
	    //p.setStrokeJoin(Join.ROUND);
		borderColor.setColor(Color.WHITE);
		borderColor.setStyle(Style.STROKE);
		borderColor.setStrokeWidth(2);
	    //p.setShadowLayer(20, 0, 0, Color.BLACK);
		borderColor.setPathEffect(new CornerPathEffect(10));
		

		outerColor = new Paint();
		outerColor.setAntiAlias(true);
		outerColor.setStrokeCap(Cap.ROUND);
	    //p.setStrokeJoin(Join.ROUND);
		outerColor.setColor(Color.DKGRAY);
		outerColor.setStyle(Style.STROKE);
		outerColor.setStrokeWidth(5);
	    //p.setShadowLayer(20, 0, 0, Color.BLACK);
		outerColor.setPathEffect(new CornerPathEffect(10));
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
	    float radiusBorder = radius - 2;
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
	    p.setColor(status);

	    c.save();
	    c.clipPath(hexagonBorderPath, Region.Op.INTERSECT);
	    super.onDraw(c);
	    
	    c.restore();

	    c.drawPath(hexagonPath,outerColor);
	    
	    c.drawPath(hexagonPath,p);

	    c.drawPath(hexagonBorderPath,borderColor);

	    

	}
	
	public void ChangeStatus(STATUS s){
		status = s.COLOR;
		postInvalidate();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        height = getMeasuredHeight();
	    setMeasuredDimension((int)height,(int) height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
	

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
	    super.onSizeChanged(w, h, oldw, oldh);

		width = getMeasuredWidth();
	    height = getMeasuredHeight();
	    radius = (height / 2);
	    calculatePath();
	}
	
}