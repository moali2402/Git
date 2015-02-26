package dev.vision.rave;

import com.hipmob.gifanimationdrawable.GifAnimationDrawable;

import android.app.Application;
import android.content.Context;

public class App extends Application{

    private static Context mContext;
    static GifAnimationDrawable gif;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

		try {
			gif = new GifAnimationDrawable(getResources().openRawResource(R.raw.dancingg));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    public static Context getContext(){
        return mContext;
    }
    
    public static GifAnimationDrawable getGif(){
        return gif;
    }
}