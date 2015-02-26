package dev.vision.relationshipninjas.purchase;

import java.io.Serializable;

import android.text.Html;
import android.text.Spanned;

public class Address implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2427929319644833997L;
	String id ="";
	String name = "";
	String addressee = "";
	String address1= "";
	String address2= "";
	String city= "";
	boolean defaultbilling;
	boolean defaultshipping;	
	String phone= "";
	String zip= "";
	String state= "";
	String country= "";

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
	 * @return the addressee
	 */
	public String getAddressee() {
		return addressee;
	}

	/**
	 * @param addressee the addressee to set
	 */
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the defaultbilling
	 */
	public boolean isDefaultbilling() {
		return defaultbilling;
	}

	/**
	 * @param defaultbilling the defaultbilling to set
	 */
	public void setDefaultbilling(boolean defaultbilling) {
		this.defaultbilling = defaultbilling;
	}

	/**
	 * @return the defaultshipping
	 */
	public boolean isDefaultshipping() {
		return defaultshipping;
	}

	/**
	 * @param defaultshipping the defaultshipping to set
	 */
	public void setDefaultshipping(boolean defaultshipping) {
		this.defaultshipping = defaultshipping;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	public Address(String id, String name, String addressee, String address1,
			String address2, String city, boolean defaultbilling,
			boolean defaultshipping, String phone, String zip, String state,
			String country) {
		super();
		this.id = id;
		this.name = name;
		this.addressee = addressee;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.defaultbilling = defaultbilling;
		this.defaultshipping = defaultshipping;
		this.phone = phone;
		this.zip = zip;
		this.state = state;
		this.country = country;
	}

	public Address() {
	}
	
	
	@Override
	public String toString(){
		return getName()
		+ "\n" + getAddressee()
		+ "\n" + getAddress1()
		+ "\n" + getAddress2()
		+ "\n" + getState()+ " " + getCity() + " "  + getZip() 
		+ "\n" + getCountry()
		+ "\nT: " + getPhone();
	}
	
	public Spanned getSpanned(){
		return Html.fromHtml("<b>" + name + "</b>" +  "<br />"  +
				 getAddressee()+ "<br />" + 
				 getAddress1()+ "<br />" + 
				 getAddress2() + "<br />" + 
				 getState() + " " + getCity() + " "  + getZip() + "<br />" + 
				 getCountry() + "<br />" + 
				 getPhone());
	}
	
}
