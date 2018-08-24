package com.capgemini.jstk.transactionregistration.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.dao.CustomerRepositoryCustom;
import com.capgemini.jstk.transactionregistration.domain.query.QCustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QProductEntity;
import com.capgemini.jstk.transactionregistration.domain.ProductEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QTransactionEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {
	
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
}
