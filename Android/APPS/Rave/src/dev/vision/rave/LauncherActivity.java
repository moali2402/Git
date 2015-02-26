package dev.vision.rave;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;

import dev.vision.rave.user.User;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class LauncherActivity extends Abc {
	String CountryCode = "20";
	String number = "1020966997";
	TransitionDrawable td;
	
	@Override
	@SuppressLint("InlinedApi")
	public void onCreate(Bundle savedInstanceState){
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
		
		user = Constants.getUserInfo(this);
		//user = new User();
		
		if(user==null){
			Intent i = new Intent(this,Login.class);
			startActivity(i);
		}else{
			Intent i = new Intent(this, SampleCirclesDefault.class);
			startActivity(i);
			//Session.openActiveSessionWithAccessToken(LauncherActivity.this, accessToken, null);
		    /*
			new Request(
				    session,
				    "/me/interests",
				    null,
				    HttpMethod.GET,
				    new Request.Callback() {
				        public void onCompleted(Response response) {
				        }
				    }
				).executeAsync();
			
			//
			  
			 
			Session.openActiveSession(LauncherActivity.this, true, new StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					if(session.isOpened()){
						Bundle b = new Bundle();
						b.putString("fields", "message");
						b.putInt("limit", 100);
						new Request(session,
							    "/616770257/statuses",
							    b,
							    HttpMethod.GET,
							    new Request.Callback() {
							        @Override
									public void onCompleted(Response response) {
							            try {
											int count = 0;
											int openness = 0;
											int extraversion = 0;
											int conscientiousness = 0;
											int neuroticism=0;
											int agreeableness =0;
											
											int wordsOpeness=0;
											int wordsCons=0;
											int wordsExtra=0;
											int wordsNeu = 0;
											int wordsAgree =0;
											
											ArrayList<Pattern> Opennes = new ArrayList<Pattern>(); 
											ArrayList<Pattern> Extraversion = new ArrayList<Pattern>(); 
											ArrayList<Pattern> Agreeableness = new ArrayList<Pattern>(); 
											ArrayList<Pattern> Neuroticism = new ArrayList<Pattern>(); 
											ArrayList<Pattern> Conscientiousness = new ArrayList<Pattern>(); 

											Opennes.addAll(LoadStringTraits("openness.txt"));
											Extraversion.addAll(LoadStringTraits("extraversion.txt"));
											Agreeableness.addAll(LoadStringTraits("agreeable.txt"));
											Neuroticism.addAll(LoadStringTraits("neuroticism.txt"));
											Conscientiousness.addAll(LoadStringTraits("conscientiousness.txt"));

									        StringBuilder sb = new StringBuilder();
							            	JSONArray js = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
											for(int i =0; i <  js.length(); i++){
												JSONObject jo = (JSONObject) js.get(i);
												if(jo.has("message")){
													//String s = jo.getString("message").replaceAll("[^\\x00-\\x7f]|[,.!]|\\b\\w{1,2}\\b", " ");
													String s = jo.getString("message").replaceAll("[^\\x00-\\x7f]|[,.!:)]", " ");
													sb.append(" " +s);										       
												}
												
											}
											if(sb.length()>0){
												String s= sb.toString().trim().toLowerCase();
												int no = Words(s);
												count+=no;									
												ArrayList<String> used = new ArrayList<String>(); 

										        for (Pattern p : Opennes) {
													//any regex
										            Matcher matcher = p.matcher(s);
										            boolean found = false;
										            //Math.log(probability()/(probability("best")x probability("of")x probability("luck")))
										            while (matcher.find()) {
										            	found= true;
										                openness++;
										                //break;
										            }
										            if(found)
										            	wordsOpeness++;
										        }
	
										        for (Pattern p : Extraversion) {
													//any regex
										            Matcher matcher = p.matcher(s);
										            boolean found = false;
										            while (matcher.find()) {
										            	found= true;
										                extraversion++;
										                //break;
										            }
										            if(found)
										            	wordsExtra++;
										        }
										        
	
												for (Pattern p : Conscientiousness) {
													//any regex
										            Matcher matcher = p.matcher(s);
										            boolean found = false;
										            while (matcher.find()) {
										            	found= true;
										            	conscientiousness++;
										                //break;
										            }
										            if(found)
										            	wordsCons++;
										        }
												
												for (Pattern p : Neuroticism) {
													//any regex
										            Matcher matcher = p.matcher(s);
										            boolean found = false;
										            while (matcher.find()) {
										            	found= true;
										            	neuroticism++;
										                //break;
										            }
										            if(found)
										            	wordsNeu++;
										        }
												
												for (Pattern p : Agreeableness) {
													//any regex
										            Matcher matcher = p.matcher(s);
										            boolean found = false;
										            while (matcher.find()) {
										            	found= true;
										            	agreeableness++;
										                //break;
										            }
										            if(found)
										            	wordsAgree++;
										        }
										        
												Log.d("Opennes",""+(openness*100/words.size()));//words.subList(count-no, count).toString() );
												Log.d("Conscientiousness",""+(conscientiousness*100/words.size()));//words.subList(count-no, count).toString() );
												Log.d("Extraversion",""+(extraversion*100/words.size()));//words.subList(count-no, count).toString() );
												Log.d("Agreeableness",""+(agreeableness*100/words.size()));
												Log.d("Neuroticism",""+(neuroticism*100/words.size()) + " /" +words.size());
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
							        }
							    }
						).executeAsync();
					}
					
				}
			});

		     */
		}
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "dev.vision.rave", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
	    
		finish();
	}
	
	static LinkedHashSet<String> words = new LinkedHashSet<String>();
	public static int Words(String s){
		String trimmed = s.trim();
		String[] sa = trimmed.split("\\s+");
		int count = trimmed.isEmpty() ? 0 : sa.length;
		if(count>0)
		words.addAll(Arrays.asList(sa));
		return count;
	}
	public static int countWords(String s){

	    int wordCount = 0;

	    boolean word = false;
	    int endOfLine = s.length() - 1;

	    for (int i = 0; i < s.length(); i++) {
	        // if the char is a letter, word = true.
	        if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
	            word = true;
	            // if char isn't a letter and there have been letters before,
	            // counter goes up.
	        } else if (!Character.isLetter(s.charAt(i)) && word) {
	            wordCount++;
	            word = false;
	            // last word of String; if it doesn't end with a non letter, it
	            // wouldn't count without this.
	        } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
	            wordCount++;
	        }
	    }
	    return wordCount;
	}
	
	
	
 
 public ArrayList<Pattern> LoadStringTraits( String fileName){
	Scanner s;
	ArrayList<Pattern> list = new ArrayList<Pattern>();

	try {
		InputStream is = getAssets().open(fileName);
		s = new Scanner(is);
	
		 while (s.hasNextLine()){
			 String ss = s.nextLine();
			 
		     list.add(Pattern.compile(ss));

		 }
		 s.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return list;
 }
 
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
	 
	        } else if (v instanceof TextView ) {
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
	}
}
