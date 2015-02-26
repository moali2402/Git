package dev.vision.relationshipninjas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class Splash_Screen extends Activity{
	
	protected boolean stopped;
	Handler h = new Handler();
	@SuppressLint("InlinedApi") 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
	        Window w = getWindow();
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);   
        }
		setContentView(R.layout.splash);	
	}
	
	@Override
	public void onResume(){
		super.onResume();
		stopped=false;
		h.removeCallbacksAndMessages(null);
		
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(!stopped)
				{				
					if(Static.isLogged(Splash_Screen.this)){
			        	startActivity(new Intent().setClass(Splash_Screen.this,Main_Activity.class));
			        }else{
			        	startActivity(new Intent().setClass(Splash_Screen.this,Login.class));
			        }
	
		        	overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		        	finish();
				}
			}
		}, 2000);
	}
	
	public void onDestroy(){
		h.removeCallbacksAndMessages(null);
		stopped=true;
		super.onDestroy();
	}
	
	public void onPause(){
		h.removeCallbacksAndMessages(null);
		stopped=true;
		super.onPause();
	}

}
