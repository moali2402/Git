package com.example.demo;

import java.io.Serializable;

public class Store implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	String Storeid = "";
	String Storename = "";
	String email = "";

	public Store(String storeid,
			String storename, String email) {
		this.Storeid = storeid;
		this.Storename = storename;
		this.email = email;
	}
	


}
