package com.capgemini.jstk.transactionregistration.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.dao.TransactionRepositoryCustom;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QCustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QProductEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QTransactionEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {
	
	@PersistenceContext
    protected EntityManager entityManager;
	
	private JPAQueryFactory queryFactory;
	private QCustomerEntity qCustomer;
	private QProductEntity qProduct;
	private QTransactionEntity qTransaction;
	
	@PostConstruct
	private void init(){
		queryFactory = new JPAQueryFactory(entityManager);
		qCustomer = QCustomerEntity.customerEntity;
		qProduct = QProductEntity.productEntity;
		qTransaction = QTransactionEntity.transactionEntity;
	}
	 
	@Override
	public List<TransactionEntity> findByProductsAmount(int amount){
		return queryFactory.selectFrom(qTransaction)
				.where(qTransaction.products.size().loe(amount))
				.fetch();
	}
	
	@Override
	public List<TransactionEntity> findByCustomerId(Long customerId){
		return queryFactory.selectFrom(qTransaction)
				.where(qTransaction.customer.id.loe(customerId))
				.fetch();
	}
	
	@Override
	public double sumOfCustomerTransactions(Long customerId){
		 return queryFactory.from(qTransaction)
				.where(qTransaction.customer.id.loe(customerId))
				.join(qTransaction.products, qProduct)
				.select(qProduct.unitPrice.sum())
				.fetchOne();
	}

	@Override
	public double sumOfCustomerTransactionsWithTransactionStatus(Long customerId, TransactionStatus status){
		return queryFactory.from(qTransaction)
				.where(qTransaction.customer.id.loe(customerId), qTransaction.status.eq(status))
				.join(qTransaction.products, qProduct)
				.select(qProduct.unitPrice.sum())
				.fetchOne();
	}
	
	@Override
	public double sumOfAllTransactionsWithTransactionStatus(TransactionStatus status){
		return queryFactory.from(qTransaction)
				.where(qTransaction.status.eq(status))
				.join(qTransaction.products, qProduct)
				.select(qProduct.unitPrice.sum())
				.fetchOne();
	}
}
