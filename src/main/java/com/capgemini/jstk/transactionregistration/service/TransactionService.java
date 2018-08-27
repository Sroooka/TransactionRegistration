package com.capgemini.jstk.transactionregistration.service;

import java.util.Collection;
import java.util.List;

import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.capgemini.jstk.transactionregistration.types.ProductTO;
import com.capgemini.jstk.transactionregistration.types.TransactionTO;

public interface TransactionService {
	public List<TransactionTO> saveTransaction(TransactionTO transaction);
	public TransactionTO updateTransaction(TransactionTO transaction);
	public TransactionTO deleteTransaction(TransactionTO transaction);
	public boolean contains(Long id);
	public int size();
	public TransactionTO findTransactionById(Long id);
	public void setCustomerInTransaction(Long transactionId, Long customerId);
	public List<TransactionTO> findByProductsAmount(int amount);
	public double sumOfCustomerTransactions(Long customerId);
	public double sumOfCustomerTransactionsWithTransactionStatus(Long customerId, TransactionStatus status);
	public double sumOfTransactionsWithTransactionStatus(TransactionStatus status);
	public List<ProductTO> findBestSellingProducts(int amount);
}
