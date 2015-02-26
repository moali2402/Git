package dev.vision.rave.views;

import dev.vision.rave.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;


public class PolaroidImage extends ImageView {

	private int borderWidth = 2;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;
	Context c;
	private boolean USE_VIGNETTE;
	private boolean circular;
	

	public PolaroidImage(Context context) {
		super(context);
		c=context;

		setup();
	}

	public PolaroidImage(Context context, AttributeSet attrs) {
		super(context, attrs);

	    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularImage);
		setup();		    
		circular = a.getBoolean(R.styleable.CircularImage_circular, true);
	    setBorderWidth(a.getInt(R.styleable.CircularImage_border_Width,2));
	    setBorderColor(a.getColor(R.styleable.CircularImage_border__color, Color.LTGRAY));
	    a.recycle();
	}

	
	public PolaroidImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		c=context;
	    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularImage);

		setup();
		circular = a.getBoolean(R.styleable.CircularImage_circular, true);
	    setBorderWidth(a.getInt(R.styleable.CircularImage_border_Width,2));	    
	    setBorderColor(a.getColor(R.styleable.CircularImage_border__color, Color.LTGRAY));

	    a.recycle();
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
		setBorderColor(Color.LTGRAY);
		paintBorder.setStrokeWidth(4f);//change
		paintBorder.setAntiAlias(true);	
		setLayerType(LAYER_TYPE_HARDWARE, paintBorder);
		
	}


	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		paintBorder.setStrokeWidth(borderWidth*2);//change
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
		
			Paint pw = new Paint();
			pw.setColor(Color.WHITE);
		    pw.setStyle(Style.FILL);;

			Paint pb = new Paint();
			pb.setColor(Color.parseColor("#20000000"));
			pb.setStrokeWidth(2);
		    pb.setStyle(Style.STROKE);
		    
		    TypedValue Tp = new TypedValue();

		    int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                     10, getResources().getDisplayMetrics());		    Path p = new Path();
		    p.addRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), Direction.CW);
		    p.addRect(value, value, getMeasuredWidth()-value, getMeasuredHeight()-(getMeasuredHeight()/4), Direction.CCW);
		    p.setFillType(FillType.EVEN_ODD);
		    p.close();
		    
		    canvas.drawPath(p,pw);

		   
			canvas.drawRect(1, 1, getMeasuredWidth()-1, getMeasuredHeight()-1, pb);

			super.onDraw(canvas);






			//canvas.restore();
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		int height = getMeasuredHeight();

		
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

		int height = getMeasuredWidth();
       
		viewWidth = height - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
    	setMeasuredDimension(height, height+height/4);

    	/*
    	int width = measureWidth(widthMeasureSpec);
    	
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);    	
    	
       
    	viewWidth = width - (borderWidth *2);
    	viewHeight = height - (borderWidth*2);
    	
    	setMeasuredDimension(width, height);

    	 */
	}
}