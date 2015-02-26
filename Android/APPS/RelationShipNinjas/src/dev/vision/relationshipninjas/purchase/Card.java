package dev.vision.relationshipninjas.purchase;

import android.text.Html;
import android.text.Spanned;

public class Card {

	String id = "";
	boolean Default;
	String name= "";
	String number= "";
	String expdate= "";
	String type= "";
	String expmonth= "";
	String expyear= "";
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the default
	 */
	public boolean isDefault() {
		return Default;
	}

	/**
	 * @param default1 the default to set
	 */
	public void setDefault(boolean default1) {
		Default = default1;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the expdate
	 */
	public String getExpdate() {
		return expdate;
	}

	/**
	 * @param expdate the expdate to set
	 */
	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the expmonth
	 */
	public String getExpmonth() {
		return expmonth;
	}

	/**
	 * @param expmonth the expmonth to set
	 */
	public void setExpmonth(String expmonth) {
		this.expmonth = expmonth;
	}

	/**
	 * @return the expyear
	 */
	public String getExpyear() {
		return expyear;
	}

	/**
	 * @param expyear the expyear to set
	 */
	public void setExpyear(String expyear) {
		this.expyear = expyear;
	}

	public Card(String id, boolean default1, String name, String number,
			String expdate, String type, String expmonth, String expyear) {
		super();
		this.id = id;
		Default = default1;
		this.name = name;
		this.number = number;
		this.expdate = expdate;
		this.type = type;
		this.expmonth = expmonth;
		this.expyear = expyear;
	}
	
	public Card(){}
	
	public Spanned getSpanned(){
		return Html.fromHtml("<b>" + asUpperCaseFirstChar(type) + " | " +number+ "</b>" +  "<br />" + 
	            "Exp. Date: " + expdate  + "<br />" + 
	            "CardHolder: " + name );
	}
		
	public final static String asUpperCaseFirstChar(final String target) {

	    if ((target == null) || (target.length() == 0)) {
	        return target; // You could omit this check and simply live with an
	                       // exception if you like
	    }
	    return Character.toUpperCase(target.charAt(0))
	            + (target.length() > 1 ? target.substring(1) : "");
	}
}
