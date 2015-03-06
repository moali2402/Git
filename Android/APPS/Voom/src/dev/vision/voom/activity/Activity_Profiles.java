package dev.vision.voom.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.IMaterialView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import dev.vision.voom.R;
import dev.vision.voom.classes.Contact;
import dev.vision.voom.parse.Profiles;

public class Activity_Profiles extends Activity {

	    private ArrayList<Profiles> profiles = new ArrayList<Profiles>();
	    Contact c;
		private Context mContext;
		private IMaterialView mListView;
	    
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.profiles);
	        mListView = (IMaterialView) findViewById(R.id.material_listview);
	        View v = findViewById(R.id.empty_header);

	        //((MaterialListView) mListView).addHeaderView(v);
	        //((MaterialListView) mListView).init(); 
	       
	        c = ((Contact)Contact.getCurrentUser());
	    }

		@Override
	    public void onResume() {
	        super.onResume();
	        getProfiles();
	    }
		
	    private void getProfiles() {
	              
		     ParseQuery<Profiles> profilesQ= Profiles.getQuery();
		     profilesQ.whereEqualTo(Profiles.USER, c);
		    		     
	         profilesQ.findInBackground(new FindCallback<Profiles>() {
		    	 public void done(List<Profiles> p, ParseException e) {
		    		 if(e == null){						
						profiles.clear();
						profiles.addAll(p);
						new fromProfiles().execute();
					}
		    	}
		     });
	        
		     
	    }

	    

	    class fromProfiles extends AsyncTask<Void, Void, ArrayList<Card>>{

			@Override
			protected ArrayList<Card> doInBackground(Void... params) {
				ArrayList<Card> cards = new ArrayList<Card>();

				for (Profiles p : profiles) {
		            Card card = p.buildCard(mContext);
		            if(card.isDefault())
		            	cards.add(0, card);
		            else cards.add(card);
		        }
				return cards;
			}
			
			@Override
			protected void onPostExecute(final ArrayList<Card> result) {
		         super.onPostExecute(result);
		         runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						//mListView.getAdapter().clear();
				        mListView.addAll(result);						
					}
				});
				 
			}
	       

	    }
}
