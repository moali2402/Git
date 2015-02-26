package dev.vision.rave;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GiftsAdapter extends BaseAdapter {
	ArrayList<String> l = new ArrayList<String>();
	Context context;
	public GiftsAdapter(Context context, ArrayList<String> s){
		super();
		this.context=context;
		l.addAll(s);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;//l.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View arg1, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.list_row, null);
//		v.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, 700));//.height=200;

	//	((view)v.findViewById(R.id.view1)).setPos(position);

		return null;
	}

}
