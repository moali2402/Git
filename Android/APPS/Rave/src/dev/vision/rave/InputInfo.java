package dev.vision.rave;

import java.util.Arrays;
import java.util.List;
import com.facebook.Session;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.TextView;

public class InputInfo extends Activity{
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
	        Window w = getWindow();
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);   
        }     
      
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.startup_send);
		overrideFonts(this, findViewById(R.id.RelativeLayout1));
		
	}

	private void overrideFonts(final Context context, final View v) {
	      try {
	        if (v instanceof ViewGroup) {
	        	ViewGroup vg = (ViewGroup) v;
	          
	             for (int i = 0; i < vg.getChildCount(); i++) {
		             View child = vg.getChildAt(i);
		             overrideFonts(context, child);
	             }
	 
	        } else if (v instanceof TextView ) {
	          ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "AvantGardeITCbyBTBook.otf"));
	        }
	 
	      } catch (Exception e) {
	      }
	}
	
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Session s = Session.getActiveSession();
      s.onActivityResult(this, requestCode, resultCode, data);
      Session.NewPermissionsRequest newPermissionsRequest = new Session
    		  .NewPermissionsRequest(this, Arrays.asList("friends_status","read_stream")).setRequestCode(requestCode);
      s.requestNewReadPermissions(newPermissionsRequest);
    return;
  }

  public static void requestPublishPermissions(Activity activity, Session session, List<String> permissions,  
		    int requestCode) {
		    if (session != null) {
		        Session.NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(activity, permissions)
		        .setRequestCode(requestCode);
		        session.requestNewPublishPermissions(reauthRequest);
		    }
		}

		public static void requestReadPermissions(Activity activity, Session session, List<String> permissions,  
		    int requestCode) {
		    if (session != null) {
		        Session.NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(activity, permissions)
		        .setRequestCode(requestCode);
		        session.requestNewReadPermissions(reauthRequest);
		    }
		}
}
