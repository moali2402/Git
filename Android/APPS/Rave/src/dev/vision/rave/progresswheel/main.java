package dev.vision.rave.progresswheel;

import java.util.Calendar;

import dev.vision.rave.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


/**
 * A sample activity showing some of the functions of the progress bar 
 */
public class main extends Activity {
	boolean running;
	ProgressWheel pw_two;

	int progress = 0;
	Runnable r;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_wheel_activity);
        pw_two = (ProgressWheel) findViewById(R.id.progressBarTwo);
     
        //ShapeDrawable bg = new ShapeDrawable(new RectShape());
        //int[] pixels = new int[] { 0xFF2E9121, 0xFF2E9121, 0xFF2E9121,
         //   0xFF2E9121, 0xFF2E9121, 0xFF2E9121, 0xFFFFFFFF, 0xFFFFFFFF};
        //Bitmap bm = Bitmap.createBitmap(pixels, 8, 1, Bitmap.Config.ARGB_8888);
       // Shader shader = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        Calendar startDay = Calendar.getInstance();
        startDay.set(Calendar.DAY_OF_MONTH,1);
        startDay.set(Calendar.MONTH,3); // 0-11 so 1 less
        startDay.set(Calendar.YEAR, 2014);
        
        Calendar endDay = Calendar.getInstance();
        endDay.set(Calendar.DAY_OF_MONTH,1);
        endDay.set(Calendar.MONTH,9); // 0-11 so 1 less
        endDay.set(Calendar.YEAR, 2014);

        Calendar today = Calendar.getInstance();

        
      long total = endDay.getTimeInMillis() - startDay.getTimeInMillis();
      long burn = today.getTimeInMillis() - startDay.getTimeInMillis();

      final float burned = burn*360/total;  

      r = new Runnable() {
			@Override
			public void run() {
				running = true;
				while(progress<burned) {
					pw_two.incrementProgress();
					progress++;

					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				running = false;
			}
        };       
	}
	@Override
	public void onResume(){
		super.onResume();
		progress = 0;
		pw_two.resetCount();
		Thread s = new Thread(r);
		s.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		progress = 0;
		pw_two.stopSpinning();
		pw_two.resetCount();
	}
}
