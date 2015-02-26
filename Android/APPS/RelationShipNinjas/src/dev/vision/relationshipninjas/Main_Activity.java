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
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.Classes.Relationship.TYPE;
import dev.vision.relationshipninjas.Classes.Relationships.DataChangeListener;
import dev.vision.relationshipninjas.Classes.USER;
import dev.vision.relationshipninjas.Fragments.MenuFragment;
import dev.vision.relationshipninjas.Fragments.RelationFragment;

public class Main_Activity extends BaseSampleActivity {
	ProgressDialog relationPd;

	protected boolean fadable = true;

	protected boolean relations;
	public GiftsAdapter giftsAdapter;
	protected ArrayList<Item> gifts;
	private Fragment mContent;

	public SlidingMenu menu ;
	
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
        
        setContentView(R.layout.content_frame);
        menu = getSlidingMenu();

    	// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			menu.setSlidingEnabled(true);
		}
		
		// set the Behind View Fragment
		
		
		

        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidth(20);
        menu.setBehindOffset(200);
        menu.setFadeDegree(0.55f);
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setFitsSystemWindows(true);
        menu.setBackgroundColor(Color.parseColor("#ff8380"));
        
        final MenuFragment m = new MenuFragment();
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, m)
		.commit();	
        

        ChangeFragments();

        Static.rs.setOnDataChangeListener(new DataChangeListener() {
			
			@Override
			public void refresh() {
				m.refresh();
			}
		});
        
        
    }
    
    /*

    public class Relationship_AsyncGetAll extends AsyncTask<Object, Void, Boolean>{
		ProgressDialog pd;

		int id;

		private ImageLoader imageLoader = ImageLoader.getInstance();
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(Main_Activity.this, null, "Loading Your Relationships!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
		    
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.RELATIONSHIPS);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
				json.put("operation", "getall");

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
						JSONArray jsa = js.getJSONArray("relationships");
						for(int i= 0; i < jsa.length(); i++){
							JSONObject r = (JSONObject) jsa.get(i);
							Relationship relation = new Relationship();
							relation.setId(r.optString("id"));
							relation.setName(r.optString("name"));
							relation.setType(TYPE.Match(r.optString("type")));
							relation.setRating(r.optString("rating"));
							relation.setFirstname(r.optString("firstname"));
							relation.setLastname(r.optString("lastname"));
							relation.setMiddlename(r.optString("middlename"));
							relation.setEmail(r.optString("email"));
							relation.setKids(r.optString("kids"));
							relation.setMyersbriggs(r.optString("myersbriggs"));
							relation.setGender(r.optString("gender"));
							relation.setFacebookid(r.optString("facebookid"));
							relation.setFinalizedstatus(r.optBoolean("finalizedstatus"));
							relation.setCover(r.optString("cover"));
							relation.setDefaultcover(r.optBoolean("isdefaultcover"));
							relation.setImage(r.optString("image"));
							relation.setDefaultimage(r.optBoolean("isdefaultimage"));
							relation.setProgress(r.optInt("progress"));
							 
							Static.rs.add(relation);
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
			super.onPostExecute(result);
			 if (Static.rs.size() > 0){
				Fragment newContent = RelationFragment.NewInstance(0);
				if (newContent != null)
					switchContent(newContent);
			 }else{
				 ChangeFragments();
			 }
			
			
	        if(pd.isShowing())
				pd.dismiss();
		}
	}

*/
	private void ChangeFragments() {
		// set the Above View Fragment
		mContent = RelationFragment.NewInstance();	
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();		
	}


	@Override
	public void onResume(){
		super.onResume();
	}
	
	
	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		//.addToBackStack(null)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				menu.showContent();
			}
		}, 50);
	}
	


	
		
	public class Relationship_AsyncGetById extends AsyncTask<Object, Void, Boolean>{
		ProgressDialog pd;

		int id;
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(Main_Activity.this, null, "Loading Your Relationships!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
		    
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.RELATIONSHIPS);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
				json.put("operation", "get");
				json.put("id", params[4]);

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
						JSONArray jsa = js.getJSONArray("relationship");
						for(int i= 0; i < jsa.length(); i++){
							JSONObject r = (JSONObject) jsa.get(i);
							Relationship relation = new Relationship();
							relation.setId(r.optString("id"));
							relation.setName(r.optString("name"));
							relation.setType(TYPE.valueOf(r.optString("type")));
							relation.setRating(r.optString("rating"));
							relation.setFirstname(r.optString("firstname"));
							relation.setLastname(r.optString("lastname"));
							relation.setMiddlename(r.optString("middlename"));
							relation.setEmail(r.optString("email"));
							relation.setKids(r.optString("kids"));
							relation.setMyersbriggs(r.optString("myersbriggs"));
							relation.setGender(r.optString("gender"));
							relation.setFacebookid(r.optString("facebookid"));
							relation.setFinalizedstatus(r.optBoolean("finalizedstatus"));
							relation.setCover(r.optString("cover"));
							relation.setDefaultcover(r.optBoolean("isdefaultcover"));
							relation.setImage(r.optString("image"));
							relation.setDefaultimage(r.optBoolean("isdefaultimage"));
							relation.setProgress(r.optInt("progress"));
							
							//rs.add(relation);
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
			super.onPostExecute(result);
			
			
		}
	}
	}