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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.viewpagerindicator.TabPageIndicator;

import dev.vision.relationshipninjas.Blur;
import dev.vision.relationshipninjas.Main_Activity;
import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.USER;
import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.purchase.Card;
import dev.vision.relationshipninjas.purchase.Purchase_Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class Fragment_User extends Fragment {
	
	CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    TabPageIndicator indicator;
	USER user = new USER();
	ImageLoader im = ImageLoader.getInstance();
	private CircularImageView me;
	private ImageView me_blur;
	private TextView me_name;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.user, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.pager); 
        indicator = (TabPageIndicator) v.findViewById(R.id.indicator);
        me = (CircularImageView) v.findViewById(R.id.circularImageView);
        me_blur = (ImageView) v.findViewById(R.id.imageView1);
        me_name = (TextView) v.findViewById(R.id.name_txt_info);

        
        return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);

		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(Color.TRANSPARENT)
		.showImageForEmptyUri(Color.TRANSPARENT)
		.showImageOnFail(Color.TRANSPARENT)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
		.threadPoolSize(2)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.defaultDisplayImageOptions(options)
		.build();
		im.init(config);

		 mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mCustomPagerAdapter);
        indicator.setViewPager(mViewPager);

    	new USER_DETAILS().execute();

       
    }
 
    class CustomPagerAdapter extends FragmentPagerAdapter {
 
        Context mContext;
 
        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }
 
        @Override
        public Fragment getItem(int position) {
 
            // Create fragment object
            Fragment fragment; 
            if(position ==0)
            	fragment= new Fragment_User_About();
        	else if(position ==1)
        		fragment= new Fragment_User_Addresses();
        	else if(position ==2)
        		fragment= new Fragment_User_CC();
        	else fragment= new Fragment_User_Orders();
            
            return fragment;
        }
 
        @Override
        public int getCount() {
            return 4;
        }
 
        @Override
        public CharSequence getPageTitle(int position) {
        	CharSequence s;
        	if(position ==0)
        		s= "ABOUT";
        	else if(position ==1)
        		s= "ADDRESSES";
        	else if(position ==2)
        		s= "CREDIT CARDS";
        	else s= "ORDERS";
        	
            return s;
        }
    }
 
   
    public class USER_API extends AsyncTask<Object, Void, Boolean>{
		ProgressDialog pd;
		int id;
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(getActivity(), null, "Loading Your Details!!");
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Object... params) {
		    
			DefaultHttpClient client = new DefaultHttpClient();
			boolean success = false;
			try{
				// add request header
				
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			    nameValuePairs.add(new BasicNameValuePair("operation", "get")); 
			    nameValuePairs.add(new BasicNameValuePair("auth_email", API.user.email)); 
			    nameValuePairs.add(new BasicNameValuePair("auth_password", API.user.pass)); 
			    String extra = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs));
				HttpGet request = new HttpGet(API.USER + "&" +extra);

				//request.setEntity();
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
						JSONObject r = js.getJSONObject("user");
						user = new USER();
						user.setId(r.optString("id"));
						user.setEmail(r.optString("email"));
						user.setPass(API.user.pass);
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
			pd.dismiss();
			
		}
	}
    
    
    public class USER_DETAILS extends AsyncTask<Object, Void, Boolean>{
    	
    	ProgressDialog pd;
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = ProgressDialog.show(getActivity(), null, "Loading Your Details!!");
			pd.show();
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
							
							user = new USER();
							
							user.setId(r.optString("id"));
							user.setEmail(r.optString("email"));
							user.setPass(API.user.pass);
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
				 API.user = user;
				 showData();
			}
			 if(pd.isShowing())
		       pd.dismiss();

			
		}
	}


	public void showData() {
		
		 mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), getActivity());
         mViewPager.setAdapter(mCustomPagerAdapter);
         indicator.setViewPager(mViewPager);
         me_name.setText(user.getName());
		 im.displayImage(user.image, me, new ImageLoadingListener() {
				
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				me.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		 });
			
		 im.loadImage(user.image, new ImageLoadingListener() {
				
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				me_blur.setImageBitmap(Blur.apply(getActivity(), loadedImage));
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		 });
	}
 
   
 
}
