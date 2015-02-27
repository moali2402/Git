package dev.vision.voom;

import android.app.Application;
import com.parse.Parse;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        Parse.initialize(this, CREDENTIALS.PARSE_APP_ID, CREDENTIALS.PARSE_CLIENT_KEY);
    }
}
