package com.coudriet.snapnshare.android;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

public class MainApplicationStartup extends Application {
	
	@Override
	public void onCreate() { 
		super.onCreate();
	    Parse.initialize(this, "Parse Applications Id", "Parse Client Key");
	    
	    PushService.setDefaultPushCallback(this, MainActivity.class);
	}
}
