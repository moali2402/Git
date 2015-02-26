
package dev.vision.rave.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.widget.ImageView;


public class RoundImage extends ImageView {

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
	
	 Path p = new Path();
	private int height;
	private int width;

	public RoundImage(Context context) {
		super(context);
	}

	public RoundImage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	public RoundImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		p.reset();
		p.addRoundRect(new RectF(0, 0, width,  height), 20, 20,  Direction.CW);
		
		canvas.clipPath(p);
	
		p.reset();		
		p.addRect(new RectF(0, 20, width,  height), Direction.CW);
		
		canvas.clipPath(p,Op.UNION);

		super.onDraw(canvas);

	}

	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
	}

	
}