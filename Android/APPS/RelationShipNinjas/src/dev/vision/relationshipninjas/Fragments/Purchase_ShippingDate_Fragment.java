package dev.vision.relationshipninjas.Fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.interfaces.Data;
import dev.vision.relationshipninjas.purchase.Purchase;
import dev.vision.relationshipninjas.purchase.Purchase_Activity;
import dev.vision.relationshipninjas.purchase.Util;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Purchase_ShippingDate_Fragment extends Fragment implements Data{
	String my;
	int day;
	String date;
	GregorianCalendar gc;
	View v;
	ViewPager vp;
	private Purchase purchase;
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inf, container, savedInstanceState);
		v =inf.inflate(R.layout.purchase_shippingdate, null);
		vp=(ViewPager) getActivity().findViewById(R.id.pagerr);
		ImageView addDay = (ImageView) v.findViewById(R.id.addday);
		ImageView minusDay = (ImageView) v.findViewById(R.id.minusday);
		TextView conti = (TextView) v.findViewById(R.id.Continue);

		addDay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Calendar max = Calendar.getInstance();
				max.add(Calendar.DATE, 10);
				if(gc.before(max)){
					gc.add(Calendar.DATE, 1);
					update();
				}
			}
		});
		
		minusDay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Calendar min = Calendar.getInstance();
				min.add(Calendar.DATE, 1);
				if(gc.after(min)){
					gc.add(Calendar.DATE, -1);
					update();
				}
			}
		});
		
		conti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				vp.setCurrentItem(vp.getCurrentItem()+1, true);
			}
		});

		return v;	
	}
	
	@SuppressLint("NewApi") 
	private void update() {
		day =gc.get(Calendar.DAY_OF_MONTH);
		date =gc.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
		my = gc.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " +
				gc.get(Calendar.YEAR);
		((TextView)v.findViewById(R.id.monthyear)).setText(my);
		((TextView)v.findViewById(R.id.day)).setText(""+day);
		((TextView)v.findViewById(R.id.date)).setText(date);
		
		purchase.setPreDate(gc.getTime());
		System.out.println(findDeference());

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState); 
		setPurchase();

		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, 1);

		update();
		
		
	}

	@Override
	public void setPurchase() {
		purchase = ((Purchase_Activity)getActivity()).getPurchase();
	}

	@Override
	public void setData() {
		
	}
	
	String findDeference(){
		
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Event ev= purchase.getEvent();
        Date date;
        int days = 0;
		try {
			date = sdf.parse(ev.getDate());

	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
			days = Util.getUnsignedDiffInDays(gc, cal);
		} catch (ParseException e) {
			e.printStackTrace();
			return  "";
		}
		return  days + "day" + (days >0 ?" before " : " after ") + ev.getName();
	}

	
}
