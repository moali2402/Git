package dev.vision.engizny.fragments;

import dev.vision.engizny.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class Fragment_SuperMarket extends BaseFragment {
	
	
	private final String TITLE = "SUPERMARKET";
	private final int ICON = R.drawable.shop;
	
	//private View v;
	
	static Fragment_SuperMarket smf;


	public Fragment_SuperMarket() {}
	

	public static Fragment_SuperMarket newInstance() {
		if(smf == null )
			smf = new Fragment_SuperMarket();
		return smf; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = super.onCreateView(inflater, container, savedInstanceState);
		return v;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		list.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.textView1,
				new String[]{"Store Name","Store Name","Store Name","Store Name","Store Name","Store Name",
							 "Store Name","Store Name","Store Name","Store Name","Store Name","Store Name",
							 "Store Name","Store Name","Store Name","Store Name","Store Name","Store Name"}));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public int getIcon() {
		return ICON;
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	
	
}
