package com.capgemini.jstk.transactionregistration.exceptions;

public class TooMuchExpensiveProductsException extends RuntimeException {
	private static final long serialVersionUID = -2622402994046564356L;

	public TooMuchExpensiveProductsException() {
		super("Too much products with price above specified amount!");
	}
	
	public TooMuchExpensiveProductsException(int specifiedPrice) {
		super("Too much products with price above specified amount! [" + specifiedPrice + "]");
	}
	
	public TooMuchExpensiveProductsException(double specifiedPrice) {
		super("Too much products with price above specified amount! [" + specifiedPrice + "]");
	}
}