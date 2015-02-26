package com.example.demo;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String Username = "";
	String password = "";
	boolean enabled = false;
	String storerep = "";
	Store store;


	public User(String username, String password, boolean enabled,
			String storeid,	String storename, String email) {
		this.Username = username;
		this.storerep = username;

		this.password = password;
		this.enabled = enabled;
		
		store= new Store(storeid, storename, email);
	}
	

	/**
	 * @return the username
	 */
	public String getUsername() {
		return Username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		Username = username;
		this.storerep = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
}
