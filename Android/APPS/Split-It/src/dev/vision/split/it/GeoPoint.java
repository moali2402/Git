package dev.vision.split.it;

import java.io.Serializable;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GeoPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2850733302289757583L;

	public static com.google.android.maps.GeoPoint from(LatLng ll) {
		 int lat = (int) (ll.latitude * 1E6);
	     int lng = (int) (ll.longitude * 1E6);
	     return new com.google.android.maps.GeoPoint(lat, lng);
	     
	}
	
	public static com.google.android.maps.GeoPoint from(Location location) {      
	     int lat = (int) (location.getLatitude() * 1E6);
	     int lng = (int) (location.getLongitude() * 1E6);
	     return new com.google.android.maps.GeoPoint(lat, lng);
	}
		     
	

}
