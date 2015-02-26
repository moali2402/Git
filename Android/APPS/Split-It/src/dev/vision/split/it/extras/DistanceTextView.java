/**
 * 
 */
package dev.vision.split.it.extras;

import java.text.DecimalFormat;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;

/**
 * @author Mo
 *
 */
public class DistanceTextView extends GillsansTextView {

	/**
	 * @param context
	 */
	public DistanceTextView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public DistanceTextView(Context context, AttributeSet attrs) {
		super(context, attrs,0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DistanceTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}
	
	private void setup() {
	}

	/**
	 * Calculate the distance between 2 points based on their GeoPoint coordinates. <br>
	 * Return the value in Km or miles based on the unit input
	 * @param gp1 (GeoPoint): First point.
	 * @param gp2 (GeoPoint): Second point.
	 * @param unit (char): Unit of measurement: 'm' for miles and 'k' for Km.
	 * @return (double): The distance in miles or Km.
	 */
	public void getDistance(GeoPoint gp1, GeoPoint gp2, char unit)
	{
		String un = unit == 'm' ? "mi" : "km";
	    //Convert from degrees to radians
	    final double d2r = Math.PI / 180.0;

	    //Change lat and lon from GeoPoint E6 format
	    final double lat1 = gp1.getLatitudeE6() / 1E6;
	    final double lat2 = gp2.getLatitudeE6() / 1E6;
	    final double lon1 = gp1.getLongitudeE6() / 1E6;
	    final double lon2 = gp2.getLongitudeE6() / 1E6;

	    //The difference between latitudes and longitudes
	    double dLat = Math.abs(lat1 - lat2) * d2r;
	    double dLon = Math.abs(lon1 - lon2) * d2r;

	    double a = Math.pow(Math.sin(dLat / 2.0), 2) 
	            + Math.cos(lat1 * d2r) * Math.cos(lat2 * d2r)
	            * Math.pow(Math.sin(dLon / 2.0), 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    DecimalFormat f = new DecimalFormat("##.#");
	    String formattedValue = f.format(((unit == 'm' ? 3956 : 6367) * c));
	    //Return the distance
	    setText(""+ formattedValue + un );
	} //End getDistance()
	
	


}
