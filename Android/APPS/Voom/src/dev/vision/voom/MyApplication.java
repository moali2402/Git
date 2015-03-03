package dev.vision.voom;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import dev.vision.voom.classes.Contact;
import dev.vision.voom.parse.ParseMessage;
import dev.vision.voom.parse.Profiles;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParseMessage.class);
        ParseUser.registerSubclass(Contact.class);
        ParseObject.registerSubclass(Profiles.class);
        Parse.initialize(this, CREDENTIALS.PARSE_APP_ID, CREDENTIALS.PARSE_CLIENT_KEY);
    }
}
