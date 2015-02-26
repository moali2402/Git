package dev.vision.layback;

import com.sinch.android.rtc.calling.Call;

import dev.vision.layback.classes.Contact;
import dev.vision.layback.classes.Number;
import dev.vision.layback.classes.Status;
import dev.vision.layback.classes.User;
import dev.vision.layback.views.HexContactStatusIcon;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends BaseActivity {

    ListView lv;
    boolean callingEnabled;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);
        SetupDemo();
        lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(new ContactsAdapter());
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent contactScreen = new Intent(ContactsActivity.this, ContactActivity.class);
				contactScreen.putExtra(SinchService.CALL_ID, position);
		        startActivity(contactScreen);				
			}
		});

    }

    @Override
    protected void onServiceConnected() {
        //TextView userName = (TextView) findViewById(R.id.loggedInName);
        //userName.setText(getSinchServiceInterface().getUserName());
        callingEnabled = true;
    }

    @Override
    public void onDestroy() {
       // if (getSinchServiceInterface() != null) {
       //     getSinchServiceInterface().stopClient();
       // }
        super.onDestroy();
    }
    
    void SetupDemo(){
    	Contact r = new Contact("SuperMarket");
        r.addNumber(new Number("Mobile", "000000"));
        r.addNumber(new Number("Mobile", "01000000"));
        r.addNumber(new Number("Mobile", "02000000"));
        Static.Contacts.addContact(r);

        r = new Contact("SuperMarket1");
        r.addNumber(new Number("Mobile", "010000000"));
        Static.Contacts.addContact(r);

        r = new Contact("SuperMarket2");
        r.addNumber(new Number("Mobile", "020000000"));
        Static.Contacts.addContact(r);

        r = new Contact("SuperMarket3");
        r.addNumber(new Number("Mobile", "030000000"));
        Static.Contacts.addContact(r);
	        
    }


    private void callButtonClicked(Contact r) {
    	if(callingEnabled){
	       /*String userName = mCallName.getText().toString();
	       if (r.isEmpty()) {
	             Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
	            return;
	        }
	        */
	
	        Call call = getSinchServiceInterface().callUser(r.getDefaultNumber());
	        String callId = call.getCallId();
	
	        Intent callScreen = new Intent(this, CallScreenActivity.class);
	        callScreen.putExtra(SinchService.CALL_ID, callId);
	        startActivity(callScreen);
    	}
    }

   class ContactsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return Static.Contacts.getList().size();
		}
	
		@Override
		public Contact getItem(int position) {
			return Static.Contacts.getContact(position);
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.contacts_item, parent, false);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(getItem(position).getName());
			HexContactStatusIcon icon = (HexContactStatusIcon) convertView.findViewById(R.id.contactIcon);
			icon.ChangeStatus(Status.ONLINE);
			
			return convertView;
		}
    }
}
