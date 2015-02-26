package com.example.demo;

public class GREEN {

	int D_MUP = 183;
	double MUP;
	double MUP_PERCENT;

	public GREEN(double discount){
		MUP = D_MUP - discount;
		MUP_PERCENT = MUP-100;
	}
}
