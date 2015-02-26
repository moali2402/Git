package dev.vision.license;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.widget.Toast;

public class LICENSE {
	
	static String EXPIRY_DATE = "20/12/2014";

	public static void isValid(Activity c){
		 try {
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        Date strDate  = sdf.parse(EXPIRY_DATE);
		        if (new Date().after(strDate)) {
		            Toast.makeText(c, "Your test license expired on " + EXPIRY_DATE, Toast.LENGTH_LONG).show();
		            c.finish();
		        }
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
	}

	public static void isValid(String EXPIRY_DATE , Activity c){
		 try {
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        Date strDate  = sdf.parse(EXPIRY_DATE);
		        if (new Date().after(strDate)) {
		            Toast.makeText(c, "Your test license expired on " + EXPIRY_DATE, Toast.LENGTH_LONG).show();
		            c.finish();
		        }
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
	}

}
