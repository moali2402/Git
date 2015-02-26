package dev.vision.layback;

import com.sinch.android.rtc.calling.Call;

import dev.vision.layback.classes.Contact;
import dev.vision.layback.classes.Number;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ContactActivity extends BaseActivity {

    ListView lv;
    Contact contact;
    boolean callingEnabled;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        lv = (ListView) findViewById(R.id.listView1);
        TextView name = (TextView) findViewById(R.id.name);
        int pos = getIntent().getIntExtra(SinchService.CALL_ID,0);
        
        contact = Static.Contacts.getContact(pos);
        name.setText(contact.getName());
        
        lv.setAdapter(new ContactsAdapter());
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
    }

    @Override
    protected void onServiceConnected() {
        callingEnabled = true;
    }

   
    private void call(int position) {
    	if(callingEnabled && contact != null){
	       /*String userName = mCallName.getText().toString();
	       if (r.isEmpty()) {
	             Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
	            return;
	        }
	        */
            if (getSinchServiceInterface() != null && getSinchServiceInterface().isStarted()) {

	    		Number n = contact.getNumbers().get(position);
		        Call call = getSinchServiceInterface().callUser(n.getNumber());
		        String callId = call.getCallId();
		
		        Intent callScreen = new Intent(this, CallScreenActivity.class);
		        callScreen.putExtra(SinchService.CALL_ID, callId);
		        startActivity(callScreen);
            }
    	}
    }

   class ContactsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return contact.getNumbers().size();
		}
	
		@Override
		public Number getItem(int position) {
			return contact.getNumbers().get(position);
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.number_item, parent, false);
			
			Number num = getItem(position);
			
			TextView type = (TextView) convertView.findViewById(R.id.type);
			TextView val = (TextView) convertView.findViewById(R.id.val);
			ImageButton call = (ImageButton) convertView.findViewById(R.id.callButton);

			type.setText(num.getType()+": ");
			val.setText(num.getNumber());
			
			call.setOnClickListener(null);
			call.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					call(position);									
				}
			});

			

			return convertView;
		}
    }
}
