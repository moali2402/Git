package dev.vision.relationshipninjas.purchase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Static;
import dev.vision.relationshipninjas.TestFragmentAdapter;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Event.ALERTLEVEL;
import dev.vision.relationshipninjas.Classes.Event.DATEUNIT;
import dev.vision.relationshipninjas.Classes.Event.STATUS;
import dev.vision.relationshipninjas.Classes.Relationship.TYPE;
import dev.vision.relationshipninjas.Classes.USER;
import dev.vision.relationshipninjas.Fragments.Purchase_Confirmation_Fragment;
import dev.vision.relationshipninjas.Fragments.Purchase_Delivery_Fragment;
import dev.vision.relationshipninjas.Fragments.Purchase_ShippingDate_Fragment;
import dev.vision.relationshipninjas.R.id;
import dev.vision.relationshipninjas.R.layout;
import dev.vision.relationshipninjas.interfaces.Data;
import dev.vision.relationshipninjas.views.CircularImage;
import dev.vision.relationshipninjas.views.UnderLinedLinear;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Purchase_Activity extends ActionBarActivity{
	
	static Purchase purchase = new Purchase();
	ViewPager vf;
	Item item;
	private Relationship relation;
	private String eventId;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase);
		final UnderLinedLinear ult = (UnderLinedLinear) findViewById(R.id.underlinedtab);
		item = (Item) getIntent().getExtras().get("item");
		eventId = getIntent().getExtras().getString("eId");
		relation = (Relationship) Static.rs.get(getIntent().getExtras().getString("rId"));
		vf = (ViewPager) findViewById(R.id.pagerr);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		setItemImage();

		vf.setOnPageChangeListener(new OnPageChangeListener() {
			
			@SuppressLint("NewApi") @Override //API 11
			public void onPageSelected(int i) {
				ult.setSelectedItem(i);
				if(i >=1){
				   ((TextView) findViewById(R.id.TextView01)).setTextColor(Color.BLACK);
				   ((ImageView) findViewById(R.id.ImageView01)).setActivated(true);
				}else{
				   ((TextView) findViewById(R.id.TextView01)).setTextColor(getResources().getColor(android.R.color.darker_gray));
				   ((ImageView) findViewById(R.id.ImageView01)).setActivated(false);
				}
				if(i >=2){
				   ((TextView) findViewById(R.id.TextView03)).setTextColor(Color.BLACK);
				   ((ImageView) findViewById(R.id.ImageView03)).setActivated(true);
				}else{
				   ((TextView) findViewById(R.id.TextView03)).setTextColor(getResources().getColor(android.R.color.darker_gray));
				   ((ImageView) findViewById(R.id.ImageView03)).setActivated(false);
				}
				((Data)((TestFragmentAdapter)vf.getAdapter()).getItem(i)).setData();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});

		ult.setSelectedItem(0);
		new PURCHASE_DETAILS().execute();
		
	}
	

	private TestFragmentAdapter setupPages(){
		Purchase_Delivery_Fragment pd = new Purchase_Delivery_Fragment();
		Purchase_ShippingDate_Fragment psd = new Purchase_ShippingDate_Fragment();
		Purchase_Confirmation_Fragment pc = new Purchase_Confirmation_Fragment();

		ArrayList<Fragment> frag = new ArrayList<Fragment>();
		frag.add(pd);
		frag.add(psd);
		frag.add(pc);
		TestFragmentAdapter tfa = new TestFragmentAdapter(getSupportFragmentManager(), frag);
		return tfa;
	}
	
	public class PURCHASE_DETAILS extends AsyncTask<Object, Void, Boolean>{
		private ProgressDialog relationPd;

		@Override
		public void onPreExecute(){
			super.onPreExecute();
			relationPd = ProgressDialog.show(Purchase_Activity.this, null, "Loading Details!!");
			relationPd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			boolean success = false;
			try{
				// add request header
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			    nameValuePairs.add(new BasicNameValuePair("operation", "getpurchase")); 
			    nameValuePairs.add(new BasicNameValuePair("auth_email", API.user.email)); 
			    nameValuePairs.add(new BasicNameValuePair("auth_password", API.user.pass)); 

			    String extraParam = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs));
				
			    HttpGet request = new HttpGet(API.PURCHASE +"&"+ extraParam);
				request.setHeader("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
				
				HttpResponse response = client.execute(request);
			 

				if(response.getStatusLine().getStatusCode() == 200){
					System.out.println("Success2");
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
						JSONObject data = js.getJSONObject("data");
						if(data!=null){
							JSONObject r = data.getJSONObject("user");
							
							USER user = new USER();
							
							user.setId(r.optString("id"));
							user.setEmail(r.optString("email"));
							user.setFirstname(r.optString("firstname"));
							user.setLastname(r.optString("lastname"));
							user.setMiddlename(r.optString("middlename"));
							user.setBirthday(r.optString("birthday"));
							user.setGender(r.optString("gender"));
							user.setImage(r.optString("image"));
							user.setProgress(r.optInt("progress"));
							user.setLevel(r.optInt("level"));
							user.setStatus(r.optString("status"));
							user.setRank(r.optString("rank"));
							user.setPoints(r.optString("points"));						
								
							JSONArray billing_addresses = data.getJSONArray("billingaddresses");
							for(int i= 0; i < billing_addresses.length(); i++){
								JSONObject e = (JSONObject) billing_addresses.get(i);
								Address ad = new Address();
								
								ad.setId(e.optString("id"));
								ad.setName(e.optString("name"));
								ad.setAddressee(e.optString("addressee"));
								ad.setAddress1(e.optString("address1"));
								ad.setAddress2(e.optString("address2"));
								ad.setCity(e.optString("city"));
								ad.setCountry(e.optString("country"));
								ad.setDefaultbilling(e.optBoolean("defaultbilling"));
								ad.setDefaultshipping(e.optBoolean("defaultshipping"));
								ad.setPhone(e.optString("phone"));
								ad.setState(e.optString("state"));
								ad.setZip(e.optString("zip"));
								
								user.addBillingAddress(ad);
							}
							
							JSONArray shipping_address = data.getJSONArray("shippingaddresses");
							for(int i= 0; i < shipping_address.length(); i++){
								JSONObject e = (JSONObject) shipping_address.get(i);
								Address ad = new Address();
								
								ad.setId(e.optString("id"));
								ad.setName(e.optString("name"));
								ad.setAddressee(e.optString("addressee"));
								ad.setAddress1(e.optString("address1"));
								ad.setAddress2(e.optString("address2"));
								ad.setCity(e.optString("city"));
								ad.setCountry(e.optString("country"));
								ad.setDefaultbilling(e.optBoolean("defaultbilling"));
								ad.setDefaultshipping(e.optBoolean("defaultshipping"));
								ad.setPhone(e.optString("phone"));
								ad.setState(e.optString("state"));
								ad.setZip(e.optString("zip"));
								
								user.addShippingAddress(ad);
							}
							
							JSONArray credit_cards = data.getJSONArray("creditcards");
							for(int i= 0; i < credit_cards.length(); i++){
								JSONObject e = (JSONObject) credit_cards.get(i);
								Card c = new Card();
								
								c.setId(e.optString("id"));
								c.setName(e.optString("name"));
								c.setNumber(e.optString("number"));
								c.setDefault(e.optBoolean("default"));
								c.setType(e.optString("type"));
								c.setExpmonth(e.optString("expmonth"));
								c.setExpyear(e.optString("expyear"));
								c.setExpdate(e.optString("expdate"));
								
								user.addCard(c);
							}
							
							purchase.setUser(user);

							
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
			purchase.setItem(item);
			purchase.setRelation(relation);
			purchase.setEvent(relation.getEvent(eventId));
			
			vf.setAdapter(setupPages());

			
			 if(relationPd.isShowing())
		       relationPd.dismiss();

			
		}
	}

	/**
	 * @return the purchase
	 */
	public Purchase getPurchase() {
		return purchase;
	}

	/**
	 * @param purchase the purchase to set
	 */
	public void setPurchase(Purchase purchase) {
		Purchase_Activity.purchase = purchase;
	}

	public void setDeliveryImage(Drawable drawable) {
		((CircularImage) findViewById(R.id.ImageView02)).setImageDrawable(drawable);
		((CircularImage) findViewById(R.id.ImageView02)).setCircular(true);
	}

	private void setItemImage() {
		ImageLoader.getInstance().displayImage(item.getThumbnail(), (ImageView) findViewById(R.id.ImageView04));
	}
}
