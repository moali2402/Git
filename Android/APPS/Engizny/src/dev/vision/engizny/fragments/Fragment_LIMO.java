package dev.vision.engizny.fragments;

import dev.vision.engizny.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment_LIMO extends BaseFragment {
	
	
	private final String TITLE = "LIMO";
	private final int ICON = R.drawable.taxi;
	
	private View v;
	
	static Fragment_LIMO smf;


	public Fragment_LIMO() {}
	

	public static Fragment_LIMO newInstance() {
		if(smf == null )
			smf = new Fragment_LIMO();
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
