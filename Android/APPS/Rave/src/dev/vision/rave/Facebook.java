package dev.vision.rave;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.*;

public class Facebook {
	static String name;
	static String user;
	static String id;
	public static void initialise(final Context c){

    // start Facebook Login
    Session.openActiveSession((Activity) c, true, new Session.StatusCallback() {

      // callback when session changes state
      @Override
      public void call(Session session, SessionState state, Exception exception) {
        if (session.isOpened()) {
  	      Constants.saveData(c, DataHolder.SESSION, session.getAccessToken());
  	      /*
  	      Session s = Session.getActiveSession();
	      Session.NewPermissionsRequest newPermissionsRequest = new Session
	    		  .NewPermissionsRequest((Activity) c, Arrays.asList("friends_status","read_stream"));
	      s.requestNewReadPermissions(newPermissionsRequest);
	      */
          // make request to the /me API
          Request.newMeRequest(session, new Request.GraphUserCallback() {
        	  
        	  // callback after Graph API response with user object
        	  @Override
        	  public void onCompleted(GraphUser user, Response response) {
        	    if (user != null) {
        	      Facebook.name = user.getName();
        	      Facebook.id = user.getId();

        	      Constants.saveData(c, Constants.USER_ID, Facebook.id);
        	      Constants.saveData(c, Constants.DISPLAY_NAME, Facebook.name);
        	      Constants.saveData(c, Constants.BIRTH_DATE, user.getBirthday());
        	      Constants.saveData(c, Constants.PROFILE_PIC, "http://graph.facebook.com/"+Facebook.id+"/picture?type=large");
        	    //  Constants.saveData(c, Constants.COVER_PIC, "https://graph.facebook.com/"+Facebook.id+"?fields=cover?");
        	      Constants.saveData(c, Constants.SEX, (String) user.getProperty("gender"));
        	    
        	      Toast.makeText(c, Facebook.name + " / " +  Facebook.id , Toast.LENGTH_SHORT).show();

        	    }
        	  }
        	}).executeAsync();
        }
      }
    });
    
    /*
    Session.openActiveSession((Activity) c, true, new Session.StatusCallback() {

        // callback when session changes state
        @Override
        public void call(Session session, SessionState state, Exception exception) {
          if (session.isOpened()) {

    
        	  new Request(session, "/me/interests", null, HttpMethod.GET,
		    	    new Request.Callback() {
		    	        public void onCompleted(Response response) {
		    	        }
		    	    }
		    	).executeAsync();
          }
        }
      });
    
    String fqlQuery = "SELECT music, books, movies FROM user where uid ="+friendID;
        Bundle bundle = new Bundle();
        bundle.putString("q", fqlQuery);
        Request request = new Request(activeSession, "/fql", bundle, HttpMethod.GET, 
            new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    // TODO Auto-generated method stub
                    Log.i("INFO", response.toString());
                }
            });
        Request.executeBatchAsync(request);
        */
  }

	
}