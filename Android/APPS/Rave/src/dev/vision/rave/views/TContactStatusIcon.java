package dev.vision.rave.views;

import dev.vision.rave.R;
import dev.vision.rave.R.color;
import dev.vision.rave.user.Status;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TContactStatusIcon extends ImageView {

	private int borderWidth = 4;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;
	Context c;
	private boolean USE_VIGNETTE;
	private int status = Color.GRAY;
	

	public TContactStatusIcon(Context context) {
		super(context);
		c=context;

		setup();
	}

	public TContactStatusIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		c=context;
		parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.CircularImage));
		setup();
	}

	public TContactStatusIcon(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		c=context;
		 parseAttributes(context.obtainStyledAttributes(attrs,
	                R.styleable.CircularImage));
		setup();
	}

	@SuppressLint("NewApi")
	private void setup()
	{
		// init paint
		paint = new Paint();
		paint.setAntiAlias(true);

		paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);        
		paintBorder.setStyle(Paint.Style.STROKE);
		//paintBorder.setShadowLayer(2, 0, 0, Color.LTGRAY);
		if(!isInEditMode())
		setBorderColor(c.getResources().getColor(color.ProfileBack));
		paintBorder.setStrokeWidth(9f);//change
		paintBorder.setAntiAlias(true);	
		setLayerType(LAYER_TYPE_HARDWARE, paintBorder);
		ChangeStatus(Status.BUSY);

	}

	 private void parseAttributes(TypedArray a) {
		 borderWidth = (int) a.getDimension(R.styleable.CircularImage_borderWidth,
				 borderWidth);
		 a.recycle();
	 }
	
	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		this.invalidate();
	}

	public void setBorderColor(int borderColor)
	{		
		if(paintBorder != null)
			paintBorder.setColor(borderColor);
		this.invalidate();
	}

	private void loadBitmap()
	{
		BitmapDrawable bitmapDrawable;
		Drawable d = this.getDrawable();
		if(d!=null){
			if(d instanceof BitmapDrawable) {
				bitmapDrawable = (BitmapDrawable) this.getDrawable();
				if(bitmapDrawable != null)
					image = bitmapDrawable.getBitmap();
			}else{
				 Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(), Config.ARGB_8888);
			     Canvas canvas = new Canvas(bitmap); 
			     d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			     d.draw(canvas);
			     image = bitmap;
			}
		}
	}

	
	@SuppressLint("DrawAllocation")
	@Override
    public void onDraw(Canvas canvas)
	{
		//load the bitmap
		loadBitmap();

		// init shader
		if(image !=null)
		{			
			float oriWidth = image.getWidth();
			float oriHeight = image.getHeight();
		    int circleCenter = viewWidth / 2;

		   
		    float aspectRatioHie = oriHeight/oriWidth;  
		    
		    float aspectRatioWid = oriWidth/oriHeight;
		    
		    Bitmap bmp = Bitmap.createScaledBitmap(image, oriWidth> oriHeight ? (int) ((viewHeight)*aspectRatioWid) : viewWidth, oriWidth> oriHeight ? viewHeight : (int) (viewWidth*aspectRatioHie), false);
		    bmp=  Bitmap.createBitmap(bmp, bmp.getWidth() > viewWidth ? (bmp.getWidth()/2)-(viewWidth/2) : 0 ,bmp.getHeight() > viewHeight ? (bmp.getHeight()/2)-(viewHeight/2) : 0,viewWidth, viewHeight);
			shader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		    if (USE_VIGNETTE) {
			    RadialGradient vignette = new RadialGradient(
			    		circleCenter + borderWidth, (circleCenter + borderWidth), circleCenter-2,
						new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
						Shader.TileMode.CLAMP);
	
				Matrix oval = new Matrix();
				oval.setScale(1.0f, 0.7f);
				vignette.setLocalMatrix(oval);
			    paint.setShader(new ComposeShader(shader, vignette, PorterDuff.Mode.SRC_OVER));

		    }else{
		    	paint.setShader(shader);
		    }
			
		    
			// circleCenter is the x or y of the view's center
			// radius is the radius in pixels of the cirle to be drawn
			// paint contains the shader that will texture the shape
		    
		   
		    if(borderWidth != 0)
			canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter, paintBorder);//-3


			Paint p = new Paint();
			p.setColor(status);
			p.setAntiAlias(true);
			   int space = 0;

			Path pa = new Path();
			   pa.moveTo(0,0);
			   pa.lineTo(getWidth()/2, space);
			   pa.lineTo(getWidth(), getHeight()/2);
			   pa.lineTo(getWidth()/2, getHeight()-space);		 
			   pa.lineTo(0, getHeight()-space);
			   pa.close();
			//canvas.drawArc(new RectF(15, 10, 2*circleCenter + borderWidth, 2*circleCenter + borderWidth), -45, 180, true, p);
			canvas.drawPath(pa, p);

			   pa = new Path();
		   pa.moveTo(0,space);
		   pa.lineTo(getWidth()/2-10, space);
		   pa.lineTo(getWidth()-10, getHeight()/2);
		   pa.lineTo(getWidth()/2-10, getHeight()-space);		 
		   pa.lineTo(0, getHeight()-space);
		   pa.close();

			canvas.drawPath(pa, paint);
			//(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter-borderWidth-3, paint);//-2
		}		
	}
	
	void ChangeStatus(Status s){
		status = s.getColor();
		postInvalidate();
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		int height = getMeasuredHeight();

		viewWidth = height - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int height = getMeasuredHeight();
       
    	
    	
    	setMeasuredDimension(height, height);

    	/*
    	int width = measureWidth(widthMeasureSpec);
    	
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);    	
    	
       
    	viewWidth = width - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
    	
    	setMeasuredDimension(width, height);

    	 */
	}
}