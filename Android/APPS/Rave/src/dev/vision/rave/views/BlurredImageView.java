package dev.vision.rave.views;

import dev.vision.rave.DataHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
 
public class BlurredImageView extends ImageView {

	Bitmap image;
	public BlurredImageView(Context context) {
		super(context);
		loadBitmap();
		
	}

	public BlurredImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadBitmap();
		//setImageBitmap(applyGaussianBlur(image));

	}

	public BlurredImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		loadBitmap();
		//setImageBitmap(applyGaussianBlur(image));
	}

	Bitmap blurfast(Bitmap bmp, int radius) {
		  int w = bmp.getWidth();
		  int h = bmp.getHeight();
		  int[] pix = new int[w * h];
		  
		  bmp.getPixels(pix, 0, w, 0, 0, w, h);

		  for(int r = radius; r >= 1; r /= 2) {
		    for(int i = r; i < h - r; i++) {
		      for(int j = r; j < w - r; j++) {
		        int tl = pix[(i - r) * w + j - r];
		        int tr = pix[(i - r) * w + j + r];
		        int tc = pix[(i - r) * w + j];
		        int bl = pix[(i + r) * w + j - r];
		        int br = pix[(i + r) * w + j + r];
		        int bc = pix[(i + r) * w + j];
		        int cl = pix[i * w + j - r];
		        int cr = pix[i * w + j + r];

		        pix[(i * w) + j] = 0xFF000000 |
		            (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) + (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3) & 0xFF |
		            (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00) + (br & 0xFF00) + (bc & 0xFF00) + (cl & 0xFF00) + (cr & 0xFF00)) >> 3) & 0xFF00 |
		            (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) + (bl & 0xFF0000) + (br & 0xFF0000) + (bc & 0xFF0000) + (cl & 0xFF0000) + (cr & 0xFF0000)) >> 3) & 0xFF0000;
		      }
		    }
		  }
		  bmp.setPixels(pix, 0, w, 0, 0, w, h);
		  return bmp;
		}

	
	
	private void loadBitmap()
	{
		Drawable d = this.getDrawable();
		if(d!=null){
			
				 Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(), Config.RGB_565);
			     Canvas canvas = new Canvas(bitmap); 
			     d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			     d.draw(canvas);
			     image = bitmap;
			
		}
	}
	
	@Override
	public void setImageBitmap(Bitmap bmp){
		super.setImageBitmap(bmp);
		loadBitmap();
		super.setImageDrawable(new BitmapDrawable(getResources(),blurfast(image, 4)));
	}
	
	@Override
	public void onDraw(Canvas ca){
		super.onDraw(ca);

		Paint p = new Paint();
		p.setAntiAlias(true);
		Shader shader = new LinearGradient(getWidth()/1.4f, 0, getWidth(), 0,  Color.TRANSPARENT, DataHolder.APP_BACKGROUND, Shader.TileMode.CLAMP);
		p.setShader(shader);
		ca.drawRect(getWidth()/1.4f, 0, getWidth(), getHeight(), p);
	
		 
	}
	
	

}
