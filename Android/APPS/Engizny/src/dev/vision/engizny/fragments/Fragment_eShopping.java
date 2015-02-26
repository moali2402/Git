package dev.vision.engizny.fragments;

import dev.vision.engizny.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment_eShopping extends BaseFragment {
	
	
	private final String TITLE = "eSHOP";
	private final int ICON = R.drawable.trolley;
	
	private View v;
	
	static Fragment_eShopping smf;


	public Fragment_eShopping() {}
	

	public static Fragment_eShopping newInstance() {
		if(smf == null )
			smf = new Fragment_eShopping();
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
