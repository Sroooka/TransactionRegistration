package com.capgemini.jstk.transactionregistration.service;

import com.capgemini.jstk.transactionregistration.types.TransactionTO;

public interface TransactionService {
	public TransactionTO saveTransaction(TransactionTO customer);
	public TransactionTO updateTransaction(TransactionTO customer);
	public TransactionTO deleteTransaction(TransactionTO customer);
	public boolean contains(Long id);
	public int size();
	public TransactionTO findTransactionById(Long id);
}
