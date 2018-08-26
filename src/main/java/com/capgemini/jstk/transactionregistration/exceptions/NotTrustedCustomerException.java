package com.capgemini.jstk.transactionregistration.exceptions;

public class NotTrustedCustomerException extends RuntimeException {
	private static final long serialVersionUID = -2622402994046564356L;

	public NotTrustedCustomerException() {
		super("Customer not trusted! Not enough realised transactions to make this transaction!");
	}
	
	public NotTrustedCustomerException(String message) {
		super("Customer not trusted! Not enough realised transactions to make this transaction! " + message);
	}
}