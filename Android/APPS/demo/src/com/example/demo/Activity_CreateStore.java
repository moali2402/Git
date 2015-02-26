package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.Activity;
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


public class Activity_CreateStore extends Activity implements OnClickListener, OnItemClickListener {
	EditText storeid;
	EditText storename;
	EditText email;

	Store stor;

	Button save;
	Button update;
	Button reset;
	
	ListView lv;

	ArrayList<Store> Stores = new ArrayList<Store>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_store);
    	 
        storeid = (EditText) findViewById(R.id.storecode);
    	storename = (EditText) findViewById(R.id.storename);
    	email = (EditText) findViewById(R.id.email);
    	

    	save = (Button) findViewById(R.id.save);
    	update = (Button) findViewById(R.id.update);
    	reset = (Button) findViewById(R.id.reset);
    	
    	save.setOnClickListener(this);
    	update.setOnClickListener(this);
    	reset.setOnClickListener(this);
    	 
    	lv = (ListView) findViewById(R.id.listView1);
    	lv.setAdapter(new StoreList(Stores));
    	lv.setOnItemClickListener(this);
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
		 stor=null;
		 storeid.setText("");
		 storename.setText("");
		 email.setText("");
	}


	private void Delete(Store store) {
		stor = null;
		new Delete().execute(store);
	}


	private void Update() {
		new Update().execute();
		//stor = null;

	}


	private void Save() {
		stor = null;
		new Save().execute();
	}
	

    class Save extends AsyncTask<Void, Void, Boolean>{

    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(Activity_CreateStore.this, null, "Creating store");
		}
    	
		@Override
		protected Boolean doInBackground(Void... params) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.CREATE_STORE);
			boolean success = false;

	        String sid = storeid.getText().toString();
	        String sname = storename.getText().toString();
	        String e = email.getText().toString();
	        
	        if(!sid.equals("") && !sname.equals("") && !e.equals("")){
				try{
					// add request header
					ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			        nameValuePair.add(new BasicNameValuePair("storeid", sid ));
			        nameValuePair.add(new BasicNameValuePair("storename", sname));
			        nameValuePair.add(new BasicNameValuePair("email", e));
	
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
							Store st = new Store(sid, sname, e);
							Stores.add(st);
						}
					}
						
				}catch (Exception ex){
					
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
				Toast.makeText(Activity_CreateStore.this, "There was a problem creating store", Toast.LENGTH_LONG).show();
			}
		}
			
    	
    }
    
    class Update extends AsyncTask<Void, Void, Boolean>{
    	
    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(Activity_CreateStore.this, null, "Updating store");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.UPDATE_STORE);
			boolean success = false;

	        String sid = storeid.getText().toString();
	        String sname = storename.getText().toString();
	        String e = email.getText().toString();
	       if(!sid.equals("") && !sname.equals("") && !e.equals(""))
	        {
				try{
					// add request header
					ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			        nameValuePair.add(new BasicNameValuePair("storeid", sid ));
			        nameValuePair.add(new BasicNameValuePair("storename", sname));
			        nameValuePair.add(new BasicNameValuePair("email", e));
			        
	
			        nameValuePair.add(new BasicNameValuePair("oldstoreid", stor.Storeid ));
			        nameValuePair.add(new BasicNameValuePair("oldstorename", stor.Storename));
			        nameValuePair.add(new BasicNameValuePair("oldemail", stor.email));
	
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
							Store st = new Store(sid, sname, e);
							Stores.set(Stores.indexOf(stor),st);
						}
					}
						
				}catch (Exception ex){
					
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
				Toast.makeText(Activity_CreateStore.this, "There was a problem updating store", Toast.LENGTH_LONG).show();
			}
		}
			
    	
    }
    
    class Delete extends AsyncTask<Store, Store, Boolean>{

    	ProgressDialog pr;
    	@Override
		protected void onPreExecute() {
    		super.onPreExecute();
			pr = ProgressDialog.show(Activity_CreateStore.this, null, "Deleting store");
		}
    	
		@Override
		protected Boolean doInBackground(Store... stores) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(API.DELETE_STORE);
			boolean success = false;
			Store stor = stores[0];
			try{
				// add request header
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		        nameValuePair.add(new BasicNameValuePair("storeid", stor.Storeid ));
		        nameValuePair.add(new BasicNameValuePair("storename", stor.Storename));
		        nameValuePair.add(new BasicNameValuePair("email", stor.email));

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
						Stores.remove(stor);
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
				Toast.makeText(Activity_CreateStore.this, "There was a problem deleting store", Toast.LENGTH_LONG).show();
			}
		}
			
    	
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
						Stores.clear();
						for (int i=0; i < ja.length(); i++) {
							JSONObject jo = ja.optJSONObject(i);
							String sid = jo.optString("storeid");
					        String sname = jo.optString("storename");
					        String e = jo.optString("email");
					        Store sto = new Store(sid, sname,e);
							Stores.add(sto);
						}
						//Log.d("Result", ""+Stores.size());
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
				Toast.makeText(Activity_CreateStore.this, "There was a problem retrieving stores or there are no stores created yet", Toast.LENGTH_LONG).show();
			}
		}
    	
    }
    

    class StoreList extends BaseAdapter{
    	
    	private ArrayList<Store> stores;

		public StoreList(ArrayList<Store> s){
    		this.stores = s;
    	}

		@Override
		public int getCount() {
			return stores.size();
		}

		@Override
		public Store getItem(int position) {
			return stores.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if(convertView == null){
				LayoutInflater lf = getLayoutInflater();
				holder = new ViewHolder();
				convertView = lf.inflate(R.layout.store_item, null);	
				holder.sid = (TextView) convertView.findViewById(R.id.sid);
				holder.sname = (TextView) convertView.findViewById(R.id.sname);	
				holder.email = (TextView) convertView.findViewById(R.id.email);	
				holder.delete = (Button)  convertView.findViewById(R.id.delete);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
	  	  	Store s= getItem(position);
	
            holder.sid.setText("Store Code : " +s.Storeid);
            holder.sname.setText("Store Name : " +s.Storename);
            holder.email.setText("Email : " + s.email);
            holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Delete(getItem(position));
				}
			});
			return convertView;
		}
		
		class ViewHolder{
			TextView sid;
			TextView sname;
			TextView email;
			Button delete;
		}
    	
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		update.setEnabled(true);
		stor = Stores.get(position);
		storeid.setText(stor.Storeid);
        storename.setText(stor.Storename);
        email.setText(stor.email);;
	}
}
