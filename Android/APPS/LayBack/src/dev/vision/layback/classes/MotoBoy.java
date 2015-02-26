package dev.vision.layback.classes;

import java.util.ArrayList;

public class MotoBoy {
	
	private String id;
	private String name;
	private Contact number;
	private Delivery currentDel;
	private ArrayList<Delivery> deliveries;
	
	/**
	 * @return the number
	 */
	public Contact getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Contact number) {
		this.number = number;
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
	 * @return the currentDel
	 */
	public Delivery getCurrentDel() {
		return currentDel;
	}

	/**
	 * @param currentDel the currentDel to set
	 */
	public void setCurrentDel(Delivery currentDel) {
		this.currentDel = currentDel;
	}

	/**
	 * @return the deliveries
	 */
	public ArrayList<Delivery> getDeliveries() {
		return deliveries;
	}
	
	/**
	 * @param d the delivery to add
	 */
	public boolean addDelivery(Delivery d) {
		if(deliveries.size() < 5)
			return deliveries.add(d);
		return false;
	}

	/**
	 * @param deliveries the deliveries to set
	 */
	public void setDeliveries(ArrayList<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public MotoBoy(){}
		
}