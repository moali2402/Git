package dev.vision.relationshipninjas.Fragments;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.interfaces.Data;
import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.purchase.Purchase;
import dev.vision.relationshipninjas.purchase.Purchase_Activity;
import dev.vision.relationshipninjas.views.CircularImage;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Purchase_Delivery_Fragment extends Fragment implements OnClickListener, Data{
	
	private TextView selectDifferent;
	private TextView newAddress;
	private TextView myAddress;
	private TextView herAddress;
	private CircularImageView you;
	private CircularImageView her;
	private Purchase purchase;
	private Relationship relation;

	
	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inf, ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inf, container, savedInstanceState);
		View v= inf.inflate(R.layout.purchase_delivery, null);
		selectDifferent = (TextView) v.findViewById(R.id.selectdifferentaddress);
		newAddress = (TextView) v.findViewById(R.id.newaddress);
		myAddress = (TextView) v.findViewById(R.id.myaddress);
		herAddress = (TextView) v.findViewById(R.id.heraddress);
		you = (CircularImageView) v.findViewById(R.id.you);
		her = (CircularImageView) v.findViewById(R.id.her);
		//Clicks
		selectDifferent.setOnClickListener(this);
		newAddress.setOnClickListener(this);
		v.findViewById(R.id.you_parent).setOnClickListener(this);
		v.findViewById(R.id.her_parent).setOnClickListener(this);

		return v;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState); 
		setPurchase();
		setData();

		
	}

	public void setPurchase() {
		purchase = ((Purchase_Activity)getActivity()).getPurchase();
	}
	
	public void setData() {
		relation = purchase.getRelationship();
		Address db= purchase.getYouDefaultShipping();
		Address ds= purchase.getHerDefaultShipping();

		if(db!=null)
			myAddress.setText(db.toString());
		else{
			myAddress.setText("");
		}
		
		if(ds!=null)
			herAddress.setText(ds.toString());
		else{
			herAddress.setText("");
		}
		
		ImageLoader im = ImageLoader.getInstance();

		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.appicon)
		.showImageForEmptyUri(R.drawable.appicon)
		.showImageOnFail(R.drawable.appicon)
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

		
		//you.setImageDrawable(purchase.getUser().getImage());
		
		im.displayImage(purchase.getUser().getImage(), you,  new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				//vh.user.setImageBitmap(loadedImage);
				//purchase.getUser().set(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});

		if(relation!=null){
			Bitmap prof = relation.getProfile_pic();
	        if(prof != null)
	        	her.setImageBitmap(prof);
	        else
	            im.displayImage(relation.getImage(), her,  new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						//vh.user.setImageBitmap(loadedImage);
						relation.setProfile_pic(loadedImage);
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						
					}
				});
		}
	}

	@Override
	public void onClick(View vi) {
		switch(vi.getId()){
		case R.id.selectdifferentaddress:
			break;
		case R.id.newaddress:
			break;
		case R.id.you_parent:
			purchase.setPre_ShippingAddress(purchase.getYouDefaultShipping());
			purchase.setToMe(true);
			((Purchase_Activity)getActivity()).setDeliveryImage(you.getDrawable());
			break;
		case R.id.her_parent:
			purchase.setPre_ShippingAddress(purchase.getHerDefaultShipping());
			purchase.setToMe(false);
			((Purchase_Activity)getActivity()).setDeliveryImage(her.getDrawable());

			break;
		}
	}
}
