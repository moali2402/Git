package dev.vision.engizny.adapters;

import java.util.ArrayList;

import dev.vision.engizny.R;
import dev.vision.engizny.R.drawable;
import dev.vision.engizny.R.id;
import dev.vision.engizny.R.layout;
import dev.vision.engizny.fragments.BaseFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Menu_Adapter extends BaseAdapter {

    int selecteIndex = 0;
    private Context con;
    private LayoutInflater ll;
	private ArrayList<BaseFragment> al;

	public Menu_Adapter(Context c, ArrayList<BaseFragment> al){
		con = c;
		ll = LayoutInflater.from(con);

		this.al = al;
	}

	@Override
	public int getCount() {
		return al.size();
	}

	@Override
	public BaseFragment getItem(int pos) {
		return al.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder vh;
		
		if(convertView == null){
			convertView = ll.inflate(R.layout.slidemenu_listitem,parent , false);
		 	vh = new ViewHolder();
		 	vh.im = (ImageView) convertView.findViewById(R.id.imageView1);
		 	vh.txt = (TextView) convertView.findViewById(R.id.textView1);
		 	
		 	convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
			
		BaseFragment bf = getItem(pos);
		vh.im.setImageResource(bf.getIcon());
		vh.txt.setText(bf.getTitle());
		convertView.setBackground(null);
		if(selecteIndex == pos){
			convertView.setBackgroundResource(R.drawable.list_back);
		}
		return convertView;
		
	}
	
	@Override
	public boolean hasStableIds(){
		return true;
		
	}

	public void setSelecteIndex(int pos, View v) {
		
		 v.setBackgroundResource(R.drawable.list_back);
         selecteIndex = pos;
	}
	
	class ViewHolder{
		ImageView im;
		TextView txt;
	}
   
}
