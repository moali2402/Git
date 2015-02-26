package dev.vision.split.it.activities;

import java.util.ArrayList;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import dev.vision.split.it.R;
import dev.vision.split.it.Restaurant;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.fragments.Main_Fragment;

public class ShowMapActivity extends FragmentActivity implements OnMarkerClickListener{

    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE = new PorterDuffXfermode(Mode.DST_IN);

	private GoogleMap googleMap;
    public static Context context;
    
    ArrayList<Restaurant> nearRestaurants = new ArrayList<Restaurant>();

	private ListView restList;

	
    @Override
    protected void onCreate(Bundle icicle) {
        // TODO Auto-generated method stub
        super.onCreate(icicle);
        context = getApplicationContext();
        setContentView(R.layout.main_page);
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				MapsInitializer.initialize(ShowMapActivity.this);
		        
		        setupDemo();
		        
		        showList();
		        showMap();				
			}
		}, 1500);
        
        
    }

    
    private void setupDemo() {
    	 String serviceString=Context.LOCATION_SERVICE;
         LocationManager locationManager;    //remember to import LocationManager
         locationManager=(LocationManager)getSystemService(serviceString);

    	 locationManager.requestLocationUpdates(
                 LocationManager.GPS_PROVIDER, 0, 0, 
                 new LocationListener() {
                   public void onLocationChanged(Location location) {
                       APP.ME.setLocation(location);

                   }
                   public void onProviderDisabled(String provider) {}
                   public void onProviderEnabled(String provider) {}
                   public void onStatusChanged(String provider, int status, 
                                               Bundle extras) {}
                 }
               );

	    String provider=LocationManager.GPS_PROVIDER;
	    Location l= locationManager.getLastKnownLocation(provider);
 


		APP.ME.setName("Mo Adel");
		APP.ME.setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.bby)).getBitmap());
        APP.ME.setLocation(l);

    	
    	
    	Restaurant rs = new Restaurant(APP.DEBUG_ID, "Nandos", 5, 1000, null);
        rs.setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.nandos)).getBitmap());
        rs.setLatLng(51.762533, -0.239338);
        nearRestaurants.add(rs);    
        

        rs = new Restaurant(APP.DEBUG_ID, "Chiquito", 3.5f, 1000, null);
        rs.setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.kitchog)).getBitmap());
        rs.setLatLng(51.763041, -0.238695);        
        nearRestaurants.add(rs);  		

        rs = new Restaurant(APP.DEBUG_ID, "Subway", 3.5f, 1000, null);
        rs.setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.slide)).getBitmap());
        rs.setLatLng(51.7626498,-0.2306110);        
        nearRestaurants.add(rs);
        

        rs = new Restaurant(APP.DEBUG_ID, "McDonald's", 4.5f, 1000, null);
        rs.setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.mcdonalds)).getBitmap());
        rs.setLatLng(51.762958, -0.237879);
        rs.setfeatured(true);
        nearRestaurants.add(0,rs);   
        

        rs = new Restaurant(APP.DEBUG_ID, "BurgerKing", 4f, 1000, null);
        rs.setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.slide)).getBitmap());
        rs.setLatLng(51.762762, -0.238458);        
        nearRestaurants.add(rs);   
        

        rs = new Restaurant(APP.DEBUG_ID, "Harvester", 2.5f, 1000, null);
        rs.setImage(((BitmapDrawable)getResources().getDrawable(R.drawable.slide)).getBitmap());
        rs.setLatLng(51.761905, -0.240164);
        rs.setfeatured(true);

        nearRestaurants.add(rs);   
        
	}

	private void showList() {
    	restList = ((Main_Fragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment)).showTables(nearRestaurants);
    	
	}

	public void showMap() {
        // TODO Auto-generated method stub

        try {
        	googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview))
                    .getMap();
        	

            
    		addRestaurants();
    		addMe();

    		
    		googleMap.setOnMarkerClickListener(this);
    		
    		
    		CameraPosition cameraPosition = new CameraPosition.Builder().target(APP.ME.getLatLng()).zoom(14).build();
     
    		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    		//googleMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
    		//googleMap.getUiSettings().setZoomGesturesEnabled(false);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void addRestaurants() {
		IconGenerator ic = new IconGenerator(this);
		ic.setStyle(IconGenerator.STYLE_PURPLE);
		ic.setColor(getResources().getColor(R.color.apptheme_color));
		ic.setContentRotation(-90);
		ic.setRotation(90);
		for(Restaurant rs : nearRestaurants){
			LatLng rll = new LatLng(rs.getLat(), rs.getLng());
			MarkerOptions rsm = new MarkerOptions().position(rll);
			
			Bitmap rsBitmap = ic.makeIcon(rs.getName());
			//tc.setColor(getResources().getColor(R.color.apptheme_color));
			//Bitmap b  = drawMarker(rs.getImage());
			//Bitmap b = scaleImage(rs.getImage(), 200);
    		// Changing marker icon
			rsm.icon(BitmapDescriptorFactory.fromBitmap(rsBitmap));
			//b.recycle();
    		// adding marker
			//rsm.title(rs.getName());
    		googleMap.addMarker(rsm);
		}
	}

	private void addMe() {
		// latitude and longitude
		
		// create marker

		MarkerOptions marker = new MarkerOptions().position(APP.ME.getLatLng());
		IconGenerator tc = new IconGenerator(this);
		HexagonImageView hx = new HexagonImageView(this);
		tc.setBackground(null);
		tc.setContentView(hx);
		
		Bitmap b = scaleImage(APP.ME.getImage(), 150);

		hx.setImageBitmap(b);
		Bitmap userPic = tc.makeIcon();
		b.recycle();
		marker.icon(BitmapDescriptorFactory.fromBitmap(userPic));

		// Changing marker icon
		
		//marker.icon(BitmapDescriptorFactory.fromBitmap(APP.ME.getMarker()));
		// adding marker
		marker.flat(false);
		marker.title("You are Here");		
		googleMap.addMarker(marker);

	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		marker.showInfoWindow();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				marker.hideInfoWindow();				
			}
		}, 3000);
		return true;
	}


	private Bitmap scaleImage(Bitmap b, int lessSideSize) {

        float sc = 0.0f;
        int scale = 1;
        // if image height is greater than width
        if (b.getHeight() > b.getWidth()) {
            sc = b.getHeight() / lessSideSize;
            scale = Math.round(sc);
        }
        // if image width is greater than height
        else {
            sc = b.getWidth() / lessSideSize;
            scale = Math.round(sc);
        }

        // Decode with inSampleSize
        
		Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth() / scale ,b.getHeight() / scale, false);

        return bhalfsize;
    }
	
	
	public Bitmap drawMarker(Bitmap b){
		Bitmap.Config conf = Bitmap.Config.RGB_565;
		Bitmap bmp = Bitmap.createBitmap(200, 200, conf);
		Canvas canvas1 = new Canvas(bmp);
		
		// paint defines the text color,
		// stroke width, size
		Paint color = new Paint();
		color.setColor(Color.BLACK);
		b = scaleImage(b, 200);
		canvas1.drawBitmap(b, 0,0, color);
		b.recycle();

		//modify canvas

		return bmp;
	}
	
	
	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
	    Bitmap result = null;
	    try {
	        result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(result);

	        int color = 0xff424242;
	        Paint paint = new Paint();
	        Rect rect = new Rect(0, 0, 200, 200);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawCircle(50, 50, 50, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);

	    } catch (NullPointerException e) {
	    } catch (OutOfMemoryError o) {
	    }
	    return result;
	}
	
	
}
