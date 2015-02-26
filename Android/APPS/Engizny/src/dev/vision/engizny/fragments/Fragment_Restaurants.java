package dev.vision.engizny.fragments;


import dev.vision.engizny.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment_Restaurants extends BaseFragment {
	
	
	private final String TITLE = "RESTAURANTS";
	private final int ICON = R.drawable.restaurant;
	
	private View v;
	
	static Fragment_Restaurants smf;


	public Fragment_Restaurants() {}
	

	public static Fragment_Restaurants newInstance() {
		if(smf == null )
			smf = new Fragment_Restaurants();
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
