package dev.vision.relationshipninjas.views;

import dev.vision.relationshipninjas.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class RoundedImage extends ImageView {

	private int borderWidth = 2;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;
	Context c;
	private boolean USE_VIGNETTE;
	

	public RoundedImage(Context context) {
		super(context);
		c=context;

		setup();
	}

	public RoundedImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setup();
	}

	public RoundedImage(Context context, AttributeSet attrs, int defStyle) {
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

		paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);        
		paintBorder.setStyle(Paint.Style.STROKE);
		//paintBorder.setShadowLayer(2, 0, 0, Color.LTGRAY);
		//if(!isInEditMode())
		setBorderColor(Color.BLACK);
		paintBorder.setStrokeWidth(4f);//change
		paintBorder.setAntiAlias(true);	
		setLayerType(LAYER_TYPE_HARDWARE, paintBorder);

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
				 Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(), Config.RGB_565);
			     Canvas canvas = new Canvas(bitmap); 
			     d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			     d.draw(canvas);
			     image = bitmap;
			}
		}
	}

	@Override
	public void setImageBitmap(Bitmap b){
		image = b;
	}
	int rad =20;
	@SuppressLint("DrawAllocation")
	@Override
    public void onDraw(Canvas canvas)
	{
		//load the bitmap
		//loadBitmap();

		// init shader
		if(image !=null)
		{		
			/*
			float oriWidth = image.getWidth();
			float oriHeight = image.getHeight();
			
		    float center_x = (float) viewWidth / (float)2;
		    float center_y = (float) viewHeight / (float)2;

		    
		    		   
		    float aspectRatioHie = oriHeight/oriWidth;  
		    
		    float aspectRatioWid = oriWidth/oriHeight;
		    */
			
		    //Bitmap bmp = Bitmap.createScaledBitmap(image, oriWidth> oriHeight ? (int) ((viewHeight)*aspectRatioWid) : viewWidth, oriWidth> oriHeight ? viewHeight : (int) (viewWidth*aspectRatioHie), false);
		    //bmp=  Bitmap.createBitmap(bmp, bmp.getWidth() > viewWidth ? (bmp.getWidth()/2)-(viewWidth/2) : 0 ,bmp.getHeight() > viewHeight ? (bmp.getHeight()/2)-(viewHeight/2) : 0,viewWidth, viewHeight);
			shader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		   	paint.setShader(shader);
		    
			
		    
			// circleCenter is the x or y of the view's center
			// radius is the radius in pixels of the cirle to be drawn
			// paint contains the shader that will texture the shape
		    final RectF rect = new RectF();
			rect.set(borderWidth*2, 

					 borderWidth*2, 

					 getMeasuredWidth()-borderWidth*2, 

					 getMeasuredHeight()-borderWidth*2);

			canvas.drawRoundRect(rect, rad, rad , paint);

			rect.set(borderWidth, 

					 borderWidth, 

					 getMeasuredWidth()-borderWidth, 

					 getMeasuredHeight()-borderWidth);

			
			canvas.drawRoundRect(rect, rad, rad, paintBorder);

			Shader shader = new LinearGradient(0, getHeight(), 0, (float) (getHeight()/1.5), Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
			//shader.setLocalMatrix(trans);

			paint.setShader(shader);
			canvas.drawRoundRect(new RectF(0, (float) (getHeight()/1.5), getWidth(), getHeight()),rad, rad, paint);
		
		  	}		
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);		
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int height = getMeasuredHeight();
		int width = getMeasuredWidth();

		viewWidth = width - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
    	
    	setMeasuredDimension(width, height);

    	/*
    	int width = measureWidth(widthMeasureSpec);
    	
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);    	
    	
       
    	viewWidth = width - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
    	
    	setMeasuredDimension(width, height);

    	 */
	}

	public int getWid() {
		// TODO Auto-generated method stub
		return viewWidth;
	}
	
	public int getHei() {
		// TODO Auto-generated method stub
		return viewHeight;
	}
}