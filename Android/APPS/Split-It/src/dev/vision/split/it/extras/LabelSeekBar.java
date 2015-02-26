package dev.vision.split.it.extras;

import dev.vision.split.it.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class LabelSeekBar extends SeekBar {
	
	private Drawable progressDrawable;
	private Rect barBounds, labelTextRect;
	private Bitmap labelBackground;
	private Point labelPos;
	private Paint labelTextPaint, labelBackgroundPaint;

	int viewWidth, barHeight, labelOffset;
//	    private int thumbX;
	float progressPosX;
	private String expression;
	private String labelText;
	//private int thumbX;


	public LabelSeekBar(Context context) {
		super(context);
		Init();
	}
	
	public LabelSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}

	public LabelSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Init();
	}
	
	private void Init() {
		progressDrawable = getProgressDrawable();

	    labelBackground = BitmapFactory.decodeResource(getResources(),
	            R.drawable.abc_ic_ab_back_holo_dark);

	    labelTextPaint = new Paint();
	    labelTextPaint.setColor(Color.WHITE);
	    labelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
	    labelTextPaint.setAntiAlias(true);
	    labelTextPaint.setDither(true);
	    labelTextPaint.setTextSize(15f);

	    labelBackgroundPaint = new Paint();

	    barBounds = new Rect();
	    labelTextRect = new Rect();

	    labelPos = new Point();		
	}

	@Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
     {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (labelBackground != null)
        {

            viewWidth = getMeasuredWidth();
            barHeight = getMeasuredHeight();// returns only the bar height (without the label);
            setMeasuredDimension(viewWidth, barHeight + labelBackground.getHeight());
        }

    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (labelBackground != null)
        {
            barBounds.left = getPaddingLeft();
            barBounds.top = labelBackground.getHeight() + getPaddingTop();
            barBounds.right = barBounds.left + viewWidth - getPaddingRight() - getPaddingLeft();
            barBounds.bottom = barBounds.top + barHeight - getPaddingBottom() - getPaddingTop();

            progressPosX = barBounds.left + ((float) this.getProgress() / (float) this.getMax()) * barBounds.width();

            labelPos.x = (int) progressPosX - labelOffset;
            labelPos.y = getPaddingTop();

            progressDrawable = getProgressDrawable();
            progressDrawable.setBounds(barBounds.left, barBounds.top, barBounds.right, barBounds.bottom);
            progressDrawable.draw(canvas);

            labelTextPaint.getTextBounds(labelText, 0, labelText.length(), labelTextRect);

            canvas.drawBitmap(labelBackground, labelPos.x, labelPos.y, labelBackgroundPaint);
            canvas.drawText(labelText, labelPos.x + labelBackground.getWidth() / 2 - labelTextRect.width() / 2, labelPos.y + labelBackground.getHeight() / 2 + labelTextRect.height() / 2, labelTextPaint);

            //thumbX = (int) progressPosX - getThumbOffset();
            //thumbDrawable.setBounds(thumbX, barBounds.top, thumbX + thumbDrawable.getIntrinsicWidth(), barBounds.top + thumbDrawable.getIntrinsicHeight());
            //thumbDrawable.draw(canvas);
        } 
    }

}
