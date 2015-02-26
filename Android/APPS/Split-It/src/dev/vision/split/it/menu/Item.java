package dev.vision.split.it.menu;

import java.io.Serializable;

/**
 * @author Mo
 *
 */
public class Item implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private float price = 0.0f;
	private float discount = 0.0f;
	private boolean discounted;
	private float rating = 0;

	
	public Item() {}

	public Item(String id, String name, float price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
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
	 * @return the price
	 */
	public float getPrice() {
		return price-discount;
	}
	
	public float getPreDiscount() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @param i
	 */
	public void applyDiscount(float i){
		discount = i;
		discounted=true;
	}

	/**
	 * 
	 */
	public void removeDiscount(){
		discount = 0.0f;
		discounted=false;
	}
	
	/**
	 * @return
	 */
	public boolean isDiscounted(){
		return discounted;
	}
	
	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

	/**
	 * @return
	 * @see java.lang.String#toString()
	 */
	public String toString() {
		return name + " : " + price;
	}


}
