package dev.vision.relationshipninjas.Fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.R.color;
import dev.vision.relationshipninjas.R.id;
import dev.vision.relationshipninjas.views.CircularImageResizable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ChartFragment extends Fragment {

	 public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	 public static final String LAYOUT_ID = "Layout_ID";
	 Relationship re;
	 ViewPager mPager;
	 private ImageView actionImg;
	private TextView actionTxt;
	private ImageLoader imageLoader = ImageLoader.getInstance();
		
	 public static ChartFragment newInstance(String message, int layid, Relationship r, ViewPager vp , ImageView acIm, TextView actx, ImageLoader im)
	 {

	   ChartFragment f = new ChartFragment();
	   f.setField(r, vp , acIm, actx, im);
	   Bundle bdl = new Bundle(1);

	   bdl.putString(EXTRA_MESSAGE, message);

	   bdl.putInt(LAYOUT_ID, layid);
	   
	   f.setArguments(bdl);
	   return f;

	 }

	private void setField(Relationship r, ViewPager vp, ImageView acIm, TextView actx,
			ImageLoader im) {
		re=r;
		mPager = vp;
		actionImg =acIm;
		actionTxt = actx;
		imageLoader = im;
	}

	CircularImageView profilePic;
	private TextView locationTxt;
	private TextView nameTxt;
	
	
	
	 @SuppressLint("NewApi") 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,

	   Bundle savedInstanceState) {

		 String message = getArguments().getString(EXTRA_MESSAGE);
	     int layoutId = getArguments().getInt(LAYOUT_ID);
		 View v = inflater.inflate(layoutId, container, false);
		 profilePic = (CircularImageView) v.findViewById(R.id.circularImageView);
		 nameTxt = (TextView) v.findViewById(R.id.name_txt_info);
		 locationTxt = (TextView) v.findViewById(R.id.location_txt_info);
		 return v;
	 }
		 
	 View v;
	 public void onViewCreated(View v, Bundle savedInstanceState){
		 super.onViewCreated(v, savedInstanceState);
		
		this.v = v;
	 }
	 
	 
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState){
		 super.onActivityCreated(savedInstanceState);
		 if(getImageView()!= null){

						
	        nameTxt.setText(re.getName());
	        //location_txt.setText(re.getName());
	        String ty =re.getType().toString();
	        actionTxt.setText("Your "+ ty.substring(0,1) + ty.substring(1).toLowerCase() +", "+re.getFirstname() + " " +re.getLastname().charAt(0) +"."  );
        	if(re.getProfile_pic() != null){
        		profilePic.setImageBitmap(re.getProfile_pic());
				actionImg.setImageBitmap(re.getProfile_pic());
        	}else
				imageLoader.displayImage(re.getImage(), profilePic, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						actionImg.setImageBitmap(loadedImage);
						re.setProfile_pic(loadedImage);
					}
					
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {}
				});
		        
		        imageLoader.loadImage("http://therelationshipninjas.com"+re.getCover(), new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						mPager.setBackgroundColor(getResources().getColor(R.color.vpi__bright_foreground_holo_dark));
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						mPager.setBackgroundColor(getResources().getColor(R.color.vpi__bright_foreground_holo_dark));
					}
					
					@SuppressLint("NewApi") @Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						mPager.setBackground(new BitmapDrawable(getResources(), loadedImage));
						re.setCover_pic(loadedImage);
					}
					
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						mPager.setBackgroundColor(getResources().getColor(R.color.vpi__bright_foreground_holo_dark));

					}
				});
		 }else {
			 
		 }
	 }
	 
	 @SuppressLint("NewApi") 
	 private void SetCharts() {
		    
			
	}

	public String ReadFromfile(String fileName, Context context) {
		 InputStream is = null;
		try {
			is = context.getAssets().open(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 int size = 0;
		try {
			size = is.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 byte[] buffer = new byte[size];
		 try {
			is.read(buffer);
			 is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		 String str = new String(buffer);
		 //str = str.replace("old string", "new string");
		    return str;
		}

	public CircularImageView getImageView() {
		return profilePic;
	}

	public TextView getNameTxtView() {
		// TODO Auto-generated method stub
		return nameTxt;
	}
	
	public TextView getLocationTxtView() {
		// TODO Auto-generated method stub
		return locationTxt;
	}

}
