package dev.vision.relationshipninjas;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.Classes.Item;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ItemGridAdapter extends BaseAdapter {  


private Context mContext;  
 ArrayList<Item> items;
 SparseArray<Bitmap> gifts = new SparseArray<Bitmap>();
private ImageLoader imageLoader = ImageLoader.getInstance();
private DisplayImageOptions options;

 // Gets the context so it can be used later  
 public ItemGridAdapter(Context c, ArrayList<Item> i) {  
	 mContext = c; 
	 items = i;

     options = new DisplayImageOptions.Builder()
     .showImageForEmptyUri(null)
     .showImageOnFail(null)
     .showImageOnLoading(null)
     .resetViewBeforeLoading(true)
	.cacheOnDisc(true)
	.considerExifParams(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.build();

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c)
		.threadPoolSize(5)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.defaultDisplayImageOptions(options)
		.build();
		imageLoader.init(config);
 }  
  
 // Total number of things contained within the adapter  
 public int getCount() {  
  return items.size();  
 }  
  
  // Require for structure, not really used in my code.  
 public Object getItem(int position) {  
  return items.get(position);  
 }  
  
 // Require for structure, not really used in my code. Can  
 // be used to get the id of an item in the adapter for   
 // manual control.   
 public long getItemId(int position) {  
  return position;  
 }  
  
 @Override
 public View getView(final int position,   
                           View convertView, ViewGroup parent) {  
	 Item item = (Item) getItem(position);
	 ViewHolder vh;
	 if(convertView == null ){
		 vh = new ViewHolder();
		 convertView = new ImageView(mContext);
		 vh.iV = (ImageView) convertView;
		 
		 convertView.setTag(vh);
	 }else{
		 vh = (ViewHolder) convertView.getTag();
	 }
	 vh.iV.setScaleType(ImageView.ScaleType.CENTER_CROP);
	 vh.iV.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, 250));
	 //if(gifts.get(position) == null)
			imageLoader.displayImage(item.getThumbnail(), vh.iV, options, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					
					float oriWidth = loadedImage.getWidth() ;
					float oriHeight = loadedImage.getHeight() ;

					
				    		   
				    float aspectRatioHie = oriHeight/oriWidth;  
				    
				    float aspectRatioWid = oriWidth/oriHeight;
				   // Bitmap bmp = Bitmap.createScaledBitmap(loadedImage, oriWidth> oriHeight ? (int) ((viewHeight)*aspectRatioWid) : viewWidth, oriWidth> oriHeight ? viewHeight : (int) (viewWidth*aspectRatioHie), false);
					//iV.setImageBitmap(loadedImage);
				    //gifts.put(position,loadedImage);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {					
				}
			});
		//else iV.setImageBitmap(gifts.get(position));
		  
		return convertView;  
 }
 
 public class ViewHolder {
	 ImageView iV;
 }

public void clearr(ArrayList<Item> gifts2) {
		items.clear();
		items.addAll(gifts2);
		notifyDataSetChanged();
}  
}  