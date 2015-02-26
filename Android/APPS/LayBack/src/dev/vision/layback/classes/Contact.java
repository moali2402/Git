package dev.vision.layback.classes;

import java.util.ArrayList;

public class Contact extends User {
	
	ArrayList<Number> numbers = new ArrayList<Number>();
	
	public Contact(String name) {
		super(name);
	}

	/**
	 */
	public void addNumber(Number num) {
		if(!numbers.contains(num))
			numbers.add(num);
		if(number == null)
			setNumber(num);
	}
	
	/**
	 */
	public void setDefaultNumber(Number num) {
		addNumber(num);
		setNumber(num);
	}
	
	/**
	 * @return the numbers
	 */
	public ArrayList<Number> getNumbers() {
		return numbers;
	}

	/**
	 * @param numbers the numbers to set
	 */
	public void setNumbers(ArrayList<Number> numbers) {
		this.numbers.clear();
		this.numbers.addAll(numbers);
	}
	
	@Override
	public boolean isEmpty() {
		return numbers.isEmpty();
	}

	

	public String getNumberAsString(String number) {

		Number[] ns = new Number[numbers.size()];
		numbers.toArray(ns);
		for(Number n : ns){
			if(n.number != null && n.number.equals(number))
				return n.toString();
		}
		return "";
	}
	
}