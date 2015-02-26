package com.example.demo;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Row {
	

	String item = "";
	
	String six_mth= "";
	String one_yr= "";
	String eighten_mth= "";
	String two_yr= "";
	String three_yr= "";
	
	TextView six_mth2;
	TextView one_yr2;
	TextView eighten_mth2;
	TextView two_yr2;
	TextView three_yr2;

	Item i = new Item();
	
	public Row() {
		calculate();
	}
	
	
	public void setPrice(float p){
		i.setPrice(p);
	}
	
	public void setDisc(int d){
		i.setDiscount(d);
	}
	
	
	
	public void calculate(){
		six_mth = String.format("%.2f", i.calculateFirst());
		one_yr = String.format("%.2f", i.calculateSecond());
		eighten_mth = String.format("%.2f", i.calculateThird());
		two_yr = String.format("%.2f", i.calculateFourth());
		three_yr = String.format("%.2f", i.calculateFifth());
	}


	public void setEditTexts(TextView six_mth2, TextView one_yr2, TextView eighten_mth2,
			TextView two_yr2, TextView three_yr2) {
		
		this.six_mth2 = six_mth2;
		this.one_yr2= one_yr2;
		this.eighten_mth2 = eighten_mth2;
		this.two_yr2 = two_yr2;
		this.three_yr2= three_yr2;
				
	}
	
	
	

}
