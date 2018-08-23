package com.capgemini.jstk.transactionregistration.service;

import com.capgemini.jstk.transactionregistration.types.TransactionTO;

public interface TransactionService {
	public TransactionTO saveTransaction(TransactionTO transaction);
	public TransactionTO updateTransaction(TransactionTO transaction);
	public TransactionTO deleteTransaction(TransactionTO transaction);
	public boolean contains(Long id);
	public int size();
	public TransactionTO findTransactionById(Long id);
	public void setCustomerInTransaction(Long transactionId, Long customerId);
}
