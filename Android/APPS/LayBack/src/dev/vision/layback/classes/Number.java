package dev.vision.layback.classes;

import java.io.Serializable;

public class Number implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5688338445089209929L;
	String type;
	String number;
	
	public Number(String type, String number) {
		this.type = type;
		this.number = number;
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
	
	@Override
	public String toString(){
		return this.type +": " + this.number;
	}
}
