package dev.vision.voom.adapters;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import dev.vision.voom.R;
import dev.vision.voom.classes.Contact;
import dev.vision.voom.parse.Profiles;
import dev.vision.voom.views.TextDrawable;
import dev.vision.voom.views.util.ColorGenerator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	ArrayList<Profiles> profiles;
	Context cx;
	LayoutInflater lf;
	TextDrawable.IBuilder builder;
	ColorGenerator generator;
	
	public ContactAdapter(Context cx, ArrayList<Profiles> profiles) {
		this.cx = cx;
		this.profiles = profiles;
		lf = LayoutInflater.from(cx);
		builder = TextDrawable.builder()
                .beginConfig()
                 .bold()
                 .textColor(Color.WHITE)
                 .toUpperCase()
                .endConfig()
                .rect();	
		generator = ColorGenerator.MATERIAL; // or use DEFAULT

	}

	@Override
	public int getCount() {
		return profiles.size();
	}

	@Override
	public Profiles getItem(int position) {
		return profiles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = lf.inflate(R.layout.contact_item, parent, false);

		}
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView status = (TextView) convertView.findViewById(R.id.status);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.hexContactStatusIcon1);
		
		
		Profiles p = getItem(position);
		
		name.setText(p.getDisplayName());
		status.setText(p.getStatus());	
		
        if (imageView != null) {
            if(p.getProfilePic() == null || p.getProfilePic().trim().isEmpty()) {
        		String first = p.getDisplayName().substring(0, 1);
        		int color = generator.getColor(first);
                imageView.setImageDrawable(builder.build(first, color));
            } else {
                Picasso.with(cx).load(p.getProfilePic()).into(imageView);
            }
        }
		return convertView;
	}

}
