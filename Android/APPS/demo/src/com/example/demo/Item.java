package com.example.demo;

public class Item {

	WeekDays first;
	WeekDays second;
	WeekDays third;
	WeekDays fourth;
	WeekDays fifth;
	
	//String name;
	float price = 0;
	int discount = 0;
	double discounted;
	GREEN g = new GREEN(discount);

	

	public Item() {
		first = new WeekDays(this, 26, (float) 182.5 ,10);
		//first.getOverheads();
		second = new WeekDays(this, 52, (float) 365 , 10);
		third = new WeekDays(this, 78, (float) 546, 10);
		fourth = new WeekDays(this, 104, (float) 730, 10);
		fifth = new WeekDays(this, 156, (float) 1095, 10);
	}
	
	
	public double getDiscount() {
		return discount;
	}
	
	public void setDiscount(int discount) {
		this.discount = discount;
		g = new GREEN(discount);
		discounted = g.MUP_PERCENT - discount;
	}
	
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float p){
		this.price = p;
	}
	
	public double calculateFirst(){
		return first.calculate(price);
	}
	
	public double calculateSecond(){
		return second.calculate(price);
	}
	
	public double calculateThird(){
		return third.calculate(price);
	}

	public double calculateFourth(){
		return fourth.calculate(price);
	}

	public double calculateFifth(){
		return fifth.calculate(price);
	}

	
	
}
