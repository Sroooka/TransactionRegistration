package com.capgemini.jstk.transactionregistration.exceptions;

public class NoSuchCustomerInDatabaseException extends RuntimeException {
	private static final long serialVersionUID = -2622402994046564356L;

	public NoSuchCustomerInDatabaseException() {
		super("Customer not found in database!");
	}
	
	public NoSuchCustomerInDatabaseException(String message) {
		super("Missing arguments to create! " + message);
	}
}