package dev.vision.relationshipninjas.Fragments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.ExpandableListAdapter;
import dev.vision.relationshipninjas.Main_Activity;
import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Static;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.Classes.Event.ALERTLEVEL;
import dev.vision.relationshipninjas.Classes.Event.DATEUNIT;
import dev.vision.relationshipninjas.Classes.Event.STATUS;
import dev.vision.relationshipninjas.Classes.Relationship.TYPE;
import dev.vision.relationshipninjas.interfaces.Data;
import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.purchase.Purchase;
import dev.vision.relationshipninjas.purchase.Purchase_Activity;
import dev.vision.relationshipninjas.views.CircularImage;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Dashboard_Fragment extends Fragment implements OnClickListener, Data, OnRefreshListener{
	
	

	public class RelationshipsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			nots.setText(""+Static.rs.size());
			return Static.rs.size();
		}

		@Override
		public Relationship getItem(int pos) {
			return Static.rs.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}
		
		@Override
		public int getItemViewType(int pos) {
			return isOdd(pos)? 0 : 1;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		boolean isOdd( int val ) { return (val & 0x01) != 0; }

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {

			final ViewHolder vh;
			if(convertView == null){
				
				vh = new ViewHolder();
				int view = getItemViewType(pos) == 0 ? R.layout.fragment_dashboard_item_reversed : R.layout.fragment_dashboard_item;
				
	    		convertView = LayoutInflater.from(getActivity()).inflate(view, null);
	            vh.user = (ImageView) convertView.findViewById(R.id.user);
	            vh.other = (ImageView) convertView.findViewById(R.id.other);
	    		
	            vh.occasion = (TextView) convertView.findViewById(R.id.occasion);
	    		//vh.next = (TextView) convertView.findViewById(R.id.name);
	    		vh.name = (TextView) convertView.findViewById(R.id.name);
	    		vh.relation = (TextView) convertView.findViewById(R.id.relation);
	    		vh.date = (TextView) convertView.findViewById(R.id.date);

				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
				
			final Relationship relation = getItem(pos);
			final Event e =relation.getEvents().get(0);
            vh.name.setText(relation.getName());
            vh.relation.setText(relation.getType().toString());
            vh.occasion.setText(e.getName());
			SimpleDateFormat df = new SimpleDateFormat("dd\nMMMM\nyyyy");

            //DateFormat df = DateFormat.getDateInstance(DateFormat.,Locale.US);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date strDate;
			try {
				strDate = sdf.parse(e.getDate());
				vh.date.setText(df.format(strDate));
				vh.date.setBackgroundResource(getColoredDrawable(e.getAlertlevel()));

			} catch (ParseException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}            
            Bitmap prof = relation.getProfile_pic();
            if(prof != null)
            	vh.user.setImageBitmap(prof);
            else
	            im.displayImage(relation.getImage(), vh.user,  new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						//vh.user.setImageBitmap(loadedImage);
						relation.setProfile_pic(loadedImage);
						notifyDataSetChanged();
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						
					}
				});
				
    		return convertView;
		}
		
		private int getColoredDrawable(ALERTLEVEL al) {
			int d;
			if(al == ALERTLEVEL.low)
				d = R.drawable.blur_green;
			else if(al == ALERTLEVEL.severe)
				d = R.drawable.blur_yellow;
			else if(al == ALERTLEVEL.critical)
				d = R.drawable.blur_red;
			else
				d = 0;
			return d;	
		}
		
		public class ViewHolder {

			public TextView date;
			public TextView relation;
			public TextView name;
			public TextView next;
			public TextView occasion;
			public ImageView other;
			public ImageView user;

		}

	}

	private TextView username;
	private TextView location;
	private TextView nots;

	private CircularImage you;
	private ImageView show_hide;
	private ListView lv;
	private ImageLoader im;
	private SwipeRefreshLayout swipeLayout;
	
	

	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inf, container, savedInstanceState);
		View v= inf.inflate(R.layout.fragment_dashboard, null);
		
		show_hide = (ImageView) v.findViewById(R.id.show_hide_menu);
		lv = (ListView) v.findViewById(R.id.listView1);
		nots = (TextView) v.findViewById(R.id.nots);
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
		return v;
	}
	
	
	@SuppressLint("NewApi")  // 11
	@Override 
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState); 
		im = ImageLoader.getInstance();
		ObjectAnimator anim_out = ObjectAnimator.ofFloat(show_hide, "alpha", 1f, 0.2f);
		anim_out.setDuration(1500);
		anim_out.setRepeatCount(ObjectAnimator.INFINITE);
		anim_out.setRepeatMode(ObjectAnimator.REVERSE);
		anim_out.start();
		show_hide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((Main_Activity)getActivity()).toggle();
			}
		});
		lv.setAdapter(new RelationshipsAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Fragment newContent = RelationFragment.NewInstance(pos);
				 if (newContent != null)
					switchFragment(newContent);				
			}
		});
		
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

	@Override
	public void onClick(View vi) {
		switch(vi.getId()){
		case R.id.selectdifferentaddress:
			break;
		case R.id.newaddress:
			break;
		}
	}


	@Override
	public void setData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setPurchase() {
		// TODO Auto-generated method stub
		
	}
	
	public class Dashboard_Relationship_AsyncGetAll extends AsyncTask<Object, Void, Boolean>{
		ProgressDialog pd;

		int id;

		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(getActivity(), null, "Refreshing your dashboard!!");
			pd.show();
			Static.rs.clear();
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
								JSONObject remaining = (JSONObject) e.get("remaining");
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
			if(result.booleanValue()){
				Static.rs.refresh(); // Remove to go back

				((RelationshipsAdapter)lv.getAdapter()).notifyDataSetChanged();
			}
			swipeLayout.setRefreshing(false);
	        if(pd.isShowing())
				pd.dismiss();
		}
	}

	@Override
	public void onRefresh() {
		new Dashboard_Relationship_AsyncGetAll().execute();
	}
}
