package com.capgemini.jstk.transactionregistration.dao;

import java.util.List;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;

public interface TransactionRepositoryCustom {
	List<TransactionEntity> findByProductsAmount(int amount);
	List<TransactionEntity> findByCustomerId(Long customerId);
	double sumOfCustomerTransactions(Long customerId);
	double sumOfCustomerTransactionsWithTransactionStatus(Long customerId, TransactionStatus status);
	public double sumOfAllTransactionsWithTransactionStatus(TransactionStatus status);
}
