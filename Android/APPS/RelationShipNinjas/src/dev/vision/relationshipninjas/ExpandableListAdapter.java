package dev.vision.relationshipninjas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 














import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import dev.vision.relationshipninjas.Classes.Relationship;
import dev.vision.relationshipninjas.Classes.Relationships;
import dev.vision.relationshipninjas.views.CircularImage;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    // private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    //private HashMap<String, List<String>> _listDataChild;
    private Relationships Relations; // header titles
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData) {
        this._context = context;

        options = new DisplayImageOptions.Builder()
		.showImageOnLoading(android.R.color.transparent)
		.showImageForEmptyUri(android.R.color.transparent)
		.showImageOnFail(android.R.color.transparent)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPoolSize(2)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.defaultDisplayImageOptions(options)
		.build();
		imageLoader.init(config);
		
        //this._listDataHeader = listDataHeader;
        //this._listDataChild = listChildData;
    }
    
    public ExpandableListAdapter(Context context, Relationships relations) {
        this._context = context;
        options = new DisplayImageOptions.Builder()
		.showImageOnLoading(android.R.color.transparent)
		.showImageForEmptyUri(android.R.color.transparent)
		.showImageOnFail(android.R.color.transparent)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPoolSize(2)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.defaultDisplayImageOptions(options)
		.build();
		imageLoader.init(config);
        this.Relations = relations;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
       // return this._listDataChild.get(this._listDataHeader.get(groupPosition))
       //         .get(childPosititon);
    	
    	return Relations.get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @SuppressLint("NewApi") @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	 LayoutInflater infalInflater = (LayoutInflater) this._context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder vh = new ViewHolder();
    	if(childPosition < getChildrenCount(groupPosition) /* uncomment -1 */){
            final Relationship relation = (Relationship) getChild(groupPosition, childPosition);

    		convertView = infalInflater.inflate(R.layout.menu_listitem_child, null);
    		convertView.setPadding(10, 10, 10, 10);
            vh.txt = (TextView) convertView.findViewById(R.id.textView1);
            vh.Icon = (CircularImage) convertView.findViewById(R.id.imageView1);
            convertView.setBackground(null);
            vh.txt.setText(relation.getName());
            Bitmap prof = relation.getProfile_pic();
            if(prof != null)
            	vh.Icon.setImageBitmap(prof);
            else
	            imageLoader.loadImage(relation.getImage(), new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						vh.Icon.setImageBitmap(loadedImage);
						relation.setProfile_pic(loadedImage);
						notifyDataSetChanged();
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						
					}
				});
    	}else{
    		//Uncomment for menu
        	//convertView =  infalInflater.inflate(R.layout.menu_relation_ship, null);
        	//convertView.setBackgroundColor(Color.parseColor("#90000000"));
    	}
 

 
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
    	int i=0;
    	if(groupPosition == 1)
    	 i = Relations.size(); /* uncomment +1; */
    		//	i = this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        return i;
    }
 
 
    @Override
    public int getGroupCount() {
        return 4;
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @SuppressLint("NewApi") @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        //String headerTitle = (String) getGroup(groupPosition);
    	ViewHolder vh;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.menu_listitem_parent, null);
            vh = new ViewHolder();
            vh.Icon = (ImageView) convertView.findViewById(R.id.imageView1);
            vh.txt = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(vh);
        }else{
        	vh= (ViewHolder) convertView.getTag();
        }
		convertView.setPadding(10, 0, 10, 0);

        vh.txt.setTypeface(null, Typeface.BOLD);
        vh.txt.getLayoutParams().height = LayoutParams.FILL_PARENT;
        int id = 0;
    	String headerTitle = null;
    	convertView.setBackground(null);
        switch (groupPosition) {
		case 0:
			
			id = R.drawable.new_home;
			headerTitle = "Home";
			break;
			
			
		case 1:
			id = R.drawable.new_relationships;
			headerTitle = "Relatioships";
			if(isExpanded){
				convertView.setBackgroundColor(Color.parseColor("#90000000"));
			}
			break;
		case 2:
			
			id = R.drawable.new_profile;
			headerTitle = "Profile";
			break;
			/*
		case 3:
			
			id = R.drawable.friend;
			headerTitle = "Invites";
			break;
	    
		case 3:
			id = R.drawable.notif;
			headerTitle = "Notifications";

			 break;
			 
		case 4:
			id = R.drawable.cale;
			headerTitle = "All Calender";

			break;
		
		case 4:
			id = R.drawable.settings;
			headerTitle = "Settings";

			break;
			*/
		case 3:
			id = R.drawable.new_signout;
			headerTitle = "Sign Out";

			break;
		default:
			break;
		}
          	vh.Icon.setImageResource(id);
          	vh.txt.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    private class ViewHolder{
    	ImageView Icon;
    	TextView txt;
    }

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}