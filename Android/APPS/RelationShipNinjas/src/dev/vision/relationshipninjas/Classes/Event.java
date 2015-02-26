package dev.vision.relationshipninjas.Classes;

import dev.vision.relationshipninjas.R;
import android.graphics.Color;

public class Event {
	
	String id;
	String date;
	boolean passed;
	String name;
	TYPE type;
	String rating;
	String originaldate;
	String relationshipid;
	STATUS status;
		
	//class remaing {
	int number;
	DATEUNIT unit;
	ALERTLEVEL alertlevel;
	//}
	
	
	public enum ALERTLEVEL{
		now("#00c6ff")
		,critical("#ff6561")	//	in 7 days or less
		,severe("#ffcc00")	//between 8 days and 17 days
		,low("#36c272");	//in more than 17 days
		
		private String code;
		 
		 private ALERTLEVEL(String c) {
		   code = c;
		 }
		 
		 public int getColor() {
		   return Color.parseColor(code);
		 }
		 
		 @Override
		 public String toString() {
		   return code;
		 }
	}
	
	public enum TYPE{
		
		valentineday(R.drawable.valentine), motherday(R.drawable.mothersday),
		womenday(R.drawable.mothersday), superbowl(0),
		christmas(R.drawable.xmas), hanukkah(0),
		mardigras(0), stpatrick(0),
		halloween(R.drawable.halloween), thanksgiving(0),
		newyear(R.drawable.newyear), easter(0),
		chinesenewyear(R.drawable.chinese), birthday(R.drawable.birthday),
		firstdate(R.drawable.firstday), engagement(R.drawable.engagement),
		wedding(R.drawable.wedding), other(R.drawable.just);
		
		private int drawable;
		 
		private TYPE(int d) {
		  drawable = d;
		}
		
		public int getDrwable() {
		   return drawable;
		}
	}

	public enum DATEUNIT{
		day	,month,	year
	}
	
	
	public enum STATUS{
		open, ordered, review, completed, skipped;	
	}
		
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isPassed() {
		return passed;
	}
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TYPE getType() {
		return type;
	}
	public void setType(TYPE type) {
		this.type = type;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getOriginaldate() {
		return originaldate;
	}
	public void setOriginaldate(String originaldate) {
		this.originaldate = originaldate;
	}
	public String getRelationshipid() {
		return relationshipid;
	}
	public void setRelationshipid(String relationshipid) {
		this.relationshipid = relationshipid;
	}
	public STATUS getStatus() {
		return status;
	}
	public void setStatus(STATUS status) {
		this.status = status;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public DATEUNIT getUnit() {
		return unit;
	}
	public void setUnit(DATEUNIT unit) {
		this.unit = unit;
	}
	public ALERTLEVEL getAlertlevel() {
		return alertlevel;
	}
	public void setAlertlevel(ALERTLEVEL alertlevel) {
		this.alertlevel = alertlevel;
	}

}
