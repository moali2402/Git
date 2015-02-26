package dev.vision.layback;

import com.sinch.android.rtc.SinchError;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register_Activity extends BaseActivity implements OnClickListener {

    private ProgressDialog mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

       
        Button register = (Button) findViewById(R.id.Button01);
        Button cancel = (Button) findViewById(R.id.Button01);
        
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);
        
          
    }
    
    public void register(){
    	EditText username = (EditText) findViewById(R.id.EditText01);
        EditText password = (EditText) findViewById(R.id.EditText01);
        
    	String user = username.getText().toString();
    	String pass = password.getText().toString();
    	
    	if(user == null || pass == null){
    		Toast.makeText(this, "Please make sure you enter both username and password", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	
    }
    
    
    public void cancel(){
    	finish();
    }


    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }



    private void openStoresActivity() {
        Intent mainActivity = new Intent(this, Login_Activity.class);
        startActivity(mainActivity);
        finish();
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.butt:
			break;
		case R.id.Button01:
			break;
		case R.id.edit_query:
			break;
		case R.id.EditText01:
			break;
		}
	}
}
