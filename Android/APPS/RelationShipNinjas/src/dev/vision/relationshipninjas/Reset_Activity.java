package dev.vision.relationshipninjas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.API.API;

public class Reset_Activity extends Activity implements OnClickListener {
	
	Fragment Welcome;
	Fragment Post;
	Fragment Exchange;
	Fragment Profile;
	List<Fragment> fList;
	EditText email;
	String emaildata;
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reset);
        
        email = ((EditText) findViewById(R.id.email_add));
        Bundle b =getIntent().getExtras();
        if(b!=null)
        email.setText(b.containsKey("email") ? b.getString("email") : "");
        Button send = (Button) findViewById(R.id.send);
        Button cancel = (Button) findViewById(R.id.cancel);
        TextView signup = (TextView) findViewById(R.id.signup);

        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
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
	
	
	@SuppressLint("NewApi") 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.send:
			emaildata = this.email.getText().toString();
			if(!emaildata.isEmpty())
			new Reset_Password().execute(emaildata);
			break;
		case R.id.cancel:
			finish();
			break;
		case R.id.signup:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.therelationshipninjas.com"));
	        startActivity(browserIntent);
	        break;
		}
	}
	
	public class Reset_Password extends AsyncTask<String, Void, Boolean>{
		ProgressDialog pd;
		private String message = "There seems to be a problem processing your request, please try again.";
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(Reset_Activity.this, null, "Please wait!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			
			DefaultHttpClient client = new DefaultHttpClient();
			
			boolean success = false;
			try{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			    nameValuePairs.add(new BasicNameValuePair("operation", "send")); 
			    nameValuePairs.add(new BasicNameValuePair("email", params[0])); 
			    String extraParam = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpGet request = new HttpGet(API.RESET +"&"+ extraParam);
				request.setHeader("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
	            
				//Log.d("Result", request.getURI().toString());

				// add request header
				HttpResponse response = client.execute(request);
				if(response.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					//Log.d("Result", result.toString());
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					if(!success)
						message = js.optString("message",message);
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
				if(pd.isShowing())
					pd.dismiss();
				Intent i = new Intent();
				i.putExtra("email", emaildata);
				setResult(RESULT_OK,i);
				finish();
			}else{
				if(pd.isShowing())
					pd.dismiss();
				 new AlertDialog.Builder(Reset_Activity.this)
			    .setTitle("Password Recovery Failed!!")
			    .setMessage(message)
			    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        }
			     })
			    .show();
			}
		}
	}
}