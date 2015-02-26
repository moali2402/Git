package dev.vision.rave.views;

import dev.vision.rave.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class CircularImageView extends ImageView {

	private int borderWidth = 5;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;
	Context c;
	private boolean USE_VIGNETTE;
	

	public CircularImageView(Context context) {
		super(context);
		c=context;

		setup();
	}

	public CircularImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c=context;
		setup();
	}

	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
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
		if(!isInEditMode())
		setBorderColor(c.getResources().getColor(color.ProfileBack));
		paintBorder.setStrokeWidth(9f);//change
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
				 Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.RGB_565);
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
			canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter, paintBorder);//-3
			canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter-borderWidth+1, paint);//-2
		}		
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.y/5;//3
		if(width==0)
			width=150;
		int height = width;
       
    	viewWidth = width - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
    	
    	setMeasuredDimension(width, width);

    	/*
    	int width = measureWidth(widthMeasureSpec);
    	
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);    	
    	
       
    	viewWidth = width - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
    	
    	setMeasuredDimension(width, height);

    	 */
	}

	private int measureWidth(int measureSpec)
	{
	        int result = 0;
	        int specMode = MeasureSpec.getMode(measureSpec);
	        int specSize = MeasureSpec.getSize(measureSpec);

	        if (specMode == MeasureSpec.EXACTLY) {
	            // We were told how big to be
	            result = specSize;
	        } else {
	            // Measure the text
	            result = viewWidth;

	        }

		return result;
	}
	
	private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight;           
        }
        return result;
    }
}