package dev.vision.relationshipninjas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.Classes.Relationship.TYPE;

public class Login extends Activity implements OnClickListener {
	
	Fragment Welcome;
	Fragment Post;
	Fragment Exchange;
	Fragment Profile;
	List<Fragment> fList;
	private EditText email_text;
    @SuppressLint("InlinedApi") 
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.login);
        
        Button login = (Button) findViewById(R.id.login);
        TextView forgot = (TextView) findViewById(R.id.forgot);
        TextView signup = (TextView) findViewById(R.id.signup);

        email_text = ((EditText) findViewById(R.id.email_add));
        email_text.setText(API.user.getEmail());
        login.setOnClickListener(this);
        forgot.setOnClickListener(this);
        signup.setOnClickListener(this);

        
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
	        Window w = getWindow();
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        w.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        
	}
	@SuppressLint("NewApi") //API 9
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login:
			String email =  email_text.getText().toString();
			String pass = ((EditText) findViewById(R.id.pass_cred)).getText().toString();
			String role = "1015";
			new Login_Process().execute(email,pass,role);
			break;
		case R.id.forgot:
			Intent i = new Intent();
			i.setClass(this, Reset_Activity.class);
			String emailr = email_text.getText().toString();
			if(!emailr.isEmpty())
			i.putExtra("email", emailr);
			startActivityForResult(i, 1000);
			break;
		case R.id.signup:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.therelationshipninjas.com"));
	        startActivity(browserIntent);
	        break;
		}
	}
	
	public class Login_Process extends AsyncTask<String, Void, Boolean>{
		ProgressDialog pd;
		int id;
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(Login.this, null, "Please wait!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
		    
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.LOGIN);
			boolean success = false;
			try{
				// add request header
				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+params[0]+", nlauth_signature="+params[1]+", nlauth_role="+params[2]);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
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
						id = js.getInt("id");
						API.user.setEmail(params[0]);
						API.user.setPass(params[1]);
						API.user.setRole(params[2]);	
						Static.login(Login.this, params[0],params[1]);
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
			
			if(result.booleanValue()){
				Intent i = new Intent(Login.this,Main_Activity.class);
				startActivity(i);
				if(pd.isShowing())
					pd.dismiss();
				finish();
			}else{
				if(pd.isShowing())
					pd.dismiss();
				 new AlertDialog.Builder(Login.this)
			    .setTitle("Login Failed!!")
			    .setMessage("Please make sure you enter correct details and try again.")
			    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        }
			     })
			    .show();
			}
		}
	}
	
	/*
	
	public class Relationship_AsyncGetAll extends AsyncTask<String, Void, Boolean>{
		ProgressDialog pd;

		int id;
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(Login.this, null, "Please Wait!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
		    
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.RELATIONSHIPS);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+params[0]+", nlauth_signature="+params[1]+", nlauth_role="+params[2]);
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
						//id = js.getInt("id");
						API.user.setEmail(params[0]);
						API.user.setPass(params[1]);
						API.user.setRole(params[2]);	
						Static.login(Login.this, params[0],params[1]);
					
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
					
			if(result.booleanValue()){
				Intent i = new Intent(Login.this,Main_Activity.class);
				startActivity(i);
				if(pd.isShowing())
					pd.dismiss();
				finish();
			}else{
				if(pd.isShowing())
					pd.dismiss();
				 new AlertDialog.Builder(Login.this)
			    .setTitle("Login Failed!!")
			    .setMessage("Please make sure you enter correct details and try again.")
			    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        }
			     })
			    .show();
			}
			
		}
	}
	
	*/
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1000 && resultCode == RESULT_OK){
		    final TextView tx = (TextView) findViewById(R.id.note);
		    String email = data.getStringExtra("email");
		    String txt = tx.getText().toString().replace("email@address.com", email);
		    email_text.setText(email);
		    tx.setText(txt);
		    tx.setVisibility(View.VISIBLE);
		    tx.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					tx.setAnimation(AnimationUtils.loadAnimation(Login.this, android.R.anim.fade_out));
					tx.setVisibility(View.INVISIBLE);
				}
			}, 5000);
		}
	}
}