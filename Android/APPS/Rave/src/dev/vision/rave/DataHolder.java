package dev.vision.rave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import dev.vision.rave.views.QuickShareTouch;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.widget.TextView;

public class DataHolder {
	protected static String SESSION = "Access_Token";

	public static String AccessToken;
	public static List<ResolveInfo> plainTextActivities;
	public static HashMap<String, ResolveInfo> htmlActivitiesPackages;
	public static boolean stopped = true;
	public static int PINK = Color.parseColor("#ff0060");
	public static int BLUE = Color.parseColor("#3d7cb7");
	public static int APP_BACKGROUND = Color.parseColor("#FFFFFF");
	public static int Text_Colour = getContrastColor(APP_BACKGROUND);

	public static QuickShareTouch qs;
	public static int TIMER = 20;
	public static int READINGS = 50;
	public static int qssize = 300;
	public static Bitmap picTaken;
	static float[] colorMatrix_Negative = { 
		    -1.0f, 0, 0, 0, 255, //red
		    0, -1.0f, 0, 0, 255, //green
		    0, 0, -1.0f, 0, 255, //blue
		    0, 0, 0, 1.0f, 0 //alpha  
		};

	public static ColorFilter colorFilter_Negative = new ColorMatrixColorFilter(colorMatrix_Negative);

	public static ArrayList<String> ToUpload = new ArrayList<String>();
	
	public static void shareFromGallery(Context c){
		
		Intent intent = new Intent();
		
		intent.setType("image/*");
		
		intent.setAction(Intent.ACTION_GET_CONTENT);
		
		((Activity) c).startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
	
	}
	
	
	private float dpFromPixels(Context c, float px)
	{
	    return px / c.getResources().getDisplayMetrics().density;
	}

	public static float pixelsFromDp(Context c, float dp)
	{
	    return dp * c.getResources().getDisplayMetrics().density;
	}
	
	public static int getContrastColor(int color) {
		// convert hex string to int
		int rgb = color;
		double y = (299 * Color.red(rgb) + 587 * Color.green(rgb) + 114 * Color.blue(rgb)) / 1000;
		  return y >= 128 ? Color.BLACK : Color.WHITE;
	}

	/*
	//One set and the other deducts in Draw
		private void DrawLikes(final PieGraph pg, int male, int female){
			final int first = male > female ? female : male;
			final int second = first == male ? female : male;
			final long firstMulti = first*READINGS;
			final long secondMulti = second*READINGS;
			
			pg.setPadd(1);
					
			final PieSlice slice = new PieSlice();
			final PieSlice slice1 = new PieSlice();
			if(first>0){
				slice.setColor(first == male ? BLUE : PINK);
				slice.setValue(firstMulti);
				pg.addSlice(slice);
			}
			if(second>0){
			
				slice1.setColor(slice.getColor() == PINK ? BLUE : PINK);
				slice1.setValue(0);
				pg.addSlice(slice1);
							
				final Handler h = new Handler();
							
				h.post(new Runnable() {
					
					@Override
					public void run() {
						if (slice1.getValue() != secondMulti) {
							slice1.setValue(slice1.getValue() + second);
							pg.postInvalidate();					
						    h.postDelayed(this, TIMER);
						}
					}
				});
			}
			
		}

		//White Leads both in Draw
		private void DrawLikes1(final PieGraph pg, int male, int female){
			final int first = male > female ? male : female;
			final int second = first == male ? female : male;
			final long firstMulti = first*READINGS;
			final long secondMulti = second*READINGS;
			final long TOTAL = firstMulti+ secondMulti;
			
			pg.setPadd(1);
			
			final PieSlice background = new PieSlice();
			background.setColor(Color.TRANSPARENT);
			background.setValue(TOTAL);
			pg.addSlice(background);
			
			final PieSlice slice = new PieSlice();
			final PieSlice slice1 = new PieSlice();
			if(first>0){
				slice.setColor(first == male ? BLUE : PINK);
				slice.setValue(0);
				pg.addSlice(slice);
			}
			if(second>0){
				slice1.setColor(slice.getColor() == PINK ? BLUE : PINK);
				slice1.setValue(0);
				pg.addSlice(slice1);
			}
			
			final Handler h = new Handler();
			
			h.post(new Runnable() {
				
				@Override
				public void run() {
					if (slice.getValue() != firstMulti)
						slice.setValue(slice.getValue() + first);
					if (slice.getValue() == firstMulti && slice1.getValue() != secondMulti)
						slice1.setValue(slice1.getValue() + second);
					if(background.getValue()>0)
						background.setValue(TOTAL-(slice.getValue()+slice1.getValue()));
					else pg.getSlices().remove(background);
					pg.postInvalidate();	
					if (slice.getValue() != firstMulti || slice1.getValue() != secondMulti)
				    h.postDelayed(this, TIMER);
				}
			});
			
		}
		
		
		//One Leads the other in Draw
		private void DrawLikes2(final PieGraph pg, int male, int female){
			final int first = male > female ? female : male;
			final int second = first == male ? female : male;
			final long firstMulti = first*READINGS;
			final long secondMulti = second*READINGS;
			//final long TOTAL = firstMulti+ secondMulti;
			
			pg.setPadd(1);
			
			final PieSlice background = new PieSlice();
			background.setColor(Color.TRANSPARENT);
			background.setValue(firstMulti);
			pg.addSlice(background);
			
			final PieSlice slice = new PieSlice();
			final PieSlice slice1 = new PieSlice();
			if(first>0){
				slice.setColor(first == male ? BLUE : PINK);
				slice.setValue(0);
				pg.addSlice(slice);
			}
			if(second>0){
				slice1.setColor(slice.getColor() == PINK ? BLUE : PINK);
				slice1.setValue(0);
				pg.addSlice(slice1);
			}
			
			final Handler h = new Handler();
			
			h.post(new Runnable() {
				
				@Override
				public void run() {
					if (slice.getValue() != firstMulti)
						slice.setValue(slice.getValue() + first);
					if (slice1.getValue() != secondMulti)
						slice1.setValue(slice1.getValue() + second);
					if(background.getValue()>0)
						background.setValue((firstMulti)-slice.getValue());
					else pg.getSlices().remove(background);
					pg.postInvalidate();	
					if (slice.getValue() != firstMulti || slice1.getValue() != secondMulti)
				    h.postDelayed(this, TIMER);
				}
			});
			
		}
		*/
	
	
	
	//White Leads both in Draw
	//One set and the other deducts in Draw
			static void DrawLikes(final PieGraph pg, int male, int female){
				final int first = male > female ? female : male;
				final int second = first == male ? female : male;
				final long firstMulti = first*READINGS;
				final long secondMulti = second*READINGS;
				
				pg.setPadd(1);
						
				final PieSlice slice = new PieSlice();
				final PieSlice slice1 = new PieSlice();
				if(first>0){
					slice.setColor(first == male ? BLUE : PINK);
					slice.setValue(firstMulti);
					pg.addSlice(slice);
				}
				if(second>0){
				
					slice1.setColor(slice.getColor() == PINK ? BLUE : PINK);
					slice1.setValue(0);
					pg.addSlice(slice1);
								
					final Handler h = new Handler();
								
					h.post(new Runnable() {
						
						@Override
						public void run() {
							if (slice1.getValue() != secondMulti) {
								slice1.setValue(slice1.getValue() + second);
								pg.postInvalidate();					
							    h.postDelayed(this, TIMER);
							}
						}
					});
				}
				
			}
	
		//One Leads the other in Draw
		public static void DrawLikes3(final PieGraph pg, int male, int female,final TextView  maleTxt, final TextView femaleTxt){
				final int first = male > female ? male : female;
				final int second = first == male ? female : male;
				final long firstMulti = first*READINGS;
				final long secondMulti = second*READINGS;
				//final TextView maleTxt = (TextView) findViewById(R.id.maleTxt);
				//final TextView femaleTxt = (TextView) findViewById(R.id.femaleTxt);
				//final long TOTAL = firstMulti+ secondMulti;
				
				pg.setPadd(1);
				
				final PieSlice background = new PieSlice();
				background.setColor(Color.TRANSPARENT);
				background.setValue(firstMulti);
				pg.addSlice(background);
				
				final PieSlice slice = new PieSlice();
				final PieSlice slice1 = new PieSlice();
				if(first>0){
					slice.setColor(first == male ? BLUE : PINK);
					slice.setValue(0);
					pg.addSlice(slice);
				}
				if(second>0){
					slice1.setColor(slice.getColor() == PINK ? BLUE : PINK);
					slice1.setValue(0);
					pg.addSlice(slice1);
				}
				
				final Handler h = new Handler();
				
				h.post(new Runnable() {
					

					@Override
					public void run() {
						if (slice.getValue() != firstMulti){
							slice.setValue(slice.getValue() + first);
							if(slice.getColor() == PINK) femaleTxt.setText("" + (int) slice.getValue()/READINGS);
							else maleTxt.setText("" + (int) slice.getValue()/READINGS);
						}
						if (slice1.getValue() != secondMulti){
							slice1.setValue(slice1.getValue() + second);
							if(slice1.getColor() == PINK) femaleTxt.setText("" + (int) slice1.getValue()/READINGS);
							else maleTxt.setText("" + (int) slice1.getValue()/READINGS);
						}
						if(background.getValue()>0)
							background.setValue((firstMulti)-slice.getValue());
						else pg.getSlices().remove(background);
							pg.postInvalidate();	
						if ( (slice.getValue() != firstMulti || slice1.getValue() != secondMulti) && !stopped){
					    h.postDelayed(this, TIMER);
						}else if ( stopped){
							maleTxt.setText("0");
							femaleTxt.setText("0");
						}
					}
				});
				
			}


		public static void ChangeColors(int color) {
			APP_BACKGROUND = color;
			Text_Colour = getContrastColor(APP_BACKGROUND);	
		}
		
}
