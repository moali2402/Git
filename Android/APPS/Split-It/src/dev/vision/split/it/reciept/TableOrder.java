package dev.vision.split.it.reciept;

import java.util.HashMap;

import dev.vision.split.it.Order;
import dev.vision.split.it.Table;
import dev.vision.split.it.user.Customer;

public class TableOrder extends HashMap<String, Order>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1615995045389535856L;
	
	Table t;
	String receipt_id;
	public TableOrder(Table t) {
		super();
		this.t = t;
		receipt_id = t.getRecieptId();
	}
	
	public void createOrder(Customer c,Order o){
		put(c.getId(), o);
	}
	
	public void createOrder(Customer c){
		Order or = new Order(receipt_id+"_"+c.getId());
		put(c.getId(), or);
	}
	
	public void removeOrder(Customer c){
		remove(c.getId());
	}
		
	
	public Order getCustomerOrder(Customer c){
		return get(c.getId());
	}
	
	public float getCustomerOrderTotal(Customer c){
		return getCustomerOrder(c).getTotal();
	}
	
	public int getCustomerOrderPTotal(Customer c){
		return getCustomerOrder(c).getPTotal();
	}
	/*
	public Order getCustomerOrder(String customer_id){
		return get(receipt_id+"_"+customer_id);
	}*/
	
	public String getRecieptId(){
		return receipt_id.replace(Table.DEFAULT, t.getNumber());
	}
	
	public float getTableOrderTotal(){
		float total=0.0f;
		for(Order or : values()){
			total += or.getTotal();
		}
		return total;
	}
	
	public int getTableOrderPTotal(){
		
		return (int) (getTableOrderTotal()*100);
	}
}
