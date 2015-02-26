package dev.vision.relationshipninjas.Classes;

import java.io.Serializable;

import org.json.JSONObject;

import android.graphics.Bitmap;

public class Item implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7245755702264637433L;
	String  id;
	String	name;
	String	price;
	String	image;
	String	thumbnail;
	String	description;
	String	detaileddescription;
	String	rating;
	String	chic;
	String	shock; 
	String	charm;
	String	category;
	
	public Item(JSONObject jo){
		setId(jo.optString("id"));
		setName(jo.optString("name"));
		setPrice(jo.optString("price"));
		setImage("http://therelationshipninjas.com"+jo.optString("image"));
		setThumbnail("http://therelationshipninjas.com"+jo.optString("thumbnail"));
		setDescription(jo.optString("description"));
		setDetaileddescription(jo.optString("detaileddescription"));
		setRating(jo.optString("rating"));
		setChic(jo.optString("chic"));
		setShock(jo.optString("shock"));
		setCharm(jo.optString("charm"));
		setCategory(jo.optString("category"));
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
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
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
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the detaileddescription
	 */
	public String getDetaileddescription() {
		return detaileddescription;
	}

	/**
	 * @param detaileddescription the detaileddescription to set
	 */
	public void setDetaileddescription(String detaileddescription) {
		this.detaileddescription = detaileddescription;
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
	 * @return the chic
	 */
	public String getChic() {
		return chic;
	}

	/**
	 * @param chic the chic to set
	 */
	public void setChic(String chic) {
		this.chic = chic;
	}

	/**
	 * @return the shock
	 */
	public String getShock() {
		return shock;
	}

	/**
	 * @param shock the shock to set
	 */
	public void setShock(String shock) {
		this.shock = shock;
	}

	/**
	 * @return the charm
	 */
	public String getCharm() {
		return charm;
	}

	/**
	 * @param charm the charm to set
	 */
	public void setCharm(String charm) {
		this.charm = charm;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

}
