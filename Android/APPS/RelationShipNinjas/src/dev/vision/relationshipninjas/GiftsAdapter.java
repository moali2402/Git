package dev.vision.relationshipninjas;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.views.RoundedImage;
import dev.vision.relationshipninjas.views.view;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GiftsAdapter extends BaseAdapter {
	ArrayList<Item> l = new ArrayList<Item>();
	SparseArray<Bitmap> gifts = new 	SparseArray<Bitmap>();

	Context context;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	int viewWidth;
	int viewHeight;
	public GiftsAdapter(Context context, ArrayList<Item> gifts2, ImageLoader imageLoader, int viewWidth, int viewHeight){
		super();
		this.context=context;
		l.addAll(gifts2);
		options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .showImageForEmptyUri(null)
        .showImageOnFail(null)
        .showImageOnLoading(null)
        .build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        // You can pass your own memory cache implementation
        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
        .build();

		this.imageLoader = imageLoader;

		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}
	
	@Override
	public int getCount() {
		return l.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(final int position, View arg1, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View v = inflater.inflate(R.layout.list_row, null);
		v.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, 700));//.height=200;
		((view)v.findViewById(R.id.view1)).setPos(position);
		
		Item item = l.get(position);

		
		final RoundedImage ri =((RoundedImage) v.findViewById(R.id.imageView1));
		final TextView nam =((TextView) v.findViewById(R.id.textView1));
		final TextView bo =((TextView) v.findViewById(R.id.textView2));
		final TextView right =((TextView) v.findViewById(R.id.textView3));
		
		nam.setText(item.getName());
		bo.setText(item.getDescription());
		//right.setText(item.getName());
		
		if(gifts.get(position) == null)
			imageLoader.loadImage(item.getImage(), options, new ImageLoadingListener() {
				
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
				    Bitmap bmp = Bitmap.createScaledBitmap(loadedImage, oriWidth> oriHeight ? (int) ((viewHeight)*aspectRatioWid) : viewWidth, oriWidth> oriHeight ? viewHeight : (int) (viewWidth*aspectRatioHie), false);
					ri.setImageBitmap(bmp);
				    gifts.put(position,bmp);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {					
				}
			});
		else ri.setImageBitmap(gifts.get(position));
		
		return v;
	}

	public void clear(ArrayList<Item> ite){
		l.clear();
		l.addAll(ite);
		notifyDataSetChanged();
	}
}
