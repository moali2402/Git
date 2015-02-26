package dev.vision.split.it;

import java.util.Arrays;
import java.util.List;

import android.graphics.Color;

public enum Operator {
	VODAFONE("#ff0000",R.drawable.vodafone, "010") //"0106", "0109", "0101"
	,MOBINIL("#ee8713",R.drawable.mobinils, "012")//, "0127", "0128", "0120")	//	in 7 days or less
	,ETISALAT("#86c519",R.drawable.etisalat,"011")//, "0114", "0112")
	,UNKNOWN("#90000000",0,"");	//in more than 17 days
	
	private int COLOR;
	private int LOGO;
	private List<String> CODES;
	 
	 private Operator(String c, int drawable ,String...codes ) {
		 this.COLOR =  Color.parseColor(c);
		 this.LOGO = drawable;
		 this.CODES = Arrays.asList(codes);
	 }
	 
	 public static Operator value(String id){
		 Operator o = null;
		 if(VODAFONE.CODES.contains(id))
			 o = VODAFONE;
		 else if(MOBINIL.CODES.contains(id))
			 o = MOBINIL;
		 else if(ETISALAT.CODES.contains(id))
			 o = ETISALAT;
		 else o = UNKNOWN;
		 return o;
	 }
	 
	 public int getColor() {
	   return this.COLOR;
	 }
	 
	 public int getLogo() {
	   return this.LOGO;
	 }
}
