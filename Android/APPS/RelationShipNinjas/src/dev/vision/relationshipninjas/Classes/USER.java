package dev.vision.relationshipninjas.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.purchase.Card;

public class USER implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7992355358315737256L;
	
	private ArrayList<Card> CARDS = new ArrayList<Card>();
	private ArrayList<Address> S_ADDRESS = new ArrayList<Address>();
	private ArrayList<Address> B_ADDRESS = new ArrayList<Address>();
	private Address Default_Billing;
	private Address Default_Shipping;
	private	Card Default_Card;
	
	
	public String id= "";
	public String email = "";
	public String pass = "";
	public String role = "1015";
	public String firstname = "";
	public String middlename = "";
	public String lastname= "";
	public String gender= "";
	public String status= "";
	public String birthday= "";
	public String points= "";
	public int progress;
	public int level;
	public String rank= "";
	public String image= "";
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
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}
	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	/**
	 * @return the points
	 */
	public String getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(String points) {
		this.points = points;
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
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param i the level to set
	 */
	public void setLevel(int i) {
		this.level = i;
	}
	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
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
		this.image = "http://therelationshipninjas.com/"+image;
	}
	
	public void addCard(Card c) {
		CARDS.add(c);
		if(c.isDefault() || Default_Card == null)
			Default_Card = c;
	}
	
	public void addShippingAddress(Address c) {
		S_ADDRESS.add(c);
		if(c.isDefaultshipping() || Default_Shipping == null)
			Default_Shipping = c;
	}

	public void addBillingAddress(Address c) {
		B_ADDRESS.add(c);
		if(c.isDefaultbilling() || Default_Billing == null)
			Default_Billing = c;
	}
	
	public Address getDBillingAddress() {
		return Default_Billing;
	}
	
	public Address getDShippingAddress() {
		return Default_Shipping;
	}
	
	public Card getDCreditCard() {
		return Default_Card;
	}
	public String getName() {
		return getFirstname() +" "+ getLastname();
	}
	
	public HashSet<Address> getAddressesList(){
		HashSet<Address> ad =new HashSet<Address>();
		ad.addAll(B_ADDRESS);
		ad.addAll(S_ADDRESS);
		return ad;
	}
	public ArrayList<Card> getCreditCardList() {
		return CARDS;
	}
}
