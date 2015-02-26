package dev.vision.rave.progresswheel;

import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.Region.Op;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


/**
 * An indicator of progress, similar to Android's ProgressBar.
 * Can be used in 'spin mode' or 'increment mode'
 *
 * @author Todd Davies
 *         <p/>
 *         Licensed under the Creative Commons Attribution 3.0 license see:
 *         http://creativecommons.org/licenses/by/3.0/
 */
@SuppressLint("NewApi") public class HeartChart extends View {

    //Sizes (with defaults)
    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private float contourSize = 0;

    //Padding (with defaults)
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    //Colors (with defaults)
    private int barColor = 0xAA000000;
    private int contourColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    //Paints
    private Paint barPaint = new Paint();
    private Paint other = new Paint();

    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();

    //Rectangles
    @SuppressWarnings("unused")
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();


    //Animation
    //The amount of pixels to move the bar by on each draw
    private int spinSpeed = 2;
    //The number of milliseconds to wait inbetween each draw
    private int delayMillis = 0;
    long burned =0;
    
    private Handler spinHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable
         * and so spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };
    int progress = 0;
    boolean isSpinning = false;

    //Other
    private String text = "";
    private String[] splitText = {};

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public HeartChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressWheel));
    }

    //----------------------------------
    //Setting up stuff
    //----------------------------------

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = getMeasuredWidth();
        layout_height=getMeasuredHeight();
        setupBounds();
        setupPaints();
        invalidate();
    }
    
    @Override
	public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){

    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth()/1.5));
 
    }
    
	Paint fm = new Paint();

    
    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(DataHolder.PINK);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.FILL_AND_STROKE);
        
        
       // barPaint.setStrokeWidth(barWidth);
       // barPaint.setShader(new RadialGradient(layout_width / 2, layout_height / 2,
       // 		fullRadius, DataHolder.PINK, DataHolder.BLUE, TileMode.CLAMP));
        this.setLayerType(LAYER_TYPE_SOFTWARE, barPaint);
        other.setColor(DataHolder.BLUE);
        other.setAntiAlias(true);
        other.setStyle(Style.FILL_AND_STROKE);
       // other.setStrokeWidth(barWidth);
        this.setLayerType(LAYER_TYPE_SOFTWARE, other);

        
        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        contourPaint.setColor(contourColor);
        contourPaint.setAntiAlias(true);
        contourPaint.setStyle(Style.STROKE);
        contourPaint.setStrokeWidth(contourSize);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to steup the circle
        int minValue = Math.min(layout_width, layout_height);
		viewWidth = getMeasuredWidth();
		viewHeight= getMeasuredHeight();
		
		midX = viewWidth/2;
  		midY = viewHeight/2;
  		
        // Calc the Offset if needed
        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        // Add the offset
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        rectBounds = new RectF(paddingLeft,
                paddingTop,
                getMeasuredWidth() - paddingRight,
                getMeasuredHeight() - paddingBottom);

        circleBounds = new RectF(paddingLeft + barWidth,
                paddingTop + barWidth,
                getMeasuredWidth() - paddingRight - barWidth,
                getMeasuredHeight() - paddingBottom - barWidth);
       
        fullRadius = (getMeasuredWidth() - paddingRight - barWidth) / 2;
        circleRadius = (fullRadius - barWidth) + 1;
  	
        float inner = (fullRadius - barWidth-15) *2 ;
        height = inner * 97.5f / 110;
	    height/=97.5;
	    width = inner * 110 / 97.5f;	   
	    width/=110;
	    height = height > width ? width : height; 
	    width = height > width ? width : height;
        
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth,
                barWidth);

        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth,
                rimWidth);

        spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_spinSpeed,
                spinSpeed);

        delayMillis = a.getInteger(R.styleable.ProgressWheel_delayMillis,
                delayMillis);
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

        barLength = (int) a.getDimension(R.styleable.ProgressWheel_barLength,
                barLength);

        textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize,
                textSize);

        textColor = a.getColor(R.styleable.ProgressWheel_textColor,
                textColor);

        //if the text is empty , so ignore it
        if (a.hasValue(R.styleable.ProgressWheel_text)) {
            setText(a.getString(R.styleable.ProgressWheel_text));
        }

        rimColor = a.getColor(R.styleable.ProgressWheel_rimColor,
                rimColor);

        circleColor = a.getColor(R.styleable.ProgressWheel_CircleColor,
                circleColor);

        contourColor = a.getColor(R.styleable.ProgressWheel_contourColor, contourColor);
        contourSize = a.getDimension(R.styleable.ProgressWheel_contourSize, contourSize);


        // Recycle
        a.recycle();
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------

    float midX, midY,height, width, viewHeight, viewWidth;


	

    @Override
	protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		canvas.drawColor(Color.parseColor("#90000000"));

        //Draw the rim
       // canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        //canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
        //canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
        //Draw the bar
	    Path myPath1 = new Path();
	    /*
	    myPath1.moveTo(75+100,50+100);
		   
	    myPath1.cubicTo(75f+100,50+100,70+100,25+100,50+100,25+100);
	  
	    myPath1.cubicTo(20f+100,25f+100,20f+100,62.5f+100,20f+100,62.5f+100);

	    myPath1.cubicTo(20+100,80+100,45+100,97+100,50+100,100+100);

	    myPath1.cubicTo(50+100,100+100,73+100,115+100,75+100,122.5f+100);

		myPath1.cubicTo(75+100,122.5f+100,77+100,115+100,100+100,100+100);

	    myPath1.cubicTo(100+100,100+100,130+100,80+100,130+100,62.5f+100);

	    myPath1.cubicTo(130f+100,62.5f+100,130f+100,25f+100,100+100,25f+100);
	
	    myPath1.cubicTo(100+100,25+100,80f+100,25+100,75+100,50+100);
	    */
	    
	    myPath1.moveTo(75,40);
	    myPath1.cubicTo(75,37,70,25,50,25);
	    myPath1.cubicTo(20,25,20,62.5f,20,62.5f);
	    myPath1.cubicTo(20,80,40,102,75,120);
	    myPath1.cubicTo(110,102,130,80,130,62.5f);
	    myPath1.cubicTo(130,62.5f,130,25,100,25);
	    myPath1.cubicTo(85,25,75,37,75,40);
	    myPath1.offset(100, 100);
	    Path myPath2 = new Path(myPath1);

	    Matrix scaleMatrix = new Matrix();
	    RectF rectF = new RectF();
	    myPath1.computeBounds(rectF, true);
	    

	    scaleMatrix.setScale(width, height,rectF.centerX(),rectF.centerY());
	    myPath1.transform(scaleMatrix); 
	   
	    Matrix translateMatrix = new Matrix();
	    translateMatrix.setTranslate(viewWidth/2-(75+100),viewHeight/2-(173.75f));
	    myPath1.transform(translateMatrix); 
	    
	    canvas.clipPath(myPath1);

	    scaleMatrix = new Matrix();
	    rectF = new RectF();
	    myPath2.computeBounds(rectF, true);
	    scaleMatrix.setScale(width*0.875f, height*0.875f,rectF.centerX(),rectF.centerY());
	    myPath2.transform(scaleMatrix);
	    

	    translateMatrix = new Matrix();
	    translateMatrix.setTranslate(viewWidth/2-(75+100),viewHeight/2-(173.75f));
	    myPath2.transform(translateMatrix); 
	    canvas.clipPath(myPath2,Op.DIFFERENCE);
	    RectF rf =new RectF(rectBounds);
	    rf.inset(-20, -20);
    	if(burned != 360 && burned!=0){
    		setGradient();
	        canvas.drawArc(rf, -90, progress, true, fm);
    	}else{
            canvas.drawArc(rf, -90, progress, true, barPaint);

            if(progress>=burned)
            canvas.drawArc(rf, -90+burned, progress-burned, true, other);

    	}
    	
          
        // if(progress>=burned)
        // canvas.drawArc(circleBounds, -90+burned, progress-burned, true, mf);

        
        //Draw the inner circle
      
        //Draw the text (attempts to center it horizontally and vertically)
        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight/2) - textPaint.descent();
        int i =1;
        for (String s : splitText) {
        	if (splitText.length>0) i = -i;
            float horizontalTextOffset = textPaint.measureText(s) / 2;
            canvas.drawText(s, this.getMeasuredWidth() / 2 - horizontalTextOffset,
                    this.getMeasuredHeight() / 2 + verticalTextOffset + i*textHeight , textPaint);
           // i++;
        }
    }

    /**
    *   Check if the wheel is currently spinning
    */
    
    public boolean isSpinning() {
    	return isSpinning;
    }
    
    /**
     * Reset the count (in increment mode)
     */
    public void resetCount() {
        progress = 0;
        setText("0%");
        invalidate();
    }

    /**
     * Turn off spin mode
     */
    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        spinHandler.removeMessages(0);
    }


    /**
     * Puts the view on spin mode
     */
    public void spin() {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    /**
     * Increment the progress by 1 (of 360)
     */
    public void incrementProgress() {
        isSpinning = false;
        progress++;
        if (progress > 360)
            progress = 360;
       // setText("PROGRESS \n"+Math.round(((float) progress / 360) * 100) + "%");
        spinHandler.sendEmptyMessage(0);
    }


    /**
     * Set the progress to a specific value
     */
    public void setProgress(int i) {
        isSpinning = false;
        progress = i;
        spinHandler.sendEmptyMessage(0);
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------

    /**
     * Set the text in the progress bar
     * Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
	public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    @Override
	public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    @Override
	public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    @Override
	public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }


    public Shader getRimShader() {
        return rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

	public void setBurned(long burned) {
		this.burned = burned;
	}
	
	private void setGradient(){
		Matrix m = new Matrix();
    	SweepGradient sw = new SweepGradient(circleBounds.centerX(),circleBounds.centerY(),new int[]{Color.parseColor("#a03d8b"), DataHolder.PINK, DataHolder.PINK, DataHolder.BLUE, DataHolder.BLUE, Color.parseColor("#a03d8b")},new float[]{0f, .1f, (burned/360f)-.05f, (burned/360f)+.05f, .9f, 1f});
    	m.setRotate(-90,circleBounds.centerX(),circleBounds.centerY());
    	sw.setLocalMatrix(m);
    	fm.setShader(sw);
    	fm.setAntiAlias(true);
	}
}
