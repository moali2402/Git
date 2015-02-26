package dev.vision.rave.views;
/*
import dev.vision.rave.R;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Bitmap;
class TouchView extends View{
	
	public TouchView(Context context) {
        super(context);

        bgr = BitmapFactory.decodeResource(getResources(),R.drawable.ki);
        overlayDefault =     BitmapFactory.decodeResource(getResources(),R.drawable.bby);
        overlay_mutable=Bitmap.createBitmap(overlayDefault.getWidth(),overlayDefault.getHeight(),Config.ARGB_8888);

	    c2 = new Canvas();
	    c2.setBitmap(overlay_mutable);
	    c2.drawBitmap(overlayDefault, 0, 0, null);
	
	    pTouch = new Paint(Paint.ANTI_ALIAS_FLAG);         
	    pTouch.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
	    pTouch.setAlpha(0);
	    pTouch.setColor(Color.TRANSPARENT);
	    pTouch.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));  // Blur modes

	}
    public TouchView(Context context, AttributeSet attrs) {
		super(context, attrs);

        bgr = BitmapFactory.decodeResource(getResources(),R.drawable.ki);
        overlayDefault =     BitmapFactory.decodeResource(getResources(),R.drawable.bby);
        overlay_mutable=Bitmap.createBitmap(overlayDefault.getWidth(),overlayDefault.getHeight(),Config.ARGB_8888);

	    c2 = new Canvas();
	    c2.setBitmap(overlay_mutable);
	    c2.drawBitmap(overlayDefault, 0, 0, null);
	
	    pTouch = new Paint(Paint.ANTI_ALIAS_FLAG);         
	    pTouch.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
	    pTouch.setAlpha(0);
	    pTouch.setColor(Color.TRANSPARENT);
	    pTouch.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));  // Blur modes
	}

	public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

        bgr = BitmapFactory.decodeResource(getResources(),R.drawable.ki);
        overlayDefault =     BitmapFactory.decodeResource(getResources(),R.drawable.bby);
        overlay_mutable=Bitmap.createBitmap(overlayDefault.getWidth(),overlayDefault.getHeight(),Config.ARGB_8888);

	    c2 = new Canvas();
	    c2.setBitmap(overlay_mutable);
	    c2.drawBitmap(overlayDefault, 0, 0, null);
	
	    pTouch = new Paint(Paint.ANTI_ALIAS_FLAG);         
	    pTouch.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
	    pTouch.setAlpha(0);
	    pTouch.setColor(Color.TRANSPARENT);
	    pTouch.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));  // Blur modes
	}

	Bitmap bgr;
    Bitmap overlayDefault;
    Bitmap overlay_mutable;
    Paint pTouch;
    int X = -100;
    int Y = -100;
    Canvas c2;

    

@Override
public boolean onTouchEvent(MotionEvent ev) {

    switch (ev.getAction()) {

        case MotionEvent.ACTION_DOWN: {

            X = (int) ev.getX();
            Y = (int) ev.getY();
            invalidate();
            break;
        }
        case MotionEvent.ACTION_MOVE: {
                X = (int) ev.getX();
                Y = (int) ev.getY();
                invalidate();
                break;
        }           
        case MotionEvent.ACTION_UP:
            break;
    }
    return true;
}

@Override
public void onDraw(Canvas canvas){
    canvas.drawBitmap(bgr, 0, 0, null);
    //copy the default overlay into temporary overlay and punch a hole in it                          
    c2.drawBitmap(overlayDefault, 0, 0, null); //exclude this line to show all as you draw
    c2.drawCircle(X, Y, 80, pTouch);
    //draw the overlay over the background  
    canvas.drawBitmap(overlay_mutable, 0, 0, null);
    super.onDraw(canvas);
}
}
*/