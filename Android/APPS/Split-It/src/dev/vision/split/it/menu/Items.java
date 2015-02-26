/**
 * 
 */
package dev.vision.split.it.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mo
 *
 */
public class Items extends ArrayList<Item> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7155344747426748073L;
	
	private boolean calculated;
	private float total = 0.0f;
	private float discount = 0.0f;
	/*
	@Override
	public boolean add(Item i){
		boolean add = super.add(i);
		if(add){
			total+=i.getPrice();
		}
		return add;
	}
	
	private boolean confirmTotal(){
		return calculateTotal() == total;
	}
	
	*/
	@Override
	public boolean add(Item i){
		boolean add = super.add(i);
		if(add){
			calculated = false;
		}
		return add;
	}


	public void applyPercentageDiscount(float value){
		if(!isCalcualted())
			calculateTotal();	
		applyDiscount(total * value / 100);
	}
	
	public void applyDiscount(float value){
		discount = value;
	}
	
	public float getDiscount(){
		return discount;
	}
	
	private void calculateTotal(){
		try{
			total = 0.0f;
			Iterator<Item> it = iterator();
			while(it.hasNext()){
				Item i = it.next();
				total+=i.getPrice();
			}
			calculated = true;
		}catch (Exception e){
			calculated = false;
		}
	}
	
	private boolean isCalcualted(){
		return calculated;
	}
	
	private float getDiscountedTotal() {
		return total-discount;
	}
	
	public float getPreDiscountTotal() {
		if(!isCalcualted())
			calculateTotal();
		return total;
	}
	
	public float getTotal(){
		if(!isCalcualted())
			calculateTotal();
		return getDiscountedTotal();
	}

/// Test Methods Can be Deleted
	public void setTotal(float i) {
		calculated = true;
		total=i;		
	}
}
