package dev.vision.split.it;

import java.util.Iterator;

import android.graphics.Bitmap;
import android.widget.TableRow;
import dev.vision.split.it.extras.APP;
import dev.vision.split.it.reciept.TableOrder;
import dev.vision.split.it.user.Customer;
import dev.vision.split.it.user.Customers;

public class Table {
	Customers cs = new Customers();
	public static String DEFAULT ="TABLE_NO";
	public static String TABLE ="'s Table";

	String number = DEFAULT;

	TableOrder tor;
	String Virtual_ID;
	onTableChangeListener tableChangeListener;
	private Customer creator; 
	
	//String id : resturant id + generated Virtual_ID , table number
	/*
	public Table(String Virtual_ID, int number) {
		this.Virtual_ID = Virtual_ID;
		this.number = number;
		tor = new TableOrder(this);
	}
	*/
	public Table(Customer creator, String restaurant_id, String vID) {
		setCreator(creator);
		setID(restaurant_id, vID);
		
		tor = new TableOrder(this);
	}

	public Table() {
	}
	
	public void addCustomer(Customer c){
		this.cs.add(c);
	}
	
	public int noOfDinersOnTable(){
		return this.cs.size()+1;
	}
	
	public void removeCustomer(Customer c){
		Iterator<Customer> it = this.cs.iterator();
		while(it.hasNext()){
			Customer temp = it.next();
			if(c.getId().equals(temp.getId())){
				this.cs.remove(temp);
				break;
			}
		}
	}

	/**
	 * @param creatorID the creatorID to set
	 */
	public void setCreator(Customer creator) {
		this.creator = creator;
	}

	public Customer getCreator() {
		return creator;
	}
	
	public Bitmap getCreatorImage() {
		return creator.getImage();
	}

	public String getCreatorName() {
		return creator.getName().split(" ")[0] + Table.TABLE;
	}

	public void createOrder(Customer c,Order o){
		tor.createOrder(c,o);;
	}
	
	public void createOrder(Customer c){
		tor.createOrder(c);
	}
	
	public void removeOrder(Customer c){
		tor.removeOrder(c);
	}
		
	
	public Order getCustomerOrder(Customer c){
		return tor.getCustomerOrder(c);
	}
	
	public float getCustomerOrderTotal(Customer c){
		return tor.getCustomerOrderTotal(c);
	}
	
	public int getCustomerOrderPTotal(Customer c){
		return tor.getCustomerOrderPTotal(c);
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

	/**
	 * @param c the c to set
	 */
	public boolean JoinTable(Customer c){
		return cs.add(c);
	}

	/**
	 * @param c the c to remove
	 */
	public boolean LeaveTable(Customer c) {
		return cs.remove(c);
	}
	
	/**
	 * @return the or
	 */
	public TableOrder getOrder() {
		return tor;
	}

	/**
	 * @param or the or to set
	 */
	public void setOrder(TableOrder or) {
		this.tor = or;
	}

	/**
	 * @param Virtual_ID the Virtual_ID to set
	 */
	public void setID(String restaurant_id, String Virtual_ID) {
		this.Virtual_ID = restaurant_id + "_" + Virtual_ID;
	}
	
	public String getID() {
		return Virtual_ID;
	}
	
	public void setOnTableChangeListener(onTableChangeListener oct) {
		tableChangeListener=oct;
	}
	
	public String getRecieptId() {
		return creator.getId()+Virtual_ID+number;
	}
	
	public interface onTableChangeListener{
		void onTableChange();
	}

	public String getListOfInvited() {
		String names = "";
		Iterator<Customer> it = this.cs.iterator();
		while(it.hasNext()){
			Customer temp = it.next();
			if(!temp.getId().equals(creator.getId())){
				names += temp.getName() + getSeperator(it);
			}
		}
		return names;
	}
	
	String getSeperator(Iterator<?> it){
		if(it.hasNext()) 
			return ", ";
		else return ".";
	}
	
	public float getTableOrderTotal(){
		return tor.getTableOrderTotal();
	}
	
	public int getTableOrderPTotal(){
		return tor.getTableOrderPTotal();
	}
}
