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


public class Fragment_Pharmacies extends BaseFragment {
	
	
	private final String TITLE = "PHARMACIES";
	private final int ICON = R.drawable.pharmacy_symbol;
	
	private View v;
	
	static Fragment_Pharmacies smf;


	public Fragment_Pharmacies() {}
	

	public static Fragment_Pharmacies newInstance() {
		if(smf == null )
			smf = new Fragment_Pharmacies();
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
