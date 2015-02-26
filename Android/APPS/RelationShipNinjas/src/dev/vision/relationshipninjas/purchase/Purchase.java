package dev.vision.relationshipninjas.purchase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import android.R.bool;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import dev.vision.relationshipninjas.Activity_History;
import dev.vision.relationshipninjas.GiftsAdapter;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.USER;

public class Purchase {

	private USER user;
	private Date preffered_date;
	private Item item;
	private Relationship relation;
	private Address Pre_ShippingAddress = new Address();
	private Event event;
	private boolean tome;
	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}



	private FragmentActivity context;

	

	public void setUser(USER user) {
		this.user = user;
	}
	
	/**
	 * @return the user
	 */
	public USER getUser() {
		return user;
	}

	/**
	 * @return the preffered_date
	 */
	public Date getPreDate() {
		return preffered_date;
	}

	/**
	 * @param preffered_date the preffered_date to set
	 */
	public void setPreDate(Date preffered_date) {
		this.preffered_date = preffered_date;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}

	public void setRelation(Relationship relation) {
		this.relation = relation;
	}

	public Relationship getRelationship() {
		return this.relation;
	}

	public Address getBillingAddress() {
		return user.getDBillingAddress();
	}
	
	public Card getCreditCard() {
		return user.getDCreditCard();
	}
	
	public Address getYouDefaultShipping() {
		return user.getDShippingAddress();
	}


	public Address getHerDefaultShipping() {
		return relation.getDShippingAddress();
	}
	
	
	/**
	 * @return the pre_ShippingAddress
	 */
	public Address getPre_ShippingAddress() {
		return Pre_ShippingAddress;
	}

	/**
	 * @param pre_ShippingAddress the pre_ShippingAddress to set
	 */
	public void setPre_ShippingAddress(Address pre_ShippingAddress) {
		Pre_ShippingAddress = pre_ShippingAddress;
	}
	
	/**
	 * @param pre_ShippingAddress the pre_ShippingAddress to set
	 */
	public void setToMe(boolean to) {
		tome = to;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
		
	public class Place_Order extends AsyncTask<Object, Void, Boolean>
	{

		private ProgressDialog Pd;
		private String mes = "";

		@Override
		public void onPreExecute(){
			super.onPreExecute();
			Pd = ProgressDialog.show(context, null, "Processing Payment, Please Wait!");
		}
		

		@Override
		protected Boolean doInBackground(Object... params) {
			 
			DefaultHttpClient client = new DefaultHttpClient();
			boolean success = false;
			try{
				/*
				request.addHeader("Authorization", "NLAuth nlauth_account=3671462, nlauth_email="+API.user.email+", nlauth_signature="+API.user.pass+", nlauth_role="+API.user.role);
				request.addHeader("Content-Type", "application/json");
				request.addHeader("User-Agent-x", "SuiteScript-Call");
				*/
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			    nameValuePairs.add(new BasicNameValuePair("operation", "create"));
				nameValuePairs.add(new BasicNameValuePair("eventid", event.getId()));
			    nameValuePairs.add(new BasicNameValuePair("itemid", item.getId()));
				nameValuePairs.add(new BasicNameValuePair("creditcardid", getCreditCard().getId()));
				nameValuePairs.add(new BasicNameValuePair("addressid", Pre_ShippingAddress.getId()));
				nameValuePairs.add(new BasicNameValuePair("billingid", getBillingAddress().getId()));
				nameValuePairs.add(new BasicNameValuePair("deliverydate", ""+(new SimpleDateFormat("yyyy-MM-dd").format(getPreDate()))));

				nameValuePairs.add(new BasicNameValuePair("shipmethod", "25599"));
			    nameValuePairs.add(new BasicNameValuePair("shipcode", "Standard"));

				nameValuePairs.add(new BasicNameValuePair("tome", ""+tome));
			    nameValuePairs.add(new BasicNameValuePair("auth_email", API.user.email)); 
			    nameValuePairs.add(new BasicNameValuePair("auth_password", API.user.pass)); 

			    String extraParam = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpGet request = new HttpGet(API.BUY +"&"+ extraParam );

				request.setHeader("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
				/*
				JSONObject json = new JSONObject();
				json.put("operation", "create");
				json.put("eventid", event.getId());
				json.put("itemid", item.getId());
				json.put("creditcardid", getCreditCard().getId());
				json.put("addressid", Pre_ShippingAddress.getId());
				json.put("billingid", getBillingAddress().getId());
				json.put("shipdate", preffered_date);
				json.put("tome", tome);

			
				request.setEntity(new StringEntity(json.toString()));
				*/
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
					if(!success)
						mes = js.getString("message");
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return success;
		}

		@Override
		public void onPostExecute(Boolean result){
			if(result.booleanValue()){
				mes = "Your order is now being processed";
				context.finish();
			}else{
			}
			Toast.makeText(context, mes, Toast.LENGTH_LONG).show();
			Pd.dismiss();

		}
		   
	}



	public void placeOrder(FragmentActivity activity) {
		this.context = activity;
		new Place_Order().execute();
	}

	
}
