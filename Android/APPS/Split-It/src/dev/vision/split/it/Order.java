package dev.vision.split.it;

import dev.vision.split.it.menu.Items;

public class Order {
	Items items;
	String orderNo;
	public Order(String iD) {
		this.orderNo = iD;
		this.items = new Items();
		items.setTotal(249.00f);
	}
	
	public float getTotal(){
		return items.getTotal();
	}
	
	public int getPTotal(){
		return (int) (getTotal() *100);
	}

	public String getOrderId(){
		return this.orderNo;
	}
}
