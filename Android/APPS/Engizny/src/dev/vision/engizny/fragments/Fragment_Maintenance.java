package dev.vision.engizny.fragments;

import dev.vision.engizny.Engizny;
import dev.vision.engizny.R;
import dev.vision.engizny.R.drawable;
import dev.vision.engizny.R.id;
import dev.vision.engizny.R.layout;
import dev.vision.engizny.adapters.NothingSelectedSpinnerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;


public class Fragment_Maintenance extends BaseFragment {
	
	
	private final String TITLE = "MAINTENANCE";
	private final int ICON = R.drawable.tools;
	
	//private View v;
	
	static Fragment_Maintenance smf;


	public Fragment_Maintenance() {}
	

	public static Fragment_Maintenance newInstance() {
		if(smf == null )
			smf = new Fragment_Maintenance();
		return smf; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v= super.onCreateView(inflater, container, savedInstanceState);
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
