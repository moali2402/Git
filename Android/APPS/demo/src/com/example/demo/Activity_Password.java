package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.demo.Activity_Manage.StoreList;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;


public class Activity_Password extends ActionBarActivity {
	String storeid;
	ShownEdittext pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.password_user);
        Bundle b =getIntent().getExtras();
        TextView verify = (TextView) findViewById(R.id.verify);
        pass = (ShownEdittext) findViewById(R.id.pass);
        
        
        if(b!=null){
           storeid = b.getString("storeid");
        
	       verify.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new Verify().execute();
				}
			});
        }
    }


    
    class Verify extends AsyncTask<Void, Void, Void>{
    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(Activity_Password.this, null, "Verifying Password");
		}
    	
		@Override
		protected Void doInBackground(Void... params) {
			 
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.VERIFY);
			boolean success = false;

			String password = pass.getText().toString();
			try{
				// add request header
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		        nameValuePair.add(new BasicNameValuePair("storeid", storeid));
		        nameValuePair.add(new BasicNameValuePair("password", password));

				request.setEntity(new UrlEncodedFormEntity(nameValuePair,
				        "UTF-8"));
			
				HttpResponse httpResponse = client.execute(request);
				HttpEntity httpEntity = httpResponse.getEntity();
				//httpResponse = EntityUtils.toString(httpEntity);
          
		
				
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					Log.d("Result", result.toString());
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					
					if(success){
						JSONArray ja = js.getJSONArray("data");
						if(1 == ja.length()) {
							JSONObject jo = ja.optJSONObject(0);
							String u = jo.optString("username");
					        String p = jo.optString("password");
					        boolean s = jo.optString("status", "0").equals("1") ? true : false ;
					        String sid = jo.optString("storeid");
					        String sname = jo.optString("storename");
					        String e = jo.optString("email");
					        if(s){
						        User us = new User(u, p, s, sid, sname, e);
						        Intent in = new Intent(Activity_Password.this, Fragment_Activity.class);
							    in.putExtra("user", us);	
								pr.dismiss();
								startActivity(in);
								finish();
					        }else{
					        	runOnUiThread(new Runnable() {
								    public void run() {
										pr.dismiss();
										Toast.makeText(Activity_Password.this, "Your account is locked", Toast.LENGTH_LONG).show();
								    }
								});
					        }
						}else{
							runOnUiThread(new Runnable() {
							    public void run() {
									pr.dismiss();
									Toast.makeText(Activity_Password.this, "Could not retrive user details", Toast.LENGTH_LONG).show();
							    }
							});
						}
					}else{
						runOnUiThread(new Runnable() {
						    public void run() {
								pr.dismiss();
								 Toast.makeText(Activity_Password.this, "Wrong Password! Please make sure you enter the correct password", Toast.LENGTH_LONG).show();
						    }
						});
					}
				}
			}catch (Exception e){
				runOnUiThread(new Runnable() {
				    public void run() {
						pr.dismiss();
						 Toast.makeText(Activity_Password.this, "There seems to be a problem with connection, please try again!", Toast.LENGTH_LONG).show();
				    }
				});
			}
			return null;
		}
    
    }
}
