package dev.vision.relationshipninjas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.Classes.Relationship.TYPE;
import dev.vision.relationshipninjas.Classes.USER;
import dev.vision.relationshipninjas.Fragments.RelationFragment;

public class Activity_History extends ActionBarActivity {
	ProgressDialog giftsPd;

	protected boolean fadable = true;

	protected boolean relations;
	public GiftsAdapter giftsAdapter;
	protected ArrayList<Item> gifts = new ArrayList<Item>();
	private ListView mListView;

	
    @SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
	        Window w = getWindow();
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);   
        }
        
        setContentView(R.layout.activity_history);
        mListView = (ListView) findViewById(R.id.list);
        
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        

        final View v = getLayoutInflater().inflate(R.layout.list_row, null);
    	
		v.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 700));//.height=200;
		Display display = getWindowManager().getDefaultDisplay();

		//final RoundedImage ri =((RoundedImage) v.findViewById(R.id.imageView1));
		Point p = new Point();
		display.getSize(p);

		viewWidth = p.x-300;
		viewHeight = 660;
        giftsAdapter = new GiftsAdapter(this, gifts, ImageLoader.getInstance(),viewWidth , viewHeight);
        mListView.setAdapter(giftsAdapter);
        String rid = getIntent().getExtras().getString("rId");
 	    new Load_Gifts().execute(rid);

        
        
        
    }

    @Override
	public void onAttachedToWindow(){
		super.onAttachedToWindow();
		
	}
    
    class Load_Gifts extends AsyncTask<Object, Void, Boolean>
	{
    	@Override
		public void onPreExecute(){
			super.onPreExecute();
			giftsPd = ProgressDialog.show(Activity_History.this, null, "Loading Relation History!!");
			giftsPd.show();
		}
		

		@Override
		protected Boolean doInBackground(Object... params) {
			gifts.clear();
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.PAST);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
				json.put("operation", "getpassed");
				json.put("relationshipid", params[0]);
				//json.put("eventid", params[1]);
				//json.put("startdate", "1900-01-02");
				//json.put("max", "2");

				request.setEntity(new StringEntity(json.toString()));
				HttpResponse response = client.execute(request);
			 
				
				if(response.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					Log.d("Result", result.toString());
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					if(success){
						JSONArray events = js.getJSONArray("events");
						for(int j= 0; j < events.length(); j++){
							JSONObject event = (JSONObject) events.get(j);
							JSONArray jsa = event.getJSONArray("items");
							for(int i= 0; i < jsa.length(); i++){
								JSONObject r = (JSONObject) jsa.get(i);
								Item it = new Item(r);
								gifts.add(it);
							}
						}
						
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return success;
		}

		@Override
		public void onPostExecute(Boolean result){
			if (mListView.getAdapter() instanceof GiftsAdapter) {
    			giftsAdapter = (GiftsAdapter) mListView.getAdapter();
    		}
			
			giftsAdapter.clear(gifts);
	        if(giftsPd.isShowing())
	        	giftsPd.dismiss();
 
		}
		   
	}
    

	@Override
	public void onResume(){
		super.onResume();
	}
	

	int viewWidth;
	int viewHeight;
	


		
}