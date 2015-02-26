package dev.vision.rave;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dd.processbutton.iml.ActionProcessButton;
import com.facebook.Session;
import com.hipmob.gifanimationdrawable.GifAnimationDrawable;

import dev.vision.rave.motion.ParallaxImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

public class Login extends Activity {
	
	String CountryCode;
	String number;
	 TransitionDrawable td;
	VideoView mVideoView;
	private ParallaxImageView imBack;
	private GifAnimationDrawable gif;
	private boolean VERIFIED;
	Spinner spinner;

	EditText verf_code;
 	ActionProcessButton next;
	private TransitionDrawable td1;
    private boolean waiting;
	private boolean overriden;
	CountDownTimer t;
	long timerLeft = 0;

	ArrayList<String> codes = new ArrayList<String>();
	
	
	@SuppressLint("InlinedApi") 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
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
      
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup);
	
		WebView web_right = (WebView) findViewById(R.id.back);
		web_right.setHorizontalScrollBarEnabled(false);
	 	web_right.setVerticalScrollBarEnabled(false);
		//web_right.getSettings().setAllowUniversalAccessFromFileURLs(true);
		//web_right.getSettings().setAllowFileAccessFromFileURLs(true);
		web_right.getSettings().setJavaScriptEnabled(true);
		web_right.setLayerType(View.LAYER_TYPE_SOFTWARE, null);		 
		
		if(web_right!= null){
			    if (Build.VERSION.SDK_INT >= 11) {
					web_right.setBackgroundColor(0x01000000);
				} else {
					web_right.setBackgroundColor(0x00000000);
				}
				String title= ReadFromfile("neon/index.html");

				//webHtml_doughnut = webHtml_doughnut.replace("$WIDTH_V$",""+ 300);

				web_right.loadDataWithBaseURL("file:///android_asset", title, "text/html", "UTF-8", null);
				
				if (Build.VERSION.SDK_INT >= 11) {
					web_right.setBackgroundColor(0x01000000);
				} else {
					web_right.setBackgroundColor(0x00000000);
				}
		 }
		
		 
		imBack = ((ParallaxImageView) findViewById(R.id.ImageView02));	
		try {
			gif= App.getGif();
			/* for using video
			 	Uri uri = Uri.parse("android.resource://dev.vision.rave/" + R.raw.dancingg);
				 
				mVideoView  = (VideoView)findViewById(R.id.VideoView);
				mVideoView.setMediaController(new MediaController(this));       
				mVideoView.setVideoURI(uri);
				mVideoView.requestFocus();
				mVideoView.setOnPreparedListener (new OnPreparedListener() {                    
				    @Override
				    public void onPrepared(MediaPlayer mp) {
				        mp.setLooping(true);
				    }
				});
				mVideoView.start();
			*/
	
			imBack.setImageDrawable(gif);
			gif.setVisible(true, true);
			imBack.setForwardTiltOffset(.35f);

            // Set SeekBar to change parallax intensity
			imBack.setParallaxIntensity(1f + ((float) 1) / 40);
                
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		InitSplash();
	}
	
	public String ReadFromfile(String fileName) {
		InputStream is = null;
		try {
			is = getAssets().open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int size = 0;
		try {
			size = is.available();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] buffer = new byte[size];
		try {
			is.read(buffer);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String str = new String(buffer);
		return str;
	}

	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		gif.start();
	}
	

	@SuppressLint("NewApi") 
	private void InitSplash() {
		String s = loadJSONFromAsset();
		JSONArray jo;
		try {
			jo = new JSONArray(s);
	
			spinner = (Spinner) findViewById(R.id.spinner1);
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayList<String> list=new ArrayList<String>();
			codes = new ArrayList<String>();

		    for (int i = 0 ;  i <jo.length(); i++) {
				JSONObject json = new JSONObject(jo.get(i).toString());
				String code = json.getString("dial_code");
				String name = json.getString("name");
				codes.add(code);
				list.add(name + " (+"+code+")");
			}
			ArrayAdapter<String> spinnerMenu = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, list){
				@Override
				public View getDropDownView(int position, View convertView,ViewGroup parent) {

			        View v = super.getDropDownView(position, convertView,parent);

			        ((TextView) v).setGravity(Gravity.CENTER);

			        return v;

			    }
			};
			
			// spinnerMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(spinnerMenu);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		overrideFonts(this, findViewById(android.R.id.content));
		

		final Button send = (Button) findViewById(R.id.verify);
		next = (ActionProcessButton) findViewById(R.id.next);
		verf_code = (EditText) findViewById(R.id.verification_code);

		verf_code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int before, int count) {
				if(s.length() == 6){
        			overriden = true;
					next.setProgress(true, 0, "Verify");	
        			next.setEnabled(true);
				}else if(next.getText().toString().equals("Verify")){
        			overriden = false;
					if(!waiting)
					{
						 next.setProgress(false, "Resend Verification Code!");
		    			 next.setEnabled(true);
						//	next.setProgress(false);
		        		//	next.setEnabled(false);	
					}else{
		    			 next.setEnabled(false);
				    	 next.setProgress(false, next.getLoadingText() + " : ( " +  timerLeft +" )");
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		
		Facebook.initialise(Login.this);
				
		next.setMode(ActionProcessButton.Mode.ENDLESS);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
    			 next.setEnabled(false);
				 if(next.getText().toString().equals("Verify")){
					 if(verf_code.getText().length()==6){
						t.cancel();
         				next.setProgress(false, "Verifying");
                 		new Verify_Code().execute();
					 }
				 }else{
						CountryCode = codes.get(spinner.getSelectedItemPosition());
						number = ((EditText) findViewById(R.id.phoneNo)).getText().toString();
						((ViewFlipper) findViewById(R.id.viewp)).setDisplayedChild(1);

						int timer = 60 * 1000;
						next.setProgress(false, next.getLoadingText() + " : ( " +timer  / 1000 +" )");
						waiting = true;

						t = new CountDownTimer(timer, 1000) {


							@Override
							public void onTick(long millisUntilFinished) {
								 timerLeft = millisUntilFinished / 1000;
						    	 if(!overriden)
						    	 next.setProgress(false, next.getLoadingText() + " : ( " +  timerLeft +" )");
 								 waiting = true;

						     }

						     @Override
							public void onFinish() {
						    	 if(!overriden)
									 next.setProgress(false, "Resend Verification Code!");
						    	 next.setEnabled(true);
   								 waiting = false;
						     }
						}.start();
						new Send_Twilio().execute();
						Toast.makeText(App.getContext(), "Sorry!! We are not in your country yet, See you soon.", Toast.LENGTH_LONG).show();
						//new Send_Clickatell().execute();

						
				 }
			}
		});	
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//new Send_Clickatell().execute();
				new Verify_Code().execute();
			}
		});	
		
		
		final Random rnd = new Random(); 

		int red =rnd.nextInt(256), green=rnd.nextInt(256), blue=rnd.nextInt(256);
		int color = Color.argb(120, red,green,blue);
		red =rnd.nextInt(256);
		green=rnd.nextInt(256);
		blue=rnd.nextInt(256);
		int color2 = Color.argb(120, red,green,blue);

		
		td = new TransitionDrawable(new Drawable[]{new ColorDrawable(color), new ColorDrawable(color2)});
		td1  = new TransitionDrawable(new Drawable[]{new ColorDrawable(color), new ColorDrawable(color2)});

		
		td.setCrossFadeEnabled(true);
		td1.setCrossFadeEnabled(true);

		((ImageView) findViewById(R.id.ImageView01)).setImageDrawable(td);
		((ImageView) findViewById(R.id.ImageView03)).setImageDrawable(td1);

		
		td.startTransition(3000);
		td1.startTransition(3000);


		new Handler().postDelayed(new Runnable() {
			

			@SuppressLint("NewApi") @Override
			public void run() {
				int red =rnd.nextInt(256), green=rnd.nextInt(256), blue=rnd.nextInt(256);
				int color = Color.argb(120, red,green,blue);
				int colorfull = Color.argb(255, red,green,blue);

				int time= 3000;
				if(VERIFIED){
					color = Color.argb(255, 255, 255, 255);
					time =1500;
				}
				td = new TransitionDrawable(new Drawable[]{td.getDrawable(1), new ColorDrawable(color)});
				td1  = new TransitionDrawable(new Drawable[]{td1.getDrawable(1), new ColorDrawable(colorfull)});
				((ImageView) findViewById(R.id.ImageView01)).setImageDrawable(td);
				((ImageView) findViewById(R.id.ImageView03)).setImageDrawable(td1);
				
				td.setCrossFadeEnabled(true);
				td1.setCrossFadeEnabled(true);
				
				td.startTransition(time);
				td1.startTransition(time);

				if(color != Color.WHITE)
					new Handler().postDelayed(this, 3000);
				else{
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							Intent i = new Intent(Login.this, InputInfo.class);
							startActivity(i);
							overridePendingTransition(R.anim.slide_in_down, R.anim.slide_down);						
						}
					}, time);
				}
			}
		}, 3000);
	}
	
	/*
	 // This method will update the UI on new sensor events
    @SuppressLint("NewApi") public void onSensorChanged(SensorEvent sensorEvent)
    {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // the values you were calculating originally here were over 10000!
                x = (int) sensorEvent.values[2]; 
                //y = (int) Math.pow(sensorEvent.values[2], 2);
                
                float[] rotationMatrix = new float[]{};
				float[] valuesOrientation = new float[]{};
				
                float f = imBack.getX();
//              System.out.println(""+f);

                if(f+x> -880 && f+x <0){
                	imBack.setX(f+x);
                }

            }
        }
    }
    */
	
	 public String loadJSONFromAsset() {
	     String json = null;
	     try {

	         InputStream is = getAssets().open("countries.json");

	         int size = is.available();

	         byte[] buffer = new byte[size];

	         is.read(buffer);

	         is.close();

	         json = new String(buffer, "UTF-8");


	     } catch (IOException ex) {
	         ex.printStackTrace();
	         return null;
	     }
	     return json;

	 }
	 

	private void overrideFonts(final Context context, final View v) {
	      try {
	        if (v instanceof ViewGroup) {
	        	ViewGroup vg = (ViewGroup) v;
	          
	             for (int i = 0; i < vg.getChildCount(); i++) {
		             View child = vg.getChildAt(i);
		             overrideFonts(context, child);
	             }
	 
	        }else if (v instanceof TextView) {
	          ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "AvantGardeITCbyBTBook.otf"));
	        }
	 
	      } catch (Exception e) {
	      }
	}
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      
	      Session s = Session.getActiveSession();
	      s.onActivityResult(this, requestCode, resultCode, data);
	      Session.NewPermissionsRequest newPermissionsRequest = new Session
	    		  .NewPermissionsRequest(this, Arrays.asList("friends_status","read_stream")).setRequestCode(requestCode);
	      s.requestNewReadPermissions(newPermissionsRequest);
	      
	    return;
	  }
	@Override
	public void onResume() {
		super.onResume();
        imBack.registerSensorManager();

	}
	
	@Override
    public void onPause() {
		//imBack.unregisterSensorManager();
        super.onPause();
    }
	
	ProgressDialog pd;
	int random_num;
	
	


	public class SmsListener extends BroadcastReceiver{

	    
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        // TODO Auto-generated method stub
	    
	        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
	            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
	            SmsMessage[] msgs = null;
	            String msg_from;
	            if (bundle != null){
	                //---retrieve the SMS message received---
	                try{
	                    Object[] pdus = (Object[]) bundle.get("pdus");
	                    msgs = new SmsMessage[pdus.length];
	                    for(int i=0; i<msgs.length; i++){
	                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	                        msg_from = msgs[i].getOriginatingAddress();
	                        String msgBody = msgs[i].getMessageBody();
	                        msgBody = msgBody.substring(msgBody.length()-6, msgBody.length());
	                        if(msgBody.equals(""+random_num)){
	    						t.cancel();
	                			next.setEnabled(false);
	                        	verf_code.setText(msgBody);
	                			next.setProgress(false, "Verifying");
	                        	new Verify_Code().execute();
	                        }
	                    }
	                }catch(Exception e){
//	                            Log.d("Exception caught",e.getMessage());
	                }
	            }
	        }
	    }
	}

	public class Send_Clickatell extends AsyncTask<Void, Void, String>{
		
		
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			//pd = ProgressDialog.show(Login.this, "Verifing Your Number", "Please Wait for an SMS...");
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			Random rnd = new Random();
			random_num = rnd.nextInt(1000000 - 100000) + 100000;
			DefaultHttpClient httpClient = new DefaultHttpClient();
			//String CountryCode = "20";
			//String number = "1020966997";
          String response = null;

			HttpPost httpPost = new HttpPost("http://api.clickatell.com/http/sendmsg");

          ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
          nameValuePair.add(new BasicNameValuePair("user", "selfiie"));
          nameValuePair.add(new BasicNameValuePair("password", "QSUfYAGHSTKVOb"));
          nameValuePair.add(new BasicNameValuePair("api_id", "3481979"));
          nameValuePair.add(new BasicNameValuePair("to", CountryCode+number));
          nameValuePair.add(new BasicNameValuePair("text", ""+random_num));
          try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,
				        "UTF-8"));
			
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
	            response = EntityUtils.toString(httpEntity);
          } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return response;
		}
		
		@Override
		public void onPostExecute(String result){
			super.onPostExecute(result);
			
			registerReceiver(new SmsListener(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
			((ViewFlipper) findViewById(R.id.viewp)).setDisplayedChild(1);

		}
	}

	public class Send_Twilio extends AsyncTask<Void, Void, String>{
		
		
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			//pd = ProgressDialog.show(Login.this, "Verifing Your Number", "Please Wait for an SMS...");
		}
		
		@Override
		protected String doInBackground(Void... arg0) {

			Random rnd = new Random();
			random_num = rnd.nextInt(1000000 - 100000) + 100000;

	        String response = null;
	
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(API.REGISTER);
	
	        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	        nameValuePair.add(new BasicNameValuePair("cc", CountryCode));
	        nameValuePair.add(new BasicNameValuePair("phone_no", number));
	        nameValuePair.add(new BasicNameValuePair("rnd", ""+random_num));
	        try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,
				        "UTF-8"));
			
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
	            response = EntityUtils.toString(httpEntity);
	        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return response;
		}
		
		@Override
		public void onPostExecute(String result){
			super.onPostExecute(result);
			//next.setProgress(true); delete
			//new Verify_Code().execute(); delete
			//Uncomment
			registerReceiver(new SmsListener(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
			//Toast.makeText(Login.this, "Sorry!! We are coming soon to your country.", Toast.LENGTH_LONG).show();
		}
	}
	
	public class Verify_Code extends AsyncTask<Void, Void, Boolean>{
		
		
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			//pd = ProgressDialog.show(Login.this, "Verifing Your Number", "Please Wait...");
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			Boolean verified = Boolean.FALSE;
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(API.VERIFY);
	
	        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	        nameValuePair.add(new BasicNameValuePair("phone_no", CountryCode+number));
	        nameValuePair.add(new BasicNameValuePair("code", verf_code.getText().toString()));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,
				        "UTF-8"));
			
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpResponse.getStatusLine().getStatusCode() == 200){
			        String response = EntityUtils.toString(httpEntity);
		            JSONObject j = new JSONObject(response);
		            verified = Boolean.valueOf(j.optInt("success") == 1 ? true : false);
				}
	           
	        } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	 		}
			return verified;
		}
		
		@Override
		public void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(result.booleanValue()){
				VERIFIED=true;
				//((ViewFlipper) findViewById(R.id.viewp)).setDisplayedChild(2);
		        Constants.saveData(Login.this, Constants.NUMBER, CountryCode+number);
		        next.setProgress(true, "Verified");
    			next.setEnabled(false);
			}else{
				next.setProgress(false, -1, "Wrong Verfication Code");
			}
				//if(pd.isShowing())
				//	pd.dismiss();
		}
	}

}
