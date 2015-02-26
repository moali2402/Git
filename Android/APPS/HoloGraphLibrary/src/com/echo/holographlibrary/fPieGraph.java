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
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class fPieGraph extends View {
	
	
	public static boolean stopped = true;
	public static int PINK = Color.parseColor("#ff63a5");
	public static int BLUE = Color.parseColor("#54a9fe");
	public static int APP_BACKGROUND = Color.parseColor("#002A3D");
	public static int TIMER = 20;
	public static int READINGS = 50;
		boolean done;


	private ArrayList<CopyOfPieSlice> slices = new ArrayList<CopyOfPieSlice>();
	private Paint paint = new Paint();
	private Path path = new Path();
	
	private int indexSelected = -1;
	private int thickness;
	private OnSliceClickedListener listener;
	
	private boolean drawCompleted = false;
	private float padding = 0;
	
	@SuppressLint("NewApi") 
	public fPieGraph(Context context) {
		super(context);
		this.setLayerType(LAYER_TYPE_SOFTWARE, paint);

		thickness = (int) (25f * context.getResources().getDisplayMetrics().density);
		if(!done){
			DrawLikes3(this, 1, 5, null, null);
			done=true;
  		}
	}
	
	@SuppressLint("NewApi") 
	public fPieGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayerType(LAYER_TYPE_SOFTWARE, paint);

		thickness = (int) (25f * context.getResources().getDisplayMetrics().density);
		if(!done){
			DrawLikes3(this, 1, 5, null, null);
			done=true;
  		}

	}
	//One Leads the other in Draw
	public static void DrawLikes3(final fPieGraph copyOfPieGraph, int male, int female,final TextView  maleTxt, final TextView femaleTxt){
			final int first = male > female ? male : female;
			final int second = first == male ? female : male;
			
			//final TextView maleTxt = (TextView) findViewById(R.id.maleTxt);
			//final TextView femaleTxt = (TextView) findViewById(R.id.femaleTxt);
			//final long TOTAL = firstMulti+ secondMulti;
			
			//copyOfPieGraph.setPadd(1);
			
			//final PieSlice background = new PieSlice();
			//background.setColor(Color.TRANSPARENT);
			//background.setValue(firstMulti);
			//pg.addSlice(background);
			
			final CopyOfPieSlice slice = new CopyOfPieSlice();
			final CopyOfPieSlice slice1 = new CopyOfPieSlice();
			if(first>0){
				slice.setColor(first == male ? BLUE : PINK);
				slice.setValue(first);
				copyOfPieGraph.addSlice(slice);
			}
			if(second>0){
				slice1.setColor(slice.getColor() == PINK ? BLUE : PINK);
				slice1.setValue(second);
				copyOfPieGraph.addSlice(slice1);
			}
			
			slice.setValue(first);

			slice1.setValue(second);

			copyOfPieGraph.postInvalidate();	
					
		}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
	    //super.onDraw(canvas);
		//Math.sqrt(1-(Math.abs(100)-1)^2);
  		float midX, midY, radius, innerRadius;

		midX = getWidth()/2;
  		midY = getHeight()/2;
  		if (midX < midY){
  			radius = midX;
  		} else {
  			radius = midY;
  		}
  		radius -= padding;
  		innerRadius = radius - thickness;
  		
  		
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

	    Matrix scaleMatrix = new Matrix();
	    RectF rectF = new RectF();
	    myPath1.computeBounds(rectF, true);
	    float height = innerRadius*2 * 97.5f / 110;
	    height/=97.5;
	    float width = innerRadius*2 * 110 / 97.5f;	   
	    width/=110;
	    height = height > width ? width : height; 
	    width = height > width ? width : height;
	    scaleMatrix.setScale(width, height,rectF.centerX(),rectF.centerY());
	    myPath1.transform(scaleMatrix); 
	   
	    Matrix translateMatrix = new Matrix();
	    translateMatrix.setTranslate(getWidth()/2-(75+100),getHeight()/2-(173.75f));
	    myPath1.transform(translateMatrix); 
	    
	    canvas.clipPath(myPath1);
	    
	    myPath1 = new Path();
		 
	    myPath1.moveTo(75+100,50+100);
		   
	    myPath1.cubicTo(72.5f+100,50+100,70+100,25+100,50+100,25+100);
	  
	    myPath1.cubicTo(20f+100,25f+100,20f+100,62.5f+100,20f+100,62.5f+100);

	    myPath1.cubicTo(20+100,80+100,45+100,97+100,50+100,100+100);

	    myPath1.cubicTo(50+100,100+100,73+100,115+100,75+100,122.5f+100);

		myPath1.cubicTo(75+100,122.5f+100,77+100,115+100,100+100,100+100);

	    myPath1.cubicTo(100+100,100+100,130+100,80+100,130+100,62.5f+100);

	    myPath1.cubicTo(130f+100,62.5f+100,130f+100,25f+100,100+100,25f+100);
	
	    myPath1.cubicTo(100+100,25+100,80f+100,25+100,75+100,50+100);
	    
	    scaleMatrix = new Matrix();
	    rectF = new RectF();
	    myPath1.computeBounds(rectF, true);
	    scaleMatrix.setScale(width*0.875f, height*0.875f,rectF.centerX(),rectF.centerY());
	    myPath1.transform(scaleMatrix);
	    

	    translateMatrix = new Matrix();
	    translateMatrix.setTranslate(getWidth()/2-(75+100),getHeight()/2-(173.75f));
	    myPath1.transform(translateMatrix); 
	    canvas.clipPath(myPath1,Op.DIFFERENCE);

	    
	    /*
	    myPath1.reset();
	    myPath1.moveTo(75,120);
	    myPath1.cubicTo(75,90,10,102,75,150);
	    paint.reset();
	    paint.setAntiAlias(true);
	    paint.setStyle(Style.STROKE);
	    paint.setStrokeWidth(5);
	    paint.setColor(Color.BLACK);

	    canvas.drawPath(myPath1, paint);
	    
	    */
	    
	    

	    /*
	    myPath1.cubicTo(110,102,130,80,130,62.5f);
	    myPath1.cubicTo(130,62.5f,130,25,100,25);
	    myPath1.cubicTo(85,25,75,37,75,40);    
	    
	    canvas.drawPath(myPath1, paint);
	    */
		   
	    	canvas.drawColor(Color.TRANSPARENT);
	  		paint.reset();
	  		paint.setAntiAlias(true);
	  		//paint.setShadowLayer(10.0f, 0.0f, 0.0f, Color.parseColor("#90000000"));
	  		
	  		//paint.setStyle(Style.STROKE);
	  		path.reset();
	  		
	  		float currentAngle = 270;
	  		float currentSweep = 0;
	  		int totalValue = 0;
	  		
	  		
	  		
	  		
	  		for (CopyOfPieSlice slice : slices){
	  			totalValue += slice.getValue();
	  		}
	  		
	  		
	  		int count = 0;
	  		for (CopyOfPieSlice slice : slices){

	  			Path p = Path();
	  			p.moveTo(midX, midY);
	  			paint.setColor(slice.getColor());
	  			currentSweep = (slice.getValue()/totalValue)*(360);
	  			p.arcTo(RectF(midX, midY, radius), currentAngle+padding, currentSweep - padding);
	  		//	p.arcTo(RectF(midX, midY, innerRadius), (currentAngle+padding) + (currentSweep - padding), -(currentSweep-padding));
	  			p.close();
	  			
	  			slice.setPath(p);
	  			slice.setRegion(Reigon(midX, midY, radius));
	  			
	  		    canvas.drawPath(p, paint);

	  			
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
	  			
	  			currentAngle = currentAngle+currentSweep;
	  			
	  			count++;
	  		}
		


	}


	
	
	/*
	public void onDraw(Canvas canvas) {
		
		Paint paint = new Paint() {
		    {
		        setStyle(Paint.Style.STROKE);
		        setStrokeCap(Paint.Cap.ROUND);
		        setStrokeWidth(3.0f);
		        setAntiAlias(true);
		    }
		};
		final Path path = new Path();
		path.moveTo(330, 200);

		final float x2 = (100 + 330) / 2;
		final float y2 = (100 + 200) / 2;
		        path.quadTo(x2, y2, 100, 100);
		canvas.drawPath(path, paint);
		
		
		
		 * 
		 * Paint paint = new Paint() {
    {
        setStyle(Paint.Style.STROKE);
        setStrokeCap(Paint.Cap.ROUND);
        setStrokeWidth(3.0f);
        setAntiAlias(true);
    }
};
final Path path = new Path();
path.moveTo(x1, y1);

final float x2 = (x3 + x1) / 2;
final float y2 = (y3 + y1) / 2;
        path.quadTo(x2, y2, x3, y3);
canvas.drawPath(path, paint);


	    DrawLikes3(this, 50, 50, null, null);

		canvas.drawColor(Color.TRANSPARENT);
		paint.reset();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(5);
		float midX, midY, radius, innerRadius;
		path.reset();
		
		float currentAngle = 180;
		float currentSweep = 0;
		int totalValue = 0;
		
		midX = getWidth()/2;
		midY = getHeight()/2;
		if (midX < midY){
			radius = midX;
		} else {
			radius = midY;
		}
		radius -= padding;
		innerRadius = radius - thickness;
		
		for (CopyOfPieSlice slice : slices){
			totalValue += slice.getValue();
		}
		
		
		int count = 0;
		for (CopyOfPieSlice slice : slices){
			Path p = Path();
			paint.setColor(slice.getColor());
			currentSweep = (slice.getValue()/(totalValue/2))*(180);
			p.arcTo(RectF(midX-radius/2+(count*(radius-1)), midY, radius/2), currentAngle+padding, currentSweep - padding);
			
			//p.arcTo(RectF(midX + 50, midY+radius/2, radius), (currentAngle+padding) + (currentSweep - padding), -(currentSweep-padding));
			//p.close();
	      
			
			slice.setPath(p);
			slice.setRegion(Reigon(midX, midY, radius));
			
			canvas.drawPath(p, paint);

			
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
			
			//currentAngle = currentAngle+currentSweep;
			
			count++;
		}
		
		
		
		drawCompleted = true;
	}
	
	*/
	
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
			for (CopyOfPieSlice slice : slices){
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
	
	public ArrayList<CopyOfPieSlice> getSlices() {
		return slices;
	}
	public void setSlices(ArrayList<CopyOfPieSlice> slices) {
		this.slices = slices;
		postInvalidate();
	}
	public CopyOfPieSlice getSlice(int index) {
		return slices.get(index);
	}
	public void addSlice(CopyOfPieSlice slice) {
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

}
