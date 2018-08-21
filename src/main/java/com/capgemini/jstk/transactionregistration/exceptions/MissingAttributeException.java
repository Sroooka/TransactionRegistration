package com.capgemini.jstk.transactionregistration.exceptions;

public class MissingAttributeException extends RuntimeException {
	private static final long serialVersionUID = -2622402994046564356L;

	public MissingAttributeException() {
		super("Missing arguments to create!");
	}
	
	public MissingAttributeException(String message) {
		super("Missing arguments to create! Missing:" + message);
	}
	
	public MissingAttributeException(String message, String className) {
		super("Missing arguments to create " + className + "! Missing:" + message);
	}
}