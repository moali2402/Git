package com.example.demo;

public class Calculator {

	double daily_princible; // amount/total_days
	double daily_overheads; // daily_princible/100 * overheads
	double daily_payment_inc_overheads;
	double weekly_payment_inc_overheads;
	double weekly_markup;
	double payment_plus_markup;
	double gst;
	double payment;
	double total_payable;
	double profit;
	double total_cogs_inc_overheads;
	double profit_term;
	double overheads;
	
	private float price;
	private WeekDays wk;



	public Calculator(WeekDays wk, float price) {
		this.price = price;
		this.wk = wk;
		calculate();
	}



	private void calculate() {
		getDailyPrincible();
		getDailyOverheads();
		getDailyPaymentIncOverheads();
		getWeeklyPaymentIncOverheads();
		getWeeklyMarkup();
		getPaymentPlusMarkup();
		getGST();
		getPayment();
		getTotalPayable();
		getProfit();
		getTotalCOGS();
		getOverheads();
	}
	
	private void getDailyPrincible(){
		daily_princible = price / wk.days;
	}
	
	private void getDailyOverheads(){
		daily_overheads = ((price/182.5)/100) * wk.OverHeads; //replaces daily_princible/100
	}
	
	private void getDailyPaymentIncOverheads(){
		daily_payment_inc_overheads = daily_princible + daily_overheads;
	}

	private void getWeeklyPaymentIncOverheads() {
		weekly_payment_inc_overheads = daily_payment_inc_overheads * 7;
	}
	
	private void getWeeklyMarkup() {
		weekly_markup = (weekly_payment_inc_overheads/100) * wk.i.g.MUP_PERCENT;
	}
	
	private void getPaymentPlusMarkup() {
		payment_plus_markup = weekly_payment_inc_overheads + weekly_markup;
	}
	
	private void getGST() {
		gst = (payment_plus_markup*10)/100;
	}
	
	private void getPayment() {
		payment = payment_plus_markup + gst;
	}
	
	private void getTotalPayable() {
		total_payable = payment * wk.weeks;
	}
	
	private void getProfit() {
		profit = weekly_markup * wk.weeks;
	}
	
	private void getTotalCOGS() {
		total_cogs_inc_overheads = weekly_payment_inc_overheads * wk.weeks;
	}
	
	private void getOverheads() {
		overheads = daily_overheads * wk.days;
	}
}
