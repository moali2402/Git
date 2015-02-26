package dev.vision.split.it;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.android.gms.maps.model.LatLng;

import dev.vision.split.it.extras.APP;
import dev.vision.split.it.user.Customer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

public class Restaurant implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5722584938507892706L;
	private String name;
	private String restaurant_id;
	private String phoneNo;
	private float rating;
	private long visitors;
	private Menu m;
	private LinkedList<Table> Tables = new LinkedList<Table>();
	private Table t;
	private byte[] byteImage;
	private boolean featured;


	private Restaurant_Interface rsi;
	private double lat;
	private double lng;
	private transient Bitmap image;

	public Restaurant(String id, String name, float f, long visitors, String menu) {
		super();
		this.name = name;
		this.rating = f;
		this.visitors = visitors;
		this.restaurant_id = id;
		this.m = new Menu(menu);
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
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

	/**
	 * @return the visitors
	 */
	public long getVisitors() {
		return visitors;
	}

	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(long visitors) {
		this.visitors = visitors;
	}

	/**
	 * @return the restaurant_id
	 */
	public String getRestaurant_id() {
		return restaurant_id;
	}

	/**
	 * @param restaurant_id the restaurant_id to set
	 */
	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	// 1
	public void createNewTable(String virtual_id){
		t = new Table( APP.ME, getRestaurant_id(), virtual_id );
		joinTable(t, APP.ME);
	}
	
	// 2
	public void setTableNo(int no){
		t.setNumber(String.valueOf(no));
	}
	
	public LinkedList<Table> getTables(){
		return Tables;
	}
	
	public void joinTable(int number){
		t = Tables.get(number);
		joinTable(t, APP.ME);
	}
	
	public void leaveTable() {
		leaveTable(t, APP.ME);
	}
	
	public void joinTable(int number, Customer c){
		Table t = Tables.get(number);
		t.JoinTable(c);
		rsi.onTableChange();
	}
	
	public void leaveTable(int number, Customer c) {
		Table t = Tables.get(number);
		t.LeaveTable(c);
		rsi.onTableChange();
	}
	
	/**
	 * @return the featured
	 */
	public boolean isfeatured() {
		return featured;
	}

	/**
	 * @param featured the featured to set
	 */
	public void setfeatured(boolean featured) {
		this.featured = featured;
	}

	public void setTables(LinkedList<Table> tables) {
		Tables = new LinkedList<Table>(tables);
	}
	

	public void setOnTableListener(Restaurant_Interface restaurant_Activity) {
		this.rsi = restaurant_Activity;
	}

	public interface Restaurant_Interface {

		//void onjoinTable(int number);
		//void onleaveTable(int number);
		
		void onTableChange();
		void onIJoin();

	}

	private void joinTable(Table t, Customer c){
		t.JoinTable(c);
		rsi.onTableChange();
	}
	
	private void leaveTable(Table t, Customer c) {
		if(c.hasJoined(t)){
			t.LeaveTable(c);
			rsi.onTableChange();
		}
	}

	public String getNumber() {
		return phoneNo;
	}

	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}

	public void setLatLng(double lt, double ln) {
		lat = lt;
		lng = ln;
	}
	
	public com.google.android.maps.GeoPoint getGeoPoint() {
		return GeoPoint.from(new LatLng(lat, lng));
	}

	public Bitmap getImage() {
		if(image==null && byteImage!=null)
		image = BitmapFactory.decodeByteArray(byteImage,0,byteImage.length);        
		return image;
	}
	public void setImage(Bitmap bitmap) {
		bitmap = toGrayscale(bitmap);
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
		byteImage = bs.toByteArray();
		image=bitmap;
	}
	
	public Bitmap toGrayscale(Bitmap bmpOriginal)
	{        
	    int width, height;
	    height = bmpOriginal.getHeight();
	    width = bmpOriginal.getWidth();    

	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    Canvas c = new Canvas(bmpGrayscale);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bmpOriginal, 0, 0, paint);
	    return bmpGrayscale;
	}

	
	
}
