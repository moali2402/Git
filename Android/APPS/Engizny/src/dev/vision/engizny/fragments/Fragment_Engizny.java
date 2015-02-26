package dev.vision.engizny.fragments;

import dev.vision.engizny.Engizny;
import dev.vision.engizny.R;
import dev.vision.engizny.adapters.NothingSelectedSpinnerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;


public class Fragment_Engizny extends BaseFragment {
	
	
	private final String TITLE = "ENGIZNY";
	private final int ICON = R.drawable.e;
	
	private ImageView show_hide;
	private View v;
	
	static Fragment_Engizny hf;


	public Fragment_Engizny() {}
	

	public static Fragment_Engizny newInstance() {
		if(hf == null )
			hf = new Fragment_Engizny();
		return hf; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v= inflater.inflate(R.layout.fragment_engizny, null);
		show_hide = (ImageView) v.findViewById(R.id.show_hide_menu);
		v.findViewById(R.id.url).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dev-vision.co.uk"));
		        startActivity(browserIntent);				
			}
		});
		
		return v;
	}
	
	
	
	@SuppressLint("NewApi") 
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		ObjectAnimator anim_out = ObjectAnimator.ofFloat(show_hide, "alpha", 1f, 0.2f);
		anim_out.setDuration(1500);
		anim_out.setRepeatCount(ObjectAnimator.INFINITE);
		anim_out.setRepeatMode(ObjectAnimator.REVERSE);
		anim_out.start();
		show_hide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((Engizny)getActivity()).toggle();
			}
		});
		String[] ob = {"الرحاب", "مدينتى", "التجمع الخامس", "الشروق"};
		Spinner s = (Spinner) getView().findViewById(R.id.spinner1);
		ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_textview, ob );
		
		s.setAdapter(
			      new NothingSelectedSpinnerAdapter(
			    		  myAdapter,
			            R.layout.spinner_textview,
			            getActivity()));
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
