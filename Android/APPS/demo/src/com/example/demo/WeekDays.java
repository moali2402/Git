package com.example.demo;

public class WeekDays {

	int weeks;
	float days;
	int OverHeads;
	Item i;
	Calculator c;
	
	public WeekDays(Item i, int weeks, float days, int oh) {
		this.i =i;
		this.weeks = weeks;
		this.days = days;
		OverHeads = oh;
	}

	
	public double calculate(float price){
		c = new Calculator(this, price);
		return getPayment();
	}
	
	public double getPayment(){
		return c.payment;
	}

	
	public double getTotalPayable(){
		return c.total_payable;
	}


	
	
}
