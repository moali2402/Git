package dev.vision.relationshipninjas.Fragments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import dev.vision.relationshipninjas.ExpandableListAdapter;
import dev.vision.relationshipninjas.Main_Activity;
import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Static;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.Classes.Event.ALERTLEVEL;
import dev.vision.relationshipninjas.Classes.Event.DATEUNIT;
import dev.vision.relationshipninjas.Classes.Event.STATUS;
import dev.vision.relationshipninjas.Classes.Relationship.TYPE;
import dev.vision.relationshipninjas.R.id;
import dev.vision.relationshipninjas.R.layout;
import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.remindme.AddAlarm;
import dev.vision.relationshipninjas.views.CircularImage;
import dev.vision.relationshipninjas.views.InScrollableListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class MenuFragment extends Fragment implements OnGroupClickListener, OnChildClickListener{
	private ExpandableListView expListView;
	private ExpandableListAdapter listAdapter;
	

	View v;
	public boolean done;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.menu, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        expListView = (ExpandableListView) v.findViewById(R.id.expandableListView1);
 		
        expListView.setOnGroupClickListener(this);
        
        expListView.setOnChildClickListener(this);
        listAdapter = new ExpandableListAdapter(getActivity(), Static.rs);
        expListView.setAdapter(listAdapter);

        
        if(Static.rs== null || Static.rs.isEmpty())
	       new Dashboard_Relationship_AsyncGetAll().execute();
	}
	
	
	public void refresh(){
		listAdapter.notifyDataSetChanged(); 
	}
	
	public class Dashboard_Relationship_AsyncGetAll extends AsyncTask<Object, Void, Boolean>{
		ProgressDialog pd;

		int id;

		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(getActivity(), null, "Loading Your Relationships!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
		    
			DefaultHttpClient client = new DefaultHttpClient();
			boolean success = false;
			try{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			    nameValuePairs.add(new BasicNameValuePair("operation", "getall")); 
			    nameValuePairs.add(new BasicNameValuePair("auth_email", API.user.email)); 
			    nameValuePairs.add(new BasicNameValuePair("auth_password", API.user.pass)); 

			    String extraParam = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs));
				
			    HttpGet request = new HttpGet(API.DASHBOARD +"&"+ extraParam);
				request.setHeader("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
				
				HttpResponse response = client.execute(request);
			 
				
				if(response.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					Log.d("Result", result.toString());
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					if(success){
						JSONArray jsa = js.getJSONArray("data");
						for(int i= 0; i < jsa.length(); i++){
							JSONObject relationship = (JSONObject) jsa.get(i);
							
							JSONObject r = relationship.getJSONObject("relationship");
							final Relationship relation = new Relationship();
							relation.setId(r.optString("id"));
							relation.setName(r.optString("name"));
							relation.setType(TYPE.Match(r.optString("type")));
							relation.setRating(r.optString("rating"));
							relation.setFirstname(r.optString("firstname"));
							relation.setLastname(r.optString("lastname"));
							relation.setMiddlename(r.optString("middlename"));
							relation.setEmail(r.optString("email"));
							relation.setKids(r.optString("kids"));
							relation.setMyersbriggs(r.optString("myersbriggs"));
							relation.setGender(r.optString("gender"));
							relation.setFacebookid(r.optString("facebookid"));
							relation.setFinalizedstatus(r.optBoolean("finalizedstatus"));
							relation.setCover(r.optString("cover"));
							relation.setDefaultcover(r.optBoolean("isdefaultcover"));
							relation.setImage(r.optString("image"));
							relation.setDefaultimage(r.optBoolean("isdefaultimage"));
							relation.setProgress(r.optInt("progress"));
							
							
							 							
							JSONArray upcomingevents = relationship.getJSONArray("upcomingevents");
							for(int x= 0; x < upcomingevents.length(); x++){
								JSONObject e = (JSONObject) upcomingevents.get(x);
								Event event = new Event();

								event.setId(e.optString("id"));
								event.setDate(e.optString("date"));
								event.setPassed(e.optBoolean("passed"));
								event.setName(e.optString("name"));
								event.setType(dev.vision.relationshipninjas.Classes.Event.TYPE.valueOf(e.optString("type")));
								event.setRating(e.optString("rating"));
								event.setOriginaldate(e.optString("originaldate"));
								event.setRelationshipid(e.optString("relationshipid"));
								JSONObject remaining = (JSONObject) e.opt("remaining");
								if(remaining != null)
								{
									event.setNumber(remaining.optInt("number"));
									event.setAlertlevel(ALERTLEVEL.valueOf(remaining.optString("alertlevel")));
									event.setUnit(DATEUNIT.valueOf(remaining.optString("unit")));
								}
								event.setStatus(STATUS.valueOf(e.optString("status")));
								
								relation.addEvent(event);
							}
							

							JSONObject data = relationship.getJSONObject("shippingaddresses");

							JSONArray shipping_address = data.getJSONArray("relationship");
							for(int y= 0; y < shipping_address.length(); y++){
								JSONObject e = (JSONObject) shipping_address.get(y);
								Address ad = new Address();
								
								ad.setId(e.optString("id"));
								ad.setName(e.optString("name"));
								ad.setAddressee(e.optString("addressee"));
								ad.setAddress1(e.optString("address1"));
								ad.setAddress2(e.optString("address2"));
								ad.setCity(e.optString("city"));
								ad.setCountry(e.optString("country"));
								ad.setDefaultshipping(e.optBoolean("defaultshipping"));
								ad.setPhone(e.optString("phone"));
								ad.setState(e.optString("state"));
								ad.setZip(e.optString("zip"));
								
								relation.addShippingAddress(ad);
							}
							
							JSONObject ev = relationship.getJSONObject("justbecauseevent");

							Event justbe = new Event();
							
							justbe.setId(ev.optString("id"));
							justbe.setName(ev.optString("name"));
							justbe.setType(dev.vision.relationshipninjas.Classes.Event.TYPE.other);
							justbe.setRating(ev.optString("rating"));
							justbe.setRelationshipid(ev.optString("relationshipid"));
							JSONObject remain = (JSONObject) ev.opt("remaining");
							if(remain != null)
							{
								justbe.setNumber(-1);
								justbe.setAlertlevel(ALERTLEVEL.now);
								justbe.setUnit(DATEUNIT.valueOf(remain.optString("unit")));
							}
							justbe.setStatus(STATUS.valueOf(ev.optString("status")));

							relation.setJustBecause(justbe);
							
							Static.rs.add(relation);
						}
						
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return success;
		}
		
		@Override
		public void onPostExecute(Boolean result){
			super.onPostExecute(result);
		    //ONLY CHANGE HERE

			/*		
			if(result.booleanValue()){
				listAdapter = new ExpandableListAdapter(getActivity(), Static.rs);
			}else{
				listAdapter = new ExpandableListAdapter(getActivity(), new Relationships());
			}
	        expListView.setAdapter(listAdapter);
			 */
			if(result.booleanValue()){
				refresh();
			//	if(!Static.rs.isEmpty())
			//	  showRelationship(0);
			//	else showDashboard();
			}
		    //TO HERE

	        if(pd.isShowing())
				pd.dismiss();
	        
	        if(!done){
	        	for(Relationship r : Static.rs){
	        		for(Event e : r.getEvents()){
	        			AddAlarm alarm = new AddAlarm();
						alarm.create(getActivity(), e, true);
	        		}
	        	}
				done= true;
			}
		}
	}

		
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		Activity ac = getActivity();
		if (ac == null)
			return;

		if (ac instanceof Main_Activity) {
			Main_Activity ra = (Main_Activity) ac;
			ra.switchContent(fragment);
		}
	}

	

	@SuppressLint("NewApi") @Override
	public boolean onGroupClick(ExpandableListView arg0, View v, int pos,
			long arg3) {
			
		 Fragment newContent = null;

			v.setBackground(null);

		if(pos==0){
		    newContent = new Dashboard_Fragment();
		 }else if(pos == 1){
			if(!expListView.isGroupExpanded(pos)){
				v.setBackgroundColor(Color.parseColor("#90000000"));
			}
		 }else if(pos==2){
			    newContent = new Fragment_User();
		 /*}else if(pos==3){
		 }else if(pos==4){
		 }else if(pos==5){
		 */
		 }else{
			 Static.logout(getActivity());
				getActivity().finish();
		 }
		 if (newContent != null)
			switchFragment(newContent);
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int group,
			int pos, long arg4) {

		if(group == 1){	
			int count = listAdapter.getChildrenCount(group);
			if(pos >= count /* uncomment -1 */){
				
			}else{
				 showRelationship(pos);
			}
		}
		return true;
	
	}
	
	private void showDashboard() {
		Fragment newContent = new Dashboard_Fragment();
		if (newContent != null)
			switchFragment(newContent);
	}

	private void showRelationship(int pos) {
		Fragment newContent = RelationFragment.NewInstance(pos);
		 if (newContent != null)
			switchFragment(newContent);		
	}


	/*
	public class Relationship_AsyncGetAll extends AsyncTask<Object, Void, Boolean>{
		ProgressDialog pd;

		int id;

		private ImageLoader imageLoader = ImageLoader.getInstance();
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(getActivity(), null, "Loading Your Relationships!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
		    
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.RELATIONSHIPS);
			boolean success = false;
			try{
				// add request header

				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				JSONObject json = new JSONObject();
				json.put("operation", "getall");

				request.setEntity(new StringEntity(json.toString()));
				HttpResponse response = client.execute(request);
			 
				
				if(response.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					Log.d("Result", result.toString());
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					if(success){
						JSONArray jsa = js.getJSONArray("relationships");
						for(int i= 0; i < jsa.length(); i++){
							JSONObject r = (JSONObject) jsa.get(i);
							final Relationship relation = new Relationship();
							relation.setId(r.optString("id"));
							relation.setName(r.optString("name"));
							relation.setType(TYPE.Match(r.optString("type")));
							relation.setRating(r.optString("rating"));
							relation.setFirstname(r.optString("firstname"));
							relation.setLastname(r.optString("lastname"));
							relation.setMiddlename(r.optString("middlename"));
							relation.setEmail(r.optString("email"));
							relation.setKids(r.optString("kids"));
							relation.setMyersbriggs(r.optString("myersbriggs"));
							relation.setGender(r.optString("gender"));
							relation.setFacebookid(r.optString("facebookid"));
							relation.setFinalizedstatus(r.optBoolean("finalizedstatus"));
							relation.setCover(r.optString("cover"));
							relation.setDefaultcover(r.optBoolean("isdefaultcover"));
							relation.setImage(r.optString("image"));
							relation.setDefaultimage(r.optBoolean("isdefaultimage"));
							relation.setProgress(r.optInt("progress"));
							 imageLoader .loadImage(relation.getImage(), new ImageLoadingListener() {
									
									@Override
									public void onLoadingStarted(String imageUri, View view) {}
									
									@Override
									public void onLoadingFailed(String imageUri, View view,
											FailReason failReason) {}
									
									@Override
									public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										relation.setProfile_pic(loadedImage);
									}
									
									@Override
									public void onLoadingCancelled(String imageUri, View view) {
										// TODO Auto-generated method stub
										
									}
								});
							Static.rs.add(relation);
						}
						
					}
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
				listAdapter = new ExpandableListAdapter(getActivity(), Static.rs);
			}else{
				listAdapter = new ExpandableListAdapter(getActivity(), new Relationships());
			}
	        expListView.setAdapter(listAdapter);
	        //onChildClick(null, null, 1, 0, 0);
	        if(pd.isShowing())
				pd.dismiss();
		}
	}
	*/
}
