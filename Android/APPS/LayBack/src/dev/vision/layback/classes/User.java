package dev.vision.layback.classes;
public class User{
	String fname;
	String sname;

	Number number;

	/**
	 * @return the name
	 */
	public String getName() {
		return fname;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.fname = name;
	}

	/**
	 * @return the number
	 */
	public String getDefaultNumber() {
		return number != null ?  number.number : null ;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Number number) {
		this.number = number;
	}
	
	public User(String name) {
		this.fname = name;
	}

	public boolean isEmpty() {
		return number == null;
	}
	
}