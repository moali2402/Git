package dev.vision.split.it.user;

import java.util.ArrayList;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.google.android.gms.maps.model.LatLng;

import dev.vision.split.it.GeoPoint;
import dev.vision.split.it.Order;
import dev.vision.split.it.Table;
import android.graphics.Bitmap;
import android.location.Location;

public class Customer {
	String name;
	String id;
	String image_url;
	Bitmap image;
	int Rewards;
	ArrayList<Order> orderHistory;
	private Bitmap marker;
	static Table TABLE;
	public LatLng location;
	private double lat;
	private double lng;
	private LatLng ll;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the image_url
	 */
	public String getImage_url() {
		return image_url;
	}

	/**
	 * @param image_url the image_url to set
	 */
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}

	public Table getTable() {
		return TABLE;
	}

	public boolean hasJoined(Table t) {
		return TABLE != null && t != null && TABLE.getID().equals(t.getID());
	}
	
	public void joinedTable(Table t) {
		TABLE = t;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the rewards
	 */
	public int getRewards() {
		return Rewards;
	}

	/**
	 * @param rewards the rewards to set
	 */
	public void setRewards(int rewards) {
		Rewards = rewards;
	}

	/**
	 * @return the orderHistory
	 */
	public ArrayList<Order> getOrderHistory() {
		return orderHistory;
	}

	/**
	 * @param orderHistory the orderHistory to set
	 */
	public void setOrderHistory(ArrayList<Order> orderHistory) {
		this.orderHistory = orderHistory;
	}
	

	public Bitmap getMarker() {
		return marker;
	}
	
	public void setMarker(HexagonImageView im) {
		marker = im.get();
	}


	public com.google.android.maps.GeoPoint getGeoPoint() {
		return GeoPoint.from(new LatLng(lat, lng));
	}
	
	public void setLocation(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
		this.ll = new LatLng(lat, lng);
	}
	
	public void setLocation(Location l) {
		this.lat = l.getLatitude();
		this.lng = l.getLongitude();
		this.ll = new LatLng(lat, lng);
	}
	

	public LatLng getLatLng() {
		return ll;
	}

	
}
