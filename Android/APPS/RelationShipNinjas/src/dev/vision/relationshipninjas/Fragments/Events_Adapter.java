package dev.vision.relationshipninjas.Fragments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.Classes.Event.ALERTLEVEL;
import dev.vision.relationshipninjas.Classes.Event.TYPE;
import dev.vision.relationshipninjas.Classes.Events;
import dev.vision.relationshipninjas.Classes.Relationship;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Events_Adapter extends BaseAdapter {
	
	Events l;
	Context context;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	int viewWidth;
	int viewHeight;
	LayoutInflater inflater;
	private Relationship relation;
	public Events_Adapter(Context context, Relationship relation, Events events, ImageLoader imageLoader){
		super();
		this.context=context;
		this.relation=relation;
		l = events;
		inflater = LayoutInflater.from(context);

		options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .showImageForEmptyUri(null)
        .showImageOnFail(null)
        .showImageOnLoading(null)
        .build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
        .build();

		this.imageLoader = imageLoader;
	}
	
	@Override
	public int getCount() {
		return l.size()+1;
	}

	@Override
	public Event getItem(int pos) {
		return l.get(pos-1);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder vh = null;
		
		if(convertView == null){
			
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.new_rs_list_item, null);
			vh.tx = (TextView) convertView.findViewById(R.id.view1);
			vh.back = (ImageView) convertView.findViewById(R.id.imageView1);

			vh.name = (TextView) convertView.findViewById(R.id.textView1);
			vh.date = (TextView) convertView.findViewById(R.id.textView2);
			vh.button = (TextView) convertView.findViewById(R.id.textView3);
			convertView.setTag(vh);
			
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, 300));
		Event ev;
		
		if(position !=0){
			
			ev = getItem(position);
			ALERTLEVEL al = ev.getAlertlevel();
			vh.name.setText(ev.getName());
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.US);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date strDate;
			try {
				strDate = sdf.parse(ev.getDate());
				vh.date.setText(df.format(strDate));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vh.tx.setBackgroundColor(al.getColor());
			vh.tx.setText(ev.getNumber()+"\n"+ev.getUnit().toString().substring(0,1).toUpperCase()+ev.getUnit().toString().substring(1));
			vh.button.setCompoundDrawablesWithIntrinsicBounds(0, getColoredDrawable(al), 0, 0);
			vh.back.setImageResource(ev.getType().getDrwable());

			
		}else{
			Event e = this.relation.getJustBecause();;

			vh.name.setText(e.getName());
			vh.date.setText("Today");
			vh.tx.setBackgroundColor(ALERTLEVEL.now.getColor());
			vh.tx.setText("!\nNow");
			vh.button.setCompoundDrawablesWithIntrinsicBounds(0, getColoredDrawable(ALERTLEVEL.now), 0, 0);
			vh.back.setImageResource(TYPE.other.getDrwable());

		}
		
		
		return convertView;
	}
	
	class ViewHolder{
		
		public ImageView back;
		public TextView date;
		public TextView name;
		TextView tx;
		TextView button;
		
	}

	private int getColoredDrawable(ALERTLEVEL al) {
		int d;
		if(al == ALERTLEVEL.low)
			d = R.drawable.green_jb;
		else if(al == ALERTLEVEL.severe)
			d = R.drawable.yellow_jb;
		else if(al == ALERTLEVEL.critical)
			d = R.drawable.red_jb;
		else
			d = R.drawable.blue_jb;
		return d;	
	}

	public void setRelation(Relationship relation) {
		this.relation=relation;
	}
}
