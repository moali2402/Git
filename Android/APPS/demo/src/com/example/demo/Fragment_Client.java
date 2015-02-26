package com.example.demo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Fragment_Client extends Fragment {
	View v;
	
    public Fragment_Client(){};

    public static final Fragment_Client newInstance()
    {
    	Fragment_Client f = new Fragment_Client();
   	 	
        return f;
    }
    
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		v= inflater.inflate(R.layout.client_details, null);
		v.findViewById(R.id.client_details_dob).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDatePickerDialog(v);				
			}
		});
		return v;
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment(v);
	    newFragment.show(getChildFragmentManager(), "datePicker");
	}
	

	public void setClientDetailsFields() {
		((Fragment_Activity)getActivity()).quote.cd.setClientDetailsFields(v);
	}

	public void getFields() {
		((Fragment_Activity)getActivity()).quote.cd.getFields(v);
	}
}
