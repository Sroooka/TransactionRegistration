package com.capgemini.jstk.transactionregistration.exceptions;

public class NoSuchProductInDatabaseException extends RuntimeException {
	private static final long serialVersionUID = -2622402994046564356L;

	public NoSuchProductInDatabaseException() {
		super("Product not found in database!");
	}
	
	public NoSuchProductInDatabaseException(String message) {
		super("Customer not found in database! " + message);
	}
}