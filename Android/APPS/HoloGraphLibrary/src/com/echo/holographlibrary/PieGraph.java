/*
 * 	   Created by Daniel Nadeau
 * 	   daniel.nadeau01@gmail.com
 * 	   danielnadeau.blogspot.com
 * 
 * 	   Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.echo.holographlibrary;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("NewApi") public class PieGraph extends View {

	private ArrayList<PieSlice> slices = new ArrayList<PieSlice>();
	private Paint paint = new Paint();
	private Path path = new Path();
	
	private int indexSelected = -1;
	private int thickness;
	private OnSliceClickedListener listener;
	
	private boolean drawCompleted = false;
	private float padding = 0;
	
	
	public PieGraph(Context context) {
		super(context);
		 if (!isInEditMode()) {
             setLayerType(View.LAYER_TYPE_HARDWARE, paint);
         }		thickness = (int) (25f * context.getResources().getDisplayMetrics().density);
	}
	
	public PieGraph(Context context, AttributeSet attrs) {
		super(context, attrs);

		 if (!isInEditMode()) {
             setLayerType(View.LAYER_TYPE_HARDWARE, paint);
         }
		thickness = (int) (25f * context.getResources().getDisplayMetrics().density);
	}
	
	public PieGraph(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		 if (!isInEditMode()) {
             setLayerType(View.LAYER_TYPE_HARDWARE, paint);
         }
		thickness = (int) (25f * context.getResources().getDisplayMetrics().density);
	}
	
    private Handler spinHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable
         * and so spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            //super.handleMessage(msg);
        }
    };
    
	float midX, midY, radius, innerRadius, height, width, viewHeight, viewWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);

        setupBounds();
        invalidate();
    }
    
	
	
	void setupBounds(){
		viewWidth = getMeasuredWidth();
		viewHeight= getMeasuredHeight();
		
		midX = viewWidth/2;
  		midY = viewHeight/2;
  		if (midX < midY){
  			radius = midX;
  		} else {
  			radius = midY;
  		}
  		radius -= padding;
  		innerRadius = radius - thickness;
  		
  		height = innerRadius*2 * 97.5f / 110;
	    height/=97.5;
	    width = innerRadius*2 * 110 / 97.5f;	   
	    width/=110;
	    height = height > width ? width : height; 
	    width = height > width ? width : height;
	}
    

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.parseColor("#90000000"));
		paint.reset();
		paint.setAntiAlias(true);
		path.reset();
		
/*
		midX = getWidth()/2;
  		midY = getHeight()/2;
  		if (midX < midY){
  			radius = midX;
  		} else {
  			radius = midY;
  		}
  		radius -= padding;
  		innerRadius = radius - thickness;
*/
	    Path myPath1 = new Path();
	    
	    myPath1.moveTo(75+100,50+100);
		   
	    myPath1.cubicTo(72.5f+100,50+100,70+100,25+100,50+100,25+100);
	  
	    myPath1.cubicTo(20f+100,25f+100,20f+100,62.5f+100,20f+100,62.5f+100);

	    myPath1.cubicTo(20+100,80+100,45+100,97+100,50+100,100+100);

	    myPath1.cubicTo(50+100,100+100,73+100,115+100,75+100,122.5f+100);

		myPath1.cubicTo(75+100,122.5f+100,77+100,115+100,100+100,100+100);

	    myPath1.cubicTo(100+100,100+100,130+100,80+100,130+100,62.5f+100);

	    myPath1.cubicTo(130f+100,62.5f+100,130f+100,25f+100,100+100,25f+100);
	
	    myPath1.cubicTo(100+100,25+100,80f+100,25+100,75+100,50+100);
	    Path myPath2 = new Path(myPath1);

	    Matrix scaleMatrix = new Matrix();
	    RectF rectF = new RectF();
	    myPath1.computeBounds(rectF, true);
	    
	    /*
	     * float height = innerRadius*2 * 97.5f / 110;
	    height/=97.5;
	    float width = innerRadius*2 * 110 / 97.5f;	   
	    width/=110;
	    height = height > width ? width : height; 
	    width = height > width ? width : height;
	     */
	    scaleMatrix.setScale(width, height,rectF.centerX(),rectF.centerY());
	    myPath1.transform(scaleMatrix); 
	   
	    Matrix translateMatrix = new Matrix();
	    translateMatrix.setTranslate(viewWidth/2-(75+100),viewHeight/2-(173.75f));
	    myPath1.transform(translateMatrix); 
	    
	    canvas.clipPath(myPath1);
	    
		/* 
	    myPath1.moveTo(75+100,50+100);
		   
	    myPath1.cubicTo(72.5f+100,50+100,70+100,25+100,50+100,25+100);
	  
	    myPath1.cubicTo(20f+100,25f+100,20f+100,62.5f+100,20f+100,62.5f+100);

	    myPath1.cubicTo(20+100,80+100,45+100,97+100,50+100,100+100);

	    myPath1.cubicTo(50+100,100+100,73+100,115+100,75+100,122.5f+100);

		myPath1.cubicTo(75+100,122.5f+100,77+100,115+100,100+100,100+100);

	    myPath1.cubicTo(100+100,100+100,130+100,80+100,130+100,62.5f+100);

	    myPath1.cubicTo(130f+100,62.5f+100,130f+100,25f+100,100+100,25f+100);
	
	    myPath1.cubicTo(100+100,25+100,80f+100,25+100,75+100,50+100);
	    */
	    scaleMatrix = new Matrix();
	    rectF = new RectF();
	    myPath2.computeBounds(rectF, true);
	    scaleMatrix.setScale(width*0.875f, height*0.875f,rectF.centerX(),rectF.centerY());
	    myPath2.transform(scaleMatrix);
	    

	    translateMatrix = new Matrix();
	    translateMatrix.setTranslate(viewWidth/2-(75+100),viewHeight/2-(173.75f));
	    myPath2.transform(translateMatrix); 
	    canvas.clipPath(myPath2,Op.DIFFERENCE);

	    
		
		
		
		float currentAngle = 270;
		float currentSweep = 0;
		int totalValue = 0;
		

		for (PieSlice slice : slices){
			totalValue += slice.getValue();
		}
		
		int count = 0;
		for (PieSlice slice : slices){

			Path p = Path();
  			p.moveTo(midX, midY);

			paint.setColor(slice.getColor());
			currentSweep = (slice.getValue()/totalValue)*(360);
			p.arcTo(RectF(midX, midY, radius), currentAngle+padding, currentSweep - padding);
			//p.arcTo(RectF(midX, midY, innerRadius), (currentAngle+padding) + (currentSweep - padding), -(currentSweep-padding));
			p.close();
			
			slice.setPath(p);
			slice.setRegion(Reigon(midX, midY, radius));
			
			canvas.drawPath(p, paint);

			/*
				if (indexSelected == count && listener != null){
					path.reset();
					paint.setColor(slice.getColor());
					paint.setColor(Color.parseColor("#33B5E5"));
					paint.setAlpha(100);
					
					if (slices.size() > 1) {
						path.arcTo(RectFa(midX, midY, radius, padding), currentAngle, currentSweep+padding);
						path.arcTo(extracted(midX, midY, innerRadius, padding), currentAngle + currentSweep + padding, -(currentSweep + padding));
						path.close();
					} else {
						path.addCircle(midX, midY, radius+padding, Direction.CW);
					}
				
					canvas.drawPath(path, paint);
					paint.setAlpha(255);
				}
		    */
			currentAngle = currentAngle+currentSweep;
			
			count++;
		}
		
		
		drawCompleted = true;
	}
	/*
	 * 
	 * Paint tPaint;
		tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    // tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    // tPaint.setColor(Color.BLACK);
	    // tPaint.setTextSize(20);
		//canvas.drawText("Female", (midX-innerRadius+(padding*2))*1.2f, midY, tPaint);
		Bitmap bm =  BitmapFactory.decodeResource(getResources(), R.drawable.femalesign);
		bm = Bitmap.createScaledBitmap(bm, 35, 45, true);
		canvas.drawBitmap(bm, (midX-innerRadius+(padding*2))*1.25f, midY, tPaint);
	 */


	private RectF RectFa(float midX, float midY,
			float innerRadius, float padding) {
		return new RectF(midX-innerRadius+(padding*2), midY-innerRadius+(padding*2), midX+innerRadius-(padding*2), midY+innerRadius-(padding*2));
	}

	private RectF extracted(float midX, float midY,
			float radius, float padding) {
		return new RectF(midX-radius-(padding*2), midY-radius-(padding*2), midX+radius+(padding*2), midY+radius+(padding*2));
	}

	private Region Reigon(float midX, float midY, float radius) {
		return new Region((int)(midX-radius), (int)(midY-radius), (int)(midX+radius), (int)(midY+radius));
	}

	private RectF RectF(float midX, float midY, float radius) {
		return new RectF(midX-radius, midY-radius, midX+radius, midY+radius);
	}

	private Path Path() {
		return new Path();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (drawCompleted) {
		
			Point point = new Point();
			point.x = (int) event.getX();
			point.y = (int) event.getY();
			
			int count = 0;
			for (PieSlice slice : slices){
				Region r = new Region();
				r.setPath(slice.getPath(), slice.getRegion());
				if (r.contains((int)point.x,(int) point.y) && event.getAction() == MotionEvent.ACTION_DOWN){
					indexSelected = count;
				} else if (event.getAction() == MotionEvent.ACTION_UP){
					if (r.contains((int)point.x,(int) point.y) && listener != null){
						if (indexSelected > -1){
							listener.onClick(indexSelected);
						}
						indexSelected = -1;
					}
					
				}
				else if(event.getAction() == MotionEvent.ACTION_CANCEL)
					indexSelected = -1;
				count++;
			}
			
			if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
				postInvalidate();
			}
	    }
	    

	    return true;
	}
	
	public ArrayList<PieSlice> getSlices() {
		return slices;
	}
	public void setSlices(ArrayList<PieSlice> slices) {
		this.slices = slices;
		postInvalidate();
	}
	public PieSlice getSlice(int index) {
		return slices.get(index);
	}
	public void addSlice(PieSlice slice) {
		this.slices.add(slice);
		postInvalidate();
	}
	public void setOnSliceClickedListener(OnSliceClickedListener listener) {
		this.listener = listener;
	}
	
	public int getThickness() {
		return thickness;
	}
	public void setThickness(int thickness) {
		this.thickness = thickness;
		postInvalidate();
	}
	public void setPadd(float padding) {
		this.padding  = padding;
		postInvalidate();
	}	
	public void removeSlices(){
		for (int i = slices.size()-1; i >= 0; i--){
			slices.remove(i);
		}
		postInvalidate();
	}

	public static interface OnSliceClickedListener {
		public abstract void onClick(int index);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int w = this.getMeasuredWidth();
		setMeasuredDimension(w, w);
	}

}
