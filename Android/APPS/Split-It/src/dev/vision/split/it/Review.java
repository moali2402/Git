package dev.vision.split.it;

import android.graphics.Bitmap;
import dev.vision.split.it.user.Customer;

public class Review {

	Customer c;
	String time;
	int rating;
	/**
	 * @return the c
	 */
	public Customer getCustomer() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setCustomer(Customer c) {
		this.c = c;
	}
	

	public Bitmap getCustomerImage() {
		return c.getImage();
	}

	public String getCustomerName() {
		return c.getName();
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	String feedback;
	
	public Review() {}

	public Review(Customer c, String time, int rating, String feedback) {
		super();
		this.c = c;
		this.time = time;
		this.rating = rating;
		this.feedback = feedback;
	}

	
}
