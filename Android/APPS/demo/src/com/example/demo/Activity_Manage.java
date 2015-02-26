package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.demo.Activity_CreateStore.StoreList;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.ProgressDialog;
import android.app.Fragment.SavedState;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;


public class Activity_Manage extends ActionBarActivity implements OnClickListener, OnItemClickListener {
	EditText username;
	EditText password;
	//Store stor;
	Spinner store_spinner;
	Button save;
	Button update;
	Button reset;
	User use;
	
	ListView lv;

	ArrayList<User> Users = new ArrayList<User>();
	ArrayList<Store> storess = new ArrayList<Store>();
	List<String> storess_string = new ArrayList<String>();
	private Spinner spinner;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.manage_users);
    	 
        username = (EditText) findViewById(R.id.username);
    	password = (EditText) findViewById(R.id.password);
    	
    	spinner = (Spinner) findViewById(R.id.spinner1);
    	store_spinner = (Spinner) findViewById(R.id.stores_spinner);


    	save = (Button) findViewById(R.id.save);
    	update = (Button) findViewById(R.id.update);
    	reset = (Button) findViewById(R.id.reset);
    	
    	
    	save.setOnClickListener(this);
    	update.setOnClickListener(this);
    	reset.setOnClickListener(this);
    	 
    	lv = (ListView) findViewById(R.id.listView1);
    	lv.setAdapter(new StoreList(Users));
    	lv.setOnItemClickListener(this);
    	new getUsers().execute(); 
    	
    	new getStores().execute(); 

    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			Save();
			break;
		case R.id.update:
			Update();
			break;
		case R.id.reset:
			Reset();
			break;
		default:
			break;
		}
		
	}


	private void Reset() {
		 update.setEnabled(false);
		 use=null;
		 username.setText("");
		 password.setText("");
		 spinner.setSelection(0);
	}


	private void Delete(User user) {
		use = null;
		new Delete().execute(user);
	}


	private void Update() {
		new Update().execute();
		//stor = null;

	}


	private void Save() {
		use = null;
		new Save().execute();
	}
	

    class Save extends AsyncTask<Void, Void, Boolean>{
    	
    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(Activity_Manage.this, null, "Creating user");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.CREATE_USER);
			boolean success = false;

	        String u = username.getText().toString();
	        String p = password.getText().toString();
	        String s = "" +spinner.getSelectedItemPosition();
	     
	        int store_index = store_spinner.getSelectedItemPosition();
	        Store st = storess.get(store_index);
	        
	        if( !u.equals("") && !p.equals("")){
				try{
					// add request header
					ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			        nameValuePair.add(new BasicNameValuePair("storeid", st.Storeid ));
			        //nameValuePair.add(new BasicNameValuePair("storename", st.Storename));
			        //nameValuePair.add(new BasicNameValuePair("email", st.email));

			        nameValuePair.add(new BasicNameValuePair("username", u));
			        nameValuePair.add(new BasicNameValuePair("password", p));
			        nameValuePair.add(new BasicNameValuePair("status", s));
	
					request.setEntity(new UrlEncodedFormEntity(nameValuePair,
					        "UTF-8"));
				
					HttpResponse httpResponse = client.execute(request);
					//HttpEntity httpEntity = httpResponse.getEntity();
					//httpResponse = EntityUtils.toString(httpEntity);
	          
			
					
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						BufferedReader rd = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity().getContent()));
					 
						StringBuffer result = new StringBuffer();
						String line = "";
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}
						Log.d("Result", result.toString());
						JSONObject js = new JSONObject(result.toString());
						int ss = js.getInt("success");
						success = ss == 1 ? true : false ;
						if(success)
						{
							User user = new User(u, p, s.equals("0") ?  false : true, st.Storeid, st.Storename, st.email);
							Users.add(user);
						}
					}
						
				}catch (Exception e){
					
				}
	        }
			return success;
		}
		
		@Override
		public void onPostExecute(Boolean b){
			pr.dismiss();
			if(b.booleanValue()){
				((StoreList)lv.getAdapter()).notifyDataSetChanged();
				Utility.setListViewHeightBasedOnChildren(lv);
				Reset();
			}else{
				Toast.makeText(Activity_Manage.this, "There was a problem creating user", Toast.LENGTH_LONG).show();
			}
		}
			
    	
    }
    
    class Update extends AsyncTask<Void, Void, Boolean>{
    	
    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(Activity_Manage.this, null, "Updating user");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
	 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.UPDATE_USER);
			boolean success = false;

			String u = username.getText().toString();
		    String p = password.getText().toString();
		    String s = "" +spinner.getSelectedItemPosition();
		     
		    int store_index = store_spinner.getSelectedItemPosition();
		    Store st = storess.get(store_index);
		        			
	        if( !u.equals("") && !p.equals(""))
	        {
				try{
					// add request header
					ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("storeid", st.Storeid ));
			        //nameValuePair.add(new BasicNameValuePair("storename", st.Storename));
			        //nameValuePair.add(new BasicNameValuePair("email", st.email));

			        nameValuePair.add(new BasicNameValuePair("username", u));
			        nameValuePair.add(new BasicNameValuePair("password", p));
			        nameValuePair.add(new BasicNameValuePair("status", s));;
			        
	
			        nameValuePair.add(new BasicNameValuePair("oldstoreid", use.store.Storeid ));
			        nameValuePair.add(new BasicNameValuePair("oldusername", use.Username));
			        nameValuePair.add(new BasicNameValuePair("oldpassword", use.password));
			        nameValuePair.add(new BasicNameValuePair("oldstatus", use.enabled? "1" : "0"));
	
					request.setEntity(new UrlEncodedFormEntity(nameValuePair,
					        "UTF-8"));
				
					HttpResponse httpResponse = client.execute(request);
					//HttpEntity httpEntity = httpResponse.getEntity();
					//httpResponse = EntityUtils.toString(httpEntity);
	          
			
					
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						BufferedReader rd = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity().getContent()));
					 
						StringBuffer result = new StringBuffer();
						String line = "";
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}
						Log.d("Result", result.toString());
						JSONObject js = new JSONObject(result.toString());
						int ss = js.getInt("success");
						success = ss == 1 ? true : false ;
						if(success)
						{
							User user = new User(u, p, s.equals("0") ?  false : true, st.Storeid, st.Storename, st.email);
							Users.set(Users.indexOf(use),user);
						}
					}
						
				}catch (Exception e){
					
				}
	        }
			return success;
		}
		
		@Override
		public void onPostExecute(Boolean b){
			pr.dismiss();
			if(b.booleanValue()){
				((StoreList)lv.getAdapter()).notifyDataSetChanged();
				Utility.setListViewHeightBasedOnChildren(lv);
				Reset();
			}else{
				Toast.makeText(Activity_Manage.this, "There was a problem updating user", Toast.LENGTH_LONG).show();
			}
		}
			
    	
    }
    
    class Delete extends AsyncTask<User, Store, Boolean>{
    	
    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(Activity_Manage.this, null, "Deleting user");
		}

		@Override
		protected Boolean doInBackground(User... users) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.DELETE_USER);
			boolean success = false;
			User user = users[0];
			try{
				// add request header
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		        nameValuePair.add(new BasicNameValuePair("storeid", user.store.Storeid ));
		        nameValuePair.add(new BasicNameValuePair("username", user.Username));
		        nameValuePair.add(new BasicNameValuePair("password", user.password));
		        nameValuePair.add(new BasicNameValuePair("status", user.enabled? "1" : "0"));

				request.setEntity(new UrlEncodedFormEntity(nameValuePair,
				        "UTF-8"));
			
				HttpResponse httpResponse = client.execute(request);
				//HttpEntity httpEntity = httpResponse.getEntity();
				//httpResponse = EntityUtils.toString(httpEntity);
          
		
				
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					//Log.d("Result", result.toString());
					JSONObject js = new JSONObject(result.toString());
					int ss = js.getInt("success");
					success = ss == 1 ? true : false ;
					if(success)
					{
						Users.remove(user);
					}
				}
					
			}catch (Exception e){
				
			}
			return success;
		}
		
		@Override
		public void onPostExecute(Boolean b){
			pr.dismiss();
			if(b.booleanValue()){
				((StoreList)lv.getAdapter()).notifyDataSetChanged();
				Utility.setListViewHeightBasedOnChildren(lv);
				Reset();
			}else{
				Toast.makeText(Activity_Manage.this, "There was a problem deleting user", Toast.LENGTH_LONG).show();
			}
		}
			
    	
    }
    

    
    class StoreList extends BaseAdapter{
    	
    	private ArrayList<User> Users;


		public StoreList(ArrayList<User> users) {
			this.Users =users;
		}

		@Override
		public int getCount() {
			return Users.size();
		}

		@Override
		public User getItem(int position) {
			// TODO Auto-generated method stub
			return Users.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if(convertView == null){
				LayoutInflater lf = getLayoutInflater();
				holder = new ViewHolder();
				convertView = lf.inflate(R.layout.user_item, null);
				holder.u = (TextView) convertView.findViewById(R.id.u);
				holder.p = (TextView) convertView.findViewById(R.id.p);	
				holder.sid = (TextView) convertView.findViewById(R.id.sid);
				holder.status = (TextView) convertView.findViewById(R.id.status);	
				holder.delete = (Button)  convertView.findViewById(R.id.delete);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
	  	  	User u= getItem(position);
	
			
			holder.u.setText("Username : " + u.Username);
            holder.p.setText("Password : " + u.password);
            holder.sid.setText("Store Code : " + u.store.Storeid + " / " + u.store.Storename);
            holder.status.setText("Status : " + (u.enabled? "Enabled" : "Disabled"));
            holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Delete(getItem(position));
				}
			});
			return convertView;
		}
		
		class ViewHolder{
			TextView u;
			TextView p;
			TextView sid;
			TextView status;
			Button delete;
		}
    	
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		update.setEnabled(true);
		use = Users.get(position);
		username.setText(use.Username);;
        password.setText(use.password);
		store_spinner.setSelection(storess.indexOf(use.store));
        spinner.setSelection(use.enabled ? 1 : 0);;
	}
	
	
	class getStores extends AsyncTask<Void, Void, Boolean>{

		
		@Override
		protected Boolean doInBackground(Void... params) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.GET_STORES);
			boolean success = false;

	          try{
				// add request header
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		       
				request.setEntity(new UrlEncodedFormEntity(nameValuePair,
				        "UTF-8"));
			
				HttpResponse httpResponse = client.execute(request);
				//HttpEntity httpEntity = httpResponse.getEntity();
				//httpResponse = EntityUtils.toString(httpEntity);
          
		
				
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					
					
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					if(success){
						JSONArray ja = js.getJSONArray("data");
						storess.clear();
						for (int i=0; i < ja.length(); i++) {
							JSONObject jo = ja.optJSONObject(i);
							String sid = jo.optString("storeid");
					        String sname = jo.optString("storename");
					        String e = jo.optString("email");
					        Store sto = new Store(sid, sname,e);
					        storess.add(sto);
					        storess_string.add(sid + " : " + sname);
						}
						Log.d("Result", ""+storess.size());

					}
					
				}
			}catch (Exception e){
				
			}
			return success;
		}
		
		@Override
		public void onPostExecute(Boolean b){
			if(b.booleanValue()){
				((StoreList)lv.getAdapter()).notifyDataSetChanged();			
				 ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity_Manage.this, android.R.layout.simple_spinner_item,storess_string);

				store_spinner.setAdapter(adapter);
			}else{
				Toast.makeText(Activity_Manage.this, "There was a problem retrieving stores or there are no stores created yet", Toast.LENGTH_LONG).show();
			}
		}
    	
    }
    
    
    class getUsers extends AsyncTask<Void, Void, Boolean>{

		
		@Override
		protected Boolean doInBackground(Void... params) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.GET_USERS);
			boolean success = false;

	          try{
				// add request header
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		       
				request.setEntity(new UrlEncodedFormEntity(nameValuePair,
				        "UTF-8"));
			
				HttpResponse httpResponse = client.execute(request);
				//HttpEntity httpEntity = httpResponse.getEntity();
				//httpResponse = EntityUtils.toString(httpEntity);
          
		
				
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					
					
					JSONObject js = new JSONObject(result.toString());
					success = js.getBoolean("success");
					if(success){
						JSONArray ja = js.getJSONArray("data");
						Users.clear();
						for (int i=0; i < ja.length(); i++) {
							JSONObject jo = ja.optJSONObject(i);
							String u = jo.optString("username");
					        String p = jo.optString("password");
					        boolean s = jo.optString("status", "0").equals("1") ? true : false ;
					        String sid = jo.optString("storeid");
					        String sname = jo.optString("storename");
					        String e = jo.optString("email");

					        User us = new User(u, p, s, sid, sname, e);
							Users.add(us);
						}

					}
					
				}
			}catch (Exception e){
				
			}
			return success;
		}
		
		@Override
		public void onPostExecute(Boolean b){
			findViewById(R.id.progressBar1).setVisibility(View.GONE);
			if(b.booleanValue()){
				((StoreList)lv.getAdapter()).notifyDataSetChanged();			
				Utility.setListViewHeightBasedOnChildren(lv);
			}else{
				Toast.makeText(Activity_Manage.this, "There was a problem retrieving users or there are no users created yet", Toast.LENGTH_LONG).show();
			}
		}
    	
    }
    

}
