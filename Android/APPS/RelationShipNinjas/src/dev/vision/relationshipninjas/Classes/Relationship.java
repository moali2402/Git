package dev.vision.relationshipninjas.Classes;

import java.io.Serializable;
import java.util.ArrayList;

import dev.vision.relationshipninjas.purchase.Address;
import android.graphics.Bitmap;
import android.os.Bundle;

public class Relationship implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5767314323633767188L;
	
	private ArrayList<Address> S_ADDRESS = new ArrayList<Address>();

	TYPE type;
	String name;
	String rating;
	String firstname;		
	String middlename;
	String lastname;	
	String id;
	Bitmap profile_pic;
	Bitmap cover_pic;
	//Base64 profile;
	//Base64 cover;
	String email;
	String kids;
	String myersbriggs;
	String gender;
	String facebookid;
	boolean finalizedstatus;
	String cover;
	boolean isdefaultcover;
	String image;
	boolean isdefaultimage;
	int progress;
	Events events = new Events();

	private Address Default_Shipping = new Address();

	private Event JustBe;
	
	public enum TYPE{
		GIRLFRIEND,	FIANCEE, WIFE, FRIENDSWITHBENEFITS, MISTRESS, 
		COWORKER, MOTHER, MOTHERINLAW, SISTER, SISTERINLAW, OTHER;
		
		public static TYPE Match(String s){
			return valueOf(s.toUpperCase());
		}
	}

	/**
	 * @return the type
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TYPE type) {
		this.type = type;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the middlename
	 */
	public String getMiddlename() {
		return middlename;
	}

	/**
	 * @param middlename the middlename to set
	 */
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the facebookid
	 */
	public String getFacebookid() {
		return facebookid;
	}

	/**
	 * @param facebookid the facebookid to set
	 */
	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string the id to set
	 */
	public void setId(String string) {
		this.id = string;
	}

	/**
	 * @return the profile_pic
	 */
	public Bitmap getProfile_pic() {
		return profile_pic;
	}

	/**
	 * @param profile_pic the profile_pic to set
	 */
	public void setProfile_pic(Bitmap profile_pic) {
		this.profile_pic = profile_pic;
	}

	/**
	 * @return the cover_pic
	 */
	public Bitmap getCover_pic() {
		return cover_pic;
	}

	/**
	 * @param cover_pic the cover_pic to set
	 */
	public void setCover_pic(Bitmap cover_pic) {
		this.cover_pic = cover_pic;
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
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the kids
	 */
	public String getKids() {
		return kids;
	}

	/**
	 * @param kids the kids to set
	 */
	public void setKids(String kids) {
		this.kids = kids;
	}

	/**
	 * @return the myersbriggs
	 */
	public String getMyersbriggs() {
		return myersbriggs;
	}

	/**
	 * @param myersbriggs the myersbriggs to set
	 */
	public void setMyersbriggs(String myersbriggs) {
		this.myersbriggs = myersbriggs;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the finalizedstatus
	 */
	public boolean isFinalizedstatus() {
		return finalizedstatus;
	}

	/**
	 * @param finalizedstatus the finalizedstatus to set
	 */
	public void setFinalizedstatus(boolean finalizedstatus) {
		this.finalizedstatus = finalizedstatus;
	}

	/**
	 * @return the cover
	 */
	public String getCover() {
		return cover;
	}

	/**
	 * @param cover the cover to set
	 */
	public void setCover(String cover) {
		this.cover = cover;
	}

	/**
	 * @return the isdefaultcover
	 */
	public boolean isDefaultcover() {
		return isdefaultcover;
	}

	/**
	 * @param isdefaultcover the isdefaultcover to set
	 */
	public void setDefaultcover(boolean isdefaultcover) {
		this.isdefaultcover = isdefaultcover;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the isdefaultimage
	 */
	public boolean isDefaultimage() {
		return isdefaultimage;
	}

	/**
	 * @param isdefaultimage the isdefaultimage to set
	 */
	public void setDefaultimage(boolean isdefaultimage) {
		this.isdefaultimage = isdefaultimage;
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	public void addEvent(Event event) {
		events.add(event);
	}

	public void clearEvents() {
		events.clear();		
	}

	public Events getEvents() {
		return events;
	}
	

	public void addShippingAddress(Address c) {
		S_ADDRESS.add(c);
		if(c.isDefaultshipping() || Default_Shipping==null)
			setDefault_Shipping(c);
	}

	/**
	 * @return the default_Shipping
	 */
	public Address getDShippingAddress() {
		return Default_Shipping;
	}

	/**
	 * @param default_Shipping the default_Shipping to set
	 */
	public void setDefault_Shipping(Address default_Shipping) {
		Default_Shipping = default_Shipping;
	}

	public Event getEvent(String eventId) {
		if(JustBe != null && JustBe.getId().equals(eventId))
			return JustBe;
		for(Event e :events){
			if(e.getId() != null &&  e.getId().equals(eventId))
				return e;
		}
		return null;
		
	}

	public void setJustBecause(Event justbe) {
		this.JustBe = justbe;
	}
	
	public Event getJustBecause() {
		return this.JustBe;
	}


}
