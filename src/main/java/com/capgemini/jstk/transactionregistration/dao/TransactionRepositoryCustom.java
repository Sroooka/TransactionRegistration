package com.capgemini.jstk.transactionregistration.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.ProductEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.querydsl.core.Tuple;

public interface TransactionRepositoryCustom {
	List<TransactionEntity> findByProductsAmount(int amount);
	List<TransactionEntity> findByCustomerId(Long customerId);
	double sumOfCustomerTransactions(Long customerId);
	double sumOfCustomerTransactionsWithTransactionStatus(Long customerId, TransactionStatus status);
	public double sumOfAllTransactionsWithTransactionStatus(TransactionStatus status);
	public List<ProductEntity> findBestSellingProducts(int amount);
	public List<CustomerEntity> findCustomersWhoSpentMostMoneyInSpecifiedTime(int amount, Date from, Date to);
	public double profitFromPeriodTime(Date from, Date to);
	public Map<String, Long> findProductsPreparedForDelivery();
}
