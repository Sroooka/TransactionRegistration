package com.capgemini.jstk.transactionregistration.dao.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Subquery;

import org.hibernate.loader.custom.sql.SQLCustomQuery;
import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.dao.TransactionRepositoryCustom;
import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.ProductEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QCustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QProductEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QTransactionEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.querydsl.jpa.impl.JPAQuery;
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
	
	@Override
	public List<ProductEntity> findBestSellingProducts(int amount){

		List<Long> maxAmount = queryFactory
				.select(qProduct.count())
				.from(qTransaction)
				.join(qTransaction.products, qProduct)
				.groupBy(qProduct)
				.orderBy(qProduct.count().desc())
				.limit(amount)
				.fetch();
		
		return queryFactory
				.select(qProduct)
				.from(qTransaction)
				.join(qTransaction.products, qProduct)
				.groupBy(qProduct.id)
				.orderBy(qProduct.count().desc())
				.having(qProduct.count().in(maxAmount))
				.limit(amount)
				.fetch();
	}
	
	@Override
	public List<CustomerEntity> findCustomersWhoSpentMostMoneyInSpecifiedTime(int amount, Date from, Date to){

		List<Double> maxAmount = queryFactory
				.select(qProduct.unitPrice.sum())
				.from(qTransaction)
				.innerJoin(qTransaction.products, qProduct)
				.innerJoin(qTransaction.customer, qCustomer)
				.where(qTransaction.date.between(from, to))
				.groupBy(qCustomer)
				.orderBy(qProduct.unitPrice.sum().desc())
				.limit(amount)
				.fetch();
		
		return queryFactory
				.select(qCustomer)
				.from(qTransaction)
				.innerJoin(qTransaction.products, qProduct)
				.innerJoin(qTransaction.customer, qCustomer)
				.where(qTransaction.date.between(from, to))
				.groupBy(qCustomer)
				.orderBy(qProduct.unitPrice.sum().desc())
				.having(qProduct.unitPrice.sum().in(maxAmount))
				.limit(amount)
				.fetch();
	}
	
	@Override
	public double profitFromPeriodTime(Date from, Date to){

		return queryFactory
				.select(qProduct.unitPrice.multiply(qProduct.marginPercent).multiply(0.01).sum())
				.from(qTransaction)
				.innerJoin(qTransaction.products, qProduct)
				.where(qTransaction.date.between(from, to))
				.fetchFirst();
	}
}
