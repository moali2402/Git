package dev.vision.split.it.user;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

import android.graphics.Bitmap;
import dev.vision.split.it.Order;
import dev.vision.split.it.Table;


public class ME extends Customer{
	
	public void newOrder(Order o) {
		orderHistory.add(o);
	}

}
