package com.capgemini.jstk.transactionregistration.exceptions;

public class TooHighProductWeightException extends RuntimeException {
	private static final long serialVersionUID = -2622402994046564356L;

	public TooHighProductWeightException() {
		super("The weight of simple product exceeds 25kg!");
	}
	
	public TooHighProductWeightException(String message) {
		super("The weight of simple product exceeds 25kg! " + message);
	}
}