package dev.vision.voom.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import dev.vision.voom.R;
import dev.vision.voom.SinchService;
import dev.vision.voom.adapters.ContactAdapter;
import dev.vision.voom.classes.Contact;
import dev.vision.voom.parse.Profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListUsersActivity extends Activity {

   // private String currentUserId;
    private ContactAdapter contactAdapter;
    private ArrayList<Profiles> profiles = new ArrayList<Profiles>();
    private ListView usersListView;
    private Button logoutButton;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    Contact c;
	protected boolean dontShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean success = intent.getBooleanExtra("success", false);
                dontShow = true;
                if(progressDialog!=null)
                progressDialog.dismiss();
                if (!success) {
                    Toast.makeText(getApplicationContext(), "Messaging service failed to start", Toast.LENGTH_LONG).show();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("dev.vision.voom.ListUsersActivity"));
    

        logoutButton = (Button) findViewById(R.id.logoutButton);
        usersListView = (ListView)findViewById(R.id.usersListView);
        
        c = ((Contact)Contact.getCurrentUser());
        // currentUserId = c.getObjectId();
        
        contactAdapter = new ContactAdapter(this, profiles);
        
        usersListView.setAdapter(contactAdapter);
        
        if(!dontShow)
        showSpinner();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                stopService(new Intent(getApplicationContext(), SinchService.class));
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                openConversation(profiles, i);
            }
        });
        //setConversationsList();
    }

    //Select all from profiles where "users" has current, "user" is contact or "isDefault" true.
    
    //display clickable a list of all users
    private void setConversationsList() {
              
	     ParseQuery<Contact> contacts = Contact.getUpdatedQuery();
	     contacts.whereNotEqualTo(Contact.ID, c.getObjectId());

	     ParseQuery<Profiles> usersQ= Profiles.getQuery();
	     usersQ.whereEqualTo(Profiles.USERS, c);
	    
	     ParseQuery<Profiles> defaultQ = Profiles.getQuery();
    	 defaultQ.whereEqualTo(Profiles.IS_DEFAULT, true);
    	
    	 ArrayList<ParseQuery<Profiles>> qu = new ArrayList<ParseQuery<Profiles>>();
    	 qu.add(usersQ);
    	 qu.add(defaultQ);
    	 
         ParseQuery<Profiles> profilesQ = ParseQuery.or(qu);     
         profilesQ.include(Profiles.USER);

         profilesQ.whereMatchesKeyInQuery(Profiles.USER, Contact.ID, contacts); 
	     
         profilesQ.findInBackground(new FindCallback<Profiles>() {
	    	 public void done(List<Profiles> p, ParseException e) {
	    		 if(e == null){
	    			HashMap<String, Profiles> mp = new HashMap<String, Profiles>();
					for(Profiles profile : p){
						String c = profile.getUser().getObjectId();
						if(mp.containsKey(c)){
							if(mp.get(c).isDefault()){
								mp.put(c, profile);
							}
						}else{
							mp.put(c, profile);
						}					
					}
					
					profiles.clear();
					profiles.addAll(mp.values());
					contactAdapter.notifyDataSetChanged();
				}
	    	}
	     });
        
	     
    }

    /*
    public void openConversation(ArrayList<Profiles> profiles, int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", profiles.get(pos).getUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
           public void done(List<ParseUser> user, com.parse.ParseException e) {
               if (e == null) {
                   Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                   intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
                   startActivity(intent);
               } else {
                   Toast.makeText(getApplicationContext(),
                       "Error finding that user",
                           Toast.LENGTH_SHORT).show();
               }
           }
        });
    }
    */
    
    public void openConversation(ArrayList<Profiles> profiles, int pos) {
	
	    Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
	    intent.putExtra("RECIPIENT_ID", profiles.get(pos).getUser().getObjectId());
	    startActivity(intent);
	    
    }

    //show a loading spinner while the sinch client starts
    private void showSpinner() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        setConversationsList();
    }
}


