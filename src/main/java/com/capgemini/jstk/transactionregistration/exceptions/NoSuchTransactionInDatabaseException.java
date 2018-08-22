package com.capgemini.jstk.transactionregistration.exceptions;

public class NoSuchTransactionInDatabaseException extends RuntimeException {
	private static final long serialVersionUID = -2622402994046564356L;

	public NoSuchTransactionInDatabaseException() {
		super("Transaction not found in database!");
	}
	
	public NoSuchTransactionInDatabaseException(String message) {
		super("Transaction not found in database! " + message);
	}
}