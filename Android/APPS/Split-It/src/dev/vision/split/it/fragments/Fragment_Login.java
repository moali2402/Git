/**
 * 
 */
package dev.vision.split.it.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dd.processbutton.iml.ActionProcessButton;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import dev.vision.split.it.Operator;
import dev.vision.split.it.R;
import dev.vision.split.it.activities.ShowMapActivity;
import dev.vision.split.it.displayingbitmaps.ui.ImageDetailFragment;

/**
 * @author Mo
 *
 */
public class Fragment_Login extends Fragment {

	String CountryCode;
	String number;
	private boolean VERIFIED;
	Spinner spinner;

	EditText verf_code;
 	ActionProcessButton next;
    private boolean waiting;
	private boolean overriden;
	CountDownTimer t;
	long timerLeft = 0;

	ArrayList<String> codes = new ArrayList<String>();
	private EditText phone;
	private ViewFlipper vf;
	

    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
    public static Fragment_Login newInstance() {
        final Fragment_Login f = new Fragment_Login();

       

        return f;
    }

    /**
     * Empty constructor as per the Fragment documentation
     */
    public Fragment_Login() {}

    /**
     * Populate image using a url from extras, use the convenience factory method
     * {@link ImageDetailFragment#newInstance(String)} to create this fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate and locate the main ImageView
        View v = inflater.inflate(R.layout.login, container, false);
        next = (ActionProcessButton) v.findViewById(R.id.next);
        next.setMode(ActionProcessButton.Mode.ENDLESS);
		spinner = (Spinner) v.findViewById(R.id.spinner1);
		phone = ((EditText) v.findViewById(R.id.phoneNo));
		vf = ((ViewFlipper) v.findViewById(R.id.viewp));
		verf_code = (EditText) v.findViewById(R.id.verification_code);

		final View b =  v.findViewById(R.id.relativeLayout3);
		final ImageView logo =  (ImageView) v.findViewById(R.id.imageView2);

		phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				Operator O;
				int len = s.length();
				if( len ==3 ){
					O = Operator.value(s.toString());
        			b.setBackgroundColor(O.getColor());
        			logo.setImageResource(O.getLogo());
				}else if( len < 3 ){
					O = Operator.UNKNOWN;
					b.setBackgroundColor(O.getColor());
        			logo.setImageResource(O.getLogo());
				
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});

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
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {				
			}
		});

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String s = loadJSONFromAsset();
		JSONArray jo;
		try {
			jo = new JSONArray(s);
	
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayList<String> list=new ArrayList<String>();
			codes = new ArrayList<String>();

		    for (int i = 0 ;  i <jo.length(); i++) {
				JSONObject json = new JSONObject(jo.get(i).toString());
				String code = json.getString("dial_code");
				String name = json.getString("name");
				if(name.contains("Egypt")){
				codes.add(code);
				list.add(name + " (+"+code+")");
				}
			}
			ArrayAdapter<String> spinnerMenu = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_list_item_1, list){
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
			e.printStackTrace();
		}
		
		
        
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
    			 next.setEnabled(false);
				 if(next.getText().toString().equals("Verify")){
					 if(verf_code.getText().length()==6){
						t.cancel();
         				next.setProgress(false, "Verifying");
         				new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								startActivity(new Intent(getActivity(), ShowMapActivity.class));
							}
						}, 3000);
                 		//new Verify_Code().execute();
					 }
				 }else{
						CountryCode = codes.get(spinner.getSelectedItemPosition());
						number = phone.getText().toString();
						vf.setDisplayedChild(1);

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
						//new Send_Twilio().execute();
						Toast.makeText(getActivity(), "Sorry!! We are not in your country yet, See you soon.", Toast.LENGTH_LONG).show();
						//new Send_Clickatell().execute();

						
				 }
			}
		});

    }
    
    public String loadJSONFromAsset() {
	     String json = null;
	     try {

	         InputStream is = getActivity().getAssets().open("countries.json");

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
}