package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import dev.vision.license.LICENSE;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	EditText storeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loging);
        storeid = (EditText) findViewById(R.id.storeid);
		
        //License
        //LICENSE.isValid("10/01/2015",this);
        
        findViewById(R.id.textView2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new VerifyStoreId().execute();
				
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    class VerifyStoreId extends AsyncTask<Void, Void, Void>{
    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(MainActivity.this, null, "Retriving Store details");
		}
		@Override
		protected Void doInBackground(Void... params) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.LOGIN);
			boolean success = false;

			String id = storeid.getText().toString();
			try{
				// add request header
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		        nameValuePair.add(new BasicNameValuePair("storeid", id));
		        
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
						if(id != null && id.equals("admin"))
						{
							pr.dismiss();
							startActivity(new Intent(MainActivity.this, Activity_Manager.class));
							finish();
						}else{
							Intent i = new Intent(MainActivity.this, Activity_Password.class);
					        i.putExtra("storeid", id);
					        /*
							JSONArray ja = js.getJSONArray("data");
							for (int x=0; x< ja.length(); x++) {
								JSONObject jo = ja.optJSONObject(x);
								String sid = jo.optString("storeid");
						        String sname = jo.optString("storename");
						        String u = jo.optString("username");
						        String p = jo.optString("password");
						        String e = jo.optString("email");

						        boolean s = jo.optString("status", "0").equals("1") ? true : false ;
						        User user = new User(u, p, s, sid, sname, e);
						        i.putExtra("user", user);
							}
							*/
							pr.dismiss();
							startActivity(i);
							finish();
						}
					}else{
						runOnUiThread(new Runnable() {
						    public void run() {
								pr.dismiss();
								Toast.makeText(MainActivity.this, "Please make sure you enter the correct store code", Toast.LENGTH_LONG).show();						    }
						});
					}
				}
			}catch (Exception e){
				runOnUiThread(new Runnable() {
				    public void run() {
						pr.dismiss();
						 Toast.makeText(MainActivity.this, "There seems to be a problem with connection, please try again!", Toast.LENGTH_LONG).show();
				    }
				});
			}
			return null;
		}
    	
    }
}
